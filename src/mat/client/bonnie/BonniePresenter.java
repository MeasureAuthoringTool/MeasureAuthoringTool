package mat.client.bonnie;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;

import mat.client.shared.MatContext;

public class BonniePresenter {

	private BonnieView view;
	
	public BonniePresenter(BonnieView bonnieView){
		view = bonnieView;
	}
	
	public void go(HasWidgets container) {
		Window.open(MatContext.get().getBonnieLink(), "_self", "");
	}
}
