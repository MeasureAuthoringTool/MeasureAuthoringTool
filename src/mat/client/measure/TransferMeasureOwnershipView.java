package mat.client.measure;

import java.util.ArrayList;
import java.util.List;

import mat.client.CustomPager;
import mat.client.measure.TransferMeasureOwnerShipModel.Result;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatSimplePager;
import mat.client.shared.PrimaryButton;
import mat.client.shared.RadioButtonCell;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.util.CellTableUtility;
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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;


// TODO: Auto-generated Javadoc
/**
 * The Class TransferMeasureOwnershipView.
 */
public class TransferMeasureOwnershipView  implements ManageMeasurePresenter.TransferDisplay {
	
	
	
	/** The main panel. */
	private FlowPanel mainPanel = new FlowPanel();
	
	/** The buttons. */
	private SaveCancelButtonBar buttons = new SaveCancelButtonBar("transferMOwnership");
	
	/** The success messages. */
	protected SuccessMessageDisplay successMessages = new SuccessMessageDisplay();
	
	/** The error messages. */
	protected ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	
	/** The view. */
	/*private SearchView<mat.client.measure.TransferMeasureOwnerShipModel.Result> view = new 
			SearchView<TransferMeasureOwnerShipModel.Result>("Users");*/
	
	/** The value set name panel. */
	HorizontalPanel valueSetNamePanel = new HorizontalPanel();
	
	/** The table. */
	private CellTable<TransferMeasureOwnerShipModel.Result> table;
	
	 /** The selection model. */
 	private SingleSelectionModel<TransferMeasureOwnerShipModel.Result> selectionModel;
	
	/** The cell table panel. */
	VerticalPanel cellTablePanel = new VerticalPanel();
	
	/** The search button. */
	private Button searchButton = new PrimaryButton("Search", "primaryGreyLeftButton");
	/** The search input. */
	private TextBox searchInput = new TextBox();
	
	/** The selected measure list. */
	private ArrayList<TransferMeasureOwnerShipModel.Result> selectedMeasureList;
	
	/** The Constant PAGE_SIZE. */
	private static final int PAGE_SIZE = 25;
	
	/**
	 * Instantiates a new transfer measure ownership view.
	 */
	public TransferMeasureOwnershipView() {
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
		mainPanel.add(valueSetNamePanel);
		mainPanel.add(new SpacerWidget());
		//mainPanel.add(view.asWidget());
		mainPanel.add(cellTablePanel);
		mainPanel.setStyleName("contentPanel");
		HorizontalPanel hp = new HorizontalPanel();
		hp.getElement().setId("hp_HorizontalPanel");
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
		mainPanel.add(successMessages);
		mainPanel.add(errorMessages);
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
	@Override
	public Widget asWidget() {
		return mainPanel;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.TransferDisplay#buildHTMLForMeasures(java.util.List)
	 */
	@Override
	public void buildHTMLForMeasures(List<ManageMeasureSearchModel.Result> measureList){
		valueSetNamePanel.clear();
		StringBuilder paragraph = new StringBuilder("<p><b>Measure Names :</b>");
		for(int i=0;i<measureList.size();i++){
			paragraph.append(measureList.get(i).getName());
			if(i < measureList.size()-1){
				paragraph.append(",");
			}
		}
		paragraph.append("</p>");
		HTML paragraphHtml = new HTML(paragraph.toString());
		valueSetNamePanel.add(paragraphHtml);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.TransferDisplay#getSaveButton()
	 */
	@Override
	public HasClickHandlers getSaveButton() {
		return buttons.getSaveButton();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.TransferDisplay#getCancelButton()
	 */
	@Override
	public HasClickHandlers getCancelButton() {
		
		return buttons.getCancelButton();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.TransferDisplay#getSelectedValue()
	 */
	@Override
	public String getSelectedValue() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.TransferDisplay#getErrorMessageDisplay()
	 */
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.TransferDisplay#getSuccessMessageDisplay()
	 */
	@Override
	public SuccessMessageDisplayInterface getSuccessMessageDisplay() {
		return successMessages;
	}
	
	
     /**
      * Adds the column to table.
      *
      * @return the cell table
      */
     private CellTable<TransferMeasureOwnerShipModel.Result> addColumnToTable() {
    	 
    	   Label searchHeader = new Label("Active MAT User List");
			searchHeader.getElement().setId("searchHeader_Label");
			searchHeader.setStyleName("recentSearchHeader");
			searchHeader.getElement().setAttribute("tabIndex", "0");
			com.google.gwt.dom.client.TableElement elem = table.getElement().cast();
			TableCaptionElement caption = elem.createCaption();
			caption.appendChild(searchHeader.getElement());
			selectionModel = new SingleSelectionModel<TransferMeasureOwnerShipModel.Result>();
			table.setSelectionModel(selectionModel);
			Column<TransferMeasureOwnerShipModel.Result,SafeHtml> userName = new 
					Column<TransferMeasureOwnerShipModel.Result,SafeHtml>(new SafeHtmlCell()){
				@Override
				public SafeHtml getValue(Result object) {
					return CellTableUtility.getColumnToolTip(object.getFirstName() + " " + object.getLastName(), 
							"Name "+object.getFirstName() + " " + object.getLastName());
					}
				};
				table.addColumn(userName,SafeHtmlUtils.fromSafeConstant(
						"<span title='Name'>" + "Name" + "</span>"));
		   
		    Column<TransferMeasureOwnerShipModel.Result,SafeHtml> emailAddress = new 
		    		Column<TransferMeasureOwnerShipModel.Result,SafeHtml>(new SafeHtmlCell()){
		    	@Override
		    	public SafeHtml getValue(Result object) {
		    		return CellTableUtility.getColumnToolTip(object.getEmailId(), "Email Address "+object.getEmailId());
		    		}
		    	};
		    	table.addColumn(emailAddress,SafeHtmlUtils.fromSafeConstant(
		    			"<span title='Email Address'>" + "Email Address" + "</span>"));
				 
		    RadioButtonCell radioButtonCell = new RadioButtonCell(true, true);
		    Column<TransferMeasureOwnerShipModel.Result,Boolean> selectUser = new 
		    		Column<TransferMeasureOwnerShipModel.Result,Boolean>(radioButtonCell){
		    	@Override
		    	public Boolean getValue(Result object) {
		    		return selectionModel.isSelected(object);
		    		}
		    	};
		    	
		    	selectUser.setFieldUpdater(new FieldUpdater<TransferMeasureOwnerShipModel.Result, Boolean>() {
					
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
	@Override
	public void buildCellTable(TransferMeasureOwnerShipModel results) {
		cellTablePanel.clear();
		table = new CellTable<TransferMeasureOwnerShipModel.Result>();
		table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		ListDataProvider<TransferMeasureOwnerShipModel.Result> sortProvider = new ListDataProvider<TransferMeasureOwnerShipModel.Result>();
		selectedMeasureList = new ArrayList<TransferMeasureOwnerShipModel.Result>();
		selectedMeasureList.addAll(results.getData());
		table.setPageSize(PAGE_SIZE);
		table.redraw();
		table.setRowCount(selectedMeasureList.size(), true);
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
		cellTablePanel.add(buildSearchWidget());
		cellTablePanel.add(new SpacerWidget());
		cellTablePanel.add(table);
		cellTablePanel.add(new SpacerWidget());
		cellTablePanel.add(spager);
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.TransferDisplay#clearRadioButtons()
	 */
	@Override
	public void clearRadioButtons(){
		List<Result> displayedItems = new ArrayList<Result>();
		displayedItems.addAll(selectedMeasureList);
		for (TransferMeasureOwnerShipModel.Result msg : displayedItems) {
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
		FlowPanel fp1 = new FlowPanel();
		fp1.add(searchInput);
		searchButton.setTitle("Search");
		fp1.add(searchButton);
		fp1.add(new SpacerWidget());
		hp.add(fp1);
		return hp;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.TransferDisplay#getSearchButton()
	 */
	@Override
	public HasClickHandlers getSearchButton() {
		return searchButton;
	}
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.AdminSearchDisplay#getSearchString()
	 */
	@Override
	public HasValue<String> getSearchString() {
		return searchInput;
	}
	
	
}
