package mat.client.measure.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import mat.client.cql.ManageCQLLibrarySearchModel;
import mat.model.clause.CQLLibrary;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.model.cql.CQLLibraryModel;
import mat.model.cql.CQLModel;

@RemoteServiceRelativePath("cqlLibrary")
public interface CQLLibraryService extends RemoteService {
	 ManageCQLLibrarySearchModel search(String searchText, String searchFrom, int startIndex, int pageSize);

	CQLLibraryDataSetObject findCQLLibraryByID(String cqlLibraryID);
	SaveCQLLibraryResult save(CQLLibraryDataSetObject cqlModel);

}
