package mat.client.clause.clauseworkspace.view;

import mat.client.ImageResources;
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
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

// TODO: Auto-generated Javadoc
/**
 * The Class EditSubTreeDialogBox.
 */
public class EditSubTreeDialogBox {
	
	/** The is clause present. */
	static boolean isClausePresent = false;
	
	/** The result. */
	static boolean result = false;
	
	/** The Validation message. */
	public static HorizontalPanel validationMessagePanel = new HorizontalPanel();
	
	/** The message label. */
	public static Label messageLabel = new Label();
	
	/** The warning icon. */
	public static  Image warningIcon = new Image(ImageResources.INSTANCE.msg_error());
	
	
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
		final VerticalPanel dialogContents = new VerticalPanel();
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
		validationMessagePanel.add(warningIcon);
		messageLabel.setText("Clause Name Already Exists");
		validationMessagePanel.setStyleName("alertMessage");
		validationMessagePanel.add(messageLabel);
		dialogContents.setCellHorizontalAlignment(validationMessagePanel, HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents.setCellHorizontalAlignment(lableNewSubTreeName, HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents.add(subTreeName);
		dialogContents.setCellHorizontalAlignment(subTreeName, HasHorizontalAlignment.ALIGN_LEFT);
		
		//Add a Enter press event to the text box
		subTreeName.addKeyPressHandler(new KeyPressHandler() {
		@Override
		 public void onKeyPress(KeyPressEvent event) { 
		    if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
				
				int numberOfClause = xmlTreeDisplay.getClauseNamesListBox().getItemCount();
				if(numberOfClause >0){
					isClausePresent = checkForClauseNameIfPresent(result, numberOfClause, subTreeName.getText().trim(), xmlTreeDisplay);
				}
				
				if(!isClausePresent){
					dialogBox.hide();
					xmlTreeDisplay.editNode(subTreeName.getText(), subTreeName.getText());
					xmlTreeDisplay.setDirty(true);
				}else{
					dialogContents.insert(validationMessagePanel, 0);
					dialogBox.show();
				}
				
			}
		}

				});
		// Add a Save button at the bottom of the dialog
		Button save = new Button("OK", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int numberOfClause = xmlTreeDisplay.getClauseNamesListBox().getItemCount();
				if(!xmlTreeDisplay.getSelectedNode().getName().equalsIgnoreCase(subTreeName.getText().trim())){
					
					if(numberOfClause >0){
						isClausePresent = checkForClauseNameIfPresent(result, numberOfClause, subTreeName.getText().trim(), xmlTreeDisplay);
					}
					
					if(!isClausePresent){
						dialogBox.hide();
						xmlTreeDisplay.editNode(subTreeName.getText(), subTreeName.getText());
						xmlTreeDisplay.setDirty(true);
					}else{
						dialogContents.insert(validationMessagePanel, 0);
						dialogBox.show();
					}
				}else{
					dialogBox.hide();
				}
				
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
		final VerticalPanel dialogContents = new VerticalPanel();
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
		validationMessagePanel.add(warningIcon);
		messageLabel.setText("Clause Name Already Exists");
		validationMessagePanel.setStyleName("alertMessage");
		validationMessagePanel.add(messageLabel);
		dialogContents.setCellHorizontalAlignment(validationMessagePanel, HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents.setCellHorizontalAlignment(lableNewSubTreeName, HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents.add(subTreeName);
		dialogContents.setCellHorizontalAlignment(subTreeName, HasHorizontalAlignment.ALIGN_LEFT);
		//Add a Enter press event to the text box
		subTreeName.addKeyPressHandler(new KeyPressHandler() {
		@Override
		 public void onKeyPress(KeyPressEvent event) { 
		    if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) { 
				dialogBox.hide();
				int numberOfClause = xmlTreeDisplay.getClauseNamesListBox().getItemCount();
				if(numberOfClause >0){
					isClausePresent = checkForClauseNameIfPresent(result, numberOfClause, subTreeName.getText().trim(), xmlTreeDisplay);
				}
				
				if(!isClausePresent){
					xmlTreeDisplay.addNode(subTreeName.getText()
							, subTreeName.getText()
							, UUIDUtilClient.uuid(), CellTreeNode.SUBTREE_NODE);
					xmlTreeDisplay.getIncludeQdmVaribale().setVisible(true);
					xmlTreeDisplay.setDirty(true);
				}else{
					dialogContents.insert(validationMessagePanel, 0);
					dialogBox.show();
				}
				
			}
		}
	});
		
		//Add a OK button at the bottom of the dialog
		Button save = new Button("OK", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				int numberOfClause = xmlTreeDisplay.getClauseNamesListBox().getItemCount();
				if(numberOfClause >0){
					isClausePresent = checkForClauseNameIfPresent(result, numberOfClause, subTreeName.getText().trim(),xmlTreeDisplay);
				}
				if(!isClausePresent){
					xmlTreeDisplay.addNode(subTreeName.getText()
							, subTreeName.getText()
							, UUIDUtilClient.uuid(), CellTreeNode.SUBTREE_NODE);
					xmlTreeDisplay.getIncludeQdmVaribale().setVisible(true);
					xmlTreeDisplay.setDirty(true);
				}else{
					dialogContents.insert(validationMessagePanel, 0);
					dialogBox.show();
				}
				
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
	 * Check for clause name if present.
	 *
	 * @param result the result
	 * @param numberOfClause the number of clause
	 * @param text the text
	 * @param xmlTreeDisplay the xml tree display
	 * @return true, if successful
	 */
	protected static boolean checkForClauseNameIfPresent(boolean result,
		int numberOfClause, String text, XmlTreeDisplay xmlTreeDisplay) {
			for(int i=0; i< numberOfClause; i++){
				if(xmlTreeDisplay.getClauseNamesListBox().getItemText(i).equalsIgnoreCase(text)){
					result = true;
					break;
				}
			}
			return result;
	}	
	
}
