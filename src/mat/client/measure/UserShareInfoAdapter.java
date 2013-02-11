package mat.client.measure;

import mat.client.shared.search.SearchResults;
import mat.model.clause.MeasureShareDTO;
import mat.model.clause.ShareLevel;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

class UserShareInfoAdapter implements SearchResults<MeasureShareDTO> {
	private static String[] headers = new String[] { "User", "Modify Share" };
	private static String[] widths = new String[] { "50%", "20%" };

	private ManageMeasureShareModel data = new ManageMeasureShareModel();
	public void setData(ManageMeasureShareModel data) {
		this.data = data;
	}
	
	
	@Override
	public boolean isColumnSortable(int columnIndex) {
		return false;
	}


	@Override
	public int getNumberOfColumns() {
		return headers.length;
	}

	@Override
	public int getNumberOfRows() {
		return data.getNumberOfRows();
	}

	@Override
	public String getColumnHeader(int columnIndex) {
		return headers[columnIndex];
	}

	@Override
	public String getColumnWidth(int columnIndex) {
		return widths[columnIndex];
	}

	@Override
	public boolean isColumnFiresSelection(int columnIndex) {
		return false;
	}

	@Override
	public Widget getValue(int row, int column) {
		Widget value;
		switch(column) {
		case 0:
			value = new Label(data.get(row).getFirstName() + " " + data.get(row).getLastName());
			break;
		case 1:
			value = buildShareRadioPanel(data.get(row));
			break;
		
		default: 
			//value = new Label("Unknown Column Index");
			value = new Label();
		}
		return value;
	}
	
	private Widget buildShareRadioPanel(final MeasureShareDTO dto) {
		FlowPanel fPanel = new FlowPanel();
		String currentShare = dto.getShareLevel();
		final CheckBox modifyCheckBox = new CheckBox();
		modifyCheckBox.setFormValue("share" + dto.getUserId());
		modifyCheckBox.setValue(false);
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

	@Override
	public int getStartIndex() {
		return data.getStartIndex();
	}

	@Override
	public int getResultsTotal() {
		return data.getResultsTotal();
	}

	@Override
	public String getKey(int row) {
		return data.get(row).getUserId();
	}


	@Override
	public MeasureShareDTO get(int row) {
		return data.get(row);
	}


	@Override
	public boolean isColumnSelectAll(int columnIndex) {
		return false;
	}

}
