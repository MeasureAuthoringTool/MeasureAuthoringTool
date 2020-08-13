package mat.server;

import mat.model.clause.Measure;
import mat.server.export.ExportResult;
import mat.server.service.MeasurePackageService;
import mat.server.service.SimpleEMeasureService;
import mat.shared.FileNameUtility;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.zip.ZipException;

/**
 * The Class BulkExportServlet.
 */
public class BulkExportServlet extends HttpServlet {
	
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
		doPost(req, resp);
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		String[] measureIds = req.getParameterValues("id");
		MeasurePackageService service = getMeasurePackageService();
		Date[] exportDates = new Date[measureIds.length];
		Measure measure;

		for (int i = 0; i < measureIds.length; i++) {
			measure = service.getById(measureIds[i]);
			exportDates[i] = measure.getExportedDate();
		}

		ExportResult export = null;

		try {
			export = getService().getBulkExportZIP(measureIds, exportDates);
			resp.setHeader("Content-Disposition", "attachment; filename=" + FileNameUtility.getBulkZipName("Export"));
			resp.setContentType("application/zip");
			resp.getOutputStream().write(export.zipbarr);
			resp.getOutputStream().close();
			export.zipbarr = null;

		} catch (Exception e) {
			if (e instanceof ZipException && null != e.getMessage() && e.getMessage().contains("Exceeded Limit")) {
				resp.setContentType("text/html");
				PrintWriter out = resp.getWriter();
				out.println("Exceeded Limit: " + humanReadableByteCount(new Long(e.getMessage().split(":")[1])));
			} else {
				throw new ServletException(e);
			}

		}
	}
	
	private SimpleEMeasureService getService(){
		SimpleEMeasureService service = context.getBean(SimpleEMeasureService.class);
		return service;
	}
	
	private MeasurePackageService getMeasurePackageService() {
		return context.getBean(MeasurePackageService.class);
	}

	private String humanReadableByteCount(long bytes) {
	    int unit = 1024;
	    if (bytes < unit) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String pre = "KMGTPE".charAt(exp-1) +  "";
	    DecimalFormat decimalFormat = new DecimalFormat("###.#" + " " + pre + "B");
	    return decimalFormat.format(bytes / Math.pow(unit, exp));
	}
}
