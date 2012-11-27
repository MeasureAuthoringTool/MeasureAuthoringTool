package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MeasureScore implements IsSerializable{
	public static class Comparator implements java.util.Comparator<MeasureScore>, IsSerializable {

		@Override
		public int compare(MeasureScore o1, MeasureScore o2) {
			return o1.getScore().compareTo(o2.getScore());
		}
		
	}
	private String id;
	private String score;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	
	

}
