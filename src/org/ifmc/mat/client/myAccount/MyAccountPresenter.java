package org.ifmc.mat.client.myAccount;


import org.ifmc.mat.client.MatPresenter;
import com.google.gwt.user.client.ui.Widget;

public class MyAccountPresenter implements MatPresenter {
	public static interface Display {
		public void beforeDisplay();
		public Widget getWidget();
	}

	private Display display;
	
	public MyAccountPresenter(Display displayArg) {
		this.display = displayArg;
		
	}
	
	@Override
	public Widget getWidget() {
		return display.getWidget();
	}

	
	@Override
	public void beforeDisplay() {
		display.beforeDisplay();
		//Mat.focusSkipLists("PersonalInfo");
	}
	@Override 
	public void beforeClosingDisplay() {
		
	}
}
