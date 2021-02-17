/// <reference types="Cypress" />


import {row3CqlLibrarySearch} from "../../elements/CqlLibraryElements";

const BAD_PASSWORD = "bad__Password_";
const BAD_USER_NAME = "bad__User__Name_";
import LoginPage from "../support/domain/pageObjects/LoginPage";

describe('Test Login functionality', function () {


    before(function () {
        cy.fixture('mat').then(function (data) {
            this.data = data;
            cy.loadCredentials(this.data)
        })
    })

    after(() => {
         cy.matLogout()
    })

    it('Mat login logout', function () {
       const loginPage = new LoginPage();

        cy.matLogin(BAD_USER_NAME, BAD_PASSWORD)

        loginPage.infoBoxError().contains("Sign in failed!")

        cy.matLogin(this.data.userName, BAD_PASSWORD)

        loginPage.infoBoxError().contains("Sign in failed!")

        cy.matLogin(this.data.userName, this.data.password, false) // wont check checkBox
        loginPage.submitButton().should('be.disabled')

        cy.matLogin(this.data.userName, this.data.password)

        cy.umlsLogin("2572222-1111-aaaa-yyyy-XXXXXXXXXX", '') // MT string wont close or check for 'UMLS Active'

        cy.get('.help-block').contains('Login failed. Please sign in again.')
        cy.get('.close').click()
        cy.umlsLogin(this.data.umlsApiKey);

    })
})
