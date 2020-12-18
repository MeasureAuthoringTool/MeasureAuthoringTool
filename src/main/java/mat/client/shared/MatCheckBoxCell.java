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

	/**
	 * Construct a new {@link MatCheckBoxCell}.
	 */
	public MatCheckBoxCell() {
		this(false);
	}

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

		boolean spacePressed = BrowserEvents.KEYDOWN.equals(type)
		                       && (event.getKeyCode() == KeyCodes.KEY_SPACE);
		boolean isChange = spacePressed || BrowserEvents.CHANGE.equals(type);
		InputElement input = parent.getFirstChild().cast();
		Boolean isChecked = input.isChecked();

		if (spacePressed) {
			if (spacePressed && (handlesSelection() || !dependsOnSelection())) {
				isChecked = !isChecked;
				input.setChecked(isChecked);
			}
		}
		if (spacePressed || isChange) {
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
			event.stopPropagation();
			event.preventDefault();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.cell.client.AbstractEditableCell#isEditing(com.google.gwt.cell.client.Cell.Context, com.google.gwt.dom.client.Element, java.lang.Object)
	 */
	@Override
	public boolean isEditing(com.google.gwt.cell.client.Cell.Context context,
			Element parent, Boolean value) {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.cell.client.AbstractCell#render(com.google.gwt.cell.client.Cell.Context, java.lang.Object, com.google.gwt.safehtml.shared.SafeHtmlBuilder)
	 */
	@Override
	public void render(Context context, Boolean value, SafeHtmlBuilder sb) {
		
		/** The Constant INPUT_CHECKED. */
		SafeHtml INPUT_CHECKED = SafeHtmlUtils.fromSafeConstant(
				"<input type=\"checkbox\"  aria-label=\" " + checkBoxTitle + "\" checked/>");

		/**
		 * An html string representation of an unchecked input box.
		 */
		SafeHtml INPUT_UNCHECKED = SafeHtmlUtils.fromSafeConstant(
				"<input type=\"checkbox\"  aria-label=\"" + checkBoxTitle + "\" />");


		Boolean viewData = getViewData(context.getKey());
		if (viewData != null && viewData.equals(value)) {
			clearViewData(context.getKey());
			viewData = null;
		}

			if (value != null && ((viewData != null) ? viewData : value)) {
				sb.append(INPUT_CHECKED);
			} else {
				sb.append(INPUT_UNCHECKED);
			}

		
	}
	
	public void setTitle(String title) {
		this.checkBoxTitle = title; 
	}
}

