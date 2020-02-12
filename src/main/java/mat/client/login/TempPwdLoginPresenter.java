package mat.client.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import mat.client.Mat;
import mat.client.event.ReturnToLoginEvent;
import mat.client.event.SuccessfulLoginEvent;
import mat.client.login.service.LoginResult;
import mat.client.login.service.LoginServiceAsync;
import mat.client.myAccount.SecurityQuestionsModel;
import mat.client.shared.MatContext;
import mat.client.shared.MessageDelegate;
import mat.client.shared.NameValuePair;
import mat.client.shared.SecurityQuestionsDisplay;
import mat.model.SecurityQuestions;
import mat.model.UserSecurityQuestion;
import mat.shared.PasswordVerifier;
import mat.shared.SecurityQuestionVerifier;
import mat.shared.StringUtility;

/**
 * The Class TempPwdLoginPresenter.
 */
public class TempPwdLoginPresenter {
	
	/** The login service. */
	LoginServiceAsync loginService = MatContext.get().getLoginService();
	
	
	/** The display. */
	private final SecurityQuestionsDisplay display;
	
	/**
	 * Instantiates a new temp pwd login presenter.
	 * 
	 * @param displayArg
	 *            the display arg
	 */
	public TempPwdLoginPresenter(SecurityQuestionsDisplay displayArg) {
		
		display = displayArg;
		
		display.getReset().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				reset();
			}
		});		
		
		display.getSubmit().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				PasswordVerifier verifier = null;
				String markupRegExp = "<[^>]+>";
				String noMarkupTextPwd = display.getPassword().getText().trim().replaceAll(markupRegExp, "");
				display.getPassword().setText(noMarkupTextPwd);
				
				String noMarkupTextConfirm = display.getConfirmPassword().getText().trim().replaceAll(markupRegExp, "");
				display.getConfirmPassword().setText(noMarkupTextConfirm);
				verifier = new PasswordVerifier(
						MatContext.get().getLoggedinLoginId(),
						display.getPassword().getText(),
						display.getConfirmPassword().getText());
				if(!verifier.isValid()) {
					display.getPasswordErrorMessageDisplay().createAlert(verifier.getMessages());
				}else{
					display.getPasswordErrorMessageDisplay().clearAlert();
				}

				SecurityQuestionsModel model = getSecurityQuestionsModel();
				SecurityQuestionVerifier sverifier =
						new SecurityQuestionVerifier(model.getQuestion1(),
								model.getQuestion1Answer(),
								model.getQuestion2(),
								model.getQuestion2Answer(),
								model.getQuestion3(),
								model.getQuestion3Answer());
				if(!sverifier.isValid()) {
					display.getSecurityErrorMessageDisplay().createAlert(sverifier.getMessages());
				}else{
					display.getSecurityErrorMessageDisplay().clearAlert();
				}
				
				validateChangedPassword(verifier,sverifier);
			}
		});
		
		display.getReset().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				reset();
				MatContext.get().getEventBus().fireEvent(new ReturnToLoginEvent());
			}
		});
	}
	
	
	
	private SecurityQuestionsModel getSecurityQuestionsModel() {
		SecurityQuestionsModel model = new SecurityQuestionsModel();
		model.setQuestion1(display.getQuestion1Text().getValue());
		model.setQuestion2(display.getQuestion2Text().getValue());
		model.setQuestion3(display.getQuestion3Text().getValue());
		
		String textBox1Value= display.getAnswerText1();
		String hidden1Value = display.getSecurityQuestionsWidget().getAnswer1Value();
		if(!StringUtility.isEmptyOrNull(textBox1Value) && !textBox1Value.equals(MessageDelegate.DEFAULT_SECURITY_QUESTION_VALUE)) {
			model.setQuestion1Answer(textBox1Value);
		} else {
			model.setQuestion1Answer(hidden1Value);
		}
		
		String textBox2Value = display.getAnswerText2();
		String hidden2Value = display.getSecurityQuestionsWidget().getAnswer2Value();
		if(!StringUtility.isEmptyOrNull(textBox2Value) && !textBox2Value.equals(MessageDelegate.DEFAULT_SECURITY_QUESTION_VALUE)) {
			model.setQuestion2Answer(textBox2Value);
		} else {
			model.setQuestion2Answer(hidden2Value);
		}
		
		String textBox3Value = display.getAnswerText3();
		String hidden3Value = display.getSecurityQuestionsWidget().getAnswer3Value();
		
		if(!StringUtility.isEmptyOrNull(textBox3Value) && !textBox3Value.equals(MessageDelegate.DEFAULT_SECURITY_QUESTION_VALUE)) {
			model.setQuestion3Answer(textBox3Value);
		} else {
			model.setQuestion3Answer(hidden3Value);
		}
		
		model.scrubForMarkUp();
		return model;
	}



	/**
	 * Validate changed password.
	 *
	 * @param verifier the verifier
	 * @param sverifier the sverifier
	 */
	public void validateChangedPassword(final PasswordVerifier verifier,final SecurityQuestionVerifier sverifier){
		loginService.validateNewPassword(MatContext.get().getLoggedinLoginId(), display.getPassword().getText(),
				new AsyncCallback<HashMap<String,String>>(){
			
			@Override
			public void onFailure(Throwable caught) {
				display.getPasswordErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
			}
			
			@Override
			public void onSuccess(HashMap<String, String> resultMap) {
				String result = resultMap.get("result");
				if(result.equals("SUCCESS")){
					display.getPasswordErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate()
							.getIS_NOT_PREVIOUS_PASSWORD());
				}
				else{
					onSuccessTempPwdLogin(verifier, sverifier);
				}
			}
		});
	}
	
	
	/**
	 * On success temp pwd login.
	 *
	 * @param verifier the verifier
	 * @param sverifier the sverifier
	 */
	public void onSuccessTempPwdLogin(PasswordVerifier verifier,SecurityQuestionVerifier sverifier){
		if(verifier.isValid() && sverifier.isValid()) {
			MatContext.get().changePasswordSecurityQuestions(getLoginModel(), new AsyncCallback<LoginResult>() {
				@Override
				public void onFailure(Throwable caught) {
					display.getSecurityErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				}
				@Override
				public void onSuccess(LoginResult result) {
					if(result.isSuccess()){
						MatContext.get().getEventBus().fireEvent(new SuccessfulLoginEvent());
					}else {
						switch(result.getFailureReason()) {
							case LoginResult.SERVER_SIDE_VALIDATION_SECURITY_QUESTIONS:
								display.getPasswordErrorMessageDisplay().createAlert(result.getMessages());
								break;
							case LoginResult.SERVER_SIDE_VALIDATION_PASSWORD:
								display.getSecurityErrorMessageDisplay().createAlert(result.getMessages());
								break;
							default:
								display.getSecurityErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate()
										.getUnknownErrorMessage(result.getFailureReason()));
						}
						
					}
				}
			});
		}
	}
	




	/**
	 * Load security questions.
	 */
	private void loadSecurityQuestions(){
		MatContext.get().getLoginService().getSecurityQuestions(new AsyncCallback<List<SecurityQuestions>>() {
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error fetching security questions :: " + caught.getMessage());
			}
			
			@Override
			public void onSuccess(List<SecurityQuestions> result) {
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
						display.addSecurityQuestionTexts(retList);
					}
				}
				beforeDisplay();
			}
			
		});
	}
	
	
	/**
	 * Reset.
	 */
	private void reset() {
		display.getPasswordErrorMessageDisplay().clearAlert();
		display.getSecurityErrorMessageDisplay().clearAlert();
		display.getPassword().setText("");
		display.getConfirmPassword().setText("");
		display.getQuestion1Answer().setValue("");
		display.getQuestion2Answer().setValue("");
		display.getQuestion3Answer().setValue("");
	}
	
	/**
	 * Gets the values.
	 * 
	 * @return the values
	 */
	private LoginModel getLoginModel() {
		LoginModel model = new LoginModel();
		model.setUserId(MatContext.get().getLoggedinUserId());
		model.setEmail(MatContext.get().getLoggedInUserEmail());
		model.setLoginId(MatContext.get().getLoggedinLoginId());
		model.setPassword(display.getPassword().getText());
		
		model.setQuestion1(display.getSecurityQuestionsWidget().getSecurityQuestion1().getValue());
		String textBox1Value= display.getAnswerText1();
		String hidden1Value = display.getSecurityQuestionsWidget().getAnswer1Value();
		
		if(!StringUtility.isEmptyOrNull(textBox1Value) && !textBox1Value.equals(MessageDelegate.DEFAULT_SECURITY_QUESTION_VALUE)) {
			model.setQuestion1Answer(textBox1Value);
		} else {
			model.setQuestion1Answer(hidden1Value);
		}
		
		model.setQuestion2(display.getSecurityQuestionsWidget().getSecurityQuestion2().getValue());
		String textBox2Value = display.getAnswerText2();
		String hidden2Value = display.getSecurityQuestionsWidget().getAnswer2Value();
		
		if(!StringUtility.isEmptyOrNull(textBox2Value) && !textBox2Value.equals(MessageDelegate.DEFAULT_SECURITY_QUESTION_VALUE)) {
			model.setQuestion2Answer(textBox2Value);
		} else {
			model.setQuestion2Answer(hidden2Value);
		}
		
		model.setQuestion3(display.getSecurityQuestionsWidget().getSecurityQuestion3().getValue());
		String textBox3Value = display.getAnswerText3();
		String hidden3Value = display.getSecurityQuestionsWidget().getAnswer3Value();
		
		if(!StringUtility.isEmptyOrNull(textBox3Value) && !textBox3Value.equals(MessageDelegate.DEFAULT_SECURITY_QUESTION_VALUE)) {
			model.setQuestion3Answer(textBox3Value);
		} else {
			model.setQuestion3Answer(hidden3Value);
		}
		
		model.scrubForMarkUp();
		return model;
	}
	
	/**
	 * Go.
	 * 
	 * @param container
	 *            the container
	 */
	public void go(HasWidgets container) {
		reset();
		loadSecurityQuestions();
		container.add(display.asWidget());
	}
	
	/**
	 * Before display.
	 */
	private void beforeDisplay() {
		MatContext.get().getLoginService().getSecurityQuestionsAnswers(MatContext.get().getLoggedinUserId(),
				new AsyncCallback<List<UserSecurityQuestion>>(){
			
			@Override
			public void onFailure(Throwable caught) {
				display.getSecurityErrorMessageDisplay().clearAlert();
				display.getSecurityErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
			}
			
			@Override
			public void onSuccess(List<UserSecurityQuestion> result) {
				if((result !=null) && (result.size()>0)){
					display.getQuestion1Text().setValue(result.get(0).getSecurityQuestions().getQuestion());
					display.getSecurityQuestionsWidget().setAnswer1Value(result.get(0).getSecurityAnswer());
					display.getQuestion1Answer().setValue(MessageDelegate.DEFAULT_SECURITY_QUESTION_VALUE);
					
					display.getQuestion2Text().setValue(result.get(1).getSecurityQuestions().getQuestion());
					display.getSecurityQuestionsWidget().setAnswer2Value(result.get(1).getSecurityAnswer());
					display.getQuestion2Answer().setValue(MessageDelegate.DEFAULT_SECURITY_QUESTION_VALUE);
					
					display.getQuestion3Text().setValue(result.get(2).getSecurityQuestions().getQuestion());
					display.getSecurityQuestionsWidget().setAnswer3Value(result.get(2).getSecurityAnswer());
					display.getQuestion3Answer().setValue(MessageDelegate.DEFAULT_SECURITY_QUESTION_VALUE);
					
				}
			}
		});
		Mat.focusSkipLists("SecurityInfo");
	}
}
