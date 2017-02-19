package mat.server.service.impl;

import java.util.List;

import mat.dao.UserDAO;
import mat.dao.clause.MeasureDAO;
import mat.model.SecurityRole;
import mat.model.User;
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

	
	/**
	 * Checks if is current measure editable.
	 *
	 * @param measureDAO the measure dao
	 * @param measureId the measure id
	 * @return true, if is current measure editable
	 */
	public boolean isCurrentMeasureEditable(MeasureDAO measureDAO,
			String measureId) {

		Measure measure = measureDAO.find(measureId);
		String currentUserId = LoggedInUserUtil.getLoggedInUser();
		String userRole = LoggedInUserUtil.getLoggedInUserRole();
		boolean isSuperUser = SecurityRole.SUPER_USER_ROLE.equals(userRole);
		MeasureShareDTO dto = measureDAO.extractDTOFromMeasure(measure);
		boolean isOwner = currentUserId.equals(dto.getOwnerUserId());
		ShareLevel shareLevel = measureDAO.findShareLevelForUser(measureId,
				currentUserId);
		boolean isSharedToEdit = false;
		if (shareLevel != null) {
			isSharedToEdit = ShareLevel.MODIFY_ID.equals(shareLevel.getId());
		}
		boolean isEditable = (isOwner || isSuperUser || isSharedToEdit)
				&& dto.isDraft();
		return isEditable;
	}
	
	/**
	 * Checks if is current measure is clonable/draftable.
	 *
	 * @param measureDAO the measure dao
	 * @param userDAO 
	 * @param measureId the measure id
	 * @return true, if is current measure editable
	 */
	public boolean isCurrentMeasureClonable(MeasureDAO measureDAO,
			UserDAO userDAO, String measureId) {
		
		boolean isClonable = false;
		String currentUserId = LoggedInUserUtil.getLoggedInUser();
		User user = userDAO.find(currentUserId);
		List<MeasureShareDTO> measureDTO = measureDAO.getMeasuresForDraft("", user);
		
		for(MeasureShareDTO measureShareDTO: measureDTO){
			if(measureShareDTO.getMeasureId().equals(measureId)){
				isClonable = true;
				break;
			}
		}
		
		return isClonable;
	}

}
