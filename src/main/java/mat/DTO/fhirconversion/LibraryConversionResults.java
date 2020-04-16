package mat.DTO.fhirconversion;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class LibraryConversionResults {
    String matLibraryId;
    String fhirLibraryId;
    String name;
    String version;
    String reason;
    Boolean success;
    String link;
    String fhirLibraryJson;
    Map<String, List<CqlConversionError>> externalErrors;
    private List<FhirValidationResult> libraryFhirValidationResults = new ArrayList();
    private CqlConversionResult cqlConversionResult = new CqlConversionResult();
}
