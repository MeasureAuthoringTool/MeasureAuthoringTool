package mat.client.measure.measuredetails.observers;

import com.google.gwt.core.client.GWT;

import mat.client.measure.measuredetails.MeasureDetailsObserver;
import mat.client.measure.measuredetails.views.MeasureDetailViewInterface;
import mat.client.measure.measuredetails.views.ReferencesView;
import mat.shared.measure.measuredetails.models.ReferencesModel;

public class ReferencesObserver implements MeasureDetailsComponentObserver{
	private ReferencesView referencesView;
	private MeasureDetailsObserver measureDetailsObserver;
	public ReferencesObserver(ReferencesView referencesView, MeasureDetailsObserver measureDetailObserver) {
		this.referencesView = referencesView;
		this.measureDetailsObserver = measureDetailObserver;
	}

	@Override
	public void handleValueChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setView(MeasureDetailViewInterface view) {
		// TODO Auto-generated method stub
		
	}

	public void handleEditClicked(int index, String reference) {
		referencesView.setEditingIndex(index);
		referencesView.getRichTextEditor().setValue(reference);
	}

	public void handleDeleteReference(int index, String object) {
		((ReferencesModel) referencesView.getMeasureDetailsComponentModel()).getReferences().remove(index);
		referencesView.buildDetailView();
		saveReferences();
	}
	
	public void handleEditReference() {
		String referenceText = referencesView.getRichTextEditor().getText();
		if(!referenceText.isEmpty()) {
			((ReferencesModel) referencesView.getMeasureDetailsComponentModel()).getReferences().set(referencesView.getEditingIndex(), referenceText);
			referencesView.setEditingIndex(null);
		}
	}

	public void handleAddReference() {
		String referenceText = referencesView.getRichTextEditor().getText();
		if(!referenceText.isEmpty()) {
			((ReferencesModel) referencesView.getMeasureDetailsComponentModel()).getReferences().add(referenceText);
		}
	}

	public void saveReferences() {
		measureDetailsObserver.saveMeasureDetails();
	}
}
