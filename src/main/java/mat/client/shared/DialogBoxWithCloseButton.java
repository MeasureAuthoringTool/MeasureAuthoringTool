package mat.client.shared;

import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import mat.client.ImageResources;

/**
 * Extended DialogBox widget with close button inside the pop-up header.
 * {@link DialogBox}
 */
public class DialogBoxWithCloseButton extends DialogBox {

    /**
     * HorizontalPanel that is used as DialogBox's Header.
     */
    private HorizontalPanel captionPanel = new HorizontalPanel();

    /**
     * Widget used as XOut for the DialogBox.
     */
    private Widget closeWidget = null;

    /**
     * HTML widget used in 'captionPanel' for setting the caption text.
     */
    private HTML textHTML = new HTML();

	/**
	 * Instantiates a new dialog box with close button.
	 * 
	 * @param text
	 *            the text
	 */
	public DialogBoxWithCloseButton(final String text) {
		super(false, true);
		setGlassEnabled(true);
		setAnimationEnabled(true);

		captionPanel.getElement().setId("captionPanel_HorizontalPanel");
		textHTML.getElement().setId("textHTML_HTML");
		Image closeImage = new Image();
		closeImage.getElement().setId("closeImage_Image");
		closeImage.setResource(ImageResources.INSTANCE.close());
		closeImage.getElement().setAttribute("alt", "Close");
		closeImage.setTitle("Close");
		closeWidget = closeImage;
		closeWidget.getElement().setAttribute("tabIndex", "0");

		setCaption(text);
		
		/*Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
            	closeWidget.getElement().setAttribute("tabIndex", "0"); 
            	closeWidget.getElement().focus();
            }
        });*/
	}

	/**
	 * Removes old caption and replaces it with a new caption.
	 * @param text - String.
	 */
	private void setCaption(final String text) {
		textHTML.setText(text);
		captionPanel.setWidth("100%");
		captionPanel.add(textHTML);
		captionPanel.add(closeWidget);
		captionPanel.setCellHorizontalAlignment(closeWidget, HasHorizontalAlignment.ALIGN_RIGHT);
		captionPanel.setCellWidth(closeWidget, "1%");
		captionPanel.addStyleName("Caption");
		// Get the cell element that holds the caption
		Element td = getCellElement(0, 1);
		// Remove the old caption
		td.setInnerHTML("");
		// append captionPanel
		td.appendChild(captionPanel.getElement());
	}

	/**
	 * Close handler, which will hide the dialog box .
	 */
	private class DialogBoxCloseHandler {
		
		/**
		 * Hides this DialogBox.
		 * 
		 * @param event
		 *            the event
		 */
		public void onClick(final Event event) {
			hide();
		}
	}

	/**
	 * Function checks if the browser event was inside the caption region.
	 * @param event
	 * 			browser event
	 * @return true if event inside the caption panel (DialogBox header).
	 */
	protected final boolean isHeaderCloseControlEvent(final NativeEvent event) {
		return isWidgetEvent(event, closeWidget);
	}

	/**
	 * Function checks if event was inside a given widget.
	 * @param event
	 *            - current event.
	 * @param w
	 *            - widget to prove if event was inside.
	 * @return - true if event inside the given widget.
	 */
	protected final boolean isWidgetEvent(final NativeEvent event, final Widget w) {
		EventTarget target = event.getEventTarget();
		if (Element.is(target)) {
			boolean t = w.getElement().isOrHasChild(Element.as(target));
			return t;
		}
		return false;
	}

	/**
	 * Overrides the browser event from the DialogBox.
	 * 
	 * @param event
	 *            the event
	 */
	@Override
	public final void onBrowserEvent(final Event event) {
		if (isHeaderCloseControlEvent(event)) {
			switch (event.getTypeInt()) {
			case Event.ONMOUSEUP:
			case Event.ONCLICK:
				new DialogBoxCloseHandler().onClick(event);
				break;
			case Event.ONMOUSEOVER:
				break;
			case Event.ONMOUSEOUT:
				break;
			default:
				break;
			}
			return;
		}
		// go to the DialogBox browser event
		super.onBrowserEvent(event);
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.DialogBox#getText()
	 */
	@Override
	public final String getText() {
		return textHTML.getText();
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.DialogBox#setText(java.lang.String)
	 */
	@Override
	public final void setText(final String text) {
		textHTML.setText(text);
	}
}
