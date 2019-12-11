import { element, by, ElementFinder } from 'protractor';

export default class ClinicasUpdatePage {
  pageTitle: ElementFinder = element(by.id('safhApp.clinicas.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  clinicaInput: ElementFinder = element(by.css('input#clinicas-clinica'));
  descricaoInput: ElementFinder = element(by.css('input#clinicas-descricao'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setClinicaInput(clinica) {
    await this.clinicaInput.sendKeys(clinica);
  }

  async getClinicaInput() {
    return this.clinicaInput.getAttribute('value');
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
