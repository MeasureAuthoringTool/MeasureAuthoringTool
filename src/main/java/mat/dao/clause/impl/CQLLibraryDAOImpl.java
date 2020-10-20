package mat.dao.clause.impl;

import mat.client.measure.MeasureSearchFilterPanel;
import mat.dao.UserDAO;
import mat.dao.search.GenericDAO;
import mat.model.LockedUserInfo;
import mat.model.SecurityRole;
import mat.model.User;
import mat.model.clause.CQLLibrary;
import mat.model.clause.ModelTypeHelper;
import mat.model.clause.ShareLevel;
import mat.model.cql.CQLLibraryShare;
import mat.model.cql.CQLLibraryShareDTO;
import mat.server.LoggedInUserUtil;
import mat.server.logging.LogFactory;
import mat.server.util.MATPropertiesService;
import mat.shared.LibrarySearchModel;
import mat.shared.SearchModel.ModelType;
import mat.shared.SearchModel.VersionType;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

@Repository("cqlLibraryDAO")
public class CQLLibraryDAOImpl extends GenericDAO<CQLLibrary, String> implements mat.dao.clause.CQLLibraryDAO {

    private static final Log logger = LogFactory.getLog(CQLLibraryDAOImpl.class);

    private static final String DRAFT = "draft";
    private static final String SET_ID = "setId";
    private static final String VERSION = "version";
    private static final String OWNER_ID = "ownerId";
    private static final String LAST_NAME = "lastName";
    private static final String FIRST_NAME = "firstName";
    private static final String SHARE_USER = "shareUser";
    private static final String MEASURE_ID = "measureId";
    private static final String CQL_LIBRARY = "cqlLibrary";
    private static final String LIBRARY_NAME = "name";
    private static final String LIBRARY_MODEL_TYPE = "libraryModelType";
    private static final String QDM_VERSION = "qdmVersion";
    private static final String FHIR_VERSION = "fhirVersion";
    private static final String REVISION_NUMBER = "revisionNumber";

    @Autowired
    private UserDAO userDAO;

    public CQLLibraryDAOImpl(@Autowired SessionFactory sessionFactory) {
        setSessionFactory(sessionFactory);
    }

    class CQLLibraryComparator implements Comparator<CQLLibrary> {

        @Override
        public int compare(CQLLibrary o1, CQLLibrary o2) {
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
     * assumption: each CQL in this list is part of the same measure set
     */
    class CQLLibraryListComparator implements Comparator<List<CQLLibrary>> {
        @Override
        public int compare(List<CQLLibrary> o1, List<CQLLibrary> o2) {
            final String v1 = o1.get(0).getName();
            final String v2 = o2.get(0).getName();
            return v1.compareToIgnoreCase(v2);
        }
    }

    private static final long LOCK_THRESHOLD = TimeUnit.MINUTES.toMillis(3); // 3 minutes

    @Override
    public List<CQLLibrary> searchForIncludes(String setId, String libraryName, String searchText, String modelType) {

        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<CQLLibrary> query = cb.createQuery(CQLLibrary.class);
        final Root<CQLLibrary> root = query.from(CQLLibrary.class);

        final Predicate predicate = buildPredicateForSearchingLibraries(setId, libraryName, searchText, cb, root, modelType);

        query.select(root).where(predicate);

        query.orderBy(cb.asc(root.get(LIBRARY_NAME)), cb.desc(root.get(VERSION)));

        return session.createQuery(query).getResultList();
    }

    private Predicate buildPredicateForSearchingLibraries(String setId, String libraryName, String searchText,
                                                          CriteriaBuilder cb, Root<CQLLibrary> root, String modelType) {
        // Common criteria
        List<Predicate> predicates = new ArrayList<>(List.of(
                cb.equal(root.get(DRAFT), false),
                cb.notEqual(root.get(LIBRARY_NAME), libraryName),
                cb.notEqual(root.get(SET_ID), setId)
        ));

        // Model version
        if (ModelTypeHelper.isFhir(modelType)) {
            predicates.add(cb.equal(root.get(FHIR_VERSION), MATPropertiesService.get().getFhirVersion()));
        } else {
            predicates.add(cb.equal(root.get(QDM_VERSION), MATPropertiesService.get().getQdmVersion()));
        }

        // Model Type criteria
        if (StringUtils.isNotBlank(modelType)) {
            predicates.add(cb.equal(root.get(LIBRARY_MODEL_TYPE), modelType));
        } else {
            predicates.add(cb.isNull(root.get(LIBRARY_MODEL_TYPE)));
        }

        final Predicate p1 = cb.and(predicates.toArray(new Predicate[0]));
        final Predicate p2 = getSearchByLibraryNameOrOwnerNamePredicate(searchText, cb, root);

        return (p2 != null) ? cb.and(p1, p2) : p1;
    }

    @Override
    public List<CQLLibrary> searchForReplaceLibraries(String setId) {

        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<CQLLibrary> query = cb.createQuery(CQLLibrary.class);
        final Root<CQLLibrary> root = query.from(CQLLibrary.class);

        query.where(cb.and(cb.equal(root.get("qdmVersion"), MATPropertiesService.get().getQdmVersion())),
                cb.equal(root.get(DRAFT), false), cb.equal(root.get(SET_ID), setId));

        query.orderBy(cb.asc(root.get(LIBRARY_NAME)), cb.desc(root.get(VERSION))).distinct(true);

        return session.createQuery(query).getResultList();
    }

    @Override
    public List<CQLLibraryShareDTO> search(LibrarySearchModel model, int pageSize, User user) {
        final String userRole = LoggedInUserUtil.getLoggedInUserRole();
        List<CQLLibraryShareDTO> orderedList;
        if (SecurityRole.ADMIN_ROLE.equals(userRole)) {
            orderedList = searchLibrariesForAdmin(model.getSearchTerm());
        } else {
            orderedList = searchForNonAdmin(model, user);
        }

        if (pageSize < orderedList.size()) {
            return orderedList.subList(0, pageSize);
        } else {
            return orderedList;
        }

    }

    public List<CQLLibraryShareDTO> searchForNonAdmin(LibrarySearchModel librarySearchModel, User user) {

        final List<CQLLibraryShareDTO> orderedList = new ArrayList<>();

        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<CQLLibrary> query = cb.createQuery(CQLLibrary.class);
        final Root<CQLLibrary> root = query.from(CQLLibrary.class);

        final Predicate predicate = buildPredicateForSearchingLibrariesForNonAdmin(librarySearchModel, cb, root, query, user.getId());

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

        final Map<String, CQLLibraryShareDTO> cqlLibIdDTOMap = new HashMap<>();
        final Map<String, CQLLibraryShareDTO> cqlLibSetIdDraftableMap = new HashMap<>();

        for (final CQLLibrary cqlLibrary : orderedCQlLibList) {
            final CQLLibraryShareDTO dto = extractDTOFromCQLLibrary(cqlLibrary);
            final boolean isDraft = dto.isDraft();
            if (isDraft) {
                cqlLibSetIdDraftableMap.put(dto.getCqlLibrarySetId(), dto);
            }
            cqlLibIdDTOMap.put(cqlLibrary.getId(), dto);
            orderedList.add(dto);
        }

        final List<CQLLibraryShare> shareList = getLibraryShareInfoByShareUserId(user.getId());

        final HashMap<String, String> cqlSetIdToShareLevel = new HashMap<>();
        if (CollectionUtils.isNotEmpty(orderedList)) {
            for (final CQLLibraryShare share : shareList) {
                final String msid = share.getCqlLibrary().getSetId();
                final String shareLevel = share.getShareLevel().getId();
                final String existingShareLevel = cqlSetIdToShareLevel.get(msid);
                if (existingShareLevel == null) {
                    cqlSetIdToShareLevel.put(msid, shareLevel);
                }
            }
            for (int i = 0; i < orderedList.size(); i++) {
                final CQLLibraryShareDTO dto = orderedList.get(i);
                final String cqlsid = dto.getCqlLibrarySetId();
                final boolean hasDraft = cqlLibSetIdDraftableMap.containsKey(cqlsid);
                final String shareLevel = cqlSetIdToShareLevel.get(cqlsid);
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
                        final CQLLibraryShareDTO draftLibrary = cqlLibSetIdDraftableMap.get(cqlsid);
                        if (dto.getCqlLibraryId().equals(draftLibrary.getCqlLibraryId())) {
                            dto.setVersionable(true);
                            dto.setDraftable(false);
                        } else if (dto.getCqlLibrarySetId().equals(draftLibrary.getCqlLibrarySetId())) {
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

    private List<CQLLibraryShare> getLibraryShareInfoByShareUserId(String userId) {
        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<CQLLibraryShare> query = cb.createQuery(CQLLibraryShare.class);
        final Root<CQLLibraryShare> root = query.from(CQLLibraryShare.class);

        query.select(root).where(cb.equal(root.get(SHARE_USER).get("id"), userId));

        return session.createQuery(query).getResultList();
    }

    private Predicate buildPredicateForSearchingLibrariesForNonAdmin(LibrarySearchModel librarySearchModel, CriteriaBuilder cb,
                                                                     Root<CQLLibrary> root, CriteriaQuery<CQLLibrary> query, String userId) {
        final List<Predicate> predicatesList = new ArrayList<>();

        predicatesList.add(buildPredicateForSearchingLibrariesForAdmin(librarySearchModel.getSearchTerm(), cb, root));

        if (librarySearchModel.getIsMyMeasureSearch() == MeasureSearchFilterPanel.MY_MEASURES) {
            final Join<CQLLibrary, CQLLibraryShare> childJoin = root.join("shares", JoinType.LEFT);
                predicatesList.add(cb.or(cb.equal(root.get(OWNER_ID).get("id"), userId), cb.equal(childJoin.get(SHARE_USER).get("id"), userId)));
        }

        if (librarySearchModel.isDraft() != VersionType.ALL) {
            predicatesList.add(cb.equal(root.get(DRAFT), librarySearchModel.isDraft() == VersionType.DRAFT));
        }

        if (librarySearchModel.getModelType() != ModelType.ALL) {
            switch (librarySearchModel.getModelType()) {
                case FHIR:
                    predicatesList.add(cb.equal(root.get(LIBRARY_MODEL_TYPE), librarySearchModel.getModelType().name()));
                    break;
                case QDM_CQL:
                    predicatesList.add(cb.equal(root.get(LIBRARY_MODEL_TYPE), "QDM"));
                    break;
                default:
                    throw new IllegalStateException("ModelType cant be QDM_QDM for libraries.");
            }
        }

        if (librarySearchModel.getModifiedDate() > 0) {
            predicatesList.add(cb.greaterThan(root.get("lastModifiedOn"),
                    LocalDate.now().minusDays(librarySearchModel.getModifiedDate()).atStartOfDay()));
        }

        if (StringUtils.isNotBlank(librarySearchModel.getModifiedOwner())) {
            final Subquery<String> subQuery = buildUserSubQuery(cb, query, librarySearchModel.getModifiedOwner().toLowerCase());
            predicatesList.add(cb.in(root.get("lastModifiedBy").get("id")).value(subQuery));
        }

        if (StringUtils.isNotBlank(librarySearchModel.getOwner())) {
            final Subquery<String> subQuery = buildUserSubQuery(cb, query, librarySearchModel.getOwner().toLowerCase());
            predicatesList.add(cb.in(root.get(OWNER_ID).get("id")).value(subQuery));
        }

        if (!librarySearchModel.isMatOnFhir()) {
            predicatesList.add(cb.notEqual(root.get(LIBRARY_MODEL_TYPE), ModelTypeHelper.FHIR));
        }

        return cb.and(predicatesList.toArray(new Predicate[predicatesList.size()]));
    }

    private Subquery<String> buildUserSubQuery(CriteriaBuilder cb, CriteriaQuery<CQLLibrary> query, String userName) {
        final Subquery<String> subQuery = query.subquery(String.class);
        final Root<User> subRoot = subQuery.from(User.class);

        subQuery.select(subRoot.get("id")).where(cb.or(
                cb.like(cb.lower(subRoot.get(FIRST_NAME)), "%" + userName + "%"),
                cb.like(cb.lower(subRoot.get(LAST_NAME)), "%" + userName + "%")));

        return subQuery;
    }

    @Override
    public List<CQLLibrary> getAllLibrariesInSet(List<CQLLibrary> libraries) {
        if (CollectionUtils.isNotEmpty(libraries)) {
            final Set<String> cqlLibSetIds = new HashSet<>();
            for (final CQLLibrary m : libraries) {
                cqlLibSetIds.add(m.getSetId());
            }

            final Session session = getSessionFactory().getCurrentSession();
            final CriteriaBuilder cb = session.getCriteriaBuilder();
            final CriteriaQuery<CQLLibrary> query = cb.createQuery(CQLLibrary.class);
            final Root<CQLLibrary> root = query.from(CQLLibrary.class);

            query.select(root).where(root.get(SET_ID).in(cqlLibSetIds));

            libraries = session.createQuery(query).getResultList();

        }
        return sortLibraryList(libraries);
    }

    private List<CQLLibrary> sortLibraryList(List<CQLLibrary> libraryResultList) {
        final List<List<CQLLibrary>> libraryList = new ArrayList<>();
        for (final CQLLibrary cqlLib : libraryResultList) {
            boolean hasList = false;
            for (final List<CQLLibrary> list : libraryList) {
                final String cqlsetId = list.get(0).getSetId();
                if (cqlLib.getSetId().equalsIgnoreCase(cqlsetId)) {
                    list.add(cqlLib);
                    hasList = true;
                    break;
                }
            }
            if (!hasList) {
                final List<CQLLibrary> cqllist = new ArrayList<>();
                cqllist.add(cqlLib);
                libraryList.add(cqllist);
            }
        }

        for (final List<CQLLibrary> list : libraryList) {
            Collections.sort(list, new CQLLibraryComparator());
        }

        Collections.sort(libraryList, new CQLLibraryListComparator());

        final List<CQLLibrary> retList = new ArrayList<>();
        for (final List<CQLLibrary> mlist : libraryList) {
            for (final CQLLibrary m : mlist) {
                retList.add(m);
            }
        }
        return retList;
    }

    @Override
    public boolean isLibraryLocked(String cqlLibraryId) {
        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<Timestamp> query = cb.createQuery(Timestamp.class);
        final Root<CQLLibrary> root = query.from(CQLLibrary.class);

        query.select(root.get("lockedOutDate")).where(cb.equal(root.get("id"), cqlLibraryId));

        final Timestamp lockedOutDate = session.createQuery(query).getSingleResult();

        return isLocked(lockedOutDate);
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
    public void updateLockedOutDate(CQLLibrary existingLibrary) {
        super.save(existingLibrary);
    }

    @Override
    public String findMaxVersion(String setId, String ownerId) {
        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<String> query = cb.createQuery(String.class);
        final Root<CQLLibrary> root = query.from(CQLLibrary.class);

        final Predicate predicate = buildPredicateForMaxVersion(setId, ownerId, cb, root);

        query.select(cb.max(root.get(VERSION)).as(String.class)).where(predicate);

        return session.createQuery(query).getSingleResult();
    }

    private Predicate buildPredicateForMaxVersion(String setId, String ownerId, CriteriaBuilder cb, Root<CQLLibrary> root) {
        // add check to filter Draft's version number when finding max version number.
        final Predicate p1 = cb.and(cb.equal(root.get(SET_ID), setId), cb.not(root.get(DRAFT)));
        Predicate p2 = null;
        if (StringUtils.isNotBlank(ownerId)) {
            p2 = cb.equal(root.get(OWNER_ID).get("id"), ownerId);
        }

        return (p2 != null) ? cb.and(p1, p2) : p1;
    }

    @Override
    public CQLLibrary findByNameAndVersion(String name, double version, int revisionNumber) {
        Session session = getSessionFactory().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<CQLLibrary> query = cb.createQuery(CQLLibrary.class);
        Root<CQLLibrary> root = query.from(CQLLibrary.class);
        query.select(root).where(cb.and(cb.equal(root.get(LIBRARY_NAME), name),
                cb.and(cb.equal(root.get(VERSION), version),
                        cb.and(cb.equal(root.get(REVISION_NUMBER), revisionNumber)))));
        return session.createQuery(query).getSingleResult();
    }

    @Override
    public String findMaxOfMinVersion(String setId, String version) {
        logger.debug("In CQLLibraryDAO.findMaxOfMinVersion()");
        String maxOfMinVersion = version;
        double minVal = 0;
        double maxVal = 0;
        if (StringUtils.isNotBlank(version)) {
            final int decimalIndex = version.indexOf('.');
            minVal = Integer.parseInt(version.substring(0, decimalIndex));
            logger.debug("Min value: " + minVal);
            maxVal = minVal + 1;
            logger.debug("Max value: " + maxVal);
        }

        final List<CQLLibrary> cqlList = getListOfVersionsForALibrary(setId);

        double tempVersion = 0;

        if (CollectionUtils.isNotEmpty(cqlList)) {
            logger.debug("Finding max of min version from the Library List. Size:" + cqlList.size());
            for (final CQLLibrary library : cqlList) {
                logger.debug("Looping through Lib Id: " + library.getId() + " Version: " + library.getVersion());
                if ((library.getVersionNumber() > minVal) && (library.getVersionNumber() < maxVal)) {
                    if (tempVersion < library.getVersionNumber()) {
                        logger.debug(tempVersion + "<" + library.getVersionNumber() + "=" + (tempVersion < library.getVersionNumber()));
                        maxOfMinVersion = library.getVersion();
                        logger.debug("maxOfMinVersion: " + maxOfMinVersion);
                    }
                    tempVersion = library.getVersionNumber();
                    logger.debug("tempVersion: " + tempVersion);
                }
            }
        }
        logger.debug("Returned maxOfMinVersion: " + maxOfMinVersion);
        return maxOfMinVersion;
    }

    private List<CQLLibrary> getListOfVersionsForALibrary(String setId) {

        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<CQLLibrary> query = cb.createQuery(CQLLibrary.class);
        final Root<CQLLibrary> root = query.from(CQLLibrary.class);

        query.select(root).where(cb.and(cb.equal(root.get(SET_ID), setId), cb.not(root.get(DRAFT))));
        query.orderBy(cb.asc(root.get(VERSION)));

        return session.createQuery(query).getResultList();

    }

    @Override
    public List<CQLLibraryShareDTO> getLibraryShareInfoForLibrary(String cqlId, String searchText) {
        final int pageSize = Integer.MAX_VALUE;

        final List<User> userResults = userDAO.getUsersListForSharingMeasureOrLibrary(searchText);

        final HashMap<String, CQLLibraryShareDTO> userIdDTOMap = new HashMap<>();
        final ArrayList<CQLLibraryShareDTO> orderedDTOList = new ArrayList<>();

        for (final User user : userResults) {
            final CQLLibraryShareDTO dto = new CQLLibraryShareDTO();
            dto.setUserId(user.getId());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setOrganizationName(user.getOrganizationName());
            userIdDTOMap.put(user.getId(), dto);
            orderedDTOList.add(dto);
        }

        if (CollectionUtils.isNotEmpty(orderedDTOList)) {

            final List<CQLLibraryShare> shareList = getShareList(cqlId, userIdDTOMap);

            for (final CQLLibraryShare share : shareList) {
                final User shareUser = share.getShareUser();
                final CQLLibraryShareDTO dto = userIdDTOMap.get(shareUser.getId());
                dto.setShareLevel(share.getShareLevel().getId());
            }
        }

        return (pageSize < orderedDTOList.size()) ? orderedDTOList.subList(0, pageSize) : orderedDTOList;
    }

    private List<CQLLibraryShare> getShareList(String cqlId, HashMap<String, CQLLibraryShareDTO> userIdDTOMap) {
        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<CQLLibraryShare> query = cb.createQuery(CQLLibraryShare.class);
        final Root<CQLLibraryShare> root = query.from(CQLLibraryShare.class);

        query.select(root).where(cb.and(root.get(SHARE_USER).get("id").in(userIdDTOMap.keySet()), cb.equal(root.get(CQL_LIBRARY).get("id"), cqlId)));

        return session.createQuery(query).getResultList();
    }

    @Override
    public List<CQLLibraryShare> getLibraryShareInforForLibrary(String libId) {
        if (libId == null) {
            return Collections.emptyList();
        }

        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<CQLLibraryShare> query = cb.createQuery(CQLLibraryShare.class);
        final Root<CQLLibraryShare> root = query.from(CQLLibraryShare.class);

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

        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<CQLLibraryShare> query = cb.createQuery(CQLLibraryShare.class);
        final Root<CQLLibraryShare> root = query.from(CQLLibraryShare.class);

        final Subquery<CQLLibrary> sq = query.subquery(CQLLibrary.class);
        final Root<CQLLibrary> lib = sq.from(CQLLibrary.class);

        sq.select(lib).where(cb.equal(lib.get(SET_ID), cqlLibrarySetId));

        query.select(root).where(cb.and(cb.equal(root.get(SHARE_USER).get("id"), userID)),
                cb.in(root.get(CQL_LIBRARY).get("id")).value(sq));

        final List<CQLLibraryShare> shareList = session.createQuery(query).getResultList();

        if (CollectionUtils.isNotEmpty(shareList)) {
            shareLevel = shareList.get(0).getShareLevel();
        }

        return shareLevel;
    }

    @Override
    public CQLLibraryShareDTO extractDTOFromCQLLibrary(CQLLibrary cqlLibrary) {
        final CQLLibraryShareDTO dto = new CQLLibraryShareDTO();

        dto.setCqlLibraryId(cqlLibrary.getId());
        dto.setCqlLibraryName(cqlLibrary.getName());
        dto.setOwnerUserId(cqlLibrary.getOwnerId().getId());
        dto.setDraft(cqlLibrary.isDraft());
        dto.setVersion(cqlLibrary.getVersion());
        dto.setLibraryModelType(cqlLibrary.getLibraryModelType());
        dto.setFinalizedDate(cqlLibrary.getFinalizedDate());
        dto.setCqlLibrarySetId(cqlLibrary.getSetId());
        dto.setRevisionNumber(cqlLibrary.getRevisionNumber());
        dto.setLibraryModelType(cqlLibrary.getLibraryModelType());
        dto.setReleaseVersion(cqlLibrary.getReleaseVersion());
        dto.setQdmVersion(cqlLibrary.getQdmVersion());
        dto.setFhirVersion(cqlLibrary.getFhirVersion());

        final boolean isLocked = isLocked(cqlLibrary.getLockedOutDate());
        dto.setLocked(isLocked);
        if (isLocked && (cqlLibrary.getLockedUserId() != null)) {
            final LockedUserInfo lockedUserInfo = new LockedUserInfo();
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
        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<CQLLibrary> query = cb.createQuery(CQLLibrary.class);
        final Root<CQLLibrary> root = query.from(CQLLibrary.class);

        query.select(root).where(cb.equal(root.get(OWNER_ID).get("id"), user.getId()));
        query.orderBy(cb.asc(root.get(VERSION)));

        final List<CQLLibrary> cList = session.createQuery(query).getResultList();
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
        final List<List<CQLLibrary>> librariesLists = new ArrayList<>();
        for (final CQLLibrary lib : libraryResultList) {
            boolean hasList = false;
            for (final List<CQLLibrary> clist : librariesLists) {
                final String setId = clist.get(0).getSetId();
                if (lib.getSetId().equalsIgnoreCase(setId)) {
                    clist.add(lib);
                    hasList = true;
                    break;
                }
            }
            if (!hasList) {
                final List<CQLLibrary> clist = new ArrayList<>();
                clist.add(lib);
                librariesLists.add(clist);
            }
        }
        Collections.sort(librariesLists, new CQLLibraryListComparator());
        // compile list
        final List<CQLLibrary> retList = new ArrayList<>();
        for (final List<CQLLibrary> clist : librariesLists) {
            boolean isDraftAvailable = false;
            CQLLibrary cql = null;
            for (final CQLLibrary lib : clist) {
                cql = lib;
                if (lib.isDraft()) {
                    isDraftAvailable = true;
                    retList.add(lib);
                    break;
                }
            }

            if (!isDraftAvailable && (cql != null)) {

                final String maxVersion = findMaxVersion(cql.getSetId(), cql.getOwnerId().getId());

                for (final CQLLibrary lib : clist) {
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
        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<CQLLibrary> query = cb.createQuery(CQLLibrary.class);
        final Root<CQLLibrary> root = query.from(CQLLibrary.class);

        final Predicate predicate = buildPredicateForSearchingLibrariesForAdmin(searchText, cb, root);

        query.select(root).where(predicate).distinct(true);

        query.orderBy(cb.desc(root.get(SET_ID)), cb.desc(root.get(DRAFT)), cb.desc(root.get(VERSION)));

        final List<CQLLibrary> libraryResultList = session.createQuery(query).getResultList();

        final ArrayList<CQLLibraryShareDTO> orderedDTOList = new ArrayList<>();
        for (final CQLLibrary library : libraryResultList) {
            final CQLLibraryShareDTO dto = extractDTOFromCQLLibrary(library);
            orderedDTOList.add(dto);
        }

        return filterLibraryListForAdmin(orderedDTOList);
    }

    private Predicate buildPredicateForSearchingLibrariesForAdmin(String searchText, CriteriaBuilder cb, Root<CQLLibrary> root) {

        final Predicate p1 = cb.isNull(root.get(MEASURE_ID));

        final Predicate p2 = getSearchByLibraryNameOrOwnerNamePredicate(searchText, cb, root);

        return (p2 != null) ? cb.and(p1, p2) : p1;
    }

    private Predicate getSearchByLibraryNameOrOwnerNamePredicate(String searchText, CriteriaBuilder cb, Root<CQLLibrary> root) {
        Predicate searchPredicate = null;
        if (StringUtils.isNotBlank(searchText)) {
            final String lowerCaseSearchText = searchText.toLowerCase();
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
     * @param cqlLibraryList - {@link List} of {@link CQLLibraryShareDTO}.
     * @return {@link List} of {@link CQLLibraryShareDTO}.
     */
    private List<CQLLibraryShareDTO> filterLibraryListForAdmin(final List<CQLLibraryShareDTO> cqlLibraryList) {
        final List<CQLLibraryShareDTO> updatedLibraryList = new ArrayList<>();
        for (final CQLLibraryShareDTO libraryShareDTO : cqlLibraryList) {
            if (CollectionUtils.isEmpty(updatedLibraryList)) {
                updatedLibraryList.add(libraryShareDTO);
            } else {
                boolean found = false;
                final ListIterator<CQLLibraryShareDTO> itr = updatedLibraryList.listIterator();
                while (itr.hasNext()) {
                    final CQLLibraryShareDTO shareDTO = itr.next();
                    if (libraryShareDTO.getCqlLibrarySetId().equals(shareDTO.getCqlLibrarySetId())) {
                        found = true;
                        if (libraryShareDTO.isDraft() ||
                                (!shareDTO.isDraft() && libraryShareDTO.getFinalizedDate().compareTo(shareDTO.getFinalizedDate()) > 0)) {
                            itr.remove();
                            itr.add(libraryShareDTO);
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
    public void refresh(CQLLibrary libObject) {
        getSessionFactory().getCurrentSession().refresh(libObject);
    }

    @Override
    public CQLLibrary getLibraryByMeasureId(String measureId) {
        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<CQLLibrary> query = cb.createQuery(CQLLibrary.class);
        final Root<CQLLibrary> root = query.from(CQLLibrary.class);

        query.select(root).where(cb.equal(root.get(MEASURE_ID), measureId));

        final List<CQLLibrary> resultCqlLibrary = session.createQuery(query).getResultList();

        return CollectionUtils.isNotEmpty(resultCqlLibrary) ? resultCqlLibrary.get(0) : null;

    }

    @Override
    public void save(CQLLibrary entity) {
        entity.setLastModifiedOn(LocalDateTime.now());
        entity.setLastModifiedBy(userDAO.findByLoginId(LoggedInUserUtil.getLoggedInLoginId()));
        super.save(entity);
    }

    @Override
    public boolean isLibraryNameExists(String name, String setId) {
        final Session session = getSessionFactory().getCurrentSession();

        final String libraryNameExistsQuery = buildLibraryNameCheckQuery(setId);

        final NativeQuery<?> booleanQuery = session.createNativeQuery(libraryNameExistsQuery);
        booleanQuery.setParameter("name", name);
        if (StringUtils.isNotBlank(setId)) {
            booleanQuery.setParameter(SET_ID, setId);
        }

        final int count = ((BigInteger) booleanQuery.uniqueResult()).intValue();
        return (count == 1);
    }

    private String buildLibraryNameCheckQuery(String setId) {
        final StringBuilder sb = new StringBuilder();
        sb.append("SELECT (");
        sb.append(buildNameCheckQuery(setId, "MEASURE", "MEASURE_SET_ID"));
        sb.append(" ) OR ( ");
        sb.append(buildNameCheckQuery(setId, "CQL_LIBRARY", "SET_ID"));
        sb.append(")");
        return sb.toString();
    }

    private String buildNameCheckQuery(String setId, String tableName, String columnName) {
        final StringBuilder sb = new StringBuilder();
        sb.append("SELECT EXISTS(");
        sb.append("SELECT 1 FROM ").append(tableName).append(" where CQL_NAME = :name ");
        if (StringUtils.isNotBlank(setId)) {
            sb.append("and ").append(columnName).append(" <> :setId ");
        }
        sb.append("and (DRAFT = 1 ");
        sb.append("OR (DRAFT = 0 and CAST(TRIM(LEADING 'v' from RELEASE_VERSION) AS decimal(2,1)) >= 5.8)))");
        return sb.toString();
    }

    public String getLibraryNameIfDraftAlreadyExists(String librarySetId) {
        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<String> query = cb.createQuery(String.class);
        final Root<CQLLibrary> root = query.from(CQLLibrary.class);

        query.select(root.get("name")).where(cb.and(cb.equal(root.get(SET_ID), librarySetId),
                cb.equal(root.get(DRAFT), true)));

        try {
            return session.createQuery(query).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public List<CQLLibrary> getDraftLibraryBySet(String cqlSetId) {
        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<CQLLibrary> query = cb.createQuery(CQLLibrary.class);
        final Root<CQLLibrary> root = query.from(CQLLibrary.class);

        query.select(root).where(cb.and(cb.equal(root.get(SET_ID), cqlSetId),
                cb.equal(root.get(DRAFT), true)));

        return session.createQuery(query).setMaxResults(1).getResultList();

    }

    @Override
    public int deleteDraftFhirLibrariesBySetId(String setId) {
        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaDelete<CQLLibrary> deleteCriteria = cb.
                createCriteriaDelete(CQLLibrary.class);
        Root<CQLLibrary> root = deleteCriteria.from(CQLLibrary.class);

        Predicate wherePredicate = cb.and(cb.equal(root.get(SET_ID), setId), cb.equal(root.get(LIBRARY_MODEL_TYPE), ModelTypeHelper.FHIR), cb.equal(root.get(DRAFT), true));

        return session.createQuery(deleteCriteria.where(wherePredicate)).executeUpdate();
    }

}
