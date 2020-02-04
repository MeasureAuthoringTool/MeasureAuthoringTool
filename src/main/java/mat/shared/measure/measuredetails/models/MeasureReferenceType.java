package mat.shared.measure.measuredetails.models;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum MeasureReferenceType implements IsSerializable {

    CITATION("Citation"),
    DOCUMENTATION("Documentation"),
    JUSTIFICATION("Justification"),
    UNKNOWN("Unknown");

    private final String displayName;

    MeasureReferenceType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
