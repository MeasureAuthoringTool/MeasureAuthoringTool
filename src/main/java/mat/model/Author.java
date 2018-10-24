package mat.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.gwt.user.client.rpc.IsSerializable;

@Entity
@Table(name = "AUTHOR")
public class Author implements IsSerializable{
	public static class Comparator implements java.util.Comparator<Author>, IsSerializable {

		@Override
		public int compare(Author o1, Author o2) {
			return o1.getAuthorName().compareTo(o2.getAuthorName());
		}
		
	}
	
	private String id;
	private String authorName;
	private String orgId;
	
	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id.trim();
	}
	
	@Column(name = "AUTHOR_NAME", nullable = false, length = 200)
	public String getAuthorName() {
		return authorName;
	}
	

	public void setAuthorName(String authorName) {
		this.authorName = authorName.trim();
	}
	
	public int compare(Author o1, Author o2) {
		return o1.getAuthorName().compareTo(o2.getAuthorName());
	}
	
	@Transient
	public String getOrgId() {
		return orgId;
	}
	
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}
