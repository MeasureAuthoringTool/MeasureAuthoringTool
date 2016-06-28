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
import javax.xml.xpath.XPathFactory;

import mat.dao.ListObjectDAO;
import mat.dao.QualityDataSetDAO;
import mat.dao.clause.MeasureDAO;
import mat.dao.clause.MeasureExportDAO;
import mat.dao.clause.MeasureXMLDAO;
import mat.model.ListObject;
import mat.model.MatValueSet;
import mat.model.QualityDataSetDTO;
import mat.model.clause.MeasureExport;
import mat.model.clause.MeasureXML;
import mat.server.CQLUtilityClass;
import mat.server.service.MeasurePackageService;
import mat.server.service.SimpleEMeasureService;
import mat.server.simplexml.HQMFHumanReadableGenerator;
import mat.server.simplexml.HumanReadableGenerator;
import mat.server.simplexml.hqmf.HQMFGenerator;
import mat.server.util.XmlProcessor;
import mat.shared.ConstantMessages;
import mat.shared.DateUtility;
import mat.shared.StringUtility;
import net.sf.saxon.TransformerFactoryImpl;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.tools.zip.ZipOutputStream;
import org.cqframework.cql.cql2elm.CQLtoELM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

// TODO: Auto-generated Javadoc
/** SimpleEMeasureServiceImpl.java **/
public class SimpleEMeasureServiceImpl implements SimpleEMeasureService {

	/** Constant for New_HQMD.xsl. **/
	private static final String conversionFile1 = "xsl/New_HQMF.xsl";
	/** Constant for mat_narrGen.xsl. **/
	private static final String conversionFile2 = "xsl/mat_narrGen.xsl";
	private static final String conversionFileForHQMF_Header = "xsl/new_measureDetails.xsl";
	/** Constant for eMeasure.xsl. **/
	private static final String conversionFileHtml = "xsl/eMeasure.xsl";
	/** Filtered User Defined QDM's as these are dummy QDM's created by user and
	should not be part of Value Set sheet.**/
	private static String userDefinedOID = ConstantMessages.USER_DEFINED_QDM_OID;
	/**X-path for Element Look Up Node.**/
	private static final String XPATH_ELEMENTLOOKUP_QDM = "/measure/elementLookUp/qdm[not(@oid='"
			+ userDefinedOID + "')]";
	/**X-path for Supplement Data Element Node.**/
	private static final String XPATH_SUPPLEMENTDATA_ELEMENTREF = "/measure/supplementalDataElements/elementRef/@id";
	/** This expression will find distinct elementRef records from
	 SimpleXML.SimpleXML will have grouping which can have
	 repeated clauses containing repeated elementRef. This XPath expression
	 will yield distinct elementRef's.**/
	/**X-path for Grouping Element Ref.**/
	private static final String XPATH_ALL_GROUPED_ELEMENTREF_ID =
			"/measure/measureGrouping/group/clause//elementRef[not(@id = preceding:: clause//elementRef/@id)]/@id";
	/**X-path for Measure Observation Element Ref.**/
	//private static final String XPATH_ALL_MSR_OBS_ELEMENTREF_ID =
	//		"/measure/measureObservations/clause//elementRef[not(@id = preceding:: clause//elementRef/@id)]/@id";
	/**X-path for Stratification Element Ref.**/
	//private static final String XPATH_ALL_STARTA_ELEMENTREF_ID =
			//"/measure/strata/clause//elementRef[not(@id = preceding:: clause//elementRef/@id)]/@id";
	/**X-path for Grouping Attributes.**/
	private static final String XPATH_ALL_GROUPED_ATTRIBUTES_UUID =
			"/measure/measureGrouping/group/clause//attribute[not(@qdmUUID = preceding:: clause//attribute/@qdmUUID)]/@qdmUUID";
	/**X-path for Measure Observation Attributes.**/
	//private static final String XPATH_ALL_MSR_OBS_ATTRIBUTES_UUID =
		//	"/measure/measureObservations/clause//attribute[not(@qdmUUID = preceding:: clause//attribute/@qdmUUID)]/@qdmUUID";
	/**X-path for Stratification Attributes.**/
	//private static final String XPATH_ALL_STARTA_ATTRIBUTES_UUID =
		//	"/measure/strata/clause//attribute[not(@qdmUUID = preceding:: clause//attribute/@qdmUUID)]/@qdmUUID";
	
	private static final String XPATH_ALL_SUBTREE_ELEMENTREF_ID = 
			"/measure/subTreeLookUp/subTree//elementRef[not(@id = preceding:: subTree//elementRef/@id)]/@id";
	
	private static final String XPATH_ALL_SUBTREE_ATTRIBUTES_UUID = 
			"/measure/subTreeLookUp/subTree//attribute[not(@qdmUUID = preceding:: subTree//attribute/@qdmUUID)]/@qdmUUID";
	/**Logger.**/
	private static final Log LOGGER = LogFactory
			.getLog(SimpleEMeasureServiceImpl.class);

	/**MeasureDAO.**/
	@Autowired
	private MeasureDAO measureDAO;
	
	/** MeasureXMLDAO*. */
	@Autowired
	private MeasureXMLDAO measureXMLDAO;

	/**MeasureExportDAO.**/
	@Autowired
	private MeasureExportDAO measureExportDAO;

	/**ApplicationContext.**/
	@Autowired
	private ApplicationContext context;

	/**QualityDataSetDAO.**/
	@Autowired
	private QualityDataSetDAO qualityDataSetDAO;

	/**ListObjectDAO.**/
	@Autowired
	private ListObjectDAO listObjectDAO;

	/**MeasureExportDAO.**/
	private HSSFWorkbook wkbk = null;

	/* (non-Javadoc)
	 * @see mat.server.service.SimpleEMeasureService#exportMeasureIntoSimpleXML(java.lang.String, java.lang.String, java.util.List)
	 */
	@Override
	public final ExportResult exportMeasureIntoSimpleXML(final String measureId,
			final String xmlString, final List<MatValueSet> matValueSets)
					throws Exception {
		
		//	System.out.println("exportMeasureIntoSimpleXML...xmlString:"
		//			+ xmlString);
		ExportResult result = new ExportResult();

		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		InputSource oldXmlstream = new InputSource(new StringReader(xmlString));
		Document originalDoc = docBuilder.parse(oldXmlstream);
		javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();

		List<String> qdmRefID = new ArrayList<String>();
		List<String> supplRefID = new ArrayList<String>();
		List<QualityDataSetDTO> masterRefID = new ArrayList<QualityDataSetDTO>();

		transform(originalDoc);
		NodeList allGroupedElementRefIDs = (NodeList) xPath.evaluate(
				XPATH_ALL_GROUPED_ELEMENTREF_ID,
				originalDoc.getDocumentElement(), XPathConstants.NODESET);
//		NodeList allMsrObsElementRefIDs = (NodeList) xPath.evaluate(
//				XPATH_ALL_MSR_OBS_ELEMENTREF_ID,
//				originalDoc.getDocumentElement(), XPathConstants.NODESET);
//		NodeList allStrataElementRefIDs = (NodeList) xPath.evaluate(
//				XPATH_ALL_STARTA_ELEMENTREF_ID,
//				originalDoc.getDocumentElement(), XPathConstants.NODESET);
		NodeList allGroupedAttributesUUIDs = (NodeList) xPath.evaluate(
				XPATH_ALL_GROUPED_ATTRIBUTES_UUID,
				originalDoc.getDocumentElement(), XPathConstants.NODESET);
//		NodeList allMsrObsAttributesUUIDs = (NodeList) xPath.evaluate(
//				XPATH_ALL_MSR_OBS_ATTRIBUTES_UUID,
//				originalDoc.getDocumentElement(), XPathConstants.NODESET);
//		NodeList allStrataAttributesUUIDs = (NodeList) xPath.evaluate(
//				XPATH_ALL_STARTA_ATTRIBUTES_UUID,
//				originalDoc.getDocumentElement(), XPathConstants.NODESET);
		NodeList allQDMRefIDs = (NodeList) xPath.evaluate(
				XPATH_ELEMENTLOOKUP_QDM, originalDoc.getDocumentElement(),
				XPathConstants.NODESET);
		NodeList allSupplementIDs = (NodeList) xPath.evaluate(
				XPATH_SUPPLEMENTDATA_ELEMENTREF,
				originalDoc.getDocumentElement(), XPathConstants.NODESET);
		NodeList allSubTreeElementRefIDs = (NodeList) xPath.evaluate(
				XPATH_ALL_SUBTREE_ELEMENTREF_ID,
				originalDoc.getDocumentElement(), XPathConstants.NODESET);
		
		NodeList allSubTreeAttributeIDs = (NodeList) xPath.evaluate(
				XPATH_ALL_SUBTREE_ATTRIBUTES_UUID,
				originalDoc.getDocumentElement(), XPathConstants.NODESET);
		
		

		for (int i = 0; i < allQDMRefIDs.getLength(); i++) {
			Node newNode = allQDMRefIDs.item(i);
			QualityDataSetDTO dataSetDTO = new QualityDataSetDTO();
			dataSetDTO.setId(newNode.getAttributes().getNamedItem("id")
					.getNodeValue().toString());
			dataSetDTO.setUuid(newNode.getAttributes().getNamedItem("uuid")
					.getNodeValue().toString());
			masterRefID.add(dataSetDTO);
		}

		findAndAddDTO(allGroupedElementRefIDs, masterRefID, qdmRefID);
		findAndAddDTO(allGroupedAttributesUUIDs, masterRefID, qdmRefID);
//		findAndAddDTO(allMsrObsElementRefIDs, masterRefID, qdmRefID);
//		findAndAddDTO(allMsrObsAttributesUUIDs, masterRefID, qdmRefID);
//		findAndAddDTO(allStrataElementRefIDs, masterRefID, qdmRefID);
//		findAndAddDTO(allStrataAttributesUUIDs, masterRefID, qdmRefID);
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
	
	private static String transform(Node node){	
		//_logger.info("In transform() method");
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		TransformerFactory transformerFactory = TransformerFactoryImpl.newInstance();
		DOMSource source = new DOMSource(node);
		StreamResult result = new StreamResult(arrayOutputStream);
		
		try {
			transformerFactory.newTransformer().transform(source, result);
		} catch (TransformerException e) {
			//_logger.info("Document object to ByteArray transformation failed "+e.getStackTrace());
			e.printStackTrace();
		}
		//_logger.info("Document object to ByteArray transformation complete");
		System.out.println(arrayOutputStream.toString());
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
	private void findAndAddDTO(final NodeList nodeList,
			final List<QualityDataSetDTO> masterRefID,
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
		MeasurePackageService measureService = (MeasurePackageService) context
				.getBean("measurePackageService");
		mat.model.clause.Measure measure = measureService.getById(measureId);
		return measure;
	}

	/* (non-Javadoc)
	 * @see mat.server.service.SimpleEMeasureService#getSimpleXML(java.lang.String)
	 */
	@Override
	public final ExportResult getSimpleXML(final String measureId) throws Exception {
		mat.model.clause.Measure measure = measureDAO.find(measureId);
		MeasureExport measureExport = getMeasureExport(measureId);
		if(measureExport == null){
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
		//MeasureXML measureXml = measureXMLDAO.findForMeasure(measureId);
		//String measureXML = measureXml.getMeasureXMLAsString();
		String simpleXML = measureExport.getSimpleXML();
		System.out.println(simpleXML);

		
		// get the name from the simple xml
		String xPathName = "/measure/cqlLookUp[1]/library[1]"; 
		XmlProcessor xmlProcessor = new XmlProcessor(simpleXML); 
		Node cqlFileName = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), xPathName); 
		
		String cqlFileString = CQLUtilityClass.getCqlString(CQLUtilityClass.getCQLStringFromMeasureXML(simpleXML, measureId),"").toString();
		ExportResult result = new ExportResult();
		result.measureName = measureExport.getMeasure().getaBBRName();
		result.export = cqlFileString;
		
		// if the cql file name is blank(before 4.5 measures), then we'll give the file name as
		// the measure name. 
		if(cqlFileName == null) {
			result.setCqlLibraryName(result.measureName);
		} else {
			result.setCqlLibraryName(cqlFileName.getTextContent());
		}
		
		
		return result;
	}

	@Override
	public final ExportResult getELMFile(final String measureId) throws Exception {
		MeasureExport measureExport = getMeasureExport(measureId);
		MeasureXML measureXml = measureXMLDAO.findForMeasure(measureId);
		String measureXML = measureXml.getMeasureXMLAsString();
		String cqlFileString = CQLUtilityClass.getCqlString(CQLUtilityClass.getCQLStringFromMeasureXML(measureXML, measureId),"").toString();
		ExportResult result = new ExportResult();
		
		String elmString = ""; 
		
		System.out.println(cqlFileString);
		
		// if the cqlFile String is blank, don't even parse it.
		if(!cqlFileString.isEmpty()) {
			System.out.println("CQL String was Empty");
			elmString = CQLtoELM.doTranslation(cqlFileString, "XML", false, false, false);	
			LOGGER.info(elmString);
			// get cql library name from the elm file. 
			// it is located at /library/identifier/@id
			String xPathIdentifier = "/library/identifier/@id";
			XmlProcessor xmlProcessor = new XmlProcessor(elmString);
			Node cqlLibraryName = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), xPathIdentifier);		
			result.setCqlLibraryName(cqlLibraryName.getTextContent());
			

		} else {
			elmString = "";
			result.measureName = measureExport.getMeasure().getaBBRName();
			result.setCqlLibraryName(result.measureName);
		}
		
		result.export = elmString; 
		
		
		return result;
	}
	/* (non-Javadoc)
	 * @see mat.server.service.SimpleEMeasureService#getEMeasureXML(java.lang.String)
	 */
	@Override
	public final ExportResult getEMeasureXML(final String measureId) throws Exception {

		MeasureExport measureExport = getMeasureExport(measureId);
		return getEMeasureXML(measureId, measureExport);
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
	public final ExportResult getEMeasureXML(final String measureId,
			final MeasureExport measureExport) throws Exception {
		XMLUtility xmlUtility = new XMLUtility();
		String tempXML = xmlUtility.applyXSL(measureExport.getSimpleXML(),
				xmlUtility.getXMLResource(conversionFile1));
		String eMeasureXML = xmlUtility.applyXSL(tempXML,
				xmlUtility.getXMLResource(conversionFile2));

		ExportResult result = new ExportResult();
		result.measureName = measureExport.getMeasure().getaBBRName();
		result.export = eMeasureXML;

		return result;
	}

	/* (non-Javadoc)
	 * @see mat.server.service.SimpleEMeasureService#getEMeasureHTML(java.lang.String)
	 */
	@Override
	public final ExportResult getEMeasureHTML(final String measureId)
			throws Exception {
		ExportResult result = getEMeasureXML(measureId);
		String html = emeasureXMLToEmeasureHTML(result.export);
		result.export = html;
		return result;
	}
	
	@Override
	public final ExportResult getNewEMeasureHTML(final String measureId) throws Exception {
		MeasureExport measureExport = getMeasureExport(measureId);
		String emeasureHTMLStr = getHumanReadableForMeasure(measureId, measureExport.getSimpleXML());
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
	private String emeasureXMLToEmeasureHTML(final String emeasureXMLStr) {
		XMLUtility xmlUtility = new XMLUtility();
		String html = xmlUtility.applyXSL(emeasureXMLStr,
				xmlUtility.getXMLResource(conversionFileHtml));
		return html;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.SimpleEMeasureService#getHumanReadableForNode(java.lang.String, java.lang.String)
	 */
	@Override
	public ExportResult getHumanReadableForNode(final String measureId, final String populationSubXML) throws Exception{
		ExportResult result = new ExportResult();
		
		MeasureXML measureExport = measureXMLDAO.findForMeasure(measureId);
		String measureXML = measureExport.getMeasureXMLAsString();
		String html = HumanReadableGenerator.generateHTMLForPopulationOrSubtree(measureId, populationSubXML, measureXML);
				
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
	public final HSSFWorkbook createEMeasureXLS(final String measureId,
			final List<String> allQDMs, final List<String> supplementalQDMS,
			final List<MatValueSet> matValueSets) throws Exception {
		CodeListXLSGenerator clgen = new CodeListXLSGenerator();
		return clgen.getXLS(getMeasureName(measureId), allQDMs,
				qualityDataSetDAO, listObjectDAO, supplementalQDMS,
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

	/* (non-Javadoc)
	 * @see mat.server.service.SimpleEMeasureService#getEMeasureXLS(java.lang.String)
	 */
	@Override
	public final ExportResult getEMeasureXLS(final String measureId) throws Exception {
		ExportResult result = new ExportResult();
		result.measureName = getMeasureName(measureId).getaBBRName();
		result.packageDate = DateUtility
				.convertDateToStringNoTime2(getMeasureName(measureId)
						.getValueSetDate());
		MeasureExport me = getMeasureExport(measureId);
		if (me.getCodeList() == null) {
			result.wkbkbarr = getHSSFWorkbookBytes(createErrorEMeasureXLS());
		} else {
			result.wkbkbarr = me.getCodeListBarr();
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see mat.server.service.SimpleEMeasureService#getValueSetXLS(java.lang.String)
	 */
	@Override
	public final ExportResult getValueSetXLS(final String valueSetId)
			throws Exception {
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
		result.lastModifiedDate = lo.getLastModified() != null ? DateUtility
				.convertDateToStringNoTime2(lo.getLastModified()) : null;
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
	private byte[] getHSSFWorkbookBytes(final HSSFWorkbook hssfwkbk)
			throws IOException {
		CodeListXLSGenerator clgen = new CodeListXLSGenerator();
		return clgen.getHSSFWorkbookBytes(hssfwkbk);
	}

	/* (non-Javadoc)
	 * @see mat.server.service.SimpleEMeasureService#getEMeasureZIP(java.lang.String)
	 */
	@Override
	public final ExportResult getEMeasureZIP(final String measureId,final Date exportDate) throws Exception {
		ExportResult result = new ExportResult();
		result.measureName = getMeasureName(measureId).getaBBRName();
		MeasureExport me = getMeasureExport(measureId);
		if(me.getMeasure().getReleaseVersion().equals("v3")){
			result.zipbarr = getZipBarr(measureId,exportDate, me, me.getMeasure().getReleaseVersion());		
		}else{
			result.zipbarr = getZipBarr(measureId,me);
		}
		return result;
	}

	/**
	 * Gets the zip barr.
	 *
	 * @param measureId the measure id
	 * @param me the me
	 * @return the zip barr
	 * @throws Exception the exception
	 */
	public final byte[] getZipBarr(final String measureId,final MeasureExport me)
			throws Exception {
				byte[] wkbkbarr = null;
				if (me.getCodeList() == null) {
					wkbkbarr = getHSSFWorkbookBytes(createErrorEMeasureXLS());
				} else {
					wkbkbarr = me.getCodeListBarr();
				}
				
				String simpleXmlStr = me.getSimpleXML();
				String emeasureHTMLStr = getHumanReadableForMeasure(measureId, simpleXmlStr);
				//String emeasureXML = getEMeasureXML(me);
				//String emeasureXML = "";
				String emeasureXML = getNewEMeasureXML(me);
		        ExportResult exportResult = getCQLLibraryFile(measureId);
		        ExportResult elmExportResult = getELMFile(measureId); 
		        String cqlFileStr = exportResult.export;
		        String elmFileStr = elmExportResult.export;

				ZipPackager zp = new ZipPackager();
				return zp.getZipBarr(me.getMeasure().getaBBRName(), wkbkbarr, (new Date()).toString(), 
						emeasureHTMLStr, simpleXmlStr,emeasureXML, cqlFileStr, elmFileStr, me.getMeasure().getReleaseVersion());
		}
	
	
	/**
	 * Gets the human readable for measure.
	 *
	 * @param measureId the measure id
	 * @param simpleXmlStr the simple xml str
	 * @return the human readable for measure
	 */
	private String getHumanReadableForMeasure(String measureId,
			String simpleXmlStr) {
		String html = HQMFHumanReadableGenerator.generateHTMLForMeasure(measureId,simpleXmlStr);
		return html;

	}
	
	public ExportResult getNewEMeasureXML(String measureId){
		MeasureExport measureExport = getMeasureExport(measureId);
		String newEmeasureXML = getNewEMeasureXML(measureExport);
		ExportResult result = new ExportResult();
		result.measureName = measureExport.getMeasure().getaBBRName();
		result.export = newEmeasureXML;
		return result;
	}
	
	private String getNewEMeasureXML(MeasureExport me){
		
		String eMeasurexml = new HQMFGenerator().generate(me);
		return eMeasurexml;
	}

	/**
	 * Gets the zip barr.
	 *
	 * @param measureId            - String.
	 * @param exportDate the export date
	 * @param releaseDate the release date
	 * @param me            - MeasureExport.
	 * @return byte[].
	 * @throws Exception             - Exception. *
	 */
	public final byte[] getZipBarr(final String measureId,Date exportDate, final MeasureExport me, String releaseVersion)
			throws Exception {
		byte[] wkbkbarr = null;
		if (me.getCodeList() == null) {
			wkbkbarr = getHSSFWorkbookBytes(createErrorEMeasureXLS());
		} else {
			wkbkbarr = me.getCodeListBarr();
		}
		System.out.println("MAKING THE ZIP FILE!!!!!!");
		StringUtility su = new StringUtility();
		ExportResult emeasureXMLResult = getEMeasureXML(measureId);
		String emeasureName = emeasureXMLResult.measureName;
		String emeasureXMLStr = emeasureXMLResult.export;
		String repee = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		String repor = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ su.nl
				+ "<?xml-stylesheet type=\"text/xsl\" href=\"xslt/eMeasure.xsl\"?>";
		emeasureXMLStr = repor + emeasureXMLStr.substring(repee.length());
		String emeasureHTMLStr = emeasureXMLToEmeasureHTML(emeasureXMLStr);
		String simpleXmlStr = me.getSimpleXML();
		XMLUtility xmlUtility = new XMLUtility();
		String emeasureXSLUrl = xmlUtility.getXMLResource(conversionFileHtml);
		ExportResult cqlExportResult = getCQLLibraryFile(measureId);
		ExportResult elmExportResult = getELMFile(measureId);

		String cqlFileStr = cqlExportResult.export;
		String elmFileStr = elmExportResult.export; 

		ZipPackager zp = new ZipPackager();
		return zp.getZipBarr(emeasureName,exportDate, releaseVersion, wkbkbarr, emeasureXMLStr,
				emeasureHTMLStr, emeasureXSLUrl, (new Date()).toString(), simpleXmlStr, cqlFileStr, elmFileStr);
	}

	/**
	 * Gets the measure export.
	 * 
	 * @param measureId
	 *            - String.
	 * @return MeasureExport. *
	 */
	private MeasureExport getMeasureExport(final String measureId) {
		MeasureExport measureExport = measureExportDAO
				.findForMeasure(measureId);
		if(measureExport == null){
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
			String repor = su.nl + "<finalizedDate value=\"" + fdstr + "\"/>"
					+ su.nl + "</measureDetails>";
			int offset = emeasureXMLStr.indexOf(repee);
			emeasureXMLStr = emeasureXMLStr.substring(0, offset) + repor
					+ emeasureXMLStr.substring(offset + repee.length());
			measureExport.setSimpleXML(emeasureXMLStr);
		}
		// 2 add version field
		final String versionStart = "<version>";
		final String versionEnd = "</version>";
		String vStr = measure.getMajorVersionStr() + "." + measure.getMinorVersionStr() + "." + measure.getRevisionNumber();
		if (emeasureXMLStr.contains(versionStart)) {
			int start = emeasureXMLStr.indexOf(versionStart)
					+ versionStart.length();
			int end = emeasureXMLStr.indexOf(versionEnd);
			emeasureXMLStr = emeasureXMLStr.substring(0, start) + vStr
					+ emeasureXMLStr.substring(end);
			measureExport.setSimpleXML(emeasureXMLStr);
		} else {
			String repee = "</measureDetails>";
			String repor = su.nl + versionStart + vStr + versionEnd + su.nl
					+ "</measureDetails>";
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
		String timeZone = "-" + getTwoDigitString(ts.getTimezoneOffset() / 60)
				+ "00";

		String tsStr = (ts.getYear() + 1900) + month + day + hours + mins
				+ timeZone;
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
	 *Getter for wkbk.
	 *@return HSSFWorkbook.
	 * **/
	public final HSSFWorkbook getWkbk() {
		return wkbk;
	}

	/* (non-Javadoc)
	 * @see mat.server.service.SimpleEMeasureService#getBulkExportZIP(java.lang.String[])
	 */
	@Override
	public final ExportResult getBulkExportZIP(final String[] measureIds, final Date[] exportDates)
			throws Exception {
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
			if(me.getMeasure().getReleaseVersion().equals("v3")){
				createFilesInBulkZip(measureId,exportDate, me, filesMap,
					format.format(fileNameCounter++));
			}
			else{
				createFilesInBulkZip(measureId, me, filesMap,
						format.format(fileNameCounter++));

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
	 * @param measureId the measure id
	 * @param me the me
	 * @param filesMap the files map
	 * @param seqNum the seq num
	 * @throws Exception the exception
	 */
	private void createFilesInBulkZip(final String measureId,
			final MeasureExport me, final Map<String, byte[]> filesMap,
			final String seqNum) throws Exception {
		
		byte[] wkbkbarr = null;
		if (me.getCodeList() == null) {
			wkbkbarr = getHSSFWorkbookBytes(createErrorEMeasureXLS());
		} else {
			wkbkbarr = me.getCodeListBarr();
		}
		
		String simpleXmlStr = me.getSimpleXML();
		String emeasureHTMLStr = getHumanReadableForMeasure(measureId, simpleXmlStr);
		String emeasureXMLStr = getNewEMeasureXML(me);
		//String emeasureXMLStr = "";
		String emeasureName = me.getMeasure().getaBBRName();
		String currentReleaseVersion = me.getMeasure().getReleaseVersion();
		ExportResult cqlEportResult = getCQLLibraryFile(measureId);
		String cqlFileStr = cqlEportResult.export;
		ExportResult elmExportResult = getELMFile(measureId); 
		String elmFileStr = elmExportResult.export; 
		ZipPackager zp = new ZipPackager();
		zp.createBulkExportZip(emeasureName, wkbkbarr, emeasureXMLStr,
				emeasureHTMLStr, (new Date()).toString(), simpleXmlStr, filesMap,
				seqNum,currentReleaseVersion, cqlFileStr, elmFileStr);
	}

	/**
	 * Creates the files in bulk zip.
	 *
	 * @param measureId            - String.
	 * @param exportDate the export date
	 * @param releaseDate the release date
	 * @param me            - MeasureExport.
	 * @param filesMap            - Map.
	 * @param seqNum            - String.
	 * @throws Exception             - Exception.
	 * 
	 *             *
	 */
	public final void createFilesInBulkZip(final String measureId,final Date exportDate,
			final MeasureExport me, final Map<String, byte[]> filesMap,
			final String seqNum) throws Exception {
		byte[] wkbkbarr = null;
		if (me.getCodeList() == null) {
			wkbkbarr = getHSSFWorkbookBytes(createErrorEMeasureXLS());
		} else {
			wkbkbarr = me.getCodeListBarr();
		}
		StringUtility su = new StringUtility();
		ExportResult emeasureXMLResult = getEMeasureXML(measureId, me);
		String emeasureName = emeasureXMLResult.measureName;
		String emeasureXMLStr = emeasureXMLResult.export;
		String repee = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		String repor = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ su.nl
				+ "<?xml-stylesheet type=\"text/xsl\" href=\"xslt/eMeasure.xsl\"?>";
		emeasureXMLStr = repor + emeasureXMLStr.substring(repee.length());
		String emeasureHTMLStr = emeasureXMLToEmeasureHTML(emeasureXMLStr);
		String simpleXmlStr = me.getSimpleXML();
		XMLUtility xmlUtility = new XMLUtility();
		String emeasureXSLUrl = xmlUtility.getXMLResource(conversionFileHtml);
		ExportResult cqlEportResult = getCQLLibraryFile(measureId);
		String cqlFileStr = cqlEportResult.export;
		ExportResult elmExportResult = getELMFile(measureId);
		String elmFileStr = elmExportResult.export;
		
		ZipPackager zp = new ZipPackager();
		zp.createBulkExportZip(emeasureName,exportDate, wkbkbarr, emeasureXMLStr,
				emeasureHTMLStr, emeasureXSLUrl, (new Date()).toString(), simpleXmlStr, filesMap,
				seqNum, me.getMeasure().getReleaseVersion(), cqlFileStr, elmFileStr);
	}
}
