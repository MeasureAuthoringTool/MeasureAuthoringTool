package mat.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.search.GenericDAO;
import mat.model.Organization;

@Repository("organizationDAO")
public class OrganizationDAOImpl extends GenericDAO<Organization, Long> implements mat.dao.OrganizationDAO {
	
	public OrganizationDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	} 
	@Override
	public int countSearchResults(String text) {
		Criteria criteria = createSearchCriteria(text);
		criteria.setProjection(Projections.rowCount());
		return ((Long) criteria.uniqueResult()).intValue();
	}
	/**
	 * Creates the search criteria.
	 * 
	 * @param text
	 *            the text
	 * @return the criteria
	 */
	private Criteria createSearchCriteria(String text) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Organization.class);
		criteria.add(Restrictions.ilike("organizationName", "%" + text + "%"));
		return criteria;
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.OrganizationDAO#findByOid(java.lang.String)
	 */
	@Override
	public Organization findByOid(String oid) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Organization.class);
		criteria.add(Restrictions.eq("organizationOID", oid));
		if (criteria.list().size() != 0) {
			return (Organization) criteria.list().get(0);
		} else {
			return null;
		}
	}
	@Override
	public Organization findById(String id) {
		Organization org = null;
		try {
			org = find(Long.valueOf(id));
		} catch (Exception e) {
			return null;
		}
		return org;
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.OrganizationDAO#getAllOrganizations()
	 */
	@Override
	public List<Organization> getAllOrganizations() {
		List<Organization> organizations = new ArrayList<Organization>();
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Organization.class);
		criteria.add(Restrictions.ne("organizationName",""));
		criteria.addOrder(Order.asc("organizationName"));
		if (!criteria.list().isEmpty()) {
			organizations = criteria.list();
		}
		return organizations;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.dao.OrganizationDAO#saveOrganization(mat.model.Organization)
	 */
	@Override
	public void saveOrganization(Organization entity) {
		Session session = null;
		try {
			session = getSessionFactory().getCurrentSession();
			session.save(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.dao.OrganizationDAO#searchOrganization(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Organization> searchOrganization(String name) {
		Criteria criteria = createSearchCriteria(name);
		criteria.addOrder(Order.asc("organizationName"));
		criteria.add(Restrictions.ne("organizationName", StringUtils.EMPTY))
		.add(Restrictions.ne("organizationOID", StringUtils.EMPTY));
		/*criteria.setFirstResult(startIndex);
		if (numResults > 0) {
			criteria.setMaxResults(numResults);
		}*/
		return criteria.list();
	}
	@Override
	public void deleteOrganization(Organization entity) {
		Organization org = find(entity.getId());
		delete(org);
		
	}
	@Override
	public void updateOrganization(Organization organization) {
		Session session = null;
		try {
			session = getSessionFactory().getCurrentSession();
			session.update(organization);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
	
}
