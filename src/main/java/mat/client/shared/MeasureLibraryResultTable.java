package mat.client.shared;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.gwt.ButtonCell;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.MultiSelectionModel;

import mat.client.Mat;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.measure.MeasureSearchView.Observer;
import mat.client.util.CellTableUtility;
import mat.shared.ClickableSafeHtmlCell;

public class MeasureLibraryResultTable {
	
	private MultiSelectionModel<ManageMeasureSearchModel.Result> selectionModel;
	private CellTable<ManageMeasureSearchModel.Result> table;
	List<ManageMeasureSearchModel.Result> selectedList;
	private Observer observer;
	
	public CellTable<ManageMeasureSearchModel.Result> addColumnToTable(String measureListLabel, CellTable<ManageMeasureSearchModel.Result> table, List<ManageMeasureSearchModel.Result> selectedList, boolean displayBulkExport,HasSelectionHandlers<ManageMeasureSearchModel.Result> fireEvent) {
		this.table = table;
		this.selectedList = selectedList;
		Label measureSearchHeader = new Label(measureListLabel);
		measureSearchHeader.getElement().setId("measureSearchHeader_Label");
		measureSearchHeader.setStyleName("recentSearchHeader");
		com.google.gwt.dom.client.TableElement elem = table.getElement().cast();
		measureSearchHeader.getElement().setAttribute("tabIndex", "0");
		TableCaptionElement caption = elem.createCaption();
		caption.appendChild(measureSearchHeader.getElement());

		selectionModel = new MultiSelectionModel<ManageMeasureSearchModel.Result>();
		table.setSelectionModel(selectionModel);

		// Measure Name Column
		Column<ManageMeasureSearchModel.Result, SafeHtml> measureName = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new ClickableSafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
				return getMeasureNameColumnToolTip(object);
			}
		};
		measureName.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, SafeHtml>() {
			@Override
			public void update(int index, ManageMeasureSearchModel.Result object, SafeHtml value) {
				SelectionEvent.fire(fireEvent, object);
			}
		});
		table.addColumn(measureName,
				SafeHtmlUtils.fromSafeConstant("<span title='Measure Name Column'>" + "Measure Name" + "</span>"));

		// Version Column
		Column<ManageMeasureSearchModel.Result, SafeHtml> version = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new MatSafeHTMLCell()) {
			@Override
			public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
				return CellTableUtility.getColumnToolTip(object.getVersion());
			}
		};
		table.addColumn(version, SafeHtmlUtils.fromSafeConstant("<span title='Version'>" + "Version" + "</span>"));

		ButtonCell buttonCell = new ButtonCell(ButtonType.LINK);
		Column<ManageMeasureSearchModel.Result, String> draftOrVersionCol = new Column<ManageMeasureSearchModel.Result, String>(
				buttonCell) {

			@Override
			public String getValue(ManageMeasureSearchModel.Result object) {
				return object.getShortName();
			}

			@Override
			public void render(Context context, ManageMeasureSearchModel.Result object, SafeHtmlBuilder sb) {
				if (object.isDraftable()) {
					sb.appendHtmlConstant(
							"<button class=\"btn btn-link\" type=\"button\" title =\"Click to create draft\" tabindex=\"0\">");
					sb.appendHtmlConstant("<i class=\"fa fa-pencil-square-o fa-lg\" style=\"margin-left: 15px;\"></i>");
					sb.appendHtmlConstant("<span class=\"invisibleButtonText\">Create Draft</span>");
					sb.appendHtmlConstant("</button>");
				} else if (object.isVersionable()) {
					sb.appendHtmlConstant(
							"<button class=\"btn btn-link\" type=\"button\" tabindex=\"0\" title =\"Click to create version\" style=\"color: goldenrod;\" >");
					sb.appendHtmlConstant("<i class=\"fa fa-star fa-lg\" style=\"margin-left: 15px;\"></i>");
					sb.appendHtmlConstant("<span class=\"invisibleButtonText\">Create Version</span>");
					sb.appendHtmlConstant("</button>");
				}

			}

			@Override
			public void onBrowserEvent(Context context, Element elem, ManageMeasureSearchModel.Result object,
					NativeEvent event) {

				String type = event.getType();
				if (type.equalsIgnoreCase(BrowserEvents.CLICK)) {
					if (!object.isDraftable() && !object.isVersionable()) {
						event.preventDefault();
					} else {
						observer.onCreateClicked(object);
					}
				} else {
					if (!object.isDraftable() && !object.isVersionable()) {
						event.preventDefault();
					}
				}

			}

		};

		table.addColumn(draftOrVersionCol, SafeHtmlUtils
				.fromSafeConstant("<span title='Create Version/Draft'>" + "Create Version/Draft" + "</span>"));

		// History
		Cell<String> historyButton = new MatButtonCell("Click to view history", "btn btn-link", "fa fa-clock-o fa-lg",
				"History");
		Column<Result, String> historyColumn = new Column<ManageMeasureSearchModel.Result, String>(historyButton) {
			@Override
			public String getValue(ManageMeasureSearchModel.Result object) {
				return "";
			}
		};
		historyColumn.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, String>() {
			@Override
			public void update(int index, ManageMeasureSearchModel.Result object, String value) {
				observer.onHistoryClicked(object);
			}
		});
		table.addColumn(historyColumn,
				SafeHtmlUtils.fromSafeConstant("<span title='History'>" + "History" + "</span>"));

		// Edit
		Column<ManageMeasureSearchModel.Result, SafeHtml> editColumn = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new ClickableSafeHtmlCell()) {
			@Override
			public SafeHtml getValue(Result object) {
				return getEditColumnToolTip(object);
			}
		};
		editColumn.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, SafeHtml>() {
			@Override
			public void update(int index, Result object, SafeHtml value) {
				SelectionEvent.fire(fireEvent, object);
			}
		});
		table.addColumn(editColumn, SafeHtmlUtils.fromSafeConstant("<span title='Edit'>" + "Edit" + "</span>"));

		// Share
		Column<ManageMeasureSearchModel.Result, SafeHtml> shareColumn = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new ClickableSafeHtmlCell()) {
			@Override
			public SafeHtml getValue(Result object) {
				return getShareColumnToolTip(object);
			}
		};
		shareColumn.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, SafeHtml>() {
			@Override
			public void update(int index, ManageMeasureSearchModel.Result object, SafeHtml value) {
				if (object.isSharable())
					observer.onShareClicked(object);
			}
		});
		table.addColumn(shareColumn, SafeHtmlUtils.fromSafeConstant("<span title='Share'>" + "Share" + "</span>"));

		// Clone
		Column<ManageMeasureSearchModel.Result, SafeHtml> cloneColumn = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new ClickableSafeHtmlCell()) {
			@Override
			public SafeHtml getValue(Result object) {
				return getCloneColumnHTML(object);
			}
		};
		cloneColumn.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, SafeHtml>() {
			@Override
			public void update(int index, ManageMeasureSearchModel.Result object, SafeHtml value) {
				if (object.isClonable())
					Mat.showLoadingMessage();
					observer.onCloneClicked(object);
			}
		});
		table.addColumn(cloneColumn, SafeHtmlUtils.fromSafeConstant("<span title='Clone'>" + "Clone" + "</span>"));

		// Export Column header

		Header<SafeHtml> bulkExportColumnHeader = getBulkExportColumnHeader();
		bulkExportColumnHeader.setUpdater(new ValueUpdater<SafeHtml>() {
			@Override
			public void update(SafeHtml value) {
				clearBulkExportCheckBoxes();
			}
		});

		table.addColumn(new Column<Result, Result>(getCompositeCellForBulkExport(displayBulkExport)) {
			@Override
			public Result getValue(Result object) {
				return object;
			}
		}, bulkExportColumnHeader);
		return table;
	}
	
	/**
	 * Gets the measure name column tool tip.
	 *
	 * @param object the object
	 * @return the measure name column tool tip
	 */
	private SafeHtml getMeasureNameColumnToolTip(ManageMeasureSearchModel.Result object){
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		String cssClass = "customCascadeButton";
		if (object.isMeasureFamily()) {
			sb.appendHtmlConstant("<div id='container' tabindex=\"-1\"><a href=\"javascript:void(0);\" "
					+ "style=\"text-decoration:none\" tabindex=\"-1\">"
					+ "<button id='div1' class='textEmptySpaces' tabindex=\"-1\" disabled='disabled'></button>");
			sb.appendHtmlConstant("<span id='div2' title=\" " + object.getName() + "\" tabindex=\"0\">" + object.getName() + "</span>");
			sb.appendHtmlConstant("</a></div>");
		} else {
			sb.appendHtmlConstant("<div id='container' tabindex=\"-1\"><a href=\"javascript:void(0);\" "
					+ "style=\"text-decoration:none\" tabindex=\"-1\" >");
			sb.appendHtmlConstant("<button id='div1' type=\"button\" title=\""
					+ object.getName() + "\" tabindex=\"-1\" class=\" " + cssClass + "\"></button>");
			sb.appendHtmlConstant("<span id='div2' title=\" " + object.getName() + "\" tabindex=\"0\">" + object.getName() + "</span>");
			sb.appendHtmlConstant("</a></div>");
		}
		return sb.toSafeHtml();		
	}
	
	/**
	 * Clear bulk export check boxes.
	 */
	public void clearBulkExportCheckBoxes(){
		List<Result> displayedItems = new ArrayList<Result>();
		displayedItems.addAll(selectedList);
		selectedList.clear();
		for (ManageMeasureSearchModel.Result msg : displayedItems) {
			selectionModel.setSelected(msg, false);
		}
		table.redraw();
		observer.onClearAllBulkExportClicked();
	}
	
	/**
	 * Gets the history column tool tip.
	 *
	 * @param object the object
	 * @return the history column tool tip
	 */
	private SafeHtml getEditColumnToolTip(Result object){
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		String title;
		String cssClass = "btn btn-link";
		String iconCss;
		if (object.isEditable()) {
			if (object.isMeasureLocked()) {
				String emailAddress = object.getLockedUserInfo().getEmailAddress();
				title = "Measure in use by " + emailAddress;
				iconCss = "fa fa-lock fa-lg";
			} else {
				title = "Edit";
				iconCss = "fa fa-pencil fa-lg";
			}
			sb.appendHtmlConstant("<button type=\"button\" title='"
					+ title + "' tabindex=\"0\" class=\" " + cssClass + "\" style=\"color: darkgoldenrod;\" > <i class=\" " + iconCss + "\"></i><span style=\"font-size:0;\">Edit</button>");
		} else {
			title = "Read-Only";
			iconCss = "fa fa-newspaper-o fa-lg";
			sb.appendHtmlConstant("<button type=\"button\" title='"
					+ title + "' tabindex=\"0\" class=\" " + cssClass + "\" style=\"color: black;\"><i class=\" "+iconCss + "\"></i> <span style=\"font-size:0;\">Read-Only</span></button>");
		}
		
		return sb.toSafeHtml();
	}
	
	/**
	 * Gets the history column tool tip.
	 *
	 * @param object the object
	 * @return the history column tool tip
	 */
	private SafeHtml getCloneColumnHTML(Result object){
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		String title = "Cloneable";
		String disabled = "";
		
		if(!object.isClonable()) {
			title = object.getIsComposite() ? "Composite measure not cloneable" :  "Measure not cloneable";
			disabled = " disabled style=\"color: gray;\"";
		}
		
		sb.appendHtmlConstant("<button type=\"button\" title='");
		sb.appendHtmlConstant(title);
		sb.appendHtmlConstant("' tabindex=\"0\" class=\"btn btn-link\"");
		sb.appendHtmlConstant(disabled);
		sb.appendHtmlConstant("><i class=\"fa fa-clone fa-lg\"></i><span style=\"font-size:0;\">");
		sb.appendHtmlConstant(title);
		sb.appendHtmlConstant("</span></button>");
		
		return sb.toSafeHtml();
	}
	
	/**
	 * Gets the bulk export column header.
	 *
	 * @return the bulk export column header
	 */
	private Header<SafeHtml> getBulkExportColumnHeader(){
		Header<SafeHtml> bulkExportColumnHeader = new Header<SafeHtml>(new ClickableSafeHtmlCell()) {
			private String cssClass = "transButtonWidth";
			private String title = "Click to Clear All";
			@Override
			public SafeHtml getValue() {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				sb.appendHtmlConstant("<span>Export</span><button type=\"button\" title='"
						+ title + "' tabindex=\"0\" class=\" " + cssClass + "\">"
						+ "<span class='textCssStyle'>(Clear)</span></button>");
				return sb.toSafeHtml();
			}
		};
		return bulkExportColumnHeader;
	}
	
	/**
	 * Gets the composite cell for bulk export.
	 *
	 * @return the composite cell for bulk export
	 */
	private CompositeCell<Result> getCompositeCellForBulkExport(boolean displayBulkExport){
		final List<HasCell<Result, ?>> cells = new LinkedList<HasCell<Result, ?>>();
		cells.add(getBulkExportButtonCell());
		if(displayBulkExport) {
			cells.add(getCheckBoxCell());
		}
		CompositeCell<Result> cell = new CompositeCell<Result>(cells) {
			@Override
			public void render(Context context, Result object, SafeHtmlBuilder sb) {
				sb.appendHtmlConstant("<table><tbody><tr>");
				for (HasCell<Result, ?> hasCell : cells) {
					render(context, object, sb, hasCell);
				}
				sb.appendHtmlConstant("</tr></tbody></table>");
			}
			@Override
			protected <X> void render(Context context, Result object,
					SafeHtmlBuilder sb, HasCell<Result, X> hasCell) {
				Cell<X> cell = hasCell.getCell();
				sb.appendHtmlConstant("<td class='emptySpaces'>");
				if ((object != null) && object.isExportable()) {
					cell.render(context, hasCell.getValue(object), sb);
				} else {
					sb.appendHtmlConstant("<span tabindex=\"-1\"></span>");
				}
				sb.appendHtmlConstant("</td>");
			}
			@Override
			protected Element getContainerElement(Element parent) {
				return parent.getFirstChildElement().getFirstChildElement()
						.getFirstChildElement();
			}
		};
		return cell;
	}
	

	/**
	 * Gets the history column tool tip.
	 *
	 * @param object the object
	 * @return the history column tool tip
	 */
	private SafeHtml getShareColumnToolTip(Result object){
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		String title ="Shareable";
		String cssClass = "btn btn-link";
		String iconCss = "fa fa-share-square fa-lg";
		if (object.isSharable()) {
			sb.appendHtmlConstant("<button type=\"button\" title='"
				+ title + "' tabindex=\"0\" class=\" " + cssClass + "\" style=\"color: darkseagreen;\"><i class=\" " + iconCss + "\"></i> <span style=\"font-size:0;\">Shareable</span></button>");
		} else {
			sb.appendHtmlConstant("<button type=\"button\" title='"
					+ title + "' tabindex=\"0\" class=\" " + cssClass + "\" disabled style=\"color: gray;\"><i class=\" " + iconCss + "\"></i><span style=\"font-size:0;\">Shareable</span></button>");
		}
		
		return sb.toSafeHtml();
	}

	
	
	/**
	 * Gets the bulk export button cell.
	 *
	 * @return the bulk export button cell
	 */
	private HasCell<Result, SafeHtml> getBulkExportButtonCell(){
		
		HasCell<Result, SafeHtml> hasCell = new HasCell<ManageMeasureSearchModel.Result, SafeHtml>() {
			
			ClickableSafeHtmlCell exportButonCell = new ClickableSafeHtmlCell();
			@Override
			public Cell<SafeHtml> getCell() {
				return exportButonCell;
			}

			@Override
			public FieldUpdater<Result, SafeHtml> getFieldUpdater() {
				
				return new FieldUpdater<Result, SafeHtml>() {
					@Override
					public void update(int index, Result object, SafeHtml value) {
						if ((object != null) && object.isExportable()) {
						observer.onExportClicked(object);
						}
					}
				};
			}

			@Override
			public SafeHtml getValue(Result object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				String title = "";
				String cssClass = "btn btn-link";
				String iconClass = "fa fa-download fa-lg";
				if((object != null) && (object.getHqmfReleaseVersion() != null)) {
					if(object.getHqmfReleaseVersion().equalsIgnoreCase("v3")){
						title = "Click to Export MAT v3";
						sb.appendHtmlConstant("<button type=\"button\" title='" + title 
								+ "' tabindex=\"0\" class=\" " + cssClass + "\" style=\"color: gray;\"/> <i class=\" " + iconClass  + "\"></i><span style=\"font-size:0;\">Export MAT v3 </span></button>");	
					} else {
						title = "Click to Export MAT " +  object.getHqmfReleaseVersion();
						sb.appendHtmlConstant("<button  type=\"button\" title='" + title 
								+ "' tabindex=\"0\" class=\" " + cssClass + "\" ><i class=\" " + iconClass  + "\"></i><span style=\"font-size:0;\">"+"Export MAT "+object.getHqmfReleaseVersion()+"</span></button>");
					}
				}
				return sb.toSafeHtml();
			}
		};
		
		return hasCell;
	}
	
	/**
	 * Gets the check box cell.
	 *
	 * @return the check box cell
	 */
	private HasCell<Result, Boolean> getCheckBoxCell(){
		HasCell<Result, Boolean> hasCell = new HasCell<ManageMeasureSearchModel.Result, Boolean>() {
			
			private MatCheckBoxCell cell = new MatCheckBoxCell(false, true);
			@Override
			public Cell<Boolean> getCell() {
				return cell;
			}
			@Override
			public Boolean getValue(Result object) {
				boolean isSelected = false;
				if (selectedList.size() > 0) {
				for (int i = 0; i < selectedList.size(); i++) {
					if (selectedList.get(i).getId().equalsIgnoreCase(object.getId())) {
						isSelected = true;
						selectionModel.setSelected(object, isSelected);
						break;
					}
				}
			} else {
				isSelected = false;
				selectionModel.setSelected(object, isSelected);
				}
				return isSelected;			
			}
			@Override
			public FieldUpdater<Result, Boolean> getFieldUpdater() {
				return new FieldUpdater<Result, Boolean>() {
					@Override
					public void update(int index, Result object,
							Boolean isCBChecked) {
						if(isCBChecked)
							selectedList.add(object);
						else{
							for (int i = 0; i < selectedList.size(); i++) {
								if (selectedList.get(i).getId().equalsIgnoreCase(object.getId())) {
									selectedList.remove(i);
									break;
								}
							}
						}
						selectionModel.setSelected(object, isCBChecked);
						observer.onExportSelectedClicked(object, isCBChecked);
					}
				};
			}
		};
		return hasCell;
	}
	/**
	 * Gets the observer.
	 *
	 * @return the observer
	 */
	public Observer getObserver() {
		return observer;
	}
	/**
	 * Sets the observer.
	 *
	 * @param observer the new observer
	 */
	public void setObserver(Observer observer) {
		this.observer = observer;
	}
}
