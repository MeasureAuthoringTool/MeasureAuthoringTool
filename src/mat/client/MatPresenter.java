package mat.client;

import com.google.gwt.user.client.ui.Widget;

public interface MatPresenter {
	public void beforeDisplay();
	public void beforeClosingDisplay();
	public Widget getWidget();
}
