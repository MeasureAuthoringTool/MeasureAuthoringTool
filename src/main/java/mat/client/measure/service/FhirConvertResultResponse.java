package mat.client.measure.service;

import com.google.gwt.user.client.rpc.IsSerializable;
import mat.client.measure.ManageMeasureSearchModel;
import mat.model.cql.CQLLibraryDataSetObject;

public class FhirConvertResultResponse implements IsSerializable {

    private FhirValidationStatus validationStatus = new FhirValidationStatus();
    private ManageMeasureSearchModel.Result sourceMeasure;
    private ManageMeasureSearchModel.Result fhirMeasure;

    private CQLLibraryDataSetObject sourceLibrary;

    private CQLLibraryDataSetObject fhirLibrary;

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

    public CQLLibraryDataSetObject getSourceLibrary() {
        return sourceLibrary;
    }

    public void setSourceLibrary(CQLLibraryDataSetObject sourceLibrary) {
        this.sourceLibrary = sourceLibrary;
    }

    public CQLLibraryDataSetObject getFhirLibrary() {
        return fhirLibrary;
    }

    public void setFhirLibrary(CQLLibraryDataSetObject fhirLibrary) {
        this.fhirLibrary = fhirLibrary;
    }
}
