package mat.client.expressionbuilder.modal;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.expressionbuilder.model.DateTimeModel;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.shared.datetime.DateTimeHelper;
import mat.client.shared.datetime.DateTimeWidget;

import java.util.ArrayList;
import java.util.List;

public class DateTimeBuilderModal extends SubExpressionBuilderModal {

	private static final String DATE_INVALID_ERROR_MESSAGE = "Please enter a valid date.";
	private static final String TIME_INVALID_ERROR_MESSAGE = "Please enter a valid time.";

	private final DateTimeModel dateTimeModel;
	private DateTimeWidget dateTimeWidget;

	public DateTimeBuilderModal(final ExpressionBuilderModal parent, final ExpressionBuilderModel parentModel, final ExpressionBuilderModel mainModel) {
		super(DateTimeHelper.DATE_TIME, parent, parentModel, mainModel);

		dateTimeModel = new DateTimeModel(parentModel);
		getParentModel().appendExpression(dateTimeModel);

		display();

		getApplyButton().addClickHandler(event -> onApplyButtonClick());
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

		dateTimeWidget = new DateTimeWidget(true);
		final HorizontalPanel datePanel = new HorizontalPanel();
		datePanel.getElement().setId("HorizontalPanel_DatePanel");
		datePanel.setWidth("555px");
		datePanel.add(dateTimeWidget.createDateWidget());
		datePanel.add(dateTimeWidget.createTimeWidget());
		dateTimeWidget.setDateTimeEnabled(true);

		panel.add(datePanel);

		return panel;
	}

	private void onApplyButtonClick() {
		if (isValidDateTimeString()) {
			dateTimeModel.setDateTimeString(dateTimeWidget.buildDateTimeString());
			getExpressionBuilderParent().showAndDisplay();
		}
	}

	private boolean isValidDateTimeString() {
		boolean isValid = true;
		final List<String> errors = new ArrayList<>();

		if (!dateTimeWidget.isValidDate()) {
			isValid = false;
			errors.add(DATE_INVALID_ERROR_MESSAGE);
		}
		if (!dateTimeWidget.isValidTime()) {
			isValid = false;
			errors.add(TIME_INVALID_ERROR_MESSAGE);
		}

		getErrorAlert().createAlert(errors);

		return isValid;
	}

}
