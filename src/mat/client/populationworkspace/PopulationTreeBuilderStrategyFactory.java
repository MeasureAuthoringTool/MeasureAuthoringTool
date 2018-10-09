package mat.client.populationworkspace;

import mat.client.clause.clauseworkspace.presenter.PopulationWorkSpaceConstants;

public class PopulationTreeBuilderStrategyFactory {
	private final CohortTreeBuildingStrategy cohortTreeBuildingStrategy = new CohortTreeBuildingStrategy();
	private final ContinuousVariableTreeBuildingStrategy continousVariableTreeBuildingStrategy = new ContinuousVariableTreeBuildingStrategy();
	private final RatioTreeBuildingStrategy ratioTreeBuildingStrategy = new RatioTreeBuildingStrategy();
	private final ProportionTreeBuildingStrategy proportionTreeBuildingStrategy = new ProportionTreeBuildingStrategy();

	PopulationTreeBuilderStrategy getPopulationTreeBuilderStrategy(String scoringType) {
		switch(scoringType) {
		case PopulationWorkSpaceConstants.SCORING_TYPE_COHORT:
			return cohortTreeBuildingStrategy;
		case PopulationWorkSpaceConstants.SCORING_TYPE_CONTINOUS_VARIABLE:
			return continousVariableTreeBuildingStrategy;
		case PopulationWorkSpaceConstants.SCORING_TYPE_RATIO:
			return ratioTreeBuildingStrategy;
		case PopulationWorkSpaceConstants.SCORING_TYPE_PROPORTION:
			return proportionTreeBuildingStrategy;
		}
		return continousVariableTreeBuildingStrategy;
	}
}
