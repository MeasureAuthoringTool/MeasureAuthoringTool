package mat.server.service.impl;

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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.tools.zip.ZipOutputStream;
import org.cqframework.cql.tools.formatter.CQLFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

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
import mat.model.clause.MeasureExport;
import mat.model.clause.MeasureXML;
import mat.model.cql.CQLModel;
import mat.server.CQLUtilityClass;
import mat.server.bonnie.BonnieServiceImpl;
import mat.server.bonnie.api.result.BonnieCalculatedResult;
import mat.server.export.ExportResult;
import mat.server.hqmf.Generator;
import mat.server.hqmf.HQMFGeneratorFactory;
import mat.server.service.MeasurePackageService;
import mat.server.service.SimpleEMeasureService;
import mat.server.simplexml.HumanReadableGenerator;
import mat.server.util.CQLUtil;
import mat.server.util.XmlProcessor;
import mat.shared.ConstantMessages;
import mat.shared.DateUtility;
import mat.shared.SaveUpdateCQLResult;
import mat.shared.StringUtility;
import mat.shared.bonnie.error.BonnieNotFoundException;
import mat.shared.bonnie.error.BonnieServerException;
import mat.shared.bonnie.error.BonnieUnauthorizedException;
import net.sf.saxon.TransformerFactoryImpl;

@Service
public class SimpleEMeasureServiceImpl implements SimpleEMeasureService {

	/** Constant for New_HQMD.xsl. **/
	private static final String conversionFile1 = "xsl/New_HQMF.xsl";
	
	/** Constant for mat_narrGen.xsl. **/
	private static final String conversionFile2 = "xsl/mat_narrGen.xsl";
	
	/** Constant for eMeasure.xsl. **/
	private static final String conversionFileHtml = "xsl/eMeasure.xsl";
	
	/**
	 * Filtered User Defined QDM's as these are dummy QDM's created by user and
	 * should not be part of Value Set sheet.
	 **/
	private static String userDefinedOID = ConstantMessages.USER_DEFINED_QDM_OID;
	
	/** X-path for Element Look Up Node. **/
	private static final String XPATH_ELEMENTLOOKUP_QDM = "/measure/elementLookUp/qdm[not(@oid='" + userDefinedOID +"')]";

	/** X-path for Supplement Data Element Node. **/
	private static final String XPATH_SUPPLEMENTDATA_ELEMENTREF = "/measure/supplementalDataElements/elementRef/@id";

	/**
	 * This expression will find distinct elementRef records from
	 * SimpleXML.SimpleXML will have grouping which can have repeated clauses
	 * containing repeated elementRef. This XPath expression will yield distinct
	 * elementRef's.
	 **/
	/** X-path for Grouping Element Ref. **/
	private static final String XPATH_ALL_GROUPED_ELEMENTREF_ID = "/measure/measureGrouping/group/clause//elementRef[not(@id = preceding:: clause//elementRef/@id)]/@id";

	/** X-path for Grouping Attributes. **/
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

	/** MeasureExportDAO. **/
	private HSSFWorkbook wkbk = null;

	@Override
	public final ExportResult exportMeasureIntoSimpleXML(final String measureId, final String xmlString, final List<MatValueSet> matValueSets) throws Exception {
		ExportResult result = new ExportResult();

		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
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
		TransformerFactory transformerFactory = TransformerFactoryImpl.newInstance();
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
		
		if(measureExport.getCql() == null) {
			String cqlFileString = CQLUtilityClass.getCqlString(cqlModel, "").toString();
	
			if (cqlFileString != null && !cqlFileString.isEmpty()) {
				CQLFormatter formatter = new CQLFormatter(); 
				cqlFileString = formatter.format(cqlFileString);
			}
			measureExport.setCql(cqlFileString);
			measureExportDAO.save(measureExport);
		}
		ExportResult result = new ExportResult();
		result.measureName = measureExport.getMeasure().getaBBRName();
		result.export = measureExport.getCql();
		
		// get the name from the simple xml
		String xPathName = "/measure/cqlLookUp[1]/library[1]";
		XmlProcessor xmlProcessor = new XmlProcessor(simpleXML);
		Node cqlFileName = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), xPathName);
		
		// if the cql file name is blank(before 4.5 measures), then we'll give the file
		// name as
		// the measure name.
		if (cqlFileName == null) {
			result.setCqlLibraryName(result.measureName);
		} else {
			result.setCqlLibraryName(cqlModel.getLibraryName() + "-" + cqlModel.getVersionUsed());
		}

		// find included CQL libraries and add them to result
		getIncludedCQLLibs(result, xmlProcessor);

		return result;
	}

	private void getIncludedCQLLibs(ExportResult result, XmlProcessor xmlProcessor) throws XPathExpressionException {

		String xPathForIncludedLibs = "//allUsedCQLLibs/lib[not( preceding::lib/@id =@id)]";
		NodeList includedCQLLibNodes = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(), xPathForIncludedLibs);

		for (int i = 0; i < includedCQLLibNodes.getLength(); i++) {
			Node libNode = includedCQLLibNodes.item(i);
			String libId = libNode.getAttributes().getNamedItem("id").getNodeValue();
			CQLLibrary cqlLibrary = this.cqlLibraryDAO.find(libId);
			CQLLibraryExport cqlLibraryExport = cqlLibraryExportDAO.findByLibraryId(cqlLibrary.getId());

			String includeCqlXMLString = new String(cqlLibrary.getCQLByteArray());
			if(cqlLibraryExport == null) {
				cqlLibraryExport = new CQLLibraryExport();
				cqlLibraryExport.setCqlLibrary(cqlLibrary);
			}
			if(cqlLibraryExport.getCql() == null) {
				String cqlFileString = CQLUtilityClass.getCqlString(CQLUtilityClass.getCQLModelFromXML(includeCqlXMLString), "").toString();
	
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

	@Override
	public final ExportResult getJSONFile(final String measureId) throws Exception {
		MeasureExport measureExport = getMeasureExport(measureId);

		String measureSimpleXML = measureExport.getSimpleXML();
		XmlProcessor xmlProcessor = new XmlProcessor(measureSimpleXML);

		CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(measureSimpleXML);

		String cqlFileString = CQLUtilityClass.getCqlString(cqlModel, "").toString();
		ExportResult result = new ExportResult();
		result.measureName = measureExport.getMeasure().getaBBRName();

		if(measureExport.getJson() == null) {
			SaveUpdateCQLResult jsonResult = CQLUtil.generateELM(cqlModel, cqlLibraryDAO);
			measureExport.setJson(jsonResult.getJsonString());
			measureExportDAO.save(measureExport);
		}
		if (!cqlFileString.isEmpty()) {
			result.setCqlLibraryName(cqlModel.getLibraryName() + "-" + cqlModel.getVersionUsed());
		} else {
			result.setCqlLibraryName(result.measureName);
		}

		result.export = measureExport.getJson();

		getIncludedCQLJSONs(result, xmlProcessor);

		return result;
	}

	private void getIncludedCQLJSONs(ExportResult result, XmlProcessor xmlProcessor) throws XPathExpressionException {

		String xPathForIncludedLibs = "//allUsedCQLLibs/lib[not( preceding::lib/@id =@id)]";
		NodeList includedCQLLibNodes = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(), xPathForIncludedLibs);

		for (int i = 0; i < includedCQLLibNodes.getLength(); i++) {
			Node libNode = includedCQLLibNodes.item(i);
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

	@Override
	public final ExportResult getELMFile(final String measureId) throws Exception {
		MeasureExport measureExport = getMeasureExport(measureId);

		String measureSimpleXML = measureExport.getSimpleXML();
		XmlProcessor xmlProcessor = new XmlProcessor(measureSimpleXML);

		CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(measureSimpleXML);

		String cqlFileString = CQLUtilityClass.getCqlString(cqlModel, "").toString();
		ExportResult result = new ExportResult();
		result.measureName = measureExport.getMeasure().getaBBRName();
		if(measureExport.getElm() == null) {
			SaveUpdateCQLResult elmResult = CQLUtil.generateELM(cqlModel, cqlLibraryDAO);
			measureExport.setElm(elmResult.getElmString());
			measureExportDAO.save(measureExport);
		}
		if (!cqlFileString.isEmpty()) {
			result.setCqlLibraryName(cqlModel.getLibraryName() + "-" + cqlModel.getVersionUsed());
		} else {
			result.setCqlLibraryName(result.measureName);
		}
		result.export = measureExport.getElm();

		getIncludedCQLELMs(result, xmlProcessor);

		return result;
	}

	private void getIncludedCQLELMs(ExportResult result, XmlProcessor xmlProcessor) throws XPathExpressionException {
		String xPathForIncludedLibs = "//allUsedCQLLibs/lib[not( preceding::lib/@id =@id)]";
		NodeList includedCQLLibNodes = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(), xPathForIncludedLibs);

		for (int i = 0; i < includedCQLLibNodes.getLength(); i++) {
			Node libNode = includedCQLLibNodes.item(i);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.server.service.SimpleEMeasureService#getEMeasureXML(java.lang.String)
	 */
	@Override
	public final ExportResult getHQMFForV3Measure(final String measureId) throws Exception {
		MeasureExport measureExport = getMeasureExport(measureId);
		return getHQMFForv3Measure(measureId, measureExport);
	}

	/**
	 * *.
	 * 
	 * @param measureId
	 *            - String.
	 * @param measureExport
	 *            - MeasureExport.
	 * @return ExportResult.
	 * @throws Exception
	 *             - Exception.
	 */
	private final ExportResult getHQMFForv3Measure(final String measureId, final MeasureExport measureExport) throws Exception {
		XMLUtility xmlUtility = new XMLUtility();
		if(measureExport.getHqmf() == null) {
			String tempXML = xmlUtility.applyXSL(measureExport.getSimpleXML(), xmlUtility.getXMLResource(conversionFile1));
			String eMeasureXML = xmlUtility.applyXSL(tempXML, xmlUtility.getXMLResource(conversionFile2));
			measureExport.setHqmf(eMeasureXML);
			measureExportDAO.save(measureExport);
		}
		ExportResult result = new ExportResult();
		result.measureName = measureExport.getMeasure().getaBBRName();
		result.export = measureExport.getHqmf();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.server.service.SimpleEMeasureService#getEMeasureHTML(java.lang.String)
	 */
	@Override
	public final ExportResult getEMeasureHTML(final String measureId) throws Exception {
		ExportResult result = getHQMFForV3Measure(measureId);
		String html = emeasureXMLToEmeasureHTML(result.export, getMeasureExport(measureId));
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

	/**
	 * Emeasure xml to emeasure html.
	 * 
	 * @param emeasureXMLStr
	 *            - String.
	 * @return String. *
	 */
	private String emeasureXMLToEmeasureHTML(final String emeasureXMLStr, final MeasureExport measureExport) {
		if(measureExport.getHumanReadable() == null) {
			XMLUtility xmlUtility = new XMLUtility();
			String html = xmlUtility.applyXSL(emeasureXMLStr, xmlUtility.getXMLResource(conversionFileHtml));
			measureExport.setHumanReadable(html);
			measureExportDAO.save(measureExport);
		}
		
		return measureExport.getHumanReadable();
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
		String html = HumanReadableGenerator.generateHTMLForPopulationOrSubtree(measureId, populationSubXML, measureXML,
				cqlLibraryDAO);

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
			result.zipbarr = getZipBarr(measureId, me);
		}
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
	public final byte[] getZipBarr(final String measureId, final MeasureExport me) throws Exception {
		byte[] wkbkbarr = null;

		String simpleXmlStr = me.getSimpleXML();
		String emeasureHTMLStr = getHumanReadableForMeasure(measureId, simpleXmlStr, me.getMeasure().getReleaseVersion(), me);
		ExportResult emeasureExportResult = getHQMF(measureId);
		String emeasureXML = emeasureExportResult.export;

		ExportResult exportResult = getCQLLibraryFile(measureId);
		ExportResult elmExportResult = getELMFile(measureId);
		ExportResult jsonExportResult = getJSONFile(measureId);

		ZipPackager zp = new ZipPackager();
		return zp.getZipBarr(me.getMeasure().getaBBRName(), wkbkbarr, (new Date()).toString(), emeasureHTMLStr,
				simpleXmlStr, emeasureXML, exportResult, elmExportResult, jsonExportResult,
				me.getMeasure().getReleaseVersion());
	}

	private String getHumanReadableForMeasure(String measureId, String simpleXmlStr, String measureVersionNumber, MeasureExport measureExport) {
		if(measureExport.getHumanReadable() == null) {
			measureExport.setHumanReadable(HumanReadableGenerator.generateHTMLForMeasure(measureId, simpleXmlStr, measureVersionNumber, cqlLibraryDAO));
			measureExportDAO.save(measureExport);
		}
		return measureExport.getHumanReadable();

	}

	public ExportResult getHQMF(String measureId) {
		MeasureExport measureExport = getMeasureExport(measureId);
		if(measureExport.getHqmf() == null) {
			Generator hqmfGenerator = hqmfGeneratoryFactory.getHQMFGenerator(measureExport.getMeasure().getReleaseVersion());
			String hqmf = "";
			try {
				hqmf = hqmfGenerator.generate(measureExport);
				measureExport.setHqmf(hqmf);
				measureExportDAO.save(measureExport);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		ExportResult result = new ExportResult();
		result.measureName = measureExport.getMeasure().getaBBRName();
		result.export = measureExport.getHqmf();
		return result;
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
		ExportResult emeasureXMLResult = getHQMFForV3Measure(measureId);
		String emeasureName = emeasureXMLResult.measureName;
		String emeasureXMLStr = emeasureXMLResult.export;
		String repee = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		String repor = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + su.nl
				+ "<?xml-stylesheet type=\"text/xsl\" href=\"xslt/eMeasure.xsl\"?>";
		emeasureXMLStr = repor + emeasureXMLStr.substring(repee.length());
		if(me.getHumanReadable() == null) {
			me.setHumanReadable(emeasureXMLToEmeasureHTML(emeasureXMLStr, me));
			measureExportDAO.save(me);
		}
		String emeasureHTMLStr = me.getHumanReadable();
		String simpleXmlStr = me.getSimpleXML();
		XMLUtility xmlUtility = new XMLUtility();
		String emeasureXSLUrl = xmlUtility.getXMLResource(conversionFileHtml);

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
	private MeasureExport getMeasureExport(final String measureId) {
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
		ZipOutputStream zip = new ZipOutputStream(baos);
		Map<String, byte[]> filesMap = new HashMap<String, byte[]>();
		ExportResult result = null;
		int fileNameCounter = 1;
		DecimalFormat format = new DecimalFormat("#00");
		Date exportDate;
		for (String measureId : measureIds) {
			result = new ExportResult();
			result.measureName = getMeasureName(measureId).getaBBRName();
			exportDate = getMeasureName(measureId).getExportedDate();
			MeasureExport me = getMeasureExport(measureId);
			if (me.getMeasure().getReleaseVersion().equals("v3")) {
				createFilesInBulkZip(measureId, exportDate, me, filesMap, format.format(fileNameCounter++));
			} else {
				createFilesInBulkZip(measureId, me, filesMap, format.format(fileNameCounter++));
			}
		}

		ZipPackager zp = new ZipPackager();
		double size = 1024 * 1024 * 100;
		Set<Entry<String, byte[]>> set = filesMap.entrySet();
		for (Entry<String, byte[]> fileArr : set) {
			zp.addBytesToZip(fileArr.getKey(), fileArr.getValue(), zip);
		}
		if (baos.size() > size) {
			zip.close();
			throw new ZipException("Exceeded Limit :" + baos.size());
		}
		zip.close();
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
	private void createFilesInBulkZip(final String measureId, final MeasureExport me,
			final Map<String, byte[]> filesMap, final String seqNum) throws Exception {

		byte[] wkbkbarr = null;

		String simpleXmlStr = me.getSimpleXML();
		String emeasureHTMLStr = getHumanReadableForMeasure(measureId, simpleXmlStr, me.getMeasure().getReleaseVersion(), me);
		ExportResult emeasureExportResult = getHQMF(measureId);
		String emeasureXMLStr = emeasureExportResult.export;
		String emeasureName = me.getMeasure().getaBBRName();
		String currentReleaseVersion = me.getMeasure().getReleaseVersion();
		ExportResult cqlEportResult = getCQLLibraryFile(measureId);
		ExportResult elmExportResult = getELMFile(measureId);
		ExportResult jsonExportResult = getJSONFile(measureId);

		ZipPackager zp = new ZipPackager();
		zp.createBulkExportZip(emeasureName, wkbkbarr, emeasureXMLStr, emeasureHTMLStr, (new Date()).toString(),
				simpleXmlStr, filesMap, seqNum, currentReleaseVersion, cqlEportResult, elmExportResult,
				jsonExportResult);
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
		ExportResult emeasureXMLResult = getHQMFForv3Measure(measureId, me);
		String emeasureName = emeasureXMLResult.measureName;
		String emeasureXMLStr = emeasureXMLResult.export;
		String repee = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		String repor = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + su.nl
				+ "<?xml-stylesheet type=\"text/xsl\" href=\"xslt/eMeasure.xsl\"?>";
		emeasureXMLStr = repor + emeasureXMLStr.substring(repee.length());
		String emeasureHTMLStr = emeasureXMLToEmeasureHTML(emeasureXMLStr, me);
		String simpleXmlStr = me.getSimpleXML();
		XMLUtility xmlUtility = new XMLUtility();
		String emeasureXSLUrl = xmlUtility.getXMLResource(conversionFileHtml);
		ExportResult cqlExportResult = getCQLLibraryFile(measureId);
		ExportResult elmExportResult = getELMFile(measureId);
		ExportResult jsonExportResult = getJSONFile(measureId);

		ZipPackager zp = new ZipPackager();
		zp.createBulkExportZip(emeasureName, exportDate, wkbkbarr, emeasureXMLStr, emeasureHTMLStr, emeasureXSLUrl,
				(new Date()).toString(), simpleXmlStr, filesMap, seqNum, me.getMeasure().getReleaseVersion(),
				cqlExportResult, elmExportResult, jsonExportResult);
	}

	@Override
	public BonnieCalculatedResult getBonnieExportCalculation(String measureId, String userId) throws IOException, BonnieUnauthorizedException, BonnieNotFoundException, BonnieServerException {
		BonnieCalculatedResult results = bonnieServiceImpl.getBonnieExportForMeasure(userId, measureId);
		return results;
	}
}
