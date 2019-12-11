import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import LeitosComponentsPage, { LeitosDeleteDialog } from './leitos.page-object';
import LeitosUpdatePage from './leitos-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('Leitos e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let leitosComponentsPage: LeitosComponentsPage;
  let leitosUpdatePage: LeitosUpdatePage;
  let leitosDeleteDialog: LeitosDeleteDialog;

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

  it('should load Leitos', async () => {
    await navBarPage.getEntityPage('leitos');
    leitosComponentsPage = new LeitosComponentsPage();
    expect(await leitosComponentsPage.getTitle().getText()).to.match(/Leitos/);
  });

  it('should load create Leitos page', async () => {
    await leitosComponentsPage.clickOnCreateButton();
    leitosUpdatePage = new LeitosUpdatePage();
    expect(await leitosUpdatePage.getPageTitle().getAttribute('id')).to.match(/safhApp.leitos.home.createOrEditLabel/);
    await leitosUpdatePage.cancel();
  });

  it('should create and save Leitos', async () => {
    async function createLeitos() {
      await leitosComponentsPage.clickOnCreateButton();
      await leitosUpdatePage.setLeitoInput('leito');
      expect(await leitosUpdatePage.getLeitoInput()).to.match(/leito/);
      await leitosUpdatePage.setTipoInput('tipo');
      expect(await leitosUpdatePage.getTipoInput()).to.match(/tipo/);
      await waitUntilDisplayed(leitosUpdatePage.getSaveButton());
      await leitosUpdatePage.save();
      await waitUntilHidden(leitosUpdatePage.getSaveButton());
      expect(await leitosUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createLeitos();
    await leitosComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await leitosComponentsPage.countDeleteButtons();
    await createLeitos();

    await leitosComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await leitosComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Leitos', async () => {
    await leitosComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await leitosComponentsPage.countDeleteButtons();
    await leitosComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    leitosDeleteDialog = new LeitosDeleteDialog();
    expect(await leitosDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/safhApp.leitos.delete.question/);
    await leitosDeleteDialog.clickOnConfirmButton();

    await leitosComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await leitosComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
