package mat.client.admin;

import com.google.gwt.user.client.rpc.IsSerializable;
import mat.model.BaseModel;

/** The Class ManageUsersDetailModel. */
public class ManageOrganizationDetailModel implements IsSerializable , BaseModel {
	
	private Long id;
	private String oid;
	private String organization;
	
	/** Do trim.
	 * 
	 * @param str the str
	 * @return the string */
	private String doTrim(String str) {
		if (str == null) {
			return str;
		} else {
			return str.trim();
		}
	}
	
	/** Gets the oid.
	 * 
	 * @return the oid */
	public String getOid() {
		return oid;
	}
	
	/** Gets the organization.
	 * 
	 * @return the organization */
	public String getOrganization() {
		return organization;
	}
	
	
	/** Sets the oid.
	 * 
	 * @param oid the new oid */
	public void setOid(String oid) {
		this.oid = doTrim(oid);
	}
	
	/** Sets the organization.
	 * 
	 * @param organization the new organization */
	public void setOrganization(String organization) {
		this.organization = doTrim(organization);
	}
	
	@Override
	public void scrubForMarkUp() {
		String markupRegExp = "<[^>]+>";
		
		String noMarkupText = this.getOrganization().trim().replaceAll(markupRegExp, "");
		//System.out.println(noMarkupText);
		if (this.getOrganization().trim().length() > noMarkupText.length()) {
			this.setOrganization(noMarkupText);
		}
		noMarkupText = getOid().trim().replaceAll(markupRegExp, "");
		//System.out.println(noMarkupText);
		if (this.getOid().trim().length() > noMarkupText.length()) {
			this.setOid(noMarkupText);
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
}
