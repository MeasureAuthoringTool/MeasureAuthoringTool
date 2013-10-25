package mat.client.measurepackage;

import java.util.ArrayList;
import java.util.List;

import mat.model.QualityDataSetDTO;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class MeasurePackageDetail.
 */
public class MeasurePackageDetail implements IsSerializable, Comparable<MeasurePackageDetail>{
	
	/** The sequence. */
	private String sequence;
	
	/** The measure id. */
	private String measureId;
	
	/** The package clauses. */
	private List<MeasurePackageClauseDetail> packageClauses = new ArrayList<MeasurePackageClauseDetail>();
	
	/** The qdm elements. */
	private List<QualityDataSetDTO> qdmElements = new ArrayList<QualityDataSetDTO>();
	
	/** The supp data elements. */
	private List<QualityDataSetDTO> suppDataElements = new ArrayList<QualityDataSetDTO>();
	
	/** The value set date. */
	private String valueSetDate;
	
	/**
	 * Gets the package name.
	 * 
	 * @return the package name
	 */
	public String getPackageName() {
		return "Measure Grouping " + getSequence();
	}
	
	/**
	 * Gets the sequence.
	 * 
	 * @return the sequence
	 */
	public String getSequence() {
		return sequence;
	}
	
	/**
	 * Sets the sequence.
	 * 
	 * @param id
	 *            the new sequence
	 */
	public void setSequence(String id) {
		this.sequence = id;
	}
	
	/**
	 * Gets the package clauses.
	 * 
	 * @return the package clauses
	 */
	public List<MeasurePackageClauseDetail> getPackageClauses() {
		return packageClauses;
	}
	
	/**
	 * Sets the package clauses.
	 * 
	 * @param packageClauses
	 *            the new package clauses
	 */
	public void setPackageClauses(List<MeasurePackageClauseDetail> packageClauses) {
		this.packageClauses = packageClauses;
	}
	
	/**
	 * Gets the measure id.
	 * 
	 * @return the measure id
	 */
	public String getMeasureId() {
		return measureId;
	}
	
	/**
	 * Sets the measure id.
	 * 
	 * @param measureId
	 *            the new measure id
	 */
	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}
	
	/**
	 * Gets the value set date.
	 * 
	 * @return the value set date
	 */
	public String getValueSetDate() {
		return valueSetDate;
	}
	
	/**
	 * Sets the value set date.
	 * 
	 * @param valueSetDate
	 *            the new value set date
	 */
	public void setValueSetDate(String valueSetDate) {
		this.valueSetDate = valueSetDate;
	}
	
	/**
	 * Gets the qdm elements.
	 * 
	 * @return the qdm elements
	 */
	public List<QualityDataSetDTO> getQdmElements() {
		return qdmElements;
	}
	
	/**
	 * Sets the qdm elements.
	 * 
	 * @param qdmElements
	 *            the new qdm elements
	 */
	public void setQdmElements(List<QualityDataSetDTO> qdmElements) {
		this.qdmElements = qdmElements;
	}
	
	/**
	 * Gets the supp data elements.
	 * 
	 * @return the supp data elements
	 */
	public List<QualityDataSetDTO> getSuppDataElements() {
		return suppDataElements;
	}
	
	/**
	 * Sets the supp data elements.
	 * 
	 * @param suppDataElements
	 *            the new supp data elements
	 */
	public void setSuppDataElements(List<QualityDataSetDTO> suppDataElements) {
		this.suppDataElements = suppDataElements;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(MeasurePackageDetail detail) {
		Integer seq1 = Integer.parseInt(this.sequence);
		Integer seq2 = Integer.parseInt(detail.getSequence());
		// TODO Auto-generated method stub
		return seq1.compareTo(seq2);
	}
	
	

}
