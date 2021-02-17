/// <reference types="Cypress" />

class LogoutElement {
    userProfileDropDown() {
        return cy.get('#userprofile > .fa');
    }

    signOutLink() {
        return cy.get('[title="Sign Out"]')
    }
}

export default LogoutElement
