package mat.client.clause;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import mat.client.CustomPager;
import mat.client.codelist.HasListBox;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.shared.DateBoxWithCalendar;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.MatSimplePager;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SecondaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.model.MatValueSet;
import mat.shared.ConstantMessages;

import org.apache.commons.lang.StringUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;


public class QDSCodeListSearchView  implements QDSCodeListSearchPresenter.SearchDisplay {

	private SimplePanel containerPanel = new SimplePanel();
	private DisclosurePanel disclosurePanel = new DisclosurePanel("Element without VSAC value set");
	private DisclosurePanel disclosurePanelCellTable = new DisclosurePanel("Element with VSAC value set");
	private TextBox userDefinedInput = new TextBox();
	private Button psuedoQDMToMeasure = new PrimaryButton("Apply to Measure", "primaryButton");
	private SimplePanel dataTypePanel = new SimplePanel();
	private ErrorMessageDisplay errorMessagePanel = new ErrorMessageDisplay();
	private SuccessMessageDisplay successMessagePanel = new SuccessMessageDisplay();	
	private ErrorMessageDisplay errorMessageUserDefinedPanel = new ErrorMessageDisplay();
	private SuccessMessageDisplay successMessageUserDefinedPanel = new SuccessMessageDisplay();
	private ListBoxMVP allDataTypeInput = new ListBoxMVP();
	private TextBox oidInput = new TextBox();	
	private DateBoxWithCalendar versionInput = new DateBoxWithCalendar(DateTimeFormat.getFormat("yyyyMMdd"));
	private Button retrieveButton = new PrimaryButton("Search","primaryMetaDataButton");
	private ListBoxMVP dataTypesListBox = new ListBoxMVP();
	private Button applyToMeasureButton = new PrimaryButton("Apply to Measure","primaryButton");
	private Button cancelButton = new SecondaryButton("Cancel");
	private CustomCheckBox specificOccurrence = new CustomCheckBox(ConstantMessages.TOOLTIP_FOR_OCCURRENCE, "Specific Occurrence",true); //US 450
	private VerticalPanel valueSetDetailsPanel = new VerticalPanel();
	MatValueSet currentMatValueSet;
	
	private  ValueChangeHandler<String> dataTypeChangeHandler = new ValueChangeHandler<String>() {
		@Override
		public void onValueChange(ValueChangeEvent<String> event) {
			specificOccurrence.setValue(false);
			String selectedValue = event.getValue();
		    if(!selectedValue.isEmpty()&& !selectedValue.equals("")){
		    	applyToMeasureButton.setEnabled(true);
		    }
		    else{
		    	applyToMeasureButton.setEnabled(false);
		    }
		    
		    ListBoxMVP listbox = (ListBoxMVP)event.getSource();
		    if(listbox.getItemText(listbox.getSelectedIndex()).equalsIgnoreCase(ConstantMessages.ATTRIBUTE)) {
		    	specificOccurrence.setValue(false);
		    	specificOccurrence.setEnabled(false);
		    }
		    else {
		    	specificOccurrence.setEnabled(true);
		    }
		}
	};
		
	public QDSCodeListSearchView() {
		VerticalPanel vp = new VerticalPanel();
		vp.getElement().setId("vp_VerticalPanel");
		vp.setWidth("100%");	
		vp.add(buildElementWithVSACValueSetWidget());
		vp.add(new SpacerWidget());
		vp.add(buildUserDefinedDisclosureWidget());
		vp.add(new SpacerWidget());
		vp.add(new SpacerWidget());
		vp.add(new SpacerWidget());		
		HorizontalPanel mainPanel = new HorizontalPanel();
		mainPanel.getElement().setId("mainPanel_HorizontalPanel");
		mainPanel.setWidth("100%");		
		mainPanel.add(vp);
		containerPanel.getElement().setId("containerPanel_SimplePanel");
		containerPanel.setWidth("100%");
		containerPanel.add(mainPanel);
		containerPanel.setStyleName("qdsContentPanel");
		MatContext.get().setQDSView(this);		
		valueSetDetailsPanel.setVisible(false);
	}	
	
	private Widget buildUserDefinedDisclosureWidget(){
		HorizontalPanel horiPanel = new HorizontalPanel();
		VerticalPanel valueSetPanel = new VerticalPanel();
		VerticalPanel dataTypePanel = new VerticalPanel();
		
		Widget widgetValueSet =LabelBuilder.buildLabel(userDefinedInput, "Name");
		valueSetPanel.add(widgetValueSet);
		valueSetPanel.add(new SpacerWidget());
		userDefinedInput.setWidth("230px");
		userDefinedInput.setMaxLength(255);
		valueSetPanel.add(userDefinedInput);
		
		Widget widgetDataType = LabelBuilder.buildLabel("Select Datatype", "Select Datatype");
		dataTypePanel.add(widgetDataType);
		dataTypePanel.add(new SpacerWidget());
		dataTypePanel.add(allDataTypeInput);
		allDataTypeInput.addFocusHandler(
				new FocusHandler() {
					@Override
					public void onFocus(FocusEvent event) {
						MatContext.get().clearDVIMessages();	
					}
		});
		dataTypePanel.setStyleName("marginLeftRight");
		horiPanel.add(valueSetPanel);
		horiPanel.add(dataTypePanel);
		
		HorizontalPanel buttonHorizontalPanel = new HorizontalPanel();
		psuedoQDMToMeasure.setTitle("Apply to Measure");
		buttonHorizontalPanel.add(psuedoQDMToMeasure);
		buttonHorizontalPanel.add(new SpacerWidget());
				
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.add(horiPanel);
		mainPanel.add(new SpacerWidget());
		mainPanel.add(successMessageUserDefinedPanel);
		mainPanel.add(errorMessageUserDefinedPanel);
		mainPanel.add(new SpacerWidget());
		mainPanel.add(buttonHorizontalPanel);
		mainPanel.add(new SpacerWidget());
		disclosurePanel.add(mainPanel);
		return disclosurePanel;
	}
	
	private Widget buildElementWithVSACValueSetWidget(){
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.getElement().setId("mainPanel_VerticalPanel");
		mainPanel.setWidth("100%");		
		mainPanel.add(successMessagePanel);
		mainPanel.add(errorMessagePanel);		
		mainPanel.add(buildSearchPanel());		
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());			
		valueSetDetailsPanel.getElement().setId("valueSetDetailsPanel_VerticalPanel");
		valueSetDetailsPanel.setStyleName("valueSetDetailsPanel");
		valueSetDetailsPanel.setWidth("95%");
		mainPanel.add(valueSetDetailsPanel);				
		disclosurePanelCellTable.setWidth("100%");
		disclosurePanelCellTable.add(mainPanel);
		disclosurePanelCellTable.setOpen(true);
		return disclosurePanelCellTable;		
	}
	
	private Widget buildSearchPanel() {
		VerticalPanel searchPanel = new VerticalPanel();
		searchPanel.getElement().setId("searchPanel_VerticalPanel");
		searchPanel.setStyleName("valueSetSearchPanel");
		Label searchHeader = new Label("Search");
		searchHeader.getElement().setId("searchHeader_Label");
		searchHeader.setStyleName("valueSetHeader");
		searchHeader.getElement().setAttribute("tabIndex", "0");
		searchPanel.add(searchHeader);
		searchPanel.add(new SpacerWidget());		
		oidInput.getElement().setId("oidInput_TextBox");
		oidInput.getElement().setAttribute("tabIndex", "0");
		oidInput.setTitle("Enter OID");
		oidInput.setWidth("300px");
		oidInput.setMaxLength(200);		
		versionInput.getElement().setId("versionInput_DateBoxWithCalendar");
		versionInput.setTitle("Enter version");
		versionInput.getElement().setAttribute("tabIndex", "0");
		retrieveButton.getElement().setId("retrieveButton_Button");
		retrieveButton.getElement().setAttribute("tabIndex", "0");
		retrieveButton.setTitle("Search");		
		Grid queryGrid = new Grid(3,2);
		queryGrid.setWidget(0, 0, LabelBuilder.buildRequiredLabel(new Label(), "OID:"));
		queryGrid.setWidget(0, 1, oidInput);
		queryGrid.setWidget(1, 0, LabelBuilder.buildLabel(new Label(), "Version (Optional):"));
		queryGrid.setWidget(1, 1, versionInput);
		queryGrid.setWidget(2, 0, retrieveButton);
		queryGrid.setStyleName("secondLabel");
		searchPanel.add(queryGrid);				
		return searchPanel;
	}

	public void buildValueSetDetailsWidget(ArrayList<MatValueSet> matValueSets) {		
		MatValueSet matValueSet = matValueSets.get(0);
		currentMatValueSet = matValueSet;
		
		valueSetDetailsPanel.clear();
		valueSetDetailsPanel.add(createDetailsWidget(matValueSet));
		if(matValueSet.isGrouping()) {
			valueSetDetailsPanel.add(new SpacerWidget());
			valueSetDetailsPanel.add(createGroupingMembersCellTable(matValueSet));
		}
		valueSetDetailsPanel.add(new SpacerWidget());
		valueSetDetailsPanel.add(new SpacerWidget());
		valueSetDetailsPanel.add(new SpacerWidget());
		valueSetDetailsPanel.add(createDataTypeWidget());
	}
	
	private Widget createGroupingMembersCellTable(MatValueSet matValueSet) {
		List<MatValueSet> groupedMatValueSets = matValueSet.getGroupedValueSet(); 
		
		CellTable<MatValueSet> groupingValueSetTable = new CellTable<MatValueSet>();
		groupingValueSetTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		groupingValueSetTable.getElement().setAttribute("tabIndex", "0");
		groupingValueSetTable.addStyleName("valueSetMarginLeft_7px");
		groupingValueSetTable.addStyleName("valueSetMarginTop");
		groupingValueSetTable.setPageSize(4);
		groupingValueSetTable.redraw();
		
		TextColumn<MatValueSet> valuesetNameColumn = new TextColumn<MatValueSet>() {
			@Override
			public String getValue(MatValueSet object) {				
				return object.getDisplayName();
			}
		};
		groupingValueSetTable.addColumn(valuesetNameColumn, "Value Set Name");
		
		TextColumn<MatValueSet> oidColumn = new TextColumn<MatValueSet>() {
			@Override
			public String getValue(MatValueSet object) {				
				return object.getID();
			}
		};
		groupingValueSetTable.addColumn(oidColumn, "OID");
		
		TextColumn<MatValueSet> codeSystemColumn = new TextColumn<MatValueSet>() {
			@Override
			public String getValue(MatValueSet object) {
				return object.getCodeSystemName();
			}
		};
		groupingValueSetTable.addColumn(codeSystemColumn, "CodeSystem");
		
		ListDataProvider<MatValueSet> listDataProvider = new ListDataProvider<MatValueSet>();	
		listDataProvider.refresh();
		listDataProvider.getList().addAll(groupedMatValueSets);
		listDataProvider.addDataDisplay(groupingValueSetTable);
		
		VerticalPanel groupingPanel = new VerticalPanel();
		groupingPanel.getElement().setId("groupingPanel_VerticalPanel");
		groupingPanel.setWidth("100%");
		Label groupingHeader = new Label("Grouping value set");
		groupingHeader.getElement().setId("groupingHeader_Label");
		groupingHeader.getElement().setAttribute("tabIndex", "0");
		groupingHeader.setStyleName("valueSetHeader");
		groupingHeader.setWidth("150px");
		groupingPanel.add(groupingHeader);
		groupingPanel.add(groupingValueSetTable);		
				
		CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
		MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true);
		spager.addStyleName("valueSetMarginLeft_7px");
        spager.setDisplay(groupingValueSetTable);
        spager.setPageStart(0);
        spager.setToolTipAndTabIndex(spager);
        groupingPanel.add(spager);
        
		return groupingPanel;
	}

	private Widget createDetailsWidget(MatValueSet matValueSet) {
		VerticalPanel detailsPanel = new VerticalPanel();
		detailsPanel.getElement().setId("detailsPanel_VerticalPanel");
		detailsPanel.setWidth("100%");
		
		Label detailsHeader = new Label("Value set details");
		detailsHeader.getElement().setId("detailsHeader_Label");
		detailsHeader.getElement().setAttribute("tabIndex", "0");
		detailsHeader.setStyleName("valueSetHeader");
		detailsPanel.add(detailsHeader);
		
		Grid details = new Grid(6,3);
		details.setCellSpacing(5);
		details.getColumnFormatter().setWidth(0, "35%");
		details.getColumnFormatter().setWidth(1, "35%");
		details.getColumnFormatter().setWidth(2, "30%");
		details.getRowFormatter().addStyleName(0, "bold");
		details.getRowFormatter().addStyleName(2, "bold");
		details.getRowFormatter().addStyleName(4, "bold");
		details.getRowFormatter().setVerticalAlign(1, HasVerticalAlignment.ALIGN_TOP);
		details.getRowFormatter().setVerticalAlign(3, HasVerticalAlignment.ALIGN_TOP);
		details.getRowFormatter().setVerticalAlign(5, HasVerticalAlignment.ALIGN_TOP);
		
		details.setWidget(0, 0, createHTML("Name:", "Name", null));
		details.setWidget(1, 0, createHTML(matValueSet.getDisplayName(), "NameValue", null));
		details.setWidget(0, 1, createHTML("OID:", "oid", "valueSetMarginLeft"));
		details.setWidget(1, 1, createHTML(matValueSet.getID(), "oidValue", "valueSetMarginLeft"));
		details.setWidget(0, 2, createHTML("Code System:", "CodeSystem", "valueSetMarginLeft"));
		details.setWidget(1, 2, createHTML(getCodeSystem(matValueSet), "CodeSystemValue", "valueSetMarginLeft"));
		details.setWidget(2, 0, createHTML("Type:", "Type", "valueSetMarginTop"));
		details.setWidget(3, 0, createHTML(matValueSet.getType(), "TypeValue", null));
		details.setWidget(2, 1, createHTML("Version:", "Version", "valueSetMarginLeft,valueSetMarginTop"));
		details.setWidget(3, 1, createHTML(matValueSet.getVersion(),"VersionValue", "valueSetMarginLeft"));
		details.setWidget(4, 0, createHTML("Developer:", "Developer", "valueSetMarginTop"));
		details.setWidget(5, 0, createHTML(matValueSet.getSource(), "DeveloperValue", null));
		details.setWidget(4, 1, createHTML("Status:", "Status", "valueSetMarginLeft,valueSetMarginTop"));
		details.setWidget(5, 1, createHTML(matValueSet.getStatus(), "StatusValue", "valueSetMarginLeft"));
		detailsPanel.add(details);
				
		return detailsPanel;
	}
	
	private String getCodeSystem(MatValueSet matValueSet) {		
		if(matValueSet.isGrouping()) {
			String codeSystem = StringUtils.EMPTY;
			List<MatValueSet> groupedMatValueSets = matValueSet.getGroupedValueSet(); 
			if(groupedMatValueSets!=null) {
				ListIterator<MatValueSet> itr = groupedMatValueSets.listIterator();
				while(itr.hasNext()) {
					MatValueSet groupedMatValueSet = itr.next();
					codeSystem += groupedMatValueSet.getCodeSystemName();
					if(itr.hasNext()) {
						codeSystem += ", ";
					}
				}				
			}		
			return codeSystem;
		}
		else {
			return matValueSet.getCodeSystemName();
		}		
	}
	
	private HTML createHTML(String value, String id, String styles) {
		HTML html = new HTML(value);		
		if(id!=null && !id.trim().isEmpty()) {
			html.getElement().setId(id);
		} else {
			html.getElement().setId(value);
		}		
		if(styles!=null && !styles.trim().isEmpty()) {
			String[] stylesArray = styles.split(",");
			for(String style : stylesArray) {
				html.addStyleName(style.trim());
			}
		}
		html.setHeight("100%");
		html.setTitle(value);
		html.getElement().setAttribute("tabIndex", "0");
		return html;
	}

	private Widget createDataTypeWidget() {
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.getElement().setId("vPanel_VerticalPanel");
		vPanel.addStyleName("valueSetMarginLeft_7px");
		vPanel.add(LabelBuilder.buildLabel(new Label(), "Select Datatype"));
		dataTypesListBox.getElement().setId("dataTypesListBox_ListBox");
		dataTypesListBox.setTitle("Select Datatype");
		dataTypesListBox.setSelectedIndex(0);
		dataTypesListBox.addValueChangeHandler(dataTypeChangeHandler);
		vPanel.add(dataTypesListBox);
		vPanel.add(new SpacerWidget());
		specificOccurrence.getElement().setId("specificOccurrence_CheckBox");
		specificOccurrence.setValue(false);
		vPanel.add(specificOccurrence);
		vPanel.add(new SpacerWidget());
		vPanel.add(new SpacerWidget());
		HorizontalPanel buttonsPanel = new HorizontalPanel();	
		buttonsPanel.getElement().setId("buttonsPanel_HorizontalPanel");
		applyToMeasureButton.getElement().setId("applyToMeasureButton_Button");
		applyToMeasureButton.addStyleName("firstLabel");
		applyToMeasureButton.setTitle("Apply To Measure");
		applyToMeasureButton.setEnabled(false);
		buttonsPanel.add(applyToMeasureButton);
		cancelButton.getElement().setId("cancelButton_Button");
		cancelButton.setTitle("Cancel");
		cancelButton.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				resetVSACValueSetWidget();
				clearVSACValueSetMessages();
			}
		});
		buttonsPanel.add(cancelButton);
		
		boolean editable = MatContext.get().getMeasureLockService().checkForEditPermission();
		dataTypesListBox.setEnabled(editable);
		specificOccurrence.setEnabled(editable);
		
		vPanel.add(buttonsPanel);
		return vPanel;
	}

	@Override
	public Widget asWidget() {
		return containerPanel;
	}
	
	@Override
	public CustomCheckBox getSpecificOccurrenceInput(){
		return specificOccurrence;
	}
	
	@Override
	public Widget getDataTypeWidget() {
		return dataTypePanel;
	}
	
	@Override
	public ErrorMessageDisplay getErrorMessageDisplay() {
		return errorMessagePanel;
	}
	
	@Override
	public SuccessMessageDisplay getSuccessMessageDisplay() {
		return successMessagePanel;
	}

	@Override
	public void clearVSACValueSetMessages() {
		getSuccessMessageDisplay().clear();
		getErrorMessageDisplay().clear();
	}
		
	@Override
	public String getDataTypeValue(ListBoxMVP inputListBox) {
		if(inputListBox.getSelectedIndex() >= 0) {
			return inputListBox.getValue(inputListBox.getSelectedIndex());
		}
		else {
			return "";
		}
	}
	
	@Override
	public String getDataTypeText(ListBoxMVP inputListBox) {
		if(inputListBox.getSelectedIndex() >= 0) {
			return inputListBox.getItemText(inputListBox.getSelectedIndex());
		}
		else {
			return "";
		}
	}

	private void setListBoxItems(ListBox listBox, List<? extends HasListBox> itemList, String defaultOption){
		listBox.clear();
		listBox.addItem(defaultOption,"");
		if(itemList != null){
			for(HasListBox listBoxContent : itemList){
				listBox.addItem(listBoxContent.getItem(),"" +listBoxContent.getValue());
			}
		}
	}	
	
	public DisclosurePanel getDisclosurePanel() {
		return disclosurePanel;
	}

	public void setDisclosurePanel(DisclosurePanel disclosurePanel) {
		this.disclosurePanel = disclosurePanel;
	}

	public DisclosurePanel getDisclosurePanelCellTable() {
		return disclosurePanelCellTable;
	}

	public void setDisclosurePanelCellTable(DisclosurePanel disclosurePanelCellTable) {
		this.disclosurePanelCellTable = disclosurePanelCellTable;
	}

	public TextBox getUserDefinedInput() {
		return userDefinedInput;
	}

	public void setUserDefinedInput(TextBox userDefinedInput) {
		this.userDefinedInput = userDefinedInput;
	}

	public Button getPsuedoQDMToMeasure() {
		return psuedoQDMToMeasure;
	}

	public void setPsuedoQDMToMeasure(Button psuedoQDMToMeasure) {
		this.psuedoQDMToMeasure = psuedoQDMToMeasure;
	}

	public ListBoxMVP getAllDataTypeInput() {
		return allDataTypeInput;
	}

	public void setAllDataTypeInput(ListBoxMVP allDataTypeInput) {
		this.allDataTypeInput = allDataTypeInput;
	}

	public ErrorMessageDisplay getErrorMessageUserDefinedPanel() {
		return errorMessageUserDefinedPanel;
	}

	public void setErrorMessageUserDefinedPanel(
			ErrorMessageDisplay errorMessageUserDefinedPanel) {
		this.errorMessageUserDefinedPanel = errorMessageUserDefinedPanel;
	}

	public SuccessMessageDisplay getSuccessMessageUserDefinedPanel() {
		return successMessageUserDefinedPanel;
	}

	public void setSuccessMessageUserDefinedPanel(
			SuccessMessageDisplay successMessageUserDefinedPanel) {
		this.successMessageUserDefinedPanel = successMessageUserDefinedPanel;
	}
		
	@Override
	public void resetVSACValueSetWidget() {
		getOIDInput().setValue(StringUtils.EMPTY);
		getVersionInput().setValue(StringUtils.EMPTY);
		getValueSetDetailsPanel().setVisible(false);
	}

	@Override
	public void setAllDataTypeOptions(List<? extends HasListBox> texts) {
		setListBoxItems(allDataTypeInput, texts, MatContext.PLEASE_SELECT);
	}
	
	@Override
	public void setDataTypesListBoxOptions(List<? extends HasListBox> texts) {
		setListBoxItems(dataTypesListBox, texts, MatContext.PLEASE_SELECT);
	}
	
	@Override
	public TextBox getOIDInput() {
		return oidInput;
	}

	@Override
	public DateBoxWithCalendar getVersionInput() {
		return versionInput;
	}

	public void setVersionInput(DateBoxWithCalendar versionInput) {
		this.versionInput = versionInput;
	}
	
	@Override
	public Button getRetrieveButton() {
		return retrieveButton;
	}

	@Override
	public VerticalPanel getValueSetDetailsPanel() {
		return valueSetDetailsPanel;
	}

	@Override
	public ListBoxMVP getDataTypesListBox() {
		return dataTypesListBox;
	}

	@Override
	public Button getApplyToMeasureButton() {
		return applyToMeasureButton;
	}
	
	@Override
	public MatValueSet getCurrentMatValueSet() {
		return currentMatValueSet;
	}	
}
