package mat.server.validator.measure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.measure.ManageCompositeMeasureDetailModel;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.measurepackage.MeasurePackageOverview;
import mat.dao.clause.CQLLibraryDAO;
import mat.model.clause.CQLLibrary;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLModel;
import mat.server.CQLLibraryService;
import mat.server.CQLUtilityClass;
import mat.server.service.MeasureLibraryService;
import mat.server.service.MeasurePackageService;
import mat.server.util.CQLValidationUtil;
import mat.server.util.MATPropertiesService;
import mat.server.util.XmlProcessor;
import mat.shared.CQLModelValidator;
import mat.shared.CompositeMeasureValidationResult;

@Component
public class CompositeMeasureValidator {

    private static final Log LOG = LogFactory.getLog(CompositeMeasureValidator.class);

    private static final String ERR_MORE_THAN_ONE_COMPONENT_MEASURE_REQUIRED = "A composite measure must have more than one component measure.";
    private static final String ERR_COMPONENT_MEASURE_DOES_NOT_CONTAIN_PACKAGE = " does not have a measure package and can not be used as a component measure.";
    private static final String ERR_COMPONENT_MEASURES_MUST_HAVE_SAME_PATIENT_BASED_INDICATOR = "All component measures must have the same patient-based indicator setting.";
    private static final String ERR_COMPONENT_MEASURES_MUST_ALL_HAVE_ALIAS = "An alias is required for each component measure.";
    private static final String ERR_COMPONENT_MEASURE_CANNOT_BE_COMPOSITE = "A component measure can not be a composite measure.";
    private static final String ERR_COMPONENT_MEASURE_HAS_MORE_THAN_ONE_PACKAGE_GROUPING = " has more than one measure grouping and can not be used as a component measure. ";
    private static final String ERR_ALIAS_NOT_VALID = " cannot be saved. An alias for a component measure must start with an alpha-character or underscore followed by an alpha-numeric character(s) or underscore(s), must not contain spaces, and it must be unique. Please correct and try again.";
    private static final String ERR_LIBRARIES_MUST_HAVE_SAME_VERSION_AND_CONTENT = "CQL libraries within a composite measure can not have the same name with different library content or version.";
    private static final String ERR_MORE_THAN_ONE_COMPONENT_MEASURE_REQUIRED_ON_PACKAGING = "Expressions must be used from two or more component measures.";
    private static final String ERR_UNIQUE_ALIAS_NAME_REQUIRED = "Invalid component measure alias. All aliases must be unique, start with an alpha character or underscore, followed by alpha-numeric character(s) or underscore(s), and must not contain spaces.";

    @Autowired
    private MeasureLibraryService measureLibraryService;
    @Autowired
    private MeasurePackageService measurePackageService;
    @Autowired
    private CQLLibraryService cqlLibraryService;
    @Autowired
    private CQLLibraryDAO cqlLibraryDAO;

    javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();

    private boolean isPackage = false;

    private HashSet<String> aliasNameSet;

    public CompositeMeasureValidationResult validateCompositeMeasureOnPackage(ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel) {
        isPackage = true;
        CompositeMeasureValidationResult result = validateCompositeMeasure(manageCompositeMeasureDetailModel);
        isPackage = false; // reset the package flag since this is a component and needs to be reset after this call.
        return result;
    }

    public CompositeMeasureValidationResult validateCompositeMeasure(ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel) {
        manageCompositeMeasureDetailModel = measureLibraryService.buildCompositeMeasure(manageCompositeMeasureDetailModel);
        CompositeMeasureValidationResult validationResult = new CompositeMeasureValidationResult();
        List<String> messages = new ArrayList<>();
        if (!compositeMeasureContainsMoreThanOneComponentMeasure(manageCompositeMeasureDetailModel)) {
            if (isPackage) {
                messages.add(ERR_MORE_THAN_ONE_COMPONENT_MEASURE_REQUIRED_ON_PACKAGING);
            } else {
                messages.add(ERR_MORE_THAN_ONE_COMPONENT_MEASURE_REQUIRED);
            }
        }

        List<String> packageErrors = validateMeasuresContainAPackage(manageCompositeMeasureDetailModel);
        messages.addAll(packageErrors);

        aliasNameSet = new HashSet<>();
        if (!includedLibrariesAllHaveTheSameVersionAndContent(manageCompositeMeasureDetailModel)) {
            messages.add(ERR_LIBRARIES_MUST_HAVE_SAME_VERSION_AND_CONTENT);
        }

        if (isExistingAliasName(manageCompositeMeasureDetailModel.getAliasMapping())) {
            messages.add(ERR_UNIQUE_ALIAS_NAME_REQUIRED);
        }

        List<String> qdmErrors = validateMeasuresAllUseTheCorrectQDMVersion(manageCompositeMeasureDetailModel);
        messages.addAll(qdmErrors);

        if (!allMeasuresHaveTheSamePatientBasedIndicator(manageCompositeMeasureDetailModel)) {
            messages.add(ERR_COMPONENT_MEASURES_MUST_HAVE_SAME_PATIENT_BASED_INDICATOR);
        }

        if (!allComponentMeasuresHaveAnAlias(manageCompositeMeasureDetailModel)) {
            messages.add(ERR_COMPONENT_MEASURES_MUST_ALL_HAVE_ALIAS);
        }

        List<String> aliasValidationErrors = validateAliasIsValidAndNotDuplicate(manageCompositeMeasureDetailModel);
        messages.addAll(aliasValidationErrors);

        if (anyComponentMeasureIsACompositeMeasure(manageCompositeMeasureDetailModel)) {
            messages.add(ERR_COMPONENT_MEASURE_CANNOT_BE_COMPOSITE);
        }

        List<String> scoringTypeErrors = validateComponentMeasureScoringType(manageCompositeMeasureDetailModel);
        messages.addAll(scoringTypeErrors);

        List<String> packageGroupingErrors = validateComponentMeasuresHaveOnePackageGrouping(manageCompositeMeasureDetailModel);
        messages.addAll(packageGroupingErrors);

        validationResult.setMessages(messages);
        validationResult.setModel(manageCompositeMeasureDetailModel);
        return validationResult;
    }

    private boolean isExistingAliasName(Map<String, String> aliasMapping) {
        for (Map.Entry<String, String> entry : aliasMapping.entrySet()) {
            if (!aliasNameSet.add(entry.getValue()))
                return true;
        }
        return false;
    }

    private boolean compositeMeasureContainsMoreThanOneComponentMeasure(ManageCompositeMeasureDetailModel model) {
        return model.getAppliedComponentMeasures().size() > 1;
    }

    private List<String> validateMeasuresContainAPackage(ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel) {
        List<String> messages = new ArrayList<>();
        for (ManageMeasureSearchModel.Result appliedComponentMeasure : manageCompositeMeasureDetailModel.getAppliedComponentMeasures()) {
            if (!appliedComponentMeasureContainsAPackage(appliedComponentMeasure, manageCompositeMeasureDetailModel)) {
                messages.add(appliedComponentMeasure.getName() + ERR_COMPONENT_MEASURE_DOES_NOT_CONTAIN_PACKAGE);
            }
        }
        return messages;
    }

    private boolean includedLibrariesAllHaveTheSameVersionAndContent(ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel) {
        Map<String, String> nameToVersionMap = new HashMap<>();
        Map<String, String> nameVersionToIdMap = new HashMap<>();

        if (manageCompositeMeasureDetailModel.getId() != null) {
            MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(manageCompositeMeasureDetailModel.getId());
            String compositeXML = xmlModel.getXml();
            CQLModel compositeCQLModel = CQLUtilityClass.getCQLModelFromXML(compositeXML);
            nameToVersionMap.put(compositeCQLModel.getLibraryName(), "0.0.000");
            boolean hasLibrariesThatAlreadyExistInAlreadyAppliedComponents = hasLibraryThatAlreadyExists(true, compositeCQLModel.getCqlIncludeLibrarys(), nameToVersionMap, nameVersionToIdMap);
            if (!hasLibrariesThatAlreadyExistInAlreadyAppliedComponents) {
                return false;
            }
        }

        List<Result> appliedComponents = manageCompositeMeasureDetailModel.getAppliedComponentMeasures();
        for (Result newlyAppliedComponent : appliedComponents) {
            MeasureXmlModel newlyAppliedComponenetXMLModel = measurePackageService.getMeasureXmlForMeasure(newlyAppliedComponent.getId());
            String newlyAppliedComponenetXML = newlyAppliedComponenetXMLModel.getXml();
            CQLModel newlyAppliedComponentCQLModel = CQLUtilityClass.getCQLModelFromXML(newlyAppliedComponenetXML);

            CQLLibrary library = cqlLibraryDAO.getLibraryByMeasureId(newlyAppliedComponent.getId());
            String componenetLibraryName = library.getName();
            String componentLibraryVersion = library.getVersion();
            String cqlLibraryId = library.getId();

            // check if there is a name with a different version in the map
            if (nameToVersionMap.get(componenetLibraryName) != null && !nameToVersionMap.get(componenetLibraryName).equals(componentLibraryVersion)) {
                return false;
            }

            String formattedName = componenetLibraryName + "-" + componentLibraryVersion;
            if (nameVersionToIdMap.get(formattedName) != null && !nameVersionToIdMap.get(formattedName).equals(cqlLibraryId)) {
                return false;
            }

            nameToVersionMap.put(componenetLibraryName, componentLibraryVersion);
            nameVersionToIdMap.put(formattedName, cqlLibraryId);

            if (!hasLibraryThatAlreadyExists(false, newlyAppliedComponentCQLModel.getCqlIncludeLibrarys(), nameToVersionMap, nameVersionToIdMap)) {
                return false;
            }
        }

        return true;
    }

    private boolean hasLibraryThatAlreadyExists(boolean isParent, List<CQLIncludeLibrary> libraries, Map<String, String> nameToVersionMap, Map<String, String> nameVersionToIdMap) {
        for (CQLIncludeLibrary includedLibrary : libraries) {
            // only run this test if it is not a composite measure. Composite measures are tested by checking the applied composite measures previous to this call.
            if (!BooleanUtils.toBoolean(includedLibrary.getIsComponent())) {
                CQLLibrary libraryModel = cqlLibraryDAO.find(includedLibrary.getCqlLibraryId());
                String libraryName = libraryModel.getName();
                String libraryVersion = includedLibrary.getVersion();
                String libraryId = libraryModel.getId();

                if (isParent) {
                    aliasNameSet.add(includedLibrary.getAliasName());
                }

                // check if there is a name with a different version in the map
                if (nameToVersionMap.get(libraryName) != null && !nameToVersionMap.get(libraryName).equals(libraryVersion)) {
                    return false;
                }

                // check if there is a name-version combination with a different id in the map
                String formattedName = libraryName + "-" + libraryVersion;
                if (nameVersionToIdMap.get(formattedName) != null && !nameVersionToIdMap.get(formattedName).equals(libraryId)) {
                    return false;
                }

                nameToVersionMap.put(libraryName, libraryVersion);
                nameVersionToIdMap.put(formattedName, libraryId);

                CQLModel includedLibraryModel = CQLUtilityClass.getCQLModelFromXML(new String(libraryModel.getCQLByteArray()));
                hasLibraryThatAlreadyExists(isParent, includedLibraryModel.getCqlIncludeLibrarys(), nameToVersionMap, nameVersionToIdMap);
            }
        }

        return true;
    }

    private List<String> validateMeasuresAllUseTheCorrectQDMVersion(ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel) {
        List<String> messages = new ArrayList<>();
        if (!manageCompositeMeasureDetailModel.getQdmVersion().equals(MATPropertiesService.get().getQdmVersion())) {
            messages.add("The measure " + manageCompositeMeasureDetailModel.getMeasureName() + " is not using the correct version of the QDM");
        } else {
            for (ManageMeasureSearchModel.Result appliedComponentMeasure : manageCompositeMeasureDetailModel.getAppliedComponentMeasures()) {
                if (!appliedComponentMeasure.getQdmVersion().equals(MATPropertiesService.get().getQdmVersion())) {
                    messages.add("The measure " + appliedComponentMeasure.getName() + " is not using the correct version of the QDM");
                }
            }
        }
        return messages;
    }

    private boolean allMeasuresHaveTheSamePatientBasedIndicator(ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel) {
        Set<Boolean> patientBasedSet = new HashSet<>();
        for (ManageMeasureSearchModel.Result appliedComponentMeasure : manageCompositeMeasureDetailModel.getAppliedComponentMeasures()) {
            patientBasedSet.add(appliedComponentMeasure.isPatientBased());
        }

        if (patientBasedSet.size() > 1) {
            return false;
        }
        return true;
    }

    private boolean allComponentMeasuresHaveAnAlias(ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel) {
        for (ManageMeasureSearchModel.Result appliedComponentMeasure : manageCompositeMeasureDetailModel.getAppliedComponentMeasures()) {
            Map<String, String> aliasMapping = manageCompositeMeasureDetailModel.getAliasMapping();
            if (!aliasMapping.containsKey(appliedComponentMeasure.getId()) || StringUtils.isEmpty(aliasMapping.get(appliedComponentMeasure.getId()))) {
                return false;
            }
        }
        return true;
    }

    private List<String> validateAliasIsValidAndNotDuplicate(ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel) {
        List<String> messages = new ArrayList<>();
        String templateXml = getTemplateXmlString(manageCompositeMeasureDetailModel.getMeasureModel());
        CQLModel model = CQLUtilityClass.getCQLModelFromXML(templateXml);
        Map<String, String> aliasMapping = manageCompositeMeasureDetailModel.getAliasMapping();

        for (ManageMeasureSearchModel.Result appliedComponentMeasure : manageCompositeMeasureDetailModel.getAppliedComponentMeasures()) {
            if (aliasMapping.containsKey(appliedComponentMeasure.getId())) {
                String aliasName = aliasMapping.get(appliedComponentMeasure.getId());
                if (Collections.frequency(aliasMapping.values(), aliasName) > 1) {
                    messages.add("Alias " + aliasName + ERR_ALIAS_NOT_VALID);
                } else {
                    CQLModelValidator modelValidator = new CQLModelValidator();
                    if (!modelValidator.doesAliasNameFollowCQLAliasNamingConvention(aliasName) || CQLValidationUtil.isDuplicateIdentifierName(aliasName, model) || CQLValidationUtil.isCQLReservedWord(aliasName)) {
                        messages.add("Alias " + aliasName + ERR_ALIAS_NOT_VALID);
                    }
                }
            }
        }
        return messages;
    }

    private boolean anyComponentMeasureIsACompositeMeasure(ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel) {
        for (ManageMeasureSearchModel.Result appliedComponentMeasure : manageCompositeMeasureDetailModel.getAppliedComponentMeasures()) {
            if (appliedComponentMeasure.getIsComposite()) {
                return true;
            }
        }
        return false;
    }

    private List<String> validateComponentMeasureScoringType(ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel) {
        List<String> messages = new ArrayList<>();
        String scoringType = manageCompositeMeasureDetailModel.getMeasScoring();
        for (ManageMeasureSearchModel.Result appliedComponentMeasure : manageCompositeMeasureDetailModel.getAppliedComponentMeasures()) {
            switch (scoringType) {
                case "Proportion":
                    if (!appliedComponentMeasure.getScoringType().equals("Proportion") && !appliedComponentMeasure.getScoringType().equals("Ratio")) {
                        messages.add("Component measure " + appliedComponentMeasure.getName() + " can not be saved. A proportion composite measure can only contain component measures that have a measure scoring of proportion or ratio.");
                    }
                    break;
                case "Ratio":
                    if (!appliedComponentMeasure.getScoringType().equals("Proportion") && !appliedComponentMeasure.getScoringType().equals("Ratio")) {
                        messages.add("Component measure " + appliedComponentMeasure.getName() + " can not be saved. A ratio composite measure can only contain component measures that have a measure scoring of proportion or ratio.");
                    }
                    break;
                case "Continuous Variable":
                    if (!appliedComponentMeasure.getScoringType().equals("Proportion") && !appliedComponentMeasure.getScoringType().equals("Ratio") && !appliedComponentMeasure.getScoringType().equals("Continuous Variable")) {
                        messages.add("Component measure " + appliedComponentMeasure.getName() + " can not be saved. A continuous variable composite measure can only contain component measures that have a measure scoring of proportion, ratio, or continuous variable.");
                    }
                    break;
            }
        }
        return messages;
    }

    private List<String> validateComponentMeasuresHaveOnePackageGrouping(ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel) {
        List<String> messages = new ArrayList<>();
        for (ManageMeasureSearchModel.Result appliedComponentMeasure : manageCompositeMeasureDetailModel.getAppliedComponentMeasures()) {
            ManageMeasureDetailModel appliedComponentModel = measureLibraryService.getMeasure(appliedComponentMeasure.getId());
            if (componentMeasureHasMoreThanOnePackageGrouping(appliedComponentModel)) {
                messages.add(appliedComponentMeasure.getName() + ERR_COMPONENT_MEASURE_HAS_MORE_THAN_ONE_PACKAGE_GROUPING);
            }
        }
        return messages;
    }

    private String getTemplateXmlString(String modelType) {
        String templateXml = "";
        try {
            XmlProcessor xmlProcessor = cqlLibraryService.loadCQLXmlTemplateFile(modelType);
            Node measureNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), "measure");
            templateXml = xmlProcessor.transform(measureNode);
        } catch (XPathExpressionException e) {
            LOG.error(e.getMessage(), e);
        }
        return templateXml;
    }

    private boolean componentMeasureHasMoreThanOnePackageGrouping(ManageMeasureDetailModel appliedComponentModel) {
        MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(appliedComponentModel.getId());
        if (xmlModel != null && StringUtils.isNotBlank(xmlModel.getXml())) {
            XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());
            try {
                String XPATH_MEASURE_GROUPING = "/measure/measureGrouping/group";
                NodeList measureGroupingNodeList = (NodeList) xPath.evaluate(XPATH_MEASURE_GROUPING,
                        xmlProcessor.getOriginalDoc(), XPathConstants.NODESET);
                if (measureGroupingNodeList.getLength() > 1) {
                    return true;
                }
            } catch (XPathExpressionException e) {
                return false;
            }

        }
        return false;
    }

    private boolean appliedComponentMeasureContainsAPackage(Result appliedComponentMeasure, ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel) {
        if (!manageCompositeMeasureDetailModel.getPackageMap().containsKey(appliedComponentMeasure.getId())) {
            return false;
        } else {
            MeasurePackageOverview packageOverview = manageCompositeMeasureDetailModel.getPackageMap().get(appliedComponentMeasure.getId());
            if (packageOverview.getPackages() != null && packageOverview.getPackages().size() > 0) {
                return true;
            }
        }

        return false;
    }
}
