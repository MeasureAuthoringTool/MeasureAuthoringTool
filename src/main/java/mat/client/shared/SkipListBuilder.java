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
		 HTML skipListHtml = new HTML("<ul class='skiplist'><li class='skip'><a id='menu' href='#"+skipstr+"'>Skip to Main Content</a></li></ul>");
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
		HTML embeddedLink = new HTML("<a class='invisible' name='"+linkName+"'>"+linkName+"</a>");
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
		HTML embeddedLink = new HTML("<a class='invisible' name='"+name+"'></a>");
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
		String embeddedString = "<a class='invisible' name='"+linkName+"'></a>";
		return embeddedString;
	}
	
}
