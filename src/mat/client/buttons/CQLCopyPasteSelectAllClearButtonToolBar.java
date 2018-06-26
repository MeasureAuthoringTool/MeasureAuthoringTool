package mat.client.buttons;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;

/**
 * This is the class that creates a button tool bar
 * with buttons (in order) 'Copy', 'Paste', 'Select All',
 * 'Clear'.
 */
public class CQLCopyPasteSelectAllClearButtonToolBar {
	
	ButtonToolBar buttonBar = new ButtonToolBar();
	GenericToolbarButton copy, paste, clear, selectAll;
	private String sectionName;
	
	
	public CQLCopyPasteSelectAllClearButtonToolBar(String sectionName) {
		this.sectionName = sectionName;
		buttonBar.getElement().setAttribute("style", "margin-left:585px");
		buttonBar.setId("copyPasteClear_"+sectionName);
		addCopyButton();
		addPasteButton();
		addSelectAllButton();
		addClearButton();
	}
	
	private void addCopyButton() {
		copy = new CopyToolBarButton(sectionName);
		buttonBar.add(copy);
	}
	
	private void addPasteButton() {
		paste = new PasteToolBarButton(sectionName);
		buttonBar.add(paste);
	}
	
	private void addSelectAllButton() {
		selectAll = new SelectAllToolBarButton(sectionName);
		buttonBar.add(selectAll);
	}
	
	private void addClearButton() {
		clear = new ClearToolBarButton(sectionName);
		buttonBar.add(clear);
	}
	
	public Button getCopyButton() {
		return copy;
	}
	
	public Button getPasteButton() {
		return paste;
	}
	
	public Button getSelectAllButton() {
		return selectAll;
	}
	
	public Button getClearButton() {
		return clear;
	}
	
	public ButtonToolBar getButtonToolBar() {
		return buttonBar;
	}


}
