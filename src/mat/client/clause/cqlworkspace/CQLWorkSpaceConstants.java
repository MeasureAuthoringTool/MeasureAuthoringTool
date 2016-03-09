package mat.client.clause.cqlworkspace;

import java.util.ArrayList;

public class CQLWorkSpaceConstants {
	public static String CQL_MODEL_DATA_TYPE ="QDM Datatype";
	public static String CQL_OTHER_DATA_TYPE ="Others";
	private static final ArrayList<String> AVAILABLE_ITEM_TO_INSERT = new ArrayList<String>();
	
	public static ArrayList<String> getAvailableItem() {
		AVAILABLE_ITEM_TO_INSERT.clear();
		AVAILABLE_ITEM_TO_INSERT.add("Parameters");
		AVAILABLE_ITEM_TO_INSERT.add("Definitions");
		AVAILABLE_ITEM_TO_INSERT.add("Functions");
		AVAILABLE_ITEM_TO_INSERT.add("Timing");
		AVAILABLE_ITEM_TO_INSERT.add("CQL Functions");
		AVAILABLE_ITEM_TO_INSERT.add("Applied QDM");
		return AVAILABLE_ITEM_TO_INSERT;
	}
	
}
