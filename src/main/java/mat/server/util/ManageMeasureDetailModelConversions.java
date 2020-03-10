package mat.server.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import mat.client.measure.ManageCompositeMeasureDetailModel;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.measure.NqfModel;
import mat.client.measure.PeriodModel;
import mat.client.measure.ReferenceTextAndType;
import mat.dao.MeasureTypeDAO;
import mat.dao.OrganizationDAO;
import mat.model.Author;
import mat.model.MeasureType;
import mat.model.Organization;
import mat.model.clause.ComponentMeasure;
import mat.model.clause.Measure;
import mat.model.clause.MeasureDetails;
import mat.model.clause.MeasureDetailsReference;
import mat.model.clause.MeasureDeveloperAssociation;
import mat.model.clause.MeasureTypeAssociation;
import mat.shared.ConstantMessages;
import mat.shared.DateUtility;
import mat.shared.model.util.MeasureDetailsUtil;

public class ManageMeasureDetailModelConversions {

    private static final String NQF_ENDORSEMENT_ID = "2.16.840.1.113883.3.560";
    private static final String NATIONAL_QUALITY_FORUM = "National Quality Forum";
    private static final String NQF_ID_ROOT = "2.16.840.1.113883.3.560.1";

    public ManageMeasureDetailModelConversions() {
    }

    public void createMeasureDetails(Measure clonedMeasure, ManageMeasureDetailModel currentDetails) {
        MeasureDetails measureDetails = new MeasureDetails();
        measureDetails.setMeasure(clonedMeasure);
        measureDetails.setDescription(currentDetails.getDescription());
        measureDetails.setCopyright(currentDetails.getCopyright());
        measureDetails.setDisclaimer(currentDetails.getDisclaimer());
        measureDetails.setStratification(currentDetails.getStratification());
        measureDetails.setRiskAdjustment(currentDetails.getRiskAdjustment());
        measureDetails.setRateAggregation(currentDetails.getRateAggregation());
        measureDetails.setRationale(currentDetails.getRationale());
        measureDetails.setClinicalRecommendation(currentDetails.getClinicalRecomms());
        measureDetails.setImprovementNotation(currentDetails.getImprovNotations());
        measureDetails.setDefinition(currentDetails.getDefinitions());
        measureDetails.setGuidance(currentDetails.getGuidance());
        measureDetails.setTransmissionFormat(currentDetails.getTransmissionFormat());
        measureDetails.setInitialPopulation(currentDetails.getInitialPop());
        measureDetails.setDenominator(currentDetails.getDenominator());
        measureDetails.setDenominatorExclusions(currentDetails.getDenominatorExclusions());
        measureDetails.setNumerator(currentDetails.getNumerator());
        measureDetails.setNumeratorExclusions(currentDetails.getNumeratorExclusions());
        measureDetails.setMeasureObservations(currentDetails.getMeasureObservations());
        measureDetails.setMeasurePopulation(currentDetails.getMeasurePopulation());
        measureDetails.setMeasurePopulationExclusions(currentDetails.getMeasurePopulationExclusions());
        measureDetails.setDenominatorExceptions(currentDetails.getDenominatorExceptions());
        measureDetails.setSupplementalDataElements(currentDetails.getSupplementalData());
        measureDetails.setMeasureSet(currentDetails.getGroupName());
        List<MeasureDetailsReference> references = new ArrayList<>();
        if (currentDetails.getReferencesList() != null) {
            for (int i = 0; i < currentDetails.getReferencesList().size(); i++) {
                ReferenceTextAndType referenceTextAndType = currentDetails.getReferencesList().get(i);
                MeasureDetailsReference reference = new MeasureDetailsReference(measureDetails, referenceTextAndType.getReferenceText(), referenceTextAndType.getReferenceType(), i);
                references.add(reference);
            }
        }
        measureDetails.setMeasureDetailsReference(references);

        clonedMeasure.setMeasureDetails(measureDetails);
    }


    public ManageCompositeMeasureDetailModel createManageCompositeMeasureDetailModel(Measure measure,
                                                                                     OrganizationDAO organizationDao, MeasureTypeDAO measureTypeDao) {
        ManageCompositeMeasureDetailModel compositeMeasureDetailModel = new ManageCompositeMeasureDetailModel();
        createMeasureDetailModel(measure, organizationDao, measureTypeDao, compositeMeasureDetailModel);

        compositeMeasureDetailModel.setCompositeScoringMethod(measure.getCompositeScoring());
        compositeMeasureDetailModel.setCompositeScoringAbbreviation(MeasureDetailsUtil.getCompositeScoringAbbreviation(measure.getCompositeScoring()));
        List<Result> resultObjectForComponentMeasures = createResultObjectForComponentMeasures(measure.getComponentMeasures());
        compositeMeasureDetailModel.setComponentMeasuresSelectedList(resultObjectForComponentMeasures);
        compositeMeasureDetailModel.setAppliedComponentMeasures(resultObjectForComponentMeasures);

        compositeMeasureDetailModel.setAliasMapping(createAliasMapping(measure));

        return compositeMeasureDetailModel;
    }


    private Map<String, String> createAliasMapping(Measure measure) {
        Map<String, String> aliasMap = new HashMap<>();
        measure.getComponentMeasures().stream().forEach(component -> aliasMap.put(component.getComponentMeasure().getId(), component.getAlias()));
        return aliasMap;
    }

    private List<Result> createResultObjectForComponentMeasures(List<ComponentMeasure> componentMeasures) {
        List<Result> measureResults = new ArrayList<>();
        measureResults.addAll(componentMeasures.stream().map(measure -> createResultObject(measure)).collect(Collectors.toList()));
        return measureResults;
    }

    private Result createResultObject(ComponentMeasure componentMeasure) {
        Result result = new Result();
        Measure measure = componentMeasure.getComponentMeasure();
        result.setId(String.valueOf(measure.getId()));
        result.setName(measure.getDescription());
        result.setMeasureModel(measure.getMeasureModel());
        result.setScoringType(measure.getMeasureScoring());
        result.setShortName(measure.getaBBRName());
        result.setVersion(MeasureUtility.formatVersionText("000", measure.getVersion()));
        result.setFinalizedDate(measure.getFinalizedDate());
        result.setDraft(measure.isDraft());
        result.setMeasureSetId(measure.getMeasureSet().getId());
        result.setOwnerfirstName(measure.getOwner().getFirstName());
        result.setOwnerLastName(measure.getOwner().getLastName());
        result.setOwnerEmailAddress(measure.getOwner().getEmailAddress());
        result.seteMeasureId(measure.geteMeasureId());
        result.setPatientBased(measure.getPatientBased());
        result.setQdmVersion(measure.getQdmVersion());
        result.setIsComposite(measure.getIsCompositeMeasure());
        return result;

    }

    public ManageMeasureDetailModel createManageMeasureDetailModel(Measure measure, OrganizationDAO organizationDao, MeasureTypeDAO measureTypeDao) {
        ManageMeasureDetailModel measureDetailModel = new ManageMeasureDetailModel();
        createMeasureDetailModel(measure, organizationDao, measureTypeDao, measureDetailModel);
        return measureDetailModel;
    }

    private String getSimpleDateFormat(Timestamp date) {
        if (date != null) {
            return new SimpleDateFormat("MM/dd/yyyy").format(date);
        }
        return null;
    }

    private void createMeasureDetailModel(Measure measure, OrganizationDAO organizationDao,
                                          MeasureTypeDAO measureTypeDao, ManageMeasureDetailModel measureDetailModel) {
        measureDetailModel.setId(measure.getId());
        measureDetailModel.setMeasureName(measure.getDescription());
        measureDetailModel.setMeasureModel(measure.getMeasureModel());
        measureDetailModel.setCQLLibraryName(measure.getCqlLibraryName());
        measureDetailModel.setShortName(measure.getaBBRName());
        measureDetailModel.setVersionNumber(measure.getVersion());
        measureDetailModel.setRevisionNumber(measure.getRevisionNumber());
        measureDetailModel.setFormattedVersion(MeasureUtility.getVersionTextWithRevisionNumber(measure.getVersion(), measure.getRevisionNumber(), measure.isDraft()));
        measureDetailModel.setGroupId(measure.getMeasureSet().getId());
        measureDetailModel.setFinalizedDate(DateUtility.convertDateToString(measure.getFinalizedDate()));
        measureDetailModel.setMeasScoring(measure.getMeasureScoring());
        measureDetailModel.setNqfModel(createNQFModel(measure));
        measureDetailModel.setNqfId(measure.getNqfNumber());
        measureDetailModel.setPeriodModel(createPeriodModel(measure));
        if (measure.getMeasurementPeriodFrom() != null && measure.getMeasurementPeriodTo() != null) {
            measureDetailModel.setMeasFromPeriod(getSimpleDateFormat(measure.getMeasurementPeriodFrom()));
            measureDetailModel.setMeasToPeriod(getSimpleDateFormat(measure.getMeasurementPeriodTo()));
        }
        measureDetailModel.setMeasureSetId(measure.getMeasureSet().getId());
        measureDetailModel.setDraft(measure.isDraft());
        measureDetailModel.setValueSetDate(String.valueOf(measure.getValueSetDate()));

        if ("null".equals(measureDetailModel.getValueSetDate())) {
            measureDetailModel.setValueSetDate(null);
        }

        measureDetailModel.seteMeasureId(measure.geteMeasureId());
        measureDetailModel.setMeasureOwnerId(measure.getOwner().getId());
        createMeasureSteward(measure, measureDetailModel, organizationDao);
        measureDetailModel.setAuthorSelectedList(getAuthorsSelectedList(measure.getMeasureDevelopers(), organizationDao));
        measureDetailModel.setMeasureTypeSelectedList(getMeasureTypeSelectedList(measure.getMeasureTypes(), measureTypeDao));
        measureDetailModel.setScoringAbbr(MeasureDetailsUtil.getScoringAbbr(measure.getMeasureScoring()));
        measureDetailModel.setOrgVersionNumber(MeasureUtility.formatVersionText(measure.getRevisionNumber(), measure.getVersion()));
        measureDetailModel.setCalenderYear(measure.getMeasurementPeriodFrom() == null);
        measureDetailModel.setIsPatientBased(measure.getPatientBased() == null ? calculateDefaultPatientBasedIndicatorBasedOnScoringType(measure.getMeasureScoring()) : measure.getPatientBased());

        boolean endorseByNQF = measure.getNqfNumber() != null;
        measureDetailModel.setEndorseByNQF(endorseByNQF);
        if (endorseByNQF) {
            measureDetailModel.setEndorsement(NATIONAL_QUALITY_FORUM);
            measureDetailModel.setEndorsementId(NQF_ENDORSEMENT_ID);
        }
        MeasureDetails measureDetails = measure.getMeasureDetails();
        if (measureDetails != null) {
            measureDetailModel.setGroupName(measureDetails.getMeasureSet());
            measureDetailModel.setDescription(measureDetails.getDescription());
            measureDetailModel.setCopyright(measureDetails.getCopyright());
            measureDetailModel.setClinicalRecomms(measureDetails.getClinicalRecommendation());
            measureDetailModel.setDefinitions(measureDetails.getDefinition());
            measureDetailModel.setGuidance(measureDetails.getGuidance());
            measureDetailModel.setTransmissionFormat(measureDetails.getTransmissionFormat());
            measureDetailModel.setRationale(measureDetails.getRationale());
            measureDetailModel.setImprovNotations(measureDetails.getImprovementNotation());
            measureDetailModel.setStratification(measureDetails.getStratification());
            measureDetailModel.setDisclaimer(measureDetails.getDisclaimer());
            measureDetailModel.setRiskAdjustment(measureDetails.getRiskAdjustment());
            measureDetailModel.setDisclaimer(measureDetails.getDisclaimer());
            measureDetailModel.setRateAggregation(measureDetails.getRateAggregation());
            measureDetailModel.setInitialPop(measureDetails.getInitialPopulation());
            measureDetailModel.setDenominator(measureDetails.getDenominator());
            measureDetailModel.setDenominatorExclusions(measureDetails.getDenominatorExclusions());
            measureDetailModel.setNumerator(measureDetails.getNumerator());
            measureDetailModel.setNumeratorExclusions(measureDetails.getNumeratorExclusions());
            measureDetailModel.setDenominatorExceptions(measureDetails.getDenominatorExceptions());
            measureDetailModel.setMeasurePopulation(measureDetails.getMeasurePopulation());
            measureDetailModel.setMeasureObservations(measureDetails.getMeasureObservations());
            measureDetailModel.setMeasurePopulationExclusions(measureDetails.getMeasurePopulationExclusions());
            measureDetailModel.setReferencesList(createReferenceList(measureDetails.getMeasureDetailsReference()));
            measureDetailModel.setSupplementalData(measureDetails.getSupplementalDataElements());
        }
    }

    private boolean calculateDefaultPatientBasedIndicatorBasedOnScoringType(String measureScoring) {
        return !StringUtils.equals(measureScoring, ConstantMessages.CONTINUOUS_VARIABLE_SCORING);
    }

    private List<ReferenceTextAndType> createReferenceList(List<MeasureDetailsReference> measureDetailsReference) {
        return measureDetailsReference.stream().map(reference -> new ReferenceTextAndType(reference.getReference(), reference.getReferenceType())).collect(Collectors.toList());
    }

    private void createMeasureSteward(Measure measure, ManageMeasureDetailModel measureDetailModel, OrganizationDAO organizationDao) {
        if (measure.getMeasureStewardId() != null) {
            Organization stewardOrg = organizationDao.findById(measure.getMeasureStewardId());
            measureDetailModel.setStewardId(stewardOrg.getId().toString());
            measureDetailModel.setStewardValue(stewardOrg.getOrganizationName());
        }
    }

    private NqfModel createNQFModel(Measure measure) {
        NqfModel nqfModel = new NqfModel();
        nqfModel.setRoot(NQF_ID_ROOT);
        nqfModel.setExtension(measure.getNqfNumber());
        return nqfModel;
    }

    private PeriodModel createPeriodModel(Measure measure) {
        PeriodModel periodModel = new PeriodModel();
        periodModel.setCalenderYear(measure.getMeasurementPeriodFrom() == null);
        periodModel.setStartDate(measure.getMeasurementPeriodFrom() == null ? "01/01/20xx" : getSimpleDateFormat(measure.getMeasurementPeriodFrom()));
        periodModel.setStopDate(measure.getMeasurementPeriodTo() == null ? "12/31/20xx" : getSimpleDateFormat(measure.getMeasurementPeriodTo()));
        periodModel.setUuid(UUID.randomUUID().toString());
        return periodModel;
    }

    private List<MeasureType> getMeasureTypeSelectedList(List<MeasureTypeAssociation> measureTypesAssociation, MeasureTypeDAO measureTypeDAO) {
        List<MeasureType> measureTypes = new ArrayList<>();
        for (int i = 0; i < measureTypesAssociation.size(); i++) {
            String measureTypeId = measureTypesAssociation.get(i).getMeasureTypes().getId();
            MeasureType measureType = measureTypeDAO.find(measureTypeId);
            measureTypes.add(new MeasureType(measureType.getId(), measureType.getDescription(), measureType.getAbbrName()));
        }
        return measureTypes;
    }

    private List<Author> getAuthorsSelectedList(List<MeasureDeveloperAssociation> measureDevelopers, OrganizationDAO organizationDAO) {
        return measureDevelopers.stream().map(developerAssociation -> getAuthor(developerAssociation.getOrganization().getId(), organizationDAO)).collect(Collectors.toList());
    }

    private Author getAuthor(Long orgId, OrganizationDAO organizationDAO) {
        Organization org = organizationDAO.find(orgId);
        return new Author(org.getOrganizationOID(), org.getOrganizationName(), org.getOrganizationOID());
    }

}
