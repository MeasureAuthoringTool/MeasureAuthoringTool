import MeasureLibraryPage from '../../../support/domain/pageObjects/MeasureLibraryPage';
import CqlComposerPage from '../../../support/domain/pageObjects/CqlComposerPage';
import CqlLibraryPage from '../../../support/domain/pageObjects/CqlLibraryPage';
import CqlEditorPage from '../../../support/domain/pageObjects/CqlEditorPage';
import CqlLibraryVersionPage from '../../../support/domain/pageObjects/CqlLibraryVersionPage';
import UnusedCqlLibraryDialog from '../../../support/domain/pageDialogs/UnusedCqlLibraryDialog';
import * as dataCreation from '../../../support/MAT/MeasureAndCQLLibraryCreation';


describe('Cql Library: Version with Unused', () => {
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
        const fhirLibraryName = dataCreation.createDraftCqlLibrary('FhirCqlLibrary', 'FHIR');

        const measureLibPage = new MeasureLibraryPage();
        measureLibPage.cqlLibraryTab().click();

        cy.log('Created ' + fhirLibraryName);

        cy.wait(3500);
        cy.get('#div2').dblclick();
        cy.wait(1000);
        const cqlComposerPage = new CqlComposerPage();

        cqlComposerPage.cqlLibraryDescriptionTextArea().clear();
        cy.wait(100);
        cqlComposerPage.cqlLibraryDescriptionTextArea().type("This is a test of removing unused CQL items");
        cqlComposerPage.publisherListBox().select("146");
        cqlComposerPage.saveButton().click();
        cqlComposerPage.cqlLibraryEditorLink().click();
        cy.wait(3500);


        const cqlEditorPage = new CqlEditorPage();
        cqlEditorPage.cqlEditor().type('{meta+a}{del}');
        cy.wait(1000);

        cqlEditorPage.cqlEditor().type(testCql.replace("<lib_name>", fhirLibraryName));
        cy.wait(200);
        cqlEditorPage.cqlEditor().contains("valueset");
        cy.wait(200);

        cqlEditorPage.saveButton().click();

        measureLibPage.cqlLibraryTab().click();
        cy.wait(3500);
        cy.get('[align="left"] > :nth-child(1) > .GB-MJYKBGE > :nth-child(3) > .GB-MJYKBPC > .GB-MJYKBBD > div > input').click();

        const cqlLibraryPage = new CqlLibraryPage();
        cqlLibraryPage.createVersionTab().click();

        const cqlLibraryVersionPage = new CqlLibraryVersionPage();
        cqlLibraryVersionPage.minorVersion().click();
        cqlLibraryVersionPage.saveAndContinueButton().click();

        const unusedCqlLibraryDialog = new UnusedCqlLibraryDialog();
        unusedCqlLibraryDialog.continueButton().click();
        cy.wait(5500);
        cy.get('#div2').dblclick();
        cy.wait(3500);
        cqlComposerPage.cqlLibraryEditorLink().click();
        cy.wait(3500);
        cqlEditorPage.cqlEditor().should('not.have.text', "valueset");
    });

    const testCql =
        "library <lib_name> version '0.0.000'\n" +
        "\n" +
        "using FHIR version '4.0.1'\n" +
        "\n" +
        "include SupplementalDataElementsFHIR4 version '2.0.000' called SDE\n" +
        "codesystem \"LOINC\": 'http://loinc.org'\n" +
        "valueset \"Payer\": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.114222.4.11.3591'\n" +
        "valueset \"Race\": 'http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.114222.4.11.836'\n" +
        "\n" +
        "parameter \"Measurement Period\" Interval<DateTime>\n" +
        "\n" +
        "code \"Birthdate\": '21112-8' from \"LOINC\" display 'Birth date'\n" +
        "context Patient\n" +
        "\n" +
        "define \"SDE Ethnicity\":\n" +
        "  SDE.\"SDE Ethnicity\"\n" +
        "\n" +
        "\n" +
        "define \"SDE Payer\":\n" +
        "  SDE.\"SDE Payer\"\\n\n" +
        "\n" +
        "define \"SDE Race\":\n" +
        "  SDE.\"SDE Race\"\n" +
        "\n" +
        "define \"SDE Sex\":\n" +
        "  SDE.\"SDE Sex\"\\n\n" +
        "\n" +
        "define \"Initial Population\":\n" +
        "  true\n" +
        "\n" +
        "define function \"ToString\"(value string ):\n" +
        "  value.value +  SDE.\"SDE Race\"";

});
