package mat.dao.impl.clause;

import java.util.List;
import java.util.Set;

import mat.dao.search.GenericDAO;
import mat.model.clause.Decision;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration({"file:**/mat-persistence.xml"})
public class DecisionDAO extends GenericDAO<Decision, String>  implements mat.dao.clause.DecisionDAO {
	
	public void save(Decision parent) {
		if(parent==null) return;
		
		try {
			super.save(parent);
			saveChildren(parent);
		}
		catch (Exception e) {
			e.printStackTrace();		  
		}
	}
	
	private void saveChildren(Decision parent) {
		if(parent!=null) {	
			Set<Decision> listOfChildren = parent.getChildDecisions();			
			if (!listOfChildren.isEmpty()) {
				for (Decision child: listOfChildren) {
					super.save(child);
					saveChildren(child);
				}
			}
		}
	}
	
	public Decision find(String parentId)  {		
		Decision parent = super.find(parentId);
		getChildren(parent);
		return parent;
	}
	
	private void getChildren(Decision parent) {
		if(parent!=null) {	
			String searchId = parent.getId();
			List<Decision> listOfChildren;			
			listOfChildren = findByParentId(searchId);
			if (!listOfChildren.isEmpty()) {
				for (Decision child: listOfChildren) {
					parent.getChildDecisions().add(child);
					getChildren(child);
				}
			}
		}
	}
	
	public List<Decision> findByParentId(String parentId) {
		List<Decision>	list = null;
		Session session = null;
		Transaction transaction = null;
		
		try {
			session = getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			Criteria criteria = session.createCriteria(Decision.class);
			criteria.add(Restrictions.eq("parentId", parentId));
			criteria.addOrder(Order.asc("orderNum"));
	        list = criteria.list();
	        transaction.commit();
		}
		catch (Exception onfe) {
			onfe.printStackTrace();
		}
		finally {
	    	rollbackUncommitted(transaction);
	    	closeSession(session);
		}
		return list;
	}
	
	public List<Decision> findByOperator(String operator) {
		//temp API
		List<Decision>	list = null;
		Session session = null;
		Transaction transaction = null;
		
		try {
			session = getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			Criteria criteria = session.createCriteria(Decision.class);
			criteria.add(Restrictions.eq("operator", operator));			
	        list = criteria.list();
	        transaction.commit();
		}
		catch (Exception onfe) {
			onfe.printStackTrace();
		}
		finally {
	    	rollbackUncommitted(transaction);
	    	closeSession(session);
		}
		return list;
	}
	@Override
	public void delete(String[] ids){
		super.delete(ids);
	}
	public void deleteAndUpdateParent(String[] ids){
		Session session = getSessionFactory().getCurrentSession();
		String idList = "";
		for(String id : ids){
			idList += "'"+id+"',";
		}
		idList = idList.substring(0, idList.length()-1);
		String queryStr = "update DECISION d set d.parent_id = null where d.id in ("+idList+");";
		SQLQuery query = session.createSQLQuery(queryStr);
		int ret = query.executeUpdate();
		delete(ids);
	}
	
}
