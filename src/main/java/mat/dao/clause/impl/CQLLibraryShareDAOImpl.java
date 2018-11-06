package mat.dao.clause.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.clause.CQLLibraryShareDAO;
import mat.dao.search.GenericDAO;
import mat.model.cql.CQLLibraryShare;

@Repository
public class CQLLibraryShareDAOImpl extends GenericDAO<CQLLibraryShare, String> implements CQLLibraryShareDAO{

	public CQLLibraryShareDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
}
