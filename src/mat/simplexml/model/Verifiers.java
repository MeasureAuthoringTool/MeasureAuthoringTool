package mat.simplexml.model;

import java.util.ArrayList;
import java.util.List;

public class Verifiers {
	private List<Verifier> verifiers;
	private String  ttext;

	public String getTtext() {
		return ttext;
	}
	public void setTtext (String ttext ) {
		this.ttext = ttext;
	}
	public List<Verifier> getVerifiers() {
		if(verifiers == null){
			verifiers = new ArrayList<Verifier>();
		}
		return verifiers;
	}
	public void setVerifiers(List<Verifier> verifiers) {
		this.verifiers = verifiers;
	}
	
}