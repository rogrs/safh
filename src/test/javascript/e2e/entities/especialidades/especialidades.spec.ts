import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import EspecialidadesComponentsPage, { EspecialidadesDeleteDialog } from './especialidades.page-object';
import EspecialidadesUpdatePage from './especialidades-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('Especialidades e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let especialidadesComponentsPage: EspecialidadesComponentsPage;
  let especialidadesUpdatePage: EspecialidadesUpdatePage;
  let especialidadesDeleteDialog: EspecialidadesDeleteDialog;

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

  it('should load Especialidades', async () => {
    await navBarPage.getEntityPage('especialidades');
    especialidadesComponentsPage = new EspecialidadesComponentsPage();
    expect(await especialidadesComponentsPage.getTitle().getText()).to.match(/Especialidades/);
  });

  it('should load create Especialidades page', async () => {
    await especialidadesComponentsPage.clickOnCreateButton();
    especialidadesUpdatePage = new EspecialidadesUpdatePage();
    expect(await especialidadesUpdatePage.getPageTitle().getAttribute('id')).to.match(/safhApp.especialidades.home.createOrEditLabel/);
    await especialidadesUpdatePage.cancel();
  });

  it('should create and save Especialidades', async () => {
    async function createEspecialidades() {
      await especialidadesComponentsPage.clickOnCreateButton();
      await especialidadesUpdatePage.setEspecialidadeInput('especialidade');
      expect(await especialidadesUpdatePage.getEspecialidadeInput()).to.match(/especialidade/);
      await waitUntilDisplayed(especialidadesUpdatePage.getSaveButton());
      await especialidadesUpdatePage.save();
      await waitUntilHidden(especialidadesUpdatePage.getSaveButton());
      expect(await especialidadesUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createEspecialidades();
    await especialidadesComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await especialidadesComponentsPage.countDeleteButtons();
    await createEspecialidades();

    await especialidadesComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await especialidadesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Especialidades', async () => {
    await especialidadesComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await especialidadesComponentsPage.countDeleteButtons();
    await especialidadesComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    especialidadesDeleteDialog = new EspecialidadesDeleteDialog();
    expect(await especialidadesDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/safhApp.especialidades.delete.question/);
    await especialidadesDeleteDialog.clickOnConfirmButton();

    await especialidadesComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await especialidadesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
