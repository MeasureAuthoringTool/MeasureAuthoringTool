package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.views.MeasureDetailViewInterface;
import mat.client.measure.measuredetails.views.ReferencesView;
import mat.shared.measure.measuredetails.models.ReferencesModel;

public class ReferencesObserver implements MeasureDetailsComponentObserver{
	ReferencesView referencesView;
	public ReferencesObserver(ReferencesView referencesView) {
		this.referencesView = referencesView;
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

	public void handleDeleteClicked(int index, String object) {
		((ReferencesModel) referencesView.getMeasureDetailsComponentModel()).getReferences().remove(index);
		referencesView.buildDetailView();
	}
	
	public void handleEditReference() {
		String referenceText = referencesView.getRichTextEditor().getText();
		if(!referenceText.trim().isEmpty()) {
			((ReferencesModel) referencesView.getMeasureDetailsComponentModel()).getReferences().set(referencesView.getEditingIndex(), referenceText);
			referencesView.setEditingIndex(null);
		}
	}

	public void handleAddReference() {
		String referenceText = referencesView.getRichTextEditor().getText();
		if(!referenceText.trim().isEmpty()) {
			((ReferencesModel) referencesView.getMeasureDetailsComponentModel()).getReferences().add(referenceText);
		}
	}
}
