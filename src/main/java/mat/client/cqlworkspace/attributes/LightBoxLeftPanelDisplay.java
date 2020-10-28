package mat.client.cqlworkspace.attributes;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;

import java.util.Collection;

public interface LightBoxLeftPanelDisplay {

    Widget asWidget();

    HandlerRegistration addSelectionHandler(FhirAttributeSelectionEvent.Handler handler);

    Collection<DataTypeSelectSectionDisplay> getSections();

    interface DataTypeSelectSectionDisplay {

        String getId();

        String getName();

        Widget asWidget();

    }
}
