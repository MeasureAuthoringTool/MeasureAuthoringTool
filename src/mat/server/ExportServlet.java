package mat.server;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ExportServlet extends HttpServlet {
	private static final Log logger = LogFactory.getLog(ExportServlet.class);
	private static final long serialVersionUID = 4539514145289378238L;
	protected ApplicationContext context;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());

		String id = req.getParameter("id");
		String format = req.getParameter("format");
		String type = req.getParameter("type");
		
		ExportResult export = null;
		
		FileNameUtility fnu = new FileNameUtility();
		try {
			if("simplexml".equals(format)) {
				export = getService().getSimpleXML(id);
				if("save".equals(type)) {
					resp.setHeader("Content-Disposition", "attachment; filename="+ fnu.getSimpleXMLName(export.measureName));
				}
				else {
					resp.setHeader("Content-Type", "text/xml");
				}
			}
			else if("emeasure".equals(format)) {
				if("open".equals(type)) {
					export=getService().getEMeasureHTML(id);
					resp.setHeader("Content-Type", "text/html");
				}
				else if("save".equals(type)) {
					export=getService().getEMeasureXML(id);
					resp.setHeader("Content-Disposition", "attachment; filename="+ fnu.getEmeasureXMLName(export.measureName));
				}
			}
			else if("codelist".equals(format)) {
				export=getService().getEMeasureXLS(id);
				resp.setHeader("Content-Disposition", "attachment; filename="+ fnu.getEmeasureXLSName(export.measureName, export.packageDate));
				resp.setContentType("application/vnd.ms-excel");
				resp.getOutputStream().write(export.wkbkbarr);
				resp.getOutputStream().close();
				export.wkbkbarr = null;
			}
			else if("zip".equals(format)) {
				export=getService().getEMeasureZIP(id);
				resp.setHeader("Content-Disposition", "attachment; filename="+ fnu.getZipName(export.measureName));
				resp.setContentType("application/zip");
				resp.getOutputStream().write(export.zipbarr);
				resp.getOutputStream().close();
				export.zipbarr = null;
			}
			else if("valueset".equals(format)) {
				export=getService().getValueSetXLS(id);
				resp.setHeader("Content-Disposition", "attachment; filename="+ fnu.getValueSetXLSName(export.valueSetName, export.lastModifiedDate));
				resp.setContentType("application/vnd.ms-excel");
				resp.getOutputStream().write(export.wkbkbarr);
				resp.getOutputStream().close();
				export.wkbkbarr = null;
			}else if("exportActiveNonAdminUsersCSV".equals(format)){
				String userRole = LoggedInUserUtil.getLoggedInUserRole();
				if("Administrator".equalsIgnoreCase(userRole)){
					String csvFileString = generateCSVOfActiveUserEmails();
					Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String activeUserCSVDate = formatter.format(new Date());
					resp.setHeader("Content-Disposition", "attachment; filename="+fnu.getCSVFileName("activeUsers", activeUserCSVDate) +";");
					resp.setContentType("text/csv");
					resp.getOutputStream().write(csvFileString.getBytes());
					resp.getOutputStream().close();
				}
			}else if("exportMeasureNotesForMeasure".equals(format)){
				String csvFileString = generateCSVToExportMeasureNotes(id);
				Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String measureNoteDate = formatter.format(new Date());
				resp.setHeader("Content-Disposition", "attachment; filename="+fnu.getCSVFileName("MeasureNotes", measureNoteDate) +";");
				resp.setContentType("text/csv");
				resp.getOutputStream().write(csvFileString.getBytes());
				resp.getOutputStream().close();
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
		if(! "codelist".equals(format) && ! "exportActiveNonAdminUsersCSV".equals(format) && ! "exportMeasureNotesForMeasure".equals(format)){
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
			//This conversion from Date to String is done in order to show date in excel.
			Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String measureNoteDate = formatter.format(measureNotes.getLastModifiedDate());
			if(measureNotes.getModifyUser()!=null)
				csvStringBuilder.append("\""+measureNotes.getNoteTitle()+"\",\""+measureNotes.getNoteDesc()+
						"\",\""+measureNoteDate+"\",\""+measureNotes.getCreateUser().getEmailAddress()+"\",\""+measureNotes.getModifyUser().getEmailAddress()+"\"");
			else
				csvStringBuilder.append("\""+measureNotes.getNoteTitle()+"\",\""+measureNotes.getNoteDesc()+
						"\",\""+measureNoteDate+"\",\""+measureNotes.getCreateUser().getEmailAddress()+"\",\""+""+"\"");
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
