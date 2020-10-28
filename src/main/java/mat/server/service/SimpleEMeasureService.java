package mat.server.service;

import mat.model.clause.ComponentMeasure;
import mat.model.clause.MeasureExport;
import mat.server.bonnie.api.result.BonnieCalculatedResult;
import mat.server.export.ExportResult;
import mat.shared.bonnie.error.BonnieBadParameterException;
import mat.shared.bonnie.error.BonnieDoesNotExistException;
import mat.shared.bonnie.error.BonnieNotFoundException;
import mat.shared.bonnie.error.BonnieServerException;
import mat.shared.bonnie.error.BonnieUnauthorizedException;
import mat.vsacmodel.ValueSet;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface SimpleEMeasureService {	
	ExportResult getSimpleXML(String measureId) throws Exception;
	
	ExportResult getEMeasureHTML(String measureId) throws Exception;
	
	ExportResult getEMeasureXLS(String measureId) throws Exception;
	
	ExportResult getEMeasureZIP(String measureId,Date exportDate) throws Exception;
	
	ExportResult getValueSetXLS(String valueSetId) throws Exception;
	
	ExportResult getBulkExportZIP(String[] measureIds, Date[] exportDates) throws Exception;
	
	ExportResult exportMeasureIntoSimpleXML(String measureId, String xmlString, List<ValueSet> ValueSetList) throws Exception;

	ExportResult getHumanReadableForNode(String measureId, String populationSubXML) throws Exception;

	ExportResult getHumanReadable(String measureId, String currentReleaseVersion) throws Exception;
	
	ExportResult getHQMF(String measureId);

	ExportResult getCQLLibraryFile(String measureId) throws Exception;

	ExportResult getELMFile(String measureId) throws Exception;

	ExportResult getJSONFile(String measureId) throws Exception;
	
	BonnieCalculatedResult getBonnieExportCalculation(String measureId, String userId) throws IOException, BonnieUnauthorizedException, BonnieNotFoundException, BonnieServerException, BonnieBadParameterException, BonnieDoesNotExistException;

	ExportResult getCompositeExportResult(String id, List<ComponentMeasure> componentMeasures) throws Exception;

	MeasureExport getMeasureExport(String id);

	ExportResult createOrGetCQLLibraryFile(String id, MeasureExport measureExport) throws Exception;

	ExportResult createOrGetELMLibraryFile(String id, MeasureExport measureExport) throws Exception;

	ExportResult createOrGetJSONLibraryFile(String id, MeasureExport measureExport) throws Exception;

	ExportResult getMeasureBundleExportResult(MeasureExport measureExport, String filetype);

	ExportResult createOrGetEMeasureHTML(String measureId) throws Exception;

	ExportResult createOrGetHQMFForv3Measure(String measureId);

	ExportResult getHQMFForv3Measure(String measureId) throws Exception;

	ExportResult createOrGetHQMF(String measureId);

	ExportResult createOrGetHumanReadable(String measureId, String measureVersionNumber) throws Exception;
	
}
