import * as helper from './helpers';

import LoginPage from './domain/pageObjects/LoginPage';
import LogoutElement from './domain/pageElements/LogoutElement';
import UmlsElement from './domain/pageElements/UmlsElement';

const ENV_TAG = 'env:';

function parse(value = '') {
    if (value.startsWith(ENV_TAG)) {
        const target = value.substr(ENV_TAG.length);
        return Cypress.env(target);
    } else {
        return value;
    }
}

Cypress.Commands.add('loadCredentials', (credentials) => {
    credentials.password = parse(credentials.password);
    credentials.userName = parse(credentials.userName);
    credentials.umlsApiKey = parse(credentials.umlsApiKey);
});

Cypress.Commands.add('matLogin', (userName, password, checkCheckBox = true) => {
    const loginPage = new LoginPage();

    loginPage.visitPage();
    loginPage.userNameTextInput().type(userName);
    loginPage.userPasswordTextInput().type(password);

    if (checkCheckBox) {
        loginPage.termsAndConditionsCheckBox().check({force: true}).should('be.checked');
        loginPage.submitButton().click();
    }
});

Cypress.Commands.add('matLogout', () => {
    const logoutPage = new LogoutElement();
    const loginPage = new LoginPage();

    logoutPage.userProfileDropDown().click();
    logoutPage.signOutLink().click();

    loginPage.formTitle().contains('Sign In');
});

Cypress.Commands.add('umlsLogin', (umlsApiKey, headerLabel = 'UMLS Active') => {
    const umlsElement = new UmlsElement();

    umlsElement.signInLink().click();
    cy.wait(1000);

    helper.enterTextConfirmCypress(umlsElement.apiKeyTextInput(), umlsApiKey);

    umlsElement.submitButton().click();
    cy.wait(1000);

    if (headerLabel.length > 0) {
        umlsElement.loginSuccessSpan().contains(headerLabel);
        umlsElement.dialogCloseButton().click();
    }
});
