/// <reference types="Cypress" />


const BAD_PASSWORD = "bad__Password_";
const BAD_USER_NAME = "bad__User__Name_";
const OKTA_ERROR_ELEMENT = '.okta-form-infobox-error';

describe('Test Login functionality', function () {


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

        cy.matLogin(BAD_USER_NAME, BAD_PASSWORD)

        cy.get(OKTA_ERROR_ELEMENT).contains("Sign in failed!")

        cy.matLogin(this.data.userName, BAD_PASSWORD)

        cy.get(OKTA_ERROR_ELEMENT).contains("Sign in failed!")

        cy.matLogin(this.data.userName, this.data.password, false) // wont check checkBox
        cy.get('#okta-signin-submit').should('be.disabled')

        cy.matLogin(this.data.userName, this.data.password)

        cy.umlsLogin("2572222-1111-aaaa-yyyy-XXXXXXXXXX", '') // MT string wont close or check for 'UMLS Active'

        cy.get('.help-block').contains('Login failed. Please sign in again.')
        cy.get('.close').click()
        cy.umlsLogin(this.data.umlsApiKey);
    })
})
