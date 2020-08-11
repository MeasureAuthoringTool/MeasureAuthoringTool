package mat.server.service.impl;

import java.math.BigDecimal;
import java.util.Objects;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mat.dao.UserDAO;
import mat.dao.clause.CQLLibraryDAO;
import mat.dao.clause.MeasureDAO;
import mat.model.SecurityRole;
import mat.model.clause.CQLLibrary;
import mat.model.clause.Measure;
import mat.model.clause.MeasureShareDTO;
import mat.model.clause.ModelTypeHelper;
import mat.model.clause.ShareLevel;
import mat.model.cql.CQLLibraryShareDTO;
import mat.server.LoggedInUserUtil;
import mat.server.service.FeatureFlagService;

@Service
public class MatContextServiceUtil implements InitializingBean {

    private static BigDecimal QDM_VER_BEFORE_CONVERSION = new BigDecimal("5.5");
    private static BigDecimal MAT_VER_BEFORE_CONVERSION = new BigDecimal("5.8");

    private static MatContextServiceUtil instance;

    @Autowired
    private FeatureFlagService featureFlagService;

    @Override
    public void afterPropertiesSet() {
        instance = this;
    }

    /**
     * Gets the.
     *
     * @return the mat context service util
     */
    public static MatContextServiceUtil get() {
        return instance;
    }


    /**
     * Checks if is current measure editable.
     *
     * @param measureDAO the measure DAO
     * @param measureId  the measure id
     * @return true, if is current measure editable
     */
    public boolean isCurrentMeasureEditable(MeasureDAO measureDAO, String measureId) {
        return isCurrentMeasureEditable(measureDAO, measureId, true);
    }

    /**
     * Checks if is current measure editable.
     *
     * @param measureDAO    the measure dao
     * @param measureId     the measure id
     * @param checkForDraft the check for draft
     * @return true, if is current measure editable
     */
    public boolean isCurrentMeasureEditable(MeasureDAO measureDAO, String measureId, boolean checkForDraft) {

        String currentUserId = LoggedInUserUtil.getLoggedInUser();
        Measure measure = measureDAO.find(measureId);
        ShareLevel shareLevel = measureDAO.findShareLevelForUser(currentUserId, measure.getMeasureSet().getId());
        return isCurrentMeasureEditable(measure, currentUserId, shareLevel == null ? null : shareLevel.getId(), checkForDraft);
    }

    public boolean isCurrentMeasureEditable(Measure measure, String currentUserId, String shareLevelId, boolean checkForDraft) {
        boolean isEditable = false;

        String userRole = LoggedInUserUtil.getLoggedInUserRole();
        boolean isSuperUser = SecurityRole.SUPER_USER_ROLE.equals(userRole);
        boolean isOwner = currentUserId.equals(measure.getOwner() == null ? null : measure.getOwner().getId());
        boolean isSharedToEdit = ShareLevel.MODIFY_ID.equals(shareLevelId);
        isEditable = (isOwner || isSuperUser || isSharedToEdit);

        if (checkForDraft) {
            isEditable = isEditable && measure.isDraft();
        }

        return isEditable;
    }

    public boolean isCurrentMeasureViewable(Measure measure) {
        String currentUserId = LoggedInUserUtil.getLoggedInUser();
        boolean isOwner = currentUserId.equals(measure.getOwner() == null ? null : measure.getOwner().getId());
        return !measure.getIsPrivate() || isOwner;
    }

    /**
     * Checks if is current measure is draftable.
     *
     * @param measureDAO the measure dao
     * @param userDAO    the user DAO
     * @param measureId  the measure id
     * @return true, if is current measure editable
     */
    public boolean isCurrentMeasureDraftable(MeasureDAO measureDAO,
                                             UserDAO userDAO, String measureId) {

        return isCurrentMeasureEditable(measureDAO, measureId, false);
    }

    /**
     * Checks if is current measure is clonable.
     *
     * @param measureDAO the measure dao
     * @param measureId  the measure id
     * @return true, if is current measure clonable
     */
    public boolean isCurrentMeasureClonable(MeasureDAO measureDAO,
                                            String measureId) {

        Measure measure = measureDAO.find(measureId);
        String currentUserId = LoggedInUserUtil.getLoggedInUser();
        String userRole = LoggedInUserUtil.getLoggedInUserRole();
        boolean isSuperUser = SecurityRole.SUPER_USER_ROLE.equals(userRole);
        MeasureShareDTO dto = measureDAO.extractDTOFromMeasure(measure);
        boolean isOwner = currentUserId.equals(dto.getOwnerUserId());
        boolean isComposite = measure.getIsCompositeMeasure();

        boolean isClonable = (isOwner || isSuperUser) && !isComposite;
        return isClonable;
    }

    /**
     * Checks if is current measure editable.
     *
     * @param cqlLibraryDAO the cql library DAO
     * @param libraryId     the measure id
     * @param checkForDraft the check for draft
     * @return true, if is current measure editable
     */
    public boolean isCurrentCQLLibraryEditable(CQLLibraryDAO cqlLibraryDAO,
                                               String libraryId, boolean checkForDraft) {

        CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
        boolean isEditable = false;

        String currentUserId = LoggedInUserUtil.getLoggedInUser();
        String userRole = LoggedInUserUtil.getLoggedInUserRole();
        boolean isSuperUser = SecurityRole.SUPER_USER_ROLE.equals(userRole);
        CQLLibraryShareDTO dto = cqlLibraryDAO.extractDTOFromCQLLibrary(cqlLibrary);
        boolean isOwner = currentUserId.equals(dto.getOwnerUserId());
        ShareLevel shareLevel = cqlLibraryDAO.findShareLevelForUser(libraryId,
                currentUserId, dto.getCqlLibrarySetId());
        boolean isSharedToEdit = false;
        if (shareLevel != null) {
            isSharedToEdit = ShareLevel.MODIFY_ID.equals(shareLevel.getId());
        }
        isEditable = (isOwner || isSuperUser || isSharedToEdit);

        if (checkForDraft) {
            isEditable = isEditable && dto.isDraft();
        }

        return isEditable;
    }

    /**
     * Checks if is current CQL library editable.
     *
     * @param cqlLibraryDAO the cql library DAO
     * @param libraryId     the measure id
     * @return true, if is current CQL library editable
     */
    public boolean isCurrentCQLLibraryEditable(CQLLibraryDAO cqlLibraryDAO,
                                               String libraryId) {
        return isCurrentCQLLibraryEditable(cqlLibraryDAO, libraryId, true);
    }

    /**
     * Checks if is current CQL library draftable.
     *
     * @param cqlLibraryDAO the cql library DAO
     * @param libraryId     the measure id
     * @return true, if is current CQL library draftable
     */
    public boolean isCurrentCQLLibraryDraftable(CQLLibraryDAO cqlLibraryDAO,
                                                String libraryId) {
        return isCurrentCQLLibraryEditable(cqlLibraryDAO, libraryId, false);
    }

    public boolean isValidatable(Measure measure) {
        return measure.isFhirMeasure() && measure.isDraft() || isQdmMeasureValidatable(measure);
    }

    private boolean isQdmMeasureValidatable(Measure measure) {
        return !measure.isDraft() &&
                measure.isQdmMeasure() &&
                isVersionEligibleForFhirValidation(measure.getQdmVersion(), measure.getReleaseVersion()) &&
                BooleanUtils.isNotTrue(measure.getIsCompositeMeasure());
    }

    public boolean isMeasureConvertible(Measure measure) {
        // 1.1 Pre-CQL measures cannot be converted.
        // 1.2 The button is disabled for draft QDM-CQL measures and Fhir Measures
        // 1.3 Pre-QDM measures cannot be converted. The button is disabled if the QDM version is before 5.5 or the MAT version is before 5.8.
        // Should be available for the owner or a super user
        String ownerId = measure.getOwner() == null ? null : measure.getOwner().getId();
        return isConvertible(measure.getMeasureModel(), measure.isDraft(), measure.getQdmVersion(), measure.getReleaseVersion(), ownerId) && BooleanUtils.isNotTrue(measure.getIsCompositeMeasure());
    }

    public boolean isConvertible(String modelType, boolean isDraft, String qdmVersion, String releaseVersion, String ownerId) {
        return isModelTypeEligibleForConversion(modelType) && !isDraft && isVersionEligibleForFhirValidation(qdmVersion, releaseVersion) && canUserConvert(ownerId);
    }

    private boolean canUserConvert(String ownerId) {
        String currentUserId = LoggedInUserUtil.getLoggedInUser();
        String userRole = LoggedInUserUtil.getLoggedInUserRole();
        boolean isSuperUser = SecurityRole.SUPER_USER_ROLE.equals(userRole);
        boolean isOwner = ownerId != null && Objects.equals(currentUserId, ownerId);
        return isOwner || isSuperUser;
    }

    private boolean isVersionEligibleForFhirValidation(String qdmVersion, String matVersion) {
        return QDM_VER_BEFORE_CONVERSION.compareTo(asDecimalVersion(qdmVersion)) <= 0
                && MAT_VER_BEFORE_CONVERSION.compareTo(asDecimalVersion(matVersion)) <= 0;
    }

    private boolean isModelTypeEligibleForConversion(String modelType) {
        return ModelTypeHelper.isQdm(modelType);
    }

    private BigDecimal asDecimalVersion(String version) {
        if (version == null || version.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(version.trim().replaceAll("v", ""));
        } catch (NullPointerException | IllegalArgumentException e) {
            return BigDecimal.ZERO;
        }
    }

    public boolean isCqlLibraryConvertible(CQLLibrary cqlLibrary) {
        String ownerId = cqlLibrary.getOwnerId() == null ? null : cqlLibrary.getOwnerId().getId();
        return isConvertible(cqlLibrary.getLibraryModelType(), cqlLibrary.isDraft(), cqlLibrary.getQdmVersion(), cqlLibrary.getReleaseVersion(), ownerId);
    }

    public boolean isCqlLibraryConvertible(CQLLibraryShareDTO cqlLibrary) {
        return isConvertible(cqlLibrary.getLibraryModelType(), cqlLibrary.isDraft(), cqlLibrary.getQdmVersion(), cqlLibrary.getReleaseVersion(), cqlLibrary.getOwnerUserId());
    }

}
