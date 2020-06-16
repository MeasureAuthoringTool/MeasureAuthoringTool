package mat.client.cqlworkspace;

import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.DropDownMenu;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Pull;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import edu.ycp.cs.dh.acegwt.client.ace.AceAnnotationType;
import mat.client.buttons.FullScreenToolBarButton;
import mat.client.buttons.InfoDropDownMenu;
import mat.client.buttons.InfoToolBarButton;
import mat.client.buttons.InsertToolBarButton;
import mat.client.buttons.SaveButton;
import mat.client.cqlworkspace.shared.CQLEditor;
import mat.client.cqlworkspace.shared.CQLEditorPanel;
import mat.client.inapphelp.component.InAppHelp;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.SpacerWidget;
import mat.shared.CQLError;

public class CQLLibraryEditorView {
    private static final String CQL_LIBRARY_EDITOR_ID = "cqlLibraryEditor";
    private static final String WIDE_CQL_EDITOR_STYLE = "wide-cql-editor";
    private VerticalPanel cqlLibraryEditorVP = new VerticalPanel();
    private HTML heading = new HTML();
    private InAppHelp inAppHelp = new InAppHelp("");
    private CQLEditorPanel editorPanel = new CQLEditorPanel(CQL_LIBRARY_EDITOR_ID, "CQL Library Editor", false);

    private Button exportErrorFile = new Button();
    private Button infoButton = new InfoToolBarButton(CQL_LIBRARY_EDITOR_ID);
    private Button insertButton = new InsertToolBarButton(CQL_LIBRARY_EDITOR_ID);
    private Button fullScreenButton = new FullScreenToolBarButton(CQL_LIBRARY_EDITOR_ID);
    private Button saveButton = new SaveButton(CQL_LIBRARY_EDITOR_ID);

    private ButtonGroup infoBtnGroup;

    public CQLLibraryEditorView() {
        cqlLibraryEditorVP.clear();
        exportErrorFile.setType(ButtonType.PRIMARY);
        exportErrorFile.setIcon(IconType.DOWNLOAD);
        exportErrorFile.setText("Export Error File");
        exportErrorFile.setTitle("Click to download Export Error File.");
        exportErrorFile.setId("Button_exportErrorFile");
    }

    public VerticalPanel buildView(boolean isEditorEditable, boolean isPageEditable) {

        editorPanel = new CQLEditorPanel(CQL_LIBRARY_EDITOR_ID, "CQL Library Editor", !isEditorEditable);
        cqlLibraryEditorVP.clear();
        cqlLibraryEditorVP.getElement().setId("cqlLibraryEditor_Id");
        heading.addStyleName("leftAligned");

        cqlLibraryEditorVP.add(SharedCQLWorkspaceUtility.buildHeaderPanel(heading, inAppHelp));
        cqlLibraryEditorVP.add(new SpacerWidget());

        getCqlAceEditor().setText("");
        getCqlAceEditor().clearAnnotations();

        if (isPageEditable) {
            exportErrorFile.setPull(Pull.LEFT);
            cqlLibraryEditorVP.add(exportErrorFile);
        }

        FlowPanel fp = new FlowPanel();
        buildInfoInsertBtnGroup();
        fp.add(infoBtnGroup);
        fp.add(insertButton);
        fp.add(fullScreenButton);
        cqlLibraryEditorVP.add(fp);

        fullScreenButton.addClickHandler(event -> {
            if (cqlLibraryEditorVP.getStyleName().contains(WIDE_CQL_EDITOR_STYLE)) {
                switchOffWideEditor();
                exitFullscreen();

            } else {
                switchOnWideEditor();
                requestFullscreen();
            }
        });

        getCqlAceEditor().setReadOnly(!isEditorEditable);
        getSaveButton().setEnabled(isEditorEditable);
        insertButton.setEnabled(isEditorEditable);

        this.editorPanel.getEditor().addDomHandler(event -> editorPanel.catchTabOutKeyCommand(event, saveButton), KeyUpEvent.getType());
        this.editorPanel.getEditor().addKeyDownHandler(event -> {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
                switchOffWideEditor();
                exitFullscreen();
            }
        });
        editorPanel.setSize("650px", "500px");

        cqlLibraryEditorVP.add(editorPanel);

        saveButton.setPull(Pull.RIGHT);
        cqlLibraryEditorVP.add(saveButton);

        cqlLibraryEditorVP.setStyleName("cqlRightContainer");
        cqlLibraryEditorVP.setWidth("700px");
        cqlLibraryEditorVP.setStyleName("marginLeft15px");
        return cqlLibraryEditorVP;
    }

    private void switchOnWideEditor() {
        cqlLibraryEditorVP.getElement().getStyle().setBackgroundColor("white");
        cqlLibraryEditorVP.addStyleName(WIDE_CQL_EDITOR_STYLE);
        cqlLibraryEditorVP.removeStyleName("cqlRightContainer");
        cqlLibraryEditorVP.removeStyleName("marginLeft15px");
        cqlLibraryEditorVP.setWidth("100%");
        cqlLibraryEditorVP.setHeight("100%");
        editorPanel.setSize("1000px", "900px");
        editorPanel.setEditorContentSize("900px", "838px");
        editorPanel.getEditor().redisplay();
    }


    private void switchOffWideEditor() {
        cqlLibraryEditorVP.removeStyleName(WIDE_CQL_EDITOR_STYLE);
        cqlLibraryEditorVP.setStyleName("cqlRightContainer");
        cqlLibraryEditorVP.setWidth("700px");
        cqlLibraryEditorVP.setStyleName("marginLeft15px");
        editorPanel.setSize("650px", "500px");
        editorPanel.setEditorContentSize("590px", "538px");
        editorPanel.getEditor().redisplay();
    }

    public static native void requestFullscreen()
        /*-{
            var docElement = $doc.documentElement;
            if (docElement.requestFullscreen) {
                docElement.requestFullscreen();
            } else if (docElement.msRequestFullscreen) {
                docElement.msRequestFullscreen();
            } else if (docElement.mozRequestFullScreen) {
                docElement.mozRequestFullScreen();
            } else if (docElement.webkitRequestFullScreen) {
                docElement.webkitRequestFullScreen();
            }
        }-*/;

    public static native void exitFullscreen()
        /*-{
            if ($doc.exitFullscreen) {
                $doc.exitFullscreen();
            } else if ($doc.msExitFullscreen) {
                $doc.msExitFullscreen();
            } else if ($doc.mozCancelFullScreen) {
                $doc.mozCancelFullScreen();
            } else if ($doc.webkitCancelFullScreen) {
                $doc.webkitCancelFullScreen();
            }
        }-*/;

    private void buildInfoInsertBtnGroup() {
        DropDownMenu ddm = new InfoDropDownMenu();
        ddm.getElement().getStyle().setMarginLeft(3, Unit.PX);

        infoButton.setMarginLeft(-10);

        infoBtnGroup = new ButtonGroup();
        infoBtnGroup.getElement().setAttribute("class", "btn-group");
        infoBtnGroup.add(infoButton);
        infoBtnGroup.add(ddm);
        infoBtnGroup.setPull(Pull.LEFT);

        insertButton.setPull(Pull.RIGHT);
    }

    public CQLEditor getCqlAceEditor() {
        return editorPanel.getEditor();
    }

    public void resetAll() {
        editorPanel = new CQLEditorPanel(CQL_LIBRARY_EDITOR_ID, "CQL Library Editor", false);
        getCqlAceEditor().setText("");
    }

    public void setHeading(String text, String linkName) {
        String linkStr = SkipListBuilder.buildEmbeddedString(linkName);
        heading.setHTML(linkStr + "<h4><b>" + text + "</b></h4>");
    }

    public Button getExportErrorFile() {
        return exportErrorFile;
    }

    public void setExportErrorFile(Button exportErrorFile) {
        this.exportErrorFile = exportErrorFile;
    }

    public void setCQLLibraryEditorAnnotations(List<CQLError> cqlErrors, String prefix, AceAnnotationType aceAnnotationType) {
        for (CQLError error : cqlErrors) {
            int line = error.getErrorInLine();
            int column = error.getErrorAtOffeset();
            this.getCqlAceEditor().addAnnotation(line - 1, column, prefix + error.getErrorMessage(), aceAnnotationType);
        }
    }

    public InAppHelp getInAppHelp() {
        return inAppHelp;
    }

    public void setInAppHelp(InAppHelp inAppHelp) {
        this.inAppHelp = inAppHelp;
    }

    public Button getInsertButton() {
        return insertButton;
    }

    public Button getInfoButton() {
        return infoButton;
    }

    public Button getSaveButton() {
        return this.saveButton;
    }

    public Button getFullScreenButton() {
        return fullScreenButton;
    }


    public void fullScreen() {

    }
}
