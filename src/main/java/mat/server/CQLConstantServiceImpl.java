package mat.server;

import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import mat.client.cqlconstant.service.CQLConstantService;
import mat.client.shared.CQLConstantContainer;
import mat.client.shared.CQLTypeContainer;
import mat.client.shared.FhirAttribute;
import mat.client.shared.FhirDataType;
import mat.client.shared.FhirDatatypeAttributeAssociation;
import mat.client.shared.MatContext;
import mat.dao.clause.QDSAttributesDAO;
import mat.dto.DataTypeDTO;
import mat.dto.UnitDTO;
import mat.model.cql.CQLKeywords;
import org.slf4j.LoggerFactory;
import mat.server.service.CodeListService;
import mat.server.service.MeasureLibraryService;
import mat.server.spreadsheet.MatAttribute;
import mat.server.util.MATPropertiesService;
import mat.server.util.QDMUtil;
import mat.shared.cql.model.FunctionSignature;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.cqframework.cql.cql2elm.SystemModelInfoProvider;
import org.hl7.elm.r1.VersionedIdentifier;
import org.hl7.elm_modelinfo.r1.ClassInfo;
import org.hl7.elm_modelinfo.r1.ClassInfoElement;
import org.hl7.elm_modelinfo.r1.ModelInfo;
import org.hl7.elm_modelinfo.r1.TypeInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CQLConstantServiceImpl extends SpringRemoteServiceServlet implements CQLConstantService {

    private static final Logger logger = LoggerFactory.getLogger(CQLConstantServiceImpl.class);

    private static final String TYPE = "type";

    private static final String QUALIFICATION = "qualification";

    private static final String SPECIALTY = "specialty";

    private static final String ROLE = "role";

    private static final String RELATIONSHIP = "relationship";

    private static final String IDENTIFIER = "identifier";

    private static final String ID = "id";

    private static final String RANK = "rank";

    private static final String PRESENT_ON_ADMISSION_INDICATOR = "presentOnAdmissionIndicator";

    private static final String RESULT = "result";

    private static final String UNIT = "unit";

    private static final String REFERENCE_RANGE = "referenceRange";

    private static final String HIGH = "high";

    private static final String LOW = "low";

    private static final String LOCATION_PERIOD = "locationPeriod";

    private static final String CODE = "code";

    private static final String VALUE = "value";

    private static final String NAMING_SYSTEM = "namingSystem";

    private static final long serialVersionUID = 1L;

    @Autowired
    private CodeListService codeListService;

    @Autowired
    private QDSAttributesDAO qDSAttributesDAO;

    @Autowired
    private MeasureLibraryService measureLibraryService;

    @Autowired
    private MappingSpreadsheetService mappingService;

    @Override
    public CQLConstantContainer getAllCQLConstants() {
        final CQLConstantContainer cqlConstantContainer = new CQLConstantContainer();

        try {
            final List<UnitDTO> unitDTOList = codeListService.getAllUnits();
            cqlConstantContainer.setCqlUnitDTOList(unitDTOList);

            // get the unit map in the form of <UnitName, CQLUnit>
            final Map<String, String> unitMap = new LinkedHashMap<>();
            unitMap.put(MatContext.PLEASE_SELECT, MatContext.PLEASE_SELECT);
            for (final UnitDTO unit : unitDTOList) {
                unitMap.put(unit.getUnit(), unit.getCqlunit());
            }
            cqlConstantContainer.setCqlUnitMap(unitMap);

            loadFhirAttributes(cqlConstantContainer);

            cqlConstantContainer.setCqlAttributeList(qDSAttributesDAO.getAllAttributes());

            final List<DataTypeDTO> dataTypeListBoxList = codeListService.getAllDataTypes();
            final List<String> datatypeList = new ArrayList<>();
            for (int i = 0; i < dataTypeListBoxList.size(); i++) {
                datatypeList.add(dataTypeListBoxList.get(i).getItem());
            }

            final List<String> qdmDatatypeList = new ArrayList<>();
            Collections.sort(datatypeList);
            qdmDatatypeList.addAll(datatypeList);
            datatypeList.remove("attribute");

            cqlConstantContainer.setCqlDatatypeList(datatypeList);
            cqlConstantContainer.setQdmDatatypeList(qdmDatatypeList);

            final CQLKeywords keywordList = measureLibraryService.getCQLKeywordsLists();
            cqlConstantContainer.setCqlKeywordList(keywordList);

            final List<String> timings = keywordList.getCqlTimingList();
            Collections.sort(timings);
            cqlConstantContainer.setCqlTimingList(timings);

            cqlConstantContainer.setCurrentQDMVersion(MATPropertiesService.get().getQdmVersion());
            cqlConstantContainer.setCurrentFhirVersion(MATPropertiesService.get().getFhirVersion());
            cqlConstantContainer.setCurrentReleaseVersion(MATPropertiesService.get().getCurrentReleaseVersion());

            cqlConstantContainer.setQdmContainer(QDMUtil.getQDMContainer());
            cqlConstantContainer.setCqlTypeContainer(getCQLTypeInformation());

            cqlConstantContainer.setFunctionSignatures(getFunctionSignatures());
        } catch (Exception e) {
            log("CQLConstantServiceImpl::getAllCQLConstants " + e.getMessage(), e);
            throw e;
        }
        return cqlConstantContainer;
    }

    private void loadFhirAttributes(CQLConstantContainer cqlConstantContainer) {
        HashSet<String> fhirDataTypeSet = new HashSet<>();
        // get all fhir attribnutes and Datatypes
        List<MatAttribute> attribs = mappingService.getMatAttributes();
        for (MatAttribute conversionMapping : attribs) {
            String fhirResource = StringUtils.trimToEmpty(conversionMapping.getFhirResource());
            if (!fhirResource.isEmpty()) {
                fhirDataTypeSet.add(fhirResource);
            }
            String fhirElement = StringUtils.trimToEmpty(conversionMapping.getFhirElement());
            if (fhirElement.isEmpty() || fhirResource.isEmpty()) {
                continue;
            }
            String fhirResourceId = hashForId(fhirResource);
            String fhirElementId = hashForId(fhirResource + "--" + fhirElement);
            FhirDataType fhirDataType =
                    cqlConstantContainer.getFhirDataTypes().computeIfAbsent(fhirResource, s -> new FhirDataType(fhirResourceId, fhirResource));
            fhirDataType.getAttributes().computeIfAbsent(fhirElement, s -> new FhirAttribute(fhirElementId, fhirElement, StringUtils.trimToEmpty(conversionMapping.getFhirType())));
        }
        cqlConstantContainer.setFhirCqlDataTypeList(mappingService.getFhirTypes());

        List<FhirDatatypeAttributeAssociation> fhirDatatypeAttributeAssociations = mappingService.fhirDatatypeAttributeAssociation();

        List<String> compoundDataTypes = new ArrayList<>();

        for (FhirDatatypeAttributeAssociation a : fhirDatatypeAttributeAssociations) {
            if (!compoundDataTypes.contains(a.getDatatype())) {
                compoundDataTypes.add(a.getDatatype()); // use list and will retain sorted order, from server
            }
        }

        List<String> populationBasisValidValues =  mappingService.populationBasisValidValues();

        cqlConstantContainer.setAttributeAssociations(fhirDatatypeAttributeAssociations);
        cqlConstantContainer.setCompoundFhirDataTypes(compoundDataTypes);
        cqlConstantContainer.setPopulationBasisValidValues(populationBasisValidValues);
    }

    private String hashForId(String value) {
        return Hashing.sha256().hashString(value, StandardCharsets.UTF_8).toString();
    }

    private List<FunctionSignature> getFunctionSignatures() {
        ClassLoader classLoader = getClass().getClassLoader();
        FunctionSignature[] signatureArray = {};
        try {
            Gson gson = new Gson();
            signatureArray = gson.fromJson(
                    new FileReader(classLoader.getResource("functions/signatures.json").getFile()),
                    FunctionSignature[].class
            );

        } catch (FileNotFoundException e) {
            logger.warn(e.getMessage(), e);
        }

        return Arrays.asList(signatureArray);
    }

    private CQLTypeContainer getCQLTypeInformation() {
        CQLTypeContainer cqlTypeContainer = new CQLTypeContainer();
        SystemModelInfoProvider systemModelInfoProvider = new SystemModelInfoProvider();

        VersionedIdentifier versionedIdentifier = new VersionedIdentifier();
        versionedIdentifier.setId("System");

        ModelInfo modelInfo = systemModelInfoProvider.load(versionedIdentifier);
        Map<String, List<String>> typeToTypeAttributeMap = new HashMap<>();

        buildTypeToTypeAttributeMap(modelInfo, typeToTypeAttributeMap);

        updateTypeToTypeAttributeMap(typeToTypeAttributeMap);

        cqlTypeContainer.setTypeToTypeAttributeMap(typeToTypeAttributeMap);
        return cqlTypeContainer;
    }

    private void buildTypeToTypeAttributeMap(ModelInfo modelInfo, Map<String, List<String>> typeToTypeAttributeMap) {
        for (TypeInfo typeInfo : modelInfo.getTypeInfo()) {
            if (typeInfo instanceof ClassInfo) {
                ClassInfo currentClassInfo = (ClassInfo) typeInfo;

                if (!typeToTypeAttributeMap.containsKey(currentClassInfo.getName())) {
                    typeToTypeAttributeMap.put(currentClassInfo.getName(), new ArrayList<>());
                }

                for (ClassInfoElement attribute : currentClassInfo.getElement()) {
                    typeToTypeAttributeMap.get(currentClassInfo.getName()).add(attribute.getName());
                }
            }
        }
    }

    private void updateTypeToTypeAttributeMap(Map<String, List<String>> typeToTypeAttributeMap) {
        // TODO: Find a better way to do this instead of hardcoding.
        typeToTypeAttributeMap.remove("System.Code");
        typeToTypeAttributeMap.remove("list<System.Code");
        typeToTypeAttributeMap.put("QDM.Id", Arrays.asList(NAMING_SYSTEM, VALUE));
        typeToTypeAttributeMap.put("list<QDM.Id>", Arrays.asList(NAMING_SYSTEM, VALUE));
        typeToTypeAttributeMap.put("QDM.FacilityLocation", Arrays.asList(CODE, LOCATION_PERIOD));
        typeToTypeAttributeMap.put("list<QDM.FacilityLocation>", Arrays.asList(CODE, LOCATION_PERIOD));
        typeToTypeAttributeMap.put("interval<System.DateTime>", Arrays.asList(LOW, HIGH));
        typeToTypeAttributeMap.put("System.Quantity", Arrays.asList(UNIT, VALUE));
        typeToTypeAttributeMap.put("interval<System.Quantity>", Arrays.asList(LOW, HIGH));
        typeToTypeAttributeMap.put("list<QDM.Component>", Arrays.asList(CODE, REFERENCE_RANGE, RESULT));
        typeToTypeAttributeMap.put("list<QDM.ResultComponent>", Arrays.asList(CODE, REFERENCE_RANGE, RESULT));
        typeToTypeAttributeMap.put("QDM.DiagnosisComponent", Arrays.asList(CODE, PRESENT_ON_ADMISSION_INDICATOR, RANK));
        typeToTypeAttributeMap.put("list<QDM.DiagnosisComponent>", Arrays.asList(CODE, PRESENT_ON_ADMISSION_INDICATOR, RANK));
        typeToTypeAttributeMap.put("QDM.PatientEntity", Arrays.asList(ID, IDENTIFIER));
        typeToTypeAttributeMap.put("QDM.CarePartner", Arrays.asList(ID, IDENTIFIER, RELATIONSHIP));
        typeToTypeAttributeMap.put("QDM.Practitioner", Arrays.asList(ID, IDENTIFIER, ROLE, SPECIALTY, QUALIFICATION));
        typeToTypeAttributeMap.put("QDM.Organization", Arrays.asList(ID, IDENTIFIER, TYPE));
        typeToTypeAttributeMap.put("QDM.Identifier", Arrays.asList(NAMING_SYSTEM, VALUE));
    }

}
