package mat.client.shared;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class SkipListBuilder {
	public static Widget buildSkipList(String skipstr){
		 HTML skipListHtml = new HTML("<ul class='skiplist'><li class='skip'><a id='menu' href='#"+skipstr+"'>Skip to Main Content</a></li></ul>");
		 return skipListHtml;
	}
	
	
	public static Widget buildEmbeddedLink(String linkName){
		HTML embeddedLink = new HTML("<a class='invisible' name='"+linkName+"'>"+linkName+"</a>");
		return embeddedLink;
	}
	
	public static Widget buildSubSkipList(String skipstr){
		HTML skipListHtml = new HTML("<ul class='skiplist'><li class='skip'><a id='submenu' href='#"+skipstr+"'>Skip to Sub Content</a></li></ul>");
		 return skipListHtml;
	}
	
	public static Widget buildEmbeddedLinkHolder(String name){
		SimplePanel embeddedLinkHolder = new SimplePanel();
		HTML embeddedLink = new HTML("<a class='invisible' name='"+name+"'></a>");
		embeddedLinkHolder.add(embeddedLink);
		return embeddedLinkHolder;
	}
	
	public static String buildEmbeddedString(String linkName){
		String embeddedString = "<a class='invisible' name='"+linkName+"'></a>";
		return embeddedString;
	}
	
}
