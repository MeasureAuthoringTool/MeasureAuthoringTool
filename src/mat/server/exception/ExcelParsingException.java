package mat.server.exception;

public class ExcelParsingException  extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errorMessage;
	
	public ExcelParsingException(){
		super();
	}
	
    public ExcelParsingException(String errMsg){
        this.errorMessage = errMsg;
    }
    
    public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
