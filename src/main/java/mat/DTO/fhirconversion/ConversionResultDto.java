package mat.DTO.fhirconversion;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ConversionResultDto {
    String measureId;
    String modified;
    private String errorReason;
    private ConversionOutcome outcome;
    private ConversionType conversionType;
    List<ValueSetConversionResults> valueSetConversionResults;
    MeasureConversionResults measureConversionResults;
    List<LibraryConversionResults> libraryConversionResults;
}
