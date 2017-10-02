package mat.model;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import mat.model.cql.CQLCode;

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
				System.out.println("getCodeIdentifier:" + noMarkupText);
				if (this.cqlCode.getCodeIdentifier().trim().length() > noMarkupText.length()) {
					this.cqlCode.setCodeIdentifier(noMarkupText);
				}
			}
			
			if ((this.cqlCode.getCodeName() != null) && !this.cqlCode.getCodeName().isEmpty()) {
				String noMarkupText = this.cqlCode.getCodeName().trim().replaceAll(markupRegExp, "");
				System.out.println("getCodeName:" + noMarkupText);
				if (this.cqlCode.getCodeName().trim().length() > noMarkupText.length()) {
					this.cqlCode.setCodeName(noMarkupText);
				}
			}
			
			if ((this.cqlCode.getCodeOID() != null) && !this.cqlCode.getCodeOID().isEmpty()) {
				String noMarkupText = this.cqlCode.getCodeOID().trim().replaceAll(markupRegExp, "");
				System.out.println("getCodeOID:" + noMarkupText);
				if (this.cqlCode.getCodeOID().trim().length() > noMarkupText.length()) {
					this.cqlCode.setCodeOID(noMarkupText);
				}
			}
			
			if ((this.cqlCode.getCodeSystemName() != null) && !this.cqlCode.getCodeSystemName().isEmpty()) {
				String noMarkupText = this.cqlCode.getCodeSystemName().trim().replaceAll(markupRegExp, "");
				System.out.println("getCodeSystemName:" + noMarkupText);
				if (this.cqlCode.getCodeSystemName().trim().length() > noMarkupText.length()) {
					this.cqlCode.setCodeSystemName(noMarkupText);
				}
			}
			
			if ((this.cqlCode.getCodeSystemOID() != null) && !this.cqlCode.getCodeSystemOID().isEmpty()) {
				String noMarkupText = this.cqlCode.getCodeSystemOID().trim().replaceAll(markupRegExp, "");
				System.out.println("getCodeSystemOID:" + noMarkupText);
				if (this.cqlCode.getCodeSystemOID().trim().length() > noMarkupText.length()) {
					this.cqlCode.setCodeSystemOID(noMarkupText);
				}
			}
			
			if ((this.cqlCode.getCodeSystemVersion() != null) && !this.cqlCode.getCodeSystemVersion().isEmpty()) {
				String noMarkupText = this.cqlCode.getCodeSystemVersion().trim().replaceAll(markupRegExp, "");
				System.out.println("getCodeSystemVersion:" + noMarkupText);
				if (this.cqlCode.getCodeSystemVersion().trim().length() > noMarkupText.length()) {
					this.cqlCode.setCodeSystemVersion(noMarkupText);
				}
			}
			
			if ((this.cqlCode.getDisplayName() != null) && !this.cqlCode.getDisplayName().isEmpty()) {
				String noMarkupText = this.cqlCode.getDisplayName().trim().replaceAll(markupRegExp, "");
				System.out.println("getDisplayName:" + noMarkupText);
				if (this.cqlCode.getDisplayName().trim().length() > noMarkupText.length()) {
					this.cqlCode.setDisplayName(noMarkupText);
				}
			}
			
			if ((this.cqlCode.getSuffix() != null) && !this.cqlCode.getSuffix().isEmpty()) {
				String noMarkupText = this.cqlCode.getSuffix().trim().replaceAll(markupRegExp, "");
				System.out.println("getSuffix:" + noMarkupText);
				if (this.cqlCode.getSuffix().trim().length() > noMarkupText.length()) {
					this.cqlCode.setSuffix(noMarkupText);
				}
			}
		}
		
	}
	
	
	public boolean isValidModel(){
		boolean isValid = true;
		if(this.cqlCode != null){
			if ((this.cqlCode.getCodeIdentifier() != null) && this.cqlCode.getCodeIdentifier().isEmpty()) {
				isValid = false;
			} else if ((this.cqlCode.getCodeName() != null) && this.cqlCode.getCodeName().isEmpty()) {
				isValid = false;
			} else if ((this.cqlCode.getCodeOID() != null) && this.cqlCode.getCodeOID().isEmpty()) {
				isValid = false;
			} else if ((this.cqlCode.getCodeSystemName() != null) && this.cqlCode.getCodeSystemName().isEmpty()) {
				isValid = false;
			} else if ((this.cqlCode.getCodeSystemOID() != null) && this.cqlCode.getCodeSystemOID().isEmpty()) {
				isValid = false;
			} else if ((this.cqlCode.getCodeSystemVersion() != null) && this.cqlCode.getCodeSystemVersion().isEmpty()) {
				isValid = false;
			} else if ((this.cqlCode.getDisplayName() != null) && this.cqlCode.getDisplayName().isEmpty()) {
				isValid = false;
			}
		} else {
			isValid = false;
		}
		return isValid;
	}
}
