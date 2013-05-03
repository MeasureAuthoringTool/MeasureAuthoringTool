package mat.client.measurepackage;

import java.util.ArrayList;
import java.util.List;

import mat.model.QualityDataSetDTO;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MeasurePackageDetail implements IsSerializable, Comparable<MeasurePackageDetail>{
	private String sequence;
	private String measureId;
	private List<MeasurePackageClauseDetail> packageClauses = new ArrayList<MeasurePackageClauseDetail>();
	private List<QualityDataSetDTO> qdmElements = new ArrayList<QualityDataSetDTO>();
	private List<QualityDataSetDTO> suppDataElements = new ArrayList<QualityDataSetDTO>();
	private String valueSetDate;
	
	public String getPackageName() {
		return "Measure Grouping " + getSequence();
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String id) {
		this.sequence = id;
	}
	public List<MeasurePackageClauseDetail> getPackageClauses() {
		return packageClauses;
	}
	public void setPackageClauses(List<MeasurePackageClauseDetail> packageClauses) {
		this.packageClauses = packageClauses;
	}
	public String getMeasureId() {
		return measureId;
	}
	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}
	public String getValueSetDate() {
		return valueSetDate;
	}
	public void setValueSetDate(String valueSetDate) {
		this.valueSetDate = valueSetDate;
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
	@Override
	public int compareTo(MeasurePackageDetail detail) {
		Integer seq1 = Integer.parseInt(this.sequence);
		Integer seq2 = Integer.parseInt(detail.getSequence());
		// TODO Auto-generated method stub
		return seq1.compareTo(seq2);
	}
	
	

}