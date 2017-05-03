package mat.dao.impl.clause;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.MeasureSearchFilterPanel;
import mat.dao.search.GenericDAO;
import mat.dao.service.DAOService;
import mat.model.LockedUserInfo;
import mat.model.SecurityRole;
import mat.model.User;
import mat.model.clause.Measure;
import mat.model.clause.MeasureSet;
import mat.model.clause.MeasureShare;
import mat.model.clause.MeasureShareDTO;
import mat.model.clause.ShareLevel;
import mat.model.cql.CQLLibraryShareDTO;
import mat.server.LoggedInUserUtil;
import mat.shared.StringUtility;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.ApplicationContext;


// TODO: Auto-generated Javadoc
/**
 * The Class MeasureDAO.
 */
public class MeasureDAO extends GenericDAO<Measure, String> implements
mat.dao.clause.MeasureDAO {
	
	/*
	 * assumption: measures here are all part of the same measure set
	 */
	/*
	 * The Class MeasureComparator.
	 */
	class MeasureComparator implements Comparator<Measure> {
		
		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(Measure o1, Measure o2) {
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
	 * assumption: each measure in this list is part of the same measure set
	 */
	/**
	 * The Class MeasureListComparator.
	 */
	class MeasureListComparator implements Comparator<List<Measure>> {
		
		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(List<Measure> o1, List<Measure> o2) {
			String v1 = o1.get(0).getDescription();
			String v2 = o2.get(0).getDescription();
			return v1.compareToIgnoreCase(v2);
		}
	}
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(MeasureDAO.class);
	
	/** The context. */
	private ApplicationContext context = null;
	
	/** The d ao service. */
	private DAOService dAOService = null;
	
	/** The lock threshold. */
	private final long lockThreshold = 3 * 60 * 1000; // 3 minutes
	
	/**
	 * Instantiates a new measure dao.
	 */
	public MeasureDAO() {
		
	}
	
	/**
	 * Instantiates a new measure dao.
	 * 
	 * @param context
	 *            the context
	 */
	public MeasureDAO(ApplicationContext context) {
		this.context = context;
	}
	
	/**
	 * Instantiates a new measure dao.
	 * 
	 * @param dAOService
	 *            the d ao service
	 */
	public MeasureDAO(DAOService dAOService) {
		// allow to test using DAOService
		this.dAOService = dAOService;
	}
	
	/**
	 * Builds the measure share for user criteria.
	 * 
	 * @param user
	 *            the user
	 * @return the criteria
	 */
	private Criteria buildMeasureShareForUserCriteria(User user) {
		Criteria mCriteria = getSessionFactory().getCurrentSession()
				.createCriteria(Measure.class);
		/*
		 * if(user.getSecurityRole().getId().equals("3")) {
		 * mCriteria.add(Restrictions.or(Restrictions.eq("owner.id",
		 * user.getId()), Restrictions.eq("share.shareUser.id", user.getId())));
		 * mCriteria.createAlias("shares", "share", Criteria.LEFT_JOIN); }
		 */
		
		mCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return mCriteria;
	}
	
	/**
	 * Builds the measure share for user criteria with filter.
	 * 
	 * @param user
	 *            the user
	 * @param filter
	 *            the filter
	 * @return the criteria
	 */
	private Criteria buildMeasureShareForUserCriteriaWithFilter(User user,
			int filter) {
		Criteria mCriteria = getSessionFactory().getCurrentSession()
				.createCriteria(Measure.class);
		if (filter == MeasureSearchFilterPanel.MY_MEASURES) {
			mCriteria.add(Restrictions.or(
					Restrictions.eq("owner.id", user.getId()),
					Restrictions.eq("share.shareUser.id", user.getId())));
			/* mCriteria.add(Restrictions.ne("deleted", "softDeleted")); */
			mCriteria.createAlias("shares", "share", Criteria.LEFT_JOIN);
		}
		
		mCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return mCriteria;
	}
	@Override
	public List<Measure> getMeasureListForMeasureOwner(User user){
		Criteria mCriteria = getSessionFactory().getCurrentSession()
				.createCriteria(Measure.class);
		mCriteria.add(Restrictions.eq("owner.id", user.getId()));
		List<Measure> measureList = mCriteria.list();
		return sortMeasureListForMeasureOwner(measureList);
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.clause.MeasureDAO#getComponentMeasureInfoForMeasures(java.util.List)
	 */
	@Override
	public List<Measure> getComponentMeasureInfoForMeasures(List<String> measureIds) {
		Criteria mCriteria = buildComponentMeasureShareForUserCriteria(measureIds);
		List<Measure> measure = mCriteria.list();
		System.out.println("Measure List Size: "+ measure.size());
		return measure;
	}
	
	/**
	 * Builds the component measure share for user criteria.
	 *
	 * @param listComponentMeasureIds the list component measure ids
	 * @return the criteria
	 */
	private Criteria buildComponentMeasureShareForUserCriteria(List<String> listComponentMeasureIds) {
		Criteria mCriteria = getSessionFactory().getCurrentSession()
				.createCriteria(Measure.class);
		//mCriteria.add(Restrictions.eq("id", "8a4d8cb2452d647301452d88111b000a"));
		mCriteria.add(Restrictions.in("id", listComponentMeasureIds));
		mCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return mCriteria;
	}
	
	/**
	 * Cloner.
	 * 
	 * @param currentDetails
	 *            the current details
	 * @param context
	 *            the context
	 * @param m
	 *            the m
	 * @return the measure
	 */
	private Measure cloner(ManageMeasureDetailModel currentDetails,
			ApplicationContext context, Measure m) {
		
		if (m == null) {
			return null;
		}
		
		Measure cloned = new Measure();
		// new measure ID
		// cloned.setId(UUID.randomUUID().toString());//auto generated by hbm
		// file
		if (m.getaBBRName() != null) {
			cloned.setaBBRName(currentDetails.getShortName());
		}
		// if (m.getClauses()!=null) cloned.setClauses(m.getClauses());
		if (m.getDescription() != null) {
			cloned.setDescription(currentDetails.getName());
		}
		/*if (m.getMeasureStatus() != null) {
			cloned.setMeasureStatus(m.getMeasureStatus());
		}*/
		if (m.getOwner() != null) {
			cloned.setOwner(m.getOwner());
		}
		// if (m.getShares()!=null) cloned.setShares(m.getShares());
		if (m.getVersion() != null) {
			cloned.setVersion(m.getVersion());
		}
		cloned.setDraft(true);
		if (currentDetails.getMeasScoring() != null) {
			cloned.setMeasureScoring(currentDetails.getMeasScoring());
		}
		
		return cloned;
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.clause.MeasureDAO#countMeasureForDraft(mat.model.User)
	 */
	@Override
	public int countMeasureForDraft(String searchText, User user) {
		List<MeasureShareDTO> dtoList = getMeasuresForDraft(searchText, user);
		return dtoList.size();
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.clause.MeasureDAO#countMeasureForVersion(mat.model.User)
	 */
	@Override
	public int countMeasureForVersion(String searchText, User user) {
		List<MeasureShareDTO> dtoList = getMeasuresForVersion(searchText, user);
		return dtoList.size();
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.clause.MeasureDAO#countMeasureShareInfoForUser(int, mat.model.User)
	 */
	@Override
	public int countMeasureShareInfoForUser(int filter, User user) {
		Criteria mCriteria = buildMeasureShareForUserCriteriaWithFilter(user,
				filter);
		// mCriteria.add(Restrictions.isNull("deleted"));
		List<Measure> ms = mCriteria.list();
		ms = getAllMeasuresInSet(ms);
		return ms.size();
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.clause.MeasureDAO#countMeasureShareInfoForUser(java.lang.String, mat.model.User)
	 */
	@Override
	public int countMeasureShareInfoForUser(String searchText, User user) {
		
		String searchTextLC = searchText.toLowerCase().trim();
		
		Criteria mCriteria = buildMeasureShareForUserCriteria(user);
		List<Measure> ms = mCriteria.list();
		
		ms = getAllMeasuresInSet(ms);
		
		int count = 0;
		for (Measure m : ms) {
			boolean increment = m.getaBBRName().toLowerCase()
					.contains(searchTextLC) ? true : m.getDescription()
							.toLowerCase().contains(searchTextLC) ? true : false;
			if (increment) {
				count++;
			}
		}
		return count;
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.clause.MeasureDAO#countMeasureShareInfoForUser(mat.model.User)
	 */
	@Override
	public int countMeasureShareInfoForUser(User user) {
		Criteria mCriteria = buildMeasureShareForUserCriteria(user);
		// mCriteria.add(Restrictions.isNull("deleted"));
		List<Measure> measureList = mCriteria.list();
		measureList = getAllMeasuresInSet(measureList);
		return measureList.size();
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.clause.MeasureDAO#countUsersForMeasureShare()
	 */
	@Override
	public int countUsersForMeasureShare() {
		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(User.class);
		criteria.add(Restrictions.eq("securityRole.id", "3"));
		criteria.add(Restrictions.ne("id", LoggedInUserUtil.getLoggedInUser()));
		criteria.setProjection(Projections.rowCount());
		return ((Long) criteria.uniqueResult()).intValue();
	}
	
	/**
	 * Extract dto from measure.
	 * 
	 * @param measure
	 *            the measure
	 * @return the measure share dto
	 */
	@Override
	public MeasureShareDTO extractDTOFromMeasure(Measure measure) {
		MeasureShareDTO dto = new MeasureShareDTO();
		
		dto.setMeasureId(measure.getId());
		dto.setMeasureName(measure.getDescription());
		dto.setScoringType(measure.getMeasureScoring());
		dto.setShortName(measure.getaBBRName());
		/*dto.setStatus(measure.getMeasureStatus());*/
		dto.setPackaged(measure.getExportedDate() != null);
		dto.setOwnerUserId(measure.getOwner().getId());
		
		/* US501 */
		dto.setDraft(measure.isDraft());
		dto.setVersion(measure.getVersion());
		dto.setFinalizedDate(measure.getFinalizedDate());
		dto.setMeasureSetId(measure.getMeasureSet().getId());
		dto.seteMeasureId(measure.geteMeasureId());
		dto.setPrivateMeasure(measure.getIsPrivate());
		dto.setRevisionNumber(measure.getRevisionNumber());
		boolean isLocked = isLocked(measure.getLockedOutDate());
		dto.setLocked(isLocked);
		if (isLocked && (measure.getLockedUser() != null)) {
			LockedUserInfo lockedUserInfo = new LockedUserInfo();
			lockedUserInfo.setUserId(measure.getLockedUser().getId());
			lockedUserInfo.setEmailAddress(measure.getLockedUser()
					.getEmailAddress());
			lockedUserInfo.setFirstName(measure.getLockedUser().getFirstName());
			lockedUserInfo.setLastName(measure.getLockedUser().getLastName());
			dto.setLockedUserInfo(lockedUserInfo);
		}
		return dto;
	}
	
	/**
	 * measureList is filtered with latest draft or version. In a measure set,
	 * first we look for a draft version. If there is a draft version then that
	 * measure is added in the measureList. Otherwise, we look for the latest
	 * version and add in the measureList. Latest version measure is the measure
	 * with the latest Finalized Date.
	 *
	 * @param measureList
	 *            - {@link List} of {@link MeasureShareDTO}.
	 * @return {@link List} of {@link MeasureShareDTO}.
	 */
	private List<MeasureShareDTO> filterMeasureListForAdmin(
			final List<MeasureShareDTO> measureList) {
		List<MeasureShareDTO> updatedMeasureList = new ArrayList<MeasureShareDTO>();
		for (MeasureShareDTO measureShareDTO : measureList) {
			if ((updatedMeasureList == null) || updatedMeasureList.isEmpty()) {
				updatedMeasureList.add(measureShareDTO);
			} else {
				boolean found = false;
				ListIterator<MeasureShareDTO> itr = updatedMeasureList
						.listIterator();
				while (itr.hasNext()) {
					MeasureShareDTO shareDTO = itr.next();
					if (measureShareDTO.getMeasureSetId().equals(shareDTO
							.getMeasureSetId())) {
						found = true;
						if (measureShareDTO.isDraft()) {
							itr.remove();
							itr.add(measureShareDTO);
						} else if (!shareDTO.isDraft()) {
							if (measureShareDTO.getFinalizedDate().compareTo(
									shareDTO.getFinalizedDate()) > 0) {
								itr.remove();
								itr.add(measureShareDTO);
							}
						}
					}
				}
				if (!found) {
					updatedMeasureList.add(measureShareDTO);
				}
			}
		}
		return updatedMeasureList;
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.clause.MeasureDAO#findByOwnerId(java.lang.String)
	 */
	@Override
	public java.util.List<Measure> findByOwnerId(String measureOwnerId) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Measure.class);
		criteria.add(Restrictions.eq("owner.id", measureOwnerId));
		return criteria.list();
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.clause.MeasureDAO#findMaxOfMinVersion(java.lang.String, java.lang.String)
	 */
	@Override
	public String findMaxOfMinVersion(String measureSetId, String version) {
		logger.info("In MeasureDao.findMaxOfMinVersion()");
		String maxOfMinVersion = version;
		double minVal = 0;
		double maxVal = 0;
		if (StringUtils.isNotBlank(version)) {
			int decimalIndex = version.indexOf('.');
			minVal = Integer.valueOf(version.substring(0, decimalIndex))
					.intValue();
			logger.info("Min value: " + minVal);
			maxVal = minVal + 1;
			logger.info("Max value: " + maxVal);
		}
		Criteria mCriteria = getSessionFactory().getCurrentSession()
				.createCriteria(Measure.class);
		// mCriteria.add(Restrictions.and(Restrictions.eq("measureSet.id",
		// measureSetId),
		// Restrictions.and(Restrictions.sizeGt("version",
		// minVal),Restrictions.sizeLt("version", maxVal))));
		// mCriteria.setProjection(Projections.max("version"));
		logger.info("Query Using Measure Set Id:" + measureSetId);
		mCriteria.add(Restrictions.eq("measureSet.id", measureSetId));
		mCriteria.add(Restrictions.ne("draft", true));
		mCriteria.addOrder(Order.asc("version"));
		List<Measure> measureList = mCriteria.list();
		double tempVersion = 0;
		if ((measureList != null) && (measureList.size() > 0)) {
			logger.info("Finding max of min version from the Measure List. Size:"
					+ measureList.size());
			for (Measure measure : measureList) {
				logger.info("Looping through Measures Id: " + measure.getId()
						+ " Version: " + measure.getVersion());
				if ((measure.getVersionNumber() > minVal)
						&& (measure.getVersionNumber() < maxVal)) {
					if (tempVersion < measure.getVersionNumber()) {
						logger.info(tempVersion + "<"
								+ measure.getVersionNumber() + "="
								+ (tempVersion < measure.getVersionNumber()));
						maxOfMinVersion = measure.getVersion();
						logger.info("maxOfMinVersion: " + maxOfMinVersion);
					}
					tempVersion = measure.getVersionNumber();
					logger.info("tempVersion: " + tempVersion);
				}
			}
		}
		logger.info("Returned maxOfMinVersion: " + maxOfMinVersion);
		return maxOfMinVersion;
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.clause.MeasureDAO#findMaxVersion(java.lang.String)
	 */
	@Override
	public String findMaxVersion(String measureSetId) {
		Criteria mCriteria = getSessionFactory().getCurrentSession()
				.createCriteria(Measure.class);
		mCriteria.add(Restrictions.eq("measureSet.id", measureSetId));
		// add check to filter Draft's version number when finding max version
		// number.
		mCriteria.add(Restrictions.ne("draft", true));
		mCriteria.setProjection(Projections.max("version"));
		String maxVersion = (String) mCriteria.list().get(0);
		return maxVersion;
	}
	
	/**
	 * for all measure sets referenced in measures in ms, return all measures
	 * that are members of the set.
	 * 
	 * @param ms
	 *            the ms
	 * @return the all measures in set
	 */
	@Override
	public List<Measure> getAllMeasuresInSet(List<Measure> ms) {
		if (!ms.isEmpty()) {
			Set<String> measureSetIds = new HashSet<String>();
			for (Measure m : ms) {
				measureSetIds.add(m.getMeasureSet().getId());
			}
			
			Criteria setCriteria = getSessionFactory().getCurrentSession()
					.createCriteria(Measure.class);
			setCriteria.add(Restrictions.in("measureSet.id", measureSetIds));
			ms = setCriteria.list();
		}
		return sortMeasureList(ms);
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.clause.MeasureDAO#getMaxEMeasureId()
	 */
	@Override
	public int getMaxEMeasureId() {
		Session session = getSessionFactory().getCurrentSession();
		String sql = "select max(eMeasureId) from mat.model.clause.Measure";
		Query query = session.createQuery(sql);
		List<Integer> result = query.list();
		if (!result.isEmpty()) {
			return result.get(0).intValue();
		} else {
			return 0;
		}
		
	}
	
	/**
	 * Gets the measures for draft.
	 *
	 * @param searchText the search text
	 * @param user the user
	 * @return the measures for draft
	 */
	@Override
	public List<MeasureShareDTO> getMeasuresForDraft(String searchText, User user) {
		List<MeasureShareDTO> orderedDTOList = getMeasureShareInfoForUser(searchText,user,
				0, Integer.MAX_VALUE);
		
		HashSet<String> hasDraft = new HashSet<String>();
		for (MeasureShareDTO dto : orderedDTOList) {
			if (dto.isDraft()) {
				String setId = dto.getMeasureSetId();
				hasDraft.add(setId);
			}
		}
		List<MeasureShareDTO> dtoList = new ArrayList<MeasureShareDTO>();
		for (MeasureShareDTO dto : orderedDTOList) {
			
			boolean canDraft = dto.isDraft() ? false
					: hasDraft.contains(dto.getMeasureSetId()) ? false
							: dto.isLocked() ? false
									: user.getSecurityRole()
									.getDescription()
									.equalsIgnoreCase(
											SecurityRole.SUPER_USER_ROLE) ? true
													: dto.getOwnerUserId()
													.equalsIgnoreCase(
															user.getId()) ? true
																	: dto.getShareLevel() == null ? false
																			: dto.getShareLevel()
																			.equalsIgnoreCase(
																					ShareLevel.MODIFY_ID) ? true
																							: false;
			
			if (canDraft) {
				dtoList.add(dto);
			}
		}
		return dtoList;
	}
	
	/*@Override
	public List<MeasureShareDTO> getMeasuresForDraft(String text,User user, int startIndex,
			int pageSize) {
		List<MeasureShareDTO> dtoList = getMeasuresForDraft(text, user);
		if (pageSize < dtoList.size()) {
			return dtoList.subList(startIndex,
					Math.min(startIndex + pageSize, dtoList.size()));
		} else {
			return dtoList;
		}
	}*/
	
	/** Gets the measures for draft.
	 * 
	 * @param user the user
	 * @return the measures for draft */
	public List<MeasureShareDTO> getMeasuresForDraft(User user) {
		List<MeasureShareDTO> orderedDTOList = getMeasureShareInfoForUser(user,
				0, Integer.MAX_VALUE);
		
		HashSet<String> hasDraft = new HashSet<String>();
		for (MeasureShareDTO dto : orderedDTOList) {
			if (dto.isDraft()) {
				String setId = dto.getMeasureSetId();
				hasDraft.add(setId);
			}
		}
		List<MeasureShareDTO> dtoList = new ArrayList<MeasureShareDTO>();
		for (MeasureShareDTO dto : orderedDTOList) {
			
			boolean canDraft = dto.isDraft() ? false
					: hasDraft.contains(dto.getMeasureSetId()) ? false
							: dto.isLocked() ? false
									: user.getSecurityRole()
									.getDescription()
									.equalsIgnoreCase(
											SecurityRole.SUPER_USER_ROLE) ? true
													: dto.getOwnerUserId()
													.equalsIgnoreCase(
															user.getId()) ? true
																	: dto.getShareLevel() == null ? false
																			: dto.getShareLevel()
																			.equalsIgnoreCase(
																					ShareLevel.MODIFY_ID) ? true
																							: false;
			
			if (canDraft) {
				dtoList.add(dto);
			}
		}
		return dtoList;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.dao.clause.MeasureDAO#getMeasuresForDraft(mat.model.User, int,
	 * int)
	 */
	@Override
	public List<MeasureShareDTO> getMeasuresForDraft(User user, int startIndex, int pageSize) {
		List<MeasureShareDTO> dtoList = getMeasuresForDraft(user);
		if (pageSize < dtoList.size()) {
			return dtoList.subList(startIndex,
					Math.min(startIndex + pageSize, dtoList.size()));
		} else {
			return dtoList;
		}
	}
	
	/**
	 * Gets the measures for version.
	 *
	 * @param searchText the search text
	 * @param user the user
	 * @return the measures for version
	 */
	@Override
	public List<MeasureShareDTO> getMeasuresForVersion(String searchText,User user) {
		List<MeasureShareDTO> orderedDTOList = getMeasureShareInfoForUser(searchText,user,
				0, Integer.MAX_VALUE);
		List<MeasureShareDTO> dtoList = new ArrayList<MeasureShareDTO>();
		for (MeasureShareDTO dto : orderedDTOList) {
			boolean canVersion = !dto.isDraft() ? false
					: dto.isLocked() ? false
							: user.getSecurityRole()
							.getDescription()
							.equalsIgnoreCase(
									SecurityRole.SUPER_USER_ROLE) ? true
											: dto.getOwnerUserId().equalsIgnoreCase(
													user.getId()) ? true
															: dto.getShareLevel() == null ? false
																	: dto.getShareLevel()
																	.equalsIgnoreCase(
																			ShareLevel.MODIFY_ID) ? true
																					: false;
			if (canVersion) {
				dtoList.add(dto);
			}
		}
		
		return dtoList;
	}

	
	/*@Override
	public List<MeasureShareDTO> getMeasuresForVersion(String searchText,User user,
			int startIndex, int pageSize) {
		List<MeasureShareDTO> dtoList = getMeasuresForVersion(searchText, user);
		if (pageSize < dtoList.size()) {
			return dtoList.subList(startIndex,
					Math.min(startIndex + pageSize, dtoList.size()));
		} else {
			return dtoList;
		}
	}*/
	
	/* (non-Javadoc)
	 * @see mat.dao.clause.MeasureDAO#getMeasureShareForMeasure(java.lang.String)
	 */
	@Override
	public List<MeasureShare> getMeasureShareForMeasure(String measureId) {
		List<MeasureShare> measureShare = new ArrayList<MeasureShare>();
		if (measureId == null) {
			return null;
		}
		Criteria shareCriteria = getSessionFactory().getCurrentSession()
				.createCriteria(MeasureShare.class);
		shareCriteria.add(Restrictions.eq("measure.id", measureId));
		measureShare = shareCriteria.list();
		return measureShare;
	}
	
	/**
	 * This method returns a List of MeasureShareDTO objects which have
	 * userId,firstname,lastname and sharelevel for the given measureId.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @return the measure share info for measure
	 */
	@Override
	public List<MeasureShareDTO> getMeasureShareInfoForMeasure(
			String measureId, int startIndex, int pageSize) {
		Criteria userCriteria = getSessionFactory().getCurrentSession()
				.createCriteria(User.class);
		userCriteria.add(Restrictions.eq("securityRole.id", "3"));
		//Added restriction for Active user's for User story MAT:2900.
		userCriteria.add(Restrictions.eq("status.id", "1"));
		userCriteria.add(Restrictions.ne("id",
				LoggedInUserUtil.getLoggedInUser()));
		userCriteria.setFirstResult(startIndex);
		// userCriteria.setMaxResults(pageSize);
		userCriteria.addOrder(Order.asc("lastName"));
		
		List<User> userResults = userCriteria.list();
		HashMap<String, MeasureShareDTO> userIdDTOMap = new HashMap<String, MeasureShareDTO>();
		ArrayList<MeasureShareDTO> orderedDTOList = new ArrayList<MeasureShareDTO>();
		List<MeasureShareDTO> dtoList = new ArrayList<MeasureShareDTO>();
		for (User user : userResults) {
			MeasureShareDTO dto = new MeasureShareDTO();
			dtoList.add(dto);
			dto.setUserId(user.getId());
			dto.setFirstName(user.getFirstName());
			dto.setLastName(user.getLastName());
			dto.setOrganizationName(user.getOrganizationName());
			userIdDTOMap.put(user.getId(), dto);
			orderedDTOList.add(dto);
		}
		
		if (dtoList.size() > 0) {
			Criteria shareCriteria = getSessionFactory().getCurrentSession()
					.createCriteria(MeasureShare.class);
			shareCriteria.add(Restrictions.in("shareUser.id",
					userIdDTOMap.keySet()));
			shareCriteria.add(Restrictions.eq("measure.id", measureId));
			List<MeasureShare> shareList = shareCriteria.list();
			for (MeasureShare share : shareList) {
				User shareUser = share.getShareUser();
				MeasureShareDTO dto = userIdDTOMap.get(shareUser.getId());
				dto.setShareLevel(share.getShareLevel().getId());
			}
		}
		if (pageSize < orderedDTOList.size()) {
			return orderedDTOList.subList(0, pageSize);
		} else {
			return orderedDTOList;
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.clause.MeasureDAO#getMeasureShareInfoForMeasureAndUser(java.lang.String, java.lang.String)
	 */
	@Override
	public List<MeasureShareDTO> getMeasureShareInfoForMeasureAndUser(String user, String measureId) {
		Criteria shareCriteria = getSessionFactory().getCurrentSession()
				.createCriteria(MeasureShare.class);
		shareCriteria.add(Restrictions.eq("shareUser.id", user));
		shareCriteria.add(Restrictions.eq("measure.id", measureId));
		List<MeasureShare> shareList = shareCriteria.list();
		List<MeasureShareDTO> shareDTOList = new ArrayList<MeasureShareDTO>();
		for (MeasureShare share : shareList) {
			MeasureShareDTO dto = new MeasureShareDTO();
			dto.setShareLevel(share.getShareLevel().getId());
			dto.setMeasureId(measureId);
			dto.setOwnerUserId(user);
			shareDTOList.add(dto);
		}
		return shareDTOList;
	}
	/* (non-Javadoc)
	 * @see mat.dao.clause.MeasureDAO#getMeasureShareInfoForUser(java.lang.String, mat.model.User, int, int)
	 */
	@Override
	public List<MeasureShareDTO> getMeasureShareInfoForUser(String searchText,
			User user, int startIndex, int pageSize) {
		
		String searchTextLC = searchText.toLowerCase().trim();
		
		Criteria mCriteria = buildMeasureShareForUserCriteria(user);
		mCriteria.addOrder(Order.desc("measureSet.id"))
		.addOrder(Order.desc("draft")).addOrder(Order.desc("version"));
		// mCriteria.add(Restrictions.isNull("deleted"));
		mCriteria.setFirstResult(startIndex);
		
		Map<String, MeasureShareDTO> measureIdDTOMap = new HashMap<String, MeasureShareDTO>();
		ArrayList<MeasureShareDTO> orderedDTOList = new ArrayList<MeasureShareDTO>();
		List<Measure> measureResultList = mCriteria.list();
		
		if (!user.getSecurityRole().getId().equals("2")) {
			measureResultList = getAllMeasuresInSet(measureResultList);
		}
		measureResultList = sortMeasureList(measureResultList);
		
		StringUtility su = new StringUtility();
		for (Measure measure : measureResultList) {
			
			boolean matchesSearch = searchResultsForMeasure(searchTextLC, su,
					measure);
			
			// measure steward (only check if necessary)
			/*
			 * if(!matchesSearch && !su.isEmptyOrNull(searchTextLC)){
			 * List<Metadata> mdList =
			 * metadataDAO.getMeasureDetails(measure.getId(), "MeasureSteward");
			 * for(Metadata md : mdList)
			 * if(md.getName().equalsIgnoreCase("MeasureSteward") &&
			 * md.getValue().toLowerCase().contains(searchTextLC)){
			 * matchesSearch = true; break; } }
			 */
			
			if (matchesSearch) {
				MeasureShareDTO dto = extractDTOFromMeasure(measure);
				measureIdDTOMap.put(measure.getId(), dto);
				orderedDTOList.add(dto);
			}
		}
		Criteria shareCriteria = getSessionFactory().getCurrentSession()
				.createCriteria(MeasureShare.class);
		shareCriteria.add(Restrictions.eq("shareUser.id", user.getId()));
		/*shareCriteria.add(Restrictions.in("measure.id",
				measureIdDTOMap.keySet()));*/
		List<MeasureShare> shareList = shareCriteria.list();
		
		if (orderedDTOList.size() > 0) {
			/*Criteria shareCriteria = getSessionFactory().getCurrentSession()
					.createCriteria(MeasureShare.class);
			shareCriteria.add(Restrictions.eq("shareUser.id", user.getId()));
			shareCriteria.add(Restrictions.in("measure.id",
					measureIdDTOMap.keySet()));
			List<MeasureShare> shareList = shareCriteria.list();*/
			// get share level for each measure set and set it on each dto
			HashMap<String, String> measureSetIdToShareLevel = new HashMap<String, String>();
			for (MeasureShare share : shareList) {
				String msid = share.getMeasure().getMeasureSet().getId();
				String shareLevel = share.getShareLevel().getId();
				
				String existingShareLevel = measureSetIdToShareLevel.get(msid);
				if ((existingShareLevel == null)
						|| ShareLevel.VIEW_ONLY_ID.equals(existingShareLevel)) {
					measureSetIdToShareLevel.put(msid, shareLevel);
				}
			}
			for (MeasureShareDTO dto : orderedDTOList) {
				String msid = dto.getMeasureSetId();
				String shareLevel = measureSetIdToShareLevel.get(msid);
				if (shareLevel != null) {
					dto.setShareLevel(shareLevel);
				}
			}
		}
		if (pageSize < orderedDTOList.size()) {
			return orderedDTOList.subList(0, pageSize);
		} else {
			return orderedDTOList;
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.clause.MeasureDAO#getMeasureShareInfoForUser(mat.model.User, int, int)
	 */
	@Override
	public List<MeasureShareDTO> getMeasureShareInfoForUser(User user,
			int startIndex, int pageSize) {
		return getMeasureShareInfoForUser("", user, startIndex, pageSize);
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.clause.MeasureDAO#getMeasureShareInfoForUserWithFilter(java.lang.String, int, int, int)
	 */
	@Override
	public List<MeasureShareDTO> getMeasureShareInfoForUserWithFilter(
			String searchText, int startIndex, int pageSize, int filter) {
		String searchTextLC = searchText.toLowerCase().trim();
		Criteria mCriteria = getSessionFactory().getCurrentSession()
				.createCriteria(Measure.class);
		mCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		mCriteria.addOrder(Order.desc("measureSet.id"))
		.addOrder(Order.desc("draft")).addOrder(Order.desc("version"));
		mCriteria.setFirstResult(startIndex);
		
		ArrayList<MeasureShareDTO> orderedDTOList = new ArrayList<MeasureShareDTO>();
		List<Measure> measureResultList = mCriteria.list();
		
		measureResultList = getAllMeasuresInSet(measureResultList);
		StringUtility su = new StringUtility();
		
		for (Measure measure : measureResultList) {
			
			if (searchResultsForMeasure(searchTextLC, su,
					measure)) {
				MeasureShareDTO dto = extractDTOFromMeasure(measure);
				orderedDTOList.add(dto);
			}
		}
		return filterMeasureListForAdmin(orderedDTOList);
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.clause.MeasureDAO#getMeasureShareInfoForUserWithFilter(java.lang.String, mat.model.User, int, int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<MeasureShareDTO> getMeasureShareInfoForUserWithFilter(
			String searchText, User user, int startIndex, int pageSize,
			int filter) {
		
		String searchTextLC = searchText.toLowerCase().trim();
		
		Criteria mCriteria = buildMeasureShareForUserCriteriaWithFilter(user,
				filter);
		// mCriteria.add(Restrictions.isNull("deleted"));//("deleted",
		// "softDeleted"));
		mCriteria.addOrder(Order.desc("measureSet.id"))
		.addOrder(Order.desc("draft")).addOrder(Order.desc("version"));
		mCriteria.setFirstResult(startIndex);
		
		Map<String, MeasureShareDTO> measureIdDTOMap = new HashMap<String, MeasureShareDTO>();
		Map<String, MeasureShareDTO> measureSetIdDraftableMap = new HashMap<String, MeasureShareDTO>();
		ArrayList<MeasureShareDTO> orderedDTOList = new ArrayList<MeasureShareDTO>();
		List<Measure> measureResultList = mCriteria.list();
		boolean isNormalUserAndAllMeasures = user.getSecurityRole().getId()
				.equals("3")
				&& (filter == MeasureSearchFilterPanel.ALL_MEASURES);
		
		if (!user.getSecurityRole().getId().equals("2")) {
			measureResultList = getAllMeasuresInSet(measureResultList);
		}
		measureResultList = sortMeasureList(measureResultList);
		
		StringUtility su = new StringUtility();
		for (Measure measure : measureResultList) {
			
			if (searchResultsForMeasure(searchTextLC, su,
					measure)) {
				MeasureShareDTO dto = extractDTOFromMeasure(measure);
				boolean isDraft = dto.isDraft();
				if(isDraft){
					measureSetIdDraftableMap.put(dto.getMeasureSetId(), dto);
				}
				measureIdDTOMap.put(measure.getId(), dto);
				orderedDTOList.add(dto);
			}
		}
		Criteria shareCriteria = getSessionFactory().getCurrentSession()
				.createCriteria(MeasureShare.class);
		shareCriteria.add(Restrictions.eq("shareUser.id", user.getId()));
		/*shareCriteria.add(Restrictions.in("measure.id",
				measureIdDTOMap.keySet()));*/
		List<MeasureShare> shareList = shareCriteria.list();
		if (orderedDTOList.size() > 0) {
			// get share level for each measure set and set it on each dto
			HashMap<String, String> measureSetIdToShareLevel = new HashMap<String, String>();
			for (MeasureShare share : shareList) {
				String msid = share.getMeasure().getMeasureSet().getId();
				String shareLevel = share.getShareLevel().getId();
				
				String existingShareLevel = measureSetIdToShareLevel.get(msid);
				if ((existingShareLevel == null)
						|| ShareLevel.VIEW_ONLY_ID.equals(existingShareLevel)) {
					measureSetIdToShareLevel.put(msid, shareLevel);
				}
			}
			
			for (Iterator<MeasureShareDTO> iterator = orderedDTOList.iterator(); iterator
					.hasNext();) {
				MeasureShareDTO dto = iterator.next();
				String msid = dto.getMeasureSetId();
				
				String shareLevel = measureSetIdToShareLevel.get(msid);
				if (isNormalUserAndAllMeasures
						&& dto.isPrivateMeasure()
						&& !dto.getOwnerUserId().equals(user.getId())
						&& ((shareLevel == null) || !shareLevel
								.equals(ShareLevel.MODIFY_ID))) {
					iterator.remove();
					continue;
				}
				boolean hasDraft = measureSetIdDraftableMap.containsKey(msid);
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
						MeasureShareDTO draftLibrary = measureSetIdDraftableMap.get(msid);
						if(dto.getMeasureId().equals(draftLibrary.getMeasureId())){
							dto.setVersionable(true);
							dto.setDraftable(false);
						} else if (dto.getMeasureSetId().equals(draftLibrary.getMeasureSetId())){
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
		if (pageSize < orderedDTOList.size()) {
			return orderedDTOList.subList(0, pageSize);
		} else {
			return orderedDTOList;
		}
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
	
	/* (non-Javadoc)
	 * @see mat.dao.clause.MeasureDAO#isMeasureLocked(java.lang.String)
	 */
	@Override
	public boolean isMeasureLocked(String measureId) {
		Session session = getSessionFactory().getCurrentSession();
		//String sql = "select lockedOutDate from mat.model.clause.Measure m  where id = '"
		//		+ measureId + "'";
		String sql = "select lockedOutDate from mat.model.clause.Measure m  where id = :measureId";
		
		Query query = session.createQuery(sql);
		query.setString("measureId", measureId);
		List<Timestamp> result = query.list();
		Timestamp lockedOutDate = null;
		if (!result.isEmpty()) {
			lockedOutDate = result.get(0);
		}
		
		boolean locked = isLocked(lockedOutDate);
		return locked;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.dao.clause.MeasureDAO#resetLockDate(mat.model.clause.Measure)
	 * This method opens a new Session and new transaction to update only the
	 * lockedDate column in the database. After updating the table,transaction
	 * has been committed and session has been closed.
	 */
	// TODO:- We need to follow the same logic/concept while
	// settingtheLockedDate.
	@Override
	public void resetLockDate(Measure m) {
		Session session = getSessionFactory().openSession();
		org.hibernate.Transaction tx = session.beginTransaction();
		try {
			String sql = "update mat.model.clause.Measure m set lockedOutDate  = :lockDate, lockedUser = :lockedUserId  where id = :measureId";
			Query query = session.createQuery(sql);
			query.setString("lockDate", null);
			query.setString("lockedUserId", null);
			query.setString("measureId", m.getId());
			int rowCount = query.executeUpdate();
			tx.commit();
		} finally {
			rollbackUncommitted(tx);
			closeSession(session);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.clause.MeasureDAO#saveandReturnMaxEMeasureId(mat.model.clause.Measure)
	 */
	@Override
	public int saveandReturnMaxEMeasureId(Measure measure) {
		int eMeasureId = getMaxEMeasureId() + 1;
		MeasureSet ms = measure.getMeasureSet();
		Session session = getSessionFactory().getCurrentSession();
		//		SQLQuery query = session
		//				.createSQLQuery("update MEASURE m set m.EMEASURE_ID = "
		//						+ eMeasureId + " where m.MEASURE_SET_ID = '"
		//						+ ms.getId() + "';");
		String sql = "update MEASURE m set m.EMEASURE_ID = :eMeasureId where m.MEASURE_SET_ID = :MEASURE_SET_ID";
		SQLQuery query = session.createSQLQuery(sql);
		query.setInteger("eMeasureId", eMeasureId);
		query.setString("MEASURE_SET_ID", ms.getId());
		query.executeUpdate();
		return eMeasureId;
		
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.clause.MeasureDAO#saveMeasure(mat.model.clause.Measure)
	 */
	@Override
	public void saveMeasure(Measure measure) {
		if (dAOService != null) {
			// allow to test using DAOService
			dAOService.getMeasureDAO().save(measure);
			
		} else {
			super.save(measure);
		}
	}
	
	/**
	 * Search results for measure.
	 * 
	 * @param searchTextLC
	 *            the search text lc
	 * @param stringUtility
	 *            the string utility
	 * @param measure
	 *            the measure
	 * @return true, if successful
	 */
	private boolean searchResultsForMeasure(String searchTextLC,
			StringUtility stringUtility, Measure measure) {
		boolean matchesSearch = stringUtility.isEmptyOrNull(searchTextLC) ? true
				:
					// measure name
					measure.getDescription().toLowerCase()
					.contains(searchTextLC) ? true
							:
								// abbreviated measure name
								measure.getaBBRName().toLowerCase()
								.contains(searchTextLC) ? true
										:
											(new Integer(measure.geteMeasureId())).toString().contains(searchTextLC)
											? true
													:
														// measure owner first name
														measure.getOwner().getFirstName()
														.toLowerCase()
														.contains(searchTextLC) ? true
																:
																	// measure owner last name
																	measure.getOwner().getLastName()
																	.toLowerCase()
																	.contains(searchTextLC) ? true
																			: false;
		return matchesSearch;
	}
	
	/**
	 * Sort measure list.
	 * 
	 * @param measureResultList
	 *            the measure result list
	 * @return the list
	 */
	private List<Measure> sortMeasureList(List<Measure> measureResultList) {
		// generate sortable lists
		List<List<Measure>> measureLists = new ArrayList<List<Measure>>();
		for (Measure m : measureResultList) {
			boolean hasList = false;
			/* if(m.getDeleted()==null){ */
			for (List<Measure> mlist : measureLists) {
				String msetId = mlist.get(0).getMeasureSet().getId();
				if (m.getMeasureSet().getId().equalsIgnoreCase(msetId)) {
					mlist.add(m);
					hasList = true;
					break;
				}
			}
			// }
			if (!hasList) {
				List<Measure> mlist = new ArrayList<Measure>();
				// Check if Measure is softDeleted then dont include that into
				// list.
				// if(m.getDeleted()==null){
				mlist.add(m);
				measureLists.add(mlist);
				// }
			}
		}
		// sort
		for (List<Measure> mlist : measureLists) {
			Collections.sort(mlist, new MeasureComparator());
		}
		Collections.sort(measureLists, new MeasureListComparator());
		// compile list
		List<Measure> retList = new ArrayList<Measure>();
		for (List<Measure> mlist : measureLists) {
			for (Measure m : mlist) {
				retList.add(m);
			}
		}
		return retList;
	}
	
	
	/**
	 * Group the measures with measure set id and find draft or highest version number which ever is available.
	 * @param measureResultList
	 * @return
	 */
	public List<Measure> sortMeasureListForMeasureOwner(List<Measure> measureResultList) {
		// generate sortable lists
		List<List<Measure>> measureLists = new ArrayList<List<Measure>>();
		for (Measure m : measureResultList) {
			boolean hasList = false;
			/* if(m.getDeleted()==null){ */
			for (List<Measure> mlist : measureLists) {
				String msetId = mlist.get(0).getMeasureSet().getId();
				if (m.getMeasureSet().getId().equalsIgnoreCase(msetId)) {
					mlist.add(m);
					hasList = true;
					break;
				}
			}
			// }
			if (!hasList) {
				List<Measure> mlist = new ArrayList<Measure>();
				// Check if Measure is softDeleted then dont include that into
				// list.
				// if(m.getDeleted()==null){
				mlist.add(m);
				measureLists.add(mlist);
				// }
			}
		}
		Collections.sort(measureLists, new MeasureListComparator());
		// compile list
		List<Measure> retList = new ArrayList<Measure>();
		for (List<Measure> mlist : measureLists) {
			boolean isDraftAvailable = false;
			Measure measure = null;
			for (Measure m : mlist) {
				measure = m;
				if (m.isDraft()) {
					isDraftAvailable = true;
					//System.out.println("Draft found for measure id :: "+ m.getId());
					retList.add(m);
					break;
				}
			}
			if (!isDraftAvailable && (measure != null)) {
				Criteria mCriteria = getSessionFactory().getCurrentSession()
						.createCriteria(Measure.class);
				mCriteria.add(Restrictions.eq("measureSet.id", measure.getMeasureSet().getId()));
				mCriteria.add(Restrictions.eq("owner.id", measure.getOwner().getId()));
				// add check to filter Draft's version number when finding max version
				// number.
				mCriteria.add(Restrictions.ne("draft", true));
				mCriteria.setProjection(Projections.max("version"));
				String maxVersion = (String) mCriteria.list().get(0);
				for (Measure m : mlist) {
					measure = m;
					if (!m.isDraft()
							&& m.getVersion().equalsIgnoreCase(maxVersion)) {
						//System.out.println("Max Version found for measure id :: "+ m.getId());
						retList.add(m);
						break;
					}
				}
			}
		}
		return retList;
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.clause.MeasureDAO#updatePrivateColumnInMeasure(java.lang.String, boolean)
	 */
	@Override
	public void updatePrivateColumnInMeasure(String measureId, boolean isPrivate) {
		Session session = getSessionFactory().openSession();
		org.hibernate.Transaction tx = session.beginTransaction();
		try {
			String sql = "update mat.model.clause.Measure m set isPrivate  = :isPrivate where id = :measureId";
			Query query = session.createQuery(sql);
			query.setBoolean("isPrivate", isPrivate);
			query.setString("measureId", measureId);
			int rowCount = query.executeUpdate();
			tx.commit();
			logger.info("Updated Private column" + rowCount);
		} finally {
			rollbackUncommitted(tx);
			closeSession(session);
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.clause.MeasureDAO#getMeasure(java.lang.String)
	 */
	@Override
	public boolean getMeasure(String measureId) {
		Criteria mCriteria = getSessionFactory().getCurrentSession()
				.createCriteria(Measure.class);
		mCriteria.add(Restrictions.eq("id", measureId));
		mCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Measure> measure = mCriteria.list();
		boolean isMeasureDeleted = false;
		if(measure.size()>0){
			isMeasureDeleted = true;
		}
		return isMeasureDeleted;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.dao.clause.MeasureDAO#findShareLevelForUser(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	 public ShareLevel findShareLevelForUser(String measureId, String userID, 
			 String measureSetId){
	  
	  ShareLevel shareLevel = null;
	  List<String> measureIds = getMeasureSetForSharedMeasure(measureId, measureSetId);
	  Criteria shareCriteria = getSessionFactory().getCurrentSession()
	    .createCriteria(MeasureShare.class);
	  shareCriteria.add(Restrictions.eq("shareUser.id",userID));
	  shareCriteria.add(Restrictions.in("measure.id", measureIds));
	  List<MeasureShare> shareList = shareCriteria.list();
	  if(!shareList.isEmpty()){
	   shareLevel = shareList.get(0).getShareLevel();
	  }
	  
	  return shareLevel;
	 }
	
	@SuppressWarnings("unchecked")
	private List<String> getMeasureSetForSharedMeasure(String measureId, 
			String measureSetId){
		
		Criteria mCriteria = getSessionFactory().getCurrentSession()
				.createCriteria(Measure.class);
		mCriteria.add(Restrictions.eq("measureSet.id", measureSetId));
		List<String> measureIds = new ArrayList<String>();
		List<Measure> measureList = mCriteria.list();
 		for(Measure measure : measureList){
 			measureIds.add(measure.getId());
		}
		
		return measureIds;
	}
	
}
