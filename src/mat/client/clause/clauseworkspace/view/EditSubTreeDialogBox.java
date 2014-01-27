package mat.client.clause.clauseworkspace.view;

import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay;
import mat.client.shared.LabelBuilder;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class EditSubTreeDialogBox {
	/** The dialog box. */
	public static DialogBox dialogBox = new DialogBox(true,true);
	
	public static void showEditDialogBox(final XmlTreeDisplay xmlTreeDisplay, final CellTreeNode cellTreeNode) {
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		dialogBox.setText("Edit");
		dialogBox.setTitle("Edit");
		// Create a table to layout the content
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.setWidth("20em");
		dialogContents.setSpacing(5);
		dialogContents.getElement().setId("dialogContents_VerticalPanel");
		dialogBox.setWidget(dialogContents);
		final TextBox subTreeName = new TextBox();
		subTreeName.getElement().setId("quantity_TextBox");
		subTreeName.setWidth("150px");
		Label lableNewSubTreeName = (Label) LabelBuilder.buildLabel(subTreeName, "New SubTree Name");
		dialogContents.add(lableNewSubTreeName);
		dialogContents.setCellHorizontalAlignment(lableNewSubTreeName, HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents.add(subTreeName);
		dialogContents.setCellHorizontalAlignment(subTreeName, HasHorizontalAlignment.ALIGN_LEFT);
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
	}
}
