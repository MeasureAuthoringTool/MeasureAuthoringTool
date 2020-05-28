package mat.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.UserDAO;
import mat.dao.search.GenericDAO;
import mat.model.Organization;
import mat.model.SecurityQuestions;
import mat.model.User;
import mat.model.UserSecurityQuestion;
import mat.server.LoggedInUserUtil;
import mat.server.model.MatUserDetails;

import static mat.model.Status.STATUS_ACTIVE;
import static mat.model.Status.STATUS_TERMINATED;

@Repository("userDAO")
public class UserDAOImpl extends GenericDAO<User, String> implements UserDAO {

    private static final Log logger = LogFactory.getLog(UserDAOImpl.class);

    private static final String SECURITY_ROLE_USER = "3";

    public UserDAOImpl(@Autowired SessionFactory sessionFactory) {
        setSessionFactory(sessionFactory);
    }

    @Override
    public void delete(String... ids) {
        for (String s : ids) {
            User u = find(s);
            delete(u);
        }
    }

    @Override
    public void expireTemporaryPasswords(Date targetDate) {
        Session session = getSessionFactory().getCurrentSession();
        int updatedCount = 0;
        final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        final CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        final Root<User> userRoot = criteriaQuery.from(User.class);
        criteriaQuery.select(userRoot).where(criteriaBuilder.lessThan(userRoot.<Date>get("createdDate"), targetDate));
        List<User> results = session.createQuery(criteriaQuery).getResultList();
        for (User u : results) {
            u.getPassword().setPassword("expired");
            updatedCount++;
        }

        logger.info("Expired password count: " + updatedCount);

    }

    @Override
    public void unlockUsers(Date unlockDate) {
        Session session = getSessionFactory().getCurrentSession();
        int updatedCount = 0;
        final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        final CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        final Root<User> userRoot = criteriaQuery.from(User.class);
        criteriaQuery.select(userRoot).where(criteriaBuilder.lessThan(userRoot.<Date>get("lockedOutDate"), unlockDate));
        List<User> results = session.createQuery(criteriaQuery).getResultList();
        for (User u : results) {
            u.setLockedOutDate(null);
            if (u.getPassword() != null) {
                u.getPassword().setForgotPwdlockCounter(0);
                u.getPassword().setPasswordlockCounter(0);
            }
            updatedCount++;
        }
        logger.info("Unlocked user count: " + updatedCount);
    }


    /**
     * Creates the search criteria.
     *
     * @param userRoot
     * @param text     the text
     * @return the criteria
     */
    private List<Predicate> createSearchCriteria(CriteriaBuilder criteriaBuilder, Root<User> userRoot, String text) {
        List<Predicate> predicates = new ArrayList<Predicate>();
        Predicate predicate1 = criteriaBuilder.or(criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(userRoot.get("firstName")), "%" + text.toLowerCase() + "%"),
                criteriaBuilder.like(criteriaBuilder.lower(userRoot.get("lastName")), "%" + text.toLowerCase() + "%")),
                criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(userRoot.get("emailAddress")), "%" + text.toLowerCase() + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(userRoot.get("loginId")), "%" + text.toLowerCase() + "%")));
        Predicate predicate2 = criteriaBuilder.and(criteriaBuilder.notEqual(userRoot.get("id"), "Admin"));
        predicates.add(predicate1);
        predicates.add(predicate2);
        return predicates;
    }

    /**
     * Creates the search criteria non admin user.
     *
     * @param text the text
     * @return the criteria
     */
    private List<Predicate> createSearchCriteriaNonAdminUser(CriteriaBuilder criteriaBuilder, Root<User> userRoot, String text) {
        List<Predicate> predicates = new ArrayList<Predicate>();
        Predicate predicate1 = criteriaBuilder.or(criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(userRoot.get("firstName")), "%" + text.toLowerCase() + "%"),
                criteriaBuilder.like(criteriaBuilder.lower(userRoot.get("lastName")), "%" + text.toLowerCase() + "%")),
                criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(userRoot.get("emailAddress")), "%" + text.toLowerCase() + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(userRoot.get("loginId")), "%" + text.toLowerCase() + "%")));
        Predicate predicate2 = criteriaBuilder.and(criteriaBuilder.notEqual(userRoot.get("securityRole").get("id"), "1"));
        Predicate predicate3 = criteriaBuilder.and(criteriaBuilder.notEqual(userRoot.get("status").get("statusId"), STATUS_TERMINATED));
        predicates.add(predicate1);
        predicates.add(predicate2);
        predicates.add(predicate3);
        return predicates;
    }

    @Override
    public List<User> searchForUsersByName(String name) {
        Session session = getSessionFactory().getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        final Root<User> userRoot = criteriaQuery.from(User.class);
        List<Predicate> predicates = createSearchCriteria(criteriaBuilder, userRoot, name);
        criteriaQuery.orderBy(criteriaBuilder.asc(userRoot.get("lastName"))).select(userRoot).where(predicates.toArray(new Predicate[predicates.size()]));
        return session.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public HashMap<String, Organization> searchAllUsedOrganizations() {
        HashMap<String, Organization> usedOrganizationMap = new HashMap<String, Organization>();
        Session session = getSessionFactory().getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Organization> criteriaQuery = criteriaBuilder.createQuery(Organization.class);
        final Root<User> userRoot = criteriaQuery.from(User.class);
        Join<Organization, User> join = userRoot.join("organization", JoinType.INNER);
        criteriaQuery.multiselect(join).distinct(true);

        List<Organization> usedOrganization = session.createQuery(criteriaQuery).getResultList();

        for (Organization org : usedOrganization) {
            usedOrganizationMap.put(Long.toString(org.getId()), org);
        }
        return usedOrganizationMap;
    }

    @Override
    public List<User> searchNonAdminUsers(String name, int startIndex, int numResults) {
        Session session = getSessionFactory().getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = criteriaQuery.from(User.class);
        criteriaQuery.orderBy(criteriaBuilder.asc(userRoot.get("lastName")));
        List<Predicate> predicates = createSearchCriteriaNonAdminUser(criteriaBuilder, userRoot, name);
        TypedQuery<User> typedQuery = session.createQuery(criteriaQuery.select(userRoot).where(predicates.toArray(new Predicate[predicates.size()])));
        typedQuery.setFirstResult(startIndex);
        if (numResults > 0) {
            typedQuery.setMaxResults(numResults);
        }
        return typedQuery.getResultList();

    }

    @Override
    public List<User> getAllNonAdminActiveUsers() {
        Session session = getSessionFactory().getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = criteriaQuery.from(User.class);
        List<Predicate> predicates = new ArrayList<>();
        Predicate predicate1 = criteriaBuilder.notEqual(userRoot.get("securityRole").get("id"), 1);
        Predicate predicate2 = criteriaBuilder.and(criteriaBuilder.equal(userRoot.get("status").get("statusId"), "1"));
        predicates.add(predicate1);
        predicates.add(predicate2);
        criteriaQuery.orderBy(criteriaBuilder.asc(userRoot.get("lastName")));
        criteriaQuery.select(userRoot).where(predicates.toArray(new Predicate[predicates.size()]));
        return session.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public MatUserDetails getUserDetailsByLoginId(String loginId) {
        Session session = getSessionFactory().getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<MatUserDetails> criteriaQuery = criteriaBuilder.createQuery(MatUserDetails.class);
        Root<MatUserDetails> userRoot = criteriaQuery.from(MatUserDetails.class);
        criteriaQuery.select(userRoot).where(criteriaBuilder.equal(userRoot.get("loginId"), loginId));
        return session.createQuery(criteriaQuery).uniqueResult();
    }

    @Override
    public MatUserDetails getUserDetailsById(String id) {
        return getSessionFactory().getCurrentSession().load(MatUserDetails.class, id);
    }

    @Override
    public Boolean userExists(String emailAddress) {
        Session session = getSessionFactory().getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<User> userRoot = criteriaQuery.from(User.class);
        criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(User.class))).where(criteriaBuilder.like(userRoot.get("emailAddress"), emailAddress));
        return session.createQuery(criteriaQuery).uniqueResult().intValue() > 0;
    }

    @Override
    public boolean findUniqueLoginId(String loginId) {
        Session session = getSessionFactory().getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<User> userRoot = criteriaQuery.from(User.class);
        criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(User.class))).where(criteriaBuilder.like(userRoot.get("loginId"), loginId));
        return session.createQuery(criteriaQuery).uniqueResult() > 0;
    }

    @Override
    public User findByEmail(String email) {
        Session session = getSessionFactory().getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = criteriaQuery.from(User.class);
        criteriaQuery.select(userRoot).where(criteriaBuilder.equal(userRoot.get("emailAddress"), email));
        List<User> results = session.createQuery(criteriaQuery).getResultList();
        if (results.size() > 0) {
            return results.get(0);
        } else {
            return null;
        }
    }

    @Override
    public User findByLoginId(String loginId) {
        Session session = getSessionFactory().getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = criteriaQuery.from(User.class);
        criteriaQuery.select(userRoot).where(criteriaBuilder.equal(userRoot.get("loginId"), loginId));
        List<User> results = session.createQuery(criteriaQuery).getResultList();
        if (results.size() > 0) {
            return results.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void saveUserDetails(MatUserDetails userdetails) {
        Session session = null;
        try {
            session = getSessionFactory().getCurrentSession();
            session.saveOrUpdate(userdetails);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    // US212
    @Override
    public void setUserSignInDate(String userid) {
        User u = find(userid);
        Date current = new Date();
        u.setSignInDate(current);
        save(u);
    }

    // US212
    @Override
    public void setUserSignOutDate(String userid) {
        User u = find(userid);
        Date current = new Date();
        u.setSignOutDate(current);
        save(u);
    }

    @Override
    public String getRandomSecurityQuestion(String userLoginId) {
        final User user = findByLoginId(userLoginId);

        return (null == user) ? getRandomSecurityQuestion() : getRandomSecurityQuestionByUserId(user);
    }

    private String getRandomSecurityQuestionByUserId(User user) {
        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        final CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<UserSecurityQuestion> userSecurityQuestionsRoot = criteriaQuery.from(UserSecurityQuestion.class);
        final Root<SecurityQuestions> securityQuestionsRoot = criteriaQuery.from(SecurityQuestions.class);
        final Root<User> userRoot = criteriaQuery.from(User.class);
        criteriaQuery.select(criteriaBuilder.count(userSecurityQuestionsRoot)).where(criteriaBuilder.and(
                criteriaBuilder.equal(userSecurityQuestionsRoot.get("securityQuestionId"), securityQuestionsRoot.get("questionId")),
                criteriaBuilder.equal(userSecurityQuestionsRoot.get("userId"), userRoot.get("id")),
                criteriaBuilder.equal(userRoot.get("id"), user.getId())));
        final int count = session.createQuery(criteriaQuery).uniqueResult().intValue();
        final CriteriaQuery<UserSecurityQuestion> securityQuestionsQuery = criteriaBuilder.createQuery(UserSecurityQuestion.class);
        userSecurityQuestionsRoot = securityQuestionsQuery.from(UserSecurityQuestion.class);
        securityQuestionsQuery.select(userSecurityQuestionsRoot).where(criteriaBuilder.equal(userSecurityQuestionsRoot.get("userId"), user.getId()));

        return session.createQuery(securityQuestionsQuery).setFirstResult(new Random().nextInt(count)).setMaxResults(1).uniqueResult().getSecurityQuestions().getQuestion();
    }

    /**
     * Gets the random security question.
     *
     * @return the random security question
     */
    public String getRandomSecurityQuestion() {
        Session session = getSessionFactory().getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(SecurityQuestions.class)));
        int count = session.createQuery(criteriaQuery).uniqueResult().intValue();
        CriteriaQuery<SecurityQuestions> securityQuestionsQuery = criteriaBuilder.createQuery(SecurityQuestions.class);
        Root<SecurityQuestions> securityQuestionsRoot = securityQuestionsQuery.from(SecurityQuestions.class);
        securityQuestionsQuery.select(securityQuestionsRoot);
        return session.createQuery(securityQuestionsQuery).setFirstResult(new Random().nextInt(count)).setMaxResults(1).uniqueResult().getQuestion();
    }

    @Override
    public List<User> searchForNonTerminatedUser() {
        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        final CriteriaQuery<User> userCriteriaQuery = criteriaBuilder.createQuery(User.class);
        final Root<User> userRoot = userCriteriaQuery.from(User.class);
        userCriteriaQuery.select(userRoot).where(criteriaBuilder.notEqual(userRoot.get("status").get("statusId"), STATUS_TERMINATED));
        userCriteriaQuery.orderBy(criteriaBuilder.asc(userRoot.get("lastName")));
        return session.createQuery(userCriteriaQuery).getResultList();
    }


    @Override
    public List<User> getAllUsers() {
        Session session = getSessionFactory().getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> userCriteriaQuery = criteriaBuilder.createQuery(User.class);
        final Root<User> userRoot = userCriteriaQuery.from(User.class);
        userCriteriaQuery.select(userRoot);
        return session.createQuery(userCriteriaQuery).getResultList();
    }

    public List<User> getUsersListForSharingMeasureOrLibrary(String userName) {
        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<User> query = cb.createQuery(User.class);
        final Root<User> root = query.from(User.class);

        final Predicate predicate = getPredicateForUsersToShareMeasure(userName, cb, root);

        query.select(root).where(predicate);
        query.orderBy(cb.asc(root.get("lastName")));

        return session.createQuery(query).getResultList();
    }

    @Override
    public boolean findAssociatedHarpId(String harpId) {
        Session session = getSessionFactory().getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = criteriaQuery.from(User.class);

        criteriaQuery.select(userRoot).where(criteriaBuilder.equal(userRoot.get("harpId"), harpId));
        return session.createQuery(criteriaQuery).getResultList().size() == 0 ? false : true;
    }

    private Predicate getPredicateForUsersToShareMeasure(String userName, CriteriaBuilder cb, Root<User> root) {
        final Predicate p1 = cb.and(cb.equal(root.get("securityRole").get("id"), SECURITY_ROLE_USER),
                cb.equal(root.get("status").get("statusId"), STATUS_ACTIVE),
                cb.notEqual(root.get("id"), LoggedInUserUtil.getLoggedInUser()));
        Predicate p2 = null;
        if (StringUtils.isNotBlank(userName)) {
            final String lowerCaseSearchText = userName.toLowerCase();
            p2 = cb.or(cb.like(cb.lower(root.get("firstName")), "%" + lowerCaseSearchText + "%"),
                    cb.like(cb.lower(root.get("lastName")), "%" + lowerCaseSearchText + "%"));
        }

        return (p2 != null) ? cb.and(p1, p2) : p1;
    }

    @Override
    public MatUserDetails getUserDetailsByHarpId(String harpId) {
        List<MatUserDetails> allUserDetailsByHarpId = getAllUserDetailsByHarpId(harpId);
        MatUserDetails foundFirstActive = allUserDetailsByHarpId.stream()
                .filter(MatUserDetails::isActive)
                .findFirst()
                .orElse(null);
        return foundFirstActive;
    }

    @Override
    public List<MatUserDetails> getAllUserDetailsByHarpId(String harpId) {
        Session session = getSessionFactory().getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<MatUserDetails> criteriaQuery = criteriaBuilder.createQuery(MatUserDetails.class);
        final Root<MatUserDetails> root = criteriaQuery.from(MatUserDetails.class);
        Order[] sortBy = {
                criteriaBuilder.asc(root.join("organization").get("organizationName")),
                criteriaBuilder.desc(root.get("id"))
        };
        criteriaQuery
                .orderBy(sortBy)
                .where(criteriaBuilder.equal(root.get("harpId"), harpId));
        return session.createQuery(criteriaQuery).getResultList();
    }

}
