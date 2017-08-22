package mat.server.simplexml.hqmf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.xpath.XPathExpressionException;

import mat.model.clause.MeasureExport;
import mat.server.service.impl.XMLUtility;
import mat.server.util.XmlProcessor;

import org.w3c.dom.Node;

// TODO: Auto-generated Javadoc
/**
 * The Class HQMFMeasureDetailsGenerator.
 */
public class HQMFMeasureDetailsGenerator implements Generator {
	
	/** The Constant conversionFileForHQMF_Header. */
	private static final String conversionFileForHQMF_Header = "xsl/new_measureDetails.xsl";
	
	/* (non-Javadoc)
	 * @see mat.server.simplexml.hqmf.Generator#generate(mat.model.clause.MeasureExport)
	 */
	@Override
	public String generate(MeasureExport me) {
		
		String simpleXML = me.getSimpleXML();
		String releaseVersion = me.getMeasure().getReleaseVersion();
		/*XmlProcessor test = new XmlProcessor(simpleXMLTemp);
		String xpath = "/measure";
		Document tempDoc = test.getOriginalDoc();
		Node measure = test.findNode(tempDoc, xpath);
		
		String version = me.getMeasure().getReleaseVersion();
		version = formatRealeaseVersion(version);
		Node child = tempDoc.createElement("realeaseVersion");
		Node attribute = tempDoc.createAttribute("version");
		attribute.setNodeValue(version);
		child.getAttributes().setNamedItem(attribute);
		measure.appendChild(child);
		
		simpleXMLTemp = test.transform(measure);*/
		
		simpleXML = addReleaseVersionToSimpleXML(simpleXML,releaseVersion);
		
		XMLUtility xmlUtility = new XMLUtility();
		String measureDetailsHQMF_XML = xmlUtility.applyXSL(simpleXML,
				xmlUtility.getXMLResource(conversionFileForHQMF_Header));
		measureDetailsHQMF_XML = incrementEndDatebyOne(measureDetailsHQMF_XML);
		return measureDetailsHQMF_XML.replaceAll("xmlns=\"\"", "");
	}
	
	/**
	  * This method will add a new tag '<measureReleaseVersion releaseVersion={version}/>' right under the '<measure>' tag.
	  * This will contain the QDM version used in the measure.
	  * If Measure Release Version is 'v4', then QDM Version is '4.1.2'; else it is '4.3'
	  * The 'new_measureDetails.xsl' will then read the '<measureReleaseVersion releaseVersion={version}/>' tag and 
	  * add a comment with the value of 'releaseVersion' attribute in it.
	  * @param simpleXML
	  * @param releaseVersion
	  * @return
	  */
	 private String addReleaseVersionToSimpleXML(String simpleXML, String releaseVersion) {
		 if(releaseVersion == null || releaseVersion.trim().length() == 0){
			 return simpleXML;
		 }
		
		 releaseVersion = formatRealeaseVersion(releaseVersion);
		 int measureDetailsTagIndex = simpleXML.indexOf("<measureDetails>");
		 if(measureDetailsTagIndex > -1){
			 simpleXML = simpleXML.substring(0, measureDetailsTagIndex) + "<measureReleaseVersion releaseVersion=\""+releaseVersion + "\"/>" + simpleXML.substring(measureDetailsTagIndex);
			 System.out.println("SIMPLE XML: " + simpleXML);
		 }
	    
		 return simpleXML;
	 }
	
	private String formatRealeaseVersion(String version){
		String formatVersion = null;
		if("v4".equals(version)){
			formatVersion = "4.1.2";
		}else if("v4.3".equals(version)){
			//This is 4.2 because were on qdm version 4.2 and export 4.3. The QDM version needs to appear in the comments
			formatVersion = "4.2";
		}
		return formatVersion;		
	}
	
	/**
	 * Increment end dateby one.
	 *
	 * @param measureDetailsHQMF_XML the measure details hqm f_ xml
	 * @return the string
	 */
	private String incrementEndDatebyOne(String measureDetailsHQMF_XML){
		XmlProcessor measureDetailsXml = new XmlProcessor(measureDetailsHQMF_XML);
		String xpathMeasurementPeriodStr = "/QualityMeasureDocument/controlVariable/measurePeriod/value/high";
		try {
			Node endDateHigh = measureDetailsXml.findNode(measureDetailsXml.getOriginalDoc(), 
					xpathMeasurementPeriodStr);
			if(endDateHigh!=null){
				String highEndDate = endDateHigh.getAttributes().getNamedItem("value").getNodeValue();
				highEndDate = formatDate(highEndDate);
				endDateHigh.getAttributes().getNamedItem("value").setNodeValue(highEndDate);
			}
			//to edit default startDate in HQMF measureDetails
			editDefaultStartDate(measureDetailsXml);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

			return measureDetailsXml.transform(measureDetailsXml.getOriginalDoc(), true);
	}
	
	/**
	 * Edits the default start date.
	 *
	 * @param measureDetailsXml the measure details xml
	 */
	private void editDefaultStartDate(XmlProcessor measureDetailsXml) {
		String xpathMeasurementPeriodStr = "/QualityMeasureDocument/controlVariable/measurePeriod/value/phase/low";
		try {
			Node measurementPeriodLowNode = measureDetailsXml.findNode(measureDetailsXml.getOriginalDoc(), 
					xpathMeasurementPeriodStr);
			if(measurementPeriodLowNode!=null){
				String lowDateValue = measurementPeriodLowNode.getAttributes().
						getNamedItem("value").getNodeValue();
				measurementPeriodLowNode.getAttributes().getNamedItem("value").
				setNodeValue(getLowValue(lowDateValue));
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the low value.
	 *
	 * @param lowValue the low value
	 * @return the low value
	 */
	private String getLowValue(String lowValue){
		String lowDate = "";
		String year = lowValue.substring(0,4);
		String mm = lowValue.substring(4,6);
		String dd = lowValue.substring(6,8);
		year = getCurrentYear();
		lowDate = year + mm + dd;
	return lowDate;
	}
	
	/**
	 * Gets the current year.
	 *
	 * @return the current year
	 */
	private String getCurrentYear(){
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		String currentYear = String.valueOf(year);
		return currentYear;
	}
	
	
	/**
	 * Format date.
	 *
	 * @param date the date
	 * @return the string
	 */
	private String formatDate(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(sdf.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.add(Calendar.DATE, 1);  // to add one to End Date
		date = sdf.format(c.getTime()); 
		return date;
	}

}
