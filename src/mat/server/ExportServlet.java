package mat.server;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mat.client.admin.service.AdminService;
import mat.model.User;
import mat.server.service.SimpleEMeasureService;
import mat.server.service.UserService;
import mat.server.service.SimpleEMeasureService.ExportResult;
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
					resp.setHeader("Content-Disposition", "attachment; filename=activeUsers.csv");
					resp.setContentType("text/csv");
					resp.getOutputStream().write(csvFileString.getBytes());
					resp.getOutputStream().close();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new ServletException(e);
		}
		if(! "codelist".equals(format) && ! "exportActiveNonAdminUsersCSV".equals(format)){
			resp.getOutputStream().println(export.export);
		}
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
}
