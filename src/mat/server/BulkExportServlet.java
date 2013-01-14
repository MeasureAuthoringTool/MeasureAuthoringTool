package mat.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mat.server.service.SimpleEMeasureService;
import mat.server.service.SimpleEMeasureService.ExportResult;
import mat.shared.FileNameUtility;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class BulkExportServlet extends HttpServlet {
	private static final long serialVersionUID = 4539514145289378238L;
	protected ApplicationContext context;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		String[] measureIds = req.getParameterValues("id");
		
		ExportResult export = null;
		
		FileNameUtility fnu = new FileNameUtility();
		try {
				export = getService().getBulkExportZIP(measureIds);
				resp.setHeader("Content-Disposition", "attachment; filename="+ fnu.getZipName(export.measureName));
				resp.setContentType("application/zip");
				resp.getOutputStream().write(export.zipbarr);
				resp.getOutputStream().close();
				export.zipbarr = null;
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new ServletException(e);
			}
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException { 
		doPost(req, resp);
	}
	
	private SimpleEMeasureService getService(){
		SimpleEMeasureService service = (SimpleEMeasureService) context.getBean("eMeasureService");
		return service;
	}
}
