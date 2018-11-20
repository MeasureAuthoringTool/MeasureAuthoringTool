package mat.client.util;

import org.gwtbootstrap3.extras.summernote.client.ui.Summernote;
import org.gwtbootstrap3.extras.summernote.client.ui.base.SummernoteFontName;
import org.gwtbootstrap3.extras.summernote.client.ui.base.Toolbar;
import org.gwtbootstrap3.extras.summernote.client.ui.base.ToolbarButton;

import com.google.gwt.user.client.Window;

import mat.client.measure.measuredetails.components.DescriptionModel;

public class RichTextEditor extends Summernote {

	//private Summernote richTextEditor = new Summernote();
	
	public RichTextEditor(){
		super();
		this.setFontNames(SummernoteFontName.ARIAL, SummernoteFontName.HELVETICA,SummernoteFontName.TAHOMA, 
                SummernoteFontName.TIMES_NEW_ROMAN, SummernoteFontName.VERDANA );
        
        
        this.setToolbar(new Toolbar()
                  .addGroup(ToolbarButton.BOLD, ToolbarButton.ITALIC, ToolbarButton.UNDERLINE, ToolbarButton.SUB_SCRIPT, ToolbarButton.SUPER_SCRIPT, ToolbarButton.STRIKETHROUGH)
                  .addGroup(ToolbarButton.UNDO, ToolbarButton.REDO, ToolbarButton.CLEAR)
                  .addGroup(ToolbarButton.UL, ToolbarButton.OL, ToolbarButton.PARAGRAPH)
                  .addGroup(ToolbarButton.FONT_NAME, ToolbarButton.FONT_SIZE));
        
        
        this.setDefaultHeight(350);
	}
	
	public String getPlanText() {
		return stripFormating(this.getCode());
	}
	
	public String getFormatedText() {
		return this.getCode();
	}

	public String stripFormating(String formatedText) {
		return formatedText.replaceAll("<[^>]*>", "");
	}
}
