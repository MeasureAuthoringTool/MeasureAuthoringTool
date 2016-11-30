package mat.dao.impl.clause;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import mat.dao.search.GenericDAO;
import mat.model.clause.Attributes;

/**
 * The Interface AttributesDAO.
 */
public class AttributesDAO extends GenericDAO<Attributes, String> implements mat.dao.clause.AttributesDAO {
	
	@Override
	public List<Attributes> findByName(String attrName) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Attributes.class);
		criteria.add(Restrictions.eq("attrName", attrName));
		List<Attributes> attributesList = criteria.list(); 
		return attributesList;
	}

	@Override
	public List<Attributes> findById(String id) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Attributes.class);
		criteria.add(Restrictions.eq("id", id));
		List<Attributes> attributesList = criteria.list(); 
		return attributesList;
	}
}