package mat.server.clause;

import mat.client.measure.service.CheckForConversionResult;
import mat.client.measure.service.FhirConvertResultResponse;
import mat.client.measure.service.FhirMeasureRemoteService;
import mat.client.shared.MatException;
import mat.client.umls.service.VsacTicketInformation;
import mat.dao.clause.MeasureDAO;
import mat.model.clause.Measure;
import mat.server.LoggedInUserUtil;
import mat.server.SpringRemoteServiceServlet;
import org.slf4j.LoggerFactory;
import mat.server.service.FhirMeasureService;
import mat.server.service.VSACApiService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static mat.client.measure.ManageMeasureSearchModel.Result;

public class FhirMeasureRemoteServiceImpl extends SpringRemoteServiceServlet implements FhirMeasureRemoteService {

    private static final long serialVersionUID = 2280421300224680146L;

    private static final Logger logger = LoggerFactory.getLogger(FhirMeasureRemoteServiceImpl.class);

    @Autowired
    private FhirMeasureService fhirMeasureService;
    @Autowired
    private VSACApiService vsacApiService;
    @Autowired
    private MeasureDAO measureDAO;

    @Override
    public FhirConvertResultResponse convert(Result sourceMeasure) throws MatException {
        logger.debug("Converting  measureId: " + sourceMeasure.getId());

        String sessionId = getThreadLocalRequest().getSession().getId();
        VsacTicketInformation vsacTicketInformation = this.vsacApiService.getVsacInformation(sessionId);
        if (vsacTicketInformation == null) {
            throw new MatException("No Valid UMLS Api Key Available.");
        }
        try {
            System.out.println("Logged in user: " + LoggedInUserUtil.getLoggedInUser());
            return fhirMeasureService.convert(sourceMeasure,
            				vsacTicketInformation.getApiKey(),
                    LoggedInUserUtil.getLoggedInUser(),
                    true);
        } catch (MatException e) {
            logger.error("Error calling fhirMeasureService.convert", e);
            throw e;
        } catch (Exception e) {
            logger.error("Error calling fhirMeasureService.convert", e);
            throw new MatException(e.getMessage(), e);
        }
    }

    @Override
    public CheckForConversionResult checkMeasureForConversion(Result sourceMeasure) {
        logger.debug("checkMeasureForConversion  measureId: " + sourceMeasure.getId() + " setId: " + sourceMeasure.getMeasureSetId());
        CheckForConversionResult result = new CheckForConversionResult();
        List<Measure> draftMeasures = measureDAO.getDraftMeasuresBySet(sourceMeasure.getMeasureSetId());
        if (draftMeasures.isEmpty()) {
            // If no drafts found we can proceed with conversion
            result.setProceedImmediately(true);
        } else {
            // If the only draft is a FHIR draft created from the same source measure then ask for confirmation to override.
            // UI cannot proceed with conversion if User didn't confirm or if there is/are other draft(s)
            result.setConfirmBeforeProceed(draftMeasures.stream().allMatch(m -> m.isFhirMeasure() && StringUtils.equals(sourceMeasure.getId(), m.getSourceMeasureId())));
        }

        return result;
    }

}
