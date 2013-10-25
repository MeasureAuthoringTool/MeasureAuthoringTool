package mat.dao.clause;

import java.util.List;

import mat.dao.IDAO;
import mat.model.User;
import mat.model.clause.Measure;
import mat.model.clause.MeasureShare;
import mat.model.clause.MeasureShareDTO;

/**
 * The Interface MeasureDAO.
 */
public interface MeasureDAO extends IDAO<Measure, String> {
	
	/**
	 * Save measure.
	 * 
	 * @param measure
	 *            the measure
	 */
	public void saveMeasure(Measure measure);
	
	/**
	 * Gets the measure share info for measure.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @return the measure share info for measure
	 */
	public List<MeasureShareDTO> getMeasureShareInfoForMeasure(String measureId, int startIndex, int pageSize);
	
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
	 * Gets the measures for version.
	 * 
	 * @param user
	 *            the user
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @return the measures for version
	 */
	public List<MeasureShareDTO> getMeasuresForVersion(User user, int startIndex, int pageSize);
	
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
	 * Count measure share info for user.
	 * 
	 * @param user
	 *            the user
	 * @return the int
	 */
	public int countMeasureShareInfoForUser(User user);
	
	/**
	 * Count measure for version.
	 * 
	 * @param user
	 *            the user
	 * @return the int
	 */
	public int countMeasureForVersion(User user);
	
	/**
	 * Count measure for draft.
	 * 
	 * @param user
	 *            the user
	 * @return the int
	 */
	public int countMeasureForDraft(User user);
	
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
	 * Reset lock date.
	 * 
	 * @param m
	 *            the m
	 */
	public void resetLockDate(Measure m);
	
	/**
	 * Find max version.
	 * 
	 * @param measureSetId
	 *            the measure set id
	 * @return the string
	 */
	public String findMaxVersion(String measureSetId);
	
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
	 * Checks if is measure locked.
	 * 
	 * @param id
	 *            the id
	 * @return true, if is measure locked
	 */
	public boolean isMeasureLocked(String id);
	
	/**
	 * Gets the max e measure id.
	 * 
	 * @return the max e measure id
	 */
	public int getMaxEMeasureId();
	
	/**
	 * Saveand return max e measure id.
	 * 
	 * @param measure
	 *            the measure
	 * @return the int
	 */
	public int saveandReturnMaxEMeasureId(Measure measure);
	
	/**
	 * Gets the all measures in set.
	 * 
	 * @param ms
	 *            the ms
	 * @return the all measures in set
	 */
	public List<Measure> getAllMeasuresInSet(List<Measure> ms);
	
	/**
	 * Gets the measure share for measure.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the measure share for measure
	 */
	List<MeasureShare> getMeasureShareForMeasure(String measureId);
	
	/**
	 * Gets the measure share info for user with filter.
	 * 
	 * @param searchText
	 *            the search text
	 * @param user
	 *            the user
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @param filter
	 *            the filter
	 * @return the measure share info for user with filter
	 */
	List<MeasureShareDTO> getMeasureShareInfoForUserWithFilter(String searchText, User user,
			int startIndex, int pageSize, int filter);
	
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
	 * Update private column in measure.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param isPrivate
	 *            the is private
	 */
	public void updatePrivateColumnInMeasure(String measureId, boolean isPrivate);
}
