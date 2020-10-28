package mat.dto.fhirconversion;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CqlConversionError extends CqlConversionBase {
    private String errorSeverity;
    private String targetIncludeLibraryId;
    private String libraryId;
    private String targetIncludeLibraryVersionId;
    private String libraryVersion;
    private String message;
    private String type;

}
