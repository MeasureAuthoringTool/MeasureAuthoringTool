/// <reference types="../support" />
import * as matheader from '../../elements/MATheader';

const os = Cypress.platform; // values are aix, darwin, freebsd, linux, openbsd, sunos, win32, android

export const copyScreenshots = () => {
    cy.log('Current OS: ' + os);
    if ((os === 'darwin') || (os === 'linux')) {
        // cy.exec('npm run copyScreenshots', { timeout: 60000 }).its('code').should('eq', 0);
        cy.exec(`cp -a ./cypress/screenshots/. ./screenshots/current/${env}`, {timeout: 60000}).its('code').should('eq', 0);
    } else if (os === 'win32') {
        cy.exec(`xcopy .\\cypress\\screenshots .\\screenshots\\current\\${env} /E /I /S`, {timeout: 60000}).its('code').should('eq', 0);
    }
};

export const logoutUserwithMultipleMAT = () => {

    visibleWithTimeout(matheader.userprofile);

    cy.get(matheader.userprofile).click();

    visibleWithTimeout(matheader.signoutMultipleMAT);

    cy.get(matheader.signoutMultipleMAT).click();

    cy.clearCookies();

    cy.clearLocalStorage();

    cy.window().then((win) => {
        win.sessionStorage.clear();
    });
};
// general
export const get = (element) => {
    cy.get(element);
};
export const navigateToURL = (url) => {
    cy.visit(url);
    // cy.visit(url,  { timeout: 30000 })  //use this if for capping the timeout of page navigation
};
export const click = (element) => {
    cy.get(element).click();
};
export const clickForce = (element) => {
    cy.get(element).click({force: true});
};
export const clickPosition = (element, position) => {
    cy.get(element).click(position);
};
export const clickDirection = (element, direction) => {
    cy.get(element).click(direction);
};
export const haveText = (element, text) => {
    cy.get(element).should('have.text', text);
};
export const notHaveText = (element, text) => {
    cy.get(element).should('not.have.text', text);
};
export const waitToHaveText = (element, text, timeout) => {
    cy.get(element, {timeout: timeout}).should('have.text', text);
};
export const waitToContainText = (element, text, timeout) => {
    cy.get(element, {timeout: timeout}).should('contain', text);
};
export const waitToNotContainText = (element, text, timeout) => {
    cy.get(element, {timeout: timeout}).should('not.contain', text);
};
export const exists = (element) => {
    cy.get(element).should('exist');
};
export const notExists = (element) => {
    cy.get(element).should('not.exist');
};
export const visible = (element) => {
    cy.get(element).should('be.visible');
};
export const visibleClick = (element) => {
    cy.get(element).should('be.visible').click();
};
export const notVisible = (element) => {
    cy.get(element).should('not.be.visible');
};
export const visibleContain = (element, text) => {
    cy.get(element).should('be.visible').should('contain', text);
};
export const existsWithTimeout = (element, timeout = 6000) => {
    cy.get(element, {timeout: timeout}).should('exist');
};
export const notExistsWithTimeout = (element, timeout = 60000) => {
    cy.get(element, {timeout: timeout}).should('not.exist');
};


export const visibleWithTimeout = (element, timeout = 60000) => {
    cy.log('Element->' + element);
    cy.get(element, {timeout: timeout}).should('be.visible');
};

export const notVisibleWithTimeout = (element, timeout = 400000) => {
    cy.get(element, {timeout: timeout}).should('not.be.visible');
};
export const notNull = (element) => {
    cy.get(element).should('not.be.null');
};
export const notUndefined = (element) => {
    cy.get(element).should('not.be.undefined');
};
export const haveAttribute = (element, attribute, value) => {
    cy.get(element).should('have.attr', attribute, value);
};
export const notHaveAttribute = (element, attribute) => {
    cy.get(element).should('not.have.attr', attribute);
};
export const haveClass = (element, classValue) => {
    cy.get(element).should('have.class', classValue);
};
export const haveNoClass = (element, classValue) => {
    cy.get(element).should('not.have.class', classValue);
};
export const containClass = (element, classValue) => {
    cy.get(element).should('contain.class', classValue);
};
export const notContainClass = (element, classValue) => {
    cy.get(element).should('not.contain.class', classValue);
};
export const urlIncludes = (text) => {
    cy.url().should('include', text);
};
export const urlNotIncludes = (text) => {
    cy.url().should('not.include', text);
};
export const elementContains = (element, text) => {
    cy.get(element).should('contain', text);
};
export const elementNotContains = (element, text) => {
    cy.get(element).should('not.contain', text);
};
export const sleep = (milliseconds) => {
    cy.wait(milliseconds);
};
export const shouldContainClick = (element, text) => {
    cy.get(element).should('contain', text).click();
};
export const rowIsChecked = (element, row) => {
    cy.get(element).eq(row).should('be.checked');
};
export const rowIsNotChecked = (element, row) => {
    cy.get(element).eq(row).should('not.be.checked');
};
export const isChecked = (element) => {
    cy.get(element).should('be.checked');
};
export const isNotChecked = (element) => {
    cy.get(element).should('not.be.checked');
};
export const check = (element) => {
    cy.get(element).check({force: true});
};
export const pageContains = (text) => {
    cy.contains(text);
};
export const pageNotContains = (text) => {
    cy.get('body').should('not.contain', text);
};
export const clear = (element) => {
    cy.get(element).clear();
};
export const selectDropdownOption = (element, option, value) => {
    cy.get(element).select(option).should('have.value', value);
};
export const selectListOption = (element, option) => {
    cy.get(element).select(option);
};
export const getDropdown = (element) => {
    cy.get(element).should('be.visible').click();
};
export const getDropdownInputOption = (element, text) => {
    cy.get(element).type(text);
};
export const getDropdownOption = (element, label) => {
    cy.get(element).contains(label).click({force: true});
};
export const clickDropdownOption = (element, text) => {
    cy.get(element).contains(text).click({force: true});
};
export const log = (val) => {
    cy.log(val);
};
export const takeScreenshot = (screenshotName) => {
    cy.screenshot(screenshotName, {capture: 'viewport'});
};
export const takeScreenshotFullPage = (screenshotName) => {
    cy.screenshot(screenshotName, {capture: 'fullPage'});
};
export const takeScreenshotRunner = (screenshotName) => {
    cy.screenshot(screenshotName, {capture: 'runner'});
};
export const stopRun = () => {
    Cypress.runner.stop();
};
export const disabled = (el) => {
    cy.get(el).should('be.disabled');
};
export const enabled = (el) => {
    cy.get(el).should('be.enabled');
};
export const enabledWithTimeout = (element, timeout = 60000) => {
    cy.get(element, {timeout: timeout}).should('be.enabled');
};

export const disabledWithTimeout = (element, timeout = 60000) => {
    cy.get(element, {timeout: timeout}).should('be.disabled');
};

// export const notDisabledWithTimeout = (element, timeout = 60000) => {
//     cy.get(element, {timeout: timeout}).should('not.be.disabled');
// };

export const selectRadio = (el) => {
    cy.get(el).check({force: true});
    cy.get(el).should('be.checked');
};
export const enterText = (el, text) => {
    cy.get(el).clear();
    cy.wait(100);
    cy.get(el).type(text, {delay: 50});
};
export const enterTextBlur = (el, text) => {
    cy.get(el).clear().type(text, {delay: 50}).focus().blur();
};

export const enterTextConfirmCypress = (cypressElement, text) => {
    cypressElement
        .click()
        .focused()
        .clear()
        .type(text, {delay: 50, force: true})
        .should('have.value', text);
};

export const enterTextConfirm = (el, text) => {
    enterTextConfirmCypress(cy.get(el), text);
};

export const enterTextNoClear = (el, text) => {
    cy.get(el).type(text, {delay: 50});
};
export const enterTextDropdown = (element, row, text) => {
    cy.get(element).eq(row).type(text, {delay: 50}).focus().blur();
};
export const enterTextEnter = (el, text) => {
    cy.get(el).clear().type(text, {delay: 50}).type('{enter}');
};
export const clickStatus = () => {
    cy.get('.status__text').click();
};
export const backspace = (element) => {
    cy.get(element).type('{backspace}');
};
export const focus = (element) => {
    cy.get(element).focus();
};
// api
export const server = () => {
    cy.server();
};
export const route = (endpoint, alias) => {
    cy.route(endpoint).as(alias);
};
export const waitForApiCall = (alias) => {
    cy.wait(alias);
};
export const waitForApiCall2 = (alias, timeout) => {
    cy.wait(alias, {requestTimeout: timeout});
};
export const scrollIntoView = (element) => {
    cy.get(element).scrollIntoView().should('be.visible');
};
export const scrollIntoViewContains = (element, text) => {
    cy.get(element).scrollIntoView().should('be.visible').should('contain', text);
};
export const scroll500 = () => {
    cy.scrollTo(0, 500);
};
export const scrollToTop = () => {
    cy.scrollTo('top');
};
export const scrollToGeneric = (position) => {
    cy.scrollTo(position);
};
export const scrollGeneric = (element, direction) => {
    cy.get(element).scrollTo(direction);
};
export const refreshPage = () => {
    cy.reload(true);
};
export const refreshPageNormal = () => {
    cy.reload();
};
export const goBack = () => {
    cy.go('back');
};
export const goForward = () => {
    cy.go('forward');
};
export const tab = () => {
    cy.tab();
};
export const uploadFile = (fileName, element) => {
    cy.uploadFile(fileName, element);
};
export const uploadMultipleFiles = (files, element, type) => {
    cy.uploadMultipleFiles(files, element, type);
};
export const logoutAPI = (authToken) => {
    cy.logout(authToken);
};
export const checkLoadTime = (timeIn) => {
    const loadTime = timeIn || 5;
    cy.window().then((win) => {
        // let time = (win.performance.timing.loadEventEnd - win.performance.timing.navigationStart) / 1000;
        const time = (win.performance.timing.domContentLoadedEventEnd - win.performance.timing.navigationStart) / 1000;
        cy.log(time);
        // verify the load is less than 2 seconds
        expect(time).to.be.lessThan(loadTime);
    });
};
// export const createFile = (file, text) => {
//     cy.writeFile(file, text);
// };
// export const appendToFile = (file, text) => {
//     cy.writeFile(file, '\n', {flag: 'a+'});
//     cy.writeFile(file, text, {flag: 'a+'});
// };

export const deleteScreenshots = () => {
    cy.log('Current OS: ' + os);
    if ((os === 'darwin') || (os === 'linux')) {
        cy.exec('npm run cypress:delete:screenshots', {timeout: 60000, failOnNonZeroExit: false});
    } else if (os === 'win32') {
        cy.exec('npm run cypress:delete:screenshots:windows', {timeout: 60000, failOnNonZeroExit: false});
    }
};
export const getElementTextValue = (element, inputValue) => {
    cy.get(element).invoke('text')
        .then(value => {
            cy.log(value);
            expect(value).to.eql(inputValue);
        });
};
export const getElementInputValue = (element, inputValue) => {
    cy.get(element).invoke('val')
        .then(value => {
            cy.log(value);
            expect(value).to.eql(inputValue);
        });
};
export const elementInputValueBlank = (element) => {
    cy.get(element).invoke('val')
        .then(value => {
            cy.log(value);
            expect(value).to.eql('');
        });
};
export const elementContains2 = (element, text) => {
    cy.get(element).contains(text);
};
export const dragNdrop = (element1, element2) => {
    cy.get(element1)
        .trigger('mousedown', {which: 1});
    cy.get(element2)
        .trigger('mousemove')
        .trigger('mouseup', {froce: true});
};
export const dragNdrop2 = (element) => {
    cy.get(element)
        .trigger('mousedown', {which: 1, pageX: 600, pageY: 100})
        .trigger('mousemove', {which: 1, pageX: 600, pageY: 600})
        .trigger('mouseup');
};
export const dragNdrop3 = (element1, element2) => {
    cy.get(element1)
        .trigger('dragstart');
    cy.get(element2)
        .trigger('drop');
};
export const hover = (element) => {
    cy.get(element).trigger('mouseenter');
};
export const hasLength = (element, length) => {
    cy.get(element).should('have.length', length);
};
export const hasLengthGreaterThanEqualTo = (element, length) => {
    cy.get(element).should('have.length.gte', length);
};
export const hasLengthGreaterThanEqualTo2 = (table, length) => {
    cy.get(table).find('tr').its('length').should('be.gte', length);
};
export const rowContains = (element, row, text) => {
    cy.get(element).should('exist');
    cy.get(element).eq(row).should('contain', text);
};
export const rowNotContains = (element, row, text) => {
    cy.get(element).should('exist');
    cy.get(element).eq(row).should('not.contain', text);
};
export const cellNotNullUndefined = (element, row) => {
    cy.get(element).should('exist');
    cy.get(element).eq(row).should('not.be.null');
    cy.get(element).eq(row).should('not.be.undefined');
};
export const rowContainClass = (element, row, classValue) => {
    cy.get(element).should('exist');
    cy.get(element).eq(row).should('contain.class', classValue);
};
export const rowHaveClass = (element, row, classValue) => {
    cy.get(element).should('exist');
    cy.get(element).eq(row).should('have.class', classValue);
};
export const clickRow = (element, row) => {
    cy.get(element).eq(row).click();
};
export const clickRowDirection = (element, row, direction) => {
    cy.get(element).eq(row).click(direction);
};
export const paginationLength = (element, length) => {
    cy.get(element).should('have.length.lte', length);
};
export const clickLastRow = (element) => {
    cy.get(element).last().click();
};
export const clickFirstRow = (element) => {
    cy.get(element).first().click();
};
export const lastRowContains = (element, text) => {
    cy.get(element).should('exist');
    cy.get(element).last().should('contain', text);
};
export const lastRowNotContains = (element, text) => {
    cy.get(element).should('exist');
    cy.get(element).last().should('not.contain', text);
};
export const firstRowContains = (element, text) => {
    cy.get(element).should('exist');
    cy.get(element).first().should('contain', text);
};
export const firstRowNotContains = (element, text) => {
    cy.get(element).should('exist');
    cy.get(element).first().should('not.contain', text);
};
export const checkFileDownloaded = (filename) => {
    cy.exec(`node ./utils/js/waitForFileToExistLocally.js "${downloadFolder}${filename}"`, {timeout: 60000}).its('code').should('eq', 0);
};
export const deleteDownloadedFile = (filename) => {
    cy.exec(`node ./utils/js/deleteFile.js "${downloadFolder}${filename}"`, {timeout: 60000}).its('code').should('eq', 0);
};
export const deleteFileGeneric = (filename) => {
    cy.exec(`rm ${filename}`, {timeout: 60000}).its('code').should('eq', 0);
};
export const waitForElementEnabled = (element, timeout) => {
    cy.get(element, {timeout: timeout}).should('be.enabled');
};

export const copyFileGeneric = (source, destination) => {
    cy.log('Current OS: ' + os);
    if ((os === 'darwin') || (os === 'linux')) {
        cy.exec(`cp -a ./${source} ./cypress/fixtures/${destination}`, {timeout: 60000}).its('code').should('eq', 0);
    } else if (os === 'win32') {
        cy.exec(`xcopy .\\${source} .\\cypress\\fixtures\\${destination} /E /I /S`, {timeout: 60000}).its('code').should('eq', 0);
    }
};
export const getFileName = (fileType) => {
    const currdate = Cypress.moment().format('YYYYMMDD');
    return `${fileType}_${currdate}`;
};
export const checkColor = (element, r, g, b) => {
    cy.get(element).should('have.css', 'color').and('equal', `rgb(${r}, ${g}, ${b})`);
};

export const spinnerVisible = () => {
    visibleWithTimeout(matheader.spinner, 60000);
};
export const spinnerNotVisible = () => {

    // waitToHaveText(matheader.spinnerShadow,'Loading Please Wait...')
    // waitToHaveText(matheader.spinnerShadow,'...')

    notVisibleWithTimeout(matheader.spinner, 90000);
    //  notVisibleWithTimeout(matheader.spinnerWrapper, 90000)
    //  notVisibleWithTimeout(matheader.spinnerShadow, 90000)
    //  notVisibleWithTimeout(matheader.spinnerModal, 90000)
    //   notVisibleWithTimeout(matheader.spinner, 90000)
};
export const spinnerExists = () => {
    existsWithTimeout(matheader.spinner, 60000);
};
export const spinnerNotExists = () => {
    notExistsWithTimeout(matheader.spinner, 60000);
    //notExistsWithTimeout(matheader.spinnerWrapper, 60000)
    notExistsWithTimeout(matheader.spinnerShadow, 60000);
    notExistsWithTimeout(matheader.spinnerModal, 60000);
};
export const verifySpinnerAppearsAndDissappears = () => {

    spinnerNotVisible();
    cy.wait(1500);
};
export const verifySpinnerExists = () => {
    spinnerExists();
    spinnerVisible();
};
export const verifySpinnerNotExists = () => {
    spinnerNotExists();
    spinnerNotVisible();
};
export const containClick = (text) => {
    cy.contains(text).click({force: true});
};
export const changeTargetToSelf = (element) => {
    // cy.get(element).invoke('attr', 'target', '_self').should('have.attr', 'target', '_self')
    cy.get(element).invoke('removeAttr', 'target');
};

Date.prototype.getMonthFormatted = function () {
    var month = this.getMonth() + 1;
    return month < 10 ? '0' + month : '' + month; // ('' + month) for string result
};

Date.prototype.getDayFormatted = function () {
    var day = this.getDate();
    return day < 10 ? '0' + day : '' + day; // ('' + month) for string result
};
