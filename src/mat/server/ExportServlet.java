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
	
	/** Human readable for Subtree Node **/
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
		String[] matVersion ={"_v3","_v4"}; 
		Measure measure = null;
		ExportResult export = null;
		Date exportDate = null;
		if (id!= null) {
			measure = service.getById(id);
			exportDate = measure.getExportedDate();
			}
		Date releaseDate = measureLibraryService.getFormattedReleaseDate(measureLibraryService.getReleaseDate());
		FileNameUtility fnu = new FileNameUtility();
		try {
			if (SIMPLEXML.equals(format)) {
				export = getService().getSimpleXML(id);
				if (SAVE.equals(type)) {
					if (measure.getExportedDate().before(measureLibraryService.getFormattedReleaseDate(measureLibraryService.getReleaseDate()))) {
					resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
							+ fnu.getSimpleXMLName(export.measureName + matVersion[0])); 
					}
					else if(measure.getExportedDate().equals(measureLibraryService
							.getFormattedReleaseDate(measureLibraryService.getReleaseDate()))
							 || measure.getExportedDate().after(measureLibraryService
									 .getFormattedReleaseDate(measureLibraryService.getReleaseDate()))){
						resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
								+ fnu.getSimpleXMLName(export.measureName + matVersion[1])); 
					}
				} else {
					resp.setHeader(CONTENT_TYPE, TEXT_XML);
				}
			} else if (EMEASURE.equals(format)) {
				if (measure.getExportedDate().before(measureLibraryService.getFormattedReleaseDate(measureLibraryService.getReleaseDate()))){
					if ("open".equals(type)) {
						export = getService().getEMeasureHTML(id);
						resp.setHeader(CONTENT_TYPE, TEXT_HTML);
					} else if (SAVE.equals(type)) {
						export = getService().getEMeasureXML(id);
//						if (measure.getExportedDate().before(measureLibraryService.getFormattedReleaseDate(measureLibraryService.getReleaseDate()))){
						resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
								+ fnu.getEmeasureXMLName(export.measureName + matVersion[0]));
//						}
					}
				}else{
					export = getService().getNewEMeasureHTML(id);
					resp.setHeader(CONTENT_TYPE, TEXT_HTML);
					if (SAVE.equals(type)) {
						resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
								+ fnu.getEmeasureHTMLName(export.measureName + matVersion[1]));
					}
				}				
			} else if (CODELIST.equals(format)) {
				export = getService().getEMeasureXLS(id);
				if(measure.getExportedDate().before(measureLibraryService.getFormattedReleaseDate(measureLibraryService.getReleaseDate()))){
				resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
						+ fnu.getEmeasureXLSName(export.measureName + matVersion[0], export.packageDate));
				} else if(measure.getExportedDate().equals(measureLibraryService
						.getFormattedReleaseDate(measureLibraryService.getReleaseDate()))
						 || measure.getExportedDate().after(measureLibraryService
								 .getFormattedReleaseDate(measureLibraryService.getReleaseDate()))){
					resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
							+ fnu.getEmeasureXLSName(export.measureName + matVersion[1], export.packageDate));
				}
				resp.setContentType("application/vnd.ms-excel");
				resp.getOutputStream().write(export.wkbkbarr);
				resp.getOutputStream().close();
				export.wkbkbarr = null;
			} else if (ZIP.equals(format)) {
				export = getService().getEMeasureZIP(id,exportDate,releaseDate);
				if(measure.getExportedDate().before(measureLibraryService.getFormattedReleaseDate(measureLibraryService.getReleaseDate()))){
				resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + fnu.getZipName(export.measureName + matVersion[0]));
				} else if(measure.getExportedDate().equals(measureLibraryService
						.getFormattedReleaseDate(measureLibraryService.getReleaseDate()))
						 || measure.getExportedDate().after(measureLibraryService
								 .getFormattedReleaseDate(measureLibraryService.getReleaseDate()))){
					resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + fnu.getZipName(export.measureName + matVersion[1]));
				}
				resp.setContentType("application/zip");
				resp.getOutputStream().write(export.zipbarr);
				resp.getOutputStream().close();
				export.zipbarr = null;
			} else if (SUBTREE_HTML.equals(format)){
				String nodeXML = req.getParameter(XML_PARAM);
				System.out.println("Export servlet received node xml:"+nodeXML +" and Measure ID:"+id);
				export = getService().getHumanReadableForNode(id,nodeXML);
				resp.setHeader(CONTENT_TYPE, TEXT_HTML);
			} else if (VALUESET.equals(format)) {
				export = getService().getValueSetXLS(id);
				 if (measure.getExportedDate().before(measureLibraryService.getFormattedReleaseDate(measureLibraryService.getReleaseDate()))){
				resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
						+ fnu.getValueSetXLSName(export.valueSetName + matVersion[0], export.lastModifiedDate));
				 } else if(measure.getExportedDate().equals(measureLibraryService
							.getFormattedReleaseDate(measureLibraryService.getReleaseDate()))
							 || measure.getExportedDate().after(measureLibraryService
									 .getFormattedReleaseDate(measureLibraryService.getReleaseDate()))){
					 resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
								+ fnu.getValueSetXLSName(export.valueSetName + matVersion[1], export.lastModifiedDate));
				 }
				resp.setContentType("application/vnd.ms-excel");
				resp.getOutputStream().write(export.wkbkbarr);
				resp.getOutputStream().close();
				export.wkbkbarr = null;
			} else if (EXPORT_ACTIVE_NON_ADMIN_USERS_CSV.equals(format)) {
				String userRole = LoggedInUserUtil.getLoggedInUserRole();
				if ("Administrator".equalsIgnoreCase(userRole)) {
					String csvFileString = generateCSVOfActiveUserEmails();
					Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String activeUserCSVDate = formatter.format(new Date());
					resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
							+ fnu.getHTMLFileName("activeUsers", activeUserCSVDate) + ";");
					resp.setContentType("text/csv");
					resp.getOutputStream().write(csvFileString.getBytes());
					resp.getOutputStream().close();
				}
			} else if (EXPORT_ACTIVE_OID_CSV.equals(format)) {
				String userRole = LoggedInUserUtil.getLoggedInUserRole();
				if ("Administrator".equalsIgnoreCase(userRole)) {
					String csvFileString = generateCSVOfActiveUserOIDs();
					Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String activeUserCSVDate = formatter.format(new Date());
					resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
							+ fnu.getHTMLFileName("activeOrganizationOids", activeUserCSVDate) + ";");
					resp.setContentType("text/csv");
					resp.getOutputStream().write(csvFileString.getBytes());
					resp.getOutputStream().close();
				}
			} else if (EXPORT_MEASURE_NOTES_FOR_MEASURE.equals(format)) {
				System.out.println("testing the print out!");
				//String csvFileString = generateCSVToExportMeasureNotes(id);
				export = getService().getSimpleXML(id);
				String csvFileString = generateHTMLToExportMeasureNotes(id,export);
				Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String measureNoteDate = formatter.format(new Date());
				resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME
						+ fnu.getHTMLFileName("MeasureNotes", measureNoteDate) + ";");
				resp.setContentType("html");
				resp.getOutputStream().write(csvFileString.getBytes());
				resp.getOutputStream().close();
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
		if (!CODELIST.equals(format) && !EXPORT_ACTIVE_NON_ADMIN_USERS_CSV.equals(format)
				&& !EXPORT_MEASURE_NOTES_FOR_MEASURE.equals(format)) {
			resp.getOutputStream().println(export.export);
		}
	}

	private String generateHTMLToExportMeasureNotes(final String measureId, ExportResult export) throws XPathExpressionException{
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
		XmlProcessor measureXMLProcessor = new XmlProcessor(export.export);
		Node name = measureXMLProcessor.findNode(measureXMLProcessor.getOriginalDoc(), "//measureDetails/title");
		Node eMeasureId = measureXMLProcessor.findNode(measureXMLProcessor.getOriginalDoc(), "//measureDetails/emeasureid");
		if(eMeasureId != null){
			header.appendText(name.getTextContent() + " (CMS " + eMeasureId.getTextContent() + ") " + "Measure Notes");
		}else{
			header.appendText(name.getTextContent() + " Measure Notes");
		}
		Element table = htmlDocument.body().appendElement("table");
		System.out.println("NOTES LENGTH: " + allMeasureNotes.size());
		Element row = table.appendElement("tr");
		CreateHeader(row);
		for(MeasureNotes measureNotes:allMeasureNotes){
			row = table.appendElement("tr");
			createBody(row, measureNotes.getNoteTitle(),false);
			createBody(row,measureNotes.getNoteDesc(), true);
			createBody(row,convertDateToString(measureNotes.getLastModifiedDate()),false);
			createBody(row,measureNotes.getCreateUser().getEmailAddress(),false);
			if (measureNotes.getModifyUser() != null) {
				createBody(row,measureNotes.getModifyUser().getEmailAddress(),false);
			}else{
				createBody(row,"",false);
			}
		}
		
		return htmlDocument.toString();
	}
	private void createBody(Element row, String message,Boolean html){
		Element col = row.appendElement("td");
		if(!html){
			col.appendText(message);
		}else{
			col.append(message);
		}
	}
	private void CreateHeader(Element row){
		createHeaderRows(row,"Title");
		createHeaderRows(row,"Description");
		createHeaderRows(row,"LastModified Date");
		createHeaderRows(row,"Created By");
		createHeaderRows(row,"Modified By");
	}
	private void createHeaderRows(Element row, String header){
		Element col = row.appendElement("td");
		col.attr("bgcolor", "#656565");
		col.attr("style", "background-color:#656565");
		Element span = col.appendElement("span");
		span.attr("class", "td_label");
		span.attr("align", "center");
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
		csvStringBuilder.append("Last Name,First Name,Email Address,Organization,Organization Id");
		csvStringBuilder.append("\r\n");
		//Add data rows
		for (User user:allNonAdminActiveUsersList) {
			csvStringBuilder.append("\"" + user.getLastName() + "\",\"" + user.getFirstName()
					+ "\",\"" + user.getEmailAddress() + "\",\"" + user.getOrganizationName()
					+ "\",\"" + user.getOrgOID() + "\"");
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
	
	private MeasurePackageService getMeasurePackageService() {
		return (MeasurePackageService) context.getBean("measurePackageService");
	}
	
	private MeasureLibraryService getMeasureLibraryService(){
		return (MeasureLibraryService) context.getBean("measureLibraryService");
	}
}
