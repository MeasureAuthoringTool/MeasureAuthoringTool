package mat.client.export;

import com.google.gwt.user.client.ui.Widget;
import mat.client.MatPresenter;
import mat.client.export.bonnie.BonnieExportPresenter;
import mat.client.export.bonnie.BonnieExportView;
import mat.client.export.measure.ManageMeasureExportPresenter;
import mat.client.export.measure.ManageMeasureExportView;
import mat.client.measure.ManageMeasurePresenter;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.shared.MatContext;
import mat.client.util.FeatureFlagConstant;

import static mat.model.clause.ModelTypeHelper.isFhir;

public class ManageExportPresenter implements MatPresenter {
	
	private ManageExportView view;
	private ManageMeasureExportView exportView;
	private BonnieExportView bonnieExportView; 
	private ManageMeasureSearchModel.Result result; 
	
	private ManageMeasurePresenter manageMeasurePresenter;

	public ManageExportPresenter(ManageExportView view, ManageMeasureSearchModel.Result result, ManageMeasurePresenter manageMeasurePresenter) {
		this.view = view; 
		this.setResult(result); 
		this.setManageMeasurePresenter(manageMeasurePresenter);
	
		initializeExportView();
		initializeBonnieExportView();
		
		addClickHandlers();
		setMeasureExportHeader();
	}

	private void initializeBonnieExportView() {
		view.getBonnieExportPane().clear();

		this.view.getBonnieExportItem().setActive(false);
		this.view.getBonnieExportPane().setActive(false);
		bonnieExportView = new BonnieExportView();
		BonnieExportPresenter bonnieExportPresenter = new BonnieExportPresenter(bonnieExportView, result, manageMeasurePresenter);
		this.view.getBonnieExportPane().add(bonnieExportView.asWidget());
		
		double qdmVersion = 0.0; 
		if(result.getQdmVersion() != null) {
			qdmVersion = Double.parseDouble(result.getQdmVersion().replace("v", ""));
		}
		
		double currentQDMVersion = Double.parseDouble(MatContext.get().getCurrentQDMVersion().replace("v", ""));	
		
		if(MatContext.get().isCQLMeasure(result.getHqmfReleaseVersion()) && qdmVersion == currentQDMVersion) {
			setBonnieExportVisible(true);
		} else {
			setBonnieExportVisible(false);
		}

		if (!MatContext.get().getFeatureFlagStatus(FeatureFlagConstant.FHIR_BONNIE) &&
				isFhir(result.getMeasureModel())) {
			setBonnieExportVisible(false);
		}
	}
	
	private void setBonnieExportVisible(boolean isVisible) {
		this.view.getBonnieExportItem().setVisible(isVisible);
		this.view.getBonnieExportPane().setVisible(isVisible);
	}

	private void initializeExportView() {
		this.view.getExportItem().setActive(true);
		this.view.getExportPane().setActive(true);
		view.getExportPane().clear();
		exportView = new ManageMeasureExportView(true);
		ManageMeasureExportPresenter exportPresenter = new ManageMeasureExportPresenter(exportView, result, manageMeasurePresenter);
		exportView.setExportOptionsBasedOnVersion(result);
		this.view.getExportPane().add(exportView.asWidget());
		exportView.showCompositeMeasure(result.getIsComposite());
	}
	
	private void addClickHandlers() {
		this.view.getExportItem().addClickHandler(event -> exportItemClickHandler());
		this.view.getBonnieExportItem().addClickHandler(event -> bonnieExportItemClickHandler());
	}
	
	private void exportItemClickHandler() {
		this.view.getExportItem().setActive(true);
		this.view.getExportPane().setActive(true);;
		this.view.getBonnieExportItem().setActive(false);
		this.view.getBonnieExportPane().setActive(false);
		
		setMeasureExportHeader();
	}
	
	private void bonnieExportItemClickHandler() {
		this.view.getExportItem().setActive(false);
		this.view.getExportPane().setActive(false);
		this.view.getBonnieExportItem().setActive(true);
		this.view.getBonnieExportPane().setActive(true);
		
		this.manageMeasurePresenter.getPanel().setHeading("My Measures > Upload to Bonnie", "MeasureLibrary");
	}
	
	@Override
	public void beforeClosingDisplay() {
		this.exportView.asWidget().removeFromParent();
		this.bonnieExportView.asWidget().removeFromParent();
	}

	@Override
	public void beforeDisplay() {
		this.exportView.asWidget().removeFromParent();
		this.bonnieExportView.asWidget().removeFromParent();
	}

	@Override
	public Widget getWidget() {
		return view.asWidget();
	}

	public ManageMeasureSearchModel.Result getResult() {
		return result;
	}

	public void setResult(ManageMeasureSearchModel.Result result) {
		this.result = result;
	}

	public ManageMeasurePresenter getManageMeasurePresenter() {
		return manageMeasurePresenter;
	}

	public void setManageMeasurePresenter(ManageMeasurePresenter manageMeasurePresenter) {
		this.manageMeasurePresenter = manageMeasurePresenter;
	}
	
	private void setMeasureExportHeader() {
		if(result.getIsComposite()) {
			this.manageMeasurePresenter.getPanel().setHeading("My Measures > Export Composite Measure", "MeasureLibrary");
		} else {
			this.manageMeasurePresenter.getPanel().setHeading("My Measures > Export", "MeasureLibrary");
		}
	}
}
