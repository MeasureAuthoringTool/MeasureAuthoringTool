package mat.model;

import mat.client.codelist.HasListBox;

import com.google.gwt.user.client.rpc.IsSerializable;

public class VSACExpansionIdentifier implements IsSerializable, HasListBox {

	
	private String name;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getSortOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getValue() {
		return name;
	}

	@Override
	public String getItem() {
		return name;
	}
	
	
	
}
