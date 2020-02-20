package mat.client.cqlworkspace.attributes;

import com.google.gwt.user.client.ui.Widget;

public interface LightBoxRightPanelDisplay {
    Widget asWidget();
    void update(FhirDataTypeModel fhirDataTypeModel);
}
