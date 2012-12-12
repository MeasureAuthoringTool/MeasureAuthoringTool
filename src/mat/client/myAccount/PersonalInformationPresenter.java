package mat.client.myAccount;

import java.util.List;

import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.shared.MyAccountModelValidator;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

public class PersonalInformationPresenter implements MatPresenter {
	public static interface Display {
		Widget asWidget();
		HasValue<String> getFirstName();
		HasValue<String> getMiddleInitial();
		HasValue<String> getLastName();
		HasValue<String> getTitle();
		HasValue<String> getEmailAddress();
		HasValue<String> getLoginId();
		HasValue<String> getPhoneNumber();
		HasValue<String> getOrganisation();
		HasValue<String> getOID();
		HasValue<String> getRootOID();
		HasClickHandlers getSaveButton();
		HasClickHandlers getCancelButton();
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		public SuccessMessageDisplayInterface getSuccessMessageDisplay();
		
	}
	
	private Display display;
	private MyAccountModel currentModel;
	
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
				currentModel = getValues();
				if(isValid(currentModel)) {
					MatContext.get().getMyAccountService().saveMyAccount(currentModel, new AsyncCallback<Void>() {
	
						public void onFailure(Throwable caught) {
							Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						}
	
						public void onSuccess(Void result) {
							display.getErrorMessageDisplay().clear();
							display.getSuccessMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getPersonalInfoUpdatedMessage());
						}
					});
				}
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
		display.getLoginId().setValue(model.getLoginId());
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
