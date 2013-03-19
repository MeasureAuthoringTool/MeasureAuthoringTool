package mat.client.clause.clauseworkspace.model;

import java.util.ArrayList;
import java.util.List;

public class TreeModel {

	private String name;
	
	private List<TreeModel> childs;
	
	private TreeModel parent;

	public TreeModel(String name) {
	    parent = null;
        this.name = name;
        childs = new ArrayList<TreeModel>();
	}

	public TreeModel() {
	}
	
	public boolean hasChildrens() {
        return childs != null && childs.size()>0;
    }
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TreeModel> getChilds() {
		return childs;
	}

	public void setChilds(List<TreeModel> childs) {
		this.childs = childs;
	}

	public TreeModel getParent() {
		return parent;
	}

	public void setParent(TreeModel parent) {
		this.parent = parent;
	}
}
