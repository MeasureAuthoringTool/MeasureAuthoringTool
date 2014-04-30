package mat.client.clause.clauseworkspace.view;

import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay;
import mat.client.shared.LabelBuilder;
import mat.shared.UUIDUtilClient;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

// TODO: Auto-generated Javadoc
/**
 * The Class EditSubTreeDialogBox.
 */
public class EditSubTreeDialogBox {
	
	/** The dialog box. */
	public static DialogBox dialogBox = new DialogBox(false,true);
	/**
	 * Show edit dialog box.
	 *
	 * @param xmlTreeDisplay the xml tree display
	 * @param cellTreeNode the cell tree node
	 */
	public static void showEditDialogBox(final XmlTreeDisplay xmlTreeDisplay, final CellTreeNode cellTreeNode) {
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		dialogBox.setText("Edit");
		dialogBox.setTitle("Edit");
		dialogBox.getElement().setAttribute("id", "EditSubTreeDialogBox");
		String existingSubTreeName = cellTreeNode.getName();
		// Create a table to layout the content
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.setWidth("20em");
		dialogContents.setSpacing(5);
		dialogContents.getElement().setId("dialogContents_VerticalPanel");
		dialogBox.setWidget(dialogContents);
		final TextBox subTreeName = new TextBox();
		subTreeName.getElement().setId("quantity_TextBox");
		subTreeName.setWidth("150px");
		subTreeName.setText(existingSubTreeName);
		subTreeName.setCursorPos(subTreeName.getText().length());
		Label lableNewSubTreeName = (Label) LabelBuilder.buildLabel(subTreeName, "New Clause Name");
		dialogContents.add(lableNewSubTreeName);
		dialogContents.setCellHorizontalAlignment(lableNewSubTreeName, HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents.add(subTreeName);
		dialogContents.setCellHorizontalAlignment(subTreeName, HasHorizontalAlignment.ALIGN_LEFT);
		//Add a Enter press event to the text box
		subTreeName.addKeyPressHandler(new KeyPressHandler() {
		@Override
		 public void onKeyPress(KeyPressEvent event) { 
		    if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) { 
		    	dialogBox.hide();
		    	xmlTreeDisplay.editNode(subTreeName.getText(), subTreeName.getText());
				xmlTreeDisplay.setDirty(true); 
				
		    }
		}
		});
		// Add a Save button at the bottom of the dialog
		Button save = new Button("OK", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				xmlTreeDisplay.editNode(subTreeName.getText(), subTreeName.getText());
				xmlTreeDisplay.setDirty(true);
				
			}
		});
		save.getElement().setId("save_Button");
		// Add a Close button at the bottom of the dialog
		Button closeButton = new Button("Cancel", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				
			}
		});
		closeButton.getElement().setId("closeButton_Button");
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(10);
		buttonPanel.add(save);
		buttonPanel.setCellHorizontalAlignment(save, HasHorizontalAlignment.ALIGN_RIGHT);
		buttonPanel.add(closeButton);
		buttonPanel.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_RIGHT);
		buttonPanel.getElement().setId("buttonPanel_HorizontalPanel");
		dialogContents.add(buttonPanel);
		dialogBox.center();
		dialogBox.show();
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

		    @Override
		    public void execute() {
		    	subTreeName.setCursorPos(subTreeName.getText().length());
		    	subTreeName.setFocus(true);
		    }
		});
		
	}
	
	/**
	 * Show add dialog box.
	 *
	 * @param xmlTreeDisplay the xml tree display
	 * @param cellTreeNode the cell tree node
	 */
	public static void showAddDialogBox(final XmlTreeDisplay xmlTreeDisplay, final CellTreeNode cellTreeNode) {
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		dialogBox.setText("Add");
		dialogBox.setTitle("Add");
		// Create a table to layout the content
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.setWidth("20em");
		dialogContents.setSpacing(5);
		dialogContents.getElement().setId("dialogContents_VerticalPanel");
		dialogBox.setWidget(dialogContents);
		final TextBox subTreeName = new TextBox();
		subTreeName.setEnabled(true);
		subTreeName.getElement().setId("quantity_TextBox");
		subTreeName.setWidth("150px");
		Label lableNewSubTreeName = (Label) LabelBuilder.buildLabel(subTreeName, "New Clause Name");
		dialogContents.add(lableNewSubTreeName);
		dialogContents.setCellHorizontalAlignment(lableNewSubTreeName, HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents.add(subTreeName);
		dialogContents.setCellHorizontalAlignment(subTreeName, HasHorizontalAlignment.ALIGN_LEFT);
		//Add a Enter press event to the text box
		subTreeName.addKeyPressHandler(new KeyPressHandler() {
		@Override
		 public void onKeyPress(KeyPressEvent event) { 
		    if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) { 
		    	dialogBox.hide();
				xmlTreeDisplay.addNode(subTreeName.getText(), subTreeName.getText()
						, UUIDUtilClient.uuid(), CellTreeNode.SUBTREE_NODE);
				xmlTreeDisplay.setDirty(true); 
				
		    }
		}
		});
		//Add a OK button at the bottom of the dialog
		Button save = new Button("OK", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				xmlTreeDisplay.addNode(subTreeName.getText()
						, subTreeName.getText()
						, UUIDUtilClient.uuid(), CellTreeNode.SUBTREE_NODE);
				xmlTreeDisplay.setDirty(true);
				
			}
			
		});
		save.getElement().setId("save_Button");
		// Add a Close button at the bottom of the dialog
		Button closeButton = new Button("Cancel", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				
			}
		});
		closeButton.getElement().setId("closeButton_Button");
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(10);
		buttonPanel.add(save);
		buttonPanel.setCellHorizontalAlignment(save, HasHorizontalAlignment.ALIGN_RIGHT);
		buttonPanel.add(closeButton);
		buttonPanel.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_RIGHT);
		buttonPanel.getElement().setId("buttonPanel_HorizontalPanel");
		dialogContents.add(buttonPanel);
		dialogBox.center();
		dialogBox.show();
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

		    @Override
		    public void execute() {
		    	subTreeName.setCursorPos(subTreeName.getText().length());
		    	subTreeName.setFocus(true);
		    }
		});
		
	}
	
}
