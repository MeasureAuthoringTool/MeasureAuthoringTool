package mat.client.measure.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureSearchModel.Result;

public interface MeasureCloningServiceAsync {

	void cloneExistingMeasure(ManageMeasureDetailModel currentDetails, AsyncCallback<ManageMeasureSearchModel.Result> callback);

	void draftExistingMeasure(ManageMeasureDetailModel currentDetails, AsyncCallback<Result> asyncCallback);
}

