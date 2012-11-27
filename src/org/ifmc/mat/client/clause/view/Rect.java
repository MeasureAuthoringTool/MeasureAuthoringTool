package org.ifmc.mat.client.clause.view;

public class Rect {
	private int left, top, right, bottom;

	public Rect(int left, int top, int right, int bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}

	public int getLeft() {
		return left;
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

	public void setLeft(int left) {
		this.left = left;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public void setRight(int right) {
		this.right = right;
	}

	public void setBottom(int bottom) {
		this.bottom = bottom;
	}
	
	public String toString() {
		return "(" + left + "," + top + "," + right + "," + bottom;
	}
}
