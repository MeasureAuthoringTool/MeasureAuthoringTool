package mat.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import mat.client.measure.ManageMeasureDetailModel;

public class MeasureDetailsResult implements IsSerializable {
	private boolean isComposite;
	private ManageMeasureDetailModel manageMeasureDetailsModel;
	public ManageMeasureDetailModel getManageMeasureDetailsModel() {
		return manageMeasureDetailsModel;
	}
	public void setManageMeasureDetailsModel(ManageMeasureDetailModel manageMeasureDetailsModel) {
		this.manageMeasureDetailsModel = manageMeasureDetailsModel;
	}
	public boolean isComposite() {
		return isComposite;
	}
	public void setComposite(boolean isComposite) {
		this.isComposite = isComposite;
	}
}
