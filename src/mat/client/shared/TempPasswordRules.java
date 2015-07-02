package mat.client.shared;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;

// TODO: Auto-generated Javadoc
/**
 * The Class PasswordRules.
 */
public class TempPasswordRules extends Composite {

	/**
	 * Instantiates a new password rules.
	 */
	public TempPasswordRules() {
		FlowPanel fPanel = new FlowPanel();
		fPanel.getElement().setId("fPanel_FlowPanel");
		Label rules = new Label("Password Rules");
		rules.addStyleName("bold");
		HTML desc = new HTML("Passwords must not consist of a single dictionary word with letters, numbers and symbols. <br><br>" +
		                     " Previous 6 passwords cannot be reused. <br><br>"+ " A Password needs to be at least one day old before you can change it again. <br><br>"+
				            " Passwords must be between 8 and 16 characters and contain at least one of each of  the following:" 
							);
		desc.addStyleName("passwordRulesDescription");
		HTML b1 = new HTML("<img src='images/bullet.png'/> Upper case character");
		HTML b2 = new HTML("<img src='images/bullet.png'/> Lower case character");
		HTML b3 = new HTML("<img src='images/bullet.png'/> One of the following special characters % &nbsp; # &nbsp; * &nbsp; + &nbsp; , &nbsp; ; &nbsp; = &nbsp; ? &nbsp; _");
		HTML b4 = new HTML("<img src='images/bullet.png'/> Numeric character");
		
		fPanel.add(rules);
		fPanel.add(desc);
		fPanel.add(b1);
		fPanel.add(b2);
		fPanel.add(b3);
		fPanel.add(b4);
		initWidget(fPanel);
	}
}
