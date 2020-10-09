package mat.vsac.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@NoArgsConstructor
public class VsacCode implements Serializable {
    private String message;
    private String status;

    private VsacData data;
    private VsacError errors;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VsacData implements Serializable {
        private int resultCount;
        private List<VsacDataResultSet> resultSet = new ArrayList<>();
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VsacError implements Serializable {
        private int errorCount;
        private List<VsacErrorResultSet> resultSet = new ArrayList<>();
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VsacErrorResultSet implements Serializable {
        private String errDesc;
        private String errCode;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VsacDataResultSet implements Serializable {
        private String csName;
        private String csOID;
        private String csVersion;
        private String code;
        private String contentMode;
        private String codeName;
        private String termType;
        private String active;
        private Long revision;
    }
}
