/**
 * 
 */
package mat.server.util;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;

import mat.shared.StringUtility;

/**
 * The Class MeasureUtility.
 * 
 * @author vandavar
 */
public class MeasureUtility {
	/** The Constant logger. */
	//private static final Log LOG = LogFactory.getLog(MeasureUtility.class);
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
			return "Draft v" + mVersion+ "." + revisionNumber;
		} else {
			return "v" + mVersion;
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
	
	/**
	 * This method will take a String and remove all non-alphabet/non-numeric characters 
	 * except underscore ("_") characters.
	 * @param originalString
	 * @return cleanedString
	 */
	public static String cleanString(String originalString) {
		originalString = originalString.replaceAll(" ", "");
		
		String cleanedString = "";	
		for(int i=0;i<originalString.length();i++){
			char c = originalString.charAt(i);
			int intc = (int)c;
			
			if(c == '_' || (intc >= 48 && intc <= 57) || (intc >= 65 && intc <= 90) || (intc >= 97 && intc <= 122)){
				
				if(!(cleanedString.isEmpty() && Character.isDigit(c))){
					cleanedString = cleanedString + "" + c;
				}
				
			} 

		}
		
		return cleanedString;
	}
	
	/**
	 * Method to set latest QDM Version in Draft's or clones of CQL type measure or CQL Stand Alone Library.
	 * 
	 * **/
	public static void updateLatestQDMVersion(XmlProcessor processor) throws XPathExpressionException {

		Node cqlLibraryQdmVersionNode = processor.findNode(processor.getOriginalDoc(), "//cqlLookUp/usingModelVersion");

		if (cqlLibraryQdmVersionNode != null) {
			cqlLibraryQdmVersionNode.setTextContent(MATPropertiesService.get().getQmdVersion());
		}
	}
	
}
