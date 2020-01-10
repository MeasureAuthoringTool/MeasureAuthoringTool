package mat.client.measure.service;

import com.google.gwt.user.client.rpc.IsSerializable;

public class FhirValidationStatus implements IsSerializable {

    boolean validationPassed;
    boolean hasValueSetResultsErrors;
    boolean hasLibraryFhirValidationErrors;
    boolean hasMeasureFhirValidationErrors;

    public boolean isValidationPassed() {
        return validationPassed;
    }

    public void setValidationPassed(boolean validationPassed) {
        this.validationPassed = validationPassed;
    }

    public boolean isHasValueSetResultsErrors() {
        return hasValueSetResultsErrors;
    }

    public void setHasValueSetResultsErrors(boolean hasValueSetResultsErrors) {
        this.hasValueSetResultsErrors = hasValueSetResultsErrors;
    }

    public boolean isHasLibraryFhirValidationErrors() {
        return hasLibraryFhirValidationErrors;
    }

    public void setHasLibraryFhirValidationErrors(boolean hasLibraryFhirValidationErrors) {
        this.hasLibraryFhirValidationErrors = hasLibraryFhirValidationErrors;
    }

    public boolean isHasMeasureFhirValidationErrors() {
        return hasMeasureFhirValidationErrors;
    }

    public void setHasMeasureFhirValidationErrors(boolean hasMeasureFhirValidationErrors) {
        this.hasMeasureFhirValidationErrors = hasMeasureFhirValidationErrors;
    }
}
