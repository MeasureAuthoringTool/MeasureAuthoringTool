package mat.client.population.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import mat.client.clause.cqlworkspace.model.PopulationsObject;
import mat.client.clause.cqlworkspace.model.StrataDataModel;
import mat.shared.SaveUpdateCQLResult;

public interface PopulationServiceAsync {
	
	void savePopulations(String measureId, PopulationsObject populationsObject,
			AsyncCallback<SaveUpdateCQLResult> callback);

	void saveStratifications(String measureId, StrataDataModel strataDataModel,
			AsyncCallback<SaveUpdateCQLResult> callback);

}
