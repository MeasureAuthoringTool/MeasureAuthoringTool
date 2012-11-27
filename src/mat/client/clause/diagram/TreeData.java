package mat.client.clause.diagram;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TreeData implements IsSerializable {
	private double breadth;
	private int depth;
	
	public TreeData() {
	}
	
	public double getBreadth() {
		return breadth;
	}
	
	public void setBreadth(double breadth) {
		this.breadth = breadth;
	}
	
	public int getDepth() { 
		return depth;
	}
	
	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	@Override
	public String toString() {
		return "depth: " + getDepth() + " breadth: " + getBreadth();
	}
}
