package mat.client.shared;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;


/**
 * The Class PasswordRules.
 */
public class PasswordRules extends Composite {

	/**
	 * Instantiates a new password rules.
	 */
	public PasswordRules() {
		FlowPanel fPanel = new FlowPanel();
		fPanel.getElement().setId("fPanel_FlowPanel");
		Label rules = new Label("Password Rules");
		rules.getElement().setId("passwordRulesLabel");
		rules.addStyleName("bold");
		HTML desc = new HTML(
				" Previous 6 passwords cannot be reused. <br><br>"
						+ " A Password needs to be at least one day old before you can change it again. <br><br>"
						+ " Passwords must be at least 15 characters long and must contain at least one of the following:");
		desc.addStyleName("passwordRulesDescription");
		desc.getElement().setId("descLabel");
		HTML b1 = new HTML(
				"<img src='images/bullet.png'/> Upper case character");
		b1.getElement().setId("b1Label");
		HTML b2 = new HTML(
				"<img src='images/bullet.png'/> Lower case character");
		b2.getElement().setId("b2Label");
		HTML b3 = new HTML(
				"<img src='images/bullet.png'/> One of the following special characters % &nbsp; # &nbsp; * &nbsp; + &nbsp; - &nbsp; , &nbsp; : &nbsp; = &nbsp; ? &nbsp; _");
		b3.getElement().setId("b3Label");
		HTML b4 = new HTML("<img src='images/bullet.png'/> Numeric character");
		b4.getElement().setId("b4Label");

		fPanel.add(rules);
		fPanel.add(desc);
		fPanel.add(b1);
		fPanel.add(b2);
		fPanel.add(b3);
		fPanel.add(b4);
		initWidget(fPanel);
	}

	public static TextArea getInvisibleTextAreaFor508() {

		TextArea textArea508 = new TextArea();
		textArea508.setVisible(false);

		textArea508.setText("Password rules blah blah");
		return textArea508;
	}
}
