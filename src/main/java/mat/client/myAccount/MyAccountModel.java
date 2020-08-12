package mat.client.myAccount;

import com.google.gwt.user.client.rpc.IsSerializable;
import mat.model.BaseModel;

public class MyAccountModel implements IsSerializable, BaseModel {
	
	private String firstName;
	
	private String middleInitial;
	
	private String lastName;
	
	private String title;
	
	private String emailAddress;
	
	private String phoneNumber;
	
	private String organization;
	
	private String oid;

	private String loginId;
	
	private boolean enableFreeTextEditor;
	

	public String getOrganization() {
		return organization;
	}
	
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	
	public String getOid() {
		return oid;
	}
	
	public void setOid(String oid) {
		this.oid = oid;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getLoginId() {
		return loginId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleInitial() {
		return middleInitial;
	}

	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	@Override
	public void scrubForMarkUp() {
		String markupRegExp = "<[^>]+>";
		
		String noMarkupText = this.getFirstName().trim().replaceAll(markupRegExp, "");
		if(this.getFirstName().trim().length() > noMarkupText.length()){
			this.setFirstName(noMarkupText);
		}
		noMarkupText = this.getLastName().trim().replaceAll(markupRegExp, "");
		if(this.getLastName().trim().length() > noMarkupText.length()){
			this.setLastName(noMarkupText);
		}
		noMarkupText = this.getMiddleInitial().trim().replaceAll(markupRegExp, "");
		if(this.getMiddleInitial().trim().length() > noMarkupText.length()){
			this.setMiddleInitial(noMarkupText);
		}
		noMarkupText = this.getTitle().trim().replaceAll(markupRegExp, "");
		if(this.getTitle().trim().length() > noMarkupText.length()){
			this.setTitle(noMarkupText);
		}
		noMarkupText = this.getEmailAddress().trim().replaceAll(markupRegExp, "");
		if(this.getEmailAddress().trim().length() > noMarkupText.length()){
			this.setEmailAddress(noMarkupText);
		}
		noMarkupText = this.getOid().trim().replaceAll(markupRegExp, "");
		if(this.getOid().trim().length() > noMarkupText.length()){
			this.setOid(noMarkupText);
		}
		noMarkupText = this.getOrganization().trim().replaceAll(markupRegExp, "");
		if(this.getOrganization().trim().length() > noMarkupText.length()){
			this.setOrganization(noMarkupText);
		}
		noMarkupText = this.getPhoneNumber().trim().replaceAll(markupRegExp, "");
		if(this.getPhoneNumber().trim().length() > noMarkupText.length()){
			this.setPhoneNumber(noMarkupText);
		}
		this.setEnableFreeTextEditor(this.isEnableFreeTextEditor());
		
	}

	public boolean isEnableFreeTextEditor() {
		return enableFreeTextEditor;
	}

	public void setEnableFreeTextEditor(boolean enableFreeTextEditor) {
		this.enableFreeTextEditor = enableFreeTextEditor;
	}
}
