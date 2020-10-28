package mat.client.measure.service;

import com.google.gwt.user.client.rpc.IsSerializable;

public class FhirValidationStatus implements IsSerializable {

    private boolean validationPassed;
    private String errorReason;
    private String outcome;

    public boolean isValidationPassed() {
        return validationPassed;
    }

    public void setValidationPassed(boolean validationPassed) {
        this.validationPassed = validationPassed;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

}
