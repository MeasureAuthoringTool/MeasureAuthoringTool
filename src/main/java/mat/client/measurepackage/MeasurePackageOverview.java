package mat.client.measurepackage;

import com.google.gwt.user.client.rpc.IsSerializable;
import mat.model.QualityDataSetDTO;
import mat.model.RiskAdjustmentDTO;
import mat.model.cql.CQLDefinition;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
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
	
	/** The cql supp data elements. */
	private List<CQLDefinition> cqlSuppDataElements = new ArrayList<CQLDefinition>();
	
	/** The cql qdm elements. */
	private List<CQLDefinition> cqlQdmElements = new ArrayList<CQLDefinition>();
	
	/** The master clause list. */
	private List<RiskAdjustmentDTO> subTreeClauseList = new ArrayList<RiskAdjustmentDTO>();
	
	/** The risk adj list. */
	private List<RiskAdjustmentDTO> riskAdjList = new ArrayList<RiskAdjustmentDTO>();
	
	private String releaseVersion ;
	
	private boolean isComposite; 
	
	public MeasurePackageOverview() {
		
	}
	
	public String getReleaseVersion() {
		return releaseVersion;
	}

	public void setReleaseVersion(String releaseVersion) {
		this.releaseVersion = releaseVersion;
	}

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
	 * Gets the cql supp data elements.
	 * 
	 * @return the cql supp data elements
	 */
	public List<CQLDefinition> getCqlSuppDataElements() {
		return cqlSuppDataElements;
	}

	/**
	 * Sets the cql supp data elements.
	 * 
	 * @param cqlSuppDataElements
	 *            the new cql supp data elements
	 */
	public void setCqlSuppDataElements(List<CQLDefinition> cqlSuppDataElements) {
		this.cqlSuppDataElements = cqlSuppDataElements;
	}
	
	/**
	 * Gets the cql qdm elements.
	 * 
	 * @return the cql qdm elements
	 */
	public List<CQLDefinition> getCqlQdmElements() {
		return cqlQdmElements;
	}

	/**
	 * Sets the cql qdm elements.
	 * 
	 * @param cqlSuppDataElements
	 *            the new cql qdm elements
	 */
	public void setCqlQdmElements(List<CQLDefinition> CqlQdmElements) {
		this.cqlQdmElements = CqlQdmElements;
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

	/**
	 * Gets the master clause list.
	 *
	 * @return the master clause list
	 */
	public List<RiskAdjustmentDTO> getSubTreeClauseList() {
		return subTreeClauseList;
	}

	/**
	 * Sets the master clause list.
	 *
	 * @param masterClauseList the new master clause list
	 */
	public void setSubTreeClauseList(List<RiskAdjustmentDTO> subTreeClauseList) {
		this.subTreeClauseList = subTreeClauseList;
	}

	/**
	 * Gets the risk adj list.
	 *
	 * @return the risk adj list
	 */
	public List<RiskAdjustmentDTO> getRiskAdjList() {
		return riskAdjList;
	}

	/**
	 * Sets the risk adj list.
	 *
	 * @param riskAdjList the new risk adj list
	 */
	public void setRiskAdjList(List<RiskAdjustmentDTO> riskAdjList) {
		this.riskAdjList = riskAdjList;
	}

	public boolean isComposite() {
		return isComposite;
	}

	public void setComposite(boolean isComposite) {
		this.isComposite = isComposite;
	}	
	
	
	
}
