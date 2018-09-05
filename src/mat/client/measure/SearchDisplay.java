package mat.client.measure;

import org.gwtbootstrap3.client.ui.Button;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.buttons.CustomButton;
import mat.client.cqlworkspace.EditConfirmationDialogBox;
import mat.client.advancedSearch.MeasureLibraryAdvancedSearchBuilder;
import mat.client.measure.MeasureSearchView.AdminObserver;
import mat.client.measure.metadata.Grid508;
import mat.client.shared.MessageAlert;
import mat.client.shared.MostRecentMeasureWidget;
import mat.client.shared.SearchWidgetWithFilter;
import mat.shared.MeasureSearchModel;

public interface SearchDisplay extends BaseDisplay {
	@Override
	public Widget asWidget();

	public void buildDataTable(ManageMeasureSearchModel manageMeasureSearchModel, int filter, String searchText);

	public void clearTransferCheckBoxes();

	public HasClickHandlers getClearButton();

	@Override
	public MessageAlert getErrorMessageDisplay();

	public MessageAlert getErrorMessagesForTransferOS();

	public HasClickHandlers getTransferButton();

	void setAdminObserver(AdminObserver adminObserver);

	public void buildCellTable(ManageMeasureSearchModel manageMeasureSearchModel, int filter, MeasureSearchModel model);

	void buildMostRecentWidget();

	public void clearBulkExportCheckBoxes(Grid508 dataTable);

	public HasClickHandlers getBulkExportButton();

	Button getCreateMeasureButton();
	
	Button getCreateCompositeMeasureButton();

	public MessageAlert getErrorMeasureDeletion();

	public MessageAlert getErrorMessageDisplayForBulkExport();

	public Button getExportSelectedButton();

	public FormPanel getForm();

	SearchWidgetWithFilter getMeasureSearchFilterWidget();

	MostRecentMeasureWidget getMostRecentMeasureWidget();

	MeasureSearchView getMeasureSearchView();

	public HasClickHandlers getSearchButton();

	public HasClickHandlers getAdminSearchButton();

	public HasValue<String> getSearchString();

	public HasValue<String> getAdminSearchString();

	int getSelectedFilter();

	public String getSelectedOption();

	public HasSelectionHandlers<ManageMeasureSearchModel.Result> getSelectIdForEditTool();

	public MessageAlert getSuccessMeasureDeletion();

	public MessageAlert getSuccessMessageDisplay();

	CustomButton getZoomButton();

	VerticalPanel getCellTablePanel();

	EditConfirmationDialogBox getDraftConfirmationDialogBox();

	public void resetMessageDisplay();
	
	public MeasureLibraryAdvancedSearchBuilder getMeasureLibraryAdvancedSearchBuilder();
}
