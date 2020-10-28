package mat.model.clause;

import com.google.gwt.user.client.rpc.IsSerializable;
import mat.shared.measure.measuredetails.models.MeasureReferenceType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "MEASURE_DETAILS_REFERENCE")
public class MeasureDetailsReference implements IsSerializable {

    private int id;
    private MeasureDetails measureDetails;
    private String reference;
    private MeasureReferenceType referenceType = MeasureReferenceType.UNKNOWN;
    private int referenceNumber;

    public MeasureDetailsReference() {

    }

    public MeasureDetailsReference(MeasureDetails measureDetails, String reference, MeasureReferenceType referenceType, int referenceNumber) {
        this.measureDetails = measureDetails;
        this.reference = reference;
        this.referenceNumber = referenceNumber;
        this.referenceType = referenceType;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEASURE_DETAILS_ID", nullable = false)
    public MeasureDetails getMeasureDetails() {
        return measureDetails;
    }

    public void setMeasureDetails(MeasureDetails measureDetails) {
        this.measureDetails = measureDetails;
    }

    @Column(name = "REFERENCE")
    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Column(name = "REFERENCE_NUMBER")
    public int getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(int referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "REFERENCE_TYPE")
    public MeasureReferenceType getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(MeasureReferenceType measureReferenceType) {
        this.referenceType = measureReferenceType;
    }

}
