/// <reference types="Cypress" />

class CqlWorkspaceGeneralInformationPage {
    definitionLink() {
        return cy.get('#defineLabel_Label');
    }
}

export default CqlWorkspaceGeneralInformationPage;
