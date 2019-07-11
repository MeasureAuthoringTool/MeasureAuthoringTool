package mat.client.measure;

import java.util.List;

import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.HelpBlock;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasValue;

import mat.client.codelist.HasListBox;
import mat.client.cqlworkspace.EditConfirmationDialogBox;
import mat.client.shared.ListBoxMVP;

public interface DetailDisplay extends BaseDisplay {
	public void clearFields();

	public HasClickHandlers getCancelButton();

	public ListBoxMVP getMeasureScoringListBox();

	public String getMeasureScoringValue();

	public HasValue<String> getMeasureVersion();

	public HasValue<String> getMeasureNameTextBox();
	
	public HasValue<String> getCQLLibraryNameTextBox();

	public HasClickHandlers getSaveButton();

	public HasValue<String> getECQMAbbreviatedTitleTextBox();

	public void setMeasureName(String name);
	
	void setScoringChoices(List<? extends HasListBox> texts);

	public void showCautionMsg(boolean show);

	public void showMeasureName(boolean show);

	ListBoxMVP getPatientBasedListBox();

	HelpBlock getHelpBlock();

	FormGroup getMessageFormGrp();
	
	EditConfirmationDialogBox getConfirmationDialogBox();
}
