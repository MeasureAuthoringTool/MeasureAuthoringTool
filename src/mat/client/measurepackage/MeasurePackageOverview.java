package mat.client.measurepackage;

import java.util.ArrayList;
import java.util.List;

import mat.model.QualityDataSetDTO;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MeasurePackageOverview implements IsSerializable{
	private List<MeasurePackageClauseDetail> clauses = new ArrayList<MeasurePackageClauseDetail>();
	private List<MeasurePackageDetail> packages = new ArrayList<MeasurePackageDetail>();
	private List<QualityDataSetDTO> qdmElements = new ArrayList<QualityDataSetDTO>();
	private List<QualityDataSetDTO> suppDataElements = new ArrayList<QualityDataSetDTO>();
	public List<MeasurePackageClauseDetail> getClauses() {
		return clauses;
	}
	public void setClauses(List<MeasurePackageClauseDetail> clauses) {
		this.clauses = clauses;
	}
	public List<MeasurePackageDetail> getPackages() {
		return packages;
	}
	public void setPackages(List<MeasurePackageDetail> packages) {
		this.packages = packages;
	}
	public List<QualityDataSetDTO> getQdmElements() {
		return qdmElements;
	}
	public void setQdmElements(List<QualityDataSetDTO> qdmElements) {
		this.qdmElements = qdmElements;
	}
	public List<QualityDataSetDTO> getSuppDataElements() {
		return suppDataElements;
	}
	public void setSuppDataElements(List<QualityDataSetDTO> suppDataElements) {
		this.suppDataElements = suppDataElements;
	}	
	
	
	
}
