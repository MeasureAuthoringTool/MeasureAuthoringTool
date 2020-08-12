package mat.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;

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
