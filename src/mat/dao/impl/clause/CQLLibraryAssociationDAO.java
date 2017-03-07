package mat.dao.impl.clause;

import java.util.List;

import mat.dao.search.GenericDAO;
import mat.model.cql.CQLLibraryAssociation;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class CQLLibraryAssociationDAO extends GenericDAO<CQLLibraryAssociation, String>
		implements mat.dao.clause.CQLLibraryAssociationDAO {

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

}
