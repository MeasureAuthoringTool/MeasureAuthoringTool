package mat.client.clause.cqlworkspace;

import mat.client.clause.cqlworkspace.CQLPopulationWorkSpaceView.CQLPopulationDetail;
import mat.client.clause.cqlworkspace.model.PopulationDataModel;

public class CQLPopulationDetailFactory {

	/*private static CQLPopulationDetailFactory instance;

	private CQLPopulationDetailFactory() {
		// preventing CQLPopulationDetailFactory object instantiation from outside
	}

	public static synchronized CQLPopulationDetailFactory getInstance() {
		if (instance == null) {
			instance = new CQLPopulationDetailFactory();
		}

		return instance;
	}*/

	public static CQLPopulationDetail getCQLPopulationDetailView(PopulationDataModel populationDataModel,
			String populationType) {
		return new CQLPopulationDetailView(populationDataModel, populationType);
	}
}
