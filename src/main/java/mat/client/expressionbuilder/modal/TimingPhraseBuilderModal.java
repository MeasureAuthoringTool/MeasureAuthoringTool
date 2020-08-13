package mat.client.expressionbuilder.modal;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.expressionbuilder.component.TimingPhraseWidget;
import mat.client.expressionbuilder.constant.TimingOperator;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.TimingOperatorModel;
import mat.client.expressionbuilder.util.TimingGraph;
import mat.client.expressionbuilder.util.TimingGraphUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TimingPhraseBuilderModal extends SubExpressionBuilderModal {

	private static final String SELECT_THE_NEXT_OPTION_FOR_THE_TIMING = "-- Select the next option for the timing. --";
	private static final String CONTINUE_TO_BUILD_YOUR_TIMING = "Continue to build your timing.";
	private static final String SELECT_THE_START_OF_THE_TIMING_PHRASE = "-- Select the start of the timing phrase --";
	private static final String HOW_WOULD_YOU_LIKE_TO_START_YOUR_TIMING_PHRASE = "How would you like to start your timing phrase?";
	private VerticalPanel timingPhrasePanel;
	
	private List<TimingPhraseWidget> timingWidgets = new ArrayList<>();

	public TimingPhraseBuilderModal(ExpressionBuilderModal parent, ExpressionBuilderModel parentModel,
			ExpressionBuilderModel mainModel) {
		super("Timing Phrase", parent, parentModel, mainModel);
		this.getApplyButton().addClickHandler(event -> onApplyButtonClick());
		display();
	}

	private void onApplyButtonClick() {
		for(TimingPhraseWidget widget: timingWidgets) {
			if(!widget.isOptional() && !widget.isComplete()) {
				this.getErrorAlert().createAlert("Please complete all required fields.");
				return;
			}
		}
		
		this.getExpressionBuilderParent().showAndDisplay();
	}
	
	@Override
	protected void onCancelButtonClick() {
		this.getParentModel().getChildModels().clear();
		super.onCancelButtonClick();
	}

	@Override
	public void display() {
		this.getContentPanel().clear();
		this.getErrorAlert().clearAlert();
		this.getContentPanel().add(buildContentPanel());
		this.updateCQLDisplay();		
	}

	private Widget buildContentPanel() {
		timingPhrasePanel = new VerticalPanel();
		timingPhrasePanel.setWidth("100%");
		
		TimingPhraseWidget timingWidget = new TimingPhraseWidget(getFirstTimings(), 
				HOW_WOULD_YOU_LIKE_TO_START_YOUR_TIMING_PHRASE, SELECT_THE_START_OF_THE_TIMING_PHRASE,
				this.getParentModel(), false);
		timingWidgets.add(timingWidget);
		
		timingWidget.addChangeHandler(event -> onTimingWidgetChange(timingWidget));
		timingWidget.addQuantityUnitChangeHandler(event -> onQuantityWidgetChange());
		timingWidget.addQuantityValueChangeHandler(event -> onQuantityWidgetChange());

		timingPhrasePanel.add(timingWidget);
		return timingPhrasePanel;
	}
	
	private void onTimingWidgetChange(TimingPhraseWidget timingWidget) {
		int index = timingWidgets.indexOf(timingWidget);
		
		// if the current timing widget is in the middle of the timing list, clear all
		// timing widgets after it
		int size = timingWidgets.size();
		if((index < size)) {
			List<TimingPhraseWidget> toDeleteList = new ArrayList<>();
			for(int i = index + 1; i < size; i++) {
				toDeleteList.add(timingWidgets.get(i));
				timingWidgets.get(i).removeFromParent();
			}
			
			timingWidgets.removeAll(toDeleteList);
		}
		
		// remove from model old ones from model and add new one to model by simply clearing
		// the previous list and going through the new list and adding the values
		this.getParentModel().getChildModels().clear();
		for(TimingPhraseWidget widget : timingWidgets) {
			if(widget.getValue() != null) {
				this.getParentModel().appendExpression(widget.getValue());
			}
		}
		
		this.updateCQLDisplay();		
		
		if(timingWidget.getValue() != null) {
			// append a new timing widget
			List<TimingOperator> nextOperators = getNextTimings(((TimingOperatorModel) timingWidget.getValue()).getTimingOperator());
			boolean isOptional = nextOperators.contains(TimingOperator.DONE_NODE);
			nextOperators.remove(TimingOperator.DONE_NODE); // we don't want done to show up in the list
			if(!nextOperators.isEmpty()) {
				nextOperators.sort((t1, t2) -> t1.getDisplayName().compareTo(t2.getDisplayName()));
				TimingPhraseWidget newElement = new TimingPhraseWidget(nextOperators, 
						CONTINUE_TO_BUILD_YOUR_TIMING, SELECT_THE_NEXT_OPTION_FOR_THE_TIMING, this.getParentModel(), isOptional);
				timingWidgets.add(newElement);
				timingPhrasePanel.add(newElement);
				newElement.addChangeHandler(event -> onTimingWidgetChange(newElement));
				newElement.addQuantityUnitChangeHandler(event -> onQuantityWidgetChange());
				newElement.addQuantityValueChangeHandler(event -> onQuantityWidgetChange());
			}
		}
	}
	
	private void onQuantityWidgetChange() {
		this.getParentModel().getChildModels().clear();
		for(TimingPhraseWidget widget : timingWidgets) {
			if(widget.getValue() != null) {
				this.getParentModel().appendExpression(widget.getValue());
			}
		}
		
		this.updateCQLDisplay();		
	}
	
	private List<TimingOperator> getNextTimings(TimingOperator previousOperator) {
		List<TimingOperator> currentPath = new ArrayList<>();
		this.getParentModel().getChildModels().forEach(m -> currentPath.add(((TimingOperatorModel) m).getTimingOperator()));
		return getTimings(previousOperator, TimingGraphUtil.getTimingGraphsWithPath(currentPath));	
	}
	
	private List<TimingOperator> getFirstTimings() {
		TimingOperator operator = TimingOperator.BEGINNING_NODE;
		List<TimingGraph> graphs = TimingGraphUtil.getTimingGraphs();
		return getTimings(operator, graphs);
	}
	
	private List<TimingOperator> getTimings(TimingOperator operator, List<TimingGraph> graphs) {
		Set<TimingOperator> timings = new HashSet<>();
		
		for(TimingGraph graph : graphs) {
			List<TimingOperator> operators = graph.getGraph().get(operator);
			operators.forEach(o -> timings.add(o));
		}
		
		return new ArrayList<>(timings);
	}
}
