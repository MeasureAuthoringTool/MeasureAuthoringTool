package mat.server.bonnie.api.result;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BonnieCalculatedResult implements IsSerializable{

	byte[] result;
	
	public BonnieCalculatedResult() {
		
	}

	public byte[] getResult() {
		return result;
	}

	public void setResult(byte[] result) {
		this.result = result;
	}
	
}
