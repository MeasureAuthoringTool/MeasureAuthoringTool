package mat.DTO.fhirconversion;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
@NoArgsConstructor
public class LibraryConversionResults {
    private String matLibraryId;
    private String fhirLibraryId;
    private String name;
    private String version;
    private String reason;
    private Boolean success;
    private String link;
    private String fhirLibraryJson;
    private Map<String, List<CqlConversionError>> externalErrors;
    private List<FhirValidationResult> libraryFhirValidationResults = new ArrayList();
    private CqlConversionResult cqlConversionResult = new CqlConversionResult();


    public boolean isSuccess() {
        return Objects.nonNull(success) && Boolean.TRUE.equals(success);
    }

    public boolean hasFailure() {
        return !isSuccess();
    }
}
