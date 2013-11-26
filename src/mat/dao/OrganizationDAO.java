package mat.dao;

import java.util.List;
import mat.model.Organization;
/** The Interface UserDAO. */
public interface OrganizationDAO extends IDAO<Organization, String> {
	
	/** @param text
	 * @return */
	int countSearchResults(String text);
	
	Organization findByOid(String oid);
	/** @param entity */
	void saveOrganization(Organization entity);
	/** @param name
	 * @param startIndex
	 * @param numResults
	 * @return */
	List<Organization> searchOrganization(String name, int startIndex, int numResults);
}
