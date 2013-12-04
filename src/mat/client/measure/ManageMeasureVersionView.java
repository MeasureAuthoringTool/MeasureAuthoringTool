/**
 * 
 */
package mat.client.measure;

import java.util.ArrayList;
import java.util.List;

import mat.client.CustomPager;
import mat.client.ImageResources;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.shared.CustomButton;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatSimplePager;
import mat.client.shared.RadioButtonCell;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SearchWidget;
import mat.client.shared.SpacerWidget;
import mat.client.shared.search.SearchResults;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * The Class ManageMeasureVersionView.
 * 
 * @author vandavar
 * 
 *         An view class to manage the widgets for the Version creation.
 */
public class ManageMeasureVersionView implements ManageMeasurePresenter.VersionDisplay {
	
	/** The button bar. */
	private SaveCancelButtonBar buttonBar = new SaveCancelButtonBar();
	
	/** The error messages. */
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	
	/** The main panel. */
	private FlowPanel mainPanel = new FlowPanel();
	
	/** The major radio. */
	private RadioButton majorRadio = new RadioButton("group","Major");
	
	/** The minor radio. */
	private RadioButton minorRadio = new RadioButton("group","Minor");
	
	/** The measure search filter widget. */
	private SearchWidget searchWidget = new SearchWidget();
	
	/** Zoom Button for Showing Search Widget. */
	private CustomButton zoomButton = (CustomButton) getImage("Search",
			ImageResources.INSTANCE.search_zoom(), "Search");
	
	/** The cell table panel. */
	private VerticalPanel cellTablePanel = new VerticalPanel(); 
	
	/** The selection model. */
	private SingleSelectionModel<Result> selectionModel;
	
	/**
	 * Instantiates a new manage measure version view.
	 */
	public ManageMeasureVersionView() {
		zoomButton.getElement().getStyle().setMarginLeft(30, Unit.PX);
		mainPanel.setStylePrimaryName("contentPanel");
		mainPanel.addStyleName("leftAligned");
		mainPanel.add(searchWidget);
		mainPanel.add(new SpacerWidget());
				
		cellTablePanel.getElement().setId("cellTablePanel_VerticalPanel");	
		cellTablePanel.setWidth("77%");
		mainPanel.add(cellTablePanel);
		mainPanel.add(new SpacerWidget());
		
		mainPanel.add(errorMessages);
		
		VerticalPanel radioPanel = new VerticalPanel();
		radioPanel.getElement().getStyle().setMarginLeft(5, Unit.PX);
		radioPanel.add(new Label("Select Version Type"));
		radioPanel.add(new SpacerWidget());
		radioPanel.add(majorRadio);
		radioPanel.add(minorRadio);		
		mainPanel.add(radioPanel);
		mainPanel.add(new SpacerWidget());
		
		SimplePanel buttonPanel = new SimplePanel();
		buttonBar.getSaveButton().setText("Save and Continue");
		buttonBar.getSaveButton().setTitle("Save and Continue");
		buttonBar.getCancelButton().setTitle("Cancel");
		buttonPanel.add(buttonBar);
		buttonPanel.setWidth("100%");
		mainPanel.add(buttonPanel);
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.BaseDisplay#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return mainPanel;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.VersionDisplay#buildDataTable(mat.client.shared.search.SearchResults)
	 */
	@Override
	public void buildDataTable(SearchResults<Result> results) {
		cellTablePanel.clear();
		cellTablePanel.setStyleName("cellTablePanel");
		Label cellTablePanelHeader = new Label("Select a Draft to create a Measure Version.");
		cellTablePanelHeader.getElement().setId("cellTablePanelHeader_Label");
		cellTablePanelHeader.setStyleName("recentSearchHeader");
		cellTablePanelHeader.getElement().setAttribute("tabIndex", "0");
		cellTablePanel.add(cellTablePanelHeader);
		cellTablePanel.add(new SpacerWidget());
		
		CellTable<ManageMeasureSearchModel.Result> cellTable = new CellTable<ManageMeasureSearchModel.Result>();
		ListDataProvider<ManageMeasureSearchModel.Result> sortProvider = new ListDataProvider<ManageMeasureSearchModel.Result>();
		
		List<Result> measureList = new ArrayList<Result>();
		measureList.addAll(((ManageVersionMeasureModel)results).getDataList());	
		cellTable.setPageSize(25);
		cellTable.redraw();
		cellTable.setRowCount(measureList.size(), true);
		cellTable.setSelectionModel(getSelectionModelWithHandler());
		sortProvider.refresh();
		sortProvider.getList().addAll(((ManageVersionMeasureModel)results).getDataList());
		cellTable = addColumnToTable(cellTable);
		sortProvider.addDataDisplay(cellTable);
		CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
		MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true);	
		spager.setPageStart(0);
		spager.setDisplay(cellTable);
        spager.setPageSize(25);
        spager.setToolTipAndTabIndex(spager);
        cellTable.setWidth("100%");
        cellTable.setColumnWidth(0, 15.0, Unit.PCT);
        cellTable.setColumnWidth(1, 63.0, Unit.PCT);
        cellTable.setColumnWidth(2, 22.0, Unit.PCT);
        cellTablePanel.add(cellTable);
        cellTablePanel.add(new SpacerWidget());
        cellTablePanel.add(spager);
	}
	
	
	/**
	 * Gets the selection model with handler.
	 *
	 * @return the selection model with handler
	 */
	private SingleSelectionModel<Result> getSelectionModelWithHandler() {
		selectionModel = new SingleSelectionModel<Result>();
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				getErrorMessageDisplay().clear();
			}
		});
		return selectionModel;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.VersionDisplay#getSelectedMeasure()
	 */
	@Override
	public Result getSelectedMeasure() {
		return selectionModel.getSelectedObject();
	}

	/** Adds the column to table.
	 * 
	 * @param cellTable the cell table
	 * @return the cell table */
	private CellTable<Result> addColumnToTable(final CellTable<Result> cellTable) {
		Column<Result, Boolean> radioButtonColumn = new Column<Result, Boolean>(new RadioButtonCell(true, true)) {
			@Override
			public Boolean getValue(Result result) {
				return cellTable.getSelectionModel().isSelected(result);
			}
		};
		radioButtonColumn.setFieldUpdater(new FieldUpdater<Result, Boolean>() {
			@Override
			public void update(int index, Result object, Boolean value) {
				cellTable.getSelectionModel().setSelected(object, true);
			}
		});
		cellTable.addColumn(radioButtonColumn, SafeHtmlUtils.fromSafeConstant("<span title='Select' tabindex=\"0\">" +"Select"+ "</span>"));
		
		Column<Result, SafeHtml> measureNameColumn = new Column<Result, SafeHtml>(new SafeHtmlCell()) {			
			@Override
			public SafeHtml getValue(Result object) {
				return getColumnValueWithToolTip("Measure Name", object.getName());
			}
		};
		cellTable.addColumn(measureNameColumn, SafeHtmlUtils.fromSafeConstant("<span title='Measure Name' tabindex=\"0\">" +"Measure Name"+ "</span>"));
		
		Column<Result, SafeHtml> versionColumn = new Column<Result, SafeHtml>(new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(Result object) {
				return getColumnValueWithToolTip("Version", object.getVersion());
			}
		};
		cellTable.addColumn(versionColumn, SafeHtmlUtils.fromSafeConstant("<span title='Version' tabindex=\"0\">" +"Version"+ "</span>"));
		
		return cellTable;
	}

	/**
	 * Gets the column value with tool tip.
	 *
	 * @param columnName the column name
	 * @param columnValue the column value
	 * @return the column value with tool tip
	 */
	private SafeHtml getColumnValueWithToolTip(String columnName, String columnValue){
		String htmlConstant = "<span tabindex=\"0\" title='" + columnName + ": " + columnValue + "'>"+ columnValue + "</span>";
		return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.VersionDisplay#getCancelButton()
	 */
	@Override
	public HasClickHandlers getCancelButton() {
		return buttonBar.getCancelButton();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.measure.ManageMeasurePresenter.BaseDisplay#getErrorMessageDisplay()
	 */
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}
	
	/** Add Image on Button with invisible text. This text will be available when css is turned off.
	 * @param action - {@link String}
	 * @param url - {@link ImageResource}.
	 * @param key - {@link String}.
	 * @return - {@link Widget}. */
	private Widget getImage(String action, ImageResource url, String key) {
		CustomButton image = new CustomButton();
		image.removeStyleName("gwt-button");
		image.setStylePrimaryName("invisibleButtonTextMeasureLibrary");
		image.setTitle(action);
		image.setResource(url, action);
		return image;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.VersionDisplay#getMajorRadioButton()
	 */
	@Override
	public RadioButton getMajorRadioButton() {
		return majorRadio;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.VersionDisplay#getMinorRadioButton()
	 */
	@Override
	public RadioButton getMinorRadioButton() {
		return minorRadio;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.VersionDisplay#getSaveButton()
	 */
	@Override
	public HasClickHandlers getSaveButton() {
		return buttonBar.getSaveButton();
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.VersionDisplay#getSearchButton()
	 */
	@Override
	public HasClickHandlers getSearchButton() {
		return searchWidget.getSearchButton();
	}

	/** Gets the measure search filter widget.
	 * 
	 * @return the searchWidget */
	@Override
	public SearchWidget getSearchWidget() {
		return searchWidget;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.VersionDisplay#getZoomButton()
	 */
	@Override
	public CustomButton getZoomButton() {
		return zoomButton;
	}	
}
