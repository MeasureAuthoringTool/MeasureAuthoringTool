package org.ifmc.mat.server.service;

import java.io.IOException;

import org.ifmc.mat.server.service.SimpleEMeasureService.ExportResult;


public interface SimpleEMeasureService {
	public static class ExportResult {
		public String measureName;
		public String valueSetName;
		public String packageDate;
		public String export;
		public byte[] wkbkbarr;
		public byte[] zipbarr;
		public String lastModifiedDate;
	}
	
	public ExportResult exportMeasureIntoSimpleXML(String measureId) throws Exception;
	public ExportResult getSimpleXML(String measureId) throws Exception;
	public ExportResult getEMeasureXML(String measureId) throws Exception;
	public ExportResult getEMeasureHTML(String measureId) throws Exception;
	public ExportResult getEMeasureXLS(String measureId) throws Exception;
	public ExportResult getEMeasureZIP(String measureId) throws Exception;
	public ExportResult getValueSetXLS(String valueSetId) throws Exception;
}
