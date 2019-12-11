import { element, by, ElementFinder } from 'protractor';

export default class MedicosUpdatePage {
  pageTitle: ElementFinder = element(by.id('safhApp.medicos.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  nomeInput: ElementFinder = element(by.css('input#medicos-nome'));
  crmInput: ElementFinder = element(by.css('input#medicos-crm'));
  cpfInput: ElementFinder = element(by.css('input#medicos-cpf'));
  emailInput: ElementFinder = element(by.css('input#medicos-email'));
  cepInput: ElementFinder = element(by.css('input#medicos-cep'));
  logradouroInput: ElementFinder = element(by.css('input#medicos-logradouro'));
  numeroInput: ElementFinder = element(by.css('input#medicos-numero'));
  complementoInput: ElementFinder = element(by.css('input#medicos-complemento'));
  bairroInput: ElementFinder = element(by.css('input#medicos-bairro'));
  cidadeInput: ElementFinder = element(by.css('input#medicos-cidade'));
  uFSelect: ElementFinder = element(by.css('select#medicos-uF'));
  especialidadesSelect: ElementFinder = element(by.css('select#medicos-especialidades'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setNomeInput(nome) {
    await this.nomeInput.sendKeys(nome);
  }

  async getNomeInput() {
    return this.nomeInput.getAttribute('value');
  }

  async setCrmInput(crm) {
    await this.crmInput.sendKeys(crm);
  }

  async getCrmInput() {
    return this.crmInput.getAttribute('value');
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
  async especialidadesSelectLastOption() {
    await this.especialidadesSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async especialidadesSelectOption(option) {
    await this.especialidadesSelect.sendKeys(option);
  }

  getEspecialidadesSelect() {
    return this.especialidadesSelect;
  }

  async getEspecialidadesSelectedOption() {
    return this.especialidadesSelect.element(by.css('option:checked')).getText();
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
