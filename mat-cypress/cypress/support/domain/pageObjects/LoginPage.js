/// <reference types="Cypress" />

class LoginPage {
    url() {
        return '/MeasureAuthoringTool/Login.html';
    }

    visitPage() {
        cy.visit(this.url());
    }

    userNameTextInput() {
        return cy.get('#okta-signin-username');
    }

    userPasswordTextInput() {
        return cy.get('#okta-signin-password');
    }

    termsAndConditionsCheckBox() {
        return cy.get('.custom-checkbox input');
    }

    submitButton() {
        return cy.get('#okta-signin-submit');
    }

    infoBoxError() {
        return cy.get('.okta-form-infobox-error');
    }

    formTitle() {
        return cy.get('.okta-form-title');
    }
}

export default LoginPage;
