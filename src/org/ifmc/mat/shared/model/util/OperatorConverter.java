package org.ifmc.mat.shared.model.util;

import org.ifmc.mat.shared.model.AttributeTerm;
import org.ifmc.mat.shared.model.Conditional;
import org.ifmc.mat.shared.model.QDSMeasurementTerm;
import org.ifmc.mat.shared.model.StatementTerm;

public class OperatorConverter {
	//US171 including Relative Association Operators
	public QDSMeasurementTerm.QDSOperator toQDSMeasurementTermOp(String op) {
		if (op.equalsIgnoreCase("QDS")) {
			return QDSMeasurementTerm.QDSOperator.QDS;
		} else if (op.equalsIgnoreCase("EAE")) {
			return QDSMeasurementTerm.QDSOperator.EAE;
		} else if (op.equalsIgnoreCase("EBS")) {
			return QDSMeasurementTerm.QDSOperator.EBS;
		} else if (op.equalsIgnoreCase("ECW")) {
			return QDSMeasurementTerm.QDSOperator.ECW;
		} else if (op.equalsIgnoreCase("SBOD")) {
			return QDSMeasurementTerm.QDSOperator.SBOD;
		} else if (op.equalsIgnoreCase("SAE")) {
			return QDSMeasurementTerm.QDSOperator.SAE;			
		} else if (op.equalsIgnoreCase("SAS")) {
			return QDSMeasurementTerm.QDSOperator.SAS;			
		} else if (op.equalsIgnoreCase("SBS")) {
			return QDSMeasurementTerm.QDSOperator.SBS;			
		} else if (op.equalsIgnoreCase("DURING")) {
			return QDSMeasurementTerm.QDSOperator.DURING;			
		} else if (op.equalsIgnoreCase("CONCURRENT")) {
			return QDSMeasurementTerm.QDSOperator.CONCURRENT;			
		} else if (op.equalsIgnoreCase("EAS")) {
			return QDSMeasurementTerm.QDSOperator.EAS;			
		} else if (op.equalsIgnoreCase("EBOD")) {
			return QDSMeasurementTerm.QDSOperator.EBOD;			
		} else if (op.equalsIgnoreCase("EDU")) {
			return QDSMeasurementTerm.QDSOperator.EDU;			
		} else if (op.equalsIgnoreCase("SCW")) {
			return QDSMeasurementTerm.QDSOperator.SCW;			
		} else if (op.equalsIgnoreCase("SDU")) {
			return QDSMeasurementTerm.QDSOperator.SDU;			
		}else if (op.equalsIgnoreCase("AUTH")) {
			return QDSMeasurementTerm.QDSOperator.AUTH;			
		}else if (op.equalsIgnoreCase("CAUS")) {
			return QDSMeasurementTerm.QDSOperator.CAUS;			
		}else if (op.equalsIgnoreCase("DRIV")) {
			return QDSMeasurementTerm.QDSOperator.DRIV;			
		}else if (op.equalsIgnoreCase("GOAL")) {
			return QDSMeasurementTerm.QDSOperator.GOAL;			
		}else if (op.equalsIgnoreCase("OUTC")) {
			return QDSMeasurementTerm.QDSOperator.OUTC;			
		}
		return null;	
	}
	
	public StatementTerm.Operator toStatementTermOp(String op) {
		if (op.equalsIgnoreCase("LESS_THAN")) {
			return StatementTerm.Operator.LESS_THAN;
		} else if (op.equalsIgnoreCase("GREATER_THAN")) {
			return StatementTerm.Operator.GREATER_THAN;
		} else if (op.equalsIgnoreCase("LESS_THAN_OR_EQUAL_TO")) {
			return StatementTerm.Operator.LESS_THAN_OR_EQUAL_TO;
		} else if (op.equalsIgnoreCase("GREATER_THAN_OR_EQUAL_TO")) {
			return StatementTerm.Operator.GREATER_THAN_OR_EQUAL_TO;
		} else if (op.equalsIgnoreCase("NOT_EQUAL_TO")) {
			return StatementTerm.Operator.NOT_EQUAL_TO;
		} else if (op.equalsIgnoreCase("EQUAL_TO")) {
			return StatementTerm.Operator.EQUAL_TO;			
		} else if (op.equalsIgnoreCase("MINUS")) {
			return StatementTerm.Operator.MINUS;
		} else if (op.equalsIgnoreCase("PLUS")) {
			return StatementTerm.Operator.PLUS;			
		} else if (op.equalsIgnoreCase("TIMES")) {
			return StatementTerm.Operator.TIMES;			
		} else if (op.equalsIgnoreCase("DIVIDE_BY")) {
			return StatementTerm.Operator.DIVIDE_BY;			
		} else if (op.equalsIgnoreCase("IS_NOT_NULL")) {
			return StatementTerm.Operator.IS_NOT_NULL;			
		} else if (op.equalsIgnoreCase("IS_NULL")) {
			return StatementTerm.Operator.IS_NULL;			
		} else if (op.equalsIgnoreCase("LIKE")) {
			return StatementTerm.Operator.LIKE;			
		}
		return null;
	}
	public AttributeTerm.Operator toAttributeTermOp(String op) {
		if (op.equalsIgnoreCase("A_LESS_THAN")) {
			return AttributeTerm.Operator.A_LESS_THAN;
		} else if (op.equalsIgnoreCase("A_GREATER_THAN")) {
			return AttributeTerm.Operator.A_GREATER_THAN;
		} else if (op.equalsIgnoreCase("A_LESS_THAN_OR_EQUAL_TO")) {
			return AttributeTerm.Operator.A_LESS_THAN_OR_EQUAL_TO;
		} else if (op.equalsIgnoreCase("A_GREATER_THAN_OR_EQUAL_TO")) {
			return AttributeTerm.Operator.A_GREATER_THAN_OR_EQUAL_TO;
		} else if (op.equalsIgnoreCase("A_NOT_EQUAL_TO")) {
			return AttributeTerm.Operator.A_NOT_EQUAL_TO;
		} else if (op.equalsIgnoreCase("A_EQUAL_TO")) {
			return AttributeTerm.Operator.A_EQUAL_TO;			
		}else if (op.equalsIgnoreCase("A_NOT_EMPTY")) {
			return AttributeTerm.Operator.A_NOT_EMPTY;			
		}
		return null;
	}

	public AttributeTerm.Operator clientToAttributeTermOp(String op) {
		if (op.equalsIgnoreCase("Less Than")) {
			return AttributeTerm.Operator.A_LESS_THAN;
		} else if (op.equalsIgnoreCase("Greater Than")) {
			return AttributeTerm.Operator.A_GREATER_THAN;
		} else if (op.equalsIgnoreCase("Less Than or Equal To")) {
			return AttributeTerm.Operator.A_LESS_THAN_OR_EQUAL_TO;
		} else if (op.equalsIgnoreCase("Greater Than or Equal To")) {
			return AttributeTerm.Operator.A_GREATER_THAN_OR_EQUAL_TO;
		} else if (op.equalsIgnoreCase("Equal To")) {
			return AttributeTerm.Operator.A_EQUAL_TO;			
		} else if (op.equalsIgnoreCase("Not Equal To")) {
			return AttributeTerm.Operator.A_NOT_EQUAL_TO;
			
		} else if (op.equalsIgnoreCase("Added To")) {
			return AttributeTerm.Operator.A_ADDED_TO;
		} else if (op.equalsIgnoreCase("Subtracted From")) {
			return AttributeTerm.Operator.A_SUBTRACTED_FROM;
		} else if (op.equalsIgnoreCase("Times")) {
			return AttributeTerm.Operator.A_TIMES;
		} else if (op.equalsIgnoreCase("Divided By")) {
			return AttributeTerm.Operator.A_DIVIDED_BY;
		} else if (op.equalsIgnoreCase("Is Not Null")) {
			return AttributeTerm.Operator.A_IS_NOT_NULL;
		} else if (op.equalsIgnoreCase("Is Null")) {
			return AttributeTerm.Operator.A_IS_NULL;
		} else if (op.equalsIgnoreCase("Like")) {
			return AttributeTerm.Operator.A_LIKE;
		} else if (op.equalsIgnoreCase("A_NOT_EMPTY")) {
			return AttributeTerm.Operator.A_NOT_EMPTY;
		}
		return null;
	}

	
	public Conditional.Operator toConditionalOp(String op) {
		if (op.equalsIgnoreCase("AND")) {
			return Conditional.Operator.AND;
		} else if (op.equalsIgnoreCase("OR")) {
			return Conditional.Operator.OR;
		}
		return null;
	}

	public String qDSMeasureTermToString(QDSMeasurementTerm.QDSOperator op) {
		return op.name();

//		switch (op) {		
//		case QDS:
//			return "QDS";
//		case EAE:
//			return "EAE";
//		case EBS:
//			return "EBS";
//		case ECW:
//			return "ECW";
//		case SBOD:
//			return "SBOD";
//		case SAE:
//			return "SAE";
//		case SAS:
//			return "SAS";
//		case SBS:
//			return "SBS";
//		case DURING:
//			return "DURING";
//		case LINKEDTO:
//			return "LINKEDTO";
//		default:
//			return "";
//		}
	}
	public String statementTermToString(StatementTerm.Operator op) {
		switch (op) {
		case LESS_THAN:
			return "LESS_THAN";
		case GREATER_THAN:
			return "GREATER_THAN";
		case LESS_THAN_OR_EQUAL_TO:
			return "LESS_THAN_OR_EQUAL_TO";
		case GREATER_THAN_OR_EQUAL_TO:
			return "GREATER_THAN_OR_EQUAL_TO";
		case NOT_EQUAL_TO:
			return "NOT_EQUAL_TO";
		case EQUAL_TO:
			return "EQUAL_TO";
		case MINUS:
			return "MINUS";
		case PLUS:
			return "PLUS";
		case TIMES:
			return "TIMES";
		case DIVIDE_BY:
			return "DIVIDE_BY";
		case IS_NOT_NULL:
			return "IS_NOT_NULL";
		case IS_NULL:
			return "IS_NULL";
		case LIKE:
			return "LIKE";
		default:
			return "";
		}
	}
	public String attributeTermToString(AttributeTerm.Operator op) {
		return op.name();
//		switch (op) {
//		case A_LESS_THAN:
//			return "A_LESS_THAN";
//		case A_GREATER_THAN:
//			return "A_GREATER_THAN";
//		case A_LESS_THAN_OR_EQUAL_TO:
//			return "A_LESS_THAN_OR_EQUAL_TO";
//		case A_GREATER_THAN_OR_EQUAL_TO:
//			return "A_GREATER_THAN_OR_EQUAL_TO";
//		case A_NOT_EQUAL_TO:
//			return "A_NOT_EQUAL_TO";
//		case A_EQUAL_TO:
//			return "A_EQUAL_TO";
//		default:
//			return "";
//		}
	}

	public String attributeTermToClientString(AttributeTerm.Operator op) {
		switch (op) {
		case A_LESS_THAN:
			return "Less Than";
		case A_GREATER_THAN:
			return "Greater Than";
		case A_LESS_THAN_OR_EQUAL_TO:
			return "Less Than or Equal To";
		case A_GREATER_THAN_OR_EQUAL_TO:
			return "Greater Than or Equal To";
		case A_EQUAL_TO:
			return "Equal To";
		case A_NOT_EQUAL_TO:
			return "Not Equal To";
			
		case A_ADDED_TO:
			return "Added To";
		case A_SUBTRACTED_FROM:
			return "Subtracted From";
		case A_TIMES:
			return "Times";
		case A_DIVIDED_BY:
			return "Divided By";
		case A_IS_NOT_NULL:
			return "Is Not Null";
		case A_IS_NULL:
			return "Is Null";
		case A_LIKE:
			return "Like";
		case A_NOT_EMPTY:
			return "A_NOT_EMPTY";
		default:
			return "";
		}
	}
	
	public String convertStatementOperatorTofunctionOperator(String op) {
		if (op == null) return "";

		if (op.trim().equals("LESS_THAN")) {
			return "Less Than";
		} else if (op.trim().equals("LESS_THAN_OR_EQUAL_TO")){
			return "Less Than or Equal To";
		}  else if (op.trim().equals("EQUAL_TO")){
			return "Equal To";
		} else if (op.trim().equals("NOT_EQUAL_TO")){
			return "Not Equal To";
		} else if (op.trim().equals("GREATER_THAN_OR_EQUAL_TO")){
			return "Greater Than or Equal To";
		} else if (op.trim().equals("GREATER_THAN")){
			return "Greater Than";
		} else {
			return "";
		}
	}

	public String conditionalToString(Conditional.Operator op) {
		switch (op) {
		case AND:
			return "AND";
		case OR:
			return "OR";
		default:
			return "";
		}
	}

}
