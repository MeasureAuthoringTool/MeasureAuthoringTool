package mat.client.measure.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import mat.model.cql.CQLLibraryDataSetObject;
import mat.shared.SaveUpdateCQLResult;

public interface CQLLibraryServiceAsync {

	void search(String searchText, String searchFrom, int filter,int startIndex, int pageSize,
			AsyncCallback<SaveCQLLibraryResult> callback);

	void findCQLLibraryByID(String cqlLibraryID, AsyncCallback<CQLLibraryDataSetObject> callback);

	void save(CQLLibraryDataSetObject cqlModel, AsyncCallback<SaveCQLLibraryResult> callback);


	void getCQLData(String id, AsyncCallback<SaveUpdateCQLResult> callback);
	
	void isLibraryLocked(String id, AsyncCallback<Boolean> isLocked);
	
	void resetLockedDate(String measureId,String userId, AsyncCallback<SaveCQLLibraryResult> callback);

	void updateLockedDate(String currentLibraryId, String loggedinUserId,AsyncCallback<SaveCQLLibraryResult> asyncCallback);
	

}
