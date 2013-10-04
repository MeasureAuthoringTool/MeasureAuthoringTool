package mat.server.service.impl;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.ZipException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import mat.dao.ListObjectDAO;
import mat.dao.QualityDataSetDAO;
import mat.dao.clause.MeasureDAO;
import mat.dao.clause.MeasureExportDAO;
import mat.model.ListObject;
import mat.model.MatValueSet;
import mat.model.QualityDataSetDTO;
import mat.model.clause.MeasureExport;
import mat.server.service.MeasurePackageService;
import mat.server.service.SimpleEMeasureService;
import mat.shared.ConstantMessages;
import mat.shared.DateUtility;
import mat.shared.StringUtility;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/** SimpleEMeasureServiceImpl.java **/
public class SimpleEMeasureServiceImpl implements SimpleEMeasureService {

	/** Constant for New_HQMD.xsl. **/
	private static final String conversionFile1 = "xsl/New_HQMF.xsl";
	/** Constant for mat_narrGen.xsl. **/
	private static final String conversionFile2 = "xsl/mat_narrGen.xsl";
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
	private static final String XPATH_ALL_MSR_OBS_ELEMENTREF_ID =
			"/measure/measureObservations/clause//elementRef[not(@id = preceding:: clause//elementRef/@id)]/@id";
	/**X-path for Stratification Element Ref.**/
	private static final String XPATH_ALL_STARTA_ELEMENTREF_ID =
			"/measure/strata/clause//elementRef[not(@id = preceding:: clause//elementRef/@id)]/@id";
	/**X-path for Grouping Attributes.**/
	private static final String XPATH_ALL_GROUPED_ATTRIBUTES_UUID =
			"/measure/measureGrouping/group/clause//attribute[not(@qdmUUID = preceding:: clause//attribute/@qdmUUID)]/@qdmUUID";
	/**X-path for Measure Observation Attributes.**/
	private static final String XPATH_ALL_MSR_OBS_ATTRIBUTES_UUID =
			"/measure/measureObservations/clause//attribute[not(@qdmUUID = preceding:: clause//attribute/@qdmUUID)]/@qdmUUID";
	/**X-path for Stratification Attributes.**/
	private static final String XPATH_ALL_STARTA_ATTRIBUTES_UUID =
			"/measure/strata/clause//attribute[not(@qdmUUID = preceding:: clause//attribute/@qdmUUID)]/@qdmUUID";
	/**Logger.**/
	private static final Log LOGGER = LogFactory
			.getLog(SimpleEMeasureServiceImpl.class);

	/**MeasureDAO.**/
	@Autowired
	private MeasureDAO measureDAO;

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

	@Override
	public final ExportResult exportMeasureIntoSimpleXML(final String measureId,
			final String xmlString, final List<MatValueSet> matValueSets)
			throws Exception {

		System.out.println("exportMeasureIntoSimpleXML...xmlString:"
				+ xmlString);
		ExportResult result = new ExportResult();

		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		InputSource oldXmlstream = new InputSource(new StringReader(xmlString));
		Document originalDoc = docBuilder.parse(oldXmlstream);
		javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();

		List<String> qdmRefID = new ArrayList<String>();
		List<String> supplRefID = new ArrayList<String>();
		List<QualityDataSetDTO> masterRefID = new ArrayList<QualityDataSetDTO>();

		NodeList allGroupedElementRefIDs = (NodeList) xPath.evaluate(
				XPATH_ALL_GROUPED_ELEMENTREF_ID,
				originalDoc.getDocumentElement(), XPathConstants.NODESET);
		NodeList allMsrObsElementRefIDs = (NodeList) xPath.evaluate(
				XPATH_ALL_MSR_OBS_ELEMENTREF_ID,
				originalDoc.getDocumentElement(), XPathConstants.NODESET);
		NodeList allStrataElementRefIDs = (NodeList) xPath.evaluate(
				XPATH_ALL_STARTA_ELEMENTREF_ID,
				originalDoc.getDocumentElement(), XPathConstants.NODESET);
		NodeList allGroupedAttributesUUIDs = (NodeList) xPath.evaluate(
				XPATH_ALL_GROUPED_ATTRIBUTES_UUID,
				originalDoc.getDocumentElement(), XPathConstants.NODESET);
		NodeList allMsrObsAttributesUUIDs = (NodeList) xPath.evaluate(
				XPATH_ALL_MSR_OBS_ATTRIBUTES_UUID,
				originalDoc.getDocumentElement(), XPathConstants.NODESET);
		NodeList allStrataAttributesUUIDs = (NodeList) xPath.evaluate(
				XPATH_ALL_STARTA_ATTRIBUTES_UUID,
				originalDoc.getDocumentElement(), XPathConstants.NODESET);
		NodeList allQDMRefIDs = (NodeList) xPath.evaluate(
				XPATH_ELEMENTLOOKUP_QDM, originalDoc.getDocumentElement(),
				XPathConstants.NODESET);
		NodeList allSupplementIDs = (NodeList) xPath.evaluate(
				XPATH_SUPPLEMENTDATA_ELEMENTREF,
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
		findAndAddDTO(allMsrObsElementRefIDs, masterRefID, qdmRefID);
		findAndAddDTO(allMsrObsAttributesUUIDs, masterRefID, qdmRefID);
		findAndAddDTO(allStrataElementRefIDs, masterRefID, qdmRefID);
		findAndAddDTO(allStrataAttributesUUIDs, masterRefID, qdmRefID);

		Set<String> uniqueRefIds = new HashSet<String>(qdmRefID);
		qdmRefID = new ArrayList<String>(uniqueRefIds);

		findAndAddDTO(allSupplementIDs, masterRefID, supplRefID);
		wkbk = createEMeasureXLS(measureId, qdmRefID, supplRefID, matValueSets);
		result.wkbkbarr = getHSSFWorkbookBytes(wkbk);
		wkbk = null;
		return result;
	}

	/**
	 *@param nodeList - NodeList.
	 *@param masterRefID - List of QualityDataSetDTO.
	 *@param finalIdList - List of String.
	 * **/
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
	 * @param measureId - String.
	 * @return Measure.
	 * **/
	private mat.model.clause.Measure getMeasureName(final String measureId) {
		MeasurePackageService measureService = (MeasurePackageService) context
				.getBean("measurePackageService");
		mat.model.clause.Measure measure = measureService.getById(measureId);
		return measure;
	}

	@Override
	public final ExportResult getSimpleXML(final String measureId) throws Exception {
		mat.model.clause.Measure measure = measureDAO.find(measureId);
		MeasureExport measureExport = getMeasureExport(measureId);

		ExportResult result = new ExportResult();
		result.measureName = measure.getaBBRName();
		result.export = measureExport.getSimpleXML();
		return result;
	}

	@Override
	public final ExportResult getEMeasureXML(final String measureId) throws Exception {

		MeasureExport measureExport = getMeasureExport(measureId);
		return getEMeasureXML(measureId, measureExport);
	}

	/***
	 *@param measureId - String.
	 *@param measureExport - MeasureExport.
	 *@return ExportResult.
	 *@throws Exception - Exception.
	 * */
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

	@Override
	public final ExportResult getEMeasureHTML(final String measureId)
			throws Exception {
		ExportResult result = getEMeasureXML(measureId);
		String html = emeasureXMLToEmeasureHTML(result.export);
		result.export = html;
		return result;
	}

	/**
	 *@param emeasureXMLStr - String.
	 *@return String.
	 * **/
	private String emeasureXMLToEmeasureHTML(final String emeasureXMLStr) {
		XMLUtility xmlUtility = new XMLUtility();
		String html = xmlUtility.applyXSL(emeasureXMLStr,
				xmlUtility.getXMLResource(conversionFileHtml));
		return html;
	}

	/***
	 *@param ctx - ApplicationContext.
	 * */
	public void setApplicationContext(final ApplicationContext ctx) {
		this.context = ctx;
	}

	/**
	 *@param measureId - String.
	 *@param allQDMs - List.
	 *@param supplementalQDMS - List.
	 *@param matValueSets - List.
	 *@return HSSFWorkbook
	 *@throws Exception - Exception.
	 * ***/
	public final HSSFWorkbook createEMeasureXLS(final String measureId,
			final List<String> allQDMs, final List<String> supplementalQDMS,
			final List<MatValueSet> matValueSets) throws Exception {
		CodeListXLSGenerator clgen = new CodeListXLSGenerator();
		return clgen.getXLS(getMeasureName(measureId), allQDMs,
				qualityDataSetDAO, listObjectDAO, supplementalQDMS,
				matValueSets);
	}

	/**
	 *@return HSSFWorkbook
	 *@throws Exception - Exception.
	 * **/
	public final HSSFWorkbook createErrorEMeasureXLS() throws Exception {
		CodeListXLSGenerator clgen = new CodeListXLSGenerator();
		return clgen.getErrorXLS();
	}

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
	 *@param hssfwkbk - HSSFWorkbook.
	 *@return byte[].
	 *@throws IOException - IOException.
	 * **/
	private byte[] getHSSFWorkbookBytes(final HSSFWorkbook hssfwkbk)
			throws IOException {
		CodeListXLSGenerator clgen = new CodeListXLSGenerator();
		return clgen.getHSSFWorkbookBytes(hssfwkbk);
	}

	@Override
	public final ExportResult getEMeasureZIP(final String measureId) throws Exception {
		ExportResult result = new ExportResult();
		result.measureName = getMeasureName(measureId).getaBBRName();
		MeasureExport me = getMeasureExport(measureId);
		result.zipbarr = getZipBarr(measureId, me);
		return result;
	}

	/**
	 *@param measureId - String.
	 * @param me - MeasureExport.
	 * @return byte[].
	 * @throws Exception - Exception.
	 * **/
	public final byte[] getZipBarr(final String measureId, final MeasureExport me)
			throws Exception {
		byte[] wkbkbarr = null;
		if (me.getCodeList() == null) {
			wkbkbarr = getHSSFWorkbookBytes(createErrorEMeasureXLS());
		} else {
			wkbkbarr = me.getCodeListBarr();
		}

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

		ZipPackager zp = new ZipPackager();
		return zp.getZipBarr(emeasureName, wkbkbarr, emeasureXMLStr,
				emeasureHTMLStr, emeasureXSLUrl, me.getMeasure()
						.getValueSetDate().toString(), simpleXmlStr);
	}

	/**
	 *@param measureId - String.
	 *@return MeasureExport.
	 * **/
	private MeasureExport getMeasureExport(final String measureId) {
		MeasureExport measureExport = measureExportDAO
				.findForMeasure(measureId);
		String emeasureXMLStr = measureExport.getSimpleXML();
		mat.model.clause.Measure measure = measureDAO.find(measureId);
		Timestamp fdts = measure.getFinalizedDate();
		StringUtility su = new StringUtility();

		// 1 add finalizedDate field
		if (fdts != null && !emeasureXMLStr.contains("<finalizedDate")) {
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
		String vStr = measure.getMajorVersionStr();
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
	 * @param i -Integer.
	 * @return String.
	 * **/
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

	@Override
	public final ExportResult getBulkExportZIP(final String[] measureIds)
			throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(baos);
		Map<String, byte[]> filesMap = new HashMap<String, byte[]>();
		ExportResult result = null;
		int fileNameCounter = 1;
		DecimalFormat format = new DecimalFormat("#00");

		for (String measureId : measureIds) {
			result = new ExportResult();
			result.measureName = getMeasureName(measureId).getaBBRName();
			MeasureExport me = getMeasureExport(measureId);
			createFilesInBulkZip(measureId, me, filesMap,
					format.format(fileNameCounter++));
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
	 *@param measureId - String.
	 *@param me - MeasureExport.
	 *@param filesMap - Map.
	 *@param seqNum - String.
	 *@throws Exception - Exception.
	 *
	 * **/
	public final void createFilesInBulkZip(final String measureId,
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

		ZipPackager zp = new ZipPackager();
		zp.createBulkExportZip(emeasureName, wkbkbarr, emeasureXMLStr,
				emeasureHTMLStr, emeasureXSLUrl, me.getMeasure()
						.getValueSetDate().toString(), simpleXmlStr, filesMap,
				seqNum);
	}
}
