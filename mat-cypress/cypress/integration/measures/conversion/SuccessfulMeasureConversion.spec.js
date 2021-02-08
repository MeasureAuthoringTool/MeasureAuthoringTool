import * as helper from '../../../support/helpers'
import * as measureLibrary from '../../../../elements/MeasureLibraryElements'
import * as dataCreation from '../../../support/MAT/MeasureAndCQLLibraryCreation'


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

        const cqlMeasureName = dataCreation.createDraftMeasure('QdmCqlMeasure', 'QDM')
        const fhirMeasureName = cqlMeasureName + 'FHIR';

        cy.log("Working measureName: " + cqlMeasureName)
        //cy.spinnerNotVisible()

        helper.enabledWithTimeout(measureLibrary.searchInputBox)
        helper.enterText(measureLibrary.searchInputBox, cqlMeasureName)
        cy.get(measureLibrary.searchBtn).click()

        cy.spinnerNotVisible()

        //assert model version for QDM Measure
        cy.get(measureLibrary.measureSearchTable).should('contain.text', 'Model Version')
        cy.get(measureLibrary.row1MeasureModelVersion).should('contain.text', '5.5')

        cy.wait(5000)
        cy.get('.GB-MJYKBMI > div').click({force: true})//.should('be.checked')
        cy.wait(5000)


        cy.get(measureLibrary.createVersionMeasureSearchBtn).click()
        cy.get(measureLibrary.majorVersionTypeRadio).click()
        cy.get(measureLibrary.packageAndVersion).click()

        cy.spinnerNotVisible()

        cy.get(measureLibrary.continueBtn).click()

        helper.verifySpinnerAppearsAndDissappears()

        helper.visibleWithTimeout(measureLibrary.row1MeasureSearch)

        cy.wait(5000)
        cy.get('.GB-MJYKBMI > div > input').click({force: true})
        cy.wait(5000)


        cy.get('[aria-label="Search History Convert"]').click()

        cy.spinnerNotVisible()

        helper.visibleWithTimeout(measureLibrary.row1MeasureSearch)
        cy.wait(15000)
        cy.get('#MeasureSearchCellTable > tbody > tr > td').contains(fhirMeasureName)
        cy.get('#MeasureSearchCellTable > tbody > tr > td').contains('QDM / CQL')
        cy.get('#MeasureSearchCellTable > tbody > tr > td').contains('FHIR / CQL')
    })
})
