package mat.reportmodel;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class Measure{
	private String id;
	private User owner;
	private String aBBRName;
	private String description;
	private String version;
	private String measureStatus;

	//US 421. Measure Scoring is now part of Measure Creation process.
	private String measureScoring;
	
	private Date exportedDate;
	private List<Clause> clauses;
	//private Set<MeasureShare> shares;
	private Timestamp lockedOutDate;
//	private User lockedUser;
	
	public List<Clause> getClauses() {
		return clauses;
	}
	public void setClauses(List<Clause> clauses) {
		this.clauses = clauses;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getaBBRName() {
		return aBBRName;
	}
	public void setaBBRName(String aBBRName) {
		this.aBBRName = aBBRName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getMeasureScoring() {
		return measureScoring;
	}
	public void setMeasureScoring(String measureScoring) {
		this.measureScoring = measureScoring;
	}	
	public String getMeasureStatus() {
		return measureStatus;
	}
	public void setMeasureStatus(String measureStatus) {
		this.measureStatus = measureStatus;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
//	public Set<MeasureShare> getShares() {
//		return shares;
//	}
//	public void setShares(Set<MeasureShare> shares) {
//		this.shares = shares;
//	}

	public Date getExportedDate() {
		return exportedDate;
	}
	public void setExportedDate(Date exportedDate) {
		this.exportedDate = exportedDate;
	}
	public Timestamp getLockedOutDate() {
		return lockedOutDate;
	}
	public void setLockedOutDate(Timestamp lockedOutDate) {
		this.lockedOutDate = lockedOutDate;
	}
	
//	public User getLockedUser() {
//		return lockedUser;
//	}
//	public void setLockedUser(User lockedUser) {
//		this.lockedUser = lockedUser;
//	}
}
