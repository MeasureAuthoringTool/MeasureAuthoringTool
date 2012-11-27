package org.ifmc.mat.simplexml.model;

import java.util.List;


public class MinMax extends FunctionHolder {
	private String funcName;
	private Qdsel qdsel;
	private And and;
	private Or or;
	private String property;
	private String ttext;

	public void diagram(PrettyPrinter pp) {
		pp.concat(getFuncName());
		if (getProperty() != null)
			pp.concat("PROPERTY", getProperty());
		pp.incrementIndentation();
		if (getQdsel() != null)
			getQdsel().diagram(pp);
		pp.decrementIndentation();
	}
	
	public MinMax(String funcName, Qdsel qdsel, String property) {
		setFuncName(funcName);
		setQdsel(qdsel);
		if (property != null && property.length() > 0)
			setProperty(property);
	}
	
	public String getFuncName() {
		return funcName;
	}

	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}	
	public Qdsel getQdsel() {
		return qdsel;
	}
	public void setQdsel(Qdsel qdsel) {
			if (qdsel.getIdAttr() != null) {

				if (qdsel.getId() != null) {
					qdsel.setId(null);
				}

				this.qdsel= qdsel;

			}
			else {
				Id id = qdsel.getId();
				if (id == null)
					throw new IllegalArgumentException("MinMax.setQdsel: No ID");
				if (id.getAnd() != null) {
					setAnd(id.getAnd());
				} 
				if (id.getOr() != null) {
					setOr(id.getOr());
				}
				List<Qdsel> idQdsel = id.getQdsel();
				if (idQdsel != null && idQdsel.size() == 1) {
					this.qdsel = idQdsel.get(0);
				
					if (this.qdsel.getId() != null) {
						Id deeperId = this.qdsel.getId();

						if (deeperId.getAnd() != null) {
							And and = deeperId.getAnd();
							transferAttributes(this.qdsel, and);
							transferFunctions(id);
							setAnd(and);
							
						}

						if (deeperId.getOr() != null) {
							Or or = deeperId.getOr();
							transferAttributes(this.qdsel, or);
							transferFunctions(id);
							setOr(or);
						}
						//Fields transfer algorithm.
						//this qdsel is not required since the data is really inside 
						//child id of this qdsel.
						//This piece of code may need more enhancements to transfer 
						//additional such fields- Vasant.
						this.qdsel = null;
					}
				}
				transferFunctions(id);
			}
	}

	private void transferAttributes(Qdsel qdsel, LogicOp op) {
		//need to determine who wins if both qdsel anf op have inclusive, num and unit attributes
		boolean doTransfer = 
			(qdsel.getHighinclusive() != null && qdsel.getHighnum() != null && qdsel.getHighunit() != null) || 
			(qdsel.getLowinclusive() != null && qdsel.getLownum() != null && qdsel.getLowunit() != null);
		if(doTransfer){
			op.setHighinclusive(qdsel.getHighinclusive());
			op.setHighnum(qdsel.getHighnum());
			op.setHighunit(qdsel.getHighunit());
	
			op.setLowinclusive(qdsel.getLowinclusive());
			op.setLownum(qdsel.getLownum());
			op.setLowunit(qdsel.getLowunit());
		}
	}

	public And getAnd() {
		return and;
	}
	public void setAnd(And and) {
		this.and = and;
	}
	public Or getOr() {
		return or;
	}
	public void setOr(Or or) {
		this.or = or;
	}
	public String getTtext() {
		return ttext;
	}
	public void setTtext(String ttext) {
		this.ttext = ttext;
	}
}
