package mat.client.measure.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.shared.MatException;

@RemoteServiceRelativePath("measureCloning")
public interface MeasureCloningRemoteService extends RemoteService {

	ManageMeasureSearchModel.Result cloneExistingMeasure(ManageMeasureDetailModel currentDetails) throws MatException;
	
	ManageMeasureSearchModel.Result draftExistingMeasure(ManageMeasureDetailModel currentDetails) throws MatException;
}

