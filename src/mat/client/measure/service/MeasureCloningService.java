package mat.client.measure.service;

import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.shared.MatException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */	
@RemoteServiceRelativePath("measureCloning")
public interface MeasureCloningService extends RemoteService {
	//ManageMeasureSearchModel.Result clone(ManageMeasureDetailModel currentDetails, String loggedinUserId,boolean creatingDraft) throws MatException;
	/**
	 * Clone.
	 * 
	 * @param currentDetails
	 *            the current details
	 * @param loggedinUserId
	 *            the loggedin user id
	 * @param creatingDraft
	 *            the creating draft
	 * @return the manage measure search model. result
	 * @throws MatException
	 *             the mat exception
	 */
	ManageMeasureSearchModel.Result clone(ManageMeasureDetailModel currentDetails, String loggedinUserId,boolean creatingDraft) throws MatException;
}

