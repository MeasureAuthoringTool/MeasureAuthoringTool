package mat.model.clause;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mat.model.User;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity
@Table (name="FHIR_CONVERSION_HISTORY")
@Getter
@Setter
@ToString
public class FhirConversionHistory {
    @Id
    @Column(name="QDM_SET_ID")
    private String qdmSetId;
    @Column(name="FHIR_SET_ID")
    private String fhirSetId;
    @ManyToOne
    @JoinColumn(name = "LAST_MODIFIED_BY",referencedColumnName="USER_ID" )
    private User lastModifiedBy;
    @Column(name="LAST_MODIFIED_ON")
    private Timestamp lastModifiedOn;
}