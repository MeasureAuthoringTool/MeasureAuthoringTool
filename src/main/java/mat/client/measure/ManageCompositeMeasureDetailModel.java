package mat.client.measure;

import com.google.gwt.user.client.rpc.IsSerializable;
import mat.client.measurepackage.MeasurePackageOverview;
import mat.model.BaseModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageCompositeMeasureDetailModel extends ManageMeasureDetailModel implements IsSerializable, BaseModel {

	private String compositeScoringMethod;
	private String compositeScoringAbbreviation; 
	private List<ManageMeasureSearchModel.Result> appliedComponentMeasures = new ArrayList<ManageMeasureSearchModel.Result>();
	private Map<String, String> aliasMapping = new HashMap<>();
	private Map<String, MeasurePackageOverview> packageMap = new HashMap<>();

	@Override
	public void scrubForMarkUp() {
		
	}
	
	public ManageCompositeMeasureDetailModel() {
		
	}

	public ManageCompositeMeasureDetailModel(ManageCompositeMeasureDetailModel originalManageCompositeMeasureDetailModel) {
		this.compositeScoringMethod = originalManageCompositeMeasureDetailModel.getCompositeScoringMethod();
		this.compositeScoringAbbreviation = originalManageCompositeMeasureDetailModel.getCompositeScoringAbbreviation();
		this.appliedComponentMeasures = new ArrayList<>();
		originalManageCompositeMeasureDetailModel.getAppliedComponentMeasures().stream().forEach(measure -> this.appliedComponentMeasures.add(new ManageMeasureSearchModel.Result(measure)));
		this.aliasMapping = new HashMap<>();
		originalManageCompositeMeasureDetailModel.getAliasMapping().keySet().stream().forEach(key -> this.aliasMapping.put(key, originalManageCompositeMeasureDetailModel.getAliasMapping().get(key)));
		this.packageMap = new HashMap<>();
		this.packageMap.putAll(originalManageCompositeMeasureDetailModel.packageMap);
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
	
	public Map<String, MeasurePackageOverview> getPackageMap() {
		return packageMap;
	}

	public void setPackageMap(Map<String, MeasurePackageOverview> packageMap) {
		this.packageMap = packageMap;
	}

	public String getCompositeScoringAbbreviation() {
		return compositeScoringAbbreviation;
	}

	public void setCompositeScoringAbbreviation(String compositeScoringAbbreviation) {
		this.compositeScoringAbbreviation = compositeScoringAbbreviation;
	}
}