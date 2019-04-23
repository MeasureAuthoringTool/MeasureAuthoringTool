package mat.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.search.GenericDAO;
import mat.model.CodeList;


@Repository("codeListDAO")
public class CodeListDAOImpl extends GenericDAO<CodeList, String> implements mat.dao.CodeListDAO {
	
	public CodeListDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
}
