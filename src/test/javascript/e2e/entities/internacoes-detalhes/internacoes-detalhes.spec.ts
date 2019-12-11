import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import InternacoesDetalhesComponentsPage, { InternacoesDetalhesDeleteDialog } from './internacoes-detalhes.page-object';
import InternacoesDetalhesUpdatePage from './internacoes-detalhes-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('InternacoesDetalhes e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let internacoesDetalhesComponentsPage: InternacoesDetalhesComponentsPage;
  let internacoesDetalhesUpdatePage: InternacoesDetalhesUpdatePage;
  let internacoesDetalhesDeleteDialog: InternacoesDetalhesDeleteDialog;

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

  it('should load InternacoesDetalhes', async () => {
    await navBarPage.getEntityPage('internacoes-detalhes');
    internacoesDetalhesComponentsPage = new InternacoesDetalhesComponentsPage();
    expect(await internacoesDetalhesComponentsPage.getTitle().getText()).to.match(/Internacoes Detalhes/);
  });

  it('should load create InternacoesDetalhes page', async () => {
    await internacoesDetalhesComponentsPage.clickOnCreateButton();
    internacoesDetalhesUpdatePage = new InternacoesDetalhesUpdatePage();
    expect(await internacoesDetalhesUpdatePage.getPageTitle().getAttribute('id')).to.match(
      /safhApp.internacoesDetalhes.home.createOrEditLabel/
    );
    await internacoesDetalhesUpdatePage.cancel();
  });

  it('should create and save InternacoesDetalhes', async () => {
    async function createInternacoesDetalhes() {
      await internacoesDetalhesComponentsPage.clickOnCreateButton();
      await internacoesDetalhesUpdatePage.setDataDetalheInput('01-01-2001');
      expect(await internacoesDetalhesUpdatePage.getDataDetalheInput()).to.eq('2001-01-01');
      await internacoesDetalhesUpdatePage.setHorarioInput('01-01-2001');
      expect(await internacoesDetalhesUpdatePage.getHorarioInput()).to.eq('2001-01-01');
      await internacoesDetalhesUpdatePage.setQtdInput('5');
      expect(await internacoesDetalhesUpdatePage.getQtdInput()).to.eq('5');
      await internacoesDetalhesUpdatePage.internacoesSelectLastOption();
      await internacoesDetalhesUpdatePage.dietasSelectLastOption();
      await internacoesDetalhesUpdatePage.prescricoesSelectLastOption();
      await internacoesDetalhesUpdatePage.posologiasSelectLastOption();
      await waitUntilDisplayed(internacoesDetalhesUpdatePage.getSaveButton());
      await internacoesDetalhesUpdatePage.save();
      await waitUntilHidden(internacoesDetalhesUpdatePage.getSaveButton());
      expect(await internacoesDetalhesUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createInternacoesDetalhes();
    await internacoesDetalhesComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await internacoesDetalhesComponentsPage.countDeleteButtons();
    await createInternacoesDetalhes();

    await internacoesDetalhesComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await internacoesDetalhesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last InternacoesDetalhes', async () => {
    await internacoesDetalhesComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await internacoesDetalhesComponentsPage.countDeleteButtons();
    await internacoesDetalhesComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    internacoesDetalhesDeleteDialog = new InternacoesDetalhesDeleteDialog();
    expect(await internacoesDetalhesDeleteDialog.getDialogTitle().getAttribute('id')).to.match(
      /safhApp.internacoesDetalhes.delete.question/
    );
    await internacoesDetalhesDeleteDialog.clickOnConfirmButton();

    await internacoesDetalhesComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await internacoesDetalhesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
