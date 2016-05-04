package mat.client.shared;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;

// TODO: Auto-generated Javadoc
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
		rules.addStyleName("bold");
		
		FocusPanel rulesLabelFocusPanel = new FocusPanel(rules);
		rulesLabelFocusPanel.setTitle("Password Rules");
		
		
		HTML desc = new HTML("Passwords must not consist of a single dictionary word with letters, numbers and symbols. <br><br>" +
		                     " Previous 6 passwords cannot be reused. <br><br>"+ " A Password needs to be at least one day old before you can change it again. <br><br>"+
				            " Passwords must be between 8 and 16 characters and contain at least one of each of  the following:" 
							);
		desc.addStyleName("passwordRulesDescription");
		
		FocusPanel descFocusPanel = new FocusPanel(desc);
		descFocusPanel.setTitle("Passwords must not consist of a single dictionary word with letters, numbers and symbols." +
                " Previous 6 passwords cannot be reused."+ " A Password needs to be at least one day old before you can change it again."+
	            " Passwords must be between 8 and 16 characters and contain at least one of each of  the following:" 
				);
		
		HTML b1 = new HTML("<img src='images/bullet.png'/> Upper case character");
		FocusPanel b1FocusPanel = new FocusPanel(b1);
		b1FocusPanel.setTitle("Upper case character.");
		
		HTML b2 = new HTML("<img src='images/bullet.png'/> Lower case character");
		FocusPanel b2FocusPanel = new FocusPanel(b2);
		b2FocusPanel.setTitle("or Lower case character.");
		
		HTML b3 = new HTML("<img src='images/bullet.png'/> One of the following special characters % &nbsp; # &nbsp; * &nbsp; + &nbsp; , &nbsp; ; &nbsp; = &nbsp; ? &nbsp; _");
		FocusPanel b3FocusPanel = new FocusPanel(b3);
		b3FocusPanel.setTitle("or One of the following special characters % # * + , ; = ? _");
		
		HTML b4 = new HTML("<img src='images/bullet.png'/> Numeric character");
		FocusPanel b4FocusPanel = new FocusPanel(b4);
		b4FocusPanel.setTitle("or Numeric character.");
		
		fPanel.add(rulesLabelFocusPanel);
		fPanel.add(descFocusPanel);
		fPanel.add(b1FocusPanel);
		fPanel.add(b2FocusPanel);
		fPanel.add(b3FocusPanel);
		fPanel.add(b4FocusPanel);
		initWidget(fPanel);
	}
}
