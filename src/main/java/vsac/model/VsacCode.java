package vsac.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class VsacCode {
    private String message;
    private String status;

    private VsacData data;
    private VsacError errors;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VsacData {
        private int resultCount;
        private List<VsacDataResultSet> resultSet = new ArrayList<>();
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VsacError {
        private  int errorCount;
        private  List<VsacErrorResultSet> resultSet = new ArrayList<>();
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VsacErrorResultSet {
        private  String errDesc;
        private  String errCode;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VsacDataResultSet {
        private String csName;
        private String csOID;
        private String csVersion;
        private String code;
        private String contentMode;
        private String codeName;
        private  String termType;
        private  String active;
        private  Long revision;
    }
}
