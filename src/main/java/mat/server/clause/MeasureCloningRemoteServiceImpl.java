package mat.server.clause;

import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.measure.service.MeasureCloningRemoteService;
import mat.client.shared.MatException;
import mat.dao.UserDAO;
import mat.dao.clause.MeasureDAO;
import mat.model.clause.ModelTypeHelper;
import mat.server.SpringRemoteServiceServlet;
import mat.server.logging.LogFactory;
import mat.server.service.MeasureCloningService;
import mat.server.service.impl.MatContextServiceUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;

public class MeasureCloningRemoteServiceImpl extends SpringRemoteServiceServlet implements MeasureCloningRemoteService {

    private static final Log logger = LogFactory.getLog(MeasureCloningRemoteServiceImpl.class);

    private static final String CANNOT_ACCESS_MEASURE = "Cannot access this measure.";

    @Autowired
    private MeasureDAO measureDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private MeasureCloningService measureCloningService;

    @Override
    public ManageMeasureSearchModel.Result cloneExistingMeasure(ManageMeasureDetailModel currentDetails) throws MatException {

        currentDetails.setMeasureSetId(null);
        if (!MatContextServiceUtil.get().isCurrentMeasureClonable(measureDAO, currentDetails.getId())) {
            createException(CANNOT_ACCESS_MEASURE);
        }

        return measureCloningService.clone(currentDetails, false);
    }

    @Override
    public Result draftExistingMeasure(ManageMeasureDetailModel currentDetails) throws MatException {
        if (!MatContextServiceUtil.get().isCurrentMeasureDraftable(measureDAO, userDAO, currentDetails.getId())) {
            createException(CANNOT_ACCESS_MEASURE);
        }

        String name = measureDAO.getMeasureNameIfDraftAlreadyExists(currentDetails.getMeasureSetId());
        if (StringUtils.isNotBlank(name)) {
            createException("This draft can not be created. A draft of " + name + " has already been created in the system.");
        }

        if (ModelTypeHelper.FHIR.equalsIgnoreCase(currentDetails.getMeasureModel())) {
            return measureCloningService.cloneForFhir(currentDetails);
        } else {
            return measureCloningService.clone(currentDetails, true);
        }

    }

    private void createException(String message) throws MatException {
        Exception e = new Exception(message);
        logger.error(e);
        log(e.getMessage(), e);
        throw new MatException(e.getMessage());
    }

}