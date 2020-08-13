package mat.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "GROUPED_CODE_LISTS")
public class GroupedCodeList implements Cloneable {
	
	private String id;
	
	private String description;
	
	private CodeList codeList;
	
	private ListObject listObject;
	
	@Id
	@Column(name = "GROUPED_CODE_LISTS_ID", unique = true, nullable = false, length = 32)
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "DESCRIPTION", nullable = false, length = 1000)
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODE_LIST_ID", nullable = false, insertable = false, updatable = false)
	public ListObject getListObject() {
		return this.listObject;
	}

	public void setListObject(ListObject listObject) {
		this.listObject = listObject;
	} 
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CODE_LIST_ID", nullable = false,unique= false)
	public CodeList getCodeList() {
		return codeList;
	}
	
	public void setCodeList(CodeList codeList) {
		this.codeList = codeList;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public GroupedCodeList clone(){
		GroupedCodeList clone = new GroupedCodeList();
		clone.setCodeList(this.getCodeList());
		clone.setDescription(this.getDescription());
		return clone;
	}
	
}
