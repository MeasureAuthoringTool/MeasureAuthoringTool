package mat.client.shared;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import org.gwtbootstrap3.client.ui.ListBox;

import java.util.List;


/**
 * The Class ListBoxMVP.
 */
public class ListBoxMVP extends ListBox implements HasValue<String> {

    /*
     * adding field doTrimDisplay
     * we may want a ListBoxMVP that does no trimming of its option values
     */
    /**
     * The do trim display.
     */
    private boolean doTrimDisplay = true;

    /**
     * The max width.
     */
    private int maxWidth = 65;

    /**
     * Gets the max width.
     *
     * @return the max width
     */
    public int getMaxWidth() {
        return maxWidth;
    }

    /**
     * Sets the max width.
     *
     * @param maxWidth the new max width
     */
    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }


    /**
     * Instantiates a new list box mvp.
     *
     * @param doTrimDisplay the do trim display
     * @param visibleCount  the visible count
     */
    public ListBoxMVP(boolean doTrimDisplay, int visibleCount) {
        this(doTrimDisplay);
        this.setVisibleItemCount(visibleCount);
    }


    /**
     * Instantiates a new list box mvp.
     *
     * @param doTrimDisplay the do trim display
     */
    public ListBoxMVP(boolean doTrimDisplay) {
        this();
        this.doTrimDisplay = doTrimDisplay;
    }

    /**
     * Instantiates a new list box mvp.
     */
    public ListBoxMVP() {
        super();
        addChangeHandler(new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                ValueChangeEvent.fire(ListBoxMVP.this, getValue(getSelectedIndex()));
            }
        });

    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.logical.shared.HasValueChangeHandlers#addValueChangeHandler(com.google.gwt.event.logical.shared.ValueChangeHandler)
     */
    public HandlerRegistration addValueChangeHandler(
            ValueChangeHandler<String> handler) {
        return super.addHandler(handler, ValueChangeEvent.getType());
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.ui.HasValue#getValue()
     */
    public String getValue() {
        int index = getSelectedIndex();
        if (index >= 0) {
            return getValue(index);
        } else {
            return null;
        }
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.ui.ListBox#insertItem(java.lang.String, java.lang.String, int)
     */
    @Override
    public void insertItem(String item, String value, int index) {
        insertItem(item, value, value, index);
    }

    /**
     * Insert item.
     *
     * @param item  the item
     * @param value the value
     * @param title the title
     * @param index the index
     */
    public void insertItem(String item, String value, String title, int index) {
        insertItem(item, value, title, index, false);
    }

    /**
     * Insert item.
     *
     * @param item       the item
     * @param value      the value
     * @param title      the title
     * @param index      the index
     * @param isValueSet the is value set
     */
    public void insertItem(String item, String value, String title, int index, boolean isValueSet) {
        if (this.doTrimDisplay) {
            if (!isValueSet && !item.equalsIgnoreCase("--Select--") && item.contains(":") && item.contains("-")) {
                //do this only for the QDM dropdown.
                String stripoffOID = MatContext.get().stripOffOID(item);
                item = stripoffOID;
            }
            SelectElement select = getElement().cast();
            OptionElement option = Document.get().createOptionElement();
            if (item.length() > this.maxWidth) {
                String shortText = item.substring(0, maxWidth);
                option.setText(shortText);
            } else {
                option.setText(item);
            }
            option.setValue(value);
            option.setTitle(title);

            if ((index == -1) || (index == select.getLength())) {
                select.add(option, null);
            } else {
                OptionElement before = select.getOptions().getItem(index);
                select.add(option, before);
            }
        } else {
            super.insertItem(item, value, index);
        }
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.ui.HasValue#setValue(java.lang.Object)
     */
    public void setValue(String value) {
        setValue(value, false);
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.ui.HasValue#setValue(java.lang.Object, boolean)
     */
    public void setValue(String value, boolean fireEvents) {
        boolean found = false;
        for (int i = 0; i < getItemCount(); i++) {
            if (getValue(i).equals(value)) {
                setItemSelected(i, true);
                found = true;
            } else {
                setItemSelected(i, false);
            }
        }

        if (found && fireEvents) {
            ValueChangeEvent.fire(this, value);
        }
    }

    /**
     * Sets the value metadata.
     *
     * @param value the new value metadata
     */
    public void setValueMetadata(String value) {
        setSelectedIndex(0);
        if (value == null) {
            value = "";
        }
        for (int i = 0; i < getItemCount(); i++) {
            if (getItemText(i).equalsIgnoreCase(value)) {
                setSelectedIndex(i);
                break;
            }
        }
    }

    /**
     * Sets the dropdown options.
     *
     * @param optionsArg the new dropdown options
     */
    public void setDropdownOptions(List<NameValuePair> optionsArg) {
        clear();
        for (NameValuePair p : optionsArg) {
            addItem(p.getValue(), p.getName());
        }
    }

    public void setDropdownOptions(List<NameValuePair> optionsArg, boolean addPleaseSelect) {
        clear();
        if (addPleaseSelect) {
            addItem("--Select--");
        }
        for (NameValuePair p : optionsArg) {
            addItem(p.getValue(), p.getName());
        }
    }
    /*
     * when value and item text are not the same thing (ex. one is a number and one is a String),
     * we cannot always rely on the trimmed item text to be the value we want to process
     */

    /**
     * Gets the item title.
     *
     * @param index the index
     * @return the item title
     */
    public String getItemTitle(int index) {
        if (index >= 0) {
            SelectElement select = getElement().cast();
            OptionElement element = select.getOptions().getItem(index);
            return element.getTitle();
        } else {
            return null;
        }
    }

    /**
     * Insert item.
     *
     * @param value the value
     * @param title the title
     */
    public void insertItem(String value, String title) {
        insertItem(title, value, title);
    }

    /**
     * Insert item.
     *
     * @param text  the text
     * @param value the value
     * @param title the title
     */
    public void insertItem(String text, String value, String title) {
        SelectElement select = getElement().cast();
        OptionElement option = Document.get().createOptionElement();

        option.setText(text);
        option.setValue(value);
        option.setTitle(title);

        select.add(option, null);
    }

    public void setDoTrimDisplay(boolean doTrimDisplay) {
        this.doTrimDisplay = doTrimDisplay;
    }
}
