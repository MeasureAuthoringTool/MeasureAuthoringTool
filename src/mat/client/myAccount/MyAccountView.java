package mat.client.myAccount;

import mat.client.shared.MatContext;
import mat.client.shared.MatTabLayoutPanel;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class MyAccountView implements MyAccountPresenter.Display {
	private FlowPanel fp = new FlowPanel();
	private MatTabLayoutPanel tabLayout;
	private PersonalInformationPresenter pip;
	private final String MY_ACCOUNT_TAB = "accountTab";
	public MyAccountView(PersonalInformationPresenter pip,
			SecurityQuestionsPresenter sqp, 
			ChangePasswordPresenter cpp) {
		this.pip = pip;
		tabLayout = new MatTabLayoutPanel(true);
		
		String title;
		
		title = "Personal Information";
		tabLayout.addPresenter(pip,	title);
		
		title = "Security Questions";
		tabLayout.addPresenter(sqp, title);
		
		title = "Password";
		tabLayout.addPresenter(cpp, title);
		
		tabLayout.setId("myAccountTabLayout");
		MatContext.get().tabRegistry.put(MY_ACCOUNT_TAB,tabLayout);
		tabLayout.addSelectionHandler(new SelectionHandler<Integer>(){
			public void onSelection(SelectionEvent<Integer> event) {
				int index = ((SelectionEvent<Integer>) event).getSelectedItem();
				// suppressing token dup
				String newToken = MY_ACCOUNT_TAB + index;
				if(!History.getToken().equals(newToken)){
					MatContext.get().recordTransactionEvent(null, null, "MY_ACCT_TAB_EVENT", newToken, 1);
					History.newItem(newToken, false);		
				}
			}});
	}

	public void beforeDisplay() {
		fp.add(tabLayout);
		pip.beforeDisplay();
		tabLayout.selectTab(pip);
	}

	class EnterKeyDownHandler implements KeyDownHandler {
		private int i = 0;
		public EnterKeyDownHandler(int index){
			i = index;
		}
		@Override
		public void onKeyDown(KeyDownEvent event) {
			if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
				tabLayout.selectTab(i);
			}
		}
	}

	@Override
	public Widget getWidget() {
		return fp;
	}
}
