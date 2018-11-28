package mat.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.search.GenericDAO;
import mat.model.ListObject;

@Repository("listObjectDAO")
public class ListObjectDAOImpl extends GenericDAO<ListObject, String> implements mat.dao.ListObjectDAO {
	
	public ListObjectDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	@Override
	public List<ListObject> getSupplimentalCodeList() {
		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		final CriteriaQuery<ListObject> query = cb.createQuery(ListObject.class);
		final Root<ListObject> root = query.from(ListObject.class);

		query.select(root).where(cb.equal(root.get("codeListContext"), "Supplimental CodeList"));

		return session.createQuery(query).getResultList();
	}
	
	@Override
	public List<ListObject> getElementCodeListByOID(List<String> elementOIDList) {
		List<ListObject> timingElementList = new ArrayList<>();

		if (CollectionUtils.isNotEmpty(elementOIDList)) {
			final Session session = getSessionFactory().getCurrentSession();
			final CriteriaBuilder cb = session.getCriteriaBuilder();
			final CriteriaQuery<ListObject> query = cb.createQuery(ListObject.class);
			final Root<ListObject> root = query.from(ListObject.class);

			final In<String> exp = cb.in(root.get("oid"));
			elementOIDList.forEach(exp::value);
			
			query.select(root).where(cb.in(exp));
			
			timingElementList = session.createQuery(query).getResultList();
		}
		return timingElementList;
	}
}
