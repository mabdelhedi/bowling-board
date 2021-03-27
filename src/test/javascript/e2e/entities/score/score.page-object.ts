import { element, by, ElementFinder } from 'protractor';

export class ScoreComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-score div table .btn-danger'));
  title = element.all(by.css('jhi-score div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class ScoreUpdatePage {
  pageTitle = element(by.id('jhi-score-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  nbKeelInput = element(by.id('field_nbKeel'));
  tourInput = element(by.id('field_tour'));
  lancierInput = element(by.id('field_lancier'));

  gameSelect = element(by.id('field_game'));
  playerSelect = element(by.id('field_player'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setNbKeelInput(nbKeel: string): Promise<void> {
    await this.nbKeelInput.sendKeys(nbKeel);
  }

  async getNbKeelInput(): Promise<string> {
    return await this.nbKeelInput.getAttribute('value');
  }

  async setTourInput(tour: string): Promise<void> {
    await this.tourInput.sendKeys(tour);
  }

  async getTourInput(): Promise<string> {
    return await this.tourInput.getAttribute('value');
  }

  async setLancierInput(lancier: string): Promise<void> {
    await this.lancierInput.sendKeys(lancier);
  }

  async getLancierInput(): Promise<string> {
    return await this.lancierInput.getAttribute('value');
  }

  async gameSelectLastOption(): Promise<void> {
    await this.gameSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async gameSelectOption(option: string): Promise<void> {
    await this.gameSelect.sendKeys(option);
  }

  getGameSelect(): ElementFinder {
    return this.gameSelect;
  }

  async getGameSelectedOption(): Promise<string> {
    return await this.gameSelect.element(by.css('option:checked')).getText();
  }

  async playerSelectLastOption(): Promise<void> {
    await this.playerSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async playerSelectOption(option: string): Promise<void> {
    await this.playerSelect.sendKeys(option);
  }

  getPlayerSelect(): ElementFinder {
    return this.playerSelect;
  }

  async getPlayerSelectedOption(): Promise<string> {
    return await this.playerSelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class ScoreDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-score-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-score'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
