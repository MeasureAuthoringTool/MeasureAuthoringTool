package mat.dao.impl.clause;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import mat.dao.search.GenericDAO;
import mat.model.clause.Attributes;
import mat.model.clause.ModesAttributes;

/**
 * The implementation ModesAttributesDAO.
 */
public class ModesAttributesDAO extends GenericDAO<ModesAttributes, String> implements mat.dao.clause.ModesAttributesDAO {

	@Override
	public List<ModesAttributes> findByAttrId(String attributeId) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Attributes.class);
		criteria.add(Restrictions.eq("attributeId", attributeId));
		List<ModesAttributes> modesAttrList = criteria.list();
		return modesAttrList; 
	}

	@Override
	public List<ModesAttributes> findByModeId(String modeId) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Attributes.class);
		criteria.add(Restrictions.eq("modeId", modeId));
		List<ModesAttributes> modesAttrList = criteria.list();
		return modesAttrList; 
	}
	
}
