package mat.client.measure;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasValue;
import mat.client.codelist.HasListBox;
import mat.client.cqlworkspace.EditConfirmationDialogBox;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.WarningConfirmationMessageAlert;
import mat.client.validator.ErrorHandler;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.HelpBlock;

import java.util.List;

public interface DetailDisplay extends BaseDisplay {
	void clearFields();

	HasClickHandlers getCancelButton();

	ListBoxMVP getMeasureScoringListBox();

	String getMeasureScoringValue();

	String getMeasureModelType();

	HasValue<String> getMeasureVersion();

	HasValue<String> getMeasureNameTextBox();
	
	HasValue<String> getCQLLibraryNameTextBox();

	HasClickHandlers getSaveButton();

	HasValue<String> getECQMAbbreviatedTitleTextBox();

	void setMeasureName(String name);

	void setMeasureModelType(String name);

	void allowAllMeasureModelTypes();

	void setScoringChoices(List<? extends HasListBox> texts);

	void showCautionMsg(boolean show);

	void showMeasureName(boolean show);

	ListBoxMVP getPatientBasedListBox();

	HelpBlock getHelpBlock();

	FormGroup getMessageFormGrp();
	
	EditConfirmationDialogBox getConfirmationDialogBox();

	WarningConfirmationMessageAlert getWarningConfirmationMessageAlert();

    CheckBox getGenerateCmsIdCheckbox();

    ErrorHandler getErrorHandler();
}
