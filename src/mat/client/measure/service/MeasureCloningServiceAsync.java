package mat.client.measure.service;

import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * The Interface MeasureCloningServiceAsync.
 */
public interface MeasureCloningServiceAsync {
	
	/**
	 * Clone.
	 * 
	 * @param currentDetails
	 *            the current details
	 * @param loggedinUserId
	 *            the loggedin user id
	 * @param isDraftCreation
	 *            the is draft creation
	 * @param callback
	 *            the callback
	 */
	void clone(ManageMeasureDetailModel currentDetails, String loggedinUserId, boolean isDraftCreation, 
			AsyncCallback<ManageMeasureSearchModel.Result> callback);
}

