package mat.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
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
			Session session = getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(ListObject.class);
			criteria.add(Restrictions
					.eq("codeListContext", "Supplimental CodeList"));
			List<ListObject> suppElementList = criteria.list();
			return suppElementList;
		}
	
	@Override
	public List<ListObject> getElementCodeListByOID(List<String> elementOIDList) {
		List<ListObject> timingElementList = new ArrayList<ListObject>();

		if (elementOIDList != null && !elementOIDList.isEmpty()) {
			Session session = getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(ListObject.class);
			criteria.add(Restrictions.in("oid", elementOIDList));
			timingElementList = criteria.list();
		}
		return timingElementList;
	}
}
