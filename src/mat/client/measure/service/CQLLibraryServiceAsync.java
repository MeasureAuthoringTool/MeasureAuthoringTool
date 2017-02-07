package mat.client.measure.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import mat.model.cql.CQLLibraryDataSetObject;

public interface CQLLibraryServiceAsync {

	void search(String searchText, String searchFrom, AsyncCallback<List<CQLLibraryDataSetObject>> callback);

	void findCQLLibraryByID(String cqlLibraryID, AsyncCallback<CQLLibraryDataSetObject> callback);

	void save(CQLLibraryDataSetObject cqlModel, AsyncCallback<SaveCQLLibraryResult> callback);
	
	

}
