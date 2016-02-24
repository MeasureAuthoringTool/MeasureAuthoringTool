package mat.client.clause.cqlworkspace;

import java.util.List;
import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;
import mat.model.cql.CQLGrammarDataType;
import mat.model.cql.CQLModel;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AddFunctionArgumentDialogBox {
	
	public static DialogBox dialogBox = new DialogBox(true,false);
	private static List<String> argumentDataTypeList = CQLGrammarDataType.getDataTypeName();
	
	public static void showComparisonDialogBox(CQLModel currentModel){
		
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		dialogBox.setText("Add Argument");
		dialogBox.setTitle("Add Argument");
		dialogBox.getElement().setAttribute("id", "AddArgumentDialogBox");
		final VerticalPanel dialogContents = new VerticalPanel();
		dialogBox.setWidget(dialogContents);
		dialogContents.clear();
		dialogContents.setWidth("21em");
		dialogContents.setSpacing(5);
		dialogContents.getElement().setId("dialogContents_VerticalPanel");
		
		final ListBoxMVP listAllDataTypes = new ListBoxMVP();
		for (int i = 0; i < argumentDataTypeList.size(); i++) {
			listAllDataTypes.addItem(argumentDataTypeList.get(i));
		}
		
		final HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.clear();
		dialogContents.add(hPanel);
		
		Label labelListBoxTimingOrFunction = (Label) LabelBuilder.buildLabel(
				listAllDataTypes, "Available Datatype");
		dialogContents.add(labelListBoxTimingOrFunction);
		dialogContents.setCellHorizontalAlignment(labelListBoxTimingOrFunction,
				HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents.add(listAllDataTypes);
		dialogContents.setCellHorizontalAlignment(listAllDataTypes,
				HasHorizontalAlignment.ALIGN_LEFT);
		
		Button save = new Button("OK", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hPanel.clear();
				dialogContents.clear();
				dialogBox.hide();
			}
		});
		
		Button closeButton = new Button("Cancel", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogContents.clear();
				dialogBox.hide();
				
			}
		});
		closeButton.getElement().setId("closeButton_Button");
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(10);
		buttonPanel.add(save);
		buttonPanel.setCellHorizontalAlignment(save,
				HasHorizontalAlignment.ALIGN_RIGHT);
		buttonPanel.add(closeButton);
		buttonPanel.setCellHorizontalAlignment(closeButton,
				HasHorizontalAlignment.ALIGN_RIGHT);
		buttonPanel.getElement().setId("buttonPanel_HorizontalPanel");
		dialogContents.add(buttonPanel);
		dialogBox.center();
		dialogBox.show();
	}
	
}
