package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Author implements IsSerializable{
	public static class Comparator implements java.util.Comparator<Author>, IsSerializable {

		@Override
		public int compare(Author o1, Author o2) {
			return o1.getAuthorName().compareTo(o2.getAuthorName());
		}
		
	}
	private String id;
	private String authorName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id.trim();
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName.trim();
	}
	
	public int compare(Author o1, Author o2) {
		return o1.getAuthorName().compareTo(o2.getAuthorName());
	}
	

}
