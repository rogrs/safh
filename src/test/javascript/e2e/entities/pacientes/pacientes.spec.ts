import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import PacientesComponentsPage, { PacientesDeleteDialog } from './pacientes.page-object';
import PacientesUpdatePage from './pacientes-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('Pacientes e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let pacientesComponentsPage: PacientesComponentsPage;
  let pacientesUpdatePage: PacientesUpdatePage;
  let pacientesDeleteDialog: PacientesDeleteDialog;

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

  it('should load Pacientes', async () => {
    await navBarPage.getEntityPage('pacientes');
    pacientesComponentsPage = new PacientesComponentsPage();
    expect(await pacientesComponentsPage.getTitle().getText()).to.match(/Pacientes/);
  });

  it('should load create Pacientes page', async () => {
    await pacientesComponentsPage.clickOnCreateButton();
    pacientesUpdatePage = new PacientesUpdatePage();
    expect(await pacientesUpdatePage.getPageTitle().getAttribute('id')).to.match(/safhApp.pacientes.home.createOrEditLabel/);
    await pacientesUpdatePage.cancel();
  });

  it('should create and save Pacientes', async () => {
    async function createPacientes() {
      await pacientesComponentsPage.clickOnCreateButton();
      await pacientesUpdatePage.setProntuarioInput('5');
      expect(await pacientesUpdatePage.getProntuarioInput()).to.eq('5');
      await pacientesUpdatePage.setNomeInput('nome');
      expect(await pacientesUpdatePage.getNomeInput()).to.match(/nome/);
      await pacientesUpdatePage.setCpfInput('cpf');
      expect(await pacientesUpdatePage.getCpfInput()).to.match(/cpf/);
      await pacientesUpdatePage.setEmailInput('email');
      expect(await pacientesUpdatePage.getEmailInput()).to.match(/email/);
      await pacientesUpdatePage.setCepInput('cep');
      expect(await pacientesUpdatePage.getCepInput()).to.match(/cep/);
      await pacientesUpdatePage.setLogradouroInput('logradouro');
      expect(await pacientesUpdatePage.getLogradouroInput()).to.match(/logradouro/);
      await pacientesUpdatePage.setNumeroInput('numero');
      expect(await pacientesUpdatePage.getNumeroInput()).to.match(/numero/);
      await pacientesUpdatePage.setComplementoInput('complemento');
      expect(await pacientesUpdatePage.getComplementoInput()).to.match(/complemento/);
      await pacientesUpdatePage.setBairroInput('bairro');
      expect(await pacientesUpdatePage.getBairroInput()).to.match(/bairro/);
      await pacientesUpdatePage.setCidadeInput('cidade');
      expect(await pacientesUpdatePage.getCidadeInput()).to.match(/cidade/);
      await pacientesUpdatePage.uFSelectLastOption();
      await pacientesUpdatePage.clinicasSelectLastOption();
      await pacientesUpdatePage.enfermariasSelectLastOption();
      await pacientesUpdatePage.leitosSelectLastOption();
      await waitUntilDisplayed(pacientesUpdatePage.getSaveButton());
      await pacientesUpdatePage.save();
      await waitUntilHidden(pacientesUpdatePage.getSaveButton());
      expect(await pacientesUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createPacientes();
    await pacientesComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await pacientesComponentsPage.countDeleteButtons();
    await createPacientes();

    await pacientesComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await pacientesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Pacientes', async () => {
    await pacientesComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await pacientesComponentsPage.countDeleteButtons();
    await pacientesComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    pacientesDeleteDialog = new PacientesDeleteDialog();
    expect(await pacientesDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/safhApp.pacientes.delete.question/);
    await pacientesDeleteDialog.clickOnConfirmButton();

    await pacientesComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await pacientesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
