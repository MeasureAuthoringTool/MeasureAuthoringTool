package mat.client.util;

import org.gwtbootstrap3.extras.summernote.client.ui.Summernote;
import org.gwtbootstrap3.extras.summernote.client.ui.base.SummernoteFontName;
import org.gwtbootstrap3.extras.summernote.client.ui.base.Toolbar;
import org.gwtbootstrap3.extras.summernote.client.ui.base.ToolbarButton;

public class RichTextEditor {

	private Summernote richTextEditor = new Summernote();
	
	public RichTextEditor(){
		richTextEditor.setFontNames(SummernoteFontName.ARIAL, SummernoteFontName.HELVETICA,SummernoteFontName.TAHOMA, 
                SummernoteFontName.TIMES_NEW_ROMAN, SummernoteFontName.VERDANA );
        
        
        richTextEditor.setToolbar(new Toolbar()
                  .addGroup(ToolbarButton.BOLD, ToolbarButton.ITALIC, ToolbarButton.UNDERLINE, ToolbarButton.SUB_SCRIPT, ToolbarButton.SUPER_SCRIPT, ToolbarButton.STRIKETHROUGH)
                  .addGroup(ToolbarButton.UNDO, ToolbarButton.REDO, ToolbarButton.CLEAR)
                  .addGroup(ToolbarButton.UL, ToolbarButton.OL, ToolbarButton.PARAGRAPH)
                  .addGroup(ToolbarButton.FONT_NAME, ToolbarButton.FONT_SIZE));
        
        
        richTextEditor.setDefaultHeight(350);
	}

	public Summernote getRichTextEditor() {
		return richTextEditor;
	}

	public void setRichTextEditor(Summernote richTextEditor) {
		this.richTextEditor = richTextEditor;
	}
}
