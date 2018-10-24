package mat.dao.clause;

import mat.dao.IDAO;
import mat.model.clause.Packager;

public interface PackagerDAO extends IDAO<Packager, String> {
	
	/**
	 * Delete all packages.
	 * 
	 * @param measureId
	 *            the measure id
	 */
	public void deleteAllPackages(String measureId);
}
