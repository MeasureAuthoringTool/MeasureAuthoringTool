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
    public DataRequirementsNoValueSetFilter(List<String> dataRequirementsNoValueSet) {
        this.dataRequirementsNoValueSet = dataRequirementsNoValueSet;
    }

    public List<HumanReadableTerminologyModel> process() {
        if( CollectionUtils.isEmpty(dataRequirementsNoValueSet)) {
            return Collections.emptyList();
        } else {
            return dataRequirementsNoValueSet.stream()
                    .map(this::createHumanReadableTerminologyModel)
                    .collect(Collectors.toList());
        }
    }

    private HumanReadableTerminologyModel createHumanReadableTerminologyModel(String dataRequirementsNoValue) {
        HumanReadableTerminologyModel humanReadableTerminologyModel = new HumanReadableValuesetModel();
        humanReadableTerminologyModel.setDatatype(dataRequirementsNoValue);
        return humanReadableTerminologyModel;
    }
}
