package mat.client.cqlworkspace.attributes;

import mat.client.shared.FhirAttribute;

public class FhirAttributeModel {
    private boolean selected;
    private FhirAttribute attribute;
    private FhirDataTypeModel dataType;


    public FhirAttributeModel(FhirDataTypeModel dataType, FhirAttribute attribute) {
        this.dataType = dataType;
        this.attribute = attribute;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getFhirElement() {
        return attribute.getFhirElement();
    }

    public String getId() {
        return attribute.getId();
    }

    public String getFhirType() {
        return attribute.getFhirType();
    }

    public FhirDataTypeModel getDataType() {
        return dataType;
    }
}
