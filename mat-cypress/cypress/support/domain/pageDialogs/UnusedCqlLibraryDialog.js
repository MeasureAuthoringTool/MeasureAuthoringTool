/// <reference types="Cypress" />

class UnusedCqlLibraryDialog {
    continueButton() {
        return cy.get('#Yes_ConfirmDialogBox');
    }
}

export default UnusedCqlLibraryDialog;
