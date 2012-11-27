package org.ifmc.mat.client.clause.presenter;

import org.ifmc.mat.client.clause.AppController;
import org.ifmc.mat.client.clause.diagram.Diagram;
import org.ifmc.mat.client.clause.event.SaveEvent;
import org.ifmc.mat.client.clause.view.DiagramView;
import org.ifmc.mat.client.diagramObject.DiagramObject;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;

public class DiagramPresenter implements Presenter, DiagramView.Presenter<Diagram> { 
	private final AppController appController;
	private final HandlerManager eventBus;
	private final DiagramView<Diagram> view;

	public DiagramPresenter(AppController appController, 
			HandlerManager eventBus, DiagramView<Diagram> view) {
		this.appController = appController;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
		view.refreshMeasurePhrases(appController.getMeasurePhraseList());
	}

	public void onSaveButtonClicked() {
		eventBus.fireEvent(new SaveEvent());
	}

	public void go(final HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}

	@Override
	public DiagramObject getMeasurePhrase(String identity) {
		return appController.getMeasurePhrase(identity);
	}
	
	@Override
	public void addMeasurePhraseDiagramObject(DiagramObject diagramObject) {
		appController.addMeasurePhrase(diagramObject);
	}

	@Override
	public void selectCriterion(String criterion) {
		view.showMeasurePhrases(appController.getMeasurePhraseList());
	}

	@Override
	public void save() {
		appController.save();
	}

	@Override
	public DiagramObject getSavedMeasurePhrase(String itemText) {
		return appController.getSavedMeasurePhrase(itemText);
	}

	@Override
	public void updateSavedMeasurePhrase(String itemText,DiagramObject diagObj) {
	      appController.updateSavedMeasurePhrase(itemText, diagObj);
	}

	@Override
	public void updateMPMap(String itemText, DiagramObject diagObject) {
		appController.updateMeasurePhraseMap(itemText, diagObject);
		
	}

	
	
}