package mat.client.shared;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class UserNameWidget.
 */
public class UserNameWidget extends Composite {
	
	/** The first name. */
	private TextBox firstName = new TextBox();
	
	/** The last name. */
	private TextBox lastName = new TextBox();
	
	/** The middle initial. */
	private TextBox middleInitial = new TextBox();
	
	/** The first name label. */
	private Widget firstNameLabel = LabelBuilder.buildRequiredLabel(firstName, "First Name");
	
	/** The middle initial label. */
	private Widget  middleInitialLabel = LabelBuilder.buildLabel(middleInitial, "M. I.");
	
	/** The last name label. */
	private Widget  lastNameLabel = LabelBuilder.buildRequiredLabel(lastName, "Last Name");
	
	/**
	 * Instantiates a new user name widget.
	 */
	public UserNameWidget() {
		firstName.getElement().setId("firstName_TextBox");
		lastName.getElement().setId("lastName_TextBox");
		middleInitial.getElement().setId("middleInitial_TextBox");
		firstName.setTitle("First Name");
		lastName.setTitle("Last Name");
		middleInitial.setTitle("Middle Initial");
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
	
	/**
	 * Gets the first name.
	 * 
	 * @return the first name
	 */
	public HasValue<String> getFirstName() {
		return firstName;
	}
	
	/**
	 * Gets the last name.
	 * 
	 * @return the last name
	 */
	public HasValue<String> getLastName() {
		return lastName;
	}
	
	/**
	 * Gets the middle initial.
	 * 
	 * @return the middle initial
	 */
	public HasValue<String> getMiddleInitial() {
		return middleInitial;
	}
	
}
