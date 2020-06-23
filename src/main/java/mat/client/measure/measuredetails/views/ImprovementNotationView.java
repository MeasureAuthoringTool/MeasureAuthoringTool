package mat.client.measure.measuredetails.views;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import mat.client.measure.measuredetails.observers.ImprovementNotationObserver;
import mat.client.measure.measuredetails.observers.MeasureDetailsComponentObserver;
import mat.client.shared.ConfirmationDialogBox;
import mat.client.shared.MatContext;
import mat.shared.measure.measuredetails.models.ImprovementNotationModel;
import mat.shared.measure.measuredetails.models.MeasureDetailsComponentModel;

public class ImprovementNotationView implements MeasureDetailViewInterface {
    private final FlowPanel mainPanel = new FlowPanel();
    private MeasureDetailsTextEditor measureDetailsTextEditor;
    private ListBox listBox;

    private ImprovementNotationModel model;
    private ImprovementNotationModel originalModel;
    private ImprovementNotationObserver observer;

    public ImprovementNotationView() {

    }

    public ImprovementNotationView(ImprovementNotationModel model) {
        this.originalModel = model;
        this.model = new ImprovementNotationModel(this.originalModel);
        buildDetailView();
    }

    @Override
    public Widget getWidget() {
        return mainPanel;
    }

    @Override
    public boolean hasUnsavedChanges() {
        return false;
    }

    @Override
    public void buildDetailView() {
        String modelValue = this.model.getEditorText();
        if (MatContext.get().isCurrentModelTypeFhir()) {
            listBox = new ListBox();
            listBox.getElement().getStyle().setMargin(5.0, Style.Unit.PX);
            listBox.setMultipleSelect(false);
            listBox.addItem("increase");
            listBox.addItem("decrease");
            listBox.setSelectedIndex("decrease".equals(modelValue) ? 1 : 0);
            mainPanel.add(listBox);
        } else {
            measureDetailsTextEditor = new MeasureDetailsTextEditor();
            mainPanel.add(measureDetailsTextEditor);
            measureDetailsTextEditor.setTitle("Improvement Notation Editor");
            measureDetailsTextEditor.setText(modelValue);
            addEventHandlers();
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.measureDetailsTextEditor.setReadOnly(readOnly);
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
        return this.model;
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
        this.originalModel = (ImprovementNotationModel) model;
        this.model = new ImprovementNotationModel(this.originalModel);
    }

    @Override
    public void setObserver(MeasureDetailsComponentObserver observer) {
        this.observer = (ImprovementNotationObserver) observer;
    }

    @Override
    public MeasureDetailsComponentObserver getObserver() {
        return observer;
    }

    private void addEventHandlers() {
        if (MatContext.get().isCurrentModelTypeFhir()) {
            listBox.addChangeHandler(e -> observer.handleValueChanged());
        } else {
            measureDetailsTextEditor.getTextEditor().addKeyUpHandler(event -> observer.handleValueChanged());
        }
    }

    @Override
    public Widget getFirstElement() {
        return null;
    }

    public ListBox getListBox() {
        return listBox;
    }
}
