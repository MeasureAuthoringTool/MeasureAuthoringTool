package mat.client.buttons;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.DropDownMenu;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;


/**
 * The Button Tool Bar for the Definition and Function sections
 * Includes 'Insert', 'Save', 'Edit', 'Delete', 'save', 'cancel', 'erase'
 */
public class Definition_FuntionButtonToolBar extends Composite {
	private Button insertButton, editButton, saveButton, deleteButton, eraseButton, cancelButton, infoButton;
	private Button timingExpIcon = new Button();
	private HorizontalPanel buttonLayout = new HorizontalPanel();
	private ButtonGroup infoButtonGroup = new ButtonGroup();
	private String sectionName;

	
	public Definition_FuntionButtonToolBar(String sectionName) {
		this.sectionName = sectionName;
		buildInfoButtonGroup();
		
		buttonLayout.getElement().setId("cql_buttonLayout_HorizontalPanel");		

		addEditButton();
		addSaveButton();
		addEraseButton();
		addInsertButton();
		addDeleteButton();
		addCancelButton();
		initWidget(buttonLayout);
			
	}
	
	private void addCancelButton() {
		cancelButton = new CancelToolBarButton(sectionName).getButton();
		buttonLayout.add(cancelButton);
	}
	
	private void addEraseButton() {
		eraseButton = new EraseToolBarButton(sectionName).getButton();
		buttonLayout.add(eraseButton);
	}
	
	private void addDeleteButton() {
		deleteButton = new DeleteToolBarButton(sectionName).getButton();
		buttonLayout.add(deleteButton);
	}
	
	private void addEditButton() {
		editButton = new EditToolBarButton(sectionName).getButton();
		editButton.setVisible(false);
		buttonLayout.add(editButton);
	}
	
	private void addSaveButton() {
		saveButton = new SaveToolBarButton(sectionName).getButton();
		buttonLayout.add(saveButton);
	}
	
	private void addInsertButton() {
		insertButton = new InsertToolBarButton(sectionName).getButton();
		buttonLayout.add(insertButton);
	}


	private void buildInfoButtonGroup() {
		
		infoButton = new InfoToolBarButton(sectionName).getButton();
		
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
		
		AnchorListItem item8 = new AnchorListItem("Ctrl-Alt-u: units");
		item8.setHref("#");
		item8.setTitle(item8.getText());
		
		AnchorListItem item9= new AnchorListItem("Ctrl-Alt-v: value sets & codes");
		item9.setHref("#");
		item9.setTitle(item9.getText());
		
		AnchorListItem item10= new AnchorListItem("Ctrl-Space: all");
		item10.setHref("#");
		item10.setTitle(item10.getText());
		
		
		Anchor itemAnchor1 = (Anchor) (item1.getWidget(0));
		itemAnchor1.getElement().setAttribute("style", "cursor:text");
		itemAnchor1.getElement().setAttribute("aria-label", "Ctrl-Alt-a: attributes");
		
		Anchor itemAnchor2 = (Anchor) (item2.getWidget(0));
		itemAnchor2.getElement().setAttribute("style", "cursor:text");
		itemAnchor2.getElement().setAttribute("aria-label", "Ctrl-Alt-y: datatypes");
		
		Anchor itemAnchor3 = (Anchor) (item3.getWidget(0));
		itemAnchor3.getElement().setAttribute("style", "cursor:text");
		itemAnchor3.getElement().setAttribute("aria-label", "Ctrl-Alt-d: definitions");
		
		Anchor itemAnchor4 = (Anchor) (item4.getWidget(0));
		itemAnchor4.getElement().setAttribute("style", "cursor:text");
		itemAnchor4.getElement().setAttribute("aria-label", "Ctrl-Alt-f: functions");
		
		Anchor itemAnchor5 = (Anchor) (item5.getWidget(0));
		itemAnchor5.getElement().setAttribute("style", "cursor:text");
		itemAnchor5.getElement().setAttribute("aria-label", "Ctrl-Alt-k: keywords");
		
		Anchor itemAnchor6 = (Anchor) (item6.getWidget(0));
		itemAnchor6.getElement().setAttribute("style", "cursor:text");
		itemAnchor6.getElement().setAttribute("aria-label", "Ctrl-Alt-p: parameters");
		
		Anchor itemAnchor7 = (Anchor) (item7.getWidget(0));
		itemAnchor7.getElement().setAttribute("style", "cursor:text");
		itemAnchor7.getElement().setAttribute("aria-label", "Ctrl-Alt-t: timings");
		
		Anchor itemAnchor8 = (Anchor) (item8.getWidget(0));
		itemAnchor8.getElement().setAttribute("style", "cursor:text");
		itemAnchor8.getElement().setAttribute("aria-label", "Ctrl-Alt-u: units");
		
		Anchor itemAnchor9 = (Anchor) (item9.getWidget(0));
		itemAnchor9.getElement().setAttribute("style", "cursor:text");
		itemAnchor9.getElement().setAttribute("aria-label", "Ctrl-Alt-v: value sets & codes");
		
		Anchor itemAnchor10 = (Anchor) (item10.getWidget(0));
		itemAnchor10.getElement().setAttribute("style", "cursor:text");
		itemAnchor10.getElement().setAttribute("aria-label", "Ctrl-Space: all");
		
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
		downMenu.add(item10);

		infoButtonGroup.getElement().setAttribute("class", "btn-group");
		infoButtonGroup.add(infoButton);
		infoButtonGroup.add(downMenu);
		infoButtonGroup.getElement().setAttribute("style", "margin-top:-10px;");
	}
	
	public void setEnabled(boolean isEnabled){
		insertButton.setEnabled(isEnabled);
		saveButton.setEnabled(isEnabled);
		deleteButton.setEnabled(isEnabled);
		eraseButton.setEnabled(isEnabled);
		infoButton.setEnabled(isEnabled);
		cancelButton.setEnabled(isEnabled);
	}
	
	public Button getInsertButton() {
		return insertButton;
	}

	public Button getSaveButton() {
		return saveButton;
	}

	public Button getDeleteButton() {
		return deleteButton;
	}

	public Button getEraseButton() {
		return eraseButton;
	}

	public Button getInfoButton(){
		return infoButton;
	}

	public Button getTimingExpButton(){
		return timingExpIcon;
	}

	public Button getCloseButton(){
		return cancelButton;
	}

	public ButtonGroup getInfoButtonGroup() {
		return infoButtonGroup;
	}

	public Button getEditButton() {
		return editButton;
	}
	
	public HorizontalPanel getButtonLayout() {
		return buttonLayout;
	}
}
