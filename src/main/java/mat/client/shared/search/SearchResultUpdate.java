package mat.client.shared.search;

import com.google.gwt.user.client.ui.TextBox;
import mat.client.measure.service.SaveCQLLibraryResult;
import mat.shared.StringUtility;

/**
 * The Class SearchResultUpdate.
 */
public class SearchResultUpdate {

	/**
	 * Update.
	 * 
	 * @param result
	 *            the result
	 * @param searchTextBox
	 *            the search text box
	 * @param lastSearchText
	 *            the last search text
	 */
	public void update(SearchResults<?> result, TextBox searchTextBox, String lastSearchText){
		// TODO this cannot be updated dynamically for 508 reasons
		
		String searchTitle = null;
		if(result.getResultsTotal() == 0 && !lastSearchText.isEmpty()){
			searchTitle = "No Records returned.";
		}else{
			if(!StringUtility.isEmptyOrNull(searchTextBox.getValue())){
				String trimmedSearchString = searchTextBox.getValue().trim();
				searchTitle = result.getResultsTotal()+" Record(s) returned for search string "+ trimmedSearchString+".";
				searchTextBox.setText(trimmedSearchString);
			}
			else{
				searchTitle = "No search string entered";
			}
		}
		searchTextBox.setTitle(searchTitle);
	}
	
	
	public void update(SaveCQLLibraryResult result, TextBox searchTextBox, String lastSearchText){
		String searchTitle = null;
		if(result.getResultsTotal() == 0 && !lastSearchText.isEmpty()){
			searchTitle = "No Records returned.";
		}else{
			if(!StringUtility.isEmptyOrNull(searchTextBox.getValue())){
				String trimmedSearchString = searchTextBox.getValue().trim();
				searchTitle = result.getResultsTotal()+" Record(s) returned for search string "+ trimmedSearchString+".";
				searchTextBox.setText(trimmedSearchString);
			}
			else{
				searchTitle = "No search string entered";
			}
		}
		searchTextBox.setTitle(searchTitle);
	}
	
}
