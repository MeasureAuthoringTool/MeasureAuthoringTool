import * as dataCreation from '../../support/MAT/MeasureAndCQLLibraryCreation';
import MeasureLibPage from '../../support/domain/pageObjects/MeasureLibPage';
import MeasureComposerPage from "../../support/domain/pageObjects/MeasureComposerPage";
import CqlWorkspaceGeneralInformationPage from "../../support/domain/pageObjects/CqlWorkspaceGeneralInformationPage";
import CqlWorkspaceDefinitionPage from "../../support/domain/pageObjects/CqlWorkspaceDefinitionPage";
import InsertItemCqlEditorDialog from "../../support/domain/pageDialogs/InsertItemCqlEditorDialog";
import InsertOptionsForAttributesDialog from "../../support/domain/pageDialogs/InsertOptionsForAttributesDialog";

describe('Verify CQL Workspace Attributes Added', () => {
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

    it('Find relatedTo Attribute for Diagnostic Study, Performed', () => {
        const measureLibPage = new MeasureLibPage();

        const cqlMeasureName = dataCreation.createDraftMeasure('WorkSpaceDefinitionTest', 'QDM');

        cy.log('Working measureName: ' + cqlMeasureName);

        cy.findAndSelectMeasure(cqlMeasureName, false);

        const measureComposerPage = new MeasureComposerPage();
        measureComposerPage.cqlWorkspaceTab().click();

        const cqlWorkspacePage = new CqlWorkspaceGeneralInformationPage();
        cqlWorkspacePage.definitionLink().click();

        const cqlWorkspaceDefinitionPage = new CqlWorkspaceDefinitionPage();
        cqlWorkspaceDefinitionPage.insertLink().click();

        const insertItemCqlEditorDialog = new InsertItemCqlEditorDialog()
        insertItemCqlEditorDialog.itemToInsertSelect().select('Attributes');

        const insertOptionsForAttributesDialog = new InsertOptionsForAttributesDialog();
        insertOptionsForAttributesDialog.attributesByDataTypeSelect()
            .select('Diagnostic Study, Performed')
            .should('have.value', 'Diagnostic Study, Performed');

        insertOptionsForAttributesDialog.attributesSelect()
            .select('relatedTo')
            .should('have.value', 'relatedTo');
        insertOptionsForAttributesDialog.cancelButton().click();

        measureComposerPage.measureDetailsTab().click();

        measureLibPage.measureLibraryTab().click();

        cy.deleteMeasure(cqlMeasureName, false);
    });
});
