package mat.client.clause.cqlworkspace;

import java.util.ArrayList;

public class CQLWorkSpaceConstants {
	public static String CQL_MODEL_DATA_TYPE ="QDM Datatype";
	public static String CQL_OTHER_DATA_TYPE ="Others";
	public static String CQL_TIMING_EXPRESSION ="Build CQL Timing Expression";
	public static String CQL_PRIMARY_TIMING_WITHIN ="within";
	
	private static final ArrayList<String> AVAILABLE_ITEM_TO_INSERT = new ArrayList<String>();
	
	private static final ArrayList<String> PRIMARY_TIMINGS = new ArrayList<String>();
	
	private static final ArrayList<String> TIMING_PRECISIONS = new ArrayList<String>();
	
	private static final ArrayList<String> QUANTITY_OFFSET_UNITS = new ArrayList<String>();
	
	private static final ArrayList<String> RELATIVE_QUALIFIER = new ArrayList<String>();
	
	private static final ArrayList<String> WITHIN_TIMING_EXP = new ArrayList<String>();	
	public static ArrayList<String> getAvailableItem() {
		AVAILABLE_ITEM_TO_INSERT.clear();
		AVAILABLE_ITEM_TO_INSERT.add("Parameters");
		AVAILABLE_ITEM_TO_INSERT.add("Definitions");
		AVAILABLE_ITEM_TO_INSERT.add("Functions");
		/*AVAILABLE_ITEM_TO_INSERT.add("Timing");*/
		AVAILABLE_ITEM_TO_INSERT.add("Pre-Defined Functions");
		AVAILABLE_ITEM_TO_INSERT.add("Applied QDM");
		return AVAILABLE_ITEM_TO_INSERT;
	}
	
	public static ArrayList<String> getPrimaryTimings() {
		PRIMARY_TIMINGS.clear();
		PRIMARY_TIMINGS.add("after");
		PRIMARY_TIMINGS.add("before");
		PRIMARY_TIMINGS.add("during");
		PRIMARY_TIMINGS.add("ends");
		PRIMARY_TIMINGS.add("included in");
		PRIMARY_TIMINGS.add("includes");
		PRIMARY_TIMINGS.add("meets");
		PRIMARY_TIMINGS.add("overlaps");
		PRIMARY_TIMINGS.add("same as");
		PRIMARY_TIMINGS.add("same or after");
		PRIMARY_TIMINGS.add("same or before");
		PRIMARY_TIMINGS.add("starts");
		PRIMARY_TIMINGS.add("within");
		return PRIMARY_TIMINGS;
	}
	
	
	public static ArrayList<String> getTimingPrecisions() {
		TIMING_PRECISIONS.clear();
		TIMING_PRECISIONS.add("day");
		TIMING_PRECISIONS.add("hour");
		TIMING_PRECISIONS.add("millisecond");
		TIMING_PRECISIONS.add("minute");
		TIMING_PRECISIONS.add("month");
		TIMING_PRECISIONS.add("second");
		TIMING_PRECISIONS.add("year");
		return TIMING_PRECISIONS;
	}
	
	public static ArrayList<String> getRelativeQualifiers() {
		RELATIVE_QUALIFIER.clear();
		RELATIVE_QUALIFIER.add("or more");
		RELATIVE_QUALIFIER.add("or less");
		return RELATIVE_QUALIFIER;
	}
	
	
	public static ArrayList<String> getQuantityOffsetUnits() {
		QUANTITY_OFFSET_UNITS.clear();
		QUANTITY_OFFSET_UNITS.add("day");
		QUANTITY_OFFSET_UNITS.add("days");
		QUANTITY_OFFSET_UNITS.add("hour");
		QUANTITY_OFFSET_UNITS.add("hours");
		QUANTITY_OFFSET_UNITS.add("millisecond");
		QUANTITY_OFFSET_UNITS.add("milliseconds");
		QUANTITY_OFFSET_UNITS.add("minute");
		QUANTITY_OFFSET_UNITS.add("minutes");
		QUANTITY_OFFSET_UNITS.add("month");
		QUANTITY_OFFSET_UNITS.add("months");
		QUANTITY_OFFSET_UNITS.add("second");
		QUANTITY_OFFSET_UNITS.add("seconds");
		QUANTITY_OFFSET_UNITS.add("year");
		QUANTITY_OFFSET_UNITS.add("years");
		return QUANTITY_OFFSET_UNITS;
	}
	

	public static ArrayList<String> getWithinTimingExp() {
		WITHIN_TIMING_EXP.clear();
		WITHIN_TIMING_EXP.add("within");
		WITHIN_TIMING_EXP.add("ends within");
		WITHIN_TIMING_EXP.add("starts within");
		WITHIN_TIMING_EXP.add("properly within");
		WITHIN_TIMING_EXP.add("ends properly within");
		WITHIN_TIMING_EXP.add("starts properly within");
		WITHIN_TIMING_EXP.add("ends within end");
		WITHIN_TIMING_EXP.add("ends within start");
		WITHIN_TIMING_EXP.add("starts within end");
		WITHIN_TIMING_EXP.add("starts within start");
		WITHIN_TIMING_EXP.add("properly within end");
		WITHIN_TIMING_EXP.add("properly within start");
		WITHIN_TIMING_EXP.add("ends properly within end");
		WITHIN_TIMING_EXP.add("ends properly within start");
		WITHIN_TIMING_EXP.add("starts properly within end");
		WITHIN_TIMING_EXP.add("starts properly within start");
		return WITHIN_TIMING_EXP;
	}
	
	
	public final static String CQL_FUNCTION_MENU = "func";
	public final static String CQL_VIEW_MENU = "view";
	public final static String CQL_DEFINE_MENU = "define";
	public final static String CQL_PARAMETER_MENU = "param";
	public final static String CQL_GENERAL_MENU = "general";
	public final static String CQL_DEFAULT_MEASUREMENTPERIOD_PARAMETER_NAME = "MeasurementPeriod";
	public final static String CQL_DEFAULT_MEASUREMENTPERIOD_PARAMETER_LOGIC = "Interval<DateTime>";
	public static final String CQL_DEFAULT_DEFINITON_CONTEXT = "Patient";
	
}
