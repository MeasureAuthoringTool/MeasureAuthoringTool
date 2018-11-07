package mat.dao.clause.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.clause.CQLLibraryAssociationDAO;
import mat.dao.search.GenericDAO;
import mat.model.cql.CQLLibraryAssociation;

@Repository("cqlLibraryAssociationDAO")
public class CQLLibraryAssociationDAOImpl extends GenericDAO<CQLLibraryAssociation, String> implements CQLLibraryAssociationDAO {

	public CQLLibraryAssociationDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	@Override
	public void deleteAssociation(CQLLibraryAssociation cqlLibraryAssociation) {

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(CQLLibraryAssociation.class);

		criteria.add(Restrictions.and(Restrictions.eq("associationId", cqlLibraryAssociation.getAssociationId()),
				Restrictions.eq("cqlLibraryId", cqlLibraryAssociation.getCqlLibraryId())));

		List<CQLLibraryAssociation> associationList = criteria.list();
		for (CQLLibraryAssociation cqlAssociation : associationList) {
			delete(cqlAssociation);
		}
	}

	@Override
	public int findAssociationCount(String associatedWithId) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(CQLLibraryAssociation.class);
		criteria.add(Restrictions.eq("associationId", associatedWithId));
		if (criteria.list() != null) {
			return criteria.list().size();
		} else {
			return 0;
		}

	}
	
	@Override
	public List<CQLLibraryAssociation> getAssociations(String associatedWithId) {

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(CQLLibraryAssociation.class);

		criteria.add(Restrictions.eq("associationId", associatedWithId));

		return criteria.list();
	}

}
