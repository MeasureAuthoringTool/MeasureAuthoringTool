package mat.client.measure;

import java.util.List;

import mat.client.measure.metadata.Grid508;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.search.HasPageSelectionHandler;
import mat.client.shared.search.HasPageSizeSelectionHandler;
import mat.client.shared.search.SearchResults;
import mat.client.shared.search.SearchView;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;


/**
 * The Class TransferMeasureOwnershipView.
 */
public class TransferMeasureOwnershipView  implements ManageMeasurePresenter.TransferDisplay {
	
	/** The container panel. */
	private ContentWithHeadingWidget containerPanel = new ContentWithHeadingWidget();
	
	/** The main panel. */
	private FlowPanel mainPanel = new FlowPanel();
	
	/** The buttons. */
	private SaveCancelButtonBar buttons = new SaveCancelButtonBar();
	
	/** The success messages. */
	protected SuccessMessageDisplay successMessages = new SuccessMessageDisplay();
	
	/** The error messages. */
	protected ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	
	/** The view. */
	private SearchView<mat.client.measure.TransferMeasureOwnerShipModel.Result> view = new SearchView<TransferMeasureOwnerShipModel.Result>("Users");
	
	/** The value set name panel. */
	HorizontalPanel valueSetNamePanel = new HorizontalPanel();
	
	/** The data table. */
	public Grid508 dataTable = view.getDataTable();
	
	/**
	 * Instantiates a new transfer measure ownership view.
	 */
	public TransferMeasureOwnershipView() {
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
		mainPanel.add(valueSetNamePanel);
		mainPanel.add(new SpacerWidget());
		mainPanel.add(view.asWidget());
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
		containerPanel.setContent(mainPanel);
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.TransferDisplay#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return containerPanel;
	}

	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.TransferDisplay#buildDataTable(mat.client.shared.search.SearchResults)
	 */
	@Override
	public void buildDataTable(SearchResults<TransferMeasureOwnerShipModel.Result> results) {
		if(results == null) {
			return;
		}
		int numRows = results.getNumberOfRows();
		int numColumns = results.getNumberOfColumns();
		dataTable.clear();
		dataTable.resize((int)numRows + 1, (int)numColumns);
		view.buildSearchResultsColumnHeaders(numRows,numColumns,results, false,false);
		buildSearchResults(numRows,numColumns,results);
        view.setViewingRange(results.getStartIndex(),results.getStartIndex() + numRows - 1,results.getResultsTotal());
		view.buildPageSizeSelector();
		
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
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.TransferDisplay#getPageSelectionTool()
	 */
	@Override
	public HasPageSelectionHandler getPageSelectionTool() {
		return view;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.TransferDisplay#getPageSizeSelectionTool()
	 */
	@Override
	public HasPageSizeSelectionHandler getPageSizeSelectionTool() {
		return view;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.TransferDisplay#getPageSize()
	 */
	@Override
	public int getPageSize() {
		return view.getPageSize();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.TransferDisplay#clearAllRadioButtons(mat.client.measure.metadata.Grid508)
	 */
	@Override
	public void clearAllRadioButtons(Grid508 dataTable){
		int rows = dataTable.getRowCount();
		int cols = dataTable.getColumnCount();
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < cols; j++){
				Widget w = getDataTable().getWidget(i, j);
				if(w instanceof RadioButton){
					RadioButton rb = ((RadioButton)w);	
					if(rb.getValue()){
						rb.setValue(false);										
					}
				}
			}
		}
	}
	
	
	/**
	 * Method to build User Results.
	 * 
	 * @param numRows
	 *            the num rows
	 * @param numColumns
	 *            the num columns
	 * @param results
	 *            the results
	 */
	protected void buildSearchResults(int numRows,int numColumns,final SearchResults<TransferMeasureOwnerShipModel.Result> results){		
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				if(results.isColumnFiresSelection(j)) {
					String innerText = results.getValue(i, j).getElement().getInnerText();
					Label a = new Label();
					a.setText(innerText);
					dataTable.setWidget(i+1, j, a);
				}
				else {
					dataTable.setWidget(i+1, j,results.getValue(i, j));
				}
			}
			if(i % 2 == 0) {
				dataTable.getRowFormatter().addStyleName(i + 1, "odd");
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.TransferDisplay#getDataTable()
	 */
	public Grid508 getDataTable() {
		return view.getDataTable();
	}
	
	/**
	 * Sets the data table.
	 * 
	 * @param dataTable
	 *            the new data table
	 */
	public void setDataTable(Grid508 dataTable) {
		this.dataTable = dataTable;
	}
	
	
}
