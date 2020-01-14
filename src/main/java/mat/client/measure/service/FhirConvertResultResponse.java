package mat.client.measure.service;

import com.google.gwt.user.client.rpc.IsSerializable;
import mat.client.measure.ManageMeasureSearchModel;

public class FhirConvertResultResponse implements IsSerializable {

    private FhirValidationStatus validationStatus = new FhirValidationStatus();
    private ManageMeasureSearchModel.Result sourceMeasure;
    private ManageMeasureSearchModel.Result fhirMeasure;

    public FhirValidationStatus getValidationStatus() {
        return validationStatus;
    }

    public void setValidationStatus(FhirValidationStatus validationStatus) {
        this.validationStatus = validationStatus;
    }

    public ManageMeasureSearchModel.Result getSourceMeasure() {
        return sourceMeasure;
    }

    public void setSourceMeasure(ManageMeasureSearchModel.Result sourceMeasure) {
        this.sourceMeasure = sourceMeasure;
    }

    public ManageMeasureSearchModel.Result getFhirMeasure() {
        return fhirMeasure;
    }

    public void setFhirMeasure(ManageMeasureSearchModel.Result fhirMeasure) {
        this.fhirMeasure = fhirMeasure;
    }

}