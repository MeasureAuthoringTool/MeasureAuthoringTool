package mat.client.shared.search;

import mat.client.measure.service.SaveCQLLibraryResult;
import mat.shared.StringUtility;

import com.google.gwt.user.client.ui.TextBox;

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
		if(result.getResultsTotal() == 0 && !lastSearchText.isEmpty()){
			String searchTitle = "No Records returned.";
			searchTextBox.setTitle(searchTitle);
		}else{
			StringUtility su = new StringUtility();
			if(!su.isEmptyOrNull(searchTextBox.getValue())){
				String trimmedSearchString = searchTextBox.getValue().trim();
				String searchTitle = result.getResultsTotal()+" Record(s) returned for search string "+ trimmedSearchString+".";
				searchTextBox.setTitle(searchTitle);
				searchTextBox.setText(trimmedSearchString);
			}
			else{
				String searchTitle = "No search string entered";
				searchTextBox.setTitle(searchTitle);
			}
		}
	}
	
	
	public void update(SaveCQLLibraryResult result, TextBox searchTextBox, String lastSearchText){
		if(result.getResultsTotal() == 0 && !lastSearchText.isEmpty()){
			String searchTitle = "No Records returned.";
			searchTextBox.setTitle(searchTitle);
		}else{
			StringUtility su = new StringUtility();
			if(!su.isEmptyOrNull(searchTextBox.getValue())){
				String trimmedSearchString = searchTextBox.getValue().trim();
				String searchTitle = result.getResultsTotal()+" Record(s) returned for search string "+ trimmedSearchString+".";
				searchTextBox.setTitle(searchTitle);
				searchTextBox.setText(trimmedSearchString);
			}
			else{
				String searchTitle = "No search string entered";
				searchTextBox.setTitle(searchTitle);
			}
		}
	}
	
}
