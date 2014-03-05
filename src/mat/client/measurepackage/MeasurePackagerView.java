package mat.client.measurepackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mat.client.ImageResources;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.LabelBuilder;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.WarningMessageDisplay;
import mat.model.QualityDataSetDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MeasurePackagerView implements MeasurePackagePresenter.PackageView {

	/** The add qdm right. */
	private FocusPanel addQDMRight = buildAddButton(ImageResources.INSTANCE
			.addRight());

	/** The add qdm left. */
	private FocusPanel addQDMLeft = buildAddButton(ImageResources.INSTANCE
			.addLeft());

	/** The add all qdm right. */
	private FocusPanel addAllQDMRight = buildDoubleAddButton(ImageResources.INSTANCE
			.addAllRight());

	/** The add all qdm left. */
	private FocusPanel addAllQDMLeft = buildDoubleAddButton(ImageResources.INSTANCE
			.addAllLeft());

	/** The qdm element id map. */
	private Map<String, QualityDataSetDTO> qdmElementIdMap = new HashMap<String, QualityDataSetDTO>();

	/** The error messages. */
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	/** The measure package success msg. */
	private SuccessMessageDisplay measurePackageSuccessMsg = new SuccessMessageDisplay();
	/** The measure package warning msg. */
	private WarningMessageDisplay measurePackageWarningMsg = new WarningMessageDisplay();
	/** The package success messages. */
	private SuccessMessageDisplay packageSuccessMessages = new SuccessMessageDisplay();
	/** The package measure. */
	private PrimaryButton packageMeasure = new PrimaryButton(
			"Create Measure Package", "primaryButton");
	/** The content. */
	private FlowPanel content = new FlowPanel();

	/** The qdm elements list box. */
	private ListBox qdmElementsListBox = new ListBox();

	/** The supp elements list box. */
	private ListBox suppElementsListBox = new ListBox();

	/** The add qdm element button panel. */
	private Widget addQDMElementButtonPanel = buildQDMElementAddButtonWidget();

	/** The qdm elements panel. */
	private FlowPanel qdmElementsPanel = new FlowPanel();

	/** The supp elements panel. */
	private FlowPanel suppElementsPanel = new FlowPanel();

	/** The add qdm elements to measure. */
	private PrimaryButton addQDMElementsToMeasure = new PrimaryButton("Save Supplemental Data Elements", "primaryButton");

	/** The qdm tab name. */
	private Label qdmTabName = new Label("Supplemental Data Elements");

	/** The list visible count. */
	private int listVisibleCount = 10;

	/**
	 * Constructor
	 */
	public MeasurePackagerView() {
		addQDMElementLeftRightClickHandlers();
		Panel topQDMElementContainer = buildQDMElementLeftRightPanel();
		content.add(new SpacerWidget());
		content.add(new SpacerWidget());
		content.add(new SpacerWidget());
		content.add(topQDMElementContainer);
		content.add(new SpacerWidget());
		content.add(new SpacerWidget());
		content.add(measurePackageSuccessMsg);
		content.add(measurePackageWarningMsg);
		packageMeasure.setTitle("Create Measure Package");
		content.add(packageMeasure);
		content.add(new SpacerWidget());
		content.add(new SpacerWidget());
		qdmElementsListBox.setVisibleItemCount(listVisibleCount);
		suppElementsListBox.setVisibleItemCount(listVisibleCount);
		content.setStyleName("contentPanel");

	}

	// QDM elements
	/**
	 * Adds the qdm element left right click handlers.
	 */
	private void addQDMElementLeftRightClickHandlers() {

		addQDMRight.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				addQDMElementRight();
			}
		});
		addQDMLeft.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				addQDMElementLeft();
			}
		});
		addAllQDMRight.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				addAllQDMElementsRight();
			}
		});
		addAllQDMLeft.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				addAllQDMElementsLeft();
			}
		});
	}

	// QDM elements
	/**
	 * Builds the qdm element left right panel.
	 * 
	 * @return the panel
	 */
	private Panel buildQDMElementLeftRightPanel() {
		SimplePanel panel = new SimplePanel();
		FlowPanel qdmTopContainer = new FlowPanel();
		VerticalPanel vPanel = new VerticalPanel();
		// qdmTopContainer.setWidth("700px");
		qdmTabName.setStyleName("valueSetHeader");
		qdmTopContainer.add(qdmTabName);
		// qdmTopContainer.add(qdmErrorMessages);
		// qdmTopContainer.add(suppDataSuccessMessages);
		qdmElementsPanel.addStyleName("newColumn");
		suppElementsPanel.addStyleName("newColumn");
		addQDMElementButtonPanel.addStyleName("column");
		// qdmElementsListBox.setStyleName("secondLabel");
		Widget qdmElementsLabel = LabelBuilder.buildLabel(qdmElementsListBox,
				"QDM Elements");
		qdmElementsLabel.addStyleName("bold");
		qdmElementsPanel.add(qdmElementsLabel);
		qdmElementsPanel.add(qdmElementsListBox);
		Widget suppElementsLabel = LabelBuilder.buildLabel(suppElementsListBox,
				"Supplemental Data Elements");
		suppElementsLabel.addStyleName("bold");
		suppElementsPanel.add(suppElementsLabel);
		suppElementsPanel.add(suppElementsListBox);

		SimplePanel wrapper = new SimplePanel();
		wrapper.setStylePrimaryName("measurePackageAddButtonHolder");
		wrapper.add(addQDMElementsToMeasure);

		suppElementsPanel.add(new SpacerWidget());
		suppElementsPanel.add(wrapper);
		vPanel.add(suppElementsPanel);
		qdmTopContainer.add(qdmElementsPanel);
		qdmTopContainer.add(addQDMElementButtonPanel);
		qdmTopContainer.add(vPanel);
		SpacerWidget spacer = new SpacerWidget();
		spacer.setStylePrimaryName("clearBoth");
		qdmTopContainer.add(spacer);
		qdmTopContainer.setStylePrimaryName("valueSetSearchPanel");
		panel.setStylePrimaryName("measurePackageLeftRightPanel");
		panel.add(qdmTopContainer);
		return panel;
	}

	private Widget buildQDMElementAddButtonWidget() {
		FlowPanel panel = new FlowPanel();
		panel.setStyleName("measurePackageAddButtonPanel");
		addQDMRight.setTitle("Add QDM element to Supplemental Data Elements");
		addQDMRight.getElement().setAttribute("alt","Add QDM element to Supplemental Data Elements");
		addQDMLeft.setTitle("Remove QDM Element from Supplemental Data Elements");
		addQDMLeft.getElement().setAttribute("alt","Remove QDM Element from Supplemental Data Elements");
		addAllQDMRight.setTitle("Add all QDM Elements to Supplemental Data Elements");
		addAllQDMRight.getElement().setAttribute("alt","Add all QDM Elements to Supplemental Data Elements");
		addAllQDMLeft.setTitle("Remove all QDM Elements from Supplemental Data Elements");
		addAllQDMLeft.getElement().setAttribute("alt","Remove all QDM Elements from Supplemental Data Elements");
		panel.add(addQDMRight);
		panel.add(addQDMLeft);
		panel.add(new SpacerWidget());
		panel.add(addAllQDMRight);
		panel.add(addAllQDMLeft);
		return panel;
	}

	private FocusPanel buildAddButton(ImageResource imageUrl) {
		FocusPanel fPanel = new FocusPanel();
		fPanel.add(new Image(imageUrl));
		fPanel.setStylePrimaryName("newGreySecondaryButton");
		fPanel.addStyleName("measurePackageAddButton");
		return fPanel;
	}

	private FocusPanel buildDoubleAddButton(ImageResource imageUrl) {
		FocusPanel focusPanel = new FocusPanel();
		FlowPanel fPanel = new FlowPanel();
		fPanel.add(new Image(imageUrl));
		fPanel.add(new Image(imageUrl));
		focusPanel.add(fPanel);
		focusPanel.setStylePrimaryName("newGreySecondaryButton");
		focusPanel.addStyleName("measurePackageAddButton");
		return focusPanel;
	}

	@Override
	public final void setQDMElementsInSuppElements(final List<QualityDataSetDTO> clauses) {
		setQDMElementsItems(suppElementsListBox, clauses);
	}

	@Override
	public final List<QualityDataSetDTO> getQDMElementsInSuppElements() {
		return getQDMElementsItems(suppElementsListBox);
	}

	// QDM elements
	/**
	 * Sets the qdm elements items.
	 * 
	 * @param lb
	 *            the lb
	 * @param valuesArg
	 *            the values arg
	 */
	private void setQDMElementsItems(final ListBox lb,final List<QualityDataSetDTO> valuesArg) {
		List<QualityDataSetDTO> values = new ArrayList<QualityDataSetDTO>();
		values.addAll(valuesArg);
		Collections.sort(values, new QualityDataSetDTO.Comparator());
		lb.clear();
		for (QualityDataSetDTO nvp : values) {
			lb.addItem(nvp.getQDMElement(), nvp.getId() + nvp.getDataType());
			qdmElementIdMap.put(nvp.getId() + nvp.getDataType(), nvp);
		}
	}

	// QDM elements
	/**
	 * Gets the qDM elements items.
	 * 
	 * @param lb
	 *            the lb
	 * @return the qDM elements items
	 */
	private List<QualityDataSetDTO> getQDMElementsItems(final ListBox lb) {
		List<QualityDataSetDTO> list = new ArrayList<QualityDataSetDTO>();
		for (int i = 0; i < lb.getItemCount(); i++) {
			QualityDataSetDTO detail = qdmElementIdMap.get(lb.getValue(i));
			list.add(detail);
		}
		return list;
	}

	private void addQDMElementRight() {
		QualityDataSetDTO nvp = getQDMElementSelectedValue(qdmElementsListBox);
		if (nvp != null) {
			removeQDMElementItem(qdmElementsListBox, nvp);
			addQDMElementItem(suppElementsListBox, nvp);
		}
	}

	/**
	 * Adds the qdm element left.
	 */
	private void addQDMElementLeft() {
		QualityDataSetDTO nvp = getQDMElementSelectedValue(suppElementsListBox);
		if (nvp != null) {
			removeQDMElementItem(suppElementsListBox, nvp);
			addQDMElementItem(qdmElementsListBox, nvp);
		}
	}

	/**
	 * Adds the all qdm elements right.
	 */
	private void addAllQDMElementsRight() {
		addQDMElementItems(suppElementsListBox,
				getQDMElementsItems(qdmElementsListBox));
		qdmElementsListBox.clear();
	}

	/**
	 * Adds the all qdm elements left.
	 */
	private void addAllQDMElementsLeft() {
		addQDMElementItems(qdmElementsListBox,
				getQDMElementsItems(suppElementsListBox));
		suppElementsListBox.clear();
	}

	// QDM elements
	/**
	 * Adds the qdm element item.
	 * 
	 * @param lb
	 *            the lb
	 * @param nvp
	 *            the nvp
	 */
	private void addQDMElementItem(final ListBox lb, final QualityDataSetDTO nvp) {
		List<QualityDataSetDTO> list = getQDMElementsItems(lb);
		list.add(nvp);
		Collections.sort(list, new QualityDataSetDTO.Comparator());
		setQDMElementsItems(lb, list);
	}

	// QDM elements
	/**
	 * Adds the qdm element items.
	 * 
	 * @param lb
	 *            the lb
	 * @param nvpList
	 *            the nvp list
	 */
	private void addQDMElementItems(final ListBox lb,final List<QualityDataSetDTO> nvpList) {
		List<QualityDataSetDTO> list = getQDMElementsItems(lb);
		list.addAll(nvpList);
		setQDMElementsItems(lb, list);
	}

	// QDM elements
	/**
	 * Gets the qDM element selected value.
	 * 
	 * @param lb
	 *            the lb
	 * @return the qDM element selected value
	 */
	private QualityDataSetDTO getQDMElementSelectedValue(final ListBox lb) {
		int index = lb.getSelectedIndex();
		QualityDataSetDTO nvp = null;
		if (index >= 0) {
			nvp = qdmElementIdMap.get(lb.getValue(index));
		}
		return nvp;
	}

	// QDM elements
	/**
	 * Removes the qdm element item.
	 * 
	 * @param lb
	 *            the lb
	 * @param nvp
	 *            the nvp
	 */
	private void removeQDMElementItem(final ListBox lb,final QualityDataSetDTO nvp) {
		for (int i = 0; i < lb.getItemCount(); i++) {
			if (lb.getValue(i).equals(nvp.getId() + nvp.getDataType())) {
				lb.removeItem(i);
				break;
			}
		}
	}

	@Override
	public final void setQDMElements(final List<QualityDataSetDTO> clauses) {
		setQDMElementsItems(qdmElementsListBox, clauses);
	}

	@Override
	public final List<QualityDataSetDTO> getQDMElements() {
		return getQDMElementsItems(qdmElementsListBox);
	}

	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}

	@Override
	public SuccessMessageDisplayInterface getMeasurePackageSuccessMsg() {
		return measurePackageSuccessMsg;
	}

	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#getMeasurePackageWarningMsg()
	 */
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
