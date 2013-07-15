package mat.client.login;


import java.util.ArrayList;
import java.util.List;

import mat.client.event.ReturnToLoginEvent;
import mat.client.event.SuccessfulLoginEvent;
import mat.client.login.service.LoginResult;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.client.shared.NameValuePair;
import mat.model.SecurityQuestions;
import mat.client.shared.SecurityQuestionWithMaskedAnswerWidget;
import mat.shared.PasswordVerifier;
import mat.shared.SecurityQuestionVerifier;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class FirstLoginPresenter {
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
		public String getAnswerText1();
		public String getAnswerText2();
		public String getAnswerText3();
		public void setAnswerText1(String answerText1);
		public void setAnswerText2(String answerText2);
		public void setAnswerText3(String answerText3);
		SecurityQuestionWithMaskedAnswerWidget getSecurityQuestionsWidget();
	}

	private final Display display;

	public FirstLoginPresenter(Display displayArg) {
		this.display = displayArg;
			
		MatContext.get().getLoginService().getSecurityQuestions(new AsyncCallback<List<SecurityQuestions>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				Window.alert("Error fetching security questions :: " + caught.getMessage());
			}

			@Override
			public void onSuccess(List<SecurityQuestions> result) {
				// TODO Auto-generated method stub
				if(result != null){
					List<NameValuePair> retList = new ArrayList<NameValuePair>();
					for(int i=0; i < result.size();i++){
							SecurityQuestions securityQues = result.get(i);
							NameValuePair nvp = new NameValuePair();
							nvp.setName(securityQues.getQuestion());
							nvp.setValue(securityQues.getQuestion());
							retList.add(nvp);
					}
						
					if(retList!=null){
						//display.addQuestionTexts(retList);
						display.addSecurityQuestionTexts(retList);
					}
				}
			}
			
		});
		
		
		/*{

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				display.getSecurityErrorMessageDisplay().setMessage(caught.getMessage());
			}

			@Override
			public void onSuccess(List<SecurityQuestions> result) {
				// TODO Auto-generated method stub
				List<NameValuePair> retList = new ArrayList<NameValuePair>();
				for(int i=0; i < result.size();i++){
						SecurityQuestions securityQues = result.get(i);
						NameValuePair nvp = new NameValuePair();
						nvp.setName(securityQues.getQuestion());
						nvp.setValue(securityQues.getQuestion());
						retList.add(nvp);
				}
					
				if(retList!=null){
					//display.addQuestionTexts(retList);
					display.addSecurityQuestionTexts(retList);
				}
			}
		});*/
			
		display.getReset().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				reset();
			}
		});

		display.getSecurityQuestionsWidget().getAnswer1().addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				display.getSecurityQuestionsWidget().getAnswer1().setText("");
				
			}
		});
		display.getSecurityQuestionsWidget().getAnswer1().addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				if(!(display.getSecurityQuestionsWidget().getAnswer1().getText()).isEmpty())
					display.getSecurityQuestionsWidget().setAnswerText1(display.getSecurityQuestionsWidget().getAnswer1().getText());
				display.getSecurityQuestionsWidget().getAnswer1().setText(display.getSecurityQuestionsWidget().maskAnswers(display.getSecurityQuestionsWidget().getAnswerText1()));
			}
		});
		
		display.getSecurityQuestionsWidget().getAnswer2().addFocusHandler(new FocusHandler() {
			
			@Override
			public void onFocus(FocusEvent event) {
				display.getSecurityQuestionsWidget().getAnswer2().setText("");
			}
		});
		display.getSecurityQuestionsWidget().getAnswer2().addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {

				if(!(display.getSecurityQuestionsWidget().getAnswer2().getText()).isEmpty())
					display.getSecurityQuestionsWidget().setAnswerText2(display.getSecurityQuestionsWidget().getAnswer2().getText());
					
				display.getSecurityQuestionsWidget().getAnswer2().setText(display.getSecurityQuestionsWidget().maskAnswers(display.getSecurityQuestionsWidget().getAnswerText2()));
			}
		});
		display.getSecurityQuestionsWidget().getAnswer3().addFocusHandler(new FocusHandler() {
			
			@Override
			public void onFocus(FocusEvent event) {
				display.getSecurityQuestionsWidget().getAnswer3().setText("");
			}
		});
		display.getSecurityQuestionsWidget().getAnswer3().addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				if(!(display.getSecurityQuestionsWidget().getAnswer3().getText()).isEmpty())
					display.getSecurityQuestionsWidget().setAnswerText3(display.getSecurityQuestionsWidget().getAnswer3().getText());
				display.getSecurityQuestionsWidget().getAnswer3().setText(display.getSecurityQuestionsWidget().maskAnswers(display.getSecurityQuestionsWidget().getAnswerText3()));
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
		model.setQuestion1(display.getSecurityQuestionsWidget().getSecurityQuestion1().getValue());
		model.setQuestion1Answer(display.getSecurityQuestionsWidget().getAnswerText1());
		model.setQuestion2(display.getSecurityQuestionsWidget().getSecurityQuestion2().getValue());
		model.setQuestion2Answer(display.getSecurityQuestionsWidget().getAnswerText2());
		model.setQuestion3(display.getSecurityQuestionsWidget().getSecurityQuestion3().getValue());
		model.setQuestion3Answer(display.getSecurityQuestionsWidget().getAnswerText3());
		return model;
	}
	public void go(HasWidgets container) {
		reset();
		container.add(display.asWidget());
	}
}
