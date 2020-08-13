package mat.dao.clause.impl;

import mat.dao.clause.CQLLibraryExportDAO;
import mat.dao.search.GenericDAO;
import mat.model.clause.CQLLibraryExport;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class CQLLibraryExportDAOImpl extends GenericDAO<CQLLibraryExport, String> implements CQLLibraryExportDAO {

	public CQLLibraryExportDAOImpl(@Autowired SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public CQLLibraryExport findByLibraryId(String libraryId) {
		Session session = getSessionFactory().getCurrentSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<CQLLibraryExport> query = cb.createQuery(CQLLibraryExport.class);
		Root<CQLLibraryExport> root = query.from(CQLLibraryExport.class);
		
		query.select(root).where(cb.equal(root.get("cqlLibrary").get("id"), libraryId));
		
		List<CQLLibraryExport> results = session.createQuery(query).getResultList();

		return !results.isEmpty() ? results.get(0) : null;
	}
	
}
