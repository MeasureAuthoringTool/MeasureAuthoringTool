package mat.client.populationworkspace;

import mat.client.populationworkspace.model.PopulationDataModel;
import mat.client.shared.CQLWorkSpaceConstants;

public class CQLPopulationDetailFactory {


	public static CQLPopulationDetail getCQLPopulationDetailView(PopulationDataModel populationDataModel,
			String populationType) {
		
		CQLPopulationDetailView cqlPopulationDetailView = new CQLPopulationDetailView(populationDataModel);
		
		switch(populationType) {
		case CQLWorkSpaceConstants.CQL_INITIALPOPULATION:
			cqlPopulationDetailView.setPopulationsObject(populationDataModel.getInitialPopulationsObject());
			break;
		case CQLWorkSpaceConstants.CQL_NUMERATOR:
			cqlPopulationDetailView.setPopulationsObject(populationDataModel.getNumeratorsObject());
			break;
		case CQLWorkSpaceConstants.CQL_NUMERATOREXCLUSIONS:
			cqlPopulationDetailView.setPopulationsObject(populationDataModel.getNumeratorExclusionsObject());
			break;
		case CQLWorkSpaceConstants.CQL_DENOMINATOR:
			cqlPopulationDetailView.setPopulationsObject(populationDataModel.getDenominatorsObject());
			break;
		case CQLWorkSpaceConstants.CQL_DENOMINATOREXCLUSIONS:
			cqlPopulationDetailView.setPopulationsObject(populationDataModel.getDenominatorExclusionsObject());
			break;
		case CQLWorkSpaceConstants.CQL_DENOMINATOREXCEPTIONS:
			cqlPopulationDetailView.setPopulationsObject(populationDataModel.getDenominatorExceptionsObject());
			break;
		case CQLWorkSpaceConstants.CQL_MEASUREPOPULATIONS:
			cqlPopulationDetailView.setPopulationsObject(populationDataModel.getMeasurePopulationsObject());
			break;
		case CQLWorkSpaceConstants.CQL_MEASUREPOPULATIONEXCLUSIONS:
			cqlPopulationDetailView.setPopulationsObject(populationDataModel.getMeasurePopulationsExclusionsObject());
			break;
		case CQLWorkSpaceConstants.CQL_MEASUREOBSERVATIONS:
			break;	
		case CQLWorkSpaceConstants.CQL_STRATIFICATIONS:
			break;
		}
		
		
		return cqlPopulationDetailView;
	}
	
	
}
