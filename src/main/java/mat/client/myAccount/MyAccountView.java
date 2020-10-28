package mat.client.myAccount;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.MatPresenter;
import mat.client.TabObserver;
import mat.client.shared.MatContext;
import mat.client.shared.MatTabLayoutPanel;

import java.util.LinkedList;
import java.util.List;

/**
 * The Class MyAccountView.
 */
public class MyAccountView implements MyAccountPresenter.Display, TabObserver {
	
	private FlowPanel fp = new FlowPanel();
	private MatTabLayoutPanel tabLayout;
	private PersonalInformationPresenter pip;
	private final String MY_ACCOUNT_TAB = "accountTab";
	private List<MatPresenter> presenterList;
	
	/**
	 * Instantiates a new my account view.
	 * 
	 * @param pip
	 *            the pip
	 */
	@SuppressWarnings("unchecked")
	public MyAccountView(PersonalInformationPresenter pip) {
		presenterList = new LinkedList<MatPresenter>();
		this.pip = pip;
		tabLayout = new MatTabLayoutPanel(this);
		
		String title;
		
		title = "Personal Information";
		tabLayout.add(pip.getWidget(),	title, true);
		presenterList.add(pip);
		
		tabLayout.getElement().setAttribute("id", "myAccountTabLayout");
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

	/* (non-Javadoc)
	 * @see mat.client.myAccount.MyAccountPresenter.Display#beforeDisplay()
	 */
	public void beforeDisplay() {
		fp.add(tabLayout);
		pip.beforeDisplay();
		tabLayout.selectTab(presenterList.indexOf(pip));
	}

	/**
	 * The Class EnterKeyDownHandler.
	 */
	class EnterKeyDownHandler implements KeyDownHandler {
		
		/** The i. */
		private int i = 0;
		
		/**
		 * Instantiates a new enter key down handler.
		 * 
		 * @param index
		 *            the index
		 */
		public EnterKeyDownHandler(int index){
			i = index;
		}
		
		/* (non-Javadoc)
		 * @see com.google.gwt.event.dom.client.KeyDownHandler#onKeyDown(com.google.gwt.event.dom.client.KeyDownEvent)
		 */
		@Override
		public void onKeyDown(KeyDownEvent event) {
			if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
				tabLayout.selectTab(i);
			}
		}
	}

	/* (non-Javadoc)
	 * @see mat.client.myAccount.MyAccountPresenter.Display#getWidget()
	 */
	@Override
	public Widget getWidget() {
		return fp;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public void updateOnBeforeSelection() {
		MatPresenter presenter = presenterList.get(tabLayout.getSelectedIndex());
		if (presenter != null) {
			MatContext.get().setAriaHidden(presenter.getWidget(),  "false");
			presenter.beforeDisplay();
		}
	}

	@Override
	public void showUnsavedChangesError() {}

	@Override
	public void notifyCurrentTabOfClosing() {
		MatPresenter oldPresenter = presenterList.get(tabLayout.getSelectedIndex());
		if (oldPresenter != null) {
			MatContext.get().setAriaHidden(oldPresenter.getWidget(), "true");
			oldPresenter.beforeClosingDisplay();
		}	
	}
}
