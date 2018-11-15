package mat.dao.clause.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.client.measure.MeasureSearchFilterPanel;
import mat.dao.search.GenericDAO;
import mat.model.LockedUserInfo;
import mat.model.SecurityRole;
import mat.model.User;
import mat.model.clause.CQLLibrary;
import mat.model.clause.ShareLevel;
import mat.model.cql.CQLLibraryShare;
import mat.model.cql.CQLLibraryShareDTO;
import mat.server.LoggedInUserUtil;
import mat.server.util.MATPropertiesService;

@Repository("cqlLibraryDAO")
public class CQLLibraryDAOImpl extends GenericDAO<CQLLibrary, String> implements mat.dao.clause.CQLLibraryDAO {

	private static final Log logger = LogFactory.getLog(CQLLibraryDAOImpl.class);
	
	private static final String DRAFT = "draft";
	private static final String SET_ID = "set_id";
	private static final String VERSION = "version";
	private static final String OWNER_ID = "ownerId";
	private static final String LIBRARY_NAME = "name";
	private static final String LAST_NAME = "lastName";
	private static final String FIRST_NAME = "firstName";
	private static final String MEASURE_ID =  "measureId";
	private static final String SHARE_USER = "shareUser";
	private static final String CQL_LIBRARY = "cqlLibrary";
	
	public CQLLibraryDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	class CQLLibraryComparator implements Comparator<CQLLibrary> {

		@Override
		public int compare(CQLLibrary o1, CQLLibrary o2) {
			// 1 if either isDraft
			// 2 version
			if(o1.isDraft()) {
				return -1;
			}
			return o2.isDraft() ? 1 : compareDoubleStrings(o1.getVersion(), o2.getVersion());
		}

		private int compareDoubleStrings(String s1, String s2) {
			Double d1 = Double.parseDouble(s1);
			Double d2 = Double.parseDouble(s2);
			return d2.compareTo(d1);
		}
	}

	/*
	 * assumption: each CQL in this list is part of the same measure set
	 */
	class CQLLibraryListComparator implements Comparator<List<CQLLibrary>> {
		@Override
		public int compare(List<CQLLibrary> o1, List<CQLLibrary> o2) {
			String v1 = o1.get(0).getName();
			String v2 = o2.get(0).getName();
			return v1.compareToIgnoreCase(v2);
		}
	}

	private static final long LOCK_THRESHOLD = TimeUnit.MINUTES.toMillis(3); // 3 minutes

	@Override
	public List<CQLLibrary> searchForIncludes(String setId, String libraryName, String searchText) {

		Session session = getSessionFactory().getCurrentSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<CQLLibrary> query = cb.createQuery(CQLLibrary.class);
		Root<CQLLibrary> root = query.from(CQLLibrary.class);
		
		Predicate predicate = buildPredicateForSearchingLibraries(setId, libraryName, searchText, cb, root);
		
		query.select(root).where(predicate);

		query.orderBy(cb.asc(root.get(LIBRARY_NAME)), cb.desc(root.get(VERSION)));

		return session.createQuery(query).getResultList();
	}

	private Predicate buildPredicateForSearchingLibraries(String setId, String libraryName, String searchText, 
			CriteriaBuilder cb, Root<CQLLibrary> root) {

		Predicate p1 = cb.and(cb.equal(root.get("qdmVersion"), MATPropertiesService.get().getQmdVersion()), 
				cb.equal(root.get(DRAFT), false), cb.notEqual(root.get(LIBRARY_NAME), libraryName), 
				cb.notEqual(root.get(SET_ID), setId));

		Predicate p2 = getSearchByLibraryNameOrOwnerNamePredicate(searchText, cb, root);

		return (p2 != null) ? cb.and(p1, p2) : p1;
	}
	
	@Override
	public List<CQLLibrary> searchForReplaceLibraries(String setId) {

		Session session = getSessionFactory().getCurrentSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<CQLLibrary> query = cb.createQuery(CQLLibrary.class);
		Root<CQLLibrary> root = query.from(CQLLibrary.class);

		query.where(cb.and(cb.equal(root.get("qdmVersion"), MATPropertiesService.get().getQmdVersion())), 
				cb.equal(root.get(DRAFT), false), cb.equal(root.get(SET_ID), setId));

		query.orderBy(cb.asc(root.get(LIBRARY_NAME)), cb.desc(root.get(VERSION))).distinct(true);

		return session.createQuery(query).getResultList();
	}

	@Override
	public List<CQLLibraryShareDTO> search(String searchText, int pageSize, User user, int filter) {
		String userRole = LoggedInUserUtil.getLoggedInUserRole();
		List<CQLLibraryShareDTO> orderedList;
		if (SecurityRole.ADMIN_ROLE.equals(userRole)) {
			orderedList = searchLibrariesForAdmin(searchText);
		} else {
			orderedList = searchForNonAdmin(searchText, user, filter);
		}

		if (pageSize < orderedList.size()) {
			return orderedList.subList(0, pageSize);
		} else {
			return orderedList;
		}

	}

	/**
	 * @param searchText
	 * @param user
	 * @param filter
	 * @param orderedList
	 */
	public List<CQLLibraryShareDTO> searchForNonAdmin(String searchText, User user, int filter) {

		List<CQLLibraryShareDTO> orderedList = new ArrayList<>();

		Session session = getSessionFactory().getCurrentSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<CQLLibrary> query = cb.createQuery(CQLLibrary.class);
		Root<CQLLibrary> root = query.from(CQLLibrary.class);

		Predicate predicate = buildPredicateForSearchingLibrariesForNonAdmin(searchText, cb, root, user.getId(), filter);

		query.select(root).where(predicate).distinct(true);

		query.orderBy(cb.desc(root.get(SET_ID)), cb.desc(root.get(DRAFT)), cb.desc(root.get(VERSION)));

		List<CQLLibrary> libraryResultList = session.createQuery(query).getResultList();
		if (!user.getSecurityRole().getId().equals("2")) {
			libraryResultList = getAllLibrariesInSet(libraryResultList);
		}

		List<CQLLibrary> orderedCQlLibList = null;
		if (libraryResultList != null) {
			orderedCQlLibList = sortLibraryList(libraryResultList);
		} else {
			orderedCQlLibList = new ArrayList<>();
		}

		Map<String, CQLLibraryShareDTO> cqlLibIdDTOMap = new HashMap<>();
		Map<String, CQLLibraryShareDTO> cqlLibSetIdDraftableMap = new HashMap<>();

		for (CQLLibrary cqlLibrary : orderedCQlLibList) {
			CQLLibraryShareDTO dto = extractDTOFromCQLLibrary(cqlLibrary);
			boolean isDraft = dto.isDraft();
			if(isDraft){
				cqlLibSetIdDraftableMap.put(dto.getCqlLibrarySetId(), dto);
			}
			cqlLibIdDTOMap.put(cqlLibrary.getId(), dto);
			orderedList.add(dto);
		}

		List<CQLLibraryShare> shareList = getLibraryShareInfoByShareUserId(user.getId());

		HashMap<String, String> cqlSetIdToShareLevel = new HashMap<>();
		if (CollectionUtils.isNotEmpty(orderedList)) {
			for (CQLLibraryShare share : shareList) {
				String msid = share.getCqlLibrary().getSet_id();
				String shareLevel = share.getShareLevel().getId();
				String existingShareLevel = cqlSetIdToShareLevel.get(msid);
				if (existingShareLevel == null){
					cqlSetIdToShareLevel.put(msid, shareLevel);
				}
			}
			for (int i=0 ;i<orderedList.size();i++) {
				CQLLibraryShareDTO dto = orderedList.get(i);
				String cqlsid = dto.getCqlLibrarySetId();
				boolean hasDraft = cqlLibSetIdDraftableMap.containsKey(cqlsid);
				String shareLevel = cqlSetIdToShareLevel.get(cqlsid);
				boolean isSharedToEdit = false;
				if (shareLevel != null) {
					dto.setShareLevel(shareLevel);
					isSharedToEdit = ShareLevel.MODIFY_ID.equals(shareLevel);
				}
				String userRole = LoggedInUserUtil.getLoggedInUserRole();
				boolean isSuperUser = SecurityRole.SUPER_USER_ROLE.equals(userRole);
				boolean isOwner = LoggedInUserUtil.getLoggedInUser().equals(dto.getOwnerUserId());
				boolean isEditable = (isOwner || isSuperUser || isSharedToEdit);
				if (!dto.isLocked() && isEditable) {

					if(hasDraft){
						CQLLibraryShareDTO draftLibrary = cqlLibSetIdDraftableMap.get(cqlsid);
						if(dto.getCqlLibraryId().equals(draftLibrary.getCqlLibraryId())){
							dto.setVersionable(true);
							dto.setDraftable(false);
						} else if (dto.getCqlLibrarySetId().equals(draftLibrary.getCqlLibrarySetId())){
							dto.setVersionable(false);
							dto.setDraftable(false);
						} 
					} else {
						dto.setVersionable(false);
						dto.setDraftable(true);
					}
				}
			}
		}

		return orderedList;
	}

	private List<CQLLibraryShare> getLibraryShareInfoByShareUserId(String userId){
		Session session = getSessionFactory().getCurrentSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<CQLLibraryShare> query = cb.createQuery(CQLLibraryShare.class);
		Root<CQLLibraryShare> root = query.from(CQLLibraryShare.class);

		query.select(root).where(cb.equal(root.get(SHARE_USER).get("id"), userId));
		
		return session.createQuery(query).getResultList();
	}
	
	private Predicate buildPredicateForSearchingLibrariesForNonAdmin(String searchText, CriteriaBuilder cb, Root<CQLLibrary> root, String userId, int filter) {

		Predicate p1 = buildPredicateForSearchingLibrariesForAdmin(searchText, cb, root);

		Predicate p2 = null;
		
		if (filter == MeasureSearchFilterPanel.MY_MEASURES) {
			Join<CQLLibrary, CQLLibraryShare> childJoin = root.join("shares", JoinType.LEFT);
			p2 = cb.or(cb.equal(root.get(OWNER_ID).get("id"), userId), cb.equal(childJoin.get(SHARE_USER).get("id"), userId));
		}
		
		return (p2 != null) ? cb.and(p1, p2) : p1;
	}
	
	@Override
	public List<CQLLibrary> getAllLibrariesInSet(List<CQLLibrary> libraries) {
		if (CollectionUtils.isNotEmpty(libraries)) {
			Set<String> cqlLibSetIds = new HashSet<>();
			for (CQLLibrary m : libraries) {
				cqlLibSetIds.add(m.getSet_id());
			}

			Session session = getSessionFactory().getCurrentSession();
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<CQLLibrary> query = cb.createQuery(CQLLibrary.class);
			Root<CQLLibrary> root = query.from(CQLLibrary.class);
			
			query.select(root).where(root.get(SET_ID).in(cqlLibSetIds));

			libraries = session.createQuery(query).getResultList();
			
		}
		return sortLibraryList(libraries);
	}

	private List<CQLLibrary> sortLibraryList(List<CQLLibrary> libraryResultList) {
		List<List<CQLLibrary>> libraryList = new ArrayList<>();
		for (CQLLibrary cqlLib : libraryResultList) {
			boolean hasList = false;
			for (List<CQLLibrary> list : libraryList) {
				String cqlsetId = list.get(0).getSet_id();
				if (cqlLib.getSet_id().equalsIgnoreCase(cqlsetId)) {
					list.add(cqlLib);
					hasList = true;
					break;
				}
			}
			if (!hasList) {
				List<CQLLibrary> cqllist = new ArrayList<>();
				cqllist.add(cqlLib);
				libraryList.add(cqllist);
			}
		}

		for (List<CQLLibrary> list : libraryList) {
			Collections.sort(list, new CQLLibraryComparator());
		}
		
		Collections.sort(libraryList, new CQLLibraryListComparator());

		List<CQLLibrary> retList = new ArrayList<>();
		for (List<CQLLibrary> mlist : libraryList) {
			for (CQLLibrary m : mlist) {
				retList.add(m);
			}
		}
		return retList;
	}

	@Override
	public boolean isLibraryLocked(String cqlLibraryId) {
		Session session = getSessionFactory().getCurrentSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Timestamp> query = cb.createQuery(Timestamp.class);
		Root<CQLLibrary> root = query.from(CQLLibrary.class);

		query.select(root.get("lockedOutDate")).where(cb.equal(root.get("id"), cqlLibraryId));
		
		Timestamp lockedOutDate = session.createQuery(query).getSingleResult();

		return isLocked(lockedOutDate);
	}

	/**
	 * Checks if is locked.
	 * 
	 * @param lockedOutDate
	 *            the locked out date
	 * @return false if current time - lockedOutDate < the lock threshold
	 */
	private boolean isLocked(Date lockedOutDate) {

		if (lockedOutDate == null) {
			return false;
		}

		long timeDiff = System.currentTimeMillis() - lockedOutDate.getTime();

		return (timeDiff < LOCK_THRESHOLD);
	}

	@Override
	public void updateLockedOutDate(CQLLibrary existingLibrary) {
		Session session = getSessionFactory().getCurrentSession();
		CQLLibrary cqlLibrary = session.load(CQLLibrary.class, existingLibrary.getId());
		cqlLibrary.setId(existingLibrary.getId());
		cqlLibrary.setLockedOutDate(null);
		cqlLibrary.setLockedUserId(null);
		session.update(cqlLibrary);
	}

	@Override
	public String findMaxVersion(String setId, String ownerId) {
		Session session = getSessionFactory().getCurrentSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<String> query = cb.createQuery(String.class);
		Root<CQLLibrary> root = query.from(CQLLibrary.class);

		Predicate predicate = buildPredicateForMaxVersion(setId, ownerId, cb, root);
		
		query.select(cb.max(root.get(VERSION)).as(String.class)).where(predicate);
		
		return session.createQuery(query).getSingleResult();
	}
	
	private Predicate buildPredicateForMaxVersion(String setId, String ownerId, CriteriaBuilder cb, Root<CQLLibrary> root) {
		// add check to filter Draft's version number when finding max version number.
		Predicate p1 = cb.and(cb.equal(root.get(SET_ID), setId), cb.not(root.get(DRAFT)));
		Predicate p2 = null;
		if(StringUtils.isNotBlank(ownerId)){
			p2 = cb.equal(root.get(OWNER_ID).get("id"), ownerId);
		}
		
		return (p2 != null) ? cb.and(p1, p2) : p1;  
	}

	@Override
	public String findMaxOfMinVersion(String setId, String version) {
		logger.info("In CQLLibraryDAO.findMaxOfMinVersion()");
		String maxOfMinVersion = version;
		double minVal = 0;
		double maxVal = 0;
		if (StringUtils.isNotBlank(version)) {
			int decimalIndex = version.indexOf('.');
			minVal = Integer.parseInt(version.substring(0, decimalIndex));
			logger.info("Min value: " + minVal);
			maxVal = minVal + 1;
			logger.info("Max value: " + maxVal);
		}

		List<CQLLibrary> cqlList = getListOfVersionsForALibrary(setId);
		
		double tempVersion = 0;
		
		if (CollectionUtils.isNotEmpty(cqlList)) {
			logger.info("Finding max of min version from the Library List. Size:" + cqlList.size());
			for (CQLLibrary library : cqlList) {
				logger.info("Looping through Lib Id: " + library.getId() + " Version: " + library.getVersion());
				if ((library.getVersionNumber() > minVal) && (library.getVersionNumber() < maxVal)) {
					if (tempVersion < library.getVersionNumber()) {
						logger.info(tempVersion + "<" + library.getVersionNumber() + "=" + (tempVersion < library.getVersionNumber()));
						maxOfMinVersion = library.getVersion();
						logger.info("maxOfMinVersion: " + maxOfMinVersion);
					}
					tempVersion = library.getVersionNumber();
					logger.info("tempVersion: " + tempVersion);
				}
			}
		}
		logger.info("Returned maxOfMinVersion: " + maxOfMinVersion);
		return maxOfMinVersion;
	}
	
	private List<CQLLibrary> getListOfVersionsForALibrary(String setId){
		
		Session session = getSessionFactory().getCurrentSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<CQLLibrary> query = cb.createQuery(CQLLibrary.class);
		Root<CQLLibrary> root = query.from(CQLLibrary.class);
		
		query.select(root).where(cb.and(cb.equal(root.get(SET_ID), setId), cb.not(root.get(DRAFT))));
		query.orderBy(cb.asc(root.get(VERSION)));
		
		return session.createQuery(query).getResultList();

	}

	/**
	 * This method returns a List of CQLLibraryShareDTO objects which have
	 * userId,firstname,lastname and sharelevel for the given measureId.
	 * 
	 * @param id
	 *            the measure id
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @return the measure share info for measure
	 */
	@Override
	public List<CQLLibraryShareDTO> getLibraryShareInfoForLibrary(String cqlId, String searchText) {
		int pageSize = Integer.MAX_VALUE;
		
		List<User> userResults = getUserListToShare(searchText);
		
		HashMap<String, CQLLibraryShareDTO> userIdDTOMap = new HashMap<>();
		ArrayList<CQLLibraryShareDTO> orderedDTOList = new ArrayList<>();
		
		for (User user : userResults) {
			CQLLibraryShareDTO dto = new CQLLibraryShareDTO();		
			dto.setUserId(user.getId());
			dto.setFirstName(user.getFirstName());
			dto.setLastName(user.getLastName());
			dto.setOrganizationName(user.getOrganizationName());
			userIdDTOMap.put(user.getId(), dto);
			orderedDTOList.add(dto);
		}

		if (CollectionUtils.isNotEmpty(orderedDTOList)) {			
			
			List<CQLLibraryShare> shareList = getShareList(cqlId, userIdDTOMap);
			
			for (CQLLibraryShare share : shareList) {
				User shareUser = share.getShareUser();
				CQLLibraryShareDTO dto = userIdDTOMap.get(shareUser.getId());
				dto.setShareLevel(share.getShareLevel().getId());
			}
		}
		
		return (pageSize < orderedDTOList.size()) ?  orderedDTOList.subList(0, pageSize) : orderedDTOList;
	}
	
	private List<User> getUserListToShare(String searchText) {
		Criteria userCriteria = getSessionFactory().getCurrentSession().createCriteria(User.class);
		
		userCriteria.add(Restrictions.eq("securityRole.id", "3"));
		// Added restriction for Active user's for User story MAT:2900.
		userCriteria.add(Restrictions.eq("status.id", "1"));
		userCriteria.add(Restrictions.ne("id", LoggedInUserUtil.getLoggedInUser()));
		//Added restriction for Search by user name MAT-8908.
		if(StringUtils.isNotBlank(searchText)) {
			String searchString = searchText.toLowerCase();
			userCriteria.add(Restrictions.or(Restrictions.ilike(FIRST_NAME, "%" + searchString + "%"),
					Restrictions.ilike(LAST_NAME, "%" + searchString + "%")));
		}
		userCriteria.setFirstResult(0);
		userCriteria.addOrder(Order.asc(LAST_NAME));

		return userCriteria.list();
	}

	private List<CQLLibraryShare> getShareList(String cqlId, HashMap<String, CQLLibraryShareDTO> userIdDTOMap){
		Session session = getSessionFactory().getCurrentSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<CQLLibraryShare> query = cb.createQuery(CQLLibraryShare.class);
		Root<CQLLibraryShare> root = query.from(CQLLibraryShare.class);

		query.select(root).where(cb.and(root.get(SHARE_USER).get("id").in(userIdDTOMap.keySet()), cb.equal(root.get(CQL_LIBRARY).get("id"), cqlId)));

		return session.createQuery(query).getResultList();
	}

	@Override
	public List<CQLLibraryShare> getLibraryShareInforForLibrary(String libId) {
		if (libId == null) {
			return Collections.emptyList();
		}
		
		Session session = getSessionFactory().getCurrentSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<CQLLibraryShare> query = cb.createQuery(CQLLibraryShare.class);
		Root<CQLLibraryShare> root = query.from(CQLLibraryShare.class);
		
		query.select(root).where(cb.equal(root.get(CQL_LIBRARY).get("id"), libId));
		
		return session.createQuery(query).getResultList();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.dao.clause.MeasureDAO#findShareLevelForUser(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public ShareLevel findShareLevelForUser(String cqlLibraryId, String userID, String cqlLibrarySetId) {
		ShareLevel shareLevel = null;
		
		Session session = getSessionFactory().getCurrentSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<CQLLibraryShare> query = cb.createQuery(CQLLibraryShare.class);
		Root<CQLLibraryShare> root = query.from(CQLLibraryShare.class);
		
		Subquery<CQLLibrary> sq = query.subquery(CQLLibrary.class);
		Root<CQLLibrary> lib = sq.from(CQLLibrary.class);

		sq.select(lib).where(cb.equal(lib.get(SET_ID), cqlLibrarySetId));
		
		query.select(root).where(cb.and(cb.equal(root.get(SHARE_USER).get("id"), userID)), 
				cb.in(root.get(CQL_LIBRARY).get("id")).value(sq));
		
		List<CQLLibraryShare> shareList = session.createQuery(query).getResultList();
		
		if (CollectionUtils.isNotEmpty(shareList)) {
			shareLevel = shareList.get(0).getShareLevel();
		}

		return shareLevel;
	}

	@Override
	public CQLLibraryShareDTO extractDTOFromCQLLibrary(CQLLibrary cqlLibrary) {
		CQLLibraryShareDTO dto = new CQLLibraryShareDTO();

		dto.setCqlLibraryId(cqlLibrary.getId());
		dto.setCqlLibraryName(cqlLibrary.getName());
		dto.setOwnerUserId(cqlLibrary.getOwnerId().getId());
		dto.setDraft(cqlLibrary.isDraft());
		dto.setVersion(cqlLibrary.getVersion());
		dto.setFinalizedDate(cqlLibrary.getFinalizedDate());
		dto.setCqlLibrarySetId(cqlLibrary.getSet_id());
		dto.setRevisionNumber(cqlLibrary.getRevisionNumber());
		boolean isLocked = isLocked(cqlLibrary.getLockedOutDate());
		dto.setLocked(isLocked);
		if (isLocked && (cqlLibrary.getLockedUserId() != null)) {
			LockedUserInfo lockedUserInfo = new LockedUserInfo();
			lockedUserInfo.setUserId(cqlLibrary.getLockedUserId().getId());
			lockedUserInfo.setEmailAddress(cqlLibrary.getLockedUserId().getEmailAddress());
			lockedUserInfo.setFirstName(cqlLibrary.getLockedUserId().getFirstName());
			lockedUserInfo.setLastName(cqlLibrary.getLockedUserId().getLastName());
			dto.setLockedUserInfo(lockedUserInfo);
		}
		return dto;
	}

	@Override
	public List<CQLLibrary> getLibraryListForLibraryOwner(User user) {
		Session session = getSessionFactory().getCurrentSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<CQLLibrary> query = cb.createQuery(CQLLibrary.class);
		Root<CQLLibrary> root = query.from(CQLLibrary.class);
		
		query.select(root).where(cb.equal(root.get(OWNER_ID).get("id"), user.getId()));
		query.orderBy(cb.asc(root.get(VERSION)));

		List<CQLLibrary> cList = session.createQuery(query).getResultList();
		return sortLibraryListForLibraryOwner(cList);
	}

	/**
	 * Group the CQL Libraries with library set id and find draft or highest
	 * version number which ever is available.
	 * 
	 * @param libraryResultList
	 * @return
	 */
	private List<CQLLibrary> sortLibraryListForLibraryOwner(List<CQLLibrary> libraryResultList) {
		// generate sortable lists
		List<List<CQLLibrary>> librariesLists = new ArrayList<>();
		for (CQLLibrary lib : libraryResultList) {
			boolean hasList = false;
			for (List<CQLLibrary> clist : librariesLists) {
				String setId = clist.get(0).getSet_id();
				if (lib.getSet_id().equalsIgnoreCase(setId)) {
					clist.add(lib);
					hasList = true;
					break;
				}
			}
			if (!hasList) {
				List<CQLLibrary> clist = new ArrayList<>();
				clist.add(lib);
				librariesLists.add(clist);
			}
		}
		Collections.sort(librariesLists, new CQLLibraryListComparator());
		// compile list
		List<CQLLibrary> retList = new ArrayList<>();
		for (List<CQLLibrary> clist : librariesLists) {
			boolean isDraftAvailable = false;
			CQLLibrary cql = null;
			for (CQLLibrary lib : clist) {
				cql = lib;
				if (lib.isDraft()) {
					isDraftAvailable = true;
					retList.add(lib);
					break;
				}
			}
			
			if (!isDraftAvailable && (cql != null)) {
				
				String maxVersion = findMaxVersion(cql.getSet_id(), cql.getOwnerId().getId());

				for (CQLLibrary lib : clist) {
					if (!lib.isDraft() && lib.getVersion().equalsIgnoreCase(maxVersion)) {
						retList.add(lib);
						break;
					}
				}
			}
		}
		return retList;
	}

	public List<CQLLibraryShareDTO> searchLibrariesForAdmin(String searchText) {
		Session session = getSessionFactory().getCurrentSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<CQLLibrary> query = cb.createQuery(CQLLibrary.class);
		Root<CQLLibrary> root = query.from(CQLLibrary.class);
		
		Predicate predicate = buildPredicateForSearchingLibrariesForAdmin(searchText, cb, root);
		
		query.select(root).where(predicate).distinct(true);
		
		query.orderBy(cb.desc(root.get(SET_ID)), cb.desc(root.get(DRAFT)), cb.desc(root.get(VERSION)));
		
		List<CQLLibrary> libraryResultList = session.createQuery(query).getResultList();
		libraryResultList = getAllLibrariesInSet(libraryResultList);

		ArrayList<CQLLibraryShareDTO> orderedDTOList = new ArrayList<>();
		for (CQLLibrary library : libraryResultList) {
			CQLLibraryShareDTO dto = extractDTOFromCQLLibrary(library);
			orderedDTOList.add(dto);
		}

		return filterLibraryListForAdmin(orderedDTOList);
	}
	
	private Predicate buildPredicateForSearchingLibrariesForAdmin(String searchText, CriteriaBuilder cb, Root<CQLLibrary> root) {

		Predicate p1 = cb.isNull(root.get(MEASURE_ID));

		Predicate p2 = getSearchByLibraryNameOrOwnerNamePredicate(searchText, cb, root);

		return (p2 != null) ? cb.and(p1, p2) : p1;
	}

	private Predicate getSearchByLibraryNameOrOwnerNamePredicate(String searchText, CriteriaBuilder cb, Root<CQLLibrary> root) {
		Predicate searchPredicate = null;
		if(StringUtils.isNotBlank(searchText)) {
			String lowerCaseSearchText = searchText.toLowerCase();
			searchPredicate = cb.or(cb.like(cb.lower(root.get(LIBRARY_NAME)), "%" + lowerCaseSearchText + "%"),
					cb.like(cb.lower(root.get(OWNER_ID).get(FIRST_NAME)), "%" + lowerCaseSearchText + "%"),
					cb.like(cb.lower(root.get(OWNER_ID).get(LAST_NAME)), "%" + lowerCaseSearchText + "%"));
		}
		return searchPredicate;
	}
	
	/**
	 * cqlLibraryList is filtered with latest draft or version. In a set, first
	 * we look for a draft version. If there is a draft version then that
	 * library is added in the cqlLibraryList. Otherwise, we look for the latest
	 * version and add in the cqlLibraryList. Latest version library is the
	 * library with the latest Finalized Date.
	 *
	 * @param cqlLibraryList
	 *            - {@link List} of {@link CQLLibraryShareDTO}.
	 * @return {@link List} of {@link CQLLibraryShareDTO}.
	 */
	private List<CQLLibraryShareDTO> filterLibraryListForAdmin(final List<CQLLibraryShareDTO> cqlLibraryList) {
		List<CQLLibraryShareDTO> updatedLibraryList = new ArrayList<>();
		for (CQLLibraryShareDTO libraryShareDTO : cqlLibraryList) {
			if (CollectionUtils.isEmpty(updatedLibraryList)) {
				updatedLibraryList.add(libraryShareDTO);
			} else {
				boolean found = false;
				ListIterator<CQLLibraryShareDTO> itr = updatedLibraryList.listIterator();
				while (itr.hasNext()) {
					CQLLibraryShareDTO shareDTO = itr.next();
					if (libraryShareDTO.getCqlLibrarySetId().equals(shareDTO.getCqlLibrarySetId())) {
						found = true;
						if (libraryShareDTO.isDraft()) {
							itr.remove();
							itr.add(libraryShareDTO);
						} else if (!shareDTO.isDraft()) {
							if (libraryShareDTO.getFinalizedDate().compareTo(shareDTO.getFinalizedDate()) > 0) {
								itr.remove();
								itr.add(libraryShareDTO);
							}
						}
					}
				}
				if (!found) {
					updatedLibraryList.add(libraryShareDTO);
				}
			}
		}
		return updatedLibraryList;
	}
	@Override
	public void refresh(CQLLibrary libObject){
		getSessionFactory().getCurrentSession().refresh(libObject);
	}
	
	@Override
	public CQLLibrary getLibraryByMeasureId(String measureId){
		Session session = getSessionFactory().getCurrentSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<CQLLibrary> query = cb.createQuery(CQLLibrary.class);
		Root<CQLLibrary> root = query.from(CQLLibrary.class);

		query.select(root).where(cb.equal(root.get(MEASURE_ID), measureId));
		
		List<CQLLibrary> resultCqlLibrary = session.createQuery(query).getResultList();		
		
		return CollectionUtils.isNotEmpty(resultCqlLibrary) ? resultCqlLibrary.get(0) : null;
		
	}

}
