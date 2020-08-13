package mat.client.population.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import mat.shared.SaveUpdateCQLResult;

public interface PopulationServiceAsync {

	void savePopulations(String measureId, String nodeName, String nodeToReplace,
			AsyncCallback<SaveUpdateCQLResult> callback);

}
