package mat.client.clause.cqlworkspace;

import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.constants.LabelType;

import com.google.gwt.user.client.ui.VerticalPanel;

import mat.client.shared.MatContext;
import mat.client.shared.SpacerWidget;
import mat.client.util.MatTextBox;

public class CQLGeneralInformationView {
	private VerticalPanel generalInfoMainVPanel = new VerticalPanel();
	
	public CQLGeneralInformationView(){
		VerticalPanel generalInfoTopPanel = new VerticalPanel();
		
		Label libraryNameLabel = new Label(LabelType.INFO, "CQL Library Name");
		
		libraryNameLabel.getElement().setAttribute("style", "font-size:90%;margin-left:15px;background-color:#0964A2;");
		libraryNameLabel.setWidth("150px");
		
		MatTextBox libraryNameValue = new MatTextBox();
		libraryNameValue.getElement().setAttribute("style", "margin-left:15px;width:250px;height:25px;");
		libraryNameValue.setText(createCQLLibraryName(MatContext.get().getCurrentMeasureName()));
		libraryNameValue.setReadOnly(true);
		
		Label libraryVersionLabel = new Label(LabelType.INFO, "Version");
		
		libraryVersionLabel.getElement().setAttribute("style", "font-size:90%;margin-left:15px;background-color:#0964A2;");
		libraryVersionLabel.setWidth("150px");
		
		MatTextBox libraryVersionValue = new MatTextBox();
		libraryVersionValue.getElement().setAttribute("style", "margin-left:15px;width:250px;height:25px;");
		
		String measureVersion = MatContext.get().getCurrentMeasureVersion();
		
		measureVersion = measureVersion.replaceAll("Draft ", "").trim();
		if(measureVersion.startsWith("v")){
			measureVersion = measureVersion.substring(1);
		}
		
		libraryVersionValue.setText(measureVersion);
		libraryVersionValue.setReadOnly(true);
		
		Label usingModeLabel = new Label(LabelType.INFO, "Using Model");
		MatTextBox usingModelValue = new MatTextBox();
		usingModeLabel.getElement().setAttribute("style", "font-size:90%;margin-left:15px;background-color:#0964A2;");
		usingModeLabel.setWidth("150px");
		usingModelValue.getElement().setAttribute("style", "margin-left:15px;width:250px;height:25px;");
		usingModelValue.setText("QDM");
		usingModelValue.setReadOnly(true);
		
		Label modelVersionLabel = new Label(LabelType.INFO, "Version");
		
		modelVersionLabel.getElement().setAttribute("style", "font-size:90%;margin-left:15px;background-color:#0964A2;");
		modelVersionLabel.setWidth("150px");
		MatTextBox modelVersionValue = new MatTextBox();
		modelVersionValue.getElement().setAttribute("style", "margin-left:15px;width:250px;height:25px;");
		//modelVersionValue.setText("5.0");
		modelVersionValue.setReadOnly(true);
		modelVersionValue.setText("5.0.2");
		
		generalInfoTopPanel.add(new SpacerWidget());
		// messagePanel.add(successMessageAlert);
		generalInfoTopPanel.add(new SpacerWidget());
		
		generalInfoTopPanel.add(libraryNameLabel);
		generalInfoTopPanel.add(new SpacerWidget());
		generalInfoTopPanel.add(libraryNameValue);
		generalInfoTopPanel.add(new SpacerWidget());
		
		generalInfoTopPanel.add(libraryVersionLabel);
		generalInfoTopPanel.add(new SpacerWidget());
		generalInfoTopPanel.add(libraryVersionValue);
		generalInfoTopPanel.add(new SpacerWidget());
		
		generalInfoTopPanel.add(usingModeLabel);
		generalInfoTopPanel.add(new SpacerWidget());
		generalInfoTopPanel.add(usingModelValue);
		generalInfoTopPanel.add(new SpacerWidget());
		
		generalInfoTopPanel.add(modelVersionLabel);
		generalInfoTopPanel.add(new SpacerWidget());
		generalInfoTopPanel.add(modelVersionValue);
		generalInfoTopPanel.add(new SpacerWidget());
		
	
		generalInfoMainVPanel.setStyleName("cqlRightContainer");
		generalInfoMainVPanel.setWidth("715px");
		generalInfoTopPanel.setWidth("700px");
		generalInfoTopPanel.setStyleName("marginLeft15px");
		generalInfoMainVPanel.add(generalInfoTopPanel);
	}
	
	/**
	 * This method will take a String and remove all non-alphabet/non-numeric characters 
	 * except underscore ("_") characters.
	 * @param originalString
	 * @return cleanedString
	 */
	private String createCQLLibraryName(String originalString) {
		originalString = originalString.replaceAll(" ", "");
		String cleanedString = "";
				
		for(int i=0;i<originalString.length();i++){
			char c = originalString.charAt(i);
			int intc = (int)c;
			if(c == '_' || (intc >= 48 && intc <= 57) || (intc >= 65 && intc <= 90) || (intc >= 97 && intc <= 122)){
				
				if(!(cleanedString.isEmpty() && Character.isDigit(c))){
					cleanedString = cleanedString + "" + c;
				}
				
			} 
			
		}
		
		return cleanedString;
	}

	public VerticalPanel asWidget(){
		return generalInfoMainVPanel;
	}

}
