package mat.server.service.impl;

import ca.uhn.fhir.context.FhirContext;
import cqltoelm.CQLFormatter;
import mat.client.measure.ManageCompositeMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.dao.ListObjectDAO;
import mat.dao.QualityDataSetDAO;
import mat.dao.clause.CQLLibraryDAO;
import mat.dao.clause.CQLLibraryExportDAO;
import mat.dao.clause.MeasureDAO;
import mat.dao.clause.MeasureExportDAO;
import mat.dao.clause.MeasureXMLDAO;
import mat.model.ListObject;
import mat.model.QualityDataSetDTO;
import mat.model.clause.CQLLibrary;
import mat.model.clause.CQLLibraryExport;
import mat.model.clause.ComponentMeasure;
import mat.model.clause.Measure;
import mat.model.clause.MeasureExport;
import mat.model.clause.MeasureXML;
import mat.model.cql.CQLModel;
import mat.server.CQLUtilityClass;
import mat.server.bonnie.BonnieServiceImpl;
import mat.server.bonnie.api.result.BonnieCalculatedResult;
import mat.server.export.ExportResult;
import mat.server.hqmf.Generator;
import mat.server.hqmf.HQMFGeneratorFactory;
import mat.server.humanreadable.HumanReadableGenerator;
import mat.server.logging.LogFactory;
import mat.server.service.SimpleEMeasureService;
import mat.server.service.impl.helper.ExportResultParser;
import mat.server.util.CQLUtil;
import mat.server.util.CompositeMeasureDetailUtil;
import mat.server.util.XmlProcessor;
import mat.shared.ConstantMessages;
import mat.shared.DateUtility;
import mat.shared.FileNameUtility;
import mat.shared.SaveUpdateCQLResult;
import mat.shared.StringUtility;
import mat.shared.bonnie.error.BonnieBadParameterException;
import mat.shared.bonnie.error.BonnieDoesNotExistException;
import mat.shared.bonnie.error.BonnieNotFoundException;
import mat.shared.bonnie.error.BonnieServerException;
import mat.shared.bonnie.error.BonnieUnauthorizedException;
import mat.vsacmodel.ValueSet;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.tools.zip.ZipOutputStream;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.ZipException;

@Service
public class SimpleEMeasureServiceImpl implements SimpleEMeasureService {
    private static final String CONVERSION_FILE_1 = "xsl/New_HQMF.xsl";
    private static final String CONVERSION_FILE_2 = "xsl/mat_narrGen.xsl";
    private static final String CONVERSION_FILE_HTML = "xsl/eMeasure.xsl";
    private static final String XPATH_SUPPLEMENTDATA_ELEMENTREF = "/measure/supplementalDataElements/elementRef/@id";
    private static final String XPATH_ALL_GROUPED_ELEMENTREF_ID = "/measure/measureGrouping/group/clause//elementRef[not(@id = preceding:: clause//elementRef/@id)]/@id";
    private static final String XPATH_ALL_GROUPED_ATTRIBUTES_UUID = "/measure/measureGrouping/group/clause//attribute[not(@qdmUUID = preceding:: clause//attribute/@qdmUUID)]/@qdmUUID";
    private static final String XPATH_ALL_SUBTREE_ELEMENTREF_ID = "/measure/subTreeLookUp/subTree//elementRef[not(@id = preceding:: subTree//elementRef/@id)]/@id";
    private static final String XPATH_ALL_SUBTREE_ATTRIBUTES_UUID = "/measure/subTreeLookUp/subTree//attribute[not(@qdmUUID = preceding:: subTree//attribute/@qdmUUID)]/@qdmUUID";
    private static final Log LOGGER = LogFactory.getLog(SimpleEMeasureServiceImpl.class);
    private static final String VERSION = "version";
    private static final String NAME = "name";

    private static final String XPATH_ELEMENTLOOKUP_QDM = "/measure/elementLookUp/qdm[not(@oid='" + ConstantMessages.USER_DEFINED_QDM_OID + "')]";

    @Autowired
    private MeasureDAO measureDAO;

    @Autowired
    private MeasureXMLDAO measureXMLDAO;

    @Autowired
    private MeasureExportDAO measureExportDAO;

    @Autowired
    private CQLLibraryDAO cqlLibraryDAO;

    @Autowired
    private CQLLibraryExportDAO cqlLibraryExportDAO;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private QualityDataSetDAO qualityDataSetDAO;

    @Autowired
    private ListObjectDAO listObjectDAO;

    @Autowired
    private HQMFGeneratorFactory hqmfGeneratoryFactory;

    @Autowired
    private BonnieServiceImpl bonnieServiceImpl;

    @Autowired
    private CompositeMeasureDetailUtil compositeMeasureDetailUtil;

    @Autowired
    private HumanReadableGenerator humanReadableGenerator;

    @Autowired
    private FhirContext fhirContext;

    private static String transform(Node node) {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        TransformerFactory transformerFactory = XMLUtility.getInstance().buildTransformerFactory();
        DOMSource source = new DOMSource(node);
        StreamResult result = new StreamResult(arrayOutputStream);

        try {
            transformerFactory.newTransformer().transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return arrayOutputStream.toString();
    }

    @Override
    public final ExportResult exportMeasureIntoSimpleXML(final String measureId,
                                                         final String xmlString,
                                                         final List<ValueSet> valueSets) throws Exception {
        ExportResult result = new ExportResult();
        DocumentBuilderFactory documentBuilderFactory = XMLUtility.getInstance().buildDocumentBuilderFactory();
        DocumentBuilder docBuilder = documentBuilderFactory.newDocumentBuilder();
        InputSource oldXmlstream = new InputSource(new StringReader(xmlString));
        Document originalDoc = docBuilder.parse(oldXmlstream);
        javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();

        List<String> qdmRefID = new ArrayList<>();
        List<String> supplRefID = new ArrayList<>();
        List<QualityDataSetDTO> masterRefID = new ArrayList<>();

        transform(originalDoc);
        NodeList allGroupedElementRefIDs = (NodeList) xPath.evaluate(XPATH_ALL_GROUPED_ELEMENTREF_ID,
                originalDoc.getDocumentElement(), XPathConstants.NODESET);
        NodeList allGroupedAttributesUUIDs = (NodeList) xPath.evaluate(XPATH_ALL_GROUPED_ATTRIBUTES_UUID,
                originalDoc.getDocumentElement(), XPathConstants.NODESET);
        NodeList allQDMRefIDs = (NodeList) xPath.evaluate(XPATH_ELEMENTLOOKUP_QDM, originalDoc.getDocumentElement(),
                XPathConstants.NODESET);
        NodeList allSupplementIDs = (NodeList) xPath.evaluate(XPATH_SUPPLEMENTDATA_ELEMENTREF,
                originalDoc.getDocumentElement(), XPathConstants.NODESET);
        NodeList allSubTreeElementRefIDs = (NodeList) xPath.evaluate(XPATH_ALL_SUBTREE_ELEMENTREF_ID,
                originalDoc.getDocumentElement(), XPathConstants.NODESET);

        NodeList allSubTreeAttributeIDs = (NodeList) xPath.evaluate(XPATH_ALL_SUBTREE_ATTRIBUTES_UUID,
                originalDoc.getDocumentElement(), XPathConstants.NODESET);

        for (int i = 0; i < allQDMRefIDs.getLength(); i++) {
            Node newNode = allQDMRefIDs.item(i);
            QualityDataSetDTO dataSetDTO = new QualityDataSetDTO();
            dataSetDTO.setId(newNode.getAttributes().getNamedItem("id").getNodeValue());
            dataSetDTO.setUuid(newNode.getAttributes().getNamedItem("uuid").getNodeValue());
            masterRefID.add(dataSetDTO);
        }

        findAndAddDTO(allGroupedElementRefIDs, masterRefID, qdmRefID);
        findAndAddDTO(allGroupedAttributesUUIDs, masterRefID, qdmRefID);
        findAndAddDTO(allSubTreeElementRefIDs, masterRefID, qdmRefID);
        findAndAddDTO(allSubTreeAttributeIDs, masterRefID, qdmRefID);

        Set<String> uniqueRefIds = new HashSet<>(qdmRefID);
        qdmRefID = new ArrayList<>(uniqueRefIds);

        findAndAddDTO(allSupplementIDs, masterRefID, supplRefID);
        HSSFWorkbook wkbk = createEMeasureXLS(measureId, qdmRefID, supplRefID, valueSets);
        result.setWkbkbarr(getHSSFWorkbookBytes(wkbk));
        return result;
    }

    private void findAndAddDTO(final NodeList nodeList, final List<QualityDataSetDTO> masterRefID,
                               final List<String> finalIdList) {

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node idNode = nodeList.item(i);
            String idNodeValue = idNode.getNodeValue();
            for (QualityDataSetDTO dataSetDTO : masterRefID) {
                if (dataSetDTO.getUuid().equalsIgnoreCase(idNodeValue)) {
                    finalIdList.add(dataSetDTO.getId());
                }
            }
        }
    }

    @Override
    public final ExportResult getSimpleXML(final String measureId) throws Exception {
        Measure measure = measureDAO.find(measureId);
        MeasureExport measureExport = getMeasureExport(measureId);
        if (measureExport == null) {
            return null;
        }
        ExportResult result = new ExportResult();
        result.setMeasureName(measure.getaBBRName());
        result.setExport(measureExport.getSimpleXML());
        return result;
    }

    @Override
    public final ExportResult getCQLLibraryFile(final String measureId) throws Exception {
        MeasureExport measureExport = getMeasureExport(measureId);
        String simpleXML = measureExport.getSimpleXML();

        CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(simpleXML);

        // get the name from the simple xml
        String xPathName = "/measure/cqlLookUp[1]/library[1]";
        XmlProcessor xmlProcessor = new XmlProcessor(simpleXML);
        Node cqlFileName = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), xPathName);

        String cqlFileString = CQLUtilityClass.getCqlString(cqlModel, "").getLeft();

        if (cqlFileString != null && !cqlFileString.isEmpty()) {
            CQLFormatter formatter = new CQLFormatter();
            cqlFileString = formatter.format(cqlFileString);
        }

        ExportResult result = new ExportResult();
        result.setMeasureName(measureExport.getMeasure().getaBBRName());
        result.setExport(cqlFileString);

        // if the cql file name is blank(before 4.5 measures), then we'll give the file
        // name as
        // the measure name.
        if (cqlFileName == null) {
            result.setCqlLibraryName(result.getMeasureName());
        } else {
            result.setCqlLibraryName(cqlModel.getLibraryName() + "-v" + cqlModel.getVersionUsed());
        }
        result.setCqlLibraryModelVersion(cqlModel.getUsingModelVersion());

        getIncludedCQLLibs(result, xmlProcessor);

        return result;
    }

    private void getIncludedCQLLibs(ExportResult result, XmlProcessor xmlProcessor) throws XPathExpressionException {

        String xPathForIncludedLibs = "//allUsedCQLLibs/lib[not( preceding::lib/@id =@id)]";
        NodeList includedCQLLibNodes = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(), xPathForIncludedLibs);

        for (int i = 0; i < includedCQLLibNodes.getLength(); i++) {
            Node libNode = includedCQLLibNodes.item(i);

            if (!isComposite(libNode)) {
                String libId = libNode.getAttributes().getNamedItem("id").getNodeValue();

                CQLLibrary cqlLibrary = this.cqlLibraryDAO.find(libId);
                CQLLibraryExport cqlLibraryExport = cqlLibraryExportDAO.findByLibraryId(cqlLibrary.getId());

                String includeCqlXMLString = new String(cqlLibrary.getCQLByteArray());
                if (cqlLibraryExport == null) {
                    cqlLibraryExport = new CQLLibraryExport();
                    cqlLibraryExport.setCqlLibrary(cqlLibrary);
                }
                if (cqlLibraryExport.getCql() == null) {
                    String cqlFileString = CQLUtilityClass.getCqlString(CQLUtilityClass.getCQLModelFromXML(includeCqlXMLString), "").getLeft();

                    try {
                        CQLFormatter formatter = new CQLFormatter();
                        cqlFileString = formatter.format(cqlFileString);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    cqlLibraryExport.setCql(cqlFileString);
                    cqlLibraryExportDAO.save(cqlLibraryExport);
                }
                ExportResult includeResult = new ExportResult();
                includeResult.setExport(cqlLibraryExport.getCql());

                String libName = libNode.getAttributes().getNamedItem(NAME).getNodeValue();
                String libVersion = libNode.getAttributes().getNamedItem(VERSION).getNodeValue();

                includeResult.setCqlLibraryName(libName + "-v" + libVersion);
                includeResult.setCqlLibraryModelVersion(cqlLibrary.isFhirLibrary() ? cqlLibrary.getFhirVersion() : cqlLibrary.getQdmVersion());

                result.getIncludedCQLExports().add(includeResult);
            }
        }
    }

    @Override
    public final ExportResult getJSONFile(final String measureId) throws Exception {
        MeasureExport measureExport = getMeasureExport(measureId);

        String measureSimpleXML = measureExport.getSimpleXML();
        XmlProcessor xmlProcessor = new XmlProcessor(measureSimpleXML);

        CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(measureSimpleXML);

        String cqlFileString = CQLUtilityClass.getCqlString(cqlModel, "").getLeft();
        ExportResult result = new ExportResult();
        result.setMeasureName(measureExport.getMeasure().getaBBRName());

        // if the cqlFile String is blank, don't even parse it.
        if (!cqlFileString.isEmpty()) {
            SaveUpdateCQLResult jsonResult = CQLUtil.generateELM(cqlModel, cqlLibraryDAO);
            result.setExport(jsonResult.getJsonString());
            result.setCqlLibraryName(cqlModel.getLibraryName() + "-v" + cqlModel.getVersionUsed());
        } else {
            result.setExport("");
            result.setMeasureName(measureExport.getMeasure().getaBBRName());
            result.setCqlLibraryName(result.getMeasureName());
        }
        result.setCqlLibraryModelVersion(cqlModel.getUsingModelVersion());
        getIncludedCQLJSONs(result, xmlProcessor);

        return result;
    }

    private void getIncludedCQLJSONs(ExportResult result, XmlProcessor xmlProcessor) throws XPathExpressionException {

        String xPathForIncludedLibs = "//allUsedCQLLibs/lib[not( preceding::lib/@id =@id)]";
        NodeList includedCQLLibNodes = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(), xPathForIncludedLibs);

        for (int i = 0; i < includedCQLLibNodes.getLength(); i++) {
            Node libNode = includedCQLLibNodes.item(i);
            if (!isComposite(libNode)) {
                String libId = libNode.getAttributes().getNamedItem("id").getNodeValue();
                CQLLibrary cqlLibrary = this.cqlLibraryDAO.find(libId);
                CQLLibraryExport cqlLibraryExport = cqlLibraryExportDAO.findByLibraryId(cqlLibrary.getId());
                String includeCqlXMLString = new String(cqlLibrary.getCQLByteArray());

                CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(includeCqlXMLString);
                if (cqlLibraryExport == null) {
                    cqlLibraryExport = new CQLLibraryExport();
                    cqlLibraryExport.setCqlLibrary(cqlLibrary);
                }
                if (cqlLibraryExport.getFhirJson() == null) {
                    SaveUpdateCQLResult jsonResult = CQLUtil.generateELM(cqlModel, cqlLibraryDAO);
                    cqlLibraryExport.setFhirJson(jsonResult.getJsonString());
                    cqlLibraryExportDAO.save(cqlLibraryExport);
                }
                ExportResult includeResult = new ExportResult();
                includeResult.setExport(cqlLibraryExport.getFhirJson());

                String libName = libNode.getAttributes().getNamedItem(NAME).getNodeValue();
                String libVersion = libNode.getAttributes().getNamedItem(VERSION).getNodeValue();

                includeResult.setCqlLibraryName(libName + "-v" + libVersion);
                includeResult.setCqlLibraryModelVersion(cqlLibrary.isFhirLibrary() ? cqlLibrary.getFhirVersion() : cqlLibrary.getQdmVersion());

                result.getIncludedCQLExports().add(includeResult);
            }
        }

    }


    @Override
    public final ExportResult getELMFile(final String measureId) throws Exception {
        MeasureExport measureExport = getMeasureExport(measureId);

        String measureSimpleXML = measureExport.getSimpleXML();
        XmlProcessor xmlProcessor = new XmlProcessor(measureSimpleXML);

        CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(measureSimpleXML);

        String cqlFileString = CQLUtilityClass.getCqlString(cqlModel, "").getLeft();
        ExportResult result = new ExportResult();
        result.setMeasureName(measureExport.getMeasure().getaBBRName());

        // if the cqlFile String is blank, don't even parse it.
        if (!cqlFileString.isEmpty()) {
            SaveUpdateCQLResult elmResult = CQLUtil.generateELM(cqlModel, cqlLibraryDAO);
            result.setExport(elmResult.getElmString());
            result.setCqlLibraryName(cqlModel.getLibraryName() + "-v" + cqlModel.getVersionUsed());
        } else {
            result.setExport("");
            result.setMeasureName(measureExport.getMeasure().getaBBRName());
            result.setCqlLibraryName(result.getMeasureName());
        }
        result.setCqlLibraryModelVersion(cqlModel.getUsingModelVersion());
        getIncludedCQLELMs(result, xmlProcessor);

        return result;
    }

    private void getIncludedCQLELMs(ExportResult result, XmlProcessor xmlProcessor) throws XPathExpressionException {
        String xPathForIncludedLibs = "//allUsedCQLLibs/lib[not( preceding::lib/@id =@id)]";
        NodeList includedCQLLibNodes = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(), xPathForIncludedLibs);

        for (int i = 0; i < includedCQLLibNodes.getLength(); i++) {
            Node libNode = includedCQLLibNodes.item(i);
            if (!isComposite(libNode)) {
                String libId = libNode.getAttributes().getNamedItem("id").getNodeValue();
                CQLLibrary cqlLibrary = this.cqlLibraryDAO.find(libId);
                CQLLibraryExport cqlLibraryExport = cqlLibraryExportDAO.findByLibraryId(cqlLibrary.getId());

                String includeCqlXMLString = new String(cqlLibrary.getCQLByteArray());

                CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(includeCqlXMLString);
                if (cqlLibraryExport == null) {
                    cqlLibraryExport = new CQLLibraryExport();
                    cqlLibraryExport.setCqlLibrary(cqlLibrary);
                }
                if (cqlLibraryExport.getElmXml() == null) {
                    SaveUpdateCQLResult elmResult = CQLUtil.generateELM(cqlModel, cqlLibraryDAO);
                    cqlLibraryExport.setElmXml(elmResult.getElmString());
                    cqlLibraryExportDAO.save(cqlLibraryExport);
                }
                ExportResult includeResult = new ExportResult();
                includeResult.setExport(cqlLibraryExport.getElmXml());

                String libName = libNode.getAttributes().getNamedItem(NAME).getNodeValue();
                String libVersion = libNode.getAttributes().getNamedItem(VERSION).getNodeValue();

                includeResult.setCqlLibraryName(libName + "-v" + libVersion);
                includeResult.setCqlLibraryModelVersion(cqlLibrary.isFhirLibrary() ? cqlLibrary.getFhirVersion() : cqlLibrary.getQdmVersion());

                result.getIncludedCQLExports().add(includeResult);
            }
        }
    }

    private boolean isComposite(Node libNode) {
        return libNode.getAttributes().getNamedItem("isComponent") != null &&
                "true".equals(libNode.getAttributes().getNamedItem("isComponent").getNodeValue());
    }

    @Override
    public final ExportResult getHQMFForv3Measure(final String measureId) {
        MeasureExport measureExport = getMeasureExport(measureId);

        ExportResult result = new ExportResult();
        result.setMeasureName(measureExport.getMeasure().getaBBRName());
        result.setExport(getHQMFForv3MeasureString(measureExport));

        return result;
    }

    public final ExportResult createOrGetHQMFForv3Measure(final String measureId) {
        MeasureExport measureExport = getMeasureExport(measureId);
        ExportResult result = new ExportResult();

        result.setMeasureName(measureExport.getMeasure().getaBBRName());
        if (measureExport.getHqmf() == null) {
            measureExport.setHqmf(getHQMFForv3MeasureString(measureExport));
            measureExportDAO.save(measureExport);
        }
        result.setExport(measureExport.getHqmf());
        return result;
    }

    private String getHQMFForv3MeasureString(final MeasureExport measureExport) {
        String tempXML = XMLUtility.getInstance().applyXSL(measureExport.getSimpleXML(), XMLUtility.getInstance().getXMLResource(CONVERSION_FILE_1));
        return XMLUtility.getInstance().applyXSL(tempXML, XMLUtility.getInstance().getXMLResource(CONVERSION_FILE_2));
    }

    @Override
    public final ExportResult getEMeasureHTML(final String measureId) throws Exception {
        ExportResult result = getHQMFForv3Measure(measureId);
        result.setExport(emeasureXMLToEmeasureHTML(result.getExport()));
        return result;
    }

    @Override
    public final ExportResult createOrGetEMeasureHTML(final String measureId) throws Exception {
        MeasureExport measureExport = getMeasureExport(measureId);
        ExportResult result = createOrGetHQMFForv3Measure(measureId);
        if (measureExport.getHumanReadable() == null) {
            measureExport.setHumanReadable(createOrGetEmeasureXMLToEmeasureHTML(result.getExport(), getMeasureExport(measureId)));
            measureExportDAO.save(measureExport);
        }
        result.setExport(measureExport.getHumanReadable());
        return result;
    }

    @Override
    public final ExportResult getHumanReadable(final String measureId, final String measureVersionNumber) throws Exception {
        MeasureExport measureExport = getMeasureExport(measureId);

        List<String> dataRequirementsNoCodeFilter = new ArrayList<>();
        if (measureExport.isFhir()) {
            ZipPackager zp = context.getBean(ZipPackagerFactory.class).getZipPackager();
            String bundleJson = zp.buildFhirMeasureJsonBundle(measureId);
            dataRequirementsNoCodeFilter = new ExportResultParser(bundleJson).parseDataRequirement();
        }
        String emeasureHTMLStr = getHumanReadableForMeasure(measureId, measureExport.getSimpleXML(), measureVersionNumber, dataRequirementsNoCodeFilter);

        ExportResult exportResult = new ExportResult();
        exportResult.setExport(emeasureHTMLStr);
        exportResult.setMeasureName(measureExport.getMeasure().getaBBRName());
        return exportResult;
    }

    @Override
    public final ExportResult createOrGetHumanReadable(final String measureId, final String measureVersionNumber) {
        MeasureExport measureExport = getMeasureExport(measureId);
        if (measureExport.getHumanReadable() == null) {
            List<String> dataRequirementsNoCodeFilter = new ArrayList<>();
            if (measureExport.isFhir()) {
                ZipPackager zp = context.getBean(ZipPackagerFactory.class).getZipPackager();
                String bundleJson = zp.buildFhirMeasureJsonBundle(measureId);
                dataRequirementsNoCodeFilter = new ExportResultParser(bundleJson).parseDataRequirement();
            }
            measureExport.setHumanReadable(getHumanReadableForMeasure(measureId, measureExport.getSimpleXML(), measureVersionNumber, dataRequirementsNoCodeFilter));
            measureExportDAO.save(measureExport);
        }

        ExportResult exportResult = new ExportResult();
        exportResult.setExport(measureExport.getHumanReadable());
        exportResult.setMeasureName(measureExport.getMeasure().getaBBRName());
        return exportResult;
    }

    private String createOrGetEmeasureXMLToEmeasureHTML(final String emeasureXMLStr, final MeasureExport measureExport) {
        if (measureExport.getHumanReadable() == null) {
            String html = XMLUtility.getInstance().applyXSL(emeasureXMLStr, XMLUtility.getInstance().getXMLResource(CONVERSION_FILE_HTML));
            measureExport.setHumanReadable(html);
            measureExportDAO.save(measureExport);
        }

        return measureExport.getHumanReadable();
    }

    private String emeasureXMLToEmeasureHTML(final String eMeasureXMLStr) {
        return XMLUtility.getInstance().applyXSL(eMeasureXMLStr, XMLUtility.getInstance().getXMLResource(CONVERSION_FILE_HTML));
    }

    @Override
    public ExportResult getHumanReadableForNode(final String measureId, final String populationSubXML)
            throws Exception {
        ExportResult result = new ExportResult();

        MeasureXML measureExport = measureXMLDAO.findForMeasure(measureId);
        String measureXML = measureExport.getMeasureXMLAsString();

        result.setExport(humanReadableGenerator.generateHTMLForPopulationOrSubtree(measureId, populationSubXML, measureXML, cqlLibraryDAO));
        return result;
    }

    public void setApplicationContext(final ApplicationContext ctx) {
        this.context = ctx;
    }

    public final HSSFWorkbook createEMeasureXLS(final String measureId, final List<String> allQDMs,
                                                final List<String> supplementalQDMS, final List<ValueSet> valueSets) {
        CodeListXLSGenerator codeListXLSGenerator = new CodeListXLSGenerator();
        return codeListXLSGenerator.getXLS(measureDAO.find(measureId), allQDMs, qualityDataSetDAO, listObjectDAO, supplementalQDMS, valueSets);
    }

    public final HSSFWorkbook createErrorEMeasureXLS() {
        CodeListXLSGenerator clgen = new CodeListXLSGenerator();
        return clgen.getErrorXLS();
    }

    @Override
    public final ExportResult getEMeasureXLS(final String measureId) throws Exception {
        ExportResult result = new ExportResult();
        Measure measure = measureDAO.find(measureId);
        result.setMeasureName(measure.getaBBRName());
        result.setPackageDate(DateUtility.convertDateToStringNoTime2(measure.getValueSetDate()));
        MeasureExport measureExport = getMeasureExport(measureId);
        if (measureExport.getCodeList() == null) {
            byte[] codes = getHSSFWorkbookBytes(createErrorEMeasureXLS());
            measureExport.setCodeListBarr(codes);
            measureExportDAO.save(measureExport);
            result.setWkbkbarr(codes);
        } else {
            result.setWkbkbarr(measureExport.getCodeListBarr());
        }
        return result;
    }

    @Override
    public final ExportResult getValueSetXLS(final String valueSetId) throws Exception {
        ExportResult result = new ExportResult();
        ListObject listObject = listObjectDAO.find(valueSetId);
        ValueSetXLSGenerator vsxg = new ValueSetXLSGenerator();
        HSSFWorkbook workBook;
        try {
            workBook = vsxg.getXLS(valueSetId, listObject);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            workBook = vsxg.getErrorXLS();
        }
        result.setWkbkbarr(vsxg.getHSSFWorkbookBytes(workBook));
        result.setValueSetName(listObject.getName());
        result.setLastModifiedDate(listObject.getLastModified() != null ? DateUtility.convertDateToStringNoTime2(listObject.getLastModified()) : null);
        return result;
    }

    /**
     * Gets the hSSF workbook bytes.
     *
     * @param hssfwkbk - HSSFWorkbook.
     * @return byte[].
     * @throws IOException - IOException. *
     */
    private byte[] getHSSFWorkbookBytes(final HSSFWorkbook hssfwkbk) throws IOException {
        CodeListXLSGenerator clgen = new CodeListXLSGenerator();
        return clgen.getHSSFWorkbookBytes(hssfwkbk);
    }

    @Override
    public final ExportResult getEMeasureZIP(final String measureId, final Date exportDate) throws Exception {
        var result = new ExportResult();
        result.setMeasureName(measureDAO.find(measureId).getaBBRName());
        var measureExport = getMeasureExport(measureId);
        if (measureExport.getMeasure().getReleaseVersion().equals("v3")) {
            // Measures with release version V3 are QDM/QDM and cannot be exported in MAT
            result.setZipbarr(getZipBarr(measureId, measureExport, measureExport.getMeasure().getReleaseVersion()));
        } else {
            var parentPath = FileNameUtility.getExportFileName(measureExport.getMeasure());
            var baos = new ByteArrayOutputStream();
            var zip = new ZipOutputStream(baos);
            getZipBarr(measureId, measureExport, parentPath, zip);
            zip.close();
            result.setZipbarr(baos.toByteArray());
        }
        return result;
    }

    public final ExportResult getCompositeExportResult(final String compositeMeasureId, List<ComponentMeasure> componentMeasures) throws Exception {
        MeasureExport compositeMeasureExport = getMeasureExport(compositeMeasureId);
        ExportResult result = new ExportResult();
//        result.setMeasureName(measureDAO.find(compositeMeasureId).getaBBRName());
        result.setZipbarr(getCompositeZipBarr(compositeMeasureId, compositeMeasureExport, componentMeasures));
        return result;
    }
    /*
        1. For QDM measures, getZipBarr() generates eCQM, HR, json, elm and cql. It also generates json, elm and cql files for all included libraries if available.
        2. This method is used to generate measure bundle, Composite measure bundle and component measures inside a composite measures.
        3. For FHIR measures json, xml and HR are generated.
    */
    public final void getZipBarr(final String measureId, final MeasureExport measureExport, final String parentPath, ZipOutputStream zip) throws Exception {
        String simpleXmlStr = measureExport.getSimpleXML();

        ExportResult exportResult = createOrGetHQMF(measureId);
        String eMeasureXML = exportResult.getExport();

        ExportResult cqlExportResult = createOrGetCQLLibraryFile(measureId, measureExport);
        ExportResult elmExportResult = createOrGetELMLibraryFile(measureId, measureExport);
        ExportResult jsonExportResult = createOrGetJSONLibraryFile(measureId, measureExport);

        List<String> dataRequirementsNoValueSet = new ArrayList<>();
        String measureJsonBundle = null;
        ZipPackager zp = context.getBean(ZipPackagerFactory.class).getZipPackager();

        if (measureExport.isFhir()) {
            measureJsonBundle = zp.buildFhirMeasureJsonBundle(measureId);
            dataRequirementsNoValueSet = new ExportResultParser(measureJsonBundle).parseDataRequirement();
        }

        String eMeasureHTMLStr = createOrGetHumanReadableFile(measureId, measureExport, simpleXmlStr, dataRequirementsNoValueSet);

        zp.getZipBarr(zip, eMeasureHTMLStr, eMeasureXML, cqlExportResult, elmExportResult, jsonExportResult,
                measureExport.getMeasure().getReleaseVersion(), parentPath, measureId, measureJsonBundle);
    }

    public final byte[] getCompositeZipBarr(final String measureId, final MeasureExport compositeMeasureExport, List<ComponentMeasure> componentMeasures) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(baos);
        String parentSimpleXML = compositeMeasureExport.getSimpleXML();

        //get composite file
        String parentPath = FileNameUtility.getExportFileName(compositeMeasureExport.getMeasure());
        getZipBarr(measureId, compositeMeasureExport, parentPath, zip);
        //get component files
        for (ComponentMeasure measure : componentMeasures) {
            String componentMeasureId = measure.getComponentMeasure().getId();
            if (checkIfComponentMeasureIsUsed(parentSimpleXML, componentMeasureId)) {
                MeasureExport componentMeasureExport = getMeasureExport(componentMeasureId);
                String componentParentPath = parentPath + File.separator + FileNameUtility.getExportFileName(componentMeasureExport.getMeasure());
                getZipBarr(componentMeasureId, componentMeasureExport, componentParentPath, zip);
            }
        }

        zip.close();
        return baos.toByteArray();
    }

    private boolean checkIfComponentMeasureIsUsed(String parentSimpleXML, String componentMeasureId) throws MarshalException, ValidationException, IOException, MappingException, XPathExpressionException {

        ManageCompositeMeasureDetailModel usedCompositeModel = compositeMeasureDetailUtil.convertXMLIntoCompositeMeasureDetailModel(parentSimpleXML);
        List<Result> usedCompositeMeasures = usedCompositeModel.getAppliedComponentMeasures();
        for (Result usedCompositeMeasure : usedCompositeMeasures) {
            String measureId = String.valueOf(usedCompositeMeasure.getId());
            measureId = StringUtils.replace(measureId, "-", "");
            if (measureId.equals(componentMeasureId)) {
                return true;
            }
        }
        return false;

    }

    private String getHumanReadableForMeasure(String measureId, String simpleXmlStr, String measureVersionNumber, List<String> dataRequirementsNoValueSet) {
        return humanReadableGenerator.generateHTMLForMeasure(measureId, simpleXmlStr, measureVersionNumber, cqlLibraryDAO, dataRequirementsNoValueSet);
    }

    public ExportResult getHQMF(String measureId) {
        MeasureExport measureExport = getMeasureExport(measureId);

        ExportResult result = new ExportResult();
        result.setMeasureName(measureExport.getMeasure().getaBBRName());
        result.setExport(getHQMFString(measureExport));
        return result;
    }

    public ExportResult createOrGetHQMF(String measureId) {
        MeasureExport measureExport = getMeasureExport(measureId);
        if (measureExport.getHqmf() == null) {
            measureExport.setHqmf(getHQMFString(measureExport));
            measureExportDAO.save(measureExport);
        }

        ExportResult result = new ExportResult();
        result.setMeasureName(measureExport.getMeasure().getaBBRName());
        result.setExport(measureExport.getHqmf());
        return result;
    }

    private String getHQMFString(MeasureExport measureExport) {
        Generator hqmfGenerator = hqmfGeneratoryFactory.getHQMFGenerator(measureExport.getMeasure().getReleaseVersion());
        String hqmf = "";
        try {
            hqmf = hqmfGenerator.generate(measureExport);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hqmf;
    }

    /**
     * Gets the zip barr.
     *
     * @param measureId - String.
     * @param measureExport        - MeasureExport.
     * @return byte[].
     * @throws Exception - Exception. *
     */
    public final byte[] getZipBarr(final String measureId, final MeasureExport measureExport,
                                   String releaseVersion) throws Exception {
        ExportResult emeasureXMLResult = createOrGetHQMFForv3Measure(measureId);
        String emeasureName = emeasureXMLResult.getMeasureName();
        String emeasureXMLStr = emeasureXMLResult.getExport();
        String repee = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String repor = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + StringUtility.nl
                + "<?xml-stylesheet type=\"text/xsl\" href=\"xslt/eMeasure.xsl\"?>";
        emeasureXMLStr = repor + emeasureXMLStr.substring(repee.length());
        if (measureExport.getHumanReadable() == null) {
            measureExport.setHumanReadable(createOrGetEmeasureXMLToEmeasureHTML(emeasureXMLStr, measureExport));
            measureExportDAO.save(measureExport);
        }
        String emeasureHTMLStr = measureExport.getHumanReadable();
        String emeasureXSLUrl = XMLUtility.getInstance().getXMLResource(CONVERSION_FILE_HTML);

        ZipPackager zp = context.getBean(ZipPackagerFactory.class).getZipPackager();
        return zp.getZipBarr(emeasureName, releaseVersion, emeasureXMLStr, emeasureHTMLStr, emeasureXSLUrl);
    }

    // gets the measureExport and updates the simpleXml with FinalizeDate if not available
    public MeasureExport getMeasureExport(final String measureId) {
        MeasureExport measureExport = measureExportDAO.findByMeasureId(measureId);
        if (measureExport == null) {
            return null;
        }
        String emeasureXMLStr = measureExport.getSimpleXML();
        mat.model.clause.Measure measure = measureDAO.find(measureId);
        Timestamp fdts = measure.getFinalizedDate();

        // 1 add finalizedDate field
        if (fdts != null && !emeasureXMLStr.contains("<finalizedDate")) {
            String fdstr = convertTimestampToString(fdts);
            String repee = "</measureDetails>";
            String repor = StringUtility.nl + "<finalizedDate value=\"" + fdstr + "\"/>" + StringUtility.nl + "</measureDetails>";
            int offset = emeasureXMLStr.indexOf(repee);
            emeasureXMLStr = emeasureXMLStr.substring(0, offset) + repor
                    + emeasureXMLStr.substring(offset + repee.length());
            measureExport.setSimpleXML(emeasureXMLStr);
        }

        return measureExport;
    }

    /**
     * Convert timestamp to string.
     *
     * @param ts - Time Stamp.
     * @return yyyymmddhhss-zzzz
     */
    @SuppressWarnings("deprecation")
    private String convertTimestampToString(final Timestamp ts) {
        String hours = getTwoDigitString(ts.getHours());
        String mins = getTwoDigitString(ts.getMinutes());
        String month = getTwoDigitString(ts.getMonth() + 1);
        String day = getTwoDigitString(ts.getDate());
        String timeZone = "-" + getTwoDigitString(ts.getTimezoneOffset() / 60) + "00";

        return (ts.getYear() + 1900) + month + day + hours + mins + timeZone;
    }

    private String getTwoDigitString(final int i) {
        String ret = i + "";
        if (ret.length() == 1) {
            ret = "0" + ret;
        }
        return ret;
    }

    @Override
    public final ExportResult getBulkExportZIP(final String[] measureIds) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ExportResult result = null;

        try (ZipOutputStream zip = new ZipOutputStream(baos)) {
            Map<String, byte[]> filesMap = new HashMap<>();

            for (String measureId : measureIds) {
                result = new ExportResult();

                var measure = measureDAO.find(measureId);
                result.setMeasureName(measure.getaBBRName());

                var measureExport = getMeasureExport(measureId);
                if (measureExport.getMeasure().getReleaseVersion().equals("v3")) {
                    createFilesInBulkZip(measureId, measureExport, filesMap);
                } else if (Boolean.TRUE.equals(measureExport.getMeasure().getIsCompositeMeasure())) {
                    createCompositeFilesInBuildZip(measureId, measureExport, filesMap);
                } else {
                    String parentPath = FileNameUtility.getExportFileName(measure);
                    createFilesInBulkZip(measureId, measureExport, filesMap, parentPath);
                }
            }

            ZipPackager zp = context.getBean(ZipPackagerFactory.class).getZipPackager();
            double size = 1024 * 1024 * 100;
            Set<Entry<String, byte[]>> set = filesMap.entrySet();
            for (Entry<String, byte[]> fileArr : set) {
                zp.addBytesToZip(fileArr.getKey(), fileArr.getValue(), zip);
            }
            if (baos.size() > size) {
                throw new ZipException("Exceeded Limit :" + baos.size());
            }
        }
        LOGGER.debug(baos.size());
        result.setZipbarr(baos.toByteArray());
        return result;
    }

    public void createFilesInBulkZip(final String measureId, final MeasureExport measureExport,
                                     final Map<String, byte[]> filesMap, String parentPath) throws Exception {
        String simpleXmlStr = measureExport.getSimpleXML();

        ZipPackager zp = context.getBean(ZipPackagerFactory.class).getZipPackager();
        List<String> dataRequirementsNoValueSet = new ArrayList<>();
        if (measureExport.isFhir()) {
            String measureJsonBundle = zp.buildFhirMeasureJsonBundle(measureId);
            dataRequirementsNoValueSet = new ExportResultParser(measureJsonBundle).parseDataRequirement();
        }

        String emeasureHTMLStr = createOrGetHumanReadableFile(measureId, measureExport, simpleXmlStr, dataRequirementsNoValueSet);
        ExportResult emeasureExportResult = createOrGetHQMF(measureId);
        String emeasureXMLStr = emeasureExportResult.getExport();
        String currentReleaseVersion = measureExport.getMeasure().getReleaseVersion();

        ExportResult cqlExportResult = createOrGetCQLLibraryFile(measureId, measureExport);
        ExportResult elmExportResult = createOrGetELMLibraryFile(measureId, measureExport);
        ExportResult jsonExportResult = createOrGetJSONLibraryFile(measureId, measureExport);

        zp.createBulkExportZip(emeasureXMLStr, emeasureHTMLStr,
                filesMap, currentReleaseVersion, cqlExportResult, elmExportResult,
                jsonExportResult, parentPath, measureExport);
    }

    private void createCompositeFilesInBuildZip(String measureId, MeasureExport measureExport, Map<String, byte[]> filesMap) throws Exception {
        Measure compositeMeasure = measureExport.getMeasure();
        List<ComponentMeasure> componentMeasures = compositeMeasure.getComponentMeasures();
        String parentSimpleXML = measureExport.getSimpleXML();
        String parentPath = FileNameUtility.getExportFileName(compositeMeasure);
        //get composite file
        createFilesInBulkZip(measureId, measureExport, filesMap, parentPath);
        //get component files
        for (ComponentMeasure measure : componentMeasures) {
            String componentMeasureId = measure.getComponentMeasure().getId();
            if (checkIfComponentMeasureIsUsed(parentSimpleXML, componentMeasureId)) {
                MeasureExport componentMeasureExport = getMeasureExport(componentMeasureId);
                String componentParentPath = parentPath + File.separator + FileNameUtility.getExportFileName(componentMeasureExport.getMeasure());
                createFilesInBulkZip(componentMeasureId, componentMeasureExport, filesMap, componentParentPath);
            }
        }
    }

    public final void createFilesInBulkZip(final String measureId, final MeasureExport measureExport,
                                           final Map<String, byte[]> filesMap) throws Exception {
        ExportResult emeasureXMLResult = createOrGetHQMFForv3Measure(measureId);
        String emeasureXMLStr = emeasureXMLResult.getExport();
        String repee = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String repor = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + StringUtility.nl
                + "<?xml-stylesheet type=\"text/xsl\" href=\"xslt/eMeasure.xsl\"?>";
        emeasureXMLStr = repor + emeasureXMLStr.substring(repee.length());
        String emeasureHTMLStr = createOrGetEmeasureXMLToEmeasureHTML(emeasureXMLStr, measureExport);
        String emeasureXSLUrl = XMLUtility.getInstance().getXMLResource(CONVERSION_FILE_HTML);

        ExportResult cqlExportResult = createOrGetCQLLibraryFile(measureId, measureExport);
        ExportResult elmExportResult = createOrGetELMLibraryFile(measureId, measureExport);
        ExportResult jsonExportResult = createOrGetJSONLibraryFile(measureId, measureExport);

        ZipPackager zp = context.getBean(ZipPackagerFactory.class).getZipPackager();
        zp.createBulkExportZip(emeasureXMLStr, emeasureHTMLStr, emeasureXSLUrl,
                filesMap, measureExport.getMeasure().getReleaseVersion(),
                cqlExportResult, elmExportResult, jsonExportResult, measureExport.getMeasure());
    }


    public ExportResult createOrGetCQLLibraryFile(String measureId, MeasureExport measureExport) throws Exception {
        ExportResult cqlExportResult;
        /*if measure export is null then create the file
        else create the export result from the cql in the model and return that;*/
        if (measureExport.getCql() == null) {
            cqlExportResult = getCQLLibraryFile(measureId);
            measureExport.setCql(cqlExportResult.getExport());
            measureExportDAO.save(measureExport);
        } else {
            CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(measureExport.getSimpleXML());
            cqlExportResult = createExportResultForFile(measureExport, measureExport.getCql(), cqlModel);
            String simpleXML = measureExport.getSimpleXML();
            XmlProcessor xmlProcessor = new XmlProcessor(simpleXML);
            // find included CQL libraries and add them to result
            getIncludedCQLLibs(cqlExportResult, xmlProcessor);
        }

        return cqlExportResult;
    }

    public String createOrGetHumanReadableFile(String measureId,
                                               MeasureExport measureExport,
                                               String simpleXmlStr,
                                               List<String> dataRequirementsNoValueSet) throws Exception {
        //if measure export is null then create the file
        if (measureExport.getHumanReadable() == null) {
            measureExport.setHumanReadable(getHumanReadableForMeasure(measureId,
                    simpleXmlStr,
                    measureExport.getMeasure().getReleaseVersion(),
                    dataRequirementsNoValueSet));
            measureExportDAO.save(measureExport);
        }
        return measureExport.getHumanReadable();
    }

    public ExportResult createOrGetELMLibraryFile(String measureId, MeasureExport measureExport) throws Exception {
        ExportResult elmExportResult;
        /*if measure export is null then create the file
        else create the export result from the elm in the model and return that*/
        if (measureExport.getElmXml() == null) {
            elmExportResult = getELMFile(measureId);
            measureExport.setElmXml(elmExportResult.getExport());
            measureExportDAO.save(measureExport);
        } else {
            CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(measureExport.getSimpleXML());
            elmExportResult = createExportResultForFile(measureExport, measureExport.getElmXml(), cqlModel);
            String simpleXML = measureExport.getSimpleXML();
            XmlProcessor xmlProcessor = new XmlProcessor(simpleXML);
            getIncludedCQLELMs(elmExportResult, xmlProcessor);
        }
        return elmExportResult;
    }

    public ExportResult createOrGetJSONLibraryFile(String measureId, MeasureExport measureExport) throws Exception {
        ExportResult jsonExportResult;
        /*if measure export is null then create the file
        else create the export result from the json in the model and return that*/
        if (measureExport.getMeasureJson() == null) {
            jsonExportResult = getJSONFile(measureId);
            measureExport.setMeasureJson(jsonExportResult.getExport());
            measureExportDAO.save(measureExport);
        } else {
            CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(measureExport.getSimpleXML());
            jsonExportResult = createExportResultForFile(measureExport, measureExport.getMeasureJson(), cqlModel);
            String simpleXML = measureExport.getSimpleXML();
            XmlProcessor xmlProcessor = new XmlProcessor(simpleXML);
            getIncludedCQLJSONs(jsonExportResult, xmlProcessor);
        }
        return jsonExportResult;
    }

    @Override
    public ExportResult getMeasureBundleExportResult(MeasureExport measureExport, String fileType) {
        ExportResult exportResult = new ExportResult();
        Measure measure = measureExport.getMeasure();
        exportResult.setMeasureName(measure.getaBBRName());

        ZipPackager zp = context.getBean(ZipPackagerFactory.class).getZipPackager();
        exportResult.setExport(zp.buildMeasureBundle(fhirContext, measureExport.getMeasureJson(), measureExport.getFhirIncludedLibsJson()));

        if (fileType.equals("xml")) {
            exportResult.setExport(zp.convertToXmlBundle(exportResult.getExport()));
        }
        exportResult.setCqlLibraryName(measure.getCqlLibraryName() + "-v" + FileNameUtility.getMeasureVersion(measure));
        exportResult.setCqlLibraryModelVersion(measure.getModelVersion());

        return exportResult;
    }

    private ExportResult createExportResultForFile(MeasureExport measureExport, String fileString, CQLModel cqlModel) {
        ExportResult result = new ExportResult();
        result.setMeasureName(measureExport.getMeasure().getaBBRName());
        result.setExport(fileString);
        result.setCqlLibraryName(fileString == null ? result.getMeasureName() : cqlModel.getLibraryName() + "-v" + cqlModel.getVersionUsed());
        result.setCqlLibraryModelVersion(cqlModel.getUsingModelVersion());
        return result;
    }

    @Override
    public BonnieCalculatedResult getBonnieExportCalculation(String measureId, String userId) throws IOException, BonnieUnauthorizedException, BonnieNotFoundException, BonnieServerException, BonnieBadParameterException, BonnieDoesNotExistException {
        return bonnieServiceImpl.getBonnieExportForMeasure(userId, measureId);
    }

}
