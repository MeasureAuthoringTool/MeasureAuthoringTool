package mat.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CQLValidationResult implements IsSerializable {
	private boolean isValid;
	private List<CQLError> errorList;
	public boolean isValid() {
		return isValid;
	}
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	public List<CQLError> getErrorList() {
		return errorList;
	}
	public void setErrorList(List<CQLError> errorList) {
		this.errorList = errorList;
	}

}
