package mat.simplexml.model;

import java.util.Arrays;

public class PrettyPrinter {
	private final String nl = System.getProperty("line.separator");
	private StringBuilder sb;	
	private int indentation;
	
	public PrettyPrinter() {
		sb = new StringBuilder();
		indentation = 0;
	}
	
	public String toString() {
		return sb.toString();
	}
	
	public void concat(String s) {
		sb.append(nl).append(indent()).append(s);
	}

	public void concat(String s1, String s2) {
		sb.append(nl).append(indent()).append(s1).append(" ").append(s2);
	}

	public String indent() {
		if (indentation < 0) indentation = 0;
		if (indentation == 0) return "";
		char[] chars = new char[indentation];
		Arrays.fill(chars, '\t');
		return new String(chars);
	}

	public void incrementIndentation() {
		++indentation;
	}
	
	public void decrementIndentation() {
		--indentation;
		if (indentation < 0)
			indentation = 0;
	}

	public int getIndentation() {
		return indentation;
	}

	public void setIndentation(int indentation) {
		this.indentation = indentation;
	}
}
