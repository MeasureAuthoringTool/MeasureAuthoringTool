/// <reference types="Cypress" />

class UmlsElement {
    apiKeyTextInput() {
        return cy.get('#inputPwd')
    }

    signInLink() {
        return cy.get(':nth-child(1) > .loginSpacer > :nth-child(1) > .btn > span')
    }

    submitButton() {
        return cy.get('#umlsSubmitButton')
    }

    loginSuccessSpan() {
        return cy.get('[style=""] > #disco > span')
    }

    dialogCloseButton() {
        return cy.get('.close')
    }
}

export default UmlsElement
