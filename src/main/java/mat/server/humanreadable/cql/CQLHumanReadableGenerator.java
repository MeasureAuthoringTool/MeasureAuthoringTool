package mat.server.humanreadable.cql;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

@Component
public class CQLHumanReadableGenerator {

    @Autowired
    private Configuration freemarkerConfiguration;
	
	public String generate(HumanReadableModel model) throws IOException, TemplateException {
		Map<String, HumanReadableModel> paramsMap = new HashMap<>();	
		paramsMap.put("model", model);
		setMeasurementPeriod(model.getMeasureInformation());
		return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate("humanreadable/human_readable.ftl"), paramsMap);
	}
	
	public String generateSinglePopulation(HumanReadablePopulationModel population) throws IOException, TemplateException {
		Map<String, HumanReadablePopulationModel> paramsMap = new HashMap<>(); 
		paramsMap.put("population", population);
		return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate("humanreadable/population_human_readable.ftl"), paramsMap);
	}

	public String generate(HumanReadableMeasureInformationModel measureInformationModel) throws IOException, TemplateException {
		setMeasurementPeriod(measureInformationModel);
		HumanReadableModel model = new HumanReadableModel();
		model.setMeasureInformation(measureInformationModel);
		Map<String, HumanReadableModel> paramsMap = new HashMap<>();				
		paramsMap.put("model", model);
		return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate("humanreadable/human_readable_measuredetails.ftl"), paramsMap);
	}

	private void setMeasurementPeriod(HumanReadableMeasureInformationModel model) {
		boolean isCalendarYear = model.getIsCalendarYear();
		String measurementPeriodStartDate = model.getMeasurementPeriodStartDate();
		String measurementPeriodEndDate = model.getMeasurementPeriodEndDate();
		model.setMeasurementPeriod(HumanReadableDateUtil.getFormattedMeasurementPeriod(isCalendarYear, measurementPeriodStartDate, measurementPeriodEndDate));
	}
}
