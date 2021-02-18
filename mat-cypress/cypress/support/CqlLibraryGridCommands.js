import * as helper from "./helpers";
import * as gridRowActions from "./MAT/GridRowActions";
import * as cqlLibrary from "../../elements/CqlLibraryElements";

function searchAndSelectCqlLibrary(cqlLibraryName) {
    helper.visibleWithTimeout('#CQLLibrarySearchCellTable');
    cy.get('#CQLLibrarySearchCellTable > :nth-child(3)').each(element => {
        if (cy.wrap(element).contains(cqlLibraryName)) {
            gridRowActions.selectRow(element);
        }
    });
}

Cypress.Commands.add("deleteCqlLibrary", (cqlLibraryName) => {
    searchAndSelectCqlLibrary(cqlLibraryName);
    cy.get(cqlLibrary.deleteCqllibrariesBtn).click();

    cy.get(cqlLibrary.confirmDeleteText).type('DELETE', {force: true});
    cy.get(cqlLibrary.confirmDeleteBtn).click();
});

Cypress.Commands.add("createCqlLibraryVersionAndVerify", (cqlLibraryName) => {
    searchAndSelectCqlLibrary(cqlLibraryName);
    cy.get(cqlLibrary.createVersionCqllibrariesBtn).click();
    cy.get(cqlLibrary.majorVersionTypeRadio).click();
    cy.get(cqlLibrary.versionSaveAndContinueBtn).click();

    helper.verifySpinnerAppearsAndDissappears();

    gridRowActions.selectRow(cqlLibrary.row1CqlLibrarySearch);
    cy.get(cqlLibrary.viewCqllibrariesBtn).should('be.visible');
});

Cypress.Commands.add("convertCqlLibraryToFHIRAndVerify", (cqlLibraryName) => {
    // Select the grid row before calling this command
    cy.get(cqlLibrary.convertToFhirLibrarySearchBtn).click();

    helper.verifySpinnerAppearsAndDissappears();
    helper.verifySpinnerAppearsAndDissappears();
    helper.verifySpinnerAppearsAndDissappears();

    // assert if new draft FHIR Library is created
    helper.visibleWithTimeout(cqlLibrary.row2CqlLibrarySearch);
    cy.get(cqlLibrary.row2CqlLibraryName).should('contain.text', cqlLibraryName + 'FHIR');
    cy.get(cqlLibrary.row2CqlLibraryModelVersion).should('contain.text', '4.0.1');
    cy.get(cqlLibrary.row2CqlLibrarySearch).should('contain.text', 'FHIR / CQL');
    cy.get(cqlLibrary.row2CqlLibraryVersionColumn).should('contain.text', 'Draft v0.0.000');
});



