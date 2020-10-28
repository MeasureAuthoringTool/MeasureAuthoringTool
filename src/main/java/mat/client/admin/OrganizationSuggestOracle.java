package mat.client.admin;

import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle;
import mat.client.shared.NameValuePair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The Class OrganizationSuggestOracle.
 */
public class OrganizationSuggestOracle extends SuggestOracle {
	
	/** The options. */
	private List<NameValuePair> options = new ArrayList<NameValuePair>();

	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.SuggestOracle#requestSuggestions(com.google.gwt.user.client.ui.SuggestOracle.Request, com.google.gwt.user.client.ui.SuggestOracle.Callback)
	 */
	@Override
	public void requestSuggestions(final Request request, final Callback callback) {
		final int limit = request.getLimit();
		final String query = request.getQuery();

		Collection<SuggestOracle.Suggestion> suggestions =
			new ArrayList<SuggestOracle.Suggestion>();
		for (int i = 0; i < options.size() && suggestions.size() < limit; i++) {
			if (options.get(i).getValue().toUpperCase().startsWith(query.toUpperCase())) {
				suggestions.add(new MultiWordSuggestOracle
						.MultiWordSuggestion(options.get(i).getValue(), options.get(i).getValue()));
			}
		}

	Response response = new Response(suggestions);
	callback.onSuggestionsReady(request, response);
	}

	/**
	 * Sets the organization values.
	 * 
	 * @param options
	 *            the new organization values
	 */
	public void setOrganizationValues(List<NameValuePair> options) {
		this.options = options;
	}
	
	/**
	 * Gets the organization values.
	 * 
	 * @return the organization values
	 */
	public List<NameValuePair> getOrganizationValues() {
		return options;
	}
}
