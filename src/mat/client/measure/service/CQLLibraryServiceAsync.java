package mat.client.measure.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import mat.model.clause.CQLLibrary;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.model.cql.CQLModel;

public interface CQLLibraryServiceAsync {

	void search(String searchText, String searchFrom, AsyncCallback<List<CQLLibraryDataSetObject>> callback);

	void findCQLLibraryByID(String cqlLibraryID, AsyncCallback<CQLLibraryDataSetObject> callback);

	void save(CQLLibraryDataSetObject cqlLibraryDataSetObject, AsyncCallback<CQLModel> callback);
	
	

}
