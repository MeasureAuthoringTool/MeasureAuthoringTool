package mat.client.measure;

import mat.client.ImageResources;
import mat.client.shared.HorizontalFlowPanel;
import mat.client.shared.MatContext;
import mat.client.shared.search.SearchResults;
import mat.client.shared.search.SearchView;
import mat.client.util.ClientConstants;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class MeasureSearchView.
 * 
 * @author aschmidt
 */
public class MeasureSearchView extends SearchView<ManageMeasureSearchModel.Result>{

	/** The odd. */
	boolean odd = false;
	
	/** The add image. */
	boolean addImage = true;
	
	/**
	 * Instantiates a new measure search view.
	 * 
	 * @param string
	 *            the string
	 */
	public MeasureSearchView(String string) {
		super(string);
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
				ManageMeasureSearchModel.Result result = (ManageMeasureSearchModel.Result)results.get(i);
				String currentMid = result.getMeasureSetId();
				result = (ManageMeasureSearchModel.Result)results.get(i-1);
				String previousMid = result.getMeasureSetId();
				if(!currentMid.equalsIgnoreCase(previousMid)){
					odd = !odd;
					addImage = true;
					result.setTransferable(true);
				}else{
					addImage = false;
				}
			}else{
				odd = false;
				addImage = true;
			}
			if(addImage){
				((ManageMeasureSearchModel.Result)results.get(i)).setTransferable(true);
			}
				
			for(int j = 0; j < numColumns; j++) {
				if(results.isColumnFiresSelection(j)) {
					String innerText = results.getValue(i, j).getElement().getInnerText();
					innerText = addSpaces(innerText, 27);
					Widget a = null;
					final int rowIndex = i;
					String currentUserRole = MatContext.get().getLoggedInUserRole();
					if(!currentUserRole.equalsIgnoreCase(ClientConstants.ADMINISTRATOR)){
						Anchor anchor = new Anchor(innerText);
						addClickHandler(anchor, results, rowIndex);
						a = anchor;
					}else{
						Label label = new Label(innerText);
						a= label;
					}
					
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
	private String addSpaces(String in, int frequency){
		
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
	private Image createImage(final int rowIndex,final SearchResults results, String text){
		Image image = new Image(ImageResources.INSTANCE.application_cascade());
		image.setTitle(text);
		image.getElement().setAttribute("alt", text);
		image.setStylePrimaryName("measureSearchResultIcon");
		addClickHandler(image, results, rowIndex);
		return image;
	}
	
}
