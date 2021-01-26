import * as helper from "./helpers";

const ENV_TAG = "env:"

function parse(value = "") {
    if (value.startsWith(ENV_TAG)) {
        const target = value.substr(ENV_TAG.length)
        return Cypress.env(target)
    } else {
        return value;
    }
}

Cypress.Commands.add("loadCredentials", (credentials) => {
    credentials.password = parse(credentials.password)
    credentials.userName = parse(credentials.userName)
    credentials.umlsApiKey = parse(credentials.umlsApiKey)
})

Cypress.Commands.add("matLogin", (userName, password) => {
    cy.visit("/MeasureAuthoringTool/Login.html")
    cy.get('#okta-signin-username').type(userName)
    cy.get('#okta-signin-password').type(password)

    cy.get('.custom-checkbox input').check({force: true}).should('be.checked')
    cy.get('#okta-signin-submit').click()
})

Cypress.Commands.add("matLogout", () => {
    cy.get('#userprofile > .fa').click()
    cy.get(':nth-child(6) > a').click()

    cy.get('.okta-form-title').contains('Sign In')
})

Cypress.Commands.add("umlsLogin", (umlsApiKey) => {
    cy.get(':nth-child(1) > .loginSpacer > :nth-child(1) > .btn > span').click();

    helper.enterText('#inputPwd', umlsApiKey)

    cy.get('#umlsSubmitButton').click()
    cy.wait(1000)

    cy.get('.close').click()
    cy.get('[style=""] > #disco > span').contains('UMLS Active')
})
