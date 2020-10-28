package mat.model.clause;

import mat.model.User;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "CQL_LIBRARY_HISTORY")
public class CQLLibraryHistory {
	private int id;
	private Measure measure;
	private CQLLibrary cqlLibrary;
	private User lastModifiedBy;
	private String cqlLibraryString;
	private Date lastModifiedOn;
	private boolean isFreeTextEditorUsed;
	
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
	@JoinColumn(name = "MEASURE_ID")
	public Measure getMeasure() {
		return measure;
	}
	public void setMeasure(Measure measure) {
		this.measure = measure;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LIBRARY_ID")
	public CQLLibrary getCqlLibrary() {
		return cqlLibrary;
	}
	public void setCqlLibrary(CQLLibrary cqlLibrary) {
		this.cqlLibrary = cqlLibrary;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAST_MODIFIED_BY")
	public User getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(User lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	
	@Column(name = "CQL_LIBRARY")
	public String getCqlLibraryString() {
		return cqlLibraryString;
	}
	public void setCqlLibraryString(String cqlLibraryString) {
		this.cqlLibraryString = cqlLibraryString;
	}
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIED_ON")
	public Date getLastModifiedOn() {
		return lastModifiedOn;
	}
	public void setLastModifiedOn(Date lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}
	
	@Column(name = "FREE_TEXT_EDITOR_USED")
	public boolean isFreeTextEditorUsed() {
		return isFreeTextEditorUsed;
	}
	public void setFreeTextEditorUsed(boolean isFreeTextEditorUsed) {
		this.isFreeTextEditorUsed = isFreeTextEditorUsed;
	}
	
}
