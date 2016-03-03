package mat.client.util;

import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

// TODO: Auto-generated Javadoc
/**
 * The Class CellTableUtility.
 */
public class CellTableUtility {
	
	 /** The Constant AMP_RE. */
 	private static final RegExp AMP_RE = RegExp.compile("&", "g");
	  
  	/** The Constant GT_RE. */
  	private static final RegExp GT_RE = RegExp.compile(">", "g");
	  
  	/** The Constant LT_RE. */
  	private static final RegExp LT_RE = RegExp.compile("<", "g");
	  
  	/** The Constant SQUOT_RE. */
  	private static final RegExp SQUOT_RE = RegExp.compile("\'", "g");
	  
  	/** The Constant QUOT_RE. */
  	private static final RegExp QUOT_RE = RegExp.compile("\"", "g");
	/** Gets the column tool tip.
	 * @param title the title
	 * @return the column tool tip */
	public static SafeHtml getColumnToolTip(String title) {
		String htmlConstant = "<html>" + "<head> </head> <body><span title=\" " + title + "\">" + title + "</span></body>" + "</html>";
		return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
	}
	/** Gets the column tool tip.
	 * @param columnText the column text
	 * @param title the title
	 * @return the column tool tip */
	public static SafeHtml getColumnToolTip(String columnText, String title) {
		String htmlConstant = "<html>" + "<head> </head> <body><span tabIndex = \"0\" title=\" " + title + "\">" + columnText
				+ "</span></body>"
				+ "</html>";
		return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
	}
	
	
	/**
	 * Html escape method to escape special characters like 
	 * "&", "<", ">", "/", "\", "'" inside Html tag.
	 *
	 * @param s the s
	 * @return the string
	 */
	public static String htmlEscape(String s) {
	    if (s.indexOf("&") != -1) {
	      s = AMP_RE.replace(s, "&amp;");
	    }
	    if (s.indexOf("<") != -1) {
	      s = LT_RE.replace(s, "&lt;");
	    }
	    if (s.indexOf(">") != -1) {
	      s = GT_RE.replace(s, "&gt;");
	    }
	    if (s.indexOf("\"") != -1) {
	      s = QUOT_RE.replace(s, "&quot;");
	    }
	    if (s.indexOf("'") != -1) {
	      s = SQUOT_RE.replace(s, "&#39;");
	    }
	    return s;
	  }
}
