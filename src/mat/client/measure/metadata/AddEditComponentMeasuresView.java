package mat.client.measure.metadata;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import mat.client.CustomPager;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.measure.MeasureSearchView.Observer;
import mat.client.resource.CellTableResource;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatCheckBoxCell;
import mat.client.shared.MatContext;
import mat.client.shared.MatSafeHTMLCell;
import mat.client.shared.MatSimplePager;
import mat.client.shared.SearchWidgetWithFilter;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SearchWidget;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.util.CellTableUtility;
import mat.shared.ClickableSafeHtmlCell;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.MultiSelectionModel;

// TODO: Auto-generated Javadoc
/**
 * The Class AddEditComponentMeasuresView.
 */
public class AddEditComponentMeasuresView implements
		MetaDataPresenter.AddEditComponentMeasuresDisplay {

	/** The search button. */
	private Button searchButton = new PrimaryButton("Search",
			"primaryGreyLeftButton");

	/** The search input. */
	private TextBox searchInput = new TextBox();

	/** The main panel. */
	private FlowPanel mainPanel = new FlowPanel();

	/** The cell table panel. */
	private VerticalPanel cellTablePanel = new VerticalPanel();

	/** The Constant PAGE_SIZE. */
	private static final int PAGE_SIZE = 25;

	/** The table. */
	private CellTable<ManageMeasureSearchModel.Result> table;

	/** The selected measure list. */
	private List<ManageMeasureSearchModel.Result> selectedMeasureList;

	/** The measure search filter widget. */
	private SearchWidgetWithFilter measureSearchFilterWidget = new SearchWidgetWithFilter(
			"searchFilter", "measureLibraryFilterDisclosurePanel","forMeasure");

	/** The search widget. */
	private SearchWidget searchWidget = new SearchWidget("Search", 
            "Search", "searchWidget");

	/** The even. */
	private Boolean even;
	
	/** The cell table css style. */
	private List<String> cellTableCssStyle;
	
	/** The cell table even row. */
	private String cellTableEvenRow = "cellTableEvenRow";
	
	/** The cell table odd row. */
	private String cellTableOddRow = "cellTableOddRow";

	/** The observer. */
	private Observer observer;

	/** The component measure selected list. */
	private List<ManageMeasureSearchModel.Result> componentMeasuresList =
			new ArrayList<ManageMeasureSearchModel.Result>();;

	/** The index. */
	private int index;

	/** The selection model. */
	private MultiSelectionModel<ManageMeasureSearchModel.Result> selectionModel;

	/** The return button. */
	protected Button returnButton = new PrimaryButton("Return to Previous");

	/** The addto component measures. */
	protected Button applytoComponentMeasures = new PrimaryButton(
			"Apply to Component Measures List");

	/** The success messages. */ 
	private SuccessMessageDisplay successMessages = new SuccessMessageDisplay();

	/**
	 * Instantiates a new adds the edit component measures view.
	 */
	public AddEditComponentMeasuresView() {
		
		successMessages.setMessage("");
		HorizontalPanel mainHorizontalPanel = new HorizontalPanel();
		mainHorizontalPanel.getElement().setId("panel_MainHorizontalPanel");
		searchInput.getElement().setId("searchInput_TextBox");
		searchButton.getElement().setId("searchButton_Button");
		mainPanel.setStyleName("contentPanel");
		SimplePanel panel = new SimplePanel();
		panel.setWidth("550px");
		VerticalPanel measureFilterVP = new VerticalPanel();
		measureFilterVP.setWidth("100px");
		measureFilterVP.getElement().setId("panel_measureFilterVP");
		searchWidget.getSearchInput().setHeight("20px");
		measureFilterVP.add(searchWidget);
		mainHorizontalPanel.add(measureFilterVP);
		mainPanel.add(mainHorizontalPanel);
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
		mainPanel.add(getApplytoComponentMeasuresBtn());
	}

	/**
	 * Adds the column to table.
	 * 
	 * @return the cell table
	 */
	private CellTable<ManageMeasureSearchModel.Result> addColumnToTable() {
		Label measureSearchHeader = new Label("All Measures");
		measureSearchHeader.getElement().setId("measureSearchHeader_Label");
		measureSearchHeader.setStyleName("recentSearchHeader");
		com.google.gwt.dom.client.TableElement elem = table.getElement().cast();
		measureSearchHeader.getElement().setAttribute("tabIndex", "0");
		TableCaptionElement caption = elem.createCaption();
		caption.appendChild(measureSearchHeader.getElement());
		selectionModel = new MultiSelectionModel<ManageMeasureSearchModel.Result>();
		table.setSelectionModel(selectionModel);
		Header<SafeHtml> selectionColumnHeader = new Header<SafeHtml>(
				new ClickableSafeHtmlCell()) {
			private String cssClass = "transButtonWidth";
			private String title = "Click to Clear All";

			@Override
			public SafeHtml getValue() {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				sb.appendHtmlConstant("<span>Select</span><button type=\"button\" title='"
						+ title
						+ "' tabindex=\"0\" class=\" "
						+ cssClass
						+ "\">"
						+ "<span class='textCssStyle'>(Clear)</span></button>");
				return sb.toSafeHtml();
			}
		};
		selectionColumnHeader.setUpdater(new ValueUpdater<SafeHtml>() {
			@Override
			public void update(SafeHtml value) {
				clearAllCheckBoxes();
			}
		});
		MatCheckBoxCell chbxCell = new MatCheckBoxCell(false, true);

		Column<ManageMeasureSearchModel.Result, Boolean> selectColumn = new Column<ManageMeasureSearchModel.Result, Boolean>(
				chbxCell) {

			@Override
			public Boolean getValue(Result object) {
				boolean isSelected = false;
				if (componentMeasuresList != null
						&& componentMeasuresList.size() > 0) {
					for (int i = 0; i < componentMeasuresList.size(); i++) {
						if (componentMeasuresList.get(i).getId()
								.equalsIgnoreCase(object.getId())) {
							isSelected = true;
							break;
						}
					}
				} else {
					isSelected = false;
				}
				return isSelected;
			}
		};

		selectColumn
				.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, Boolean>() {

					@Override
					public void update(int index, Result object, Boolean value) {
						selectionModel.setSelected(object, value);
						if (value) {
							componentMeasuresList.add(object);
						} else {
							for (int i = 0; i < componentMeasuresList
									.size(); i++) {
								if (componentMeasuresList.get(i).getId()
										.equalsIgnoreCase(object.getId())) {
									componentMeasuresList.remove(i);
									break;
								}
							}
						}

					}
				});
		table.addColumn(selectColumn, selectionColumnHeader);

		Column<ManageMeasureSearchModel.Result, SafeHtml> measureName = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new ClickableSafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
				return CellTableUtility.getColumnToolTip(object.getName());
			}
		};
		table.addColumn(measureName, SafeHtmlUtils
				.fromSafeConstant("<span title='Measure Name Column'>"
						+ "Measure Name" + "</span>"));
		// Version Column
		Column<ManageMeasureSearchModel.Result, SafeHtml> version = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new MatSafeHTMLCell()) {
			@Override
			public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
				return CellTableUtility.getColumnToolTip(object.getVersion());
			}
		};
		table.addColumn(version, SafeHtmlUtils
				.fromSafeConstant("<span title='Version'>" + "Version"
						+ "</span>"));
		// Finalized Date
		Column<ManageMeasureSearchModel.Result, SafeHtml> finalizedDate = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new MatSafeHTMLCell()) {
			@Override
			public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
				if (object.getFinalizedDate() != null) {
					return CellTableUtility
							.getColumnToolTip(convertTimestampToString(object
									.getFinalizedDate()));
				}
				return null;
			}
		};
		table.addColumn(finalizedDate, SafeHtmlUtils
				.fromSafeConstant("<span title='Finalized Date'>"
						+ "Finalized Date" + "</span>"));
		return table;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.measure.metadata.BaseMetaDataPresenter.BaseAddEditDisplay#
	 * buildCellTable(mat.client.measure.ManageMeasureSearchModel)
	 */
	@Override
	public void buildCellTable(ManageMeasureSearchModel result,
			final String searchText, List<ManageMeasureSearchModel.Result> measureSelectedList) {
		cellTablePanel.clear();
		cellTablePanel.setStyleName("cellTablePanel");
		cellTablePanel.setWidth("900px");
		if ((result.getData() != null) && (result.getData().size() > 0)) {
			table = new CellTable<ManageMeasureSearchModel.Result>(PAGE_SIZE,
					(Resources) GWT.create(CellTableResource.class));
			table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
			selectedMeasureList = new ArrayList<Result>();
			//componentMeasuresList = new ArrayList<ManageMeasureSearchModel.Result>();
			componentMeasuresList = measureSelectedList;
			selectedMeasureList.addAll(result.getData());
			table.setPageSize(PAGE_SIZE);
			table.redraw();
			AsyncDataProvider<ManageMeasureSearchModel.Result> provider = new AsyncDataProvider<ManageMeasureSearchModel.Result>() {
				@Override
				protected void onRangeChanged(
						HasData<ManageMeasureSearchModel.Result> display) {
					final int start = display.getVisibleRange().getStart();
					index = start;
					AsyncCallback<ManageMeasureSearchModel> callback = new AsyncCallback<ManageMeasureSearchModel>() {
						@Override
						public void onFailure(Throwable caught) {
							Window.alert(caught.getMessage());
						}

						@Override
						public void onSuccess(ManageMeasureSearchModel result) {
							List<ManageMeasureSearchModel.Result> manageMeasureSearchList = new ArrayList<ManageMeasureSearchModel.Result>();
							manageMeasureSearchList.addAll(result.getData());
							selectedMeasureList = manageMeasureSearchList;
							buildCellTableCssStyle();
							updateRowData(start, manageMeasureSearchList);
						}
					};

					MatContext
							.get()
							.getMeasureService()
							.search(searchText, start + 1, start + PAGE_SIZE,
									1, callback);
				}
			};
			table.setRowData(selectedMeasureList);
			table.setRowCount(result.getResultsTotal(), true);
			table = addColumnToTable();
			provider.addDataDisplay(table);
			CustomPager.Resources pagerResources = GWT
					.create(CustomPager.Resources.class);
			MatSimplePager spager = new MatSimplePager(
					CustomPager.TextLocation.CENTER, pagerResources, false, 0,
					true);
			spager.setPageStart(0);
			buildCellTableCssStyle();
			spager.setDisplay(table);
			spager.setPageSize(PAGE_SIZE);
			table.setWidth("100%");
			table.setColumnWidth(0, 14.0, Unit.PCT);
			table.setColumnWidth(1, 48.0, Unit.PCT);
			table.setColumnWidth(2, 20.0, Unit.PCT);
			table.setColumnWidth(3, 18.0, Unit.PCT);
			Label invisibleLabel = (Label) LabelBuilder
					.buildInvisibleLabel(
							"measureSearchSummary",
							"In the following Measure List table,Select is given in first Column, Measure Name is given in Second column,"
									+ " Version in Third column, Finalized Date in fouth column.");
			table.getElement().setAttribute("id", "MeasureSearchCellTable");
			table.getElement().setAttribute("aria-describedby",
					"measureSearchSummary");
			cellTablePanel.add(invisibleLabel);
			cellTablePanel.add(table);
			cellTablePanel.add(new SpacerWidget());
			cellTablePanel.add(spager);
			mainPanel.add(cellTablePanel);

		} else {
			Label measureSearchHeader = new Label("All Measures");
			measureSearchHeader.getElement().setId("measureSearchHeader_Label");
			measureSearchHeader.setStyleName("recentSearchHeader");
			measureSearchHeader.getElement().setAttribute("tabIndex", "0");
			HTML desc = new HTML("<p> No My Measures. </p>");
			cellTablePanel.add(measureSearchHeader);
			cellTablePanel.add(new SpacerWidget());
			cellTablePanel.add(desc);
			mainPanel.add(cellTablePanel);
		}
		SimplePanel addMeasureBtnPanel = new SimplePanel();
		addMeasureBtnPanel.addStyleName("marginTop");
		addMeasureBtnPanel.add(getApplytoComponentMeasuresBtn());
		SimplePanel addSuccessMsgPanel = new SimplePanel();
		addSuccessMsgPanel.addStyleName("marginTop");
		addSuccessMsgPanel.add(successMessages);
		mainPanel.add(addSuccessMsgPanel);
		mainPanel.add(addMeasureBtnPanel);
	}

	/**
	 * Builds the cell table css style.
	 */
	private void buildCellTableCssStyle() {
		cellTableCssStyle = new ArrayList<String>();
		for (int i = 0; i < selectedMeasureList.size(); i++) {
			cellTableCssStyle.add(i, null);
		}
		table.setRowStyles(new RowStyles<ManageMeasureSearchModel.Result>() {
			@Override
			public String getStyleNames(
					ManageMeasureSearchModel.Result rowObject, int rowIndex) {
				if (rowIndex > PAGE_SIZE - 1) {
					rowIndex = rowIndex - index;
				}
				if (rowIndex != 0) {
					if (cellTableCssStyle.get(rowIndex) == null) {
						if (even) {
							if (rowObject.getMeasureSetId().equalsIgnoreCase(
									selectedMeasureList.get(rowIndex - 1)
											.getMeasureSetId())) {
								even = true;
								cellTableCssStyle
										.add(rowIndex, cellTableOddRow);
								return cellTableOddRow;
							} else {
								even = false;
								cellTableCssStyle.add(rowIndex,
										cellTableEvenRow);
								return cellTableEvenRow;
							}
						} else {
							if (rowObject.getMeasureSetId().equalsIgnoreCase(
									selectedMeasureList.get(rowIndex - 1)
											.getMeasureSetId())) {
								even = false;
								cellTableCssStyle.add(rowIndex,
										cellTableEvenRow);
								return cellTableEvenRow;
							} else {
								even = true;
								cellTableCssStyle
										.add(rowIndex, cellTableOddRow);
								return cellTableOddRow;
							}
						}
					} else {
						return cellTableCssStyle.get(rowIndex);
					}
				} else {
					if (cellTableCssStyle.get(rowIndex) == null) {
						even = true;
						cellTableCssStyle.add(rowIndex, cellTableOddRow);
						return cellTableOddRow;
					} else {
						return cellTableCssStyle.get(rowIndex);
					}
				}
			}
		});
	}

	/**
	 * Clear all check boxes.
	 */
	public void clearAllCheckBoxes() {
		List<Result> displayedItems = new ArrayList<Result>();
		displayedItems.addAll(componentMeasuresList);
		componentMeasuresList.clear();
		for (ManageMeasureSearchModel.Result msg : displayedItems) {
			selectionModel.setSelected(msg, false);
		}
		table.redraw();
	}

	/**
	 * Update component measures selected list.
	 * 
	 * @param componentMeasureList
	 *            the component measure list
	 */
	private void updateComponentMeasuresSelectedList(
			List<ManageMeasureSearchModel.Result> componentMeasureList) {
		if (componentMeasuresList.size() != 0) {
			for (int i = 0; i < componentMeasuresList.size(); i++) {
				for (int j = 0; j < componentMeasureList.size(); j++) {
					if (componentMeasuresList
							.get(i)
							.getId()
							.equalsIgnoreCase(
									componentMeasureList.get(j).getId())) {
						componentMeasuresList.set(i,
								componentMeasureList.get(j));
						break;
					}
				}
			}
		}

	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.measure.metadata.BaseMetaDataPresenter.BaseAddEditDisplay#
	 * getSuccessMessageDisplay()
	 */
	@Override
	public SuccessMessageDisplayInterface getSuccessMessageDisplay() {
		return successMessages;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.measure.metadata.MetaDataPresenter.AddEditComponentMeasuresDisplay
	 * #getMeasureSearchFilterWidget()
	 */
	@Override
	public SearchWidgetWithFilter getMeasureSearchFilterWidget() {
		return measureSearchFilterWidget;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.measure.metadata.BaseMetaDataPresenter.BaseAddEditDisplay#
	 * asWidget()
	 */
	@Override
	public Widget asWidget() {
		return mainPanel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.measure.metadata.BaseMetaDataPresenter.BaseAddEditDisplay#
	 * getReturnButton()
	 */
	@Override
	public HasClickHandlers getReturnButton() {
		return returnButton;
	}

	/**
	 * Convert timestamp to string.
	 * 
	 * @param ts
	 *            the ts
	 * @return the string
	 */
	private String convertTimestampToString(Timestamp ts) {
		String tsStr;
		if (ts == null) {
			tsStr = "";
		} else {
			int hours = ts.getHours();
			String ap = hours < 12 ? "AM" : "PM";
			int modhours = hours % 12;
			String mins = ts.getMinutes() + "";
			if (mins.length() == 1) {
				mins = "0" + mins;
			}
			String hoursStr = modhours == 0 ? "12" : modhours + "";
			tsStr = (ts.getMonth() + 1) + "/" + ts.getDate() + "/"
					+ (ts.getYear() + 1900) + " " + hoursStr + ":" + mins + " "
					+ ap;
		}
		return tsStr;
	}

	/**
	 * Gets the observer.
	 * 
	 * @return the observer
	 */
	public Observer getObserver() {
		return observer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.measure.metadata.MetaDataPresenter.AddEditComponentMeasuresDisplay
	 * #getSelectedFilter()
	 */
	@Override
	public int getSelectedFilter() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.measure.metadata.MetaDataPresenter.AddEditComponentMeasuresDisplay
	 * #getRetButton()
	 */
	public Button getRetButton() {
		returnButton.getElement().setId("returnButton_Button");
		return returnButton;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.measure.metadata.MetaDataPresenter.AddEditComponentMeasuresDisplay
	 * #getAddtoComponentMeasuresBtn()
	 */
	public Button getApplytoComponentMeasuresBtn() {
		applytoComponentMeasures.getElement().setId("applytoComponentMeasures_Button");
		return applytoComponentMeasures;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.measure.metadata.MetaDataPresenter.AddEditComponentMeasuresDisplay
	 * #getAddtoComponentMeasuresButtonHandler()
	 */
	@Override
	public HasClickHandlers getApplytoComponentMeasuresButtonHandler() {
		return applytoComponentMeasures;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.measure.metadata.MetaDataPresenter.AddEditComponentMeasuresDisplay
	 * #getSearchButton()
	 */
	@Override
	public HasClickHandlers getSearchButton() {
		return searchWidget.getSearchButton();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.measure.metadata.MetaDataPresenter.AddEditComponentMeasuresDisplay
	 * #getSearchString()
	 */
	@Override
	public HasValue<String> getSearchString() {
		return searchWidget.getSearchInput();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.AddEditComponentMeasuresDisplay#getComponentMeasuresList()
	 */
	@Override
	public List<ManageMeasureSearchModel.Result> getComponentMeasuresList() {
		return componentMeasuresList;
	}

}
