package mat.client.shared;

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


public class MatCheckBoxCell extends AbstractEditableCell<Boolean, Boolean> {

	/**
	 * An html string representation of a checked input box.
	 */
	
	private String checkBoxTitle = "Click checkbox to select";
	


	/** The depends on selection. */
	private final boolean dependsOnSelection;
	
	/** The handles selection. */
	private final boolean handlesSelection;
	
	/** The is used. */
	private boolean isUsed;
	

	/**
	 * Checks if is used.
	 * 
	 * @return true, if is used
	 */
	public boolean isUsed() {
		return isUsed;
	}
	
	/**
	 * Sets the used.
	 * 
	 * @param isUsed
	 *            the new used
	 */
	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}
	/**
	 * Construct a new {@link MatCheckBoxCell}.
	 */
	public MatCheckBoxCell() {
		this(false);
	}
	
	/**
	 * Instantiates a new mat check box cell.
	 *
	 * @param dependsOnSelection the depends on selection
	 * @param handlesSelection the handles selection
	 * @param isEditable the is editable
	 */
	public MatCheckBoxCell(boolean dependsOnSelection, boolean handlesSelection, boolean isEditable) {
		super(BrowserEvents.CHANGE, BrowserEvents.KEYDOWN);
		this.dependsOnSelection = dependsOnSelection;
		this.handlesSelection = handlesSelection;
		this.isUsed = isEditable;
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
	
	public MatCheckBoxCell(boolean dependsOnSelection, boolean handlesSelection, String title) {
		super(BrowserEvents.CHANGE, BrowserEvents.KEYDOWN);
		this.dependsOnSelection = dependsOnSelection;
		this.handlesSelection = handlesSelection;
		checkBoxTitle = title;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.cell.client.AbstractCell#dependsOnSelection()
	 */
	@Override
	public boolean dependsOnSelection() {
		return dependsOnSelection;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.cell.client.AbstractCell#handlesSelection()
	 */
	@Override
	public boolean handlesSelection() {
		return handlesSelection;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.cell.client.AbstractCell#onBrowserEvent(com.google.gwt.cell.client.Cell.Context, com.google.gwt.dom.client.Element, java.lang.Object, com.google.gwt.dom.client.NativeEvent, com.google.gwt.cell.client.ValueUpdater)
	 */
	@Override
	public void onBrowserEvent(Context context, Element parent, Boolean value, 
			NativeEvent event, ValueUpdater<Boolean> valueUpdater) {
		String type = event.getType();

		boolean enterPressed = BrowserEvents.KEYDOWN.equals(type)
		&& event.getKeyCode() == KeyCodes.KEY_ENTER;
		if ((BrowserEvents.CHANGE.equals(type) || enterPressed) && !isUsed) {
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
	
	/* (non-Javadoc)
	 * @see com.google.gwt.cell.client.AbstractEditableCell#isEditing(com.google.gwt.cell.client.Cell.Context, com.google.gwt.dom.client.Element, java.lang.Object)
	 */
	@Override
	public boolean isEditing(com.google.gwt.cell.client.Cell.Context context,
			Element parent, Boolean value) {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.cell.client.AbstractCell#render(com.google.gwt.cell.client.Cell.Context, java.lang.Object, com.google.gwt.safehtml.shared.SafeHtmlBuilder)
	 */
	@Override
	public void render(Context context, Boolean value, SafeHtmlBuilder sb) {
		
		/** The Constant INPUT_CHECKED. */
		SafeHtml INPUT_CHECKED = SafeHtmlUtils.fromSafeConstant(
				"<input type=\"checkbox\" tabindex=\"0\" title=\" " + checkBoxTitle + "\" checked/>");

		/**
		 * An html string representation of an unchecked input box.
		 */
		SafeHtml INPUT_UNCHECKED = SafeHtmlUtils.fromSafeConstant(
				"<input type=\"checkbox\" tabindex=\"0\" title=\"" + checkBoxTitle + "\" />");

		/** The Constant INPUT_UNCHECKED_DISABLED. */
		SafeHtml INPUT_UNCHECKED_DISABLED = SafeHtmlUtils.fromSafeConstant(
				"<input type=\"checkbox\" tabindex=\"0\" disabled=\"disabled\"  title=\"" + checkBoxTitle + "\" />");
		
		/** The Constant INPUT_CHECKED_DISABLED. */
		SafeHtml INPUT_CHECKED_DISABLED = SafeHtmlUtils.fromSafeConstant(
				"<input type=\"checkbox\" tabindex=\"0\" disabled=\"disabled\"  title=\"" + checkBoxTitle + "\" checked/>");
		
		Boolean viewData = getViewData(context.getKey());
		if (viewData != null && viewData.equals(value)) {
			clearViewData(context.getKey());
			viewData = null;
		}
		if (!isUsed) {    
			if (value != null && ((viewData != null) ? viewData : value)) {
				sb.append(INPUT_CHECKED);
			} else {
				sb.append(INPUT_UNCHECKED);
			}
		} else {
			if (value != null && ((viewData != null) ? viewData : value)) {
				sb.append(INPUT_CHECKED_DISABLED);
			} else {
				sb.append(INPUT_UNCHECKED_DISABLED);
			}
		}
		
	}
	
	public void setTitle(String title) {
		this.checkBoxTitle = title; 
	}
}

