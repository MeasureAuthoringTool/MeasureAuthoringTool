package mat.dao.impl;

import mat.dao.search.GenericDAO;
import mat.model.Code;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("codeDAO")
public class CodeDAOImpl extends GenericDAO<Code, String> implements mat.dao.CodeDAO {
	
	public CodeDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

}
	
