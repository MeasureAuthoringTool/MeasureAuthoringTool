package mat.client.myAccount;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.login.service.LoginServiceAsync;
import mat.client.myAccount.service.SaveMyAccountResult;
import mat.client.shared.MatContext;
import mat.client.shared.MessageAlert;
import mat.client.util.ClientConstants;
import mat.shared.MyAccountModelValidator;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.TextBox;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class PersonalInformationPresenter.
 */
public class PersonalInformationPresenter implements MatPresenter {
	
	/** The login service. */
	LoginServiceAsync loginService = MatContext.get().getLoginService();
	
	
	/**
	 * The Interface Display.
	 */
	public static interface Display {
		
		/**
		 * As widget.
		 * 
		 * @return the widget
		 */
		Widget asWidget();
		
		/**
		 * Gets the first name.
		 * 
		 * @return the first name
		 */
		HasValue<String> getFirstName();
		
		/**
		 * Gets the middle initial.
		 * 
		 * @return the middle initial
		 */
		HasValue<String> getMiddleInitial();
		
		/**
		 * Gets the last name.
		 * 
		 * @return the last name
		 */
		HasValue<String> getLastName();
		
		/**
		 * Gets the title.
		 * 
		 * @return the title
		 */
		HasValue<String> getTitle();
		
		/**
		 * Gets the email address.
		 * 
		 * @return the email address
		 */
		HasValue<String> getEmailAddress();
		
		/**
		 * Gets the login id.
		 * 
		 * @return the login id
		 */
		Label getLoginId();
		
		/**
		 * Gets the phone number.
		 * 
		 * @return the phone number
		 */
		HasValue<String> getPhoneNumber();
		
		/**
		 * Gets the organisation.
		 * 
		 * @return the organisation
		 */
		TextBox getOrganisation();
		
		/**
		 * Gets the oid.
		 * 
		 * @return the oid
		 */
		TextBox getOID();
		
		/**
		 * Gets the save button.
		 * 
		 * @return the save button
		 */
		HasClickHandlers getSaveButton();
		
		/**
		 * Gets the cancel button.
		 * 
		 * @return the cancel button
		 */
		HasClickHandlers getCancelButton();
		
		/**
		 * Gets the error message display.
		 * 
		 * @return the error message display
		 */
		MessageAlert getErrorMessageDisplay();
		
		/**
		 * Gets the success message display.
		 * 
		 * @return the success message display
		 */
		MessageAlert getSuccessMessageDisplay();

		Input getPasswordInput();

		boolean isFreeTextEditorEnabled();

		CheckBox getFreeTextEditorCheckBox();
		
		Panel getUserPreferencePanel();
	}
	
	/** The submit on enter handler. */
	private KeyDownHandler submitOnEnterHandler = event -> {
        if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
            savePersonalInformation();
        }
    };
	
	
	/** The display. */
	private Display display;
	
	/** The current model. */
	private MyAccountModel currentModel;
	
	/**
	 * Instantiates a new personal information presenter.
	 * 
	 * @param displayArg
	 *            the display arg
	 */
	public PersonalInformationPresenter(Display displayArg) {
		display = displayArg;
		
		display.getCancelButton().addClickHandler(event -> beforeDisplay());
		display.getSaveButton().addClickHandler(event -> savePersonalInformation());
		
		// if the user enters the "Enter" key on the password field, do a save.
		display.getPasswordInput().addKeyDownHandler(submitOnEnterHandler);
		
		
	}
	
	/**
	 * Save personal information.
	 */
	private void savePersonalInformation() {
		display.getErrorMessageDisplay().clearAlert();
		display.getSuccessMessageDisplay().clearAlert();
        Mat.showLoadingMessage();
        currentModel = getValues();
        if(isValid(currentModel)) {
        	Mat.showLoadingMessage();
            MatContext.get().getMyAccountService().saveMyAccount(currentModel, new AsyncCallback<SaveMyAccountResult>() {
                @Override
                public void onFailure(Throwable caught) {
                	Mat.hideLoadingMessage();
                    Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
                    Mat.hideLoadingMessage();
                }

                @Override
                public void onSuccess(SaveMyAccountResult result) {
					Mat.hideLoadingMessage();
                    if (result.isSuccess()) {
                        display.getErrorMessageDisplay().clearAlert();
                        display.getSuccessMessageDisplay().createAlert(MatContext.get()
                                .getMessageDelegate().getPersonalInfoUpdatedMessage());
                        display.getFirstName().setValue(currentModel.getFirstName());
                        display.getMiddleInitial().setValue(currentModel
                                .getMiddleInitial());
                        display.getLastName().setValue(currentModel.getLastName());
                        display.getTitle().setValue(currentModel.getTitle());
                        display.getEmailAddress().setValue(currentModel.getEmailAddress());
                        display.getPhoneNumber().setValue(currentModel.getPhoneNumber());
                        display.getOrganisation().setValue(currentModel.getOrganization());
                        display.getOID().setValue(currentModel.getOid());
                        display.getFreeTextEditorCheckBox().setValue(currentModel.isEnableFreeTextEditor());

                        MatContext.get().getCurrentUserInfo().userFirstName = currentModel.getFirstName();
						MatContext.get().getCurrentUserInfo().userLastName = currentModel.getLastName();
						MatContext.get().getCurrentUserInfo().organizationName = currentModel.getOrganization();

                        Mat.setSignedInAsNameOrg();
                        MatContext.get().getLoggedInUserPreference().setFreeTextEditorEnabled(currentModel.isEnableFreeTextEditor());
                    } else {
                        List<String> messages = new ArrayList<>();
                        switch(result.getFailureReason()) {
                            case SaveMyAccountResult.ID_NOT_UNIQUE:
                                messages.add(MatContext.get().getMessageDelegate()
                                        .getEmailAlreadyExistsMessage());
                                break;
                            case SaveMyAccountResult.SERVER_SIDE_VALIDATION:
                                messages = result.getMessages();
                                break;
                            default:
                                messages.add(MatContext.get().getMessageDelegate().
                                        getUnknownErrorMessage(result.getFailureReason()));
                        }
                        display.getErrorMessageDisplay().createAlert(messages);
                    }
                    Mat.hideLoadingMessage();
                }
            });
        }
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		
		display.getErrorMessageDisplay().clearAlert();
		display.getSuccessMessageDisplay().clearAlert();
		MatContext.get().getMyAccountService().getMyAccount(new AsyncCallback<MyAccountModel>() {
			
			@Override
			public void onSuccess(MyAccountModel result) {
				currentModel = result;
				setValues(currentModel);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				display.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
			}
		});
		Mat.focusSkipLists("PersonalInfo");
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
	}
	
	/**
	 * Sets the values.
	 * 
	 * @param model
	 *            the new values
	 */
	private void setValues(MyAccountModel model) {
		display.getFirstName().setValue(model.getFirstName());
		display.getMiddleInitial().setValue(model.getMiddleInitial());
		display.getLastName().setValue(model.getLastName());
		display.getTitle().setValue(model.getTitle());
		display.getEmailAddress().setValue(model.getEmailAddress());
		display.getLoginId().setText(model.getLoginId());
		display.getPhoneNumber().setValue(model.getPhoneNumber());
		display.getOrganisation().setValue(model.getOrganization());
		display.getOrganisation().setTitle(model.getOrganization());
		display.getOrganisation().setWidth(getRequiredWidth(display.getOrganisation().getValue().length()));
		display.getOID().setValue(model.getOid());
		display.getOID().setTitle(model.getOid());
		display.getOID().setWidth(getRequiredWidth(display.getOID().getValue().length()));
		display.getFreeTextEditorCheckBox().setValue(model.isEnableFreeTextEditor());
		if(ClientConstants.ADMINISTRATOR.equalsIgnoreCase(MatContext.get()
				.getLoggedInUserRole())){
			display.getUserPreferencePanel().setVisible(false);
		}
	}
	
	private String getRequiredWidth(int length) {
		length = (int) (length * 6.5);
		if (length < 200) {
			length = 200;
		}
		return length+"px";
	}
	
	/**
	 * Gets the values.
	 * 
	 * @return the values
	 */
	private MyAccountModel getValues() {
		MyAccountModel model = new MyAccountModel();
		model.setFirstName(display.getFirstName().getValue());
		model.setMiddleInitial(display.getMiddleInitial().getValue());
		model.setLastName(display.getLastName().getValue());
		model.setTitle(display.getTitle().getValue());
		model.setEmailAddress(display.getEmailAddress().getValue());
		model.setPhoneNumber(display.getPhoneNumber().getValue());
		model.setOrganization(display.getOrganisation().getValue());
		model.setOid(display.getOID().getValue());
		model.setEnableFreeTextEditor(display.isFreeTextEditorEnabled());
		model.scrubForMarkUp();
		return model;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#getWidget()
	 */
	@Override
	public Widget getWidget() {
		return display.asWidget();
	}
	
	/**
	 * Checks if is valid.
	 * 
	 * @param model
	 *            the model
	 * @return true, if is valid
	 */
	private boolean isValid(MyAccountModel model) {
		
		MyAccountModelValidator test = new MyAccountModelValidator();
		List<String>  message= test.validate(model);
		
		boolean valid = message.size() == 0;
		if(!valid) {
			display.getErrorMessageDisplay().createAlert(message);
		}
		else {
			display.getErrorMessageDisplay().clearAlert();
		}
		return valid;
	}
}
