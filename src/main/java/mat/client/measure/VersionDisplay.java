package mat.client.measure;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.RadioButton;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.shared.MessageAlert;
import mat.client.shared.MessagePanel;

public interface VersionDisplay extends BaseDisplay {

	HasClickHandlers getCancelButton();

	RadioButton getMajorRadioButton();

	RadioButton getMinorRadioButton();

	HasClickHandlers getSaveButton();

	Result getSelectedMeasure();

	void setSelectedMeasure(Result selectedMeasure);

	MessageAlert getErrorMessageDisplay();
	
	MessagePanel getMessagePanel();
}
