package mat.dao;

import mat.model.User;
import mat.model.UserBonnieAccessInfo;

public interface UserBonnieAccessInfoDAO extends IDAO<UserBonnieAccessInfo, String> {
	
	/**
	 * User bonnie Access exists. Check with UserId
	 * 
	 * @param userid
	 *            the userid
	 * @return the boolean
	 */
	public Boolean userExists(String userId);
	
	/**
	 * Gets the user bonnie access info.
	 * 
	 * @param userBonnieAccessId
	 *            the user bonnie access id
	 * @return the userBonnieAccessInfo
	 */
	public UserBonnieAccessInfo getUserBonnieAccess(String userBonnieAccessId);
	
	/**
	 * Save user bonnie access details.
	 * 
	 * @param userBonnieAccessInfo
	 *            the user bonnie access details
	 */
	public void saveUserBonnieAccessDetails(UserBonnieAccessInfo userBonnieAccessInfo);
	
	/**
	 * Sets the users accessToken.
	 * 
	 * @param accessToken
	 * 			the new accessToken
	 * @param userBonnieAccessInfoId
	 *            the user Bonnie Access Info Id to set
	 */
	public void setAccessToken(String accessToken, String userBonnieAccessInfoId);
	
	/**
	 * Sets the users refreshToken.
	 * 
	 * @param refreshToken
	 * 			the new refreshToken
	 * @param userid
	 *            the user to set
	 */
	public void setRefreshToken(String refreshToken, String userBonnieAccessInfoId);
	
	/**
	 * Find by User bonnie access id.
	 * 
	 * @param userId
	 *            the user id
	 * @return the user bonnie access id
	 */
	public UserBonnieAccessInfo findByUserId(String userID);

}
