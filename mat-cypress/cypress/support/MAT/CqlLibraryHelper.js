import * as helper from '../helpers'
import * as gridRowActions from './GridRowActions'
import * as cqlLibrary from '../../../elements/CqlLibraryElements'

export function createCqlLibraryVersionAndVerify () {
    cy.get(cqlLibrary.createVersionCqllibrariesBtn).click()
    cy.get(cqlLibrary.majorVersionTypeRadio).click()
    cy.get(cqlLibrary.versionSaveAndContinueBtn).click()

    helper.verifySpinnerAppearsAndDissappears()

    gridRowActions.selectRow(cqlLibrary.row1CqlLibrarySearch)
    cy.get(cqlLibrary.viewCqllibrariesBtn).should('be.visible')
}

export function convertCqlLibraryToFHIRAndVerify(qdmCqlLibraryName) {
    // Select the versioned lib and convert it to FHIR
    cy.get(cqlLibrary.convertToFhirLibrarySearchBtn).click()

    helper.verifySpinnerAppearsAndDissappears()
    helper.verifySpinnerAppearsAndDissappears()
    helper.verifySpinnerAppearsAndDissappears()

    // assert if new draft FHIR Library is created
    helper.visibleWithTimeout(cqlLibrary.row2CqlLibrarySearch)
    cy.get(cqlLibrary.row2CqlLibraryName).should('contain.text', qdmCqlLibraryName + 'FHIR')
    cy.get(cqlLibrary.row2CqlLibraryModelVersion).should('contain.text', '4.0.1')
    cy.get(cqlLibrary.row2CqlLibrarySearch).should('contain.text', 'FHIR / CQL')
    cy.get(cqlLibrary.row2CqlLibraryVersionColumn).should('contain.text', 'Draft v0.0.000')
}
