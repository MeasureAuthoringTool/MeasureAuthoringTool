package mat.client.buttons;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;

/**
 * This is the class that creates a button tool bar
 * with buttons (in order) 'Copy', 'Paste', 'Select All',
 * 'Clear'.
 */
public class Codes_ValuesetsButtonToolBar {
	
	private ButtonToolBar buttonBar = new ButtonToolBar();
	private Button copyButton, pasteButton, selectAllButton, clearButton;
	private String sectionName;
	
	
	public Codes_ValuesetsButtonToolBar(String sectionName) {
		this.sectionName = sectionName;
		buttonBar.getElement().setAttribute("style", "margin-left:585px");
		buttonBar.setId("copyPasteClear_"+sectionName);
		addCopyButton();
		addPasteButton();
		addSelectAllButton();
		addClearButton();
	}
	
	private void addCopyButton() {
		copyButton = new CopyToolBarButton(sectionName).getButton();
		buttonBar.add(copyButton);
	}
	
	private void addPasteButton() {
		pasteButton = new PasteToolBarButton(sectionName).getButton();
		buttonBar.add(pasteButton);
	}
	
	private void addSelectAllButton() {
		selectAllButton = new SelectAllToolBarButton(sectionName).getButton();
		buttonBar.add(selectAllButton);
	}
	
	private void addClearButton() {
		clearButton = new ClearToolBarButton(sectionName).getButton();
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
