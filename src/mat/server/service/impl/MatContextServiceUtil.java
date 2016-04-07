package mat.server.service.impl;

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
	public static MatContextServiceUtil get(){
		return instance;
	}
	
	public boolean isCurrentMeasureEditable(MeasureDAO measureDAO, String measureId){

		Measure measure = measureDAO.find(measureId);
		String currentUserId = LoggedInUserUtil.getLoggedInUser();

		String userRole = LoggedInUserUtil.getLoggedInUserRole();
		boolean isSuperUser = SecurityRole.SUPER_USER_ROLE.equals(userRole);

		MeasureShareDTO dto = measureDAO.extractDTOFromMeasure(measure);
		boolean isOwner = currentUserId.equals(dto.getOwnerUserId());
		
		boolean isEditable = (isOwner || isSuperUser || ShareLevel.MODIFY_ID.equals(dto.getShareLevel())) && dto.isDraft();

		return isEditable;

	}
	

}
