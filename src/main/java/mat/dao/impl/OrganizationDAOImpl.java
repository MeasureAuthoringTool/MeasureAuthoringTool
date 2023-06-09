package mat.dao.impl;

import mat.dao.search.GenericDAO;
import mat.model.Organization;
import mat.model.User;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository("organizationDAO")
public class OrganizationDAOImpl extends GenericDAO<Organization, Long> implements mat.dao.OrganizationDAO {
	
	private static final String ORGANIZATION_NAME = "organizationName";
	
	private static final String ORGANIZATION_OID = "organizationOID";
	
	public OrganizationDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	} 

	@Override
	public Organization findByOid(String oid) {
		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		final CriteriaQuery<Organization> query = cb.createQuery(Organization.class);
		final Root<Organization> root = query.from(Organization.class);
		
		query.select(root).where(cb.equal(root.get(ORGANIZATION_OID), oid));
		
		final List<Organization> organizationList = session.createQuery(query).getResultList();
		
		return CollectionUtils.isNotEmpty(organizationList) ? organizationList.get(0) : null;
	}

	@Override
	public Organization findById(String id) {
		Organization org = null;
		try {
			org = find(Long.valueOf(id));
		} catch (final Exception e) {
			return null;
		}
		return org;
	}
	
	@Override
	public List<Organization> getAllOrganizations() {
		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		final CriteriaQuery<Organization> query = cb.createQuery(Organization.class);
		final Root<Organization> root = query.from(Organization.class);
		
		query.select(root).where(cb.notEqual(root.get(ORGANIZATION_NAME), StringUtils.EMPTY));
		query.orderBy(cb.asc(root.get(ORGANIZATION_NAME)));
		
		final List<Organization> organizationList = session.createQuery(query).getResultList();
		
		return CollectionUtils.isNotEmpty(organizationList) ? organizationList : new ArrayList<>();
	}

	@Override
	public void saveOrganization(Organization entity) {
		Session session = null;
		try {
			session = getSessionFactory().getCurrentSession();
			session.save(entity);
		} catch (final Exception e) {
			e.printStackTrace();
		} 
	}
	
	@Override
	public List<Organization> searchOrganization(String name) {
		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		final CriteriaQuery<Organization> query = cb.createQuery(Organization.class);
		final Root<Organization> root = query.from(Organization.class);
		
		final Predicate p1 = cb.and(cb.notEqual(root.get(ORGANIZATION_NAME), StringUtils.EMPTY), 
				cb.notEqual(root.get(ORGANIZATION_OID), StringUtils.EMPTY));
		
		Predicate p2 = null;
		
		if(StringUtils.isNotBlank(name)) {
			p2 = cb.like(cb.lower(root.get(ORGANIZATION_NAME)), "%" + name.toLowerCase() + "%");
		}
		
		final Predicate predicate = (p2 != null) ? cb.and(p1, p2) : p1;
				
		query.select(root).where(predicate);
		query.orderBy(cb.asc(root.get(ORGANIZATION_NAME)));

		return session.createQuery(query).getResultList();
	}

	@Override
	public void deleteOrganization(Organization entity) {
		final Organization org = find(entity.getId());
		delete(org);
		
	}

	@Override
	public void updateOrganization(Organization organization) {
		try {
			final Session session = getSessionFactory().getCurrentSession();
			session.update(organization);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Organization> getActiveOrganizationForAdminCSVReport() {

		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		final CriteriaQuery<Organization> query = cb.createQuery(Organization.class);
		
		final Root<Organization> root = query.from(Organization.class);
		final Root<User> user = query.from(User.class);
		
		query.select(root);
		query.where(cb.and(cb.equal(root.get("id"), user.get("organization").get("id")), cb.notEqual(user.get("status").get("statusId"), "2")));
		query.distinct(true);
		
		return session.createQuery(query).getResultList();
	}

	@Override
	public Organization findByOidOrId(String oidOrId) {
		Organization organization = findById(oidOrId);
		if(organization == null) {
			organization = findByOid(oidOrId);
		}
		return organization;
	}
	
}
