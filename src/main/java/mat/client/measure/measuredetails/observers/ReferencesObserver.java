package mat.client.measure.measuredetails.observers;

import java.util.List;

import mat.client.measure.measuredetails.MeasureDetailsObserver;
import mat.client.measure.measuredetails.views.MeasureDetailViewInterface;
import mat.client.measure.measuredetails.views.ReferencesView;
import mat.shared.measure.measuredetails.models.ReferencesModel;

public class ReferencesObserver implements MeasureDetailsComponentObserver {
	private ReferencesView referencesView;
	private MeasureDetailsObserver measureDetailsObserver;
	public ReferencesObserver(ReferencesView referencesView, MeasureDetailsObserver measureDetailObserver) {
		this.referencesView = referencesView;
		this.measureDetailsObserver = measureDetailObserver;
	}

	@Override
	public void handleValueChanged() {
		//checks if the plain text length is greater than zero
		if(referencesView.getRichTextEditor().getFormattedText() != null && referencesView.getRichTextEditor().getFormattedText().trim().length() !=0) {
			handleTextValueChanged();
		}
	}
	
	public void handleTextValueChanged() {
		try {
			String reference = nullCheckRichTextEditor();
			referencesView.getReferencesModel().getReferences().set(referencesView.getEditingIndex(), reference);
		} catch(IndexOutOfBoundsException iobe) {
			referencesView.getReferencesModel().getReferences().add(referencesView.getRichTextEditor().getFormattedText().trim());
		}
	}

	private String nullCheckRichTextEditor() {
		return referencesView.getRichTextEditor().getFormattedText() == null ? "" : referencesView.getRichTextEditor().getFormattedText();
	}

	@Override
	public void setView(MeasureDetailViewInterface view) {
		// TODO Auto-generated method stub
	}

	public void handleEditClicked(int index) {
		List<String> referenceList = referencesView.getOriginalModel().getReferences();
		referencesView.getReferencesModel().setReferences(referenceList);
		if(referenceList != null && referenceList.get(index) != null) {
			referencesView.setEditingIndex(index);
			referencesView.getRichTextEditor().setHTML(referenceList.get(index));
		}
	}

	public void handleDeleteReference(int index, String object) {
		((ReferencesModel) referencesView.getMeasureDetailsComponentModel()).getReferences().remove(index);
		referencesView.setEditingIndex(referencesView.getReferencesModel().getReferences().size());
		referencesView.buildDetailView();
		saveReferences();
	}

	private void saveReferences() {
		measureDetailsObserver.saveMeasureDetails();
	}
}
