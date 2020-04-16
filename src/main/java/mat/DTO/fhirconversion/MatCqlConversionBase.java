package mat.DTO.fhirconversion;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MatCqlConversionBase {
    private Integer startLine;
    private Integer startChar;
    private Integer endLine;
    private Integer endChar;
    private String errorType;
}
