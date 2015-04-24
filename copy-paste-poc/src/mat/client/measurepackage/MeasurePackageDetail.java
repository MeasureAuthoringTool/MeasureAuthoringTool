package mat.client.measurepackage;

import java.util.ArrayList;
import java.util.List;

import mat.model.Author;
import mat.model.MeasureType;
import mat.model.QualityDataSetDTO;
import mat.model.RiskAdjustmentDTO;

import com.google.gwt.user.client.rpc.IsSerializable;

// TODO: Auto-generated Javadoc
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
	
	/** The to compare package clauses. */
	private List<MeasurePackageClauseDetail> toComparePackageClauses;
	
	/** The to compare supp data elements. */
	private List<QualityDataSetDTO> toCompareSuppDataElements;
	//riskAdj
	/** The risk adj clauses. */
	private List<RiskAdjustmentDTO> riskAdjClauses = new ArrayList<RiskAdjustmentDTO>();
	
	/** The risk adj vars. */
	private List<RiskAdjustmentDTO> riskAdjVars = new ArrayList<RiskAdjustmentDTO>();
	
	/** The to compare risk adj vars. */
	private List<RiskAdjustmentDTO> toCompareRiskAdjVars;

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
	
	/**
	 * Gets the to compare package clauses.
	 *
	 * @return the to compare package clauses
	 */
	public List<MeasurePackageClauseDetail> getToComparePackageClauses() {
		return toComparePackageClauses;
	}

	/**
	 * Sets the to compare package clauses.
	 *
	 * @param toComparePackageClauses the new to compare package clauses
	 */
	public void setToComparePackageClauses(
			List<MeasurePackageClauseDetail> toComparePackageClauses) {
		this.toComparePackageClauses = toComparePackageClauses;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((toCompareRiskAdjVars == null) ? 0 : toCompareRiskAdjVars
						.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MeasurePackageDetail other = (MeasurePackageDetail) obj;
		if (toComparePackageClauses == null) {
			if (other.toComparePackageClauses != null) {
				return false;
			}
		} else if (!isEqual(toComparePackageClauses, other.toComparePackageClauses)) {
			return false;
		}
		
		if (toCompareSuppDataElements == null) {
			if (other.toCompareSuppDataElements != null) {
				return false;
			}
		} else if (!isEqual(toCompareSuppDataElements, other.toCompareSuppDataElements)) {
			return false;
		}
		
		if (toCompareRiskAdjVars == null) {
			if (other.toCompareRiskAdjVars != null)
				return false;
		} else if (!isEqual(toCompareRiskAdjVars,other.toCompareRiskAdjVars))
			return false;
		return true;
	}
	
	/**
	 * Checks if is equal.
	 *
	 * @param listA the list a
	 * @param listB the list b
	 * @return true, if is equal
	 */
	public boolean isEqual(List listA, List listB) {
		if (listA.size() != listB.size()) {
			return false;
		}
		for (int i = 0; i < listA.size(); i++) {
			 if (listA.get(i) instanceof MeasurePackageClauseDetail) {
				 MeasurePackageClauseDetail val1 = (MeasurePackageClauseDetail) listA.get(i);
				 MeasurePackageClauseDetail val2 = (MeasurePackageClauseDetail) listB.get(i);
				if (val1.compareTo(val2) != 0) {
					return false;
				} else if(!isEqual(val1.getItemCountList(), val2.getDbItemCountList())) { 
					return false;
				} else if((val1.getAssociatedPopulationUUID()!=null) && (val2.getDbAssociatedPopulationUUID()!=null)) { 
					if(val1.getAssociatedPopulationUUID()
							.compareTo(val2.getDbAssociatedPopulationUUID()) != 0){
						return false;	
					}
				} else if((val1.getAssociatedPopulationUUID()!=null) && (val2.getDbAssociatedPopulationUUID()==null)) { 
						return false;	
				} else if((val1.getAssociatedPopulationUUID()==null) && (val2.getDbAssociatedPopulationUUID()!=null)) { 
					   return false;	
			   } 
				
			} else if (listA.get(i) instanceof QualityDataSetDTO) {
				 QualityDataSetDTO val1 = (QualityDataSetDTO) listA.get(i);
				 QualityDataSetDTO val2 = (QualityDataSetDTO) listB.get(i);
				if (val1.compare(val1, val2) != 0) {
					return false;
				}
			} else if (listA.get(i) instanceof RiskAdjustmentDTO) {
				RiskAdjustmentDTO val1 = (RiskAdjustmentDTO) listA.get(i);
				 RiskAdjustmentDTO val2 = (RiskAdjustmentDTO) listB.get(i);
				if (val1.compare(val1, val2) != 0) {
					return false;
				}
			}
			 
		}
		return true;
	}

	
	/**
	 * Gets the to compare supp data elements.
	 *
	 * @return the to compare supp data elements
	 */
	public List<QualityDataSetDTO> getToCompareSuppDataElements() {
		return toCompareSuppDataElements;
	}

	/**
	 * Sets the to compare supp data elements.
	 *
	 * @param toCompareSuppDataElements the new to compare supp data elements
	 */
	public void setToCompareSuppDataElements(
			List<QualityDataSetDTO> toCompareSuppDataElements) {
		this.toCompareSuppDataElements = toCompareSuppDataElements;
	}

	/**
	 * Gets the risk adj clauses.
	 *
	 * @return the risk adj clauses
	 */
	public List<RiskAdjustmentDTO> getRiskAdjClauses() {
		return riskAdjClauses;
	}

	/**
	 * Sets the risk adj clauses.
	 *
	 * @param riskAdjClauses the new risk adj clauses
	 */
	public void setRiskAdjClauses(List<RiskAdjustmentDTO> riskAdjClauses) {
		this.riskAdjClauses = riskAdjClauses;
	}

	/**
	 * Gets the risk adj vars.
	 *
	 * @return the risk adj vars
	 */
	public List<RiskAdjustmentDTO> getRiskAdjVars() {
		return riskAdjVars;
	}

	/**
	 * Sets the risk adj vars.
	 *
	 * @param riskAdjVars the new risk adj vars
	 */
	public void setRiskAdjVars(List<RiskAdjustmentDTO> riskAdjVars) {
		this.riskAdjVars = riskAdjVars;
	}

	/**
	 * Gets the to compare risk adj vars.
	 *
	 * @return the to compare risk adj vars
	 */
	public List<RiskAdjustmentDTO> getToCompareRiskAdjVars() {
		return toCompareRiskAdjVars;
	}

	/**
	 * Sets the to compare risk adj vars.
	 *
	 * @param toCompareRiskAdjVars the new to compare risk adj vars
	 */
	public void setToCompareRiskAdjVars(List<RiskAdjustmentDTO> toCompareRiskAdjVars) {
		this.toCompareRiskAdjVars = toCompareRiskAdjVars;
	}

}
