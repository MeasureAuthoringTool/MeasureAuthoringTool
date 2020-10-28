package mat.client.cqlworkspace.shared;

import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.RequiresResize;
import edu.ycp.cs.dh.acegwt.client.ace.AceAnnotationType;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorCursorPosition;

public interface EditorDisplay extends RequiresResize, HasText, TakesValue<String> {

    void redisplay();

    void removeAllMarkers();

    void addAnnotation(final int row, final int column, final String text, final AceAnnotationType type);

    void clearAnnotations();

    void setAnnotations();

    void insertAtCursor(String text);

    AceEditorCursorPosition getCursorPosition();

    void focus();

}
