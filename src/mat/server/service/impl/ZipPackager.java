package mat.server.service.impl;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Map;

import mat.shared.FileNameUtility;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

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
	public byte[] getZipBarr(String emeasureName,Date exportDate, Date releaseDate, byte[] wkbkbarr, String emeasureXMLStr, 
			String emeasureHTMLStr, String emeasureXSLUrl, String packageDate, String simpleXmlStr) throws Exception{
		byte[] ret = null;
		
		FileNameUtility fnu = new FileNameUtility();
		
		try{
//			String parentPath = fnu.getParentPath(emeasureName);
//			String emeasureXSLPath = parentPath+File.separator+"xslt"+File.separator+"eMeasure.xsl";
			
			URL u = new URL(emeasureXSLUrl);
			int contentLength = u.openConnection().getContentLength();
			InputStream openStream = u.openStream();
			byte[] emeasureXSLBarr = new byte[contentLength];
			openStream.read(emeasureXSLBarr);
			openStream.close();
			
//			String emeasureXMLPath = parentPath+File.separator+fnu.getEmeasureXMLName(emeasureName);
//			String emeasureHumanReadablePath = parentPath+File.separator+fnu.getEmeasureHumanReadableName(emeasureName);
//			String codeListXLSPath = parentPath+File.separator+fnu.getEmeasureXLSName(emeasureName,packageDate);
//			String simpleXMLPath = parentPath+File.separator+fnu.getSimpleXMLName(emeasureName);
			String parentPath = "";
			String emeasureXSLPath = "";
			String emeasureXMLPath = "";
			String emeasureHumanReadablePath = "";
			String codeListXLSPath = "";
			String simpleXMLPath = "";
            String matVersion[] = {"_v3","_v4"};
			if(exportDate.before(releaseDate)){
				
				parentPath = fnu.getParentPath(emeasureName + matVersion[0]);
				emeasureXSLPath = parentPath+File.separator+"xslt"+File.separator+"eMeasure.xsl";
				emeasureXMLPath = parentPath+File.separator+fnu.getEmeasureXMLName(emeasureName + matVersion[0]);
				emeasureHumanReadablePath = parentPath+File.separator+fnu.getEmeasureHumanReadableName(emeasureName + matVersion[0]);
				codeListXLSPath = parentPath+File.separator+fnu.getEmeasureXLSName(emeasureName + matVersion[0],packageDate);
				simpleXMLPath = parentPath+File.separator+fnu.getSimpleXMLName(emeasureName + matVersion[0]);
			} else if(exportDate.after(releaseDate) || exportDate.equals(releaseDate)){
				
				parentPath = fnu.getParentPath(emeasureName + matVersion[1]);
				emeasureXSLPath = parentPath+File.separator+"xslt"+File.separator+"eMeasure.xsl";
				emeasureXMLPath = parentPath+File.separator+fnu.getEmeasureXMLName(emeasureName + matVersion[1]);
				emeasureHumanReadablePath = parentPath+File.separator+fnu.getEmeasureHumanReadableName(emeasureName + matVersion[1]);
				codeListXLSPath = parentPath+File.separator+fnu.getEmeasureXLSName(emeasureName + matVersion[1],packageDate);
				simpleXMLPath = parentPath+File.separator+fnu.getSimpleXMLName(emeasureName + matVersion[1]);
			}
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    ZipOutputStream zip = new ZipOutputStream(baos);
		    
		    addBytesToZip(simpleXMLPath, simpleXmlStr.getBytes(), zip);
			addBytesToZip(emeasureXSLPath, emeasureXSLBarr, zip);
		    addBytesToZip(emeasureXMLPath, emeasureXMLStr.getBytes(), zip);
		    addBytesToZip(emeasureHumanReadablePath, emeasureHTMLStr.getBytes(), zip);
		    addBytesToZip(codeListXLSPath, wkbkbarr, zip);
		    
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
	public void createBulkExportZip(String emeasureName,Date exportDate, Date releaseDate, byte[] wkbkbarr, String emeasureXMLStr, String emeasureHTMLStr,
			String emeasureXSLUrl, String packageDate, String simpleXmlStr, Map<String, byte[]> filesMap, String seqNum) throws Exception{
		
		FileNameUtility fnu = new FileNameUtility();
		try{
//			String parentPath = fnu.getParentPath(seqNum+"_"+emeasureName);
//			String emeasureXSLPath = parentPath+File.separator+"xslt"+File.separator+"eMeasure.xsl";
			
			URL u = new URL(emeasureXSLUrl);
			int contentLength = u.openConnection().getContentLength();
			InputStream openStream = u.openStream();
			byte[] emeasureXSLBarr = new byte[contentLength];
			openStream.read(emeasureXSLBarr);
			openStream.close();

			
//			String emeasureXMLPath = parentPath+File.separator+fnu.getEmeasureXMLName(emeasureName);
//			String emeasureHumanReadablePath = parentPath+File.separator+fnu.getEmeasureHumanReadableName(emeasureName);
//			String codeListXLSPath = parentPath+File.separator+fnu.getEmeasureXLSName(emeasureName,packageDate);
//			String simpleXMLPath = parentPath+File.separator+fnu.getSimpleXMLName(emeasureName);
			
			String parentPath = "";
			String emeasureXSLPath = "";
			String emeasureXMLPath = "";
			String emeasureHumanReadablePath = "";
			String codeListXLSPath = "";
			String simpleXMLPath = "";
            String matVersion[] = {"_v3","_v4"};
			if(exportDate.before(releaseDate)){
				
				parentPath = fnu.getParentPath(seqNum +"_"+ emeasureName + matVersion[0]);
				emeasureXSLPath = parentPath+File.separator+"xslt"+File.separator+"eMeasure.xsl";
				emeasureXMLPath = parentPath+File.separator+fnu.getEmeasureXMLName(emeasureName + matVersion[0]);
				emeasureHumanReadablePath = parentPath+File.separator+fnu.getEmeasureHumanReadableName(emeasureName + matVersion[0]);
				codeListXLSPath = parentPath+File.separator+fnu.getEmeasureXLSName(emeasureName + matVersion[0],packageDate);
				simpleXMLPath = parentPath+File.separator+fnu.getSimpleXMLName(emeasureName + matVersion[0]);
			} else if(exportDate.after(releaseDate) || exportDate.equals(releaseDate) ){
				
				parentPath = fnu.getParentPath(seqNum +"_"+ emeasureName + matVersion[1]);
				emeasureXSLPath = parentPath+File.separator+"xslt"+File.separator+"eMeasure.xsl";
				emeasureXMLPath = parentPath+File.separator+fnu.getEmeasureXMLName(emeasureName + matVersion[1]);
				emeasureHumanReadablePath = parentPath+File.separator+fnu.getEmeasureHumanReadableName(emeasureName + matVersion[1]);
				codeListXLSPath = parentPath+File.separator+fnu.getEmeasureXLSName(emeasureName + matVersion[1],packageDate);
				simpleXMLPath = parentPath+File.separator+fnu.getSimpleXMLName(emeasureName + matVersion[1]);
			}
		   
			filesMap.put(simpleXMLPath, simpleXmlStr.getBytes());
			filesMap.put(emeasureXSLPath, emeasureXSLBarr);
			filesMap.put(emeasureXMLPath, emeasureXMLStr.getBytes());
			filesMap.put(emeasureHumanReadablePath, emeasureHTMLStr.getBytes());
			filesMap.put(codeListXLSPath, wkbkbarr);
		    
		  
		}catch(Exception e){
			System.out.println(e.toString());
			System.out.println(e.fillInStackTrace());
		}
	}
	
	/**
	 * Gets the zip barr.
	 *
	 * @param emeasureName the emeasure name
	 * @param wkbkbarr the wkbkbarr
	 * @param packageDate the package date
	 * @param emeasureHTMLStr the emeasure html str
	 * @param simpleXmlStr the simple xml str
	 * @return the zip barr
	 */
	public byte[] getZipBarr(String emeasureName, byte[] wkbkbarr,
						 String packageDate,String emeasureHTMLStr, String simpleXmlStr, String emeasureXMLStr) {
		byte[] ret = null;
		
		FileNameUtility fnu = new FileNameUtility();
		
		try{

			String parentPath = "";
			String emeasureHumanReadablePath = "";
			String codeListXLSPath = "";
			String simpleXMLPath = "";
			String emeasureXMLPath = "";
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    ZipOutputStream zip = new ZipOutputStream(baos);
			parentPath = fnu.getParentPath(emeasureName +"_v4");
			emeasureHumanReadablePath = parentPath+File.separator+fnu.getEmeasureHumanReadableName(emeasureName + "_v4");
			codeListXLSPath = parentPath+File.separator+fnu.getEmeasureXLSName(emeasureName + "_v4",packageDate);
			simpleXMLPath = parentPath+File.separator+fnu.getSimpleXMLName(emeasureName +"_v4");
//			emeasureXMLPath = parentPath+File.separator+fnu.getEmeasureXMLName(emeasureName + "_v4");
			
		    addBytesToZip(simpleXMLPath, simpleXmlStr.getBytes(), zip);
			addBytesToZip(emeasureHumanReadablePath, emeasureHTMLStr.getBytes(), zip);
		    addBytesToZip(codeListXLSPath, wkbkbarr, zip);
//		    addBytesToZip(emeasureXMLPath,emeasureXMLStr.getBytes(),zip);
		    
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
	 * @throws Exception the exception
	 */
	public void createBulkExportZip(String emeasureName, byte[] wkbkbarr,
			String emeasureXMLStr, String emeasureHTMLStr,
			String packageDate, String simpleXmlStr,
			Map<String, byte[]> filesMap, String seqNum) throws Exception{
		FileNameUtility fnu = new FileNameUtility();

		try{
			String parentPath = "";
			String emeasureHumanReadablePath = "";
			String codeListXLSPath = "";
			String simpleXMLPath = "";
			String emeasureXMLPath = "";
			parentPath = fnu.getParentPath(seqNum +"_"+ emeasureName + "_v4");
			emeasureHumanReadablePath = parentPath+File.separator+fnu.getEmeasureHumanReadableName(emeasureName + "_v4");
			codeListXLSPath = parentPath+File.separator+fnu.getEmeasureXLSName(emeasureName + "_v4",packageDate);
			simpleXMLPath = parentPath+File.separator+fnu.getSimpleXMLName(emeasureName + "_v4");
			emeasureXMLPath = parentPath+File.separator+fnu.getEmeasureXMLName(emeasureName + "_v4");
			filesMap.put(simpleXMLPath, simpleXmlStr.getBytes());
			filesMap.put(emeasureHumanReadablePath, emeasureHTMLStr.getBytes());
			filesMap.put(codeListXLSPath, wkbkbarr);
//			filesMap.put(emeasureXMLPath, emeasureXMLStr.getBytes());
		}catch(Exception e){
			System.out.println(e.toString());
			System.out.println(e.fillInStackTrace());
		}
		
	}	 
	 
}
