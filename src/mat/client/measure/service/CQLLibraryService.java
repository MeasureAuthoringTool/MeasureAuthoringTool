package mat.client.measure.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import mat.client.cql.ManageCQLLibrarySearchModel;
import mat.model.cql.CQLLibraryDataSetObject;

@RemoteServiceRelativePath("cqlLibrary")
public interface CQLLibraryService extends RemoteService {
	 ManageCQLLibrarySearchModel search(String searchText, String searchFrom, int startIndex, int pageSize);

	CQLLibraryDataSetObject findCQLLibraryByID(String cqlLibraryID);
	SaveCQLLibraryResult save(CQLLibraryDataSetObject cqlModel);

}
