package mat.server.exception;

/**
 * The Class ExcelParsingException.
 */
public class ExcelParsingException  extends Exception{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The error message. */
	private String errorMessage;
	
	/**
	 * Instantiates a new excel parsing exception.
	 */
	public ExcelParsingException(){
		super();
	}
	
    /**
	 * Instantiates a new excel parsing exception.
	 * 
	 * @param errMsg
	 *            the err msg
	 */
    public ExcelParsingException(String errMsg){
        this.errorMessage = errMsg;
    }
    
    /**
	 * Gets the error message.
	 * 
	 * @return the error message
	 */
    public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * Sets the error message.
	 * 
	 * @param errorMessage
	 *            the new error message
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
