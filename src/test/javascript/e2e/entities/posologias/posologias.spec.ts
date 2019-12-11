import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import PosologiasComponentsPage, { PosologiasDeleteDialog } from './posologias.page-object';
import PosologiasUpdatePage from './posologias-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('Posologias e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let posologiasComponentsPage: PosologiasComponentsPage;
  let posologiasUpdatePage: PosologiasUpdatePage;
  let posologiasDeleteDialog: PosologiasDeleteDialog;

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

  it('should load Posologias', async () => {
    await navBarPage.getEntityPage('posologias');
    posologiasComponentsPage = new PosologiasComponentsPage();
    expect(await posologiasComponentsPage.getTitle().getText()).to.match(/Posologias/);
  });

  it('should load create Posologias page', async () => {
    await posologiasComponentsPage.clickOnCreateButton();
    posologiasUpdatePage = new PosologiasUpdatePage();
    expect(await posologiasUpdatePage.getPageTitle().getAttribute('id')).to.match(/safhApp.posologias.home.createOrEditLabel/);
    await posologiasUpdatePage.cancel();
  });

  it('should create and save Posologias', async () => {
    async function createPosologias() {
      await posologiasComponentsPage.clickOnCreateButton();
      await posologiasUpdatePage.setPosologiaInput('posologia');
      expect(await posologiasUpdatePage.getPosologiaInput()).to.match(/posologia/);
      await waitUntilDisplayed(posologiasUpdatePage.getSaveButton());
      await posologiasUpdatePage.save();
      await waitUntilHidden(posologiasUpdatePage.getSaveButton());
      expect(await posologiasUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createPosologias();
    await posologiasComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await posologiasComponentsPage.countDeleteButtons();
    await createPosologias();

    await posologiasComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await posologiasComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Posologias', async () => {
    await posologiasComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await posologiasComponentsPage.countDeleteButtons();
    await posologiasComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    posologiasDeleteDialog = new PosologiasDeleteDialog();
    expect(await posologiasDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/safhApp.posologias.delete.question/);
    await posologiasDeleteDialog.clickOnConfirmButton();

    await posologiasComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await posologiasComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
