package org.ifmc.mat.client.measure;

import org.ifmc.mat.client.shared.search.SearchResults;
import org.ifmc.mat.model.clause.MeasureShareDTO;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

class UserShareInfoAdapter implements SearchResults<MeasureShareDTO> {
	private static String[] headers = new String[] { "User", "Sharing Mode" };
	private static String[] widths = new String[] { "50%", "50%" };

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
		final RadioButton viewOnlyRadio = new RadioButton("share" + dto.getUserId(), "View Only");
		final RadioButton editRadio = new RadioButton("share" + dto.getUserId(), "Modify");
		if("1".equals(currentShare)) {
			viewOnlyRadio.setValue(Boolean.TRUE);
		}
		else if("2".equals(currentShare)) {
			editRadio.setValue(Boolean.TRUE);
		}
		
		ValueChangeHandler<Boolean> changeHandler = new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if(viewOnlyRadio.getValue().equals(Boolean.TRUE)){
					dto.setShareLevel("1");
				}
				else if(editRadio.getValue().equals(Boolean.TRUE)) {
					dto.setShareLevel("2");
				}
			}
		};
		viewOnlyRadio.addValueChangeHandler(changeHandler);
		editRadio.addValueChangeHandler(changeHandler);
		fPanel.add(viewOnlyRadio);
		fPanel.add(editRadio);
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
