import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import PrescricoesComponentsPage, { PrescricoesDeleteDialog } from './prescricoes.page-object';
import PrescricoesUpdatePage from './prescricoes-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('Prescricoes e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let prescricoesComponentsPage: PrescricoesComponentsPage;
  let prescricoesUpdatePage: PrescricoesUpdatePage;
  let prescricoesDeleteDialog: PrescricoesDeleteDialog;

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

  it('should load Prescricoes', async () => {
    await navBarPage.getEntityPage('prescricoes');
    prescricoesComponentsPage = new PrescricoesComponentsPage();
    expect(await prescricoesComponentsPage.getTitle().getText()).to.match(/Prescricoes/);
  });

  it('should load create Prescricoes page', async () => {
    await prescricoesComponentsPage.clickOnCreateButton();
    prescricoesUpdatePage = new PrescricoesUpdatePage();
    expect(await prescricoesUpdatePage.getPageTitle().getAttribute('id')).to.match(/safhApp.prescricoes.home.createOrEditLabel/);
    await prescricoesUpdatePage.cancel();
  });

  it('should create and save Prescricoes', async () => {
    async function createPrescricoes() {
      await prescricoesComponentsPage.clickOnCreateButton();
      await prescricoesUpdatePage.setPrescricaoInput('prescricao');
      expect(await prescricoesUpdatePage.getPrescricaoInput()).to.match(/prescricao/);
      await waitUntilDisplayed(prescricoesUpdatePage.getSaveButton());
      await prescricoesUpdatePage.save();
      await waitUntilHidden(prescricoesUpdatePage.getSaveButton());
      expect(await prescricoesUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createPrescricoes();
    await prescricoesComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await prescricoesComponentsPage.countDeleteButtons();
    await createPrescricoes();

    await prescricoesComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await prescricoesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Prescricoes', async () => {
    await prescricoesComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await prescricoesComponentsPage.countDeleteButtons();
    await prescricoesComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    prescricoesDeleteDialog = new PrescricoesDeleteDialog();
    expect(await prescricoesDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/safhApp.prescricoes.delete.question/);
    await prescricoesDeleteDialog.clickOnConfirmButton();

    await prescricoesComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await prescricoesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
