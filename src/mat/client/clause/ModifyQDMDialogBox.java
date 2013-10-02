package mat.client.clause;

import mat.model.QualityDataSetDTO;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ModifyQDMDialogBox {
	/**
	 * Instance of DialogBox.
	 * **/
	private static DialogBox dialogBox = new DialogBox(true, true);

	/**
	 * Access method for dialog box.
	 * @return dialogBox.
	 * **/

	public static DialogBox getDialogBox() {
		return dialogBox;
	}

	/**
	 * Method to create Modify QDM dialog box.
	 * @param widget - Widget.
	 * @param modifyValueSetDTO - DTO to set caption heading.
	 * **/
	public static void showModifyDialogBox(final Widget widget, final QualityDataSetDTO modifyValueSetDTO) {

		dialogBox.getElement().setId("dialogBox_DialogBox");
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		String text = "Modify Applied QDM ( ";
		if (modifyValueSetDTO.getOccurrenceText() != null) {
			text = text.concat(" " + modifyValueSetDTO.getOccurrenceText() + " of ");
		}
		text = text.concat(modifyValueSetDTO.getCodeListName() + " : " + modifyValueSetDTO.getDataType()  + " )");
		dialogBox.setText(text);
		dialogBox.setTitle("Modify Applied QDM");
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.getElement().setId("dialogContents_VerticalPanel");
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
