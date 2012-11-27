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

public class ExportServlet extends HttpServlet {
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
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new ServletException(e);
		}
		if(! "codelist".equals(format))
			resp.getOutputStream().println(export.export);
	}

	private SimpleEMeasureService getService(){
		SimpleEMeasureService service = (SimpleEMeasureService) context.getBean("eMeasureService");
		return service;
	}
}
