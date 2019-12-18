package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class CQLLibrarySelectedEvent.
 */
public class CQLLibrarySelectedEvent extends GwtEvent<CQLLibrarySelectedEvent.Handler> {


    private String cqlLibraryId;

    private String libraryName;

    private boolean isEditable;

    private boolean isLocked;

    private String lockedUserId;

    private String lockedUserEmail;

    private String lockedUserName;

    private String cqlLibraryVersion;

    private boolean isDraft;

    private String libraryModelType;

    public static GwtEvent.Type<CQLLibrarySelectedEvent.Handler> TYPE =
            new GwtEvent.Type<>();

    /**
     * The Interface Handler.
     */
    public static interface Handler extends EventHandler {

        /**
         * On CQLLibrary selected.
         *
         * @param event the event
         */
        public void onLibrarySelected(CQLLibrarySelectedEvent event);
    }


    /**
     * Instantiates a new cql library selected event.
     */
    public CQLLibrarySelectedEvent(Builder builder) {
        this.cqlLibraryId = builder.cqlLibraryId;
        this.cqlLibraryVersion = builder.cqlLibraryVersion;
        this.libraryName = builder.libraryName;
        this.isEditable = builder.isEditable;
        this.isLocked = builder.isLocked;
        this.lockedUserId = builder.lockedUserId;
        this.lockedUserEmail = builder.lockedUserEmail;
        this.lockedUserName = builder.lockedUserName;
        this.isDraft = builder.isDraft;
        this.libraryModelType = builder.libraryModelType;
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(Handler handler) {
        handler.onLibrarySelected(this);
    }

    public String getCqlLibraryId() {
        return cqlLibraryId;
    }

    public void setCqlLibraryId(String cqlLibraryId) {
        this.cqlLibraryId = cqlLibraryId;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public String getCqlLibraryVersion() {
        return cqlLibraryVersion;
    }

    public void setCqlLibraryVersion(String cqlLibraryVersion) {
        this.cqlLibraryVersion = cqlLibraryVersion;
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

    public String getLockedUserEmail() {
        return lockedUserEmail;
    }

    public void setLockedUserEmail(String lockedUserEmail) {
        this.lockedUserEmail = lockedUserEmail;
    }

    public String getLockedUserName() {
        return lockedUserName;
    }

    public void setLockedUserName(String lockedUserName) {
        this.lockedUserName = lockedUserName;
    }

    public boolean isDraft() {
        return isDraft;
    }

    public void setDraft(boolean isDraft) {
        this.isDraft = isDraft;
    }

    public String getLibraryModelType() {
        return libraryModelType;
    }

    public void setLibraryModelType(String libraryModelType) {
        this.libraryModelType = libraryModelType;
    }

    public static class Builder {

        private String cqlLibraryId;
        private String libraryName;
        private boolean isEditable;
        private boolean isLocked;
        private String lockedUserId;
        private String lockedUserEmail;
        private String lockedUserName;
        private String cqlLibraryVersion;
        private boolean isDraft;
        private String libraryModelType;

        public CQLLibrarySelectedEvent build() {
            return new CQLLibrarySelectedEvent(this);
        }

        public Builder withCqlLibraryId(String cqlLibraryId) {
            this.cqlLibraryId = cqlLibraryId;
            return this;
        }

        public Builder withLibraryName(String libraryName) {
            this.libraryName = libraryName;
            return this;
        }

        public Builder withEditable(boolean editable) {
            isEditable = editable;
            return this;
        }

        public Builder withLocked(boolean locked) {
            isLocked = locked;
            return this;
        }

        public Builder withLockedUserId(String lockedUserId) {
            this.lockedUserId = lockedUserId;
            return this;
        }

        public Builder withLockedUserEmail(String lockedUserEmail) {
            this.lockedUserEmail = lockedUserEmail;
            return this;
        }

        public Builder withLockedUserName(String lockedUserName) {
            this.lockedUserName = lockedUserName;
            return this;
        }

        public Builder withCqlLibraryVersion(String cqlLibraryVersion) {
            this.cqlLibraryVersion = cqlLibraryVersion;
            return this;
        }

        public Builder withDraft(boolean draft) {
            isDraft = draft;
            return this;
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder withLibraryType(String libraryModelType) {
            this.libraryModelType = libraryModelType;
            return this;
        }
    }

}
