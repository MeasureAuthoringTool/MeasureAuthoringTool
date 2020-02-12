package mat.client.cqlworkspace.attributes;

import java.util.ArrayList;
import java.util.List;

public class InsertFhirAttributesDialogModel {

    private List<FhirDataType> dataTypes = new ArrayList<>();


    public InsertFhirAttributesDialogModel() {
    }

    public InsertFhirAttributesDialogModel(List<FhirDataType> dataTypes) {
        this.dataTypes = dataTypes;
    }

    public List<FhirDataType> getDataTypes() {
        return dataTypes;
    }

    public void setDataTypes(List<FhirDataType> dataTypes) {
        this.dataTypes = dataTypes;
    }

}
