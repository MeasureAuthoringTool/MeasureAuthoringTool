package mat.vsacmodel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
public class BasicResponse implements Serializable {
    public static final int REQUEST_TIMEDOUT = 3;
    public static final int REQUEST_FAILED = 4;

    private String xmlPayLoad;
    private List<String> pgmRels;
    private boolean isFailResponse;
    private int failReason;
}
