package mat.model;

import mat.model.clause.CQLLibraryHistory;
import mat.model.clause.MeasureShare;
import mat.model.cql.CQLLibraryShare;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "USER", uniqueConstraints = @UniqueConstraint(columnNames = "LOGIN_ID"))
public class User {

    private String id;

    private String firstName;

    private String middleInit;

    private String lastName;

    private String emailAddress;

    private String phoneNumber;

    private String title;

    private Date activationDate;

    private Date terminationDate;

    private Timestamp signInDate;

    private Timestamp signOutDate;

    private Timestamp lockedOutDate;

    private Organization organization;

    private UserPassword password;

    private AuditLog auditLog;

    private Status status;

    private SecurityRole securityRole;

    private UserBonnieAccessInfo userBonnieAccessInfo;

    private List<UserSecurityQuestion> userSecurityQuestions;

    private Set<MeasureShare> measureShares;

    private Set<MeasureShare> ownedMeasureShares;

    private Set<CQLLibraryShare> cqlLibraryShares;

    private Set<CQLLibraryShare> ownedCQLLibraryShares;

    private String loginId;

    private String harpId;

    private String sessionId;

    private Set<UserPasswordHistory> passwordHistory;

    private UserPreference userPreference;

    private List<CQLLibraryHistory> cqlLibraryHistory;

    private boolean fhirFlag;


    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "USER_ID", unique = true, nullable = false, length = 40)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "FIRST_NAME", nullable = false, length = 100)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "MIDDLE_INITIAL", length = 45)
    public String getMiddleInit() {
        return middleInit;
    }


    public void setMiddleInit(String middleInit) {
        this.middleInit = middleInit;
    }


    @Column(name = "LAST_NAME", nullable = false, length = 100)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    @Column(name = "EMAIL_ADDRESS", nullable = false, length = 254)
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Column(name = "PHONE_NO", nullable = false, length = 45)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Column(name = "TITLE", length = 45)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "ACTIVATION_DATE", length = 10)
    public Date getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(Date activationDate) {
        this.activationDate = activationDate;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "TERMINATION_DATE", length = 10)
    public Date getTerminationDate() {
        return terminationDate;
    }

    public void setTerminationDate(Date terminationDate) {
        this.terminationDate = terminationDate;
    }

    @Transient
    public String getOrganizationName() {
        String orgName = "";
        if (this.organization != null) {
            orgName = this.organization.getOrganizationName();
        }
        return orgName;
    }

    @Transient
    public String getOrgOID() {
        String orgOID = "";
        if (this.organization != null) {
            orgOID = this.organization.getOrganizationOID();
        }
        return orgOID;
    }

    @Transient
    public String getOrganizationId() {
        String orgId = "";
        if (this.organization != null) {
            orgId = String.valueOf(this.organization.getId());
        }
        return orgId;
    }

    @Transient
    public String getRootOID() {
        return "";
    }

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    public UserPassword getPassword() {
        return password;
    }

    public void setPassword(UserPassword password) {
        if (password != null && password.getUser() == null) {
            password.setUser(this);
        }
        this.password = password;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "AUDIT_ID", nullable = false)
    public AuditLog getAuditLog() {
        if (auditLog == null) {
            auditLog = new AuditLog();
        }
        return auditLog;
    }

    public void setAuditLog(AuditLog auditLog) {
        this.auditLog = auditLog;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STATUS_ID", nullable = false)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SECURITY_ROLE_ID", nullable = false)
    public SecurityRole getSecurityRole() {
        return securityRole;
    }

    public void setSecurityRole(SecurityRole securityRole) {
        this.securityRole = securityRole;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userId")
    public List<UserSecurityQuestion> getUserSecurityQuestions() {
        return userSecurityQuestions;
    }

    public void setUserSecurityQuestions(List<UserSecurityQuestion> securityQuestions) {
        this.userSecurityQuestions = securityQuestions;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SIGN_IN_DATE", length = 10)
    public Date getSignInDate() {
        return signInDate;
    }


    public void setSignInDate(Date signInDate) {
        if (signInDate != null) {
            this.signInDate = new Timestamp(signInDate.getTime());
        } else {
            this.signInDate = null;
        }
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SIGN_OUT_DATE", length = 10)
    public Date getSignOutDate() {
        return signOutDate;
    }

    public void setSignOutDate(Date signOutDate) {
        if (signOutDate != null) {
            this.signOutDate = new Timestamp(signOutDate.getTime());
        } else {
            this.signOutDate = null;
        }
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LOCKED_OUT_DATE", length = 10)
    public Date getLockedOutDate() {
        return lockedOutDate;
    }

    public void setLockedOutDate(Date lockedOutDate) {
        if (lockedOutDate != null) {
            this.lockedOutDate = new Timestamp(lockedOutDate.getTime());
        } else {
            this.lockedOutDate = null;
        }
    }

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "user")
    public UserBonnieAccessInfo getUserBonnieAccessInfo() {
        return userBonnieAccessInfo;
    }

    public void setUserBonnieAccessInfo(UserBonnieAccessInfo userBonnieAccessInfo) {
        this.userBonnieAccessInfo = userBonnieAccessInfo;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shareUser", cascade = CascadeType.ALL)
    public Set<MeasureShare> getMeasureShares() {
        return measureShares;
    }


    public void setMeasureShares(Set<MeasureShare> measureShares) {
        this.measureShares = measureShares;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner", cascade = CascadeType.ALL)
    public Set<MeasureShare> getOwnedMeasureShares() {
        return ownedMeasureShares;
    }

    public void setOwnedMeasureShares(Set<MeasureShare> ownedMeasureShares) {
        this.ownedMeasureShares = ownedMeasureShares;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    @Column(name = "LOGIN_ID", unique = true, length = 45)
    public String getLoginId() {
        return loginId;
    }

    @Column(name = "SESSION_ID", length = 64)
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORG_ID", nullable = false)
    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    public Set<UserPasswordHistory> getPasswordHistory() {
        return passwordHistory;
    }

    public void setPasswordHistory(Set<UserPasswordHistory> passwordHistory) {
        this.passwordHistory = passwordHistory;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shareUser")
    public Set<CQLLibraryShare> getCqlLibraryShares() {
        return cqlLibraryShares;
    }

    public void setCqlLibraryShares(Set<CQLLibraryShare> cqlLibraryShares) {
        this.cqlLibraryShares = cqlLibraryShares;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    public Set<CQLLibraryShare> getOwnedCQLLibraryShares() {
        return ownedCQLLibraryShares;
    }

    public void setOwnedCQLLibraryShares(Set<CQLLibraryShare> ownedCQLLibraryShares) {
        this.ownedCQLLibraryShares = ownedCQLLibraryShares;
    }

    @Transient
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    public UserPreference getUserPreference() {
        return userPreference;
    }

    public void setUserPreference(UserPreference userPreference) {
        if (userPreference != null && userPreference.getUser() == null) {
            userPreference.setUser(this);
        }

        this.userPreference = userPreference;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "lastModifiedBy", cascade = CascadeType.ALL)
    public List<CQLLibraryHistory> getCqlLibraryHistory() {
        return cqlLibraryHistory;
    }

    public void setCqlLibraryHistory(List<CQLLibraryHistory> cqlLibraryHistory) {
        this.cqlLibraryHistory = cqlLibraryHistory;
    }

    @Column(name = "HARP_ID", unique = true, length = 45)
    public String getHarpId() {
        return harpId;
    }

    public void setHarpId(String harpId) {
        this.harpId = harpId;
    }

    @Column(name = "FHIR_FLAG")
    public boolean getFhirFlag() {
        return fhirFlag;
    }

    public void setFhirFlag(boolean fhirFlag) {
        this.fhirFlag = fhirFlag;
    }

}
