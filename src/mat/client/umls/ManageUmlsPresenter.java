package mat.client.umls;

import mat.client.MatPresenter;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.client.umls.service.VSACAPIServiceAsync;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
		public Widget asWidget();
		public HasKeyDownHandlers getUseridField();
		public HasKeyDownHandlers getPasswordField();
		public void setInitialFocus();
	}
	
	VSACAPIServiceAsync vsacapiService  = MatContext.get().getVsacapiServiceAsync();
	private  UMLSDisplay display;
	
	
	public ManageUmlsPresenter(UMLSDisplay displayArg) {
		this.display = displayArg;
		resetWidget();
		display.getSubmit().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				submit();
			}


		});
		
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
	
	private void submit() {
		display.getErrorMessageDisplay().clear();
		display.setInfoMessageVisible(false);
		if(display.getUserid().getValue().isEmpty()) {
			display.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getLoginIDRequiredMessage());
		}else if(display.getPassword().getValue().isEmpty()) {
			display.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getPasswordRequiredMessage());
		}else{
			vsacapiService.validateVsacUser(display.getUserid().getValue(), display.getPassword().getValue(), new AsyncCallback<String>() {
				
				@Override
				public void onSuccess(String result) {
				
					display.getUserid().setValue("");
					display.getPassword().setValue("");
					if(result!=null){
						display.getInfoMessage().setText("Successfully logged into UMLS");
						Window.alert("Ticket Granted for session :  " +result);
					}
					else
						display.getErrorMessageDisplay().setMessage("Unable to verify your credentials on UMLS. Please contact UMLS helpdesk or try again.");
					
				}
				
				@Override
				public void onFailure(Throwable caught) {
					display.getErrorMessageDisplay().setMessage("Unable to verify your credentials on UMLS. Please contact UMLS helpdesk or try again.");
					caught.printStackTrace();
				}
			});
		}
	}
	
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
