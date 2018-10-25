package mat.client.measure.measuredetails.view;

import mat.client.shared.MeasureDetailsConstants.MeasureDetailsItems;

public class MeasureDetailsViewFactory {
	private static MeasureDetailsViewFactory instance;
	
	public static MeasureDetailsViewFactory get() {
		if(instance == null) {
			instance = new MeasureDetailsViewFactory();
		}
		return instance;
	}
	
	public ComponentDetailView getMeasureDetailComponentView(MeasureDetailsItems measureDetail) {
		// TODO Auto-generated method stub
		switch(measureDetail) {
		case GENERAL_MEASURE_INFORMATION:
			default:
			return new GeneralMeasureInformationView();
		}
	}

}
