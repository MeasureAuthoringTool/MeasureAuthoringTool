package mat.client.shared;

import com.google.gwt.user.client.ui.SuggestOracle;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class CQLSuggestOracle extends SuggestOracle {

        private Collection<String> data;

        public CQLSuggestOracle(Collection<String> data) {
            this.data = data;
        }

        @Override
        public void requestSuggestions(final Request request, final Callback callback) {
            String userInput = request.getQuery();
            List<Suggestion> suggestions = new LinkedList<Suggestion>();
            for (final String s : data) {
                if (s.startsWith(userInput) || s.toLowerCase().contains(userInput.toLowerCase())) {
                    suggestions.add(new Suggestion() {
                        @Override
                        public String getReplacementString() {
                            return s;
                        }

                        @Override
                        public String getDisplayString() {
                          return s;
                        }
                    });
                }
            }
            Response response = new Response(suggestions);
            callback.onSuggestionsReady(request, response);
        }
        
        public void setData(Collection<String> data) {
        	this.data = data;
        }
    }
