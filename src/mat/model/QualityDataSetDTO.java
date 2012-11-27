package mat.model;



import com.google.gwt.user.client.rpc.IsSerializable;

public class QualityDataSetDTO implements IsSerializable {
	
	public static class Comparator implements java.util.Comparator<QualityDataSetDTO>, IsSerializable {

		@Override
		public int compare(QualityDataSetDTO o1,
				QualityDataSetDTO o2) {
			return o1.getQDMElement().compareTo(o2.getQDMElement());
		}
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getCodeListName() {
		return codeListName;
	}

	public void setCodeListName(String codeListName) {
		this.codeListName = codeListName;
	}

	private String id;

	private String dataType;
	
	private String codeListName;
	
	private String occurrenceText;
	
	private boolean suppDataElement;
	
	private String oid;
	
	public String toString() {
		if(occurrenceText!= null && !occurrenceText.equals(""))
			return occurrenceText + " of "+codeListName + ": " + dataType + "-" +getOid();
		else
			return codeListName + ": " + dataType + "-" + getOid();
	}
	
	public String getQDMElement() {
		return codeListName + ": " + dataType;
	}
	
	@Override
	public boolean equals (Object o) {
		QualityDataSetDTO temp = (QualityDataSetDTO)o;
		if (temp.getId().equals(getId())) return true;
		return false;
	}

	public String getOccurrenceText() {
		return occurrenceText;
	}

	public void setOccurrenceText(String occurrenceText) {
		this.occurrenceText = occurrenceText;
	}

	public boolean isSuppDataElement() {
		return suppDataElement;
	}

	public void setSuppDataElement(boolean suppDataElement) {
		this.suppDataElement = suppDataElement;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}	
	
	
	
	
}
