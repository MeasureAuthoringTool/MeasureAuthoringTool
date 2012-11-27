package org.ifmc.mat.server.service.impl;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.ifmc.mat.shared.FileNameUtility;

/**
 * User Story ID 357 
 * Delegate creation of a zip file containing eMeasure artifacts for export
 * used by SimpleEMeasureServiceImpl.java
 * @author aschmidt
 *
 */
public class ZipPackager {
	
	public byte[] getZipBarr(String emeasureName, byte[] wkbkbarr, String emeasureXMLStr, String emeasureHTMLStr, String emeasureXSLUrl, String packageDate) throws Exception{
		byte[] ret = null;
		
		FileNameUtility fnu = new FileNameUtility();
		
		try{
			String parentPath = fnu.getParentPath(emeasureName);
			String emeasureXSLPath = parentPath+File.separator+"xslt"+File.separator+"eMeasure.xsl";
			
			URL u = new URL(emeasureXSLUrl);
			int contentLength = u.openConnection().getContentLength();
			InputStream openStream = u.openStream();
			byte[] emeasureXSLBarr = new byte[contentLength];
			openStream.read(emeasureXSLBarr);
			openStream.close();

			
			String emeasureXMLPath = parentPath+File.separator+fnu.getEmeasureXMLName(emeasureName);
			String emeasureHumanReadablePath = parentPath+File.separator+fnu.getEmeasureHumanReadableName(emeasureName);
			String codeListXLSPath = parentPath+File.separator+fnu.getEmeasureXLSName(emeasureName,packageDate);
			
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    ZipOutputStream zip = new ZipOutputStream(baos);
		    
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
	private void addBytesToZip(String path, byte[] input, ZipOutputStream zip) throws Exception {
		ZipEntry entry = new ZipEntry(path);
        entry.setSize(input.length);
        zip.putNextEntry(entry);
        zip.write(input);
        zip.closeEntry();
	}
}
