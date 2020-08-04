package mat.model.clause;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;

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

import org.hibernate.annotations.GenericGenerator;

import mat.model.User;

@Entity
@Table(name = "MEASURE")
public class Measure {

    private String id;

    private User owner;

    private String aBBRName;

    private String description;

    private String measureModel;

    private String version;

    private String revisionNumber;

    private String measureScoring;

    private String compositeScoring;

    private Date exportedDate;

    private Set<MeasureShare> shares;

    private Timestamp lockedOutDate;

    private User lockedUser;

    private boolean draft;

    private MeasureSet measureSet;

    private Timestamp finalizedDate;

    private Timestamp valueSetDate;

    private int eMeasureId;

    private boolean isPrivate;

    private String releaseVersion;

    private Boolean patientBased;

    private Timestamp lastModifiedOn;

    private User lastModifiedBy;

    private String qdmVersion;

    private String fhirVersion;

    private Boolean isCompositeMeasure = false;

    private List<ComponentMeasure> componentMeasures;

    private List<MeasureTypeAssociation> measureTypes;

    private List<MeasureDeveloperAssociation> measureDevelopers;

    private MeasureDetails measureDetails;

    private String measureStewardId;

    private String nqfNumber;

    private Timestamp measurementPeriodFrom;

    private Timestamp measurementPeriodTo;

    private List<CQLLibraryHistory> cqlLibraryHistory;

    private String cqlLibraryName;

    private boolean experimental;

    // We don't map it as a Measure object, since it can be potentially invalid,
    // if a source QDM measure is removed after conversion.
    private String sourceMeasureId;

    @Column(name = "VALUE_SET_DATE", length = 19)
    public Timestamp getValueSetDate() {
        return valueSetDate;
    }

    public void setValueSetDate(Timestamp valueSetDate) {
        this.valueSetDate = valueSetDate;
    }

    @Column(name = "DRAFT")
    public boolean isDraft() {
        return draft;
    }

    public void setDraft(boolean draft) {
        this.draft = draft;
    }

    @ManyToOne
    @JoinColumn(name = "MEASURE_SET_ID")
    public MeasureSet getMeasureSet() {
        if (measureSet == null) {
            measureSet = new MeasureSet();
        }
        return measureSet;
    }

    public void setMeasureSet(MeasureSet measureSet) {
        this.measureSet = measureSet;
    }

    @Column(name = "FINALIZED_DATE", length = 19)
    public Timestamp getFinalizedDate() {
        return finalizedDate;
    }

    public void setFinalizedDate(Timestamp finalizedDate) {
        this.finalizedDate = finalizedDate;
    }

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "ID", unique = true, length = 64)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "ABBR_NAME", length = 45)
    public String getaBBRName() {
        return aBBRName;
    }

    public void setaBBRName(String aBBRName) {
        this.aBBRName = aBBRName;
    }

    @Column(name = "DESCRIPTION", length = 2000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "MEASURE_MODEL")
    public String getMeasureModel() {
        return measureModel;
    }

    public void setMeasureModel(String measureModel) {
        this.measureModel = measureModel;
    }

    @Transient
    public boolean isQdmMeasure() {
        return ModelTypeHelper.isQdm(getMeasureModel());
    }

    @Transient
    public boolean isFhirMeasure() {
        return ModelTypeHelper.isFhir(getMeasureModel());
    }

    @Transient
    public String getModelVersion() {
        return isFhirMeasure() ? getFhirVersion() : getQdmVersion();
    }

    @Transient
    public int getMinorVersionInt() {
        int minVersion = 0;
        if ((version != null) && !version.isEmpty()) {
            int decimalIndex = version.indexOf('.');
            if (decimalIndex < 0) {
                minVersion = 0;
            } else {
                minVersion = Integer.valueOf(version.substring(decimalIndex + 1)).intValue();
            }
        }
        return minVersion;
    }

    @Transient
    public String getMinorVersionStr() {
        return getMinorVersionInt() + "";
    }

    @Transient
    public int getMajorVersionInt() {
        int maxVersion = 0;
        if ((version != null) && !version.isEmpty()) {
            int decimalIndex = version.indexOf('.');
            if (decimalIndex < 0) {
                maxVersion = Integer.valueOf(version).intValue();
            } else {
                maxVersion = Integer.valueOf(version.substring(0, decimalIndex)).intValue();
            }
        }
        return maxVersion;
    }

    @Transient
    public String getMajorVersionStr() {
        return getMajorVersionInt() + "";
    }

    @Transient
    public double getVersionNumber() {
        double versionNumber = 0;
        if ((version != null) && !version.isEmpty()) {
            versionNumber = Double.valueOf(version).doubleValue();
        }
        return versionNumber;
    }

    @Column(name = "VERSION")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Column(name = "SCORING", length = 200)
    public String getMeasureScoring() {
        return measureScoring;
    }

    public void setMeasureScoring(String measureScoring) {
        this.measureScoring = measureScoring;
    }

    @Column(name = "COMPOSITE_SCORING", length = 200)
    public String getCompositeScoring() {
        return compositeScoring;
    }

    public void setCompositeScoring(String compositeScoring) {
        this.compositeScoring = compositeScoring;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEASURE_OWNER_ID")
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @OneToMany(mappedBy = "measure", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public Set<MeasureShare> getShares() {
        return shares;
    }

    public void setShares(Set<MeasureShare> shares) {
        this.shares = shares;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXPORT_TS", length = 19)
    public Date getExportedDate() {
        return exportedDate;
    }

    public void setExportedDate(Date exportedDate) {
        this.exportedDate = exportedDate;
    }

    @Column(name = "LOCKED_OUT_DATE", length = 19)
    public Timestamp getLockedOutDate() {
        return lockedOutDate;
    }

    public void setLockedOutDate(Timestamp lockedOutDate) {
        this.lockedOutDate = lockedOutDate;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "LOCKED_USER_ID", referencedColumnName = "USER_ID", insertable = false)
    public User getLockedUser() {
        return lockedUser;
    }

    public void setLockedUser(User lockedUser) {
        this.lockedUser = lockedUser;
    }

    @Column(name = "EMEASURE_ID")
    public int geteMeasureId() {
        return eMeasureId;
    }

    public void seteMeasureId(int eMeasureId) {
        this.eMeasureId = eMeasureId;
    }

    @Column(name = "PRIVATE")
    public boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    @Column(name = "REVISION_NUMBER")
    public String getRevisionNumber() {
        return revisionNumber;
    }

    public void setRevisionNumber(String revisionNumber) {
        this.revisionNumber = revisionNumber;
    }

    @Column(name = "RELEASE_VERSION", length = 50)
    public String getReleaseVersion() {
        return releaseVersion;
    }

    public void setReleaseVersion(String releaseVersion) {
        this.releaseVersion = releaseVersion;
    }

    @Column(name = "PATIENT_BASED")
    public Boolean getPatientBased() {
        return patientBased;
    }

    public void setPatientBased(Boolean patientBased) {
        this.patientBased = patientBased;
    }

    @Column(name = "LAST_MODIFIED_ON", length = 19)
    public Timestamp getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Timestamp lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LAST_MODIFIED_BY")
    public User getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(User lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Column(name = "QDM_VERSION", length = 45)
    public String getQdmVersion() {
        return qdmVersion;
    }

    public void setQdmVersion(String qdmVersion) {
        this.qdmVersion = qdmVersion;
    }

    @Column(name = "FHIR_VERSION", length = 45)
    public String getFhirVersion() {
        return fhirVersion;
    }

    public void setFhirVersion(String fhirVersion) {
        this.fhirVersion = fhirVersion;
    }

    @Column(name = "IS_COMPOSITE_MEASURE")
    public Boolean getIsCompositeMeasure() {
        return isCompositeMeasure;
    }

    public void setIsCompositeMeasure(Boolean isCompositeMeasure) {
        this.isCompositeMeasure = isCompositeMeasure;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "compositeMeasure", cascade = CascadeType.ALL)
    public List<ComponentMeasure> getComponentMeasures() {
        return componentMeasures;
    }

    public void setComponentMeasures(List<ComponentMeasure> componentMeasures) {
        this.componentMeasures = componentMeasures;
    }

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "measure", cascade = CascadeType.ALL)
    public MeasureDetails getMeasureDetails() {
        return measureDetails;
    }

    public void setMeasureDetails(MeasureDetails measureDetails) {
        if (measureDetails != null && measureDetails.getMeasure() == null) {
            measureDetails.setMeasure(this);
        }

        this.measureDetails = measureDetails;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "measure", cascade = CascadeType.ALL)
    public List<MeasureTypeAssociation> getMeasureTypes() {
        return measureTypes;
    }

    public void setMeasureTypes(List<MeasureTypeAssociation> measureTypes) {
        this.measureTypes = measureTypes;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "measure", cascade = CascadeType.ALL)
    public List<MeasureDeveloperAssociation> getMeasureDevelopers() {
        return measureDevelopers;
    }

    public void setMeasureDevelopers(List<MeasureDeveloperAssociation> measureDevelopers) {
        this.measureDevelopers = measureDevelopers;
    }

    @Column(name = "MEASURE_STEWARD_ID")
    public String getMeasureStewardId() {
        return measureStewardId;
    }

    public void setMeasureStewardId(String measureStewardId) {
        this.measureStewardId = measureStewardId;
    }

    @Column(name = "NQF_NUMBER")
    public String getNqfNumber() {
        return nqfNumber;
    }

    public void setNqfNumber(String nqfNumber) {
        this.nqfNumber = nqfNumber;
    }

    @Column(name = "MEASUREMENT_PERIOD_FROM")
    public Timestamp getMeasurementPeriodFrom() {
        return measurementPeriodFrom;
    }

    public void setMeasurementPeriodFrom(Timestamp measurementPeriodFrom) {
        this.measurementPeriodFrom = measurementPeriodFrom;
    }

    @Column(name = "MEASUREMENT_PERIOD_TO")
    public Timestamp getMeasurementPeriodTo() {
        return measurementPeriodTo;
    }

    public void setMeasurementPeriodTo(Timestamp measurementPeriodTo) {
        this.measurementPeriodTo = measurementPeriodTo;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "measure", cascade = CascadeType.ALL)
    public List<CQLLibraryHistory> getCqlLibraryHistory() {
        return cqlLibraryHistory;
    }

    public void setCqlLibraryHistory(List<CQLLibraryHistory> cqlLibraryHistory) {
        this.cqlLibraryHistory = cqlLibraryHistory;
    }

    @Column(name = "CQL_NAME")
    public String getCqlLibraryName() {
        return cqlLibraryName;
    }

    public void setCqlLibraryName(String cqlLibraryName) {
        this.cqlLibraryName = cqlLibraryName;
    }

    @Column(name = "SOURCE_MEASURE_ID")
    public String getSourceMeasureId() {
        return sourceMeasureId;
    }

    public void setSourceMeasureId(String sourceMeasureId) {
        this.sourceMeasureId = sourceMeasureId;
    }

    @Column(name = "EXPERIMENTAL")
    public boolean isExperimental() {
        return experimental;
    }

    public void setExperimental(boolean experimental) {
        this.experimental = experimental;
    }
}
