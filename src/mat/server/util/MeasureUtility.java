/**
 * 
 */
package mat.server.util;

import mat.shared.StringUtility;

/**
 * @author vandavar
 *
 */
public class MeasureUtility {
	
	public static String getVersionText(String version, boolean isDraft){
		
		String mVersion = formatVersionText(version);
	
		if(isDraft)
			return "Draft based on v"+mVersion;
		else
			return "v"+mVersion;
	}
	public static String getClauseLibraryVersionPrefix(String version, boolean isDraft){
		String mVersion = formatVersionText(version);
		
		if(isDraft)
			return "(D"+mVersion+")";
		else
			return "(v"+mVersion+")";
	}
	
	public static String formatVersionText(String version){
		StringUtility su = new StringUtility();
		String[] versionArr = version.split("\\.");
		String majorVersion = su.trimLeadingZeros(versionArr[0]);
		String minorVersion = su.trimLeadingZeros(versionArr[1]);
		String modifiedVersion =  majorVersion + "." + minorVersion;
		return modifiedVersion;
	}
	
}
