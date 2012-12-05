package mat.client.login;

import mat.client.event.FirstLoginPageEvent;
import mat.client.event.SuccessfulLoginEvent;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.shared.PasswordVerifier;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class TempPwdLoginPresenter {
	public static interface Display {
		public HasValue<String> getPassword();
		public HasValue<String> getConfirmPassword();
		
		public HasClickHandlers getSubmit();
		public void setWelcomeVisible(boolean value);
		public void setInfoMessageVisible(boolean value);
		public ErrorMessageDisplayInterface getErrorMessageDisplay();

		public Widget asWidget();
		
		public HasKeyDownHandlers getNewPasswordField();
		public HasKeyDownHandlers getConfirmPasswordField();
		
	}
	
	private final Display display;
	private LoginModel loginModel;
	
	public TempPwdLoginPresenter(Display displayArg) {
		this.display = displayArg;
		display.getSubmit().addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
					submitTempChangePassword();
			}
		});
	}
	
	private void submitTempChangePassword() {
		display.getErrorMessageDisplay().clear();
		PasswordVerifier verifier = new PasswordVerifier(MatContext.get().getLoggedInUserEmail(),display.getPassword().getValue(), 
										display.getConfirmPassword().getValue());

		if(!verifier.isValid()) {
			display.getErrorMessageDisplay().setMessages(verifier.getMessages());
		}else {
			
			MatContext.get().getLoginService().changeTempPassword(MatContext.get().getLoggedInUserEmail(),
																				display.getPassword().getValue(), 
																				new AsyncCallback<LoginModel>() {
			public void onSuccess(LoginModel result) {
				loginModel = result;
				if(loginModel.isInitialPassword()){
					  MatContext.get().getEventBus().fireEvent(new FirstLoginPageEvent());
				 }else if(loginModel.isLoginFailedEvent()){
					  display.getErrorMessageDisplay().setMessage(loginModel.getErrorMessage());
				 }else{
					  MatContext.get().getEventBus().fireEvent(new SuccessfulLoginEvent());
				 }
			}
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			    display.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
				}
			}
		);
	}
}
	public Widget getWidget() {
		return display.asWidget();
	}


	public void go(HasWidgets container) {
		reset();
		container.add(display.asWidget());
		displayTempChangePswdView();
	}
	
	private void reset() {
		display.getPassword().setValue("");
		display.getConfirmPassword().setValue("");
		display.getErrorMessageDisplay().clear();
	}
	private void displayTempChangePswdView(){
		display.setWelcomeVisible(true);
		display.setInfoMessageVisible(false);
	}
}
