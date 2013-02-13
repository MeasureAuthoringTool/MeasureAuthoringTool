package mat.client.clause;

import java.util.List;

import mat.client.codelist.HasListBox;
import mat.client.codelist.ValueSetSearchFilterPanel;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.FocusableWidget;
import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.search.HasPageSelectionHandler;
import mat.client.shared.search.HasPageSizeSelectionHandler;
import mat.client.shared.search.SearchResults;
import mat.client.shared.search.SearchView;
import mat.model.CodeListSearchDTO;
import mat.shared.ConstantMessages;

import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class QDSCodeListSearchView  implements QDSCodeListSearchPresenter.SearchDisplay {

	private SimplePanel containerPanel = new SimplePanel();
	private Button searchButton = new PrimaryButton("Search");
	private TextBox searchInput = new TextBox();
	private SearchView<CodeListSearchDTO> view = new SearchView<CodeListSearchDTO>(true);
	private CustomCheckBox specificOccurrence = new CustomCheckBox(ConstantMessages.TOOLTIP_FOR_OCCURRENCE, "Specific Occurrence"); //US 450
	private Button addToMeasure = new PrimaryButton("Apply to Measure");
	private SimplePanel dataTypePanel = new SimplePanel();
	private ErrorMessageDisplay errorMessagePanel = new ErrorMessageDisplay();
	private SuccessMessageDisplay successMessagePanel;
	private ListBoxMVP dataTypeInput = new ListBoxMVP();
	private ListBox appliedQDM = new ListBox();
	private FocusableWidget messageFocus;
	private Button removeButton = new Button("Remove");
   // private ScrollPanel sp;
    
    private ValueSetSearchFilterPanel vssfp = new ValueSetSearchFilterPanel();
	
    private  ValueChangeHandler<String> dataTypeChangeHandler = new ValueChangeHandler<String>() {
		
		@Override
		public void onValueChange(ValueChangeEvent<String> event) {
			specificOccurrence.setValue(false);
			String selectedValue = event.getValue();
		    if(!selectedValue.isEmpty()&& !selectedValue.equals(""))
		    	addToMeasure.setEnabled(true);
		    else
		    	addToMeasure.setEnabled(false);
		}
	};
    
	public SuccessMessageDisplay getSuccessMessagePanel(){
		return successMessagePanel;
	}
	
	public ErrorMessageDisplay getErrorMessagePanel(){
		return errorMessagePanel;
	}
	
	public QDSCodeListSearchView() {
		
		successMessagePanel = new SuccessMessageDisplay();
		successMessagePanel.clear();
		messageFocus = new FocusableWidget(successMessagePanel);
		VerticalPanel vp = new VerticalPanel();
		FlowPanel header = new FlowPanel();
		header.addStyleName("codeListHeader");
		Label codeListLabel = new Label("Value Sets");
		FocusableWidget  labelFocus = new FocusableWidget(codeListLabel);
		header.add(labelFocus);
		dataTypeInput.addValueChangeHandler(dataTypeChangeHandler);
		specificOccurrence.setEnabled(false);
		addToMeasure.setEnabled(false);
		view.buildQDSDataTable(new QDSCodeListSearchModel());
		FlowPanel searchCriteriaPanel = new FlowPanel();
		searchCriteriaPanel.add(new SpacerWidget());
		searchCriteriaPanel.addStyleName("leftAligned");
		Widget searchWidget = buildSearchWidget();
		Widget label = LabelBuilder.buildLabel(searchInput, "Search for a Value Set");
		searchCriteriaPanel.add(label);
		searchCriteriaPanel.add(errorMessagePanel);
		searchCriteriaPanel.add(vssfp.getPanel());
		searchCriteriaPanel.add(new SpacerWidget());
		searchCriteriaPanel.add(searchWidget);
		searchCriteriaPanel.add(new SpacerWidget());
		searchCriteriaPanel.add(new SpacerWidget());
		
		VerticalPanel hp = new VerticalPanel();
		//hp.setStylePrimaryName("qdmListBox");
		appliedQDM.setVisibleItemCount(20);
		appliedQDM.setWidth("200px");
		hp.add(appliedQDM);
		
		searchCriteriaPanel.add(view.asWidget());
		searchCriteriaPanel.add(messageFocus);
		searchCriteriaPanel.add(buildInitialDisabledWidget());
		searchCriteriaPanel.add(buildSpecificOccurrenceWidget());
		searchCriteriaPanel.add(new SpacerWidget());
		searchCriteriaPanel.add(addToMeasure);
		//sp = new ScrollPanel(searchCriteriaPanel);
		//sp.setHeight("200px");
		
		vp.add(SkipListBuilder.buildEmbeddedLinkHolder("ClauseWorkspace"));
		vp.add(header);
		//vp.add(sp);
		vp.add(searchCriteriaPanel);
		vp.add(new SpacerWidget());
		
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.add(vp);
		hp.setStyleName("qdmListBox");
		hPanel.add(hp);
		hp.add(new SpacerWidget());
		hp.add(new SpacerWidget());
		hp.add(removeButton);
		containerPanel.add(hPanel);
		//containerPanel.add(vp);
		
		containerPanel.setStyleName("qdsContentPanel");
		
		MatContext.get().setQDSView(this);
	}
	
	
	private Widget buildInitialDisabledWidget(){
		dataTypeInput.addItem(MatContext.PLEASE_SELECT);
		dataTypeInput.setEnabled(false);
		dataTypePanel.clear();
		dataTypePanel.add(buildDataTypeWidget());
		return dataTypePanel;
	}
	
	private Widget buildDataTypeWidget(){
		FlowPanel fPanel = new FlowPanel();
		fPanel.addStyleName("leftAligned");
		fPanel.add(new SpacerWidget());
		fPanel.add(LabelBuilder.buildLabel(dataTypeInput, "Select Data Type"));
		fPanel.add(dataTypeInput);
		dataTypeInput.addFocusHandler(
				new FocusHandler() {
					@Override
					public void onFocus(FocusEvent event) {
						MatContext.get().clearDVIMessages();	
					}
				});

		fPanel.add(new SpacerWidget());
		return fPanel;
	}
	
	private Widget buildSpecificOccurrenceWidget(){
		FlowPanel fPanel = new FlowPanel();
		fPanel.addStyleName("leftAligned");
		fPanel.add(new SpacerWidget());		
		fPanel.add(specificOccurrence);
		return fPanel;
	}
	
	private Widget buildSearchWidget(){
		HorizontalPanel hp = new HorizontalPanel();
		SimplePanel sp1 = new SimplePanel();
		sp1.addStyleName("codeListLink");
		sp1.add(searchInput);
		searchInput.addFocusHandler(
				new FocusHandler() {
					@Override
					public void onFocus(FocusEvent event) {
						MatContext.get().clearDVIMessages();
					}
				});
		
		SimplePanel sp2 = new SimplePanel();
		sp2.add(searchButton);
		searchButton.addFocusHandler(
				new FocusHandler() {
					@Override
					public void onFocus(FocusEvent event) {
						MatContext.get().clearDVIMessages();
					}
				});
		hp.add(sp1);
		hp.add(sp2);
		return hp;
	}
	
	
	@Override
	public Widget asWidget() {
		return containerPanel;
	}


	
	@Override
	public HasClickHandlers getSearchButton() {
		return searchButton;
	}
	
	@Override
	public void buildQDSDataTable(SearchResults<CodeListSearchDTO> results) {
		view.buildQDSDataTable(results);
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

	private void setListBoxItems(ListBox listBox, List<? extends HasListBox> itemList, String defaultOption){
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
}
