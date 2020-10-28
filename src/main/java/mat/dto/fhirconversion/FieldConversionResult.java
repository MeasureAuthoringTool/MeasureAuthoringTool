package mat.dto.fhirconversion;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FieldConversionResult {
    private String field;
    private String destination;
    private String reason;
}
