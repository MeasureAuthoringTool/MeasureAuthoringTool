package mat.client.clause.cqlworkspace;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import mat.client.shared.ErrorMessageDisplay;

public class DeleteConfirmationDialogBox {

	private  Button yesButton = new Button("Yes"); 
	private Button noButton = new Button("Cancel");
	PopupPanel panel = new PopupPanel();

	
	public DeleteConfirmationDialogBox() {
		yesButton.getElement().setId("yes_Button");
		noButton.getElement().setId("no_Button");
	}
	
	public void show(String message) {
		panel.setAutoHideEnabled(false);
		panel.setAnimationEnabled(true);
		panel.setGlassEnabled(true);
		panel.getElement().getStyle().setZIndex(1000);
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.getElement().setId("dialogContents_VerticalPanel");
		dialogContents.setWidth("28em");
		panel.setWidget(dialogContents);
		
		ErrorMessageDisplay errorMessageDisplay = new ErrorMessageDisplay();
		errorMessageDisplay.setMessage(message);
		dialogContents.add(errorMessageDisplay);
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		buttonPanel.getElement().setId("buttonPanel_HorizontalPanel");
		buttonPanel.setSpacing(10);
		
		yesButton.getElement().getStyle().setMargin(5.0, Style.Unit.PX);
		noButton.getElement().getStyle().setMargin(5.0, Style.Unit.PX);
		
		buttonPanel.add(yesButton);
		buttonPanel.add(noButton);
		
		
		buttonPanel.setCellHorizontalAlignment(yesButton, HasHorizontalAlignment.ALIGN_RIGHT);
		buttonPanel.setCellHorizontalAlignment(noButton, HasHorizontalAlignment.ALIGN_RIGHT);
		
		dialogContents.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		dialogContents.add(buttonPanel);
		
		panel.getElement().focus();
		panel.center();	
	}
	
	public void hide() {
		panel.hide();
	}
	
	
	public Button getYesButton() {
		return yesButton; 
	}
	
	public Button getNoButton() {
		return noButton; 
	}

}
