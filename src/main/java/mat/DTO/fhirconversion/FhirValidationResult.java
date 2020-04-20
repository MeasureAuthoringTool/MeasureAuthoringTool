package mat.DTO.fhirconversion;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FhirValidationResult {
    private String severity;
    private String locationField;
    private String errorDescription;
}
