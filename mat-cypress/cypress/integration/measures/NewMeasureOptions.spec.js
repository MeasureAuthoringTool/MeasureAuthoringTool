import * as newMeasurePage from '../../../elements/CreateNewMeasureElements';
import * as measureLibrary from '../../../elements/MeasureLibraryElements';
import * as helper from '../../support/helpers';

describe('New Measure: CQL Library Name Options', () => {
  beforeEach(() => {
    cy.get(measureLibrary.newMeasureButton).click();
    cy.wait(1000);
  });

  afterEach(() => {
    cy.get(newMeasurePage.cancelBtn).click();
    cy.wait(1000);
  });

  before(() => {
    cy.fixture('mat').then(function (data) {
      cy.loadCredentials(data).then(() => {
        cy.matLogin(data.userName, data.password);
        helper.enabledWithTimeout(measureLibrary.newMeasureButton);
      });
    });
  });

  after(() => {
    cy.matLogout();
  });

  it('has default state', () => {
    cy.get(newMeasurePage.generateCmsIdCheckbox).should('be.enabled');
    cy.get(newMeasurePage.matchLibraryNameCheckbox).should('be.disabled');
    cy.get(newMeasurePage.cqlLibraryName).should('be.enabled');
  });

  it('adjusts the label based on Measure Type', () => {
    cy.get(newMeasurePage.generateCmsIdCheckbox).should('be.enabled');
    cy.get(newMeasurePage.modelradioFHIR).click();
    cy.get(newMeasurePage.generateCmsIdCheckboxLabel).should('contain', 'CMS ID');
    cy.get(newMeasurePage.generateCmsIdCheckboxLabel).should('not.contain', 'eCQM ID');

    cy.get(newMeasurePage.modelradioQDM).click();
    cy.get(newMeasurePage.generateCmsIdCheckboxLabel).should('not.contain', 'CMS ID');
    cy.get(newMeasurePage.generateCmsIdCheckboxLabel).should('contain', 'eCQM ID');
  });

  it('enables the Match CQL Library Name checkbox when Generate ID checkbox is checked', () => {
    cy.get(newMeasurePage.generateCmsIdCheckbox).check();
    cy.get(newMeasurePage.matchLibraryNameCheckbox).should('be.enabled');
    cy.get(newMeasurePage.cqlLibraryName).should('be.enabled');

    cy.get(newMeasurePage.generateCmsIdCheckbox).uncheck();
    cy.get(newMeasurePage.matchLibraryNameCheckbox).should('be.disabled');
    cy.get(newMeasurePage.cqlLibraryName).should('be.enabled');
  });

  it('disables and clears the CQL Library Name text box when the Match CQL Library Name Checkbox is checked', () => {
    cy.get(newMeasurePage.generateCmsIdCheckbox).check();
    cy.get(newMeasurePage.matchLibraryNameCheckbox).should('be.enabled');
    cy.get(newMeasurePage.cqlLibraryName).should('be.enabled');

    cy.get(newMeasurePage.matchLibraryNameCheckbox).check();
    cy.get(newMeasurePage.cqlLibraryName).should('be.disabled');

    cy.get(newMeasurePage.matchLibraryNameCheckbox).uncheck();
    cy.get(newMeasurePage.cqlLibraryName).should('be.enabled');
    cy.get(newMeasurePage.cqlLibraryName).type('ValidLibraryName');
    cy.get(newMeasurePage.cqlLibraryName).blur();
    cy.get(newMeasurePage.cqlLibraryName).should('have.value', 'ValidLibraryName');

    cy.get(newMeasurePage.matchLibraryNameCheckbox).check();
    cy.get(newMeasurePage.cqlLibraryName).should('be.disabled');
    cy.get(newMeasurePage.cqlLibraryName).should('not.have.value');

    cy.get(newMeasurePage.matchLibraryNameCheckbox).uncheck();
    cy.get(newMeasurePage.cqlLibraryName).should('be.enabled');
    cy.get(newMeasurePage.cqlLibraryName).should('not.have.value');
  });

  it('clears the Match CQL Library Name checkbox when Generate ID checkbox is unchecked', () => {
    cy.get(newMeasurePage.generateCmsIdCheckbox).check();
    cy.get(newMeasurePage.matchLibraryNameCheckbox).should('be.enabled');

    cy.get(newMeasurePage.matchLibraryNameCheckbox).check();
    cy.get(newMeasurePage.cqlLibraryName).should('be.disabled');

    cy.get(newMeasurePage.generateCmsIdCheckbox).uncheck();
    cy.get(newMeasurePage.matchLibraryNameCheckbox).should('have.value', 'on');
    cy.get(newMeasurePage.cqlLibraryName).should('be.enabled');
  });

  it('uses defaults on each new measure', () => {
    cy.get(newMeasurePage.generateCmsIdCheckbox).should('be.enabled');
    cy.get(newMeasurePage.matchLibraryNameCheckbox).should('be.disabled');
    cy.get(newMeasurePage.cqlLibraryName).should('be.enabled');

    cy.get(newMeasurePage.generateCmsIdCheckbox).check();
    cy.get(newMeasurePage.matchLibraryNameCheckbox).check();

    cy.get(newMeasurePage.cancelBtn).click();
    cy.wait(1000);
    cy.get(measureLibrary.newMeasureButton).click();
    cy.wait(1000);

    cy.get(newMeasurePage.generateCmsIdCheckbox).should('be.enabled');
    cy.get(newMeasurePage.matchLibraryNameCheckbox).should('be.disabled');
    cy.get(newMeasurePage.cqlLibraryName).should('be.enabled');
  });
});
