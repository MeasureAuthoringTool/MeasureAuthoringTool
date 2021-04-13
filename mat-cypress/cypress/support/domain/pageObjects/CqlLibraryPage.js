/// <reference types="Cypress" />

class CqlLibraryPage {
    url() {
        return '/MeasureAuthoringTool/Mat.html#mainTab2';
    }

    createVersionTab() {
        return cy.get('#recentActivitySummary_gridToolbar > [aria-label="Recent Activity Create version or draft"]');
    }


}

export default CqlLibraryPage;
