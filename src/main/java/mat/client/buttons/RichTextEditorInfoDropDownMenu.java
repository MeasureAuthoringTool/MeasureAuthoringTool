package mat.client.buttons;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.DropDownMenu;

public class RichTextEditorInfoDropDownMenu extends DropDownMenu{

	public RichTextEditorInfoDropDownMenu() {
		super();
		
		AnchorListItem item1= new AnchorListItem("Ctrl-b: bold");
		item1.setTitle(item1.getText());
		item1.setHref("#");
		
		AnchorListItem item2= new AnchorListItem("Ctrl-i: italic");
		item2.setHref("#");
		item2.setTitle(item2.getText());
		
		AnchorListItem item3= new AnchorListItem("Ctrl-u: underline");
		item3.setHref("#");
		item3.setTitle(item3.getText());
		
		AnchorListItem item4= new AnchorListItem("Ctrl-shift-s: strikethrough");
		item4.setHref("#");
		item4.setTitle(item4.getText());
		
		AnchorListItem item5= new AnchorListItem("Ctrl-z: undo");
		item5.setHref("#");
		item5.setTitle(item5.getText());
		
		AnchorListItem item6= new AnchorListItem("Ctrl-y: redo");
		item6.setHref("#");
		item6.setTitle(item6.getText());
		
		AnchorListItem item7= new AnchorListItem("Ctrl-\\: remove font styling");
		item7.setHref("#");
		item7.setTitle(item7.getText());
		
		AnchorListItem item8 = new AnchorListItem("Ctrl-Shift-7: unordered list");
		item8.setHref("#");
		item8.setTitle(item8.getText());
		
		AnchorListItem item9= new AnchorListItem("Ctrl-Shift-8: ordered list");
		item9.setHref("#");
		item9.setTitle(item9.getText());
		
		AnchorListItem item10= new AnchorListItem("Ctrl-Shift-l: align left");
		item10.setHref("#");
		item10.setTitle(item10.getText());
		
		AnchorListItem item11= new AnchorListItem("Ctrl-Shift-e: align center");
		item11.setHref("#");
		item11.setTitle(item11.getText());
		
		AnchorListItem item12= new AnchorListItem("Ctrl-Shift-r: align right");
		item12.setHref("#");
		item12.setTitle(item12.getText());
		
		AnchorListItem item13= new AnchorListItem("Ctrl-Shift-j: justify full");
		item13.setHref("#");
		item13.setTitle(item13.getText());
		
		AnchorListItem item14= new AnchorListItem("Ctrl-[: outdent");
		item14.setHref("#");
		item14.setTitle(item14.getText());
		
		AnchorListItem item15= new AnchorListItem("Ctrl-]: indent");
		item15.setHref("#");
		item15.setTitle(item15.getText());
		
		AnchorListItem item16= new AnchorListItem("Ctrl-Shift-Tab: exit editor back");
		item16.setHref("#");
		item16.setTitle(item16.getText());
		
		AnchorListItem item17= new AnchorListItem("Ctrl-Tab: exit editor forward");
		item17.setHref("#");
		item17.setTitle(item17.getText());
		
		
		Anchor itemAnchor1 = (Anchor) (item1.getWidget(0));
		itemAnchor1.getElement().setAttribute("style", "cursor:text");
		itemAnchor1.getElement().setAttribute("aria-label", "Ctrl-b: bold");
		
		Anchor itemAnchor2 = (Anchor) (item2.getWidget(0));
		itemAnchor2.getElement().setAttribute("style", "cursor:text");
		itemAnchor2.getElement().setAttribute("aria-label", "Ctrl-i: italic");
		
		Anchor itemAnchor3 = (Anchor) (item3.getWidget(0));
		itemAnchor3.getElement().setAttribute("style", "cursor:text");
		itemAnchor3.getElement().setAttribute("aria-label", "Ctrl-u: underline");
		
		Anchor itemAnchor4 = (Anchor) (item4.getWidget(0));
		itemAnchor4.getElement().setAttribute("style", "cursor:text");
		itemAnchor4.getElement().setAttribute("aria-label", "Ctrl-shift-s: strikethrough");
		
		Anchor itemAnchor5 = (Anchor) (item5.getWidget(0));
		itemAnchor5.getElement().setAttribute("style", "cursor:text");
		itemAnchor5.getElement().setAttribute("aria-label", "Ctrl-z: undo");
		
		Anchor itemAnchor6 = (Anchor) (item6.getWidget(0));
		itemAnchor6.getElement().setAttribute("style", "cursor:text");
		itemAnchor6.getElement().setAttribute("aria-label", "Ctrl-y: redo");
		
		Anchor itemAnchor7 = (Anchor) (item7.getWidget(0));
		itemAnchor7.getElement().setAttribute("style", "cursor:text");
		itemAnchor7.getElement().setAttribute("aria-label", "Ctrl-\\: remove font styling");
		
		Anchor itemAnchor8 = (Anchor) (item8.getWidget(0));
		itemAnchor8.getElement().setAttribute("style", "cursor:text");
		itemAnchor8.getElement().setAttribute("aria-label", "Ctrl-Shift-7: unordered list");
		
		Anchor itemAnchor9 = (Anchor) (item9.getWidget(0));
		itemAnchor9.getElement().setAttribute("style", "cursor:text");
		itemAnchor9.getElement().setAttribute("aria-label", "Ctrl-Shift-8: ordered list");
		
		Anchor itemAnchor10 = (Anchor) (item10.getWidget(0));
		itemAnchor10.getElement().setAttribute("style", "cursor:text");
		itemAnchor10.getElement().setAttribute("aria-label", "Ctrl-Shift-l: align left");
		
		Anchor itemAnchor11 = (Anchor) (item11.getWidget(0));
		itemAnchor11.getElement().setAttribute("style", "cursor:text");
		itemAnchor11.getElement().setAttribute("aria-label", "Ctrl-Shift-e: align center");
		
		Anchor itemAnchor12 = (Anchor) (item12.getWidget(0));
		itemAnchor12.getElement().setAttribute("style", "cursor:text");
		itemAnchor12.getElement().setAttribute("aria-label", "Ctrl-Shift-r: align right");
		
		Anchor itemAnchor13 = (Anchor) (item13.getWidget(0));
		itemAnchor13.getElement().setAttribute("style", "cursor:text");
		itemAnchor13.getElement().setAttribute("aria-label", "Ctrl-Shift-j: justify full");
		
		Anchor itemAnchor14 = (Anchor) (item14.getWidget(0));
		itemAnchor14.getElement().setAttribute("style", "cursor:text");
		itemAnchor14.getElement().setAttribute("aria-label", "Ctrl-[: outdent");
		
		Anchor itemAnchor15 = (Anchor) (item15.getWidget(0));
		itemAnchor15.getElement().setAttribute("style", "cursor:text");
		itemAnchor15.getElement().setAttribute("aria-label", "Ctrl-]: indent");
		
		Anchor itemAnchor16 = (Anchor) (item16.getWidget(0));
		itemAnchor16.getElement().setAttribute("style", "cursor:text");
		itemAnchor16.getElement().setAttribute("aria-label", "Ctrl-Shift-Tab: exit editor back");
		
		Anchor itemAnchor17 = (Anchor) (item17.getWidget(0));
		itemAnchor17.getElement().setAttribute("style", "cursor:text");
		itemAnchor17.getElement().setAttribute("aria-label", "Ctrl-Tab: exit editor forward");
		
		super.setWidth("50px");
		super.getElement().setAttribute("style", "font-size:small;");
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
		super.add(item11);
		super.add(item12);
		super.add(item13);
		super.add(item14);
		super.add(item15);
		super.add(item16);
		super.add(item17);
	}
}
