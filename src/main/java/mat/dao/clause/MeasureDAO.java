package mat.dao.clause;

import java.util.List;

import mat.dao.IDAO;
import mat.model.User;
import mat.model.clause.Measure;
import mat.model.clause.MeasureShare;
import mat.model.clause.MeasureShareDTO;
import mat.model.clause.ShareLevel;
import mat.shared.MeasureSearchModel;

public interface MeasureDAO extends IDAO<Measure, String> {
	/**
	 * Count users for measure share.
	 * 
	 * @return the int
	 */
	int countUsersForMeasureShare();
	
	/**
	 * Find by owner id.
	 * 
	 * @param measureOwnerId
	 *            the measure owner id
	 * @return the java.util. list
	 */
	java.util.List<Measure> findByOwnerId(String measureOwnerId);

	Measure getMeasureByMeasureId(String measureId);
	
	/**
	 * Find max of min version.
	 * 
	 * @param measureSetId
	 *            the measure set id
	 * @param version
	 *            the version
	 * @return the string
	 */
	String findMaxOfMinVersion(String measureSetId, String version);
	
	/**
	 * Find max version.
	 * 
	 * @param measureSetId
	 *            the measure set id
	 * @return the string
	 */
	String findMaxVersion(String measureSetId);
	
	/**
	 * Gets the all measures in set.
	 * 
	 * @param ms
	 *            the ms
	 * @return the all measures in set
	 */
	List<Measure> getAllMeasuresInSet(List<Measure> ms);
	
	/**
	 * Gets the max e measure id.
	 * 
	 * @return the max e measure id
	 */
	int getMaxEMeasureId();
	
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
	List<MeasureShareDTO> getMeasureShareInfoForMeasure(String userName, String measureId, int startIndex, int pageSize);
	
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
	boolean isMeasureLocked(String id);
	
	/**
	 * Reset lock date.
	 * 
	 * @param m
	 *            the m
	 */
	void resetLockDate(Measure m);
	
	/**
	 * Saveand return max e measure id.
	 * 
	 * @param measure
	 *            the measure
	 * @return the int
	 */
	int saveandReturnMaxEMeasureId(Measure measure);
	
	/**
	 * Save measure.
	 * 
	 * @param measure
	 *            the measure
	 */
	void saveMeasure(Measure measure);
	/**
	 * Update private column in measure.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param isPrivate
	 *            the is private
	 */
	void updatePrivateColumnInMeasure(String measureId, boolean isPrivate);
	
	
	/**
	 * Gets the measure.
	 *
	 * @param measureId the measure id
	 * @return the measure
	 */
	boolean getMeasure(String measureId);
	
	List<Measure> getMeasureListForMeasureOwner(User user);

	
	/**
	 * Gets the MeasureShareDTO.
	 *
	 * @param measure the measure
	 * @return the MeasureShareDTO.
	 */
	MeasureShareDTO extractDTOFromMeasure(Measure measure);

	ShareLevel findShareLevelForUser(String measureId, String userID, String measureSetId);

	List<MeasureShareDTO> getComponentMeasureShareInfoForUserWithFilter(MeasureSearchModel measureSearchModel, User user);
	
	String getMeasureNameIfDraftAlreadyExists(String measureSetId);
}
