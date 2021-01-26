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

Cypress.Commands.add("matLogin", (userName, password, checkCheckBox = true) => {
    cy.visit("/MeasureAuthoringTool/Login.html")
    cy.get('#okta-signin-username').type(userName)
    cy.get('#okta-signin-password').type(password)

    if (checkCheckBox) {
        cy.get('.custom-checkbox input').check({force: true}).should('be.checked')
        cy.get('#okta-signin-submit').click()
    }
})

Cypress.Commands.add("matLogout", () => {
    cy.get('#userprofile > .fa').click()
    cy.get(':nth-child(6) > a').click()

    cy.get('.okta-form-title').contains('Sign In')
})

Cypress.Commands.add("umlsLogin", (umlsApiKey, headerLabel = 'UMLS Active') => {
    cy.get(':nth-child(1) > .loginSpacer > :nth-child(1) > .btn > span').click();

    helper.enterTextConfirm('#inputPwd', umlsApiKey)

    cy.get('#umlsSubmitButton').click()
    cy.wait(1000)

    if (headerLabel.length === 0) {
        console.log("Not checking");
    } else {
        console.log(" checking");
        cy.get('.close').click()
        cy.get('[style=""] > #disco > span').contains(headerLabel)
    }
})
