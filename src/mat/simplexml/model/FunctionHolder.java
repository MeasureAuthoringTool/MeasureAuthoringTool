package mat.simplexml.model;

import java.util.ArrayList;
import java.util.List;

public class FunctionHolder {
	protected List<First> listOfFirst;
	protected List<Second> listOfSecond;
	protected List<Third> listOfThird;
	protected List<Last> listOfLast;
	protected List<Count> listOfCount;
	protected List<CountUniqueByDate>listOfCountUniqueByDate;
	protected List<Max> listOfMax;
	protected List<Min> listOfMin;
	protected List<Not> listOfNot;
	protected List<Function> listOfFunctions;

	public void diagramFunctions(PrettyPrinter pp) {
		if (getCount() != null)
			for (Count count : getCount())
				count.diagram(pp);
		if (getCountUniqueByDate() != null)
			for (CountUniqueByDate count : getCountUniqueByDate())
				count.diagram(pp);		
		if (getMax() != null)
			for (Max max : getMax())
				max.diagram(pp);
		if (getMin() != null)
			for (Min min : getMin())
				min.diagram(pp);
		if (getFirst() != null)
			for (First first : getFirst())
				first.diagram(pp);
		if (getSecond() != null)
			for (Second second : getSecond())
				second.diagram(pp);		
		if (getThird() != null)
			for (Third third : getThird())
				third.diagram(pp);				
		if (getLast() != null)
			for (Last last : getLast())
				last.diagram(pp);	
		if (getNot() != null)
			for (Not not : getNot())
				not.diagram(pp);		
	}
	protected void transferFunctions(FunctionHolder funcHolder) {

		if(funcHolder.listOfCount != null) {
			this.listOfCount = new ArrayList<Count>();
			this.listOfCount.addAll(funcHolder.listOfCount);
		}
		if(funcHolder.listOfFirst != null) {
			this.listOfFirst = new ArrayList<First>();
			this.listOfFirst.addAll(funcHolder.listOfFirst);
		}
		if(funcHolder.listOfSecond != null) {
			this.listOfSecond = new ArrayList<Second>();
			this.listOfSecond.addAll(funcHolder.listOfSecond);
		}
		if(funcHolder.listOfThird != null) {
			this.listOfThird = new ArrayList<Third>();
			this.listOfThird.addAll(funcHolder.listOfThird);
		}
		if(funcHolder.listOfLast != null) {
			this.listOfLast = new ArrayList<Last>();
			this.listOfLast.addAll(funcHolder.listOfLast);
		}

		if(funcHolder.listOfMin != null) {
			this.listOfMin = new ArrayList<Min>();
			this.listOfMin.addAll(funcHolder.listOfMin);
		}
		if(funcHolder.listOfMax != null) {
			this.listOfMax = new ArrayList<Max>();
			this.listOfMax.addAll(funcHolder.listOfMax);
		}

		if(funcHolder.listOfFunctions != null) {
			this.listOfFunctions = new ArrayList<Function>();
			this.listOfFunctions.addAll(funcHolder.listOfFunctions);
		}

		if(funcHolder.listOfNot != null) {
			this.listOfNot = new ArrayList<Not>();
			this.listOfNot.addAll(funcHolder.listOfNot);
		}
	}

	public List<First> getFirst() {
		return listOfFirst;
	}
	public void addFirst(First first) {
		if (this.listOfFirst == null)
			this.listOfFirst = new ArrayList<First>();
		this.listOfFirst.add(first);
	}
	public List<Second> getSecond() {	
		return listOfSecond;
	}
	public void addSecond(Second second) {
		if (this.listOfSecond == null)
			this.listOfSecond = new ArrayList<Second>();
		this.listOfSecond.add(second);
	}	
	public List<Third> getThird() {		
		return listOfThird;
	}
	public void addThird(Third third) {
		if (this.listOfThird == null)
			this.listOfThird = new ArrayList<Third>();
		this.listOfThird.add(third);
	}		
	public List<Last> getLast() {		
		return listOfLast;
	}
	public void addLast(Last last) {
		if (this.listOfLast == null)
			this.listOfLast = new ArrayList<Last>();
		this.listOfLast.add(last);
	}			
	public List<Count> getCount() {	
		return listOfCount;
	}
	public void addCount(Count count) {
		if (this.listOfCount == null)
			this.listOfCount = new ArrayList<Count>();
		this.listOfCount.add(count);
	}				
	
	public List<Function> getFunctions() {	
		return listOfFunctions;
	}
	public void addFunction(Function func) {
		if (this.listOfFunctions == null)
			this.listOfFunctions = new ArrayList<Function>();
		this.listOfFunctions.add(func);
	}
	public List<CountUniqueByDate> getCountUniqueByDate() {
		return listOfCountUniqueByDate;
	}

	public void addCountUniqueByDate(CountUniqueByDate countUniqueByDate) {
		if (this.listOfCountUniqueByDate == null)
			this.listOfCountUniqueByDate = new ArrayList<CountUniqueByDate>();
		this.listOfCountUniqueByDate.add(countUniqueByDate);
	}	
	
	public List<Max> getMax() {			
		return listOfMax;
	}
	public void addMax(Max max) {
		if (this.listOfMax == null)
			this.listOfMax = new ArrayList<Max>();
		this.listOfMax.add(max);
	}					
	public List<Min> getMin() {
		return listOfMin;
	}
	public void addMin(Min min) {
		if (this.listOfMin == null)
			this.listOfMin = new ArrayList<Min>();
		this.listOfMin.add(min);
	}						
	public List<Not> getNot() {
		return listOfNot;
	}
	public void addNot(Not not) {
		if (this.listOfNot == null)
			this.listOfNot = new ArrayList<Not>();
		this.listOfNot.add(not);
	}				
}
