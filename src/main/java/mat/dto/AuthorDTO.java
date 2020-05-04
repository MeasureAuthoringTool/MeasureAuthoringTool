package mat.dto;


import com.google.gwt.user.client.rpc.IsSerializable;

import mat.client.codelist.HasListBox;

public class AuthorDTO implements IsSerializable, HasListBox {

	private String id;

	private String authorName;

	public AuthorDTO(){
		
	}
	
	public AuthorDTO(String id, String authorName) {
		super();
		this.id = id;
		this.authorName = authorName;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
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
		return 0;
	}
	
}
