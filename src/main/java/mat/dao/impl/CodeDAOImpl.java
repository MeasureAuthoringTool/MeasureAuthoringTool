package mat.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.search.GenericDAO;
import mat.model.Code;

@Repository("codeDAO")
public class CodeDAOImpl extends GenericDAO<Code, String> implements mat.dao.CodeDAO {
	
	public CodeDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	public void deleteCodes(List<Code> codes){
		Session session = getSessionFactory().getCurrentSession();
	                String hql = "delete from mat.model.Code c where c in (:codesList)";
	    	        Query query = session.createQuery(hql);
	    	        query.setParameterList("codesList", codes);
	    	        int row = query.executeUpdate();
	    	        if (row == 0){
	    	          System.out.println("No Code Rows Deleted!");
	    	        }
	    	        else{
	    	          System.out.println("Number of Code Rows Deleted: " + row);
	    	        }
		}


	/* (non-Javadoc)
	 * @see mat.dao.CodeDAO#searchCodes(java.lang.String, int, int)
	 */
	@Override
	public List<Code> searchCodes(String codeListId, int startIndex, int pageSize) {
		
		
		Criteria codesCriteria = getSessionFactory().getCurrentSession().createCriteria(Code.class);
		codesCriteria.add(Restrictions.eq("codeListID", codeListId));
		@SuppressWarnings("unchecked")
		ArrayList<Code> results = (ArrayList)codesCriteria.list();
		Collections.sort(results,new Code.Comparator());
		List<Code> filteredList = new ArrayList<Code>();
		if(results.size() > pageSize){
			filteredList = getOnlyFilteredCodes(pageSize,results,startIndex);
			return filteredList;
		}else
			return results;
	}
	
	/**
	 * Gets the only filtered codes.
	 * 
	 * @param pageSize
	 *            the page size
	 * @param codes
	 *            the codes
	 * @param startIndex
	 *            the start index
	 * @return the only filtered codes
	 */
	private ArrayList<Code> getOnlyFilteredCodes(int pageSize, ArrayList<Code> codes,int startIndex){
		ArrayList<Code> codesList = new ArrayList<Code>();
		int counter = 1;
		for(int i = startIndex;i<codes.size(); i++){
			if(counter > pageSize){
				break;
			}else{
				counter++;
				codesList.add(codes.get(i));
			}
		}
		return codesList;
	}
	
}
	
