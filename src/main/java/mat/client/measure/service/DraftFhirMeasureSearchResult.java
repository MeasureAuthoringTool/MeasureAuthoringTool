package mat.client.measure.service;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DraftFhirMeasureSearchResult implements IsSerializable {
    private String measureModel;
    private String id;
    private boolean found;

    public String getMeasureModel() {
        return measureModel;
    }

    public void setMeasureModel(String measureModel) {
        this.measureModel = measureModel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }
}
