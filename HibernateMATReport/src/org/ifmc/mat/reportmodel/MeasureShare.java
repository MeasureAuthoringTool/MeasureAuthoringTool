package org.ifmc.mat.reportmodel;


public class MeasureShare {
	private String id;
	private Measure measure;
	private ShareLevel shareLevel;
	private User owner;
	private User shareUser;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Measure getMeasure() {
		return measure;
	}
	public void setMeasure(Measure measure) {
		this.measure = measure;
	}
	public ShareLevel getShareLevel() {
		return shareLevel;
	}
	public void setShareLevel(ShareLevel shareLevel) {
		this.shareLevel = shareLevel;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	public User getShareUser() {
		return shareUser;
	}
	public void setShareUser(User shareUser) {
		this.shareUser = shareUser;
	}
}
