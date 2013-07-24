package mat.client.clause;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ModifyQDMDialogBox {
	public static DialogBox dialogBox = new DialogBox(true,true);
	
	public static void showModifyDialogBox(Widget widget){
		
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		dialogBox.setText("Edit");
		dialogBox.setTitle("Edit");
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.setWidth("93em");
		DOM.setStyleAttribute(dialogBox.getElement(), "width", "2000px");
		DOM.setStyleAttribute(dialogBox.getElement(), "height", "2000px");
		//dialogBox.getElement().removeAttribute("style");
		dialogBox.setWidget(dialogContents);
		
		dialogContents.add(widget);
		dialogContents.setCellHorizontalAlignment(widget, HasHorizontalAlignment.ALIGN_LEFT);
		// Add a Save button at the bottom of the dialog
		Button save = new Button("OK", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();

			}
		});
		// Add a Close button at the bottom of the dialog
		Button closeButton = new Button("Cancel", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(10);
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
