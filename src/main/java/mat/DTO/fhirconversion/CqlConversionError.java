package mat.DTO.fhirconversion;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CqlConversionError extends CqlConversionBase {
    String errorSeverity;
    String targetIncludeLibraryId;
    String libraryId;
    String targetIncludeLibraryVersionId;
    String libraryVersion;
    String message;
    String type;

}
