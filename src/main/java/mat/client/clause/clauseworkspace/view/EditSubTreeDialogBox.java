package mat.client.clause.clauseworkspace.view;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import mat.client.ImageResources;
import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay;
import mat.client.shared.LabelBuilder;
import mat.shared.UUIDUtilClient;


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
	
	/** The is clause name added. */
	private static boolean isClauseNameAdded;

	/**
	 * Show edit dialog box.
	 *
	 * @param xmlTreeDisplay the xml tree display
	 * @param cellTreeNode the cell tree node
	 */
	public static void showEditDialogBox(final XmlTreeDisplay xmlTreeDisplay, final CellTreeNode cellTreeNode) {
		
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		dialogBox.setText("Edit Clause Name");
		dialogBox.getElement().setAttribute("id", "EditSubTreeDialogBox");
		String existingSubTreeName = cellTreeNode.getName();
		// Create a table to layout the content
		final VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.getElement().setId("dialogContents_VerticalPanel");
		DOM.setStyleAttribute(dialogBox.getElement(), "width", "1000px");
		DOM.setStyleAttribute(dialogBox.getElement(), "height", "1000px");
		dialogBox.setWidget(dialogContents);
		
		final HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.getElement().setId("editClauseName_hPanel");
		
		final VerticalPanel dialogContents1 = new VerticalPanel();
		dialogContents1.setWidth("20em");
		dialogContents1.setSpacing(5);
		
		final TextBox subTreeName = new TextBox();
		subTreeName.getElement().setId("quantity_TextBox");
		subTreeName.setWidth("300px");
		subTreeName.setMaxLength(75);
		subTreeName.setText(existingSubTreeName);
		subTreeName.setCursorPos(subTreeName.getText().length());
		subTreeName.setTitle(existingSubTreeName);
		Label lableNewSubTreeName = (Label) LabelBuilder.buildLabel(subTreeName, "New Clause Name");
		dialogContents1.add(lableNewSubTreeName);
	
		dialogContents1.setCellHorizontalAlignment(validationMessagePanel, HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents1.setCellHorizontalAlignment(lableNewSubTreeName, HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents1.add(subTreeName);
		dialogContents1.setCellHorizontalAlignment(subTreeName, HasHorizontalAlignment.ALIGN_LEFT);
		
		final VerticalPanel dialogContents2 = new VerticalPanel();
		Label clauseCreationRule = (Label) LabelBuilder.buildLabel(subTreeName, "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Clause Creation Rules:</b>");
		Label clauseNameRule1 = (Label) LabelBuilder.buildLabel(subTreeName, "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Allowed Characters: Alphanumeric, White space, Underscore");
		Label clauseNameRule2 = (Label) LabelBuilder.buildLabel(subTreeName, "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Must begin with a letter");
		Label clauseNameRule3 = (Label) LabelBuilder.buildLabel(subTreeName, "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Clause name cannot be greater than 75 characters");
		
		dialogContents2.add(clauseCreationRule);
		dialogContents2.add(clauseNameRule1);
		dialogContents2.add(clauseNameRule2);
		dialogContents2.add(clauseNameRule3);
		
		dialogContents2.setCellHorizontalAlignment(clauseCreationRule, HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents2.setCellHorizontalAlignment(clauseNameRule1, HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents2.setCellHorizontalAlignment(clauseNameRule2, HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents2.setCellHorizontalAlignment(clauseNameRule3, HasHorizontalAlignment.ALIGN_LEFT);
		
		hPanel.add(dialogContents1);
		hPanel.add(dialogContents2);
		dialogContents.add(hPanel);
		
		// Add a Save button at the bottom of the dialog
		final Button save = new Button("OK", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogContents.remove(validationMessagePanel);
				String clauseNameWithSpaces = subTreeName.getText().trim();
				String clauseName = clauseNameWithSpaces.replaceAll("( )+", " ");
				subTreeName.setText(clauseName);

				boolean isValidClauseName = true;
				int numberOfClause = xmlTreeDisplay.getClauseNamesListBox().getItemCount();
				for(int i = 0; i< clauseName.length(); i++){
					if(!Character.isLetter(clauseName.charAt(i)) && !Character.isDigit(clauseName.charAt(i)) && clauseName.charAt(i) != '_'
							&& clauseName.charAt(i) != ' '){
						isValidClauseName = false;
						break;
					}
				}
				if(!isValidClauseName){
					validationMessagePanel.add(warningIcon);
					messageLabel.setText("The Clause Name you entered does not match the following rules: \n  Allowed Characters: Alphanumeric, White space, Underscore");
					validationMessagePanel.setStyleName("alertMessage");
					validationMessagePanel.add(messageLabel);
					dialogContents.insert(validationMessagePanel, 0);
					dialogBox.setPopupPosition(365, 215);
					dialogBox.show();
				}
				else{
					if(!xmlTreeDisplay.getSelectedNode().getName().equalsIgnoreCase(subTreeName.getText().trim())){
						
						if(numberOfClause >0){
							isClausePresent = checkForClauseNameIfPresent(result, numberOfClause, subTreeName.getText().trim(), xmlTreeDisplay);
						}
						
						if(!isClausePresent){
							dialogBox.hide();
							xmlTreeDisplay.editNode(subTreeName.getText(), subTreeName.getText());
							xmlTreeDisplay.setDirty(true);
						}else{
							validationMessagePanel.add(warningIcon);
							messageLabel.setText("Clause name already exists");
							validationMessagePanel.setStyleName("alertMessage");
							validationMessagePanel.add(messageLabel);
							dialogContents.insert(validationMessagePanel, 0);
							dialogBox.setPopupPosition(365, 215);
							dialogBox.show();
						}
					} else {
						
						if (xmlTreeDisplay.getSelectedNode().getName().equalsIgnoreCase(subTreeName.getText().trim())) {
							isClausePresent = false;
						} 
						
						if(!isClausePresent){
							dialogBox.hide();
							xmlTreeDisplay.editNode(subTreeName.getText(), subTreeName.getText());
							xmlTreeDisplay.setDirty(true);
						}else{
							validationMessagePanel.add(warningIcon);
							messageLabel.setText("Clause name already exists");
							validationMessagePanel.setStyleName("alertMessage");
							validationMessagePanel.add(messageLabel);
							dialogContents.insert(validationMessagePanel, 0);
							dialogBox.setPopupPosition(365, 215);
							dialogBox.show();
						}
						//dialogBox.hide();
					}
				}
				
			}
		});
		save.getElement().setId("save_Button");
		
		//Add a Enter press event to the text box
		subTreeName.addKeyUpHandler(new KeyUpHandler() {
			@Override
			 public void onKeyUp(KeyUpEvent event) { 
				String clauseName = subTreeName.getText().trim();
				if(clauseName == null || "".equals(clauseName.trim()) || !Character.isLetter(clauseName.charAt(0))){
					subTreeName.setText("");
					save.setEnabled(false);
				}
				else{
					save.setEnabled(true);
				}
				subTreeName.setTitle(clauseName);
			    if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
					save.click();
				}
			}
		});
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
		//dialogBox.center();
		dialogBox.setPopupPosition(365, 215);
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
		dialogBox.setText("Add Clause Name");
		setClauseNameAdded(false);
		// Create a table to layout the content
		final VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.getElement().setId("dialogContents_VerticalPanel");
		DOM.setStyleAttribute(dialogBox.getElement(), "width", "1000px");
		DOM.setStyleAttribute(dialogBox.getElement(), "height", "1000px");
		dialogBox.setWidget(dialogContents);
		
		final HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.getElement().setId("addCluaseName_hPanel");
		
		final VerticalPanel dialogContents1 = new VerticalPanel();
		dialogContents1.setWidth("20em");
		dialogContents1.setSpacing(5);
		
		final TextBox subTreeName = new TextBox();
		subTreeName.setEnabled(true);
		subTreeName.getElement().setId("quantity_TextBox");
		subTreeName.setWidth("300px");
		subTreeName.setMaxLength(75);
		Label lableNewSubTreeName = (Label) LabelBuilder.buildLabel(subTreeName, "New Clause Name");
		dialogContents1.add(lableNewSubTreeName);
		
		dialogContents1.setCellHorizontalAlignment(validationMessagePanel, HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents1.setCellHorizontalAlignment(lableNewSubTreeName, HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents1.add(subTreeName);
		dialogContents1.setCellHorizontalAlignment(subTreeName, HasHorizontalAlignment.ALIGN_LEFT);
		
		final VerticalPanel dialogContents2 = new VerticalPanel();
		Label clauseCreationRule = (Label) LabelBuilder.buildLabel(subTreeName, "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Clause Creation Rules:</b>");
		Label clauseNameRule1 = (Label) LabelBuilder.buildLabel(subTreeName, "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Allowed Characters: Alphanumeric, White space, Underscore");
		Label clauseNameRule2 = (Label) LabelBuilder.buildLabel(subTreeName, "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Must begin with a letter");
		Label clauseNameRule3 = (Label) LabelBuilder.buildLabel(subTreeName, "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Clause name cannot be greater than 75 characters");
		
		dialogContents2.add(clauseCreationRule);
		dialogContents2.add(clauseNameRule1);
		dialogContents2.add(clauseNameRule2);
		dialogContents2.add(clauseNameRule3);
		
		dialogContents2.setCellHorizontalAlignment(clauseCreationRule, HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents2.setCellHorizontalAlignment(clauseNameRule1, HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents2.setCellHorizontalAlignment(clauseNameRule2, HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents2.setCellHorizontalAlignment(clauseNameRule3, HasHorizontalAlignment.ALIGN_LEFT);
		
		hPanel.add(dialogContents1);
		hPanel.add(dialogContents2);
		dialogContents.add(hPanel);
		
		
		//Add a OK button at the bottom of the dialog
		final Button save = new Button("OK", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				if (!isClauseNameAdded()) {
					dialogBox.hide();
					validationMessagePanel.removeStyleName("alertMessage");
					dialogContents.remove(validationMessagePanel);
					String clauseNameWithSpaces = subTreeName.getText().trim();
					String clauseName = clauseNameWithSpaces.replaceAll("( )+", " ");
					subTreeName.setText(clauseName);
					boolean isValidClauseName = true;
					int numberOfClause = xmlTreeDisplay.getClauseNamesListBox().getItemCount();
					for(int i = 0; i< clauseName.length(); i++){
						if(!Character.isLetter(clauseName.charAt(i)) && !Character.isDigit(clauseName.charAt(i)) && clauseName.charAt(i) != '_'
								&& clauseName.charAt(i) != ' '){
							isValidClauseName = false;
							break;
						}
					}
					if(!isValidClauseName){
						validationMessagePanel.add(warningIcon);
						messageLabel.setText("The Clause Name you entered does not match the following rules: \n Allowed Characters: Alphanumeric, White space, Underscore");
						validationMessagePanel.setStyleName("alertMessage");
						validationMessagePanel.add(messageLabel);
						dialogContents.insert(validationMessagePanel, 0);
						dialogBox.setPopupPosition(365, 215);
						dialogBox.show();
					}
					else{
						if(numberOfClause >0){
							isClausePresent = checkForClauseNameIfPresent(result, numberOfClause, subTreeName.getText().trim(),xmlTreeDisplay);
						}
						if(!isClausePresent){
							xmlTreeDisplay.addNode(subTreeName.getText()
									, subTreeName.getText()
									, UUIDUtilClient.uuid(), CellTreeNode.SUBTREE_NODE);
							xmlTreeDisplay.getIncludeQdmVaribale().setVisible(true);
							xmlTreeDisplay.setDirty(true);
							setClauseNameAdded(true);
						}else{
							validationMessagePanel.add(warningIcon);
							messageLabel.setText("Clause name already exists");
							validationMessagePanel.setStyleName("alertMessage");
							validationMessagePanel.add(messageLabel);
							dialogContents.insert(validationMessagePanel, 0);
							dialogBox.setPopupPosition(365, 215);
							dialogBox.show();
						}
					}
				} // end !isClauseNameAdded() 
			}
		});
		
		save.getElement().setId("save_Button");
		save.setEnabled(false);	

		//Add a Enter press event to the text box
		subTreeName.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
  				String clauseName = subTreeName.getText().trim();
				if(clauseName == null || "".equals(clauseName.trim()) || !Character.isLetter(clauseName.charAt(0))){
					subTreeName.setText("");
					save.setEnabled(false);
				}
				else{
					save.setEnabled(true);
				}
				subTreeName.setTitle(clauseName);
			    if (clauseName != null && ! clauseName.equals("") && event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) { 
			    	save.click();
			    }
			}
		});
		
		
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
		//dialogBox.center();
		dialogBox.setPopupPosition(365, 215);
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
	
	/**
	 * Checks if is clause name added.
	 *
	 * @return true, if is clause name added
	 */
	public static boolean isClauseNameAdded() {
		return isClauseNameAdded;
	}

	/**
	 * Sets the clause name added.
	 *
	 * @param isClauseNameAdded the new clause name added
	 */
	public static void setClauseNameAdded(boolean isClauseNameAdded) {
		EditSubTreeDialogBox.isClauseNameAdded = isClauseNameAdded;
	}
}
