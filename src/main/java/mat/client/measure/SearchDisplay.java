package mat.client.measure;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.advancedsearch.AdvancedSearchPillPanel;
import mat.client.buttons.CustomButton;
import mat.client.cqlworkspace.EditConfirmationDialogBox;
import mat.client.measure.MeasureSearchView.AdminObserver;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.shared.MessageAlert;
import mat.client.shared.MostRecentMeasureWidget;
import mat.client.shared.SearchWidgetWithFilter;
import mat.shared.MeasureSearchModel;
import org.gwtbootstrap3.client.ui.Button;

public interface SearchDisplay extends BaseDisplay {
    @Override
    Widget asWidget();

    void buildDataTable(ManageMeasureSearchModel manageMeasureSearchModel, int filter, String searchText);

    void clearTransferCheckBoxes();

    HasClickHandlers getClearButton();

    @Override
    MessageAlert getErrorMessageDisplay();

    MessageAlert getErrorMessagesForTransferOS();

    HasClickHandlers getTransferButton();

    void setAdminObserver(AdminObserver adminObserver);

    void buildCellTable(ManageMeasureSearchModel manageMeasureSearchModel, int filter, MeasureSearchModel model);

    void buildMostRecentWidget();

    Button getCreateMeasureButton();

    Button getCreateCompositeMeasureButton();

    MessageAlert getErrorMeasureDeletion();

    MessageAlert getErrorMessageDisplayForBulkExport();

    FormPanel getForm();

    SearchWidgetWithFilter getMeasureSearchFilterWidget();

    MostRecentMeasureWidget getMostRecentMeasureWidget();

    VerticalPanel getMostRecentMeasureVerticalPanel();

    MeasureSearchView getMeasureSearchView();

    HasClickHandlers getSearchButton();

    HasClickHandlers getAdminSearchButton();

    HasValue<String> getSearchString();

    HasValue<String> getAdminSearchString();

    int getSelectedFilter();

    HasSelectionHandlers<ManageMeasureSearchModel.Result> getSelectIdForEditTool();

    MessageAlert getSuccessMeasureDeletion();

    MessageAlert getSuccessMessageDisplay();

    CustomButton getZoomButton();

    VerticalPanel getCellTablePanel();

    EditConfirmationDialogBox getDraftConfirmationDialogBox();

    void resetMessageDisplay();

    CustomCheckBox getCustomFilterCheckBox();

    AdvancedSearchPillPanel getSearchPillPanel();

    void resetDisplay();
}
