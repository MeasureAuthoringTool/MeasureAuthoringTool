package mat.client.util;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class CellTableUtility {
	/** Gets the column tool tip.
	 * @param title the title
	 * @return the column tool tip */
	public static SafeHtml getColumnToolTip(String title) {
		String htmlConstant = "<html>" + "<head> </head> <Body><span title=\" " + title + "\">" + title + "</span></body>" + "</html>";
		return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
	}
	/** Gets the column tool tip.
	 * @param columnText the column text
	 * @param title the title
	 * @return the column tool tip */
	public static SafeHtml getColumnToolTip(String columnText, String title) {
		String htmlConstant = "<html>" + "<head> </head> <Body><span tabIndex = \"0\" title=\" " + title + "\">" + columnText
				+ "</span></body>"
				+ "</html>";
		return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
	}
}
