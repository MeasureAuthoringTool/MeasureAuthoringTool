package mat.server.service.impl;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
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
import mat.model.QualityDataSetDTO;
import mat.model.clause.MeasureExport;
import mat.server.service.MeasurePackageService;
import mat.server.service.SimpleEMeasureService;
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



public class SimpleEMeasureServiceImpl implements SimpleEMeasureService{
	
	//private static final String CONVERSION_FILE_1="xsl/db2hqmf_08262010_1.xsl";
	private static final String CONVERSION_FILE_1="xsl/New_HQMF.xsl";
	private static final String CONVERSION_FILE_2="xsl/mat_narrGen.xsl";
	private static final String CONVERSION_FILE_HTML="xsl/eMeasure.xsl";
	private static final String XPATH_ELEMENTLOOKUP_QDM="/measure/elementLookUp/qdm";
	private static final String XPATH_SUPPLEMENTDATA_ELEMENTREF="/measure/supplementalDataElements/elementRef/@id";
	private static final String SUPPLEMENTDATAELEMENT="supplementalDataElements";
	//This expression will find distinct elementRef records from SimpleXML.SimpleXML will have grouping which can have
	//repeated clauses containing repeated elementRef. This XPath expression will yield distinct elementRef's.
	private static final String XPATH_ALL_ELEMENTREF_ID="/measure/measureGrouping/group/clause//elementRef[not(@id = preceding:: clause//elementRef/@id)]/@id";
	private static final Log logger = LogFactory.getLog(SimpleEMeasureServiceImpl.class);
	
	@Autowired
	private MeasureDAO measureDAO;
	
	@Autowired
	private MeasureExportDAO measureExportDAO;	
	
	/*@Autowired
	private HeaderInfoGenerator headerInfoGenerator;
	
	@Autowired
	private ElementLookupGenerator elementLookupGenerator;
	
	@Autowired
	private SuppDataElementsGenerator suppDataElementsGenerator;
	
	@Autowired
	private ClauseBusinessService clauseService;

	@Autowired
	private AttachmentGenerator attachmentGenerator;*/
		
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private QualityDataSetDAO qualityDataSetDAO;
	
	@Autowired
	private ListObjectDAO listObjectDAO;
	
	//@Autowired
	//private MeasureValidationLogDAO measureValidationLogDAO;	
	
	/*@Autowired
	private QualityDataSetDAO qdsDAO;
	
	@Autowired
	private ClauseDAO clauseDAO;
	
	@Autowired
	private ContextDAO contextDAO;
	
	@Autowired
	private UserDAO userDAO;*/
	
	private HSSFWorkbook wkbk = null;
	
	
	
	/*public QualityDataSetDAO getQDSDAO() {
		return qdsDAO;
	}*/
	
	@Override
	public ExportResult exportMeasureIntoSimpleXML(String measureId ,String xmlString) throws Exception {	
		//Measure measure = createSimpleXML(measureId,xmlString);
		ExportResult result = new ExportResult();
		//result.measureName = getMeasureName(measureId).getaBBRName();
		//result.export = writeMeasureXML(measure);
	
		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		InputSource oldXmlstream = new InputSource(new StringReader(xmlString));
		Document originalDoc = docBuilder.parse(oldXmlstream);
		javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
		
		List<String> qdmRefID = new ArrayList<String>();
		List<String> supplRefID = new ArrayList<String>();
		List<QualityDataSetDTO> masterRefID = new ArrayList<QualityDataSetDTO>();
		
		NodeList allElementRefIDs = (NodeList) xPath.evaluate(XPATH_ALL_ELEMENTREF_ID, originalDoc.getDocumentElement(), XPathConstants.NODESET);
		NodeList allQDMRefIDs = (NodeList) xPath.evaluate(XPATH_ELEMENTLOOKUP_QDM, originalDoc.getDocumentElement(), XPathConstants.NODESET);
		NodeList allSupplementIDs = (NodeList) xPath.evaluate(XPATH_SUPPLEMENTDATA_ELEMENTREF, originalDoc.getDocumentElement(), XPathConstants.NODESET);
		for(int i=0;i<allQDMRefIDs.getLength();i++){
			Node newNode = allQDMRefIDs.item(i);
			QualityDataSetDTO dataSetDTO = new QualityDataSetDTO();
			dataSetDTO.setId(newNode.getAttributes().getNamedItem("id").getNodeValue().toString());
			dataSetDTO.setUuid(newNode.getAttributes().getNamedItem("uuid").getNodeValue().toString());
			masterRefID.add(dataSetDTO);
		}
		
		for(int i=0;i<allElementRefIDs.getLength();i++){
			Node idNode = allElementRefIDs.item(i);
			String idNodeValue = idNode.getNodeValue();
			for(QualityDataSetDTO dataSetDTO: masterRefID){
				if(dataSetDTO.getUuid().equalsIgnoreCase(idNodeValue)){
					qdmRefID.add(dataSetDTO.getId());
				}
			}
		}
		
		for(int i=0;i<allSupplementIDs.getLength();i++){
			Node idNode = allSupplementIDs.item(i);
			String idNodeValue = idNode.getNodeValue();
			for(QualityDataSetDTO dataSetDTO: masterRefID){
				if(dataSetDTO.getUuid().equalsIgnoreCase(idNodeValue)){
					supplRefID.add(dataSetDTO.getId());
				}
			}
		}
		wkbk = createEMeasureXLS(measureId,qdmRefID ,supplRefID);
		result.wkbkbarr = getHSSFWorkbookBytes(wkbk);
		wkbk = null;
		//doXSDValidation(result.export, measureId);
		return result;
	}
	
	/**
	 * use interimXML to generate and evaluate the emeasure xml
	 * persist the interimXML if a validation failure occurs
	 * @param interimXML 
	 * @param measureId
	 * @throws Exception
	 */
	/*private void doXSDValidation(String interimXML, String measureId) throws Exception{
		String emeasureXML = null;
		emeasureXML = getEMeasureXML(measureId, interimXML).export;
		mat.model.clause.Measure measure = measureDAO.find(measureId);
		XSDValidationService xvs = new XSDValidationService(emeasureXML, interimXML, measure, measureValidationLogDAO);
		xvs.validateEmeasureXML();
	}*/
	
	private mat.model.clause.Measure getMeasureName(String measureId) {
		MeasurePackageService measureService = (MeasurePackageService)context.getBean("measurePackageService");
		mat.model.clause.Measure measure = measureService.getById(measureId);
		return measure;
	}
	
	@Override
	public ExportResult getSimpleXML(String measureId) throws Exception {
		mat.model.clause.Measure measure = measureDAO.find(measureId);
		MeasureExport measureExport = getMeasureExport(measureId);
		
		ExportResult result = new ExportResult();
		result.measureName = measure.getaBBRName();
		result.export = measureExport.getSimpleXML();
		return result;
	}

	@Override
	public ExportResult getEMeasureXML(String measureId) throws Exception {	
		
		MeasureExport measureExport = getMeasureExport(measureId);
		String interimXML = measureExport.getSimpleXML();
		return getEMeasureXML(measureId, interimXML);
	}

	public ExportResult getEMeasureXML(String measureId, String interimXML) throws Exception {	
		mat.model.clause.Measure measure = measureDAO.find(measureId);
		XMLUtility xmlUtility = new XMLUtility();
		String tempXML = xmlUtility.applyXSL(interimXML, xmlUtility.getXMLResource(CONVERSION_FILE_1));
		String eMeasureXML = xmlUtility.applyXSL(tempXML, xmlUtility.getXMLResource(CONVERSION_FILE_2));
		
		ExportResult result = new ExportResult();
		result.measureName = measure.getaBBRName();
		result.export = eMeasureXML;
		
		return result;
	}
	
	@Override
	public ExportResult getEMeasureHTML(String measureId) throws Exception {
		ExportResult result = getEMeasureXML(measureId);
		String html = emeasureXMLToEmeasureHTML(result.export);
		result.export = html;
		return result;
	}
	
	private String emeasureXMLToEmeasureHTML(String emeasureXMLStr){
		XMLUtility xmlUtility = new XMLUtility();
		String html = xmlUtility.applyXSL(emeasureXMLStr, xmlUtility.getXMLResource(CONVERSION_FILE_HTML));
		return html;
	}
	
	/*public Measure createSimpleXML(String measureId,String xmlString) throws Exception { 
		Measure m = new Measure();
		//headers
		//New code.
		//Handle header information 
//		Get header metadata info from the data.
		Headers headers = retrieveMetaDataInfo(measureId);
		m.setHeaders(headers);
		String userOrgOid = "";
		User currentUser = userDAO.find(LoggedInUserUtil.getLoggedInUser());
		if(currentUser != null && currentUser.getOrgOID() != null) {
			userOrgOid = currentUser.getOrgOID();
		}
		//element lookup
		//Fetch Quality Data set from the database.
		//QualityDataSetDAO.getQDSElements(measureId, version);
		// set qdsel via ElementLookup
		List<QualityDataSet> qdsList = qdsDAO.getForMeasure(measureId);
		Elementlookup datacriteria = elementLookupGenerator.createElementLookup(qdsList, headers, userOrgOid);
		datacriteria.setMeasurecalcs(new ArrayList<Function>());
		datacriteria.setPropels(new ArrayList<Propel>());
		datacriteria.getQdsels().addAll(new ArrayList<Qdsel>());
		datacriteria.getIqdsels().addAll(new ArrayList<Iqdsel>());
		m.setElementlookup(datacriteria);
		
		SupplementalDataElements suppDataElements = suppDataElementsGenerator.createSuppDataElements(qdsList, headers, userOrgOid);
		suppDataElements.getQdsels().addAll(new ArrayList<Qdsel>());
		suppDataElements.getIqdsels().addAll(new ArrayList<Iqdsel>());
		m.setSupplementalDataElements(suppDataElements);
		
		//Load all clauses for this measure.
		//Filter system clauses.
		
		//US 594:- getting the supplimentalQDM Elements added to the measures. 
		List<QualityDataSet> supplementalQDMS = suppDataElementsGenerator.getOnlySupplimentalQDMS(qdsList);
		handleRules(measureId, m,supplementalQDMS);
		return m;
	}*/
	
	/*private Criterion createCriterionAndAddToMeasure(CriterionToInterim cti,
			Measure m, String measureId, Clause clause, boolean doEmptyCheck) throws Exception {
		Clause c = populateClauseRules(measureId, clause);
		if(doEmptyCheck && isEmpty(c))
			return null;
		
		Criterion criterion = cti.getCriterion(c);
		m.getElementlookup().getMeasurecalcs().addAll(cti.getFunctions());
		m.getElementlookup().addPropelsSansDups(cti.getPropels());
		List<Qdsel> qdselsret = cti.getQdsels();
		addQdselsToSimpleXML(m,qdselsret);
		m.setCriterion(criterion);
		
		return criterion;
	}
	*/
	/*private boolean isEmpty(Clause c){
		if(c.getDecisions().isEmpty())
			return true;
		if(c.getDecisions().get(0) instanceof Clause)
			return isEmpty((Clause) c.getDecisions().get(0));
		return false;
	}*/
	
	//TODO Add Numerator Exclusions
	/*private void handleRules(String measureId, Measure m,List<QualityDataSet> supplementalQDMS) throws Exception {
		Collection<PackageInfo> packages = 
			attachmentGenerator.buildPackageInfoForMeasure(measureId);
		QualityDataSetDAO qualityDataSetDAO = getQDSDAO();
		List<QualityDataSet> qds= qualityDataSetDAO.getForMeasure(measureId);
		CriterionToInterim cti = new CriterionToInterim(qds, context);

		boolean multiplePackages = packages.size() != 1;
		int counter = 1;
		for(PackageInfo packageInfo : packages) {
			Criterion population = createCriterionAndAddToMeasure(cti,m, measureId, packageInfo.population, false); 
			
			String pTitle = multiplePackages ? "Initial Patient Population " + counter : "Initial Patient Population";
			String dTitle = "Denominator " + counter;
			String pClause = packageInfo.population.getName();
			
			counter++;
			
			
			 * should check for measure type
			 * if a continuous variable measure, then denominator will be null
			 
			if(packageInfo.denominator != null){
				Criterion denominator = createCriterionAndAddToMeasure(cti, m, measureId, packageInfo.denominator, false);
				attachmentGenerator.addAttachment((Denominator)denominator, population, pClause, pTitle);
				String dClause = packageInfo.denominator.getName();
				
				for(Clause numeratorClause : packageInfo.numerators) {
					Criterion crit = createCriterionAndAddToMeasure(cti, m, measureId, numeratorClause, true);
					boolean addAttachment = isRatioMeasure(m) || (multiplePackages && crit != null); 
					if(addAttachment) {
						attachmentGenerator.addAttachment((CriterionWithAttachments) crit, denominator, dClause, dTitle);
					}
				}
				for(Clause exclusionClause : packageInfo.numExclusions) {
					Criterion crit = createCriterionAndAddToMeasure(cti, m, measureId, exclusionClause, true);
					if(multiplePackages && crit != null) {
						attachmentGenerator.addAttachment((CriterionWithAttachments) crit, denominator, dClause, dTitle);
					}
				}
				
				for(Clause exceptionClause : packageInfo.exceptions) {
					
					Criterion crit = createCriterionAndAddToMeasure(cti, m, measureId, exceptionClause, true);
					if(multiplePackages && crit != null) {
						attachmentGenerator.addAttachment((CriterionWithAttachments) crit, denominator, dClause, dTitle);
					}
				}
			}
			
			for(Clause exclusionClause : packageInfo.exclusions) {
				Criterion crit = createCriterionAndAddToMeasure(cti, m, measureId, exclusionClause, true);
				if(multiplePackages && crit != null) {
					attachmentGenerator.addAttachment((CriterionWithAttachments) crit, population, pClause, pTitle);
				}
			}
			for(Clause measurePopulationClause : packageInfo.measurePopulation) {
				Criterion crit = createCriterionAndAddToMeasure(cti, m, measureId, measurePopulationClause, true);
				//an attachment for a measurePopulation should always be made
				if(crit != null) {
					attachmentGenerator.addAttachment((CriterionWithAttachments) crit, population, pClause, pTitle);
				}
			}
			for(Clause measureObservationClause : packageInfo.measureObservation) {
				Criterion crit = createCriterionAndAddToMeasure(cti, m, measureId, measureObservationClause, true);
			}
		}
		
		US597 - US598
		List<Clause> stratifications =  getStrataFromMeasure(measureId);
		for(Clause stratificationClause : stratifications) {
			Criterion crit = createCriterionAndAddToMeasure(cti, m, measureId, stratificationClause, true);
			//no attachments are necessary for stratification system clauses
		}
		
		//wkbk = createEMeasureXLS(measureId, cti.getQdmRefs(),supplementalQDMS);
	}*/
	
	/*private boolean isClauseEmpty(Clause c){
		CriterionToInterimUtility ctiu = new CriterionToInterimUtility();
		IQDSTerm term = ctiu.peel(c);
		return term.getDecisions().isEmpty();
	}*/
	
	
	/**
	 * US597
	 * @param measureId
	 * @return List of Stratification Clauses
	 * 
	 */
	/*private List<Clause> getStrataFromMeasure(String measureId){
		Context ctx = contextDAO.getContext(ConstantMessages.STRAT_CONTEXT_DESC);
		return clauseDAO.getAllStratificationClauses(measureId,ctx.getId());
	}*/
	
	/*private void addQdselsToSimpleXML(Measure m, List<Qdsel> qdselsret) {
		for (Qdsel q: qdselsret) {
			if (!m.getElementlookup().getQdsels().contains(q)) {
				m.getElementlookup().getQdsels().add(q);		
			}
		}
	}

	private Clause populateClauseRules(String measureId, Clause cl) {
		// TODO Auto-generated method stub
		return clauseService.loadClause(measureId, cl.getId(), context);
	}*/

	/*private Headers retrieveMetaDataInfo(String measureId) {
		return headerInfoGenerator.getAllHeaders(measureId);
	}*/
	
	/*private String writeMeasureXML(Measure measure) throws IOException {		

		SimpleXMLWriter sxw = new SimpleXMLWriter();
		String x = sxw.toXML(measure);
//		System.out.println(x);
		return x;
	}*/

	public void setApplicationContext(ApplicationContext ctx) {
		this.context = ctx;
	}
	
	public HSSFWorkbook createEMeasureXLS(String measureId, List<String> allQDMs,List<String> supplementalQDMS) throws Exception {
		CodeListXLSGenerator clgen = new CodeListXLSGenerator();
		return clgen.getXLS(getMeasureName(measureId), allQDMs, qualityDataSetDAO,listObjectDAO,supplementalQDMS);
	}
	
	public HSSFWorkbook createErrorEMeasureXLS() throws Exception {
		CodeListXLSGenerator clgen = new CodeListXLSGenerator();
		return clgen.getErrorXLS();
	}
	
	@Override
	public ExportResult getEMeasureXLS(String measureId) throws Exception {
		ExportResult result = new ExportResult();
		result.measureName = getMeasureName(measureId).getaBBRName();
		result.packageDate = DateUtility.convertDateToStringNoTime2(getMeasureName(measureId).getValueSetDate());
		MeasureExport me = getMeasureExport(measureId);
		if(me.getCodeList()==null)
			result.wkbkbarr = getHSSFWorkbookBytes(createErrorEMeasureXLS());
		else
			result.wkbkbarr = me.getCodeListBarr();
		return result;
	}
	@Override
	public ExportResult getValueSetXLS(String valueSetId) throws Exception {
		ExportResult result = new ExportResult();
		ListObject lo = listObjectDAO.find(valueSetId);
		ValueSetXLSGenerator vsxg = new ValueSetXLSGenerator();
		HSSFWorkbook wkbk = null;
		try{
			wkbk = vsxg.getXLS(valueSetId, lo);
		}catch(Exception e){
			logger.error(e.getMessage());
			wkbk = vsxg.getErrorXLS();
		}
		result.wkbkbarr = vsxg.getHSSFWorkbookBytes(wkbk);
		result.valueSetName = lo.getName();
		result.lastModifiedDate = lo.getLastModified()!=null ? DateUtility.convertDateToStringNoTime2(lo.getLastModified()) : null;
		return result;
	}
	private byte[] getHSSFWorkbookBytes(HSSFWorkbook hssfwkbk) throws IOException{
		CodeListXLSGenerator clgen = new CodeListXLSGenerator();
			return clgen.getHSSFWorkbookBytes(hssfwkbk);
	}
	@Override
	public ExportResult getEMeasureZIP(String measureId) throws Exception {
		ExportResult result = new ExportResult();
		result.measureName = getMeasureName(measureId).getaBBRName();
		MeasureExport me = getMeasureExport(measureId);		
		result.zipbarr = getZipBarr(measureId, me);
		return result;
	}
	
	public byte[] getZipBarr(String measureId, MeasureExport me) throws Exception{
		byte[] wkbkbarr = null;
		if(me.getCodeList()==null)
			wkbkbarr = getHSSFWorkbookBytes(createErrorEMeasureXLS());
		else
			wkbkbarr = me.getCodeListBarr();
		
		StringUtility su = new StringUtility();
		ExportResult emeasureXMLResult = getEMeasureXML(measureId);
		String emeasureName = emeasureXMLResult.measureName;
		String emeasureXMLStr = emeasureXMLResult.export;
		String repee = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		String repor = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+su.nl+"<?xml-stylesheet type=\"text/xsl\" href=\"xslt/eMeasure.xsl\"?>";
		emeasureXMLStr = repor+emeasureXMLStr.substring(repee.length());
		String emeasureHTMLStr = emeasureXMLToEmeasureHTML(emeasureXMLStr);
		String simpleXmlStr = me.getSimpleXML();
		XMLUtility xmlUtility = new XMLUtility();
		String emeasureXSLUrl = xmlUtility.getXMLResource(CONVERSION_FILE_HTML);
		
		ZipPackager zp = new ZipPackager();
		return zp.getZipBarr(emeasureName, wkbkbarr, emeasureXMLStr, emeasureHTMLStr,emeasureXSLUrl,me.getMeasure().getValueSetDate().toString(), simpleXmlStr);
	}
	
	
	
	/*private boolean isRatioMeasure(Measure m){
		boolean hasScoringType = m.getHeaders().getScores() == null ? false :
			m.getHeaders().getScores().getScores().isEmpty() ? false :
				true;
		if(hasScoringType){
			String scoringType = m.getHeaders().getScores().getScores().get(0).getTtext();
			return scoringType.equalsIgnoreCase("RATIO");
		}
		return false;
	}*/
	
	private MeasureExport getMeasureExport(String measureId){
		MeasureExport measureExport = measureExportDAO.findForMeasure(measureId);
		String emeasureXMLStr = measureExport.getSimpleXML();
		mat.model.clause.Measure measure = measureDAO.find(measureId);
		Timestamp fdts = measure.getFinalizedDate();
		StringUtility su = new StringUtility();
		
		// 1 add finalizedDate field
		if(fdts != null && !emeasureXMLStr.contains("<finalizedDate")){
			String fdstr = convertTimestampToString(fdts);
			String repee = "</headers>";
			String repor = su.nl+"<finalizedDate value=\""+fdstr+"\"/>"
				+su.nl+"</headers>";
			int offset = emeasureXMLStr.indexOf(repee);
			emeasureXMLStr = emeasureXMLStr.substring(0,offset)+repor+emeasureXMLStr.substring(offset+repee.length());
			measureExport.setSimpleXML(emeasureXMLStr);
		}
		// 2 add version field
		final String VERSION_START = "<version>";
		final String VERSION_END = "</version>";
		String vStr = measure.getMajorVersionStr();
		if(emeasureXMLStr.contains(VERSION_START)){
			int start = emeasureXMLStr.indexOf(VERSION_START)+VERSION_START.length();
			int end = emeasureXMLStr.indexOf(VERSION_END);
			emeasureXMLStr = emeasureXMLStr.substring(0, start) + vStr + emeasureXMLStr.substring(end);
			measureExport.setSimpleXML(emeasureXMLStr);
		}else{
			String repee = "</headers>";
			String repor = su.nl+VERSION_START+vStr+VERSION_END
				+su.nl+"</headers>";
			int offset = emeasureXMLStr.indexOf(repee);
			emeasureXMLStr = emeasureXMLStr.substring(0,offset)+repor+emeasureXMLStr.substring(offset+repee.length());
			measureExport.setSimpleXML(emeasureXMLStr);
		}
		
		return measureExport;
	}
	
	/**
	 * 
	 * @param ts
	 * @return yyyymmddhhss-zzzz
	 */
	@SuppressWarnings("deprecation")
	private String convertTimestampToString(Timestamp ts){
		String hours = getTwoDigitString(ts.getHours());
		String mins = getTwoDigitString(ts.getMinutes());
		String month = getTwoDigitString(ts.getMonth()+1);
		String day = getTwoDigitString(ts.getDate());
		String timeZone = "-"+getTwoDigitString(ts.getTimezoneOffset()/60)+"00";
		
		String tsStr = (ts.getYear()+1900)+month+day+hours+mins+timeZone;
		return tsStr;
	}
	private String getTwoDigitString(int i){
		String ret = i+"";
		if(ret.length()==1)
			ret = "0"+ret;
		return ret;
	}
	
	public HSSFWorkbook getWkbk(){
		return wkbk;
	}

	@Override
	public ExportResult getBulkExportZIP(String[] measureIds) throws Exception {
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
			createFilesInBulkZip(measureId, me, filesMap,  format.format(fileNameCounter++));
		}
		
		ZipPackager zp = new ZipPackager();
		double size = 1024 * 1024 * 100;
		Set<Entry<String, byte[]>> set = filesMap.entrySet();		
		for (Entry<String, byte[]> fileArr : set) {
			zp.addBytesToZip(fileArr.getKey(), fileArr.getValue(), zip);
		}
		if(baos.size() > size){
			zip.close();	
			throw new ZipException("Exceeded Limit :" + baos.size());
		}
		zip.close();
		logger.debug(baos.size());
		result.zipbarr = baos.toByteArray();
		return result;
	}
	
	
	public void createFilesInBulkZip(String measureId, MeasureExport me, Map<String, byte[]> filesMap, String seqNum) throws Exception{
		byte[] wkbkbarr = null;
		if(me.getCodeList()==null)
			wkbkbarr = getHSSFWorkbookBytes(createErrorEMeasureXLS());
		else
			wkbkbarr = me.getCodeListBarr();
		
		StringUtility su = new StringUtility();
		ExportResult emeasureXMLResult = getEMeasureXML(measureId);
		String emeasureName = emeasureXMLResult.measureName;
		String emeasureXMLStr = emeasureXMLResult.export;
		String repee = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		String repor = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+su.nl+"<?xml-stylesheet type=\"text/xsl\" href=\"xslt/eMeasure.xsl\"?>";
		emeasureXMLStr = repor+emeasureXMLStr.substring(repee.length());
		String emeasureHTMLStr = emeasureXMLToEmeasureHTML(emeasureXMLStr);
		String simpleXmlStr = me.getSimpleXML();
		XMLUtility xmlUtility = new XMLUtility();
		String emeasureXSLUrl = xmlUtility.getXMLResource(CONVERSION_FILE_HTML);
		
		ZipPackager zp = new ZipPackager();
		zp.createBulkExportZip(emeasureName, wkbkbarr, emeasureXMLStr, emeasureHTMLStr,
				emeasureXSLUrl,me.getMeasure().getValueSetDate().toString(), simpleXmlStr, filesMap, seqNum);
	}
}
