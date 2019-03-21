package mat.server.humanreadable.cql;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import mat.shared.StringUtility;

@Component
public class CQLHumanReadableGenerator {

	@Autowired private Configuration freemarkerConfiguration;
	
	public String generate(HumanReadableModel model) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		Map<String, HumanReadableModel> paramsMap = new HashMap<>();	
		escapeHTMLCharacters(model.getMeasureInformation());
		paramsMap.put("model", model);
		setMeasurementPeriod(model.getMeasureInformation());
		return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate("humanreadable/human_readable.ftl"), paramsMap);
	}

	
	public String generateSinglePopulation(HumanReadablePopulationModel population) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		Map<String, HumanReadablePopulationModel> paramsMap = new HashMap<>(); 
		paramsMap.put("population", population);
		return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate("humanreadable/population_human_readable.ftl"), paramsMap);
	}


	public String generate(HumanReadableMeasureInformationModel measureInformationModel) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		setMeasurementPeriod(measureInformationModel);
		HumanReadableModel model = new HumanReadableModel();
		model.setMeasureInformation(measureInformationModel);
		Map<String, HumanReadableModel> paramsMap = new HashMap<>();				
		paramsMap.put("model", model);
		return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate("humanreadable/human_readable_measuredetails.ftl"), paramsMap);
	}


	private void setMeasurementPeriod(HumanReadableMeasureInformationModel model) {
		escapeHTMLCharacters(model);
		boolean isCalendarYear = model.getIsCalendarYear();
		String measurementPeriodStartDate = model.getMeasurementPeriodStartDate();
		String measurementPeriodEndDate = model.getMeasurementPeriodEndDate();
		model.setMeasurementPeriod(HumanReadableDateUtil.getFormattedMeasurementPeriod(isCalendarYear, measurementPeriodStartDate, measurementPeriodEndDate));
	}
	
	private void escapeHTMLCharacters(HumanReadableMeasureInformationModel measureInformationModel) {
		measureInformationModel.setClinicalRecommendationStatement(StringUtility.escapeHtml(measureInformationModel.getClinicalRecommendationStatement()));
		measureInformationModel.setCopyright(StringUtility.escapeHtml(measureInformationModel.getCopyright()));
		measureInformationModel.setDefinition(StringUtility.escapeHtml(measureInformationModel.getDefinition()));
		measureInformationModel.setDenominatorExceptions(StringUtility.escapeHtml(measureInformationModel.getDenominatorExceptions()));
		measureInformationModel.setDenominatorExclusions(StringUtility.escapeHtml(measureInformationModel.getDenominatorExclusions()));
		measureInformationModel.setDenominator(StringUtility.escapeHtml(measureInformationModel.getDenominator()));
		measureInformationModel.setDescription(StringUtility.escapeHtml(measureInformationModel.getDescription()));
		measureInformationModel.setDisclaimer(StringUtility.escapeHtml(measureInformationModel.getDisclaimer()));
		measureInformationModel.setGuidance(StringUtility.escapeHtml(measureInformationModel.getGuidance()));
		measureInformationModel.setImprovementNotation(StringUtility.escapeHtml(measureInformationModel.getImprovementNotation()));
		measureInformationModel.setMeasureObservations(StringUtility.escapeHtml(measureInformationModel.getMeasureObservations()));
		measureInformationModel.setMeasurePopulationExclusions(StringUtility.escapeHtml(measureInformationModel.getMeasurePopulationExclusions()));
		measureInformationModel.setMeasurePopulation(StringUtility.escapeHtml(measureInformationModel.getMeasurePopulation()));
		measureInformationModel.setMeasureSet(StringUtility.escapeHtml(measureInformationModel.getMeasureSet()));
		measureInformationModel.setNumeratorExclusions(StringUtility.escapeHtml(measureInformationModel.getNumeratorExclusions()));
		measureInformationModel.setNumerator(StringUtility.escapeHtml(measureInformationModel.getNumerator()));
		measureInformationModel.setRateAggregation(StringUtility.escapeHtml(measureInformationModel.getRateAggregation()));
		measureInformationModel.setRationale(StringUtility.escapeHtml(measureInformationModel.getRationale()));
		measureInformationModel.setRiskAdjustment(StringUtility.escapeHtml(measureInformationModel.getRiskAdjustment()));
		measureInformationModel.setStratification(StringUtility.escapeHtml(measureInformationModel.getStratification()));
		measureInformationModel.setSupplementalDataElements(StringUtility.escapeHtml(measureInformationModel.getSupplementalDataElements()));
		measureInformationModel.setTransmissionFormat(StringUtility.escapeHtml(measureInformationModel.getTransmissionFormat()));
		List<String> references = measureInformationModel.getReferences();
		if(references != null) {
			for(int i = 0; i<references.size(); i++) {
				String curRef = StringUtility.escapeHtml(references.get(i));
				references.set(i, curRef);
			}
			measureInformationModel.setReferences(references);
		}
	}
}
