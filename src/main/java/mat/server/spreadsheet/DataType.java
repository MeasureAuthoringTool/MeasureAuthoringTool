package mat.server.spreadsheet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class DataType {
    private String dataType;
    private String validValues;
    private String regex;
    private String type;
}
