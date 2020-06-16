package mat.shared;

/**
 * Delegate common File name creation behavior shared by ExportServlet.java and ZipPackager.java  
 * @author aschmidt
 *
 */
public class FileNameUtility {
	
	/**
	 * Gets the eCQM xml name.
	 * 
	 * @param name
	 *            the name
	 * @return the eCQM xml name
	 */
	public static String getEmeasureXMLName(String name) {
		return name.replaceAll("\\W","") + "_eCQM.xml";
	}
	
	/**
	 * Gets the eCQM xls name.
	 * 
	 * @param name
	 *            the name
	 * @param packageDate
	 *            the package date
	 * @return the eCQM xls name
	 */
	public static String getEmeasureXLSName(String name, String packageDate) {	
		packageDate = packageDate.replace(':', '.');
		return name.replaceAll("\\W","") + "_"+packageDate+".xls";
	}
	
	/**
	 * Gets the zip name.
	 * 
	 * @param name
	 *            the name
	 * @return the zip name
	 */
	public static String getZipName(String name) {
		return name.replaceAll("\\W","") + "_Artifacts.zip";
	}
	
	/**
	 * Gets the eCQM html name.
	 * 
	 * @param name
	 *            the name
	 * @return the eCQM html name
	 */
	public static String getEmeasureHTMLName(String name) {
		return name.replaceAll("\\W","") + "_eCQM.html";
	}
	
	/**
	 * Gets the simple xml name.
	 * 
	 * @param name
	 *            the name
	 * @return the simple xml name
	 */
	public static String getSimpleXMLName(String name) {
		return name.replaceAll("\\W","") + "_SimpleXML.xml";
	}
	
	/**
	 * Gets the parent path.
	 * 
	 * @param name
	 *            the name
	 * @return the parent path
	 */
	public String getParentPath(String name) {
		return name.replaceAll("\\W","") + "_Artifacts";
	}
	
	/**
	 * Gets the eCQM human readable name.
	 * 
	 * @param name
	 *            the name
	 * @return the eCQM human readable name
	 */
	public static String getEmeasureHumanReadableName(String name) {
		return name.replaceAll("\\W","") + "_HumanReadable.html";
	}
	
	/**
	 * Gets the value set xls name.
	 * 
	 * @param name
	 *            the name
	 * @param lastModifiedDate
	 *            the last modified date
	 * @return the value set xls name
	 */
	public static String getValueSetXLSName(String name, String lastModifiedDate) {
		if(lastModifiedDate == null)
			lastModifiedDate = "DRAFT";
		else
			lastModifiedDate = lastModifiedDate.replace(':', '.');
		return name.replaceAll("\\W","") + "_"+lastModifiedDate+".xls";
	}
	
	/**
	 * Gets the bulk zip name.
	 * 
	 * @param name
	 *            the name
	 * @return the bulk zip name
	 */
	public static String getBulkZipName(String name) {
		return name.replaceAll("\\W","") + "_Artifacts.zip";
	}
	
	/**
	 * Gets the cSV file name.
	 * 
	 * @param name
	 *            the name
	 * @param currentTime
	 *            the current time
	 * @return the cSV file name
	 */
	public static String getHTMLFileName(String name , String currentTime){
		return (name.concat(currentTime)).replaceAll("\\W","").concat(".html");
	}
	
	/**
	 * Gets the cSV file name.
	 *
	 * @param name the name
	 * @param currentTime the current time
	 * @return the cSV file name
	 */
	public static String getCSVFileName(String name , String currentTime){
		return (name.concat(currentTime)).replaceAll("\\W","").concat(".csv");
	}
	
	public static String getCQLFileName(String name) {
		return name.replaceAll("\\W","") + "_CQL.cql";
	}
	
	public static String getELMFileName(String name) {
		return name.replaceAll("\\W","") + "_ELM.xml";
	}

	public static String getJsonFilePath(String releaseVersion) {
		return "-" + releaseVersion + ".json";
	}

    public static String getJsonFilePath(String measureName, String releaseVersion) {
        return "-" + measureName + "-" + releaseVersion + ".json";
    }

    public static String getXmlFilePath(String measureName, String releaseVersion) {
        return "-" + measureName + "-" + releaseVersion + ".xml";
    }

    public static String getHtmlFilePath(String measureName, String releaseVersion) {
        return "-" + measureName + "-" + releaseVersion + ".html";
    }
}

