package mat.client.buttons;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;

/**
 * This is the class that creates a button tool bar
 * with buttons (in order) 'Copy', 'Paste', 'Select All',
 * 'Clear'.
 */
public class CodesValuesetsButtonToolBar {
	
	private ButtonToolBar buttonBar = new ButtonToolBar();
	private Button copyButton, pasteButton, selectAllButton, clearButton;
	private String sectionName;
	
	
	public CodesValuesetsButtonToolBar(String sectionName) {
		this.sectionName = sectionName;
		buttonBar.getElement().setAttribute("style", "margin-left:580px");
		buttonBar.setId("copyPasteClear_"+sectionName);
		addCopyButton();
		addPasteButton();
		addSelectAllButton();
		addClearButton();
	}
	
	private void addCopyButton() {
		copyButton = new CopyToolBarButton(sectionName);
		buttonBar.add(copyButton);
	}
	
	private void addPasteButton() {
		pasteButton = new PasteToolBarButton(sectionName);
		buttonBar.add(pasteButton);
	}
	
	private void addSelectAllButton() {
		selectAllButton = new SelectAllToolBarButton(sectionName);
		buttonBar.add(selectAllButton);
	}
	
	private void addClearButton() {
		clearButton = new ClearToolBarButton(sectionName);
		buttonBar.add(clearButton);
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
		return buttonBar;
	}


}