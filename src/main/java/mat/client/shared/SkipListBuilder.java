package mat.client.shared;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class SkipListBuilder.
 */
public class SkipListBuilder {
	
	/**
	 * Builds the skip list.
	 * 
	 * @param skipstr
	 *            the skipstr
	 * @return the widget
	 */
	public static Widget buildSkipList(String skipstr){
		 HTML skipListHtml = new HTML("<ul class='skiplist'><li class='skip'><a id='menu' href='#' onclick='setFocusToFirstFocusable(\"" + skipstr + "\")'>Skip to Main Content</a></li></ul>");
		 return skipListHtml;
	}
	
	/**
	 * Builds the embedded link.
	 * 
	 * @param linkName
	 *            the link name
	 * @return the widget
	 */
	public static Widget buildEmbeddedLink(String linkName){
		HTML embeddedLink = new HTML("<span class='invisible' name='"+linkName+"'>"+linkName+"</span>");
		return embeddedLink;
	}
	
	/**
	 * Builds the sub skip list.
	 * 
	 * @param skipstr
	 *            the skipstr
	 * @return the widget
	 */
	public static Widget buildSubSkipList(String skipstr){
		HTML skipListHtml = new HTML("<ul class='skiplist'><li class='skip'><a id='submenu' href='#"+skipstr+"'>Skip to Sub Content</a></li></ul>");
		 return skipListHtml;
	}
	
	/**
	 * Builds the embedded link holder.
	 * 
	 * @param name
	 *            the name
	 * @return the widget
	 */
	public static Widget buildEmbeddedLinkHolder(String name){
		SimplePanel embeddedLinkHolder = new SimplePanel();
		HTML embeddedLink = new HTML("<span class='invisible' name='"+name+"'></span>");
		embeddedLinkHolder.add(embeddedLink);
		return embeddedLinkHolder;
	}
	
	/**
	 * Builds the embedded string.
	 * 
	 * @param linkName
	 *            the link name
	 * @return the string
	 */
	public static String buildEmbeddedString(String linkName){
		String embeddedString = "<span class='invisible' name='"+linkName+"'></span>";
		return embeddedString;
	}
	
}
