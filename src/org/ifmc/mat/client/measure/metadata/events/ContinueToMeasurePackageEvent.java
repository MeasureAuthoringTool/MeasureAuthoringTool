package org.ifmc.mat.client.measure.metadata.events;



import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class ContinueToMeasurePackageEvent extends GwtEvent<ContinueToMeasurePackageEvent.Handler>{
	
	public static GwtEvent.Type<ContinueToMeasurePackageEvent.Handler> TYPE = 
		new GwtEvent.Type<ContinueToMeasurePackageEvent.Handler>();
		
	public static interface Handler extends EventHandler {
		public void onContinueToMeasurePackage(ContinueToMeasurePackageEvent event);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onContinueToMeasurePackage(this);
	}
	
	
}
