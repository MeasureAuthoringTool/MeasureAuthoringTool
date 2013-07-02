package mat.client.shared;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PasswordEditInfoWidget extends Composite {
	
	private PasswordTextBox password = new PasswordTextBox();
	
	public PasswordEditInfoWidget() {
		FlowPanel container = new FlowPanel();
		container.add(LabelBuilder.buildRequiredLabel(password, "Enter existing password to confirm changes"));
		container.add(wrap(password));
		container.add(new SpacerWidget());
		
		password.setWidth("100px");
		initWidget(container);
	}
	private SimplePanel wrap(Widget widget) {
		SimplePanel p = new SimplePanel();
		p.add(widget);
		return p;
	}
	public PasswordTextBox getPassword() {
		return password;
	}
	public void setPassword(PasswordTextBox password) {
		this.password = password ;
	}
	
}
