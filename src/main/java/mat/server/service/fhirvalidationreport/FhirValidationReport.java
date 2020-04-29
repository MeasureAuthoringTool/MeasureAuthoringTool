package mat.server.service.fhirvalidationreport;

import freemarker.template.TemplateException;

import java.io.IOException;

public interface FhirValidationReport {
    String generateReport(String measureId, String vsacGrantingTicket, boolean converted) throws IOException, TemplateException;
}
