package mat.dao.clause.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.clause.ComponentMeasuresDAO;
import mat.dao.search.GenericDAO;
import mat.model.clause.ComponentMeasure;
import mat.model.clause.Measure;

@Repository("componentMeasuresDAO")
public class ComponentMeasureDAOImpl extends GenericDAO<ComponentMeasure, String> implements ComponentMeasuresDAO{
	
	private static final Log logger = LogFactory.getLog(ComponentMeasureDAOImpl.class);

	public ComponentMeasureDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	@Override
	public void saveComponentMeasures(List<ComponentMeasure> componentMeasuresList) {
		for(ComponentMeasure component : componentMeasuresList) {
			super.save(component);	
		}
	}
	
	@Override
	public void updateComponentMeasures(String measureId, List<ComponentMeasure> componentMeasuresList) {
		String hql = "DELETE from mat.model.clause.ComponentMeasure where compositeMeasure.id = :compositeMeasureId";
		try {
			Session session = getSessionFactory().getCurrentSession();
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

		//Create CriteriaBuilder
		CriteriaBuilder builder = session.getCriteriaBuilder();
		
		//Create CriteriaQuery
		CriteriaQuery<ComponentMeasure> query = builder.createQuery(ComponentMeasure.class);
		
		Root<ComponentMeasure> root = query.from(ComponentMeasure.class);
		
		query.where(builder.equal(root.get("componentMeasure").get("id"), measureId));
		
		return session.createQuery(query).getResultList();
	}
}
