package mat.client.cqlworkspace.attributes;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import mat.client.shared.FhirAttribute;

public class FhirAttributeSelectionEvent extends GwtEvent<FhirAttributeSelectionEvent.Handler> {

    public static final Type<Handler> TYPE = new Type<>();

    private boolean selected;
    private FhirAttribute attribute;

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(Handler handler) {
        handler.onAttributeSelectionChanged(this);
    }

    public interface Handler extends EventHandler {
        void onAttributeSelectionChanged(FhirAttributeSelectionEvent event);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public FhirAttribute getAttribute() {
        return attribute;
    }

    public void setAttribute(FhirAttribute attribute) {
        this.attribute = attribute;
    }

    @Override
    public String toString() {
        return "FhirAttributeSelectionEvent{" +
                "selected=" + selected +
                ", attribute=" + attribute +
                '}';
    }
}
