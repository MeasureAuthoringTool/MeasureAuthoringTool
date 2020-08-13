package mat.client.expressionbuilder.util;

import mat.client.expressionbuilder.constant.TimingOperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimingGraph {

	private Map<TimingOperator, List<TimingOperator>> graph; 
	
	public TimingGraph() {
		graph = new HashMap<>();
	}
	
	public void addNode(TimingOperator timing) {
		this.graph.put(timing, new ArrayList<>());
	}
	
	public void addEdge(TimingOperator parent, TimingOperator child) {
		if(this.graph.get(parent) == null) {
			addNode(parent);
		}
		
		if(this.graph.get(child) == null) {
			addNode(child);
		}
		
		this.graph.get(parent).add(child);
	}
	
	public Map<TimingOperator, List<TimingOperator>> getGraph() {
		return graph;
	}
}
