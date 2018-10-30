package mat.server.humanreadable.cql;

import org.apache.commons.lang3.StringUtils;

public class HumanReadableValuesetModel {
	private String name;
	private String oid;
	private String version;
	private String terminologyDisplay;
	private String datatype; 
	private String dataCriteriaDisplay; 

	public HumanReadableValuesetModel(String name, String oid, String version, String datatype) {
		this.name = name;
		this.oid = oid;
		this.version = version;
		this.datatype = datatype; 
		createTerminologyDisplay();
		createtDataCriteriaDisplay(); 
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	private void createTerminologyDisplay() {
		String output = "";
		if(StringUtils.isNotBlank(version) && !version.equalsIgnoreCase("1.0")) {
			output = "valueset \"" + name + "\" (" + oid + ", version " + version + ")";
		} else {
			output = "valueset \"" + name + "\" (" + oid + ")";
		}
		
		this.terminologyDisplay = output; 
	}

	public String getTerminologyDisplay() {
		createTerminologyDisplay();
		return terminologyDisplay;
	}

	public void setTerminologyDisplay(String display) {
		this.terminologyDisplay = display;
	}

	
	private void createtDataCriteriaDisplay() {
		if("attribute".equals(datatype)){
			datatype = "Attribute";
		}
									
		String output = String.format("\"%s: %s\" using \"%s (%s)\"", datatype, name, name, oid);
		
		if(StringUtils.isNotBlank(version) && !version.equals("1.0") && !version.equals("1")){
			output = String.format("\"%s: %s\" using \"%s (%s, version %s)\"", datatype, name, name, oid, version);
		}
		
		this.dataCriteriaDisplay = output; 
	}
	
	public String getDataCriteriaDisplay() {
		createtDataCriteriaDisplay(); 
		return dataCriteriaDisplay;
	}

	public void setDataCriteriaDisplay(String dataCriteriaDisplay) {
		this.dataCriteriaDisplay = dataCriteriaDisplay;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
}
