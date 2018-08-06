package mat.server.service;

import java.util.Date;
import java.util.List;

import mat.model.MatValueSet;
import mat.server.export.ExportResult;

public interface SimpleEMeasureService {	
	ExportResult getSimpleXML(String measureId) throws Exception;
	
	ExportResult getHQMFForV3Measure(String measureId) throws Exception;
	
	ExportResult getEMeasureHTML(String measureId) throws Exception;
	
	ExportResult getEMeasureXLS(String measureId) throws Exception;
	
	ExportResult getEMeasureZIP(String measureId,Date exportDate) throws Exception;
	
	ExportResult getValueSetXLS(String valueSetId) throws Exception;
	
	ExportResult getBulkExportZIP(String[] measureIds, Date[] exportDates) throws Exception;
	
	ExportResult exportMeasureIntoSimpleXML(String measureId, String xmlString, List<MatValueSet> matValueSetList) throws Exception;

	ExportResult getHumanReadableForNode(String measureId, String populationSubXML) throws Exception;

	ExportResult getHumanReadable(String measureId, String currentReleaseVersion) throws Exception;
	
	ExportResult getHQMF(String measureId);

	ExportResult getCQLLibraryFile(String measureId) throws Exception;

	ExportResult getELMFile(String measureId) throws Exception;

	ExportResult getJSONFile(String measureId) throws Exception;
}
