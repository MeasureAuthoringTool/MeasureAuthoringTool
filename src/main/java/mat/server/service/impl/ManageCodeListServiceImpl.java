package mat.server.service.impl;

import mat.client.codelist.HasListBox;
import mat.client.codelist.service.SaveUpdateCodeListResult;
import mat.client.shared.MatRuntimeException;
import mat.dao.AuthorDAO;
import mat.dao.CategoryDAO;
import mat.dao.CodeSystemDAO;
import mat.dao.DataTypeDAO;
import mat.dao.MeasureScoreDAO;
import mat.dao.MeasureTypeDAO;
import mat.dao.QualityDataSetDAO;
import mat.dao.StewardDAO;
import mat.dao.UnitDAO;
import mat.dao.UnitTypeDAO;
import mat.dao.UnitTypeMatrixDAO;
import mat.dao.clause.OperatorDAO;
import mat.dto.CodeSystemDTO;
import mat.dto.DataTypeDTO;
import mat.dto.OperatorDTO;
import mat.dto.UnitDTO;
import mat.dto.VSACCodeSystemDTO;
import mat.model.Category;
import mat.model.CodeListSearchDTO;
import mat.model.CodeSystem;
import mat.model.DataType;
import mat.model.GroupedCodeList;
import mat.model.ListObject;
import mat.model.MatValueSetTransferObject;
import mat.vsacmodel.ValueSet;
import mat.model.QualityDataModelWrapper;
import mat.model.QualityDataSetDTO;
import mat.server.logging.LogFactory;
import mat.server.service.CodeListService;
import mat.server.service.MeasureLibraryService;
import mat.shared.ConstantMessages;
import mat.shared.DateUtility;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.UUID;

@Service
public class ManageCodeListServiceImpl implements CodeListService {
    private Log log = LogFactory.getLog(ManageCodeListServiceImpl.class);

    private static final String OID_TO_VSAC_CODE_SYSTEM_DTO = "oidToVSACDodeSystemDTO";
    private static final String QDM_TAG = "<qdm";
    private static final String VALUESET_TAG = "<valueset ";
    private static final String QUALITY_DATA_MODEL_MAPPING = "QualityDataModelMapping.xml";
    private static final String VALUESET_MAPPING = "ValueSetsMapping.xml";

    private static final int ASCII_START = 65;
    private static final int ASCII_END = 90;
    @Autowired
    private AuthorDAO authorDAO;
    @Autowired
    private CategoryDAO categoryDAO;
    @Autowired
    private CodeSystemDAO codeSystemDAO;
    @Autowired
    private DataTypeDAO dataTypeDAO;
    @Autowired
    private MeasureScoreDAO measureScoreDAO;
    @Autowired
    private MeasureTypeDAO measureTypeDAO;
    @Autowired
    private OperatorDAO operatorDAO;
    @Autowired
    private QualityDataSetDAO qualityDataSetDAO;
    @Autowired
    private StewardDAO stewardDAO;
    @Autowired
    private UnitDAO unitDAO;
    @Autowired
    private UnitTypeDAO unitTypeDAO;
    @Autowired
    private UnitTypeMatrixDAO unitTypeMatrixDAO;
    @Autowired
    private MeasureLibraryService measureLibraryService;

    @Value("${QDM_QICORE_MAPPING_SERVICES_URL:http://localhost:9090}/codeSystemEntries")
    private String mappingsUrl;

    @Autowired
    @Qualifier("internalRestTemplate")
    private RestTemplate restTemplate;

    private String addAppliedQDMInMeasureXML(String mapping, String startTag, QualityDataModelWrapper qualityDataSetDTOWrapper) {
        log.info("addAppliedQDMInMeasureXML Method Call Start.");
        String xmlString = null;
        String stream = createNewXML(mapping, qualityDataSetDTOWrapper);
        if (StringUtils.isNotBlank(stream)) {
            int startIndex = stream.indexOf(startTag, 0);
            int lastIndex = stream.indexOf("/>", startIndex);
            xmlString = stream.substring(startIndex, lastIndex + 2);
            log.debug("addAppliedQDMInMeasureXML Method Call xmlString :: " + xmlString);
        }
        return xmlString;
    }

    private String createNewXML(String mapping, QualityDataModelWrapper qualityDataSetDTOWrapper) {
        String stream = null;
        try {
            final XMLMarshalUtil xmlMarshalUtil = new XMLMarshalUtil();
            stream = xmlMarshalUtil.convertObjectToXML(mapping, qualityDataSetDTOWrapper);
        } catch (MarshalException | ValidationException | IOException | MappingException e) {
            log.info("Exception in converting XML to object: " + e.getMessage(), e);
        }
        log.info("Exiting ManageCodeListServiceImpl.createXml()");
        return stream;
    }

    /**
     * Check for duplicates.
     *
     * @param ValueSetTransferObject the mat Value Set Transfer Object
     * @param isVSACValueSet            the is mat.vsac value set
     * @param isSpecificOccurrence      the is specific occurrence
     * @return true if Found and false if not found.
     */
    private boolean isDuplicate(
            MatValueSetTransferObject ValueSetTransferObject,
            boolean isVSACValueSet, boolean isSpecificOccurrence) {
        log.info(" checkForDuplicates Method Call Start.");
        boolean isQDSExist = false;
        boolean isExpOrVerNotEq = false;
        DataType dt = dataTypeDAO.find(ValueSetTransferObject.getDatatype());
        String qdmCompareNameOrID = "";
        String version = "";
        String expansionIdentifier = "";
        if (ValueSetTransferObject.isVersionDate()) {
            version = ValueSetTransferObject.getValueSet().getVersion();
        } else {
            version = "1.0";
        }
        if (ValueSetTransferObject.isExpansionProfile()) {
            expansionIdentifier = ValueSetTransferObject.getValueSet()
                    .getExpansionProfile();
        }

        if (isVSACValueSet) {
            qdmCompareNameOrID = ValueSetTransferObject.getValueSet()
                    .getID();
        } else {
            qdmCompareNameOrID = ValueSetTransferObject
                    .getCodeListSearchDTO().getName();
        }
        List<QualityDataSetDTO> existingQDSList = ValueSetTransferObject
                .getAppliedQDMList();
        for (QualityDataSetDTO dataSetDTO : existingQDSList) {
            // For "Element without VSAC value set", duplicates should not be
            // checked in
            // elements with VSAC value set in applied QDM list.
            if (!isVSACValueSet
                    && !dataSetDTO.getOid().equalsIgnoreCase(
                    ConstantMessages.USER_DEFINED_QDM_OID)) {
                continue;
            }
            String codeListNameOrOID = "";
            if (isVSACValueSet) {
                codeListNameOrOID = dataSetDTO.getOid();
            } else {
                codeListNameOrOID = dataSetDTO.getCodeListName();
            }

            /**
             * this condition is true if oid and Expansion Identifier is same
             * else if the newly created exp Identifier does'nt match with
             * existing exp Identifier then the flag isExporVerNotEq is set to
             * true.
             */
            // for Expansion Identifier
            if (codeListNameOrOID.equalsIgnoreCase(qdmCompareNameOrID)
                    && (dataSetDTO.getExpansionIdentifier() != null)) {
                if (!expansionIdentifier.equals(dataSetDTO
                        .getExpansionIdentifier())) {
                    isExpOrVerNotEq = true;
                    break;
                }
            }

            /**
             * this condition is true if oid and version are same else if the
             * newly created version does'nt match with existing version then
             * the flag isExporVerNotEq is set to true. this is same in case of
             * Most Recent
             */

            // for Version
            else if (codeListNameOrOID.equalsIgnoreCase(qdmCompareNameOrID)) {
                String versionOfDataSetDTO = null;
                if (dataSetDTO.getVersion().equals("1.0")
                        || dataSetDTO.getVersion().equals("1")) {
                    versionOfDataSetDTO = "1.0";
                } else {
                    versionOfDataSetDTO = dataSetDTO.getVersion();
                }
                if (!version.equals(versionOfDataSetDTO)
                        || !expansionIdentifier.isEmpty()) {
                    isExpOrVerNotEq = true;
                    break;
                }
            }

            if (!isSpecificOccurrence
                    && dt.getDescription().equalsIgnoreCase(
                    dataSetDTO.getDataType())
                    && (codeListNameOrOID.equalsIgnoreCase(qdmCompareNameOrID))) {
                // if the same dataType exists and the occurrenceText is also
                // null
                // then there is a any occurrence exists for that dataType.
                isQDSExist = true;
                break;
            }
        }
        log.info("checkForDuplicates Method Call End.Check resulted in :"
                + (isQDSExist || isExpOrVerNotEq));
        return (isQDSExist || isExpOrVerNotEq);
    }

    /*
     * This method is used to find the number of occurrences that exist for the
     * given measure ,codeList and datType. null can be passed to
     * getQDSElementsFor()...since the whole purpose of this measure is to find
     * how many occurrence exists for the given dataType not for the specific
     * occurrence.
     */

    /**
     * Check for occurrence count mat.vsac api.
     *
     * @param dataTypeId  the data type id
     * @param ValueSet the mat value set
     * @param appliedQDM  the applied qdm
     * @return the int
     */
    private int checkForOccurrenceCountVsacApi(final String dataTypeId,
                                               final ValueSet ValueSet,
                                               final ArrayList<QualityDataSetDTO> appliedQDM) {
        int occurrenceCount = ASCII_START;
        ListIterator<QualityDataSetDTO> qdsIterator = appliedQDM.listIterator();
        DataType dt = null;
        dt = dataTypeDAO.find(dataTypeId);
        while (qdsIterator.hasNext()) {
            QualityDataSetDTO dto = qdsIterator.next();
            if (ValueSet.getID().equalsIgnoreCase(dto.getOid())) {
                if (dt.getDescription().equalsIgnoreCase(dto.getDataType())
                        && (dto.getOccurrenceText() != null)
                        && StringUtils.isNotEmpty(dto.getOccurrenceText())
                        && StringUtils.isNotBlank(dto.getOccurrenceText())) {
                    String nextOccString = dto.getOccurrenceText();
                    Character text = nextOccString.charAt(nextOccString
                            .length() - 1);
                    int newOcc = (text);
                    if (newOcc >= occurrenceCount) {
                        occurrenceCount = ++newOcc;
                    }
                }
            }
        }
        return occurrenceCount;
    }

    /**
     * Check for duplicates.
     *
     * @param ValueSetTransferObject the mat value set transfer object
     * @param isVSACValueSet            the is mat.vsac value set
     * @return true, if successful
     */
    private boolean checkForDuplicates(
            MatValueSetTransferObject ValueSetTransferObject,
            boolean isVSACValueSet) {
        log.info(" checkForDuplicates Method Call Start.");
        boolean isQDSExist = false;
        DataType dt = dataTypeDAO.find(ValueSetTransferObject.getDatatype());
        String qdmCompareNameOrID = "";
        if (isVSACValueSet) {
            qdmCompareNameOrID = ValueSetTransferObject.getValueSet()
                    .getID();
        } else {
            qdmCompareNameOrID = ValueSetTransferObject
                    .getCodeListSearchDTO().getName();
        }
        List<QualityDataSetDTO> existingQDSList = ValueSetTransferObject
                .getAppliedQDMList();
        for (QualityDataSetDTO dataSetDTO : existingQDSList) {
            // For "Element without VSAC value set", duplicates should not be
            // checked in
            // elements with VSAC value set in applied QDM list.
            if (!isVSACValueSet
                    && !dataSetDTO.getOid().equalsIgnoreCase(
                    ConstantMessages.USER_DEFINED_QDM_OID)) {
                continue;
            }
            String codeListNameOrOID = "";
            if (isVSACValueSet) {
                codeListNameOrOID = dataSetDTO.getOid();
            } else {
                codeListNameOrOID = dataSetDTO.getCodeListName();
            }
            if (dt.getDescription().equalsIgnoreCase(dataSetDTO.getDataType())
                    && (codeListNameOrOID.equalsIgnoreCase(qdmCompareNameOrID))
                    && StringUtils.isBlank(dataSetDTO.getOccurrenceText())) {
                // if the same dataType exists and the occurrenceText is also
                // null
                // then there is a any occurrence exists for that dataType.
                isQDSExist = true;
                break;
            }
        }
        log.info("checkForDuplicates Method Call End.Check resulted in :"
                + isQDSExist);
        return isQDSExist;
    }


    /**
     * Filter code lists for current category.
     *
     * @param listObject the list object
     */
    public void filterCodeListsForCurrentCategory(ListObject listObject) {
        if (listObject.getCodesLists() != null) {
            List<GroupedCodeList> toBeRemoved = new ArrayList<>();
            for (GroupedCodeList cl : listObject.getCodesLists()) {
                if (!cl.getCodeList().getCategory().getId()
                        .equals(listObject.getCategory().getId())) {
                    toBeRemoved.add(cl);
                }
            }
            listObject.getCodesLists().removeAll(toBeRemoved);
        }
    }

    public List<DataTypeDTO> getAllDataTypes() {
        return dataTypeDAO.findAllDataType();
    }

    @Override
    public List<OperatorDTO> getAllOperators() {
        return operatorDAO.getAllOperators();
    }

    @Override
    public List<UnitDTO> getAllUnits() {
        return unitDAO.getAllUnits();
    }

    @Override
    public List<? extends HasListBox> getCodeListsForCategory(String categoryId) {
        return new ArrayList<>();
    }

    @Override
    public List<? extends HasListBox> getCodeSystemsForCategory(
            String categoryId) {
        List<CodeSystemDTO> retList = new ArrayList<>();
        if ((categoryId != null) && !"".equals(categoryId)) {
            Category category = categoryDAO.find(categoryId);
            for (CodeSystem codeSys : category.getCodeSystems()) {
                String codeSysDesc = codeSys.getDescription();
                // TODO:- don't need to check for grouping code System. Since in
                // another user story it is been removed.
                // US 424. As per new Appendix LOINC should be displayed
                // US 594. The Supplimental Value Sets Code System should not be
                // included in the codeSystem dropDown.
                if (!ConstantMessages.GROUPING_CODE_SYSTEM
                        .equalsIgnoreCase(codeSysDesc)
                        && !ConstantMessages.HL7_ADMINGENDER_CODE_SYSTEM
                        .equalsIgnoreCase(codeSysDesc)
                        && !ConstantMessages.CDC_CODE_SYSTEM
                        .equalsIgnoreCase(codeSysDesc)
                        && !ConstantMessages.SOURCE_OF_PAYMENT
                        .equalsIgnoreCase(codeSysDesc)) {
                    CodeSystemDTO dto = new CodeSystemDTO();
                    dto.setDescription(codeSys.getDescription());
                    dto.setId(codeSys.getId());
                    retList.add(dto);
                }
            }
        }
        return retList;
    }

    @Override
    public mat.client.codelist.service.CodeListService.ListBoxData getListBoxData() {
        mat.client.codelist.service.CodeListService.ListBoxData data = new mat.client.codelist.service.CodeListService.ListBoxData();
        data.setCategoryList(categoryDAO.getAllCategories());
        data.setCodeSystemList(codeSystemDAO.getAllCodeSystem());
        data.setMeasureStewardList(stewardDAO.getAllStewardOrg());
        data.setAuthorsList(authorDAO.getAllAuthors());
        data.setMeasureTypeList(measureTypeDAO.getAllMeasureTypes());
        data.setScoringList(measureScoreDAO.getAllMeasureScores());
        data.setUnitList(unitDAO.getAllUnits());
        data.setUnitTypeList(unitTypeDAO.getAllUnitTypes());
        data.setUnitTypeMatrixList(unitTypeMatrixDAO.getAllUnitMatrix());
        data.setLogicalOperatorList(operatorDAO.getLogicalOperators());
        data.setRelTimingOperatorList(operatorDAO.getRelTimingperators());
        data.setRelAssocOperatorList(operatorDAO.getRelAssociationsOperators());
        return data;
    }

    @Override
    public List<? extends HasListBox> getQDSDataTypeForCategory(
            String categoryId) {
        List<DataTypeDTO> retList = new ArrayList<>();
        if (StringUtils.isNotBlank(categoryId)) {
            Category category = categoryDAO.find(categoryId);
            for (DataType dataType : category.getDataTypes()) {
                retList.add(new DataTypeDTO(dataType.getId(), dataType.getDescription()));
            }
        }
        return retList;
    }

    @Override
    public java.util.List<QualityDataSetDTO> getQDSElements(
            final String measureId, final String version) {
        return qualityDataSetDAO.getQDSElements(false, measureId);
    }


    /**
     * Modify applied element list.
     *
     * @param dataSetDTO the data set dto
     * @param appliedQDM the applied qdm
     * @return the quality data model wrapper
     */
    private QualityDataModelWrapper modifyAppliedElementList(
            final QualityDataSetDTO dataSetDTO,
            final ArrayList<QualityDataSetDTO> appliedQDM) {
        QualityDataModelWrapper wrapper = new QualityDataModelWrapper();
        Iterator<QualityDataSetDTO> iterator = appliedQDM.iterator();
        while (iterator.hasNext()) {
            QualityDataSetDTO qualityDataSetDTO = iterator.next();
            if (qualityDataSetDTO.getUuid().equals(dataSetDTO.getUuid())) {
                iterator.remove();
                break;
            }
        }
        appliedQDM.add(dataSetDTO);
        wrapper.setQualityDataDTO(appliedQDM);
        return wrapper;
    }

    @Override
    public final SaveUpdateCodeListResult saveQDStoMeasure(
            MatValueSetTransferObject valueSetTransferObject) {
        SaveUpdateCodeListResult result = new SaveUpdateCodeListResult();
        QualityDataModelWrapper wrapper = new QualityDataModelWrapper();
        ArrayList<QualityDataSetDTO> qdsList = new ArrayList<>();
        wrapper.setQualityDataDTO(qdsList);
        valueSetTransferObject.scrubForMarkUp();
        QualityDataSetDTO qds = new QualityDataSetDTO();
        String dataType = valueSetTransferObject.getDatatype();
        if (dataType != null) {
            DataType dt = dataTypeDAO.find(dataType);
            qds.setDataType(dt.getDescription());
        }
        ValueSet ValueSet = valueSetTransferObject.getValueSet();
        qds.setOid(ValueSet.getID());
        qds.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        qds.setCodeListName(ValueSet.getDisplayName());
        qds.setValueSetType(ValueSet.getType());
        if (ValueSet.isGrouping()) {
            qds.setTaxonomy(ConstantMessages.GROUPING_CODE_SYSTEM);
        } else {
            qds.setTaxonomy(ValueSet.getCodeSystemName());
        }
        qds.setUuid(UUID.randomUUID().toString());
        if (valueSetTransferObject.isVersionDate()
                || valueSetTransferObject.isEffectiveDate()) {
            qds.setVersion(valueSetTransferObject.getValueSet().getVersion());
        } else {
            qds.setVersion("1.0");
        }
        if (valueSetTransferObject.isExpansionProfile()) {
            qds.setExpansionIdentifier(valueSetTransferObject.getValueSet()
                    .getExpansionProfile());
        }

        ArrayList<QualityDataSetDTO> qualityDataSetDTOs = (ArrayList<QualityDataSetDTO>) valueSetTransferObject
                .getAppliedQDMList();

        if (valueSetTransferObject.isSpecificOccurrence()) {

            if (isDuplicate(valueSetTransferObject, true, true)) {
                result.setSuccess(false);
                result.setFailureReason(SaveUpdateCodeListResult.ALREADY_EXISTS);
                return result;
            }
            int occurrenceCount = checkForOccurrenceCountVsacApi(dataType,
                    ValueSet, qualityDataSetDTOs);
            if (occurrenceCount < ASCII_END) {
                // Alphabet ASCII Integer Values.
                char occTxt = (char) occurrenceCount;
                qds.setOccurrenceText("Occurrence" + " " + occTxt);
                wrapper.getQualityDataDTO().add(qds);
                result.setOccurrenceMessage(qds.getOccurrenceText());
                qualityDataSetDTOs.add(qds);
                String qdmXMLString = addAppliedQDMInMeasureXML(QUALITY_DATA_MODEL_MAPPING, QDM_TAG, wrapper);
                String newqdmXMLString = addAppliedQDMInMeasureXML(VALUESET_MAPPING, VALUESET_TAG, wrapper);
                result.setSuccess(true);
                result.setAppliedQDMList(sortQualityDataSetList(qualityDataSetDTOs));
                result.setXmlString(qdmXMLString);
                result.setnewXmlString(newqdmXMLString);
            }
        } else {
            // Treat as regular QDM
            if (!isDuplicate(valueSetTransferObject, true, false)) {
                wrapper.getQualityDataDTO().add(qds);
                result.setOccurrenceMessage(qds.getOccurrenceText());
                String qdmXMLString = addAppliedQDMInMeasureXML(QUALITY_DATA_MODEL_MAPPING, QDM_TAG, wrapper);
                String newqdmXMLString = addAppliedQDMInMeasureXML(VALUESET_MAPPING, VALUESET_TAG, wrapper);
                result.setSuccess(true);
                qualityDataSetDTOs.add(qds);
                result.setAppliedQDMList(sortQualityDataSetList(qualityDataSetDTOs));
                result.setXmlString(qdmXMLString);
                result.setnewXmlString(newqdmXMLString);
            } else {
                result.setSuccess(false);
                result.setFailureReason(SaveUpdateCodeListResult.ALREADY_EXISTS);
            }
        }
        return result;
    }

    @Override
    public SaveUpdateCodeListResult saveUserDefinedQDStoMeasure(
            MatValueSetTransferObject ValueSetTransferObject) {
        SaveUpdateCodeListResult result = new SaveUpdateCodeListResult();
        QualityDataModelWrapper wrapper = new QualityDataModelWrapper();
        ValueSetTransferObject.scrubForMarkUp();
        List<String> errorMessages = new ArrayList<>();
        if (errorMessages.size() == 0) {
            ArrayList<QualityDataSetDTO> qdsList = new ArrayList<>();
            List<QualityDataSetDTO> existingQDSList = ValueSetTransferObject
                    .getAppliedQDMList();
            String dataType = ValueSetTransferObject.getDatatype();
            DataType dt = dataTypeDAO.find(dataType);
            boolean isQDSExist = false;
            for (QualityDataSetDTO dataSetDTO : existingQDSList) {
                if (dataSetDTO.getOid().equalsIgnoreCase(
                        ConstantMessages.USER_DEFINED_QDM_OID)) {
                    if (dt.getDescription().equalsIgnoreCase(
                            dataSetDTO.getDataType())
                            && (dataSetDTO.getCodeListName()
                            .equalsIgnoreCase(ValueSetTransferObject
                                    .getUserDefinedText()))
                            && (dataSetDTO.getOccurrenceText() == null)) {
                        // if the same dataType exists and the occurrenceText is
                        // also
                        // null
                        // then there is a any occurrence exists for that
                        // dataType.
                        isQDSExist = true;
                        break;
                    }
                }
            }
            if (!isQDSExist) {
                wrapper.setQualityDataDTO(qdsList);
                QualityDataSetDTO qds = new QualityDataSetDTO();
                qds.setDataType(dt.getDescription());
                qds.setOid(ConstantMessages.USER_DEFINED_QDM_OID);
                qds.setId(UUID.randomUUID().toString());
                qds.setCodeListName(ValueSetTransferObject
                        .getUserDefinedText());
                qds.setTaxonomy(ConstantMessages.USER_DEFINED_QDM_NAME);
                qds.setUuid(UUID.randomUUID().toString());
                qds.setVersion("1.0");
                wrapper.getQualityDataDTO().add(qds);
                String qdmXMLString = addAppliedQDMInMeasureXML(QUALITY_DATA_MODEL_MAPPING, QDM_TAG, wrapper);
                String newqdmXMLString = addAppliedQDMInMeasureXML(VALUESET_MAPPING, VALUESET_TAG, wrapper);
                result.setSuccess(true);
                result.setAppliedQDMList(sortQualityDataSetList(wrapper
                        .getQualityDataDTO()));
                result.setXmlString(qdmXMLString);
                result.setnewXmlString(newqdmXMLString);
            } else {
                result.setSuccess(false);
                result.setFailureReason(SaveUpdateCodeListResult.ALREADY_EXISTS);
            }
        } else {
            result.setSuccess(false);
            result.setFailureReason(SaveUpdateCodeListResult.SERVER_SIDE_VALIDATION);
        }
        return result;
    }

    /**
     * Sets the dto values from model.
     *
     * @param cl  the cl
     * @param dto the dto
     */
    void setDTOValuesFromModel(final ListObject cl, final CodeListSearchDTO dto) {
        dto.setCategoryCode(cl.getCategory().getId());
        dto.setCategoryDisplay(cl.getCategory().getDescription());
        dto.setAbbreviatedCategory(cl.getCategory().getAbbreviation());
        dto.setAbbreviatedCodeSystem(cl.getCodeSystem().getDescription());
        dto.setId(cl.getId());
        dto.setName(cl.getName());
        dto.setOid(cl.getOid());
        dto.setSteward(cl.getSteward().getOrgName());
        dto.setCodeSystem(cl.getCodeSystem().getDescription());
        dto.setGroupedCodeList(true);
        if (cl.isDraft()) {
            dto.setLastModified("Draft");
        } else {
            dto.setLastModified(DateUtility.convertDateToString(cl
                    .getLastModified()));
        }
    }

    /**
     * Sort quality data set list.
     *
     * @param finalList the final list
     * @return the array list
     */
    private List<QualityDataSetDTO> sortQualityDataSetList(
            final List<QualityDataSetDTO> finalList) {

        Collections.sort(finalList, new Comparator<QualityDataSetDTO>() {
            @Override
            public int compare(final QualityDataSetDTO o1,
                               final QualityDataSetDTO o2) {
                return o1.getCodeListName().compareToIgnoreCase(
                        o2.getCodeListName());
            }
        });

        return finalList;

    }

    @Override
    public final SaveUpdateCodeListResult updateQDStoMeasure(
            MatValueSetTransferObject ValueSetTransferObject) {
        SaveUpdateCodeListResult result = null;
        ValueSetTransferObject.scrubForMarkUp();
        if (ValueSetTransferObject.getValueSet() != null) {
            result = updateVSACValueSetInElementLookUp(ValueSetTransferObject);
        } else if (ValueSetTransferObject.getCodeListSearchDTO() != null) {
            result = updateUserDefineQDMInElementLookUp(ValueSetTransferObject);
        }
        return result;
    }

    /**
     * Update user define qdm in element look up.
     *
     * @param ValueSetTransferObject - mat Value Set Transfer Object
     * @return SaveUpdateCodeListResult
     */
    private SaveUpdateCodeListResult updateUserDefineQDMInElementLookUp(
            MatValueSetTransferObject ValueSetTransferObject) {
        QualityDataModelWrapper wrapper = new QualityDataModelWrapper();
        SaveUpdateCodeListResult result = new SaveUpdateCodeListResult();
        List<String> messageList = new ArrayList<>();
        if (messageList.size() == 0) {
            if (!isDuplicate(ValueSetTransferObject, false, false)) {
                ArrayList<QualityDataSetDTO> qdsList = new ArrayList<>();
                wrapper.setQualityDataDTO(qdsList);
                QualityDataSetDTO qds = ValueSetTransferObject
                        .getQualityDataSetDTO();
                DataType dt = dataTypeDAO.find(ValueSetTransferObject
                        .getDatatype());
                qds.setDataType(dt.getDescription());
                qds.setOid(ConstantMessages.USER_DEFINED_QDM_OID);
                qds.setId(UUID.randomUUID().toString());
                qds.setCodeListName(ValueSetTransferObject
                        .getCodeListSearchDTO().getName());
                qds.setTaxonomy(ConstantMessages.USER_DEFINED_QDM_NAME);
                qds.setOccurrenceText(null);
                qds.setVersion("1.0");
                wrapper = modifyAppliedElementList(
                        qds,
                        (ArrayList<QualityDataSetDTO>) ValueSetTransferObject
                                .getAppliedQDMList());
                String qdmXMLString = addAppliedQDMInMeasureXML(QUALITY_DATA_MODEL_MAPPING, QDM_TAG, wrapper);
                String newqdmXMLString = addAppliedQDMInMeasureXML(VALUESET_MAPPING, VALUESET_TAG, wrapper);
                result.setSuccess(true);
                result.setAppliedQDMList(sortQualityDataSetList(wrapper
                        .getQualityDataDTO()));
                result.setXmlString(qdmXMLString);
                result.setnewXmlString(newqdmXMLString);
                result.setDataSetDTO(qds);
            } else {
                result.setSuccess(false);
                result.setFailureReason(SaveUpdateCodeListResult.ALREADY_EXISTS);
            }
        } else {
            result.setSuccess(false);
            result.setFailureReason(SaveUpdateCodeListResult.SERVER_SIDE_VALIDATION);
        }
        return result;
    }


    /**
     * Update mat.vsac value set in element look up.
     *
     * @param ValueSetTransferObject the mat value set transfer object
     * @return the save update code list result
     */
    private SaveUpdateCodeListResult updateVSACValueSetInElementLookUp(
            MatValueSetTransferObject ValueSetTransferObject) {
        SaveUpdateCodeListResult result = new SaveUpdateCodeListResult();
        QualityDataSetDTO oldQdm = new QualityDataSetDTO();
        populatedOldQDM(oldQdm,
                ValueSetTransferObject.getQualityDataSetDTO());
        if (ValueSetTransferObject.isSpecificOccurrence()) {

            QualityDataSetDTO qds = ValueSetTransferObject
                    .getQualityDataSetDTO();

            String dataType = ValueSetTransferObject.getDatatype();
            ValueSet ValueSet = ValueSetTransferObject
                    .getValueSet();
            qds.setOid(ValueSet.getID());
            qds.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            qds.setCodeListName(ValueSet.getDisplayName());
            qds.setValueSetType(ValueSet.getType());
            if (ValueSet.isGrouping()) {
                qds.setTaxonomy(ConstantMessages.GROUPING_CODE_SYSTEM);
            } else {
                qds.setTaxonomy(ValueSet.getCodeSystemName());
            }
            if (ValueSetTransferObject.isVersionDate()
                    || ValueSetTransferObject.isEffectiveDate()) {
                qds.setVersion(ValueSetTransferObject.getValueSet()
                        .getVersion());
            } else {
                qds.setVersion("1.0");
            }
            if (ValueSetTransferObject.isExpansionProfile()) {
                qds.setExpansionIdentifier(ValueSetTransferObject
                        .getValueSet().getExpansionProfile());
            } else {
                qds.setExpansionIdentifier(null);
            }
            int occurrenceCount = checkForOccurrenceCountVsacApi(dataType,
                    ValueSet,
                    (ArrayList<QualityDataSetDTO>) ValueSetTransferObject
                            .getAppliedQDMList());
            if (occurrenceCount < ASCII_END) { // Alphabet ASCII Integer Values.
                char occTxt = (char) occurrenceCount;
                qds.setOccurrenceText("Occurrence" + " " + occTxt);
                qds.setSpecificOccurrence(true);
                if (dataType != null) {
                    DataType dt = dataTypeDAO.find(dataType);
                    qds.setDataType(dt.getDescription());
                }
                QualityDataModelWrapper wrapper = modifyAppliedElementList(
                        qds,
                        (ArrayList<QualityDataSetDTO>) ValueSetTransferObject
                                .getAppliedQDMList());

                boolean isAllOIDsUpdated = findAndUpdateAllOids(oldQdm, qds, wrapper.getQualityDataDTO(),
                        ValueSetTransferObject.getMeasureId());
                result.setOccurrenceMessage(qds.getOccurrenceText());
                result.setSuccess(true);
                result.setAppliedQDMList(sortQualityDataSetList(wrapper
                        .getQualityDataDTO()));
                result.setDataSetDTO(qds);
                result.setAllOIDsUpdated(isAllOIDsUpdated);
            }
        } else { // Treat as regular QDM
            List<QualityDataSetDTO> origAppliedQDMList = ValueSetTransferObject
                    .getAppliedQDMList();
            List<QualityDataSetDTO> tempAppliedQDMList = new ArrayList<>();
            tempAppliedQDMList.addAll(ValueSetTransferObject
                    .getAppliedQDMList());
            // Removing the QDS that is being modified from the
            // tempAppliedQDMList.
            Iterator<QualityDataSetDTO> iterator = tempAppliedQDMList
                    .iterator();
            while (iterator.hasNext()) {
                QualityDataSetDTO qualityDataSetDTO = iterator.next();
                if (qualityDataSetDTO.getUuid().equals(
                        ValueSetTransferObject.getQualityDataSetDTO()
                                .getUuid())) {
                    iterator.remove();
                    break;
                }
            }
            ValueSetTransferObject.setAppliedQDMList(tempAppliedQDMList);

            if (!checkForDuplicates(ValueSetTransferObject, true)) {
                ValueSetTransferObject.setAppliedQDMList(origAppliedQDMList);
                QualityDataSetDTO qds = ValueSetTransferObject
                        .getQualityDataSetDTO();
                String dataType = ValueSetTransferObject.getDatatype();
                if (dataType != null) {
                    DataType dt = dataTypeDAO.find(dataType);
                    qds.setDataType(dt.getDescription());
                }
                ValueSet ValueSet = ValueSetTransferObject
                        .getValueSet();
                qds.setOid(ValueSet.getID());
                qds.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                qds.setCodeListName(ValueSet.getDisplayName());
                qds.setValueSetType(ValueSet.getType());
                if (ValueSet.isGrouping()) {
                    qds.setTaxonomy(ConstantMessages.GROUPING_CODE_SYSTEM);
                } else {
                    qds.setTaxonomy(ValueSet.getCodeSystemName());
                }
                if (ValueSetTransferObject.isVersionDate()
                        || ValueSetTransferObject.isEffectiveDate()) {
                    qds.setVersion(ValueSetTransferObject.getValueSet()
                            .getVersion());
                } else {
                    qds.setVersion("1.0");
                }
                if (ValueSetTransferObject.isExpansionProfile()) {
                    qds.setExpansionIdentifier(ValueSetTransferObject
                            .getValueSet().getExpansionProfile());
                } else {
                    qds.setExpansionIdentifier(null);
                }
                qds.setOccurrenceText(null);
                QualityDataModelWrapper wrapper = modifyAppliedElementList(
                        qds,
                        (ArrayList<QualityDataSetDTO>) ValueSetTransferObject
                                .getAppliedQDMList());

                boolean isAllOIDsUpdated = findAndUpdateAllOids(oldQdm, qds, wrapper.getQualityDataDTO(),
                        ValueSetTransferObject.getMeasureId());
                result.setOccurrenceMessage(qds.getOccurrenceText());
                result.setSuccess(true);
                result.setAppliedQDMList(sortQualityDataSetList(wrapper
                        .getQualityDataDTO()));
                result.setDataSetDTO(qds);
                result.setAllOIDsUpdated(isAllOIDsUpdated);
            } else {
                ValueSetTransferObject.setAppliedQDMList(origAppliedQDMList);
                result.setSuccess(false);
                result.setFailureReason(SaveUpdateCodeListResult.ALREADY_EXISTS);
            }
        }
        return result;
    }

    /**
     * Populated old qdm.
     *
     * @param oldQdm            the old qdm
     * @param qualityDataSetDTO the quality data set dto
     */
    private void populatedOldQDM(QualityDataSetDTO oldQdm,
                                 QualityDataSetDTO qualityDataSetDTO) {
        if (qualityDataSetDTO.isSpecificOccurrence()) {
            oldQdm.setOccurrenceText(qualityDataSetDTO.getOccurrenceText());
            oldQdm.setSpecificOccurrence(true);
        }
        oldQdm.setCodeListName(qualityDataSetDTO.getCodeListName());
        oldQdm.setOid(qualityDataSetDTO.getOid());
        oldQdm.setUuid(qualityDataSetDTO.getUuid());
        oldQdm.setVersion(qualityDataSetDTO.getVersion());
        oldQdm.setDataType(qualityDataSetDTO.getDataType());
        oldQdm.setExpansionIdentifier(qualityDataSetDTO.getExpansionIdentifier());
    }

    public final MeasureLibraryService getMeasureService() {
        return measureLibraryService;
    }

    /**
     * Find and update all oids.
     *
     * @param oldQdm    the old qdm
     * @param qds       the qds
     * @param list      the list
     * @param measureId the measure id
     * @return true, if successful
     */
    private boolean findAndUpdateAllOids(QualityDataSetDTO oldQdm, QualityDataSetDTO qds, List<QualityDataSetDTO> list, String measureId) {

        boolean findAllOid = false;
        boolean version = false;
        boolean expansionId = false;
        boolean isAllOidsMod = false;

        if ((qds.getVersion().equalsIgnoreCase("1.0") || qds.getVersion()
                .equalsIgnoreCase("1"))
                && (oldQdm.getVersion().equalsIgnoreCase("1.0") || oldQdm
                .getVersion().equalsIgnoreCase("1"))) {

            if ((qds.getExpansionIdentifier() != null)
                    && oldQdm.getExpansionIdentifier() == null) {
                findAllOid = true;
                expansionId = true;
            }

            if ((qds.getExpansionIdentifier() != null)
                    && oldQdm.getExpansionIdentifier() != null) {
                if (!oldQdm.getExpansionIdentifier().equalsIgnoreCase(
                        qds.getExpansionIdentifier())) {
                    findAllOid = true;
                    expansionId = true;
                }
            }

            if ((qds.getExpansionIdentifier() == null)
                    && oldQdm.getExpansionIdentifier() != null) {
                findAllOid = true;
                expansionId = true;
            }

        } else if ((!(qds.getVersion().equalsIgnoreCase("1.0") || qds
                .getVersion().equalsIgnoreCase("1")) && (oldQdm.getVersion()
                .equalsIgnoreCase("1.0") || oldQdm.getVersion()
                .equalsIgnoreCase("1")))
                || ((qds.getVersion().equalsIgnoreCase("1.0") || qds
                .getVersion().equalsIgnoreCase("1")) && !(oldQdm
                .getVersion().equalsIgnoreCase("1.0") || oldQdm
                .getVersion().equalsIgnoreCase("1")))) {
            findAllOid = true;
            version = true;

            if ((qds.getExpansionIdentifier() != null)
                    && oldQdm.getExpansionIdentifier() == null) {
                findAllOid = true;
                expansionId = true;
            } else if ((qds.getExpansionIdentifier() == null)
                    && oldQdm.getExpansionIdentifier() != null) {
                findAllOid = true;
                expansionId = true;
            }

        } else { //In case If it just a change from version to version.
            findAllOid = true;
            version = true;
        }

        if (findAllOid) {
            String oid = qds.getOid();
            String uuid = qds.getUuid();
            for (int i = 0; i < list.size(); i++) {
                QualityDataSetDTO qdmToEval = list.get(i);
                QualityDataSetDTO modifiableQDM = new QualityDataSetDTO();
                populatedOldQDM(modifiableQDM, qdmToEval);
                if (!uuid.equalsIgnoreCase(qdmToEval.getUuid())
                        && qdmToEval.getOid().equalsIgnoreCase(oid)) {
                    isAllOidsMod = true;
                    if (version) {
                        modifiableQDM.setVersion(qds.getVersion());
                    }
                    if (expansionId) {
                        modifiableQDM.setExpansionIdentifier(qds
                                .getExpansionIdentifier());
                    }
                    getMeasureService().updateMeasureXML(modifiableQDM,
                            qdmToEval, measureId);
                }
            }

        }
        return (findAllOid && isAllOidsMod);
    }

    @Override
    public int countSearchResultsWithFilter(String searchText, boolean defaultCodeList, int filter) {
        return 0;
    }


    @Override
    public List<CodeListSearchDTO> search(String searchText, int startIndex, int pageSize, String sortColumn,
                                          boolean isAsc, boolean defaultCodeList, int filter) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Cacheable(OID_TO_VSAC_CODE_SYSTEM_DTO)
    @Override
    public Map<String, VSACCodeSystemDTO> getOidToVsacCodeSystemMap() {
        //Note: Cache is auto evicted in the Application class after a default of 30 minutes.
        Map<String, VSACCodeSystemDTO> result = new HashMap<>();

        log.info("Entering getCodesSystemMappings");
        ResponseEntity<VSACCodeSystemDTO[]> response;
        try {
            response = restTemplate.exchange(mappingsUrl, HttpMethod.GET, null, VSACCodeSystemDTO[].class, (Object) null);
            if (response.getStatusCode().isError()) {
                throw new MatRuntimeException("getCodesSystemMappings returned error code " + response.getStatusCode());
            }
            Arrays.stream(response.getBody()).forEach(v -> result.put(v.getOid(),v));
            log.info("Exiting getCodesSystemMappings " + result);
        } catch (RestClientResponseException e) {
            throw new MatRuntimeException(e);
        }
        return result;
    }
}