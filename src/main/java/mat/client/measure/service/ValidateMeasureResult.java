package mat.client.measure.service;

import com.google.gwt.user.client.rpc.IsSerializable;
import mat.client.shared.GenericResult;

import java.util.List;

/**
 * The Class ValidateMeasureResult.
 */
public class ValidateMeasureResult extends GenericResult implements IsSerializable {

    private boolean valid;
    private List<String> validationMessages;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public List<String> getValidationMessages() {
        return validationMessages;
    }

    public void setValidationMessages(List<String> validationMessages) {
        this.validationMessages = validationMessages;
    }

    @Override
    public String toString() {
        return "ValidateMeasureResult{" +
                "valid=" + valid +
                ", validationMessages=" + validationMessages +
                '}';
    }
}
