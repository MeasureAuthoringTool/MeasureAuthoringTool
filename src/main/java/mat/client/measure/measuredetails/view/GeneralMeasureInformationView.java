package mat.client.measure.measuredetails.view;

import java.util.List;

import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.TextBox;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.codelist.HasListBox;
import mat.client.measure.measuredetails.MeasureDetailState;
import mat.client.measure.measuredetails.components.GeneralInformationModel;
import mat.client.measure.measuredetails.observers.GeneralMeasureInformationObserver;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.MessageDelegate;
import mat.client.shared.SpacerWidget;
import mat.shared.CompositeMethodScoringConstant;
import mat.shared.StringUtility;

public class GeneralMeasureInformationView implements ComponentDetailView{
	private FlowPanel mainPanel = new FlowPanel();
	private GeneralInformationModel generalInformationModel;
	private TextBox abbrInput = new TextBox();
	private TextBox measureNameInput = new TextBox();
	private boolean readOnly = false;
	private boolean isCompositeMeasure = false;
	private ListBoxMVP  measureScoringInput = new ListBoxMVP();
	private ListBoxMVP compositeScoringMethodInput = new ListBoxMVP();
	private ListBoxMVP patientBasedInput = new ListBoxMVP();
	private static final String TEXT_BOX_WIDTH = "300px";
	private GeneralMeasureInformationObserver observer;
    
	public GeneralMeasureInformationView(boolean isComposite, GeneralInformationModel generalInformationModel) {
		this.generalInformationModel = generalInformationModel;
		this.isCompositeMeasure = isComposite;
		buildDetailView();
		setReadOnly(readOnly);
	}

	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public Widget getWidget() {
		return mainPanel;
	}

	@Override
	public boolean hasUnsavedChanges() {
		//TODO compare model against old model for dirty check
		return false;
	}

	@Override
	public void buildDetailView() {
		mainPanel.clear();
		HorizontalPanel detailPanel = new HorizontalPanel();
		Grid panelGrid = new Grid(5, 2);
		
		VerticalPanel measureNamePanel = buildMeasureNamePanel();
		panelGrid.setWidget(0, 0, measureNamePanel);
		
		VerticalPanel abbreviationPanel = buildAbbreviationPanel();
		panelGrid.setWidget(0, 1, abbreviationPanel);
		
		VerticalPanel measureScoringPanel = buildMeasureScoringPanel();
		
		//TODO compare model
		//TODO add confirmation dialog
		//TODO handle save...
		//TODO check read only is working right
		if(isCompositeMeasure) {
			VerticalPanel compositeScoringPanel = buildCompositeScoringPanel();
			panelGrid.setWidget(1, 0, compositeScoringPanel);
			VerticalPanel blankPanel = buildBlankPanel();
			panelGrid.setWidget(1, 1, blankPanel);
			setCompositeScoringSelectedValue(generalInformationModel.getCompositeScoringMethod());
		} else {
			MatContext.get().getListBoxCodeProvider().getScoringList(new AsyncCallback<List<? extends HasListBox>>() {
				@Override
				public void onFailure(Throwable caught) {
					Window.alert(MessageDelegate.s_ERR_RETRIEVE_SCORING_CHOICES);
				}

				@Override
				public void onSuccess(List<? extends HasListBox> result) {
					setScoringChoices(result);
				}
			});
		}
		
		panelGrid.setWidget(2, 0, measureScoringPanel);
		
		VerticalPanel patientBasedPanel = buildPatientBasedPanel();
		panelGrid.setWidget(2, 1, patientBasedPanel);
		
		VerticalPanel finalizedDatePanel = buildFinalizedDate();
		panelGrid.setWidget(3, 0, finalizedDatePanel);
		
		VerticalPanel guidPanel = buildGUIDPanel();
		panelGrid.setWidget(3, 1, guidPanel);
		
		VerticalPanel eCQMVersionPanel = buildeCQMVersionPanel();
		panelGrid.setWidget(4, 0, eCQMVersionPanel);
		
		detailPanel.add(panelGrid);
		mainPanel.add(detailPanel);
		addEventHandlers();
	}

	private void addEventHandlers() {
		getMeasureScoringInput().addChangeHandler(event -> observer.handleMeasureScoringChanged());
		getCompositeScoringMethodInput().addChangeHandler(event -> observer.handleCompositeScoringChanged());
	}

	private VerticalPanel buildBlankPanel() {
		VerticalPanel blankPanel = new VerticalPanel();
		blankPanel.getElement().addClassName("generalInformationPanel");
		blankPanel.add(new SpacerWidget());
		return blankPanel;
	}
	
	private VerticalPanel buildFinalizedDate() {
		VerticalPanel panel = new VerticalPanel();
		panel.getElement().addClassName("generalInformationPanel");
		TextBox finalizedDateTextBox = new TextBox();
		FormLabel finalizedDateLabel = new FormLabel();
		finalizedDateLabel.setText("Finalized Date");
		finalizedDateLabel.setTitle(finalizedDateLabel.getText());
		panel.add(finalizedDateLabel);
		finalizedDateLabel.setId("finalizedDateLabel");
		finalizedDateLabel.setFor("finalizedDate");
		finalizedDateTextBox.setId("finalizedDate");
		finalizedDateTextBox.setReadOnly(true);
		finalizedDateTextBox.setEnabled(false);
		finalizedDateTextBox.setWidth(TEXT_BOX_WIDTH);
		panel.add(finalizedDateTextBox);
		panel.add(new SpacerWidget());
		
		finalizedDateTextBox.setText(generalInformationModel.getFinalizedDate());
		return panel;
	}
	
	private VerticalPanel buildeCQMVersionPanel() {
		VerticalPanel panel = new VerticalPanel();
		panel.getElement().addClassName("generalInformationPanel");
		TextBox eCQMVersionNumberTextBox = new TextBox(); 
		FormLabel eCQMVersionNumberLabel = new FormLabel();
		eCQMVersionNumberLabel.setText("eCQM Version Number");
		eCQMVersionNumberLabel.setTitle(eCQMVersionNumberLabel.getText());
		eCQMVersionNumberLabel.setId("versionInputLabel");
		eCQMVersionNumberLabel.setFor("versionInput");
		eCQMVersionNumberTextBox.setReadOnly(true);
		eCQMVersionNumberTextBox.setEnabled(false);
		eCQMVersionNumberTextBox.setWidth(TEXT_BOX_WIDTH);
		eCQMVersionNumberTextBox.setId("versionInput");
		panel.add(eCQMVersionNumberLabel);
		panel.add(eCQMVersionNumberTextBox);
		
		eCQMVersionNumberTextBox.setText(generalInformationModel.geteCQMVersionNumber());
		return panel;
	}
	
	private VerticalPanel buildGUIDPanel() {
		VerticalPanel panel = new VerticalPanel();
		panel.getElement().addClassName("generalInformationPanel");
		FormLabel guidLabel = new FormLabel();
		guidLabel.setText("GUID");
		guidLabel.setTitle(guidLabel.getText());
		guidLabel.setId("guidLabel");
		guidLabel.setFor("guidLabel");
		TextBox guidTextBox = new TextBox(); 
		guidTextBox.setId("guidLabel");
		guidTextBox.setReadOnly(true);
		guidTextBox.setEnabled(false);
		guidTextBox.setWidth(TEXT_BOX_WIDTH);
		panel.add(guidLabel);
		panel.add(guidTextBox);
		panel.add(new SpacerWidget());
		
		guidTextBox.setText(generalInformationModel.getGuid());
		return panel;
	}
	
	

	private VerticalPanel buildMeasureNamePanel() {
		VerticalPanel measureNamePanel = new VerticalPanel();
		measureNamePanel.getElement().addClassName("generalInformationPanel");
		FormLabel measureNameLabel = buildMeasureNameLabel();
		measureNameInput.setId("measureNameInput");
		measureNameInput.setText(generalInformationModel.getMeasureName());
		measureNameInput.setWidth(TEXT_BOX_WIDTH);
		measureNamePanel.add(measureNameLabel);
		measureNamePanel.add(measureNameInput);
		return measureNamePanel;
	}
	
	private VerticalPanel buildAbbreviationPanel() {
		VerticalPanel abbreviationPanel = new VerticalPanel();
		abbreviationPanel.getElement().addClassName("generalInformationPanel");
		FormLabel abbrInputLabel = buildAbbreviationLabel();
		abbreviationPanel.add(abbrInputLabel);
		abbrInput.setId("abbrInput");
		abbrInput.setText(generalInformationModel.geteCQMAbbreviatedTitle());
		abbrInput.setWidth(TEXT_BOX_WIDTH);
		abbreviationPanel.add(abbrInput);
		return abbreviationPanel;
	}

	protected VerticalPanel buildPatientBasedPanel() {
		VerticalPanel patientBasedPanel = new VerticalPanel();
		patientBasedPanel.getElement().addClassName("generalInformationPanel");
		FormLabel patientBasedLabel = buildPatientBasedLabel();
		buildPatientBasedInput();
		patientBasedPanel.add(patientBasedLabel);
		patientBasedPanel.add(patientBasedInput);
		return patientBasedPanel;
	}
	
	private FormLabel buildPatientBasedLabel() {
		FormLabel patientBasedLabel =  new FormLabel();
		patientBasedLabel.setText("Patient-based Measure");
		patientBasedLabel.setTitle(patientBasedLabel.getText());
		patientBasedLabel.setId("patientBasedLabel");
		patientBasedLabel.setFor("patientBasedMeasure_listbox");
		return patientBasedLabel;
	}
	
	protected void buildPatientBasedInput() {
		patientBasedInput.getElement().setId("patientBasedMeasure_listbox");
		patientBasedInput.setTitle("Patient Based Indicator Required.");
		patientBasedInput.setStyleName("form-control");
		patientBasedInput.setVisibleItemCount(1);
		patientBasedInput.setWidth("18em");
		resetPatientBasedInput();
		patientBasedInput.setSelectedIndex(generalInformationModel.isPatientBased() ? 1 : 0);
	}

	public void resetPatientBasedInput() {
		patientBasedInput.clear();
		patientBasedInput.addItem("No", "No");
		patientBasedInput.addItem("Yes", "Yes");
		// default the selected index to be 1, which is yes.  				
		patientBasedInput.setSelectedIndex(1);
	}
	
	private VerticalPanel buildMeasureScoringPanel() {
		VerticalPanel measureSCoringPanel = new VerticalPanel();
		measureSCoringPanel.getElement().addClassName("generalInformationPanel");
		FormLabel measureScoringLabel = buildMeasureScoringLabel();
		buildMeasureScoringInput();
		
		measureSCoringPanel.add(measureScoringLabel);
		measureSCoringPanel.add(measureScoringInput);
		return measureSCoringPanel;
	}
	

	private FormLabel buildMeasureScoringLabel() {
		FormLabel measureScoringLabel =  new FormLabel();
		measureScoringLabel.setText("Measure Scoring");
		measureScoringLabel.setTitle(measureScoringLabel.getText());
		measureScoringLabel.setId("measureScoringLabel");
		measureScoringLabel.setFor("measScoringInput_ListBoxMVP");
		return measureScoringLabel;
	}
	
	private void buildMeasureScoringInput() {
		measureScoringInput.getElement().setId("measScoringInput_ListBoxMVP");
		measureScoringInput.setTitle("Measure Scoring Required.");
		measureScoringInput.setStyleName("form-control");
		measureScoringInput.setVisibleItemCount(1);
		measureScoringInput.setWidth("18em");
	}
	
	
	private VerticalPanel buildCompositeScoringPanel() {
		VerticalPanel compositeScoringPanel = new VerticalPanel();
		compositeScoringPanel.getElement().addClassName("generalInformationPanel");
		FormLabel compositeScoringLabel =  new FormLabel();
		compositeScoringLabel.setText("Composite Scoring");
		compositeScoringLabel.setTitle(compositeScoringLabel.getText());
		compositeScoringLabel.setId("compositeScoringLabel");
		compositeScoringLabel.setFor("compositeScoringMethodInput");
		compositeScoringPanel.add(compositeScoringLabel);
		compositeScoringPanel.add(compositeScoringMethodInput);
		return compositeScoringPanel;
	}

	private FormLabel buildAbbreviationLabel() {
		FormLabel abbrInputLabel =  new FormLabel();
		abbrInputLabel.setText("eCQM Abbreviated Title");
		abbrInputLabel.setTitle(abbrInputLabel.getText());
		abbrInputLabel.setId("eCQMAbbrTitleLabel");
		abbrInputLabel.setFor("abbrInput");
		return abbrInputLabel;
	}

	private FormLabel buildMeasureNameLabel() {
		FormLabel measureNameLabel =  new FormLabel();
		measureNameLabel.setText("Measure Name");
		measureNameLabel.setTitle(measureNameLabel.getText());
		measureNameLabel.setId("measureNameLabel");
		measureNameLabel.setFor("measureNameInput");
		return measureNameLabel;
	}
	

	@Override
	public void setReadOnly(boolean readOnly) {
		//TODO cehck all inputs
		this.readOnly = readOnly;
		abbrInput.setReadOnly(readOnly);
		abbrInput.setEnabled(!readOnly);
		measureNameInput.setReadOnly(readOnly);
		measureNameInput.setEnabled(!readOnly);
	}

	@Override
	public MeasureDetailState getState() {
		return MeasureDetailState.INCOMPLETE;
	}
	
	public ListBoxMVP getMeasureScoringInput() {
		return measureScoringInput;
	}

	public void setMeasureScoringInput(ListBoxMVP measureScoringInput) {
		this.measureScoringInput = measureScoringInput;
	}

	public ListBoxMVP getCompositeScoringMethodInput() {
		return compositeScoringMethodInput;
	}

	public void setCompositeScoringMethodInput(ListBoxMVP compositeScoringMethodInput) {
		this.compositeScoringMethodInput = compositeScoringMethodInput;
	}

	public ListBoxMVP getPatientBasedInput() {
		return patientBasedInput;
	}

	public void setPatientBasedInput(ListBoxMVP patientBasedInput) {
		this.patientBasedInput = patientBasedInput;
	}
	
	public void setScoringChoices(List<? extends HasListBox> texts) {
		MatContext.get().setListBoxItems(measureScoringInput, texts, MatContext.PLEASE_SELECT);
	}
	
	public void setCompositeScoringChoices(List<? extends HasListBox> texts) {
		MatContext.get().setListBoxItems(compositeScoringMethodInput, texts, MatContext.PLEASE_SELECT);
	}
	
	public String getCompositeScoringValue() {
		return compositeScoringMethodInput.getItemText(compositeScoringMethodInput.getSelectedIndex());
	}

	public void setObserver(GeneralMeasureInformationObserver observer) {
		this.observer = observer;
	}
	
	public void setCompositeScoringSelectedValue(String compositeScoringMethod) {
		if (CompositeMethodScoringConstant.ALL_OR_NOTHING.equals(compositeScoringMethod)) {
			getCompositeScoringMethodInput().setSelectedIndex(1);	
		} else if (CompositeMethodScoringConstant.OPPORTUNITY.equals(compositeScoringMethod)) {
			getCompositeScoringMethodInput().setSelectedIndex(2);
		} else if (CompositeMethodScoringConstant.PATIENT_LEVEL_LINEAR.equals(compositeScoringMethod)) {
			getCompositeScoringMethodInput().setSelectedIndex(3);
		} else {
			getCompositeScoringMethodInput().setSelectedIndex(0);
		}
		
		compositeScoringMethod = StringUtility.isEmptyOrNull(compositeScoringMethod) ? MatContext.PLEASE_SELECT : compositeScoringMethod;
		setScoringChoices(MatContext.get().getSelectionMap().get(compositeScoringMethod));
	}
}
