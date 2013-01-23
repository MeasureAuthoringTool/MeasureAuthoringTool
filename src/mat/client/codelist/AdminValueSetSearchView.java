package mat.client.codelist;

import mat.client.shared.HorizontalFlowPanel;
import mat.client.shared.search.SearchResults;
import mat.model.CodeListSearchDTO;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;

public class AdminValueSetSearchView extends ValueSetSearchView{

	protected void buildSearchResults(int numRows,int numColumns,final SearchResults results){
		
		for(int i = 0; i < numRows; i++) {
			
			if(i > 0){
				CodeListSearchDTO result = (CodeListSearchDTO)results.get(i);
				String currentValueSetFamily = result.getOid();
				result = (CodeListSearchDTO)results.get(i-1);
				String previousValueSetFamily = result.getOid();
				if(!currentValueSetFamily.equalsIgnoreCase(previousValueSetFamily)){
					odd = !odd;
					addImage = true;
				}else{
					addImage = false;
				}
			}else{
				odd = false;
				addImage = true;
			}
			
			for(int j = 0; j < numColumns; j++) {
				if(results.isColumnFiresSelection(j)) {
					String innerText = results.getValue(i, j).getElement().getInnerText();
					innerText = addSpaces(innerText, 27);
					Label a = new Label();
					a.setText(innerText);
					final int rowIndex = i;
					Panel holder = new HorizontalFlowPanel();
					SimplePanel innerPanel = new SimplePanel();
					if(addImage){
						
						innerPanel.setStylePrimaryName("pad-right5px");
						Image image = createImage(rowIndex, results, innerText);
						innerPanel.add(image);
						holder.add(innerPanel);
						holder.add(a);
					}else{
						innerPanel.setStylePrimaryName("pad-left21px");
						innerPanel.add(a);
						holder.add(innerPanel);
					}
					
					dataTable.setWidget(i+1, j, holder);
				}
				else {
					dataTable.setWidget(i+1, j,results.getValue(i, j));
				}
			}
			if(odd){
				dataTable.getRowFormatter().addStyleName(i + 1, "odd");
			}else{
				//if already set to 'odd' and we are just refreshing, then 'odd' has to be removed
				dataTable.getRowFormatter().removeStyleName(i + 1, "odd");
			}
		}
	}
}
