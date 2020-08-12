package mat.client.expressionbuilder.util;

import mat.shared.CQLIdentifierObject;

import java.util.List;

public class IdentifierSortUtil {
	
	private IdentifierSortUtil() {
		throw new IllegalStateException("Identifier Sort Util");
	}
	
	public static List<CQLIdentifierObject> sortIdentifierList(List<CQLIdentifierObject> identifierList) {
		identifierList.sort((CQLIdentifierObject identifier1, CQLIdentifierObject identifier2) -> identifier1.getDisplay().compareToIgnoreCase(identifier2.getDisplay()));
		return identifierList;
	}
}
