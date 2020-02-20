package mat.client.cqlworkspace.attributes;

import java.util.Map;
import java.util.TreeMap;

import mat.client.shared.FhirAttribute;
import mat.client.shared.FhirDataType;

public class FhirDataTypeModel {
    private boolean selected;
    private FhirDataType dataType;
    // Sorted by element name
    private final Map<String, FhirAttributeModel> attributesByElement = new TreeMap<>();

    public FhirDataTypeModel(final FhirDataType dataType) {
        this.dataType = dataType;
        for (FhirAttribute attribute : dataType.getAttributes().values()) {
            attributesByElement.put(attribute.getFhirElement(), new FhirAttributeModel(this, attribute));
        }
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Map<String, FhirAttributeModel> getAttributesByElement() {
        return attributesByElement;
    }

    public String getId() {
        return dataType.getId();
    }

    public String getFhirResource() {
        return dataType.getFhirResource();
    }
}
