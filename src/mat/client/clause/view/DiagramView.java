package mat.client.clause.view;

import java.util.List;

import mat.client.clause.AppController;
import mat.client.clause.diagram.Diagram;
import mat.client.diagramObject.DiagramObject;
import mat.client.diagramObject.Phrase;
import mat.client.shared.ErrorMessageDisplay;

import org.vaadin.gwtgraphics.client.DrawingArea;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public interface DiagramView<T> {
	public interface Presenter<T> {
		void addMeasurePhraseDiagramObject(DiagramObject diagramObject);
		void selectCriterion(String criterion);
		DiagramObject getMeasurePhrase(String itemText);
		DiagramObject getSavedMeasurePhrase(String itemText);
		void updateMPMap(String itemText, DiagramObject diagObject);
		void updateSavedMeasurePhrase(String itemText,DiagramObject diagObject);
		void save();
		
	}		
			
	public void setPresenter(Presenter<T> presenter);
	Widget asWidget();
	public void showPropertyEditor();		
	public void hidePropertyEditor();
	public void showAttributeEditor(Phrase phrase);
	public void hideAttributeEditor();
	public void showSavedMessage(boolean success);
	public DrawingArea getCanvas();	
	public String getCurrentCriterion();
	public void showMeasurePhrases(List<String> measurePhraseList);
	public void measurePhrases(List<String> measurePhraseList);
	public void selectMeasurePhrase(String name);
	public boolean isMeasurePhrase(String name) ;
	public DiagramObject getMeasurePhrase(String identity);
	public DiagramObject getSavedMeasurePhrase(String identity);
	public void drawMeasurePhrase(DiagramObject diagramObject);
	public void drawDiagram();	
	public void drawDiagramObject() ;
	public void calcExtent(int right, int bottom);
	public Diagram getDiagram();
	public void hilightDiagramObject(DiagramObject diagramObject, ClickEvent event);
	public HorizontalPanel getPropertyEditor();
	public AppController getAppController();
	public void setEditable(boolean b);
	public boolean criterionHasOnlyOneChild();
	public boolean isEditable();
	void refreshMeasurePhrases(List<String> measurePhraseList);
	public void moveFocusToPropertyEditor();
	public void firePropertyEditorAlert();
	public ErrorMessageDisplay getPropEditErrorMessages();
	/**
	 * api to control phrase and clause library selection and handlers
	 */
	public void disableLibraries(String source);
	public void enableLibraries(String source);
}
	