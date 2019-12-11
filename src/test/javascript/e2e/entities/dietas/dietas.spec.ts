import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import DietasComponentsPage, { DietasDeleteDialog } from './dietas.page-object';
import DietasUpdatePage from './dietas-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('Dietas e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let dietasComponentsPage: DietasComponentsPage;
  let dietasUpdatePage: DietasUpdatePage;
  let dietasDeleteDialog: DietasDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.waitUntilDisplayed();

    await signInPage.username.sendKeys('admin');
    await signInPage.password.sendKeys('admin');
    await signInPage.loginButton.click();
    await signInPage.waitUntilHidden();
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load Dietas', async () => {
    await navBarPage.getEntityPage('dietas');
    dietasComponentsPage = new DietasComponentsPage();
    expect(await dietasComponentsPage.getTitle().getText()).to.match(/Dietas/);
  });

  it('should load create Dietas page', async () => {
    await dietasComponentsPage.clickOnCreateButton();
    dietasUpdatePage = new DietasUpdatePage();
    expect(await dietasUpdatePage.getPageTitle().getAttribute('id')).to.match(/safhApp.dietas.home.createOrEditLabel/);
    await dietasUpdatePage.cancel();
  });

  it('should create and save Dietas', async () => {
    async function createDietas() {
      await dietasComponentsPage.clickOnCreateButton();
      await dietasUpdatePage.setDietaInput('dieta');
      expect(await dietasUpdatePage.getDietaInput()).to.match(/dieta/);
      await dietasUpdatePage.setDescricaoInput('descricao');
      expect(await dietasUpdatePage.getDescricaoInput()).to.match(/descricao/);
      await waitUntilDisplayed(dietasUpdatePage.getSaveButton());
      await dietasUpdatePage.save();
      await waitUntilHidden(dietasUpdatePage.getSaveButton());
      expect(await dietasUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createDietas();
    await dietasComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await dietasComponentsPage.countDeleteButtons();
    await createDietas();

    await dietasComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await dietasComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Dietas', async () => {
    await dietasComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await dietasComponentsPage.countDeleteButtons();
    await dietasComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    dietasDeleteDialog = new DietasDeleteDialog();
    expect(await dietasDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/safhApp.dietas.delete.question/);
    await dietasDeleteDialog.clickOnConfirmButton();

    await dietasComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await dietasComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
