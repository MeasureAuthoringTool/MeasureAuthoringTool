package mat.server;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import mat.model.CQLLibraryOwnerReportDTO;
import mat.model.MeasureOwnerReportDTO;
import mat.model.User;
import mat.model.clause.Measure;
import mat.server.service.MeasureLibraryService;
import mat.server.service.MeasurePackageService;
import mat.server.service.SimpleEMeasureService;
import mat.server.service.SimpleEMeasureService.ExportResult;
import mat.server.service.UserService;
import mat.server.service.impl.ZipPackager;
import mat.shared.CQLErrors;
import mat.shared.FileNameUtility;
import mat.shared.InCorrectUserRoleException;
import mat.shared.SaveUpdateCQLResult;
/**
 * The Class ExportServlet.
 */
public class ExportServlet extends HttpServlet {
	
	private static final String LIBRARY_ID = "libraryid";
	private static final String EXPORT_MEASURE_OWNER = "exportMeasureOwner";
	private static final String EXPORT_CQL_ERROR_FILE_FOR_STAND_ALONE = "errorFileStandAlone";
	private static final String EXPORT_CQL_ERROR_FILE_FOR_MEASURE = "errorFileMeasure";
	/** The Constant EXPORT_ACTIVE_NON_ADMIN_USERS_CSV. */
	private static final String EXPORT_ACTIVE_NON_ADMIN_USERS_CSV = "exportActiveNonAdminUsersCSV";
	private static final String EXPORT_ALL_USERS_CSV = "exportAllUsersCSV";
	
	/** The Constant EXPORT_ACTIVE_OID_CSV. */
	private static final String EXPORT_ACTIVE_OID_CSV = "exportActiveOIDCSV";
	
	private static final String EXPORT_ACTIVE_USER_CQL_LIBRARY_OWNERSHIP = "exportCQLLibraryOwner";
	
	/** The Constant ZIP. */
	private static final String ZIP = "zip";
	
	/** Human readable for Subtree Node *. */
	private static final String SUBTREE_HTML = "subtreeHTML";
	
	/** The Constant CODELIST. */
	private static final String CODELIST = "codelist";
	
	/** The Constant SAVE. */
	private static final String SAVE = "save";
	
	/** The Constant ATTACHMENT_FILENAME. */
	private static final String ATTACHMENT_FILENAME = "attachment; filename=";
	
	/** The Constant CONTENT_DISPOSITION. */
	private static final String CONTENT_DISPOSITION = "Content-Disposition";
	
	/** The Constant TEXT_XML. */
	private static final String TEXT_XML = "text/xml";
	
	/** The Constant APPLICATION_JSON. */
	private static final String APPLICATION_JSON = "application/json";
	
	/** The Constant TEXT_HTML. */
	private static final String TEXT_HTML = "text/html";
	
	/** The Constant TEST_PLAN */
	private static final String TEXT_PLAIN = "text/plain"; 
	
	/** The Constant CONTENT_TYPE. */
	private static final String CONTENT_TYPE = "Content-Type";
	
	/** The Constant EMEASURE. */
	private static final String EMEASURE = "emeasure";
	
	/** The Constant SIMPLEXML. */
	private static final String SIMPLEXML = "simplexml";
	
	/** The Constant TYPE_PARAM. */
	private static final String TYPE_PARAM = "type";
	
	/** The Constant XML_PARAM. */
	private static final String XML_PARAM = "xml";
	
	/** The Constant FORMAT_PARAM. */
	private static final String FORMAT_PARAM = "format";
	
	/** The Constant ID_PARAM. */
	private static final String ID_PARAM = "id";
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(ExportServlet.class);
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4539514145289378238L;
	
	/** The context. */
	protected ApplicationContext context;
	
	private static final String CQL_LIBRARY = "cqlLibrary";
	private static final String ELM = "elm"; 
	private static final String JSON = "json"; 
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		MeasurePackageService service = getMeasurePackageService();
		MeasureLibraryService measureLibraryService = getMeasureLibraryService();
		
		String id = req.getParameter(ID_PARAM);
		String format = req.getParameter(FORMAT_PARAM);
		String type = req.getParameter(TYPE_PARAM);
		String libraryId = req.getParameter(LIBRARY_ID);
		Measure measure = null;
		ExportResult export = null;
		Date exportDate = null;
		
		logger.info("FOMAT: " + format);
		
		if (id!= null) {
			measure = service.getById(id);
			exportDate = measure.getExportedDate();
		}

		FileNameUtility fnu = new FileNameUtility();
		try {
			if (SIMPLEXML.equals(format)) {
				export = exportSimpleXML(resp, measureLibraryService, id, type,
						measure, fnu);
			} else if (EMEASURE.equals(format)) {
				export = exportEmeasureXML(resp, measureLibraryService, id,
						type,  measure, export, fnu);
			} else if (CODELIST.equals(format)) {
				export = exportCodeListXLS(resp, measureLibraryService, id,
						measure, fnu);
			} else if (CQL_LIBRARY.equals(format)) {
				export = exportCQLLibraryFile(resp, measureLibraryService, id, type,
						measure, fnu);
			} else if(ELM.equals(format)) {
				export = exportELMFile(resp, measureLibraryService, id, type, 
						measure, fnu); 
					
			} else if(JSON.equals(format)) {
				export = exportJSONFile(resp, measureLibraryService, id, type, 
						measure, fnu); 
					
			}else if (ZIP.equals(format)) {
				export = exportEmeasureZip(resp, measureLibraryService, id,
						measure, exportDate, fnu);
			} else if (SUBTREE_HTML.equals(format)){
				export = exportSubTreeHumanReadable(req, resp, id);
			} else if (EXPORT_ACTIVE_NON_ADMIN_USERS_CSV.equals(format)) {
				exportActiveUserListCSV(resp, fnu);
			} else if (EXPORT_ACTIVE_OID_CSV.equals(format)) {
				exportActiveOrganizationListCSV(resp, fnu);
			} else if(EXPORT_MEASURE_OWNER.equalsIgnoreCase(format)){
				exportActiveUserMeasureOwnershipListCSV(resp,fnu);
			} else if (EXPORT_ALL_USERS_CSV.equals(format)) {
				exportAllUserCSV(resp, fnu);
			} else if(EXPORT_ACTIVE_USER_CQL_LIBRARY_OWNERSHIP.equals(format)) {
				exportActiveUserCQLLibraryOwnershipListCSV(resp, fnu);
			} else if(EXPORT_CQL_ERROR_FILE_FOR_MEASURE.equalsIgnoreCase(format)) {
				exportErrorFileForMeasure(resp, measureLibraryService, id);
			} else if(EXPORT_CQL_ERROR_FILE_FOR_STAND_ALONE.equalsIgnoreCase(format)) {
				exportErrorFileForStandAloneLib(resp, libraryId);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}finally {
			if(resp!=null && resp.getOutputStream()!=null)
				resp.getOutputStream().close();
		}		
	}		
	
	private void exportErrorFileForMeasure(HttpServletResponse resp, MeasureLibraryService measureLibraryService,
			String id) throws IOException {
		SaveUpdateCQLResult result = measureLibraryService.getMeasureCQLLibraryData(id);
		addLineNumberAndErrorMessageToCQLErrorExport(resp, result);
	}
	
	private void exportErrorFileForStandAloneLib(HttpServletResponse resp,	String id) throws IOException {
		SaveUpdateCQLResult result = getCQLLibraryService().getCQLLibraryFileData(id);
		addLineNumberAndErrorMessageToCQLErrorExport(resp, result);
	}

	private void addLineNumberAndErrorMessageToCQLErrorExport(HttpServletResponse resp, SaveUpdateCQLResult result)
			throws IOException {
		StringBuilder sb = new StringBuilder();
		String cqlString = result.getCqlString();
		String[] cqlLinesArray = cqlString.split("\n");
		for(int i=0;i<cqlLinesArray.length;i++) {
			System.out.println((i+1)+" "+cqlLinesArray[i]);
			sb.append((i+1)+" "+cqlLinesArray[i] + "\r\n");
		}
		if (!result.getCqlErrors().isEmpty()) {
			sb.append("/*******************************************************************************************************************");
			for (CQLErrors error : result.getCqlErrors()) {
				String errorMessage = new String();
				errorMessage = errorMessage.concat("Line " + error.getErrorInLine()+ ":" +error.getErrorMessage());
				sb.append("\r\n");
				sb.append(errorMessage + "\r\n");
				
			}
			sb.append("*******************************************************************************************************************/");
		}
		System.out.println(sb.toString());
		resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
				+ result.getLibraryName() + ".txt");
		resp.getOutputStream().write(sb.toString().getBytes());
	}

	
	
	private ExportResult exportELMFile(HttpServletResponse resp, MeasureLibraryService measureLibraryService, String id,
			String type, Measure measure, FileNameUtility fnu) throws Exception {
		
		ExportResult export = getService().getELMFile(id); 
		
		if(export.getIncludedCQLExports().size() > 0){
			ZipPackager zp = new ZipPackager();
			zp.getCQLZipBarr(export, "xml");
			
			resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + fnu.getZipName(export.measureName + "_" + "ELM"));
			resp.setContentType("application/zip");
			resp.getOutputStream().write(export.zipbarr);
			export.zipbarr = null;
		}else if (SAVE.equals(type)) {
			String currentReleaseVersion = measure.getReleaseVersion();
			if(currentReleaseVersion.contains(".")){
				currentReleaseVersion = currentReleaseVersion.replace(".", "_");
			}
			System.out.println("Release version zip " + currentReleaseVersion);
			resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
					//+ fnu.getELMFileName(export.getCqlLibraryName()));
					+ export.getCqlLibraryName() + ".xml");
			resp.getOutputStream().write(export.export.getBytes());
		} else {
			resp.setHeader(CONTENT_TYPE, TEXT_XML);
			resp.getOutputStream().write(export.export.getBytes());
		}
		return export;
	}
	
	private ExportResult exportJSONFile(HttpServletResponse resp, MeasureLibraryService measureLibraryService, String id,
			String type, Measure measure, FileNameUtility fnu) throws Exception {
		
		ExportResult export = getService().getJSONFile(id); 
		
		if(export.getIncludedCQLExports().size() > 0){
			ZipPackager zp = new ZipPackager();
			zp.getCQLZipBarr(export, "json");
			
			resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + fnu.getZipName(export.measureName + "_" + "JSON"));
			resp.setContentType("application/zip");
			resp.getOutputStream().write(export.zipbarr);
			export.zipbarr = null;
		}else if (SAVE.equals(type)) {
			String currentReleaseVersion = measure.getReleaseVersion();
			if(currentReleaseVersion.contains(".")){
				currentReleaseVersion = currentReleaseVersion.replace(".", "_");
			}
			System.out.println("Release version zip " + currentReleaseVersion);
			resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
					//+ fnu.getELMFileName(export.getCqlLibraryName()));
					+ export.getCqlLibraryName() + ".json");
			resp.getOutputStream().write(export.export.getBytes());
		} else {
			resp.setHeader(CONTENT_TYPE, APPLICATION_JSON);
			resp.getOutputStream().write(export.export.getBytes()); 
		}
		return export;
	}

	
	
	private ExportResult exportCQLLibraryFile(HttpServletResponse resp,
			MeasureLibraryService measureLibraryService, String id,
			String type, Measure measure, FileNameUtility fnu) throws Exception {
		
		ExportResult export;
		export = getService().getCQLLibraryFile(id);
		
		if(export.getIncludedCQLExports().size() > 0){
			ZipPackager zp = new ZipPackager();
			zp.getCQLZipBarr(export, "cql");
			
			resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + fnu.getZipName(export.measureName + "_" + "CQL"));
			resp.setContentType("application/zip");
			resp.getOutputStream().write(export.zipbarr);
			export.zipbarr = null;
		}else if (SAVE.equals(type)) {
			String currentReleaseVersion = measure.getReleaseVersion();
			if(currentReleaseVersion.contains(".")){
				currentReleaseVersion = currentReleaseVersion.replace(".", "_");
			}
			System.out.println("Release version zip " + currentReleaseVersion);
			resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
					//+ fnu.getCQLFileName(export.getCqlLibraryName()));
					+ export.getCqlLibraryName()+".cql");
			resp.getOutputStream().write(export.export.getBytes());
		} else {
			resp.setHeader(CONTENT_TYPE, TEXT_PLAIN);
			resp.getOutputStream().write(export.export.getBytes());
		}
		return export;
	}
	
	public void exportActiveOrganizationListCSV(HttpServletResponse resp,
			FileNameUtility fnu) throws IOException {
		String userRole = LoggedInUserUtil.getLoggedInUserRole();
		if ("Administrator".equalsIgnoreCase(userRole)) {
			String csvFileString = generateCSVOfActiveUserOIDs();
			Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String activeUserCSVDate = formatter.format(new Date());
			resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
					+ fnu.getCSVFileName("activeOrganizationOids", activeUserCSVDate) + ";");
			resp.setContentType("text/csv");
			resp.getOutputStream().write(csvFileString.getBytes());
		}
	}
	
	public void exportActiveUserListCSV(HttpServletResponse resp,
			FileNameUtility fnu) throws InCorrectUserRoleException, IOException {
		String userRole = LoggedInUserUtil.getLoggedInUserRole();
		if ("Administrator".equalsIgnoreCase(userRole)) {
			String csvFileString = generateCSVOfActiveUserEmails();
			Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String activeUserCSVDate = formatter.format(new Date());
			resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
					+ fnu.getCSVFileName("activeUsers", activeUserCSVDate) + ";");
			resp.setContentType("text/csv");
			resp.getOutputStream().write(csvFileString.getBytes());
		}
	}
	private void exportAllUserCSV(HttpServletResponse resp, FileNameUtility fnu) throws InCorrectUserRoleException, IOException {
		String userRole = LoggedInUserUtil.getLoggedInUserRole();
		if ("Administrator".equalsIgnoreCase(userRole)) {
			String csvFileString = generateCSVOfAllUser();
			Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String allUserCSVDate = formatter.format(new Date());
			resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
					+ fnu.getCSVFileName("allUsersReport", allUserCSVDate) + ";");
			resp.setContentType("text/csv");
			resp.getOutputStream().write(csvFileString.getBytes());
		}
		
	}
	public void exportActiveUserMeasureOwnershipListCSV(HttpServletResponse resp,
			FileNameUtility fnu) throws InCorrectUserRoleException, IOException, XPathExpressionException {
		String userRole = LoggedInUserUtil.getLoggedInUserRole();
		if ("Administrator".equalsIgnoreCase(userRole)) {
			String csvFileString = generateCSVOfMeasureOwnershipForActiveUser();
			Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String aCSVDate = formatter.format(new Date());
			resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
					+ fnu.getCSVFileName("activeUsersMeasureOwnership", aCSVDate) + ";");
			resp.setContentType("text/csv");
			resp.getOutputStream().write(csvFileString.getBytes());
		}
	}
	
	public void exportActiveUserCQLLibraryOwnershipListCSV(HttpServletResponse resp, FileNameUtility fnu) throws IOException {
		String userRole = LoggedInUserUtil.getLoggedInUserRole();
		if("Administrator".equalsIgnoreCase(userRole)) {
			String csvFileString = generateCSVOfCQLLibraryOwnershipForActiveUser();
			Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String aCSVDate = formatter.format(new Date());
			resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
					+ fnu.getCSVFileName("activeUsersCQLLibraryOwnership", aCSVDate) + ";");
			resp.setContentType("text/csv");
			resp.getOutputStream().write(csvFileString.getBytes());
		}
	}
	
	
	public ExportResult exportValueSetListXLS(HttpServletResponse resp,
			MeasureLibraryService measureLibraryService, String id,
			Measure measure, FileNameUtility fnu)
					throws Exception, IOException {
		ExportResult export;
		export = getService().getValueSetXLS(id);
		
		String currentReleaseVersion = measure.getReleaseVersion();
		if(currentReleaseVersion.contains(".")){
			currentReleaseVersion = currentReleaseVersion.replace(".", "_");
		}
		System.out.println("Release version zip " + currentReleaseVersion);
		
		resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
				+ fnu.getValueSetXLSName(export.valueSetName + "_" + currentReleaseVersion, export.lastModifiedDate));
		resp.setContentType("application/vnd.ms-excel");
		resp.getOutputStream().write(export.wkbkbarr);
		export.wkbkbarr = null;
		return export;
	}
	
	public ExportResult exportSubTreeHumanReadable(HttpServletRequest req,
			HttpServletResponse resp, String id) throws Exception {
		ExportResult export;
		String nodeXML = req.getParameter(XML_PARAM);
		System.out.println("Export servlet received node xml:"+nodeXML +" and Measure ID:"+id);
		export = getService().getHumanReadableForNode(id,nodeXML);
		resp.setHeader(CONTENT_TYPE, TEXT_HTML);
		resp.getOutputStream().println(export.export);
		return export;
	}
	
	public ExportResult exportEmeasureZip(HttpServletResponse resp,
			MeasureLibraryService measureLibraryService, String id,
			Measure measure, Date exportDate,
			FileNameUtility fnu) throws Exception,
			IOException {
		ExportResult export;
		export = getService().getEMeasureZIP(id,exportDate);
				
		String currentReleaseVersion = measure.getReleaseVersion();
		if(currentReleaseVersion.contains(".")){
			currentReleaseVersion = currentReleaseVersion.replace(".", "_");
		}
		System.out.println("Release version zip " + currentReleaseVersion);
		resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + fnu.getZipName(export.measureName + "_" + currentReleaseVersion));
		resp.setContentType("application/zip");
		resp.getOutputStream().write(export.zipbarr);
		export.zipbarr = null;
		return export;
	}
	
	public ExportResult exportCodeListXLS(HttpServletResponse resp,
			MeasureLibraryService measureLibraryService, String id,
			Measure measure, FileNameUtility fnu)
					throws Exception, IOException {
		ExportResult export;
		export = getService().getEMeasureXLS(id);
		String currentReleaseVersion = measure.getReleaseVersion();
		if(currentReleaseVersion.contains(".")){
			currentReleaseVersion = currentReleaseVersion.replace(".", "_");
		}
		System.out.println("Release version zip " + currentReleaseVersion);
		resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
				+ fnu.getEmeasureXLSName(export.measureName + "_" + currentReleaseVersion, export.packageDate));
		resp.setContentType("application/vnd.ms-excel");
		resp.getOutputStream().write(export.wkbkbarr);
		export.wkbkbarr = null;
		return export;
	}
	
	public ExportResult exportEmeasureXML(HttpServletResponse resp,
			MeasureLibraryService measureLibraryService, String id,
			String type, Measure measure,
			ExportResult export, FileNameUtility fnu) throws Exception {
		
		String currentReleaseVersion = measure.getReleaseVersion();
		if(currentReleaseVersion.contains(".")){
			currentReleaseVersion = currentReleaseVersion.replace(".", "_");
		}
		System.out.println("Release version zip " + currentReleaseVersion);
		if (currentReleaseVersion.equals("v3")){
			if ("open".equals(type)) {
				export = getService().getEMeasureHTML(id);
				resp.setHeader(CONTENT_TYPE, TEXT_HTML);
			} else if (SAVE.equals(type)) {
				export = getService().getEMeasureXML(id);
				//						if (measure.getExportedDate().before(measureLibraryService.getFormattedReleaseDate(measureLibraryService.getReleaseDate()))){
				resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
						+ fnu.getEmeasureXMLName(export.measureName + "_" + currentReleaseVersion));
				//						}
			}
		}else{
			export = exportEMeasureForNewMeasures(resp, id, type, export, fnu,
					measure.getReleaseVersion());
		}
		resp.getOutputStream().write(export.export.getBytes());
		return export;
	}


	/**
	 * Export Human Readable (HTML) or HQMF XML for measures > v3.0
	 * 
	 * @param resp
	 * @param id
	 * @param type
	 * @param export
	 * @param fnu
	 * @param currentReleaseVersion
	 * @return
	 * @throws Exception
	 */
	public ExportResult exportEMeasureForNewMeasures(HttpServletResponse resp,
			String id, String type, ExportResult export, FileNameUtility fnu,
			String currentReleaseVersion) throws Exception {
		
		if ("open".equals(type)) {
			export = getService().getNewEMeasureHTML(id, currentReleaseVersion);
			resp.setHeader(CONTENT_TYPE, TEXT_HTML);
		}else if (SAVE.equals(type)) {
			export = getService().getNewEMeasureXML(id);
			resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
					+ fnu.getEmeasureXMLName(export.measureName + "_" + currentReleaseVersion));
		}
		return export;
	}
	
	public ExportResult exportSimpleXML(HttpServletResponse resp,
			MeasureLibraryService measureLibraryService, String id,
			String type, Measure measure,
			FileNameUtility fnu) throws Exception {
		ExportResult export;
		export = getService().getSimpleXML(id);
		if (SAVE.equals(type)) {
			String currentReleaseVersion = measure.getReleaseVersion();
			if(currentReleaseVersion.contains(".")){
				currentReleaseVersion = currentReleaseVersion.replace(".", "_");
			}
			System.out.println("Release version zip " + currentReleaseVersion);
			resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
					+ fnu.getSimpleXMLName(export.measureName + "_" + currentReleaseVersion));
		} else {
			resp.setHeader(CONTENT_TYPE, TEXT_XML);
		}
		resp.getOutputStream().write(export.export.getBytes());
		return export;
	}
	
	
	/**
	 * Generate csv of active user emails.
	 * 
	 * @return the string
	 * @throws InCorrectUserRoleException
	 *             the in correct user role exception
	 */
	private String generateCSVOfActiveUserEmails() throws InCorrectUserRoleException {
		logger.info("Generating CSV of email addrs for all Active Users...");
		//Get all the active users
		List<User> allNonAdminActiveUsersList = getUserService().getAllNonAdminActiveUsers();
		
		//Iterate through the 'allNonAdminActiveUsersList' and generate a csv
		return createCSVOfAllNonAdminActiveUsers(allNonAdminActiveUsersList);
	}
	
	private String generateCSVOfAllUser() throws InCorrectUserRoleException {
		logger.info("Generating CSV For All Users...");
		//Get all the active users
		List<User> allUsersList = getUserService().getAllUsers();
		
		//Iterate through the 'allNonAdminActiveUsersList' and generate a csv
		return createCSVOfUsers(allUsersList);
	}
	
	/**
	 * Generate csv of active user emails.
	 * 
	 * @return the string
	 * @throws InCorrectUserRoleException
	 *             the in correct user role exception
	 * @throws XPathExpressionException
	 */
	private String generateCSVOfMeasureOwnershipForActiveUser() throws InCorrectUserRoleException, XPathExpressionException {
		logger.info("Generating CSV of Measure Ownership for all Active Non Admin Users...");
		List<MeasureOwnerReportDTO> ownerReList = 	getMeasureLibraryService().getMeasuresForMeasureOwner();
		//Iterate through the 'allNonAdminActiveUsersList' and generate a csv
		return createCSVOfActiveUserMeasures(ownerReList);
	}
	
	/**
	 * Generate csv of cql library ownership for all active users
	 * @return the csv string
	 */
	private String generateCSVOfCQLLibraryOwnershipForActiveUser() {
		logger.info("Generating CSV of CQL Library Ownership for all Active Non Admin Users...");
		List<CQLLibraryOwnerReportDTO> ownerList = getCQLLibraryService().getCQLLibrariesForOwner();
		return createCSVOfActiveUserCQLLibrary(ownerList);
		
	}
	
	/**
	 * Generate csv of active OIDs.
	 * @return the string
	 */
	private String generateCSVOfActiveUserOIDs() {
		logger.info("Generating CSV of Active User OID's...");
		//Get all the active users
		List<User> allNonTerminatedUsersList = getUserService().searchForNonTerminatedUsers();
		Map<String, String> activeOidsMap = new TreeMap<String, String>();
		for (User user : allNonTerminatedUsersList) {
			activeOidsMap.put(user.getOrgOID(), user.getOrganizationName());
		}
		
		//Iterate through the 'allNonTerminatedUsersList' and generate a csv
		return createCSVOfAllActiveUsersOID(activeOidsMap);
	}
	
	/**
	 * Creates the csv of Active User's OIDs.
	 * 
	 * @param activeOidsMap
	 *            Map of Distinct OID's
	 * @return the string
	 */
	private String createCSVOfAllActiveUsersOID(
			final Map<String, String> activeOidsMap) {
		
		StringBuilder csvStringBuilder = new StringBuilder();
		//Add the header row
		csvStringBuilder.append("Organization,Organization Id");
		csvStringBuilder.append("\r\n");
		//Add data rows
		for (Map.Entry<String, String> entry : activeOidsMap.entrySet()) {
			csvStringBuilder.append("\"" + entry.getValue() + "\",\"" + entry.getKey() +  "\"");
			csvStringBuilder.append("\r\n");
		}
		return csvStringBuilder.toString();
	}
	
	/**
	 * Creates the csv of all non admin active users.
	 * 
	 * @param allNonAdminActiveUsersList
	 *            the all non admin active users list
	 * @return the string
	 */
	private String createCSVOfAllNonAdminActiveUsers(
			final List<User> allNonAdminActiveUsersList) {
		
		StringBuilder csvStringBuilder = new StringBuilder();
		//Add the header row
		csvStringBuilder.append("User ID,Last Name,First Name,Email Address,Organization,User Role,Organization Id");
		csvStringBuilder.append("\r\n");
		
		
		//Add data rows
		for (User user:allNonAdminActiveUsersList) {
			csvStringBuilder.append("\"" + user.getLoginId() 
					+ "\",\"" + user.getLastName() + "\",\"" + user.getFirstName()
					+ "\",\"" + user.getEmailAddress() + "\",\"" + user.getOrganizationName()
					+ "\",\"" + user.getSecurityRole().getDescription()
					+ "\",\"" + user.getOrgOID() + "\"");
			csvStringBuilder.append("\r\n");
		}
		return csvStringBuilder.toString();
	}
	
	private String createCSVOfUsers(
			final List<User> allNonAdminActiveUsersList) {
		
		StringBuilder csvStringBuilder = new StringBuilder();
		//Add the header row
		csvStringBuilder.append("User ID,Last Name,First Name,Organization,Organization Id,Email Address,User Status,Role,Date Of Termination");
		csvStringBuilder.append("\r\n");
		
		
		//Add data rows
		for (User user:allNonAdminActiveUsersList) {
			csvStringBuilder.append("\"" + user.getLoginId() 
					+ "\",\"" + user.getLastName() + "\",\"" + user.getFirstName()
					+ "\",\"" + user.getOrganizationName()
					+ "\",\"" + user.getOrgOID()
					+ "\",\"" + user.getEmailAddress()
					+ "\",\"" + user.getStatus().getDescription()
					+ "\",\"" + user.getSecurityRole().getDescription()
					+ "\",\"" + user.getTerminationDate() + "\"");
			csvStringBuilder.append("\r\n");
		}
		return csvStringBuilder.toString();
	}
	
	/**
	 * Generates Measure and Measure Owner report for Active Non Admin Users.
	 * @param ownerReList - List.
	 * @return CSV String
	 */
	private String createCSVOfActiveUserMeasures(
			final List<MeasureOwnerReportDTO> ownerReList) {
		
		StringBuilder csvStringBuilder = new StringBuilder();
		//Add the header row
		csvStringBuilder.append("Last Name,First Name,Organization,Measure Name,Emeasure Id , GUID ,NQF Number");
		csvStringBuilder.append("\r\n");
		for (MeasureOwnerReportDTO measureOwnerReportDTO : ownerReList) {
			csvStringBuilder.append("\"" + measureOwnerReportDTO.getLastName() + "\",\"" + measureOwnerReportDTO.getFirstName()
					+ "\",\"" + measureOwnerReportDTO.getOrganization()
					+ "\",\"" + measureOwnerReportDTO.getName() + "\",\"");
			if (measureOwnerReportDTO.getCmsNumber() != 0) {
				csvStringBuilder.append(measureOwnerReportDTO.getCmsNumber() + "\",\"");
			} else {
				csvStringBuilder.append("" + "\",\"");
			}
			if (measureOwnerReportDTO.getId() != null) {
				csvStringBuilder.append(measureOwnerReportDTO.getId() + "\"");
			} else {
				csvStringBuilder.append("" + "\"");
			}
			if (measureOwnerReportDTO.getNqfId() != null) {
				csvStringBuilder.append(",\"\t" + measureOwnerReportDTO.getNqfId() + "\"");
			} else {
				csvStringBuilder.append(",\"" + "" + "\"");
			}
			csvStringBuilder.append("\r\n");
		}
		return csvStringBuilder.toString();
	}
	
	/**
	 * Creates the csv string for the cql library ownership report
	 * @param ownerList the list of cql library owner reports
	 * @return the csv string
	 */
	private String createCSVOfActiveUserCQLLibrary(final List<CQLLibraryOwnerReportDTO> ownerList) {
		StringBuilder csvStringBuilder = new StringBuilder();
		
		// add the header
		csvStringBuilder.append("CQL Library Name,Type,Status,Version #,ID #,Set ID #,First Name,Last Name,Organization");
		csvStringBuilder.append("\r\n");
		
		// add data
		for(CQLLibraryOwnerReportDTO cqlLibraryOwnerReport : ownerList) {
			csvStringBuilder.append("\"" + cqlLibraryOwnerReport.getName() + "\",\"");
			csvStringBuilder.append(cqlLibraryOwnerReport.getType()+ "\",\"");
			csvStringBuilder.append(cqlLibraryOwnerReport.getStatus()+ "\",\"");
			csvStringBuilder.append(cqlLibraryOwnerReport.getVersionNumber()+ "\",\"");
			csvStringBuilder.append(cqlLibraryOwnerReport.getId() + "\",\"");
			csvStringBuilder.append(cqlLibraryOwnerReport.getSetId()+ "\",\"");
			csvStringBuilder.append(cqlLibraryOwnerReport.getFirstName()+ "\",\"");
			csvStringBuilder.append(cqlLibraryOwnerReport.getLastName()+ "\",\"");
			csvStringBuilder.append(cqlLibraryOwnerReport.getOrganization() + "" + "\"");
			csvStringBuilder.append("\r\n");
		}
		
		
		return csvStringBuilder.toString();
	}
	
	/**
	 * Gets the service.
	 * 
	 * @return the service
	 */
	private SimpleEMeasureService getService() {
		SimpleEMeasureService service = (SimpleEMeasureService) context.getBean("eMeasureService");
		return service;
	}
	
	/**
	 * Gets the user service.
	 * 
	 * @return the user service
	 */
	private UserService getUserService() {
		return (UserService) context.getBean("userService");
	}
	
	/**
	 * Gets the measure package service.
	 *
	 * @return the measure package service
	 */
	private MeasurePackageService getMeasurePackageService() {
		return (MeasurePackageService) context.getBean("measurePackageService");
	}
	
	/**
	 * Gets the measure library service.
	 *
	 * @return the measure library service
	 */
	private MeasureLibraryService getMeasureLibraryService(){
		return (MeasureLibraryService) context.getBean("measureLibraryService");
	}	
	
	/**
	 * Gets the cql library service
	 * @return the cql library service
	 */
	private CQLLibraryService getCQLLibraryService() {
		return (CQLLibraryService) context.getBean("cqlLibraryService");
	}
}
