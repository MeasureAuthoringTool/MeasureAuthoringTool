package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;
import mat.model.cql.CQLCode;
import mat.shared.StringUtility;

import java.util.List;

public class MatCodeTransferObject implements IsSerializable , BaseModel{

	List<CQLCode> codeList;
	
	CQLCode cqlCode;

	String id;
	
	public CQLCode getCqlCode() {
		return cqlCode;
	}

	public List<CQLCode> getCodeList() {
		return codeList;
	}

	public void setCodeList(List<CQLCode> codeList) {
		this.codeList = codeList;
	}

	public void setCqlCode(CQLCode cqlCode) {
		this.cqlCode = cqlCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void scrubForMarkUp() {
		String markupRegExp = "<[^>]+>";
		if(this.cqlCode != null){
			if ((this.cqlCode.getCodeIdentifier() != null) && !this.cqlCode.getCodeIdentifier().isEmpty()) {
				String noMarkupText = this.cqlCode.getCodeIdentifier().trim().replaceAll(markupRegExp, "");
				if (this.cqlCode.getCodeIdentifier().trim().length() > noMarkupText.length()) {
					this.cqlCode.setCodeIdentifier(noMarkupText);
				}
			}
			
			if ((this.cqlCode.getName() != null) && !this.cqlCode.getName().isEmpty()) {
				String noMarkupText = this.cqlCode.getName().trim().replaceAll(markupRegExp, "");
				if (this.cqlCode.getName().trim().length() > noMarkupText.length()) {
					this.cqlCode.setName(noMarkupText);
				}
			}
			
			if ((this.cqlCode.getCodeOID() != null) && !this.cqlCode.getCodeOID().isEmpty()) {
				String noMarkupText = this.cqlCode.getCodeOID().trim().replaceAll(markupRegExp, "");
				if (this.cqlCode.getCodeOID().trim().length() > noMarkupText.length()) {
					this.cqlCode.setCodeOID(noMarkupText);
				}
			}
			
			if ((this.cqlCode.getCodeSystemName() != null) && !this.cqlCode.getCodeSystemName().isEmpty()) {
				String noMarkupText = this.cqlCode.getCodeSystemName().trim().replaceAll(markupRegExp, "");
				if (this.cqlCode.getCodeSystemName().trim().length() > noMarkupText.length()) {
					this.cqlCode.setCodeSystemName(noMarkupText);
				}
			}
			
			if ((this.cqlCode.getCodeSystemOID() != null) && !this.cqlCode.getCodeSystemOID().isEmpty()) {
				String noMarkupText = this.cqlCode.getCodeSystemOID().trim().replaceAll(markupRegExp, "");
				if (this.cqlCode.getCodeSystemOID().trim().length() > noMarkupText.length()) {
					this.cqlCode.setCodeSystemOID(noMarkupText);
				}
			}
			
			if ((this.cqlCode.getCodeSystemVersion() != null) && !this.cqlCode.getCodeSystemVersion().isEmpty()) {
				String noMarkupText = this.cqlCode.getCodeSystemVersion().trim().replaceAll(markupRegExp, "");
				if (this.cqlCode.getCodeSystemVersion().trim().length() > noMarkupText.length()) {
					this.cqlCode.setCodeSystemVersion(noMarkupText);
				}
			}
			
			if ((this.cqlCode.getDisplayName() != null) && !this.cqlCode.getDisplayName().isEmpty()) {
				String noMarkupText = this.cqlCode.getDisplayName().trim().replaceAll(markupRegExp, "");
				if (this.cqlCode.getDisplayName().trim().length() > noMarkupText.length()) {
					this.cqlCode.setDisplayName(noMarkupText);
				}
			}
			
			if ((this.cqlCode.getSuffix() != null) && !this.cqlCode.getSuffix().isEmpty()) {
				String noMarkupText = this.cqlCode.getSuffix().trim().replaceAll(markupRegExp, "");
				if (this.cqlCode.getSuffix().trim().length() > noMarkupText.length()) {
					this.cqlCode.setSuffix(noMarkupText);
				}
			}
		}
		
	}
	
	public boolean isValidModel(){
		boolean isValid = true;
		if (this.cqlCode == null || StringUtility.isEmptyOrNull(this.cqlCode.getCodeIdentifier()) || StringUtility.isEmptyOrNull(this.cqlCode.getName())
				|| StringUtility.isEmptyOrNull(this.cqlCode.getCodeOID()) || StringUtility.isEmptyOrNull(this.cqlCode.getCodeSystemName())
				|| StringUtility.isEmptyOrNull(this.cqlCode.getCodeSystemOID()) || StringUtility.isEmptyOrNull(this.cqlCode.getCodeSystemVersion())
				|| StringUtility.isEmptyOrNull(this.cqlCode.getDisplayName())) {
			isValid = false;
		}
		return isValid;
	}
	
	public boolean isValidCodeData(CQLCode applyCode) {

		return applyCode.equals(this.cqlCode);
	}
}
