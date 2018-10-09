package mat.server.bonnie.api.result;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BonnieCalculatedResult implements IsSerializable{

	private String name; 
	private byte[] result;
	
	public BonnieCalculatedResult() {
		
	}

	public byte[] getResult() {
		return result;
	}

	public void setResult(byte[] result) {
		this.result = result;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
