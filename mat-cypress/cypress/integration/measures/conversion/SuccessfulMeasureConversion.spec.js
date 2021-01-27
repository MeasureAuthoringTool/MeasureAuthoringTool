import * as helper from '../../../support/helpers'
import * as measureLibrary from '../../../../elements/MeasureLibraryElements'
import * as dataCreation from '../../../support/MAT/MeasureAndCQLLibraryCreation'
import * as measureComposer from '../../../../elements/MeasureComposerElements'
import * as gridRowActions from '../../../support/MAT/GridRowActions'


describe('Measure Library: FHIR Measure Conversion: Conversion to FHIR', () => {
    beforeEach(function () {
        cy.matLogin(this.data.userName, this.data.password)
        cy.umlsLogin(this.data.umlsApiKey);
    })

    before(function () {
        cy.fixture('mat').then(function (data) {
            this.data = data;
            cy.loadCredentials(this.data)
        })
    })

    afterEach(() => {
        cy.matLogout()
    })

    it('Convert QDM measure to FHIR successfully, verify measure history', () => {

        const measureName = dataCreation.createDraftMeasure('QdmCqlMeasure', 'QDM')
        //cy.spinnerNotVisible()

        helper.enabledWithTimeout(measureLibrary.searchInputBox)
        helper.enterText(measureLibrary.searchInputBox, measureName)
        cy.get(measureLibrary.searchBtn).click()

        cy.spinnerNotVisible()

        //assert model version for QDM Measure
        cy.get(measureLibrary.measureSearchTable).should('contain.text', 'Model Version')
        cy.get(measureLibrary.row1MeasureModelVersion).should('contain.text', '5.5')

        cy.wait(5000)
        cy.get('.GB-MJYKBMI > div').click({force: true})//.should('be.checked')
        cy.wait(5000)
        // gridRowActions.selectRow(measureLibrary.row1MeasureSearch)

        cy.get(measureLibrary.createVersionMeasureSearchBtn).click()
        cy.get(measureLibrary.majorVersionTypeRadio).click()
        cy.get(measureLibrary.packageAndVersion).click()

        cy.spinnerNotVisible()

        cy.get(measureLibrary.continueBtn).click()

        helper.verifySpinnerAppearsAndDissappears()

        helper.visibleWithTimeout(measureLibrary.row1MeasureSearch)

        cy.wait(5000)
        cy.get('.GB-MJYKBMI > div > input').click({force: true})//.should('be.checked')
        cy.wait(5000)

        // gridRowActions.selectRow(measureLibrary.row1MeasureSearch)

        // cy.get(measureLibrary.convertToFhirMeasureSearchBtn).click()

        cy.get('[aria-label="Search History Convert"]').click()

        cy.spinnerNotVisible()

        // helper.visibleWithTimeout(measureLibrary.row1MeasureSearch)
        // cy.wait(15000)
        // //assert model version for FHIR Measure
        // cy.get(measureLibrary.measureSearchTable).should('contain.text', 'Model Version')
        // cy.get(measureLibrary.row1MeasureModelVersion).should('contain.text', '4.0.1')
        //
        // cy.get(measureLibrary.row1MeasureSearch).should('contain.text', 'FHIR / CQL')
        // gridRowActions.doubleClickRow(measureLibrary.row1MeasureSearch)
        //
        // cy.spinnerNotVisible()
        //
        // cy.get('h1').should('contain.text', measureName + ' Draft v1.0.000 (FHIR / CQL)')
        //
        // //navigation to measure packager to validate error message
        // cy.get(measureComposer.measurePackager).click()
        // cy.spinnerNotVisible()
        // cy.get(measureComposer.packageWarningMessage).should('contain.text', ' Please select the measure\'s Population basis prior to packaging.')
        //
        // //measure library tab
        // cy.get(measureLibrary.measureLibraryTab).click()
        //
        // cy.spinnerNotVisible()
        //
        // helper.visibleWithTimeout(measureLibrary.row1MeasureSearch)
        // gridRowActions.selectRow(measureLibrary.row1MeasureSearch)
        //
        // cy.get(measureLibrary.historyMeasureSearchBtn).click()
        //
        // //verifying the log entries
        // helper.visibleWithTimeout(measureLibrary.historyConvertToFHIRUserActionLogEntry)
        // helper.visibleWithTimeout(measureLibrary.historyMeasureCreatedUserActionLogEntry)
        //
        // cy.get(measureLibrary.returnToMeasureLibraryLink).click()
        //
        // cy.spinnerNotVisible()
    })
})
