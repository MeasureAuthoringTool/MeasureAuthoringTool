package mat.dto.fhirconversion;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MatCqlConversionException extends MatCqlConversionBase {
    private String errorSeverity;
    private String targetIncludeLibraryId;
    private String targetIncludeLibraryVersionId;
    private String type;
    private String message;
}
