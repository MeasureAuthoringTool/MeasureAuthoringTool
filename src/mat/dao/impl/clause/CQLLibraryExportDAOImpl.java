package mat.dao.impl.clause;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.clause.CQLLibraryExportDAO;
import mat.dao.search.GenericDAO;
import mat.model.clause.CQLLibraryExport;

@Repository
public class CQLLibraryExportDAOImpl extends GenericDAO<CQLLibraryExport, String> implements CQLLibraryExportDAO {

	public CQLLibraryExportDAOImpl(@Autowired SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
}
