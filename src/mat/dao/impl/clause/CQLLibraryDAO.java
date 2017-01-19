package mat.dao.impl.clause;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import mat.dao.search.GenericDAO;
import mat.model.clause.CQLLibrary;
import mat.shared.StringUtility;

public class CQLLibraryDAO extends GenericDAO<CQLLibrary, String> implements mat.dao.clause.CQLLibraryDAO {
	
	
class CQlLibraryComparator implements Comparator<CQLLibrary> {
		
		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(CQLLibrary o1, CQLLibrary o2) {
			// 1 if either isDraft
			// 2 version
			int ret = o1.isDraft() ? -1 : o2.isDraft() ? 1
					: compareDoubleStrings(o1.getVersion(), o2.getVersion());
			return ret;
		}
		
		/**
		 * Compare double strings.
		 * 
		 * @param s1
		 *            the s1
		 * @param s2
		 *            the s2
		 * @return the int
		 */
		private int compareDoubleStrings(String s1, String s2) {
			Double d1 = Double.parseDouble(s1);
			Double d2 = Double.parseDouble(s2);
			return d2.compareTo(d1);
		}
	}
	
	/*
	 * assumption: each CQL in this list is part of the same measure set
	 */
	/**
	 * The Class CQLLibraryListComparator.
	 */
	class CQLLibraryListComparator implements Comparator<List<CQLLibrary>> {
		
		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(List<CQLLibrary> o1, List<CQLLibrary> o2) {
			String v1 = o1.get(0).getName();
			String v2 = o2.get(0).getName();
			return v1.compareToIgnoreCase(v2);
		}
	}
	
	

	@Override
	public List<CQLLibrary> search(String searchText, String searchFrom) {
		
		
		String searchString = searchText.toLowerCase().trim();
		Criteria cCriteria = getSessionFactory().getCurrentSession()
				.createCriteria(CQLLibrary.class);
		cCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if(!searchFrom.equalsIgnoreCase("StandAlone")){
			cCriteria.add(Restrictions.eq("draft", false));
			cCriteria.addOrder(Order.desc("measureSetId.id"))
			.addOrder(Order.desc("version"));
		} else{
			cCriteria.addOrder(Order.desc("measureSetId.id"))
			.addOrder(Order.desc("draft")).addOrder(Order.desc("version"));
		}
		
		
		List<CQLLibrary> libraryResultList = cCriteria.list();
		List<CQLLibrary> orderedCQlLibList = sortLibraryList(libraryResultList);
		
		
		StringUtility su = new StringUtility();
		List<CQLLibrary> orderedList = new ArrayList<CQLLibrary>();
		for (CQLLibrary cqlLibrary : orderedCQlLibList) {
			
			boolean matchesSearch = searchResultsForCQLLibrary(searchString, su,
					cqlLibrary);
			if (matchesSearch) {
				orderedList.add(cqlLibrary);
			}
		}
		return orderedList;
		
	}
	/**
	 * Sort Library list by measure set Id.
	 * @param librariesResultList
	 * @return
	 */
	private List<CQLLibrary> sortLibraryList(List<CQLLibrary> librariesResultList) {
		// generate sortable lists
		List<List<CQLLibrary>> cqlLibList = new ArrayList<List<CQLLibrary>>();
		for (CQLLibrary cql : librariesResultList) {
			boolean hasList = false;
			for (List<CQLLibrary> cqlSubSetList : cqlLibList) {
				String msetId = cqlSubSetList.get(0).getMeasureSetId().getId();
				if (cql.getMeasureSetId().getId().equalsIgnoreCase(msetId)) {
					cqlSubSetList.add(cql);
					hasList = true;
					break;
				}
			}
			if (!hasList) {
				List<CQLLibrary> finalList = new ArrayList<CQLLibrary>();
				finalList.add(cql);
				cqlLibList.add(finalList);
			}
		}
		// sort
		for (List<CQLLibrary> mlist : cqlLibList) {
			Collections.sort(mlist, new CQlLibraryComparator());
		}
		Collections.sort(cqlLibList, new CQLLibraryListComparator());
		// compile list
		List<CQLLibrary> retList = new ArrayList<CQLLibrary>();
		for (List<CQLLibrary> mlist : cqlLibList) {
			for (CQLLibrary m : mlist) {
				retList.add(m);
			}
		}
		return retList;
	}
	
	
	
	
	
	/**
	 * Search method to search cql lib by owner name(First/last) or by cql library name.
	 * @param searchTextLC
	 * @param stringUtility
	 * @param cqlLibrary
	 * @return boolean
	 */
	private boolean searchResultsForCQLLibrary(String searchTextLC,
			StringUtility stringUtility, CQLLibrary cqlLibrary) {
		
		boolean matchesSearch = stringUtility.isEmptyOrNull(searchTextLC) ? true :
		// CQL name
				cqlLibrary.getName().toLowerCase().contains(searchTextLC) ? true : // CQL
																					// owner
																					// first
																					// name
						cqlLibrary.getOwnerId().getFirstName().toLowerCase().contains(searchTextLC) ? true :
						// CQL owner last name
								cqlLibrary.getOwnerId().getLastName().toLowerCase().contains(searchTextLC) ? true:
									// Owner email address
									//cqlLibrary.getOwnerId().getEmailAddress().contains(searchTextLC) ? true :
									false;
		return matchesSearch;
	}
	
}
