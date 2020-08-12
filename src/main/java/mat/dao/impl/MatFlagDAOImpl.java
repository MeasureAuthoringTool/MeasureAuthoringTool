package mat.dao.impl;


import mat.dao.MatFlagDAO;
import mat.dao.search.GenericDAO;
import mat.model.MatFlag;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("matFlagDAO")
public class MatFlagDAOImpl extends GenericDAO<MatFlag, String> implements MatFlagDAO {
	
	public MatFlagDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
}
