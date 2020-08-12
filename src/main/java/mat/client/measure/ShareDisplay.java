package mat.client.measure;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.user.client.ui.FocusPanel;
import mat.client.shared.MessageAlert;
import mat.client.shared.SearchWidgetBootStrap;

public interface ShareDisplay extends BaseDisplay {

	public void buildDataTable(UserShareInfoAdapter adapter);

	public HasClickHandlers getCancelButton();

	public HasClickHandlers getSaveButton();

	public HasValueChangeHandlers<Boolean> privateCheckbox();

	public void setMeasureName(String name);

	public void setPrivate(boolean isPrivate);

	SearchWidgetBootStrap getSearchWidgetBootStrap();

	FocusPanel getSearchWidgetFocusPanel();

	MessageAlert getWarningMessageDisplay();

	void resetMessageDisplay();
}
