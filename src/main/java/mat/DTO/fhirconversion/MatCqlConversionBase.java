package mat.DTO.fhirconversion;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MatCqlConversionBase {
    Integer startLine;
    Integer startChar;
    Integer endLine;
    Integer endChar;
    String errorType;
}
