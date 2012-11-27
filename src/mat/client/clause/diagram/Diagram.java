package mat.client.clause.diagram;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Diagram implements IsSerializable {
	public static final int TOP_MARGIN = 40;
	private TraversalTree tree;
	private int top;
	private int right;
	private int bottom;
	private boolean dirty;
	
	public Diagram() {
		tree = new TraversalTree();
		setPosition(tree, 0);
		top = 0;
		right = 0;
		bottom = 0;
		dirty = false;
	}
	
	public void reset() {
		setPosition(tree, 0);
	}
	
	private void setPosition(TraversalTree t, int pad) {
		TreeData td = new TreeData();
		td.setDepth(t.getDepth());
		if (t.isLeaf())
			td.setBreadth(pad);
		else {	
			int l = t.numLeavesInBranch()-1;
			td.setBreadth(pad + (l*0.5));
		}
		
		t.setData(td);
		
		for (TraversalTree c : t.getChildren()) {
			setPosition(c, pad);
			pad += c.numLeavesInBranch();
		}
	}	
	
	public TraversalTree getTree() {
		return tree;
	}
	
	public void deleteTree() {
		tree = null;
	}

	public void calcExtent(int right, int bottom) {
		this.right = Math.max(this.right, right);
		this.bottom = Math.max(this.bottom, bottom);
	}
	
	public void setTop(int top) {
		this.top = top;
	}
	
	public int getTop() {
		return top;
	}
	
	public int getRight() {
		return right;
	}

	public int getBottom() {
		return bottom;
	}
	
	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
}
