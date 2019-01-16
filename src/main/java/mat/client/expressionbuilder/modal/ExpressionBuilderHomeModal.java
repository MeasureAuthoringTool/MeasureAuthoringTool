package mat.client.expressionbuilder.modal;

import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Pull;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.expressionbuilder.component.ExpressionTypeSelectorList;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.constant.OperatorType;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.observer.BuildButtonObserver;
import mat.client.shared.ConfirmationDialogBox;
import mat.client.shared.ConfirmationObserver;

public class ExpressionBuilderHomeModal extends ExpressionBuilderModal {

	private Button exitBuilderButton;
	private Button completeBuildButton;
	private BuildButtonObserver buildButtonObserver;
	private AceEditor editorToInsertFinalTextInto;
	
	public ExpressionBuilderHomeModal(AceEditor editorToInsertFinalTextInto) {
		super("CQL Expression Builder", new ExpressionBuilderModel());
		buildButtonObserver = new BuildButtonObserver(this, this.getModel());
		this.editorToInsertFinalTextInto = editorToInsertFinalTextInto;
		display();
	}

	@Override
	public void display() {
		this.getContentPanel().clear();
		this.getFooter().clear();

		List<ExpressionType> availableExpressionTypes = new ArrayList<>();
		availableExpressionTypes.add(ExpressionType.RETRIEVE);

		List<OperatorType> availableOperatorTypes = new ArrayList<>();
		availableOperatorTypes.add(OperatorType.UNION);
		availableOperatorTypes.add(OperatorType.EXCEPT);
		availableOperatorTypes.add(OperatorType.INTERSECT);

		VerticalPanel selectorsPanel = new VerticalPanel();
		selectorsPanel.setStyleName("selectorsPanel");
		this.getContentPanel().add(new ExpressionTypeSelectorList(availableExpressionTypes, availableOperatorTypes, buildButtonObserver, this.getModel()));
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
		exitBuilderButton.setTitle("Exit Buidler");
		exitBuilderButton.setType(ButtonType.DANGER);
		exitBuilderButton.getElement().setAttribute("aria-label",
				"Click this button to cancel this bulid and exit the expression builder");
		exitBuilderButton.setSize(ButtonSize.LARGE);
		exitBuilderButton.addClickHandler(event -> onClose());
	}

	private void onCompleteBuildButtonClick() {
		String text = this.editorToInsertFinalTextInto.getText() + "\n" + this.getModel().getCQL();
		text = text.trim();
		this.editorToInsertFinalTextInto.setText(text);
		this.hide();
	}

	private void onClose() {
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

					}

					@Override
					public void onClose() {

					}
				});

		confirmExitDialogBox.show();
	}
}
