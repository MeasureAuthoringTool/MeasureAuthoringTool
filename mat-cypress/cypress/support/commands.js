/// <reference types="Cypress" />


const spinner = '.spinner-loading';


Cypress.Commands.add("spinnerNotVisible", (timeOut = 10000) => {
    cy.get(spinner, {timeout: timeOut}).should('not.be.visible');
    // cy.get(spinnerShadow, {timeout: timeOut}).should('not.be.visible')
    // cy.get(spinnerWrapper, {timeout: timeOut}).should('not.be.visible')
    //cy.get(spinnerModal, {timeout: timeOut}).should('not.be.visible')
});














