package mat.client.shared;

import com.google.gwt.dom.client.Document;
import org.gwtbootstrap3.client.ui.ListItem;
import org.gwtbootstrap3.client.ui.base.ComplexWidget;
import org.gwtbootstrap3.client.ui.base.helper.StyleHelper;
import org.gwtbootstrap3.client.ui.constants.Styles;

public class ListElement extends ComplexWidget {

    /**
     * Creates an empty list.
     */
    public ListElement() {
        setElement(Document.get().createLIElement());
    }

    /**
     * Creates a list and adds the given widgets.
     *
     * @param widgets widgets to be added
     */
    public ListElement(final ListItem... widgets) {
        this();

        // Add all the list items to the widget
        for (final ListItem li : widgets) {
            add(li);
        }
    }

    /**
     * Sets the UnorderedList to be unstyled
     *
     * @param unstyled boolean true/false to make unstyled
     */
    public void setUnstyled(final boolean unstyled) {
        setStyleName(Styles.UNSTYLED, unstyled);
    }

    /**
     * Returns a boolean of whether or not the UnorderedList is unstyled
     *
     * @return true/false for unstyled or not
     */
    public boolean isUnstyled() {
        return StyleHelper.containsStyle(Styles.UNSTYLED, getStyleName());
    }

    /**
     * Sets the UnorderedList to appear inline rather then stacked
     *
     * @param inline true/false for inline or not
     */
    public void setInline(final boolean inline) {
        StyleHelper.toggleStyleName(this, inline, Styles.LIST_INLINE);
    }

    /**
     * Returns a boolean of whether or not the UnorderedList in inline
     *
     * @return true/false for inline or not
     */
    public boolean isInline() {
        return StyleHelper.containsStyle(Styles.LIST_INLINE, getStyleName());
    }
}