package mat.server;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mat.model.MeasureNotes;
import mat.model.User;
import mat.server.service.MeasureNotesService;
import mat.server.service.SimpleEMeasureService;
import mat.server.service.SimpleEMeasureService.ExportResult;
import mat.server.service.UserService;
import mat.shared.FileNameUtility;
import mat.shared.InCorrectUserRoleException;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.gwt.user.client.Random;

public class ExportServlet extends HttpServlet {
	
	private static final String EXPORT_MEASURE_NOTES_FOR_MEASURE = "exportMeasureNotesForMeasure";
	private static final String EXPORT_ACTIVE_NON_ADMIN_USERS_CSV = "exportActiveNonAdminUsersCSV";
	private static final String VALUESET = "valueset";
	private static final String ZIP = "zip";
	private static final String CODELIST = "codelist";
	private static final String SAVE = "save";
	private static final String ATTACHMENT_FILENAME = "attachment; filename=";
	private static final String CONTENT_DISPOSITION = "Content-Disposition";
	private static final String TEXT_XML = "text/xml";
	private static final String CONTENT_TYPE = "Content-Type";
	private static final String EMEASURE = "emeasure";
	private static final String SIMPLEXML = "simplexml";
	private static final String TYPE_PARAM = "type";
	private static final String FORMAT_PARAM = "format";
	private static final String ID_PARAM = "id";
	
	private static final Log logger = LogFactory.getLog(ExportServlet.class);
	private static final long serialVersionUID = 4539514145289378238L;
	protected ApplicationContext context;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());

		String id = req.getParameter(ID_PARAM);
		String format = req.getParameter(FORMAT_PARAM);
		String type = req.getParameter(TYPE_PARAM);
		
		ExportResult export = null;
		
		FileNameUtility fnu = new FileNameUtility();
		try {
			if(SIMPLEXML.equals(format)) {
				export = getService().getSimpleXML(id);
				if(SAVE.equals(type)) {
					resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME+ fnu.getSimpleXMLName(export.measureName));
				}
				else {
					resp.setHeader(CONTENT_TYPE, TEXT_XML);
				}
			}
			else if(EMEASURE.equals(format)) {
				if("open".equals(type)) {
					export=getService().getEMeasureHTML(id);
					resp.setHeader(CONTENT_TYPE, TEXT_XML);
				}
				else if(SAVE.equals(type)) {
					export=getService().getEMeasureXML(id);
					resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME+ fnu.getEmeasureXMLName(export.measureName));
				}
			}
			else if(CODELIST.equals(format)) {
				export=getService().getEMeasureXLS(id);
				resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME+ fnu.getEmeasureXLSName(export.measureName, export.packageDate));
				resp.setContentType("application/vnd.ms-excel");
				resp.getOutputStream().write(export.wkbkbarr);
				resp.getOutputStream().close();
				export.wkbkbarr = null;
			}
			else if(ZIP.equals(format)) {
				export=getService().getEMeasureZIP(id);
				resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME+ fnu.getZipName(export.measureName));
				resp.setContentType("application/zip");
				resp.getOutputStream().write(export.zipbarr);
				resp.getOutputStream().close();
				export.zipbarr = null;
			}
			else if(VALUESET.equals(format)) {
				export=getService().getValueSetXLS(id);
				resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME+ fnu.getValueSetXLSName(export.valueSetName, export.lastModifiedDate));
				resp.setContentType("application/vnd.ms-excel");
				resp.getOutputStream().write(export.wkbkbarr);
				resp.getOutputStream().close();
				export.wkbkbarr = null;
			}else if(EXPORT_ACTIVE_NON_ADMIN_USERS_CSV.equals(format)){
				String userRole = LoggedInUserUtil.getLoggedInUserRole();
				if("Administrator".equalsIgnoreCase(userRole)){
					String csvFileString = generateCSVOfActiveUserEmails();
					Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String activeUserCSVDate = formatter.format(new Date());
					resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME+fnu.getCSVFileName("activeUsers", activeUserCSVDate) +";");
					resp.setContentType("text/csv");
					resp.getOutputStream().write(csvFileString.getBytes());
					resp.getOutputStream().close();
				}
			}else if("exportMeasureNotesForMeasure".equals(format)){
				String csvFileString = generateCSVToExportMeasureNotes(id);
				Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String measureNoteDate = formatter.format(new Date());
				resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME+UUID.randomUUID() +";");
				resp.setContentType("text/csv");
				resp.getOutputStream().write(csvFileString.getBytes());
				resp.getOutputStream().close();
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
		if(! CODELIST.equals(format) && ! EXPORT_ACTIVE_NON_ADMIN_USERS_CSV.equals(format) && ! EXPORT_MEASURE_NOTES_FOR_MEASURE.equals(format)){
			resp.getOutputStream().println(export.export);
		}
	}
	
	private String generateCSVToExportMeasureNotes(String measureId){
		logger.info("Generating CSV of Measure Notes...");
		List<MeasureNotes> allMeasureNotes = getMeasureNoteService().getAllMeasureNotesByMeasureID(measureId) ;
		
		StringBuilder csvStringBuilder = new StringBuilder();
		//Add the header row
		csvStringBuilder.append("Title,Description,LastModifiedDate,Created By,Modified By");
		csvStringBuilder.append("\r\n");
		//Add data rows
		for(MeasureNotes measureNotes:allMeasureNotes){
			if(measureNotes.getModifyUser()!=null)
				csvStringBuilder.append("\""+measureNotes.getNoteTitle()+"\",\""+measureNotes.getNoteDesc()+
						"\",\""+measureNotes.getLastModifiedDate()+"\",\""+measureNotes.getCreateUser().getEmailAddress()+"\",\""+measureNotes.getModifyUser().getEmailAddress()+"\"");
			else
				csvStringBuilder.append("\""+measureNotes.getNoteTitle()+"\",\""+measureNotes.getNoteDesc()+
						"\",\""+measureNotes.getLastModifiedDate()+"\",\""+measureNotes.getCreateUser().getEmailAddress()+"\",\""+""+"\"");
			csvStringBuilder.append("\r\n");
		}
		return csvStringBuilder.toString();
	}
	
	private String generateCSVOfActiveUserEmails() throws InCorrectUserRoleException{
		logger.info("Generating CSV of email addrs for all Active Users...");
		//Get all the active users
		List<User> allNonAdminActiveUsersList = getUserService().getAllNonAdminActiveUsers();
		
		//Iterate through the 'allNonAdminActiveUsersList' and generate a csv
		return createCSVOfAllNonAdminActiveUsers(allNonAdminActiveUsersList);
	}

	private String createCSVOfAllNonAdminActiveUsers(
			List<User> allNonAdminActiveUsersList) {
		
		StringBuilder csvStringBuilder = new StringBuilder();
		//Add the header row
		csvStringBuilder.append("Last Name,First Name,Email Address,Organization,Organization Id");
		csvStringBuilder.append("\r\n");
		//Add data rows
		for(User user:allNonAdminActiveUsersList){
			csvStringBuilder.append("\""+user.getLastName()+"\",\""+user.getFirstName()+
					"\",\""+user.getEmailAddress()+"\",\""+user.getOrganizationName()+"\",\""+user.getOrgOID()+"\"");
			csvStringBuilder.append("\r\n");
		}
		return csvStringBuilder.toString();
	}
	
	private SimpleEMeasureService getService(){
		SimpleEMeasureService service = (SimpleEMeasureService) context.getBean("eMeasureService");
		return service;
	}
	private UserService getUserService() {
		return (UserService)context.getBean("userService");
	}
	
	private MeasureNotesService getMeasureNoteService(){
		return (MeasureNotesService)context.getBean("measureNotesService");
	}
}
