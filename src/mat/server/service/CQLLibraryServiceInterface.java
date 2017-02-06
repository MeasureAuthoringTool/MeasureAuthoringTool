package mat.server.service;

import java.sql.Timestamp;
import java.util.List;

import mat.model.User;
import mat.model.clause.MeasureSet;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.model.cql.CQLModel;

public interface CQLLibraryServiceInterface {
	
	List<CQLLibraryDataSetObject> search(String searchText, String searchFrom);
	
	void save(String libraryName, String measureId, User owner, MeasureSet measureSet, String version, String releaseVersion, 
			Timestamp finalizedDate, byte[] cqlByteArray);

	CQLLibraryDataSetObject findCQLLibraryByID(String cqlLibraryId);
	public CQLModel save(CQLLibraryDataSetObject cqlLibraryDataSetObject);
	
	
}
