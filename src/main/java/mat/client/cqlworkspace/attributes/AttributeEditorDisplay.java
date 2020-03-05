package mat.client.cqlworkspace.attributes;

import com.google.gwt.user.client.ui.Widget;

public interface AttributeEditorDisplay {
    Widget asWidget();
    void setSelected(boolean selected);
    boolean isSelected();
}
