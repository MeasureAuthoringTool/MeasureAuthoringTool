package mat.dao.clause;

import java.util.List;

import mat.dao.IDAO;
import mat.model.clause.Packager;

/**
 * The Interface PackagerDAO.
 */
public interface PackagerDAO extends IDAO<Packager, String> {
	
	/**
	 * Gets the for measure.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the for measure
	 */
	public List<Packager> getForMeasure(String measureId);
	
	/**
	 * Gets the number of packages for measure.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the number of packages for measure
	 */
	public long getNumberOfPackagesForMeasure(String measureId);
	
	/**
	 * Delete package.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param sequence
	 *            the sequence
	 */
	public void deletePackage(String measureId, String sequence);
	
	/**
	 * Delete all packages.
	 * 
	 * @param measureId
	 *            the measure id
	 */
	public void deleteAllPackages(String measureId);
}
