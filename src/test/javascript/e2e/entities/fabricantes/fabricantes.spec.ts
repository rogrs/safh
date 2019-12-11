import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import FabricantesComponentsPage, { FabricantesDeleteDialog } from './fabricantes.page-object';
import FabricantesUpdatePage from './fabricantes-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('Fabricantes e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let fabricantesComponentsPage: FabricantesComponentsPage;
  let fabricantesUpdatePage: FabricantesUpdatePage;
  let fabricantesDeleteDialog: FabricantesDeleteDialog;

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

  it('should load Fabricantes', async () => {
    await navBarPage.getEntityPage('fabricantes');
    fabricantesComponentsPage = new FabricantesComponentsPage();
    expect(await fabricantesComponentsPage.getTitle().getText()).to.match(/Fabricantes/);
  });

  it('should load create Fabricantes page', async () => {
    await fabricantesComponentsPage.clickOnCreateButton();
    fabricantesUpdatePage = new FabricantesUpdatePage();
    expect(await fabricantesUpdatePage.getPageTitle().getAttribute('id')).to.match(/safhApp.fabricantes.home.createOrEditLabel/);
    await fabricantesUpdatePage.cancel();
  });

  it('should create and save Fabricantes', async () => {
    async function createFabricantes() {
      await fabricantesComponentsPage.clickOnCreateButton();
      await fabricantesUpdatePage.setFabricanteInput('fabricante');
      expect(await fabricantesUpdatePage.getFabricanteInput()).to.match(/fabricante/);
      await waitUntilDisplayed(fabricantesUpdatePage.getSaveButton());
      await fabricantesUpdatePage.save();
      await waitUntilHidden(fabricantesUpdatePage.getSaveButton());
      expect(await fabricantesUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createFabricantes();
    await fabricantesComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await fabricantesComponentsPage.countDeleteButtons();
    await createFabricantes();

    await fabricantesComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await fabricantesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Fabricantes', async () => {
    await fabricantesComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await fabricantesComponentsPage.countDeleteButtons();
    await fabricantesComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    fabricantesDeleteDialog = new FabricantesDeleteDialog();
    expect(await fabricantesDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/safhApp.fabricantes.delete.question/);
    await fabricantesDeleteDialog.clickOnConfirmButton();

    await fabricantesComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await fabricantesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
