package mat.client.measure;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import mat.client.CustomPager;
import mat.client.measure.AdminMeasureSearchResultAdaptor.Observer;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatButtonCell;
import mat.client.shared.MatCheckBoxCell;
import mat.client.shared.MatContext;
import mat.client.shared.MatSafeHTMLCell;
import mat.client.shared.MatSimplePager;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.util.CellTableUtility;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.MultiSelectionModel;

// TODO: Auto-generated Javadoc
/**
 * The Class AdminManageMeasureSearchView.
 */
public class AdminManageMeasureSearchView implements ManageMeasurePresenter.AdminSearchDisplay {
	
	
	/**
	 * The Interface Observer.
	 */
	public static interface Observer {
		/**
		 * On history clicked.
		 * @param result
		 *            the result
		 */
		void onHistoryClicked(ManageMeasureSearchModel.Result result);
		/**
		 * On transfer selected clicked.
		 * @param result
		 *            the result
		 */
		void onTransferSelectedClicked(ManageMeasureSearchModel.Result result);
		
		/**
		 * On clear all bulk export clicked.
		 */
		void onClearAllTranferChkBoxClicked();
	}
	
	/** Cell Table Page Size. */
	private static final int PAGE_SIZE = 25;
	/** The clear button. */
	private Button clearButton = new PrimaryButton("Clear All", "primaryGreyLeftButton");
	/** The error message panel. */
	private ErrorMessageDisplay errorMessagePanel = new ErrorMessageDisplay();
	/** The error messages for transfer os. */
	private ErrorMessageDisplay errorMessagesForTransferOS = new ErrorMessageDisplay();
	/** The form. */
	private final FormPanel form = new FormPanel();
	/** The main panel. */
	private FlowPanel mainPanel = new FlowPanel();
	/** The search button. */
	private Button searchButton = new PrimaryButton("Search", "primaryGreyLeftButton");
	/** The search input. */
	private TextBox searchInput = new TextBox();
	/** The selected measure list. */
	private List<Result> selectedMeasureList;
	/** The transfer button. */
	private Button transferButton = new PrimaryButton("Transfer", "primaryGreyButton");
	/** The measure vpanel. */
	private VerticalPanel measureVpanel = new VerticalPanel();
	/** The index. */
	private int index;
	/** The search string. */
	private String searchString;
	/** The cell table. */
	CellTable<ManageMeasureSearchModel.Result> cellTable;
	/** The data. */
	private ManageMeasureSearchModel data = new ManageMeasureSearchModel();
	/** The is history clicked. */
	private boolean isHistoryClicked;
	/** The selection model. */
	private MultiSelectionModel<ManageMeasureSearchModel.Result> selectionModel;
	/** The col size. */
	private int COL_SIZE = 6;
	/** The observer. */
	private Observer observer;
	
	private List<ManageMeasureSearchModel.Result> selectedList;
	/**
	 * Instantiates a new admin manage measure search view.
	 */
	public AdminManageMeasureSearchView() {
		mainPanel.add(new SpacerWidget());
		mainPanel.add(buildSearchWidget());
		mainPanel.add(new SpacerWidget());
		mainPanel.add(measureVpanel);
		mainPanel.setStyleName("contentPanel");
		mainPanel.add(new SpacerWidget());
		mainPanel.add(buildBottomButtonWidget((PrimaryButton) transferButton,
				(PrimaryButton) clearButton, errorMessagesForTransferOS));
		MatContext.get().setAdminManageMeasureSearchView(this);
	}
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.AdminSearchDisplay#asWidget()
	 */
	/**
	 * As widget.
	 *
	 * @return the widget
	 */
	@Override
	public Widget asWidget() {
		return mainPanel;
	}
	/**
	 * Builds the bottom button widget.
	 * @param transferButton
	 *            the transfer button
	 * @param clearButton
	 *            the clear button
	 * @param errorMessageDisplay
	 *            the error message display
	 * @return the widget
	 */
	private Widget buildBottomButtonWidget(PrimaryButton transferButton, PrimaryButton clearButton,
			ErrorMessageDisplay errorMessageDisplay) {
		FlowPanel flowPanel = new FlowPanel();
		flowPanel.add(errorMessageDisplay);
		transferButton.setTitle("Transfer");
		clearButton.setTitle("Clear");
		flowPanel.add(transferButton);
		flowPanel.add(clearButton);
		form.setWidget(flowPanel);
		return form;
	}
	/*
	 * (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.AdminSearchDisplay#buildDataTable( mat.client.measure.AdminMeasureSearchResultAdaptor)
	 */
	/**
	 * Builds the data table.
	 *
	 * @param results the results
	 * @param filter the filter
	 * @param searchText the search text
	 */
	@Override
	public void buildDataTable(ManageMeasureSearchModel results, int filter, String searchText) {
		buildMeasureDataTable(results, filter, searchText);
	}
	
	/**
	 * Builds the measure data table.
	 *
	 * @param results the results
	 * @param filter the filter
	 * @param searchText the search text
	 */
	private void buildMeasureDataTable(ManageMeasureSearchModel results, final int filter, final String searchText) {
		if (results == null) {
			return;
		}
		searchString = searchText;
		errorMessagesForTransferOS.clear();
		cellTable = new CellTable<ManageMeasureSearchModel.Result>();
		selectedList = new ArrayList<ManageMeasureSearchModel.Result>();
		selectedMeasureList = new ArrayList<Result>();
		selectedMeasureList.addAll(results.getData());
		cellTable.setRowData(selectedMeasureList);
		cellTable.setRowCount(results.getResultsTotal(), true);
		cellTable.setPageSize(PAGE_SIZE);
		cellTable.redraw();
		ListHandler<ManageMeasureSearchModel.Result> sortHandler = new ListHandler<
				ManageMeasureSearchModel.Result>(results.getData());
		cellTable.addColumnSortHandler(sortHandler);
		AsyncDataProvider<ManageMeasureSearchModel.Result> provider = new AsyncDataProvider<ManageMeasureSearchModel.Result>() {
			@Override
			protected void onRangeChanged(
					HasData<ManageMeasureSearchModel.Result> display) {
				final int start = display.getVisibleRange().getStart();
				index = start;
				AsyncCallback<ManageMeasureSearchModel> callback = new AsyncCallback<ManageMeasureSearchModel>() {
					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(ManageMeasureSearchModel result) {
						List<ManageMeasureSearchModel.Result> manageMeasureSearchList = new ArrayList<ManageMeasureSearchModel.Result>();
						manageMeasureSearchList.addAll(result.getData());
						selectedMeasureList = manageMeasureSearchList;
						updateRowData(start, manageMeasureSearchList);
					}
				};

				MatContext.get().getMeasureService()
						.search(searchText, start + 1, start + PAGE_SIZE, filter, callback);
			}
		};
		cellTable = addColumnToTable(sortHandler);
		provider.addDataDisplay(cellTable);
		CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
		MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true);
		spager.setPageStart(0);
		spager.setDisplay(cellTable);
		spager.setPageSize(PAGE_SIZE);
		measureVpanel.clear();
		Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel(
				"measureOwnerShipSummary",
				"In the following Transfer Ownership table, Measure Name is given in the first column, "
						+ "Owner in the second column, Owner Email Address in the third Column,"
						+ "eMeasure ID in the fourth column, History in the fifth column and "
						+ "Transfer in the sixth column with Check boxes positioned to the right of the table");
		cellTable.getElement().setAttribute("id", "measureOwnerShipCellTable");
		cellTable.getElement().setAttribute("aria-describedby", "measureOwnerShipSummary");
		measureVpanel.setStyleName("cellTablePanel");
		measureVpanel.add(invisibleLabel);
		measureVpanel.add(cellTable);
		measureVpanel.add(new SpacerWidget());
		measureVpanel.add(spager);
	}
	
	/**
	 * Adds the column to table.
	 *
	 * @param table the table
	 * @param sortHandler the sort handler
	 * @return the cell table
	 */
	public CellTable<ManageMeasureSearchModel.Result> addColumnToTable(
			ListHandler<ManageMeasureSearchModel.Result> sortHandler) {
		if (cellTable.getColumnCount() != COL_SIZE) {
			Label searchHeader = new Label("Select Measures to Transfer Ownership.");
			searchHeader.getElement().setId("measureTransferOwnerShipCellTableCaption_Label");
			searchHeader.setStyleName("recentSearchHeader");
			searchHeader.getElement().setAttribute("tabIndex", "0");
			com.google.gwt.dom.client.TableElement elem = cellTable.getElement().cast();
			TableCaptionElement caption = elem.createCaption();
			caption.appendChild(searchHeader.getElement());
			selectionModel = new MultiSelectionModel<ManageMeasureSearchModel.Result>();
			cellTable.setSelectionModel(selectionModel);
			Column<ManageMeasureSearchModel.Result, SafeHtml> measureName = new Column<
					ManageMeasureSearchModel.Result, SafeHtml>(new MatSafeHTMLCell()) {
				@Override
				public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
					return CellTableUtility.getColumnToolTip(object.getName(), object.getName());
				}
			};
			measureName.setSortable(true);
			sortHandler.setComparator(measureName, new Comparator<ManageMeasureSearchModel.Result>() {
				@Override
				public int compare(ManageMeasureSearchModel.Result o1, ManageMeasureSearchModel.Result o2) {
					if (o1 == o2) {
						return 0;
					}
					// Compare the name columns.
					if (o1 != null) {
						return (o2 != null) ? o1.getName().compareTo(o2.getName()) : 1;
					}
					return -1;
				}
			});
			cellTable.addColumn(measureName, SafeHtmlUtils.fromSafeConstant("<span title=\"Measure Name\">"
					+ "Measure Name" + "</span>"));
			Column<ManageMeasureSearchModel.Result, SafeHtml> ownerName = new Column<
					ManageMeasureSearchModel.Result, SafeHtml>(new MatSafeHTMLCell()) {
				@Override
				public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
					return CellTableUtility.getColumnToolTip(object.getOwnerfirstName()
							+ "  " + object.getOwnerLastName(),object.getOwnerfirstName()
							+ "  " + object.getOwnerLastName());
				}
			};
			ownerName.setSortable(true);
			sortHandler.setComparator(ownerName, new Comparator<ManageMeasureSearchModel.Result>() {
				@Override
				public int compare(ManageMeasureSearchModel.Result o1, ManageMeasureSearchModel.Result o2) {
					if (o1 == o2) {
						return 0;
					}
					// Compare the name columns.
					if (o1 != null) {
						return (o2 != null) ? o1.getOwnerfirstName().compareTo(o2.getOwnerfirstName()) : 1;
					}
					return -1;
				}
			});
			cellTable.addColumn(ownerName, SafeHtmlUtils.fromSafeConstant("<span title=\"Owner\">" + "Owner" + "</span>"));
			Column<ManageMeasureSearchModel.Result, SafeHtml> ownerEmailAddress = new Column<
					ManageMeasureSearchModel.Result, SafeHtml>(new MatSafeHTMLCell()) {
				@Override
				public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
					return CellTableUtility.getColumnToolTip(object.getOwnerEmailAddress(),object.getOwnerEmailAddress());
				}
			};
			ownerEmailAddress.setSortable(true);
			sortHandler.setComparator(ownerEmailAddress, new Comparator<ManageMeasureSearchModel.Result>() {
				@Override
				public int compare(ManageMeasureSearchModel.Result o1, ManageMeasureSearchModel.Result o2) {
					if (o1 == o2) {
						return 0;
					}
					// Compare the name columns.
					if (o1 != null) {
						return (o2 != null) ? o1.getOwnerEmailAddress().compareTo(o2.getOwnerEmailAddress()) : 1;
					}
					return -1;
				}
			});
			cellTable.addColumn(ownerEmailAddress, SafeHtmlUtils.fromSafeConstant("<span title=\"Owner E-mail Address\">"
					+ "Owner E-mail Address" + "</span>"));
			Column<ManageMeasureSearchModel.Result, SafeHtml> eMeasureID = new Column<ManageMeasureSearchModel.Result,
					SafeHtml>(new MatSafeHTMLCell()) {
				@Override
				public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
					return CellTableUtility.getColumnToolTip("" + object.geteMeasureId(), "" + object.geteMeasureId());
				}
			};
			cellTable.addColumn(eMeasureID, SafeHtmlUtils.fromSafeConstant("<span title=\"eMeasure Id\">"
					+ "eMeasure Id" + "</span>"));
			Cell<String> historyButton = new MatButtonCell("Click to view history", "customClockButton");
			Column<Result, String> historyColumn = new Column<ManageMeasureSearchModel.Result, String>(historyButton) {
				@Override
				public String getValue(ManageMeasureSearchModel.Result object) {
					return "History";
				}
			};
			historyColumn.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, String>() {
				@Override
				public void update(int index, ManageMeasureSearchModel.Result object, String value) {
					observer.onHistoryClicked(object);
				}
			});
			cellTable.addColumn(historyColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"History\">" + "History" + "</span>"));
			Cell<Boolean> transferCB = new MatCheckBoxCell();
			Column<Result, Boolean> transferColumn = new Column<ManageMeasureSearchModel.Result, Boolean>(transferCB) {
				@Override
				public Boolean getValue(ManageMeasureSearchModel.Result object) {
					return object.isTransferable();
				}
			};
			transferColumn.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, Boolean>() {
				@Override
				public void update(int index, ManageMeasureSearchModel.Result object, Boolean value) {
					object.setTransferable(value);
					observer.onTransferSelectedClicked(object);
				}
			});
			cellTable.addColumn(transferColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Check for Ownership Transfer\">"
					+ "Transfer </span>"));
			cellTable.setColumnWidth(0, 30.0, Unit.PCT);
			cellTable.setColumnWidth(1, 20.0, Unit.PCT);
			cellTable.setColumnWidth(2, 20.0, Unit.PCT);
			cellTable.setColumnWidth(3, 15.0, Unit.PCT);
			cellTable.setColumnWidth(4, 5.0, Unit.PCT);
			cellTable.setColumnWidth(5, 5.0, Unit.PCT);
			cellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		}
		return cellTable;
	}
	
	/**
	 * Clear bulk export check boxes.
	 */
	@Override
	public void clearTransferCheckBoxes(){
		List<Result> displayedItems = new ArrayList<Result>();
		displayedItems.addAll(selectedList);
		selectedList.clear();
		for (ManageMeasureSearchModel.Result msg : displayedItems) {
			selectionModel.setSelected(msg, false);
			msg.setTransferable(false);
		}
		cellTable.redraw();
		//observer.onClearAllTranferChkBoxClicked();
	}
	/**
	 * Builds the search widget.
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
	 * @see mat.client.measure.ManageMeasurePresenter.AdminSearchDisplay#clearTransferCheckBoxes()
	 */
	/**
	 * Clear transfer check boxes.
	 */
//	@Override
//	public void clearTransferCheckBoxes() {
//		for (ManageMeasureSearchModel.Result result : selectedMeasureList) {
//			result.setTransferable(false);
//		}
//		ManageMeasureSearchModel adapter = new ManageMeasureSearchModel();
//		adapter.setData(selectedMeasureList);
//		buildDataTable(adapter, 1, searchString);
//	}
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.AdminSearchDisplay#getClearButton()
	 */
	/**
	 * Gets the clear button.
	 *
	 * @return the clear button
	 */
	@Override
	public HasClickHandlers getClearButton() {
		return clearButton;
	}
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.AdminSearchDisplay#getErrorMessageDisplay()
	 */
	/**
	 * Gets the error message display.
	 *
	 * @return the error message display
	 */
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessagePanel;
	}
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.AdminSearchDisplay#getErrorMessagesForTransferOS()
	 */
	/**
	 * Gets the error messages for transfer os.
	 *
	 * @return the error messages for transfer os
	 */
	@Override
	public ErrorMessageDisplayInterface getErrorMessagesForTransferOS() {
		return errorMessagesForTransferOS;
	}
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.AdminSearchDisplay#getSearchButton()
	 */
	/**
	 * Gets the search button.
	 *
	 * @return the search button
	 */
	@Override
	public HasClickHandlers getSearchButton() {
		return searchButton;
	}
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.AdminSearchDisplay#getSearchString()
	 */
	/**
	 * Gets the search string.
	 *
	 * @return the search string
	 */
	@Override
	public HasValue<String> getSearchString() {
		return searchInput;
	}
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.AdminSearchDisplay#getTransferButton()
	 */
	/**
	 * Gets the transfer button.
	 *
	 * @return the transfer button
	 */
	@Override
	public HasClickHandlers getTransferButton() {
		return transferButton;
	}
	/**
	 * Sets the clear button.
	 * @param clearButton
	 *            the new clear button
	 */
	public void setClearButton(Button clearButton) {
		this.clearButton = clearButton;
	}
	
	public ManageMeasureSearchModel getData() {
		return data;
	}
	
	/**
	 * Checks if is history clicked.
	 * @return true, if is history clicked
	 */
	public boolean isHistoryClicked() {
		return isHistoryClicked;
	}
	/**
	 * Sets the data.
	 * @param data
	 *            the new data
	 */
	public void setData(ManageMeasureSearchModel data) {
		this.data = data;
	}
	/**
	 * Sets the history clicked.
	 * @param isHistoryClicked
	 *            the new history clicked
	 */
	public void setHistoryClicked(boolean isHistoryClicked) {
		this.isHistoryClicked = isHistoryClicked;
	}
	/**
	 * Sets the observer.
	 * @param observer
	 *            the new observer
	 */
	public void setObserver(Observer observer) {
		this.observer = observer;
	}
}
