package mat.client.measure.measuredetails.translate;

import mat.client.measure.ManageCompositeMeasureDetailModel;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.measuredetails.components.GeneralInformationModel;
import mat.client.measure.measuredetails.components.MeasureDetailsModel;

public class ManageMeasureDetailModelMapper implements MeasureDetailModelMapper{
	private ManageMeasureDetailModel manageMeasureDetailModel;
	private MeasureDetailsModel measureDetailsModel;
	
	public ManageMeasureDetailModelMapper(ManageMeasureDetailModel manageMeasureDetailModel) {
		this.manageMeasureDetailModel = manageMeasureDetailModel;
	}
	
	public ManageMeasureDetailModelMapper(MeasureDetailsModel measureDetailsComponent) {
		this.measureDetailsModel = measureDetailsComponent;
	}
	
	@Override
	public MeasureDetailsModel getMeasureDetailsModel(boolean isCompositeMeasure) {
		measureDetailsModel = new MeasureDetailsModel();
		measureDetailsModel.setOwnerUserId(manageMeasureDetailModel.getMeasureOwnerId());
		measureDetailsModel.setComposite(isCompositeMeasure);
		measureDetailsModel.setScoringType(manageMeasureDetailModel.getScoringAbbr());
		GeneralInformationModel generalInformationComponent = buildGeneralInformationComponent();
		measureDetailsModel.setGeneralInformation(generalInformationComponent);
		return measureDetailsModel;
	}

	private GeneralInformationModel buildGeneralInformationComponent() {
		GeneralInformationModel generalInformationModel = new GeneralInformationModel();
		generalInformationModel.setMeasureName(manageMeasureDetailModel.getName());
		generalInformationModel.seteCQMAbbreviatedTitle(manageMeasureDetailModel.getShortName());
		generalInformationModel.setFinalizedDate(manageMeasureDetailModel.getFinalizedDate());
		generalInformationModel.setPatientBased(manageMeasureDetailModel.isPatientBased());
		generalInformationModel.setGuid(manageMeasureDetailModel.getMeasureSetId());
		generalInformationModel.seteCQMVersionNumber(manageMeasureDetailModel.getVersionNumber());
		if(manageMeasureDetailModel instanceof ManageCompositeMeasureDetailModel) {
			generalInformationModel.setCompositeScoringMethod(((ManageCompositeMeasureDetailModel) manageMeasureDetailModel).getCompositeScoringMethod());
		}

		return generalInformationModel;
	}

	@Override
	public ManageMeasureDetailModel convertMeasureDetailsToModel() {
		// TODO Auto-generated method stub
		return null;
	}
}
