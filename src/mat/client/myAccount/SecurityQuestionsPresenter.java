package mat.client.myAccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.gwtbootstrap3.client.ui.Input;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import mat.client.shared.MessageDelegate;
import mat.client.shared.NameValuePair;
import mat.client.shared.SecurityQuestionsDisplay;
import mat.client.util.ClientConstants;
import mat.model.SecurityQuestions;
import mat.shared.SecurityQuestionVerifier;
import mat.shared.StringUtility;

/**
 * The Class SecurityQuestionsPresenter.
 */
public class SecurityQuestionsPresenter implements MatPresenter {
	
	/** The login service. */
	LoginServiceAsync loginService = MatContext.get().getLoginService();
	
	/** The submit on enter handler. */
	private KeyDownHandler submitOnEnterHandler = new KeyDownHandler() {
		@Override
		public void onKeyDown(KeyDownEvent event) {
			if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
				saveSecurityQuestions(display.getPassword().getValue());
			}
		}
	};
	
	/** The display. */
	private SecurityQuestionsDisplay display;
	
	/** The current values. */
	private SecurityQuestionsModel currentValues;
	
	
	/**
	 * Instantiates a new security questions presenter.
	 * 
	 * @param displayArg
	 *            the display arg
	 */
	public SecurityQuestionsPresenter(SecurityQuestionsDisplay displayArg) {
		display = displayArg;
		String ruleHTML = "<img src='images/bullet.png'/><span style='font-size:1.5 em;'> All fields are required.</span>";
		display.getSecurityQuestionsWidget().prependRule(ruleHTML);
		loginService.getSecurityQuestions(new AsyncCallback<List<SecurityQuestions>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}
			
			@Override
			public void onSuccess(List<SecurityQuestions> result) {
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
		});
		
		
		display.getCancelButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				display.getPasswordErrorMessageDisplay().clearAlert();
				display.getSuccessMessageDisplay().clearAlert();
				if(currentValues != null) {
					setValues(currentValues);
				}
				//Reset the newly entered values
				display.getSecurityAnswers().put("answer1", MessageDelegate.EMPTY_VALUE);
				display.getSecurityAnswers().put("answer2", MessageDelegate.EMPTY_VALUE);
				display.getSecurityAnswers().put("answer3", MessageDelegate.EMPTY_VALUE);
			}
		});
		
		display.getSaveButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				display.getPasswordErrorMessageDisplay().clearAlert();
				SecurityQuestionsModel securityQuestionsModel = getSecurityQuestionsModel();
				SecurityQuestionVerifier sverifier =
						new SecurityQuestionVerifier(securityQuestionsModel.getQuestion1(),securityQuestionsModel.getQuestion1Answer(),
								securityQuestionsModel.getQuestion2(), securityQuestionsModel.getQuestion2Answer(),
								securityQuestionsModel.getQuestion3(), securityQuestionsModel.getQuestion3Answer());
				if(!sverifier.isValid()) {
					display.getSuccessMessageDisplay().clearAlert();
					display.getPasswordErrorMessageDisplay().createAlert(sverifier.getMessages());
				}
				else {
					saveSecurityQuestions(display.getPassword().getValue());
				}
				display.getPassword().setValue("");
			}
		});
		
		// if the user enters the "Enter" key on the password field, do a save.
		//display.getPasswordEditInfoWidget().getPassword().addKeyDownHandler(submitOnEnterHandler);
		Input passwordTextBox = display.getPassword();
		passwordTextBox.addKeyDownHandler(submitOnEnterHandler);
	}
	
	/**
	 * Gets the SecurityQuestionsModel.
	 * 
	 * @return the SecurityQuestionsModel
	 */
	private SecurityQuestionsModel getSecurityQuestionsModel() {
		SecurityQuestionsModel model = new SecurityQuestionsModel();
		model.setQuestion1(display.getQuestion1Text().getValue());
		model.setQuestion2(display.getQuestion2Text().getValue());
		model.setQuestion3(display.getQuestion3Text().getValue());
		
		String masked1Value = display.getSecurityAnswers().get("answer1");
		String textBox1Value= StringUtility.isEmptyOrNull(masked1Value) ? display.getAnswerText1() : masked1Value ;
		String hidden1Value = display.getSecurityQuestionsWidget().getAnswer1Value();
		if(!StringUtility.isEmptyOrNull(textBox1Value) && !textBox1Value.equals(MessageDelegate.DEFAULT_SECURITY_QUESTION_VALUE)) {
			model.setQuestion1Answer(textBox1Value);
		} else {
			model.setQuestion1Answer(hidden1Value);
		}
		
		String masked2Value = display.getSecurityAnswers().get("answer2");
		String textBox2Value = StringUtility.isEmptyOrNull(masked2Value) ? display.getAnswerText2() : masked2Value;
		String hidden2Value = display.getSecurityQuestionsWidget().getAnswer2Value();
		if(!StringUtility.isEmptyOrNull(textBox2Value) && !textBox2Value.equals(MessageDelegate.DEFAULT_SECURITY_QUESTION_VALUE)) {
			model.setQuestion2Answer(textBox2Value);
		} else {
			model.setQuestion2Answer(hidden2Value);
		}
		
		String masked3Value = display.getSecurityAnswers().get("answer3");
		String textBox3Value = StringUtility.isEmptyOrNull(masked3Value) ? display.getAnswerText3() : masked3Value ;
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
	 * Save security questions.
	 * 
	 * @param password
	 *            the password
	 */
	private void saveSecurityQuestions(String password){
		loginService.validatePassword(MatContext.get().getLoggedinLoginId(), password, new AsyncCallback<HashMap<String,String>>(){
			@Override
			public void onSuccess(HashMap<String,String> resultMap) {
				String result = resultMap.get("result");
				if(result.equals("SUCCESS")){
					currentValues = getSecurityQuestionsModel();
					MatContext.get().getMyAccountService().saveSecurityQuestions(currentValues, new AsyncCallback<SaveMyAccountResult>() {
						@Override
						public void onFailure(Throwable caught) {
							Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						}
						@Override
						public void onSuccess(SaveMyAccountResult result) {
							if(result.isSuccess()){
								display.getPasswordErrorMessageDisplay().clearAlert();
								display.getSuccessMessageDisplay().clearAlert();
								display.getSuccessMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getSecurityQuestionsUpdatedMessage());
								MatContext.get().getMyAccountService().getSecurityQuestions(new AsyncCallback<SecurityQuestionsModel>() {
									@Override
									public void onSuccess(SecurityQuestionsModel result) {
										setValues(result);
										currentValues = result;
									}
									
									@Override
									public void onFailure(Throwable caught) {
										display.getSuccessMessageDisplay().clearAlert();
										display.getPasswordErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
										MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
									}
								});
							}else{
								List<String> messages = new ArrayList<String>();
								switch(result.getFailureReason()) {
									case SaveMyAccountResult.SERVER_SIDE_VALIDATION:
										messages = result.getMessages();
										break;
									default:
										messages.add(MatContext.get().getMessageDelegate().getUnknownErrorMessage(result.getFailureReason()));
								}
								display.getSuccessMessageDisplay().clearAlert();
								display.getPasswordErrorMessageDisplay().createAlert(messages);
							}
						}
					});
				}else{
					display.getPasswordErrorMessageDisplay().clearAlert();
					display.getSuccessMessageDisplay().clearAlert();
					String displayErrorMsg= resultMap.get("message");
					if(displayErrorMsg.equals("REDIRECT")){
						MatContext.get().redirectToHtmlPage(ClientConstants.HTML_LOGIN);
					}else{
						display.getPasswordErrorMessageDisplay().createAlert(displayErrorMsg);
					}
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}
		});
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		MatContext.get().getMyAccountService().getSecurityQuestions(new AsyncCallback<SecurityQuestionsModel>() {
			@Override
			public void onSuccess(SecurityQuestionsModel result) {
				display.getPasswordErrorMessageDisplay().clearAlert();
				display.getSuccessMessageDisplay().clearAlert();
				setValues(result);
				currentValues = result;
			}
			
			@Override
			public void onFailure(Throwable caught) {
				display.getSuccessMessageDisplay().clearAlert();
				display.getPasswordErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
			}
		});
		Mat.focusSkipLists("SecurityInfo");
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#getWidget()
	 */
	@Override
	public Widget getWidget() {
		return display.asWidget();
	}
	

	
	/**
	 * Sets the values.
	 * 
	 * @param result
	 *            the new values
	 */
	private void setValues(SecurityQuestionsModel result) {
		display.getSecurityQuestionsWidget().setAnswer1Value(result.getQuestion1Answer());
		display.getSecurityQuestionsWidget().getAnswer1().setText(MessageDelegate.DEFAULT_SECURITY_QUESTION_VALUE);
		display.getQuestion1Text().setValue(result.getQuestion1());
		
		display.getSecurityQuestionsWidget().setAnswer2Value(result.getQuestion2Answer());
		display.getSecurityQuestionsWidget().getAnswer2().setText(MessageDelegate.DEFAULT_SECURITY_QUESTION_VALUE);
		display.getSecurityQuestionsWidget().getSecurityQuestion2().setValue(result.getQuestion2());
		
		display.getSecurityQuestionsWidget().setAnswer3Value(result.getQuestion3Answer());
		display.getSecurityQuestionsWidget().getAnswer3().setText(MessageDelegate.DEFAULT_SECURITY_QUESTION_VALUE);
		display.getSecurityQuestionsWidget().getSecurityQuestion3().setValue(result.getQuestion3());
		display.getPassword().setValue("");
	}
	
}
