class CqlEditorPage {
    url() {
        return '/MeasureAuthoringTool/Mat.html#mainTab3';
    }

    cqlEditor() {
        return cy.get('.ace_content');
    }

    saveButton() {
        return cy.get('.btn.btn-primary[title="Save"]');
    }
}

export default CqlEditorPage;