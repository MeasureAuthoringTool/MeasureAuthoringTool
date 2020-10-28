package mat.dto.fhirconversion;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ConversionResultDto {
    private String measureId;
    private String modified;
    private String errorReason;
    private String outcome;
    private ConversionType conversionType;
    private List<ValueSetConversionResults> valueSetConversionResults;
    private MeasureConversionResults measureConversionResults;
    private List<LibraryConversionResults> libraryConversionResults;
}
