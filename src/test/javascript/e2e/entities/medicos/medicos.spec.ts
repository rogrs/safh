import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import MedicosComponentsPage, { MedicosDeleteDialog } from './medicos.page-object';
import MedicosUpdatePage from './medicos-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('Medicos e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let medicosComponentsPage: MedicosComponentsPage;
  let medicosUpdatePage: MedicosUpdatePage;
  let medicosDeleteDialog: MedicosDeleteDialog;

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

  it('should load Medicos', async () => {
    await navBarPage.getEntityPage('medicos');
    medicosComponentsPage = new MedicosComponentsPage();
    expect(await medicosComponentsPage.getTitle().getText()).to.match(/Medicos/);
  });

  it('should load create Medicos page', async () => {
    await medicosComponentsPage.clickOnCreateButton();
    medicosUpdatePage = new MedicosUpdatePage();
    expect(await medicosUpdatePage.getPageTitle().getAttribute('id')).to.match(/safhApp.medicos.home.createOrEditLabel/);
    await medicosUpdatePage.cancel();
  });

  it('should create and save Medicos', async () => {
    async function createMedicos() {
      await medicosComponentsPage.clickOnCreateButton();
      await medicosUpdatePage.setNomeInput('nome');
      expect(await medicosUpdatePage.getNomeInput()).to.match(/nome/);
      await medicosUpdatePage.setCrmInput('crm');
      expect(await medicosUpdatePage.getCrmInput()).to.match(/crm/);
      await medicosUpdatePage.setCpfInput('cpf');
      expect(await medicosUpdatePage.getCpfInput()).to.match(/cpf/);
      await medicosUpdatePage.setEmailInput('email');
      expect(await medicosUpdatePage.getEmailInput()).to.match(/email/);
      await medicosUpdatePage.setCepInput('cep');
      expect(await medicosUpdatePage.getCepInput()).to.match(/cep/);
      await medicosUpdatePage.setLogradouroInput('logradouro');
      expect(await medicosUpdatePage.getLogradouroInput()).to.match(/logradouro/);
      await medicosUpdatePage.setNumeroInput('numero');
      expect(await medicosUpdatePage.getNumeroInput()).to.match(/numero/);
      await medicosUpdatePage.setComplementoInput('complemento');
      expect(await medicosUpdatePage.getComplementoInput()).to.match(/complemento/);
      await medicosUpdatePage.setBairroInput('bairro');
      expect(await medicosUpdatePage.getBairroInput()).to.match(/bairro/);
      await medicosUpdatePage.setCidadeInput('cidade');
      expect(await medicosUpdatePage.getCidadeInput()).to.match(/cidade/);
      await medicosUpdatePage.uFSelectLastOption();
      await medicosUpdatePage.especialidadesSelectLastOption();
      await waitUntilDisplayed(medicosUpdatePage.getSaveButton());
      await medicosUpdatePage.save();
      await waitUntilHidden(medicosUpdatePage.getSaveButton());
      expect(await medicosUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createMedicos();
    await medicosComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await medicosComponentsPage.countDeleteButtons();
    await createMedicos();

    await medicosComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await medicosComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Medicos', async () => {
    await medicosComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await medicosComponentsPage.countDeleteButtons();
    await medicosComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    medicosDeleteDialog = new MedicosDeleteDialog();
    expect(await medicosDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/safhApp.medicos.delete.question/);
    await medicosDeleteDialog.clickOnConfirmButton();

    await medicosComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await medicosComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
