package org.ifmc.mat.client.clause.view;

import org.ifmc.mat.client.shared.MatTabLayoutPanel;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CanvasPanelDelegate {

	public void addCanvasPanel(MatTabLayoutPanel criterionPanel, ScrollPanel canvasPanel, String header){
		VerticalPanel vp = new VerticalPanel();
		if(canvasPanel == null)
			canvasPanel = new ScrollPanel();
		canvasPanel.setWidth("800px");
		canvasPanel.setHeight("240px");
		canvasPanel.setStylePrimaryName("canvasPanelDVI");
		vp.add(canvasPanel);
		criterionPanel.add(vp, header);
		/*
		<span style="font-size: 14px; font-weight: bold; text-align: top">
			Population
		</span>
		*/
	}
	
	public void addCanvasPanel(MatTabLayoutPanel criterionPanel, FlowPanel flowPanel, Label label, String header){
		if(flowPanel == null)
			flowPanel = new FlowPanel();
		flowPanel.setWidth("800px");
		flowPanel.setHeight("240px");
		flowPanel.setStylePrimaryName("canvasPanelDVI");
		if(label == null)
			label = new Label();
		label.setWordWrap(true);
		label.setWidth("100%");
		label.setHeight("240px");
		label.setStylePrimaryName("measurePhraseTextAreaDVI");
		flowPanel.add(label);
		criterionPanel.add(flowPanel, header);
	}
	public void addCanvasPanel(MatTabLayoutPanel criterionPanel, FlowPanel flowPanel, TextArea label, String header){
		if(flowPanel == null)
			flowPanel = new FlowPanel();
		flowPanel.setWidth("800px");
		flowPanel.setHeight("240px");
		flowPanel.setStylePrimaryName("canvasPanelDVI");
		if(label == null)
			label = new TextArea();
		label.setReadOnly(true); 
		label.setVisible(true);
		label.setWidth("100%");
		label.setHeight("240px");
		label.setStylePrimaryName("measurePhraseTextAreaDVI");
		flowPanel.add(label);
		criterionPanel.add(flowPanel, header);
	}
}
