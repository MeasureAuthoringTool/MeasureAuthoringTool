package mat.client.measure;

import java.util.ArrayList;
import java.util.List;

import mat.client.CustomPager;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatContext;
import mat.client.shared.MatSimplePager;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SpacerWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
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

// TODO: Auto-generated Javadoc
/**
 * The Class AdminManageMeasureSearchView.
 */
public class AdminManageMeasureSearchView implements ManageMeasurePresenter.AdminSearchDisplay {
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
	//Used custom pager class - for disabling next/last button when on last page and for showing correct pagination number.
	/** The spager. */
	private MatSimplePager spager;
	/** The transfer button. */
	private Button transferButton = new PrimaryButton("Transfer", "primaryGreyButton");
	/** The measure vpanel. */
	private VerticalPanel measureVpanel = new VerticalPanel();
	/** The index. */
	private int index;
	
	private String searchString;
	/**
	 * Instantiates a new admin manage measure search view.
	 */
	public AdminManageMeasureSearchView() {
		CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
		spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true);
		spager.setPageStart(0);
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
	public void buildDataTable(AdminMeasureSearchResultAdaptor results, int filter, String searchText) {
		buildMeasureDataTable(results, filter, searchText);
	}
	
	/**
	 * Builds the measure data table.
	 *
	 * @param results the results
	 * @param filter the filter
	 * @param searchText the search text
	 */
	private void buildMeasureDataTable(AdminMeasureSearchResultAdaptor results, final int filter, final String searchText) {
		if (results == null) {
			return;
		}
		searchString = searchText;
		errorMessagesForTransferOS.clear();
		CellTable<ManageMeasureSearchModel.Result> cellTable = new CellTable<ManageMeasureSearchModel.Result>();
		selectedMeasureList = new ArrayList<Result>();
		selectedMeasureList.addAll(results.getData().getData());
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

		provider.addDataDisplay(cellTable);
		cellTable.setPageSize(PAGE_SIZE);
		cellTable.redraw();
		cellTable.setRowCount(selectedMeasureList.size(), true);
		ListHandler<ManageMeasureSearchModel.Result> sortHandler = new ListHandler<
				ManageMeasureSearchModel.Result>(results.getData().getData());
		cellTable.addColumnSortHandler(sortHandler);
		cellTable = results.addColumnToTable(cellTable, sortHandler);
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
	@Override
	public void clearTransferCheckBoxes() {
		for (ManageMeasureSearchModel.Result result : selectedMeasureList) {
			result.setTransferable(false);
		}
		AdminMeasureSearchResultAdaptor adapter = new AdminMeasureSearchResultAdaptor();
		adapter.getData().setData(selectedMeasureList);
		buildDataTable(adapter, 1, searchString);
	}
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
}
