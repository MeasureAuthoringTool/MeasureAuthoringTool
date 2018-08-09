package mat.dao.impl.clause;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
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
	public void updateComponentMeasures(String measureId, List<ComponentMeasure> componentMeasuresList) {
		String hql = "DELETE from mat.model.clause.ComponentMeasure where compositeMeasureId = :compositeMeasureId";
		Transaction tx = null;
		try (Session session = HibernateConf.createHibernateSession();){
			
			Query<?> query = session.createQuery(hql);
			query.setParameter("compositeMeasureId", measureId);
			
			tx = session.beginTransaction();
			query.executeUpdate();			
			componentMeasuresList.forEach(component -> session.save(component));			
			tx.commit();
			
		} catch (Exception e) {
			logger.error("Error updating component measures: " + e.getMessage());
			if (tx != null) {
				tx.rollback();
			}
		}
		
	}
}
