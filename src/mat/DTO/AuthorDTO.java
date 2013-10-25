package mat.DTO;


import mat.client.codelist.HasListBox;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class AuthorDTO.
 */
public class AuthorDTO implements IsSerializable, HasListBox {

	/** The id. */
	private String id;
	
	/** The author name. */
	private String authorName;
	
	/**
	 * Instantiates a new author dto.
	 */
	public AuthorDTO(){
		
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
		return authorName;
	}
	
	/**
	 * Gets the author name.
	 * 
	 * @return the author name
	 */
	public String getAuthorName() {
		return authorName;
	}
	
	/**
	 * Sets the author name.
	 * 
	 * @param authorName
	 *            the new author name
	 */
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
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
