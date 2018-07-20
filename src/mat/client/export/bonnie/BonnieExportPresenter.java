package mat.client.export.bonnie;

import com.google.gwt.user.client.ui.Widget;

import mat.client.MatPresenter;
import mat.client.measure.ManageMeasurePresenter;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.shared.ErrorMessageAlert;

public class BonnieExportPresenter implements MatPresenter {

	private BonnieExportView view;
	private ManageMeasurePresenter manageMeasurePresenter;
	private Result result; 
	
	public BonnieExportPresenter(BonnieExportView view, Result result, ManageMeasurePresenter manageMeasurePresenter) {
		this.view = view; 
		this.manageMeasurePresenter = manageMeasurePresenter;
		this.result = result; 
		addClickHandlers();
		initializeContent();
//		createErrorMessage("This is a message");
		setBonnieUserId("JaMeyer9286");
	}
	
	private void initializeContent() {
		this.view.getMeasureNameLink().setText(result.getShortName());
	}
	
	private void addClickHandlers() {
		this.view.getUploadButton().addClickHandler(event -> uploadButtonClickHandler());
		this.view.getCancelButton().addClickHandler(event -> cancelButtonClickHandler());
		this.view.getMeasureNameLink().addClickHandler(event -> measureLinkButtonClickHandler());
	}
	
	private void measureLinkButtonClickHandler() {
		this.manageMeasurePresenter.fireMeasureSelected(result);
	}
	
	private void uploadButtonClickHandler() {
		createErrorMessage("THIS IS AN ERROR");
	}
	
	private void cancelButtonClickHandler() {
		this.manageMeasurePresenter.displaySearch();
	}

	
	@Override
	public void beforeClosingDisplay() {
	}

	@Override
	public void beforeDisplay() {
	}

	@Override
	public Widget getWidget() {
		return view.asWidget();
	}
	
	public void createErrorMessage(String message) {
		view.getAlertPanel().clear();
		ErrorMessageAlert error = new ErrorMessageAlert();
		error.createAlert(message);
		view.getAlertPanel().add(error);
	}
	
	public void setBonnieUserId(String userId) {
		view.getBonnieIdLabel().setText(userId);
	}
}
