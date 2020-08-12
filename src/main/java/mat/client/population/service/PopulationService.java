package mat.client.population.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import mat.shared.SaveUpdateCQLResult;

@RemoteServiceRelativePath("populationService")
public interface PopulationService extends RemoteService{
	SaveUpdateCQLResult savePopulations(String measureId, String nodeName, String nodeToReplace);
}
