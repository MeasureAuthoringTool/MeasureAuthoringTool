/// <reference types="Cypress" />

class CreateMeasureVersionDraftPage {
    majorVersionTypeRadio() {
        return cy.get('#majorRadio_RadioButton')
    }

    packageAndVersionButton() {
        return cy.get('#SaveAndContinueButton_measuVersion')
    }

    dialogContinueButton() {
        return cy.get('#Yes_ConfirmDialogBox')
    }

}

export default CreateMeasureVersionDraftPage
