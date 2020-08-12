package mat.client.measure;

import com.google.gwt.user.client.rpc.IsSerializable;
import mat.shared.measure.measuredetails.models.MeasureReferenceType;

import java.util.Objects;

public class ReferenceTextAndType implements IsSerializable {
    private String referenceText;
    private MeasureReferenceType referenceType = MeasureReferenceType.UNKNOWN;

    public ReferenceTextAndType() {
    }

    public ReferenceTextAndType(String referenceText, MeasureReferenceType referenceType) {
        this.referenceText = referenceText;
        this.referenceType = referenceType;
    }

    public String getReferenceText() {
        return referenceText;
    }

    public void setReferenceText(String referenceText) {
        this.referenceText = referenceText;
    }

    public MeasureReferenceType getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(MeasureReferenceType referenceType) {
        this.referenceType = referenceType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReferenceTextAndType that = (ReferenceTextAndType) o;
        return Objects.equals(getReferenceText(), that.getReferenceText()) &&
                getReferenceType() == that.getReferenceType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReferenceText(), getReferenceType());
    }

    @Override
    public String toString() {
        return "ReferenceWithType{" +
                "referenceText='" + referenceText + '\'' +
                ", referenceType=" + referenceType +
                '}';
    }

}
