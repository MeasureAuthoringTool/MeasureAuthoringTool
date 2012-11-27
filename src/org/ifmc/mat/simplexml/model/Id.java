package org.ifmc.mat.simplexml.model;

import java.util.ArrayList;
import java.util.List;

public class Id extends FunctionHolder {
	private To to;
	private String toAttr;
	private String id;
	private String highnum;
	private And and;
	private Or or;
	private String ttext;
	private String highunit;
	private String highinclusive;
	private String rel;
	private List<Qdsel> qdsel;

	public void diagram(PrettyPrinter pp) {
		pp.concat("ID");
		pp.incrementIndentation();
		if (getId() != null)
			pp.concat("ID", getId());
		if (getAnd() != null)
			getAnd().diagram(pp);
		if (getOr() != null)
			getOr().diagram(pp);
		diagramFunctions(pp);
		if (getRel() != null)
			pp.concat("REL", getRel());
		int saveIndentation = pp.getIndentation();
		List<Qdsel>qdselList = this.getQdsel();
		if (qdselList != null)
			for (Qdsel qdsel : qdselList) {
				qdsel.diagram(pp);
				pp.setIndentation(saveIndentation);
			}		
		if (getTo() != null)
			getTo().diagram(pp);
		if (getToAttr() != null)
			pp.concat("TO", getToAttr());
		if (getHighnum() != null)
			pp.concat("HIGHNUM", getHighnum());
		if (getHighunit() != null)
			pp.concat("HIGHUNIT", getHighunit());
		if (getHighinclusive() != null)
			pp.concat("HIGHINCLUSIVE", getHighinclusive());
		if (getTtext() != null)
			pp.concat("TTEXT", getTtext());
		pp.decrementIndentation();
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
	public void setToAttr (String to ) {
		this.toAttr = to;
	}
	public String getId() {
		return id;
	}
	public void setId (String id ) {
		this.id = id;
	}
	public String getHighnum() {
		return highnum;
	}
	public void setHighnum (String highnum ) {
		this.highnum = highnum;
	}
	public And getAnd() {
		return and;
	}
	public void setLogicOp(LogicOp logicOp) {
		if (logicOp instanceof And)
			setAnd((And)logicOp);
		else
			setOr((Or)logicOp);
	}	
	public void setAnd(And and) {
		this.and = and;
	}	
	public Or getOr() {
		return or;
	}
	public void setOr (Or or ) {
		this.or = or;
	}
	public LogicOp getLogicOp() {
		if (and != null)
			return and;
		else
			if (or != null)
				return or;
			else
				return null;
	}
	public String getTtext() {
		return ttext;
	}
	public void setTtext (String ttext ) {
		this.ttext = ttext;
	}
	public String getHighunit() {
		return highunit;
	}
	public void setHighunit (String highunit ) {
		this.highunit = highunit;
	}
	public String getHighinclusive() {
		return highinclusive;
	}
	public void setHighinclusive (String highinclusive ) {
		this.highinclusive = highinclusive;
	}
	public String getRel() {
		return rel;
	}
	public void setRel (String rel ) {
		this.rel = rel;
	}
	public List<Qdsel> getQdsel() {
		return qdsel;
	}
	public void setQdsel(List<Qdsel> qdsel) {
		this.qdsel = qdsel;
	}
	public void addQdsel(Qdsel q) {
		if (qdsel == null)
			qdsel = new ArrayList<Qdsel>();
		qdsel.add(q);
	}
}