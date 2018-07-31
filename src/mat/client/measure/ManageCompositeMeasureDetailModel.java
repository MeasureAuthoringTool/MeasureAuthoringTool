package mat.client.measure;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

import mat.model.BaseModel;

public class ManageCompositeMeasureDetailModel extends ManageMeasureDetailModel implements IsSerializable, BaseModel {

	private String compositeScoringMethod;
	List<ManageMeasureSearchModel.Result> appliedComponentMeasures;
	Map<String, String> aliasMapping;

	@Override
	public void scrubForMarkUp() {
		
	}

	public String getCompositeScoringMethod() {
		return compositeScoringMethod;
	}


	public void setCompositeScoringMethod(String compositeScoringMethod) {
		this.compositeScoringMethod = compositeScoringMethod;
	}
	
	public List<ManageMeasureSearchModel.Result> getAppliedComponentMeasures() {
		return appliedComponentMeasures;
	}


	public void setAppliedComponentMeasures(List<ManageMeasureSearchModel.Result> appliedComponentMeasures) {
		this.appliedComponentMeasures = appliedComponentMeasures;
	}
	
	public Map<String, String> getAliasMapping() {
		return aliasMapping;
	}

	public void setAliasMapping(Map<String, String> aliasMapping) {
		this.aliasMapping = aliasMapping;
	}
}