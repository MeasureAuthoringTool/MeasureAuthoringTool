package mat.client.export;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import mat.client.MatPresenter;
import mat.client.export.bonnie.BonnieExportView;
import mat.client.export.measure.ManageMeasureExportPresenter;
import mat.client.export.measure.ManageMeasureExportView;
import mat.client.measure.ManageMeasurePresenter;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.shared.MatContext;

import java.util.logging.Level;
import java.util.logging.Logger;

import static mat.model.clause.ModelTypeHelper.isFhir;

public class ManageExportPresenter implements MatPresenter {
	
	private final ManageExportView view;
	private ManageMeasureExportView exportView;
	private BonnieExportView bonnieExportView; 
	private final ManageMeasureSearchModel.Result result;

	private final Boolean isTransferableToMadie;
	private final ManageMeasurePresenter manageMeasurePresenter;

	private static final Logger logger = Logger.getLogger(ManageExportPresenter.class.getSimpleName());

	public ManageExportPresenter(ManageExportView view,
								 ManageMeasureSearchModel.Result result,
								 ManageMeasurePresenter manageMeasurePresenter, Boolean isTransferableToMadie) {
		this.view = view; 
		this.result = result;
		this.manageMeasurePresenter = manageMeasurePresenter;
		this.isTransferableToMadie = isTransferableToMadie;

		initializeExportView();

		// On initial load "Upload to Bonnie" is not displayed.
		setBonnieExportVisible(false);
		if (!isFhir(result.getMeasureModel())) {
			initializeBonnieExportView();
		}
		addClickHandlers();
		setMeasureExportHeader();
	}

	private void initializeExportView() {
		this.view.getExportItem().setActive(true);
		this.view.getExportPane().setActive(true);
		view.getExportPane().clear();
		exportView = new ManageMeasureExportView();
		new ManageMeasureExportPresenter(exportView, result, manageMeasurePresenter);
		boolean isStaleQdm = !isFhir(result.getMeasureModel()) && !isLatestQdm();
		exportView.setExportOptionsBasedOnVersion(result.getHqmfReleaseVersion(), result.getIsComposite(), result.getMeasureModel(), isTransferableToMadie, isStaleQdm);
		if (!isTransferableToMadie && !result.getIsComposite()) {
			exportView.displayErrorMessage("This measure cannot be transferred to MADiE because either you are not the owner or a version of this measure has already been transferred.");
		}
		this.view.getExportPane().add(exportView.asWidget());
		exportView.showCompositeMeasure(result.getIsComposite());
	}

	private boolean isLatestQdm() {
		double qdmVersion = 0.0;
		boolean isLatest = false;
		if (result.getQdmVersion() != null) {
			qdmVersion = Double.parseDouble(result.getQdmVersion().replace("v", ""));
		}
		try {
			double currentQDMVersion = Double.parseDouble(MatContext.get().getCurrentQDMVersion());
			isLatest = MatContext.get().isCQLMeasure(result.getHqmfReleaseVersion()) && qdmVersion == currentQDMVersion;
		} catch (NumberFormatException nfe) {
			logger.log(Level.SEVERE, "ManageExportPresenter::isLatestQdm -> Invalid MatContext.get().getCurrentQDMVersion()");
			Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
		}
		return isLatest;
	}

	private void initializeBonnieExportView() {
		view.getBonnieExportPane().clear();
		this.view.getBonnieExportItem().setActive(false);
		this.view.getBonnieExportPane().setActive(false);

		bonnieExportView = new BonnieExportView();
		// unused BonnieExportPresenter
		// BonnieExportPresenter bonnieExportPresenter = new BonnieExportPresenter(bonnieExportView, result, manageMeasurePresenter);

		this.view.getBonnieExportPane().add(bonnieExportView.asWidget());

		// "Upload to Bonnie" is displayed to user only if the measure is QDM, isCQLMeasure and uses latest QDM version.
		double qdmVersion = 0.0; 
		if (result.getQdmVersion() != null) {
			qdmVersion = Double.parseDouble(result.getQdmVersion().replace("v", ""));
		}
		try {
			double currentQDMVersion = Double.parseDouble(MatContext.get().getCurrentQDMVersion());
			setBonnieExportVisible(MatContext.get().isCQLMeasure(result.getHqmfReleaseVersion()) && qdmVersion == currentQDMVersion);
		} catch (NumberFormatException nfe) {
			logger.log(Level.SEVERE, "ManageExportPresenter::initializeBonnieExportView -> Invalid MatContext.get().getCurrentQDMVersion()");
			Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
		}
	}
	
	private void setBonnieExportVisible(boolean isVisible) {
		this.view.getBonnieExportItem().setVisible(isVisible);
		this.view.getBonnieExportPane().setVisible(isVisible);
	}
	
	private void addClickHandlers() {
		this.view.getExportItem().addClickHandler(event -> exportItemClickHandler());
		this.view.getBonnieExportItem().addClickHandler(event -> bonnieExportItemClickHandler());
	}
	
	private void exportItemClickHandler() {
		this.view.getExportItem().setActive(true);
		this.view.getExportPane().setActive(true);
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
	
	private void setMeasureExportHeader() {
		if (result.getIsComposite()) {
			this.manageMeasurePresenter.getPanel().setHeading("My Measures > Export Composite Measure", "MeasureLibrary");
		} else {
			this.manageMeasurePresenter.getPanel().setHeading("My Measures > Export", "MeasureLibrary");
		}
	}
}
