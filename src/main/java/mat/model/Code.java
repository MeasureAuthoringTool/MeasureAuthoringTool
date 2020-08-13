package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "CODE", uniqueConstraints = @UniqueConstraint(columnNames = { "CODE", "CODE_LIST_ID" }))
public class Code implements IsSerializable , Cloneable{
	public static class Comparator implements java.util.Comparator<Code>, IsSerializable {
		@Override
		public int compare(Code o1, Code o2) {
			return o1.getCode().compareTo(o2.getCode());
		}
	}

	private String id;
	private String code;
	private String description;
	private ListObject listObject;
	
	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid", strategy = "uuid")
	@Column(name = "CODE_ID", unique = true, nullable = false, length = 36)
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		if(id!=null)
			id=id.trim();
		this.id = id;
	}
	
	@Column(name = "CODE", nullable = false, length = 32)
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code.trim();
	}
	

	@Column(name = "DESCRIPTION", nullable = false, length = 1400)
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description.trim();
	}

	@Override
    public boolean equals(Object aThat) {
	   
	    Code that = (Code)aThat;
	    return
	    	this.code.equalsIgnoreCase(that.code);
		}
	
	@Override
	public int hashCode() {
		int code = getCode().hashCode(); 
		return code;
	}

	public Code clone(){
		Code clone = new Code();
		clone.setCode(this.getCode());
		clone.setDescription(this.getDescription());
		return clone;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODE_LIST_ID", nullable = false, insertable = false, updatable = false)
	public ListObject getListObject() {
		return this.listObject;
	}

	public void setListObject(ListObject listObject) {
		this.listObject = listObject;
	}
	
	
}
