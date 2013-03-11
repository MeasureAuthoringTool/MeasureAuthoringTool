package mat.client.clause;

import mat.client.MatPresenter;
import mat.client.codelist.service.CodeListService;
import mat.client.codelist.service.CodeListServiceAsync;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.model.QualityDataSetDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

public class QDSAppliedListPresenter implements MatPresenter {

	private SimplePanel panel = new SimplePanel();
	private SearchDisplay searchDisplay;
	List<QualityDataSetDTO> qdsList = new ArrayList<QualityDataSetDTO>();

	public static interface SearchDisplay {
		public SuccessMessageDisplayInterface getApplyToMeasureSuccessMsg();
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		//public void setAppliedQDMs(List<QualityDataSetDTO> appliedQDMs);
		public  void buildCellListWidget(QDSAppliedListModel appliedListModel);
		public Widget asWidget();
	}

	public QDSAppliedListPresenter(SearchDisplay sDisplayArg) {
		this.searchDisplay = sDisplayArg;
		showAppliedQDMsInMeasure(MatContext.get().getCurrentMeasureId());
	}

	void loadCodeListData() {
		panel.clear();
		showAppliedQDMsInMeasure(MatContext.get().getCurrentMeasureId());
		displaySearch();
	}

	public Widget getWidget() {
		return panel;
	}

	public void resetQDSFields() {
		searchDisplay.getApplyToMeasureSuccessMsg().clear();
		searchDisplay.getErrorMessageDisplay().clear();
	}

	private void displaySearch() {
		panel.clear();
		panel.add(searchDisplay.asWidget());
	}
	/**
	 * Method for fetching all applied Value Sets in a measure which is loaded
	 * in context.
	 * 
	 * */
	public void showAppliedQDMsInMeasure(String measureId) {
		measureId = MatContext.get().getCurrentMeasureId();
		CodeListServiceAsync codeListService = (CodeListServiceAsync) GWT
				.create(CodeListService.class);
		if (measureId != null && measureId != "") {
			codeListService.getQDSElements(measureId, null,
					new AsyncCallback<List<QualityDataSetDTO>>() {
						@Override
						public void onFailure(Throwable caught) {
							Window.alert(MatContext.get().getMessageDelegate()
									.getGenericErrorMessage());
						}

						@Override
						public void onSuccess(List<QualityDataSetDTO> result) {
							QDSAppliedListModel appliedListModel = new QDSAppliedListModel();
							appliedListModel.setAppliedQDMs(result);
							searchDisplay.buildCellListWidget(appliedListModel);
						}
					});
		}
	}

	@Override
	public void beforeDisplay() {
		resetQDSFields();
		loadCodeListData();

	}

	@Override
	public void beforeClosingDisplay() {
	}

}
