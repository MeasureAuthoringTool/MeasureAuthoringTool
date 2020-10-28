package mat.shared;

import mat.client.measurepackage.MeasurePackageClauseDetail;
import mat.client.shared.MatContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class MeasurePackageClauseValidator {

	public List<String> isValidMeasurePackage(List<MeasurePackageClauseDetail> detailList, String scoring){
		List<String> messages = new ArrayList<>();

		if (ConstantMessages.CONTINUOUS_VARIABLE_SCORING.equalsIgnoreCase(scoring)) {
			messages = checkContinuousVariableGroupings(detailList);
		} else if (ConstantMessages.PROPORTION_SCORING.equalsIgnoreCase(scoring)) {
			messages = checkProportionGroupings(detailList);
		} else if (ConstantMessages.RATIO_SCORING.equalsIgnoreCase(scoring)) {
			messages = checkRatioGroupings(detailList);
		} else if (ConstantMessages.COHORT_SCORING.equalsIgnoreCase(scoring)) {
			messages = checkCohortGroupings(detailList);
		}

		return messages;
	}

	private List<String> checkContinuousVariableGroupings(List<MeasurePackageClauseDetail> detailList){
		List<String> messages = new ArrayList<>();
		if ((countDetailsWithType(detailList, ConstantMessages.POPULATION_CONTEXT_ID) != 1)
				|| (countDetailsWithType(detailList, ConstantMessages.MEASURE_POPULATION_CONTEXT_ID) != 1)
				|| (countDetailsWithType(detailList, ConstantMessages.MEASURE_OBSERVATION_CONTEXT_ID) < 1)) {
			messages.add(MatContext.get().getMessageDelegate().getContinuousVariableWrongNumMessage());
		}

		if ((countDetailsWithType(detailList, ConstantMessages.NUMERATOR_CONTEXT_ID) != 0)
				|| (countDetailsWithType(detailList, ConstantMessages.NUMERATOR_EXCLUSIONS_CONTEXT_ID) != 0)
				|| (countDetailsWithType(detailList, ConstantMessages.DENOMINATOR_CONTEXT_ID) != 0)
				|| (countDetailsWithType(detailList, ConstantMessages.DENOMINATOR_EXCLUSIONS_CONTEXT_ID) != 0)
				|| (countDetailsWithType(detailList, ConstantMessages.DENOMINATOR_EXCEPTIONS_CONTEXT_ID) != 0)) {
			messages.add(MatContext.get().getMessageDelegate().getContinuousVariableMayNotContainMessage());
		}

		return messages;
	}

	private List<String> checkProportionGroupings(List<MeasurePackageClauseDetail> detailList){
		List<String> messages = new ArrayList<>();
		/*
		 * PROPORTION at least one and only one Population,
		 * Denominator at least one or more Numerator zero or
		 * one Denominator Exclusions Denominator Exceptions and
		 * no Numerator Exclusions, Measure Population, Measure
		 * Observations
		 */
		/*
		 * at least one and only one Population, Denominator
		 */
		if ((countDetailsWithType(detailList, ConstantMessages.POPULATION_CONTEXT_ID) != 1)
				|| (countDetailsWithType(detailList, ConstantMessages.DENOMINATOR_CONTEXT_ID) != 1)
				|| (countDetailsWithType(detailList, ConstantMessages.NUMERATOR_CONTEXT_ID) != 1)) {
			messages.add(MatContext.get().getMessageDelegate().getProportionWrongNumMessage());
		}
		/*
		 * zero or one Denominator Exclusions, Denominator Exceptions
		 */
		if ((countDetailsWithType(detailList, ConstantMessages.DENOMINATOR_EXCLUSIONS_CONTEXT_ID) > 1)
				|| (countDetailsWithType(detailList, ConstantMessages.DENOMINATOR_EXCEPTIONS_CONTEXT_ID) > 1)
				|| (countDetailsWithType(detailList, ConstantMessages.NUMERATOR_EXCLUSIONS_CONTEXT_ID) > 1)) {
			messages.add(MatContext.get().getMessageDelegate().getProportionTooManyMessage());
		}
		/* no Numerator Exclusions, Measure Population, Measure Observations */
		if ((countDetailsWithType(detailList, ConstantMessages.MEASURE_POPULATION_CONTEXT_ID) != 0)
				|| (countDetailsWithType(detailList, ConstantMessages.MEASURE_OBSERVATION_CONTEXT_ID) != 0)) {
			messages.add(MatContext.get().getMessageDelegate().getProportionMayNotContainMessage());
		}

		return messages;		
	}

	private List<String> checkRatioGroupings(List<MeasurePackageClauseDetail> detailList){
		List<String> messages = new ArrayList<>();
		/*
		 * at least one and only one Population, Denominator,
		 * Numerator, zero or one Denominator Exclusions and no
		 * Denominator Exceptions, Measure Population
		 * May contain one or more Measure Observation.
		 */
		if ((countDetailsWithType(detailList, ConstantMessages.DENOMINATOR_CONTEXT_ID) != 1)
				|| (countDetailsWithType(detailList, ConstantMessages.NUMERATOR_CONTEXT_ID) != 1)) {
			messages.add(MatContext.get().getMessageDelegate().getRatioWrongNumMessage());
		}
		/*
		 * zero or one Denominator Exclusions
		 */
		if ((countDetailsWithType(detailList, ConstantMessages.DENOMINATOR_EXCLUSIONS_CONTEXT_ID) > 1)
				|| (countDetailsWithType(detailList, ConstantMessages.NUMERATOR_EXCLUSIONS_CONTEXT_ID) > 1)) {
			messages.add(MatContext.get().getMessageDelegate().getRatioTooManyMessage());
		}
		if ((countDetailsWithType(detailList, ConstantMessages.POPULATION_CONTEXT_ID) < 1)) {
			messages.add(MatContext.get().getMessageDelegate().getRATIO_TOO_FEW_POPULATIONS());
		}
		/*
		 * Not more than two populations are allowed.
		 * */
		if ((countDetailsWithType(detailList, ConstantMessages.POPULATION_CONTEXT_ID) > 2)) {
			messages.add(MatContext.get().getMessageDelegate().getRATIO_TOO_MANY_POPULATIONS());
		}
		if ((countDetailsWithType(detailList, ConstantMessages.DENOMINATOR_EXCEPTIONS_CONTEXT_ID) != 0)
				|| (countDetailsWithType(detailList, ConstantMessages.MEASURE_POPULATION_CONTEXT_ID) != 0)) {
			messages.add(MatContext.get().getMessageDelegate().getRatioMayNotContainMessage());
		}
		
		//Check Association for Measure Observation.
		Predicate<MeasurePackageClauseDetail> isMOAssociationMissing = p -> ConstantMessages.MEASURE_OBSERVATION_CONTEXT_ID.equals(p.getType()) && p.getAssociatedPopulationUUID() == null;

		if (detailList.stream().anyMatch(isMOAssociationMissing)) {
			messages.add(MatContext.get().getMessageDelegate().getRatioMeasureObsAssociationRequired());
		}
		
		// In case of two IP's, Denominator and Numerator must contain associations.
		if (countDetailsWithType(detailList, ConstantMessages.POPULATION_CONTEXT_ID) == 2) {
			
			Predicate<MeasurePackageClauseDetail> isIPAssociationMissing = p -> (ConstantMessages.DENOMINATOR_CONTEXT_ID.equals(p.getType()) || ConstantMessages.NUMERATOR_CONTEXT_ID.equals(p.getType()))
					&& (p.getAssociatedPopulationUUID() == null);

			if (detailList.stream().anyMatch(isIPAssociationMissing)) {
				messages.add(MatContext.get().getMessageDelegate().getRatioNumDenoAssociationRequired());
			}

		}

		return messages;
	}

	private List<String> checkCohortGroupings(List<MeasurePackageClauseDetail> detailList){
		List<String> messages = new ArrayList<>();
		if ((countDetailsWithType(detailList, ConstantMessages.POPULATION_CONTEXT_ID) != 1)) {
			messages.add(MatContext.get().getMessageDelegate().getCOHORT_WRONG_NUM());
		}

		return messages;
	}

	/**
	 * Checks if Clauses (right/left) movement into/from package group List is valid.
	 * This is done in order to make sure not more then 1 IP/numerator/denominator is added
	 * for certain measure types as association widget is built with a condition of not holding
	 * more then 2 elements.
	 *
	 * @param validatGroupingList the validate grouping list
	 * @return List of messages.
	 */
	public List<String> isValidClauseMove(List<MeasurePackageClauseDetail> validatGroupingList) {
		List<MeasurePackageClauseDetail> detailList = validatGroupingList;
		List<String> messages = new ArrayList<>();

		String scoring = MatContext.get().getCurrentMeasureScoringType();

		if (ConstantMessages.CONTINUOUS_VARIABLE_SCORING.equalsIgnoreCase(scoring)) {
			messages = checkContinuousVariableMoveFromLeftToRight(detailList);
		} else if (ConstantMessages.PROPORTION_SCORING.equalsIgnoreCase(scoring)) {
			messages = checkProportionMoveFromLeftToRight(detailList);
		} else if (ConstantMessages.RATIO_SCORING.equalsIgnoreCase(scoring)) {
			messages = checkRatioMoveFromLeftToRight(detailList);			
		} else if (ConstantMessages.COHORT_SCORING.equalsIgnoreCase(scoring)) {
			messages = checkCohortMoveFromLeftToRight(detailList);
		}

		return messages;
	}

	private List<String> checkContinuousVariableMoveFromLeftToRight(List<MeasurePackageClauseDetail> detailList){
		List<String> messages = new ArrayList<>();
		if ((countDetailsWithType(detailList, ConstantMessages.POPULATION_CONTEXT_ID) > 1)
				|| (countDetailsWithType(detailList, ConstantMessages.MEASURE_POPULATION_CONTEXT_ID) > 1)) {
			messages.add(MatContext.get().getMessageDelegate().getContinuousVariableWrongNumMessage());
		}

		if ((countDetailsWithType(detailList, ConstantMessages.NUMERATOR_CONTEXT_ID) != 0)
				|| (countDetailsWithType(detailList, ConstantMessages.NUMERATOR_EXCLUSIONS_CONTEXT_ID) != 0)
				|| (countDetailsWithType(detailList, ConstantMessages.DENOMINATOR_CONTEXT_ID) != 0)
				|| (countDetailsWithType(detailList, ConstantMessages.DENOMINATOR_EXCLUSIONS_CONTEXT_ID) != 0)
				|| (countDetailsWithType(detailList, ConstantMessages.DENOMINATOR_EXCEPTIONS_CONTEXT_ID) != 0)) {
			messages.add(MatContext.get().getMessageDelegate().getContinuousVariableMayNotContainMessage());
		}

		return messages;
	}

	private List<String> checkProportionMoveFromLeftToRight(List<MeasurePackageClauseDetail> detailList){
		List<String> messages = new ArrayList<>();
		/*
		 * PROPORTION at least one and only one Population,
		 * Denominator at least one or more Numerator zero or
		 * one Denominator Exclusions Denominator Exceptions and
		 * no Numerator Exclusions, Measure Population, Measure
		 * Observations
		 */
		/*
		 * at least one and only one Population, Denominator and Numerator
		 */
		if ((countDetailsWithType(detailList, ConstantMessages.POPULATION_CONTEXT_ID) > 1)
				|| (countDetailsWithType(detailList, ConstantMessages.DENOMINATOR_CONTEXT_ID) > 1)
				|| (countDetailsWithType(detailList, ConstantMessages.NUMERATOR_CONTEXT_ID) > 1)) {
			messages.add(MatContext.get().getMessageDelegate().getProportionWrongNumMessage());
		}
		/*
		 * zero or one Denominator Exclusions, Denominator Exceptions
		 */
		if ((countDetailsWithType(detailList, ConstantMessages.DENOMINATOR_EXCLUSIONS_CONTEXT_ID) > 1)
				|| (countDetailsWithType(detailList, ConstantMessages.DENOMINATOR_EXCEPTIONS_CONTEXT_ID) > 1)
				|| (countDetailsWithType(detailList, ConstantMessages.NUMERATOR_EXCLUSIONS_CONTEXT_ID) > 1)) {
			messages.add(MatContext.get().getMessageDelegate().getProportionTooManyMessage());
		}
		/* no Numerator Exclusions, Measure Population, Measure Observations */
		if ((countDetailsWithType(detailList, ConstantMessages.MEASURE_POPULATION_CONTEXT_ID) != 0)
				|| (countDetailsWithType(detailList, ConstantMessages.MEASURE_OBSERVATION_CONTEXT_ID) != 0)) {
			messages.add(MatContext.get().getMessageDelegate().getProportionMayNotContainMessage());
		}

		return messages;
	}

	private List<String> checkRatioMoveFromLeftToRight(List<MeasurePackageClauseDetail> detailList){
		List<String> messages = new ArrayList<>();
		/*
		 * at least one and only one Population, Denominator,
		 * Numerator, zero or one Denominator Exclusions and no
		 * Denominator Exceptions, Measure Population
		 * May contain one or more Measure Observation.
		 */
		if ((countDetailsWithType(detailList, ConstantMessages.DENOMINATOR_CONTEXT_ID) > 1)
				|| (countDetailsWithType(detailList, ConstantMessages.NUMERATOR_CONTEXT_ID) > 1)) {
			messages.add(MatContext.get().getMessageDelegate().getRatioWrongNumMessage());
		}
		/*
		 * zero or one Denominator Exclusions
		 */
		if ((countDetailsWithType(detailList, ConstantMessages.DENOMINATOR_EXCLUSIONS_CONTEXT_ID) > 1)
				|| (countDetailsWithType(detailList, ConstantMessages.NUMERATOR_EXCLUSIONS_CONTEXT_ID) > 1)) {
			messages.add(MatContext.get().getMessageDelegate().getRatioTooManyMessage());
		}
		/*
		 * Not more than two populations are allowed.
		 * */
		if ((countDetailsWithType(detailList, ConstantMessages.POPULATION_CONTEXT_ID) > 2)) {
			messages.add(MatContext.get().getMessageDelegate().getRATIO_TOO_MANY_POPULATIONS());
		}
		if ((countDetailsWithType(detailList, ConstantMessages.DENOMINATOR_EXCEPTIONS_CONTEXT_ID) != 0)
				|| (countDetailsWithType(detailList, ConstantMessages.MEASURE_POPULATION_CONTEXT_ID) != 0)) {
			messages.add(MatContext.get().getMessageDelegate().getRatioMayNotContainMessage());
		}

		return messages;
	}

	private List<String> checkCohortMoveFromLeftToRight(List<MeasurePackageClauseDetail> detailList){
		List<String> messages = new ArrayList<>();
		if ((countDetailsWithType(detailList, ConstantMessages.POPULATION_CONTEXT_ID) > 1)) {
			messages.add(MatContext.get().getMessageDelegate().getCOHORT_WRONG_NUM());
		}

		return messages;
	}

	public boolean canMovingPopulationFromRightToLeftAffectAssociations(List<MeasurePackageClauseDetail> detailList){
		return countDetailsWithType(detailList, ConstantMessages.POPULATION_CONTEXT_ID) > 1 ||
				countDetailsWithType(detailList, ConstantMessages.MEASURE_OBSERVATION_CONTEXT_ID) > 0;
	}
	
	private int countDetailsWithType(final List<MeasurePackageClauseDetail> detailList, final String type) {
		return Math.toIntExact(detailList.stream().filter(pop -> pop.getType().equals(type)).count());
	}
	 
}
