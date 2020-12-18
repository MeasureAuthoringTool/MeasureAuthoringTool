package mat.dto.fhirconversion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushValidationResult {
    boolean isValid;
    String url;
    List<FhirValidationResult> fhirValidationResults;
}
