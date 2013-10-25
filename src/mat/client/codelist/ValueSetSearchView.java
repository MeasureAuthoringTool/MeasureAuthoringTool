/**
 * 
 */
package mat.client.codelist;

import mat.client.ImageResources;
import mat.client.shared.HorizontalFlowPanel;
import mat.client.shared.search.SearchResults;
import mat.client.shared.search.SearchView;
import mat.model.CodeListSearchDTO;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * The Class ValueSetSearchView.
 * 
 * @author vandavar
 */
public class ValueSetSearchView extends SearchView<CodeListSearchDTO>{
	
	/** The odd. */
	boolean odd = false;
	
	/** The add image. */
	boolean addImage = true;
	
	/**
	 * Instantiates a new value set search view.
	 */
	public ValueSetSearchView() {
		super();
	}
	
	/**
	 * assumption made: results are sorted by the time they are given here.
	 * 
	 * @param numRows
	 *            the num rows
	 * @param numColumns
	 *            the num columns
	 * @param results
	 *            the results
	 */
	@Override
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
					
					Anchor a = new Anchor(innerText);
					final int rowIndex = i;
					addClickHandler(a, results, rowIndex);
					
					
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
	
	/**
	 * Adds the spaces.
	 * 
	 * @param in
	 *            the in
	 * @param frequency
	 *            the frequency
	 * @return the string
	 */
	public String addSpaces(String in, int frequency){
		
		if(in.length() <= frequency)
			return in;
		
		char[] inArr = in.toCharArray();
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for(char c : inArr){
			if(i == frequency){
				sb.append(' ');
				i = 0;
			}else if(c == ' ')
				i = 0;
			else
				i++;
			sb.append(c);
		}
			
		return sb.toString();
	}
	
	/**
	 * Creates the image.
	 * 
	 * @param rowIndex
	 *            the row index
	 * @param results
	 *            the results
	 * @param text
	 *            value to be assigned to the alt and title attributes of the
	 *            return image
	 * @return the image
	 */
	public Image createImage(final int rowIndex,final SearchResults results, String text){
		Image image = new Image(ImageResources.INSTANCE.application_cascade());
		image.setTitle(text);
		image.getElement().setAttribute("alt", text);
		image.setStylePrimaryName("measureSearchResultIcon");
		addClickHandler(image, results, rowIndex);
		return image;
	}

	/**
	 * Builds the data table.
	 * 
	 * @param results
	 *            the results
	 * @param isAscending
	 *            the is ascending
	 */
	public void buildDataTable(AdminCodeListSearchResultsAdapter results,
			boolean isAscending) {
			
	}
}
