package mat.server.service;

import java.sql.Timestamp;

import mat.client.measure.service.SaveCQLLibraryResult;
import mat.model.User;
import mat.model.clause.MeasureSet;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.server.util.XmlProcessor;
import mat.shared.SaveUpdateCQLResult;

public interface CQLLibraryServiceInterface {
	
	SaveCQLLibraryResult search(String searchText, String searchFrom, int filter,int startIndex, int pageSize);
	
	void save(String libraryName, String measureId, User owner, MeasureSet measureSet, String version, String releaseVersion, 
			Timestamp finalizedDate, byte[] cqlByteArray);

	CQLLibraryDataSetObject findCQLLibraryByID(String cqlLibraryId);
	public SaveCQLLibraryResult save(CQLLibraryDataSetObject cqlLibraryDataSetObject);

	String getCQLLookUpXml(String libraryName, String version,XmlProcessor xmlProcessor);

	String createCQLLookUpTag(String libraryName,String version);

	XmlProcessor loadCQLXmlTemplateFile();
	
	public SaveUpdateCQLResult getCQLData(String id);
	
	
}
