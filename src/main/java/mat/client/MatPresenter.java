package mat.client;

import com.google.gwt.user.client.ui.Widget;


/**
 * MatPresenter.java.
 */
public interface MatPresenter {
	/**
	 * Method to close/clean Panel before moving away from implemented
	 * Presenter.
	 */
	void beforeClosingDisplay();
	/**
	 * Method to set view on Panel.
	 */
	void beforeDisplay();
	
	/**
	 * Gets the widget.
	 *
	 * @return {@link Widget}.
	 */
	Widget getWidget();
}
