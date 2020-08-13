package mat.dao.clause.impl;

import mat.dao.clause.CQLLibraryAssociationDAO;
import mat.dao.search.GenericDAO;
import mat.model.cql.CQLLibraryAssociation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository("cqlLibraryAssociationDAO")
public class CQLLibraryAssociationDAOImpl extends GenericDAO<CQLLibraryAssociation, String> implements CQLLibraryAssociationDAO {
	
	private static final String ASSOCIATION_ID = "associationId";

	public CQLLibraryAssociationDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	@Override
	public void deleteAssociation(CQLLibraryAssociation cqlLibraryAssociation) {
		Session session = getSessionFactory().getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaDelete<CQLLibraryAssociation> delete = builder.createCriteriaDelete(CQLLibraryAssociation.class);
		Root<CQLLibraryAssociation> root = delete.from(CQLLibraryAssociation.class);

		delete.where(builder.and(builder.equal(root.get(ASSOCIATION_ID), cqlLibraryAssociation.getAssociationId()), 
				builder.equal(root.get("cqlLibraryId"), cqlLibraryAssociation.getCqlLibraryId())));
		
		session.createQuery(delete).executeUpdate();
	}

	@Override
	public int findAssociationCount(String associatedWithId) {
		Session session = getSessionFactory().getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<CQLLibraryAssociation> root = countQuery.from(CQLLibraryAssociation.class);
		countQuery =  countQuery.select(builder.count(root)).where(builder.equal(root.get(ASSOCIATION_ID), associatedWithId));
		
		Long count = session.createQuery(countQuery).getSingleResult();
		if (count == null) {
			return 0;
		} else {
			return Math.toIntExact(count);
		}

	}
	
	@Override
	public List<CQLLibraryAssociation> getAssociations(String associatedWithId) {
		Session session = getSessionFactory().getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<CQLLibraryAssociation> query = builder.createQuery(CQLLibraryAssociation.class);
		Root<CQLLibraryAssociation> root = query.from(CQLLibraryAssociation.class);
		query.where(builder.equal(root.get(ASSOCIATION_ID), associatedWithId));
		
		return session.createQuery(query).getResultList();
		
	}

}
