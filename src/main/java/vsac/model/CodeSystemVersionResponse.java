package vsac.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class CodeSystemVersionResponse {
    private final String version;
    private final Boolean success;
    private final String message;
}
