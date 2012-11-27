package org.ifmc.mat.shared;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ifmc.mat.client.clause.MeasurePhrases;
import org.ifmc.mat.client.diagramObject.Phrase;
import org.ifmc.mat.client.diagramObject.SimpleStatement;

public class TopologicalSort {
	MeasurePhrases measurePhrases;
	private List<String> names;
	private Vertex vertexList[]; // list of vertices
	private Map<String, Integer> vertexMap; // hash map to look up vertex index
	private int matrix[][]; // adjacency matrix
	private int numVerts; // current number of vertices
	private String sortedArray[];

	public TopologicalSort(MeasurePhrases measurePhrases, List<String> names) throws Exception {
		this.measurePhrases = measurePhrases;
		this.names = names;
		setVertices();
		addEdges();
	}

	public void setVertices() {
		numVerts = names.size();
		vertexList = new Vertex[numVerts];
		vertexMap = new HashMap<String, Integer>();
		matrix = new int[numVerts][numVerts];
		for (int i = 0; i < numVerts; i++)
			for (int j = 0; j < numVerts; j++)
				matrix[i][j] = 0;
		sortedArray = new String[numVerts]; // sorted vert labels
		
		int i = 0;
		for (String phrase : names) {
			vertexList[i] = new Vertex(phrase);
			vertexMap.put(phrase, new Integer(i));
			++i;
		}
	}

	private void addEdges() throws Exception {
		for (String name : names) {
			SimpleStatement measurePhrase = (SimpleStatement)(measurePhrases.getItem(name));
			if (names.contains(measurePhrase.getPhrase1Text()))
				addEdge(measurePhrase.getPhrase1Text(), name);
			if (measurePhrase.getPhrase2() != null && measurePhrase.getPhrase2Text() != null &&
					names.contains(measurePhrase.getPhrase2Text()))
				addEdge(measurePhrase.getPhrase2Text(), name);
			for (Phrase additionalPhrase : measurePhrase.getAdditionalPhraseList()) {
				if (names.contains(additionalPhrase.getText()))
					addEdge(additionalPhrase.getText(), name);
			}
		}
	}
	
	private void addEdge(int start, int end) {
		matrix[start][end] = 1;
	}

	public void topologicalSort() throws Exception {
		while (numVerts > 0) { // while vertices remain,
			// get a vertex with no successors, or -1
			int currentVertex = noSuccessors();
			if (currentVertex == -1)  // CYCLE FOUND - RECURSION!!!
				throw new IllegalRecursionException();
			// insert vertex label in sorted array (start at end)
			sortedArray[numVerts - 1] = vertexList[currentVertex].label;

			deleteVertex(currentVertex); // delete vertex
		}
	}

	public String[] getSortedArray() {
		return sortedArray;
	}

	private int noSuccessors() {  // returns vert with no successors (or -1 if no such verts)
		boolean isEdge; // edge from row to column in adjMat

		for (int row = 0; row < numVerts; row++) {
			isEdge = false; // check edges
			for (int col = 0; col < numVerts; col++) {
				if (matrix[row][col] > 0){ // if edge to another,
					isEdge = true;
					break; // this vertex has a successor try another
				}
			}
			if (!isEdge) // if no edges, has no successors
				return row;
		}
		return -1; // no
	}

	private void deleteVertex(int delVert) {
		if (delVert != numVerts - 1) { // if not last vertex, delete from vertexList
			for (int j = delVert; j < numVerts - 1; j++)
				vertexList[j] = vertexList[j + 1];

			for (int row = delVert; row < numVerts - 1; row++)
				moveRowUp(row, numVerts);

			for (int col = delVert; col < numVerts - 1; col++)
				moveColLeft(col, numVerts - 1);
		}
		numVerts--; // one less vertex
	}

	private void moveRowUp(int row, int length) {
		for (int col = 0; col < length; col++)
			matrix[row][col] = matrix[row + 1][col];
	}

	private void moveColLeft(int col, int length) {
		for (int row = 0; row < length; row++)
			matrix[row][col] = matrix[row][col + 1];
	}

	public void addEdge(String a, String b) throws Exception {
		Integer ia = vertexMap.get(a);
		Integer ib = vertexMap.get(b);
		if (ia == null || ib == null)
			throw new IllegalArgumentException(a + " and " + b + " must both be on the initial list.");
		addEdge(ia.intValue(), ib.intValue());
	}
}