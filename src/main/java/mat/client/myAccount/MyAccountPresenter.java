package mat.client.myAccount;


import com.google.gwt.user.client.ui.Widget;
import mat.client.MatPresenter;

/**
 * The Class MyAccountPresenter.
 */
public class MyAccountPresenter implements MatPresenter {
	
	/**
	 * The Interface Display.
	 */
	public static interface Display {
		
		/**
		 * Before display.
		 */
		public void beforeDisplay();
		
		/**
		 * Gets the widget.
		 * 
		 * @return the widget
		 */
		public Widget getWidget();
	}

	/** The display. */
	private Display display;
	
	/**
	 * Instantiates a new my account presenter.
	 * 
	 * @param displayArg
	 *            the display arg
	 */
	public MyAccountPresenter(Display displayArg) {
		this.display = displayArg;
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#getWidget()
	 */
	@Override
	public Widget getWidget() {
		return display.getWidget();
	}

	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		display.beforeDisplay();
		//Mat.focusSkipLists("PersonalInfo");
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override 
	public void beforeClosingDisplay() {
		
	}
}
