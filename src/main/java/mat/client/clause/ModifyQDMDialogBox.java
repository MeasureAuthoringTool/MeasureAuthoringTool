package mat.client.clause;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.shared.DialogBoxWithCloseButton;
import mat.model.QualityDataSetDTO;
import org.apache.commons.lang3.StringUtils;

@Deprecated
/*
 * This class is for code that is non maintained anymore. It should not be changed. 
 */
public class ModifyQDMDialogBox {
	
	/** The dialog box. */
	private static DialogBoxWithCloseButton dialogBox = new DialogBoxWithCloseButton(StringUtils.EMPTY);
	
	/** The handler registration. */
	static HandlerRegistration handlerRegistration;
	
	/**
	 * Creates and displays Modify QDM dialog box (pop-up).
	 * @param widget - Widget.
	 * @param modifyValueSetDTO - DTO to set caption heading.
	 * @param qdmAvailableValueSetPresenter - QDMAvailableValueSetPresenter.
	 */

	public static void showModifyDialogBox(final Widget widget, final QualityDataSetDTO modifyValueSetDTO,
			final QDMAvailableValueSetPresenter qdmAvailableValueSetPresenter) {
		String text = "Modify Applied QDM ( ";
		if (modifyValueSetDTO.getOccurrenceText() != null) {
			text = text.concat(" " + modifyValueSetDTO.getOccurrenceText() + " of ");
		}
		text = text.concat(modifyValueSetDTO.getCodeListName() + " : " + modifyValueSetDTO.getDataType()  + " )");
		
		dialogBox.setText(text);
		dialogBox.getElement().setId("ModifyQDMDialogBox");
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
		
		if (handlerRegistration != null) {
			handlerRegistration.removeHandler();
		}
		
		handlerRegistration = dialogBox.addCloseHandler(new CloseHandler<PopupPanel>() {
			@Override
			public void onClose(final CloseEvent<PopupPanel> event) {
				//Reloading applied QDM list on close.
				qdmAvailableValueSetPresenter.reloadAppliedQDMList();
			}
		});
	}
}
