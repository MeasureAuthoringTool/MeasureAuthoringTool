package mat.client.shared;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class UserNameWidget extends Composite {
	private TextBox firstName = new TextBox();
	private TextBox lastName = new TextBox();
	private TextBox middleInitial = new TextBox();

	private Widget firstNameLabel = LabelBuilder.buildRequiredLabel(firstName, "First Name");
	private Widget  middleInitialLabel = LabelBuilder.buildLabel(middleInitial, "M. I.");
	private Widget  lastNameLabel = LabelBuilder.buildRequiredLabel(lastName, "Last Name");
	
	public UserNameWidget() {
		Grid nameGrid = new Grid(2,3);
		nameGrid.setWidget(0, 0, firstNameLabel);
		nameGrid.setWidget(0, 1, middleInitialLabel);
		nameGrid.setWidget(0, 2, lastNameLabel);
		nameGrid.setWidget(1, 0, firstName);
		nameGrid.setWidget(1, 1, middleInitial);
		nameGrid.setWidget(1, 2, lastName);
		
		nameGrid.addStyleName("leftAligned");
		
		firstName.setWidth("165px");
		lastName.setWidth("165px");
		middleInitial.setWidth("30px");
		
		middleInitial.setMaxLength(1);
		firstName.setMaxLength(100);
		lastName.setMaxLength(100);
		initWidget(nameGrid);
	}
	
	public HasValue<String> getFirstName() {
		return firstName;
	}

	public HasValue<String> getLastName() {
		return lastName;
	}

	public HasValue<String> getMiddleInitial() {
		return middleInitial;
	}

}
