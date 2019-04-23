package mat.dao;

import java.util.List;

import mat.model.Organization;

public interface OrganizationDAO extends IDAO<Organization, Long> {
	
	Organization findByOid(String oid);

	void saveOrganization(Organization entity);
	
	List<Organization> searchOrganization(String name);
	
	List<Organization> getAllOrganizations();
	
	void deleteOrganization(Organization entity);
	
	Organization findById(String id);

	void updateOrganization(Organization organization);
	
	List<Organization> getActiveOrganizationForAdminCSVReport();
}
