package mat.client.export.measure;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

import mat.client.MatPresenter;
import mat.client.measure.ManageMeasurePresenter;
import mat.client.measure.ManageMeasureSearchModel.Result;

public class ManageMeasureExportPresenter implements MatPresenter {
	
	private ManageMeasureExportView view; 
	private Result result; 
	private ManageMeasurePresenter manageMeasurePresenter;
	
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
		Window.open(buildExportURL() + "&type=open", "_blank", "");
	}

	private void saveButtonClickHandler() {
		Window.open(buildExportURL() + "&type=save", "_self", "");
	}
	
	private String buildExportURL() {
		String url = GWT.getModuleBaseURL() + "export?id=" + result.getId() + "&format=";
		System.out.println("URL: " + url);

		url += (view.isHQMF() ? "hqmf" :
				view.isHumanReadable() ? "humanreadable" :
						view.isSimpleXML() ? "simplexml" :
								view.isMatXML() ? "matxml" :
										view.isCQLLibrary() ? "cqlLibrary" :
												view.isELM() ? "elm" :
														view.isJSON() ? "json" :
																"zip");
		return url;
	}

	@Override
	public void beforeClosingDisplay() {
	}

	@Override
	public void beforeDisplay() {
	}

	@Override
	public Widget getWidget() {
		return null;
	}

}
