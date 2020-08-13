package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MEASURE_SCORE")
public class MeasureScore implements IsSerializable{
	
	public static class Comparator implements java.util.Comparator<MeasureScore>, IsSerializable {

		@Override
		public int compare(MeasureScore o1, MeasureScore o2) {
			return o1.getScore().compareTo(o2.getScore());
		}
		
	}
	
	private String id;
	
	private String score;
	
	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "SCORE", nullable = false, length = 200)
	public String getScore() {
		return score;
	}
	
	public void setScore(String score) {
		this.score = score;
	}
	
	

}
