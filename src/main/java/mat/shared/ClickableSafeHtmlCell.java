package mat.shared;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class ClickableSafeHtmlCell extends AbstractCell<SafeHtml> {
	/**
	 * Construct a new ClickableSafeHtmlCell.
	 */
	public ClickableSafeHtmlCell() {
		super("click");
	}
	
	@Override
	public void onBrowserEvent(Context context, Element parent, SafeHtml value, NativeEvent event,
			ValueUpdater<SafeHtml> valueUpdater) {
		super.onBrowserEvent(context, parent, value, event, valueUpdater);
		if ("click".equals(event.getType())) {
			onEnterKeyDown(context, parent, value, event, valueUpdater);
		}
	}
	
	@Override
	protected void onEnterKeyDown(Context context, Element parent, SafeHtml value,
			NativeEvent event, ValueUpdater<SafeHtml> valueUpdater) {
		if (valueUpdater != null) {
			valueUpdater.update(value);
		}
	}
	
	@Override
	public void render(Context context, SafeHtml value, SafeHtmlBuilder sb) {
		if (value != null) {
			sb.append(value);
		}
	}
}
