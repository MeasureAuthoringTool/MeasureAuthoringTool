package mat.model;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MatGroup implements IsSerializable {

	private String ID;
	private String sourceOrganization;
	private String displayName;
	private ArrayList<String> keywordList;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getSourceOrganization() {
		return sourceOrganization;
	}

	public void setSourceOrganization(String sourceOrganization) {
		this.sourceOrganization = sourceOrganization;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setKeywordList(ArrayList<String> keywordList) {
		this.keywordList = keywordList;
	}

	public ArrayList<String> getKeywordList() {
		return keywordList;
	}

}
