package mat.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CQLErrors implements IsSerializable {
	int errorInLine;
	int errorAtOffeset;
	String errorMessage;
	
	public int getErrorInLine() {
		return errorInLine;
	}
	public void setErrorInLine(int errorInLine) {
		this.errorInLine = errorInLine;
	}
	public int getErrorAtOffeset() {
		return errorAtOffeset;
	}
	public void setErrorAtOffeset(int errorAtOffeset) {
		this.errorAtOffeset = errorAtOffeset;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.errorInLine + ";" + this.errorAtOffeset + ":" + this.errorMessage;
	}
}
