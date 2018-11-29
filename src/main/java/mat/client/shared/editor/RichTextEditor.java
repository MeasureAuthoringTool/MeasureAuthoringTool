package mat.client.shared.editor;

import org.gwtbootstrap3.extras.summernote.client.ui.Summernote;
import org.gwtbootstrap3.extras.summernote.client.ui.base.SummernoteFontName;
import org.gwtbootstrap3.extras.summernote.client.ui.base.Toolbar;
import org.gwtbootstrap3.extras.summernote.client.ui.base.ToolbarButton;

public class RichTextEditor extends Summernote {
	
	public RichTextEditor(){
		super();
		this.setFontNames(SummernoteFontName.ARIAL, SummernoteFontName.HELVETICA,SummernoteFontName.TAHOMA, 
                SummernoteFontName.TIMES_NEW_ROMAN, SummernoteFontName.VERDANA );
        
        
        this.setToolbar(new Toolbar()
                  .addGroup(ToolbarButton.BOLD, ToolbarButton.ITALIC, ToolbarButton.UNDERLINE, ToolbarButton.STRIKETHROUGH)
                  .addGroup(ToolbarButton.UNDO, ToolbarButton.REDO, ToolbarButton.CLEAR)
                  .addGroup(ToolbarButton.UL, ToolbarButton.OL, ToolbarButton.PARAGRAPH)
                  .addGroup(ToolbarButton.FONT_NAME, ToolbarButton.FONT_SIZE));
        
        
        this.setDefaultHeight(350);
	}
	
	public String getPlainText() {
		return stripFormatting(this.getCode());
	}

	public String getFormattedText() {
		return this.getCode();
	}

	public String stripFormatting(String formatedText) {
		return formatedText.replaceAll("<[^>]*>", "").replaceAll("&nbsp;", " ");
	}
}
