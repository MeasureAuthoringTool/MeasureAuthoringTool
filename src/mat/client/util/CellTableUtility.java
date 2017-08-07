package mat.client.util;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

// TODO: Auto-generated Javadoc
/**
 * The Class CellTableUtility.
 */
public class CellTableUtility {
	

	/** Gets the column tool tip.
	 * @param title the title
	 * @return the column tool tip */
	public static SafeHtml getColumnToolTip(String title) {
		String htmlConstant = "<html>" + "<head> </head> <body><span tabIndex = \"0\" title=\" " + title + "\">" + title + "</span></body>" + "</html>";
		return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
	}
	/** Gets the column tool tip.
	 * @param columnText the column text
	 * @param title the title
	 * @return the column tool tip */
	public static SafeHtml getColumnToolTip(String columnText, String title) {
		String htmlConstant = "<html>" + "<head> </head> <body><span tabIndex = \"0\" title=\" " + escapeHtml(title) + "\">" + escapeHtml(columnText)
				+ "</span></body>"
				+ "</html>";
		return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
	}
	
	
	/**
	 * Gets the name column tool tip.
	 *
	 * @param columnText the column text
	 * @param title the title
	 * @return the name column tool tip
	 */
	public static SafeHtml getNameColumnToolTip(String columnText, String title) {
		String htmlConstant = "<div id='container' tabindex=\"-1\">" +
	                           "<span id='div2' title=\" " + escapeHtml(title) + "\" tabindex=\"0\">" + 
	                           escapeHtml(columnText) + "</span>" + "</div>";
		return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
	}
	
	
	/**
	 * Gets the name column tool tip.
	 *
	 * @param columnText the column text
	 * @param title the title
	 * @return the name column tool tip
	 */
	public static SafeHtml getCodeDescriptorColumnToolTip(String columnText, String title) {
		
		if(columnText.length() >450){
			columnText = columnText.substring(0, 449);
		}
		String htmlConstant = "<div id='container' tabindex=\"-1\"> "+
	                           "<span id='div3' title=\" " + escapeHtml(title) + "\" tabindex=\"0\">" + 
	                           escapeHtml(columnText) + "</span>" + "</div>";
		
		
	
		return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
	}
	
	
	/**
	 * Escape html  method to escape special characters like 
	 * "&", "<", ">", "/", "\", "'" inside Html tag.
	 *
	 * @param html the html
	 * @return the string
	 */
	private static String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;").replaceAll("\"", "&quot;").replaceAll("'", "&#39;");
	}
}
