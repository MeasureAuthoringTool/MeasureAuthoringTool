package mat.client.expressionbuilder.util;

import mat.client.expressionbuilder.constant.TimingOperator;

import java.util.ArrayList;
import java.util.List;

public class TimingGraphUtil {
	
	private TimingGraphUtil() {
		throw new IllegalStateException("Timing Graph Util");
	}

    // ('starts' | 'ends' | 'occurs')? 'same' dateTimePrecision? (relativeQualifier | 'as') ('start' | 'end')? #concurrentWithIntervalOperatorPhrase
	public static TimingGraph getConcurrentWithIntervalOperatorPhraseGraph() {
		TimingGraph graph = new TimingGraph();
		
		graph.addEdge(TimingOperator.BEGINNING_NODE, TimingOperator.STARTS);
		graph.addEdge(TimingOperator.BEGINNING_NODE,  TimingOperator.ENDS);
		graph.addEdge(TimingOperator.BEGINNING_NODE,  TimingOperator.OCCURS);
		graph.addEdge(TimingOperator.BEGINNING_NODE, TimingOperator.SAME);
		
		graph.addEdge(TimingOperator.STARTS, TimingOperator.SAME);
		graph.addEdge(TimingOperator.ENDS, TimingOperator.SAME);
		graph.addEdge(TimingOperator.OCCURS, TimingOperator.SAME);

		addDateTimePrecisionToParent(graph, TimingOperator.SAME);
		
		addRelativeQualifierToParent(graph, TimingOperator.SAME);
		graph.addEdge(TimingOperator.SAME, TimingOperator.AS);

		addChildToDateTimePrecision(graph, TimingOperator.OR_AFTER);
		addChildToDateTimePrecision(graph, TimingOperator.OR_BEFORE);
		
		addChildToDateTimePrecision(graph, TimingOperator.AS);
				
		addChildToRelativeQualifier(graph,  TimingOperator.START_OF);
		addChildToRelativeQualifier(graph,  TimingOperator.END_OF);
		addChildToRelativeQualifier(graph,  TimingOperator.DONE_NODE);


		graph.addEdge(TimingOperator.AS, TimingOperator.START_OF);
		graph.addEdge(TimingOperator.AS, TimingOperator.END_OF);
		graph.addEdge(TimingOperator.AS, TimingOperator.DONE_NODE);
		
		graph.addEdge(TimingOperator.START_OF, TimingOperator.DONE_NODE);
		graph.addEdge(TimingOperator.END_OF, TimingOperator.DONE_NODE);
		
		return graph;
	}
		
	
    // 'properly'? 'includes' dateTimePrecisionSpecifier? ('start' | 'end')? #includesIntervalOperatorPhrase
	public static TimingGraph getIncludesIntervalOperatorPhraseGraph() {
		TimingGraph graph = new TimingGraph();
		
		graph.addEdge(TimingOperator.BEGINNING_NODE, TimingOperator.PROPERLY);
		graph.addEdge(TimingOperator.BEGINNING_NODE, TimingOperator.INCLUDES);
		
		graph.addEdge(TimingOperator.PROPERLY, TimingOperator.INCLUDES);

		addDateTimePrecisionSpecifierToParent(graph, TimingOperator.INCLUDES);
		graph.addEdge(TimingOperator.INCLUDES, TimingOperator.START_OF);
		graph.addEdge(TimingOperator.INCLUDES, TimingOperator.END_OF);
		graph.addEdge(TimingOperator.INCLUDES, TimingOperator.DONE_NODE);
		
		addChildToDateTimePrecisionSpecifier(graph, TimingOperator.START_OF);
		addChildToDateTimePrecisionSpecifier(graph, TimingOperator.END_OF);

		graph.addEdge(TimingOperator.START_OF, TimingOperator.DONE_NODE);
		graph.addEdge(TimingOperator.END_OF, TimingOperator.DONE_NODE);
		
		return graph;
	}
	
    // ('starts' | 'ends' | 'occurs')? 'properly'? ('during' | 'included in') dateTimePrecisionSpecifier? #includedInIntervalOperatorPhrase
	public static TimingGraph getIncludedInIntervalOperatorPhrase() {
		TimingGraph graph = new TimingGraph();
		
		// starting with starts, ends, occurs
		graph.addEdge(TimingOperator.BEGINNING_NODE, TimingOperator.STARTS);
		graph.addEdge(TimingOperator.BEGINNING_NODE, TimingOperator.ENDS);
		graph.addEdge(TimingOperator.BEGINNING_NODE, TimingOperator.OCCURS);
		
		// starting with properly
		graph.addEdge(TimingOperator.BEGINNING_NODE, TimingOperator.PROPERLY);
		
		// starting with during or included in
		graph.addEdge(TimingOperator.BEGINNING_NODE, TimingOperator.DURING);
		graph.addEdge(TimingOperator.BEGINNING_NODE, TimingOperator.INCLUDED_IN);
		
		graph.addEdge(TimingOperator.STARTS, TimingOperator.PROPERLY);
		graph.addEdge(TimingOperator.ENDS, TimingOperator.PROPERLY);
		graph.addEdge(TimingOperator.OCCURS, TimingOperator.PROPERLY);
		
		graph.addEdge(TimingOperator.STARTS, TimingOperator.DURING);
		graph.addEdge(TimingOperator.ENDS, TimingOperator.DURING);
		graph.addEdge(TimingOperator.OCCURS, TimingOperator.DURING);
		
		graph.addEdge(TimingOperator.STARTS, TimingOperator.INCLUDED_IN);
		graph.addEdge(TimingOperator.ENDS, TimingOperator.INCLUDED_IN);
		graph.addEdge(TimingOperator.OCCURS, TimingOperator.INCLUDED_IN);
		
		graph.addEdge(TimingOperator.PROPERLY, TimingOperator.DURING);
		graph.addEdge(TimingOperator.PROPERLY, TimingOperator.INCLUDED_IN);
		
		addDateTimePrecisionSpecifierToParent(graph, TimingOperator.DURING);
		
		graph.addEdge(TimingOperator.DURING, TimingOperator.DONE_NODE);

		addDateTimePrecisionSpecifierToParent(graph, TimingOperator.INCLUDED_IN);

		graph.addEdge(TimingOperator.INCLUDED_IN, TimingOperator.DONE_NODE);
		
		return graph;
	}
	
	// ('starts' | 'ends' | 'occurs')? quantityOffset? temporalRelationship dateTimePrecisionSpecifier? ('start' | 'end')? 
	public static TimingGraph getBeforeOrAfterIntervalOperatorPhraseGraph() {
		TimingGraph graph = new TimingGraph();
		graph.addEdge(TimingOperator.BEGINNING_NODE, TimingOperator.STARTS);
		graph.addEdge(TimingOperator.BEGINNING_NODE, TimingOperator.ENDS);
		graph.addEdge(TimingOperator.BEGINNING_NODE, TimingOperator.OCCURS);
		
		addQuantityOffsetToParent(graph, TimingOperator.BEGINNING_NODE);
		addTemporalRelationshipToParent(graph, TimingOperator.BEGINNING_NODE);
		
		addQuantityOffsetToParent(graph, TimingOperator.STARTS);
		addQuantityOffsetToParent(graph, TimingOperator.ENDS);
		addQuantityOffsetToParent(graph, TimingOperator.OCCURS);

		addTemporalRelationshipToParent(graph, TimingOperator.STARTS);
		addTemporalRelationshipToParent(graph, TimingOperator.ENDS);
		addTemporalRelationshipToParent(graph, TimingOperator.OCCURS);

		addChildToQuantityOffset(graph, TimingOperator.BEFORE);
		addChildToQuantityOffset(graph, TimingOperator.AFTER);
		addChildToQuantityOffset(graph, TimingOperator.BEFORE_OR_ON);
		addChildToQuantityOffset(graph, TimingOperator.AFTER_OR_ON);
		addChildToQuantityOffset(graph, TimingOperator.ON_OR_BEFORE);
		addChildToQuantityOffset(graph, TimingOperator.ON_OR_AFTER);

		addDateTimePrecisionSpecifierToParent(graph, TimingOperator.BEFORE);
		addDateTimePrecisionSpecifierToParent(graph, TimingOperator.AFTER);
		addDateTimePrecisionSpecifierToParent(graph, TimingOperator.BEFORE_OR_ON);
		addDateTimePrecisionSpecifierToParent(graph, TimingOperator.AFTER_OR_ON);
		addDateTimePrecisionSpecifierToParent(graph, TimingOperator.ON_OR_BEFORE);
		addDateTimePrecisionSpecifierToParent(graph, TimingOperator.ON_OR_AFTER);

		addChildToTemporalRelationship(graph,  TimingOperator.START_OF);
		addChildToTemporalRelationship(graph,  TimingOperator.END_OF);
		addChildToTemporalRelationship(graph,  TimingOperator.DONE_NODE);

		addChildToDateTimePrecisionSpecifier(graph, TimingOperator.START_OF);
		addChildToDateTimePrecisionSpecifier(graph, TimingOperator.END_OF);
		addChildToDateTimePrecisionSpecifier(graph, TimingOperator.DONE_NODE);


		graph.addEdge(TimingOperator.START_OF, TimingOperator.DONE_NODE);
		graph.addEdge(TimingOperator.END_OF, TimingOperator.DONE_NODE);

		return graph;
	}
	
    // ('starts' | 'ends' | 'occurs')? 'properly'? 'within' quantity 'of' ('start' | 'end')? #withinIntervalOperatorPhrase
	public static TimingGraph getWithinIntervalOperatorPhraseGraph() {
		TimingGraph graph = new TimingGraph();
		graph.addEdge(TimingOperator.BEGINNING_NODE, TimingOperator.STARTS);
		graph.addEdge(TimingOperator.BEGINNING_NODE, TimingOperator.ENDS);
		graph.addEdge(TimingOperator.BEGINNING_NODE, TimingOperator.OCCURS);
		graph.addEdge(TimingOperator.BEGINNING_NODE, TimingOperator.PROPERLY);
		graph.addEdge(TimingOperator.BEGINNING_NODE, TimingOperator.WITHIN);

		graph.addEdge(TimingOperator.STARTS, TimingOperator.PROPERLY);
		graph.addEdge(TimingOperator.ENDS, TimingOperator.PROPERLY);
		graph.addEdge(TimingOperator.OCCURS, TimingOperator.PROPERLY);
		
		graph.addEdge(TimingOperator.STARTS, TimingOperator.WITHIN);
		graph.addEdge(TimingOperator.ENDS, TimingOperator.WITHIN);
		graph.addEdge(TimingOperator.OCCURS, TimingOperator.WITHIN);
		
		graph.addEdge(TimingOperator.PROPERLY, TimingOperator.WITHIN);
		
		graph.addEdge(TimingOperator.WITHIN, TimingOperator.QUANTITY_OF);
		
		graph.addEdge(TimingOperator.QUANTITY_OF, TimingOperator.START_OF);
		graph.addEdge(TimingOperator.QUANTITY_OF, TimingOperator.END_OF);
		graph.addEdge(TimingOperator.QUANTITY_OF, TimingOperator.DONE_NODE);

		graph.addEdge(TimingOperator.START_OF, TimingOperator.DONE_NODE);
		graph.addEdge(TimingOperator.END_OF, TimingOperator.DONE_NODE);
		
		
		return graph;
	}
	
	// 'meets' ('before' | 'after')? dateTimePrecisionSpecifier? #meetsIntervalOperatorPhrase
	public static TimingGraph getMeetsIntervalOperatorPhrase() {
		TimingGraph graph = new TimingGraph();

		graph.addEdge(TimingOperator.BEGINNING_NODE, TimingOperator.MEETS);
		
		graph.addEdge(TimingOperator.MEETS, TimingOperator.BEFORE);
		graph.addEdge(TimingOperator.MEETS, TimingOperator.AFTER);
		addDateTimePrecisionSpecifierToParent(graph, TimingOperator.MEETS);

		graph.addEdge(TimingOperator.MEETS, TimingOperator.DONE_NODE);

		addDateTimePrecisionSpecifierToParent(graph, TimingOperator.BEFORE);
		addDateTimePrecisionSpecifierToParent(graph, TimingOperator.AFTER);

		graph.addEdge(TimingOperator.BEFORE, TimingOperator.DONE_NODE);
		graph.addEdge(TimingOperator.AFTER, TimingOperator.DONE_NODE);
		
		addChildToDateTimePrecisionSpecifier(graph,  TimingOperator.DONE_NODE);

		return graph;
	}

    // 'overlaps' ('before' | 'after')? dateTimePrecisionSpecifier? #overlapsIntervalOperatorPhrase
	public static TimingGraph getOverlapsIntervalOperatorPhrase() {
		TimingGraph graph = new TimingGraph();

		graph.addEdge(TimingOperator.BEGINNING_NODE, TimingOperator.OVERLAPS);
		
		graph.addEdge(TimingOperator.OVERLAPS, TimingOperator.BEFORE);
		graph.addEdge(TimingOperator.OVERLAPS, TimingOperator.AFTER);
		addDateTimePrecisionSpecifierToParent(graph, TimingOperator.OVERLAPS);
		
		graph.addEdge(TimingOperator.OVERLAPS, TimingOperator.DONE_NODE);
		
		addDateTimePrecisionSpecifierToParent(graph, TimingOperator.BEFORE);
		addDateTimePrecisionSpecifierToParent(graph, TimingOperator.AFTER);
		
		graph.addEdge(TimingOperator.BEFORE, TimingOperator.DONE_NODE);
		graph.addEdge(TimingOperator.AFTER, TimingOperator.DONE_NODE);
				
		addChildToDateTimePrecisionSpecifier(graph, TimingOperator.DONE_NODE);
		
		return graph;
	}

	
	// 'starts' dateTimePrecisionSpecifier? #startsIntervalOperatorPhrase
	public static TimingGraph getStartsIntervalOperatorPhraseGraph() {
		TimingGraph graph = new TimingGraph();
		graph.addEdge(TimingOperator.BEGINNING_NODE, TimingOperator.STARTS);
		
		addDateTimePrecisionSpecifierToParent(graph, TimingOperator.STARTS);
		
		graph.addEdge(TimingOperator.STARTS, TimingOperator.DONE_NODE);
		
		addChildToDateTimePrecisionSpecifier(graph, TimingOperator.DONE_NODE);

		return graph;
	}
	
    // 'ends' dateTimePrecisionSpecifier? #endsIntervalOperatorPhrase
	public static TimingGraph getEndsIntervalOperatorPhraseGraph() {
		TimingGraph graph = new TimingGraph();
		graph.addEdge(TimingOperator.BEGINNING_NODE, TimingOperator.ENDS);
		
		addDateTimePrecisionSpecifierToParent(graph, TimingOperator.ENDS);
		
		graph.addEdge(TimingOperator.ENDS, TimingOperator.DONE_NODE);
		
		addChildToDateTimePrecisionSpecifier(graph, TimingOperator.DONE_NODE);

		return graph;
	}
	
	private static void addRelativeQualifierToParent(TimingGraph graph, TimingOperator parent) {
		graph.addEdge(parent, TimingOperator.OR_BEFORE);
		graph.addEdge(parent, TimingOperator.OR_AFTER);
	}
	
	private static void addChildToRelativeQualifier(TimingGraph graph, TimingOperator child) {
		graph.addEdge(TimingOperator.OR_BEFORE, child);
		graph.addEdge(TimingOperator.OR_AFTER, child);
	}
	
	private static void addQuantityOffsetToParent(TimingGraph graph, TimingOperator parent) {
		graph.addEdge(parent, TimingOperator.QUANTITY_OR_MORE);
		graph.addEdge(parent, TimingOperator.QUANTITY_OR_LESS);
		graph.addEdge(parent, TimingOperator.LESS_THAN_QUANTITY);
		graph.addEdge(parent, TimingOperator.MORE_THAN_QUANTITY);
	}
	
	private static void addChildToQuantityOffset(TimingGraph graph, TimingOperator child) {
		graph.addEdge(TimingOperator.QUANTITY_OR_MORE, child);
		graph.addEdge(TimingOperator.QUANTITY_OR_LESS, child);
		graph.addEdge(TimingOperator.LESS_THAN_QUANTITY, child);
		graph.addEdge(TimingOperator.MORE_THAN_QUANTITY, child);
	}
	
	private static void addTemporalRelationshipToParent(TimingGraph graph, TimingOperator parent) {
		graph.addEdge(parent, TimingOperator.BEFORE);
		graph.addEdge(parent, TimingOperator.AFTER);
		graph.addEdge(parent, TimingOperator.BEFORE_OR_ON);
		graph.addEdge(parent, TimingOperator.AFTER_OR_ON);
		graph.addEdge(parent, TimingOperator.ON_OR_BEFORE);
		graph.addEdge(parent, TimingOperator.ON_OR_AFTER);
	}
	
	private static void addChildToTemporalRelationship(TimingGraph graph, TimingOperator child) {
		graph.addEdge(TimingOperator.BEFORE, child);
		graph.addEdge(TimingOperator.AFTER, child);
		graph.addEdge(TimingOperator.BEFORE_OR_ON, child);
		graph.addEdge(TimingOperator.AFTER_OR_ON, child);
		graph.addEdge(TimingOperator.ON_OR_BEFORE, child);
		graph.addEdge(TimingOperator.ON_OR_AFTER, child);
	}
	
	private static void addDateTimePrecisionToParent(TimingGraph graph, TimingOperator parent) {
		graph.addEdge(parent, TimingOperator.YEAR);
		graph.addEdge(parent, TimingOperator.MONTH);
		graph.addEdge(parent, TimingOperator.WEEK);
		graph.addEdge(parent, TimingOperator.DAY);
		graph.addEdge(parent, TimingOperator.HOUR);
		graph.addEdge(parent, TimingOperator.MINUTE);
		graph.addEdge(parent, TimingOperator.SECOND);
		graph.addEdge(parent, TimingOperator.MILISECOND);
	}
	
	private static void addChildToDateTimePrecision(TimingGraph graph, TimingOperator child) {
		graph.addEdge(TimingOperator.YEAR, child);
		graph.addEdge(TimingOperator.MONTH, child);
		graph.addEdge(TimingOperator.WEEK, child);
		graph.addEdge(TimingOperator.DAY, child);
		graph.addEdge(TimingOperator.HOUR, child);
		graph.addEdge(TimingOperator.MINUTE, child);
		graph.addEdge(TimingOperator.SECOND, child);
		graph.addEdge(TimingOperator.MILISECOND, child);
	}
	
	private static void addDateTimePrecisionSpecifierToParent(TimingGraph graph, TimingOperator parent) {
		graph.addEdge(parent, TimingOperator.YEAR_OF);
		graph.addEdge(parent, TimingOperator.MONTH_OF);
		graph.addEdge(parent, TimingOperator.WEEK_OF);
		graph.addEdge(parent, TimingOperator.DAY_OF);
		graph.addEdge(parent, TimingOperator.HOUR_OF);
		graph.addEdge(parent, TimingOperator.MINUTE_OF);
		graph.addEdge(parent, TimingOperator.SECOND_OF);
		graph.addEdge(parent, TimingOperator.MILISECOND_OF);
	}
	
	private static void addChildToDateTimePrecisionSpecifier(TimingGraph graph, TimingOperator child) {
		graph.addEdge(TimingOperator.YEAR_OF, child);
		graph.addEdge(TimingOperator.MONTH_OF, child);
		graph.addEdge(TimingOperator.WEEK_OF, child);
		graph.addEdge(TimingOperator.DAY_OF, child);
		graph.addEdge(TimingOperator.HOUR_OF, child);
		graph.addEdge(TimingOperator.MINUTE_OF, child);
		graph.addEdge(TimingOperator.SECOND_OF, child);
		graph.addEdge(TimingOperator.MILISECOND_OF, child);
	}
	
	public static List<TimingGraph> getTimingGraphs() {
		List<TimingGraph> graphs = new ArrayList<>();
		
		graphs.add(getConcurrentWithIntervalOperatorPhraseGraph());
		graphs.add(getIncludesIntervalOperatorPhraseGraph());
		graphs.add(getIncludedInIntervalOperatorPhrase());
		graphs.add(getBeforeOrAfterIntervalOperatorPhraseGraph());
		graphs.add(getWithinIntervalOperatorPhraseGraph());
		graphs.add(getMeetsIntervalOperatorPhrase());
		graphs.add(getOverlapsIntervalOperatorPhrase());
		graphs.add(getStartsIntervalOperatorPhraseGraph());
		graphs.add(getEndsIntervalOperatorPhraseGraph());
		
		return graphs;
	}
	
	public static List<TimingGraph> getTimingGraphsWithPath(List<TimingOperator> path) {
		path.add(0, TimingOperator.BEGINNING_NODE);
		List<TimingGraph> graphsWithPath = new ArrayList<>();

		List<TimingGraph> allTimingGraphs = getTimingGraphs();
		
		for(TimingGraph graph : allTimingGraphs) {
			if(hasPath(graph, path)) {
				graphsWithPath.add(graph);
			}
		}
		
		
		return graphsWithPath;
	}
	
	public static boolean hasPath(TimingGraph graph, List<TimingOperator> path) {
		for(int i = 0; i < path.size() - 1; i++) {		
			TimingOperator currentOperator = path.get(i);
			TimingOperator nextOperator = path.get(i + 1);

			List<TimingOperator> edges = graph.getGraph().get(currentOperator);
			if(!edges.contains(nextOperator)) {
				return false;
			}
		}
		 
		return true;
	}
}
