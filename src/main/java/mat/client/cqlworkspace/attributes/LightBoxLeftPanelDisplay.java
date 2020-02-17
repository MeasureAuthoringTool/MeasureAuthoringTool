package mat.client.cqlworkspace.attributes;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;

public interface LightBoxLeftPanelDisplay {

    Widget asWidget();

    HandlerRegistration addSelectionHandler(FhirAttributeSelectionEvent.Handler handler);

    interface DataTypeSelectSectionDisplay {

        String getId();

        String getName();

        Widget asWidget();

    }
}
