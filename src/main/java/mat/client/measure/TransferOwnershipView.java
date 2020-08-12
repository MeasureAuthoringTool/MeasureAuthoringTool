package mat.client.measure;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import mat.client.CustomPager;
import mat.client.buttons.SaveContinueCancelButtonBar;
import mat.client.measure.TransferOwnerShipModel.Result;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatSimplePager;
import mat.client.shared.MessageAlert;
import mat.client.shared.RadioButtonCell;
import mat.client.shared.SearchWidgetBootStrap;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageAlert;
import mat.client.util.CellTableUtility;
import mat.model.cql.CQLLibraryDataSetObject;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class TransferOwnershipView.
 */
public class TransferOwnershipView {
	
	/** The main panel. */
	private FlowPanel mainPanel = new FlowPanel();
	
	/** The buttons. */
	private SaveContinueCancelButtonBar buttons = new SaveContinueCancelButtonBar("transferMOwnership");
	
	/** The success messages. */
	protected MessageAlert successMessages = new SuccessMessageAlert();
	
	/** The CQL error message. */
	private MessageAlert errorMessageAlert = new ErrorMessageAlert();
	
	/** The value set name panel. */
	HorizontalPanel TransferObjectNamePanel = new HorizontalPanel();
	
	/** The table. */
	private CellTable<TransferOwnerShipModel.Result> table;
	
	 /** The selection model. */
 	private SingleSelectionModel<TransferOwnerShipModel.Result> selectionModel;
	
	/** The cell table panel. */
	VerticalPanel cellTablePanel = new VerticalPanel();
	
	SearchWidgetBootStrap searchWidgetBootStrap = new SearchWidgetBootStrap("Search", "Search");
	
	/** The selected measure list. */
	private ArrayList<TransferOwnerShipModel.Result> selectedUserList;
	
	/** The Constant PAGE_SIZE. */
	private static final int PAGE_SIZE = 25;
	
	/**
	 * Instantiates a new transfer measure ownership view.
	 */
	public TransferOwnershipView() {
		mainPanel.add(new SpacerWidget());
		mainPanel.add(buildSearchWidget());
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
		mainPanel.add(TransferObjectNamePanel);
		mainPanel.add(new SpacerWidget());
		mainPanel.add(cellTablePanel);
		mainPanel.setStyleName("contentPanel");
		HorizontalPanel hp = new HorizontalPanel();
		hp.getElement().setId("hp_HorizontalPanel");
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
		mainPanel.add(successMessages);
		mainPanel.add(errorMessageAlert);
		mainPanel.add(new SpacerWidget());
		buttons.getCancelButton().setTitle("Cancel");
		buttons.getCancelButton().setText("Cancel");
		buttons.getSaveButton().setTitle("Save");
		hp.add(buttons);
		mainPanel.add(hp);
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.TransferDisplay#asWidget()
	 */
	public Widget asWidget() {
		return mainPanel;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.TransferDisplay#buildHTMLForMeasures(java.util.List)
	 */
	public void buildHTMLForMeasures(List<ManageMeasureSearchModel.Result> measureList){
		TransferObjectNamePanel.clear();
		StringBuilder paragraph = new StringBuilder("<p><b>Measure Names :</b>");
		for(int i=0;i<measureList.size();i++){
			paragraph.append(measureList.get(i).getName());
			if(i < measureList.size()-1){
				paragraph.append(",");
			}
		}
		paragraph.append("</p>");
		HTML paragraphHtml = new HTML(paragraph.toString());
		TransferObjectNamePanel.add(paragraphHtml);
	}
	
	public void buildHTMLForLibraries(List<CQLLibraryDataSetObject> measureList){
		TransferObjectNamePanel.clear();
		StringBuilder paragraph = new StringBuilder("<p><b>CQL Library Names :</b>");
		for(int i=0;i<measureList.size();i++){
			paragraph.append(measureList.get(i).getCqlName());
			if(i < measureList.size()-1){
				paragraph.append(",");
			}
		}
		paragraph.append("</p>");
		HTML paragraphHtml = new HTML(paragraph.toString());
		TransferObjectNamePanel.add(paragraphHtml);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.TransferDisplay#getSaveButton()
	 */
	public HasClickHandlers getSaveButton() {
		return buttons.getSaveButton();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.TransferDisplay#getCancelButton()
	 */
	public HasClickHandlers getCancelButton() {
		
		return buttons.getCancelButton();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.TransferDisplay#getSelectedValue()
	 */
	public String getSelectedValue() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.TransferDisplay#getErrorMessageDisplay()
	 */
	public MessageAlert getErrorMessageDisplay() {
		return errorMessageAlert;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.TransferDisplay#getSuccessMessageDisplay()
	 */
	public MessageAlert getSuccessMessageDisplay() {
		return successMessages;
	}
	
     /**
      * Adds the column to table.
      *
      * @return the cell table
      */
     private CellTable<TransferOwnerShipModel.Result> addColumnToTable() {
    	 
    	   Label searchHeader = new Label("Active MAT User List");
			searchHeader.getElement().setId("searchHeader_Label");
			searchHeader.setStyleName("recentSearchHeader");
			searchHeader.getElement().setAttribute("tabIndex", "0");
			com.google.gwt.dom.client.TableElement elem = table.getElement().cast();
			TableCaptionElement caption = elem.createCaption();
			caption.appendChild(searchHeader.getElement());
			selectionModel = new SingleSelectionModel<TransferOwnerShipModel.Result>();
			table.setSelectionModel(selectionModel);
			Column<TransferOwnerShipModel.Result,SafeHtml> userName = new 
					Column<TransferOwnerShipModel.Result,SafeHtml>(new SafeHtmlCell()){
				@Override
				public SafeHtml getValue(Result object) {
					return CellTableUtility.getColumnToolTip(object.getFirstName() + " " + object.getLastName(), 
							"Name "+object.getFirstName() + " " + object.getLastName());
					}
				};
				table.addColumn(userName,SafeHtmlUtils.fromSafeConstant(
						"<span title='Name'>" + "Name" + "</span>"));
		   
		    Column<TransferOwnerShipModel.Result,SafeHtml> emailAddress = new 
		    		Column<TransferOwnerShipModel.Result,SafeHtml>(new SafeHtmlCell()){
		    	@Override
		    	public SafeHtml getValue(Result object) {
		    		return CellTableUtility.getColumnToolTip(object.getEmailId(), "Email Address "+object.getEmailId());
		    		}
		    	};
		    	table.addColumn(emailAddress,SafeHtmlUtils.fromSafeConstant(
		    			"<span title='Email Address'>" + "Email Address" + "</span>"));
				 
		    RadioButtonCell radioButtonCell = new RadioButtonCell(true, true);
		    Column<TransferOwnerShipModel.Result,Boolean> selectUser = new 
		    		Column<TransferOwnerShipModel.Result,Boolean>(radioButtonCell){
		    	@Override
		    	public Boolean getValue(Result object) {
		    		return selectionModel.isSelected(object);
		    		}
		    	};
		    	
		    	selectUser.setFieldUpdater(new FieldUpdater<TransferOwnerShipModel.Result, Boolean>() {
					
					@Override
					public void update(int index, Result object, Boolean value) {
						selectionModel.setSelected(object, value);
						object.setSelected(selectionModel.isSelected(object));
					}
				});	
		    	table.addColumn(selectUser, SafeHtmlUtils.fromSafeConstant(
							"<span title='Select'>" + "Select" + "</span>"));
		
		return table;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.TransferDisplay#buildCellTable(mat.client.measure.TransferMeasureOwnerShipModel)
	 */
	//@Override
	public void buildCellTable(TransferOwnerShipModel results) {
		cellTablePanel.clear();
		table = new CellTable<TransferOwnerShipModel.Result>();
		table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		ListDataProvider<TransferOwnerShipModel.Result> sortProvider = new ListDataProvider<TransferOwnerShipModel.Result>();
		selectedUserList = new ArrayList<TransferOwnerShipModel.Result>();
		selectedUserList.addAll(results.getData());
		table.setPageSize(PAGE_SIZE);
		table.redraw();
		table.setRowCount(selectedUserList.size(), true);
		sortProvider.refresh();
		sortProvider.getList().addAll(results.getData());
		table = addColumnToTable();
		sortProvider.addDataDisplay(table);
		CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
		MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true,"MeasureOwnerShipTransfer");
		spager.setPageStart(0);
		spager.setDisplay(table);
		spager.setPageSize(PAGE_SIZE);
		table.setWidth("100%");
		table.setColumnWidth(0, 45.0, Unit.PCT);
		table.setColumnWidth(1, 45.0, Unit.PCT);
		table.setColumnWidth(2, 10.0, Unit.PCT);
		Label invisibleLabel = (Label) LabelBuilder
				.buildInvisibleLabel(
						"activeMATUsersListSummary",
						"In the following Active MAT User List table, Name is given in first column,"
								+ " Email Address in second column and Select in third column.");
		table.getElement().setAttribute("id", "UserCellTable");
		table.getElement().setAttribute("aria-describedby", "activeMATUsersListSummary");
		cellTablePanel.setWidth("100%");
		cellTablePanel.add(invisibleLabel);
		cellTablePanel.add(new SpacerWidget());
		cellTablePanel.add(table);
		cellTablePanel.add(new SpacerWidget());
		cellTablePanel.add(spager);
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.TransferDisplay#clearRadioButtons()
	 */
	public void clearRadioButtons(){
		List<Result> displayedItems = new ArrayList<Result>();
		displayedItems.addAll(selectedUserList);
		for (TransferOwnerShipModel.Result msg : displayedItems) {
			selectionModel.setSelected(msg, false);
		}
	}
	
	/**
	 * Builds the search widget.
	 *
	 * @return the widget
	 */
	private Widget buildSearchWidget() {
		HorizontalPanel hp = new HorizontalPanel();
		searchWidgetBootStrap.getSearchBox().setWidth("232px");
		hp.add(searchWidgetBootStrap.getSearchWidget());
		hp.getElement().setAttribute("style", "width:100px;margin-top:20px;");
		return hp;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.TransferDisplay#getSearchButton()
	 */
	public HasClickHandlers getSearchButton() {
		return searchWidgetBootStrap.getGo();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.AdminSearchDisplay#getSearchString()
	 */
	public HasValue<String> getSearchString() {
		return searchWidgetBootStrap.getSearchBox();
	}
}
