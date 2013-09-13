package mat.client.measure.metadata;

import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.RequiredIndicator;
import mat.client.shared.SpacerWidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DeleteMeasureConfirmationBox {
	public static DialogBox dialogBox = new DialogBox(true,true);
	public static DialogBox getDialogBox() {
		return dialogBox;
	}
	public static Button save = new Button("Confirm");
	public static String passwordEntered;
	public static String getPasswordEntered() {
		return passwordEntered;
	}
	public static void setPasswordEntered(String passwordEntered) {
		DeleteMeasureConfirmationBox.passwordEntered = passwordEntered;
	}
	public static Button getSave() {
		return save;
	}
	public static void setSave(Button save) {
		DeleteMeasureConfirmationBox.save = save;
	}
	public static void showDeletionConfimationDialog(){
		
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		dialogBox.setText("Warning");
		// Create a table to layout the content
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.getElement().setId("dialogContents_VerticalPanel");
		dialogContents.setWidth("28em");
		dialogContents.setSpacing(5);
		dialogBox.setWidget(dialogContents);
		
		ErrorMessageDisplay errorMessageDisplay = new ErrorMessageDisplay();
		errorMessageDisplay.setMessage("Deleting a draft or version of a measure will permanently remove the designated measure draft or version from  the Measure Authoring Tool. Deleted measures cannot <br> be recovered.");
		VerticalPanel passwordPanel = new VerticalPanel();
		passwordPanel.getElement().setId("passwordPanel_VerticalPanel");
		final HTML passwordText = new HTML("To confirm deletion enter your password below:");
		final PasswordTextBox password = new PasswordTextBox();
		password.getElement().setId("password_PasswordTextBox");
		passwordPanel.add(passwordText);
		passwordPanel.add(new SpacerWidget());
		HorizontalPanel hp = new HorizontalPanel();
		hp.getElement().setId("hp_HorizontalPanel");
		HTML required = new HTML(RequiredIndicator.get());
		hp.add(password);
		hp.add(required);
		passwordPanel.add(hp);
		
		dialogContents.add(errorMessageDisplay);
		dialogContents.setCellHorizontalAlignment(errorMessageDisplay, HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents.add(passwordPanel);
		dialogContents.setCellHorizontalAlignment(passwordPanel, HasHorizontalAlignment.ALIGN_LEFT);
		// Add a Save button at the bottom of the dialog
		/*final Button save = new Button("Confirm", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				Window.alert(password.getText());
			}
		});*/
		save.setEnabled(false);
		// Add a Close button at the bottom of the dialog
		Button closeButton = new Button("Cancel", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
			
				dialogBox.hide();
				
			}
		});
		closeButton.getElement().setId("closeButton_Button");
		password.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if(password.getText()!=null && event.getNativeEvent().getKeyCode() != KeyCodes.KEY_TAB 
						&& event.getNativeEvent().getKeyCode() != KeyCodes.KEY_BACKSPACE){
					save.setEnabled(true);
					setPasswordEntered(password.getText());
				}else if(password.getText()==null || password.getText().trim().length()==0){
					save.setEnabled(false);
				}
				
			}
		});
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.getElement().setId("buttonPanel_HorizontalPanel");
		buttonPanel.setSpacing(10);
		save.getElement().setId("save_Button");
		buttonPanel.add(save);
		buttonPanel.setCellHorizontalAlignment(save,
				HasHorizontalAlignment.ALIGN_RIGHT);
		buttonPanel.add(closeButton);
		buttonPanel.setCellHorizontalAlignment(closeButton,
				HasHorizontalAlignment.ALIGN_RIGHT);

		dialogContents.add(buttonPanel);
		dialogBox.center();
		dialogBox.show();
		
	}
	

}
