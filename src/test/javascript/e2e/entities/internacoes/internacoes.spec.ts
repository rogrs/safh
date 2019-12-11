import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import InternacoesComponentsPage, { InternacoesDeleteDialog } from './internacoes.page-object';
import InternacoesUpdatePage from './internacoes-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('Internacoes e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let internacoesComponentsPage: InternacoesComponentsPage;
  let internacoesUpdatePage: InternacoesUpdatePage;
  let internacoesDeleteDialog: InternacoesDeleteDialog;

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

  it('should load Internacoes', async () => {
    await navBarPage.getEntityPage('internacoes');
    internacoesComponentsPage = new InternacoesComponentsPage();
    expect(await internacoesComponentsPage.getTitle().getText()).to.match(/Internacoes/);
  });

  it('should load create Internacoes page', async () => {
    await internacoesComponentsPage.clickOnCreateButton();
    internacoesUpdatePage = new InternacoesUpdatePage();
    expect(await internacoesUpdatePage.getPageTitle().getAttribute('id')).to.match(/safhApp.internacoes.home.createOrEditLabel/);
    await internacoesUpdatePage.cancel();
  });

  it('should create and save Internacoes', async () => {
    async function createInternacoes() {
      await internacoesComponentsPage.clickOnCreateButton();
      await internacoesUpdatePage.setDataInternacaoInput('01-01-2001');
      expect(await internacoesUpdatePage.getDataInternacaoInput()).to.eq('2001-01-01');
      await internacoesUpdatePage.setDescricaoInput('descricao');
      expect(await internacoesUpdatePage.getDescricaoInput()).to.match(/descricao/);
      await internacoesUpdatePage.pacientesSelectLastOption();
      await internacoesUpdatePage.clinicasSelectLastOption();
      await internacoesUpdatePage.medicosSelectLastOption();
      await waitUntilDisplayed(internacoesUpdatePage.getSaveButton());
      await internacoesUpdatePage.save();
      await waitUntilHidden(internacoesUpdatePage.getSaveButton());
      expect(await internacoesUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createInternacoes();
    await internacoesComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await internacoesComponentsPage.countDeleteButtons();
    await createInternacoes();

    await internacoesComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await internacoesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Internacoes', async () => {
    await internacoesComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await internacoesComponentsPage.countDeleteButtons();
    await internacoesComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    internacoesDeleteDialog = new InternacoesDeleteDialog();
    expect(await internacoesDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/safhApp.internacoes.delete.question/);
    await internacoesDeleteDialog.clickOnConfirmButton();

    await internacoesComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await internacoesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
