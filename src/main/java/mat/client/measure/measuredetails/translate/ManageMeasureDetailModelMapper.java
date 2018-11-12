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
		measureDetailsComponent.setOwnerUserId(manageMeasureDetailModel.getMeasureOwnerId());
		GeneralInformationModel generalInformationComponent = buildGeneralInformationComponent();
		measureDetailsComponent.setGeneralInformation(generalInformationComponent);
		return measureDetailsComponent;
	}

	private GeneralInformationModel buildGeneralInformationComponent() {
		GeneralInformationModel generalInformationModel = new GeneralInformationModel();
		generalInformationModel.setMeasureName(manageMeasureDetailModel.getName());
		generalInformationModel.seteCQMAbbreviatedTitle(manageMeasureDetailModel.getShortName());
		generalInformationModel.setFinalizedDate(manageMeasureDetailModel.getFinalizedDate());
		generalInformationModel.setPatientBased(manageMeasureDetailModel.isPatientBased());
		generalInformationModel.setGuid(manageMeasureDetailModel.getMeasureSetId());
		generalInformationModel.seteCQMVersionNumber(manageMeasureDetailModel.getVersionNumber());
		return generalInformationModel;
	}

	@Override
	public ManageMeasureDetailModel convertMeasureDetailsToModel() {
		// TODO Auto-generated method stub
		return null;
	}

}
