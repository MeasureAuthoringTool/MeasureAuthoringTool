package mat.client.bonnie;

import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.ModalSize;
import org.gwtbootstrap3.client.ui.constants.ButtonDismiss;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;

import mat.client.buttons.NoButton;
import mat.client.buttons.YesButton;

public class BonnieModal {
	private final String MESSAGE = "You are leaving the Measure Authoring Tool (MAT) and entering another website. "
			+ "The MAT can not attest to the accuracy of the information provided by linked sites. While on another"
			+ " website, you will be subject to that site's Privacy Policy. After a successful "
			+ "login you will be automatically redirected back to your open MAT session. Do you wish to continue?" ;
	
	private YesButton yesButton = new YesButton("BonnieModal");
	private NoButton noButton = new NoButton("BonnieModal");
	private Modal panel = new Modal();
	private ModalBody modalBody = new ModalBody(); 
	private Label messageLabel = new Label();

	public BonnieModal(){
		String bonnieLink = buildBonnieLink();
		panel.setClosable(false);
		panel.setFade(true);
		panel.getElement().setTabIndex(0);
		panel.setDataBackdrop(ModalBackdrop.STATIC);
		panel.setSize(ModalSize.MEDIUM);
		panel.getElement().getStyle().setZIndex(1000);
		panel.setRemoveOnHide(true);
		modalBody.getElement().setTabIndex(0);
		
		modalBody.add(messageLabel);
		messageLabel.setText(MESSAGE);
		messageLabel.getElement().setTabIndex(0);
		
		ModalFooter modalFooter = new ModalFooter(); 
		ButtonToolBar buttonToolBar = new ButtonToolBar(); 
		noButton.setDataDismiss(ButtonDismiss.MODAL);
		noButton.setTabIndex(0);
		yesButton.setTabIndex(0);
		yesButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				panel.setVisible(false);
				Window.open(bonnieLink, "_self", "");
			}
		});
		
		buttonToolBar.add(yesButton);
		buttonToolBar.add(noButton);
		modalFooter.add(buttonToolBar);
		
		panel.add(modalBody);
		panel.add(modalFooter);
		panel.getElement().setAttribute("role", "dialog");
	}
	
	private String buildBonnieLink() {
		BonnieLink bonnieLinkInfo = new BonnieLink();
		String responseType = bonnieLinkInfo.getResponseType();
		String clientId = bonnieLinkInfo.getClientId();
		String redirectURI = bonnieLinkInfo.getRedirectURI();
		
		String link = "https://bonnie-prior.ahrqstg.org/oauth/authorize?response_type="
					+ responseType + "&client_id=" + clientId + "&redirect_uri=" + redirectURI;
		
		return link;
	}
	
	public void show() {
		panel.show();
		panel.getElement().focus();		
	}
		
}
