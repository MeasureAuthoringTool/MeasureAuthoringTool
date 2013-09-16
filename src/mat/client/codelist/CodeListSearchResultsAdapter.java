package mat.client.codelist;

import mat.client.ImageResources;
import mat.client.shared.CustomButton;
import mat.client.shared.FocusableImageButton;
import mat.client.shared.search.SearchResults;
import mat.model.CodeListSearchDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

class CodeListSearchResultsAdapter implements SearchResults<CodeListSearchDTO>{

	public static interface Observer {
		public void onHistoryClicked(CodeListSearchDTO result);
		public void onCloneClicked(CodeListSearchDTO result);
		public void onExportClicked(CodeListSearchDTO result);
		
	}
		
	private ManageCodeListSearchModel model = new ManageCodeListSearchModel();
	private Observer observer;
	private ClickHandler clickHandler = buildClickHandler();
	
	private CodeListSearchDTO getResultForId(String id) {
		for(int i = 0; i < model.getNumberOfRows(); i++) {			
			if(id.equals(model.getKey(i))) {
				return model.get(i);
			}
		}
		Window.alert("Could not find id " + id);
		return null;
	}
	
	public void setObserver(Observer observer) {
		this.observer = observer;
	}
	public void setData(ManageCodeListSearchModel model) {
		this.model = model;
	}
	
	private ClickHandler buildClickHandler() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String id = ((Widget)event.getSource()).getElement().getId();
				int index = id.indexOf('_');
				String action = id.substring(0, index);
				String key = id.substring(index + 1);
				CodeListSearchDTO result = getResultForId(key);

				if(observer != null) {
					if("history".equals(action)){
						observer.onHistoryClicked(result);
						//observer.onCloneClicked(result);
					}
					// Code commented for User Story MAT-2372 : Remove Value Set Creation.
					/*else if("clone".equals(action)){
						observer.onCloneClicked(result);
					}*/
					else if("export".equals(action)){
						observer.onExportClicked(result);
					}
				}
			}
		};
	}
	
	@Override
	public boolean isColumnSortable(int columnIndex) {
		return model.isColumnSortable(columnIndex);
	}


	@Override
	public int getNumberOfColumns() {
		return model.getNumberOfColumns();
	}

	@Override
	public int getNumberOfRows() {
		return model.getNumberOfRows();
	}

	@Override
	public String getColumnHeader(int columnIndex) {
		return model.getColumnHeader(columnIndex);
	}

	@Override
	public String getColumnWidth(int columnIndex) {
		return model.getColumnWidth(columnIndex);
	}

	@Override
	public boolean isColumnFiresSelection(int columnIndex) {
		return model.isColumnFiresSelection(columnIndex);
	}

	@Override
	public Widget getValue(int row, int column) {
		Widget value;
		
		switch(column) {
		case 0:
			value = new Label(model.get(row).getName());
			break;
		case 1:
			value = new Label(model.get(row).getLastModified());
			break;
		case 2:
			value = new Label(model.getData().get(row).getSteward());
			break;
		case 3:
			value = new Label(model.getData().get(row).getCategoryDisplay());
			break;
		case 4:
			value=new Label(model.getData().get(row).getCodeSystem());
			break;
		case 5:
			value = getImage("history", ImageResources.INSTANCE.clock(),model.getData().get(row).getId());
			break;
			// Code commented for User Story MAT-2372 : Remove Value Set Creation.
		/*case 6:
			value = getImage("clone", ImageResources.INSTANCE.g_page_copy(), model.getData().get(row).getId());
			break;*/
		case 6:
			value = getImage("export", ImageResources.INSTANCE.g_package_go(), model.getData().get(row).getId());
			break;
			    
		default: 
			value = new Label("Unknown Column Index");
		}
		return value;
	}
	
	private Widget getImage(String action, ImageResource url, String key) {
		SimplePanel holder = new SimplePanel();
		holder.setStyleName("searchTableCenteredHolder");
		CustomButton image = new CustomButton();
		image.removeStyleName("gwt-button");
		image.setStylePrimaryName("invisibleButtonText");
		image.setTitle(action);
		image.setResource(url,action);
		setId(image, action, key);
		addListener(image);
		holder.add(image);
		return holder;
	}
	
	private void addListener(CustomButton image) {
		image.addClickHandler(clickHandler);
	}
	private void setImageStyle(FocusableImageButton image) {
		image.setStylePrimaryName("measureSearchResultIcon");
	}
	private void setId(CustomButton image, String action, String key) {
		String id = action + "_" + key;
		image.getElement().setAttribute("id", id);
	}

	@Override
	public int getStartIndex() {
		return model.getStartIndex();
	}

	@Override
	public int getResultsTotal() {
		return model.getResultsTotal();
	}

	@Override
	public String getKey(int row) {
		return model.get(row).getId();
	}


	@Override
	public CodeListSearchDTO get(int row) {
		return model.get(row);
	}

	@Override
	public boolean isColumnSelectAll(int columnIndex) {
		return model.isColumnSelectAll(columnIndex);
	}
	
	

}
