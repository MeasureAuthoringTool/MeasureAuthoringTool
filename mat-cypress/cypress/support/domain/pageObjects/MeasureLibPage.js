/// <reference types="Cypress" />

class MeasureLibPage {
    url() {
        return '/MeasureAuthoringTool/Mat.html#mainTab0';
    }

    searchInputBox() {
        return cy.get('#SearchFilterWidget_SearchInputHPanel_forMeasure > tbody > tr > td > input');
    }

    searchFilterTextBox() {
        return cy.get('.form-control.searchFilterTextBox');
    }

    searchButton() {
        return cy.get('#SearchWidgetButton_forMeasure');
    }

    createVersionTabButton() {
        return cy.get('#MeasureSearchCellTable_gridToolbar > [title="Click to create version"]');
    }

    convertToFhirTabButton() {
        return cy.get('[aria-label="Search History Convert"]');
    }

    tr1MeasureSearch() {
        return cy.get('#MeasureSearchCellTable [__gwt_row="0"]');
    }

    measureSearchResultCheckBox() {
        return cy.get('.GB-MJYKBMI > div > input');
    }

    measureSearchCellTable() {
        return cy.get('#MeasureSearchCellTable');
    }

    measureSearchCellTableTds() {
        return cy.get('#MeasureSearchCellTable > tbody > tr > td');
    }

    measureSearchCellTableTbody() {
        return cy.get('#MeasureSearchCellTable > tbody');
    }

    modelTypeSelect() {
        return cy.get('#modelType');
    }

    alertSuccess() {
        return cy.get('.alert-success > div > b');
    }
}

export default MeasureLibPage;
