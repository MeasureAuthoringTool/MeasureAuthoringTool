package mat.client.cqlworkspace.attributes;

import java.util.ArrayList;
import java.util.List;

public class FhirDataType {
    private String id;
    private String name;
    private List<FhirAttribute> attributes = new ArrayList<>();

    public FhirDataType(String id, String name, List<FhirAttribute> attributes) {
        this.id = id;
        this.name = name;
        this.attributes = attributes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FhirAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<FhirAttribute> attributes) {
        this.attributes = attributes;
    }
}
