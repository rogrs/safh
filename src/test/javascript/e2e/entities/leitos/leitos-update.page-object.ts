import { element, by, ElementFinder } from 'protractor';

export default class LeitosUpdatePage {
  pageTitle: ElementFinder = element(by.id('safhApp.leitos.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  leitoInput: ElementFinder = element(by.css('input#leitos-leito'));
  tipoInput: ElementFinder = element(by.css('input#leitos-tipo'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setLeitoInput(leito) {
    await this.leitoInput.sendKeys(leito);
  }

  async getLeitoInput() {
    return this.leitoInput.getAttribute('value');
  }

  async setTipoInput(tipo) {
    await this.tipoInput.sendKeys(tipo);
  }

  async getTipoInput() {
    return this.tipoInput.getAttribute('value');
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
