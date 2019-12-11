import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import ClinicasComponentsPage, { ClinicasDeleteDialog } from './clinicas.page-object';
import ClinicasUpdatePage from './clinicas-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('Clinicas e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let clinicasComponentsPage: ClinicasComponentsPage;
  let clinicasUpdatePage: ClinicasUpdatePage;
  let clinicasDeleteDialog: ClinicasDeleteDialog;

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

  it('should load Clinicas', async () => {
    await navBarPage.getEntityPage('clinicas');
    clinicasComponentsPage = new ClinicasComponentsPage();
    expect(await clinicasComponentsPage.getTitle().getText()).to.match(/Clinicas/);
  });

  it('should load create Clinicas page', async () => {
    await clinicasComponentsPage.clickOnCreateButton();
    clinicasUpdatePage = new ClinicasUpdatePage();
    expect(await clinicasUpdatePage.getPageTitle().getAttribute('id')).to.match(/safhApp.clinicas.home.createOrEditLabel/);
    await clinicasUpdatePage.cancel();
  });

  it('should create and save Clinicas', async () => {
    async function createClinicas() {
      await clinicasComponentsPage.clickOnCreateButton();
      await clinicasUpdatePage.setClinicaInput('clinica');
      expect(await clinicasUpdatePage.getClinicaInput()).to.match(/clinica/);
      await clinicasUpdatePage.setDescricaoInput('descricao');
      expect(await clinicasUpdatePage.getDescricaoInput()).to.match(/descricao/);
      await waitUntilDisplayed(clinicasUpdatePage.getSaveButton());
      await clinicasUpdatePage.save();
      await waitUntilHidden(clinicasUpdatePage.getSaveButton());
      expect(await clinicasUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createClinicas();
    await clinicasComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await clinicasComponentsPage.countDeleteButtons();
    await createClinicas();

    await clinicasComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await clinicasComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Clinicas', async () => {
    await clinicasComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await clinicasComponentsPage.countDeleteButtons();
    await clinicasComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    clinicasDeleteDialog = new ClinicasDeleteDialog();
    expect(await clinicasDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/safhApp.clinicas.delete.question/);
    await clinicasDeleteDialog.clickOnConfirmButton();

    await clinicasComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await clinicasComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
