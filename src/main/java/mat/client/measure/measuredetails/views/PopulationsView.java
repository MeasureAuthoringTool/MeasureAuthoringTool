package mat.client.measure.measuredetails.views;

import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import mat.client.measure.measuredetails.observers.MeasureDetailsComponentObserver;
import mat.client.shared.ConfirmationDialogBox;
import mat.client.shared.editor.RichTextEditor;
import mat.shared.measure.measuredetails.models.MeasureDetailsComponentModel;

public class PopulationsView implements MeasureDetailViewInterface {
	private FlowPanel mainPanel = new FlowPanel();

	public PopulationsView() {
		buildDetailView();
	}

	@Override
	public boolean isComplete() {
		return true;
	}

	public Widget getWidget() {
		return mainPanel;
	}

	@Override
	public boolean hasUnsavedChanges() {
		return false;
	}

	@Override
	public void buildDetailView() {
		Label helpLabel = new Label("Click on the + sign on this tab to expand the populations available with the Scoring Type chosen for this "
				+ "measure. Click on each population sub-tab to enter the descriptions for all of the populations used within your measure.");
		helpLabel.getElement().setTabIndex(0);
		helpLabel.setWidth("90%");
		mainPanel.add(helpLabel);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
	}

	@Override
	public ConfirmationDialogBox getSaveConfirmation() {
		return null;
	}

	@Override
	public void resetForm() {
	}

	@Override
	public MeasureDetailsComponentModel getMeasureDetailsComponentModel() {
		return null;
	}

	@Override
	public RichTextEditor getRichTextEditor() {
		return null;
	}

	@Override
	public void clear() {
	}

	@Override
	public void setMeasureDetailsComponentModel(MeasureDetailsComponentModel model) {
	}

	@Override
	public void setObserver(MeasureDetailsComponentObserver observer) {
	}
	
	@Override
	public MeasureDetailsComponentObserver getObserver() {
		return null;
	}
}
