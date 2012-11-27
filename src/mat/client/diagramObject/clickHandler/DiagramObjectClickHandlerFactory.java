package mat.client.diagramObject.clickHandler;

import mat.client.clause.view.DiagramView;
import mat.client.diagramObject.DiagramObject;

import org.vaadin.gwtgraphics.client.shape.Rectangle;

import com.google.gwt.event.dom.client.ClickHandler;

public class DiagramObjectClickHandlerFactory {
	public static ClickHandler getClickHandlerFor(Rectangle rectangle, DiagramView<?> view, DiagramObject diagramObject) {
		if (view.getCurrentCriterion().equals("Measure Phrase"))
			return null;
		if (diagramObject.getClass().getName().endsWith("Qdsel"))
			return new QdselClickHandler(view, diagramObject).getClickHandler();
		else 
			if (diagramObject.getClass().getName().endsWith("Criterion"))
				return new CriterionClickHandler(view, diagramObject).getClickHandler();
			else
				if (diagramObject.getClass().getName().endsWith("Conditional"))
					return new ConditionalClickHandler(view, diagramObject).getClickHandler();
				else
					if (diagramObject.getClass().getName().endsWith("Rel"))
						return new RelClickHandler(view, diagramObject).getClickHandler();	
					else if (diagramObject.getClass().getName().endsWith("PlaceHolder"))
						return new PlaceholderClickHandler(view, diagramObject).getClickHandler();
					else
						return null;
	}
}
