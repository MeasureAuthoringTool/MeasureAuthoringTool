package mat.shared;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class SafeHtmlCell extends AbstractCell<SafeHtml> {
    /**
     * Construct a new ClickableSafeHtmlCell.
     */
    public SafeHtmlCell() {
        super();
    }


    @Override
    public void render(Context context, SafeHtml value, SafeHtmlBuilder sb) {
        if (value != null) {
            sb.append(value);
        }
    }
}
