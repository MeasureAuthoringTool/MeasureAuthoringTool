package mat.client.measure.service;

import com.google.gwt.user.client.rpc.IsSerializable;
import mat.client.measure.ManageMeasureSearchModel;
import mat.model.cql.CQLLibraryDataSetObject;

public class FhirConvertResultResponse implements IsSerializable {

    private FhirValidationStatus validationStatus = new FhirValidationStatus();
    private String fhirMeasureId;

    public FhirValidationStatus getValidationStatus() {
        return validationStatus;
    }

    public void setValidationStatus(FhirValidationStatus validationStatus) {
        this.validationStatus = validationStatus;
    }

    public String getFhirMeasureId() {
        return fhirMeasureId;
    }

    public void setFhirMeasureId(String fhirMeasureId) {
        this.fhirMeasureId = fhirMeasureId;
    }
}
