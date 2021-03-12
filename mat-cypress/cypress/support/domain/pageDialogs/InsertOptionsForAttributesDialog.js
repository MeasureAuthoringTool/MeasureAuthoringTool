/// <reference types="Cypress" />

class InsertOptionsForAttributesDialog {
    attributesByDataTypeSelect() {
        return cy.get('#DataTypeBtAtrr_listBox');
    }

    attributesSelect() {
        return cy.get('#Atrr_listBox');
    }

   cancelButton() {
       return cy.get('#Cancel_button');
   }
}

export default InsertOptionsForAttributesDialog;
