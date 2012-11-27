package mat.client.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mat.client.shared.NameValuePair;

import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle;

public class OrganizationSuggestOracle extends SuggestOracle {
	private List<NameValuePair> options = new ArrayList<NameValuePair>();
	
	@Override
	public void requestSuggestions(final Request request, final Callback callback) {
		final int limit = request.getLimit();
		final String query = request.getQuery();

		Collection<SuggestOracle.Suggestion> suggestions = 
			new ArrayList<SuggestOracle.Suggestion>();
		for(int i = 0; i < options.size() && suggestions.size() < limit; i++) {
			if(options.get(i).getValue().toUpperCase().startsWith(query.toUpperCase())) {
				suggestions.add(new MultiWordSuggestOracle.MultiWordSuggestion(options.get(i).getValue(), options.get(i).getValue()));
			}
		}
		
	Response response = new Response(suggestions);
	callback.onSuggestionsReady(request, response);
	}

	public void setOrganizationValues(List<NameValuePair> options) {
		this.options = options;
	}
	public List<NameValuePair> getOrganizationValues() {
		return options;
	}

}
