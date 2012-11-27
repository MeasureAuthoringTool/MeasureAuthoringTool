package mat.client.shared;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class ContentWithHeadingWidget extends Composite{
	
	
	HTML heading = new HTML();
	SimplePanel codeListInfo = new SimplePanel(); 
	SimplePanel content = new SimplePanel();
	SimplePanel footer = new SimplePanel();
	FocusableWidget headerHolder = new FocusableWidget(heading);
	SimplePanel embeddedLinkHolder = new SimplePanel();
	public ContentWithHeadingWidget() {
		FlowPanel vPanel = new FlowPanel();
		
		heading.addStyleName("contentWithHeadingHeader");
		heading.addStyleName("leftAligned");
		vPanel.add(heading);
		vPanel.add(codeListInfo);
		vPanel.add(content);
		vPanel.addStyleName("contentWithHeadingPanel");
		vPanel.add(footer);
		footer.addStyleName("returnLink");
		SimplePanel sPanel = new SimplePanel();
		sPanel.add(vPanel);
		initWidget(sPanel);		
	}
	public ContentWithHeadingWidget(Widget contentWidget, String headingStr,String linkName) {
		this();
		setHeading(headingStr,linkName);
		setContent(contentWidget);
	}
	
	public void setHeading(String text,String linkName) {
		String linkStr = SkipListBuilder.buildEmbeddedString(linkName);
		heading.setHTML(linkStr +"<h1>" + text + "</h1>");
	}
	
	/*public void setEmbeddedLink(String linkName){
		Widget w = SkipListBuilder.buildEmbeddedLink(linkName);
		embeddedLinkHolder.clear();
		embeddedLinkHolder.add(w);
	}*/
	
	
	public void setCodeListInfo(Widget w){
		codeListInfo.clear();
		codeListInfo.add(w);
	}
	
	public void setContent(Widget w) {
		content.clear();
		content.add(w);
	}
	public void setFooter(Widget w){
		footer.clear();
		footer.add(w);
	}
	
	
}
