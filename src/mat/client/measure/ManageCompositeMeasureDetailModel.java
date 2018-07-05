package mat.client.measure;

import com.google.gwt.user.client.rpc.IsSerializable;

import mat.model.BaseModel;

public class ManageCompositeMeasureDetailModel extends ManageMeasureDetailModel implements IsSerializable, BaseModel {

	private String compositeScoringMethod;
	
	
	@Override
	public void scrubForMarkUp() {
		
	}


	public String getCompositeScoringMethod() {
		return compositeScoringMethod;
	}


	public void setCompositeScoringMethod(String compositeScoringMethod) {
		this.compositeScoringMethod = compositeScoringMethod;
	}
}
