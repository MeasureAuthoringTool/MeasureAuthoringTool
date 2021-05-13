package mat.server;

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
import java.util.zip.ZipException;

public class BulkExportServlet extends HttpServlet {

    private static final long serialVersionUID = 4539514145289378238L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		String[] measureIds = req.getParameterValues("id");

		try {
            var export = context.getBean(SimpleEMeasureService.class).getBulkExportZIP(measureIds);
			resp.setHeader("Content-Disposition", "attachment; filename=" + FileNameUtility.getBulkExportZipName("Export"));
			resp.setContentType("application/zip");
			resp.getOutputStream().write(export.getZipbarr());
			resp.getOutputStream().close();
			export.setZipbarr(null);
		} catch (Exception e) {
			if (e instanceof ZipException && null != e.getMessage() && e.getMessage().contains("Exceeded Limit")) {
				resp.setContentType("text/html");
				PrintWriter out = resp.getWriter();
				out.println("Exceeded Limit: " + humanReadableByteCount(Long.parseLong(e.getMessage().split(":")[1])));
			} else {
				throw new ServletException(e);
			}
		}
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
