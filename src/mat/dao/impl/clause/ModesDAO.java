package mat.dao.impl.clause;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import mat.dao.search.GenericDAO;
import mat.model.clause.Attributes;
import mat.model.clause.Modes;

/**
 * The implementation ModesDAO.
 */
public class ModesDAO extends GenericDAO<Modes, String> implements mat.dao.clause.ModesDAO{
	
	@Override
	public List<Modes> findByName(String modeName) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Attributes.class);
		criteria.add(Restrictions.eq("modeName", modeName));
		List<Modes> modesList = criteria.list();
		return modesList; 
	}

	@Override
	public List<Modes> findById(String id) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Attributes.class);
		criteria.add(Restrictions.eq("id", id));
		List<Modes> modesList = criteria.list();
		return modesList; 
	}
}
