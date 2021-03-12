/// <reference types="Cypress" />

class CqlWorkspaceDefinitionPage {
    insertLink() {
        return cy.get('#insertButton_definition');
    }
}

export default CqlWorkspaceDefinitionPage;
