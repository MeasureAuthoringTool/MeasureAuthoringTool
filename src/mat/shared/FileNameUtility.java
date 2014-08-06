package mat.shared;


/**
 * Delegate common File name creation behavior shared by ExportServlet.java and ZipPackager.java  
 * @author aschmidt
 *
 */
public class FileNameUtility {
	
	/**
	 * Gets the emeasure xml name.
	 * 
	 * @param name
	 *            the name
	 * @return the emeasure xml name
	 */
	public String getEmeasureXMLName(String name) {
		return name.replaceAll("\\W","") + "_eMeasure.xml";
	}
	
	/**
	 * Gets the emeasure xls name.
	 * 
	 * @param name
	 *            the name
	 * @param packageDate
	 *            the package date
	 * @return the emeasure xls name
	 */
	public String getEmeasureXLSName(String name, String packageDate) {	
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
	public String getZipName(String name) {
		return name.replaceAll("\\W","") + "_Artifacts.zip";
	}
	
	/**
	 * Gets the emeasure html name.
	 * 
	 * @param name
	 *            the name
	 * @return the emeasure html name
	 */
	public String getEmeasureHTMLName(String name) {
		return name.replaceAll("\\W","") + "_eMeasure.html";
	}
	
	/**
	 * Gets the simple xml name.
	 * 
	 * @param name
	 *            the name
	 * @return the simple xml name
	 */
	public String getSimpleXMLName(String name) {
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
	 * Gets the emeasure human readable name.
	 * 
	 * @param name
	 *            the name
	 * @return the emeasure human readable name
	 */
	public String getEmeasureHumanReadableName(String name) {
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
	public String getValueSetXLSName(String name, String lastModifiedDate) {
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
	public String getBulkZipName(String name) {
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
	public String getCSVFileName(String name , String currentTime){
		return (name.concat(currentTime)).replaceAll("\\W","").concat(".html");
	}
}
