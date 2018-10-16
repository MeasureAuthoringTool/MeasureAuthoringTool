package mat.dao;

import java.util.List;
import mat.model.Organization;
/** The Interface UserDAO. */
public interface OrganizationDAO extends IDAO<Organization, Long> {
	
	/** @param text
	 * @return */
	int countSearchResults(String text);
	
	Organization findByOid(String oid);
	/** @param entity */
	void saveOrganization(Organization entity);
	/** @param name
	 * @return */
	List<Organization> searchOrganization(String name);
	
	/**
	 * Gets all the organizations.
	 *
	 * @return The list of all organizations
	 */
	List<Organization> getAllOrganizations();
	void deleteOrganization(Organization entity);
	
	Organization findById(String id);
	/** @param entity */
	void updateOrganization(Organization organization);
}
