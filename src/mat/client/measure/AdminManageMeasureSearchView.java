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
import mat.client.shared.search.SearchView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

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
	/** The view. */
	private SearchView<ManageMeasureSearchModel.Result> view = new SearchView<ManageMeasureSearchModel.Result>(true);
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
		mainPanel.add(view.asWidget());
		mainPanel.setStyleName("contentPanel");
		mainPanel.add(new SpacerWidget());
		mainPanel.add(buildBottomButtonWidget((PrimaryButton) transferButton,
				(PrimaryButton) clearButton, errorMessagesForTransferOS));
		MatContext.get().setAdminManageMeasureSearchView(this);
	}
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.AdminSearchDisplay#asWidget()
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
		//flowPanel.setStyleName("rightAlignButton");
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
	@Override
	public void buildDataTable(AdminMeasureSearchResultAdaptor results) {
		buildMeasureDataTable(results);
	}
	/**
	 * Builds the measure data table.
	 * @param results
	 *            the results
	 */
	private void buildMeasureDataTable(AdminMeasureSearchResultAdaptor results) {
		if (results == null) {
			return;
		}
		errorMessagesForTransferOS.clear();
		CellTable<ManageMeasureSearchModel.Result> cellTable = new CellTable<ManageMeasureSearchModel.Result>();
		ListDataProvider<ManageMeasureSearchModel.Result> sortProvider = new ListDataProvider<ManageMeasureSearchModel.Result>();
		selectedMeasureList = new ArrayList<Result>();
		selectedMeasureList.addAll(results.getData().getData());
		// Display 50 rows on a page
		cellTable.setPageSize(PAGE_SIZE);
		cellTable.redraw();
		cellTable.setRowCount(selectedMeasureList.size(), true);
		sortProvider.refresh();
		sortProvider.getList().addAll(results.getData().getData());
		ListHandler<ManageMeasureSearchModel.Result> sortHandler = new ListHandler<
				ManageMeasureSearchModel.Result>(sortProvider.getList());
		cellTable.addColumnSortHandler(sortHandler);
		cellTable = results.addColumnToTable(cellTable, sortHandler);
		sortProvider.addDataDisplay(cellTable);
		spager.setDisplay(cellTable);
		spager.setPageSize(PAGE_SIZE);
		/* spager.setToolTipAndTabIndex(spager); */
		view.getvPanelForQDMTable().clear();
		Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel(
				"measureOwnerShipSummary",
				"In the following Transfer Ownership table, Measure Name is given in the first column, "
						+ "Owner in the second column, Owner Email Address in the third Column,"
						+ "eMeasure ID in the fourth column, History in the fifth column and "
						+ "Transfer in the sixth column with Check boxes positioned to the right of the table");
		cellTable.getElement().setAttribute("id", "measureOwnerShipCellTable");
		cellTable.getElement().setAttribute("aria-describedby", "measureOwnerShipSummary");
		view.getvPanelForQDMTable().setStyleName("cellTablePanel");
		view.getvPanelForQDMTable().add(invisibleLabel);
		view.getvPanelForQDMTable().add(cellTable);
		view.getvPanelForQDMTable().add(new SpacerWidget());
		view.getvPanelForQDMTable().add(spager);
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
	@Override
	public void clearTransferCheckBoxes() {
		for (ManageMeasureSearchModel.Result result : selectedMeasureList) {
			result.setTransferable(false);
		}
		AdminMeasureSearchResultAdaptor adapter = new AdminMeasureSearchResultAdaptor();
		adapter.getData().setData(selectedMeasureList);
		buildDataTable(adapter);
	}
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.AdminSearchDisplay#getClearButton()
	 */
	@Override
	public HasClickHandlers getClearButton() {
		return clearButton;
	}
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.AdminSearchDisplay#getErrorMessageDisplay()
	 */
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessagePanel;
	}
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.AdminSearchDisplay#getErrorMessagesForTransferOS()
	 */
	@Override
	public ErrorMessageDisplayInterface getErrorMessagesForTransferOS() {
		return errorMessagesForTransferOS;
	}
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.AdminSearchDisplay#getSearchButton()
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
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.AdminSearchDisplay#getTransferButton()
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
