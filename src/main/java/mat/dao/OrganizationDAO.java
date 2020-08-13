package mat.dao;

import mat.model.Organization;

import java.util.List;

public interface OrganizationDAO extends IDAO<Organization, Long> {
	
	Organization findByOid(String oid);

	void saveOrganization(Organization entity);
	
	List<Organization> searchOrganization(String name);
	
	List<Organization> getAllOrganizations();
	
	void deleteOrganization(Organization entity);
	
	Organization findById(String id);
	
	Organization findByOidOrId(String oidOrId);

	void updateOrganization(Organization organization);
	
	List<Organization> getActiveOrganizationForAdminCSVReport();
}
