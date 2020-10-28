package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class MeasureSelectedEvent.
 */
public class MeasureSelectedEvent extends GwtEvent<MeasureSelectedEvent.Handler> {

    /**
     * The type.
     */
    public static Type<Handler> TYPE = new Type<Handler>();

    private String measureId;
    private String measureName;
    private String shortName;
    private String scoringType;
    private boolean isEditable;
    private boolean isLocked;
    private String lockedUserId;
    private String measureVersion;
    private boolean isDraft;
    private boolean isPatientBased;
    private String measureModel;

    /**
     * The Interface Handler.
     */
    public static interface Handler extends EventHandler {

        /**
         * On measure selected.
         *
         * @param event the event
         */
        public void onMeasureSelected(MeasureSelectedEvent event);
    }


    /**
     * Instantiates a new measure selected event.
     *
     * @param measureId      the measure id
     * @param measureVersion the measure version
     * @param measureName    the measure name
     * @param shortName      the short name
     * @param scoringType    the scoring type
     * @param isEditable     the is editable
     * @param isLocked       the is locked
     * @param lockedUserId   the locked user id
     * @param isDraft        tells us if te measure is in a draft state
     */
    public MeasureSelectedEvent(String measureId, String measureVersion, String measureName, String shortName, String scoringType, boolean isEditable,
                                boolean isLocked, String lockedUserId, boolean isDraft, boolean isPatientBased, String measureModel) {
        this.measureId = measureId;
        this.measureVersion = measureVersion;
        this.measureName = measureName;
        this.shortName = shortName;
        this.scoringType = scoringType;
        this.isEditable = isEditable;
        this.isLocked = isLocked;
        this.lockedUserId = lockedUserId;
        this.setDraft(isDraft);
        this.isPatientBased = isPatientBased;
        this.measureModel = measureModel;
    }


    /* (non-Javadoc)
     * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
     */
    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
     */
    @Override
    protected void dispatch(Handler handler) {
        handler.onMeasureSelected(this);
    }

    /**
     * Gets the measure id.
     *
     * @return the measure id
     */
    public String getMeasureId() {
        return measureId;
    }

    /**
     * Sets the measure id.
     *
     * @param measureId the new measure id
     */
    public void setMeasureId(String measureId) {
        this.measureId = measureId;
    }

    /**
     * Gets the scoring type.
     *
     * @return the scoring type
     */
    public String getScoringType() {
        return scoringType;
    }

    /**
     * Sets the scoring type.
     *
     * @param s the new scoring type
     */
    public void setScoringType(String s) {
        scoringType = s;
    }

    /**
     * Gets the measure name.
     *
     * @return the measure name
     */
    public String getMeasureName() {
        return measureName;
    }


    /**
     * Sets the measure name.
     *
     * @param measureName the new measure name
     */
    public void setMeasureName(String measureName) {
        this.measureName = measureName;
    }

    /**
     * Gets the measure version.
     *
     * @return the measure version
     */
    public String getMeasureVersion() {
        return measureVersion;
    }

    /**
     * Sets the measure version.
     *
     * @param version the new measure version
     */
    public void setMeasureVersion(String version) {
        measureVersion = version;
    }


    /**
     * Gets the short name.
     *
     * @return the short name
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * Sets the short name.
     *
     * @param shortName the new short name
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    /**
     * Checks if is editable.
     *
     * @return true, if is editable
     */
    public boolean isEditable() {
        return isEditable;
    }

    /**
     * Gets the locked user id.
     *
     * @return the locked user id
     */
    public String getLockedUserId() {
        return lockedUserId;
    }

    /**
     * Sets the locked user id.
     *
     * @param lockedUserId the new locked user id
     */
    public void setLockedUserId(String lockedUserId) {
        this.lockedUserId = lockedUserId;
    }

    /**
     * Sets the editable.
     *
     * @param isEditable the new editable
     */
    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }


    /**
     * Checks if is locked.
     *
     * @return true, if is locked
     */
    public boolean isLocked() {
        return isLocked;
    }

    /**
     * Sets the locked.
     *
     * @param isLocked the new locked
     */
    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }


    public boolean isDraft() {
        return isDraft;
    }


    public void setDraft(boolean isDraft) {
        this.isDraft = isDraft;
    }


    public boolean isPatientBased() {
        return isPatientBased;
    }


    public void setPatientBased(boolean isPatientBased) {
        this.isPatientBased = isPatientBased;
    }

    public String getMeasureModel() {
        return measureModel;
    }

    public void setMeasureModel(String measureModel) {
        this.measureModel = measureModel;
    }


}
