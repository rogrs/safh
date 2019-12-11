import { element, by, ElementFinder } from 'protractor';

export default class PrescricoesUpdatePage {
  pageTitle: ElementFinder = element(by.id('safhApp.prescricoes.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  prescricaoInput: ElementFinder = element(by.css('input#prescricoes-prescricao'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setPrescricaoInput(prescricao) {
    await this.prescricaoInput.sendKeys(prescricao);
  }

  async getPrescricaoInput() {
    return this.prescricaoInput.getAttribute('value');
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
