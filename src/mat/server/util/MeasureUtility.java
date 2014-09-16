/**
 * 
 */
package mat.server.util;

import mat.shared.StringUtility;

// TODO: Auto-generated Javadoc
/**
 * The Class MeasureUtility.
 * 
 * @author vandavar
 */
public class MeasureUtility {
	
	/**
	 * Gets the version text.
	 * 
	 * @param version
	 *            the version
	 * @param isDraft
	 *            the is draft
	 * @return the version text
	 */
	public static String getVersionText(String version, boolean isDraft){
		
		String mVersion = formatVersionText(version);
		
		if(isDraft) {
			return "Draft based on v"+mVersion;
		} else {
			return "v"+mVersion;
		}
	}
	
	/**
	 * Gets the clause library version prefix.
	 * 
	 * @param version
	 *            the version
	 * @param isDraft
	 *            the is draft
	 * @return the clause library version prefix
	 */
	public static String getClauseLibraryVersionPrefix(String version, boolean isDraft) {
		String mVersion = formatVersionText(version);
		
		if(isDraft) {
			return "(D"+mVersion+")";
		} else {
			return "(v"+mVersion+")";
		}
	}
	
	/**
	 * Format version text.
	 * 
	 * @param version
	 *            the version
	 * @return the string
	 */
	public static String formatVersionText(String version) {
		StringUtility su = new StringUtility();
		String[] versionArr = version.split("\\.");
		String majorVersion = su.trimLeadingZeros(versionArr[0]);
		String minorVersion = su.trimLeadingZeros(versionArr[1]);
		if (versionArr.length > 2) {
			String revisionNumber = versionArr[2];
			String modifiedVersion =  majorVersion + "." + minorVersion + "." + revisionNumber;
			return modifiedVersion;
		} else {
			String modifiedVersion =  majorVersion + "." + minorVersion;
			return modifiedVersion;
		}
	}
	
	/**
	 * Gets the version text.
	 *
	 * @param orgVersionNumber the org version number
	 * @param revisionNumber the revision number
	 * @param draft the draft
	 * @return the version text
	 */
	public static String getVersionText(String orgVersionNumber, String revisionNumber, boolean draft) {
		String mVersion = formatVersionText(orgVersionNumber);
		
		if(draft) {
			return "Draft based on v" + mVersion + "." + revisionNumber;
		} else {
			return "v" + mVersion;
		}
	}
	
	/**
	 * Gets the version text with revision number.
	 *
	 * @param orgVersionNumber the org version number
	 * @param revisionNumber the revision number
	 * @param draft the draft
	 * @return the version text with revision number
	 */
	public static String getVersionTextWithRevisionNumber(String orgVersionNumber, String revisionNumber, boolean draft) {
		String mVersion = formatVersionText(orgVersionNumber);
		
		if(draft) {
			return "Draft based on v" + mVersion;
		} else {
			return "v" + mVersion+ "." + revisionNumber;
		}
	}
	
	/**
	 * Format version text.
	 *
	 * @param revisionNumber the revision number
	 * @param version the version
	 * @return the string
	 */
	public static String formatVersionText(String revisionNumber, String version) {
		StringUtility su = new StringUtility();
		String[] versionArr = version.split("\\.");
		String majorVersion = su.trimLeadingZeros(versionArr[0]);
		String minorVersion = su.trimLeadingZeros(versionArr[1]);
		String modifiedVersion =  majorVersion + "." + minorVersion + "." + revisionNumber;
		return modifiedVersion;
	}
	
}
