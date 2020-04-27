package mat.client;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HorizontalPanel;
import org.gwtbootstrap3.client.ui.AnchorButton;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.DropDownMenu;
import org.gwtbootstrap3.client.ui.ListDropDown;
import org.gwtbootstrap3.client.ui.ListItem;
import org.gwtbootstrap3.client.ui.Navbar;
import org.gwtbootstrap3.client.ui.NavbarCollapse;
import org.gwtbootstrap3.client.ui.NavbarNav;
import org.gwtbootstrap3.client.ui.constants.IconPosition;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Styles;
import org.gwtbootstrap3.client.ui.constants.Toggle;

public class HarpLayout {

    private HorizontalPanel linksPanel = new HorizontalPanel();

    private static ListItem signedInAsName = new ListItem();

    private AnchorListItem signOut = new AnchorListItem("Sign Out");

    public void buildLinksPanel() {
        linksPanel.add(buildProfileMenu());
        linksPanel.setStyleName("navLinksBanner", true);
    }

    private NavbarCollapse buildProfileMenu() {
        NavbarCollapse collapse = new NavbarCollapse();
        NavbarNav nav = new NavbarNav();

        ListDropDown ldd = new ListDropDown();
        AnchorButton icon = buildProfileIcon();
        DropDownMenu ddm = buildDropDownMenu();

        ldd.add(icon);
        ldd.add(ddm);

        nav.add(ldd);

        collapse.add(nav);
        return collapse;
    }

    private AnchorButton buildProfileIcon() {
        AnchorButton ab = new AnchorButton();

        ab.setIcon(IconType.USER_CIRCLE_O);
        ab.setIconSize(IconSize.TIMES2);
        ab.setIconPosition(IconPosition.RIGHT);
        ab.setDataToggle(Toggle.DROPDOWN);
        ab.setToggleCaret(false);
        ab.setStyleName(Styles.DROPDOWN_TOGGLE);
        ab.setActive(true);
        ab.setTitle("Profile");
        ab.setId("userprofile");
        ab.setDataTarget(Styles.NAVBAR_COLLAPSE);

        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        sb.appendHtmlConstant("<span style=\"font-size:0px;\" tabindex=\"0\">Profile</span>");
        ab.getElement().setInnerSafeHtml(sb.toSafeHtml());

        return ab;
    }

    private DropDownMenu buildDropDownMenu() {
        DropDownMenu ddm = new DropDownMenu();

        setAccessibilityForLinks();

        ddm.add(buildSignedInAs());
        ddm.add(signedInAsName);
        ddm.add(buildDivider());
        ddm.add(signOut);
        ddm.setStyleName(Styles.DROPDOWN_MENU);
        ddm.addStyleDependentName(Styles.RIGHT);

        return ddm;
    }

    private void setAccessibilityForLinks() {
        signOut.setStyleName(Styles.DROPDOWN);
        signOut.getWidget(0).setTitle("Sign Out");
    }

    private ListItem buildSignedInAs() {
        ListItem li = new ListItem();
        li.setText("Signed in as");
        li.setTitle("Signed in as");
        li.getElement().setTabIndex(0);
        li.setStyleName("profileText", true);
        return li;
    }

    private Navbar buildDivider() {
        Navbar divider = new Navbar();
        divider.setStyleName(Styles.DIVIDER);
        divider.setWidth("100%");
        return divider;
    }

    public static void setSignedInName(String name) {
        signedInAsName.setText(name);
        signedInAsName.setTitle(name);
        signedInAsName.setStyleName("labelStyling", true);
        signedInAsName.setStyleName("profileText", true);
        signedInAsName.getElement().setTabIndex(0);
    }

    public AnchorListItem getSignOut() {
        return signOut;
    }

}
