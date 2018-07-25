package mat.client.export.bonnie;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.bonnie.BonnieUploadCancelButtonBar;
import mat.client.measure.BaseDisplay;
import mat.client.shared.MessageAlert;
import mat.client.shared.SpacerWidget;

public class BonnieExportView implements BaseDisplay {
	
	private VerticalPanel mainPanel = new VerticalPanel();
	private VerticalPanel contentPanel = new VerticalPanel();
		
	private Button measureNameLink = new Button();
	private Label bonnieIdText = new Label();

	private VerticalPanel alertPanel = new VerticalPanel(); 
	
	private BonnieUploadCancelButtonBar bonnieUploadCancelButtonBar;
	private Button bonnieSignOutButton;

	public BonnieExportView() {
		mainPanel.add(new SpacerWidget());
		createMeasureInformation();
		createBonnieInformation();
		createSignoutOfBonnieButton();
		createAlertWidget();
		mainPanel.add(contentPanel);
		createBonnieButtonToolBar();
	}
	
	private void createAlertWidget() {
		contentPanel.add(alertPanel);
	}
	
	private void createMeasureInformation() {
		measureNameLink.getElement().setId("measureNameLabel");
		measureNameLink.setType(ButtonType.LINK);
		measureNameLink.setTitle("Link to Measure Details for Measure");
		
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
		bonnieSignOutButton = new Button("Sign out of Bonnie");
		bonnieSignOutButton.setTitle("Sign out of Bonnie");
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
