package mat.client.cqlworkspace.attributes;

import mat.client.shared.FhirDataType;

import java.util.Map;
import java.util.TreeMap;

public class InsertFhirAttributesDialogModel {

    // Sorted by resource name
    private Map<String, FhirDataType> dataTypes = new TreeMap<>();

    public InsertFhirAttributesDialogModel() {
    }

    public InsertFhirAttributesDialogModel(Map<String, FhirDataType> dataTypes) {
        if (dataTypes == null) {
            throw new IllegalArgumentException("Required data types map is null");
        }
        this.dataTypes = dataTypes;
    }

    public Map<String, FhirDataType> getDataTypes() {
        return dataTypes;
    }

    public void setDataTypes(Map<String, FhirDataType> dataTypes) {
        this.dataTypes = dataTypes;
    }

}
