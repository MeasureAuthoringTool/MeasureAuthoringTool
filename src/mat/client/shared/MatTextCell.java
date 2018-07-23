package mat.client.shared;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.AbstractSafeHtmlCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SimpleSafeHtmlRenderer;


public class MatTextCell extends AbstractSafeHtmlCell<String> {
	private String tooltip;
	private AbstractCell<String> cell;

	public MatTextCell(AbstractCell<String> cell, String tooltip) {
		super(SimpleSafeHtmlRenderer.getInstance(), BrowserEvents.CLICK, BrowserEvents.KEYDOWN);
		this.cell = cell;
		this.tooltip = tooltip;
	}

	@Override
	protected void render(Context context, SafeHtml data, SafeHtmlBuilder sb) {
		sb.appendHtmlConstant("<div title=\"" + tooltip + "\">");
		cell.render(context, data.asString(), sb);
		sb.appendHtmlConstant("</div>");
	}

	@Override
	public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context, Element parent, String value, NativeEvent event, ValueUpdater<String> valueUpdater)
	{
		cell.onBrowserEvent(context, parent, value, event, valueUpdater);
	}

}