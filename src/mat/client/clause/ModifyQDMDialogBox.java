package mat.client.clause;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ModifyQDMDialogBox {
	public static DialogBox dialogBox = new DialogBox(true,true);
	
	public static void showModifyDialogBox(Widget widget){
		
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		dialogBox.setText("Modify Applied QDM");
		dialogBox.setTitle("Modify Applied QDM");
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.setWidth("93em");
		DOM.setStyleAttribute(dialogBox.getElement(), "width", "2000px");
		DOM.setStyleAttribute(dialogBox.getElement(), "height", "2000px");
	
		dialogBox.setWidget(dialogContents);
		
		dialogContents.add(widget);
		dialogContents.setCellHorizontalAlignment(widget, HasHorizontalAlignment.ALIGN_LEFT);
		dialogBox.center();
		dialogBox.show();
	}

}
