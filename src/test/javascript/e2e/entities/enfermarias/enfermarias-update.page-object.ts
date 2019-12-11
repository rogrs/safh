import { element, by, ElementFinder } from 'protractor';

export default class EnfermariasUpdatePage {
  pageTitle: ElementFinder = element(by.id('safhApp.enfermarias.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  enfermariaInput: ElementFinder = element(by.css('input#enfermarias-enfermaria'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setEnfermariaInput(enfermaria) {
    await this.enfermariaInput.sendKeys(enfermaria);
  }

  async getEnfermariaInput() {
    return this.enfermariaInput.getAttribute('value');
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
