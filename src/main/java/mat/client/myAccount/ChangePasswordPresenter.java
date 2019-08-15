package mat.client.myAccount;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.gwtbootstrap3.client.ui.Input;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.login.service.LoginServiceAsync;
import mat.client.myAccount.service.SaveMyAccountResult;
import mat.client.shared.MatContext;
import mat.client.shared.MessageAlert;
import mat.client.shared.PasswordEditInfoWidget;
import mat.client.util.ClientConstants;
import mat.shared.PasswordVerifier;
import mat.shared.StringUtility;

/**
 * The Class ChangePasswordPresenter.
 */
public class ChangePasswordPresenter implements MatPresenter {
	
	/** The login service. */
	LoginServiceAsync loginService = MatContext.get().getLoginService();
	
	
	/**
	 * The Interface Display.
	 */
	public static interface Display {
		
		/**
		 * Gets the password.
		 * 
		 * @return the password
		 */
		public Input getPassword();
		
		/**
		 * Gets the confirm password.
		 * 
		 * @return the confirm password
		 */
		public Input getConfirmPassword();
		
		/**
		 * Gets the submit.
		 * 
		 * @return the submit
		 */
		public HasClickHandlers getSubmit();
		
		/**
		 * Gets the reset.
		 * 
		 * @return the reset
		 */
		public HasClickHandlers getReset();
		
		/**
		 * Gets the error message display.
		 * 
		 * @return the error message display
		 */
		public MessageAlert getErrorMessageDisplay();
		
		/**
		 * Gets the success message display.
		 * 
		 * @return the success message display
		 */
		public MessageAlert getSuccessMessageDisplay();
		
		/**
		 * As widget.
		 * 
		 * @return the widget
		 */
		public Widget asWidget();
		
		/**
		 * Gets the current password.
		 * 
		 * @return the current password
		 */
		Input getCurrentPassword();
		
		/**
		 * Gets the current password widget.
		 * 
		 * @return the current password widget
		 */
		PasswordEditInfoWidget getPasswordEditInfoWidget();
		
	}
	
	
	/** The submit on enter handler. */
	private KeyDownHandler submitOnEnterHandler = new KeyDownHandler() {
		@Override
		public void onKeyDown(KeyDownEvent event) {
			if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
				saveChangedPassword(display.getCurrentPassword().getText());
			}
		}
	};
	
	
	/** The display. */
	private final Display display;
	
	/** The my account model. */
	private MyAccountModel myAccountModel;
	
	/**
	 * Instantiates a new change password presenter.
	 * 
	 * @param displayArg
	 *            the display arg
	 */
	public ChangePasswordPresenter(Display displayArg) {
		display = displayArg;
		
		display.getReset().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				clearValues();
			}
		});
		
		display.getSubmit().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				String newPassword = display.getPassword().getText();
				String confirmPassword = display.getConfirmPassword().getText();
				if ((newPassword == null) || newPassword.equals("") || (confirmPassword == null)
						|| confirmPassword.equals("")) {
					display.getErrorMessageDisplay().clearAlert();
					display.getSuccessMessageDisplay().clearAlert();
					display.getErrorMessageDisplay().createAlert(
							MatContext.get().getMessageDelegate().getAllPasswordFieldsRequired());
				} else {
					saveChangedPassword(display.getCurrentPassword().getText());
					display.getCurrentPassword().setText("");
				}
			}
		});
		
		display.getPasswordEditInfoWidget().getPassword().addKeyDownHandler(submitOnEnterHandler);
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#getWidget()
	 */
	/**
	 * Gets the widget.
	 *
	 * @return the widget
	 */
	@Override
	public Widget getWidget() {
		return display.asWidget();
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	/**
	 * Before display.
	 */
	@Override
	public void beforeDisplay() {
		
		MatContext.get().getMyAccountService().getMyAccount(new AsyncCallback<MyAccountModel>() {

			@Override
			public void onSuccess(MyAccountModel result) {
				MatContext.get().getMyAccountService().getSecurityQuestions(new AsyncCallback<SecurityQuestionsModel>() {
					@Override
					public void onSuccess(SecurityQuestionsModel result) {
						if(StringUtility.isEmptyOrNull(result.getQuestion1Answer())) {
							display.getErrorMessageDisplay().createAlert("Your Security Questions require updating. Please update the Security Questions prior to changing your password.");
						}
					}
					
					@Override
					public void onFailure(Throwable caught) {
					
					}
				});
				
				
				myAccountModel = result;
			}
			
			@Override
			public void onFailure(Throwable caught) {
				display.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+ caught.getLocalizedMessage(), 0);
			}
		});
		
		clearValues();
		Mat.focusSkipLists("ChangePassword");
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	/**
	 * Before closing display.
	 */
	@Override
	public void beforeClosingDisplay() {
		
	}
	
	
	/**
	 * Clear values.
	 */
	private void clearValues() {
		display.getPassword().setText("");
		display.getConfirmPassword().setText("");
		display.getCurrentPassword().setText("");
		display.getErrorMessageDisplay().clearAlert();
		display.getSuccessMessageDisplay().clearAlert();
	}
	
	/**
	 * Save changed password.
	 * 
	 * @param password
	 *            the password
	 */
	private void saveChangedPassword(String password){
		MatContext.get().getMyAccountService().getSecurityQuestions(new AsyncCallback<SecurityQuestionsModel>() {
			@Override
			public void onSuccess(SecurityQuestionsModel result) {
				if(StringUtility.isEmptyOrNull(result.getQuestion1Answer())) {
					display.getErrorMessageDisplay().createAlert("The password update was not successful. Please update your Security Questions then try again.");
				} else {
					loginService.validatePassword(MatContext.get().getLoggedinLoginId(), password, new AsyncCallback<HashMap<String,String>>(){
						@Override
						public void onSuccess(HashMap<String,String> resultMap) {
							String result = resultMap.get("result");
							if(result.equals("SUCCESS")){
								try {
									ValidatePasswordCreation();
								} catch (IOException e) {
									e.printStackTrace();
								}
							} else {
								display.getErrorMessageDisplay().clearAlert();
								display.getSuccessMessageDisplay().clearAlert();
								String displayErrorMsg= resultMap.get("message");
								if(displayErrorMsg.equals("REDIRECT")){
									MatContext.get().redirectToHtmlPage(ClientConstants.HTML_LOGIN);
								}else{
									display.getErrorMessageDisplay().createAlert(displayErrorMsg);
								}
							}
						}
						@Override
						public void onFailure(Throwable caught) {
							Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						}

					});
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}
		});
	}
	
	/**
	 * Submit change password.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void submitChangePassword() throws IOException {
		
		display.getErrorMessageDisplay().clearAlert();
		MatContext.get().getMyAccountService().changePassword(display.getPassword().getText(),
				new AsyncCallback<SaveMyAccountResult>() {
			
			@Override
			public void onFailure(Throwable caught) {
				display.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
			}
			
			@Override
			public void onSuccess(SaveMyAccountResult result) {
				if(result.isSuccess()){
					clearValues();
					display.getSuccessMessageDisplay().createAlert( MatContext.get().getMessageDelegate().getPasswordSavedMessage());
				}else{
					List<String> messages = new ArrayList<String>();
					switch(result.getFailureReason()) {
						case SaveMyAccountResult.SERVER_SIDE_VALIDATION:
							messages = result.getMessages();
							break;
						case SaveMyAccountResult.DICTIONARY_EXCEPTION:
							messages.add(MatContext.get().getMessageDelegate().getMustNotContainDictionaryWordMessage());
							break;
						default:
							messages.add(MatContext.get().getMessageDelegate().getUnknownErrorMessage(result.getFailureReason()));
					}
					display.getErrorMessageDisplay().createAlert(messages);
					
				}
			}
		});
	}
	
	/**
	 * Validate changed password.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void ValidateChangedPassword() throws IOException{
		String markupRegExp = "<[^>]+>";
		String noMarkupTextPwd = display.getPassword().getText().trim().replaceAll(markupRegExp, "");
		display.getPassword().setText(noMarkupTextPwd);
		
		String noMarkupTextConfirm = display.getConfirmPassword().getText().trim().replaceAll(markupRegExp, "");
		display.getConfirmPassword().setText(noMarkupTextConfirm);
		
		PasswordVerifier verifier = new PasswordVerifier(
				myAccountModel.getLoginId(),
				display.getPassword().getText(),
				display.getConfirmPassword().getText());
		
		if(!verifier.isValid()) {
			display.getErrorMessageDisplay().createAlert(verifier.getMessages());
		}else{
			loginService.validateNewPassword(MatContext.get().getLoggedinLoginId(), display.getPassword().getText(), new AsyncCallback<HashMap<String,String>>(){
				
				
				/* (non-Javadoc)
				 * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
				 */
				@Override
				public void onFailure(Throwable caught) {
					display.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
					MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
				}
				
				/**
				 * On success.
				 *
				 * @param resultMap the result map
				 */
				@Override
				public void onSuccess(HashMap<String, String> resultMap) {
					String result = resultMap.get("result");
					if(result.equals("SUCCESS")){
						display.getSuccessMessageDisplay().clearAlert();
						display.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getIS_NOT_CURRENT_PASSWORD());
					}
					else{
						try {
							submitChangePassword();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				
			});
			
		}
	}
	
	/** for validating Password Creation with current date and restricting
	 * user to change password on the same day of new password creation.
	 * Validate password creation Dates.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void ValidatePasswordCreation() throws IOException{
		loginService.validatePasswordCreationDate(MatContext.get().getLoggedinLoginId(), new AsyncCallback<HashMap<String,String>>(){
			
			@Override
			public void onFailure(Throwable caught) {
				display.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
			}
			
			/**
			 * On success.
			 *
			 * @param resultMap the result map
			 */
			@Override
			public void onSuccess(HashMap<String, String> resultMap) {
				
				String result = resultMap.get("result");
				if(result.equals("SUCCESS")){
					display.getSuccessMessageDisplay().clearAlert();
					display.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getCHANGE_OLD_PASSWORD());
				}
				else{
					try {
						ValidateChangedPassword();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
		});
	}
	
}
