/// <reference types="Cypress" />

class MeasureComposerPage {
  url() {
    return '/MeasureAuthoringTool/Mat.html#mainTab1';
  }

  deleteButton() {
    return cy.get('#MeasureDetailsView\\.deleteMeasureButton');
  }
}

export default MeasureComposerPage;
