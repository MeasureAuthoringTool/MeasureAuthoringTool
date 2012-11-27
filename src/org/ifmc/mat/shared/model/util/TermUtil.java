package org.ifmc.mat.shared.model.util;

import java.util.Arrays;
import java.util.List;


public class TermUtil {
	public static List<String> functions = Arrays.asList(new String[]  {
			"ABS","ADDDATE","ADDTIME","AVG","COUNTDISTINCT","COUNT","CURDATE","CURTIME","DATEDIFF","DAYOFMONTH","DAYOFWEEK","DAYOFYEAR","HOUR","MAX",
			"MEDIAN","MIN",
				"MINUTE","MONTH","NOW","POSITION","ROUND","SEC","STDDEV","SUBDATE","SUBTIME","SUM","TIME",
				"TIMEDIFF","VARIANCE","WEEK"," WEEKDAY","WEEKOFYEAR","YEAR","YEARWEEK","FIRST","SECOND",
				"THIRD","FOURTH","FIFTH","LAST","RELATIVEFIRST","RELATIVESECOND", "NOT"});
	public boolean isFunctionTerm(String op) {
		return functions.contains(op);
//		if (op.equalsIgnoreCase("COUNT")) {
//			return true;
//		} else if (op.equalsIgnoreCase("NOT")) {
//			return true;
//		} else if (op.equalsIgnoreCase("FIRST")) {
//			return true;
//		} else if (op.equalsIgnoreCase("SECOND")) {
//			return true;
//		} else if (op.equalsIgnoreCase("THIRD")) {
//			return true;
//		} else if (op.equalsIgnoreCase("LAST")) {
//			return true;
//		} else if (op.equalsIgnoreCase("MIN")) {
//			return true;
//		} else if (op.equalsIgnoreCase("MAX")) {
//			return true;
//		}
//		return false;
	}
	public boolean isConditional(String op) {
		if (op.equalsIgnoreCase("OR")) {
			return true;
		} else if (op.equalsIgnoreCase("AND")) {
			return true;
		}
		return false;
	}
	
	public boolean isClauseOperation(String op) {
		if (op.equalsIgnoreCase("CLAUSE")) {
			return true;
		}
		return false;
	}

	public boolean isQDSTermOperation(String op) {
		if (op.equalsIgnoreCase("QDSTERM")) {
			return true;
		}
		return false;
	}
	
	public boolean isAttributeTermOperation(String op) {
		if (op.equalsIgnoreCase("QDSATTRIBUTE")) {
			return true;
		}
		return false;
	}

	public boolean isMeasurementTermOperation(String op) {
		if (op.equalsIgnoreCase("MEASUREMENTTERM")) {
			return true;
		}
		return false;
	}
	public boolean isStatementTermOperator(String op) {
		if (op.equalsIgnoreCase("LESS_THAN")) {
			return true;
		} else if (op.equalsIgnoreCase("GREATER_THAN")) {
			return true;
		} else if (op.equalsIgnoreCase("LESS_THAN_OR_EQUAL_TO")) {
			return true;
		} else if (op.equalsIgnoreCase("GREATER_THAN_OR_EQUAL_TO")) {
			return true;
		} else if (op.equalsIgnoreCase("NOT_EQUAL_TO")) {
			return true;
		} else if (op.equalsIgnoreCase("EQUAL_TO")) {
			return true;
		} else if (op.equalsIgnoreCase("MINUS")) {
			return true;
		} else if (op.equalsIgnoreCase("PLUS")) {
			return true;
		} else if (op.equalsIgnoreCase("TIMES")) {
			return true;
		} else if (op.equalsIgnoreCase("DIVIDE_BY")) {
			return true;
		} else if (op.equalsIgnoreCase("IS_NOT_NULL")) {
			return true;
		} else if (op.equalsIgnoreCase("IS_NULL")) {
			return true;
		} else if (op.equalsIgnoreCase("LIKE")) {
			return true;
		}
		return false;		
	}
	public boolean isAtttibuteTermOperator(String op) {
		if (op.equalsIgnoreCase("A_LESS_THAN")) {
			return true;
		} else if (op.equalsIgnoreCase("A_GREATER_THAN")) {
			return true;
		} else if (op.equalsIgnoreCase("A_LESS_THAN_OR_EQUAL_TO")) {
			return true;
		} else if (op.equalsIgnoreCase("A_GREATER_THAN_OR_EQUAL_TO")) {
			return true;
		} else if (op.equalsIgnoreCase("A_EQUAL_TO")) {
			return true;
		} else if (op.equalsIgnoreCase("A_NOT_EQUAL_TO")) {
			return true;
		} else if (op.equalsIgnoreCase("A_ADDED_TO")) {
			return true;

		} else if (op.equalsIgnoreCase("A_SUBTRACTED_FROM")) {
			return true;
		} else if (op.equalsIgnoreCase("A_TIMES")) {
			return true;
		} else if (op.equalsIgnoreCase("A_DIVIDED_BY")) {
			return true;
		} else if (op.equalsIgnoreCase("A_IS_NOT_NULL")) {
			return true;
		} else if (op.equalsIgnoreCase("A_IS_NULL")) {
			return true;
		} else if (op.equalsIgnoreCase("A_LIKE")) {
			return true;
		} else if (op.equalsIgnoreCase("A_NOT_EMPTY")) {
			return true;
		}

		return false;
	}
	public boolean isQDSMeasurementTermOperator(String op) {
		//US171 including Relative Association Operators
		if (op.equalsIgnoreCase("QDS")) {
			return true;
		} else if (op.equalsIgnoreCase("EAE")) {
			return true;
		} else if (op.equalsIgnoreCase("EAS")) {
			return true;
		} else if (op.equalsIgnoreCase("EBOD")) {
			return true;
		} else if (op.equalsIgnoreCase("EBS")) {
			return true;
		} else if (op.equalsIgnoreCase("ECW")) {
			return true;
		} else if (op.equalsIgnoreCase("EDU")) {
			return true;
		} else if (op.equalsIgnoreCase("SBOD")) {
			return true;
		} else if (op.equalsIgnoreCase("SAE")) {
			return true;
		} else if (op.equalsIgnoreCase("SAS")) {
			return true;
		} else if (op.equalsIgnoreCase("SBS")) {
			return true;
		} else if (op.equalsIgnoreCase("SCW")) {
			return true;
		} else if (op.equalsIgnoreCase("SDU")) {
			return true;
		} else if (op.equalsIgnoreCase("DURING")) {
			return true;
		} else if (op.equalsIgnoreCase("LINKEDTO")) {
			return true;
		} else if (op.equalsIgnoreCase("CONCURRENT")) {
			return true;
		}else if (op.equalsIgnoreCase("AUTH")) {
			return true;
		}else if (op.equalsIgnoreCase("CAUS")) {
			return true;
		}else if (op.equalsIgnoreCase("DRIV")) {
			return true;
		}else if (op.equalsIgnoreCase("GOAL")) {
			return true;
		}else if (op.equalsIgnoreCase("OUTC")) {
			return true;
		}
		return false;	
	}
}
