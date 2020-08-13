package mat.dto;


import com.google.gwt.user.client.rpc.IsSerializable;
import mat.client.codelist.HasListBox;

/**
 * DTO for Measure Score table attributes.
 */
public class MeasureScoreDTO implements IsSerializable, HasListBox {

	/** The id. */
	private String id;
	
	/** The score. */
	private String score;
	
	/**
	 * Instantiates a new measure score dto.
	 */
	public MeasureScoreDTO(){
		
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
		return score;
	}
	
	/**
	 * Gets the score.
	 * 
	 * @return the score
	 */
	public String getScore() {
		return score;
	}
	
	/**
	 * Sets the score.
	 * 
	 * @param score
	 *            the new score
	 */
	public void setScore(String score) {
		this.score = score;
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
