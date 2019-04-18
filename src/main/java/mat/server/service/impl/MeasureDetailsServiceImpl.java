package mat.server.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import mat.client.measure.ManageMeasureDetailModel;
import mat.model.clause.MeasureDetails;
import mat.model.clause.MeasureDetailsReference;
import mat.server.service.MeasureDetailsService;

public class MeasureDetailsServiceImpl implements MeasureDetailsService {
	
	@Override
	public MeasureDetails getMeasureDetailFromManageMeasureDetailsModel(MeasureDetails measureDetails, ManageMeasureDetailModel model) {
		if( measureDetails == null) {
			measureDetails = new MeasureDetails();
		}
		measureDetails.setDescription(model.getDescription());
		measureDetails.setCopyright(model.getCopyright());
		measureDetails.setDisclaimer(model.getDisclaimer());
		measureDetails.setStratification(model.getStratification());
		measureDetails.setRiskAdjustment(model.getRiskAdjustment());
		measureDetails.setRateAggregation(model.getRateAggregation());
		measureDetails.setRationale(model.getRationale());
		measureDetails.setClinicalRecommendation(model.getClinicalRecomms());
		measureDetails.setImprovementNotation(model.getImprovNotations());
		measureDetails.setDefinition(model.getDefinitions());
		measureDetails.setGuidance(model.getGuidance());
		measureDetails.setTransmissionFormat(model.getTransmissionFormat());
		measureDetails.setInitialPopulation(model.getInitialPop());
		measureDetails.setDenominator(model.getDenominator());
		measureDetails.setDenominatorExclusions(model.getDenominatorExclusions());
		measureDetails.setNumerator(model.getNumerator());
		measureDetails.setNumeratorExclusions(model.getNumeratorExclusions());
		measureDetails.setMeasureObservations(model.getMeasureObservations());
		measureDetails.setMeasurePopulation(model.getMeasurePopulation());
		measureDetails.setDenominatorExceptions(model.getDenominatorExceptions());
		measureDetails.setSupplementalDataElements(model.getSupplementalData());
		measureDetails.setMeasureSet(model.getGroupName());
		
		measureDetails.setMeasureDetailsReference(createReferenceConversion(model.getReferencesList(), measureDetails));
		
		return measureDetails;
	}
	
	private List<MeasureDetailsReference> createReferenceConversion(List<String> newReferencesList, MeasureDetails measureDetails) {
		List<MeasureDetailsReference> references = new ArrayList<>();
		for(int i = 0; i< newReferencesList.size(); i++) {
			references.add(new MeasureDetailsReference(measureDetails, newReferencesList.get(i), i));
		}
		return references;
	}

	@Override
	public ManageMeasureDetailModel getManageMeasureDetailModelFromMeasureDetails(ManageMeasureDetailModel model, MeasureDetails measureDetails) {
		if(measureDetails != null) {
			model.setDescription(measureDetails.getDescription());
			model.setCopyright(measureDetails.getCopyright());
			model.setDisclaimer(measureDetails.getDisclaimer());
			model.setStratification(measureDetails.getStratification());
			model.setRiskAdjustment(measureDetails.getRiskAdjustment());
			model.setRateAggregation(measureDetails.getRateAggregation());
			model.setRationale(measureDetails.getRationale());
			model.setClinicalRecomms(measureDetails.getClinicalRecommendation());
			model.setImprovNotations(measureDetails.getImprovementNotation());
			model.setDefinitions(measureDetails.getDefinition());
			model.setGuidance(measureDetails.getGuidance());
			model.setTransmissionFormat(measureDetails.getTransmissionFormat());
			model.setInitialPop(measureDetails.getInitialPopulation());
			model.setDenominator(measureDetails.getDenominator());
			model.setDenominatorExclusions(measureDetails.getDenominatorExclusions());
			model.setNumerator(measureDetails.getNumerator());
			model.setNumeratorExclusions(measureDetails.getNumeratorExclusions());
			model.setMeasureObservations(measureDetails.getMeasureObservations());
			model.setMeasurePopulation(measureDetails.getMeasurePopulation());
			model.setDenominatorExceptions(measureDetails.getDenominatorExceptions());
			model.setSupplementalData(measureDetails.getSupplementalDataElements());
			model.setGroupName(measureDetails.getMeasureSet());

			model.setReferencesList(measureDetails.getMeasureDetailsReference().stream().map(reference -> reference.getReference()).collect(Collectors.toList()));
		}
		return model;
	}
}
