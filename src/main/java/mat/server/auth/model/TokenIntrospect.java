package mat.server.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@ToString
@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenIntrospect {
    private boolean isActive;
    private String username;
    private String name;
    private String email;
}
