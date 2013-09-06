package mat.client.umls;

import mat.client.MatPresenter;
import mat.client.shared.ErrorMessageDisplayInterface;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;


public class ManageUmlsPresenter implements MatPresenter{

	public static interface UMLSDisplay {
		public HasClickHandlers getSubmit();
		public HasValue<String> getUserid();
		public HasValue<String> getPassword();
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		public HasHTML getInfoMessage();
		public void setWelcomeVisible(boolean value);
		public void setInfoMessageVisible(boolean value);
		public HasClickHandlers getForgotPassword();
		public HasClickHandlers getForgotLoginId();
		public Widget asWidget();

		public HasKeyDownHandlers getUseridField();
		public HasKeyDownHandlers getPasswordField();
		public void setInitialFocus();
	}
	
	private  UMLSDisplay display;
	
	
	public ManageUmlsPresenter(UMLSDisplay displayArg) {
		this.display = displayArg;
		resetWidget();
		display.getSubmit().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				
			}


		});
		/*display.getForgotPassword().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				
				MatContext.get().getEventBus().fireEvent(new ForgottenPasswordEvent());
			}
		});
		
		display.getForgotLoginId().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				
				MatContext.get().getEventBus().fireEvent(new ForgotLoginIDEvent());
			}
		});*/
		display.getUseridField().addKeyDownHandler(submitOnEnterHandler);
		display.getPasswordField().addKeyDownHandler(submitOnEnterHandler);
	}
	
	private KeyDownHandler submitOnEnterHandler = new KeyDownHandler() {
		@Override
		public void onKeyDown(KeyDownEvent event) {
			if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
				
			}
		}
	};
	
	private void resetWidget() {
		display.getErrorMessageDisplay().clear();
		display.setInfoMessageVisible(false);
		display.getPassword().setValue("");
		display.getUserid().setValue("");
	}
	
	@Override
	public void beforeDisplay() {
		resetWidget();
		display.asWidget();
	}

	@Override
	public void beforeClosingDisplay() {
		resetWidget();
		
	}

	@Override
	public Widget getWidget() {
		
		return display.asWidget();
	}

}
