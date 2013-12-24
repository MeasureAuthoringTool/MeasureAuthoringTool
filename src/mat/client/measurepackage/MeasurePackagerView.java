package mat.client.measurepackage;

import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.WarningMessageDisplay;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;


public class MeasurePackagerView implements MeasurePackagePresenter.PackageView{
	/** The error messages. */
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	/** The measure package success msg. */
	private SuccessMessageDisplay measurePackageSuccessMsg = new SuccessMessageDisplay();
	/** The measure package warning msg. */
	private WarningMessageDisplay measurePackageWarningMsg = new WarningMessageDisplay();
	/** The package success messages. */
	private SuccessMessageDisplay packageSuccessMessages = new SuccessMessageDisplay();
	/** The package measure. */
	private PrimaryButton packageMeasure = new PrimaryButton("Create Measure Package", "primaryButton");
	/** The content. */
	private FlowPanel content = new FlowPanel();
	/**
	 * Constructor
	 */
	public MeasurePackagerView() {
		content.add(new SpacerWidget());
		content.add(new SpacerWidget());
		content.add(new SpacerWidget());
		
		content.add(measurePackageSuccessMsg);
		content.add(measurePackageWarningMsg);
		packageMeasure.setTitle("Create Measure Package");
		content.add(packageMeasure);
		content.add(new SpacerWidget());
		content.add(new SpacerWidget());
		content.setStyleName("contentPanel");
		
	}
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}
	
	@Override
	public SuccessMessageDisplayInterface getMeasurePackageSuccessMsg() {
		return measurePackageSuccessMsg;
	}
	
	@Override
	public WarningMessageDisplay getMeasurePackageWarningMsg() {
		return measurePackageWarningMsg;
	}
	
	@Override
	public ErrorMessageDisplayInterface getPackageErrorMessageDisplay() {
		return errorMessages;
	}
	
	@Override
	public HasClickHandlers getPackageMeasureButton() {
		return packageMeasure;
	}
	
	@Override
	public SuccessMessageDisplayInterface getPackageSuccessMessageDisplay() {
		return packageSuccessMessages;
	}
	@Override
	public final Widget asWidget() {
		return content;
	}
	
}
