package mat.client.clause.diagram;

import java.util.ArrayList;
import java.util.List;

import mat.client.clause.AppController;
import mat.client.clause.view.shape.DiagramShape;
import mat.client.clause.view.shape.TextRectangle;
import mat.client.diagramObject.DiagramObject;
import mat.client.diagramObject.DiagramObjectFactory;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TraversalTree implements IsSerializable {
	private List<TraversalTree> children = new ArrayList<TraversalTree>();
	private TraversalTree parent;
	private TreeData data;
	private DiagramObject diagramObject;
	private DiagramShape diagramShape;
	private boolean systemClause = false;
	
	public TraversalTree() {
	}
	
	public TraversalTree(TraversalTree parent){
		setParent(parent);
		if (parent != null)
			parent.addChild(this);
	}

	public TraversalTree(TraversalTree parent, DiagramObject diagramObject) {
		this(parent);
		this.diagramObject = diagramObject;
	}	
	
	public boolean isRoot(){
		return parent == null;
	}

	public boolean isLeaf(){
		return children.isEmpty();
	}

	public int numLeavesInBranch(){
		if (isLeaf())
			return 1;
		else {
			int i = 0;
			for (TraversalTree t : children)
				i += t.numLeavesInBranch();
			return i;
		}
	}

	public void setParent(TraversalTree parent){
		this.parent = parent;
	}

	public void addChild(TraversalTree child){
		child.setParent(this);
		this.children.add(child);
	}

	public void addDiagramObject(DiagramObject diagramObject) {
		if (this.diagramObject == null)
			this.diagramObject = diagramObject;
		else
			new TraversalTree(this, diagramObject);
	}
	
	public List<TraversalTree> getChildren(){
		return this.children;
	}
	
	private void clear() {
		for (TraversalTree c : this.children)
			c.clear();	
		children.clear();		
	}
	
	public void deleteChild(TraversalTree tree) {
		this.children.remove(tree);
	}
	
	public void deleteChildren() {
		this.clear();
		this.children = new ArrayList<TraversalTree>();
	}
	
	public TraversalTree getParent(){
		return this.parent;
	}

	public int getDepth(){
		int i = 0;
		TraversalTree temp = parent;
		while (temp != null) {
			temp = temp.getParent();
			i++;
		}
		return i;
	}

	public DiagramObject getDiagramObject() {
		return diagramObject;
	}

	public TraversalTree findDiagramObject(DiagramObject diagramObject) {
		if (this.diagramObject == diagramObject)
			return this;
		return findDiagramObject(getChildren(), diagramObject);
	}
	
	private TraversalTree findDiagramObject(List<TraversalTree> children, DiagramObject diagramObject) {
		for (TraversalTree child : children) {
			if (child.diagramObject == diagramObject)
				return child;
			if (child.getChildren().size() > 0) {
				TraversalTree found = findDiagramObject(child.getChildren(), diagramObject);
				if (found != null)
					return found;
			}
		}
		return null;
	}	
	
	public void setDiagramObject(DiagramObject diagramObject) {
		this.diagramObject = diagramObject;
	}
	
	public double getX() {
		return data.getDepth();
	}
	
	public double getY() {
		return data.getBreadth();
	}
	
	public TreeData getData() {
		return data;
	}

	public void setData(TreeData data) {
		this.data = data;
	}

	public String getName() {
		return diagramObject.getIdentity();
	}

	public void setName(String identity) {
		diagramObject.setIdentity(identity);
	}
	
	public String getDescription() {
		return diagramObject.getDescription();
	}

	public void setDescription(String description) {
		diagramObject.setDescription(description);
	}	
	
	public DiagramShape getDiagramShape() {
		return diagramShape;
	}

	public void setDiagramShape(DiagramShape diagramShape) {
		this.diagramShape = diagramShape;
	}
	
	public TraversalTree clone(AppController appController) {
		TraversalTree dest = new TraversalTree();
		clone(appController, null, this, dest);
		return dest;
	}
	
	public TraversalTree clone(AppController appController, boolean isSystemClause) {
		TraversalTree dest = new TraversalTree();
		dest.setSystemClause(isSystemClause);
		clone(appController, null, this, dest);
		return dest;
	}
	
	public void clone(AppController appController, TraversalTree parent, TraversalTree src, TraversalTree dest) {
		
		dest.children = new ArrayList<TraversalTree>();	
		dest.parent = parent;
		dest.data = new TreeData();
		dest.data.setBreadth(src.data.getBreadth());
		dest.data.setDepth(src.data.getDepth());
		dest.diagramObject =  DiagramObjectFactory.clone(appController, src.diagramObject);
		if (src.diagramShape instanceof TextRectangle)
			dest.diagramShape = ((TextRectangle)src.diagramShape).clone(appController);
		if (parent != null)
			parent.addChild(dest);
		
		if (src.children.size() > 0)
			for (TraversalTree t : src.children) {
				clone(appController, dest, t, new TraversalTree());
			}
	}

	public void setSystemClause(boolean systemClause) {
		this.systemClause = systemClause;
	}

	public boolean isSystemClause() {
		return systemClause;
	}
}
