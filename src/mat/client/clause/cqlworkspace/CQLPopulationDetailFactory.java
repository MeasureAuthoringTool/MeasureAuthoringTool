package mat.client.clause.cqlworkspace;

import mat.client.clause.cqlworkspace.CQLPopulationWorkSpaceView.CQLPopulationDetail;

public class CQLPopulationDetailFactory {
	private static CQLPopulationDetailFactory instance;
	public static CQLPopulationDetailFactory getInstance() {
		if (instance == null) {
			instance = new CQLPopulationDetailFactory();
		}

		return instance;
	}
	public CQLPopulationDetail getCQLPopulationDetailView(PopulationDataModel populationDataModel, String populationType) {
		return new CQLPopulationDetailView(populationDataModel, populationType);
	}
}
