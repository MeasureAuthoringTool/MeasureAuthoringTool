package mat.server.humanreadable.cql;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import mat.model.clause.ModelTypeHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

@Component
public class CQLHumanReadableGenerator {

    @Autowired
    private Configuration freemarkerConfiguration;
	
	public String generate(HumanReadableModel model, boolean isFhir) throws IOException, TemplateException {
		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("model", model);
		paramsMap.put("isFhir", isFhir);
		setMeasurementPeriodForQdm(model.getMeasureInformation());
		return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate("humanreadable/human_readable.ftl"), paramsMap);
	}
	
	public String generateSinglePopulation(HumanReadablePopulationModel population, boolean isFhir) throws IOException, TemplateException {
		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("population", population);
		paramsMap.put("isFhir", isFhir);
		return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate("humanreadable/population_human_readable.ftl"), paramsMap);
	}

	public String generate(HumanReadableMeasureInformationModel measureInformationModel, String measureModel) throws IOException, TemplateException {
		if (!ModelTypeHelper.isFhir(measureModel)) {
		    setMeasurementPeriodForQdm(measureInformationModel);
        } else {
            setMeasurementPeriodForFhir(measureInformationModel);
        }
		HumanReadableModel model = new HumanReadableModel();
		model.setMeasureInformation(measureInformationModel);
		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("model", model);
		paramsMap.put("isFhir", ModelTypeHelper.isFhir(measureModel));
		return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate("humanreadable/human_readable_measuredetails.ftl"), paramsMap);
	}

	private void setMeasurementPeriodForQdm(HumanReadableMeasureInformationModel model) {
		boolean isCalendarYear = model.getIsCalendarYear();
		String measurementPeriodStartDate = model.getMeasurementPeriodStartDate();
		String measurementPeriodEndDate = model.getMeasurementPeriodEndDate();
		model.setMeasurementPeriod(HumanReadableDateUtil.getFormattedMeasurementPeriod(isCalendarYear, measurementPeriodStartDate, measurementPeriodEndDate));
	}

    private void setMeasurementPeriodForFhir(HumanReadableMeasureInformationModel model) {
        String[] startDate = model.getMeasurementPeriodStartDate().split(" ");
        String[] endDate = model.getMeasurementPeriodEndDate().split(" ");
	    model.setMeasurementPeriod(startDate[0] + " through " + endDate[0]);
    }
}
