package mat.client.myAccount;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import mat.client.Mat;
import mat.client.MatPresenter;

import mat.client.login.service.LoginServiceAsync;
import mat.client.myAccount.service.SaveMyAccountResult;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.client.shared.PasswordEditInfoWidget;
import mat.client.shared.SecurityQuestionWithMaskedAnswerWidget;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.util.ClientConstants;
import mat.shared.MyAccountModelValidator;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class PersonalInformationPresenter implements MatPresenter {
	LoginServiceAsync loginService = (LoginServiceAsync) MatContext.get().getLoginService();
	
	
	public static interface Display {
		Widget asWidget();
		HasValue<String> getFirstName();
		HasValue<String> getMiddleInitial();
		HasValue<String> getLastName();
		HasValue<String> getTitle();
		HasValue<String> getEmailAddress();
		Label getLoginId();
		HasValue<String> getPhoneNumber();
		HasValue<String> getOrganisation();
		HasValue<String> getOID();
		HasValue<String> getRootOID();
		HasValue<String> getPassword();
		HasClickHandlers getSaveButton();
		HasClickHandlers getCancelButton();
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		public SuccessMessageDisplayInterface getSuccessMessageDisplay();
		PasswordEditInfoWidget getPasswordEditInfoWidget();
	}
	
	private Display display;
	private MyAccountModel currentModel;
	


	/*private HibernateUserDetailService  getHibernateUserService(){
		return (HibernateUserDetailService)context.getBean("hibernateUserDetailService");
	}
	
	private UserService  getUserService(){
		return (UserService)context.getBean("userService");
	}
		*/
	
	public PersonalInformationPresenter(Display displayArg) {
		this.display = displayArg;
		
		display.getCancelButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				beforeDisplay();
				//if(currentModel != null) {
				//	setValues(currentModel);
				//}
			}
		});
		display.getSaveButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				savePersonalInformation(display.getPassword().getValue());
				display.getPassword().setValue("");
			}
		});
	
	}
	
	private void savePersonalInformation(String password){
		
		    loginService.validatePassword(MatContext.get().getLoggedinLoginId(), password, new AsyncCallback<HashMap<String,String>>(){ 
			@Override
			public void onSuccess(HashMap<String,String> resultMap) {
				String result = (String)resultMap.get("result");
		    	if(result.equals("SUCCESS")){
		    		currentModel = getValues();
			    	if(isValid(currentModel)) {	
		    			MatContext.get().getMyAccountService().saveMyAccount(currentModel, new AsyncCallback<SaveMyAccountResult>() {
							public void onFailure(Throwable caught) {
								Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
							}
		
							@Override
							public void onSuccess(SaveMyAccountResult result) {
								if(result.isSuccess()){
									display.getErrorMessageDisplay().clear();
									display.getSuccessMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getPersonalInfoUpdatedMessage());
								}else{
									List<String> messages = new ArrayList<String>();
									switch(result.getFailureReason()) {
										case SaveMyAccountResult.SERVER_SIDE_VALIDATION:
											messages = result.getMessages();
											break;
										default:
											messages.add(MatContext.get().getMessageDelegate().getUnknownErrorMessage(result.getFailureReason()));
									}
									display.getErrorMessageDisplay().setMessages(messages);
								}
							}
						});
			    	}	
				}else{
					display.getErrorMessageDisplay().clear();
					display.getSuccessMessageDisplay().clear();
					String displayErrorMsg= (String)resultMap.get("message");
					if(displayErrorMsg.equals("REDIRECT")){
						MatContext.get().redirectToHtmlPage(ClientConstants.HTML_LOGIN);
					}else{
						display.getErrorMessageDisplay().setMessage(displayErrorMsg);
					}
					
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				
			}
		});
		
	}
	
	
	@Override
	public void beforeDisplay() {
		
		display.getErrorMessageDisplay().clear();
		display.getSuccessMessageDisplay().clear();
		MatContext.get().getMyAccountService().getMyAccount(new AsyncCallback<MyAccountModel>() {
			
			public void onSuccess(MyAccountModel result) {
				currentModel = result;
				setValues(currentModel);
			}
			
			public void onFailure(Throwable caught) {
				display.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
			}
		});	
		Mat.focusSkipLists("PersonalInfo");
	}
	@Override 
	public void beforeClosingDisplay() {
	}

	private void setValues(MyAccountModel model) {
		display.getFirstName().setValue(model.getFirstName());
		display.getMiddleInitial().setValue(model.getMiddleInitial());
		display.getLastName().setValue(model.getLastName());
		display.getTitle().setValue(model.getTitle());
		display.getEmailAddress().setValue(model.getEmailAddress());
		display.getLoginId().setText(model.getLoginId());
		display.getPhoneNumber().setValue(model.getPhoneNumber());
		display.getOrganisation().setValue(model.getOrganisation());
		display.getOID().setValue(model.getOid());
		display.getRootOID().setValue(model.getRootoid());
	}
	
	private MyAccountModel getValues() {
		MyAccountModel model = new MyAccountModel();
		model.setFirstName(display.getFirstName().getValue());
		model.setMiddleInitial(display.getMiddleInitial().getValue());
		model.setLastName(display.getLastName().getValue());
		model.setTitle(display.getTitle().getValue());
		model.setEmailAddress(display.getEmailAddress().getValue());
		model.setPhoneNumber(display.getPhoneNumber().getValue());
		model.setOrganisation(display.getOrganisation().getValue());
		model.setOid(display.getOID().getValue());
		model.setRootoid(display.getRootOID().getValue());
		return model;
	}
	
	public Widget getWidget() {
		return display.asWidget();
	}
	
	private boolean isValid(MyAccountModel model) {
		
		MyAccountModelValidator test = new MyAccountModelValidator();
		List<String>  message= test.validate(model);
		
		boolean valid = message.size() == 0;
		if(!valid) {
			display.getErrorMessageDisplay().setMessages(message);
		}
		else {
			display.getErrorMessageDisplay().clear();
		}
		return valid;
	}
}
