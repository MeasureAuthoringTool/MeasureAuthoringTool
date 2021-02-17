/// <reference types="Cypress" />

import * as cqlElements from '../../../elements/CqlLibraryElements'
import * as helper from '../../support/helpers'
import * as measureElements from '../../../elements/MeasureLibraryElements'


describe('Mat Test', function () {
    beforeEach(function () {
        cy.matLogin(this.data.userName, this.data.password)
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

    it('Mat login logout', function () {
        cy.get(measureElements.cqlLibraryTab).click()
        cy.spinnerNotVisible()
        helper.enabledWithTimeout(cqlElements.searchInputBox)
        cy.get(cqlElements.filterByMyLibrariesChkBox).click()
        helper.enterText(cqlElements.searchInputBox, "AdvancedIllnessandFrailtyExclusion")

        cy.get(cqlElements.modelTypeListBox).select('Model Type: FHIR / CQL Only').should('have.value', 'FHIR')
        cy.get(cqlElements.searchBtn).click()
        cy.spinnerNotVisible()

        cy.get('#CQLLibrarySearchCellTable [__gwt_row] > .GB-MJYKBGJ').each((element) => {
            cy.wrap(element).should('contain.text', 'FHIR / CQL')
        })

        cy.get(cqlElements.modelTypeListBox).select('Model Type: QDM / CQL Only').should('have.value', 'QDM_CQL')
        cy.get(cqlElements.searchBtn).click({force: true})

        cy.spinnerNotVisible()
        cy.wait(2000 )

        cy.get('#CQLLibrarySearchCellTable [__gwt_row] > .GB-MJYKBGJ').each((element) => {
            cy.wrap(element).should('contain.text', 'QDM / CQL')
        })
    })
})
