import * as helper from "./helpers";
import MeasureLibPage from "./domain/pageObjects/MeasureLibPage";
import MeasureComposerPage from "./domain/pageObjects/MeasureComposerPage";
import DeleteMeasureDialog from "./domain/pageDialogs/DeleteMeasureDialog";

export function getModelTypeLabel(isFhir) {
    if (isFhir) {
        return 'Model Type: FHIR / CQL Only';
    } else {
        return 'Model Type: QDM / CQL Only';
    }
}

export function getCellTable(isFhir) {
    if (isFhir) {
        return 'FHIR / CQL';
    } else {
        return 'QDM / CQL';
    }
}

export function getModelTypeValue(isFhir) {
    if (isFhir) {
        return 'FHIR';
    } else {
        return 'QDM_CQL';
    }
}

Cypress.Commands.add("deleteMeasure", (measureName, isFhir) => {
    const measureLibPage = new MeasureLibPage();

    cy.url().should('include', measureLibPage.url());

    helper.enterTextConfirmCypress(measureLibPage.searchFilterTextBox(), measureName);

    measureLibPage.modelTypeSelect().select(getModelTypeLabel(isFhir)).should('have.value', getModelTypeValue(isFhir));
    measureLibPage.searchButton().click();
    cy.wait(2000);

    measureLibPage.measureSearchCellTableTbody().find('tr').should('have.length', 2);
    measureLibPage.measureSearchCellTableTds().contains(getCellTable(isFhir));

    measureLibPage.measureSearchCellTable().find('tbody').first().find('tr').first().click();
    cy.wait(100);
    measureLibPage.measureSearchCellTable().find('tbody').first().find('tr').first().dblclick();

    const measureComposerPage = new MeasureComposerPage();
    cy.url().should('include', measureComposerPage.url());

    measureComposerPage.deleteButton().click();
    cy.wait(1000);

    const deleteMeasureDialog = new DeleteMeasureDialog();
    helper.enterTextConfirmCypress(deleteMeasureDialog.passwordTextBox(), "DELETE");
    cy.wait(1000);

    deleteMeasureDialog.deleteLibraryForever().contains('Delete Library Forever').click();

    cy.url().should('include', measureLibPage.url());
    cy.wait(3000);
    measureLibPage.alertSuccess().contains("No measures returned. Please change your search criteria and search again.");
});

