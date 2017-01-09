package mat.server.service;

import java.util.List;

import mat.model.cql.CQLLibraryDataSetObject;

public interface CQLLibraryServiceInterface {
	
	List<CQLLibraryDataSetObject> search(String searchText, String searchFrom);
	
	
}
