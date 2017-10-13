package mat.server.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mat.model.MatValueSet;


/**
 * The Interface SimpleEMeasureService.
 */
public interface SimpleEMeasureService {
	
	/**
	 * The Class ExportResult.
	 */
	public static class ExportResult {
		
		private String cqlLibraryName; 
		
		/** The measure name. */
		public String measureName;
		
		/** The value set name. */
		public String valueSetName;
		
		/** The package date. */
		public String packageDate;
		
		/** The export. */
		public String export;
		
		public List<ExportResult> includedCQLExports = new ArrayList<ExportResult>();
		
		/** The wkbkbarr. */
		public byte[] wkbkbarr;
		
		/** The zipbarr. */
		public byte[] zipbarr;
		
		/** The last modified date. */
		public String lastModifiedDate;

		public String getCqlLibraryName() {
			return cqlLibraryName;
		}

		public void setCqlLibraryName(String cqlLibraryName) {
			this.cqlLibraryName = cqlLibraryName;
		}
		
		public List<ExportResult> getIncludedCQLExports() {
			return includedCQLExports;
		}
	}
	
	/**
	 * Gets the simple xml.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the simple xml
	 * @throws Exception
	 *             the exception
	 */
	public ExportResult getSimpleXML(String measureId) throws Exception;
	
	/**
	 * Gets the e measure xml.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the e measure xml
	 * @throws Exception
	 *             the exception
	 */
	public ExportResult getEMeasureXML(String measureId) throws Exception;
	
	/**
	 * Gets the e measure html.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the e measure html
	 * @throws Exception
	 *             the exception
	 */
	public ExportResult getEMeasureHTML(String measureId) throws Exception;
	
	/**
	 * Gets the e measure xls.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the e measure xls
	 * @throws Exception
	 *             the exception
	 */
	public ExportResult getEMeasureXLS(String measureId) throws Exception;
	
	/**
	 * Gets the e measure zip.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the e measure zip
	 * @throws Exception
	 *             the exception
	 */
	public ExportResult getEMeasureZIP(String measureId,Date exportDate) throws Exception;
	
	/**
	 * Gets the value set xls.
	 * 
	 * @param valueSetId
	 *            the value set id
	 * @return the value set xls
	 * @throws Exception
	 *             the exception
	 */
	public ExportResult getValueSetXLS(String valueSetId) throws Exception;
	
	/**
	 * Gets the bulk export zip.
	 * 
	 * @param measureIds
	 *            the measure ids
	 * @return the bulk export zip
	 * @throws Exception
	 *             the exception
	 */
	public ExportResult getBulkExportZIP(String[] measureIds, Date[] exportDates) throws Exception;
	
	/**
	 * Export measure into simple xml.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param xmlString
	 *            the xml string
	 * @param matValueSetList
	 *            the mat value set list
	 * @return the export result
	 * @throws Exception
	 *             the exception
	 */
	ExportResult exportMeasureIntoSimpleXML(String measureId, String xmlString, List<MatValueSet> matValueSetList)
			throws Exception;

	ExportResult getHumanReadableForNode(String measureId, String populationSubXML) throws Exception;

	ExportResult getNewEMeasureHTML(String measureId, String currentReleaseVersion) throws Exception;
	public ExportResult getNewEMeasureXML(String measureId);

	ExportResult getCQLLibraryFile(String measureId) throws Exception;

	ExportResult getELMFile(String measureId) throws Exception;

	ExportResult getJSONFile(String measureId) throws Exception;

}
