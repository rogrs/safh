import { element, by, ElementFinder } from 'protractor';

export default class PosologiasUpdatePage {
  pageTitle: ElementFinder = element(by.id('safhApp.posologias.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  posologiaInput: ElementFinder = element(by.css('input#posologias-posologia'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setPosologiaInput(posologia) {
    await this.posologiaInput.sendKeys(posologia);
  }

  async getPosologiaInput() {
    return this.posologiaInput.getAttribute('value');
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
