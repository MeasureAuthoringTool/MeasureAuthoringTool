package mat.client.shared.search;

import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class SelectAllEvent.
 */
public class SelectAllEvent extends GwtEvent<SelectAllEventHandler> {
	
	/** The type. */
	public static GwtEvent.Type<SelectAllEventHandler> TYPE = 
		new GwtEvent.Type<SelectAllEventHandler>();

	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
	 */
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SelectAllEventHandler> getAssociatedType() {
		return TYPE;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
	 */
	@Override
	protected void dispatch(SelectAllEventHandler handler) {
	   handler.onSelectAll(this);
	}
	
	/** The checked. */
	private boolean checked;
	
	/**
	 * Checks if is checked.
	 * 
	 * @return true, if is checked
	 */
	public boolean isChecked() {
		return checked;
	}

	/**
	 * Sets the checked.
	 * 
	 * @param checked
	 *            the new checked
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	/**
	 * Instantiates a new select all event.
	 * 
	 * @param checked
	 *            the checked
	 */
	public SelectAllEvent(boolean checked){
	this.checked = checked;	
	}
}
