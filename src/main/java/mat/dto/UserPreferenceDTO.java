package mat.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UserPreferenceDTO implements IsSerializable {
	
	private boolean freeTextEditorEnabled;

	public UserPreferenceDTO() {
		
	}
	
	public UserPreferenceDTO(boolean freeTextEditorEnabled) {
		this.freeTextEditorEnabled = freeTextEditorEnabled;
	}

	public boolean isFreeTextEditorEnabled() {
		return freeTextEditorEnabled;
	}

	public void setFreeTextEditorEnabled(boolean freeTextEditorEnabled) {
		this.freeTextEditorEnabled = freeTextEditorEnabled;
	}
	
	
}
