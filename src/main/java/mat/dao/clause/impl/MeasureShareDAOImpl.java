package mat.dao.clause.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.clause.MeasureShareDAO;
import mat.dao.search.GenericDAO;
import mat.model.clause.MeasureShare;

@Repository
public class MeasureShareDAOImpl extends GenericDAO<MeasureShare, String> implements MeasureShareDAO{
	public MeasureShareDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
}
