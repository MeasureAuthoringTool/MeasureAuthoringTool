package mat.client.codelist;

import mat.client.ImageResources;
import mat.client.shared.FocusableImageButton;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class ExcelViewerWidget.
 */
public class ExcelViewerWidget extends Composite{
	
	  /** The export viewer html. */
  	private HTML exportViewerHtml;
	  
	  /** The export view anchor. */
  	private Anchor exportViewAnchor;
	  
	  /** The exportlinkimage. */
  	private FocusableImageButton exportlinkimage = new FocusableImageButton(ImageResources.INSTANCE.icon_newWindow(),"Excel Viewer");
	  
	  /** The export view click handler. */
  	private ClickHandler exportViewClickHandler = new ClickHandler() {
		
		@Override
		public void onClick(ClickEvent event) {
			
			
		}
	};
	  
	  /**
	 * Instantiates a new excel viewer widget.
	 */
  	public ExcelViewerWidget(){
		  	 VerticalPanel exportViewerPanel = new VerticalPanel();
    	     exportViewerPanel.getElement().setId("exportViewerPanel_VerticalPanel");
    	     exportViewerPanel.add(new Label("Excel Viewer is required to view and print the files available on this page."));
    	     HorizontalPanel exportViewerLinkHolder = new HorizontalPanel();
    	     exportViewerLinkHolder.getElement().setId("exportViewerLinkHolder_HorizontalPanel");
    	     exportViewerHtml = new HTML("Install the latest version of");
    	     exportViewAnchor = new Anchor("Excel Viewer");
    	     exportViewAnchor.addClickHandler(exportViewClickHandler);
    	     exportViewAnchor.getElement().setId("exportViewAnchor_Anchor");
    	     exportlinkimage.getElement().setId("exportlinkimage_FocusableImageButton");
    	     exportViewerLinkHolder.add(exportViewerHtml);
    	     exportViewerLinkHolder.add(new HTML("&nbsp;"));
    	     exportViewerLinkHolder.add(exportlinkimage);
    	     exportViewerLinkHolder.add(new HTML("&nbsp;"));
    	     exportViewerLinkHolder.add(exportViewAnchor);
    	     exportViewerPanel.add(exportViewerLinkHolder);
    	     initWidget(exportViewerPanel);
      }
	  
	  /**
	 * Gets the export view anchor.
	 * 
	 * @return the export view anchor
	 */
  	public Anchor getExportViewAnchor(){
		  return  exportViewAnchor;
		  
	  }
}
