package mat.client.shared;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ListBox;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class MultiListBoxMVP.
 */
public class MultiListBoxMVP extends ListBox implements HasValue<List<String>> {
	
	/**
	 * Instantiates a new multi list box mvp.
	 */
	public MultiListBoxMVP() {
		super(true);
		addChangeHandler( new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				ValueChangeEvent.fire(MultiListBoxMVP.this, getValue());
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.event.logical.shared.HasValueChangeHandlers#addValueChangeHandler(com.google.gwt.event.logical.shared.ValueChangeHandler)
	 */
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<List<String>> handler) {
		return super.addHandler(handler, ValueChangeEvent.getType());
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.HasValue#getValue()
	 */
	public List<String> getValue() {
		List<String> values = new ArrayList<String>();
		for(int i = 0; i < getItemCount(); i++) {
			if(isItemSelected(i)) {
				values.add(getValue(i));
			}
		}
		return values;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.HasValue#setValue(java.lang.Object)
	 */
	public void setValue(List<String> values) {
		setValue(values, false);
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.HasValue#setValue(java.lang.Object, boolean)
	 */
	public void setValue(List<String> values, boolean fireEvents) {
			for(int i = 0; i < getItemCount(); i++) {
				if(values.contains(getValue(i))) {
					setItemSelected(i, true);
				}
				else {
					setItemSelected(i, false);
				}
			}
		
		if(fireEvents) {
			ValueChangeEvent.fire(this, getValue());
		}
	}

	/**
	 * Sets the dropdown options.
	 * 
	 * @param options
	 *            the new dropdown options
	 */
	public void setDropdownOptions(List<NameValuePair> options) {
		clear();
		addItem("");
		for(NameValuePair p : options) {
			addItem(p.getValue(), p.getName());
		}
	}
}
