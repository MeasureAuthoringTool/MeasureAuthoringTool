package mat.client.shared;


import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.ImageResources;
import mat.client.Mat;
import mat.client.event.LogoffEvent;
import mat.client.event.TimedOutEvent;
import mat.shared.ConstantMessages;

import java.util.Date;

/**
 * The Class TimeoutManager.
 */
class TimeoutManager {

	/** The Constant WARNING_TIME. */
	private static final int WARNING_TIME = 25 * 60 * 1000;

	/** The Constant WARNING_INTERVAL. */
	private static final int WARNING_INTERVAL = 5 * 60 * 1000;

	/** The Constant REPEATED_WARNING_INTERVAL. */
	private static final int REPEATED_WARNING_INTERVAL = 1 * 60 * 1000;

	/** The Constant TIMEOUTTHRESHOLD_TIME. */
	private static final int TIMEOUTTHRESHOLD_TIME = WARNING_TIME + WARNING_INTERVAL;

	/** The alert icon. */
	private Image alertIcon = new Image(ImageResources.INSTANCE.msg_error());

	/** The Constant UMLS_TIME_OUT. */
	private static final int UMLS_TIME_OUT = 8 * 60 * 60 * 1000;//5 *  60 * 1000;//

	/** The warning banner widget. */
	private static HTML warningBannerWidget ;


	/** The last activity time. */
	private long lastActivityTime = 0;

	/** The last umls sign in. */
	private long lastUMLSSignIn =0;
	//US 439
	/** The active module. */
	private volatile String activeModule = null;

	/** The time out panel. */
	private static Panel timeOutPanel;

	/** The actual time out time. */
	private  long actualTimeOutTime;

	/** The formatted time. */
	private  String formattedTime;

	/***
	 * RepeatedWarning Timer will keep running every 2 minutes from the warning time,
	 * up to the actualTimeOut time.
	 * RepeatedWarning Timer will keep showing the warning message,
	 * until currentTime < actualTimeOutTime, else fireLogOffEvent.
	 */
	private Timer repeatedWarning = new Timer() {
	    	public void run() {

	    		Date currentDate = new Date();
				if (currentDate.getTime() <  actualTimeOutTime) {
					  showTimeOutWarning();
				} else {
					this.cancel();
					fireLogOffEvent();
				}
	    	}
	    };


	    /** The umls ticket time out. */
    	private Timer umlsTicketTimeOut = new Timer(){

			@Override
			public void run() {
				Date today = new Date();
				if((today.getTime() - lastUMLSSignIn) >= UMLS_TIME_OUT){
					Mat.hideUMLSActive(true);
					MatContext.get().setUMLSLoggedIn(false);
					invalidateVSacSession();
				}
			}

	    };

	    /**
		 * Invalidate v sac session.
		 */
    	private void invalidateVSacSession(){
	    	MatContext.get().getVsacapiServiceAsync().inValidateVsacUser(new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable caught) {

				}

				@Override
				public void onSuccess(Void result) {

				}
			});

		}

	/*
	 * TimeoutWarning timer is scheduled to run at the 10th minute from the last activity Time.
	 * TimeOutWarning timer will show warning only if the
	 * currentTime - lastActivityTime < 15 minutes. else, fires logOff event.
	 */
	//US 153
	/** The timeout warning. */
	private Timer timeoutWarning = new Timer() {
		public void run() {
			    Date today = new Date();
			    if((today.getTime() - lastActivityTime) < TIMEOUTTHRESHOLD_TIME){//show warning message only if the lastActivityTime is within 30 minutes.
		    		Date actualTimeOutDate = new Date(today.getTime()+WARNING_INTERVAL); //Timeout time = warningTime + 5 Minutes.
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
	/**
	 * Start activity timers.
	 *
	 * @param module
	 *            the module
	 */
	public void startActivityTimers(String module) {
		clearTimeOutWarning();
		activeModule = module;
		Date lastActivityDate = new Date();
		lastActivityTime = lastActivityDate.getTime();
		timeoutWarning.cancel();
		repeatedWarning.cancel();
		//US 439. Warning message only required for Mat module not Login module.
		if(activeModule != null && activeModule.equalsIgnoreCase(ConstantMessages.MAT_MODULE)){
			if(timeoutWarning != null)
				timeoutWarning.schedule(WARNING_TIME);
		}
	}

	/**
	 * Start umls timer.
	 */
	public void startUMLSTimer(){
		Date umlsActivityDate = new Date();
		lastUMLSSignIn = umlsActivityDate.getTime();
		umlsTicketTimeOut.cancel();
		umlsTicketTimeOut.schedule(UMLS_TIME_OUT);
	}



	/**
	 * Builds the time out warning panel.
	 *
	 * @return the panel
	 */
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
		alertIcon.setTitle("Information");
		timeOutPanel.add(alertIcon);
		timeOutPanel.add(warningBannerWidget);
		timeOutPanel.setStyleName("alertMessage");
		return timeOutPanel;
	}

	/**
	 * Show time out warning.
	 */
	private void showTimeOutWarning(){
		   clearTimeOutWarning();
		   warningBannerWidget = new HTML("Warning! Your session is about to expire at " + formattedTime +
				   ". Please click on the screen or press any key to continue. Unsaved changes will not be retained if the session is allowed to time out.");
		   RootPanel.get("timeOutWarning").add(buildTimeOutWarningPanel());
		   RootPanel.get("timeOutWarning").getElement().setAttribute("role", "alert");
		   RootPanel.get("timeOutWarning").getElement().focus();
		   RootPanel.get("timeOutWarning").getElement().setTabIndex(0);
	}

	/**
	 * Clear time out warning.
	 */
	private void clearTimeOutWarning(){
		if(RootPanel.get("timeOutWarning") != null){
			RootPanel.get("timeOutWarning").getElement().removeAttribute("role");
			RootPanel.get("timeOutWarning").clear();
		}
	}

	/**
	 * Sets the id.
	 *
	 * @param widget
	 *            the widget
	 * @param id
	 *            the id
	 */
	protected void setId(Widget widget, String id) {
		DOM.setElementAttribute(widget.getElement(), "id", id);
	}

	/**
	 * Fire log off event.
	 */
	private void fireLogOffEvent(){
		Mat.hideLoadingMessage();
		Mat.showSignOutMessage();
		timeoutWarning.cancel();
		repeatedWarning.cancel();
		clearTimeOutWarning();
		MatContext.get().getEventBus().fireEvent(new LogoffEvent());
	}
}
