package mat.client.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class NameValuePair implements IsSerializable{
	
	public static class Comparator implements IsSerializable, java.util.Comparator<NameValuePair> {

		@Override
		public int compare(NameValuePair o1, NameValuePair o2) {
			return o1.getName().compareTo(o2.getName());
		}
		
	}
	
	private String name;
	private String value;
	public NameValuePair(String name, String value) {
		this.name = name;
		this.value = value;
	}
	public NameValuePair() {
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
