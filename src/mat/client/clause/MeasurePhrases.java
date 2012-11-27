package mat.client.clause;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mat.client.diagramObject.DiagramObject;
import mat.client.diagramObject.DiagramObjectFactory;
import mat.client.shared.MatContext;

public class MeasurePhrases {
	protected AppController appController;
	protected Map<String, DiagramObject>diagramObjects;
	protected Map<String, DiagramObject>savedDiagramObjects;
	
	
	public MeasurePhrases(AppController appController) {
		this.appController = appController;
		this.diagramObjects = new HashMap<String, DiagramObject>();
		this.savedDiagramObjects = new HashMap<String, DiagramObject>();
	}

	public void put(String name, DiagramObject src) {
		diagramObjects.put(name, src);
	}
	
	public void insert(String name, DiagramObject src) {
		DiagramObject clone = DiagramObjectFactory.clone(appController, src);
		name = name.trim();
		
		if (diagramObjects.containsKey(name)) {
			appController.getDiagramView().clearMessages();
			appController.getDiagramView().getPropEditErrorMessages().setMessage(MatContext.get().getMessageDelegate().getMeasurePhraseAlreadyExistsMessage(name));
		}
		else
			diagramObjects.put(name, clone);
	}


	public void insertSavedItem(String name, DiagramObject src){
		DiagramObject clone = DiagramObjectFactory.clone(appController, src);
		name = name.trim();
		savedDiagramObjects.put(name, clone);
	}
	
	public void remove(String name) {
		diagramObjects.remove(name);
	}
	
	public void removeFromSavedList(String name){
		savedDiagramObjects.remove(name);
	}
	
	public void addToSavedList(String name, DiagramObject dobj){
		removeFromSavedList(name);
		if(dobj.getChangedName()!= null){
			dobj.setIdentity(dobj.getChangedName());
		    insertSavedItem(dobj.getChangedName(), dobj);
		}
	}
	
	public void updateMPMap(String name, DiagramObject dobj){
		remove(name);
		if(dobj.getChangedName()!= null){
			dobj.setIdentity(dobj.getChangedName());
		    insert(dobj.getChangedName(), dobj);
		}
	}
	
	public DiagramObject getItem(String name) {
		return diagramObjects.get(name);
	}

	public DiagramObject getSavedItem(String name){
		return savedDiagramObjects.get(name);
	}
	
	public boolean isMeasurePhrase(String identity) {
		return diagramObjects.get(identity) != null;
	}
	
	public List<String> getList() {
		List<String> list = new ArrayList<String>();
		List<String> diagramObjectList = new ArrayList<String>();
		for (String key : diagramObjects.keySet()){
			   DiagramObject dobo = diagramObjects.get(key);
			   diagramObjectList.add(dobo.getIdentity());
		}
		Collections.sort(diagramObjectList);
		list.addAll(diagramObjectList);
		return list;
	}
	
	public MeasurePhrases clone() {
		MeasurePhrases mp2 = new MeasurePhrases(appController);
		for (String key : diagramObjects.keySet())
			mp2.put(key, diagramObjects.get(key));
		return mp2;
	}
	
	public void reset() {
		this.diagramObjects.clear();
	}
	
	public void resetSavedDiagramObjects(){
		this.savedDiagramObjects.clear();
	}
	
}
