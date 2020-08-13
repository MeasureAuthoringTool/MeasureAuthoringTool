package mat.client.shared;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import mat.client.ImageResources;

import java.util.List;

public class InProgressMessageDisplay extends Composite implements WarningMessageDisplayInterface {
	/** The h panel. */
	private HorizontalPanel hPanel;
	
	/** The warning icon. */
	private Image inProgressIcon = new Image(ImageResources.INSTANCE.alert());
	
	/** The image panel. */
	private FlowPanel imagePanel;
	
	/** The msg panel. */
	private FlowPanel msgPanel;
	public InProgressMessageDisplay() {
		hPanel = new HorizontalPanel();
		hPanel.getElement().setId("hPanel_HorizontalPanel");
		imagePanel = new FlowPanel();
		msgPanel = new FlowPanel();
		msgPanel.getElement().setId("msgPanel_FlowPanel");
		inProgressIcon.getElement().setAttribute("alt", "Alert");
		imagePanel.getElement().setId("imagePanel_FlowPanel");
		imagePanel.setTitle("inProgressAlert");
		imagePanel.add(inProgressIcon);
		initWidget(hPanel);
		
	}
	@Override
	public void clear() {
		hPanel.removeStyleName("inprogress-alert");
		msgPanel.clear();
		hPanel.remove(imagePanel);
		hPanel.remove(msgPanel);
		
		//Turn off images
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.WarningMessageDisplayInterface#setMessages(java.util.List)
	 */
	@Override
	public void setMessages(List<String> messages) {
		hPanel.addStyleName("inprogress-alert");
		msgPanel.clear();
		hPanel.add(imagePanel);
		hPanel.add(msgPanel);
		
		
		for(String message : messages) {
			msgPanel.add(wrap(message));
		}
		hPanel.add(new SpacerWidget());
		setFocus();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.WarningMessageDisplayInterface#setMessage(java.lang.String)
	 */
	@Override
	public void setMessage(String message) {
		hPanel.addStyleName("inprogress-alert");
		msgPanel.clear();
		hPanel.add(imagePanel);
		hPanel.add(msgPanel);
		msgPanel.add(wrap(message));
		msgPanel.add(new SpacerWidget());
		setFocus();
	}
	
	/**
	 * Wrap.
	 * 
	 * @param arg
	 *            the arg
	 * @return the widget
	 */
	private Widget wrap(String arg) {
		return new HTML(arg);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.WarningMessageDisplayInterface#setFocus()
	 */
	@Override
	public void setFocus(){
		try{
			hPanel.getElement().focus();
			hPanel.getElement().setAttribute("id", "inProgressMessage");
			hPanel.getElement().setAttribute("aria-role", "image");
			hPanel.getElement().setAttribute("aria-labelledby", "LiveRegion");
			hPanel.getElement().setAttribute("aria-live", "assertive");
			hPanel.getElement().setAttribute("aria-atomic", "true");
			hPanel.getElement().setAttribute("aria-relevant", "all");
			hPanel.getElement().setAttribute("role", "alert");
		}catch(JavaScriptException e){
			//This try/catch block is needed for IE7 since it is throwing exception "cannot move
			//focus to the invisible control."
			//do nothing.
		}
	}
	@Override
	public void setMessageWithButtons(String message, List<String> buttonNames) {
		// TODO Auto-generated method stub
	}
}
