package mat.client.expressionbuilder.modal;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.cqlworkspace.AbstractCQLWorkspacePresenter;
import mat.client.expressionbuilder.component.ExpressionTypeSelectorList;
import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.constant.OperatorType;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.observer.BuildButtonObserver;
import mat.client.expressionbuilder.util.OperatorTypeUtil;
import mat.client.shared.ConfirmationDialogBox;
import mat.client.shared.ConfirmationObserver;
import mat.client.shared.MatContext;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Pull;

import java.util.ArrayList;
import java.util.List;

public class ExpressionBuilderHomeModal extends ExpressionBuilderModal {

	private Button exitBuilderButton;
	private Button completeBuildButton;
	private BuildButtonObserver buildButtonObserver;
	private AceEditor editorToInsertFinalTextInto;
	private AbstractCQLWorkspacePresenter workspacePresenter;
	
	public ExpressionBuilderHomeModal(AbstractCQLWorkspacePresenter workspacePresenter, ExpressionBuilderModel model) {
		super("CQL Expression Builder", model, model);
		buildButtonObserver = new BuildButtonObserver(this, this.getParentModel(), this.getMainModel());
		this.editorToInsertFinalTextInto = workspacePresenter.getCQLWorkspaceView().getCQLDefinitionsView().getDefineAceEditor();
		this.workspacePresenter = workspacePresenter;
		display();
	}

	@Override
	public void display() {
		this.getContentPanel().clear();
		this.getFooter().clear();

		List<ExpressionType> availableExpressionTypes = new ArrayList<>();
		availableExpressionTypes.add(ExpressionType.ATTRIBUTE);
		availableExpressionTypes.add(ExpressionType.COMPARISON);
		availableExpressionTypes.add(ExpressionType.COMPUTATION);
		availableExpressionTypes.add(ExpressionType.RETRIEVE);
		availableExpressionTypes.add(ExpressionType.DATE_TIME);
		availableExpressionTypes.add(ExpressionType.DEFINITION);
		availableExpressionTypes.add(ExpressionType.EXISTS);
		availableExpressionTypes.add(ExpressionType.FUNCTION);
		availableExpressionTypes.add(ExpressionType.INTERVAL);
		availableExpressionTypes.add(ExpressionType.NOT);
		availableExpressionTypes.add(ExpressionType.IS_NULL_NOT_NULL);
		availableExpressionTypes.add(ExpressionType.QUANTITY);
		availableExpressionTypes.add(ExpressionType.QUERY);
		availableExpressionTypes.add(ExpressionType.TIME_BOUNDARY);
		availableExpressionTypes.add(ExpressionType.IS_TRUE_FALSE);
		availableExpressionTypes.add(ExpressionType.TIMING);
				
		List<OperatorType> availableOperatorTypes = new ArrayList<>();
		availableOperatorTypes.addAll(OperatorTypeUtil.getAvailableOperatorsCQLType(CQLType.ANY));
		
		VerticalPanel selectorsPanel = new VerticalPanel();
		selectorsPanel.setStyleName("selectorsPanel");
		String label = "What type of expression would you like to build?";
		this.getContentPanel().add(new ExpressionTypeSelectorList(availableExpressionTypes, availableOperatorTypes, 
				buildButtonObserver, this.getParentModel(), label, this));
		this.getFooter().add(buildFooter());
		this.updateCQLDisplay();
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
		completeBuildButton.getElement().setAttribute("aria-label",
				"Click this button to complete this build and go back to the CQL Workspace");
		completeBuildButton.setPull(Pull.RIGHT);
		completeBuildButton.setIcon(IconType.HOME);
		completeBuildButton.setSize(ButtonSize.LARGE);
		completeBuildButton.addClickHandler(event -> onCompleteBuildButtonClick());
	}

	private void buildExitBuilderButton() {
		exitBuilderButton = new Button();
		exitBuilderButton.setText("Exit Builder");
		exitBuilderButton.setTitle("Exit Builder");
		exitBuilderButton.setType(ButtonType.DANGER);
		exitBuilderButton.getElement().setAttribute("aria-label",
				"Click this button to cancel this bulid and exit the expression builder");
		exitBuilderButton.setSize(ButtonSize.LARGE);
		exitBuilderButton.addClickHandler(event -> onClose());
	}

	private void onCompleteBuildButtonClick() {
		MatContext.get().restartTimeoutWarning();
		String text = this.editorToInsertFinalTextInto.getText() + "\n" + this.getParentModel().getCQL("");
		text = text.trim();
		this.editorToInsertFinalTextInto.setText(text);
		this.hide();
		this.workspacePresenter.setHelpBlockText("The Expression has been successfully added to the CQL Editor. Click the save icon to save the Definition.");
		this.workspacePresenter.setIsPageDirty(true);
	}

	private void onClose() {
		exitBuilderButton.setEnabled(false);
		completeBuildButton.setEnabled(false);
		ConfirmationDialogBox confirmExitDialogBox = new ConfirmationDialogBox(
				"Are you sure you want to exit the Expression Builder? Any entries made to this point will not be saved. "
						+ "Click Exit to exit the Expression Builder or click Go Back to go back to the Expression Builder and continue building your expression.",
				"Exit", "Go Back", new ConfirmationObserver() {

					@Override
					public void onYesButtonClicked() {
						hide();
					}

					@Override
					public void onNoButtonClicked() {
						onCloseOfWarningModal();
					}

					@Override
					public void onClose() {
						onCloseOfWarningModal();
					}
					
					private void onCloseOfWarningModal() {
						exitBuilderButton.setEnabled(true);
						completeBuildButton.setEnabled(true);
					}
				});

		confirmExitDialogBox.show();
	}
}
