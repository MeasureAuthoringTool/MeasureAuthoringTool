package mat.dao.clause;

import java.util.List;

import mat.dao.IDAO;
import mat.model.User;
import mat.model.clause.Measure;
import mat.model.clause.MeasureExport;
import mat.model.clause.MeasureShare;
import mat.model.clause.MeasureShareDTO;
import mat.model.clause.ShareLevel;
import mat.shared.MeasureSearchModel;

public interface MeasureDAO extends IDAO<Measure, String> {
	
	/**
	 * Count measure share info for user.
	 * 
	 * @param filter
	 *            the filter
	 * @param user
	 *            the user
	 * @return the int
	 */
	int countMeasureShareInfoForUser(int filter, User user);
	
	/**
	 * Count measure share info for user.
	 * 
	 * @param searchText
	 *            the search text
	 * @param user
	 *            the user
	 * @return the int
	 */
	public int countMeasureShareInfoForUser(String searchText, User user);
	
	/**
	 * Count measure share info for user.
	 * 
	 * @param user
	 *            the user
	 * @return the int
	 */
	public int countMeasureShareInfoForUser(User user);
	
	/**
	 * Count users for measure share.
	 * 
	 * @return the int
	 */
	public int countUsersForMeasureShare();
	
	/**
	 * Find by owner id.
	 * 
	 * @param measureOwnerId
	 *            the measure owner id
	 * @return the java.util. list
	 */
	public java.util.List<Measure> findByOwnerId(String measureOwnerId);
	
	/**
	 * Find max of min version.
	 * 
	 * @param measureSetId
	 *            the measure set id
	 * @param version
	 *            the version
	 * @return the string
	 */
	public String findMaxOfMinVersion(String measureSetId, String version);
	
	/**
	 * Find max version.
	 * 
	 * @param measureSetId
	 *            the measure set id
	 * @return the string
	 */
	public String findMaxVersion(String measureSetId);
	
	/**
	 * Gets the all measures in set.
	 * 
	 * @param ms
	 *            the ms
	 * @return the all measures in set
	 */
	public List<Measure> getAllMeasuresInSet(List<Measure> ms);
	
	/**
	 * Gets the max e measure id.
	 * 
	 * @return the max e measure id
	 */
	public int getMaxEMeasureId();
	
	
	/**
	 * Gets the measures for draft.
	 * 
	 * @param user
	 *            the user
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @return the measures for draft
	 */
	public List<MeasureShareDTO> getMeasuresForDraft(User user, int startIndex, int pageSize);
	
	/**
	 * Gets the measure share for measure.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the measure share for measure
	 */
	List<MeasureShare> getMeasureShareForMeasure(String measureId);
	
	/**
	 * Gets the measure share info for measure.
	 *
	 * @param userName
	 *            the user name  
	 * @param measureId
	 *            the measure id
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @return the measure share info for measure
	 */
	public List<MeasureShareDTO> getMeasureShareInfoForMeasure(String userName, String measureId, int startIndex, int pageSize);
	
	/**
	 * Gets the measure share info for measure and user.
	 *
	 * @param user the user
	 * @param measureId the measure id
	 * @return the measure share info for measure and user
	 */
	List<MeasureShareDTO> getMeasureShareInfoForMeasureAndUser(String user, String measureId);
	
	/**
	 * Gets the measure share info for user.
	 * 
	 * @param searchText
	 *            the search text
	 * @param user
	 *            the user
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @return the measure share info for user
	 */
	public List<MeasureShareDTO> getMeasureShareInfoForUser(String searchText, User user, int startIndex, int pageSize);
	
	/**
	 * Gets the measure share info for user.
	 * 
	 * @param user
	 *            the user
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @return the measure share info for user
	 */
	public List<MeasureShareDTO> getMeasureShareInfoForUser(User user, int startIndex, int pageSize);
	
	/**
	 * Gets the measure share info for user with filter.
	 * 
	 * @param searchText
	 *            the search text
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @param filter
	 *            the filter
	 * @return the measure share info for user with filter
	 */
	List<MeasureShareDTO> getMeasureShareInfoForUserWithFilter(String searchText,
			int startIndex, int pageSize, int filter);
	
	/**
	 * Gets the measure share info for user with filter.
	 * 
	 * @param advancesSearchModel
	 * 		which represents the model of the selections the user selected to do an advance search
	 * @param user
	 * 		the current logged in user
	 * @return the measure share info for user with filter
	 */
	List<MeasureShareDTO> getMeasureShareInfoForUserWithFilter(MeasureSearchModel advancedSearchModel, User user);
	
	/**
	 * Checks if is measure locked.
	 * 
	 * @param id
	 *            the id
	 * @return true, if is measure locked
	 */
	public boolean isMeasureLocked(String id);
	
	/**
	 * Reset lock date.
	 * 
	 * @param m
	 *            the m
	 */
	public void resetLockDate(Measure m);
	
	/**
	 * Saveand return max e measure id.
	 * 
	 * @param measure
	 *            the measure
	 * @return the int
	 */
	public int saveandReturnMaxEMeasureId(Measure measure);
	
	/**
	 * Save measure.
	 * 
	 * @param measure
	 *            the measure
	 */
	public void saveMeasure(Measure measure);
	/**
	 * Update private column in measure.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param isPrivate
	 *            the is private
	 */
	public void updatePrivateColumnInMeasure(String measureId, boolean isPrivate);
	
	
	/**
	 * Gets the measure.
	 *
	 * @param measureId the measure id
	 * @return the measure
	 */
	public boolean getMeasure(String measureId);
	
	List<Measure> getMeasureListForMeasureOwner(User user);

	
	/**
	 * Gets the MeasureShareDTO.
	 *
	 * @param measureId the measure id
	 * @return the MeasureShareDTO.
	 */
	MeasureShareDTO extractDTOFromMeasure(Measure measure);

	/**
	 * Find share level for user.
	 *
	 * @param measureId the measure id
	 * @param userID the user id
	 * @param string 
	 * @return the share level
	 */
	ShareLevel findShareLevelForUser(String measureId, String userID, String measureSetId);

	List<MeasureShareDTO> getComponentMeasureShareInfoForUserWithFilter(MeasureSearchModel measureSearchModel,
			User user);

	MeasureExport getMeasureExportForMeasure(String measureId);
}
