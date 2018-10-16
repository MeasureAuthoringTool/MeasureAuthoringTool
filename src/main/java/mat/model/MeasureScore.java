package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class MeasureScore.
 */
public class MeasureScore implements IsSerializable{
	
	/**
	 * The Class Comparator.
	 */
	public static class Comparator implements java.util.Comparator<MeasureScore>, IsSerializable {

		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(MeasureScore o1, MeasureScore o2) {
			return o1.getScore().compareTo(o2.getScore());
		}
		
	}
	
	/** The id. */
	private String id;
	
	/** The score. */
	private String score;
	
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
	
	

}
