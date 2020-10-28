package mat.client.measure;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import mat.client.shared.search.SearchResults;
import mat.model.clause.MeasureShareDTO;
import mat.model.clause.ShareLevel;

/**
 * The Class UserShareInfoAdapter.
 */
class UserShareInfoAdapter implements SearchResults<MeasureShareDTO> {
	
	/** The headers. */
	private static String[] headers = new String[] { "User", "Organization", "Share" };
	
	/** The widths. */
	private static String[] widths = new String[] { "40%", "40%", "12%" };
	
	/** The data. */
	private ManageMeasureShareModel data = new ManageMeasureShareModel();
	
	/**
	 * Builds the share checkbox panel.
	 * 
	 * @param dto
	 *            the dto
	 * @return the widget
	 */
	private Widget buildShareCheckboxPanel(final MeasureShareDTO dto) {
		FlowPanel fPanel = new FlowPanel();
		fPanel.setTitle("Share");
		fPanel.setStyleName("centerAligned");
		String currentShare = dto.getShareLevel();
		final CheckBox modifyCheckBox = new CheckBox();
		//508 - remove extra label with checkbox.Set ID attribute to Span and remove id from input.
		modifyCheckBox.getElement().removeChild(modifyCheckBox.getElement().getLastChild());
		modifyCheckBox.getElement().setAttribute("id", "share - " + dto.getUserId());
		modifyCheckBox.getElement().getFirstChildElement().removeAttribute("id");
		modifyCheckBox.setFormValue("share" + dto.getUserId());
		modifyCheckBox.setValue(false);
		modifyCheckBox.setTitle("Select User "+ dto.getFirstName()+ " " +dto.getLastName()+ " to Share Measure." );
		
		if(ShareLevel.VIEW_ONLY_ID.equals(currentShare)) {
			modifyCheckBox.setValue(false);
		}else if(ShareLevel.MODIFY_ID.equals(currentShare)) {
			modifyCheckBox.setValue(true);
		}
		
		ValueChangeHandler<Boolean> changeHandler = new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if(modifyCheckBox.getValue()){
					dto.setShareLevel(ShareLevel.MODIFY_ID);
				}else{
					dto.setShareLevel(ShareLevel.VIEW_ONLY_ID);
				}
			}
		};
		modifyCheckBox.addValueChangeHandler(changeHandler);
		fPanel.add(modifyCheckBox);
		return fPanel;
	}
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#get(int)
	 */
	@Override
	public MeasureShareDTO get(int row) {
		return data.get(row);
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getColumnHeader(int)
	 */
	@Override
	public String getColumnHeader(int columnIndex) {
		return headers[columnIndex];
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getColumnWidth(int)
	 */
	@Override
	public String getColumnWidth(int columnIndex) {
		return widths[columnIndex];
	}
	
	/**
	 * @return the data
	 */
	public ManageMeasureShareModel getData() {
		return data;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getKey(int)
	 */
	@Override
	public String getKey(int row) {
		return data.get(row).getUserId();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getNumberOfColumns()
	 */
	@Override
	public int getNumberOfColumns() {
		return headers.length;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getNumberOfRows()
	 */
	@Override
	public int getNumberOfRows() {
		return data.getNumberOfRows();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getResultsTotal()
	 */
	@Override
	public int getResultsTotal() {
		return data.getResultsTotal();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getStartIndex()
	 */
	@Override
	public int getStartIndex() {
		return data.getStartIndex();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getValue(int, int)
	 */
	@Override
	public Widget getValue(int row, int column) {
		Widget value;
		switch(column) {
			case 0:
				value = new Label(data.get(row).getFirstName() + " " + data.get(row).getLastName());
				break;
			case 1:
				value = new Label(data.get(row).getOrganizationName());
				break;
			case 2:
				value = buildShareCheckboxPanel(data.get(row));
				break;
				
			default:
				value = new Label();
		}
		return value;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnFiresSelection(int)
	 */
	@Override
	public boolean isColumnFiresSelection(int columnIndex) {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnSelectAll(int)
	 */
	@Override
	public boolean isColumnSelectAll(int columnIndex) {
		return false;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnSortable(int)
	 */
	@Override
	public boolean isColumnSortable(int columnIndex) {
		return false;
	}
	
	
	/**
	 * Sets the data.
	 * 
	 * @param data
	 *            the new data
	 */
	public void setData(ManageMeasureShareModel data) {
		this.data = data;
	}
	
}
