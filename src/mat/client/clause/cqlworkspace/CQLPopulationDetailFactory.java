package mat.client.clause.cqlworkspace;

import mat.client.clause.cqlworkspace.CQLPopulationWorkSpaceView.CQLPopulationDetail;
import mat.client.clause.cqlworkspace.model.PopulationDataModel;

public class CQLPopulationDetailFactory {


	public static CQLPopulationDetail getCQLPopulationDetailView(PopulationDataModel populationDataModel,
			String populationType) {
		return new CQLPopulationDetailView(populationDataModel, populationType);
	}
}
