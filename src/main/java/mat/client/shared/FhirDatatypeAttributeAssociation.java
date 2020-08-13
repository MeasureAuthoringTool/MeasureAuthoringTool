package mat.client.shared;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gwt.user.client.rpc.IsSerializable;
import lombok.Data;
import lombok.NoArgsConstructor;


@JsonIgnoreProperties(ignoreUnknown = true)
public class FhirDatatypeAttributeAssociation implements IsSerializable {
    String datatype;
    String attribute;

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
}
