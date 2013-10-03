package mat.client.clause;

import mat.model.QualityDataSetDTO;

import org.apache.commons.lang.StringUtils;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ModifyQDMDialogBox {
	private static DialogBoxWithCloseButton dialogBox = new DialogBoxWithCloseButton(StringUtils.EMPTY);
	static HandlerRegistration handlerRegistration;

	/**
	 * Gets dialog box with close button.
	 * @return Dialog Box with close button.
	 */
	public static DialogBoxWithCloseButton getDialogBox() {
		return dialogBox;
	}

	/**
	 * Creates and displays Modify QDM dialog box (pop-up). 
	 * @param widget
	 * @param modifyValueSetDTO - DTO to set caption heading.
	 * @param qdmAvailableValueSetPresenter
	 */
	public static void showModifyDialogBox(Widget widget, QualityDataSetDTO modifyValueSetDTO, 
			final QDMAvailableValueSetPresenter qdmAvailableValueSetPresenter){
		String text = "Modify Applied QDM ( ";
		if(modifyValueSetDTO.getOccurrenceText()!=null)
			text = text.concat(" " + modifyValueSetDTO.getOccurrenceText()+ " of ");
		text = text.concat(modifyValueSetDTO.getCodeListName() + " : " + modifyValueSetDTO.getDataType() +" )");

		dialogBox.setText(text);
		dialogBox.getElement().setId("dialogBox_DialogBox");
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

		if(handlerRegistration != null)
			handlerRegistration.removeHandler();
		
		handlerRegistration = dialogBox.addCloseHandler(new CloseHandler<PopupPanel>() {
			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				//Reloading applied QDM list on close.
				qdmAvailableValueSetPresenter.reloadAppliedQDMList();
			}
		});
	}
}
