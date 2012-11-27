package mat.client.admin;

import java.util.List;

import mat.client.shared.NameValuePair;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

public class OrganizationAutoComplete extends Composite implements HasValue<String> {
	private OrganizationSuggestOracle myOracle = new OrganizationSuggestOracle();
	private SuggestBox sBox;
	private String value;
	
	public OrganizationAutoComplete() {
		sBox = new SuggestBox(myOracle);
		initWidget(sBox);
		
		sBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				Suggestion suggestion = event.getSelectedItem();
				String value = suggestion.getDisplayString();
				for(NameValuePair nvp : myOracle.getOrganizationValues()) {
					if(nvp.getValue().equals(value)) {
						OrganizationAutoComplete.this.value = nvp.getName();
					}
				}
			}
		});
		
	}
	
	public void setOrganizationValues(List<NameValuePair> options) {
		myOracle.setOrganizationValues(options);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<String> handler) {
		throw new RuntimeException("Not Implemented");
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String value) {
		setValue(value, false);
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		for(NameValuePair nvp : myOracle.getOrganizationValues()) {
			if(nvp.getName().equals(value)) {
				this.value = nvp.getName();
				sBox.setValue(nvp.getValue());
			}
		}
		//
		// should throw event here
		//
	}
		
}
