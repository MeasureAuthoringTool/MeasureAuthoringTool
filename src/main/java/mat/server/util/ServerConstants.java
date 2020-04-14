package mat.server.util;

import org.apache.commons.lang3.StringUtils;

/**
 * The Class ServerConstants.
 */
public class ServerConstants {
	
	/** The Constant NEW_ACCESS_SUBJECT. */
	public final static String NEW_ACCESS_SUBJECT="Your MAT Account Has Been Created.";
	
	/** The Constant FORGOT_LOGINID_SUBJECT. */
	public final static String FORGOT_LOGINID_SUBJECT="Your MAT User Account Information";
	
	/** The Constant TEMP_PWD_SUBJECT. */
	public final static String TEMP_PWD_SUBJECT ="Your MAT Account";
	
	/** The Constant ACCOUNT_LOCKED_SUBJECT. */
	public final static String ACCOUNT_LOCKED_SUBJECT = "MAT Account Notification";
	
	/** The Constant ACCOUNT_LOCKED_MESSAGE. */
	public final static String ACCOUNT_LOCKED_MESSAGE = "An unsuccessful attempt was made to reset the password of the MAT account associated with this email address. As a result the account has been locked.\r\n\r\n" + 
														 "If you initiated a \"Forgot Password\" request, please contact the MAT Support Desk and we can assist you with resetting your password. The MAT Support Desk contact information may be found on the Contact Us tab of the MAT web site.\r\n\r\n" +
														 "If you did not initiate a \"Forgot Password\" request, please contact the MAT Support Desk as soon as possible. This could be an indicator that an unauthorized individual is attempting to gain access to your account. The MAT Support Desk will work with you to ensure that your account remains under your control.\r\n\r\n" +
														 "This e-mail contains no web links. Please use the URL or bookmark that you have on file to access the MAT web site.";
	
	private final static String PROD_URL = "https://www.emeasuretool.cms.gov/MeasureAuthoringTool/Login.html";
	
	
	
	
	/**
	 * This method will fetch the "ENVIRONMENT" variable set in the Glassfish system properties.
	 * If not found then it will return blank String, else the value of "ENVIRONMENT" variable
	 * enclosed in [].
	 * @return
	 */
	public static final String getEnvName(){
		String envName = System.getProperty("ENVIRONMENT");
		if(envName != null && envName.trim().length() > 0){
			return " [" + envName + "]";
		}
		return "";
	}
	
	public static final String getEnvURL() {
		String envURL = System.getProperty("ENVIRONMENTURL");
		if(!StringUtils.isBlank(envURL)){
			return envURL;
		}
		return PROD_URL;
	}

	public static final String getHarpUrl() {
		String harpUrl = System.getProperty("HARP_URL");
		if(!StringUtils.isBlank(harpUrl)){
			return harpUrl;
		}
		return null;
	}
	public static final String getHarpBaseUrl() {
		String harpBaseUrl = System.getProperty("HARP_BASE_URL");
		if(!StringUtils.isBlank(harpBaseUrl)){
			return harpBaseUrl;
		}
		return null;
	}

	public static final String getHarpClientId() {
		String harpClientId = System.getProperty("HARP_CLIENT_ID");
		if(!StringUtils.isBlank(harpClientId)){
			return harpClientId;
		}
		return null;
	}
}
