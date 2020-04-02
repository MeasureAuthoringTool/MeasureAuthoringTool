package mat.dao.clause.impl;

import java.sql.Timestamp;
import java.time.LocalDate;
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
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.client.measure.MeasureSearchFilterPanel;
import mat.client.shared.SearchWidgetWithFilter;
import mat.dao.UserDAO;
import mat.dao.clause.MeasureDAO;
import mat.dao.search.GenericDAO;
import mat.model.LockedUserInfo;
import mat.model.SecurityRole;
import mat.model.User;
import mat.model.clause.Measure;
import mat.model.clause.MeasureShare;
import mat.model.clause.MeasureShareDTO;
import mat.model.clause.ModelTypeHelper;
import mat.model.clause.ShareLevel;
import mat.server.LoggedInUserUtil;
import mat.shared.MeasureSearchModel;
import mat.shared.MeasureSearchModel.PatientBasedType;
import mat.shared.SearchModel;
import mat.shared.SearchModel.VersionType;

@Repository("measureDAO")
public class MeasureDAOImpl extends GenericDAO<Measure, String> implements MeasureDAO {

    private static final Log logger = LogFactory.getLog(MeasureDAOImpl.class);
    private static final long LOCK_THRESHOLD = TimeUnit.MINUTES.toMillis(3); // 3 minutes
    private static final int MAX_PAGE_SIZE = Integer.MAX_VALUE;
    private static final String OWNER = "owner";
    private static final String DRAFT = "draft";
    private static final String VERSION = "version";
    private static final String MEASURE = "measure";
    private static final String LAST_NAME = "lastName";
    private static final String FIRST_NAME = "firstName";
    private static final String SHARE_USER = "shareUser";
    private static final String MEASURE_SET = "measureSet";
    private static final String PATIENT_BASED = "patientBased";
    private static final String MEASURE_SCORING_TYPE = "measureScoring";
    private static final String SECURITY_ROLE_USER = "3";
    private static final String MEASURE_MODEL = "measureModel";
    private static final String ID = "id";

    @Autowired
    private UserDAO userDAO;

    public MeasureDAOImpl(@Autowired SessionFactory sessionFactory) {
        setSessionFactory(sessionFactory);
    }

    public MeasureDAOImpl() {
    }

    @Override
    public List<Measure> getMeasureListForMeasureOwner(User user) {
        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<Measure> query = cb.createQuery(Measure.class);
        final Root<Measure> root = query.from(Measure.class);

        query.select(root).where(cb.equal(root.get(OWNER).get(ID), user.getId()));

        final List<Measure> measureList = session.createQuery(query).getResultList();

        return sortMeasureListForMeasureOwner(measureList);
    }

    @Override
    public int countUsersForMeasureShare() {
        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        final Root<User> root = countQuery.from(User.class);

        countQuery = countQuery.select(cb.count(root)).where(cb.and(cb.equal(root.get("securityRole").get(ID), SECURITY_ROLE_USER),
                cb.notEqual(root.get(ID), LoggedInUserUtil.getLoggedInUser())));

        final Long count = session.createQuery(countQuery).getSingleResult();

        return (count == null) ? 0 : Math.toIntExact(count);
    }

    @Override
    public MeasureShareDTO extractDTOFromMeasure(Measure measure) {
        final MeasureShareDTO dto = new MeasureShareDTO();

        dto.setMeasureId(measure.getId());
        dto.setMeasureName(measure.getDescription());
        dto.setScoringType(measure.getMeasureScoring());
        dto.setShortName(measure.getaBBRName());
        dto.setPackaged(measure.getExportedDate() != null);
        dto.setOwnerUserId(measure.getOwner().getId());
        dto.setMeasureModel(measure.getMeasureModel());
        dto.setDraft(measure.isDraft());
        dto.setVersion(measure.getVersion());
        dto.setReleaseVersion(measure.getReleaseVersion());
        dto.setQdmVersion(measure.getQdmVersion());
        dto.setFinalizedDate(measure.getFinalizedDate());
        dto.setMeasureSetId(measure.getMeasureSet().getId());
        dto.seteMeasureId(measure.geteMeasureId());
        dto.setPrivateMeasure(measure.getIsPrivate());
        dto.setRevisionNumber(measure.getRevisionNumber());
        final boolean isLocked = isLocked(measure.getLockedOutDate());
        dto.setLocked(isLocked);
        if (measure.getPatientBased() != null) {
            dto.setPatientBased(measure.getPatientBased());
        }

        if (isLocked && (measure.getLockedUser() != null)) {
            final LockedUserInfo lockedUserInfo = new LockedUserInfo();
            lockedUserInfo.setUserId(measure.getLockedUser().getId());
            lockedUserInfo.setEmailAddress(measure.getLockedUser().getEmailAddress());
            lockedUserInfo.setFirstName(measure.getLockedUser().getFirstName());
            lockedUserInfo.setLastName(measure.getLockedUser().getLastName());
            dto.setLockedUserInfo(lockedUserInfo);
        }
        return dto;
    }

    /**
     * measureList is filtered with latest draft or version. In a measure set, first
     * we look for a draft version. If there is a draft version then that measure is
     * added in the measureList. Otherwise, we look for the latest version and add
     * in the measureList. Latest version measure is the measure with the latest
     * Finalized Date.
     *
     * @param measureList - {@link List} of {@link MeasureShareDTO}.
     * @return {@link List} of {@link MeasureShareDTO}.
     */
    private List<MeasureShareDTO> filterMeasureListForAdmin(final List<MeasureShareDTO> measureList) {
        final List<MeasureShareDTO> updatedMeasureList = new ArrayList<>();
        for (final MeasureShareDTO measureShareDTO : measureList) {
            if (updatedMeasureList.isEmpty()) {
                updatedMeasureList.add(measureShareDTO);
            } else {
                boolean found = false;
                final ListIterator<MeasureShareDTO> itr = updatedMeasureList.listIterator();
                while (itr.hasNext()) {
                    final MeasureShareDTO shareDTO = itr.next();
                    if (measureShareDTO.getMeasureSetId().equals(shareDTO.getMeasureSetId())) {
                        found = true;
                        if (measureShareDTO.isDraft() ||
                                (!shareDTO.isDraft() && measureShareDTO.getFinalizedDate().compareTo(shareDTO.getFinalizedDate()) > 0)) {
                            itr.remove();
                            itr.add(measureShareDTO);
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

    @Override
    public List<Measure> findByOwnerId(String measureOwnerId) {
        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<Measure> query = cb.createQuery(Measure.class);
        final Root<Measure> root = query.from(Measure.class);

        query.select(root).where(cb.equal(root.get(OWNER).get(ID), measureOwnerId));

        return session.createQuery(query).getResultList();
    }

    @Override
    public Measure getMeasureByMeasureId(String measureId) {
        if (isEmpty(measureId)) {
            return null;
        }
        return getSessionFactory().getCurrentSession().get(clazz, measureId);
    }

    @Override
    public String findMaxOfMinVersion(String measureSetId, String version) {
        logger.info("In MeasureDao.findMaxOfMinVersion()");
        String maxOfMinVersion = version;
        double minVal = 0;
        double maxVal = 0;
        if (StringUtils.isNotBlank(version)) {
            final int decimalIndex = version.indexOf('.');
            minVal = Integer.parseInt(version.substring(0, decimalIndex));
            logger.info("Min value: " + minVal);
            maxVal = minVal + 1;
            logger.info("Max value: " + maxVal);
        }

        final List<Measure> measureList = getListOfVersionsForAMeasure(measureSetId);

        double tempVersion = 0;

        if (CollectionUtils.isNotEmpty(measureList)) {
            logger.info("Finding max of min version from the Measure List. Size:" + measureList.size());
            for (final Measure measure : measureList) {
                logger.info("Looping through Measures Id: " + measure.getId() + " Version: " + measure.getVersion());
                if ((measure.getVersionNumber() > minVal) && (measure.getVersionNumber() < maxVal)) {
                    if (tempVersion < measure.getVersionNumber()) {
                        logger.info(tempVersion + "<" + measure.getVersionNumber() + "="
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

    private List<Measure> getListOfVersionsForAMeasure(String measureSetId) {
        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<Measure> query = cb.createQuery(Measure.class);
        final Root<Measure> root = query.from(Measure.class);
        logger.info("Query Using Measure Set Id:" + measureSetId);
        query.select(root).where(cb.and(cb.equal(root.get(MEASURE_SET).get(ID), measureSetId), cb.not(root.get(DRAFT))));
        query.orderBy(cb.asc(root.get(VERSION)));

        return session.createQuery(query).getResultList();
    }

    @Override
    public String findMaxVersion(String measureSetId) {
        return getMaxVersion(measureSetId, null);
    }

    private String getMaxVersion(String measureSetId, String ownerId) {
        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<String> query = cb.createQuery(String.class);
        final Root<Measure> root = query.from(Measure.class);

        final Predicate predicate = buildPredicateForMaxVersion(measureSetId, ownerId, cb, root);

        query.select(cb.max(root.get(VERSION)).as(String.class)).where(predicate);

        return session.createQuery(query).getSingleResult();
    }

    private Predicate buildPredicateForMaxVersion(String measureSetId, String ownerId, CriteriaBuilder cb, Root<Measure> root) {
        // add check to filter Draft's version number when finding max version number.
        final Predicate p1 = cb.and(cb.equal(root.get(MEASURE_SET).get(ID), measureSetId), cb.not(root.get(DRAFT)));
        Predicate p2 = null;
        if (StringUtils.isNotBlank(ownerId)) {
            p2 = cb.equal(root.get(OWNER).get(ID), ownerId);
        }

        return (p2 != null) ? cb.and(p1, p2) : p1;
    }

    /**
     * for all measure sets referenced in measures in ms, return all measures that
     * are members of the set.
     *
     * @param ms the ms
     * @return the all measures in set
     */
    @Override
    public List<Measure> getAllMeasuresInSet(List<Measure> ms) {
        if (CollectionUtils.isNotEmpty(ms)) {
            final Set<String> measureSetIds = new HashSet<>();
            for (final Measure m : ms) {
                measureSetIds.add(m.getMeasureSet().getId());
            }

            final Session session = getSessionFactory().getCurrentSession();
            final CriteriaBuilder cb = session.getCriteriaBuilder();
            final CriteriaQuery<Measure> query = cb.createQuery(Measure.class);
            final Root<Measure> root = query.from(Measure.class);

            query.select(root).where(root.get(MEASURE_SET).get(ID).in(measureSetIds));

            ms = session.createQuery(query).getResultList();
        }
        return sortMeasureList(ms);
    }

    @Override
    public int getMaxEMeasureId() {
        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<Integer> query = cb.createQuery(Integer.class);
        final Root<Measure> root = query.from(Measure.class);

        query.select(cb.max(root.get("eMeasureId")));

        return session.createQuery(query).getSingleResult();
    }

    @Override
    public List<MeasureShare> getMeasureShareForMeasure(String measureId) {
        if (measureId == null) {
            return Collections.emptyList();
        }

        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<MeasureShare> query = cb.createQuery(MeasureShare.class);
        final Root<MeasureShare> root = query.from(MeasureShare.class);

        query.select(root).where(cb.equal(root.get(MEASURE).get(ID), measureId));

        return session.createQuery(query).getResultList();
    }

    /**
     * This method returns a List of MeasureShareDTO objects which have
     * userId,firstname,lastname and sharelevel for the given measureId.
     *
     * @param userName   the user name entered for search
     * @param measureId  the measure id
     * @param startIndex the start index
     * @param pageSize   the page size
     * @return the measure share info for measure
     */
    @Override
    public List<MeasureShareDTO> getMeasureShareInfoForMeasure(String userName, String measureId, int startIndex, int pageSize) {

        final List<User> userResults = userDAO.getUsersListForSharingMeasureOrLibrary(userName);

        final HashMap<String, MeasureShareDTO> userIdDTOMap = new HashMap<>();

        final ArrayList<MeasureShareDTO> orderedDTOList = new ArrayList<>();

        for (final User user : userResults) {
            final MeasureShareDTO dto = new MeasureShareDTO();
            dto.setUserId(user.getId());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setOrganizationName(user.getOrganizationName());
            userIdDTOMap.put(user.getId(), dto);
            orderedDTOList.add(dto);
        }

        if (CollectionUtils.isNotEmpty(orderedDTOList)) {
            final List<MeasureShare> shareList = getShareList(null, measureId, userIdDTOMap);
            for (final MeasureShare share : shareList) {
                final User shareUser = share.getShareUser();
                final MeasureShareDTO dto = userIdDTOMap.get(shareUser.getId());
                dto.setShareLevel(share.getShareLevel().getId());
            }
        }
        if (pageSize < orderedDTOList.size()) {
            return orderedDTOList.subList(0, pageSize);
        } else {
            return orderedDTOList;
        }
    }

    private Predicate getPredicateForShareList(String userId, String measureId, HashMap<String, MeasureShareDTO> userIdDTOMap,
                                               CriteriaBuilder cb, Root<MeasureShare> root) {
        if (StringUtils.isNotBlank(userId)) {
            return cb.equal(root.get(SHARE_USER).get(ID), userId);
        }
        return cb.and(root.get(SHARE_USER).get(ID).in(userIdDTOMap.keySet()), cb.equal(root.get(MEASURE).get(ID), measureId));
    }

    private List<MeasureShare> getShareList(String userId, String measureId, HashMap<String, MeasureShareDTO> userIdDTOMap) {
        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<MeasureShare> query = cb.createQuery(MeasureShare.class);
        final Root<MeasureShare> root = query.from(MeasureShare.class);

        final Predicate predicate = getPredicateForShareList(userId, measureId, userIdDTOMap, cb, root);

        query.select(root).where(predicate);

        return session.createQuery(query).getResultList();
    }

    @Override
    public List<MeasureShareDTO> getMeasureShareInfoForUserWithFilter(String searchText, int startIndex, int pageSize, int filter) {
        final String searchTextLC = searchText.toLowerCase().trim();

        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<Measure> query = cb.createQuery(Measure.class);
        final Root<Measure> root = query.from(Measure.class);

        query.select(root).distinct(true);

        if (StringUtils.isNotBlank(searchText)) {
            query.where(getSearchByMeasureOrOwnerNamePredicate(searchTextLC, cb, root));
        }

        query.orderBy(cb.desc(root.get(MEASURE_SET).get(ID)), cb.desc(root.get(DRAFT)), cb.desc(root.get(VERSION)));

        final ArrayList<MeasureShareDTO> orderedDTOList = new ArrayList<>();
        final List<Measure> measureResultList = session.createQuery(query).getResultList();

        for (final Measure measure : measureResultList) {
            final MeasureShareDTO dto = extractDTOFromMeasure(measure);
            orderedDTOList.add(dto);
        }
        return filterMeasureListForAdmin(orderedDTOList);
    }

    @Override
    public List<MeasureShareDTO> getMeasureShareInfoForUserWithFilter(MeasureSearchModel measureSearchModel, User user) {
        final List<Measure> measureResultList = fetchMeasureResultListForCritera(user, measureSearchModel);
        return getOrderedDTOListFromMeasureResults(measureSearchModel, user, measureResultList);
    }

    private List<Measure> fetchMeasureResultListForCritera(User user, MeasureSearchModel measureSearchModel) {
        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<Measure> criteriaQuery = cb.createQuery(Measure.class);
        final Root<Measure> root = criteriaQuery.from(Measure.class);
        root.fetch("measureDetails", JoinType.LEFT);

        final Predicate predicate = buildPredicateForMeasureSearch(user.getId(), cb, root, criteriaQuery, measureSearchModel);

        criteriaQuery.select(root).where(predicate).distinct(true);

        List<Measure> measureResultList = session.createQuery(criteriaQuery).getResultList();

        if (isMeasureSetSearch(user, measureSearchModel)) {
            measureResultList = getAllMeasuresInSet(measureResultList);
        }

        measureResultList = sortMeasureList(measureResultList);
        return measureResultList;
    }

    private boolean isMeasureSetSearch(User u, MeasureSearchModel m) {
        boolean isAdvancedSearch = checkIfAdvancedSearchWasUsed(m);
        return m.getModelType() == SearchModel.ModelType.ALL &&
                !StringUtils.equals(u.getSecurityRole().getId(), "2") &&
                !isAdvancedSearch;
    }

    private boolean checkIfAdvancedSearchWasUsed(MeasureSearchModel measureSearchModel) {
        return StringUtils.isNotBlank(measureSearchModel.getCqlLibraryName()) ||
                !measureSearchModel.isDraft().equals(SearchModel.VersionType.ALL) ||
                !measureSearchModel.isPatientBased().equals(MeasureSearchModel.PatientBasedType.ALL) ||
                (CollectionUtils.isNotEmpty(measureSearchModel.getScoringTypes()) && measureSearchModel.getScoringTypes().size() < 4) ||
                measureSearchModel.getModifiedDate() > 0 ||
                StringUtils.isNotBlank(measureSearchModel.getModifiedOwner()) ||
                StringUtils.isNotBlank(measureSearchModel.getOwner());
    }

    private Predicate buildPredicateForMeasureSearch(String userId, CriteriaBuilder cb, Root<Measure> root, CriteriaQuery<Measure> query,
                                                     MeasureSearchModel measureSearchModel) {
        final List<Predicate> predicatesList = new ArrayList<>();

        if (measureSearchModel.getIsMyMeasureSearch() == MeasureSearchFilterPanel.MY_MEASURES) {
            final Join<Measure, MeasureShare> childJoin = root.join("shares", JoinType.LEFT);
            predicatesList.add(cb.or(cb.equal(root.get(OWNER).get("id"), userId), cb.equal(childJoin.get(SHARE_USER).get("id"), userId)));
        }

        if (StringUtils.isNotBlank(measureSearchModel.getSearchTerm())) {
            predicatesList.add(getSearchByMeasureOrOwnerNamePredicate(measureSearchModel.getSearchTerm(), cb, root));
        }

        if (StringUtils.isNotBlank(measureSearchModel.getCqlLibraryName())) {
            predicatesList.add(cb.like(root.get("cqlLibraryName"), "%" + measureSearchModel.getCqlLibraryName() + "%"));
        }

        if (measureSearchModel.getModelType() != SearchModel.ModelType.ALL) {
            switch (measureSearchModel.getModelType()) {
                case FHIR:
                    predicatesList.add(cb.equal(root.get(MEASURE_MODEL), measureSearchModel.getModelType().name()));
                    break;
                case QDM_CQL:
                    predicatesList.add(cb.equal(root.get(MEASURE_MODEL), "QDM"));
                    break;
                case QDM_QDM:
                    predicatesList.add(cb.isNull(root.get(MEASURE_MODEL)));
                    break;
                default:
                    throw new IllegalStateException("ModelType cant be ALL here.");
            }
        }

        if (measureSearchModel.isDraft() != VersionType.ALL) {
            predicatesList.add(cb.equal(root.get(DRAFT), measureSearchModel.isDraft() == VersionType.DRAFT));
        }

        if (measureSearchModel.isPatientBased() != PatientBasedType.ALL) {
            predicatesList.add(cb.equal(root.get(PATIENT_BASED), measureSearchModel.isPatientBased() == PatientBasedType.PATIENT));
        }

        if (CollectionUtils.isNotEmpty(measureSearchModel.getScoringTypes()) && measureSearchModel.getScoringTypes().size() < 4) {
            predicatesList.add(root.get(MEASURE_SCORING_TYPE).in(measureSearchModel.getScoringTypes()));
        }

        if (measureSearchModel.getModifiedDate() > 0) {
            predicatesList.add(cb.greaterThan(root.get("lastModifiedOn"),
                    java.sql.Date.valueOf(LocalDate.now().minusDays(measureSearchModel.getModifiedDate()))));
        }

        if (StringUtils.isNotBlank(measureSearchModel.getModifiedOwner())) {
            final Subquery<String> subQuery = buildUserSubQuery(cb, query, measureSearchModel.getModifiedOwner().toLowerCase());
            predicatesList.add(cb.in(root.get("lastModifiedBy").get(ID)).value(subQuery));
        }

        if (StringUtils.isNotBlank(measureSearchModel.getOwner())) {
            final Subquery<String> subQuery = buildUserSubQuery(cb, query, measureSearchModel.getOwner().toLowerCase());
            predicatesList.add(cb.in(root.get(OWNER).get(ID)).value(subQuery));
        }

        return cb.and(predicatesList.toArray(new Predicate[predicatesList.size()]));
    }

    private Subquery<String> buildUserSubQuery(CriteriaBuilder cb, CriteriaQuery<Measure> query, String userName) {
        final Subquery<String> subQuery = query.subquery(String.class);
        final Root<User> subRoot = subQuery.from(User.class);

        subQuery.select(subRoot.get(ID)).where(cb.or(
                cb.like(cb.lower(subRoot.get(FIRST_NAME)), "%" + userName + "%"),
                cb.like(cb.lower(subRoot.get(LAST_NAME)), "%" + userName + "%")));

        return subQuery;
    }

    private List<MeasureShareDTO> getOrderedDTOListFromMeasureResults(MeasureSearchModel measureSearchModel, User user,
                                                                      List<Measure> measureResultList) {

        final ArrayList<MeasureShareDTO> orderedDTOList = new ArrayList<>();

        final Map<String, MeasureShareDTO> measureSetIdDraftableMap = new HashMap<>();

        final List<Measure> measureSets = getAllMeasuresInSet(measureResultList);

        for (final Measure measure : measureSets) {
            final MeasureShareDTO dto = extractDTOFromMeasure(measure);
            if (dto.isDraft()) {
                measureSetIdDraftableMap.put(dto.getMeasureSetId(), dto);
            }
            if (measureResultList.contains(measure)) {
                orderedDTOList.add(dto);
            }
        }

        final boolean isNormalUserAndAllMeasures = user.getSecurityRole().getId().equals("3") && (measureSearchModel.getIsMyMeasureSearch() == SearchWidgetWithFilter.ALL);

        if (CollectionUtils.isNotEmpty(orderedDTOList)) {
            final List<MeasureShare> shareList = getShareList(user.getId(), null, null);
            final HashMap<String, String> measureSetIdToShareLevel = new HashMap<>();
            for (final MeasureShare share : shareList) {
                final String msid = share.getMeasure().getMeasureSet().getId();
                final String shareLevel = share.getShareLevel().getId();

                final String existingShareLevel = measureSetIdToShareLevel.get(msid);
                if ((existingShareLevel == null) || ShareLevel.VIEW_ONLY_ID.equals(existingShareLevel)) {
                    measureSetIdToShareLevel.put(msid, shareLevel);
                }
            }

            for (final Iterator<MeasureShareDTO> iterator = orderedDTOList.iterator(); iterator.hasNext(); ) {
                final MeasureShareDTO dto = iterator.next();
                final String msid = dto.getMeasureSetId();

                final String shareLevel = measureSetIdToShareLevel.get(msid);
                if (isNormalUserAndAllMeasures && dto.isPrivateMeasure() && !dto.getOwnerUserId().equals(user.getId())
                        && ((shareLevel == null) || !shareLevel.equals(ShareLevel.MODIFY_ID))) {
                    iterator.remove();
                    continue;
                }

                final boolean hasDraft = measureSetIdDraftableMap.containsKey(msid);
                boolean isSharedToEdit = false;

                if (shareLevel != null) {
                    dto.setShareLevel(shareLevel);
                    isSharedToEdit = ShareLevel.MODIFY_ID.equals(shareLevel);
                }

                final String userRole = LoggedInUserUtil.getLoggedInUserRole();
                final boolean isSuperUser = SecurityRole.SUPER_USER_ROLE.equals(userRole);
                final boolean isOwner = LoggedInUserUtil.getLoggedInUser().equals(dto.getOwnerUserId());
                final boolean isEditable = (isOwner || isSuperUser || isSharedToEdit);

                if (!dto.isLocked() && isEditable) {
                    if (hasDraft) {
                        final MeasureShareDTO draftLibrary = measureSetIdDraftableMap.get(msid);
                        if (dto.getMeasureId().equals(draftLibrary.getMeasureId())) {
                            dto.setVersionable(true);
                            dto.setDraftable(false);
                        } else if (dto.getMeasureSetId().equals(draftLibrary.getMeasureSetId())) {
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

        if (MAX_PAGE_SIZE < orderedDTOList.size()) {
            return orderedDTOList.subList(0, MAX_PAGE_SIZE);
        } else {
            return orderedDTOList;
        }

    }

    /**
     * Checks if is locked.
     *
     * @param lockedOutDate the locked out date
     * @return false if current time - lockedOutDate < the lock threshold
     */
    private boolean isLocked(Date lockedOutDate) {
        if (lockedOutDate == null) {
            return false;
        }
        final long timeDiff = System.currentTimeMillis() - lockedOutDate.getTime();

        return (timeDiff < LOCK_THRESHOLD);
    }

    @Override
    public boolean isMeasureLocked(String measureId) {

        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<Timestamp> query = cb.createQuery(Timestamp.class);
        final Root<Measure> root = query.from(Measure.class);

        query.select(root.get("lockedOutDate")).where(cb.equal(root.get(ID), measureId));

        final Timestamp lockedOutDate = session.createQuery(query).getSingleResult();

        return isLocked(lockedOutDate);
    }

    /*
     * (non-Javadoc)
     *
     * @see mat.dao.clause.MeasureDAO#resetLockDate(mat.model.clause.Measure) This
     * method opens a new Session and new transaction to update only the lockedDate
     * column in the database. After updating the table,transaction has been
     * committed and session has been closed.
     */
    @Override
    public void resetLockDate(Measure m) {
        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaUpdate<Measure> update = cb.createCriteriaUpdate(Measure.class);
        final Root<Measure> root = update.from(Measure.class);

        update.set("lockedUser", null);
        update.set("lockedOutDate", null);
        update.where(cb.equal(root.get(ID), m.getId()));

        final int rowCount = session.createQuery(update).executeUpdate();
        logger.info("Updated Private column" + rowCount);
    }

    @Override
    public int saveandReturnMaxEMeasureId(Measure measure) {
        final int eMeasureId = getMaxEMeasureId() + 1;
        final List<Measure> measuresToGetSetFor = new ArrayList<>();
        measuresToGetSetFor.add(measure);

        final List<Measure> measuresInSet = getAllMeasuresInSet(measuresToGetSetFor);
        for (final Measure m : measuresInSet) {
            m.seteMeasureId(eMeasureId);
            save(m);
        }

        return eMeasureId;

    }

    @Override
    public void saveMeasure(Measure measure) {
        save(measure);
    }

    private Predicate getSearchByMeasureOrOwnerNamePredicate(String searchText, CriteriaBuilder cb, Root<Measure> root) {
        final String lowerCaseSearchText = searchText.toLowerCase();

        return cb.or(cb.like(cb.lower(root.get("aBBRName")), "%" + lowerCaseSearchText + "%"),
                cb.like(cb.lower(root.get("description")), "%" + lowerCaseSearchText + "%"),
                cb.like(cb.lower(root.get(OWNER).get(FIRST_NAME)), "%" + lowerCaseSearchText + "%"),
                cb.like(cb.lower(root.get(OWNER).get(LAST_NAME)), "%" + lowerCaseSearchText + "%"),
                cb.like(root.get("eMeasureId").as(String.class), "%" + lowerCaseSearchText + "%"));
    }

    private List<Measure> sortMeasureList(List<Measure> measureResultList) {
        // generate sortable lists
        final List<List<Measure>> measureLists = new ArrayList<>();
        for (final Measure m : measureResultList) {
            boolean hasList = false;
            for (final List<Measure> mlist : measureLists) {
                final String msetId = mlist.get(0).getMeasureSet().getId();
                if (m.getMeasureSet().getId().equalsIgnoreCase(msetId)) {
                    mlist.add(m);
                    hasList = true;
                    break;
                }
            }

            if (!hasList) {
                final List<Measure> mlist = new ArrayList<>();
                mlist.add(m);
                measureLists.add(mlist);
            }
        }
        // sort
        for (final List<Measure> mlist : measureLists) {
            Collections.sort(mlist, new MeasureComparator());
        }
        Collections.sort(measureLists, new MeasureListComparator());

        // compile list
        final List<Measure> retList = new ArrayList<>();
        for (final List<Measure> mlist : measureLists) {
            for (final Measure m : mlist) {
                retList.add(m);
            }
        }
        return retList;
    }

    /**
     * Group the measures with measure set id and find draft or highest version
     * number which ever is available.
     *
     * @param measureResultList
     * @return
     */
    public List<Measure> sortMeasureListForMeasureOwner(List<Measure> measureResultList) {
        // generate sortable lists
        final List<List<Measure>> measureLists = new ArrayList<>();
        for (final Measure m : measureResultList) {
            boolean hasList = false;
            for (final List<Measure> mlist : measureLists) {
                final String msetId = mlist.get(0).getMeasureSet().getId();
                if (m.getMeasureSet().getId().equalsIgnoreCase(msetId)) {
                    mlist.add(m);
                    hasList = true;
                    break;
                }
            }

            if (!hasList) {
                final List<Measure> mlist = new ArrayList<>();
                mlist.add(m);
                measureLists.add(mlist);
            }
        }
        Collections.sort(measureLists, new MeasureListComparator());
        // compile list
        final List<Measure> retList = new ArrayList<>();
        for (final List<Measure> mlist : measureLists) {
            boolean isDraftAvailable = false;
            Measure measure = null;
            for (final Measure m : mlist) {
                measure = m;
                if (m.isDraft()) {
                    isDraftAvailable = true;
                    retList.add(m);
                    break;
                }
            }
            if (!isDraftAvailable && measure != null) {

                final String maxVersion = getMaxVersion(measure.getMeasureSet().getId(), measure.getOwner().getId());

                for (final Measure m : mlist) {
                    if (!m.isDraft() && m.getVersion().equalsIgnoreCase(maxVersion)) {
                        retList.add(m);
                        break;
                    }
                }
            }
        }
        return retList;
    }

    @Override
    public void updatePrivateColumnInMeasure(String measureId, boolean isPrivate) {
        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaUpdate<Measure> update = cb.createCriteriaUpdate(Measure.class);
        final Root<Measure> root = update.from(Measure.class);

        update.set("isPrivate", isPrivate);
        update.where(cb.equal(root.get(ID), measureId));

        final int rowCount = session.createQuery(update).executeUpdate();
        logger.info("Updated Private column" + rowCount);
    }

    @Override
    public boolean getMeasure(String measureId) {
        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<Long> query = cb.createQuery(Long.class);
        final Root<Measure> root = query.from(Measure.class);

        query.select(cb.countDistinct(root)).where(cb.equal(root.get(ID), measureId));

        return (session.createQuery(query).getSingleResult() > 0);
    }

    @Override
    public ShareLevel findShareLevelForUser(String measureId, String userID, String measureSetId) {
        ShareLevel shareLevel = null;

        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<MeasureShare> query = cb.createQuery(MeasureShare.class);
        final Root<MeasureShare> root = query.from(MeasureShare.class);

        final Subquery<Measure> sq = query.subquery(Measure.class);
        final Root<Measure> lib = sq.from(Measure.class);

        sq.select(lib.get(ID)).where(cb.equal(lib.get(MEASURE_SET).get(ID), measureSetId));

        query.select(root).where(cb.and(cb.equal(root.get(SHARE_USER).get(ID), userID)),
                cb.in(root.get(MEASURE).get(ID)).value(sq));

        final List<MeasureShare> shareList = session.createQuery(query).getResultList();

        if (CollectionUtils.isNotEmpty(shareList)) {
            shareLevel = shareList.get(0).getShareLevel();
        }

        return shareLevel;
    }

    @Override
    public List<MeasureShareDTO> getComponentMeasureShareInfoForUserWithFilter(MeasureSearchModel measureSearchModel, User user) {
        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<Measure> query = cb.createQuery(Measure.class);
        final Root<Measure> root = query.from(Measure.class);

        final Predicate predicate = buildPredicateForCompositeMeasure(measureSearchModel, cb, root);

        query.select(root).where(predicate).distinct(true);

        List<Measure> measureResultList = session.createQuery(query).getResultList();

        measureResultList = sortMeasureList(measureResultList);

        return getOrderedDTOListFromMeasureResults(measureSearchModel, user, measureResultList);
    }

    private Predicate buildPredicateForCompositeMeasure(MeasureSearchModel measureSearchModel, CriteriaBuilder cb, Root<Measure> root) {
        final List<Predicate> predicatesList = new ArrayList<>();

        if (StringUtils.isNotBlank(measureSearchModel.getQdmVersion())) {
            predicatesList.add(cb.equal(root.get("qdmVersion"), measureSearchModel.getQdmVersion()));
        }
        //Filter out draft measures
        predicatesList.add(cb.not(root.get(DRAFT)));

        if (BooleanUtils.isTrue(measureSearchModel.isOmitCompositeMeasure())) {
            predicatesList.add(cb.not(root.get("isCompositeMeasure")));
        }

        if (measureSearchModel.getMeasureSetId() != null) {
            predicatesList.add(cb.equal(root.get(MEASURE_SET).get(ID), measureSearchModel.getMeasureSetId()));
        }

        if (StringUtils.isNotBlank(measureSearchModel.getSearchTerm())) {
            predicatesList.add(getSearchByMeasureOrOwnerNamePredicate(measureSearchModel.getSearchTerm(), cb, root));
        }

        //Filtering out Non-CQL measures
        final Expression<Integer> relVerInt = cb.substring(root.get("releaseVersion"), 2, 1).as(Integer.class);
        predicatesList.add(cb.greaterThanOrEqualTo(relVerInt, 5));

        return cb.and(predicatesList.toArray(new Predicate[predicatesList.size()]));
    }

    public String getMeasureNameIfDraftAlreadyExists(String measureSetId) {
        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<String> query = cb.createQuery(String.class);
        final Root<Measure> root = query.from(Measure.class);

        query.select(root.get("description")).where(cb.and(cb.equal(root.get(MEASURE_SET).get(ID), measureSetId),
                cb.equal(root.get(DRAFT), true)));

        try {
            return session.createQuery(query).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public Optional<Measure> getDraftMeasureIfExists(String measureSetId) {
        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<Measure> query = cb.createQuery(Measure.class);
        final Root<Measure> root = query.from(Measure.class);

        query.select(root).where(cb.and(cb.equal(root.get(MEASURE_SET).get(ID), measureSetId),
                cb.equal(root.get(DRAFT), true)));

        // Measure set can have only 0 or 1 draft measures in it.
        List<Measure> resultList = session.createQuery(query).setMaxResults(1).getResultList();
        return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.get(0));
    }

    @Override
    public int deleteFhirMeasuresBySetId(String measureSetId) {
        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaDelete<Measure> deleteCriteria = cb.
                createCriteriaDelete(Measure.class);
        Root<Measure> root = deleteCriteria.from(Measure.class);

        Predicate wherePredicate = cb.and(cb.equal(root.get(MEASURE_SET).get(ID), measureSetId), cb.equal(root.get(MEASURE_MODEL), ModelTypeHelper.FHIR));

        return session.createQuery(deleteCriteria.where(wherePredicate)).executeUpdate();
    }

    class MeasureComparator implements Comparator<Measure> {

        @Override
        public int compare(Measure o1, Measure o2) {
            // 1 if either isDraft
            // 2 version
            if (o1.isDraft()) {
                return -1;
            }
            return o2.isDraft() ? 1 : compareDoubleStrings(o1.getVersion(), o2.getVersion());
        }

        private int compareDoubleStrings(String s1, String s2) {
            final Double d1 = Double.parseDouble(s1);
            final Double d2 = Double.parseDouble(s2);
            return d2.compareTo(d1);
        }
    }

    /*
     * assumption: each measure in this list is part of the same measure set
     */
    class MeasureListComparator implements Comparator<List<Measure>> {
        @Override
        public int compare(List<Measure> o1, List<Measure> o2) {
            final String v1 = o1.get(0).getDescription();
            final String v2 = o2.get(0).getDescription();
            return v1.compareToIgnoreCase(v2);
        }
    }
}
