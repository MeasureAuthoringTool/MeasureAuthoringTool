package mat.server.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.ZipException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

import mat.CQLFormatter;
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
import mat.model.MatValueSet;
import mat.model.QualityDataSetDTO;
import mat.model.clause.CQLLibrary;
import mat.model.clause.CQLLibraryExport;
import mat.model.clause.ComponentMeasure;
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
import mat.server.service.MeasurePackageService;
import mat.server.service.SimpleEMeasureService;
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

@Service
public class SimpleEMeasureServiceImpl implements SimpleEMeasureService {

	private static final String conversionFile1 = "xsl/New_HQMF.xsl";
	
	private static final String conversionFile2 = "xsl/mat_narrGen.xsl";
	
	private static final String conversionFileHtml = "xsl/eMeasure.xsl";
	
	private static String userDefinedOID = ConstantMessages.USER_DEFINED_QDM_OID;
	
	private static final String XPATH_ELEMENTLOOKUP_QDM = "/measure/elementLookUp/qdm[not(@oid='" + userDefinedOID +"')]";

	private static final String XPATH_SUPPLEMENTDATA_ELEMENTREF = "/measure/supplementalDataElements/elementRef/@id";
	
	private static final String XPATH_ALL_GROUPED_ELEMENTREF_ID = "/measure/measureGrouping/group/clause//elementRef[not(@id = preceding:: clause//elementRef/@id)]/@id";

	private static final String XPATH_ALL_GROUPED_ATTRIBUTES_UUID = "/measure/measureGrouping/group/clause//attribute[not(@qdmUUID = preceding:: clause//attribute/@qdmUUID)]/@qdmUUID";

	private static final String XPATH_ALL_SUBTREE_ELEMENTREF_ID = "/measure/subTreeLookUp/subTree//elementRef[not(@id = preceding:: subTree//elementRef/@id)]/@id";

	private static final String XPATH_ALL_SUBTREE_ATTRIBUTES_UUID = "/measure/subTreeLookUp/subTree//attribute[not(@qdmUUID = preceding:: subTree//attribute/@qdmUUID)]/@qdmUUID";
	
	private static final Log LOGGER = LogFactory.getLog(SimpleEMeasureServiceImpl.class);

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

	/** MeasureExportDAO. **/
	private HSSFWorkbook wkbk = null;
	
	@Autowired
	private HumanReadableGenerator humanReadableGenerator;

	@Override
	public final ExportResult exportMeasureIntoSimpleXML(final String measureId, final String xmlString, final List<MatValueSet> matValueSets) throws Exception {
		ExportResult result = new ExportResult();
		DocumentBuilderFactory documentBuilderFactory = XMLUtility.getInstance().buildDocumentBuilderFactory();
		DocumentBuilder docBuilder = documentBuilderFactory.newDocumentBuilder();
		InputSource oldXmlstream = new InputSource(new StringReader(xmlString));
		Document originalDoc = docBuilder.parse(oldXmlstream);
		javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();

		List<String> qdmRefID = new ArrayList<String>();
		List<String> supplRefID = new ArrayList<String>();
		List<QualityDataSetDTO> masterRefID = new ArrayList<QualityDataSetDTO>();

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
			dataSetDTO.setId(newNode.getAttributes().getNamedItem("id").getNodeValue().toString());
			dataSetDTO.setUuid(newNode.getAttributes().getNamedItem("uuid").getNodeValue().toString());
			masterRefID.add(dataSetDTO);
		}

		findAndAddDTO(allGroupedElementRefIDs, masterRefID, qdmRefID);
		findAndAddDTO(allGroupedAttributesUUIDs, masterRefID, qdmRefID);
		findAndAddDTO(allSubTreeElementRefIDs, masterRefID, qdmRefID);
		findAndAddDTO(allSubTreeAttributeIDs, masterRefID, qdmRefID);

		Set<String> uniqueRefIds = new HashSet<String>(qdmRefID);
		qdmRefID = new ArrayList<String>(uniqueRefIds);

		findAndAddDTO(allSupplementIDs, masterRefID, supplRefID);
		wkbk = createEMeasureXLS(measureId, qdmRefID, supplRefID, matValueSets);
		result.wkbkbarr = getHSSFWorkbookBytes(wkbk);
		wkbk = null;
		return result;
	}

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

	/**
	 * Find and add dto.
	 * 
	 * @param nodeList
	 *            - NodeList.
	 * @param masterRefID
	 *            - List of QualityDataSetDTO.
	 * @param finalIdList
	 *            - List of String. *
	 */
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

	/**
	 * Gets the measure name.
	 * 
	 * @param measureId
	 *            - String.
	 * @return Measure. *
	 */
	private mat.model.clause.Measure getMeasureName(final String measureId) {
		MeasurePackageService measureService = (MeasurePackageService) context.getBean("measurePackageService");
		mat.model.clause.Measure measure = measureService.getById(measureId);
		return measure;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.server.service.SimpleEMeasureService#getSimpleXML(java.lang.String)
	 */
	@Override
	public final ExportResult getSimpleXML(final String measureId) throws Exception {
		mat.model.clause.Measure measure = measureDAO.find(measureId);
		MeasureExport measureExport = getMeasureExport(measureId);
		if (measureExport == null) {
			return null;
		}
		ExportResult result = new ExportResult();
		result.measureName = measure.getaBBRName();
		result.export = measureExport.getSimpleXML();
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
		
		String cqlFileString = CQLUtilityClass.getCqlString(cqlModel, "").toString();

		if (cqlFileString != null && !cqlFileString.isEmpty()) {
			CQLFormatter formatter = new CQLFormatter(); 
			cqlFileString = formatter.format(cqlFileString);
		}

		ExportResult result = new ExportResult();
		result.measureName = measureExport.getMeasure().getaBBRName();
		result.export = cqlFileString;
		
		// if the cql file name is blank(before 4.5 measures), then we'll give the file
		// name as
		// the measure name.
		if (cqlFileName == null) {
			result.setCqlLibraryName(result.measureName);
		} else {
			result.setCqlLibraryName(cqlModel.getLibraryName() + "-" + cqlModel.getVersionUsed());
		}

		getIncludedCQLLibs(result, xmlProcessor);
		
		return result;
	}

	private void getIncludedCQLLibs(ExportResult result, XmlProcessor xmlProcessor) throws XPathExpressionException {

		String xPathForIncludedLibs = "//allUsedCQLLibs/lib[not( preceding::lib/@id =@id)]";
		NodeList includedCQLLibNodes = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(), xPathForIncludedLibs);

		for (int i = 0; i < includedCQLLibNodes.getLength(); i++) {
			Node libNode = includedCQLLibNodes.item(i);
			
			if(!isComposite(libNode)) {
				String libId = libNode.getAttributes().getNamedItem("id").getNodeValue();
				
				CQLLibrary cqlLibrary = this.cqlLibraryDAO.find(libId);
				CQLLibraryExport cqlLibraryExport = cqlLibraryExportDAO.findByLibraryId(cqlLibrary.getId());
	
				String includeCqlXMLString = new String(cqlLibrary.getCQLByteArray());
				if(cqlLibraryExport == null) {
					cqlLibraryExport = new CQLLibraryExport();
					cqlLibraryExport.setCqlLibrary(cqlLibrary);
				}
				if(cqlLibraryExport.getCql() == null) {
					String cqlFileString = CQLUtilityClass.getCqlString(CQLUtilityClass.getCQLModelFromXML(includeCqlXMLString), "");
		
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
				includeResult.export = cqlLibraryExport.getCql();
	
				String libName = libNode.getAttributes().getNamedItem("name").getNodeValue();
				String libVersion = libNode.getAttributes().getNamedItem("version").getNodeValue();
	
				includeResult.setCqlLibraryName(libName + "-" + libVersion);
	
				result.includedCQLExports.add(includeResult);
			}
		}
	}

	@Override
	public final ExportResult getJSONFile(final String measureId) throws Exception {
		MeasureExport measureExport = getMeasureExport(measureId);

		String measureSimpleXML = measureExport.getSimpleXML();
		XmlProcessor xmlProcessor = new XmlProcessor(measureSimpleXML);

		CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(measureSimpleXML);

		String cqlFileString = CQLUtilityClass.getCqlString(cqlModel, "");
		ExportResult result = new ExportResult();
		result.measureName = measureExport.getMeasure().getaBBRName();
		

		// if the cqlFile String is blank, don't even parse it.
		if (!cqlFileString.isEmpty()) {
			SaveUpdateCQLResult jsonResult = CQLUtil.generateELM(cqlModel, cqlLibraryDAO);
			result.export = jsonResult.getJsonString();
			result.setCqlLibraryName(cqlModel.getLibraryName() + "-" + cqlModel.getVersionUsed());
		} else {
			result.export = "";
			result.measureName = measureExport.getMeasure().getaBBRName();
			result.setCqlLibraryName(result.measureName);
		}

		getIncludedCQLJSONs(result, xmlProcessor);

		return result;
	}

	private void getIncludedCQLJSONs(ExportResult result, XmlProcessor xmlProcessor) throws XPathExpressionException {

		String xPathForIncludedLibs = "//allUsedCQLLibs/lib[not( preceding::lib/@id =@id)]";
		NodeList includedCQLLibNodes = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(), xPathForIncludedLibs);

		for (int i = 0; i < includedCQLLibNodes.getLength(); i++) {
			Node libNode = includedCQLLibNodes.item(i);
			if(!isComposite(libNode)) {
				String libId = libNode.getAttributes().getNamedItem("id").getNodeValue();
				CQLLibrary cqlLibrary = this.cqlLibraryDAO.find(libId);
				CQLLibraryExport cqlLibraryExport = cqlLibraryExportDAO.findByLibraryId(cqlLibrary.getId());
				String includeCqlXMLString = new String(cqlLibrary.getCQLByteArray());
	
				CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(includeCqlXMLString);
				if(cqlLibraryExport == null) {
					cqlLibraryExport = new CQLLibraryExport();
					cqlLibraryExport.setCqlLibrary(cqlLibrary);
				}
				if(cqlLibraryExport.getJson() == null) {
					SaveUpdateCQLResult jsonResult = CQLUtil.generateELM(cqlModel, cqlLibraryDAO);
					cqlLibraryExport.setJson(jsonResult.getJsonString());
					cqlLibraryExportDAO.save(cqlLibraryExport);
				}
				ExportResult includeResult = new ExportResult();
				includeResult.export = cqlLibraryExport.getJson();
	
				String libName = libNode.getAttributes().getNamedItem("name").getNodeValue();
				String libVersion = libNode.getAttributes().getNamedItem("version").getNodeValue();
	
				includeResult.setCqlLibraryName(libName + "-" + libVersion);
	
				result.includedCQLExports.add(includeResult);
			}
		}

	}

	@Override
	public final ExportResult getELMFile(final String measureId) throws Exception {
		MeasureExport measureExport = getMeasureExport(measureId);

		String measureSimpleXML = measureExport.getSimpleXML();
		XmlProcessor xmlProcessor = new XmlProcessor(measureSimpleXML);

		CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(measureSimpleXML);

		String cqlFileString = CQLUtilityClass.getCqlString(cqlModel, "");
		ExportResult result = new ExportResult();
		result.measureName = measureExport.getMeasure().getaBBRName();

		// if the cqlFile String is blank, don't even parse it.
		if (!cqlFileString.isEmpty()) {
			SaveUpdateCQLResult elmResult = CQLUtil.generateELM(cqlModel, cqlLibraryDAO);
			result.export = elmResult.getElmString();
			result.setCqlLibraryName(cqlModel.getLibraryName() + "-" + cqlModel.getVersionUsed());
		} else {
			result.export = "";
			result.measureName = measureExport.getMeasure().getaBBRName();
			result.setCqlLibraryName(result.measureName);
		}

		getIncludedCQLELMs(result, xmlProcessor);

		return result;
	}

	private void getIncludedCQLELMs(ExportResult result, XmlProcessor xmlProcessor) throws XPathExpressionException {
		String xPathForIncludedLibs = "//allUsedCQLLibs/lib[not( preceding::lib/@id =@id)]";
		NodeList includedCQLLibNodes = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(), xPathForIncludedLibs);

		for (int i = 0; i < includedCQLLibNodes.getLength(); i++) {
			Node libNode = includedCQLLibNodes.item(i);
			if(!isComposite(libNode)) {
				String libId = libNode.getAttributes().getNamedItem("id").getNodeValue();
				CQLLibrary cqlLibrary = this.cqlLibraryDAO.find(libId);
				CQLLibraryExport cqlLibraryExport = cqlLibraryExportDAO.findByLibraryId(cqlLibrary.getId());
	
				String includeCqlXMLString = new String(cqlLibrary.getCQLByteArray());
	
				CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(includeCqlXMLString);
				if(cqlLibraryExport == null) {
					cqlLibraryExport = new CQLLibraryExport();
					cqlLibraryExport.setCqlLibrary(cqlLibrary);
				}
				if(cqlLibraryExport.getElm() == null) {
					SaveUpdateCQLResult elmResult = CQLUtil.generateELM(cqlModel, cqlLibraryDAO);
					cqlLibraryExport.setElm(elmResult.getElmString());
					cqlLibraryExportDAO.save(cqlLibraryExport);
				}
				ExportResult includeResult = new ExportResult();
				includeResult.export = cqlLibraryExport.getElm();
	
				String libName = libNode.getAttributes().getNamedItem("name").getNodeValue();
				String libVersion = libNode.getAttributes().getNamedItem("version").getNodeValue();
	
				includeResult.setCqlLibraryName(libName + "-" + libVersion);
	
				result.includedCQLExports.add(includeResult);
			}
		}

	}
	
	private boolean isComposite(Node libNode) {
		return libNode.getAttributes().getNamedItem("isComponent") != null && 
				("true").equals(libNode.getAttributes().getNamedItem("isComponent").getNodeValue());
	}

	/**
	 * *.
	 * 
	 * @param measureId
	 *            - String.
	 * @return ExportResult.
	 * @throws Exception
	 *             - Exception.
	 */
	@Override
	public final ExportResult getHQMFForv3Measure(final String measureId) throws Exception {
		MeasureExport measureExport = getMeasureExport(measureId);
		
		ExportResult result = new ExportResult();
		result.measureName = measureExport.getMeasure().getaBBRName();
		result.export = getHQMFForv3MeasureString(measureId, measureExport);

		return result;
	}
	
	public final ExportResult createOrGetHQMFForv3Measure(final String measureId) {
		MeasureExport measureExport = getMeasureExport(measureId);
		ExportResult result = new ExportResult();
		
		result.measureName = measureExport.getMeasure().getaBBRName();
		if(measureExport.getHqmf() == null) {
			measureExport.setHqmf(getHQMFForv3MeasureString(measureId, measureExport));
			measureExportDAO.save(measureExport);
		}
		result.export = measureExport.getHqmf();
		return result;
	}
	
	private String getHQMFForv3MeasureString(final String measureId, final MeasureExport measureExport) {
		String tempXML = XMLUtility.getInstance().applyXSL(measureExport.getSimpleXML(), XMLUtility.getInstance().getXMLResource(conversionFile1));
		String eMeasureXML = XMLUtility.getInstance().applyXSL(tempXML, XMLUtility.getInstance().getXMLResource(conversionFile2));
		
		return eMeasureXML;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.server.service.SimpleEMeasureService#getEMeasureHTML(java.lang.String)
	 */
	@Override
	public final ExportResult getEMeasureHTML(final String measureId) throws Exception {
		ExportResult result = getHQMFForv3Measure(measureId);
		String html = emeasureXMLToEmeasureHTML(result.export, getMeasureExport(measureId));
		result.export = html;
		return result;
	}
	
	@Override
	public final ExportResult createOrGetEMeasureHTML(final String measureId) throws Exception {
		MeasureExport measureExport = getMeasureExport(measureId);
		ExportResult result = createOrGetHQMFForv3Measure(measureId);
		if(measureExport.getHumanReadable() == null) {
			measureExport.setHumanReadable(createOrGetEmeasureXMLToEmeasureHTML(result.export, getMeasureExport(measureId)));
			measureExportDAO.save(measureExport);
		}
		String html = measureExport.getHumanReadable();
		result.export = html;
		return result;
	}

	@Override
	public final ExportResult getHumanReadable(final String measureId, final String measureVersionNumber) throws Exception {
		MeasureExport measureExport = getMeasureExport(measureId);
		String emeasureHTMLStr = getHumanReadableForMeasure(measureId, measureExport.getSimpleXML(), measureVersionNumber, measureExport);

		ExportResult exportResult = new ExportResult();
		exportResult.export = emeasureHTMLStr;
		exportResult.measureName = measureExport.getMeasure().getaBBRName();
		return exportResult;
	}
	
	@Override
	public final ExportResult createOrGetHumanReadable(final String measureId, final String measureVersionNumber) throws Exception {
		MeasureExport measureExport = getMeasureExport(measureId);
		if(measureExport.getHumanReadable() == null) {
			measureExport.setHumanReadable(getHumanReadableForMeasure(measureId, measureExport.getSimpleXML(), measureVersionNumber, measureExport));
			measureExportDAO.save(measureExport);
		}

		ExportResult exportResult = new ExportResult();
		exportResult.export = measureExport.getHumanReadable();
		exportResult.measureName = measureExport.getMeasure().getaBBRName();
		return exportResult;
	}

	/**
	 * Emeasure xml to emeasure html.
	 * 
	 * @param emeasureXMLStr
	 *            - String.
	 * @return String. *
	 */
	private String createOrGetEmeasureXMLToEmeasureHTML(final String emeasureXMLStr, final MeasureExport measureExport) {
		if(measureExport.getHumanReadable() == null) {
			String html = XMLUtility.getInstance().applyXSL(emeasureXMLStr, XMLUtility.getInstance().getXMLResource(conversionFileHtml));
			measureExport.setHumanReadable(html);
			measureExportDAO.save(measureExport);
		}
		
		return measureExport.getHumanReadable();
	}
	
	private String emeasureXMLToEmeasureHTML(final String emeasureXMLStr, final MeasureExport measureExport) {
		return XMLUtility.getInstance().applyXSL(emeasureXMLStr, XMLUtility.getInstance().getXMLResource(conversionFileHtml));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.server.service.SimpleEMeasureService#getHumanReadableForNode(java.lang.
	 * String, java.lang.String)
	 */
	@Override
	public ExportResult getHumanReadableForNode(final String measureId, final String populationSubXML)
			throws Exception {
		ExportResult result = new ExportResult();

		MeasureXML measureExport = measureXMLDAO.findForMeasure(measureId);
		String measureXML = measureExport.getMeasureXMLAsString();
		String html = humanReadableGenerator.generateHTMLForPopulationOrSubtree(measureId, populationSubXML, measureXML, cqlLibraryDAO);

		result.export = html;
		return result;
	}

	/**
	 * *.
	 * 
	 * @param ctx
	 *            - ApplicationContext.
	 */
	public void setApplicationContext(final ApplicationContext ctx) {
		this.context = ctx;
	}

	/**
	 * Creates the e measure xls.
	 * 
	 * @param measureId
	 *            - String.
	 * @param allQDMs
	 *            - List.
	 * @param supplementalQDMS
	 *            - List.
	 * @param matValueSets
	 *            - List.
	 * @return HSSFWorkbook
	 * @throws Exception
	 *             - Exception. **
	 */
	public final HSSFWorkbook createEMeasureXLS(final String measureId, final List<String> allQDMs,
			final List<String> supplementalQDMS, final List<MatValueSet> matValueSets) throws Exception {
		CodeListXLSGenerator clgen = new CodeListXLSGenerator();
		return clgen.getXLS(getMeasureName(measureId), allQDMs, qualityDataSetDAO, listObjectDAO, supplementalQDMS,
				matValueSets);
	}

	/**
	 * Creates the error e measure xls.
	 * 
	 * @return HSSFWorkbook
	 * @throws Exception
	 *             - Exception. *
	 */
	public final HSSFWorkbook createErrorEMeasureXLS() throws Exception {
		CodeListXLSGenerator clgen = new CodeListXLSGenerator();
		return clgen.getErrorXLS();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.server.service.SimpleEMeasureService#getEMeasureXLS(java.lang.String)
	 */
	@Override
	public final ExportResult getEMeasureXLS(final String measureId) throws Exception {
		ExportResult result = new ExportResult();
		result.measureName = getMeasureName(measureId).getaBBRName();
		result.packageDate = DateUtility.convertDateToStringNoTime2(getMeasureName(measureId).getValueSetDate());
		MeasureExport me = getMeasureExport(measureId);
		if (me.getCodeList() == null) {
			byte[] codes = getHSSFWorkbookBytes(createErrorEMeasureXLS());
			me.setCodeListBarr(codes);
			measureExportDAO.save(me);
			result.wkbkbarr = codes;
		} else {
			result.wkbkbarr = me.getCodeListBarr();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.server.service.SimpleEMeasureService#getValueSetXLS(java.lang.String)
	 */
	@Override
	public final ExportResult getValueSetXLS(final String valueSetId) throws Exception {
		ExportResult result = new ExportResult();
		ListObject lo = listObjectDAO.find(valueSetId);
		ValueSetXLSGenerator vsxg = new ValueSetXLSGenerator();
		HSSFWorkbook workBook = null;
		try {
			workBook = vsxg.getXLS(valueSetId, lo);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			workBook = vsxg.getErrorXLS();
		}
		result.wkbkbarr = vsxg.getHSSFWorkbookBytes(workBook);
		result.valueSetName = lo.getName();
		result.lastModifiedDate = lo.getLastModified() != null
				? DateUtility.convertDateToStringNoTime2(lo.getLastModified())
				: null;
		return result;
	}

	/**
	 * Gets the hSSF workbook bytes.
	 * 
	 * @param hssfwkbk
	 *            - HSSFWorkbook.
	 * @return byte[].
	 * @throws IOException
	 *             - IOException. *
	 */
	private byte[] getHSSFWorkbookBytes(final HSSFWorkbook hssfwkbk) throws IOException {
		CodeListXLSGenerator clgen = new CodeListXLSGenerator();
		return clgen.getHSSFWorkbookBytes(hssfwkbk);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.server.service.SimpleEMeasureService#getEMeasureZIP(java.lang.String)
	 */
	@Override
	public final ExportResult getEMeasureZIP(final String measureId, final Date exportDate) throws Exception {
		ExportResult result = new ExportResult();
		result.measureName = getMeasureName(measureId).getaBBRName();
		MeasureExport me = getMeasureExport(measureId);
		if (me.getMeasure().getReleaseVersion().equals("v3")) {
			result.zipbarr = getZipBarr(measureId, exportDate, me, me.getMeasure().getReleaseVersion());
		} else {
			String currentReleaseVersion = getFormatedReleaseVersion(me.getMeasure().getReleaseVersion());
			FileNameUtility fnu = new FileNameUtility();
			String parentPath = fnu.getParentPath(me.getMeasure().getaBBRName() +"_" + currentReleaseVersion);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    ZipOutputStream zip = new ZipOutputStream(baos);
		    getZipBarr(measureId, me, parentPath, zip);
		    zip.close();
			result.zipbarr = baos.toByteArray();
		}
		return result;
	}
	
	public final ExportResult getCompositeExportResult(final String compositeMeasureId, List<ComponentMeasure> componentMeasures) throws Exception {
		MeasureExport compositeMeasureExport = getMeasureExport(compositeMeasureId);
		ExportResult result = new ExportResult();
		result.measureName = getMeasureName(compositeMeasureId).getaBBRName();
		result.zipbarr = getCompositeZipBarr(compositeMeasureId, compositeMeasureExport, componentMeasures);
		return result;
	}
	
	
	/**
	 * Gets the zip barr.
	 *
	 * @param measureId
	 *            the measure id
	 * @param me
	 *            the me
	 * @return the zip barr
	 * @throws Exception
	 *             the exception
	 */
	public final void getZipBarr(final String measureId, final MeasureExport me, final String parentPath, ZipOutputStream zip) throws Exception {
		String simpleXmlStr = me.getSimpleXML();
		String emeasureHTMLStr = createOrGetHumanReadableFile(measureId, me, simpleXmlStr);
		ExportResult emeasureExportResult = createOrGetHQMF(measureId);
		String emeasureXML = emeasureExportResult.export;

		MeasureExport measureExport = getMeasureExport(measureId);
		ExportResult cqlExportResult= createOrGetCQLLibraryFile(measureId, measureExport);
		ExportResult elmExportResult = createOrGetELMLibraryFile(measureId, measureExport);
		ExportResult jsonExportResult = createOrGetJSONLibraryFile(measureId, measureExport);

		ZipPackager zp = new ZipPackager();
		zp.getZipBarr(me.getMeasure().getaBBRName(), zip, (new Date()).toString(), emeasureHTMLStr,
				simpleXmlStr, emeasureXML, cqlExportResult, elmExportResult, jsonExportResult,
				me.getMeasure().getReleaseVersion(), parentPath);
	}
	
	private String getFormatedReleaseVersion(String currentReleaseVersion) {
		return StringUtils.replace(currentReleaseVersion, ".", "_");
	}
	/**
	 * Gets the zip barr.
	 *
	 * @param measureId
	 *            the measure id
	 * @param me
	 *            the me
	 * @param componentMeasures
	 * 			  a list of component measures for the composite measure
	 * @return the zip barr
	 * @throws Exception
	 *             the exception
	 */
	public final byte[] getCompositeZipBarr(final String measureId, final MeasureExport me, List<ComponentMeasure> componentMeasures) throws Exception {
		String currentReleaseVersion = me.getMeasure().getReleaseVersion();
		currentReleaseVersion = getFormatedReleaseVersion(currentReleaseVersion);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ZipOutputStream zip = new ZipOutputStream(baos);
	    String parentSimpleXML = me.getSimpleXML();
	    
		FileNameUtility fnu = new FileNameUtility();
		//get composite file
		String parentPath = fnu.getParentPath(me.getMeasure().getaBBRName() +"_" + currentReleaseVersion);
		getZipBarr(measureId, me, parentPath, zip);
		//get component files
		for(ComponentMeasure measure : componentMeasures) {
			String componentMeasureId = measure.getComponentMeasure().getId();
			if(checkIfComponentMeasureIsUsed(parentSimpleXML, componentMeasureId)) {
				MeasureExport componentMeasureExport = getMeasureExport(componentMeasureId);
				String componentParentPath = parentPath + File.separator + fnu.getParentPath(componentMeasureExport.getMeasure().getaBBRName() +"_" + currentReleaseVersion); 
				getZipBarr(componentMeasureId, componentMeasureExport, componentParentPath, zip);
			}
		}
		
		zip.close();
		return baos.toByteArray();
	}

	private boolean checkIfComponentMeasureIsUsed(String parentSimpleXML, String componentMeasureId) throws MarshalException, ValidationException, IOException, MappingException, XPathExpressionException {
		
		ManageCompositeMeasureDetailModel usedCompositeModel = compositeMeasureDetailUtil.convertXMLIntoCompositeMeasureDetailModel(parentSimpleXML);
		List<Result> usedCompositeMeasures = usedCompositeModel.getAppliedComponentMeasures();
		for (Result usedCompositeMeasure: usedCompositeMeasures) {
			String measureId = String.valueOf(usedCompositeMeasure.getId());
			measureId = StringUtils.replace(measureId, "-", "");
			if(measureId.equals(componentMeasureId)) {
				return true;
			}
		}
		return false;
		
	}
	private String getHumanReadableForMeasure(String measureId, String simpleXmlStr, String measureVersionNumber, MeasureExport measureExport) {
		return humanReadableGenerator.generateHTMLForMeasure(measureId, simpleXmlStr, measureVersionNumber, cqlLibraryDAO);
	}
	

	public ExportResult getHQMF(String measureId) {
		MeasureExport measureExport = getMeasureExport(measureId);
		
		ExportResult result = new ExportResult();
		result.measureName = measureExport.getMeasure().getaBBRName();
		result.export = getHQMFString(measureExport);
		return result;
	}
	
	public ExportResult createOrGetHQMF(String measureId) {
		MeasureExport measureExport = getMeasureExport(measureId);
		if(measureExport.getHqmf() == null) {
			measureExport.setHqmf(getHQMFString(measureExport));
			measureExportDAO.save(measureExport);
		}
		
		ExportResult result = new ExportResult();
		result.measureName = measureExport.getMeasure().getaBBRName();
		result.export = measureExport.getHqmf();
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
	 * @param measureId
	 *            - String.
	 * @param exportDate
	 *            the export date
	 * @param releaseDate
	 *            the release date
	 * @param me
	 *            - MeasureExport.
	 * @return byte[].
	 * @throws Exception
	 *             - Exception. *
	 */
	public final byte[] getZipBarr(final String measureId, Date exportDate, final MeasureExport me,
			String releaseVersion) throws Exception {
		byte[] wkbkbarr = null;
		StringUtility su = new StringUtility();
		ExportResult emeasureXMLResult = createOrGetHQMFForv3Measure(measureId);
		String emeasureName = emeasureXMLResult.measureName;
		String emeasureXMLStr = emeasureXMLResult.export;
		String repee = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		String repor = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + su.nl
				+ "<?xml-stylesheet type=\"text/xsl\" href=\"xslt/eMeasure.xsl\"?>";
		emeasureXMLStr = repor + emeasureXMLStr.substring(repee.length());
		if(me.getHumanReadable() == null) {
			me.setHumanReadable(createOrGetEmeasureXMLToEmeasureHTML(emeasureXMLStr, me));
			measureExportDAO.save(me);
		}
		String emeasureHTMLStr = me.getHumanReadable();
		String simpleXmlStr = me.getSimpleXML();
		String emeasureXSLUrl = XMLUtility.getInstance().getXMLResource(conversionFileHtml);

		ZipPackager zp = new ZipPackager();
		return zp.getZipBarr(emeasureName, exportDate, releaseVersion, wkbkbarr, emeasureXMLStr, emeasureHTMLStr,
				emeasureXSLUrl, (new Date()).toString(), simpleXmlStr);
	}

	/**
	 * Gets the measure export.
	 * 
	 * @param measureId
	 *            - String.
	 * @return MeasureExport. *
	 */
	public MeasureExport getMeasureExport(final String measureId) {
		MeasureExport measureExport = measureExportDAO.findByMeasureId(measureId);
		if (measureExport == null) {
			return null;
		}
		String emeasureXMLStr = measureExport.getSimpleXML();
		mat.model.clause.Measure measure = measureDAO.find(measureId);
		Timestamp fdts = measure.getFinalizedDate();
		StringUtility su = new StringUtility();

		// 1 add finalizedDate field
		if ((fdts != null) && !emeasureXMLStr.contains("<finalizedDate")) {
			String fdstr = convertTimestampToString(fdts);
			String repee = "</measureDetails>";
			String repor = su.nl + "<finalizedDate value=\"" + fdstr + "\"/>" + su.nl + "</measureDetails>";
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
	 * @param ts
	 *            - Time Stamp.
	 * @return yyyymmddhhss-zzzz
	 */
	@SuppressWarnings("deprecation")
	private String convertTimestampToString(final Timestamp ts) {
		String hours = getTwoDigitString(ts.getHours());
		String mins = getTwoDigitString(ts.getMinutes());
		String month = getTwoDigitString(ts.getMonth() + 1);
		String day = getTwoDigitString(ts.getDate());
		String timeZone = "-" + getTwoDigitString(ts.getTimezoneOffset() / 60) + "00";

		String tsStr = (ts.getYear() + 1900) + month + day + hours + mins + timeZone;
		return tsStr;
	}

	/**
	 * Gets the two digit string.
	 * 
	 * @param i
	 *            -Integer.
	 * @return String. *
	 */
	private String getTwoDigitString(final int i) {
		String ret = i + "";
		if (ret.length() == 1) {
			ret = "0" + ret;
		}
		return ret;
	}

	/**
	 * Getter for wkbk.
	 * 
	 * @return HSSFWorkbook.
	 */
	public final HSSFWorkbook getWkbk() {
		return wkbk;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.server.service.SimpleEMeasureService#getBulkExportZIP(java.lang.String[])
	 */
	@Override
	public final ExportResult getBulkExportZIP(final String[] measureIds, final Date[] exportDates) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ExportResult result = null;
		try(ZipOutputStream zip = new ZipOutputStream(baos);){
			Map<String, byte[]> filesMap = new HashMap<>();
			int fileNameCounter = 1;
			DecimalFormat format = new DecimalFormat("#00");
			Date exportDate;
			FileNameUtility fnu = new FileNameUtility();
			for (String measureId : measureIds) {
				result = new ExportResult();
				result.measureName = getMeasureName(measureId).getaBBRName();
				exportDate = getMeasureName(measureId).getExportedDate();
				MeasureExport me = getMeasureExport(measureId);
				String currentReleaseVersion = getFormatedReleaseVersion(me.getMeasure().getReleaseVersion());
				String sequence = format.format(fileNameCounter++);
				if (me.getMeasure().getReleaseVersion().equals("v3")) {
					createFilesInBulkZip(measureId, exportDate, me, filesMap, sequence);
				} else if(me.getMeasure().getIsCompositeMeasure()) {
					createCompositeFilesInBuildZip(measureId, me, filesMap, sequence, currentReleaseVersion, sequence);
				} else {
					String parentPath = fnu.getParentPath(sequence +"_"+ result.measureName + "_" + currentReleaseVersion);
					createFilesInBulkZip(measureId, me, filesMap, sequence, parentPath);
				}
			}
	
			ZipPackager zp = new ZipPackager();
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
		result.zipbarr = baos.toByteArray();
		return result;
	}

	

	/**
	 * Creates the files in bulk zip.
	 *
	 * @param measureId
	 *            the measure id
	 * @param me
	 *            the me
	 * @param filesMap
	 *            the files map
	 * @param seqNum
	 *            the seq num
	 * @throws Exception
	 *             the exception
	 */
	public void createFilesInBulkZip(final String measureId, final MeasureExport me,
			final Map<String, byte[]> filesMap, final String seqNum, String parentPath) throws Exception {

		byte[] wkbkbarr = null;

		String simpleXmlStr = me.getSimpleXML();
		String emeasureHTMLStr = createOrGetHumanReadableFile(measureId, me, simpleXmlStr);
		ExportResult emeasureExportResult = createOrGetHQMF(measureId);
		String emeasureXMLStr = emeasureExportResult.export;
		String emeasureName = me.getMeasure().getaBBRName();
		String currentReleaseVersion = me.getMeasure().getReleaseVersion();

		MeasureExport measureExport = getMeasureExport(measureId);
		ExportResult cqlExportResult = createOrGetCQLLibraryFile(measureId, measureExport);
		ExportResult elmExportResult = createOrGetELMLibraryFile(measureId, measureExport);
		ExportResult jsonExportResult = createOrGetJSONLibraryFile(measureId, measureExport);

		ZipPackager zp = new ZipPackager();
		zp.createBulkExportZip(emeasureName, wkbkbarr, emeasureXMLStr, emeasureHTMLStr, (new Date()).toString(),
				simpleXmlStr, filesMap, seqNum, currentReleaseVersion, cqlExportResult, elmExportResult,
				jsonExportResult, parentPath);
	}
	
	private void createCompositeFilesInBuildZip(String measureId, MeasureExport me, Map<String, byte[]> filesMap,
			String format, String currentReleaseVersion, String sequance) throws Exception {
		List<ComponentMeasure> componentMeasures = me.getMeasure().getComponentMeasures();
		FileNameUtility fnu = new FileNameUtility();
		String parentSimpleXML = me.getSimpleXML();
		String parentPath = fnu.getParentPath(sequance +"_"+ me.getMeasure().getaBBRName() + "_" + currentReleaseVersion);
		//get composite file
		createFilesInBulkZip(measureId, me, filesMap, format, parentPath);
		//get component files
		for(ComponentMeasure measure : componentMeasures) {
			String componentMeasureId = measure.getComponentMeasure().getId();
			if(checkIfComponentMeasureIsUsed(parentSimpleXML, componentMeasureId)) {
				MeasureExport componentMeasureExport = getMeasureExport(componentMeasureId);
				String componentParentPath = parentPath + File.separator + fnu.getParentPath(componentMeasureExport.getMeasure().getaBBRName() +"_" + currentReleaseVersion); 
				createFilesInBulkZip(componentMeasureId, componentMeasureExport, filesMap, format, componentParentPath);
			}
		}
	}

	/**
	 * Creates the files in bulk zip.
	 *
	 * @param measureId
	 *            - String.
	 * @param exportDate
	 *            the export date
	 * @param releaseDate
	 *            the release date
	 * @param me
	 *            - MeasureExport.
	 * @param filesMap
	 *            - Map.
	 * @param seqNum
	 *            - String.
	 * @throws Exception
	 *             - Exception.
	 * 
	 *             *
	 */
	public final void createFilesInBulkZip(final String measureId, final Date exportDate, final MeasureExport me,
			final Map<String, byte[]> filesMap, final String seqNum) throws Exception {
		byte[] wkbkbarr = null;
		StringUtility su = new StringUtility();
		ExportResult emeasureXMLResult = createOrGetHQMFForv3Measure(measureId);
		String emeasureName = emeasureXMLResult.measureName;
		String emeasureXMLStr = emeasureXMLResult.export;
		String repee = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		String repor = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + su.nl
				+ "<?xml-stylesheet type=\"text/xsl\" href=\"xslt/eMeasure.xsl\"?>";
		emeasureXMLStr = repor + emeasureXMLStr.substring(repee.length());
		String emeasureHTMLStr = createOrGetEmeasureXMLToEmeasureHTML(emeasureXMLStr, me);
		String simpleXmlStr = me.getSimpleXML();
		String emeasureXSLUrl = XMLUtility.getInstance().getXMLResource(conversionFileHtml);
		MeasureExport measureExport = getMeasureExport(measureId);
		
		ExportResult cqlExportResult= createOrGetCQLLibraryFile(measureId, measureExport);
		ExportResult elmExportResult = createOrGetELMLibraryFile(measureId, measureExport);
		ExportResult jsonExportResult = createOrGetJSONLibraryFile(measureId, measureExport);

		ZipPackager zp = new ZipPackager();
		zp.createBulkExportZip(emeasureName, exportDate, wkbkbarr, emeasureXMLStr, emeasureHTMLStr, emeasureXSLUrl,
				(new Date()).toString(), simpleXmlStr, filesMap, seqNum, me.getMeasure().getReleaseVersion(),
				cqlExportResult, elmExportResult, jsonExportResult);
	}
	
	
	public ExportResult createOrGetCQLLibraryFile(String measureId, MeasureExport measureExport) throws Exception {
		ExportResult cqlExportResult = null;
		//if measure export is null then create the file
		if(measureExport.getCql() == null) {
			cqlExportResult = getCQLLibraryFile(measureId);
			measureExport.setCql(cqlExportResult.export);
			measureExportDAO.save(measureExport);
		} else { // else create the export result from the cql in the model and return that;
			CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(measureExport.getSimpleXML());
			cqlExportResult = createExportResultForFile(measureExport, measureExport.getCql(), cqlModel);
			String simpleXML = measureExport.getSimpleXML();
			XmlProcessor xmlProcessor = new XmlProcessor(simpleXML);
			// find included CQL libraries and add them to result
			getIncludedCQLLibs(cqlExportResult, xmlProcessor);
		}
		
		return cqlExportResult;
	}
	
	public String createOrGetHumanReadableFile(String measureId, MeasureExport measureExport, String simpleXmlStr) throws Exception {
		//if measure export is null then create the file
		if(measureExport.getHumanReadable() == null) {
			measureExport.setHumanReadable(getHumanReadableForMeasure(measureId, simpleXmlStr, measureExport.getMeasure().getReleaseVersion(), measureExport));
			measureExportDAO.save(measureExport);
		} 
		return measureExport.getHumanReadable();
	}
	
	public ExportResult createOrGetELMLibraryFile(String measureId, MeasureExport measureExport) throws Exception {
		ExportResult elmExportResult = null;
		//if measure export is null then create the file
		if(measureExport.getElm() == null) {
			elmExportResult = getELMFile(measureId);
			measureExport.setElm(elmExportResult.export);
			measureExportDAO.save(measureExport);
		} else { // else create the export result from the elm in the model and return that;
			CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(measureExport.getSimpleXML());
			elmExportResult = createExportResultForFile(measureExport, measureExport.getElm(), cqlModel);
			String simpleXML = measureExport.getSimpleXML();
			XmlProcessor xmlProcessor = new XmlProcessor(simpleXML);
			getIncludedCQLELMs(elmExportResult, xmlProcessor);
		}
		return elmExportResult;
	}
	
	public ExportResult createOrGetJSONLibraryFile(String measureId, MeasureExport measureExport) throws Exception {
		ExportResult jsonExportResult = null;
		//if measure export is null then create the file
		if(measureExport.getJson() == null) {
			jsonExportResult = getJSONFile(measureId);
			measureExport.setJson(jsonExportResult.export);
			measureExportDAO.save(measureExport);
		} else { // else create the export result from the json in the model and return that;
			CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(measureExport.getSimpleXML());
			jsonExportResult = createExportResultForFile(measureExport, measureExport.getJson(), cqlModel);
			String simpleXML = measureExport.getSimpleXML();
			XmlProcessor xmlProcessor = new XmlProcessor(simpleXML);
			getIncludedCQLJSONs(jsonExportResult, xmlProcessor);
		}
		return jsonExportResult;
	}
	
	private ExportResult createExportResultForFile(MeasureExport measureExport, String fileString, CQLModel cqlModel) {
		ExportResult result = new ExportResult();
		result.measureName = measureExport.getMeasure().getaBBRName();
		result.export = fileString;
		result.setCqlLibraryName(fileString == null ? result.measureName : cqlModel.getLibraryName() + "-" + cqlModel.getVersionUsed());

		return result;
	}

	@Override
	public BonnieCalculatedResult getBonnieExportCalculation(String measureId, String userId) throws IOException, BonnieUnauthorizedException, BonnieNotFoundException, BonnieServerException, BonnieBadParameterException, BonnieDoesNotExistException {
		BonnieCalculatedResult results = bonnieServiceImpl.getBonnieExportForMeasure(userId, measureId);
		return results;
	}
}
