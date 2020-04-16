package mat.client.measure.service;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CheckForConversionResult implements IsSerializable {
    private boolean proceedImmediately;
    private boolean confirmBeforeProceed;

    public boolean isProceedImmediately() {
        return proceedImmediately;
    }

    public void setProceedImmediately(boolean proceedImmediately) {
        this.proceedImmediately = proceedImmediately;
    }

    public boolean isConfirmBeforeProceed() {
        return confirmBeforeProceed;
    }

    public void setConfirmBeforeProceed(boolean confirmBeforeProceed) {
        this.confirmBeforeProceed = confirmBeforeProceed;
    }
}
