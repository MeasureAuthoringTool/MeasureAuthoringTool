package mat.client.measure.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import static mat.client.measure.ManageMeasureSearchModel.Result;

@RemoteServiceRelativePath("fhirMeasure")
public interface FhirMeasureRemoteServiceAsync {

    void convert(Result sourceMeasure, AsyncCallback<FhirConvertResultResponse> callback);

    void checkMeasureForConversion(Result sourceMeasure, AsyncCallback<CheckMeasureForConversionResult> asyncCallback);

}
