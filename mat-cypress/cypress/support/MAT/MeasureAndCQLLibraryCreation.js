import * as helper from '../helpers';
import * as measureLibraryElements from '../../../elements/MeasureLibraryElements';
import * as newMeasureElements from '../../../elements/CreateNewMeasureElements';
import * as measureComposerElements from '../../../elements/MeasureComposerElements';
import * as cqlLibraryElements from '../../../elements/CqlLibraryElements';
import * as createNewCqlLibrary from '../../../elements/CreateNewCQLLibraryElements';
import * as cqlComposerElements from '../../../elements/CQLComposerElements';
import * as measureDetailsEleemnts from '../../../elements/MeasureDetailsElements';
import * as gridRowActions from './GridRowActions';

const draftMeasure = 'DraftMeasure';

export const createDraftMeasure = (measure, model) => {
    let name = '';

    if (!measure) {
        name = draftMeasure + Date.now();
    } else {
        name = measure + Date.now();
    }

    // creating new measure
    helper.enabledWithTimeout(measureLibraryElements.newMeasureButton);
    cy.get(measureLibraryElements.newMeasureButton).click();

    cy.get(newMeasureElements.measureName).type(name, {delay: 50});

    if (!model || model === 'QDM') {
        cy.get(newMeasureElements.modelradioQDM).click();
    } else {
        cy.get(newMeasureElements.modelradioFHIR).click();
    }

    cy.get(newMeasureElements.cqlLibraryName).type(name, {delay: 50});
    cy.get(newMeasureElements.shortName).type(name, {delay: 50});

    cy.get(newMeasureElements.measureScoringListBox).select('Proportion');
    cy.get(newMeasureElements.patientBasedMeasureListBox).select('Yes');

    cy.get(newMeasureElements.saveAndContinueBtn).click();

    helper.verifySpinnerAppearsAndDissappears();

    cy.get(newMeasureElements.confirmationContinueBtn).click();

    helper.verifySpinnerAppearsAndDissappears();

    cy.get(measureDetailsEleemnts.measureStewardDeveloper).click();
    cy.get(measureDetailsEleemnts.measureStewardListBox).select('SemanticBits');
    cy.get(measureDetailsEleemnts.row1CheckBox).click();
    cy.get(measureDetailsEleemnts.saveBtn).click();
    helper.verifySpinnerAppearsAndDissappears();
    helper.visibleWithTimeout(measureDetailsEleemnts.warningMessage);

    cy.get(measureDetailsEleemnts.description).click();
    helper.enterText(measureDetailsEleemnts.textAreaInput, 'description');
    cy.get(measureDetailsEleemnts.saveBtn).click();
    helper.verifySpinnerAppearsAndDissappears();
    helper.visibleWithTimeout(measureDetailsEleemnts.warningMessage);

    cy.get(measureDetailsEleemnts.measureType).click();
    cy.get(measureDetailsEleemnts.row1CheckBox).click();
    cy.get(measureDetailsEleemnts.saveBtn).click();

    helper.verifySpinnerAppearsAndDissappears();

    helper.visibleWithTimeout(measureDetailsEleemnts.warningMessage);

    cy.get(measureLibraryElements.measureLibraryTab).click();

    helper.verifySpinnerAppearsAndDissappears();

    return name;
};

export const createDraftCqlLibrary = (library, model) => {
    let name = '';

    if (!library) {
        name = 'DraftCqllibrary' + Date.now();
    } else {
        name = library + Date.now();
    }

    cy.get(measureLibraryElements.cqlLibraryTab).then((tab) => {
        const value = tab.attr('class');

        if (value.toString() === 'gwt-TabBarItem') {
            cy.get(measureLibraryElements.cqlLibraryTab).click();
            helper.verifySpinnerAppearsAndDissappears();
        }
    });
    helper.verifySpinnerAppearsAndDissappears();
    helper.verifySpinnerAppearsAndDissappears();
    helper.enabledWithTimeout(cqlLibraryElements.newLibraryBtn);
    cy.get(cqlLibraryElements.newLibraryBtn).click();

    if (!model || model === 'QDM') {

        cy.get(createNewCqlLibrary.modelQDMRadio).click();

    } else {

        cy.get(createNewCqlLibrary.modelFHIRRadio).click();

    }

    cy.get(createNewCqlLibrary.cqlLibraryName).type(name, {delay: 50});

    cy.get(createNewCqlLibrary.saveAndContinueBtn).click();

    helper.verifySpinnerAppearsAndDissappears();
    helper.verifySpinnerAppearsAndDissappears();

    cy.get(cqlComposerElements.confirmationContinueBtn).click();

    helper.verifySpinnerAppearsAndDissappears();

    cy.get(measureLibraryElements.cqlLibraryTab).click();

    helper.verifySpinnerAppearsAndDissappears();

    return name;
};


export const createMajorVersionMeasure = (measure) => {
    let name = '';

    if (!measure) {
        name = createDraftMeasure('MajorVersion');
    } else {
        name = measure;
    }

    cy.get(measureLibraryElements.searchInputBox).type(name, {delay: 50});
    cy.get(measureLibraryElements.searchBtn).click();

    helper.verifySpinnerAppearsAndDissappears();
    helper.verifySpinnerAppearsAndDissappears();

    helper.enabledWithTimeout(measureLibraryElements.searchInputBox);
    helper.visibleWithTimeout(measureLibraryElements.row1MeasureSearch);

    gridRowActions.selectRow(measureLibraryElements.row1MeasureSearch);

    cy.get(measureLibraryElements.createVersionMeasureSearchBtn).click();

    cy.get(measureLibraryElements.majorVersionTypeRadio).click();
    cy.get(measureLibraryElements.packageAndVersion).click();

    helper.verifySpinnerAppearsAndDissappears();
    helper.verifySpinnerAppearsAndDissappears();

    cy.get(measureLibraryElements.continueBtn).click();

    helper.verifySpinnerAppearsAndDissappears();

    cy.get(measureLibraryElements.searchInputBox).clear();

    return name;
};


export const loginCreateVersionedMeasureNotOwnerLogout = () => {
    return createMajorVersionMeasure();
};

export const loginCreateDraftCqlLibraryNotOwnerLogout = () => {
    return createDraftCqlLibrary();
};

export const addDefinition = (definitionName, CQL) => {
    cy.get(measureComposerElements.definition).click();

    helper.waitToContainText(measureComposerElements.cqlWorkspaceTitleGlobal2, 'Definition');

    cy.get(measureComposerElements.addNewBtn).click();
    cy.get(measureComposerElements.definitionNameInput).type(definitionName, {delay: 50});

    helper.visibleWithTimeout(measureComposerElements.definitionCQLExpressionEditorInput);

    cy.wait(1500);

    cy.get(measureComposerElements.definitionCQLExpressionEditorInput).type(CQL, {delay: 50});
    cy.get(measureComposerElements.definitionSaveBtn).click();

    helper.visibleWithTimeout(measureComposerElements.warningMessage);
};


export const addCode = (codeUrl) => {
    cy.get(measureComposerElements.codes).click();

    helper.waitToContainText(measureComposerElements.cqlWorkspaceTitleGlobal, 'Codes');

    helper.visibleWithTimeout(measureComposerElements.codeUrlInput);
    helper.enabledWithTimeout(measureComposerElements.codeUrlInput);
    cy.get(measureComposerElements.codeUrlInput).click();
    cy.get(measureComposerElements.codeUrlInput).type(codeUrl, {delay: 50});
    cy.get(measureComposerElements.retrieveBtn).click();
    helper.verifySpinnerAppearsAndDissappears();
    cy.get(measureComposerElements.applyBtn).click();
    helper.verifySpinnerAppearsAndDissappears();

    helper.visibleWithTimeout(measureComposerElements.warningMessage);
};



export const addValueSet = (OID) => {
    helper.verifySpinnerAppearsAndDissappears();

    cy.get(measureComposerElements.valueSets).click();

    helper.waitToContainText(measureComposerElements.cqlWorkspaceTitleGlobal, 'Value Sets');

    cy.get(measureComposerElements.OIDInput).type(OID, {delay: 50});
    cy.get(measureComposerElements.retrieveOIDBtn).click();

    helper.verifySpinnerAppearsAndDissappears();
    helper.verifySpinnerAppearsAndDissappears();

    helper.waitForElementEnabled(measureComposerElements.applyBtn);
    cy.get(measureComposerElements.applyBtn).click();

    helper.verifySpinnerAppearsAndDissappears();
    helper.verifySpinnerAppearsAndDissappears();

    helper.visibleWithTimeout(measureComposerElements.warningMessage);
};


// create Cohort Fhir measure and package

export const createFhirCohortMeasure = () => {
    cy.get(measureLibraryElements.newMeasureButton).click();
    const measureName = 'CreateFhirCohortMeasure' + Date.now();

    cy.get(newMeasureElements.measureName).type(measureName, {delay: 50});
    cy.get(newMeasureElements.modelradioFHIR).click();
    cy.get(newMeasureElements.cqlLibraryName).type(measureName, {delay: 50});
    cy.get(newMeasureElements.shortName).type(measureName, {delay: 50});
    cy.get(newMeasureElements.measureScoringListBox).select('Cohort');
    cy.get(newMeasureElements.patientBasedMeasureListBox).select('Yes');

    cy.get(newMeasureElements.saveAndContinueBtn).click();

    cy.get(newMeasureElements.confirmationContinueBtn).click();

    helper.verifySpinnerAppearsAndDissappears();

    cy.get(measureComposerElements.cqlWorkspace).click();

    helper.verifySpinnerAppearsAndDissappears();

    helper.waitToContainText(measureComposerElements.cqlWorkspaceTitleGeneralInformation, 'General Information');

    // Includes

    cy.get(measureComposerElements.includes).click();

    cy.get(measureComposerElements.includesListItems).its('length').should('equal', 3);

    cy.get(measureComposerElements.includesListItems).eq(0).should('contain.text', 'FHIRHelpers');
    cy.get(measureComposerElements.includesListItems).eq(1).should('contain.text', 'Global');
    cy.get(measureComposerElements.includesListItems).eq(2).should('contain.text', 'SDE');

    cy.get(measureComposerElements.searchInputBox).type('tjc', {delay: 50});
    cy.get(measureComposerElements.searchBtn).click();
    cy.get(measureComposerElements.availableLibrariesRow1checkbox).click();
    cy.get(measureComposerElements.libraryAliasInputBox).type('TJC', {delay: 50});
    cy.get(measureComposerElements.saveIncludes).click();

    helper.visibleWithTimeout(measureComposerElements.warningMessage);

    // Value Sets

    cy.get(measureComposerElements.valueSets).click();

    helper.verifySpinnerAppearsAndDissappears();

    addValueSet('2.16.840.1.113883.3.666.5.307');
    addValueSet('2.16.840.1.113762.1.4.1182.118');
    addValueSet('2.16.840.1.113762.1.4.1111.161');

    // Codes

    cy.get(measureComposerElements.valueSets).click();

    helper.verifySpinnerAppearsAndDissappears();

    addCode('CODE:/CodeSystem/LOINC/Version/2.46/Code/21112-8/Info');
    addCode('CODE:/CodeSystem/SNOMEDCT/Version/2016-03/Code/419099009/Info');
    addCode('CODE:/CodeSystem/SNOMEDCT/Version/2017-09/Code/371828006/Info');

    // Definition

    cy.get(measureComposerElements.definition).click();

    helper.verifySpinnerAppearsAndDissappears();

    addDefinition('Initial Population', 'TJC."Encounter with Principal Diagnosis and Age"');

    // CQL Library Editor

    cy.get(measureComposerElements.cqlLibraryEditor).click();

    helper.waitToContainText(measureComposerElements.cqlWorkspaceTitleCQLLibraryEditor, 'CQL Library Editor');

    helper.visibleWithTimeout(measureComposerElements.warningMessage);
    helper.waitToContainText(measureComposerElements.warningMessage, 'You are viewing CQL with no validation errors.');

    cy.wait(2000);

    // Population Workspace

    cy.get(measureComposerElements.populationWorkspace).click();

    helper.verifySpinnerAppearsAndDissappears();

    cy.get(measureComposerElements.initialPopulation).click();

    helper.verifySpinnerAppearsAndDissappears();

    cy.get(measureComposerElements.initialPopulationDefinitionListBox).select('Initial Population');
    cy.get(measureComposerElements.initialPopulationSaveBtn).click();

    helper.visibleWithTimeout(measureComposerElements.warningMessage);
    helper.waitToContainText(measureComposerElements.warningMessage, 'Changes to Initial Populations have been successfully saved.');

    // navigate to Measure Packager
    cy.get(measureComposerElements.measurePackager).click();

    helper.verifySpinnerAppearsAndDissappears();

    // verifying the the Population Workspace data is viewable in the Populations list in Measure Packager
    cy.get(measureComposerElements.populationsListItems).its('length').should('equal', 1);

    cy.get(measureComposerElements.populationsListItems).eq(0).should('contain.text', 'Initial Population 1');

    // Package Grouping
    cy.get(measureComposerElements.addAllItemsToGrouping).click();
    cy.get(measureComposerElements.saveGrouping).click();

    cy.get(measureComposerElements.measureGroupingTable).should('contain.text', 'Measure Grouping 1');

    // Create Measure Package
    cy.get(measureComposerElements.createMeasurePackageBtn).click();

    helper.verifySpinnerAppearsAndDissappears();
    helper.verifySpinnerAppearsAndDissappears();
    helper.verifySpinnerAppearsAndDissappears();

    helper.waitToContainText(measureComposerElements.packageWarningMessage, 'Measure packaged successfully. Please access the Measure Library to export the measure.');

    cy.get(measureLibraryElements.measureLibraryTab).click();

    helper.verifySpinnerAppearsAndDissappears();

    return measureName;
};

// create Proportion QDM Draft Measure

export const createQDMProportionMeasure = () => {
    cy.get(measureLibraryElements.newMeasureButton).click();
    const measureName = 'QdmProportionMeasure' + Date.now();

    cy.get(newMeasureElements.measureName).type(measureName, {delay: 50});
    cy.get(newMeasureElements.modelradioQDM).click();
    cy.get(newMeasureElements.cqlLibraryName).type(measureName, {delay: 50});
    cy.get(newMeasureElements.shortName).type(measureName, {delay: 50});
    cy.get(newMeasureElements.measureScoringListBox).select('Proportion');
    cy.get(newMeasureElements.patientBasedMeasureListBox).select('Yes');

    cy.get(newMeasureElements.saveAndContinueBtn).click();

    cy.get(newMeasureElements.confirmationContinueBtn).click();

    helper.verifySpinnerAppearsAndDissappears();

    cy.get(measureComposerElements.cqlWorkspace).click();

    helper.verifySpinnerAppearsAndDissappears();

    helper.waitToContainText(measureComposerElements.cqlWorkspaceTitleGeneralInformation, 'General Information');

    // Includes

    cy.get(measureComposerElements.includes).click();

    helper.waitToContainText(measureComposerElements.cqlWorkspaceTitleIncludes, 'Includes');

    cy.get(measureComposerElements.searchInputBox).type('matglobal', {delay: 50});
    cy.get(measureComposerElements.searchBtn).click();
    cy.get(measureComposerElements.availableLibrariesRow1checkbox).click();
    cy.get(measureComposerElements.libraryAliasInputBox).type('Global', {delay: 50});
    cy.get(measureComposerElements.saveIncludes).click();

    helper.visibleWithTimeout(measureComposerElements.warningMessage);

    // Value Sets

    addValueSet('2.16.840.1.113883.3.117.1.7.1.30');
    addValueSet('2.16.840.1.113883.3.117.1.7.1.27');
    addValueSet('2.16.840.1.113883.3.666.5.307');
    addValueSet('2.16.840.1.114222.4.11.837');
    addValueSet('2.16.840.1.113883.3.117.1.7.1.35');
    addValueSet('2.16.840.1.113762.1.4.1029.205');
    addValueSet('2.16.840.1.113762.1.4.1');
    addValueSet('2.16.840.1.113762.1.4.1029.67');
    addValueSet('2.16.840.1.113883.3.117.1.7.1.38');
    addValueSet('2.16.840.1.114222.4.11.3591');
    addValueSet('2.16.840.1.114222.4.11.836');
    addValueSet('2.16.840.1.113883.3.117.1.7.1.26');

    // Codes

    addCode('CODE:/CodeSystem/CPT/Version/2020/Code/99201/Info');

    // Parameter

    cy.get(measureComposerElements.parameter).click();

    helper.waitToContainText(measureComposerElements.cqlWorkspaceTitleGlobal2, 'Parameter');

    cy.get(measureComposerElements.addNewBtn).click();
    cy.get(measureComposerElements.parameterNameInput).type('test.Parameter', {delay: 50});
    cy.get(measureComposerElements.parameterCQLExpressionEditorInput).type('Interval<DateTime>', {delay: 50});
    cy.get(measureComposerElements.parameterSaveBtn).click();

    helper.visibleWithTimeout(measureComposerElements.warningMessage);

    // Definition

    addDefinition('Initial Population', 'AgeInYearsAt(start of "Measurement Period")> 12');
    addDefinition('Denominator', 'true');
    addDefinition('Numerator', 'true');
    addDefinition('Breast Milk Feeding', '["Substance, Administered": "Breast Milk"] Feeding');
    addDefinition('ED Visit', 'Global."ED Encounter"');

    // Function

    cy.get(measureComposerElements.functionMeasureComposer).click();

    helper.waitToContainText(measureComposerElements.cqlWorkspaceTitleGlobal2, 'Function');

    cy.get(measureComposerElements.addNewBtn).click();
    cy.get(measureComposerElements.functionNameInput).type('CalendarDayOfOrDayAfter', {delay: 50});
    cy.get(measureComposerElements.addArgument).click();
    helper.visibleWithTimeout(measureComposerElements.argumentNameInput);
    helper.enterText(measureComposerElements.argumentNameInput, 'StartValue');
    cy.get(measureComposerElements.availableDatatypesListBox).select('DateTime');
    cy.get(measureComposerElements.addBtn).click();
    cy.get(measureComposerElements.functionCQLExpressionEditorInput).type('Interval[StartValue, ToDate(StartValue + 2 days))', {delay: 50});
    cy.get(measureComposerElements.functionSaveBtn).click();

    helper.visibleWithTimeout(measureComposerElements.warningMessage);

    // CQL Library Editor

    cy.get(measureComposerElements.cqlLibraryEditor).click();

    helper.waitToContainText(measureComposerElements.cqlWorkspaceTitleCQLLibraryEditor, 'CQL Library Editor');

    helper.visibleWithTimeout(measureComposerElements.warningMessage);
    helper.waitToContainText(measureComposerElements.warningMessage, 'You are viewing CQL with no validation errors.');

    cy.wait(2000);

    cy.get(measureComposerElements.populationWorkspace).click();

    helper.verifySpinnerAppearsAndDissappears();

    cy.get(measureComposerElements.initialPopulation).click();

    helper.verifySpinnerAppearsAndDissappears();

    cy.get(measureComposerElements.initialPopulationDefinitionListBox).select('Initial Population');
    cy.get(measureComposerElements.initialPopulationSaveBtn).click();

    helper.visibleWithTimeout(measureComposerElements.warningMessage);
    helper.waitToContainText(measureComposerElements.warningMessage, 'Changes to Initial Populations have been successfully saved.');

    cy.get(measureComposerElements.denominator).click();

    helper.verifySpinnerAppearsAndDissappears();

    cy.get(measureComposerElements.denominatorDefinitionListBox).select('Denominator');
    cy.get(measureComposerElements.denominatorSaveBtn).click();

    helper.visibleWithTimeout(measureComposerElements.warningMessage);
    helper.waitToContainText(measureComposerElements.warningMessage, 'Changes to Denominators have been successfully saved.');

    cy.get(measureComposerElements.numerator).click();

    helper.verifySpinnerAppearsAndDissappears();

    cy.get(measureComposerElements.numeratorDefinitionListBox).select('Numerator');
    cy.get(measureComposerElements.numeratorSaveBtn).click();

    helper.visibleWithTimeout(measureComposerElements.warningMessage);
    helper.waitToContainText(measureComposerElements.warningMessage, 'Changes to Numerators have been successfully saved.');

    // navigate to Measure Packager
    cy.get(measureComposerElements.measurePackager).click();

    helper.verifySpinnerAppearsAndDissappears();

    // verifying the the Population Workspace data is viewable in the Populations list in Measure Packager
    cy.get(measureComposerElements.populationsListItems).its('length').should('equal', 3);

    cy.get(measureComposerElements.populationsListItems).eq(0).should('contain.text', 'Initial Population 1');
    cy.get(measureComposerElements.populationsListItems).eq(1).should('contain.text', 'Denominator 1');
    cy.get(measureComposerElements.populationsListItems).eq(2).should('contain.text', 'Numerator 1');

    // Package Grouping
    cy.get(measureComposerElements.addAllItemsToGrouping).click();
    cy.get(measureComposerElements.saveGrouping).click();

    helper.verifySpinnerAppearsAndDissappears();

    cy.get(measureComposerElements.measureGroupingTable).should('contain.text', 'Measure Grouping 1');

    // Create Measure Package
    cy.get(measureComposerElements.createMeasurePackageBtn).click();

    helper.verifySpinnerAppearsAndDissappears();
    helper.verifySpinnerAppearsAndDissappears();
    helper.verifySpinnerAppearsAndDissappears();

    helper.waitToContainText(measureComposerElements.packageWarningMessage, 'Measure packaged successfully. Please access the Measure Library to export the measure.');

    cy.get(measureLibraryElements.measureLibraryTab).click();

    helper.verifySpinnerAppearsAndDissappears();

    return measureName;
};

// Create Draft Measure


// create FHIR Measure, specifying measure type
export const createFHIRMeasureByType = (measure, type, patientBased) => {
    let name = '';

    if (!measure) {
        name = draftMeasure + Date.now();
    } else {
        name = measure + Date.now();
    }

    // creating new measure
    helper.enabledWithTimeout(measureLibraryElements.newMeasureButton);
    cy.get(measureLibraryElements.newMeasureButton).click();

    cy.get(newMeasureElements.measureName).type(name, {delay: 50});

    cy.get(newMeasureElements.cqlLibraryName).type(name, {delay: 50});
    cy.get(newMeasureElements.shortName).type(name, {delay: 50});

    cy.get(newMeasureElements.measureScoringListBox).select(type);
    cy.get(newMeasureElements.patientBasedMeasureListBox).select(patientBased);

    cy.get(newMeasureElements.saveAndContinueBtn).click();

    helper.verifySpinnerAppearsAndDissappears();
    helper.verifySpinnerAppearsAndDissappears();

    cy.get(newMeasureElements.confirmationContinueBtn).click();

    helper.verifySpinnerAppearsAndDissappears();
    helper.verifySpinnerAppearsAndDissappears();

    cy.get(measureDetailsEleemnts.measureStewardDeveloper).click();
    cy.get(measureDetailsEleemnts.measureStewardListBox).select('SemanticBits');
    cy.get(measureDetailsEleemnts.row1CheckBox).click();
    cy.get(measureDetailsEleemnts.saveBtn).click();
    helper.visibleWithTimeout(measureDetailsEleemnts.warningMessage);

    cy.get(measureDetailsEleemnts.description).click();
    helper.enterText(measureDetailsEleemnts.textAreaInput, 'description');
    cy.get(measureDetailsEleemnts.saveBtn).click();
    helper.visibleWithTimeout(measureDetailsEleemnts.warningMessage);

    cy.get(measureDetailsEleemnts.measureType).click();
    cy.get(measureDetailsEleemnts.row1CheckBox).click();
    cy.get(measureDetailsEleemnts.saveBtn).click();

    helper.verifySpinnerAppearsAndDissappears();

    helper.visibleWithTimeout(measureDetailsEleemnts.warningMessage);

    cy.get(measureLibraryElements.measureLibraryTab).click();

    helper.verifySpinnerAppearsAndDissappears();

    return name;
};





