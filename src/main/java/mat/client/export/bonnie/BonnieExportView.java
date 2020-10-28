package mat.client.export.bonnie;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.bonnie.BonnieUploadCancelButtonBar;
import mat.client.measure.BaseDisplay;
import mat.client.shared.MessageAlert;
import mat.client.shared.SpacerWidget;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

public class BonnieExportView implements BaseDisplay {
	
	private VerticalPanel mainPanel = new VerticalPanel();
	private VerticalPanel contentPanel = new VerticalPanel();
		
	private Button measureNameLink = new Button();
	private Label bonnieIdText = new Label();
	private HelpBlock helpBlock = new HelpBlock();

	private VerticalPanel alertPanel = new VerticalPanel(); 
	
	private BonnieUploadCancelButtonBar bonnieUploadCancelButtonBar;
	private Button bonnieSignOutButton;
	
	private static final String DISCONNECT_FROM_BONNIE ="Disconnect from Bonnie";

	public BonnieExportView() {
		mainPanel.add(new SpacerWidget());
		createHelpBlock();
		createMeasureInformation();
		createBonnieInformation();
		createSignoutOfBonnieButton();
		createAlertWidget();
		mainPanel.add(contentPanel);
		createBonnieButtonToolBar();
	}
	
	private void createAlertWidget() {
		alertPanel.getElement().setTabIndex(0);
		contentPanel.add(alertPanel);
	}
	
	private void createMeasureInformation() {
		measureNameLink.getElement().setId("measureNameLabel");
		measureNameLink.setType(ButtonType.LINK);
		measureNameLink.setStyleName("btn-wrap-text", true);
		
		FormGroup group = new FormGroup();
		FormLabel measureNameLabel = new FormLabel();
		measureNameLabel.setText("Measure: ");
		measureNameLabel.setTitle("Measure: ");
		measureNameLabel.setFor("measureNameLabel");
		
		HorizontalPanel panel = new HorizontalPanel();
		group.add(measureNameLabel);
		group.add(measureNameLink);
		panel.add(group);
		
		contentPanel.add(panel);
	}
	
	private void createBonnieInformation() {
		bonnieIdText.getElement().setId("bonnieIdText");
				
		FormLabel bonnieIdLabel = new FormLabel();
		bonnieIdLabel.setText("Bonnie ID:");
		bonnieIdLabel.setTitle("Bonnie ID");
		bonnieIdLabel.setPaddingRight(5.0);
		
		HorizontalPanel bonniePanel = new HorizontalPanel();
		
		bonniePanel.add(bonnieIdLabel);		
		bonniePanel.add(bonnieIdText);

		contentPanel.add(bonniePanel);
	}
	
	private void createSignoutOfBonnieButton() {
		bonnieSignOutButton = new Button(DISCONNECT_FROM_BONNIE);
		bonnieSignOutButton.setTitle(DISCONNECT_FROM_BONNIE);
		bonnieSignOutButton.setType(ButtonType.LINK);
		bonnieSignOutButton.setIcon(IconType.SIGN_OUT);
		bonnieSignOutButton.setPaddingLeft(0.0);
		
		HorizontalPanel panel = new HorizontalPanel();
		panel.add(bonnieSignOutButton);

		contentPanel.add(panel);
	}

	private void createBonnieButtonToolBar() {
		bonnieUploadCancelButtonBar = new BonnieUploadCancelButtonBar("bonnie_upload");
		mainPanel.add(bonnieUploadCancelButtonBar);
	}
	
	/*Hidden panel with info about the bonnie sign in status for 508 compliance*/
	private void createHelpBlock() {
		HorizontalPanel hp = new HorizontalPanel();
		helpBlock.setText("initial text");
		helpBlock.setColor("transparent");
		helpBlock.setVisible(false);
		helpBlock.setHeight("0px");
		hp.getElement().setAttribute("role", "alert");
		hp.add(helpBlock);
		contentPanel.add(hp);
	}
	
	public void setHelpBlockMessage(String message) {
		helpBlock.setText(message);
	}
	
	public Button getUploadButton() {
		return this.bonnieUploadCancelButtonBar.getUploadButton();
	}
	
	public Button getCancelButton() {
		return this.bonnieUploadCancelButtonBar.getCancelButton();
	}
	

	@Override
	public Widget asWidget() {
		return mainPanel;
	}

	@Override
	public MessageAlert getErrorMessageDisplay() {
		return null;
	}

	public Button getMeasureNameLink() {
		return measureNameLink;
	}

	public void setMeasureNameTextBox(Button measureNameLink) {
		this.measureNameLink = measureNameLink;
	}
	
	public Panel getAlertPanel() {
		return alertPanel;
	}
	
	public Label getBonnieIdLabel() {
		return bonnieIdText;
	}
	
	
	public Button getBonnieSignOutButton() {
		return bonnieSignOutButton;
	}

	public void setBonnieSignOutButton(Button bonnieSignOutButton) {
		this.bonnieSignOutButton = bonnieSignOutButton;
	}
}
