package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MeasureType implements IsSerializable{
	public static class Comparator implements java.util.Comparator<MeasureType>, IsSerializable {

		@Override
		public int compare(MeasureType o1, MeasureType o2) {
			return o1.getDescription().compareTo(o2.getDescription());
		}
		
	}
	private String id;
	private String abbrDesc;//Added for XML conversion.
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id.trim();
	}

	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description.trim();
	}
	
	public int compare(MeasureType o1, MeasureType o2) {
		return o1.getDescription().compareTo(o2.getDescription());
	}

	/**
	 * @return the abbrDesc
	 */
	public String getAbbrDesc() {
		return abbrDesc;
	}

	/**
	 * @param abbrDesc the abbrDesc to set
	 */
	public void setAbbrDesc(String abbrDesc) {
		this.abbrDesc = abbrDesc;
	}

}
