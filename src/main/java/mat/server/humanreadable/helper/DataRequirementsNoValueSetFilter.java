package mat.server.humanreadable.helper;

import mat.server.humanreadable.cql.HumanReadableTerminologyModel;
import mat.server.humanreadable.cql.HumanReadableValuesetModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DataRequirementsNoValueSetFilter {
    private final List<String> dataRequirementsNoValueSet;
    private final List<HumanReadableTerminologyModel> valuesetAndCodeDataCriteriaList;

    public DataRequirementsNoValueSetFilter(List<String> dataRequirementsNoValueSet, List<HumanReadableTerminologyModel> valuesetAndCodeDataCriteriaList) {
        this.dataRequirementsNoValueSet = dataRequirementsNoValueSet;
        this.valuesetAndCodeDataCriteriaList = valuesetAndCodeDataCriteriaList;
    }

    public List<HumanReadableTerminologyModel> process() {
        if( CollectionUtils.isEmpty(dataRequirementsNoValueSet)) {
            return Collections.emptyList();
        } else {
            return dataRequirementsNoValueSet.stream()
                    .filter(this::isNotAlreadyInCriteriaList)
                    .map(this::createHumanReadableTerminologyModel)
                    .collect(Collectors.toList());
        }
    }

    private HumanReadableTerminologyModel createHumanReadableTerminologyModel(String dataRequirementsNoValue) {
        HumanReadableTerminologyModel humanReadableTerminologyModel = new HumanReadableValuesetModel();
        humanReadableTerminologyModel.setDatatype(dataRequirementsNoValue);
        return humanReadableTerminologyModel;
    }

    private boolean isNotAlreadyInCriteriaList(String dataRequirementsNoValue) {
        if (CollectionUtils.isEmpty(valuesetAndCodeDataCriteriaList)) {
            return true;
        } else {
            return valuesetAndCodeDataCriteriaList.stream()
                    .filter(v -> StringUtils.isNotBlank(v.getDatatype()))
                    .filter(v -> v.getDatatype().equals(dataRequirementsNoValue))
                    .findAny().isEmpty();

        }
    }
}
