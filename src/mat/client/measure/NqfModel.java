package mat.client.measure;

import com.google.gwt.user.client.rpc.IsSerializable;

public class NqfModel implements IsSerializable{
	
	private String root;
	
	private String extension;

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	@Override
	public String toString() {
		return "NqfModel [root=" + root + ", extension=" + extension + "]";
	}

}
