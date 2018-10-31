package mat.server.humanreadable.cql;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

@Component
public class CQLHumanReadableGenerator {

	@Autowired private Configuration freemarkerConfiguration;
	
	public String generate(HumanReadableModel model) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		Map<String, HumanReadableModel> paramsMap = new HashMap<>();				
		paramsMap.put("model", model);
		
		boolean isCalendarYear = model.getMeasureInformation().getIsCalendarYear();
		String measurementPeriodStartDate = model.getMeasureInformation().getMeasurementPeriodStartDate();
		String measurementPeriodEndDate = model.getMeasureInformation().getMeasurementPeriodEndDate();
		model.getMeasureInformation().setMeasurementPeriod(HumanReadableDateUtil.getFormattedMeasurementPeriod(isCalendarYear, measurementPeriodStartDate, measurementPeriodEndDate));
		return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate("humanreadable/human_readable.ftl"), paramsMap);
	}
	
	public String generateSinglePopulation(HumanReadablePopulationModel population) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		Map<String, HumanReadablePopulationModel> paramsMap = new HashMap<>(); 
		paramsMap.put("population", population);
		return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate("humanreadable/population_human_readable.ftl"), paramsMap);
	}
}
