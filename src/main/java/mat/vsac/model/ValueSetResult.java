package mat.vsac.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ValueSetResult implements Serializable {
    private String xmlPayLoad;
    private List<String> pgmRels;
    private boolean isFailResponse;
    private String failReason;
}
