package mat.shared.bonnie.result;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BonnieUserInformationResult implements IsSerializable {
	private String bonnieUsername; 	
	
	public BonnieUserInformationResult(String bonnieUsername) {
		this.setBonnieUsername(bonnieUsername);
	}
	
	public BonnieUserInformationResult() {
		
	}

	public String getBonnieUsername() {
		return bonnieUsername;
	}

	public void setBonnieUsername(String bonnieUsername) {
		this.bonnieUsername = bonnieUsername;
	}
}
