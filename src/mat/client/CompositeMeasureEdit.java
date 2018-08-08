package mat.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;

import mat.client.measure.ManageCompositeMeasureDetailView;
import mat.client.shared.ContentWithHeadingWidget;

public class CompositeMeasureEdit implements MatPresenter{
	
	private ContentWithHeadingWidget panel = new ContentWithHeadingWidget();
	ManageCompositeMeasureDetailView compositeMeasureDetailView = new ManageCompositeMeasureDetailView();
	
	public CompositeMeasureEdit() {
		buildScreen();
	}
	
	private void buildScreen() {
		panel.getButtonPanel().clear(); 
		
		panel.setHeading("My Measures > Edit Measure", "MeasureLibrary");
		compositeMeasureDetailView.showMeasureName(false);
		compositeMeasureDetailView.showCautionMsg(true);
		compositeMeasureDetailView.getName().setValue("name of measure");
		compositeMeasureDetailView.getShortName().setValue("abbrv name of measure");
		compositeMeasureDetailView.getMeasScoringChoice().setValueMetadata("Cohort");
		
		// set the patient based indicators, yes is index 1, no is index 0
		if(false) {//isPatientBased) {
			compositeMeasureDetailView.getPatientBasedInput().setSelectedIndex(1);
		} else {
			compositeMeasureDetailView.getPatientBasedInput().setSelectedIndex(0);
		}
		
		if(false){//scoring.equalsIgnoreCase(MatConstants.CONTINUOUS_VARIABLE)) {
			compositeMeasureDetailView.getPatientBasedInput().removeItem(1);
		}
		
		panel.setContent(compositeMeasureDetailView.asWidget());
	
	
		compositeMeasureDetailView.getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO in story 9303
				
			}
			
			
		});
		
		compositeMeasureDetailView.getCancelButton().addClickHandler(new ClickHandler() {
		
			@Override
			public void onClick(ClickEvent event) {
				// TODO in story 9303
				
			}
			
		});
	}
	

	@Override
	public void beforeClosingDisplay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeDisplay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Widget getWidget() {
		return panel;
	}
	

}