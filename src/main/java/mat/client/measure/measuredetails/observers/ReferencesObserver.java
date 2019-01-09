package mat.client.measure.measuredetails.observers;

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
		if(referencesView.getReferencesModel().getReferences() != null && referencesView.getReferencesModel().getReferences().get(index) != null) {
			referencesView.setEditingIndex(index);
			referencesView.getRichTextEditor().setValue(referencesView.getReferencesModel().getReferences().get(index));
		}
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
