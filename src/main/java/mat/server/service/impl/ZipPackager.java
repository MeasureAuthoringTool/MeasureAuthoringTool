package mat.server.service.impl;

import java.io.File;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.Date;
import java.util.Map;

import mat.dao.clause.CQLLibraryDAO;
import mat.dao.clause.CQLLibraryExportDAO;
import mat.dao.clause.MeasureDAO;
import mat.dao.clause.MeasureExportDAO;
import mat.model.clause.CQLLibrary;
import mat.model.clause.CQLLibraryExport;
import mat.model.clause.Measure;
import mat.model.clause.MeasureExport;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import mat.client.shared.MatContext;
import mat.server.export.ExportResult;
import mat.shared.FileNameUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * User Story ID 357 
 * Delegate creation of a zip file containing eMeasure artifacts for export
 * used by SimpleEMeasureServiceImpl.java
 * @author aschmidt
 *
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ZipPackager {


	private static final Log logger = LogFactory.getLog(MethodHandles.lookup().lookupClass());

    private static final String MEASURE = "measure";
    private static final String INCLUDED_LIBRARY = "included-library";
    private static final String SEPARATOR = "-";
    private static final String LIBRARY = "library";
    private static final String HUMAN_READABLE = "humanReadable";


	@Autowired
    private MeasureDAO measureDAO;

	@Autowired
    private MeasureExportDAO measureExportDAO;

	@Autowired
    private CQLLibraryExportDAO cqlLibraryExportDAO;

	@Autowired
    private CQLLibraryDAO cqlLibraryDAO;
	
	/**
	 * Gets the zip barr.
	 *
	 * @param emeasureName            the emeasure name
	 * @param exportDate the export date
	 * @param releaseDate the release date
	 * @param wkbkbarr            the wkbkbarr
	 * @param emeasureXMLStr            the emeasure xml str
	 * @param emeasureHTMLStr            the emeasure html str
	 * @param emeasureXSLUrl            the emeasure xsl url
	 * @param packageDate            the package date
	 * @param simpleXmlStr            the simple xml str
	 * @return the zip barr
	 * @throws Exception             the exception
	 */
	public byte[] getZipBarr(String emeasureName,Date exportDate, String releaseVersion, byte[] wkbkbarr, String emeasureXMLStr, 
			String emeasureHTMLStr, String emeasureXSLUrl, String packageDate, String simpleXmlStr) throws Exception{
		byte[] ret = null;
		
		FileNameUtility fnu = new FileNameUtility();
		
		try{			
			URL u = new URL(emeasureXSLUrl);
			int contentLength = u.openConnection().getContentLength();
			InputStream openStream = u.openStream();
			byte[] emeasureXSLBarr = new byte[contentLength];
			openStream.read(emeasureXSLBarr);
			openStream.close();
		
			String parentPath = "";
			String emeasureXSLPath = "";
			String emeasureXMLPath = "";
			String emeasureHumanReadablePath = "";
			if(releaseVersion.contains(".")){
				releaseVersion = releaseVersion.replace(".", "_");
			}
			System.out.println("Release version zip " + releaseVersion);
			parentPath = fnu.getParentPath(emeasureName + releaseVersion);
			emeasureXSLPath = parentPath+File.separator+"xslt"+File.separator+"eCQM.xsl";
			emeasureXMLPath = parentPath+File.separator+FileNameUtility.getEmeasureXMLName(emeasureName + releaseVersion);
			emeasureHumanReadablePath = parentPath+File.separator+FileNameUtility.getEmeasureHumanReadableName(emeasureName + releaseVersion);
						
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    ZipOutputStream zip = new ZipOutputStream(baos);
		    
			addBytesToZip(emeasureXSLPath, emeasureXSLBarr, zip);
		    addBytesToZip(emeasureXMLPath, emeasureXMLStr.getBytes(), zip);
		    addBytesToZip(emeasureHumanReadablePath, emeasureHTMLStr.getBytes(), zip);

		    zip.close();
		    ret = baos.toByteArray();
		}catch(Exception e){
			System.out.println(e.toString());
			System.out.println(e.fillInStackTrace());
		}
		return ret;
	}
	
	/**
	 * Adds the bytes to zip.
	 * 
	 * @param path
	 *            the path
	 * @param input
	 *            the input
	 * @param zip
	 *            the zip
	 * @throws Exception
	 *             the exception
	 */
	public void addBytesToZip(String path, byte[] input, ZipOutputStream zip) throws Exception {
		ZipEntry entry = new ZipEntry(path);
        entry.setSize(input.length);
        zip.putNextEntry(entry);
        zip.write(input);
        zip.closeEntry();
	}
	
	/**
	 * Creates the bulk export zip.
	 *
	 * @param emeasureName            the emeasure name
	 * @param exportDate the export date
	 * @param releaseDate the release date
	 * @param wkbkbarr            the wkbkbarr
	 * @param emeasureXMLStr            the emeasure xml str
	 * @param emeasureHTMLStr            the emeasure html str
	 * @param emeasureXSLUrl            the emeasure xsl url
	 * @param packageDate            the package date
	 * @param simpleXmlStr            the simple xml str
	 * @param filesMap            the files map
	 * @param seqNum            the seq num
	 * @throws Exception             the exception
	 */
	public void createBulkExportZip(String emeasureName,Date exportDate, byte[] wkbkbarr, String emeasureXMLStr, String emeasureHTMLStr,
			String emeasureXSLUrl, String packageDate, String simpleXmlStr, Map<String, byte[]> filesMap, String seqNum, String releaseVersion, ExportResult cqlExportResult, ExportResult elmExportResult,ExportResult jsonExportResult) throws Exception{
		
		FileNameUtility fnu = new FileNameUtility();
		try{
			
			URL u = new URL(emeasureXSLUrl);
			int contentLength = u.openConnection().getContentLength();
			InputStream openStream = u.openStream();
			byte[] emeasureXSLBarr = new byte[contentLength];
			openStream.read(emeasureXSLBarr);
			openStream.close();
						
			String parentPath = "";
			String emeasureXSLPath = "";
			String emeasureXMLPath = "";
			String emeasureHumanReadablePath = "";
			if(releaseVersion.contains(".")){
				releaseVersion = releaseVersion.replace(".", "_");
			}
			System.out.println("Release version zip " + releaseVersion);
			parentPath = fnu.getParentPath(seqNum +"_"+ emeasureName + "_" + releaseVersion);
			emeasureXSLPath = parentPath+File.separator+"xslt"+File.separator+"eMeasure.xsl";
			emeasureXMLPath = parentPath+File.separator+FileNameUtility.getEmeasureXMLName(emeasureName + "_" + releaseVersion);
			emeasureHumanReadablePath = parentPath+File.separator+FileNameUtility.getEmeasureHumanReadableName(emeasureName + "_" + releaseVersion);
	
			filesMap.put(emeasureXSLPath, emeasureXSLBarr);
			filesMap.put(emeasureXMLPath, emeasureXMLStr.getBytes());
			filesMap.put(emeasureHumanReadablePath, emeasureHTMLStr.getBytes());
						
			if(MatContext.get().isCQLMeasure(releaseVersion)){
				addVersion5Exports(filesMap, cqlExportResult, elmExportResult, parentPath, jsonExportResult);
			}
				  
		} catch (Exception e){
			System.out.println(e.toString());
			System.out.println(e.fillInStackTrace());
		}
	}
	
	/**
	 * Gets the zip barr.
	 *
	 * @param emeasureName the emeasure name
	 * @param releaseVersion 
	 * @param exportDate 
	 * @param wkbkbarr the wkbkbarr
	 * @param packageDate the package date
	 * @param emeasureHTMLStr the emeasure html str
	 * @param simpleXmlStr the simple xml str
	 * @param jsonExportResult 
	 * @param currentRealeaseVersion 
	 * @param string 
	 * @param string 
	 * @return the zip barr
	 */
	public void getZipBarr(String emeasureName, ZipOutputStream zip,String emeasureHTMLStr,
                           String emeasureXMLStr, ExportResult cqlExportResult, ExportResult elmExportResult, ExportResult jsonExportResult,
                           String currentRealeaseVersion, String parentPath, String measureId) {

        Measure measure = measureDAO.getMeasureByMeasureId(measureId);
        MeasureExport measureExport = measureExportDAO.findByMeasureId(measureId);
        CQLLibrary cqlLibrary = cqlLibraryDAO.getLibraryByMeasureId(measureId);
        CQLLibraryExport cqlLibraryExport = cqlLibraryExportDAO.findByLibraryId(cqlLibrary.getId());

		try{
			String emeasureHumanReadablePath = "";
			String emeasureXMLPath = "";


		    String measureReleaseVersion = currentRealeaseVersion;
		    if(currentRealeaseVersion.contains(".")){
		    	currentRealeaseVersion = currentRealeaseVersion.replace(".", "_");
		    }

		    if (!measure.isFhirMeasure()) {
                emeasureHumanReadablePath = parentPath + File.separator + FileNameUtility.getEmeasureHumanReadableName(emeasureName + "_" + currentRealeaseVersion);
                addBytesToZip(emeasureHumanReadablePath, emeasureHTMLStr.getBytes(), zip);

                emeasureXMLPath = parentPath + File.separator + FileNameUtility.getEmeasureXMLName(emeasureName + "_" + currentRealeaseVersion);
                addBytesToZip(emeasureXMLPath,emeasureXMLStr.getBytes(),zip);

            } else {

		        emeasureHumanReadablePath = parentPath + File.separator + HUMAN_READABLE + FileNameUtility.getHtmlFilePath(emeasureName, currentRealeaseVersion);
                addBytesToZip(emeasureHumanReadablePath, emeasureHTMLStr.getBytes(), zip);

                String measureJsonPath = parentPath + File.separator + MEASURE + FileNameUtility.getJsonFilePath(emeasureName, currentRealeaseVersion);
                addBytesToZip(measureJsonPath,measureExport.getJson().getBytes(),zip);

                emeasureXMLPath = parentPath + File.separator + MEASURE + FileNameUtility.getXmlFilePath(emeasureName, currentRealeaseVersion);
                addBytesToZip(emeasureXMLPath,measureExport.getFhirXml().getBytes(),zip);

                String measureIncludedLibraryJsonPath = parentPath + File.separator + MEASURE + SEPARATOR + INCLUDED_LIBRARY + FileNameUtility.getJsonFilePath(emeasureName, currentRealeaseVersion);
                addBytesToZip(measureIncludedLibraryJsonPath,measureExport.getFhirIncludedLibsJson().getBytes(),zip);

                String measureLibraryXmlPath = parentPath + File.separator + MEASURE + SEPARATOR + INCLUDED_LIBRARY + FileNameUtility.getXmlFilePath(emeasureName, currentRealeaseVersion);
                addBytesToZip(measureLibraryXmlPath,measureExport.getFhirIncludedLibsXml().getBytes(),zip);

                String cqlLibraryJsonPath = parentPath + File.separator + LIBRARY + FileNameUtility.getJsonFilePath(cqlLibrary.getName(), currentRealeaseVersion);
                addBytesToZip(cqlLibraryJsonPath,cqlLibraryExport.getJson().getBytes(),zip);

                String cqlLibraryXmlPath = parentPath + File.separator + LIBRARY + FileNameUtility.getXmlFilePath(cqlLibrary.getName(), currentRealeaseVersion);
                addBytesToZip(cqlLibraryXmlPath,cqlLibraryExport.getFhirXml().getBytes(),zip);

                addFileToZip(measure, cqlExportResult, parentPath, "cql", zip);
            }
		    
		    if(isV5OrGreater(measureReleaseVersion) && !measure.isFhirMeasure()){
			    addFileToZip(measure, cqlExportResult, parentPath, "cql", zip);
			    addFileToZip(measure, elmExportResult, parentPath, "xml", zip);
			    addFileToZip(measure, jsonExportResult, parentPath, "json", zip);
		    }
		    
		} catch (Exception e){
			System.out.println(e.toString());
			System.out.println(e.fillInStackTrace());
		}
	}

	private boolean isV5OrGreater(String version) {
		boolean result = false;
		try {
			result = Double.parseDouble(version.substring(1,version.length())) >= 5.0;
		} catch (RuntimeException re) {
			logger.error("Error formatting version " + version,re);
		}
		return result;
	}

	/**
	 * Creates the bulk export zip.
	 *
	 * @param emeasureName the emeasure name
	 * @param wkbkbarr the wkbkbarr
	 * @param emeasureXMLStr the emeasure xml str
	 * @param emeasureHTMLStr the emeasure html str
	 * @param packageDate the package date
	 * @param simpleXmlStr the simple xml str
	 * @param filesMap the files map
	 * @param seqNum the seq num
	 * @param currentReleaseVersion 
	 * @param elmExportResult 
	 * @param jsonExportResult 
	 * @throws Exception the exception
	 */
	public void createBulkExportZip(String emeasureName, byte[] wkbkbarr,
			String emeasureXMLStr, String emeasureHTMLStr,
			String packageDate, String simpleXmlStr,
			Map<String, byte[]> filesMap, String seqNum, String currentReleaseVersion, ExportResult cqlExportResult, ExportResult elmExportResult, ExportResult jsonExportResult, String parentPath) throws Exception{
		try{
			boolean isCQLMeasure = false;
			String emeasureHumanReadablePath = "";
			String emeasureXMLPath = "";
			isCQLMeasure = MatContext.get().isCQLMeasure(currentReleaseVersion);
			
			if (currentReleaseVersion.contains(".")){
				currentReleaseVersion = currentReleaseVersion.replace(".", "_");
			}
			emeasureHumanReadablePath = parentPath+File.separator+FileNameUtility.getEmeasureHumanReadableName(emeasureName + "_" + currentReleaseVersion);
			emeasureXMLPath = parentPath+File.separator+FileNameUtility.getEmeasureXMLName(emeasureName + "_" + currentReleaseVersion);

			filesMap.put(emeasureHumanReadablePath, emeasureHTMLStr.getBytes());
			filesMap.put(emeasureXMLPath, emeasureXMLStr.getBytes());
			
			if(isCQLMeasure){
				addVersion5Exports(filesMap, cqlExportResult, elmExportResult, parentPath, jsonExportResult);
			}
			
		}catch(Exception e){
			System.out.println(e.toString());
			System.out.println(e.fillInStackTrace());
		}
		
	}

	/**
	 * @param filesMap
	 * @param cqlExportResult
	 * @param elmExportResult
	 * @param parentPath
	 * @param jsonExportResult 
	 */
	private void addVersion5Exports(Map<String, byte[]> filesMap,
			ExportResult cqlExportResult, ExportResult elmExportResult,
			String parentPath, ExportResult jsonExportResult) {
				
		if(cqlExportResult.includedCQLExports.size() > 0){
			String filePath = "";
			filePath = parentPath+File.separator + cqlExportResult.getCqlLibraryName() + "." + "cql";
			filesMap.put(filePath,cqlExportResult.export.getBytes());
			
			for(ExportResult includedResult : cqlExportResult.getIncludedCQLExports()){
				filePath = parentPath+File.separator + includedResult.getCqlLibraryName() + "." + "cql";
				filesMap.put(filePath,includedResult.export.getBytes());
			}
		}else{
			String filePath = "";
			filePath = parentPath+File.separator + cqlExportResult.getCqlLibraryName() + "." + "cql";
			filesMap.put(filePath,cqlExportResult.export.getBytes());
		}

		if(elmExportResult.includedCQLExports.size() > 0){
			String filePath = "";
			filePath = parentPath+File.separator + cqlExportResult.getCqlLibraryName() + "." + "xml";
			filesMap.put(filePath,elmExportResult.export.getBytes());
			
			for(ExportResult includedResult : elmExportResult.getIncludedCQLExports()){
				filePath = parentPath + File.separator + includedResult.getCqlLibraryName() + "." + "xml";
				filesMap.put(filePath,includedResult.export.getBytes());
			}
		}else{
			String filePath = "";
			filePath = parentPath + File.separator + cqlExportResult.getCqlLibraryName() + "." + "xml";
			filesMap.put(filePath,elmExportResult.export.getBytes());
		}
		
		if(jsonExportResult.includedCQLExports.size() > 0){
			String filePath = "";
			filePath = parentPath+File.separator + cqlExportResult.getCqlLibraryName() + "." + "json";
			filesMap.put(filePath,jsonExportResult.export.getBytes());
			
			for(ExportResult includedResult : jsonExportResult.getIncludedCQLExports()){
				filePath = parentPath + File.separator + includedResult.getCqlLibraryName() + "." + "json";
				filesMap.put(filePath,includedResult.export.getBytes());
			}
		}else{
			String filePath = "";
			filePath = parentPath + File.separator + cqlExportResult.getCqlLibraryName() + "." + "json";
			filesMap.put(filePath,jsonExportResult.export.getBytes());
		}
	}	 
	

	public byte[] getCQLZipBarr(Measure measure, ExportResult export, String extension) {
		byte[] ret = null;
		
		try{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ZipOutputStream zip = new ZipOutputStream(baos);
			
			String parentPath = "";
			parentPath = export.measureName + "_" + extension;
			
			addFileToZip(measure, export, parentPath, extension, zip);
			
			zip.close();
			ret = baos.toByteArray();
			export.zipbarr = ret;
		}catch(Exception er){
			er.printStackTrace();
		}
		return ret;
	}

	/**
	 * @param export
	 * @param extension
	 * @param parentPath 
	 * @param zip
	 * @throws Exception
	 */
	private void addFileToZip(Measure measure, ExportResult export, String parentPath, String extension,
			 ZipOutputStream zip) throws Exception {
							
		String cqlFilePath = "";
		cqlFilePath = measure.isFhirMeasure() ? parentPath + File.separator + LIBRARY + "-" + export.getCqlLibraryName() + "." + extension
                : parentPath + File.separator + export.getCqlLibraryName() + "." + extension;
		addBytesToZip(cqlFilePath, export.export.getBytes(), zip);
		
		for (ExportResult includedResult : export.getIncludedCQLExports()) {
			cqlFilePath = parentPath+File.separator + includedResult.getCqlLibraryName() + "." + extension;
			addBytesToZip(cqlFilePath, includedResult.export.getBytes(), zip);
		}
	}
	 
}
