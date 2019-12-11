import { element, by, ElementFinder } from 'protractor';

export default class DietasUpdatePage {
  pageTitle: ElementFinder = element(by.id('safhApp.dietas.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  dietaInput: ElementFinder = element(by.css('input#dietas-dieta'));
  descricaoInput: ElementFinder = element(by.css('input#dietas-descricao'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setDietaInput(dieta) {
    await this.dietaInput.sendKeys(dieta);
  }

  async getDietaInput() {
    return this.dietaInput.getAttribute('value');
  }

  async setDescricaoInput(descricao) {
    await this.descricaoInput.sendKeys(descricao);
  }

  async getDescricaoInput() {
    return this.descricaoInput.getAttribute('value');
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
