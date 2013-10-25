package mat.client.event;

import mat.client.shared.MatContext;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Window;

/**
 * Control center for MAT conditionally executed events handleEvent
 * implementation is responsible for determining whether or not to execute an
 * event.
 * 
 * @author aschmidt
 */
public abstract class MATEventHandler implements EventHandler{
	
	/**
	 * implement this method with the intended behavior for the event.
	 * 
	 * @param event
	 *            the event
	 */
	protected abstract void onEvent(GwtEvent event);
	
	/**
	 * A MATEventHandler implementation should override if an alert should not
	 * fire when the event behavior is blocked.
	 * 
	 * @return true if an alert should fire
	 */
	protected boolean doAlert(){
		return true;
	}
		
	/**
	 * do event behavior only if not loading.
	 * 
	 * @param event
	 *            the event
	 */
	protected void handleEvent(GwtEvent event){
		if(!MatContext.get().isLoading()){
			this.onEvent(event);
		}
		else if(doAlert()){
			Window.alert(getAlertMessage());
		}
	}
	
	/**
	 * override this method to customize an alert message do nothing to use the
	 * default.
	 * 
	 * @return the alert message
	 */
	protected String getAlertMessage(){
		return MatContext.get().getMessageDelegate().getAlertLoadingMessage();
	}
	
}
