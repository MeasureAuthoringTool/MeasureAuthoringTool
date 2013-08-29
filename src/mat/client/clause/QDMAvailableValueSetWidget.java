package mat.client.clause;

import java.util.List;

import mat.client.CustomPager;
import mat.client.codelist.HasListBox;
import mat.client.codelist.ValueSetSearchFilterPanel;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.FocusableWidget;
import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.MatSimplePager;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SecondaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.search.HasPageSelectionHandler;
import mat.client.shared.search.HasPageSizeSelectionHandler;
import mat.client.shared.search.SearchView;
import mat.model.CodeListSearchDTO;
import mat.shared.ConstantMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

public class QDMAvailableValueSetWidget implements QDMAvailableValueSetPresenter.SearchDisplay {
	
	HorizontalPanel mainPanel = new HorizontalPanel();
	private Button searchButton = new PrimaryButton("Search","primaryGreyLeftButton");
	
	private DisclosurePanel disclosurePanel = new DisclosurePanel("QDM element with undefined value set");
	private DisclosurePanel disclosurePanelCellTable = new DisclosurePanel("QDM elements with value set");
	private TextBox searchInput = new TextBox();
	private TextBox userDefinedInput = new TextBox();
	private SearchView<CodeListSearchDTO> view = new SearchView<CodeListSearchDTO>(true);
	private CustomCheckBox specificOccurrence = new CustomCheckBox(ConstantMessages.TOOLTIP_FOR_OCCURRENCE, "Specific Occurrence",true); //US 450
	private Button addToMeasure = new PrimaryButton("Apply to Measure","primaryButton");
	private Button psuedoQDMToMeasure = new PrimaryButton("Apply to Measure", "primaryButton");
	private Button psuedoQDMCancel = new SecondaryButton("Cancel");
	private Button cancel = new Button("Close");
	private Button userDefinedCancel = new SecondaryButton("Close");
	private SimplePanel dataTypePanel = new SimplePanel();
	private ErrorMessageDisplay errorMessagePanel = new ErrorMessageDisplay();
	private SuccessMessageDisplay successMessagePanel;
	private ErrorMessageDisplay errorMessageUserDefinedPanel = new ErrorMessageDisplay();
	private SuccessMessageDisplay successMessageUserDefinedPanel = new SuccessMessageDisplay();
	private ListBoxMVP dataTypeInput = new ListBoxMVP();
	private ListBoxMVP allDataTypeInput = new ListBoxMVP();
	private FocusableWidget messageFocus;
	VerticalPanel listBoxVPanel = new VerticalPanel();
	CellTable<CodeListSearchDTO> table = new CellTable<CodeListSearchDTO>();
    private ValueSetSearchFilterPanel vssfp = new ValueSetSearchFilterPanel();
    private String cautionMsgStr = "<div style=\"padding-left:5px;\">WARNING: Changing the 'Data Type' for an applied QDM element will automatically delete invalid attributes  <br/> associated with this element in the Clause Workspace." +
    								"</div>";
    
	private  ValueChangeHandler<String> dataTypeChangeHandler = new ValueChangeHandler<String>() {
		@Override
		public void onValueChange(ValueChangeEvent<String> event) {
			specificOccurrence.setValue(false);
			String selectedValue = event.getValue();
		    if(!selectedValue.isEmpty()&& !selectedValue.equals("")){
		    	addToMeasure.setEnabled(true);
		    }
		    else{
		    	addToMeasure.setEnabled(false);
		    }
		}
	};
	
	public QDMAvailableValueSetWidget(){
		successMessagePanel = new SuccessMessageDisplay();
		successMessagePanel.clear();
		messageFocus = new FocusableWidget(successMessagePanel);
		VerticalPanel vp = new VerticalPanel();
		vp.getElement().setAttribute("id", "ModifyVerticalPanel");
		FlowPanel header = new FlowPanel();
		header.getElement().setAttribute("id", "ModifyHeaderFlowPanel");
		header.addStyleName("codeListHeader");
		Label codeListLabel = new Label("QDM");
		FocusableWidget  labelFocus = new FocusableWidget(codeListLabel);
		header.add(labelFocus);
		dataTypeInput.addValueChangeHandler(dataTypeChangeHandler);
		specificOccurrence.setEnabled(false);
		addToMeasure.setEnabled(false);
		
		vp.add(buildCellTableWidget());
		vp.add(new SpacerWidget());
		vp.add(buildUserDefinedDisclosureWidget());
		vp.add(new SpacerWidget());
		vp.add(new SpacerWidget());
		vp.add(new SpacerWidget());
		mainPanel.add(vp);
		mainPanel.getElement().setAttribute("id","ModifyMainPanel");
		MatContext.get().setModifyQDMPopUpWidget(this);
		
	}
	
	private Widget buildInitialDisabledWidget(){
		dataTypeInput.addItem(MatContext.PLEASE_SELECT);
		dataTypeInput.setEnabled(false);
		dataTypePanel.clear();
		dataTypePanel.add(buildDataTypeWidget(dataTypeInput));
		return dataTypePanel;
	}
	
	private Widget buildDataTypeWidget(ListBoxMVP listBox){
		FlowPanel fPanel = new FlowPanel();
		fPanel.getElement().setAttribute("id", "ModifyDataTypeWidgetFlowPanel");
		fPanel.addStyleName("leftAligned");
		
		fPanel.add(new SpacerWidget());
		fPanel.add(LabelBuilder.buildLabel(listBox, "Select Data Type"));
		fPanel.add(new SpacerWidget());
		HorizontalPanel hp = new HorizontalPanel();
		hp.getElement().setAttribute("id", "ModifyDataTypeWidgetHoziPanel");
		hp.add(listBox);
		hp.add(new HTML("&nbsp;&nbsp;"));
		hp.add(new HTML(cautionMsgStr));
		listBox.addFocusHandler(
				new FocusHandler() {
					@Override
					public void onFocus(FocusEvent event) {
						MatContext.get().clearDVIMessages();	
					}
				});
		
		fPanel.add(hp);
		fPanel.add(new SpacerWidget());
		return fPanel;
	}
	
	private Widget buildSpecificOccurrenceWidget(){
		FlowPanel fPanel = new FlowPanel();
		fPanel.getElement().setAttribute("id", "ModifySpecificOccWidgetFlowPanel");
		fPanel.addStyleName("leftAligned");
		fPanel.add(new SpacerWidget());		
		fPanel.add(specificOccurrence);
		return fPanel;
	}
	
	private Widget buildSearchWidget(){
		VerticalPanel vp = new VerticalPanel();
		vp.getElement().setAttribute("id", "ModifySearchInputVerPanel");
		FlowPanel fp1 = new FlowPanel();
		fp1.getElement().setAttribute("id", "ModifySearchInputFlowPanel");
		fp1.add(searchInput);
		searchInput.addFocusHandler(
				new FocusHandler() {
					@Override
					public void onFocus(FocusEvent event) {
						MatContext.get().clearDVIMessages();
					}
				});
		
		fp1.add(searchButton);
		searchButton.addFocusHandler(
				new FocusHandler() {
					@Override
					public void onFocus(FocusEvent event) {
						MatContext.get().clearDVIMessages();
					}
				});
		fp1.add(new SpacerWidget());
		vp.add(fp1);
		vp.setStylePrimaryName("marginLeft");
		return vp;
	}
	
	private Widget buildUserDefinedDisclosureWidget(){
		HorizontalPanel horiPanel = new HorizontalPanel();
		VerticalPanel valueSetPanel = new VerticalPanel();
		VerticalPanel dataTypePanel = new VerticalPanel();
		
		Widget widgetValueSet =LabelBuilder.buildRequiredLabel(userDefinedInput, "Value Set Name");
		valueSetPanel.add(widgetValueSet);
		valueSetPanel.add(new SpacerWidget());
		userDefinedInput.setWidth("230px");
		userDefinedInput.setMaxLength(255);
		valueSetPanel.add(userDefinedInput);
		
		Widget widgetDataType = LabelBuilder.buildLabel("Select Data Type", "Select Data Type");
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
		psuedoQDMCancel.setTitle("Cancel");
		buttonHorizontalPanel.add(psuedoQDMCancel);
		buttonHorizontalPanel.add(new SpacerWidget());
		userDefinedCancel.setTitle("Close");
		buttonHorizontalPanel.add(userDefinedCancel);
		
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
	
	private Widget buildCellTableWidget(){
		FlowPanel searchCriteriaPanel = new FlowPanel();
		searchCriteriaPanel.getElement().setAttribute("id", "ModifySearchCriteriaPanel");
		searchCriteriaPanel.add(new SpacerWidget());
		searchCriteriaPanel.addStyleName("leftAligned");
		Widget searchWidget = buildSearchWidget();
		Widget label = LabelBuilder.buildLabel(searchInput, "Search for a Value Set");
		searchInput.setHeight("18px");
		searchCriteriaPanel.add(label);
		HorizontalPanel searchHorizontalPanel = new HorizontalPanel();
		searchCriteriaPanel.getElement().setAttribute("id", "ModifySearchHorizontalPanel");
		searchCriteriaPanel.add(new SpacerWidget());
		searchHorizontalPanel.add(vssfp.getPanel());
		searchHorizontalPanel.add(searchWidget);
		searchCriteriaPanel.add(searchHorizontalPanel);
		searchCriteriaPanel.add(new SpacerWidget());
		searchCriteriaPanel.add(new SpacerWidget());
		
		searchCriteriaPanel.add(new SpacerWidget());
		searchCriteriaPanel.add(errorMessagePanel);
		searchCriteriaPanel.add(view.asWidget());
		searchCriteriaPanel.add(messageFocus);
		searchCriteriaPanel.add(buildInitialDisabledWidget());
		searchCriteriaPanel.add(buildSpecificOccurrenceWidget());
		searchCriteriaPanel.add(new SpacerWidget());
		
		HorizontalPanel buttonLayout = new HorizontalPanel();
		buttonLayout.getElement().setAttribute("id", "ModifyButtonLayout");
		buttonLayout.setStylePrimaryName("myAccountButtonLayout");
		addToMeasure.setTitle("Apply to Measure");
		cancel.setTitle("Close");
		cancel.setStyleName("rightAlignSecondaryButton");
		buttonLayout.add(addToMeasure);
		buttonLayout.add(cancel);
		searchCriteriaPanel.add(buttonLayout);
		
		disclosurePanelCellTable.add(searchCriteriaPanel);
		disclosurePanelCellTable.setOpen(true);
		return disclosurePanelCellTable;
		
	}
	
	
	@Override
	public HasClickHandlers getSearchButton() {
		return searchButton;
	}
	@Override	
	public HasPageSelectionHandler getPageSelectionTool() {
		return view;
	}
	@Override
	public HasPageSizeSelectionHandler getPageSizeSelectionTool() {
		return view;
	}
	@Override
	public HasSelectionHandlers<CodeListSearchDTO> getSelectIdForQDSElement() {
		return view;
	}

	
	@Override
	public ListBoxMVP getDataTypeInput(){
		return dataTypeInput;
	}
	
	
	@Override
	public CustomCheckBox getSpecificOccurrenceInput(){
		return specificOccurrence;
	}
	

	@Override
	public HasValue<String> getSearchString() {
		return searchInput;
	}


	@Override
	public int getPageSize() {
		return view.getPageSize();
	}


	@Override
	public HasClickHandlers getAddToMeasureButton() {
		return  addToMeasure; 
	}


	@Override
	public Widget getDataTypeWidget() {
		return dataTypePanel;
	}

    
	@Override
	public FocusableWidget getMsgFocusWidget(){
		return messageFocus;
	}
	
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessagePanel;
	}

	
	@Override
	public void scrollToBottom(){
	//	sp.scrollToBottom();
	}
    
	
	
	@Override
	public String getDataTypeValue() {
		if(dataTypeInput.getSelectedIndex() >= 0) {
			return dataTypeInput.getValue(dataTypeInput.getSelectedIndex());
		}
		else {
			return "";
		}
	}
	
	@Override
	public String getDataTypeText() {
		if(dataTypeInput.getSelectedIndex() >= 0) {
			return dataTypeInput.getItemText(dataTypeInput.getSelectedIndex());
		}
		else {
			return "";
		}
	}
	
	public void setListBoxItems(ListBox listBox, List<? extends HasListBox> itemList, String defaultOption){
		listBox.clear();
		listBox.addItem(defaultOption,"");
		if(itemList != null){
			for(HasListBox listBoxContent : itemList){
				listBox.addItem(listBoxContent.getItem(),"" +listBoxContent.getValue());
			}
		}
	}

	@Override
	public void setDataTypeOptions(List<? extends HasListBox> texts) {
		setListBoxItems(dataTypeInput, texts, MatContext.PLEASE_SELECT);
	}
	
	@Override
	public void setAllDataTypeOptions(List<? extends HasListBox> texts) {
		setListBoxItems(allDataTypeInput, texts, MatContext.PLEASE_SELECT);
	}


	@Override
	public SuccessMessageDisplayInterface getApplyToMeasureSuccessMsg() {
		return successMessagePanel;
	}


	@Override
	public void setAddToMeasureButtonEnabled(boolean enabled) {
		addToMeasure.setEnabled(enabled);
	}

	@Override
    public Button getApplyToMeasure(){
    	return addToMeasure;
    }
	
	@Override
	public HasSelectionHandlers<CodeListSearchDTO> getSelectedOption() {
		return view;
	}

	@Override
	public ValueSetSearchFilterPanel getValueSetSearchFilterPanel() {
		return vssfp;
	}
	@Override
	public void setEnabled(boolean enabled){
		//search button
		searchButton.setEnabled(enabled);
		//search text
		searchInput.setEnabled(enabled);
		//search list
		getValueSetSearchFilterPanel().setEnabled(enabled);
		//data type radios
		view.setEnabled(enabled);
	}

	
	public void buildTableQDS( QDSCodeListSearchModel results,Boolean isTableEnabled){
		 table = new CellTable<CodeListSearchDTO>();
		table.getElement().setAttribute("id", "ModifyAvailableValueSetTable");
		table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		ListDataProvider<CodeListSearchDTO> sortProvider = new ListDataProvider<CodeListSearchDTO>();
		  
		// Display 50 rows in one page or all records.
		table.setPageSize(10);
		table.setSelectionModel(results.addSelectionHandlerOnTable());
		table = results.addColumnToTable(table,isTableEnabled);
		
		table.redraw();
		sortProvider.refresh();
		sortProvider.setList(results.getData());
	
		sortProvider.addDataDisplay(table);
		//Used custom pager class - for disabling next/last button when on last page and for showing correct pagination number.
		MatSimplePager spager;
		CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
	    spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true);
        spager.setDisplay(table);
        spager.setPageStart(0);
        spager.setToolTipAndTabIndex(spager);
        view.getvPanelForQDMTable().clear();
        view.getvPanelForQDMTable().add(table);
		view.getvPanelForQDMTable().add(new SpacerWidget());
		view.getvPanelForQDMTable().add(spager);

	}

	public HorizontalPanel getMainPanel() {
		return mainPanel;
	}

	public SearchView<CodeListSearchDTO> getView() {
		return view;
	}
	
	public SuccessMessageDisplay getSuccessMessagePanel(){
		return successMessagePanel;
	}
	
	public ErrorMessageDisplay getErrorMessagePanel(){
		return errorMessagePanel;
	}

	public FocusableWidget getMessageFocus() {
		return messageFocus;
	}
	@Override
	public Button getCancel() {
		return cancel;
	}
	
	public CellTable<CodeListSearchDTO> getTable() {
		return table;
	}

	@Override
	public Widget asWidget() {
		return getMainPanel();
	}

	public TextBox getUserDefinedInput() {
		return userDefinedInput;
	}

	public void setUserDefinedInput(TextBox userDefinedInput) {
		this.userDefinedInput = userDefinedInput;
	}

	public DisclosurePanel getDisclosurePanel() {
		return disclosurePanel;
	}

	public void setDisclosurePanel(DisclosurePanel disclosurePanel) {
		this.disclosurePanel = disclosurePanel;
	}

	public Button getPsuedoQDMToMeasure() {
		return psuedoQDMToMeasure;
	}

	public void setPsuedoQDMToMeasure(Button psuedoQDMToMeasure) {
		this.psuedoQDMToMeasure = psuedoQDMToMeasure;
	}

	public Button getPsuedoQDMCancel() {
		return psuedoQDMCancel;
	}

	public void setPsuedoQDMCancel(Button psuedoQDMCancel) {
		this.psuedoQDMCancel = psuedoQDMCancel;
	}

	public ListBoxMVP getAllDataTypeInput() {
		return allDataTypeInput;
	}

	public void setAllDataTypeInput(ListBoxMVP allDataTypeInput) {
		this.allDataTypeInput = allDataTypeInput;
	}

	public DisclosurePanel getDisclosurePanelCellTable() {
		return disclosurePanelCellTable;
	}

	public void setDisclosurePanelCellTable(DisclosurePanel disclosurePanelCellTable) {
		this.disclosurePanelCellTable = disclosurePanelCellTable;
	}

	@Override
	public void buildQDSDataTable(QDSCodeListSearchModel results,Boolean isTableEnabled) {
		buildTableQDS(results,isTableEnabled);
		
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

	public Button getUserDefinedCancel() {
		return userDefinedCancel;
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

}
