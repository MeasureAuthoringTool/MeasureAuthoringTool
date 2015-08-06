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

import mat.model.MeasureNotes;
import mat.model.MeasureOwnerReportDTO;
import mat.model.User;
import mat.model.clause.Measure;
import mat.server.service.MeasureLibraryService;
import mat.server.service.MeasureNotesService;
import mat.server.service.MeasurePackageService;
import mat.server.service.SimpleEMeasureService;
import mat.server.service.SimpleEMeasureService.ExportResult;
import mat.server.service.UserService;
import mat.server.simplexml.MATCssUtil;
import mat.server.util.XmlProcessor;
import mat.shared.FileNameUtility;
import mat.shared.InCorrectUserRoleException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Element;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.w3c.dom.Node;

// TODO: Auto-generated Javadoc
/**
 * The Class ExportServlet.
 */
public class ExportServlet extends HttpServlet {
	
	/** The Constant EXPORT_MEASURE_NOTES_FOR_MEASURE. */
	private static final String EXPORT_MEASURE_NOTES_FOR_MEASURE = "exportMeasureNotesForMeasure";
	
	/** The Constant EXPORT_ACTIVE_NON_ADMIN_USERS_CSV. */
	private static final String EXPORT_ACTIVE_NON_ADMIN_USERS_CSV = "exportActiveNonAdminUsersCSV";
	
	/** The Constant EXPORT_ACTIVE_OID_CSV. */
	private static final String EXPORT_ACTIVE_OID_CSV = "exportActiveOIDCSV";
	
	/** The Constant VALUESET. */
	private static final String VALUESET = "valueset";
	
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
	
	/** The Constant TEXT_HTML. */
	private static final String TEXT_HTML = "text/html";
	
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
		//String[] matVersion ={"_v3","_v4"};
		Measure measure = null;
		ExportResult export = null;
		Date exportDate = null;
		
		System.out.println("FOMAT: " + format);
		
		if (id!= null) {
			measure = service.getById(id);
			exportDate = measure.getExportedDate();
		}
		//Date releaseDate = measureLibraryService.getFormattedReleaseDate(measureLibraryService.getReleaseDate());
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
			} else if (ZIP.equals(format)) {
				export = exportEmeasureZip(resp, measureLibraryService, id,
						 measure, exportDate, fnu);
			} else if (SUBTREE_HTML.equals(format)){
				export = exportSubTreeHumanReadable(req, resp, id);
			} else if (VALUESET.equals(format)) {
				export = exportValueSetListXLS(resp, measureLibraryService, id,
						 measure, fnu);
			} else if (EXPORT_ACTIVE_NON_ADMIN_USERS_CSV.equals(format)) {
				exportActiveUserListCSV(resp, fnu);
			} else if (EXPORT_ACTIVE_OID_CSV.equals(format)) {
				exportActiveOrganizationListCSV(resp, fnu);
			} else if (EXPORT_MEASURE_NOTES_FOR_MEASURE.equals(format)) {
				export = exportMeasureNotesCSV(resp, id, measure, fnu);
			} else if("exportMeasureOwner".equalsIgnoreCase(format)){
				exportActiveUserMeasureOwnershipListCSV(resp,fnu);
				
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
		if (!CODELIST.equals(format) && !EXPORT_ACTIVE_NON_ADMIN_USERS_CSV.equals(format) && !EXPORT_ACTIVE_OID_CSV.equals(format)
				&& !EXPORT_MEASURE_NOTES_FOR_MEASURE.equals(format) && !"exportMeasureOwner".equalsIgnoreCase(format)) {
			resp.getOutputStream().println(export.export);
		}
	}
	
	public ExportResult exportMeasureNotesCSV(HttpServletResponse resp,
			String id, Measure measure, FileNameUtility fnu) throws Exception,
			XPathExpressionException, IOException {
		ExportResult export;
		System.out.println("testing the print out!");
		//String csvFileString = generateCSVToExportMeasureNotes(id);
		export = getService().getSimpleXML(id);
		String csvFileString = generateHTMLToExportMeasureNotes(id,export,measure.getDescription());
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String measureNoteDate = formatter.format(new Date());
		resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
				+ fnu.getHTMLFileName("MeasureNotes", measureNoteDate) + ";");
		resp.setContentType("html");
		resp.getOutputStream().write(csvFileString.getBytes());
		resp.getOutputStream().close();
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
			resp.getOutputStream().close();
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
			resp.getOutputStream().close();
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
			resp.getOutputStream().close();
		}
	}
	
	
	public ExportResult exportValueSetListXLS(HttpServletResponse resp,
			MeasureLibraryService measureLibraryService, String id,
			Measure measure, FileNameUtility fnu)
					throws Exception, IOException {
		ExportResult export;
		export = getService().getValueSetXLS(id);
		/*if (measure.getExportedDate().before(measureLibraryService.getFormattedReleaseDate(measureLibraryService.getReleaseDate()))){
			resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
					+ fnu.getValueSetXLSName(export.valueSetName + matVersion[0], export.lastModifiedDate));
		} else if(measure.getExportedDate().equals(measureLibraryService
				.getFormattedReleaseDate(measureLibraryService.getReleaseDate()))
				|| measure.getExportedDate().after(measureLibraryService
						.getFormattedReleaseDate(measureLibraryService.getReleaseDate()))){
			resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
					+ fnu.getValueSetXLSName(export.valueSetName + matVersion[1], export.lastModifiedDate));
		}*/
		
		String currentReleaseVersion = measure.getReleaseVersion();
		if(currentReleaseVersion.contains(".")){
			currentReleaseVersion = currentReleaseVersion.replace(".", "_");
		}
		System.out.println("Release version zip " + currentReleaseVersion);
		
		resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
				+ fnu.getValueSetXLSName(export.valueSetName + "_" + currentReleaseVersion, export.lastModifiedDate));
		resp.setContentType("application/vnd.ms-excel");
		resp.getOutputStream().write(export.wkbkbarr);
		resp.getOutputStream().close();
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
		return export;
	}
	
	public ExportResult exportEmeasureZip(HttpServletResponse resp,
			MeasureLibraryService measureLibraryService, String id,
			Measure measure, Date exportDate,
			FileNameUtility fnu) throws Exception,
			IOException {
		ExportResult export;
		export = getService().getEMeasureZIP(id,exportDate);
		/*if(measure.getExportedDate().before(measureLibraryService.getFormattedReleaseDate(measureLibraryService.getReleaseDate()))){
			resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + fnu.getZipName(export.measureName + matVersion[0]));
		} else if(measure.getExportedDate().equals(measureLibraryService
				.getFormattedReleaseDate(measureLibraryService.getReleaseDate()))
				|| measure.getExportedDate().after(measureLibraryService
						.getFormattedReleaseDate(measureLibraryService.getReleaseDate()))){
			resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + fnu.getZipName(export.measureName + matVersion[1]));
		}*/
		
		String currentReleaseVersion = measure.getReleaseVersion();
		if(currentReleaseVersion.contains(".")){
			currentReleaseVersion = currentReleaseVersion.replace(".", "_");
		}
		System.out.println("Release version zip " + currentReleaseVersion);
		resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + fnu.getZipName(export.measureName + "_" + currentReleaseVersion));
		resp.setContentType("application/zip");
		resp.getOutputStream().write(export.zipbarr);
		resp.getOutputStream().close();
		export.zipbarr = null;
		return export;
	}
	
	public ExportResult exportCodeListXLS(HttpServletResponse resp,
			MeasureLibraryService measureLibraryService, String id,
			Measure measure, FileNameUtility fnu)
					throws Exception, IOException {
		ExportResult export;
		export = getService().getEMeasureXLS(id);
		/*if(measure.getExportedDate().before(measureLibraryService.getFormattedReleaseDate(measureLibraryService.getReleaseDate()))){
			resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
					+ fnu.getEmeasureXLSName(export.measureName + matVersion[0], export.packageDate));
		} else if(measure.getExportedDate().equals(measureLibraryService
				.getFormattedReleaseDate(measureLibraryService.getReleaseDate()))
				|| measure.getExportedDate().after(measureLibraryService
						.getFormattedReleaseDate(measureLibraryService.getReleaseDate()))){
			resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
					+ fnu.getEmeasureXLSName(export.measureName + matVersion[1], export.packageDate));
		}*/
		
		String currentReleaseVersion = measure.getReleaseVersion();
		if(currentReleaseVersion.contains(".")){
			currentReleaseVersion = currentReleaseVersion.replace(".", "_");
		}
		System.out.println("Release version zip " + currentReleaseVersion);
		resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
				+ fnu.getEmeasureXLSName(export.measureName + "_" + currentReleaseVersion, export.packageDate));
		resp.setContentType("application/vnd.ms-excel");
		resp.getOutputStream().write(export.wkbkbarr);
		resp.getOutputStream().close();
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
			if ("open".equals(type)) {
				export = getService().getNewEMeasureHTML(id);
				resp.setHeader(CONTENT_TYPE, TEXT_HTML);
			}else if (SAVE.equals(type)) {
				export = getService().getNewEMeasureXML(id);
				resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
						+ fnu.getEmeasureXMLName(export.measureName + "_" + currentReleaseVersion));
			}
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
			/*if (measure.getExportedDate().before(measureLibraryService.getFormattedReleaseDate(measureLibraryService.getReleaseDate()))) {
				resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
						+ fnu.getSimpleXMLName(export.measureName + matVersion[0]));
			}
			else if(measure.getExportedDate().equals(measureLibraryService
					.getFormattedReleaseDate(measureLibraryService.getReleaseDate()))
					|| measure.getExportedDate().after(measureLibraryService
							.getFormattedReleaseDate(measureLibraryService.getReleaseDate()))){
				resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
						+ fnu.getSimpleXMLName(export.measureName + matVersion[1]));
			}*/
			
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
		return export;
	}
	
	/**
	 * Generate html to export measure notes.
	 *
	 * @param measureId the measure id
	 * @param export the export
	 * @param name the name
	 * @return the string
	 * @throws XPathExpressionException the x path expression exception
	 */
	private String generateHTMLToExportMeasureNotes(final String measureId, ExportResult export,String name) throws XPathExpressionException{
		List<MeasureNotes> allMeasureNotes = getMeasureNoteService().getAllMeasureNotesByMeasureID(measureId);
		org.jsoup.nodes.Document htmlDocument = new org.jsoup.nodes.Document("");
		Element html = htmlDocument.appendElement("html");
		html.appendElement("head");
		html.appendElement("body");
		Element head = htmlDocument.head();
		htmlDocument.title("Measure Notes");
		
		String styleTagString = MATCssUtil.getCSS();
		head.append(styleTagString);
		Element header = htmlDocument.body().appendElement("h1");
		Node eMeasureId = null;
		if(export != null){
			XmlProcessor measureXMLProcessor = new XmlProcessor(export.export);
			eMeasureId = measureXMLProcessor.findNode(measureXMLProcessor.getOriginalDoc(), "//measureDetails/emeasureid");
		}
		if(eMeasureId != null){
			header.appendText(name+ " (CMS " + eMeasureId.getTextContent() + ") " + "Measure Notes");
		}else{
			header.appendText(name + " Measure Notes");
		}
		Element table = htmlDocument.body().appendElement("table");
		table.attr("class", "header_table");
		table.attr("width", "100%");
		Element row = table.appendElement("tr");
		createHeader(row);
		for(MeasureNotes measureNotes:allMeasureNotes){
			row = table.appendElement("tr");
			createBody(row, measureNotes.getNoteTitle(),false,"20%");
			createBody(row,measureNotes.getNoteDesc(), true,"33%");
			createBody(row,convertDateToString(measureNotes.getLastModifiedDate()),false,"17%");
			createBody(row,measureNotes.getCreateUser().getEmailAddress(),false,"15%");
			if (measureNotes.getModifyUser() != null) {
				createBody(row,measureNotes.getModifyUser().getEmailAddress(),false,"15%");
			}else{
				createBody(row,"",false,"15%");
			}
		}
		
		return htmlDocument.toString();
	}
	
	/**
	 * Creates the body.
	 *
	 * @param row the row
	 * @param message the message
	 * @param html the html
	 * @param width the width
	 */
	private void createBody(Element row, String message,Boolean html,String width){
		Element col = row.appendElement("td");
		col.attr("width", width);
		if(!html){
			col.appendText(message);
		}else{
			col.append(message);
		}
	}
	
	/**
	 * Creates the header.
	 *
	 * @param row the row
	 */
	private void createHeader(Element row){
		createHeaderRows(row,"Title","20%");
		createHeaderRows(row,"Description","33%");
		createHeaderRows(row,"Last Modified Date","17%");
		createHeaderRows(row,"Created By","15%");
		createHeaderRows(row,"Modified By","15%");
	}
	
	/**
	 * Creates the header rows.
	 *
	 * @param row the row
	 * @param header the header
	 * @param width the width
	 */
	private void createHeaderRows(Element row, String header,String width){
		Element col = row.appendElement("td");
		col.attr("bgcolor", "#656565");
		col.attr("style", "background-color:#656565");
		Element span = col.appendElement("span");
		span.attr("class", "td_label");
		span.attr("width", width);
		span.appendText(header);
		
	}
	/**
	 * Generate csv to export measure notes.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the string
	 */
	private String generateCSVToExportMeasureNotes(final String measureId) {
		logger.info("Generating CSV of Measure Notes...");
		List<MeasureNotes> allMeasureNotes = getMeasureNoteService().getAllMeasureNotesByMeasureID(measureId);
		
		StringBuilder csvStringBuilder = new StringBuilder();
		//Add the header row
		csvStringBuilder.append("Title,Description,LastModifiedDate,Created By,Modified By");
		csvStringBuilder.append("\r\n");
		//Add data rows
		for (MeasureNotes measureNotes:allMeasureNotes) {
			if (measureNotes.getModifyUser() != null) {
				csvStringBuilder.append("\"" + measureNotes.getNoteTitle() + "\",\""
						+ measureNotes.getNoteDesc() + "\",\""
						+ convertDateToString(measureNotes.getLastModifiedDate()) + "\",\""
						+ measureNotes.getCreateUser().getEmailAddress() + "\",\""
						+ measureNotes.getModifyUser().getEmailAddress() + "\"");
			} else {
				csvStringBuilder.append("\"" + measureNotes.getNoteTitle() + "\",\""
						+ measureNotes.getNoteDesc() + "\",\""
						+ convertDateToString(measureNotes.getLastModifiedDate()) + "\",\""
						+ measureNotes.getCreateUser().getEmailAddress() + "\",\"" + "" + "\"");
			}
			csvStringBuilder.append("\r\n");
		}
		return csvStringBuilder.toString();
	}
	
	/**
	 * Converts a date into a date time string of "MM/dd/yyyy hh:mm:ss a z" format.
	 * @param date - date to be formated into a string.
	 * @return the formated date string.
	 */
	private String convertDateToString(final Date date) {
		String dateString = StringUtils.EMPTY;
		if (date != null) {
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a z");
			dateString = format.format(date);
		}
		return dateString;
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
		csvStringBuilder.append("Last Name,First Name,Email Address,Organization,User Role,Organization Id");
		csvStringBuilder.append("\r\n");
		
		
		//Add data rows
		for (User user:allNonAdminActiveUsersList) {
			csvStringBuilder.append("\"" + user.getLastName() + "\",\"" + user.getFirstName()
					+ "\",\"" + user.getEmailAddress() + "\",\"" + user.getOrganizationName()
					+ "\",\"" + user.getSecurityRole().getDescription()
					+ "\",\"" + user.getOrgOID() + "\"");
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
					+ "\",\"" + measureOwnerReportDTO.getOrganizationName()
					+ "\",\"" + measureOwnerReportDTO.getMeasureDescription() + "\",\"");
			if (measureOwnerReportDTO.getCmsNumber() != 0) {
				csvStringBuilder.append(measureOwnerReportDTO.getCmsNumber() + "\",\"");
			} else {
				csvStringBuilder.append("" + "\",\"");
			}
			if (measureOwnerReportDTO.getGuid() != null) {
				csvStringBuilder.append(measureOwnerReportDTO.getGuid() + "\"");
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
	 * Gets the measure note service.
	 * 
	 * @return the measure note service
	 */
	private MeasureNotesService getMeasureNoteService() {
		return (MeasureNotesService) context.getBean("measureNotesService");
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
}
