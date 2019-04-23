package mat.model;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "CODE_LIST")
@PrimaryKeyJoinColumn(name = "CODE_LIST_ID")
public class CodeList extends ListObject {
	
}
