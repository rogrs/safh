import { element, by, ElementFinder } from 'protractor';

export default class InternacoesUpdatePage {
  pageTitle: ElementFinder = element(by.id('safhApp.internacoes.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  dataInternacaoInput: ElementFinder = element(by.css('input#internacoes-dataInternacao'));
  descricaoInput: ElementFinder = element(by.css('input#internacoes-descricao'));
  pacientesSelect: ElementFinder = element(by.css('select#internacoes-pacientes'));
  clinicasSelect: ElementFinder = element(by.css('select#internacoes-clinicas'));
  medicosSelect: ElementFinder = element(by.css('select#internacoes-medicos'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setDataInternacaoInput(dataInternacao) {
    await this.dataInternacaoInput.sendKeys(dataInternacao);
  }

  async getDataInternacaoInput() {
    return this.dataInternacaoInput.getAttribute('value');
  }

  async setDescricaoInput(descricao) {
    await this.descricaoInput.sendKeys(descricao);
  }

  async getDescricaoInput() {
    return this.descricaoInput.getAttribute('value');
  }

  async pacientesSelectLastOption() {
    await this.pacientesSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async pacientesSelectOption(option) {
    await this.pacientesSelect.sendKeys(option);
  }

  getPacientesSelect() {
    return this.pacientesSelect;
  }

  async getPacientesSelectedOption() {
    return this.pacientesSelect.element(by.css('option:checked')).getText();
  }

  async clinicasSelectLastOption() {
    await this.clinicasSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async clinicasSelectOption(option) {
    await this.clinicasSelect.sendKeys(option);
  }

  getClinicasSelect() {
    return this.clinicasSelect;
  }

  async getClinicasSelectedOption() {
    return this.clinicasSelect.element(by.css('option:checked')).getText();
  }

  async medicosSelectLastOption() {
    await this.medicosSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async medicosSelectOption(option) {
    await this.medicosSelect.sendKeys(option);
  }

  getMedicosSelect() {
    return this.medicosSelect;
  }

  async getMedicosSelectedOption() {
    return this.medicosSelect.element(by.css('option:checked')).getText();
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
