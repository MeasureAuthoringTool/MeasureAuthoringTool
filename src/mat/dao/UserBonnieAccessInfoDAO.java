package mat.dao;

import mat.model.UserBonnieAccessInfo;

public interface UserBonnieAccessInfoDAO extends IDAO<UserBonnieAccessInfo, String> {
	
	
	/**
	 * Gets the user bonnie access info.
	 * 
	 * @param userBonnieAccessId
	 *            the user bonnie access id
	 * @return the userBonnieAccessInfo
	 */
	public UserBonnieAccessInfo getUserBonnieAccessInfo(String userBonnieAccessId);

	
	/**
	 * Find by User bonnie access id.
	 * 
	 * @param userId
	 *            the user id
	 * @return the user bonnie access id
	 */
	public UserBonnieAccessInfo findByUserId(String userID);
	
	/**
	 * Saves or updates the user bonnie access info object in the database.
	 * 
	 * @param userBonnieAccessInfo
	 * 				the object to save or update
	 */
	public void saveOrUpdate(UserBonnieAccessInfo userBonnieAccessInfo);

}
