package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;
import mat.client.codelist.HasListBox;

// TODO: Auto-generated Javadoc
/**
 * The Class VSACVersion.
 */
public class VSACVersion  implements IsSerializable, HasListBox {

	/** The name. */
	private String name;

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
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
