package mat.DTO;


import mat.client.codelist.HasListBox;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class MeasureTypeDTO.
 */
public class MeasureTypeDTO implements IsSerializable, HasListBox {
	
	/** The id. */
	private String id;
	
	/** The Name. */
	private String Name;
	
	private String abbrName;
	
	/**
	 * Instantiates a new measure type dto.
	 */
	public MeasureTypeDTO(){
		
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
	
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.HasListBox#getValue()
	 */
	public String getValue() {
		return id;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.HasListBox#getItem()
	 */
	public String getItem() {
		// TODO Auto-generated method stub
		return Name;
	}
	
	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return Name;
	}
	
	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		Name = name;
	}
	
	/**
	 * Gets the abbreviated name
	 * @return the abbreviated name
	 */
	public String getAbbrName() {
		return abbrName;
	}

	/**
	 * Sets the abbreviated name
	 * @param abbrName the abbreviated name
	 */
	public void setAbbrName(String abbrName) {
		this.abbrName = abbrName;
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
