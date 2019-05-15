package mat.client.buttons;

import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.DropDownMenu;

public class InfoDropDownMenu extends DropDownMenu{

	public InfoDropDownMenu() {
		super();
		
		super.setWidth("50px");
		super.getElement().setAttribute("style", "font-size:small;margin-left:13px");
		
		super.add(createAnchor("Ctrl-Alt-a: attributes"));
		super.add(createAnchor("Ctrl-Alt-y: datatypes"));
		super.add(createAnchor("Ctrl-Alt-d: definitions"));
		super.add(createAnchor("Ctrl-Alt-f: functions"));
		super.add(createAnchor("Ctrl-Alt-k: keywords"));
		super.add(createAnchor("Ctrl-Alt-p: parameters"));
		super.add(createAnchor("Ctrl-Alt-t: timings"));
		super.add(createAnchor("Ctrl-Alt-u: units"));
		super.add(createAnchor("Ctrl-Alt-v: value sets & codes"));
		super.add(createAnchor("Ctrl-Space: all"));
		super.add(createAnchor("Ctrl-f: Find/Replace"));
		super.add(createAnchor("Shift-up arrow: Exit editor to previous item"));
		super.add(createAnchor("Shift-down arrow: Exit editor to next item"));
	}

	private AnchorListItem createAnchor(String name) {
		final AnchorListItem item= new AnchorListItem(name);
		item.setTitle(name);
		item.setHref("#");
		return item;
	}
}
