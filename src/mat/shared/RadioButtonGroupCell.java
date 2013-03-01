package mat.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 
import com.google.gwt.cell.client.AbstractInputCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.client.SafeHtmlTemplates.Template;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class RadioButtonGroupCell extends AbstractInputCell<String, String> {
	 
    interface Template extends SafeHtmlTemplates {
            @Template("<input type=\"radio\" name=\"choices\" tabindex=\"-1\" value=\"{0}\" /> {0}</>")
            SafeHtml deselected(String choice);

            @Template("<input type=\"radio\" name=\"choices\" tabindex=\"-1\" checked=\"checked\" value=\"{0}\" /> {0}</>")
            SafeHtml selected(String choice);
    }

    private static Template template;

    private final HashMap<String, Integer> indexForOption = new HashMap<String, Integer>();

    private final List<String> options;

    /**
     * Construct a new RadioButtonGroupCell with the specified options.
     *
     * @param options
     *            the options in the cell
     */
    public RadioButtonGroupCell(List<String> options) {
            super("change");
            if (template == null) {
                    template = GWT.create(Template.class);
            }
            this.options = new ArrayList<String>(options);
            int index = 0;
            for (String option : options) {
                    indexForOption.put(option, index++);
            }

    }

    @Override
    public void onBrowserEvent(Context context, Element parent, String value, NativeEvent event, ValueUpdater<String> valueUpdater) {
            super.onBrowserEvent(context, parent, value, event, valueUpdater);

            String type = event.getType();

            if ("change".equals(type)) {

                    Object key = context.getKey();
                    InputElement select;
                    NodeList<Node> nodeList = parent.getChildNodes();
                    int nodesNum = nodeList.getLength();
                    String newValue = null;

                    for (int i = 0; i < nodesNum; i++) {
                            select = parent.getChild(i).cast();
                            if (select.isChecked()) {
                                    newValue = select.getPropertyString("value");
                                    break;
                            }
                    }

                    setViewData(key, newValue);
                    finishEditing(parent, newValue, key, valueUpdater);
                    if (valueUpdater != null) {
                            valueUpdater.update(newValue);
                    }
            }
    }

    @Override
    public void render(Context context, String value, SafeHtmlBuilder sb) {
            // Get the view data.
            Object key = context.getKey();
            String viewData = getViewData(key);
            if (viewData != null && viewData.equals(value)) {
                    clearViewData(key);
                    viewData = null;
            }

            int selectedIndex = getSelectedIndex(viewData == null ? value : viewData);

            int index = 0;
            for (String option : options) {
                    if (index++ == selectedIndex) {
                            sb.append(template.selected(option));
                    } else {
                            sb.append(template.deselected(option));
                    }
            }
    }

    private int getSelectedIndex(String value) {
            Integer index = indexForOption.get(value);
            if (index == null) {
                    return -1;
            }
            return index.intValue();
    }
}
