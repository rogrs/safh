import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import EnfermariasComponentsPage, { EnfermariasDeleteDialog } from './enfermarias.page-object';
import EnfermariasUpdatePage from './enfermarias-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('Enfermarias e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let enfermariasComponentsPage: EnfermariasComponentsPage;
  let enfermariasUpdatePage: EnfermariasUpdatePage;
  let enfermariasDeleteDialog: EnfermariasDeleteDialog;

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

  it('should load Enfermarias', async () => {
    await navBarPage.getEntityPage('enfermarias');
    enfermariasComponentsPage = new EnfermariasComponentsPage();
    expect(await enfermariasComponentsPage.getTitle().getText()).to.match(/Enfermarias/);
  });

  it('should load create Enfermarias page', async () => {
    await enfermariasComponentsPage.clickOnCreateButton();
    enfermariasUpdatePage = new EnfermariasUpdatePage();
    expect(await enfermariasUpdatePage.getPageTitle().getAttribute('id')).to.match(/safhApp.enfermarias.home.createOrEditLabel/);
    await enfermariasUpdatePage.cancel();
  });

  it('should create and save Enfermarias', async () => {
    async function createEnfermarias() {
      await enfermariasComponentsPage.clickOnCreateButton();
      await enfermariasUpdatePage.setEnfermariaInput('enfermaria');
      expect(await enfermariasUpdatePage.getEnfermariaInput()).to.match(/enfermaria/);
      await waitUntilDisplayed(enfermariasUpdatePage.getSaveButton());
      await enfermariasUpdatePage.save();
      await waitUntilHidden(enfermariasUpdatePage.getSaveButton());
      expect(await enfermariasUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createEnfermarias();
    await enfermariasComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await enfermariasComponentsPage.countDeleteButtons();
    await createEnfermarias();

    await enfermariasComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await enfermariasComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Enfermarias', async () => {
    await enfermariasComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await enfermariasComponentsPage.countDeleteButtons();
    await enfermariasComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    enfermariasDeleteDialog = new EnfermariasDeleteDialog();
    expect(await enfermariasDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/safhApp.enfermarias.delete.question/);
    await enfermariasDeleteDialog.clickOnConfirmButton();

    await enfermariasComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await enfermariasComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
