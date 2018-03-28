package mat.server.util;

/**
 * The Class MATPropertiesService.
 */
public class MATPropertiesService {
	
	
	private static MATPropertiesService instance = new MATPropertiesService();
	
	/** The current release version. */
	private  static String currentReleaseVersion;
	
	/** The qmd version. */
	private  static String qmdVersion;
	
	
	
	public static MATPropertiesService get(){
		return instance;
	}
	
	

	/**
	 * Gets the current release version.
	 *
	 * @return the current release version
	 */
	public String getCurrentReleaseVersion() {
		return currentReleaseVersion;
	}

	/**
	 * Sets the current release version.
	 *
	 * @param currentReleaseVersion the new current release version
	 */
	public void setCurrentReleaseVersion(String releaseVersion) {
		
		currentReleaseVersion = releaseVersion;
		System.out.println("**************************CurrentRelase Version:  "+currentReleaseVersion);
	}

	/**
	 * Gets the qmd version.
	 *
	 * @return the qmd version
	 */
	public String getQmdVersion() {
		return qmdVersion;
	}

	/**
	 * Sets the qmd version.
	 *
	 * @param qmdVersion the new qmd version
	 */
	public void setQmdVersion(String qdm_version) {
		qmdVersion = qdm_version;
	}
	
}
