package mat.client.admin;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import mat.client.shared.NameValuePair;

import java.util.List;

/**
 * The Class OrganizationAutoComplete.
 */
public class OrganizationAutoComplete extends Composite implements HasValue<String> {
	
	/** The my oracle. */
	private OrganizationSuggestOracle myOracle = new OrganizationSuggestOracle();
	
	/** The s box. */
	private SuggestBox sBox;
	
	/** The value. */
	private String value;

	/**
	 * Instantiates a new organization auto complete.
	 */
	public OrganizationAutoComplete() {
		sBox = new SuggestBox(myOracle);
		initWidget(sBox);

		sBox.addSelectionHandler(event -> {
            Suggestion suggestion = event.getSelectedItem();
            String value = suggestion.getDisplayString();
            for (NameValuePair nvp : myOracle.getOrganizationValues()) {
                if (nvp.getValue().equals(value)) {
                    OrganizationAutoComplete.this.value = nvp.getName();
                }
            }
        });
	}

	/**
	 * Sets the organization values.
	 * 
	 * @param options
	 *            the new organization values
	 */
	public void setOrganizationValues(List<NameValuePair> options) {
		myOracle.setOrganizationValues(options);
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.logical.shared.HasValueChangeHandlers#addValueChangeHandler(com.google.gwt.event.logical.shared.ValueChangeHandler)
	 */
	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<String> handler) {
		throw new RuntimeException("Not Implemented");
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.HasValue#getValue()
	 */
	@Override
	public String getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.HasValue#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(String value) {
		setValue(value, false);
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.HasValue#setValue(java.lang.Object, boolean)
	 */
	@Override
	public void setValue(String value, boolean fireEvents) {
		for (NameValuePair nvp : myOracle.getOrganizationValues()) {
			if (nvp.getName().equals(value)) {
				this.value = nvp.getName();
				sBox.setValue(nvp.getValue());
			}
		}
		//
		// should throw event here
		//
	}
}
