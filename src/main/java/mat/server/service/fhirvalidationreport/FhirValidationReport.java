package mat.server.service.fhirvalidationreport;

import freemarker.template.TemplateException;

import java.io.IOException;

public interface FhirValidationReport {
    String DATE_FORMAT = "dd-MMM-YYYY";
    String TIME_FORMAT = "hh:mm aa";
    String CONVERSION_SERVICE_ERROR = "An error occurred while validating the FHIR conversion. " +
            "Please try again later. If this continues please contact the MAT help desk.";


    String generateReport(String measureId, String vsacGrantingTicket, boolean converted) throws IOException, TemplateException;
}
