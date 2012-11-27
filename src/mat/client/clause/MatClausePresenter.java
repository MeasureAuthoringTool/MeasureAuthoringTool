package mat.client.clause;

import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.MeasureComposerPresenter;
import mat.client.clause.event.QDSElementCreatedEvent;
import mat.client.clause.event.SaveEvent;
import mat.client.clause.event.SaveEventHandler;
import mat.client.shared.MatContext;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
	
public class MatClausePresenter implements MatPresenter {
	private SimplePanel emptyWidget = new SimplePanel();
	private SimplePanel panel = new SimplePanel();
	private HandlerManager eventBus = MatContext.get().getEventBus();
	private ClauseServiceAsync rpcService = MatContext.get().getClauseService();
	private AppController appController = new AppController(rpcService,eventBus);
	
	
	public MatClausePresenter() {
		emptyWidget.add(new Label("No Measure Selected"));
		panel.setStyleName("contentPanel");
		eventBus.addHandler(QDSElementCreatedEvent.TYPE, new QDSElementCreatedEvent.Handler() {
			@Override
			public void onCreation(QDSElementCreatedEvent event) {
				appController.loadQDSElements(false);
			}
		});
		
		eventBus.addHandler(SaveEvent.TYPE,
				new SaveEventHandler() {
			@Override
			public void onSave(SaveEvent event) {
				appController.save();
			}
		});
	}
	@Override
	public void beforeDisplay() {
		if(MatContext.get().getCurrentMeasureId() != null && !MatContext.get().getCurrentMeasureId().equals("")) {
			panel.clear();
			appController.open(MatContext.get().getCurrentMeasureId(),panel);
		}
		else {
			Mat.hideLoadingMessage();
			displayEmpty();
		}
		MeasureComposerPresenter.setSubSkipEmbeddedLink("ClauseWorkspace");
		Mat.focusSkipLists("MeasureComposer");
	}
	
	@Override 
	public void beforeClosingDisplay() {
		if (appController.isLoaded()) {
			appController.saveMainPhrases();
		}else{
			MatContext.get().getSynchronizationDelegate().setSavingClauses(false);
		}
		appController.setSavingStatus();
		
	}
	@Override
	public Widget getWidget() {
		return panel;
	}
	
	private void displayEmpty(){
		panel.clear();
		panel.add(emptyWidget);
	}
	
	public AppController getAppController() {
		return appController;
	}
	public void setAppController(AppController appController) {
		this.appController = appController;
	}
	
}
