package mat.client.buttons;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.DropDownMenu;

public class InfoDropDownMenu extends DropDownMenu{

	public InfoDropDownMenu() {
		super();
		
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
		
		super.setWidth("50px");
		super.getElement().setAttribute("style", "font-size:small;margin-left:13px");
		super.add(item1);
		super.add(item2);
		super.add(item3);
		super.add(item4);
		super.add(item5);
		super.add(item6);
		
		super.add(item7);
		super.add(item8);
		super.add(item9);
		super.add(item10);
	}
}
