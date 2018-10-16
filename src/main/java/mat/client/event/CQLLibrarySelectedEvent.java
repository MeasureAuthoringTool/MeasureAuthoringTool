package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class CQLLibrarySelectedEvent.
 */
public class CQLLibrarySelectedEvent extends GwtEvent<CQLLibrarySelectedEvent.Handler> {
	
	/** The type. */
	public static GwtEvent.Type<CQLLibrarySelectedEvent.Handler> TYPE = 
		new GwtEvent.Type<CQLLibrarySelectedEvent.Handler>();
		
	/**
	 * The Interface Handler.
	 */
	public static interface Handler extends EventHandler {
		
		/**
		 * On CQLLibrary selected.
		 * 
		 * @param event
		 *            the event
		 */
		public void onLibrarySelected(CQLLibrarySelectedEvent event);
	}

	private String cqlLibraryId;
	
	private String libraryName;
	
	private boolean isEditable;
	
	private boolean isLocked;
	
	private String lockedUserId;
	
	private String lockedUserEmail;
	
	private String lockedUserName;
	
	private String cqlLibraryVersion;
	
	private boolean isDraft;
	

	/**
	 * Instantiates a new cql library selected event.
	 */
	public CQLLibrarySelectedEvent(String cqlLibraryId, String cqlLibraryVersion, String libraryName,  boolean isEditable,boolean isLocked,String lockedUserId,String lockedUserEmail,String lockedUserName, boolean isDraft) {
		this.cqlLibraryId = cqlLibraryId;
		this.cqlLibraryVersion = cqlLibraryVersion;
		this.libraryName = libraryName;
		this.isEditable = isEditable;
		this.isLocked = isLocked;
		this.lockedUserId = lockedUserId;
		this.lockedUserEmail = lockedUserEmail;
		this.lockedUserName = lockedUserName;
		this.isDraft = isDraft;
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
	 */
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
	 */
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
	 * @param lockedUserId
	 *            the new locked user id
	 */
	public void setLockedUserId(String lockedUserId) {
		this.lockedUserId = lockedUserId;
	}

	/**
	 * Sets the editable.
	 * 
	 * @param isEditable
	 *            the new editable
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
	 * @param isLocked
	 *            the new locked
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

}
