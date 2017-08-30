package mat.client.shared;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.DropDownMenu;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Toggle;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;


// TODO: Auto-generated Javadoc
/**
 * The Class CQLButtonToolBar.
 */
public class CQLButtonToolBar extends Composite {
	
	/** The Insert button. */
	private Button insertButton = new Button();
	
	/** The save button. */
	private Button saveButton = new Button();
	
	/** The delete button. */
	private Button deleteButton = new Button();
		
	/** The erase button. */
	private Button eraseButton = new Button();
	
	/** The close button. */
	private Button closeButton = new Button();
	
	/** The info button. */
	private Button infoButton = new Button();
	
	/** The timing exp icon. */
	private Button timingExpIcon = new Button();
	
	/** The button layout. */
	private HorizontalPanel buttonLayout = new HorizontalPanel();
	
	ButtonGroup infoButtonGroup = new ButtonGroup();
	
	/**
	 * Instantiates a new CQL Button tool bar.
	 *
	 * @param sectionName the section name
	 */
	public CQLButtonToolBar(String sectionName) {
		
		buildInfoButtonGroup(sectionName);
		
		buttonLayout.getElement().setId("cql_buttonLayout_HorizontalPanel");
		//buttonLayout.setStylePrimaryName("myAccountButtonLayout continueButton");
		
		insertButton.setType(ButtonType.LINK);
		insertButton.getElement().setId("insertButton_"+sectionName);
		insertButton.setMarginTop(10);
		insertButton.setTitle("Insert");
		insertButton.setText("Insert");
		insertButton.setIcon(IconType.PLUS_SQUARE);
		insertButton.setIconSize(IconSize.LARGE);
		insertButton.setColor("#0964A2");
		insertButton.setSize("70px", "30px");
		insertButton.getElement().setAttribute("aria-label", "Insert");
		
		
		saveButton.setType(ButtonType.LINK);
		saveButton.getElement().setId("saveButton_"+sectionName);
		saveButton.setMarginTop(10);
		saveButton.setTitle("Save");
		saveButton.setText("Save");
		saveButton.setIcon(IconType.SAVE);
		saveButton.setIconSize(IconSize.LARGE);
		saveButton.setColor("#0964A2");
		saveButton.setSize("70px", "30px");
		saveButton.getElement().setAttribute("aria-label", "Save");
		
		deleteButton.setType(ButtonType.LINK);
		deleteButton.getElement().setId("deleteButton_"+sectionName);
		deleteButton.setMarginTop(10);
		deleteButton.setTitle("Delete");
		deleteButton.setText("Delete");
		// MAT-7737, Use the trash.png image instead of the IconType.TRASH
		/*Image trash = new Image("images/trash.png");
		deleteButton.getElement().appendChild(trash.getElement());*/
		deleteButton.setSize("70px", "30px");
		deleteButton.getElement().setAttribute("aria-label", "Delete");
		deleteButton.setIcon(IconType.TRASH);
		deleteButton.setIconSize(IconSize.LARGE);
		deleteButton.setColor("#0964A2");
		
		eraseButton.setType(ButtonType.LINK);
		eraseButton.getElement().setId("eraseButton_"+sectionName);
		eraseButton.setMarginTop(10);
		eraseButton.setTitle("Erase");
		eraseButton.setText("Erase");
		eraseButton.setIcon(IconType.ERASER);
		eraseButton.setIconSize(IconSize.LARGE);
		eraseButton.setColor("#0964A2");
		eraseButton.setSize("70px", "30px");
		eraseButton.getElement().setAttribute("aria-label", "Erase");
		
		closeButton.setType(ButtonType.LINK);
		closeButton.getElement().setId("closeButton_"+sectionName);
		closeButton.setMarginTop(10);
		closeButton.setTitle("Cancel");
		closeButton.setText("Cancel");
		closeButton.setIcon(IconType.CLOSE);
		closeButton.setIconSize(IconSize.LARGE);
		closeButton.setColor("#0964A2");
		closeButton.setSize("70px", "30px");
		closeButton.getElement().setAttribute("aria-label", "Cancel");
		
		
		
		/*timingExpIcon.setType(ButtonType.LINK);
		timingExpIcon.getElement().setId("timingExpButton_"+sectionName);
		timingExpIcon.setMarginTop(10);
		timingExpIcon.setTitle("Timing Expression");
		timingExpIcon.setIcon(IconType.TEXT_WIDTH);
		timingExpIcon.setIconSize(IconSize.LARGE);
		timingExpIcon.setColor("#0964A2");
		timingExpIcon.setSize("30px", "30px");
		timingExpIcon.getElement().setAttribute("aria-label", "Timing Expression");*/
		
		buttonLayout.add(saveButton);
		buttonLayout.add(eraseButton);
		buttonLayout.add(insertButton);
		//buttonLayout.add(timingExpIcon);
		
		buttonLayout.add(deleteButton);
		buttonLayout.add(closeButton);
		initWidget(buttonLayout);
		
	}


	/**
	 * 
	 */
	private void buildInfoButtonGroup(String sectionName) {
		
		infoButton.setType(ButtonType.LINK);
		infoButton.getElement().setId("infoButton_"+sectionName);
		infoButton.setMarginTop(10);
		infoButton.setTitle("Click to view available short cut keys information");
		infoButton.setText("Information");
		infoButton.setIcon(IconType.INFO_CIRCLE);
		infoButton.setIconSize(IconSize.LARGE);
		infoButton.setColor("#0964A2");
		infoButton.setSize("120px", "30px");
		infoButton.getElement().setAttribute("aria-label", "Click to view available short cut keys information");
		
		infoButton.setToggleCaret(false);
		infoButton.setDataToggle(Toggle.DROPDOWN);
		
		DropDownMenu downMenu = new DropDownMenu();
		
		AnchorListItem item1= new AnchorListItem("Ctrl-Alt-a: attributes");
		item1.setTitle("Ctrl-Alt-a: attributes");
		item1.setHref("#");
		AnchorListItem item2= new AnchorListItem("Ctrl-Alt-y: datatypes");
		item2.setHref("#");
		item2.setTitle("Ctrl-Alt-y: datatypes");
		AnchorListItem item3= new AnchorListItem("Ctrl-Alt-d: definitions");
		item3.setHref("#");
		item3.setTitle("Ctrl-Alt-d: definitions");
		AnchorListItem item4= new AnchorListItem("Ctrl-Alt-f: functions");
		item4.setHref("#");
		item4.setTitle(item4.getText());
		AnchorListItem item5= new AnchorListItem("Ctrl-Alt-k: keywords");
		item5.setHref("#");
		item5.setTitle(item5.getText());
		AnchorListItem item6= new AnchorListItem("Ctrl-Alt-p: parameters");
		item6.setHref("#");
		item6.setTitle(item6.getText());
		AnchorListItem item7= new AnchorListItem("Ctrl-Alt-t: timings");
		item7.setHref("#");
		item7.setTitle(item7.getText());
		AnchorListItem item8= new AnchorListItem("Ctrl-Alt-v: value sets & codes");
		item8.setHref("#");
		item8.setTitle(item8.getText());
		AnchorListItem item9= new AnchorListItem("Ctrl-Space: all");
		item9.setHref("#");
		item9.setTitle(item9.getText());
		
		
		Anchor itemAnchor1 = (Anchor) (item1.getWidget(0));
		itemAnchor1.getElement().setAttribute("style", "cursor:text");
		
		Anchor itemAnchor2 = (Anchor) (item2.getWidget(0));
		itemAnchor2.getElement().setAttribute("style", "cursor:text");
		
		Anchor itemAnchor3 = (Anchor) (item3.getWidget(0));
		itemAnchor3.getElement().setAttribute("style", "cursor:text");
		
		Anchor itemAnchor4 = (Anchor) (item4.getWidget(0));
		itemAnchor4.getElement().setAttribute("style", "cursor:text");
		
		Anchor itemAnchor5 = (Anchor) (item5.getWidget(0));
		itemAnchor5.getElement().setAttribute("style", "cursor:text");
		
		Anchor itemAnchor6 = (Anchor) (item6.getWidget(0));
		itemAnchor6.getElement().setAttribute("style", "cursor:text");
		
		Anchor itemAnchor7 = (Anchor) (item7.getWidget(0));
		itemAnchor7.getElement().setAttribute("style", "cursor:text");
		
		Anchor itemAnchor8 = (Anchor) (item8.getWidget(0));
		itemAnchor8.getElement().setAttribute("style", "cursor:text");
		
		Anchor itemAnchor9 = (Anchor) (item9.getWidget(0));
		itemAnchor9.getElement().setAttribute("style", "cursor:text");
		
		
		downMenu.setWidth("50px");
		downMenu.getElement().setAttribute("style", "font-size:small;");
		downMenu.add(item1);
		downMenu.add(item2);
		downMenu.add(item3);
		downMenu.add(item4);
		downMenu.add(item5);
		downMenu.add(item6);
		
		downMenu.add(item7);
		downMenu.add(item8);
		downMenu.add(item9);
		//infoButtonGroup.setDropUp(true);
		infoButtonGroup.getElement().setAttribute("class", "btn-group");
		
		infoButtonGroup.add(infoButton);
		infoButtonGroup.add(downMenu);
		infoButtonGroup.getElement().setAttribute("style", "margin-top:-10px;");
	}
	
	
	/**
	 * Sets the enabled.
	 *
	 * @param isEnabled the new enabled
	 */
	public void setEnabled(boolean isEnabled){
		insertButton.setEnabled(isEnabled);
		saveButton.setEnabled(isEnabled);
		deleteButton.setEnabled(isEnabled);
		eraseButton.setEnabled(isEnabled);
		infoButton.setEnabled(isEnabled);
		closeButton.setEnabled(isEnabled);
	}
	
	/**
	 * Gets the insert button.
	 *
	 * @return the insert button
	 */
	public Button getInsertButton() {
		return insertButton;
	}
	/**
	 * Gets the save button.
	 *
	 * @return the save button
	 */
	public Button getSaveButton() {
		return saveButton;
	}
	
	
	/**
	 * Gets the delete button.
	 *
	 * @return the delete button
	 */
	public Button getDeleteButton() {
		return deleteButton;
	}
	
	
	/**
	 * Gets the erase button.
	 *
	 * @return the erase button
	 */
	public Button getEraseButton() {
		return eraseButton;
	}
	
	/**
	 * Gets the info button.
	 *
	 * @return the info button
	 */
	public Button getInfoButton(){
		return infoButton;
	}
	
	/**
	 * Gets the timing exp button.
	 *
	 * @return the timing exp button
	 */
	public Button getTimingExpButton(){
		return timingExpIcon;
	}
	
	
	/**
	 * Gets the button layout.
	 *
	 * @return the button layout
	 */
	public HorizontalPanel getButtonLayout() {
		return buttonLayout;
	}
	
	/**
	 * Gets the close button.
	 *
	 * @return the close button
	 */
	public Button getCloseButton(){
		return closeButton;
	}


	public ButtonGroup getInfoButtonGroup() {
		return infoButtonGroup;
	}


	public void setInfoButtonGroup(ButtonGroup infoButtonGroup) {
		this.infoButtonGroup = infoButtonGroup;
	}
	
}
