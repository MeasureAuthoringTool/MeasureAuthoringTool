package mat.client.measure.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import mat.model.clause.CQLLibrary;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.model.cql.CQLLibraryModel;
import mat.model.cql.CQLModel;

@RemoteServiceRelativePath("cqlLibrary")
public interface CQLLibraryService extends RemoteService {
	 List<CQLLibraryDataSetObject> search(String searchText,String searchFrom);

	CQLLibraryDataSetObject findCQLLibraryByID(String cqlLibraryID);
    CQLModel save(CQLLibraryDataSetObject cqlModel);

}
