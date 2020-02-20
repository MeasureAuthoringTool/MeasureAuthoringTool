package mat.client.cqlworkspace.attributes;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import mat.client.shared.FhirDataType;

public class InsertFhirAttributesDialogModel {

    private final Map<String, FhirDataTypeModel> dataTypesByResource = new TreeMap<>();
    private final Map<String, FhirAttributeModel> allAttributesById = new HashMap<>();

    public InsertFhirAttributesDialogModel() {
    }

    public InsertFhirAttributesDialogModel(Map<String, FhirDataType> dataTypes) {
        if (dataTypes == null) {
            throw new IllegalArgumentException("Required data types map is null");
        }
        for (FhirDataType dataType : dataTypes.values()) {
            FhirDataTypeModel dataTypeModel = new FhirDataTypeModel(dataType);
            dataTypesByResource.put(dataType.getFhirResource(), dataTypeModel);
            dataTypeModel.getAttributesByElement().values().stream().forEach(attr ->
                    allAttributesById.put(attr.getId(), attr)
            );
        }
    }

    public Map<String, FhirDataTypeModel> getDataTypesByResource() {
        return dataTypesByResource;
    }

    public Map<String, FhirAttributeModel> getAllAttributesById() {
        return allAttributesById;
    }
}
