package mat.client.measure.measuredetails.observers;

import mat.client.measure.ReferenceTextAndType;
import mat.client.measure.measuredetails.MeasureDetailsObserver;
import mat.client.measure.measuredetails.views.MeasureDetailViewInterface;
import mat.client.measure.measuredetails.views.ReferencesView;
import mat.client.shared.MessagePanel;
import mat.shared.measure.measuredetails.models.MeasureReferenceType;
import mat.shared.measure.measuredetails.models.ReferencesModel;

import java.util.List;

public class ReferencesObserver implements MeasureDetailsComponentObserver {
    private ReferencesView referencesView;
    private MeasureDetailsObserver measureDetailsObserver;
    private MessagePanel messagePanel;

    public ReferencesObserver(ReferencesView referencesView, MeasureDetailsObserver measureDetailObserver, MessagePanel messagePanel) {
        this.referencesView = referencesView;
        this.measureDetailsObserver = measureDetailObserver;
        this.messagePanel = messagePanel;
    }

    @Override
    public void handleValueChanged() {
        // checks if the plain text length is greater than zero
        if (referencesView.getTextEditor().getText() != null && referencesView.getTextEditor().getText().trim().length() != 0) {
            handleTextValueChanged();
        }
    }

    public void handleTextValueChanged() {
        String referenceText = nullCheckRichTextEditor();
        MeasureReferenceType referenceType = MeasureReferenceType.valueOf(referencesView.getReferenceTypeEditor().getSelectedValue());
        try {
            ReferenceTextAndType currentRef = referencesView.getReferencesModel().getReferences().get(referencesView.getEditingIndex());
            currentRef.setReferenceText(referenceText);
            currentRef.setReferenceType(referenceType);
        } catch (IndexOutOfBoundsException iobe) {
            ReferenceTextAndType newRef = new ReferenceTextAndType(referenceText, referenceType);
            referencesView.getReferencesModel().getReferences().add(newRef);
        }
    }

    private String nullCheckRichTextEditor() {
        return referencesView.getTextEditor().getText() == null ? "" : referencesView.getTextEditor().getText().trim();
    }

    @Override
    public void setView(MeasureDetailViewInterface view) {
        // No-op
    }

    public void handleEditClicked(int index) {
        messagePanel.clearAlerts();
        List<ReferenceTextAndType> referenceList = referencesView.getOriginalModel().getReferences();
        referencesView.getReferencesModel().setReferences(referenceList);
        if (referenceList != null) {
            ReferenceTextAndType ref = referenceList.get(index);
            if (ref != null) {
                referencesView.setEditingIndex(index);
                referencesView.getTextEditor().setText(ref.getReferenceText());
                referencesView.getReferenceTypeEditor().setSelectedIndex(ref.getReferenceType().ordinal());
                referencesView.getReferenceTypeEditor().setEnabled(referencesView.isEditorDirty());
            }
        }
    }

    public void handleDeleteReference(int index) {
        ((ReferencesModel) referencesView.getMeasureDetailsComponentModel()).getReferences().remove(index);
        referencesView.setEditingIndex(referencesView.getReferencesModel().getReferences().size());
        referencesView.buildDetailView();
        saveReferences();
    }

    private void saveReferences() {
        measureDetailsObserver.saveMeasureDetails();
    }
}
