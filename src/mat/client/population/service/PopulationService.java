package mat.client.population.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import mat.client.clause.cqlworkspace.model.PopulationsObject;
import mat.shared.SaveUpdateCQLResult;

@RemoteServiceRelativePath("populationService")
public interface PopulationService extends RemoteService{
	SaveUpdateCQLResult savePopulations(String measureId, PopulationsObject populationsObject);
}
