import * as helper from "./helpers";

export function getModelTypeLabel(isFhir) {
    if (isFhir) {
        return 'Model Type: FHIR / CQL Only'
    } else {
        return 'Model Type: QDM / CQL Only'
    }
}

export function getCellTable(isFhir) {
    if (isFhir) {
        return 'FHIR / CQL'
    } else {
        return 'QDM / CQL'
    }
}

export function getModelTypeValue(isFhir) {
    if (isFhir) {
        return 'FHIR'
    } else {
        return 'QDM_CQL'
    }
}

Cypress.Commands.add("deleteMeasure", (measureName, isFhir) => {
    cy.url().should('include', 'Mat.html#mainTab0')
    helper.enterText('.form-control.searchFilterTextBox', measureName)

    cy.get('#modelType').select(getModelTypeLabel(isFhir)).should('have.value', getModelTypeValue(isFhir))
    cy.get('#SearchWidgetButton_forMeasure').click();
    cy.wait(2000)
    cy.get('#MeasureSearchCellTable > tbody').find('tr').should('have.length', 2)
    cy.get('#MeasureSearchCellTable > tbody > tr > td').contains(getCellTable(isFhir))

    cy.get('#MeasureSearchCellTable').find('tbody').first().find('tr').first().click();
    cy.wait(100)
    cy.get('#MeasureSearchCellTable').find('tbody').first().find('tr').first().dblclick();
    cy.url().should('include', 'Mat.html#mainTab1')

    cy.get('#MeasureDetailsView\\.deleteMeasureButton').click()
    cy.wait(1000)

    helper.enterText('#password_PasswordTextBox', "DELETE")
    cy.wait(1000)

    cy.get('.btn-primary').contains('Delete Library Forever').click();
    cy.url().should('include', 'Mat.html#mainTab0')
    cy.wait(3000)
    cy.get('b').contains("No measures returned. Please change your search criteria and search again.")
})

