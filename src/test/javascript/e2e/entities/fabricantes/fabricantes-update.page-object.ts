import { element, by, ElementFinder } from 'protractor';

export default class FabricantesUpdatePage {
  pageTitle: ElementFinder = element(by.id('safhApp.fabricantes.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  fabricanteInput: ElementFinder = element(by.css('input#fabricantes-fabricante'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setFabricanteInput(fabricante) {
    await this.fabricanteInput.sendKeys(fabricante);
  }

  async getFabricanteInput() {
    return this.fabricanteInput.getAttribute('value');
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
