package vsac.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString
public class ValueSetResult {
    private String xmlPayLoad;
    private List<String> pgmRels;
    private boolean isFailResponse;
    private String failReason;
}
