package mat.shared;

import java.util.ArrayList;
import java.util.List;
import mat.client.measurepackage.MeasurePackageClauseDetail;
import mat.client.shared.MatContext;

public class MeasurePackageClauseValidator {
	
	public List<String> isValidMeasurePackage(List<MeasurePackageClauseDetail> detailList){
		List<String> messages = new ArrayList<String>();
		String scoring = MatContext.get().getCurrentMeasureScoringType();
		
		if (ConstantMessages.CONTINUOUS_VARIABLE_SCORING
				.equalsIgnoreCase(scoring)) {
			if ((countDetailsWithType(detailList,
					ConstantMessages.POPULATION_CONTEXT_ID) != 1)
					|| (countDetailsWithType(detailList,
							ConstantMessages.MEASURE_POPULATION_CONTEXT_ID) != 1)
							|| (countDetailsWithType(detailList,
									ConstantMessages.MEASURE_OBSERVATION_CONTEXT_ID) < 1)
					) {
				messages.add(MatContext.get().getMessageDelegate()
						.getContinuousVariableWrongNumMessage());
			}
			
			if ((countDetailsWithType(detailList,
					ConstantMessages.NUMERATOR_CONTEXT_ID) != 0)
					|| (countDetailsWithType(detailList,
							ConstantMessages.NUMERATOR_EXCLUSIONS_CONTEXT_ID) != 0)
							|| (countDetailsWithType(detailList,
									ConstantMessages.DENOMINATOR_CONTEXT_ID) != 0)
									|| (countDetailsWithType(detailList,
											ConstantMessages.DENOMINATOR_EXCLUSIONS_CONTEXT_ID) != 0)
											|| (countDetailsWithType(detailList,
													ConstantMessages.DENOMINATOR_EXCEPTIONS_CONTEXT_ID) != 0)) {
				messages.add(MatContext.get().getMessageDelegate()
						.getContinuousVariableMayNotContainMessage());
			}
			
		} else if (ConstantMessages.PROPORTION_SCORING.equalsIgnoreCase(scoring)) { /*
		 * PROPORTION at least one and only one Population,
		 * Denominator at least one or more Numerator zero or
		 * one Denominator Exclusions Denominator Exceptions and
		 * no Numerator Exclusions, Measure Population, Measure
		 * Observations
		 */
			/*
			 * at least one and only one Population, Denominator
			 */
			if ((countDetailsWithType(detailList,
					ConstantMessages.POPULATION_CONTEXT_ID) != 1)
					|| (countDetailsWithType(detailList,
							ConstantMessages.DENOMINATOR_CONTEXT_ID) != 1)
							|| (countDetailsWithType(detailList,
									ConstantMessages.NUMERATOR_CONTEXT_ID) != 1)
					) {
				messages.add(MatContext.get().getMessageDelegate()
						.getProportionWrongNumMessage());
			}
			/*
			 * zero or one Denominator Exclusions, Denominator Exceptions
			 */
			if ((countDetailsWithType(detailList,
					ConstantMessages.DENOMINATOR_EXCLUSIONS_CONTEXT_ID) > 1)
					|| (countDetailsWithType(detailList,
							ConstantMessages.DENOMINATOR_EXCEPTIONS_CONTEXT_ID) > 1)) {
				messages.add(MatContext.get().getMessageDelegate()
						.getProportionTooManyMessage());
			}
			/* no Numerator Exclusions, Measure Population, Measure Observations */
			if ((countDetailsWithType(detailList,
					ConstantMessages.MEASURE_POPULATION_CONTEXT_ID) != 0)
					|| (countDetailsWithType(detailList,
							ConstantMessages.MEASURE_OBSERVATION_CONTEXT_ID) != 0)) {
				messages.add(MatContext.get().getMessageDelegate()
						.getProportionMayNotContainMessage());
			}
		} else if (ConstantMessages.RATIO_SCORING.equalsIgnoreCase(scoring)) { /*
		 * at least one and only one Population, Denominator,
		 * Numerator, zero or one Denominator Exclusions and no
		 * Denominator Exceptions, Measure Population
		 * May contain one or more Measure Observation.
		 */
			if ((countDetailsWithType(detailList,
					ConstantMessages.DENOMINATOR_CONTEXT_ID) != 1)
					|| (countDetailsWithType(detailList,
							ConstantMessages.NUMERATOR_CONTEXT_ID) != 1)
					) {
				messages.add(MatContext.get().getMessageDelegate()
						.getRatioWrongNumMessage());
			}
			/*
			 * zero or one Denominator Exclusions
			 */
			if ((countDetailsWithType(detailList,
					ConstantMessages.DENOMINATOR_EXCLUSIONS_CONTEXT_ID) > 1)
					|| (countDetailsWithType(detailList,
							ConstantMessages.NUMERATOR_EXCLUSIONS_CONTEXT_ID) > 1)) {
				messages.add(MatContext.get().getMessageDelegate()
						.getRatioTooManyMessage());
			}
			if ((countDetailsWithType(detailList,
					ConstantMessages.POPULATION_CONTEXT_ID) < 1)) {
				messages.add(MatContext.get().getMessageDelegate().getRATIO_TOO_FEW_POPULATIONS());
			}
			/*
			 * Not more than two populations are allowed.
			 * */
			if ((countDetailsWithType(detailList,
					ConstantMessages.POPULATION_CONTEXT_ID) > 2)) {
				messages.add(MatContext.get().getMessageDelegate().getRATIO_TOO_MANY_POPULATIONS());
			}
			if ((countDetailsWithType(detailList,
					ConstantMessages.DENOMINATOR_EXCEPTIONS_CONTEXT_ID) != 0)
					|| (countDetailsWithType(detailList,
							ConstantMessages.MEASURE_POPULATION_CONTEXT_ID) != 0)) {
				messages.add(MatContext.get().getMessageDelegate()
						.getRatioMayNotContainMessage());
			}
			//Check Association for Measure Observation.
			for (MeasurePackageClauseDetail detail : detailList) {
				if((ConstantMessages.MEASURE_OBSERVATION_CONTEXT_ID).equals(detail.getType())){
					if ((detail.getAssociatedPopulationUUID() == null)) {
						messages.add(MatContext.get().getMessageDelegate()
								.getRatioMeasureObsAssociationRequired());
						break;
					}
				}
			}
			// In case of two IP's, Denominator and Numerator must contain associations.
			int iPCount = (countDetailsWithType(detailList,
					ConstantMessages.POPULATION_CONTEXT_ID));
			if (iPCount == 2) {
				for (MeasurePackageClauseDetail detail : detailList) {
					if ((ConstantMessages.DENOMINATOR_CONTEXT_ID).equals(detail.getType())
							|| (ConstantMessages.NUMERATOR_CONTEXT_ID).equals(detail.getType())) {
						if ((detail.getAssociatedPopulationUUID() == null)) {
							messages.add(MatContext.get().getMessageDelegate()
									.getRatioNumDenoAssociationRequired());
							break;
						}
					}
				}
			}
		} else if (ConstantMessages.COHORT_SCORING.equalsIgnoreCase(scoring)) {
			if ((countDetailsWithType(detailList,
					ConstantMessages.POPULATION_CONTEXT_ID) != 1)
					) {
				messages.add(MatContext.get().getMessageDelegate().getCOHORT_WRONG_NUM());
			}
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
		List<String> messages = new ArrayList<String>();
		
		String scoring = MatContext.get().getCurrentMeasureScoringType();
		
		// TODO refactor this into a common shared class so the server can use
		// it for validation also
		if (ConstantMessages.CONTINUOUS_VARIABLE_SCORING
				.equalsIgnoreCase(scoring)) {
			if ((countDetailsWithType(detailList,
					ConstantMessages.POPULATION_CONTEXT_ID) > 1)
					|| (countDetailsWithType(detailList,
							ConstantMessages.MEASURE_POPULATION_CONTEXT_ID) > 1)
					) {
				messages.add(MatContext.get().getMessageDelegate()
						.getContinuousVariableWrongNumMessage());
			}
			
			if ((countDetailsWithType(detailList,
					ConstantMessages.NUMERATOR_CONTEXT_ID) != 0)
					|| (countDetailsWithType(detailList,
							ConstantMessages.NUMERATOR_EXCLUSIONS_CONTEXT_ID) != 0)
							|| (countDetailsWithType(detailList,
									ConstantMessages.DENOMINATOR_CONTEXT_ID) != 0)
									|| (countDetailsWithType(detailList,
											ConstantMessages.DENOMINATOR_EXCLUSIONS_CONTEXT_ID) != 0)
											|| (countDetailsWithType(detailList,
													ConstantMessages.DENOMINATOR_EXCEPTIONS_CONTEXT_ID) != 0)) {
				messages.add(MatContext.get().getMessageDelegate()
						.getContinuousVariableMayNotContainMessage());
			}
			
		} else if (ConstantMessages.PROPORTION_SCORING.equalsIgnoreCase(scoring)) { /*
		 * PROPORTION at least one and only one Population,
		 * Denominator at least one or more Numerator zero or
		 * one Denominator Exclusions Denominator Exceptions and
		 * no Numerator Exclusions, Measure Population, Measure
		 * Observations
		 */
			/*
			 * at least one and only one Population, Denominator and Numerator
			 */
			if ((countDetailsWithType(detailList,
					ConstantMessages.POPULATION_CONTEXT_ID) > 1)
					|| (countDetailsWithType(detailList,
							ConstantMessages.DENOMINATOR_CONTEXT_ID) > 1)
							|| (countDetailsWithType(detailList,
									ConstantMessages.NUMERATOR_CONTEXT_ID) > 1)
					) {
				messages.add(MatContext.get().getMessageDelegate()
						.getProportionWrongNumMessage());
			}
			/*
			 * zero or one Denominator Exclusions, Denominator Exceptions
			 */
			if ((countDetailsWithType(detailList,
					ConstantMessages.DENOMINATOR_EXCLUSIONS_CONTEXT_ID) > 1)
					|| (countDetailsWithType(detailList,
							ConstantMessages.DENOMINATOR_EXCEPTIONS_CONTEXT_ID) > 1)) {
				messages.add(MatContext.get().getMessageDelegate()
						.getProportionTooManyMessage());
			}
			/* no Numerator Exclusions, Measure Population, Measure Observations */
			if ((countDetailsWithType(detailList,
					ConstantMessages.MEASURE_POPULATION_CONTEXT_ID) != 0)
					|| (countDetailsWithType(detailList,
							ConstantMessages.MEASURE_OBSERVATION_CONTEXT_ID) != 0)) {
				messages.add(MatContext.get().getMessageDelegate()
						.getProportionMayNotContainMessage());
			}
		} else if (ConstantMessages.RATIO_SCORING.equalsIgnoreCase(scoring)) { /*
		 * at least one and only one Population, Denominator,
		 * Numerator, zero or one Denominator Exclusions and no
		 * Denominator Exceptions, Measure Population
		 * May contain one or more Measure Observation.
		 */
			if ((countDetailsWithType(detailList,
					ConstantMessages.DENOMINATOR_CONTEXT_ID) > 1)
					|| (countDetailsWithType(detailList,
							ConstantMessages.NUMERATOR_CONTEXT_ID) > 1)
					) {
				messages.add(MatContext.get().getMessageDelegate()
						.getRatioWrongNumMessage());
			}
			/*
			 * zero or one Denominator Exclusions
			 */
			if ((countDetailsWithType(detailList,
					ConstantMessages.DENOMINATOR_EXCLUSIONS_CONTEXT_ID) > 1)
					|| (countDetailsWithType(detailList,
							ConstantMessages.NUMERATOR_EXCLUSIONS_CONTEXT_ID) > 1)) {
				messages.add(MatContext.get().getMessageDelegate()
						.getRatioTooManyMessage());
			}
			/*
			 * Not more than two populations are allowed.
			 * */
			if ((countDetailsWithType(detailList,
					ConstantMessages.POPULATION_CONTEXT_ID) > 2)) {
				messages.add(MatContext.get().getMessageDelegate().getRATIO_TOO_MANY_POPULATIONS());
			}
			if ((countDetailsWithType(detailList,
					ConstantMessages.DENOMINATOR_EXCEPTIONS_CONTEXT_ID) != 0)
					|| (countDetailsWithType(detailList,
							ConstantMessages.MEASURE_POPULATION_CONTEXT_ID) != 0)) {
				messages.add(MatContext.get().getMessageDelegate()
						.getRatioMayNotContainMessage());
			}
		} else if (ConstantMessages.COHORT_SCORING.equalsIgnoreCase(scoring)) {
			if ((countDetailsWithType(detailList,
					ConstantMessages.POPULATION_CONTEXT_ID) > 1)
					) {
				messages.add(MatContext.get().getMessageDelegate().getCOHORT_WRONG_NUM());
			}
		}
		return messages;
	}
	
	public boolean hasAssociation(List<MeasurePackageClauseDetail> detailList){
		return false;
		
	}
	
	/**
	 * countDetailsWithType.
	 * @param detailList - List of MeasurePackageClauseDetail.
	 * @param type - String.
	 *
	 * @return Integer.
	 */
	private int countDetailsWithType(
			final List<MeasurePackageClauseDetail> detailList, final String type) {
		int count = 0;
		for (MeasurePackageClauseDetail detail : detailList) {
			if (type.equals(detail.getType())) {
				count++;
			}
		}
		return count;
	}
}
