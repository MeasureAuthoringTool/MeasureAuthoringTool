package mat.client.cqlworkspace.generalinformation;

import com.google.gwt.user.client.ui.HorizontalPanel;
import mat.client.inapphelp.component.InAppHelp;
import mat.client.util.MatTextBox;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.TextArea;

public interface CQLGeneralInformationView {

    public static final String COMMENT_LENGTH_ERROR = "Comment cannot exceed 2500 characters.";
    public static final String LIBRARY_LENGTH_ERROR = "CQL Library Name cannot exceed 500 characters.";

    /**
     * This method will take a String and remove all non-alphabet/non-numeric characters
     * except underscore ("_") characters.
     *
     * @param originalString the original string
     * @return cleanedString
     */
    String createCQLLibraryName(String originalString);

    HorizontalPanel getView(boolean isEditable);

    MatTextBox getLibraryNameTextBox();

    MatTextBox getLibraryVersionTextBox();

    MatTextBox getUsingModelTextBox();

    MatTextBox getModelVersionTextBox();

    Button getSaveButton();

    void setIsEditable(boolean isEditable);

    void resetAll();

    void resetFormGroup();

    FormGroup getLibraryNameGroup();

    FormGroup getUsingModelGroup();

    FormGroup getLibraryVersionGroup();

    FormGroup getModelVersionGroup();

    void setHeading(String text, String linkName);

    TextArea getCommentsTextBox();

    FormGroup getCommentsGroup();

    InAppHelp getInAppHelp();
}
