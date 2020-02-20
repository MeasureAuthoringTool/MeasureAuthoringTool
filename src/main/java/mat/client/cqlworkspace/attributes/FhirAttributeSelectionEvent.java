package mat.client.cqlworkspace.attributes;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class FhirAttributeSelectionEvent extends GwtEvent<FhirAttributeSelectionEvent.Handler> {

    public static final Type<Handler> TYPE = new Type<>();

    private final boolean selected;
    private final String attributeId;

    public FhirAttributeSelectionEvent(String attributeId, boolean selected) {
        this.attributeId = attributeId;
        this.selected = selected;
    }

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(Handler handler) {
        handler.onAttributeSelectionChanged(this);
    }

    public boolean isSelected() {
        return selected;
    }

    public String getAttributeId() {
        return attributeId;
    }

    @Override
    public String toString() {
        return "FhirAttributeSelectionEvent{" +
                "selected=" + selected +
                ", attributeId='" + attributeId + '\'' +
                '}';
    }

    public interface Handler extends EventHandler {
        void onAttributeSelectionChanged(FhirAttributeSelectionEvent event);
    }

}
