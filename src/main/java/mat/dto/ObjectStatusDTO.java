package mat.dto;

import mat.client.codelist.HasListBox;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * The Class ObjectStatusDTO.
 */
public class ObjectStatusDTO implements IsSerializable, HasListBox {
	
	/** The id. */
	private String id;
	
	/** The description. */
	private String description;

	/**
	 * Instantiates a new object status dto.
	 */
	public ObjectStatusDTO(){
		
	}
	
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 * 
	 * @param description
	 *            the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.HasListBox#getValue()
	 */
	@Override
	public String getValue() {
		return id;
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.HasListBox#getItem()
	 */
	@Override
	public String getItem() {
		return description;
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.HasListBox#getSortOrder()
	 */
	@Override
	public int getSortOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

}
