/// <reference types="Cypress" />


const spinner = '.spinner-loading';
const spinnerShadow = '.spinner-loading .spinner-loading-shadow';
const spinnerWrapper = '.spinner-wrapper';
const spinnerModal = '#loadingSpinnerModalMessage';

Cypress.Commands.add("spinnerNotVisible", (timeOut = 10000) => {
    cy.get(spinner, {timeout: timeOut}).should('not.be.visible');
    // cy.get(spinnerShadow, {timeout: timeOut}).should('not.be.visible')
    // cy.get(spinnerWrapper, {timeout: timeOut}).should('not.be.visible')
    //cy.get(spinnerModal, {timeout: timeOut}).should('not.be.visible')
});














