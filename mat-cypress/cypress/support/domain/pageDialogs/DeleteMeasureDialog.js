/// <reference types="Cypress" />

class DeleteMeasureDialog {
    passwordTextBox() {
        return cy.get('#password_PasswordTextBox');
    }

    deleteLibraryForever() {
        return cy.get('.btn-primary');
    }
}

export default DeleteMeasureDialog;
