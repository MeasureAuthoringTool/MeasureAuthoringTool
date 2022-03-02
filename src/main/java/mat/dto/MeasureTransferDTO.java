package mat.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import mat.client.measure.ManageMeasureDetailModel;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class MeasureTransferDTO implements Serializable {
    private String emailId;
    private String harpId;
    private ManageMeasureDetailModel manageMeasureDetailModel;
    private String measureResourceJson;
    private String libraryResourcesJson;
}
