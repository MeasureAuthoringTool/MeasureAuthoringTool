package org.ifmc.mat.shared;


/**
 * Delegate common File name creation behavior shared by ExportServlet.java and ZipPackager.java  
 * @author aschmidt
 *
 */
public class FileNameUtility {
	
	public String getEmeasureXMLName(String name) {
		return name.replaceAll("\\W","") + "_eMeasure.xml";
	}
	public String getEmeasureXLSName(String name, String packageDate) {	
		packageDate = packageDate.replace(':', '.');
		return name.replaceAll("\\W","") + "_"+packageDate+".xls";
	}
	public String getZipName(String name) {
		return name.replaceAll("\\W","") + "_Artifacts.zip";
	}
	public String getEmeasureHTMLName(String name) {
		return name.replaceAll("\\W","") + "_eMeasure.html";
	}
	public String getSimpleXMLName(String name) {
		return name.replaceAll("\\W","") + "_SimpleXML.xml";
	}
	public String getParentPath(String name) {
		return name.replaceAll("\\W","") + "_Artifacts";
	}
	public String getEmeasureHumanReadableName(String name) {
		return name.replaceAll("\\W","") + "_HumanReadable.html";
	}
	public String getValueSetXLSName(String name, String lastModifiedDate) {
		if(lastModifiedDate == null)
			lastModifiedDate = "DRAFT";
		else
			lastModifiedDate = lastModifiedDate.replace(':', '.');
		return name.replaceAll("\\W","") + "_"+lastModifiedDate+".xls";
	}
}
