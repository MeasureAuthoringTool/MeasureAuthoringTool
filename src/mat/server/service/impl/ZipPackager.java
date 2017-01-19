package mat.server.service.impl;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

import mat.server.util.XmlProcessor;
import mat.shared.FileNameUtility;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.eclipse.persistence.jaxb.compiler.XMLProcessor;
import org.w3c.dom.Node;

// TODO: Auto-generated Javadoc
/**
 * User Story ID 357 
 * Delegate creation of a zip file containing eMeasure artifacts for export
 * used by SimpleEMeasureServiceImpl.java
 * @author aschmidt
 *
 */
public class ZipPackager {
	
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
			String emeasureHTMLStr, String emeasureXSLUrl, String packageDate, String simpleXmlStr, String cqlFileStr, String elmFileStr) throws Exception{
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
			//String codeListXLSPath = "";
//			String simpleXMLPath = "";
			String cqlFilePath = "";
			String elmFilePath = "";
			
			if(releaseVersion.contains(".")){
				releaseVersion = releaseVersion.replace(".", "_");
			}
			System.out.println("Release version zip " + releaseVersion);
			parentPath = fnu.getParentPath(emeasureName + releaseVersion);
			emeasureXSLPath = parentPath+File.separator+"xslt"+File.separator+"eMeasure.xsl";
			emeasureXMLPath = parentPath+File.separator+fnu.getEmeasureXMLName(emeasureName + releaseVersion);
			emeasureHumanReadablePath = parentPath+File.separator+fnu.getEmeasureHumanReadableName(emeasureName + releaseVersion);
			//codeListXLSPath = parentPath+File.separator+fnu.getEmeasureXLSName(emeasureName + releaseVersion,packageDate);
//			simpleXMLPath = parentPath+File.separator+fnu.getSimpleXMLName(emeasureName + releaseVersion);
			String cqlLibraryName = getCQLLibraryName(simpleXmlStr); 
			if(cqlLibraryName.isEmpty()) {
				cqlFilePath = parentPath+File.separator+fnu.getCQLFileName(emeasureName);
				elmFilePath = parentPath+File.separator+fnu.getELMFileName(emeasureName);
			} else {			
				cqlFilePath = parentPath+File.separator+fnu.getCQLFileName(cqlLibraryName);
				elmFilePath = parentPath+File.separator+fnu.getELMFileName(cqlLibraryName);
			}

			
			
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    ZipOutputStream zip = new ZipOutputStream(baos);
		    
//		    addBytesToZip(simpleXMLPath, simpleXmlStr.getBytes(), zip);
			addBytesToZip(emeasureXSLPath, emeasureXSLBarr, zip);
		    addBytesToZip(emeasureXMLPath, emeasureXMLStr.getBytes(), zip);
		    addBytesToZip(emeasureHumanReadablePath, emeasureHTMLStr.getBytes(), zip);
		   // addBytesToZip(codeListXLSPath, wkbkbarr, zip);
		    addBytesToZip(cqlFilePath, cqlFileStr.getBytes(), zip);
		    addBytesToZip(elmFilePath, elmFileStr.getBytes(), zip);
		    
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
			String emeasureXSLUrl, String packageDate, String simpleXmlStr, Map<String, byte[]> filesMap, String seqNum, String releaseVersion, String cqlFileStr, String elmFileStr) throws Exception{
		
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
			//String codeListXLSPath = "";
//			String simpleXMLPath = "";
			String cqlFilePath = "";
			String elmFilePath = "";
          
			if(releaseVersion.contains(".")){
				releaseVersion = releaseVersion.replace(".", "_");
			}
			System.out.println("Release version zip " + releaseVersion);
			parentPath = fnu.getParentPath(seqNum +"_"+ emeasureName + "_" + releaseVersion);
			emeasureXSLPath = parentPath+File.separator+"xslt"+File.separator+"eMeasure.xsl";
			emeasureXMLPath = parentPath+File.separator+fnu.getEmeasureXMLName(emeasureName + "_" + releaseVersion);
			emeasureHumanReadablePath = parentPath+File.separator+fnu.getEmeasureHumanReadableName(emeasureName + "_" + releaseVersion);
			//codeListXLSPath = parentPath+File.separator+fnu.getEmeasureXLSName(emeasureName +"_" + releaseVersion,packageDate);
//			simpleXMLPath = parentPath+File.separator+fnu.getSimpleXMLName(emeasureName + "_" + releaseVersion);
			String cqlLibraryName = getCQLLibraryName(simpleXmlStr); 
			if(cqlLibraryName.isEmpty()) {
				cqlFilePath = parentPath+File.separator+fnu.getCQLFileName(emeasureName);
				elmFilePath = parentPath+File.separator+fnu.getELMFileName(emeasureName);
			} else {			
				cqlFilePath = parentPath+File.separator+fnu.getCQLFileName(cqlLibraryName);
				elmFilePath = parentPath+File.separator+fnu.getELMFileName(cqlLibraryName);
			}
			
//			filesMap.put(simpleXMLPath, simpleXmlStr.getBytes());
			filesMap.put(emeasureXSLPath, emeasureXSLBarr);
			filesMap.put(emeasureXMLPath, emeasureXMLStr.getBytes());
			filesMap.put(emeasureHumanReadablePath, emeasureHTMLStr.getBytes());
		//	filesMap.put(codeListXLSPath, wkbkbarr);
			filesMap.put(cqlFilePath, cqlFileStr.getBytes());
			filesMap.put(elmFilePath, elmFileStr.getBytes());
		    
		  
		}catch(Exception e){
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
	 * @param currentRealeaseVersion 
	 * @param string 
	 * @param string 
	 * @return the zip barr
	 */
	public byte[] getZipBarr(String emeasureName, byte[] wkbkbarr,
						 String packageDate,String emeasureHTMLStr, String simpleXmlStr, String emeasureXMLStr, String cqlFileStr, String elmFileStr, String currentRealeaseVersion) {
		byte[] ret = null;
		
		FileNameUtility fnu = new FileNameUtility();
		
		try{

			String parentPath = "";
			String emeasureHumanReadablePath = "";
			//String codeListXLSPath = "";
//			String simpleXMLPath = "";
			String emeasureXMLPath = "";
			String cqlFilePath = "";
			String elmFilePath = "";
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    ZipOutputStream zip = new ZipOutputStream(baos);
		    
		    if(currentRealeaseVersion.contains(".")){
		    	currentRealeaseVersion = currentRealeaseVersion.replace(".", "_");
		    }
		    
			parentPath = fnu.getParentPath(emeasureName +"_" + currentRealeaseVersion);
			emeasureHumanReadablePath = parentPath+File.separator+fnu.getEmeasureHumanReadableName(emeasureName + "_" +currentRealeaseVersion);
			//codeListXLSPath = parentPath+File.separator+fnu.getEmeasureXLSName(emeasureName + "_" + currentRealeaseVersion,packageDate);
//			simpleXMLPath = parentPath+File.separator+fnu.getSimpleXMLName(emeasureName +"_" + currentRealeaseVersion);
			emeasureXMLPath = parentPath+File.separator+fnu.getEmeasureXMLName(emeasureName + "_" + currentRealeaseVersion);
			String cqlLibraryName = getCQLLibraryName(simpleXmlStr); 
			if(cqlLibraryName.isEmpty()) {
				cqlFilePath = parentPath+File.separator+fnu.getCQLFileName(emeasureName);
				elmFilePath = parentPath+File.separator+fnu.getELMFileName(emeasureName);
			} else {			
				cqlFilePath = parentPath+File.separator+fnu.getCQLFileName(cqlLibraryName);
				elmFilePath = parentPath+File.separator+fnu.getELMFileName(cqlLibraryName);
			}			
//		    addBytesToZip(simpleXMLPath, simpleXmlStr.getBytes(), zip);
			addBytesToZip(emeasureHumanReadablePath, emeasureHTMLStr.getBytes(), zip);
		   // addBytesToZip(codeListXLSPath, wkbkbarr, zip);
		    addBytesToZip(emeasureXMLPath,emeasureXMLStr.getBytes(),zip);
		    addBytesToZip(cqlFilePath,cqlFileStr.getBytes(),zip);
		    addBytesToZip(elmFilePath, elmFileStr.getBytes(), zip);
		    
		    zip.close();
		    ret = baos.toByteArray();
		}catch(Exception e){
			System.out.println(e.toString());
			System.out.println(e.fillInStackTrace());
		}
		return ret;
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
	 * @param elmFileStr 
	 * @throws Exception the exception
	 */
	public void createBulkExportZip(String emeasureName, byte[] wkbkbarr,
			String emeasureXMLStr, String emeasureHTMLStr,
			String packageDate, String simpleXmlStr,
			Map<String, byte[]> filesMap, String seqNum, String currentReleaseVersion, String cqlFileStr, String elmFileStr) throws Exception{
		FileNameUtility fnu = new FileNameUtility();

		try{
			String parentPath = "";
			String emeasureHumanReadablePath = "";
		//	String codeListXLSPath = "";
//			String simpleXMLPath = "";
			String emeasureXMLPath = "";
			String cqlFilePath = "";
			String elmFilePath = "";
			
			if (currentReleaseVersion.contains(".")){
				currentReleaseVersion = currentReleaseVersion.replace(".", "_");
			}
			
			parentPath = fnu.getParentPath(seqNum +"_"+ emeasureName + "_" + currentReleaseVersion);
			emeasureHumanReadablePath = parentPath+File.separator+fnu.getEmeasureHumanReadableName(emeasureName + "_" + currentReleaseVersion);
		//	codeListXLSPath = parentPath+File.separator+fnu.getEmeasureXLSName(emeasureName + "_" + currentReleaseVersion,packageDate);
//			simpleXMLPath = parentPath+File.separator+fnu.getSimpleXMLName(emeasureName + "_" + currentReleaseVersion);
			emeasureXMLPath = parentPath+File.separator+fnu.getEmeasureXMLName(emeasureName + "_" + currentReleaseVersion);
			
			String cqlLibraryName = getCQLLibraryName(simpleXmlStr); 
			if(cqlLibraryName.isEmpty()) {
				cqlFilePath = parentPath+File.separator+fnu.getCQLFileName(emeasureName);
				elmFilePath = parentPath+File.separator+fnu.getELMFileName(emeasureName);
			} else {			
				cqlFilePath = parentPath+File.separator+fnu.getCQLFileName(cqlLibraryName);
				elmFilePath = parentPath+File.separator+fnu.getELMFileName(cqlLibraryName);
			}


//			filesMap.put(simpleXMLPath, simpleXmlStr.getBytes());
			filesMap.put(emeasureHumanReadablePath, emeasureHTMLStr.getBytes());
	//		filesMap.put(codeListXLSPath, wkbkbarr);
			filesMap.put(emeasureXMLPath, emeasureXMLStr.getBytes());
			filesMap.put(cqlFilePath, cqlFileStr.getBytes());
			filesMap.put(elmFilePath, elmFileStr.getBytes());

			
		}catch(Exception e){
			System.out.println(e.toString());
			System.out.println(e.fillInStackTrace());
		}
		
	}	 
	
	private String getCQLLibraryName(String simpleXMLString) {
		
		// get the name from the simple xml
		String xPathName = "/measure/cqlLookUp[1]/library[1]"; 
		XmlProcessor xmlProcessor = new XmlProcessor(simpleXMLString); 
		Node cqlFileName = null;
		try {
			cqlFileName = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), xPathName);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		if(cqlFileName == null) {
			return ""; 
		}
		
		else {
			return cqlFileName.getTextContent(); 
		}
		
		
	}
	 
}
