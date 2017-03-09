package mat.client.cql;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import mat.client.CqlLibraryPresenter;
import mat.client.CustomPager;
import mat.client.ImageResources;
import mat.client.measure.service.SaveCQLLibraryResult;
import mat.client.shared.CustomButton;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatSimplePager;
import mat.client.shared.RadioButtonCell;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SearchWidget;
import mat.client.shared.SpacerWidget;
import mat.client.util.CellTableUtility;
import mat.model.cql.CQLLibraryDataSetObject;

public class CQLLibraryDraftView implements CqlLibraryPresenter.DraftDisplay{

	/** CellTable Page Size. */
	private static final int PAGE_SIZE = 25;
	
	/** The cell table panel. */
	private VerticalPanel cellTablePanel = new VerticalPanel();
	
	/** The main panel. */
	private FlowPanel mainPanel = new FlowPanel();
	
	
	/** The measure search filter widget. */
	private SearchWidget searchWidget = new SearchWidget("Search", 
            "Search", "searchWidget");
	
	/** Zoom Button for Showing Search Widget. */
	private CustomButton zoomButton = (CustomButton) getImage("Search",
			ImageResources.INSTANCE.search_zoom(), "Search");
	
	private ErrorMessageAlert errorMessages = new ErrorMessageAlert();
	
	
	private SingleSelectionModel<CQLLibraryDataSetObject> selectionModel;
	
	private SaveCancelButtonBar buttonBar = new SaveCancelButtonBar();
	
	
	
	private Widget getImage(String action, ImageResource url, String key) {
		CustomButton image = new CustomButton();
		image.removeStyleName("gwt-button");
		image.setStylePrimaryName("invisibleButtonTextMeasureLibrary");
		image.setTitle(action);
		image.setResource(url, action);
		image.getElement().setAttribute("id", "CQLLibDraftViewSearchButton");
		return image;
	}
	
	public CQLLibraryDraftView(){
		zoomButton.getElement().getStyle().setMarginLeft(30, Unit.PX);
		zoomButton.getElement().setId("CqlzoomButton_CustomButton");
		mainPanel.setStylePrimaryName("contentPanel");
		mainPanel.addStyleName("leftAligned");
		
		mainPanel.add(searchWidget);		
		mainPanel.add(new SpacerWidget());
		
		cellTablePanel.getElement().setId("cqlcellTablePanel_VerticalPanel");
		cellTablePanel.setWidth("99%");
		mainPanel.add(cellTablePanel);
		mainPanel.add(new SpacerWidget());
		
		mainPanel.add(errorMessages);
		errorMessages.getElement().setId("errorMessages_ErrorMessageDisplay");
		
		
		mainPanel.add(new SpacerWidget());
		
		mainPanel.add(buttonBar);
	}
	
	
	/** Adds the column to table.
	 * @param cellTable the cell table
	 * @return the cell table */
	private CellTable<CQLLibraryDataSetObject> addColumnToTable(final CellTable<CQLLibraryDataSetObject> cellTable) {
		Column<CQLLibraryDataSetObject, Boolean> radioButtonColumn = new Column<CQLLibraryDataSetObject, Boolean>(new RadioButtonCell(true, true)) {
			@Override
			public Boolean getValue(CQLLibraryDataSetObject result) {
				return cellTable.getSelectionModel().isSelected(result);
			}
		};
		radioButtonColumn.setFieldUpdater(new FieldUpdater<CQLLibraryDataSetObject, Boolean>() {
			@Override
			public void update(int index, CQLLibraryDataSetObject object, Boolean value) {
				cellTable.getSelectionModel().setSelected(object, true);
			}
		});
		cellTable.addColumn(radioButtonColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Select\">"
				+ "Select" + "</span>"));
		Column<CQLLibraryDataSetObject, SafeHtml> libraryNameColumn = new Column<CQLLibraryDataSetObject, SafeHtml>(new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(CQLLibraryDataSetObject object) {
				String title = "Library Name " + object.getCqlName();
				return CellTableUtility.getNameColumnToolTip(object.getCqlName(), title);
			}
		};
		cellTable.addColumn(libraryNameColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Library Name\">"
				+ "Library Name" + "</span>"));
		Column<CQLLibraryDataSetObject, SafeHtml> versionColumn = new Column<CQLLibraryDataSetObject, SafeHtml>(new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(CQLLibraryDataSetObject object) {
				String title = "Version " + object.getVersion();
				return CellTableUtility.getColumnToolTip(object.getVersion(), title);
			}
		};
		cellTable.addColumn(versionColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Version\">"
				+ "Version" + "</span>"));
		return cellTable;
	}
	@Override
	public void buildDataTable(SaveCQLLibraryResult result) {
		cellTablePanel.clear();
		cellTablePanel.setStyleName("cellTablePanel");
		Label cellTablePanelHeader = new Label("Select a CQL Library Version to create a Draft.");
		cellTablePanelHeader.getElement().setId("cellTablePanelHeader_Label");
		cellTablePanelHeader.setStyleName("recentSearchHeader");
		selectionModel = null;
		if (result.getCqlLibraryDataSetObjects() != null && result.getCqlLibraryDataSetObjects().size() > 0) {
			CellTable<CQLLibraryDataSetObject> cellTable = new CellTable<CQLLibraryDataSetObject>();
			ListDataProvider<CQLLibraryDataSetObject> sortProvider = new ListDataProvider<CQLLibraryDataSetObject>();
			List<CQLLibraryDataSetObject> measureList = new ArrayList<CQLLibraryDataSetObject>();
			measureList.addAll(result.getCqlLibraryDataSetObjects());
			cellTable.setPageSize(PAGE_SIZE);
			cellTable.redraw();
			cellTable.setRowCount(measureList.size(), true);
			cellTable.setSelectionModel(getSelectionModelWithHandler());
			sortProvider.refresh();
			sortProvider.getList().addAll(result.getCqlLibraryDataSetObjects());
			cellTable = addColumnToTable(cellTable);
			sortProvider.addDataDisplay(cellTable);
			CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
			MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true);
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
			Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel("libraryDraftSummary",
					"In the following CQL Library Draft of exisiting version table, a radio button is positioned to the left "
							+ "of the table with a select column header followed by Library name in "
							+ "second column and version in the third column. The draft CQL Library "
							+ "are listed alphabetically in a table.");
			cellTable.getElement().setAttribute("id", "libraryDraftFromVersionCellTable");
			cellTable.getElement().setAttribute("aria-describedby", "libraryDraftSummary");
			cellTablePanel.add(invisibleLabel);
			cellTablePanel.add(cellTable);
			cellTablePanel.add(new SpacerWidget());
			cellTablePanel.add(spager);
		} else {
			HTML desc = new HTML("<p> No available libraries.</p>");
			cellTablePanel.add(cellTablePanelHeader);
			cellTablePanel.add(new SpacerWidget());
			cellTablePanel.add(desc);
		}
	}
	
	private SingleSelectionModel<CQLLibraryDataSetObject> getSelectionModelWithHandler() {
		selectionModel = new SingleSelectionModel<CQLLibraryDataSetObject>();
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				errorMessages.clearAlert();
			}
		});
		return selectionModel;
	}
	@Override
	public Widget asWidget() {
		return mainPanel;
	}
	@Override
	public HasClickHandlers getSaveButton() {
		return buttonBar.getSaveButton();
	}
	@Override
	public HasClickHandlers getCancelButton() {
		return buttonBar.getCancelButton();
	}
	
	@Override
	public HasClickHandlers getSearchButton() {
		return searchWidget.getSearchButton();
	}
	
	@Override
	public SearchWidget getSearchWidget() {
		return searchWidget;
	}
	@Override
	public CustomButton getZoomButton() {
		return zoomButton;
	}
	@Override
	public CQLLibraryDataSetObject getSelectedLibrary() {
		if (selectionModel != null) {
			return selectionModel.getSelectedObject();
		} else {
			return null;
		}
	}
	@Override
	public ErrorMessageAlert getErrorMessages() {
		return errorMessages;
	}
	
	
}
