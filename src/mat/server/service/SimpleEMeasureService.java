package mat.server.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import mat.model.MatValueSet;
import mat.model.clause.ComponentMeasure;
import mat.server.bonnie.api.result.BonnieCalculatedResult;
import mat.server.export.ExportResult;
import mat.shared.bonnie.error.BonnieNotFoundException;
import mat.shared.bonnie.error.BonnieServerException;
import mat.shared.bonnie.error.BonnieUnauthorizedException;

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
	
	BonnieCalculatedResult getBonnieExportCalculation(String measureId, String userId) throws IOException, BonnieUnauthorizedException, BonnieNotFoundException, BonnieServerException;

	ExportResult getCompositeExportResult(String id, List<ComponentMeasure> componentMeasures) throws Exception;
	
}
