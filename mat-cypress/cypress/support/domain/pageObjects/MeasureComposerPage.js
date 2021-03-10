/// <reference types="Cypress" />

class MeasureComposerPage {
    url() {
        return '/MeasureAuthoringTool/Mat.html#mainTab1';
    }

    deleteButton() {
        return cy.get('#MeasureDetailsView\\.deleteMeasureButton');
    }

    cqlWorkspaceTab() {
        return cy.get('#CQL\\ Workspace');
    }

    measureDetailsTab() {
        return cy.get('#Measure\\ Details > .gwt-HTML');
    }
}

export default MeasureComposerPage;
