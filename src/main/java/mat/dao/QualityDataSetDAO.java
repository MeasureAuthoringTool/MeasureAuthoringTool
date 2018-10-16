package mat.dao;

import mat.model.QualityDataSet;
import mat.model.QualityDataSetDTO;

/**
 * The Interface QualityDataSetDAO.
 */
public interface QualityDataSetDAO extends IDAO<QualityDataSet, String> {
	
	/**
	 * Gets the qDS elements.
	 * 
	 * @param showSDEs
	 *            the show sd es
	 * @param measureId
	 *            the measure id
	 * @return the qDS elements
	 */
	public java.util.List<QualityDataSetDTO> getQDSElements(boolean showSDEs, String measureId);
	
	/**
	 * Gets the qDS elements for.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param codeListId
	 *            the code list id
	 * @return the qDS elements for
	 */
	public java.util.List<QualityDataSetDTO> getQDSElementsFor(String measureId, String codeListId);
	
	/**
	 * Gets the qDS elements for.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param codeListId
	 *            the code list id
	 * @param dataTypeId
	 *            the data type id
	 * @param occurrence
	 *            the occurrence
	 * @return the qDS elements for
	 */
	public java.util.List<QualityDataSet> getQDSElementsFor(String measureId, String codeListId, String dataTypeId, String occurrence);
	
	/**
	 * Gets the for measure.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the for measure
	 */
	public java.util.List<QualityDataSet> getForMeasure(String measureId);
	
	/**
	 * Clone qds elements.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param clonedMeasure
	 *            the cloned measure
	 */
	public void cloneQDSElements(String measureId, mat.model.clause.Measure clonedMeasure);
	
	/**
	 * Generate unique oid.
	 * 
	 * @return the string
	 */
	public String generateUniqueOid();
	
	/**
	 * Update list object id.
	 * 
	 * @param oldLOID
	 *            the old loid
	 * @param newLOID
	 *            the new loid
	 */
	public void updateListObjectId(String oldLOID, String newLOID);
}
