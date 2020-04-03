package mat.client.measure.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import mat.client.shared.MatException;

import static mat.client.measure.ManageMeasureSearchModel.Result;

@RemoteServiceRelativePath("fhirMeasureService")
public interface FhirMeasureRemoteService extends RemoteService {

    FhirConvertResultResponse convert(Result sourceMeasure) throws MatException;

    CheckMeasureForConversionResult checkMeasureForConversion(Result sourceMeasure);

}
