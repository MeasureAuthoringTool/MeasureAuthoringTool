import * as dataCreation from "../../../support/MAT/MeasureAndCQLLibraryCreation";
import * as helper from '../../../support/helpers';
import * as gridRowActions from '../../../support/MAT/GridRowActions';
import * as cqlLibrary from '../../../../elements/CqlLibraryElements';

let qdmCqlLibraryName = '';

describe('Cql Library: FHIR cqlLibrary Conversion: Conversion to FHIR', () => {
    before(function () {
        cy.fixture('mat').then(function (data) {
            this.data = data;
            cy.log(this.data.password);
            cy.loadCredentials(this.data);
        }).then(function () {
            cy.matLogin(this.data.userName, this.data.password);
            cy.umlsLogin(this.data.umlsApiKey);
        });
    });

    after(() => {
        cy.deleteCqlLibrary(qdmCqlLibraryName);
        cy.matLogout();
    });

    it('Convert QDM CQL Library to FHIR successfully', () => {
        qdmCqlLibraryName = dataCreation.createDraftCqlLibrary('qdmCqlLibrary', 'QDM');
        // Search for created draft QDM lib
        helper.enabledWithTimeout(cqlLibrary.searchInputBox);
        helper.enterText(cqlLibrary.searchInputBox, qdmCqlLibraryName);
        cy.get(cqlLibrary.searchBtn).click();

        helper.verifySpinnerAppearsAndDissappears();

        helper.visibleWithTimeout(cqlLibrary.row1CqlLibrarySearch);

        // assert model version for QDM Library
        cy.get(cqlLibrary.cqlLibrarySearchTable).should('contain.text', 'Model Version');
        cy.get(cqlLibrary.row1CqlLibraryModelVersion).should('contain.text', '5.6');

        // Select the Qdm Cql library created, version it and convert
        cy.createCqlLibraryVersionAndVerify(qdmCqlLibraryName);
        cy.convertCqlLibraryToFHIRAndVerify(qdmCqlLibraryName);

        gridRowActions.selectRow(cqlLibrary.row2CqlLibrarySearch);
        helper.disabledWithTimeout(cqlLibrary.convertToFhirLibrarySearchBtn);

        cy.get(cqlLibrary.editCqllibrariesEnabledBtn).should('be.visible');

        cy.get(cqlLibrary.historyCqllibrariesBtn).click();

        helper.verifySpinnerAppearsAndDissappears();
        helper.visibleWithTimeout(cqlLibrary.historyConvertToFHIRUserActionLogEntry);
        helper.visibleWithTimeout(cqlLibrary.historyCQLLibraryCreatedUserActionLogEntry);

        cy.get(cqlLibrary.returnToCqlLibrary).click();

        helper.verifySpinnerAppearsAndDissappears();

        // Verify to see if reconversion is disabled
        gridRowActions.selectRow(cqlLibrary.row1CqlLibrarySearch);
        helper.disabledWithTimeout(cqlLibrary.convertToFhirLibrarySearchBtn);

        cy.get(cqlLibrary.historyCqllibrariesBtn).click();

        helper.visibleWithTimeout(cqlLibrary.historyConvertToFHIRUserActionLogEntry);

        cy.get(cqlLibrary.returnToCqlLibrary).click();

        helper.verifySpinnerAppearsAndDissappears();

        // Delete converted FHIR library and reconvert
        cy.deleteCqlLibrary(qdmCqlLibraryName + 'FHIR');

        helper.verifySpinnerAppearsAndDissappears();

        gridRowActions.selectRow(cqlLibrary.row1CqlLibrarySearch);
        cy.convertCqlLibraryToFHIRAndVerify(qdmCqlLibraryName);
    });
});
