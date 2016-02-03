package mat.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CQLValidationResult implements IsSerializable {
	private boolean isValid;
	private List<CQLErrors> errorList;
	public boolean isValid() {
		return isValid;
	}
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	public List<CQLErrors> getErrorList() {
		return errorList;
	}
	public void setErrorList(List<CQLErrors> errorList) {
		this.errorList = errorList;
	}

}
