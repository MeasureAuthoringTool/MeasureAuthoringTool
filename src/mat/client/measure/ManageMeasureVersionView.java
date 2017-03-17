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
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatSimplePager;
import mat.client.shared.RadioButtonCell;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SearchWidget;
import mat.client.shared.SpacerWidget;
import mat.client.shared.search.SearchResults;
import mat.client.util.CellTableUtility;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
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
	
	/** CellTable Page Size. */
	private static final int PAGE_SIZE = 25;
	
	/** The button bar. */
	private SaveCancelButtonBar buttonBar = new SaveCancelButtonBar("measuVersion");
	
	/** The cell table panel. */
	private VerticalPanel cellTablePanel = new VerticalPanel();
	
	/** The error messages. */
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	
	/** The main panel. */
	private FlowPanel mainPanel = new FlowPanel();
	
	/** The major radio. */
	private RadioButton majorRadio = new RadioButton("group", "Major");
	
	/** The minor radio. */
	private RadioButton minorRadio = new RadioButton("group", "Minor");
	
	/** The measure search filter widget. */
	private SearchWidget searchWidget = new SearchWidget("Search", 
            "Search", "searchWidget");
	
	/** The selection model. */
	private SingleSelectionModel<Result> selectionModel;
	
	/** Zoom Button for Showing Search Widget. */
	private CustomButton zoomButton = (CustomButton) getImage("Search",
			ImageResources.INSTANCE.search_zoom(), "Search");
	
	/**
	 * Instantiates a new manage measure version view.
	 */
	public ManageMeasureVersionView() {
		zoomButton.getElement().getStyle().setMarginLeft(30, Unit.PX);
		zoomButton.getElement().setId("zoomButton_CustomButton");
		mainPanel.setStylePrimaryName("contentPanel");
		mainPanel.addStyleName("leftAligned");
		//searchWidget.getSearchInput().setHeight("20px");
		mainPanel.add(searchWidget);		
		mainPanel.add(new SpacerWidget());
		
		cellTablePanel.getElement().setId("cellTablePanel_VerticalPanel");
		cellTablePanel.setWidth("99%");
		mainPanel.add(cellTablePanel);
		mainPanel.add(new SpacerWidget());
		
		mainPanel.add(errorMessages);
		errorMessages.getElement().setId("errorMessages_ErrorMessageDisplay");
		
		VerticalPanel radioPanel = new VerticalPanel();
		radioPanel.getElement().getStyle().setMarginLeft(5, Unit.PX);
		radioPanel.add(new Label("Select Version Type"));
		radioPanel.add(new SpacerWidget());
		radioPanel.add(majorRadio);
		majorRadio.getElement().setId("majorRadio_RadioButton");
		radioPanel.add(minorRadio);
		minorRadio.getElement().setId("minorRadio_RadioButton");
	
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
	
	/** Adds the column to table.
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
		cellTable.addColumn(radioButtonColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Select\">"
				+ "Select" + "</span>"));
		Column<Result, SafeHtml> measureNameColumn = new Column<Result, SafeHtml>(new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(Result object) {
				String title = "Measure Name " + object.getName();
				return CellTableUtility.getColumnToolTip(object.getName(), title);
			}
		};
		cellTable.addColumn(measureNameColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Measure Name\">"
				+ "Measure Name" + "</span>"));
		Column<Result, SafeHtml> versionColumn = new Column<Result, SafeHtml>(new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(Result object) {
				String title = "Version " + object.getVersion();
				return CellTableUtility.getColumnToolTip(object.getVersion(), title);
			}
		};
		cellTable.addColumn(versionColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Version\">"
				+ "Version" + "</span>"));
		return cellTable;
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
		CellTable<ManageMeasureSearchModel.Result> cellTable = new CellTable<ManageMeasureSearchModel.Result>();
		ListDataProvider<ManageMeasureSearchModel.Result> sortProvider = new ListDataProvider<ManageMeasureSearchModel.Result>();
		List<Result> measureList = new ArrayList<Result>();
		measureList.addAll(((ManageVersionMeasureModel) results).getDataList());
		cellTable.setPageSize(PAGE_SIZE);
		cellTable.redraw();
		cellTable.setRowCount(measureList.size(), true);
		cellTable.setSelectionModel(getSelectionModelWithHandler());
		sortProvider.refresh();
		sortProvider.getList().addAll(((ManageVersionMeasureModel) results).getDataList());
		cellTable = addColumnToTable(cellTable);
		sortProvider.addDataDisplay(cellTable);
		CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
		MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true,"measureVersion");
		spager.setPageStart(0);
		spager.setDisplay(cellTable);
		spager.setPageSize(PAGE_SIZE);
		/* spager.setToolTipAndTabIndex(spager); */
		cellTable.setWidth("100%");
		cellTable.setColumnWidth(0, 15.0, Unit.PCT);
		cellTable.setColumnWidth(1, 63.0, Unit.PCT);
		cellTable.setColumnWidth(2, 22.0, Unit.PCT);
		com.google.gwt.dom.client.TableElement elem = cellTable.getElement().cast();
		TableCaptionElement caption = elem.createCaption();
		caption.appendChild(cellTablePanelHeader.getElement());
		Label invisibleLabel = (Label) LabelBuilder
				.buildInvisibleLabel(
						"measureVersionSummary",
						"In the following Measure version of draft table, a radio button is positioned to the left "
								+ "of the table with a select column header followed by Measure name in "
								+ "second column and version in the third column. The draft Measures "
								+ "are listed alphabetically in a table.");
		cellTable.getElement().setAttribute("id", "measureVersionFromDraftCellTable");
		cellTable.getElement().setAttribute("aria-describedby", "measureVersionSummary");
		cellTablePanel.add(invisibleLabel);
		cellTablePanel.add(cellTable);
		cellTablePanel.add(new SpacerWidget());
		cellTablePanel.add(spager);
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
		image.getElement().setAttribute("id", "MeasureSearchButton");
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
	 * @see mat.client.measure.ManageMeasurePresenter.VersionDisplay#getSelectedMeasure()
	 */
	@Override
	public Result getSelectedMeasure() {
		return selectionModel.getSelectedObject();
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
	 * @see mat.client.measure.ManageMeasurePresenter.VersionDisplay#getZoomButton()
	 */
	@Override
	public CustomButton getZoomButton() {
		return zoomButton;
	}
}
