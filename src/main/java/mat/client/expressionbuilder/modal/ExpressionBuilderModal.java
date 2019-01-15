package mat.client.expressionbuilder.modal;

import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.ModalHeader;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.PanelType;
import org.gwtbootstrap3.client.ui.constants.Pull;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;
import mat.client.expressionbuilder.component.ExpressionTypeSelector;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.constant.OperatorType;
import mat.client.shared.ConfirmationDialogBox;
import mat.client.shared.ConfirmationObserver;

public class ExpressionBuilderModal extends Modal {

	private Button exitBuilderButton;
	private Button completeBuildButton;

	public ExpressionBuilderModal(AceEditor editorToInserFinalTextInto) {
		buildModal();
	}
	
	private void buildModal() {			
		this.setId("expressionBuilderModal");	
		ModalHeader header = new ModalHeader();
		header.setTitle("CQL Expression Builder");
		this.add(header);
		
		ModalBody body = new ModalBody();
		List<ExpressionType> availableExpressionTypes = new ArrayList<>();
		availableExpressionTypes.add(ExpressionType.RETRIEVE);
		
		List<OperatorType> availableOperatorTypes = new ArrayList<>();
		availableOperatorTypes.add(OperatorType.UNION);
		availableOperatorTypes.add(OperatorType.EXCEPT);
		availableOperatorTypes.add(OperatorType.INTERSECT);

		VerticalPanel selectorsPanel = new VerticalPanel();
		selectorsPanel.setStyleName("selectorsPanel");
		
		ExpressionTypeSelector selector = new ExpressionTypeSelector(availableExpressionTypes, availableOperatorTypes);
		selectorsPanel.add(selector);
		
		
		body.add(selectorsPanel);
		body.add(buildAceEditorPanel());
		
		ModalFooter footer = new ModalFooter();
		footer.add(buildFooter());
		
		
		this.add(body);
		this.add(footer);
	}
	
	private HorizontalPanel buildFooter() {
		HorizontalPanel panel = new HorizontalPanel();
		panel.setWidth("100%");
		
		buildExitBuilderButton();
		buildCompleteBuildButton();
		
		panel.add(exitBuilderButton);
		panel.add(completeBuildButton);
		
		return panel;
	}

	private void buildCompleteBuildButton() {
		completeBuildButton = new Button();
		completeBuildButton.setText("Complete Build");
		completeBuildButton.setTitle("Complete Build");
		completeBuildButton.setType(ButtonType.SUCCESS);
		completeBuildButton.getElement().setAttribute("aria-label", "Click this button to complete this build and go back to the CQL Workspace");
		completeBuildButton.setPull(Pull.RIGHT);
		completeBuildButton.setIcon(IconType.HOME);
		completeBuildButton.setSize(ButtonSize.LARGE);
	}

	private void buildExitBuilderButton() {
		exitBuilderButton = new Button();
		exitBuilderButton.setText("Exit Builder");
		exitBuilderButton.setTitle("Exit Buidler");
		exitBuilderButton.setType(ButtonType.DANGER);
		exitBuilderButton.getElement().setAttribute("aria-label", "Click this button to cancel this bulid and exit the expression builder");
		exitBuilderButton.setSize(ButtonSize.LARGE);
		exitBuilderButton.addClickHandler(event -> exitBulderButtonClick());
	}
	
	private void exitBulderButtonClick() {
		Modal modal = this;
		ConfirmationDialogBox confirmExitDialogBox = new ConfirmationDialogBox(
				"Are you sure you want to exit the Expression Builder? Any entries made to this point will not be saved. "
				+ "Click Exit to exit the Expression Builder or click Go Back to go back to the Expression Builder and continue building your expression.",
				"Exit", "Go Back", new ConfirmationObserver() {
					
					@Override
					public void onYesButtonClicked() {
						modal.hide();
						
					}
					
					@Override
					public void onNoButtonClicked() {
						// just use the default modal data dismiss.					
					}
					
					@Override
					public void onClose() {
												
					}
				});
		
		confirmExitDialogBox.show();
	}

	private Panel buildAceEditorPanel() {
		Panel panel = new Panel();
		panel.setMarginLeft(0.0);
		panel.setMarginRight(0.0);
		panel.setType(PanelType.PRIMARY);
		
		PanelHeader header = new PanelHeader();
		header.setText("CQL Expression");
		header.setTitle("CQL Expression");
		header.setStyleName("expressionBuilderExpressionPanel", true);
		PanelBody body = new PanelBody();
		body.add(buildAceEditor());
		
		panel.add(header);
		panel.add(body);
		return panel;
	}
	
	private AceEditor buildAceEditor() {
		AceEditor editor = new AceEditor();
		editor.startEditor();
		editor.setText("");
		editor.setMode(AceEditorMode.CQL);
		editor.setTheme(AceEditorTheme.ECLIPSE);
		editor.getElement().getStyle().setFontSize(14, Unit.PX);
		editor.setHeight("150px");
		editor.setAutocompleteEnabled(true);
		editor.addAutoCompletions();
		editor.setUseWrapMode(true);
		editor.removeAllMarkers();
		editor.clearAnnotations();
		editor.getElement().setAttribute("id", "cql_expression_editor");
		editor.setTitle("CQL Expression");
		editor.setReadOnly(true);
		return editor; 
	}
}
