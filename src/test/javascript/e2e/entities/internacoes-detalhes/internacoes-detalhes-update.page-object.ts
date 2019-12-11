import { element, by, ElementFinder } from 'protractor';

export default class InternacoesDetalhesUpdatePage {
  pageTitle: ElementFinder = element(by.id('safhApp.internacoesDetalhes.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  dataDetalheInput: ElementFinder = element(by.css('input#internacoes-detalhes-dataDetalhe'));
  horarioInput: ElementFinder = element(by.css('input#internacoes-detalhes-horario'));
  qtdInput: ElementFinder = element(by.css('input#internacoes-detalhes-qtd'));
  internacoesSelect: ElementFinder = element(by.css('select#internacoes-detalhes-internacoes'));
  dietasSelect: ElementFinder = element(by.css('select#internacoes-detalhes-dietas'));
  prescricoesSelect: ElementFinder = element(by.css('select#internacoes-detalhes-prescricoes'));
  posologiasSelect: ElementFinder = element(by.css('select#internacoes-detalhes-posologias'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setDataDetalheInput(dataDetalhe) {
    await this.dataDetalheInput.sendKeys(dataDetalhe);
  }

  async getDataDetalheInput() {
    return this.dataDetalheInput.getAttribute('value');
  }

  async setHorarioInput(horario) {
    await this.horarioInput.sendKeys(horario);
  }

  async getHorarioInput() {
    return this.horarioInput.getAttribute('value');
  }

  async setQtdInput(qtd) {
    await this.qtdInput.sendKeys(qtd);
  }

  async getQtdInput() {
    return this.qtdInput.getAttribute('value');
  }

  async internacoesSelectLastOption() {
    await this.internacoesSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async internacoesSelectOption(option) {
    await this.internacoesSelect.sendKeys(option);
  }

  getInternacoesSelect() {
    return this.internacoesSelect;
  }

  async getInternacoesSelectedOption() {
    return this.internacoesSelect.element(by.css('option:checked')).getText();
  }

  async dietasSelectLastOption() {
    await this.dietasSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async dietasSelectOption(option) {
    await this.dietasSelect.sendKeys(option);
  }

  getDietasSelect() {
    return this.dietasSelect;
  }

  async getDietasSelectedOption() {
    return this.dietasSelect.element(by.css('option:checked')).getText();
  }

  async prescricoesSelectLastOption() {
    await this.prescricoesSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async prescricoesSelectOption(option) {
    await this.prescricoesSelect.sendKeys(option);
  }

  getPrescricoesSelect() {
    return this.prescricoesSelect;
  }

  async getPrescricoesSelectedOption() {
    return this.prescricoesSelect.element(by.css('option:checked')).getText();
  }

  async posologiasSelectLastOption() {
    await this.posologiasSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async posologiasSelectOption(option) {
    await this.posologiasSelect.sendKeys(option);
  }

  getPosologiasSelect() {
    return this.posologiasSelect;
  }

  async getPosologiasSelectedOption() {
    return this.posologiasSelect.element(by.css('option:checked')).getText();
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
