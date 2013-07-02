package mat.client.shared;

import mat.model.QualityDataSetDTO;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

public class MatCheckBoxCell extends AbstractEditableCell<Boolean,Boolean> {

	/**
	 * An html string representation of a checked input box.
	 */
	private static final SafeHtml INPUT_CHECKED = SafeHtmlUtils.fromSafeConstant("<input type=\"checkbox\" checked/>");

	/**
	 * An html string representation of an unchecked input box.
	 */
	private static final SafeHtml INPUT_UNCHECKED = SafeHtmlUtils.fromSafeConstant("<input type=\"checkbox\"/>");

	private static final SafeHtml INPUT_UNCHECKED_DISABLED = SafeHtmlUtils.fromSafeConstant("<input type=\"checkbox\" disabled=\"disabled\"/>");

	private final boolean dependsOnSelection;
	private final boolean handlesSelection;
	private boolean isUsed;

	public boolean isUsed() {
		return isUsed;
	}
	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}
	/**
	 * Construct a new {@link MatCheckBoxCell}.
	 */
	public MatCheckBoxCell() {
		this(false);
	}
	public MatCheckBoxCell(QualityDataSetDTO dataSetDTO){
		this(false);
		this.isUsed = dataSetDTO.isUsed(); 
		
	}

	/**
	 * Construct a new {@link MatCheckBoxCell} that optionally controls selection.
	 *
	 * @param isSelectBox true if the cell controls the selection state
	 * @deprecated use {@link #CheckboxCell(boolean, boolean)} instead
	 */
	@Deprecated
	public MatCheckBoxCell(boolean isSelectBox) {
		this(isSelectBox, isSelectBox);
	}

	/**
	 * Construct a new {@link MatCheckBoxCell} that optionally controls selection.
	 *
	 * @param dependsOnSelection true if the cell depends on the selection state
	 * @param handlesSelection true if the cell modifies the selection state
	 */
	public MatCheckBoxCell(boolean dependsOnSelection, boolean handlesSelection) {
		super(BrowserEvents.CHANGE, BrowserEvents.KEYDOWN);
		this.dependsOnSelection = dependsOnSelection;
		this.handlesSelection = handlesSelection;
	}

	@Override
	public boolean dependsOnSelection() {
		return dependsOnSelection;
	}

	@Override
	public boolean handlesSelection() {
		return handlesSelection;
	}

	@Override
	public void onBrowserEvent(Context context, Element parent, Boolean value, 
			NativeEvent event, ValueUpdater<Boolean> valueUpdater) {
		String type = event.getType();

		boolean enterPressed = BrowserEvents.KEYDOWN.equals(type)
		&& event.getKeyCode() == KeyCodes.KEY_ENTER;
		if (BrowserEvents.CHANGE.equals(type) || enterPressed) {
			InputElement input = parent.getFirstChild().cast();
			Boolean isChecked = input.isChecked();

			/*
			 * Toggle the value if the enter key was pressed and the cell handles
			 * selection or doesn't depend on selection. If the cell depends on
			 * selection but doesn't handle selection, then ignore the enter key and
			 * let the SelectionEventManager determine which keys will trigger a
			 * change.
			 */
			if (enterPressed && (handlesSelection() || !dependsOnSelection())) {
				isChecked = !isChecked;
				input.setChecked(isChecked);
			}

			/*
			 * Save the new value. However, if the cell depends on the selection, then
			 * do not save the value because we can get into an inconsistent state.
			 */
			if (value != isChecked && !dependsOnSelection()) {
				setViewData(context.getKey(), isChecked);
			} else {
				clearViewData(context.getKey());
			}

			if (valueUpdater != null) {
				valueUpdater.update(isChecked);
			}
		}
	}
	
	@Override
	public boolean isEditing(com.google.gwt.cell.client.Cell.Context context,
			Element parent, Boolean value) {
		return false;
	}
	
	@Override
	public void render(Context context, Boolean value, SafeHtmlBuilder sb) {
		Boolean viewData = getViewData(context.getKey());
		if (viewData != null && viewData.equals(value)) {
			clearViewData(context.getKey());
			viewData = null;
		}
		if(!isUsed){    
			if (value != null && ((viewData != null) ? viewData : value)) {
				sb.append(INPUT_CHECKED);
			} else {
				sb.append(INPUT_UNCHECKED);
			}
		}else{
			sb.append(INPUT_UNCHECKED_DISABLED);
			//sb.append(INPUT_UNCHECKED);

		}
	}

	private boolean checkForEnable(){

		return MatContext.get().getMeasureLockService().checkForEditPermission();
	}
}

