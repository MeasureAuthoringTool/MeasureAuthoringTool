package mat.client.shared;


import java.util.Date;

import mat.client.ImageResources;
import mat.client.event.LogoffEvent;
import mat.client.event.TimedOutEvent;
import mat.shared.ConstantMessages;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;


class TimeoutManager {
	
	private static final int WARNING_TIME = 50 * 60 * 1000;
	private static final int WARNING_INTERVAL = 10 * 60 * 1000;
	private static final int REPEATED_WARNING_INTERVAL = 2 * 60 * 1000;
	private static final int TIMEOUTTHRESHOLD_TIME = WARNING_TIME+WARNING_INTERVAL;
	private Image alertIcon = new Image(ImageResources.INSTANCE.msg_error());
	
	private static HTML warningBannerWidget ;
	
	
	private long lastActivityTime = 0;
	//US 439
	private volatile String activeModule = null;
	
	private static Panel timeOutPanel;

	private  long actualTimeOutTime;
	private  String formattedTime;
	
	/*
	 * RepeatedWarning Timer will keep running every 2 minutes from the warning time, 
	 * up to the actualTimeOut time. 
	 * RepeatedWarning Timer will keep showing the warning message, 
	 * until currentTime < actualTimeOutTime, else fireLogOffEvent. 
	 */
	//US 153
	private Timer repeatedWarning = new Timer(){
	    	public void run(){
	    		
//				String msg = "Running Repeated Warning Timer.";
//				MatContext.get().recordTransactionEvent(null, null, "REPEATED_WARNING_TIMER", msg, true);
	    		
	    		Date currentDate = new Date(); 
				if(currentDate.getTime() <  actualTimeOutTime){
					  showTimeOutWarning();
				}else{
					this.cancel();
					fireLogOffEvent();
				}
	    	}
	    };
	

	/*
	 * TimeoutWarning timer is scheduled to run at the 50th minute from the last activity Time. 
	 * TimeOutWarning timer will show warning only if the 
	 * currentTime - lastActivityTime < 60 minutes. else, fires logOff event.
	 */
	//US 153
	private Timer timeoutWarning = new Timer() {
		public void run() {
			    Date today = new Date();
			    if((today.getTime() - lastActivityTime) < TIMEOUTTHRESHOLD_TIME){//show warning message only if the lastActivityTime is within 60 minutes.
						Date actualTimeOutDate = new Date(today.getTime()+WARNING_INTERVAL); //Timeout time = warningTime + 10 Minutes.
					    actualTimeOutTime = actualTimeOutDate.getTime();
					    formattedTime = DateTimeFormat.getMediumDateTimeFormat().format(actualTimeOutDate);
					    
					String msg ="Running TimeOutTimer, ActualTimeOut time is :-" + formattedTime;
					MatContext.get().recordTransactionEvent(null, null, "TIMEOUT_WARNING_EVENT", msg, ConstantMessages.DB_LOG);
					    
					    //Make save operation , release Lock, and Forward to Measure Library while the warning is about to display.
					    MatContext.get().getEventBus().fireEvent(new TimedOutEvent());
					    showTimeOutWarning();
					    repeatedWarning.scheduleRepeating(REPEATED_WARNING_INTERVAL); //repeat showing warning message until user interacts with the app.
			   }else{
					fireLogOffEvent();
			   }
		}
	};
	
	
	
	/*
	 * This startActivityTimer will be called everytime the user interacts within the app.
	 * 
	 */
	public void startActivityTimers(String module) {
		clearTimeOutWarning();
		activeModule = module;
		Date lastActivityDate = new Date();
		lastActivityTime = lastActivityDate.getTime();
//		String msg ="The Last Activity Time is:-"+ DateTimeFormat.getMediumDateTimeFormat().format(lastActivityDate);
//		MatContext.get().recordTransactionEvent(null, null, "TIMEOUT_EVENT", msg, false);
		timeoutWarning.cancel();
		repeatedWarning.cancel();
		//US 439. Warning message only required for Mat module not Login module.
		if(activeModule != null && activeModule.equalsIgnoreCase(ConstantMessages.MAT_MODULE)){	
			if(timeoutWarning != null)
				timeoutWarning.schedule(WARNING_TIME);
		}
	}
	
	
	
	
	private Panel buildTimeOutWarningPanel(){
		timeOutPanel = new HorizontalPanel();
		timeOutPanel.getElement().setAttribute("id", "timeOutContainer");
		timeOutPanel.getElement().setAttribute("aria-role", "warningBannerWidget");
		timeOutPanel.getElement().setAttribute("aria-labelledby", "LiveRegion");
		timeOutPanel.getElement().setAttribute("aria-live", "assertive");
		timeOutPanel.getElement().setAttribute("aria-atomic", "true");
		timeOutPanel.getElement().setAttribute("aria-relevant", "all");
		timeOutPanel.setStylePrimaryName("mainContentPanel");
		setId(timeOutPanel, "timeOutContainer");
		warningBannerWidget.setStyleName("padLeft5px");
		timeOutPanel.add(alertIcon);
		timeOutPanel.add(warningBannerWidget);
		timeOutPanel.setStyleName("alertMessage");
		return timeOutPanel;
	}
	
	private void showTimeOutWarning(){
		   clearTimeOutWarning();
		   warningBannerWidget = new HTML("Warning! Your session is about to expire at " + formattedTime +
				   ". Please click on the screen or press any key to continue. Unsaved changes will not be retained if the session is allowed to time out.");
		   RootPanel.get("timeOutWarning").add(buildTimeOutWarningPanel());
		   RootPanel.get("timeOutWarning").getElement().setAttribute("role", "alert");
	}
	
	private void clearTimeOutWarning(){
		if(RootPanel.get("timeOutWarning") != null){
			RootPanel.get("timeOutWarning").getElement().setAttribute("role", "alert");
			RootPanel.get("timeOutWarning").clear();
		}
	}
	
	protected void setId(Widget widget, String id) {
		DOM.setElementAttribute(widget.getElement(), "id", id);
	}
	
	private void fireLogOffEvent(){
		MatContext.get().stopUserLockUpdate();
		MatContext.get().recordTransactionEvent(null, null, "TIMEOUT_EVENT", null, 1);
		timeoutWarning.cancel();
		repeatedWarning.cancel();
		clearTimeOutWarning();
		MatContext.get().getEventBus().fireEvent(new LogoffEvent());
	}
}
