import { element, by, ElementFinder } from 'protractor';

export default class PacientesUpdatePage {
  pageTitle: ElementFinder = element(by.id('safhApp.pacientes.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  prontuarioInput: ElementFinder = element(by.css('input#pacientes-prontuario'));
  nomeInput: ElementFinder = element(by.css('input#pacientes-nome'));
  cpfInput: ElementFinder = element(by.css('input#pacientes-cpf'));
  emailInput: ElementFinder = element(by.css('input#pacientes-email'));
  cepInput: ElementFinder = element(by.css('input#pacientes-cep'));
  logradouroInput: ElementFinder = element(by.css('input#pacientes-logradouro'));
  numeroInput: ElementFinder = element(by.css('input#pacientes-numero'));
  complementoInput: ElementFinder = element(by.css('input#pacientes-complemento'));
  bairroInput: ElementFinder = element(by.css('input#pacientes-bairro'));
  cidadeInput: ElementFinder = element(by.css('input#pacientes-cidade'));
  uFSelect: ElementFinder = element(by.css('select#pacientes-uF'));
  clinicasSelect: ElementFinder = element(by.css('select#pacientes-clinicas'));
  enfermariasSelect: ElementFinder = element(by.css('select#pacientes-enfermarias'));
  leitosSelect: ElementFinder = element(by.css('select#pacientes-leitos'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setProntuarioInput(prontuario) {
    await this.prontuarioInput.sendKeys(prontuario);
  }

  async getProntuarioInput() {
    return this.prontuarioInput.getAttribute('value');
  }

  async setNomeInput(nome) {
    await this.nomeInput.sendKeys(nome);
  }

  async getNomeInput() {
    return this.nomeInput.getAttribute('value');
  }

  async setCpfInput(cpf) {
    await this.cpfInput.sendKeys(cpf);
  }

  async getCpfInput() {
    return this.cpfInput.getAttribute('value');
  }

  async setEmailInput(email) {
    await this.emailInput.sendKeys(email);
  }

  async getEmailInput() {
    return this.emailInput.getAttribute('value');
  }

  async setCepInput(cep) {
    await this.cepInput.sendKeys(cep);
  }

  async getCepInput() {
    return this.cepInput.getAttribute('value');
  }

  async setLogradouroInput(logradouro) {
    await this.logradouroInput.sendKeys(logradouro);
  }

  async getLogradouroInput() {
    return this.logradouroInput.getAttribute('value');
  }

  async setNumeroInput(numero) {
    await this.numeroInput.sendKeys(numero);
  }

  async getNumeroInput() {
    return this.numeroInput.getAttribute('value');
  }

  async setComplementoInput(complemento) {
    await this.complementoInput.sendKeys(complemento);
  }

  async getComplementoInput() {
    return this.complementoInput.getAttribute('value');
  }

  async setBairroInput(bairro) {
    await this.bairroInput.sendKeys(bairro);
  }

  async getBairroInput() {
    return this.bairroInput.getAttribute('value');
  }

  async setCidadeInput(cidade) {
    await this.cidadeInput.sendKeys(cidade);
  }

  async getCidadeInput() {
    return this.cidadeInput.getAttribute('value');
  }

  async setUFSelect(uF) {
    await this.uFSelect.sendKeys(uF);
  }

  async getUFSelect() {
    return this.uFSelect.element(by.css('option:checked')).getText();
  }

  async uFSelectLastOption() {
    await this.uFSelect
      .all(by.tagName('option'))
      .last()
      .click();
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

  async enfermariasSelectLastOption() {
    await this.enfermariasSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async enfermariasSelectOption(option) {
    await this.enfermariasSelect.sendKeys(option);
  }

  getEnfermariasSelect() {
    return this.enfermariasSelect;
  }

  async getEnfermariasSelectedOption() {
    return this.enfermariasSelect.element(by.css('option:checked')).getText();
  }

  async leitosSelectLastOption() {
    await this.leitosSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async leitosSelectOption(option) {
    await this.leitosSelect.sendKeys(option);
  }

  getLeitosSelect() {
    return this.leitosSelect;
  }

  async getLeitosSelectedOption() {
    return this.leitosSelect.element(by.css('option:checked')).getText();
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
