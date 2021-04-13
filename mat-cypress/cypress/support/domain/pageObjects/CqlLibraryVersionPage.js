/// <reference types="Cypress" />

class CqlLibraryVersionPage {
    url() {
        return '/MeasureAuthoringTool/Mat.html#mainTab2';
    }

    minorVersion() {
        return cy.get('#gwt-uid-18');
    }

    saveAndContinueButton() {
        return cy.get('#SaveAndContinueButton_cqlVersion');
    }
}

export default CqlLibraryVersionPage;
