package mat.client.diagramObject;

import mat.client.clause.AppController;

public class DiagramObjectFactory {
	public static DiagramObject clone(AppController appController, DiagramObject src) {
		DiagramObject clone;
		
		if (src instanceof Conditional)
			clone = Conditional.clone((Conditional)src);
		else if (src instanceof Criterion)
			clone = Criterion.clone((Criterion)src);
		else if (src instanceof Qdsel)
			clone = Qdsel.clone((Qdsel)src);
		else if (src instanceof SimpleStatement)
			clone = new SimpleStatement(appController).clone((SimpleStatement)src);
		else if (src instanceof Rel)
			clone = Rel.clone((Rel)src);
		else if (src instanceof PlaceHolder)
			clone =  ((PlaceHolder)src).clone();
		else if (src instanceof InsertPhrase)
			clone = InsertPhrase.clone((InsertPhrase)src);	
		else
			throw new IllegalArgumentException("DiagramObject cannot clone object of class " + src.getClass().getName());	
		
		return clone;
	}
}
