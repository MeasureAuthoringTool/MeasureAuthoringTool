package mat.client.measure.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import mat.client.cql.ManageCQLLibrarySearchModel;
import mat.model.clause.CQLLibrary;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.model.cql.CQLModel;

public interface CQLLibraryServiceAsync {

	void search(String searchText, String searchFrom, int startIndex, int pageSize,
			AsyncCallback<ManageCQLLibrarySearchModel> callback);

	void findCQLLibraryByID(String cqlLibraryID, AsyncCallback<CQLLibraryDataSetObject> callback);

	void save(CQLLibraryDataSetObject cqlModel, AsyncCallback<SaveCQLLibraryResult> callback);
	
	

}
