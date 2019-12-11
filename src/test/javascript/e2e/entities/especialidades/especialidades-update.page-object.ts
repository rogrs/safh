import { element, by, ElementFinder } from 'protractor';

export default class EspecialidadesUpdatePage {
  pageTitle: ElementFinder = element(by.id('safhApp.especialidades.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  especialidadeInput: ElementFinder = element(by.css('input#especialidades-especialidade'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setEspecialidadeInput(especialidade) {
    await this.especialidadeInput.sendKeys(especialidade);
  }

  async getEspecialidadeInput() {
    return this.especialidadeInput.getAttribute('value');
  }

  async save() {
    await this.saveButton.click();
  }

  async cancel() {
    await this.cancelButton.click();
  }

  getSaveButton() {
    return this.saveButton;
  }
}
