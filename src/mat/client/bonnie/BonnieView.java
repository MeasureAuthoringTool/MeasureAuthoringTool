package mat.client.bonnie;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class BonnieView implements BonnieViewDisplay{
	
	private VerticalPanel mainPanel = new VerticalPanel();
	BonnieModal modal;
	
	@Override
	public Widget asWidget() {
		return modal.asWidget();
	}
	@Override
	public void showBonnieModal() {
		modal.show();
	}

	
	public BonnieView() {
		modal = new BonnieModal();
	}
}
