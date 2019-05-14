package mat.client.populationworkspace;

import mat.shared.ConstantMessages;

public class PopulationTreeBuilderStrategyFactory {
	private final CohortTreeBuildingStrategy cohortTreeBuildingStrategy = new CohortTreeBuildingStrategy();
	private final ContinuousVariableTreeBuildingStrategy continousVariableTreeBuildingStrategy = new ContinuousVariableTreeBuildingStrategy();
	private final RatioTreeBuildingStrategy ratioTreeBuildingStrategy = new RatioTreeBuildingStrategy();
	private final ProportionTreeBuildingStrategy proportionTreeBuildingStrategy = new ProportionTreeBuildingStrategy();

	PopulationTreeBuilderStrategy getPopulationTreeBuilderStrategy(String scoringType) {
		switch(scoringType) {
		case ConstantMessages.COHORT_SCORING:
			return cohortTreeBuildingStrategy;
		case ConstantMessages.CONTINUOUS_VARIABLE_SCORING:
			return continousVariableTreeBuildingStrategy;
		case ConstantMessages.RATIO_SCORING:
			return ratioTreeBuildingStrategy;
		case ConstantMessages.PROPORTION_SCORING:
			return proportionTreeBuildingStrategy;
		}
		return continousVariableTreeBuildingStrategy;
	}
}
