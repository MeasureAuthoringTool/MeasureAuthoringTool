package mat.client.shared;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;

public class CQLCopyPasteClearButtonToolBar {
	
	private Button copyButton = new Button();
	
	private Button pasteButton = new Button();
	
	private Button selectAllButton = new Button();
	
	private Button clearButton = new Button();
	
	private ButtonToolBar buttonToolBar = new ButtonToolBar();
	
	public CQLCopyPasteClearButtonToolBar(String sectionName){
		
		buttonToolBar.setId("copyPasteClear_"+sectionName);
		
		copyButton.setType(ButtonType.LINK);
		copyButton.getElement().setId("copyButton_"+sectionName);
		copyButton.setMarginTop(10);
		copyButton.setTitle("Copy");
		copyButton.setText("Copy");
		copyButton.setIcon(IconType.FILE_O);
		copyButton.setIconSize(IconSize.LARGE);
		copyButton.setColor("#0964A2");
		copyButton.setSize("70px", "30px");
		copyButton.getElement().setAttribute("aria-label", "Copy");
		
		pasteButton.setType(ButtonType.LINK);
		pasteButton.getElement().setId("pasteButton_"+sectionName);
		pasteButton.setMarginTop(10);
		pasteButton.setTitle("Paste");
		pasteButton.setText("Paste");
		pasteButton.setIcon(IconType.CLIPBOARD);
		pasteButton.setIconSize(IconSize.LARGE);
		pasteButton.setColor("#0964A2");
		pasteButton.setSize("70px", "30px");
		pasteButton.getElement().setAttribute("aria-label", "Paste");
		
		selectAllButton.setType(ButtonType.LINK);
		selectAllButton.getElement().setId("selectAllButton_"+sectionName);
		selectAllButton.setMarginTop(10);
		selectAllButton.setTitle("Select All");
		selectAllButton.setText("Select All");
		selectAllButton.setIcon(IconType.CHECK_SQUARE);
		selectAllButton.setIconSize(IconSize.LARGE);
		selectAllButton.setColor("#0964A2");
		selectAllButton.setSize("95px", "30px");
		selectAllButton.getElement().setAttribute("aria-label", "Select All");
		
		
		clearButton.setType(ButtonType.LINK);
		clearButton.getElement().setId("clearButton_"+sectionName);
		clearButton.setMarginTop(10);
		clearButton.setTitle("Clear");
		clearButton.setText("Clear");
		clearButton.setIcon(IconType.ERASER);
		clearButton.setIconSize(IconSize.LARGE);
		clearButton.setColor("#0964A2");
		clearButton.setSize("70px", "30px");
		clearButton.getElement().setAttribute("aria-label", "Clear");
		
		buttonToolBar.add(copyButton);
		buttonToolBar.add(pasteButton);
		buttonToolBar.add(selectAllButton);
		buttonToolBar.add(clearButton);
		buttonToolBar.getElement().setAttribute("style", "margin-left:585px;");
		
	}

	public Button getCopyButton() {
		return copyButton;
	}

	public Button getPasteButton() {
		return pasteButton;
	}
	
	public Button getSelectAllButton() {
		return selectAllButton;
	}

	public Button getClearButton() {
		return clearButton;
	}

	public ButtonToolBar getButtonToolBar() {
		return buttonToolBar;
	}

}
