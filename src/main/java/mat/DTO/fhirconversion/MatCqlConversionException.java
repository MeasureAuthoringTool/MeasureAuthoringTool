package mat.DTO.fhirconversion;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MatCqlConversionException extends MatCqlConversionBase {
    String errorSeverity;
    String targetIncludeLibraryId;
    String targetIncludeLibraryVersionId;
    String type;
    String message;
}
