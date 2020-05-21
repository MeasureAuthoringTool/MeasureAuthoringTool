package mat.client.measure.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FhirLibraryPackageResult {
    private String cql;
    private String fhirJson;
    private String fhirXml;
    private String elmXml;
    private String elmJson;
}
