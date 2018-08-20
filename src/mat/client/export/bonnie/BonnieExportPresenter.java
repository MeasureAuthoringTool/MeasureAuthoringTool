package mat.client.export.bonnie;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.measure.ManageMeasurePresenter;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.MatContext;
import mat.client.shared.SuccessMessageAlert;
import mat.client.umls.service.VsacTicketInformation;
import mat.shared.StringUtility;
import mat.shared.bonnie.error.BonnieServerException;
import mat.shared.bonnie.error.BonnieUnauthorizedException;
import mat.shared.bonnie.error.UMLSNotActiveException;
import mat.shared.bonnie.result.BonnieUserInformationResult;

public class BonnieExportPresenter implements MatPresenter {

	private static final String SIGN_INTO_BONNIE_MESSAGE = "Please sign into Bonnie.";
	private static final String SIGN_INTO_UMLS = "Please sign into UMLS";
	public static final String UNABLE_TO_CONNECT_TO_BONNIE_MESSAGE = "Unable to connect to Bonnie at this time. Please try again. If the problem persists, contact the MAT Support Desk.";
	
	private BonnieExportView view;
	private ManageMeasurePresenter manageMeasurePresenter;
	private Result result; 
	
	public BonnieExportPresenter(BonnieExportView view, Result result, ManageMeasurePresenter manageMeasurePresenter) {
		this.view = view; 
		this.manageMeasurePresenter = manageMeasurePresenter;
		this.result = result; 
		addClickHandlers();
		initializeContent();
		getBonnieUserInformation();
		clearMessagePanel();
	}
	
	private void getBonnieUserInformation() {
		String matUserId = MatContext.get().getLoggedinUserId();
		
		MatContext.get().getBonnieService().getBonnieUserInformationForUser(matUserId, new AsyncCallback<BonnieUserInformationResult>() {
			
			@Override
			public void onSuccess(BonnieUserInformationResult result) {
				setBonnieUserId(result.getBonnieUsername());
				view.getBonnieSignOutButton().setVisible(true);
				view.getUploadButton().setEnabled(true);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				if(caught instanceof BonnieUnauthorizedException) {
					view.getBonnieSignOutButton().setVisible(false);
					view.getUploadButton().setEnabled(false);
					createErrorMessage(SIGN_INTO_BONNIE_MESSAGE);
				}
				
				else if(caught instanceof BonnieServerException) {
					view.getBonnieSignOutButton().setVisible(false);
					view.getUploadButton().setEnabled(false);
					createErrorMessage(UNABLE_TO_CONNECT_TO_BONNIE_MESSAGE);
				} 
								
				else {
					Window.alert(UNABLE_TO_CONNECT_TO_BONNIE_MESSAGE);
				}
			}
		});
	}

	
	private void initializeContent() {
		this.view.getMeasureNameLink().setText(result.getName());
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
		Mat.showLoadingMessage();
		clearMessagePanel();
		MatContext.get().getVsacapiServiceAsync().getTicketGrantingToken(new AsyncCallback<VsacTicketInformation>() {
			
			@Override
			public void onSuccess(VsacTicketInformation result) {
				if(result == null) {
					createErrorMessage(SIGN_INTO_UMLS);
					Mat.hideLoadingMessage();
				}
				else {
					getMeasureExportForMeasure(result);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
					Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}
		});
		
		
		
	}
	
	private void getMeasureExportForMeasure(VsacTicketInformation vsacTicket) {
		String matUserId = MatContext.get().getLoggedinUserId();
		String measureId = this.result.getId();
		MatContext.get().getBonnieService().getUpdateOrUploadMeasureToBonnie(measureId, matUserId, vsacTicket, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				
				if(caught instanceof UMLSNotActiveException) {
					createErrorMessage(SIGN_INTO_UMLS);
				}
				if(caught instanceof BonnieUnauthorizedException) {
					view.getBonnieSignOutButton().setVisible(false);
					view.getUploadButton().setEnabled(false);
					createErrorMessage(SIGN_INTO_BONNIE_MESSAGE);
				} else {
					Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				}
				Mat.hideLoadingMessage();
			}

			@Override
			public void onSuccess(String result) {
				clearMessagePanel();
				getExportFromBonnieForMeasure(measureId, matUserId, result);
			}

		});
	}
	private void cancelButtonClickHandler() {
		this.manageMeasurePresenter.displaySearch();
	}

	private void getExportFromBonnieForMeasure(String measureId, String matUserId, String successMessage) {
		String url = GWT.getModuleBaseURL() + "export?id=" + result.getId() + "&userId=" + matUserId + "&format=calculateBonnieMeasureResult";
		Window.open(url + "&type=open", "_blank", "");
		createSuccessMessage(successMessage);
		Mat.hideLoadingMessage();
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
	
	public void createSuccessMessage(String message) {
		view.getAlertPanel().clear();
		SuccessMessageAlert success = new SuccessMessageAlert();
		success.createAlert(message);
		view.getAlertPanel().add(success);
	}
	
	public void clearMessagePanel() {
		view.getAlertPanel().clear();
	}
	
	public void setBonnieUserId(String userId) {
		view.getBonnieIdLabel().setText(userId);
	}
}
