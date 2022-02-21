package mat.client.export.measure;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.measure.ManageMeasurePresenter;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.shared.MatContext;

public class ManageMeasureExportPresenter implements MatPresenter {
	
	private final ManageMeasureExportView view;
	private final Result result;
	private final ManageMeasurePresenter manageMeasurePresenter;

	private static final String UNHANDLED_EXCEPTION = "Unhandled Exception: ";
	
	public ManageMeasureExportPresenter(ManageMeasureExportView view, Result result, ManageMeasurePresenter manageMeasurePresenter) {
		this.view = view; 
		this.result = result;
		this.manageMeasurePresenter = manageMeasurePresenter;
	
		addClickHandlers();
		intializeContent();
	}
	
	private void intializeContent() {
		this.view.getMeasureNameLink().setText(result.getName());
		this.view.getMeasureNameLink().setTitle(result.getName() + " link");
	}
	
	private void addClickHandlers() {
		this.view.getCancelButton().addClickHandler(event -> cancelButtonClickHandler());
		this.view.getOpenButton().addClickHandler(event -> openButtonClickHandler());
		this.view.getSaveButton().addClickHandler(event -> saveButtonClickHandler());
		this.view.getMeasureNameLink().addClickHandler(event -> measureNameLinkClickHandler());
	}
	
	private void measureNameLinkClickHandler() {
		this.manageMeasurePresenter.fireMeasureSelected(result);
	}
	
	private void cancelButtonClickHandler() {
		this.manageMeasurePresenter.displaySearch();
	}
	
	
	private void openButtonClickHandler() {
		if (view.isTransferToMadieRadio()) {
			transferMeasureToMadie();
		} else {
			Window.open(buildExportURL() + "&type=open", "_blank", "");
		}
	}

	private void saveButtonClickHandler() {
		if (view.isTransferToMadieRadio()) {
			transferMeasureToMadie();
		} else {
			Window.open(buildExportURL() + "&type=save", "_self", "");
		}
	}
	
	private String buildExportURL() {
		String url = GWT.getModuleBaseURL() + "export?id=" + result.getId() + "&format=";
		System.out.println("URL: " + url);

		url += (view.isHQMF() ? "hqmf" : view.isHumanReadable() ? "humanreadable" : view.isSimpleXML() ? "simplexml" : 
			view.isCQLLibrary() ? "cqlLibrary" : view.isELM() ? "elm" : view.isJSON() ? "json" : view.isXml() ? "xml" : "zip");
		return url;
	}

	private void transferMeasureToMadie() {
		Mat.showLoadingMessage();
		MatContext.get().getMeasureService().transferMeasureToMadie(result.geteMeasureId(), new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable throwable) {
				Mat.hideLoadingMessage();
				view.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, UNHANDLED_EXCEPTION + throwable.getLocalizedMessage(), 0);
			}

			@Override
			public void onSuccess(Boolean success) {
				Mat.hideLoadingMessage();
				if (success) {
					view.displaySuccessMessage("Measure is being processed and transferred to MADiE. A message will be spent to the e-mail associated with this account once the transfer has completed.");
				} else {
					view.displayErrorMessage("Unable to transfer measure to MADiE, Try again");
				}
			}
		});
	}

	@Override
	public void beforeClosingDisplay() {
		view.clearAlerts();
	}

	@Override
	public void beforeDisplay() {
		view.clearAlerts();
	}

	@Override
	public Widget getWidget() {
		return null;
	}

}
