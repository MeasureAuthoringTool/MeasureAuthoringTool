/**
 * 
 */
package mat.server.util;

import mat.shared.StringUtility;

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
	
		if(isDraft)
			return "Draft based on v"+mVersion;
		else
			return "v"+mVersion;
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
	public static String getClauseLibraryVersionPrefix(String version, boolean isDraft){
		String mVersion = formatVersionText(version);
		
		if(isDraft)
			return "(D"+mVersion+")";
		else
			return "(v"+mVersion+")";
	}
	
	/**
	 * Format version text.
	 * 
	 * @param version
	 *            the version
	 * @return the string
	 */
	public static String formatVersionText(String version){
		StringUtility su = new StringUtility();
		String[] versionArr = version.split("\\.");
		String majorVersion = su.trimLeadingZeros(versionArr[0]);
		String minorVersion = su.trimLeadingZeros(versionArr[1]);
		String modifiedVersion =  majorVersion + "." + minorVersion;
		return modifiedVersion;
	}
	
}
