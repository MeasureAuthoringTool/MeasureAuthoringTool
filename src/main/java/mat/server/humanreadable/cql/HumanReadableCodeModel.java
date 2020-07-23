package mat.server.humanreadable.cql;

import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor
public class HumanReadableCodeModel implements HumanReadableTerminologyModel {
	private String name;
	private String oid;
	private String codesystemName;
	private String codesystemVersion;
	private boolean isCodesystemVersionIncluded;
	private String datatype; 
	private String terminologyDisplay;
	private String dataCriteriaDisplay; 
	
	public HumanReadableCodeModel(String name, String oid, String codesystemName, boolean isCodeSystemVersionIncluded, String codesystemVersion, String datatype) {
		this.name = name;
		this.oid = oid;
		this.codesystemName = codesystemName;
		this.codesystemVersion = codesystemVersion;
		this.isCodesystemVersionIncluded = isCodeSystemVersionIncluded;
		this.datatype = datatype; 
		createDataCriteriaDisplay(); 
		createTerminologyDisplay();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getCodesystemName() {
		return codesystemName;
	}

	public void setCodesystemName(String taxonomy) {
		this.codesystemName = taxonomy;
	}

	public boolean getIsCodesystemVersionIncluded() {
		return isCodesystemVersionIncluded;
	}

	public void setIsCodesystemVersionIncluded(boolean isCodeSystemIncluded) {
		this.isCodesystemVersionIncluded = isCodeSystemIncluded;
	}

	public String getTerminologyDisplay() {
		createTerminologyDisplay();
		return this.terminologyDisplay;
	}

	private void createTerminologyDisplay() {
		String codeSystemVersion = "";
		if (isCodesystemVersionIncluded) {
			codeSystemVersion = " version " + codesystemVersion;
		}

		String codeOutput = "code \"" + name + "\" (\"" + codesystemName + codeSystemVersion + " Code (" + oid
				+ ")\")";
		this.terminologyDisplay = codeOutput;
	}

	@Override
	public void setTerminologyDisplay(String display) {
		this.terminologyDisplay = display;
	}

	public String getCodesystemVersion() {
		return codesystemVersion;
	}

	public void setCodesystemVersion(String codesystemVersion) {
		this.codesystemVersion = codesystemVersion;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	
	private void createDataCriteriaDisplay() {
		if("attribute".equals(datatype)){
			datatype = "Attribute";
		}

		String output = "";
		if(isCodesystemVersionIncluded) {
			output = String.format("\"%s: %s\" using \"%s (%s version %s Code %s)\"", datatype, name, name, codesystemName, codesystemVersion, oid);
		} else {
			output = String.format("\"%s: %s\" using \"%s (%s Code %s)\"", datatype, name, name, codesystemName, oid);
		}
			
		this.dataCriteriaDisplay = output; 
	}

	@Override
	public String getDataCriteriaDisplay() {
		createDataCriteriaDisplay();
		return dataCriteriaDisplay;
	}

	@Override
	public void setDataCriteriaDisplay(String dataCriteriaDisplay) {
		this.dataCriteriaDisplay = dataCriteriaDisplay;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name, oid, codesystemName, codesystemVersion, datatype);
	}

	@Override
	public boolean equals(Object obj) {
		HumanReadableCodeModel model = (HumanReadableCodeModel) obj; 
		return name.equals(model.name) && oid.equals(model.oid) && codesystemName.equals(model.codesystemName) && codesystemVersion.equals(model.codesystemVersion) && isDatatypeEqual(datatype, model.datatype);
	}
	
	private boolean isDatatypeEqual(String d1, String d2) {
		// if datatype 1 is not null, then check the equality to datatype 2.
		// if datatype 1 is null, then check to see if datatype 2 is null. 
		if(d1 != null) {
			return d1.equals(d2);
		} else {
			return d2 == null; 
		}
	}
}
