package mat.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.gwt.user.client.rpc.IsSerializable;

@Entity
@Table(name="MEASURE_TYPES")
public class MeasureType implements IsSerializable{
	
	public static class Comparator implements java.util.Comparator<MeasureType>, IsSerializable {

		@Override
		public int compare(MeasureType o1, MeasureType o2) {
			return o1.getDescription().compareTo(o2.getDescription());
		}
		
	}
	
	private String id;
	
	private String description;
	
	private String abbrName;//Added for XML conversion.
	
	public MeasureType(){
		
	}
	
	public MeasureType(String id, String description, String abbrName) {
		this.id = id;
		this.description = description;
		this.abbrName = abbrName;
	}
	
	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "NAME", nullable = false, length = 50)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description.trim();
	}
	
	public int compare(MeasureType o1, MeasureType o2) {
		return o1.getDescription().compareTo(o2.getDescription());
	}

	@Column(name = "ABBR_NAME", length = 45)
	public String getAbbrName() {
		return abbrName;
	}

	public void setAbbrName(String abbrName) {
		this.abbrName = abbrName;
	}

}
