package mat.client.measure.measuredetails.translate;

import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.measuredetails.components.GeneralInformationModel;
import mat.client.measure.measuredetails.components.MeasureDetailsModel;

public class ManageMeasureDetailModelMapper implements MeasureDetailModelMapper{
	private ManageMeasureDetailModel manageMeasureDetailModel;
	private MeasureDetailsModel measureDetailsComponent;
	
	public ManageMeasureDetailModelMapper(ManageMeasureDetailModel manageMeasureDetailModel) {
		this.manageMeasureDetailModel = manageMeasureDetailModel;
	}
	
	public ManageMeasureDetailModelMapper(MeasureDetailsModel measureDetailsComponent) {
		this.measureDetailsComponent = measureDetailsComponent;
	}
	
	@Override
	public MeasureDetailsModel getMeasureDetailsComponent() {
		measureDetailsComponent = new MeasureDetailsModel();
		GeneralInformationModel generalInformationComponent = buildGeneralInformationComponent();
		measureDetailsComponent.setGeneralInformation(generalInformationComponent);
		return measureDetailsComponent;
	}

	private GeneralInformationModel buildGeneralInformationComponent() {
		GeneralInformationModel generalInformationComponent = new GeneralInformationModel();
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
