package mat.client.expressionbuilder.modal;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.expressionbuilder.component.ExpressionTypeSelectorList;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.TimeBoundaryModel;
import mat.client.expressionbuilder.observer.BuildButtonObserver;
import mat.client.expressionbuilder.util.QueryFinderHelper;
import mat.client.shared.ListBoxMVP;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;

import java.util.ArrayList;
import java.util.List;

public class TimeBoundaryBuilderModal extends SubExpressionBuilderModal {

	private static final String END_OF = "end of";
	private static final String START_OF = "start of";
	private static final String TITLE = "Start of / End of";
	private static final String SELECT_PLACEHOLDER = "-- Select a Boundary --";
	private static final String BOUNDARY_LABEL = "Which boundary would you like to use?";
	private static final String LABEL = "What is the interval you would like to find the start or end of?";

	private int selectedIndex;

	private final TimeBoundaryModel timeBoundaryModel;

	private final BuildButtonObserver buildButtonObserver;
	
	private ListBoxMVP timeBoundaryListBox;

	public TimeBoundaryBuilderModal(ExpressionBuilderModal parent, ExpressionBuilderModel parentModel, ExpressionBuilderModel mainModel) {
		super(TITLE, parent, parentModel, mainModel);
		
		timeBoundaryModel = new TimeBoundaryModel(parentModel);
		getParentModel().appendExpression(timeBoundaryModel);
		buildButtonObserver = new BuildButtonObserver(this, timeBoundaryModel, mainModel);
		
		getApplyButton().addClickHandler(event -> onApplyButtonClick());
		display();
	}

	@Override
	public void display() {
		getErrorAlert().clearAlert();
		getContentPanel().add(buildContentPanel());
		updateCQLDisplay();
	}
	
	private Widget buildContentPanel() {
		getContentPanel().clear();
		
		final VerticalPanel panel = new VerticalPanel();
		panel.setStyleName("selectorsPanel");
		
		panel.add(buildFirstExpressionList());
		panel.add(buildSecondExpressionList());
		
		return panel;
	}

	private FormGroup buildFirstExpressionList() {
		final FormGroup timeBoundaryFormGroup = new FormGroup();		
		timeBoundaryFormGroup.setWidth("36%");

		final FormLabel timeBoundaryLabel = new FormLabel();
		timeBoundaryLabel.setText(BOUNDARY_LABEL);
		timeBoundaryLabel.setTitle(BOUNDARY_LABEL);
		
		buildTimeBoundaryListBox();
		
		timeBoundaryFormGroup.add(timeBoundaryLabel);
		timeBoundaryFormGroup.add(timeBoundaryListBox);
		
		return timeBoundaryFormGroup;
		
	}

	private void buildTimeBoundaryListBox() {
		timeBoundaryListBox = new ListBoxMVP();
		timeBoundaryListBox.insertItem(SELECT_PLACEHOLDER, SELECT_PLACEHOLDER);
		timeBoundaryListBox.insertItem(END_OF, END_OF);
		timeBoundaryListBox.insertItem(START_OF, START_OF);

		timeBoundaryListBox.setSelectedIndex(selectedIndex);
		timeBoundaryListBox.addChangeHandler(event -> onStartOfEndOfListBoxChange(timeBoundaryListBox.getSelectedIndex()));
	}
	
	private void onStartOfEndOfListBoxChange(int selectedIndex) {
		this.selectedIndex = selectedIndex;

		if(selectedIndex == 0) {
			timeBoundaryModel.setOperatorText("");
		} else  {
			timeBoundaryModel.setOperatorText(timeBoundaryListBox.getSelectedValue());
		}
		
		this.updateCQLDisplay();
	}

	private ExpressionTypeSelectorList buildSecondExpressionList() {
		final List<ExpressionType> availableExpressionTypes = new ArrayList<>();
		availableExpressionTypes.add(ExpressionType.ATTRIBUTE);		
		availableExpressionTypes.add(ExpressionType.DEFINITION);
		availableExpressionTypes.add(ExpressionType.FUNCTION);
		availableExpressionTypes.add(ExpressionType.INTERVAL);
		availableExpressionTypes.add(ExpressionType.PARAMETER);

		return new ExpressionTypeSelectorList(
				availableExpressionTypes, new ArrayList<>(), QueryFinderHelper.findAliasNames(this.timeBoundaryModel),
				buildButtonObserver, timeBoundaryModel, LABEL, this);
	}

	private void onApplyButtonClick() {
		if(timeBoundaryModel.getChildModels().isEmpty() || timeBoundaryListBox.getSelectedIndex() == 0) {
			getErrorAlert().createAlert("All fields are required.");
		} else {
			getExpressionBuilderParent().showAndDisplay();
		}
	}
}
