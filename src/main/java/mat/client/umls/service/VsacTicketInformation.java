package mat.client.umls.service;

import com.google.gwt.user.client.rpc.IsSerializable;

public class VsacTicketInformation implements IsSerializable {

    private String apiKey;

    private boolean isValidApiKey;

    public VsacTicketInformation() {

    }
    
    public VsacTicketInformation(String apiKey, boolean isValidApiKey) {
    	this.apiKey = apiKey;
    	this.isValidApiKey = isValidApiKey;
    }

    public String getApiKey() {
        return apiKey;
    }
    
    public boolean isValidApiKey() {
    	return this.isValidApiKey;
    }
    public void setIsValidApiKey(boolean isValidApiKey) {
    	this.isValidApiKey = isValidApiKey;
    }
}
