package mat.client.clause.clauseworkspace.model;

import java.util.List;

/**
 * The Class TreeModel.
 */
public class TreeModel {

	/** The name. */
	private String name;

	/** The childs. */
	private List<TreeModel> childs;

	/** The parent. */
	private TreeModel parent;

	/** The label. */
	private String label;

	/** The is editable. */
	private boolean isEditable = true;

	/** The is removable. */
	private boolean isRemovable = true;

	/** The is open. */
	private boolean isOpen;


	/**
	 * Instantiates a new tree model.
	 */
	public TreeModel() { }

	/**
	 * Checks for childrens.
	 * 
	 * @return true, if successful
	 */
	public boolean hasChildrens() {
        return childs != null && childs.size() > 0;
    }

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the childs.
	 * 
	 * @return the childs
	 */
	public List<TreeModel> getChilds() {
		return childs;
	}

	/**
	 * Sets the childs.
	 * 
	 * @param childs
	 *            the new childs
	 */
	public void setChilds(List<TreeModel> childs) {
		this.childs = childs;
	}

	/**
	 * Gets the parent.
	 * 
	 * @return the parent
	 */
	public TreeModel getParent() {
		return parent;
	}

	/**
	 * Sets the parent.
	 * 
	 * @param parent
	 *            the new parent
	 */
	public void setParent(TreeModel parent) {
		this.parent = parent;
	}

	/**
	 * Gets the label.
	 * 
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets the label.
	 * 
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Checks if is editable.
	 * 
	 * @return the isEditable
	 */
	public boolean isEditable() {
		return isEditable;
	}

	/**
	 * Sets the editable.
	 * 
	 * @param isEditable
	 *            the isEditable to set
	 */
	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	/**
	 * Checks if is removable.
	 * 
	 * @return the isRemovable
	 */
	public boolean isRemovable() {
		return isRemovable;
	}

	/**
	 * Sets the removable.
	 * 
	 * @param isRemovable
	 *            the isRemovable to set
	 */
	public void setRemovable(boolean isRemovable) {
		this.isRemovable = isRemovable;
	}

	/**
	 * Checks if is open.
	 * 
	 * @return the isOpen
	 */
	public boolean isOpen() {
		return isOpen;
	}

	/**
	 * Sets the open.
	 * 
	 * @param isOpen
	 *            the isOpen to set
	 */
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
}
