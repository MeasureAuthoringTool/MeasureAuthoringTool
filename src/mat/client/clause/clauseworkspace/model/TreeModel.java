package mat.client.clause.clauseworkspace.model;

import java.util.List;

public class TreeModel {

	private String name;
	
	private List<TreeModel> childs;
	
	private TreeModel parent;
	
	private String label;
	
	private boolean isEditable = true;
	
	private boolean isRemovable = true;
	
	private boolean isOpen;


	public TreeModel() { }
	
	public boolean hasChildrens() {
        return childs != null && childs.size() > 0;
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

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the isEditable
	 */
	public boolean isEditable() {
		return isEditable;
	}

	/**
	 * @param isEditable the isEditable to set
	 */
	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	/**
	 * @return the isRemovable
	 */
	public boolean isRemovable() {
		return isRemovable;
	}

	/**
	 * @param isRemovable the isRemovable to set
	 */
	public void setRemovable(boolean isRemovable) {
		this.isRemovable = isRemovable;
	}

	/**
	 * @return the isOpen
	 */
	public boolean isOpen() {
		return isOpen;
	}

	/**
	 * @param isOpen the isOpen to set
	 */
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
}
