package mat.DTO.fhirconversion;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FhirValidationResult {
    String severity;
    String locationField;
    String errorDescription;
}
