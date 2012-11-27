package mat.client.shared.search;

import com.google.gwt.event.shared.GwtEvent;

public class SelectAllEvent extends GwtEvent<SelectAllEventHandler> {
	public static GwtEvent.Type<SelectAllEventHandler> TYPE = 
		new GwtEvent.Type<SelectAllEventHandler>();

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SelectAllEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SelectAllEventHandler handler) {
	   handler.onSelectAll(this);
	}
	private boolean checked;
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public SelectAllEvent(boolean checked){
	this.checked = checked;	
	}
}
