package mat.client.measurepackage;

import java.util.ArrayList;
import java.util.List;

import mat.model.QualityDataSetDTO;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class MeasurePackageOverview.
 */
public class MeasurePackageOverview implements IsSerializable{
	
	/** The clauses. */
	private List<MeasurePackageClauseDetail> clauses = new ArrayList<MeasurePackageClauseDetail>();
	
	/** The packages. */
	private List<MeasurePackageDetail> packages = new ArrayList<MeasurePackageDetail>();
	
	/** The qdm elements. */
	private List<QualityDataSetDTO> qdmElements = new ArrayList<QualityDataSetDTO>();
	
	/** The supp data elements. */
	private List<QualityDataSetDTO> suppDataElements = new ArrayList<QualityDataSetDTO>();
	
	/**
	 * Gets the clauses.
	 * 
	 * @return the clauses
	 */
	public List<MeasurePackageClauseDetail> getClauses() {
		return clauses;
	}
	
	/**
	 * Sets the clauses.
	 * 
	 * @param clauses
	 *            the new clauses
	 */
	public void setClauses(List<MeasurePackageClauseDetail> clauses) {
		this.clauses = clauses;
	}
	
	/**
	 * Gets the packages.
	 * 
	 * @return the packages
	 */
	public List<MeasurePackageDetail> getPackages() {
		return packages;
	}
	
	/**
	 * Sets the packages.
	 * 
	 * @param packages
	 *            the new packages
	 */
	public void setPackages(List<MeasurePackageDetail> packages) {
		this.packages = packages;
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
	
	
	
}
