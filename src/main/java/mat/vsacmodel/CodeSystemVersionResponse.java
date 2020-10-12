package mat.vsacmodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CodeSystemVersionResponse implements Serializable {
    private String version;
    private Boolean success;
    private String message;
}
