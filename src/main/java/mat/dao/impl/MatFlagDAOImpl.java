package mat.dao.impl;


import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.MatFlagDAO;
import mat.dao.search.GenericDAO;
import mat.model.MatFlag;

@Repository
public class MatFlagDAOImpl extends GenericDAO<MatFlag, String> implements MatFlagDAO {
	
	public MatFlagDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
}
