package mat.server;

import mat.client.shared.MatRuntimeException;
import mat.server.spreadsheet.DataType;
import mat.client.shared.FhirDatatypeAttributeAssociation;
import mat.server.spreadsheet.MatAttribute;
import mat.server.spreadsheet.QdmToQicoreMapping;
import mat.server.spreadsheet.RequiredMeasureField;
import mat.server.spreadsheet.ResourceDefinition;
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

@Service
public class MappingSpreadsheetService {
    private static final String MAT_ATTRIBUTES = "/matAttributes";
    private static final String QDM_TO_QICORE_MAPPING = "/qdmToQicoreMappings";
    private static final String DATA_TYPES = "/dataTypes";
    private static final String REQUIRED_MEASURE_FIELDS = "/requiredMeasureFields";
    private static final String RESOURCE_DEFINITION = "/resourceDefinition";
    private static final String TYPES_FOR_FUNCTION_ARGS = "/fhirLightboxDataTypesForFunctionArgs";
    private static final String FHIR_DATATYPE_ATTRIBUTE_ASSOCIATION = "/fhirLightBoxDatatypeAttributeAssociation";

    @Qualifier("internalRestTemplate")
    private final RestTemplate restTemplate;
    @Value("${QDM_QICORE_MAPPING_SERVICES_URL:http://localhost:9090}")
    private String fhirMatMappingServicesUrl;
    @Resource
    private MappingSpreadsheetService self;

    public MappingSpreadsheetService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable("spreadSheetfhirTypes")
    public List<String> getFhirTypes() {
        return typesForFunctionArgs(); // rest call sorts list
    }

    @Cacheable("spreadSheetMatAttributes")
    public List<MatAttribute> getMatAttributes() {
        ResponseEntity<MatAttribute[]> response;
        try {
            response = restTemplate.getForEntity(fhirMatMappingServicesUrl + MAT_ATTRIBUTES, MatAttribute[].class);
        } catch (RestClientResponseException e) {
            throw new MatRuntimeException(e);
        }
        return response.getBody() == null ? Collections.emptyList() : Arrays.asList(response.getBody());
    }

    @Cacheable("spreadSheetQdmToQiCoreMapping")
    public List<QdmToQicoreMapping> qdmToQicoreMappings() {
        ResponseEntity<QdmToQicoreMapping[]> response;
        try {
            response = restTemplate.getForEntity(fhirMatMappingServicesUrl + QDM_TO_QICORE_MAPPING, QdmToQicoreMapping[].class);
        } catch (RestClientResponseException e) {
            throw new MatRuntimeException(e);
        }
        return response.getBody() == null ? Collections.emptyList() : Arrays.asList(response.getBody());
    }

    @Cacheable("spreadSheetDataTypes")
    public List<DataType> dataTypes() {
        ResponseEntity<DataType[]> response;
        try {
            response = restTemplate.getForEntity(fhirMatMappingServicesUrl + DATA_TYPES, DataType[].class);
        } catch (RestClientResponseException e) {
            throw new MatRuntimeException(e);
        }
        return response.getBody() == null ? Collections.emptyList() : Arrays.asList(response.getBody());
    }

    @Cacheable("spreadSheetResourceDefinitions")
    public List<ResourceDefinition> resourceDefinitions() {
        ResponseEntity<ResourceDefinition[]> response;
        try {
            response = restTemplate.getForEntity(fhirMatMappingServicesUrl + RESOURCE_DEFINITION, ResourceDefinition[].class);
        } catch (RestClientResponseException e) {
            throw new MatRuntimeException(e);
        }
        return response.getBody() == null ? Collections.emptyList() : Arrays.asList(response.getBody());
    }

    @Cacheable("requiredMeasureFields")
    public List<RequiredMeasureField> requiredMeasureFields() {
        ResponseEntity<RequiredMeasureField[]> response;
        try {
            response = restTemplate.getForEntity(fhirMatMappingServicesUrl + REQUIRED_MEASURE_FIELDS, RequiredMeasureField[].class);
        } catch (RestClientResponseException e) {
            throw new MatRuntimeException(e);
        }
        return response.getBody() == null ? Collections.emptyList() : Arrays.asList(response.getBody());
    }

    @Cacheable("typesForFunctionArgs")
    public List<String> typesForFunctionArgs() {
        ResponseEntity<String[]> response;
        try {
            response = restTemplate.getForEntity(fhirMatMappingServicesUrl + TYPES_FOR_FUNCTION_ARGS, String[].class);
        } catch (RestClientResponseException e) {
            throw new MatRuntimeException(e);
        }
        return response.getBody() == null ? Collections.emptyList() : Arrays.asList(response.getBody());
    }

   @Cacheable("fhirAssociation")
    public List<FhirDatatypeAttributeAssociation> fhirDatatypeAttributeAssociation() {
        ResponseEntity<FhirDatatypeAttributeAssociation[]> response;
        try {
            response = restTemplate.getForEntity(fhirMatMappingServicesUrl + FHIR_DATATYPE_ATTRIBUTE_ASSOCIATION, FhirDatatypeAttributeAssociation[].class);
        } catch (RestClientResponseException e) {
            throw new MatRuntimeException(e);
        }
        return response.getBody() == null ? Collections.emptyList() : Arrays.asList(response.getBody());
    }

}
