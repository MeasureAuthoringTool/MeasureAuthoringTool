package mat.server;

import mat.client.shared.MatRuntimeException;
import mat.server.spreadsheet.DataType;
import mat.server.spreadsheet.MatAttribute;
import mat.server.spreadsheet.QdmToQicoreMapping;
import mat.server.spreadsheet.RequiredMeasureField;
import mat.server.spreadsheet.ResourceDefinition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MappingSpreadsheetService {
    private static final String MAT_ATTRIBUTES = "/matAttributes";
    private static final String QDM_TO_QICORE_MAPPING = "/qdmToQicoreMappings";
    private static final String DATA_TYPES = "/dataTypes";
    private static final String REQUIRED_MEASURE_FIELDS = "/requiredMeasureFields";
    private static final String RESOURCE_DEFINITION = "/resourceDefinition";

    @Value("${QDM_QICORE_MAPPING_SERVICES_URL:http://localhost:9090}")
    private String fhirMatServicesUrl;

    @Resource
    private MappingSpreadsheetService self;

    @Qualifier("internalRestTemplate")
    private RestTemplate restTemplate;

    public MappingSpreadsheetService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable("spreadSheetfhirTypes")
    public List<String> getFhirTypes() {
        return self.resourceDefinitions().stream().
                filter(r -> StringUtils.isNotBlank(r.getElementId()) &&
                        StringUtils.contains(r.getElementId(), '.')).
                map(r -> r.getElementId().substring(0,r.getElementId().lastIndexOf("."))).
                distinct().
                sorted(String.CASE_INSENSITIVE_ORDER).collect(Collectors.toList());
    }

    @Cacheable("spreadSheetMatAttributes")
    public List<MatAttribute> getMatAttributes() {
        ResponseEntity<MatAttribute[]> response;
        try {
            response = restTemplate.getForEntity(fhirMatServicesUrl + MAT_ATTRIBUTES, MatAttribute[].class);
        } catch (RestClientResponseException e) {
            throw new MatRuntimeException(e);
        }
        return response.getBody() == null ? Collections.emptyList() : Arrays.asList(response.getBody());
    }

    @Cacheable("spreadSheetQdmToQiCoreMapping")
    public List<QdmToQicoreMapping> qdmToQicoreMappings() {
        ResponseEntity<QdmToQicoreMapping[]> response;
        try {
            response = restTemplate.getForEntity(fhirMatServicesUrl + QDM_TO_QICORE_MAPPING, QdmToQicoreMapping[].class);
        } catch (RestClientResponseException e) {
            throw new MatRuntimeException(e);
        }
        return response.getBody() == null ? Collections.emptyList() : Arrays.asList(response.getBody());
    }

    @Cacheable("spreadSheetDataTypes")
    public List<DataType> dataTypes() {
        ResponseEntity<DataType[]> response;
        try {
            response = restTemplate.getForEntity(fhirMatServicesUrl + DATA_TYPES, DataType[].class);
        } catch (RestClientResponseException e) {
            throw new MatRuntimeException(e);
        }
        return response.getBody() == null ? Collections.emptyList() : Arrays.asList(response.getBody());
    }

    @Cacheable("spreadSheetResourceDefinitions")
    public List<ResourceDefinition> resourceDefinitions() {
        ResponseEntity<ResourceDefinition[]> response;
        try {
            response = restTemplate.getForEntity(fhirMatServicesUrl + RESOURCE_DEFINITION, ResourceDefinition[].class);
        } catch (RestClientResponseException e) {
            throw new MatRuntimeException(e);
        }
        return response.getBody() == null ? Collections.emptyList() : Arrays.asList(response.getBody());
    }

    @Cacheable("requiredMeasureFields")
    public List<RequiredMeasureField> requiredMeasureFields() {
        ResponseEntity<RequiredMeasureField[]> response;
        try {
            response = restTemplate.getForEntity(fhirMatServicesUrl + REQUIRED_MEASURE_FIELDS, RequiredMeasureField[].class);
        } catch (RestClientResponseException e) {
            throw new MatRuntimeException(e);
        }
        return response.getBody() == null ? Collections.emptyList() : Arrays.asList(response.getBody());
    }
}
