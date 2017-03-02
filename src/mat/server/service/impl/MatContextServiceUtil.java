package mat.server.service.impl;

import mat.dao.UserDAO;
import mat.dao.clause.MeasureDAO;
import mat.model.SecurityRole;
import mat.model.clause.Measure;
import mat.model.clause.MeasureShareDTO;
import mat.model.clause.ShareLevel;
import mat.server.LoggedInUserUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class MatContextServiceUtil.
 */
public class MatContextServiceUtil {

	/** The instance. */
	private static MatContextServiceUtil instance = new MatContextServiceUtil();

	/**
	 * Gets the.
	 * 
	 * @return the mat context service util
	 */
	public static MatContextServiceUtil get() {
		return instance;
	}

	
	public boolean isCurrentMeasureEditable(MeasureDAO measureDAO,
			String measureId) {
		return isCurrentMeasureEditable(measureDAO, measureId, true);
	}
	
	/**
	 * Checks if is current measure editable.
	 *
	 * @param measureDAO the measure dao
	 * @param measureId the measure id
	 * @return true, if is current measure editable
	 */
	public boolean isCurrentMeasureEditable(MeasureDAO measureDAO,
			String measureId, boolean checkForDraft) {

		Measure measure = measureDAO.find(measureId);
		String currentUserId = LoggedInUserUtil.getLoggedInUser();
		String userRole = LoggedInUserUtil.getLoggedInUserRole();
		boolean isSuperUser = SecurityRole.SUPER_USER_ROLE.equals(userRole);
		MeasureShareDTO dto = measureDAO.extractDTOFromMeasure(measure);
		boolean isOwner = currentUserId.equals(dto.getOwnerUserId());
		ShareLevel shareLevel = measureDAO.findShareLevelForUser(measureId,
				currentUserId, dto.getMeasureSetId());
		boolean isSharedToEdit = false;
		if (shareLevel != null) {
			isSharedToEdit = ShareLevel.MODIFY_ID.equals(shareLevel.getId());
		}
		boolean isEditable = (isOwner || isSuperUser || isSharedToEdit);
		
		if(checkForDraft){
			isEditable = isEditable && dto.isDraft();
		}
		
		return isEditable;
	}
	
	/**
	 * Checks if is current measure is draftable.
	 *
	 * @param measureDAO the measure dao
	 * @param userDAO 
	 * @param measureId the measure id
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
	 * @param measureId the measure id
	 * @return true, if is current measure editable
	 */
	public boolean isCurrentMeasureClonable(MeasureDAO measureDAO,
			String measureId) {

		Measure measure = measureDAO.find(measureId);
		String currentUserId = LoggedInUserUtil.getLoggedInUser();
		String userRole = LoggedInUserUtil.getLoggedInUserRole();
		boolean isSuperUser = SecurityRole.SUPER_USER_ROLE.equals(userRole);
		MeasureShareDTO dto = measureDAO.extractDTOFromMeasure(measure);
		boolean isOwner = currentUserId.equals(dto.getOwnerUserId());
		
		boolean isClonable = (isOwner || isSuperUser);
		return isClonable;
	}

}
