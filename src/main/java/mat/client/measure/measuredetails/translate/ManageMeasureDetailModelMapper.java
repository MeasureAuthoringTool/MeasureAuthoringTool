package mat.client.measure.measuredetails.translate;

import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.measuredetails.components.GeneralInformationComponent;
import mat.client.measure.measuredetails.components.MeasureDetailsComponent;

public class ManageMeasureDetailModelMapper implements MeasureDetailModelMapper{
	private ManageMeasureDetailModel manageMeasureDetailModel;
	private MeasureDetailsComponent measureDetailsComponent;
	
	public ManageMeasureDetailModelMapper(ManageMeasureDetailModel manageMeasureDetailModel) {
		this.manageMeasureDetailModel = manageMeasureDetailModel;
	}
	
	public ManageMeasureDetailModelMapper(MeasureDetailsComponent measureDetailsComponent) {
		this.measureDetailsComponent = measureDetailsComponent;
	}
	
	@Override
	public MeasureDetailsComponent getMeasureDetailsComponent() {
		measureDetailsComponent = new MeasureDetailsComponent();
		GeneralInformationComponent generalInformationComponent = buildGeneralInformationComponent();
		measureDetailsComponent.setGeneralInformation(generalInformationComponent);
		return measureDetailsComponent;
	}

	private GeneralInformationComponent buildGeneralInformationComponent() {
		GeneralInformationComponent generalInformationComponent = new GeneralInformationComponent();
		generalInformationComponent.seteCQMAbbreviatedTitle(manageMeasureDetailModel.getShortName());
		generalInformationComponent.setFinalizedDate(manageMeasureDetailModel.getFinalizedDate());
		generalInformationComponent.setPatientBased(manageMeasureDetailModel.isPatientBased());
		generalInformationComponent.setGuid(manageMeasureDetailModel.getMeasureSetId());
		generalInformationComponent.seteCQMVersionNumber(manageMeasureDetailModel.getVersionNumber());
		return generalInformationComponent;
	}

	@Override
	public ManageMeasureDetailModel convertMeasureDetailsToModel() {
		// TODO Auto-generated method stub
		return null;
	}

}
