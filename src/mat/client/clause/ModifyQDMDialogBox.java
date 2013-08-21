package mat.client.clause;

import mat.model.QualityDataSetDTO;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ModifyQDMDialogBox {
	public static DialogBox dialogBox = new DialogBox(true,true);
	
	public static void showModifyDialogBox(Widget widget, QualityDataSetDTO modifyValueSetDTO){
		
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		String text = "Modify Applied QDM ( ";
		if(modifyValueSetDTO.getOccurrenceText()!=null)
			text = text.concat(" " + modifyValueSetDTO.getOccurrenceText()+ " of ");
		text = text.concat(modifyValueSetDTO.getCodeListName() + " : " + modifyValueSetDTO.getDataType() +" )");
		dialogBox.setText(text);
		dialogBox.setTitle("Modify Applied QDM");
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.setWidth("70em");
		DOM.setStyleAttribute(dialogBox.getElement(), "width", "1000px");
		DOM.setStyleAttribute(dialogBox.getElement(), "height", "1000px");
	
		dialogBox.setWidget(dialogContents);
		
		dialogContents.add(widget);
		dialogContents.setCellHorizontalAlignment(widget, HasHorizontalAlignment.ALIGN_LEFT);
		dialogBox.center();
		dialogBox.show();
	}

}
