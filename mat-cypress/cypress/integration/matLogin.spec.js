/// <reference types="Cypress" />


const badPassword = "bad__Password_";
const badUserName = "bad__User__Name_";
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

        cy.matLogin(badUserName, badPassword)

        cy.get('.okta-form-infobox-error').contains("Sign in failed!")

        cy.matLogin(this.data.userName, badPassword)

        cy.get('.okta-form-infobox-error').contains("Sign in failed!")

        cy.matLogin(this.data.userName, this.data.password, false) // wont check checkBox
        cy.get('#okta-signin-submit').should('be.disabled')

        cy.matLogin(this.data.userName, this.data.password)

        cy.umlsLogin("2572222-1111-aaaa-yyyy-XXXXXXXXXX", '') // MT string wont close or check for 'UMLS Active'

        cy.get('.help-block').contains('Login failed. Please sign in again.')
        cy.get('.close').click()
        cy.umlsLogin(this.data.umlsApiKey);

    })
})
