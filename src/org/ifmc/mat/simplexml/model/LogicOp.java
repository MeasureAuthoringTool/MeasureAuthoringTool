package org.ifmc.mat.simplexml.model;

import java.util.ArrayList;
import java.util.List;

import org.ifmc.mat.shared.model.Conditional.Operator;
import org.ifmc.mat.shared.model.MeasurementTerm;
import org.ifmc.mat.shared.model.StatementTerm;

public class LogicOp extends FunctionHolder implements IPhrase{
	private List<Qdsel> qdsel;
	private Measureel measureel;
	private List<And> and;
	private List<Or> or;
	private String ttext;
	private To to;
	private String toAttr;
	private String highnum;
	private String highunit;
	private String highinclusive;
	private String lownum;
	private String lowunit;
	private String lowinclusive;	
	private String rel;
	private String property;

	public String getAritnum() {
		return aritnum;
	}

	public void setAritnum(String aritnum) {
		this.aritnum = aritnum;
	}

	public String getAritunit() {
		return aritunit;
	}

	public void setAritunit(String aritunit) {
		this.aritunit = aritunit;
	}

	private String aritnum;
	private String aritunit;
	
	private String aritop;

	public String getAritop() {
		return aritop;
	}

	public void setAritop(String aritop) {
		this.aritop = aritop;
	}

	public void diagram(PrettyPrinter pp) {
		pp.concat(this.getClass().getSimpleName().toUpperCase());
		pp.incrementIndentation();
		if (getRel() != null)
			pp.concat("REL", getRel());
		
		if (getQdsel() != null) {
			int saveIndentation = pp.getIndentation();
			for (Qdsel qdsel : getQdsel()) {
				qdsel.diagram(pp);
				pp.setIndentation(saveIndentation);
			}
		}
		
		if (getTo() != null)
			getTo().diagram(pp);
		if (getToAttr() != null)
			pp.concat("TOATTR", getToAttr());
		
		diagramFunctions(pp);
		
		if (getAnd() != null) {
			int saveIndentation = pp.getIndentation();
			for (And and : getAnd()) {
				and.diagram(pp);
				pp.setIndentation(saveIndentation);
			}
		}
		
		if (getOr() != null) {
			int saveIndentation = pp.getIndentation();
			for (Or or : getOr()) {
				or.diagram(pp);
				pp.setIndentation(saveIndentation);
			}
		}
	
		if (getProperty() != null)
			pp.concat("PROPERTY", getProperty());
		
		if (getMeasureel() != null)
			getMeasureel().diagram(pp);
		if (getHighnum() != null) {
			pp.concat("HIGHNUM", getHighnum());
			pp.concat("HIGHUNIT", getHighunit());
		}
		if (getHighinclusive() != null) {
			pp.concat("HIGHINCLUSIVE", getHighinclusive());
			pp.concat("HIGHUNIT", getHighunit());
		}
		if (getLownum() != null) {
			pp.concat("LOWNUM", getLownum());
			pp.concat("LOWUNIT", getLowunit());
		}
		if (getLowinclusive() != null) {
			pp.concat("LOWINCLUSIVE", getLowinclusive());
			pp.concat("LOWUNIT", getLowunit());
		}
		if (getTtext() != null)
			pp.concat("TTEXT", getTtext());		
		pp.decrementIndentation();
	}	
	
	public static LogicOp logicOpFactory(Operator operator) {
		if (operator == Operator.AND)
			return new And();
		else if (operator == Operator.OR)
			return new Or();
		else
			return null;
	}
	
	public List<Qdsel> getQdsel() {
		return qdsel;
	}
	public void setQdsel (List<Qdsel> qdsel ) {
		this.qdsel = qdsel;
	}
	private void setQdsel(Qdsel qdsel) {
		if (qdsel.getIdAttr() != null) {

			if (qdsel.getId() != null) {
				qdsel.setId(null);
			}

			And and = new And();
			and.addQdsel(qdsel);
			addAnd(and);
		}
		else {
		Id id = qdsel.getId();
		if (id == null)
			throw new IllegalArgumentException("Count.setQdsel: No ID");
		if (id.getAnd() != null) {
			addAnd(id.getAnd());
		}
		
		if (id.getOr() != null) {
				addOr(id.getOr());
		}

		List<Qdsel> idQdsel = id.getQdsel();
		if (idQdsel != null && idQdsel.size() == 1) {
			Qdsel tempQdsel = idQdsel.get(0);
		
			if (tempQdsel.getId() != null) {
				Id deeperId = tempQdsel.getId();

				if (deeperId.getAnd() != null) {
					And and = deeperId.getAnd();
					transferAttributes(tempQdsel, and);
					addAnd(and);
					
				}

				if (deeperId.getOr() != null) {
					Or or = deeperId.getOr();
					transferAttributes(tempQdsel, or);
					addOr(or);
				}
				//Fields transfer algorythm.
				//this qdsel is not required since the data is really inside 
				//child id of this qdsel.
				//This piece of code may need more enhancements to transfer 
				//additional such fields- Vasant.
				this.qdsel = null;
			}
		}
		And and = new And();
		transferRelTo(and, qdsel);
		transferAttributes(qdsel, and);
		addAnd(and);
		and.transferFunctions(id);
		}
	}

	private void transferRelTo(LogicOp lop, Qdsel qdsel){
		lop.setTo(qdsel.getTo());
		lop.setToAttr(qdsel.getToAttr());
		lop.setRel(qdsel.getRel());
	}
	
	public void transferAttributes(Qdsel qdsel, LogicOp op) {
		op.setHighinclusive(qdsel.getHighinclusive());
		op.setHighnum(qdsel.getHighnum());
		if(qdsel.getHighunit()!= null && !qdsel.getHighunit().trim().equals("")){
			op.setHighunit(qdsel.getHighunit());
			op.setLowunit(qdsel.getLowunit());
		}
		op.setLowinclusive(qdsel.getLowinclusive());
		op.setLownum(qdsel.getLownum());
		
		
	}
	public void addQdsel (Qdsel qdsel) {
		if (getQdsel() == null) {
			setQdsel(new ArrayList<Qdsel>());
		}

		if (qdsel.getIdAttr() != null) {
			if (qdsel.getId() != null) {
				qdsel.setId(null);
			}
			this.qdsel.add(qdsel);
		} else {
			setQdsel(qdsel);
		}
	}
	public Measureel getMeasureel() {
		return measureel;
	}
	public void setMeasureel (Measureel measureel ) {
		this.measureel = measureel;
	}
	public String getTtext() {
		return ttext;
	}
	public void setTtext (String ttext ) {
		this.ttext = ttext;
	}
	public List<And> getAnd() {
		return and;
	}
	public void addAnd(And and) {
		if (this.and == null)
			this.and = new ArrayList<And>();
		this.and.add((And)and);
	}
	public List<Or> getOr() {
		return or;
	}
	public void addOr(Or or) {
		if (this.or == null)
			this.or = new ArrayList<Or>();
		this.or.add(or);
	}
	public void addLogicOp(LogicOp logicOp) {
		if (logicOp instanceof And)
			addAnd((And)logicOp);
		else
			addOr((Or)logicOp);
	}
	public To getTo() {
		return to;
	}
	public void setTo(To to) {
		this.to = to;
	}	
	public String getToAttr() {
		return toAttr;
	}
	public void setToAttr(String toAttr) {
		this.toAttr = toAttr;
	}
	public String getHighnum() {
		return highnum;
	}
	public void setHighnum(String highnum) {
		this.highnum = highnum;
	}
	public String getHighunit() {
		return highunit;
	}
	public void setHighunit(String highunit) {
		this.highunit = highunit;
	}
	public String getHighinclusive() {
		return highinclusive;
	}
	public void setHighinclusive(String highinclusive) {
		this.highinclusive = highinclusive;
	}
	public String getLownum() {
		return lownum;
	}
	public void setLownum(String lownum) {
		this.lownum = lownum;
	}
	public String getLowunit() {
		return lowunit;
	}
	public void setLowunit(String lowunit) {
		this.lowunit = lowunit;
	}
	public String getLowinclusive() {
		return lowinclusive;
	}
	public void setLowinclusive(String lowinclusive) {
		this.lowinclusive = lowinclusive;
	}	
	public String getRel() {
		return rel;		
	}
	public void setRel(String rel) {
		this.rel = rel;
	}
	public void setValueAttr(MeasurementTerm measurementTerm, StatementTerm.Operator operator) {
		switch (operator) {
			case LESS_THAN:	
				setHighnum(measurementTerm.getQuantity());
				setHighinclusive("false");
				if(isUnitNotEmpty(measurementTerm)){
					setHighunit(measurementTerm.getUnit());
				}
				break;
			case LESS_THAN_OR_EQUAL_TO:
				setHighnum(measurementTerm.getQuantity());
				setHighinclusive("true");
				if(isUnitNotEmpty(measurementTerm)){
					setHighunit(measurementTerm.getUnit());
				}
				break;					
			case GREATER_THAN:
				setLownum(measurementTerm.getQuantity());
				setLowinclusive("false");
				if(isUnitNotEmpty(measurementTerm)){
					setLowunit(measurementTerm.getUnit());
				}
				break;
			case GREATER_THAN_OR_EQUAL_TO:
				setLownum(measurementTerm.getQuantity());
				setLowinclusive("true");
				if(isUnitNotEmpty(measurementTerm)){
					setLowunit(measurementTerm.getUnit());
				}
				break;
			case EQUAL_TO:
				setHighnum(measurementTerm.getQuantity());
				setHighinclusive("true");
				if(isUnitNotEmpty(measurementTerm)){
					setHighunit(measurementTerm.getUnit());	
					setLowunit(measurementTerm.getUnit());
				}
				setLownum(measurementTerm.getQuantity());
				setLowinclusive("true");
						
				break;
			case NOT_EQUAL_TO:
				setHighnum(measurementTerm.getQuantity());
				setHighinclusive("false");
				if(isUnitNotEmpty(measurementTerm)){
					setHighunit(measurementTerm.getUnit());		
					setLowunit(measurementTerm.getUnit());	
				}
				setLownum(measurementTerm.getQuantity());
				setLowinclusive("false");
				break;
			case PLUS:
				setAritnum(measurementTerm.getQuantity());
				if(isUnitNotEmpty(measurementTerm)){
					setAritunit(measurementTerm.getUnit());
				}
				setAritop(PLUS_OP);
				break;
			case MINUS:
				setAritnum(measurementTerm.getQuantity());
				if(isUnitNotEmpty(measurementTerm)){
					setAritunit(measurementTerm.getUnit());
				}
				setAritop(MINUS_OP);
				break;
			case TIMES:
				setAritnum(measurementTerm.getQuantity());
				if(isUnitNotEmpty(measurementTerm)){
					setAritunit(measurementTerm.getUnit());
				}
				setAritop(MULTIPLY_OP);
				break;
			case DIVIDE_BY:
				setAritnum(measurementTerm.getQuantity());
				if(isUnitNotEmpty(measurementTerm)){
					setAritunit(measurementTerm.getUnit());
				}
				setAritop(DIVIDE_OP);
				break;
		}
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getProperty() {
		return property;
	}
	
	private boolean isUnitNotEmpty(MeasurementTerm mTerm){
	     return mTerm.getUnit()!= null && !mTerm.getUnit().trim().equals("");
	}
}
