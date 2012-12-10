package mat.client.myAccount;

import java.io.IOException;

import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.event.SuccessfulLoginEvent;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.shared.PasswordVerifier;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

public class ChangePasswordPresenter implements MatPresenter {
	public static interface Display {
		public HasValue<String> getPassword();
		public HasValue<String> getConfirmPassword();
		
		public HasClickHandlers getSubmit();
		public HasClickHandlers getReset();
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		public SuccessMessageDisplayInterface getSuccessMessageDisplay();
		
		public Widget asWidget();
	}
	
	private final Display display;
	private MyAccountModel myAccountModel;
	
	public ChangePasswordPresenter(Display displayArg) {
		this.display = displayArg;
		
		display.getReset().addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				clearValues();
			}
		});
		
		display.getSubmit().addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				try {
					submitChangePassword();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	@Override
	public Widget getWidget() {;
		return display.asWidget();
	}
	
	
	@Override
	public void beforeDisplay() {
		
		MatContext.get().getMyAccountService().getMyAccount(new AsyncCallback<MyAccountModel>() {
			
			public void onSuccess(MyAccountModel result) {
				myAccountModel = result;
			}
			
			public void onFailure(Throwable caught) {
				display.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
			}
		});		
		clearValues();
		Mat.focusSkipLists("ChangePassword");
	}
	@Override 
	public void beforeClosingDisplay() {
		
	}
	
	
	private void clearValues() {
		display.getPassword().setValue("");
		display.getConfirmPassword().setValue("");
		display.getErrorMessageDisplay().clear();
		display.getSuccessMessageDisplay().clear();
	}
	private void submitChangePassword() throws IOException {

		PasswordVerifier verifier = new PasswordVerifier(
				myAccountModel.getEmailAddress(), 
				display.getPassword().getValue(), 
				display.getConfirmPassword().getValue());
		
		if(!verifier.isValid()) {
			display.getErrorMessageDisplay().setMessages(verifier.getMessages());
		}
		else {
			display.getErrorMessageDisplay().clear();
			MatContext.get().getMyAccountService().changePassword(display.getPassword().getValue(), 
					new AsyncCallback<String>() {

						public void onFailure(Throwable caught) {
							display.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
							MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
						}
						/*@Override
						public void onSuccess(Boolean result) {
							if(result){
								clearValues();
								display.getSuccessMessageDisplay().setMessage( MatContext.get().getMessageDelegate().getPasswordSavedMessage());
							}else{
								display.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getMustNotContainDictionaryWordMessage());
								
							}
						}*/
						@Override
						public void onSuccess(String result) {
							if(result.equalsIgnoreCase("SUCCESS")){
								clearValues();
								display.getSuccessMessageDisplay().setMessage( MatContext.get().getMessageDelegate().getPasswordSavedMessage());
							}else if(result.equalsIgnoreCase("EXCEPTION")){
								display.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
							}else{
								display.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getMustNotContainDictionaryWordMessage());
							}
						}
					});
		}
		
	}
}
