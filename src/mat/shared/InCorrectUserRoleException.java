package mat.shared;

import java.io.Serializable;

public class InCorrectUserRoleException extends Exception implements Serializable{
	
	public InCorrectUserRoleException(){
		super();
	}
	
	public InCorrectUserRoleException(String message){
		super(message);
	}
}
