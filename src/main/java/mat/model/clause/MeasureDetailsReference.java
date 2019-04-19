package mat.model.clause;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.google.gwt.user.client.rpc.IsSerializable;

@Entity
@Table(name = "MEASURE_DETAILS_REFERENCE")
public class MeasureDetailsReference implements IsSerializable{
	
	private int id;
	private MeasureDetails measureDetails;
	private String reference;
	private int referenceNumber;
	
	public MeasureDetailsReference() {
		
	}
	
	public MeasureDetailsReference( MeasureDetails measureDetails, String reference, int referenceNumber) {
		this.measureDetails = measureDetails;
		this.reference = reference;
		this.referenceNumber = referenceNumber;
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
}
