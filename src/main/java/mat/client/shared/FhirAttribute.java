package mat.client.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Objects;

public class FhirAttribute implements IsSerializable {

    private String id;
    private String fhirElement;
    private String fhirType;

    public FhirAttribute() {
    }

    public FhirAttribute(String id, String fhirElement, String fhirType) {
        this.id = id;
        this.fhirElement = fhirElement;
        this.fhirType = fhirType;
    }

    public String getFhirElement() {
        return fhirElement;
    }

    public void setFhirElement(String fhirElement) {
        this.fhirElement = fhirElement;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFhirType() {
        return fhirType;
    }

    public void setFhirType(String fhirType) {
        this.fhirType = fhirType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FhirAttribute that = (FhirAttribute) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "FhirAttribute{" +
                "id='" + id + '\'' +
                ", fhirElement='" + fhirElement + '\'' +
                ", fhirType='" + fhirType + '\'' +
                '}';
    }

}
