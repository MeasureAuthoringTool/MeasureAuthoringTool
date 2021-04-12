/// <reference types="Cypress" />

class InsertItemCqlEditorDialog {
    itemToInsertSelect() {
        return cy.get('#availableItemToInsert_ListBox');
    }
}

export default InsertItemCqlEditorDialog;
