package mat.client.shared;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class PasswordEditInfoWidget.
 */
public class PasswordEditInfoWidget extends Composite {
	
	/** The password. */
	private PasswordTextBox password = new PasswordTextBox();
	
	/**
	 * Instantiates a new password edit info widget.
	 */
	public PasswordEditInfoWidget() {
		FlowPanel container = new FlowPanel();
		container.getElement().setId("container_FlowPanel");
		password.getElement().setId("password_PasswordTextBox");
		container.add(LabelBuilder.buildRequiredLabel(password, "Enter existing password to confirm changes"));
		container.add(new SpacerWidget());
		container.add(wrap(password));
		container.add(new SpacerWidget());
		
		password.setWidth("100px");
		initWidget(container);
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
	public PasswordTextBox getPassword() {
		return password;
	}
	
	/**
	 * Sets the password.
	 * 
	 * @param password
	 *            the new password
	 */
	public void setPassword(PasswordTextBox password) {
		this.password = password ;
	}
	
}
