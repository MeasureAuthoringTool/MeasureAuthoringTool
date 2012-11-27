package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class MeasureSelectedEvent extends GwtEvent<MeasureSelectedEvent.Handler> {
	public static GwtEvent.Type<MeasureSelectedEvent.Handler> TYPE = 
		new GwtEvent.Type<MeasureSelectedEvent.Handler>();
		
	public static interface Handler extends EventHandler {
		public void onMeasureSelected(MeasureSelectedEvent event);
	}

	private String measureId;
	private String measureName;
	private String shortName;
	private String scoringType;
	private boolean isEditable;
	private boolean isLocked;
	private String lockedUserId;
	private String measureVersion;
	

	public MeasureSelectedEvent(String measureId, String measureVersion, String measureName, String shortName, String scoringType, boolean isEditable,boolean isLocked,String lockedUserId) {
		this.measureId = measureId;
		this.measureVersion = measureVersion;
		this.measureName = measureName;
		this.shortName = shortName;
		this.scoringType = scoringType;
		this.isEditable = isEditable;
		this.isLocked = isLocked;
		this.lockedUserId = lockedUserId;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onMeasureSelected(this);
	}

	public String getMeasureId() {
		return measureId;
	}

	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}

	public String getScoringType(){
		return scoringType;
	}
	
	public void setScoringType(String s){
		scoringType = s;
	}
	
	public String getMeasureName() {
		return measureName;
	}
	
	
	public void setMeasureName(String measureName) {
		this.measureName = measureName;
	}
	public String getMeasureVersion(){
		return measureVersion;
	}
	public void setMeasureVersion(String version){
		 measureVersion = version;
	}
	
	
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public boolean isEditable() {
		return isEditable;
	}

	public String getLockedUserId() {
		return lockedUserId;
	}

	public void setLockedUserId(String lockedUserId) {
		this.lockedUserId = lockedUserId;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}
	

	public boolean isLocked() {
		return isLocked;
	}

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

}
