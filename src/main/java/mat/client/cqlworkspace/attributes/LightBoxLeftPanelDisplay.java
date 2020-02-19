package mat.client.cqlworkspace.attributes;

import java.util.Collection;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;

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
