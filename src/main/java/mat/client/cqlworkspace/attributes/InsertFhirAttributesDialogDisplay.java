package mat.client.cqlworkspace.attributes;

import com.google.gwt.event.dom.client.HasClickHandlers;

public interface InsertFhirAttributesDialogDisplay {

    LightBoxLeftPanelDisplay getLeftPanel();

    LightBoxRightPanelDisplay getRightPanel();

    void show();

    void hide();

    HasClickHandlers getInsertButton();

    HasClickHandlers getCloseButton();

}
