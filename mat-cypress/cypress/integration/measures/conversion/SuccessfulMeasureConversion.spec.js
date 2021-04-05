import * as helper from '../../../support/helpers';
import * as measureLibrary from '../../../../elements/MeasureLibraryElements';
import * as dataCreation from '../../../support/MAT/MeasureAndCQLLibraryCreation';
import MeasureLibPage from '../../../support/domain/pageObjects/MeasureLibPage';
import CreateMeasureVersionDraftPage from '../../../support/domain/pageObjects/CreateMeasureVersionDraftPage';

describe('Measure Library: FHIR Measure Conversion: Conversion to FHIR', () => {
  beforeEach(function () {
    cy.matLogin(this.data.userName, this.data.password);
    cy.umlsLogin(this.data.umlsApiKey);
  });

  before(function () {
    cy.fixture('mat').then(function (data) {
      this.data = data;
      cy.loadCredentials(this.data);
    });
  });

  afterEach(() => {
    cy.matLogout();
  });

  it('Convert QDM measure to FHIR successfully, verify measure history', () => {
    const measureLibPage = new MeasureLibPage();

    const cqlMeasureName = dataCreation.createDraftMeasure('QdmCqlMeasure', 'QDM');
    const fhirMeasureName = cqlMeasureName + 'FHIR';

    cy.log('Working measureName: ' + cqlMeasureName);
    // cy.spinnerNotVisible()

    measureLibPage.searchInputBox().should('be.enabled');
    // helper.enabledWithTimeout(measureLibrary.searchInputBox)

    helper.enterTextConfirmCypress(measureLibPage.searchInputBox(), cqlMeasureName);

    measureLibPage.searchButton().click();

    cy.spinnerNotVisible();

    // assert model version for QDM Measure
    cy.get(measureLibrary.measureSearchTable).should('contain.text', 'Model Version');
    cy.get(measureLibrary.row1MeasureModelVersion).should('contain.text', '5.5');

    cy.wait(5000);
    measureLibPage.measureSearchResultCheckBox().click({ force: true });
    // cy.get('.GB-MJYKBMI > div').click({force: true})//.should('be.checked')
    cy.wait(5000);

    measureLibPage.createVersionTabButton().click();
    // cy.get(measureLibrary.createVersionMeasureSearchBtn).click()

    const createMeasureVersionDraftPage = new CreateMeasureVersionDraftPage();
    createMeasureVersionDraftPage.majorVersionTypeRadio().click();
    createMeasureVersionDraftPage.packageAndVersionButton().click();
    cy.spinnerNotVisible();
    createMeasureVersionDraftPage.dialogContinueButton().click();

    helper.verifySpinnerAppearsAndDissappears();

    measureLibPage.tr1MeasureSearch().should('be.visible');
    // helper.visibleWithTimeout(measureLibrary.row1MeasureSearch)

    cy.wait(5000);
    measureLibPage.measureSearchResultCheckBox().click({ force: true });
    // cy.get('.GB-MJYKBMI > div > input').click({force: true})
    cy.wait(5000);

    measureLibPage.convertToFhirTabButton().click();

    cy.spinnerNotVisible();

    helper.visibleWithTimeout(measureLibrary.row1MeasureSearch);
    cy.wait(15000);
    measureLibPage.measureSearchCellTableTds().contains(fhirMeasureName);
    measureLibPage.measureSearchCellTableTds().contains('QDM / CQL');
    measureLibPage.measureSearchCellTableTds().contains('FHIR / CQL');

    cy.wait(1000);

    cy.deleteMeasure(cqlMeasureName, false);
    cy.deleteMeasure(fhirMeasureName, true);
  });
});
