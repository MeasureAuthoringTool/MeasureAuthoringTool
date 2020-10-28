package mat.client.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Map;
import java.util.TreeMap;

public class FhirDataType implements IsSerializable {
    private String id;
    private String fhirResource;
    private Map<String, FhirAttribute> attributes = new TreeMap<>();

    public FhirDataType() {
    }

    public FhirDataType(String id, String fhirResource) {
        this.id = id;
        this.fhirResource = fhirResource;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFhirResource() {
        return fhirResource;
    }

    public void setFhirResource(String fhirResource) {
        this.fhirResource = fhirResource;
    }

    public Map<String, FhirAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, FhirAttribute> attributes) {
        this.attributes = attributes;
    }
}
