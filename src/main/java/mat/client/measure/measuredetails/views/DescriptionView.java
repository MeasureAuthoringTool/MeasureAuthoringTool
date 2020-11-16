package mat.client.measure.measuredetails.views;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import mat.client.measure.measuredetails.observers.DescriptionObserver;
import mat.client.measure.measuredetails.observers.MeasureDetailsComponentObserver;
import mat.client.shared.ConfirmationDialogBox;
import mat.shared.measure.measuredetails.models.DescriptionModel;
import mat.shared.measure.measuredetails.models.MeasureDetailsComponentModel;

public class DescriptionView implements MeasureDetailViewInterface {
    private FlowPanel mainPanel = new FlowPanel();
    private MeasureDetailsTextEditor measureDetailsTextEditor;
    private DescriptionModel originalDescriptionModel;
    private DescriptionModel descriptionModel;
    private DescriptionObserver observer;

    public DescriptionView() {

    }

    public DescriptionView(DescriptionModel descriptionModel) {
        this.originalDescriptionModel = descriptionModel;
        buildGeneralInformationModel(this.originalDescriptionModel);
        buildDetailView();
    }


    @Override
    public Widget getWidget() {
        return mainPanel;
    }

    @Override
    public boolean hasUnsavedChanges() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void buildDetailView() {
        measureDetailsTextEditor = new MeasureDetailsTextEditor();
        mainPanel.add(measureDetailsTextEditor);
        measureDetailsTextEditor.setTitle("Description Editor");
        measureDetailsTextEditor.setText(this.descriptionModel.getEditorText());
        addEventHandlers();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        measureDetailsTextEditor.setReadOnly(readOnly);
    }

    @Override
    public ConfirmationDialogBox getSaveConfirmation() {
        return null;
    }

    @Override
    public void resetForm() {
        // TODO Auto-generated method stub

    }

    @Override
    public MeasureDetailsComponentModel getMeasureDetailsComponentModel() {
        return this.descriptionModel;
    }

    @Override
    public TextArea getTextEditor() {
        return measureDetailsTextEditor.getTextEditor();
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub
    }

    @Override
    public void setMeasureDetailsComponentModel(MeasureDetailsComponentModel model) {
        this.descriptionModel = (DescriptionModel) model;
        this.originalDescriptionModel = descriptionModel;
        buildGeneralInformationModel(this.originalDescriptionModel);
    }


    @Override
    public void setObserver(MeasureDetailsComponentObserver observer) {
        this.observer = (DescriptionObserver) observer;
    }

    @Override
    public DescriptionObserver getObserver() {
        return observer;
    }

    private void addEventHandlers() {
        measureDetailsTextEditor.getTextEditor().addKeyUpHandler(event -> observer.handleValueChanged());
    }

    private void buildGeneralInformationModel(DescriptionModel originalDescriptionModel) {
        this.descriptionModel = new DescriptionModel(originalDescriptionModel);
    }

    @Override
    public Widget getFirstElement() {
        return measureDetailsTextEditor.getTextEditor();
    }
}
