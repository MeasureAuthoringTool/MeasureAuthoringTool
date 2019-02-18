package mat.client.measure.measuredetails.observers;

import java.util.List;

import mat.client.measure.measuredetails.MeasureDetailsObserver;
import mat.client.measure.measuredetails.views.MeasureDetailViewInterface;
import mat.client.measure.measuredetails.views.ReferencesView;
import mat.shared.StringUtility;
import mat.shared.measure.measuredetails.models.ReferencesModel;

public class ReferencesObserver implements MeasureDetailsComponentObserver {
	private ReferencesView referencesView;
	private MeasureDetailsObserver measureDetailsObserver;
	public ReferencesObserver(ReferencesView referencesView, MeasureDetailsObserver measureDetailObserver) {
		this.referencesView = referencesView;
		this.measureDetailsObserver = measureDetailObserver;
	}

	@Override
	public void handleValueChanged() {}
	
	public void handleTextValueChanged() {
		try {
			referencesView.getReferencesModel().getReferences().set(referencesView.getEditingIndex(), referencesView.getRichTextEditor().getValue());
		} catch(IndexOutOfBoundsException iobe) {
			referencesView.getReferencesModel().getReferences().add(referencesView.getRichTextEditor().getValue());
		}
	}

	@Override
	public void setView(MeasureDetailViewInterface view) {
		// TODO Auto-generated method stub
	}

	public void handleEditClicked(int index, String reference) {
		List<String> referenceList = referencesView.getReferencesModel().getReferences();
		if(referenceList != null && referenceList.get(index) != null) {
			try {
				if(StringUtility.isEmptyOrNull(referenceList.get(referencesView.getEditingIndex()))) {
					referenceList.remove(referenceList.get(referencesView.getEditingIndex()));
				}
			} catch(IndexOutOfBoundsException iobe) {}
			referencesView.setEditingIndex(index);
			referencesView.getRichTextEditor().setValue(referenceList.get(index));
		}
	}

	public void handleDeleteReference(int index, String object) {
		((ReferencesModel) referencesView.getMeasureDetailsComponentModel()).getReferences().remove(index);
		referencesView.setEditingIndex(referencesView.getReferencesModel().getReferences().size());
		referencesView.buildDetailView();
		saveReferences();
	}

	public void saveReferences() {
		measureDetailsObserver.saveMeasureDetails();
	}
}
