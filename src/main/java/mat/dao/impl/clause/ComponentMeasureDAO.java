package mat.dao.impl.clause;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import mat.dao.clause.ComponentMeasuresDAO;
import mat.dao.search.GenericDAO;
import mat.hibernate.HibernateConf;
import mat.model.clause.ComponentMeasure;

public class ComponentMeasureDAO extends GenericDAO<ComponentMeasure, String> implements ComponentMeasuresDAO{
	
	private static final Log logger = LogFactory.getLog(ComponentMeasureDAO.class);
	
	@Override
	public void saveComponentMeasures(List<ComponentMeasure> componentMeasuresList) {
		for(ComponentMeasure component : componentMeasuresList) {
			super.save(component);	
		}
	}
	
	@Override
	public void deleteComponentMeasures(List<ComponentMeasure> componentMeasuresToDelete) {
		try (Session session = HibernateConf.createHibernateSession();){
			for(ComponentMeasure component : componentMeasuresToDelete) {
				session.delete(component);
			}
			
		} catch (Exception e) {
			logger.error("Error deleting component measures: " + e);
		}
	}

	@Override
	public void updateComponentMeasures(String measureId, List<ComponentMeasure> componentMeasuresList) {
		String hql = "DELETE from mat.model.clause.ComponentMeasure where compositeMeasure.id = :compositeMeasureId";
		try (Session session = HibernateConf.createHibernateSession();){
			
			Query<?> query = session.createQuery(hql);
			query.setParameter("compositeMeasureId", measureId);
			
			query.executeUpdate();			
			saveComponentMeasures(componentMeasuresList);			
			
		} catch (Exception e) {
			logger.error("Error updating component measures: " + e);
		}
		
	}

	@Override
	public List<ComponentMeasure> findByComponentMeasureId(String measureId) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ComponentMeasure.class);
		criteria.add(Restrictions.eq("componentMeasure.id", measureId));
		return criteria.list();
	}
}
