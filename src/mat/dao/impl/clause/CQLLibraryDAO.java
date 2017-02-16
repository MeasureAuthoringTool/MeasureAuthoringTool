package mat.dao.impl.clause;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import mat.client.measure.MeasureSearchFilterPanel;
import mat.dao.search.GenericDAO;
import mat.model.User;
import mat.model.clause.CQLLibrary;
import mat.shared.StringUtility;

public class CQLLibraryDAO extends GenericDAO<CQLLibrary, String> implements mat.dao.clause.CQLLibraryDAO {
	
	
class CQLLibraryComparator implements Comparator<CQLLibrary> {
		
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
	
	/** The lock threshold. */
	private final long lockThreshold = 3 * 60 * 1000; // 3 minutes


	@Override
	public List<CQLLibrary> search(String searchText, String searchFrom, int pageSize, User user, int filter ) {
		
		String searchString = searchText.toLowerCase().trim();
		Criteria cCriteria = getSessionFactory().getCurrentSession()
				.createCriteria(CQLLibrary.class);
		cCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		//cCriteria.setFirstResult(startIndex);
		if(!searchFrom.equalsIgnoreCase("StandAlone")){
			cCriteria.add(Restrictions.eq("draft", false));
			
			cCriteria.addOrder(Order.desc("measureSet.id"))
			.addOrder(Order.desc("version"));
		} else{
			if (filter == MeasureSearchFilterPanel.MY_MEASURES) {
				cCriteria.add(Restrictions.eq("ownerId.id", user.getId()));
			}
			cCriteria.add(Restrictions.isNotNull("cqlSet.id"))
			
			.addOrder(Order.desc("cqlSet.id"))
			.addOrder(Order.desc("draft"))
			.addOrder(Order.desc("version"));
		}
		cCriteria.setFirstResult(0);
		
		List<CQLLibrary> libraryResultList = cCriteria.list();
		if(searchFrom.equalsIgnoreCase("StandAlone")) {
			if (!user.getSecurityRole().getId().equals("2")) {
				libraryResultList = getAllLibrariesInSet(libraryResultList);
			}
		}
		List<CQLLibrary> orderedCQlLibList = null;
		if(libraryResultList != null){
			orderedCQlLibList = sortLibraryList(libraryResultList, searchFrom);
		} else {
			orderedCQlLibList = new ArrayList<CQLLibrary>();
		}
		
		StringUtility su = new StringUtility();
		List<CQLLibrary> orderedList = new ArrayList<CQLLibrary>();
		for (CQLLibrary cqlLibrary : orderedCQlLibList) {
			
			boolean matchesSearch = searchResultsForCQLLibrary(searchString, su,
					cqlLibrary);
			if (matchesSearch) {
				orderedList.add(cqlLibrary);
			}
		}
		
		
		if (pageSize < orderedList.size()) {
			return orderedList.subList(0, pageSize);
		} else {
			return orderedList;
		}
		
	}
	
	public List<CQLLibrary> getAllLibrariesInSet(List<CQLLibrary> libraries) {
		if (!libraries.isEmpty()) {
			Set<String> cqlLibSetIds = new HashSet<String>();
			for (CQLLibrary m : libraries) {
				cqlLibSetIds.add(m.getCqlSet().getId());
			}
			
			Criteria setCriteria = getSessionFactory().getCurrentSession()
					.createCriteria(CQLLibrary.class);
			setCriteria.add(Restrictions.in("cqlSet.id", cqlLibSetIds));
			libraries = setCriteria.list();
		}
		return sortLibraryList(libraries);
	}
	
	
	
	private List<CQLLibrary> sortLibraryList(List<CQLLibrary> libraryResultList) {
		// generate sortable lists
		List<List<CQLLibrary>> libraryList = new ArrayList<List<CQLLibrary>>();
		for (CQLLibrary cqlLib : libraryResultList) {
			boolean hasList = false;
			for (List<CQLLibrary> list : libraryList) {
				String cqlsetId = list.get(0).getCqlSet().getId();
				if (cqlLib.getMeasureSet().getId().equalsIgnoreCase(cqlsetId)) {
					list.add(cqlLib);
					hasList = true;
					break;
				}
			}
			// }
			if (!hasList) {
				List<CQLLibrary> cqllist = new ArrayList<CQLLibrary>();
				// Check if Measure is softDeleted then dont include that into
				// list.
				// if(m.getDeleted()==null){
				cqllist.add(cqlLib);
				libraryList.add(cqllist);
				// }
			}
		}
		// sort
		for (List<CQLLibrary> list : libraryList) {
			Collections.sort(list, new CQLLibraryComparator());
		}
		Collections.sort(libraryList, new CQLLibraryListComparator());
		// compile list
		List<CQLLibrary> retList = new ArrayList<CQLLibrary>();
		for (List<CQLLibrary> mlist : libraryList) {
			for (CQLLibrary m : mlist) {
				retList.add(m);
			}
		}
		return retList;
	}
	
	
	/**
	 * Sort Library list by measure set Id.
	 * @param librariesResultList
	 * @return
	 */
	private List<CQLLibrary> sortLibraryList(List<CQLLibrary> librariesResultList, String searchFrom) {
		// generate sortable lists
		List<List<CQLLibrary>> cqlLibList = new ArrayList<List<CQLLibrary>>();
		for (CQLLibrary cql : librariesResultList) {
			boolean hasList = false;
			for (List<CQLLibrary> cqlSubSetList : cqlLibList) {
				String setId = "";
				String listSetId = "";
				if(searchFrom.equalsIgnoreCase("StandAlone")){
					setId = cqlSubSetList.get(0).getCqlSet().getId();
					listSetId = cql.getCqlSet().getId();
				} else {
					setId = cqlSubSetList.get(0).getMeasureSet().getId();
					listSetId = cql.getMeasureSet().getId();
				}
				
				if (listSetId.equalsIgnoreCase(setId)) {
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
			Collections.sort(mlist, new CQLLibraryComparator());
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
	
	@Override
	public boolean isLibraryLocked(String cqlLibraryId) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria libCriteria = session.createCriteria(CQLLibrary.class);
		libCriteria.setProjection(Projections.property("lockedOutDate"));
		libCriteria.add(Restrictions.eq("id", cqlLibraryId));
		CQLLibrary libResults = (CQLLibrary) libCriteria.uniqueResult();
		Timestamp lockedOutDate = null;
		if (libResults != null) {
			lockedOutDate = libResults.getLockedOutDate();
		}
		boolean locked = isLocked(lockedOutDate);
		session.close();
		return locked;
	}
	
	/**
	 * Checks if is locked.
	 * 
	 * @param lockedOutDate
	 *            the locked out date
	 * @return false if current time - lockedOutDate < the lock threshold
	 */
	private boolean isLocked(Date lockedOutDate) {
		
		boolean locked = false;
		if (lockedOutDate == null) {
			return locked;
		}
		
		long currentTime = System.currentTimeMillis();
		long lockedOutTime = lockedOutDate.getTime();
		long timeDiff = currentTime - lockedOutTime;
		locked = timeDiff < lockThreshold;
		
		return locked;
	}
	
	@Override
	public void updateLockedOutDate(CQLLibrary existingLibrary) {

		Session session = getSessionFactory().openSession();
		org.hibernate.Transaction tx = session.beginTransaction();
		try {
			CQLLibrary cqlLibrary = (CQLLibrary) session.load(CQLLibrary.class, existingLibrary.getId());
			cqlLibrary.setId(existingLibrary.getId());
			cqlLibrary.setLockedOutDate(null);
			cqlLibrary.setLockedUserId(null);
			session.update(cqlLibrary);
			tx.commit();
			session.close();
		} finally {
			rollbackUncommitted(tx);
			closeSession(session);
		}
	}
}
