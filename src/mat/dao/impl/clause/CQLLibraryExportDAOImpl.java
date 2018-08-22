package mat.dao.impl.clause;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
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
	
	@Override
	public CQLLibraryExport findByLibraryId(String measureId) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(CQLLibraryExport.class);
		criteria.add(Restrictions.eq("cqlLibrary.id", measureId));
		List<CQLLibraryExport> results =  criteria.list();
		if(!results.isEmpty()) {
			return results.get(0);
		}
		else {
			return null;
		}
	}
	
}
