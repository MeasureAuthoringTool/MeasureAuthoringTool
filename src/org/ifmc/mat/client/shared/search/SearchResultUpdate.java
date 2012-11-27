package org.ifmc.mat.client.shared.search;

import org.ifmc.mat.shared.StringUtility;

import com.google.gwt.user.client.ui.TextBox;

public class SearchResultUpdate {

	public void update(SearchResults<?> result, TextBox searchTextBox, String lastSearchText){
		if(result.getResultsTotal() == 0 && !lastSearchText.isEmpty()){
			String searchTitle = "No Records returned.";
			searchTextBox.setTitle(searchTitle);
		}else{
			StringUtility su = new StringUtility();
			if(!su.isEmptyOrNull(searchTextBox.getValue())){
				String searchTitle = result.getResultsTotal()+" Record(s) returned.";
				searchTextBox.setTitle(searchTitle);
			}
			else{
				String searchTitle = "No search string entered";
				searchTextBox.setTitle(searchTitle);
			}
		}
	}
	
}
