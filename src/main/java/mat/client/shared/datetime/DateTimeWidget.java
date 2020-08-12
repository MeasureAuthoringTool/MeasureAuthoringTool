package mat.client.shared.datetime;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import mat.client.expressionbuilder.constant.CQLType;
import mat.client.shared.CustomDateTimeTextBox;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

public class DateTimeWidget {

	private static final String DOT_SYMBOL = "<h2 style=\"margin:0px 5px 15px 5px\">.</h2>";
	private static final String COLON_SYMBOL = "<h2 style=\"margin:0px 5px 15px 5px\">:</h2>";
	private static final String SPACE_SYMBOL = "<h2 style=\"margin:0px 5px 10px 5px\">/</h2>";

	private boolean isDate;
	private boolean isTime;
	private boolean isExpressionBuilder = false;

	private CustomDateTimeTextBox yyyyTxtBox = new CustomDateTimeTextBox(4);
	private CustomDateTimeTextBox mmTxtBox = new CustomDateTimeTextBox(2);
	private CustomDateTimeTextBox ddTxtBox = new CustomDateTimeTextBox(2);
	private CustomDateTimeTextBox hhTextBox = new CustomDateTimeTextBox(2);
	private CustomDateTimeTextBox minTxtBox = new CustomDateTimeTextBox(2);
	private CustomDateTimeTextBox ssTxtBox = new CustomDateTimeTextBox(2);
	private CustomDateTimeTextBox msTxtBox = new CustomDateTimeTextBox(3);

	private FormGroup yearFormGroup = new FormGroup();
	private FormGroup mmFormGroup = new FormGroup();
	private FormGroup ddFormGroup = new FormGroup();
	private FormGroup hourFormGroup = new FormGroup();
	private FormGroup minFormGroup = new FormGroup();
	private FormGroup secondsFormGroup = new FormGroup();
	private FormGroup millisecFormGroup = new FormGroup();

	public DateTimeWidget() {
		super();
	}

	public DateTimeWidget(final boolean isExprBuilder) {
		super();
		isExpressionBuilder = isExprBuilder;
	}

	public Grid createDateWidget() {

		buildYear();

		buildMonth();

		buildDay();

		return buildDateGrid();
	}

	public Grid createTimeWidget() {

		buildHour();

		buildMinute();

		buildSeconds();

		buildMilliseconds();

		return buildTimeGrid();
	}

	private Grid buildDateGrid() {
		final Grid dateGrid = new Grid(2, 5);
		dateGrid.setWidget(0, 0, buildYearLabel());
		dateGrid.setWidget(1, 0, yearFormGroup);
		dateGrid.setWidget(1, 1, new HTML(SPACE_SYMBOL));
		dateGrid.setWidget(0, 2, buildMonthLabel());
		dateGrid.setWidget(1, 2, mmFormGroup);
		dateGrid.setWidget(1, 3, new HTML(SPACE_SYMBOL));
		dateGrid.setWidget(0, 4, buildDayLabel());
		dateGrid.setWidget(1, 4, ddFormGroup);
		return dateGrid;
	}

	private Grid buildTimeGrid() {
		final Grid timeGrid = new Grid(2, 7);
		timeGrid.setWidget(0, 0, buildHourLabel());
		timeGrid.setWidget(1, 0, hourFormGroup);
		timeGrid.setWidget(1, 1, new HTML(COLON_SYMBOL));
		timeGrid.setWidget(0, 2, buildMinuteLabel());
		timeGrid.setWidget(1, 2, minFormGroup);
		timeGrid.setWidget(1, 3, new HTML(COLON_SYMBOL));
		timeGrid.setWidget(0, 4, buildSecondsLabel());
		timeGrid.setWidget(1, 4, secondsFormGroup);
		timeGrid.setWidget(1, 5, new HTML(DOT_SYMBOL));
		timeGrid.setWidget(0, 6, buildMillisLabel());
		timeGrid.setWidget(1, 6, millisecFormGroup);
		return timeGrid;
	}

	private FormLabel buildYearLabel() {
		return buildLabel("YYYY", "Year", "year-Label");
	}

	private FormLabel buildMonthLabel() {
		return buildLabel("MM", "Month", "month-Label");
	}

	private FormLabel buildDayLabel() {
		return buildLabel("DD", "Day", "day-Label");
	}

	private FormLabel buildHourLabel() {
		return buildLabel("hh", "Hour(s)", "hour-Label");
	}

	private FormLabel buildMinuteLabel() {
		return buildLabel("mm", "Minute(s)", "minute-Label");
	}

	private FormLabel buildSecondsLabel() {
		return buildLabel("ss", "Second(s)", "seconds-Label");
	}

	private FormLabel buildMillisLabel() {
		return buildLabel("fff", "Millisecond(s)", "millisec-Label");
	}

	private FormLabel buildLabel(final String text, final String title, final String style) {
		final FormLabel dayFormLabel = new FormLabel();
		dayFormLabel.setText(text);
		dayFormLabel.setTitle(title);
		if (!isExpressionBuilder) {
			dayFormLabel.setStyleName(style);
		}
		return dayFormLabel;
	}

	private void buildYear() {
		yyyyTxtBox.clear();
		yyyyTxtBox.setWidth("50px");
		yyyyTxtBox.setTitle("Year");
		//year range validator
		yyyyTxtBox.addBlurHandler(event -> validateYearRange());

		getYearFormGroup().clear();
		getYearFormGroup().add(yyyyTxtBox);
	}

	private void buildMonth() {
		mmTxtBox.clear();
		mmTxtBox.setWidth("50px");
		mmTxtBox.setTitle("Month");
		//month range validator
		mmTxtBox.addBlurHandler(event -> validateMonthRange());

		mmFormGroup.clear();
		mmFormGroup.add(mmTxtBox);
	}

	private void buildDay() {
		ddTxtBox.clear();
		ddTxtBox.setWidth("50px");
		ddTxtBox.setTitle("Date");
		//day range validator
		ddTxtBox.addBlurHandler(event -> validateDayRange());

		ddFormGroup.clear();
		ddFormGroup.add(ddTxtBox);
	}

	private void buildHour() {
		hhTextBox.clear();
		hhTextBox.setWidth("50px");
		hhTextBox.setTitle("Hours");
		//hour range validator
		hhTextBox.addBlurHandler(event -> validateHoursRange());

		hourFormGroup.clear();
		hourFormGroup.add(hhTextBox);
	}

	private void buildMinute() {
		minTxtBox.clear();
		minTxtBox.setWidth("50px");
		minTxtBox.setTitle("Minutes");
		//minute range validator
		minTxtBox.addBlurHandler(event -> validateMinutesRange());

		minFormGroup.clear();
		minFormGroup.add(minTxtBox);
	}

	private void buildSeconds() {
		ssTxtBox.clear();
		ssTxtBox.setWidth("50px");
		ssTxtBox.setTitle("Seconds");
		//seconds range validator
		ssTxtBox.addBlurHandler(event -> validateSecondsRange());

		secondsFormGroup.clear();
		secondsFormGroup.add(ssTxtBox);
	}

	private void buildMilliseconds() {
		msTxtBox.clear();
		msTxtBox.setWidth("50px");
		msTxtBox.setTitle("MilliSeconds");
		//milliseconds range validator
		msTxtBox.addBlurHandler(event -> validateMillisecondsRange());

		millisecFormGroup.clear();
		millisecFormGroup.add(msTxtBox);
	}

	private void validateYearRange() {
		if (!yyyyTxtBox.getText().isEmpty()){
			isDate = true;
			yearFormGroup.setValidationState(DateTimeHelper.getValidationState(yyyyTxtBox.getText(), 0, 9999));
		}
	}

	private void validateMonthRange() {
		if (!mmTxtBox.getText().isEmpty()){
			mmFormGroup.setValidationState(DateTimeHelper.getValidationState(mmTxtBox.getText(), 1, 12));
		}
	}

	private void validateDayRange() {
		if (!ddTxtBox.getText().isEmpty()){
			ddFormGroup.setValidationState(DateTimeHelper.getValidationState(ddTxtBox.getText(), 1, 31));
		}
	}

	private void validateHoursRange() {
		if (!hhTextBox.getText().isEmpty()){
			isTime = true;
			hourFormGroup.setValidationState(DateTimeHelper.getValidationState(hhTextBox.getText(), 1, 24));
		}
	}

	private void validateMinutesRange() {
		if (!minTxtBox.getText().isEmpty()){
			minFormGroup.setValidationState(DateTimeHelper.getValidationState(minTxtBox.getText(), 0, 59));
		}
	}

	private void validateSecondsRange() {
		if (!ssTxtBox.getText().isEmpty()){
			secondsFormGroup.setValidationState(DateTimeHelper.getValidationState(ssTxtBox.getText(), 0, 59));
		}
	}

	private void validateMillisecondsRange() {
		if (!msTxtBox.getText().isEmpty()){
			millisecFormGroup.setValidationState(DateTimeHelper.getValidationState(msTxtBox.getText(), 0, 999));
		}
	}

	public boolean isValidDate() {
		return DateTimeHelper.validateDate(yyyyTxtBox.getText(), mmTxtBox.getText(), ddTxtBox.getText());
	}

	public boolean isValidTime() {
		return DateTimeHelper.validateTime(hhTextBox.getText(), minTxtBox.getText(), ssTxtBox.getText(), msTxtBox.getText());
	}

	public String buildDateTimeString(){
		final StringBuilder sb = new StringBuilder();
		sb.append("@");

		if (!yyyyTxtBox.getText().isEmpty()) {
			sb.append(DateTimeHelper.buildZeroString(yyyyTxtBox.getText(), 4));
		}

		if (!mmTxtBox.getText().isEmpty()) {
			sb.append("-").append(DateTimeHelper.buildZeroString(mmTxtBox.getText(), 2));
		}

		if (!ddTxtBox.getText().isEmpty()) {
			sb.append("-").append(DateTimeHelper.buildZeroString(ddTxtBox.getText(), 2));
		}

		if (!hhTextBox.getText().isEmpty()) {
			sb.append("T").append(DateTimeHelper.buildZeroString(hhTextBox.getText(), 2));
		}

		if (!minTxtBox.getText().isEmpty()) {
			sb.append(":").append(DateTimeHelper.buildZeroString(minTxtBox.getText(), 2));
		}

		if (!ssTxtBox.getText().isEmpty()) {
			sb.append(":").append(DateTimeHelper.buildZeroString(ssTxtBox.getText(), 2));
		}

		if (!msTxtBox.getText().isEmpty()) {
			sb.append(".").append(msTxtBox.getText());
		}

		return sb.toString();
	}

	public String buildDateTimeCQLTypeString(){
		final StringBuilder sb = new StringBuilder();
		sb.append(buildDateTimeType()).append("(");

		if (!yyyyTxtBox.getText().isEmpty()) {
			sb.append(DateTimeHelper.buildZeroString(yyyyTxtBox.getText(), 4));
		}

		if (!mmTxtBox.getText().isEmpty()) {
			sb.append(",").append(DateTimeHelper.buildZeroString(mmTxtBox.getText(), 2));
		}

		if (!ddTxtBox.getText().isEmpty()) {
			sb.append(",").append(DateTimeHelper.buildZeroString(ddTxtBox.getText(), 2));
		}

		if (!hhTextBox.getText().isEmpty()) {
			sb.append(",").append(DateTimeHelper.buildZeroString(hhTextBox.getText(), 2));
		}

		if (!minTxtBox.getText().isEmpty()) {
			sb.append(",").append(DateTimeHelper.buildZeroString(minTxtBox.getText(), 2));
		}

		if (!ssTxtBox.getText().isEmpty()) {
			sb.append(",").append(DateTimeHelper.buildZeroString(ssTxtBox.getText(), 2));
		}

		if (!msTxtBox.getText().isEmpty()) {
			sb.append(",").append(msTxtBox.getText());
		}

		sb.append(")");
		return sb.toString();
	}

	private String buildDateTimeType() {
		String type = "";
		if (isDate && isTime) {
			type = CQLType.DATETIME.getName();
		} else if (isDate) {
			type = CQLType.DATE.getName();
		} else if (isTime) {
			type = CQLType.TIME.getName();
		}
		return type;
	}

	public void clearFormGroup() {
		yearFormGroup.clear();
		mmFormGroup.clear();
		ddFormGroup.clear();
		hourFormGroup.clear();
		minFormGroup.clear();
		secondsFormGroup.clear();
		millisecFormGroup.clear();
	}

	public void clearDateTime() {
		yyyyTxtBox.clear();
		mmTxtBox.clear();
		ddTxtBox.clear();
		hhTextBox.clear();
		minTxtBox.clear();
		ssTxtBox.clear();
		msTxtBox.clear();
		isDate = false;
		isTime = false;
	}

	public void setDateTimeEnabled(final boolean enabled){
		yyyyTxtBox.setEnabled(enabled);
		mmTxtBox.setEnabled(enabled);
		ddTxtBox.setEnabled(enabled);
		hhTextBox.setEnabled(enabled);
		minTxtBox.setEnabled(enabled);
		ssTxtBox.setEnabled(enabled);
		msTxtBox.setEnabled(enabled);
	}

	public void setDefaultValidationState(){
		yearFormGroup.setValidationState(ValidationState.NONE);
		mmFormGroup.setValidationState(ValidationState.NONE);
		ddFormGroup.setValidationState(ValidationState.NONE);
		hourFormGroup.setValidationState(ValidationState.NONE);
		minFormGroup.setValidationState(ValidationState.NONE);
		secondsFormGroup.setValidationState(ValidationState.NONE);
		millisecFormGroup.setValidationState(ValidationState.NONE);
	}

	public CustomDateTimeTextBox getYyyyTxtBox() {
		return yyyyTxtBox;
	}

	public void setYyyyTxtBox(final CustomDateTimeTextBox yyyyTxtBox) {
		this.yyyyTxtBox = yyyyTxtBox;
	}

	public CustomDateTimeTextBox getMmTxtBox() {
		return mmTxtBox;
	}

	public void setMmTxtBox(final CustomDateTimeTextBox mmTxtBox) {
		this.mmTxtBox = mmTxtBox;
	}

	public CustomDateTimeTextBox getDdTxtBox() {
		return ddTxtBox;
	}

	public void setDdTxtBox(final CustomDateTimeTextBox ddTxtBox) {
		this.ddTxtBox = ddTxtBox;
	}

	public CustomDateTimeTextBox getHhTextBox() {
		return hhTextBox;
	}

	public void setHhTextBox(final CustomDateTimeTextBox hhTextBox) {
		this.hhTextBox = hhTextBox;
	}

	public CustomDateTimeTextBox getMinTxtBox() {
		return minTxtBox;
	}

	public void setMinTxtBox(final CustomDateTimeTextBox minTxtBox) {
		this.minTxtBox = minTxtBox;
	}

	public CustomDateTimeTextBox getSsTxtBox() {
		return ssTxtBox;
	}

	public void setSsTxtBox(final CustomDateTimeTextBox ssTxtBox) {
		this.ssTxtBox = ssTxtBox;
	}

	public CustomDateTimeTextBox getMsTxtBox() {
		return msTxtBox;
	}

	public void setMsTxtBox(final CustomDateTimeTextBox msTxtBox) {
		this.msTxtBox = msTxtBox;
	}

	public FormGroup getYearFormGroup() {
		return yearFormGroup;
	}

	public void setYearFormGroup(final FormGroup yearFormGroup) {
		this.yearFormGroup = yearFormGroup;
	}

	public FormGroup getMmFormGroup() {
		return mmFormGroup;
	}

	public void setMmFormGroup(final FormGroup mmFormGroup) {
		this.mmFormGroup = mmFormGroup;
	}

	public FormGroup getDdFormGroup() {
		return ddFormGroup;
	}

	public void setDdFormGroup(final FormGroup ddFormGroup) {
		this.ddFormGroup = ddFormGroup;
	}

	public FormGroup getHourFormGroup() {
		return hourFormGroup;
	}

	public void setHourFormGroup(final FormGroup hourFormGroup) {
		this.hourFormGroup = hourFormGroup;
	}

	public FormGroup getMinFormGroup() {
		return minFormGroup;
	}

	public void setMinFormGroup(final FormGroup minFormGroup) {
		this.minFormGroup = minFormGroup;
	}

	public FormGroup getSecondsFormGroup() {
		return secondsFormGroup;
	}

	public void setSecondsFormGroup(final FormGroup secondsFormGroup) {
		this.secondsFormGroup = secondsFormGroup;
	}

	public FormGroup getMillisecFormGroup() {
		return millisecFormGroup;
	}

	public void setMillisecFormGroup(final FormGroup millisecFormGroup) {
		this.millisecFormGroup = millisecFormGroup;
	}

}
