package mat.client.login;

import java.util.List;

import mat.client.Mat;

import mat.client.event.ReturnToLoginEvent;
import mat.client.event.SuccessfulLoginEvent;
import mat.client.login.service.LoginResult;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.client.shared.NameValuePair;
import mat.model.UserSecurityQuestion;
import mat.shared.PasswordVerifier;
import mat.shared.SecurityQuestionVerifier;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class TempPwdLoginPresenter {
	public static interface Display {
		public HasClickHandlers getSubmit();
		public HasClickHandlers getReset();
		public HasValue<String> getQuestion1Answer();
		public HasValue<String> getQuestion2Answer();
		public HasValue<String> getQuestion3Answer();
		public HasValue<String> getQuestion1Text();
		public HasValue<String> getQuestion2Text();
		public HasValue<String> getQuestion3Text();
		public HasValue<String> getPassword();
		public HasValue<String> getConfirmPassword();
		public ErrorMessageDisplayInterface getPasswordErrorMessageDisplay();
		public ErrorMessageDisplayInterface getSecurityErrorMessageDisplay();
		public void addSecurityQuestionTexts(List<NameValuePair> texts);
		public Widget asWidget();
		
	}

	
	private final Display display;
	
	public TempPwdLoginPresenter(Display displayArg) {
		
		this.display = displayArg;

		MatContext.get().getSecurityQuestions(new AsyncCallback<List<NameValuePair>>() {
			public void onSuccess(List<NameValuePair> values) {
				display.addSecurityQuestionTexts(values);
			}
			public void onFailure(Throwable t) {
				display.getSecurityErrorMessageDisplay().setMessage(t.getMessage());
			}
		});

		display.getReset().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				reset();
			}
		});

		display.getSubmit().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
					PasswordVerifier verifier = null;
					
						verifier = new PasswordVerifier(
								MatContext.get().getLoggedinLoginId(), 
								display.getPassword().getValue(), 
								display.getConfirmPassword().getValue());
					

					if(!verifier.isValid()) {
						display.getPasswordErrorMessageDisplay().setMessages(verifier.getMessages());
					}else{
						display.getPasswordErrorMessageDisplay().clear();
					}

					SecurityQuestionVerifier sverifier = 
													new SecurityQuestionVerifier(display.getQuestion1Text().getValue(),
																display.getQuestion1Answer().getValue(),
																display.getQuestion2Text().getValue(),
																display.getQuestion2Answer().getValue(),
																display.getQuestion3Text().getValue(),
																display.getQuestion3Answer().getValue());
					if(!sverifier.isValid()) {
						display.getSecurityErrorMessageDisplay().setMessages(sverifier.getMessages());
					}else{
						display.getSecurityErrorMessageDisplay().clear();
					}

					if(verifier.isValid() && sverifier.isValid()) {
						MatContext.get().changePasswordSecurityQuestions(getValues(), new AsyncCallback<LoginResult>() {
							@Override
							public void onFailure(Throwable caught) {
								display.getSecurityErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
							}
							@Override
							public void onSuccess(LoginResult result) {
								if(result.isSuccess()){
									MatContext.get().getEventBus().fireEvent(new SuccessfulLoginEvent());
								}else {
									switch(result.getFailureReason()) {
										case LoginResult.SERVER_SIDE_VALIDATION_SECURITY_QUESTIONS:
											display.getPasswordErrorMessageDisplay().setMessages(result.getMessages());
											break;
										case LoginResult.SERVER_SIDE_VALIDATION_PASSWORD:
											display.getSecurityErrorMessageDisplay().setMessages(result.getMessages());
											break;
										case LoginResult.DICTIONARY_EXCEPTION:
											display.getPasswordErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getMustNotContainDictionaryWordMessage());
											break;
										default:
											display.getSecurityErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getUnknownErrorMessage(result.getFailureReason()));
									}
									
								}
							}
						});
					
				}

			}
		});

		display.getReset().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				reset();
				MatContext.get().getEventBus().fireEvent(new ReturnToLoginEvent());	
			}
		});
	}

	private void reset() {
		display.getPasswordErrorMessageDisplay().clear();
		display.getSecurityErrorMessageDisplay().clear();
		display.getPassword().setValue("");
		display.getConfirmPassword().setValue("");
		display.getQuestion1Answer().setValue("");
		display.getQuestion2Answer().setValue("");
		display.getQuestion3Answer().setValue("");
		display.getPasswordErrorMessageDisplay().clear();
		display.getSecurityErrorMessageDisplay().clear();
	}

	private LoginModel getValues() {
		LoginModel model = new LoginModel();
		model.setUserId(MatContext.get().getLoggedinUserId());
		model.setEmail(MatContext.get().getLoggedInUserEmail());
		model.setLoginId(MatContext.get().getLoggedinLoginId());
		model.setPassword(display.getPassword().getValue());
		model.setQuestion1(display.getQuestion1Text().getValue());
		model.setQuestion1Answer(display.getQuestion1Answer().getValue());
		model.setQuestion2(display.getQuestion2Text().getValue());
		model.setQuestion2Answer(display.getQuestion2Answer().getValue());
		model.setQuestion3(display.getQuestion3Text().getValue());
		model.setQuestion3Answer(display.getQuestion3Answer().getValue());
		return model;
	}
	public void go(HasWidgets container) {
		reset();
		beforeDisplay();
		container.add(display.asWidget());
	}	
	
	private void beforeDisplay() {
		
		MatContext.get().getLoginService().getSecurityQuestionsAnswers(MatContext.get().getLoggedinUserId(), new AsyncCallback<List<UserSecurityQuestion>>(){
			
			public void onFailure(Throwable caught) {
				display.getSecurityErrorMessageDisplay().clear();
				display.getSecurityErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
			}

			@Override
			public void onSuccess(List<UserSecurityQuestion> result) {
				if(result !=null && result.size()>0){
					display.getQuestion1Answer().setValue(result.get(0).getSecurityAnswer());
					display.getQuestion1Text().setValue(result.get(0).getSecurityQuestion());

					display.getQuestion2Answer().setValue(result.get(1).getSecurityAnswer());
					display.getQuestion2Text().setValue(result.get(1).getSecurityQuestion());
				
					display.getQuestion3Answer().setValue(result.get(2).getSecurityAnswer());
					display.getQuestion3Text().setValue(result.get(2).getSecurityQuestion());
				}
			}
		});
		Mat.focusSkipLists("SecurityInfo");
	}
		
		
		
		
		
		
	
	/*	this.display = displayArg;
		display.getSubmit().addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
					submitTempChangePassword();
			}
		});
	}
	
	private void submitTempChangePassword() {
		display.getPasswordErrorMessageDisplay().clear();
		PasswordVerifier verifier = new PasswordVerifier(MatContext.get().getLoggedinLoginId(),display.getPassword().getValue(), 
										display.getConfirmPassword().getValue());

		if(!verifier.isValid()) {
			display.getPasswordErrorMessageDisplay().setMessages(verifier.getMessages());
		}else {
			
			MatContext.get().getLoginService().changeTempPassword(MatContext.get().getLoggedinLoginId(),
																				display.getPassword().getValue(), 
																				new AsyncCallback<LoginModel>() {
			public void onSuccess(LoginModel result) {
				loginModel = result;
				if(loginModel.isInitialPassword()){
					  MatContext.get().getEventBus().fireEvent(new FirstLoginPageEvent());
				 }else if(loginModel.isLoginFailedEvent()){
					  display.getPasswordErrorMessageDisplay().setMessage(loginModel.getErrorMessage());
				 }else{
					  MatContext.get().getEventBus().fireEvent(new SuccessfulLoginEvent());
				 }
			}
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			    display.getPasswordErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
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
		//displayTempChangePswdView();
	}
	
	private void reset() {
		display.getPassword().setValue("");
		display.getConfirmPassword().setValue("");
		display.getPasswordErrorMessageDisplay().clear();
	}*/
	
}
