class CqlComposerPage {
    url() {
        return '/MeasureAuthoringTool/Mat.html#mainTab3';
    }

    cqlLibraryDescriptionTextArea() {
        return cy.get('#descriptionTextArea');
    }

    publisherListBox() {
        return cy.get('#publisherListBoxMVP');
    }

    saveButton() {
        return cy.get('#saveButton_GeneralInfo');
    }

    cqlLibraryEditorLink() {
        return cy.get('#cqlLibraryEditor_Anchor > a');
    }
}

export default CqlComposerPage;