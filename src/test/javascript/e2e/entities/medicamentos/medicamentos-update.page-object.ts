import { element, by, ElementFinder } from 'protractor';

export default class MedicamentosUpdatePage {
  pageTitle: ElementFinder = element(by.id('safhApp.medicamentos.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  descricaoInput: ElementFinder = element(by.css('input#medicamentos-descricao'));
  registroMinisterioSaudeInput: ElementFinder = element(by.css('input#medicamentos-registroMinisterioSaude'));
  codigoBarrasInput: ElementFinder = element(by.css('input#medicamentos-codigoBarras'));
  qtdAtualInput: ElementFinder = element(by.css('input#medicamentos-qtdAtual'));
  qtdMinInput: ElementFinder = element(by.css('input#medicamentos-qtdMin'));
  qtdMaxInput: ElementFinder = element(by.css('input#medicamentos-qtdMax'));
  observacoesInput: ElementFinder = element(by.css('input#medicamentos-observacoes'));
  apresentacaoInput: ElementFinder = element(by.css('input#medicamentos-apresentacao'));
  posologiaPadraoSelect: ElementFinder = element(by.css('select#medicamentos-posologiaPadrao'));
  fabricantesSelect: ElementFinder = element(by.css('select#medicamentos-fabricantes'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setDescricaoInput(descricao) {
    await this.descricaoInput.sendKeys(descricao);
  }

  async getDescricaoInput() {
    return this.descricaoInput.getAttribute('value');
  }

  async setRegistroMinisterioSaudeInput(registroMinisterioSaude) {
    await this.registroMinisterioSaudeInput.sendKeys(registroMinisterioSaude);
  }

  async getRegistroMinisterioSaudeInput() {
    return this.registroMinisterioSaudeInput.getAttribute('value');
  }

  async setCodigoBarrasInput(codigoBarras) {
    await this.codigoBarrasInput.sendKeys(codigoBarras);
  }

  async getCodigoBarrasInput() {
    return this.codigoBarrasInput.getAttribute('value');
  }

  async setQtdAtualInput(qtdAtual) {
    await this.qtdAtualInput.sendKeys(qtdAtual);
  }

  async getQtdAtualInput() {
    return this.qtdAtualInput.getAttribute('value');
  }

  async setQtdMinInput(qtdMin) {
    await this.qtdMinInput.sendKeys(qtdMin);
  }

  async getQtdMinInput() {
    return this.qtdMinInput.getAttribute('value');
  }

  async setQtdMaxInput(qtdMax) {
    await this.qtdMaxInput.sendKeys(qtdMax);
  }

  async getQtdMaxInput() {
    return this.qtdMaxInput.getAttribute('value');
  }

  async setObservacoesInput(observacoes) {
    await this.observacoesInput.sendKeys(observacoes);
  }

  async getObservacoesInput() {
    return this.observacoesInput.getAttribute('value');
  }

  async setApresentacaoInput(apresentacao) {
    await this.apresentacaoInput.sendKeys(apresentacao);
  }

  async getApresentacaoInput() {
    return this.apresentacaoInput.getAttribute('value');
  }

  async posologiaPadraoSelectLastOption() {
    await this.posologiaPadraoSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async posologiaPadraoSelectOption(option) {
    await this.posologiaPadraoSelect.sendKeys(option);
  }

  getPosologiaPadraoSelect() {
    return this.posologiaPadraoSelect;
  }

  async getPosologiaPadraoSelectedOption() {
    return this.posologiaPadraoSelect.element(by.css('option:checked')).getText();
  }

  async fabricantesSelectLastOption() {
    await this.fabricantesSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async fabricantesSelectOption(option) {
    await this.fabricantesSelect.sendKeys(option);
  }

  getFabricantesSelect() {
    return this.fabricantesSelect;
  }

  async getFabricantesSelectedOption() {
    return this.fabricantesSelect.element(by.css('option:checked')).getText();
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
