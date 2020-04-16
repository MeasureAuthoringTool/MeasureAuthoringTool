package mat.DTO.fhirconversion;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FieldConversionResult {
    String field;
    String destination;
    String reason;
}
