package mat.client.measure.metadata;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.DOM;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class Grid508.
 * 
 * @author skarunakaran
 */
public class Grid508 extends Grid{

	/** The scope. */
	private final String SCOPE = "scope";
	
	/** The row. */
	private final String ROW = "row";
	
	/** The col. */
	private final String COL = "col";
	
	/**
	 * Instantiates a new grid508.
	 */
	public Grid508(){
		super();
	}
	
	
	/**
	 * Instantiates a new grid508.
	 * 
	 * @param rows
	 *            the rows
	 * @param columns
	 *            the columns
	 */
	public Grid508(int rows, int columns){
		  super(rows,columns);
    }
	
/**
 * *************************************************** from Grid.java
 * ***************************************************
 * 
 * @param table
 *            the table
 * @param rows
 *            the rows
 * @param columns
 *            the columns
 */
	  /**
	   * Method to add rows into a table with a given number of columns where 
	   * the first row is a header row <th>. 
	   * NOTE: attribute scope is added to each <th> and to the first <td> of each row 
	   * @param table the table element
	   * @param rows number of rows to add
	   * @param columns the number of columns per row
	   */
	  private void addRows(Element table, int rows, int columns){
	     Element row = DOM.createTR();
	     Element hrow = DOM.createTR();
	     for(int cellNum = 0; cellNum < columns; cellNum++) {
	       Element cell = createCell();
	       if(cellNum==0)
	    	   cell.setAttribute(SCOPE, ROW);
	       row.appendChild(cell);
	       Element hcell = createHeaderCell();
	       hrow.appendChild(hcell);
	     }
	     //do not add header row if it already exists
	     int rowNum = 0;
	     if(table.getChildCount()==0){
	    	 table.appendChild(hrow);
	    	 rowNum = 1;
	     }
	     for(; rowNum < rows; rowNum++) {  
	       table.appendChild(row.cloneNode(true));
	     }
	   };
	  
	  /**
	 * Resizes the grid to the specified number of rows. ensure the following:
	 * <table>
	 * <tr>
	 * <th scope="col">...</th>...
	 * </tr>
	 * <tr>
	 * <td scope="row">...</td>
	 * <td>...</td>
	 * ...
	 * </tr>
	 * </table>
	 * 
	 * @param rows
	 *            the number of rows
	 */
	  @Override
	  public void resizeRows(int rows) {
	    if (numRows == rows) {
	      return;
	    }
	    if (rows < 0) {
	      throw new IndexOutOfBoundsException("Cannot set number of rows to "
	          + rows);
	    }
	    if (numRows < rows) {
	      addRows(getBodyElement(), rows - numRows, numColumns);
	      numRows = rows;
	    } else {
	      while (numRows > rows) {
	        // Fewer rows. Remove extraneous ones.
	        removeRow(numRows - 1);
	      }
	    }
	  }
	  
/**
 * *************************************************** from HTMLTable.java
 * ***************************************************
 * 
 * @return the element
 */
/*	  
 	//consider overriding to use header cells where appropriate
	@Override
	  protected void insertCell(int row, int column) {
	    Element tr = DOM.getChild(getBodyElement(), row);
	    Element tc = null;
	    if(row == 0)
	    	tc= createHeaderCell();
	    else
	    	tc= createCell();
	    DOM.insertChild(tr, tc, column);
	  }
*/  
/*****************************************************
 * for GridWithTableHeaders.java
 *****************************************************/
  
	  protected Element createHeaderCell(){
		Element th = DOM.createTH();
		// Add a non-breaking space to the TD. This ensures that the cell is
	    // displayed.
	    DOM.setInnerHTML(th, "&nbsp;");
	    th.setAttribute(SCOPE, COL);
	    return th;
	  }

	  /**
	 * invoke setEnabled on each FocusWidget in this table.
	 * 
	 * @param enabled
	 *            the new enabled
	 */
	  public void setEnabled(boolean enabled){
			int rows = getRowCount();
			int cols = getColumnCount();
			for(int i = 0; i < rows; i++){
				for(int j = 0; j < cols; j++){
					Widget w = getWidget(i, j);
					if(w instanceof FocusWidget){
						((FocusWidget)w).setEnabled(enabled);
					}
				}
			}
	  }
	  
	  
	 /**
	 * clears all checkboxes in Grid.
	 * 
	 * @param name
	 *            must nut be null. checkBox name.
	 */
	public void clearCheckBoxesInGrid(String name){
			int rows = getRowCount();
			int cols = getColumnCount();
			for(int i = 0; i < rows; i++){
				for(int j = 0; j < cols; j++){
					Widget w = getWidget(i, j);
					if(w instanceof CheckBox &&
							w.getTitle().equals(name)){						
						((CheckBox)w).setValue(false);
					}
				}
			}
	  }
	  
	
	 /**
	 * Returns checkbox values that are checked and in Grid.
	 * 
	 * @param name
	 *            must not be null. CheckBox name
	 * @return List for checkbox formValue
	 */
	public List<String> getCheckBoxValuesInGrid(String name){
		  List<String> values = new ArrayList<String>();
		  int rows = getRowCount();
			int cols = getColumnCount();
			for(int i = 0; i < rows; i++){
				for(int j = 0; j < cols; j++){
					Widget w = getWidget(i, j);
					if(w instanceof CheckBox){
						CheckBox checkBox = ((CheckBox) w);
						if(checkBox.getTitle().equals(name) && checkBox.getValue()){						
							values.add(checkBox.getFormValue());
						}
					}
				}
			}
			return values;
	  }
}
