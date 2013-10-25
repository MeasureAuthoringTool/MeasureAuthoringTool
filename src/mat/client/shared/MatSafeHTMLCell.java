package mat.client.shared;

import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * The Class MatSafeHTMLCell.
 */
public class MatSafeHTMLCell extends SafeHtmlCell {

	
	 /**
	   * Construct a new SafeHtmlCell.
	   */
	  public MatSafeHTMLCell() {
		  super();
	  }

	  /* (non-Javadoc)
  	 * @see com.google.gwt.cell.client.SafeHtmlCell#render(com.google.gwt.cell.client.Cell.Context, com.google.gwt.safehtml.shared.SafeHtml, com.google.gwt.safehtml.shared.SafeHtmlBuilder)
  	 */
  	@Override
	  public void render(Context context, SafeHtml value, SafeHtmlBuilder sb) {
	    if (value != null) {
	      sb.appendHtmlConstant("<span tabindex=\"0\">");
	      if (value != null) {
	          sb.append(value);
	        }
	        sb.appendHtmlConstant("</span>");
	    }
	  }
}
