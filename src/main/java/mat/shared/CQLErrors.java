package mat.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CQLErrors implements IsSerializable ,Comparable<CQLErrors> {
	int errorInLine;
	int errorAtOffeset;
	
	int startErrorInLine;
	int startErrorAtOffset;
	
	int endErrorInLine; 
	int endErrorAtOffset; 
	
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

	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.errorInLine + ";" + this.errorAtOffeset + ":" + this.errorMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public int getStartErrorInLine() {
		return startErrorInLine;
	}
	public void setStartErrorInLine(int startErrorInLine) {
		this.startErrorInLine = startErrorInLine;
	}
	public int getStartErrorAtOffset() {
		return startErrorAtOffset;
	}
	public void setStartErrorAtOffset(int startErrorAtOffset) {
		this.startErrorAtOffset = startErrorAtOffset;
	}
	public int getEndErrorInLine() {
		return endErrorInLine;
	}
	public void setEndErrorInLine(int endErrorInLine) {
		this.endErrorInLine = endErrorInLine;
	}
	public int getEndErrorAtOffset() {
		return endErrorAtOffset;
	}
	public void setEndErrorAtOffset(int endErrorAtOffset) {
		this.endErrorAtOffset = endErrorAtOffset;
	}
	
	
	@Override
    public int compareTo(CQLErrors o) {
           
           if(this.errorInLine > o.errorInLine) {
                  return 1;
           }else if(this.errorInLine < o.errorInLine) {
                  return -1;
           }
           
           return 0;
    }

}
