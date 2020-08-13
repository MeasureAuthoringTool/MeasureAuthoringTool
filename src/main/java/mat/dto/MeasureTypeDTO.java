package mat.dto;


import com.google.gwt.user.client.rpc.IsSerializable;
import mat.client.codelist.HasListBox;

public class MeasureTypeDTO implements IsSerializable, HasListBox {
	
	private String id;
	private String name;
	private String abbrName;
	
	public MeasureTypeDTO(){
	}
	
	public MeasureTypeDTO(String id, String name, String abbrName) {
		super();
		this.id = id;
		this.name = name;
		this.abbrName = abbrName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAbbrName() {
		return abbrName;
	}

	public void setAbbrName(String abbrName) {
		this.abbrName = abbrName;
	}

	@Override
	public String getValue() {
		return id;
	}
	
	@Override
	public String getItem() {
		return name;
	}

	@Override
	public int getSortOrder() {
		return 0;
	}
	
}
