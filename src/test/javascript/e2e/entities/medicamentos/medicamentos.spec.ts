import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import MedicamentosComponentsPage, { MedicamentosDeleteDialog } from './medicamentos.page-object';
import MedicamentosUpdatePage from './medicamentos-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('Medicamentos e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let medicamentosComponentsPage: MedicamentosComponentsPage;
  let medicamentosUpdatePage: MedicamentosUpdatePage;
  let medicamentosDeleteDialog: MedicamentosDeleteDialog;

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

  it('should load Medicamentos', async () => {
    await navBarPage.getEntityPage('medicamentos');
    medicamentosComponentsPage = new MedicamentosComponentsPage();
    expect(await medicamentosComponentsPage.getTitle().getText()).to.match(/Medicamentos/);
  });

  it('should load create Medicamentos page', async () => {
    await medicamentosComponentsPage.clickOnCreateButton();
    medicamentosUpdatePage = new MedicamentosUpdatePage();
    expect(await medicamentosUpdatePage.getPageTitle().getAttribute('id')).to.match(/safhApp.medicamentos.home.createOrEditLabel/);
    await medicamentosUpdatePage.cancel();
  });

  it('should create and save Medicamentos', async () => {
    async function createMedicamentos() {
      await medicamentosComponentsPage.clickOnCreateButton();
      await medicamentosUpdatePage.setDescricaoInput('descricao');
      expect(await medicamentosUpdatePage.getDescricaoInput()).to.match(/descricao/);
      await medicamentosUpdatePage.setRegistroMinisterioSaudeInput('registroMinisterioSaude');
      expect(await medicamentosUpdatePage.getRegistroMinisterioSaudeInput()).to.match(/registroMinisterioSaude/);
      await medicamentosUpdatePage.setCodigoBarrasInput('codigoBarras');
      expect(await medicamentosUpdatePage.getCodigoBarrasInput()).to.match(/codigoBarras/);
      await medicamentosUpdatePage.setQtdAtualInput('5');
      expect(await medicamentosUpdatePage.getQtdAtualInput()).to.eq('5');
      await medicamentosUpdatePage.setQtdMinInput('5');
      expect(await medicamentosUpdatePage.getQtdMinInput()).to.eq('5');
      await medicamentosUpdatePage.setQtdMaxInput('5');
      expect(await medicamentosUpdatePage.getQtdMaxInput()).to.eq('5');
      await medicamentosUpdatePage.setObservacoesInput('observacoes');
      expect(await medicamentosUpdatePage.getObservacoesInput()).to.match(/observacoes/);
      await medicamentosUpdatePage.setApresentacaoInput('apresentacao');
      expect(await medicamentosUpdatePage.getApresentacaoInput()).to.match(/apresentacao/);
      await medicamentosUpdatePage.posologiaPadraoSelectLastOption();
      await medicamentosUpdatePage.fabricantesSelectLastOption();
      await waitUntilDisplayed(medicamentosUpdatePage.getSaveButton());
      await medicamentosUpdatePage.save();
      await waitUntilHidden(medicamentosUpdatePage.getSaveButton());
      expect(await medicamentosUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createMedicamentos();
    await medicamentosComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await medicamentosComponentsPage.countDeleteButtons();
    await createMedicamentos();

    await medicamentosComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await medicamentosComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Medicamentos', async () => {
    await medicamentosComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await medicamentosComponentsPage.countDeleteButtons();
    await medicamentosComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    medicamentosDeleteDialog = new MedicamentosDeleteDialog();
    expect(await medicamentosDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/safhApp.medicamentos.delete.question/);
    await medicamentosDeleteDialog.clickOnConfirmButton();

    await medicamentosComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await medicamentosComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
