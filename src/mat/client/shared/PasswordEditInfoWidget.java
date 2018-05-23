package mat.client.shared;

import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.constants.InputType;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class PasswordEditInfoWidget.
 */
public class PasswordEditInfoWidget extends Composite {
	
	/** The password. */
	//private PasswordTextBox password = new PasswordTextBox();
	private Input password = new Input(InputType.PASSWORD);
	FormGroup passwordExistingGroup = new FormGroup();
	
	/**
	 * Instantiates a new password edit info widget.
	 */
	public PasswordEditInfoWidget() {
		
		
		FormLabel label = new FormLabel();
		label.setText("Enter existing password to confirm changes");
		label.setTitle("Enter existing password to confirm changes");
		label.setId("existingPasswordLabel");
		label.setFor("exisitingPasswordInput");
		label.setShowRequiredIndicator(true);
		
		password.setWidth("200px");
		password.setTitle("Enter existing password to confirm changes");
		password.setId("exisitingPasswordInput");
		passwordExistingGroup.add(label);
		passwordExistingGroup.add(password);
		
		/*FlowPanel container = new FlowPanel();
		container.getElement().setId("container_FlowPanel_passwordEditInfo");
		password.getElement().setId("password_ExistingPasswordTextBox");
		
		container.add(LabelBuilder.buildRequiredLabel(password, "Enter existing password to confirm changes"));
		container.add(new SpacerWidget());
		container.add(wrap(password));
		container.add(new SpacerWidget());*/
	}
	
	/**
	 * Wrap.
	 * 
	 * @param widget
	 *            the widget
	 * @return the simple panel
	 */
	private SimplePanel wrap(Widget widget) {
		SimplePanel p = new SimplePanel();
		p.getElement().setId("p_SimplePanel");
		p.add(widget);
		return p;
	}
	
	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */
	public Input getPassword() {
		return password;
	}
	
	/**
	 * Sets the password.
	 * 
	 * @param password
	 *            the new password
	 */
	public void setPassword(Input password) {
		this.password = password ;
	}
	
	public FormGroup getPasswordExistingGroup() {
		return passwordExistingGroup;
	}
	
}
