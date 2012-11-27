package org.ifmc.mat.client.diagramObject;

import java.util.ArrayList;
import java.util.List;

import org.ifmc.mat.client.clause.AppController;
import org.ifmc.mat.client.shared.ListBoxMVP;
import org.ifmc.mat.client.shared.MATRadioButton;
import org.ifmc.mat.client.shared.MatContext;
import org.ifmc.mat.shared.Attribute;
import org.ifmc.mat.shared.ConstantMessages;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Anchor;

public class Phrase {
	protected AppController appController;
	protected Anchor link; 
	protected List<Attribute>attributes;
	protected ListBoxMVP listBox;
	protected String text;
	protected boolean isMeasurePhrase;
	private MATRadioButton qdmElementsRadio;
	private MATRadioButton phrasesRadio;
	
	public void setQdmElementsRadio(MATRadioButton qdmElementsRadio){
		this.qdmElementsRadio= qdmElementsRadio;
	}
	public void setPhrasesRadio(MATRadioButton phrasesRadio){
		this.phrasesRadio = phrasesRadio;
	}
	
	
	//Note this will return null if you haven't registered the radio buttons
	public MATRadioButton getQdmElementsRadio(){
		return qdmElementsRadio;
	}
	public MATRadioButton getPhrasesRadio(){
		return phrasesRadio;
	}
	

	public Phrase(AppController appController) {
		this.appController = appController;
		
		attributes = new ArrayList<Attribute>();
		listBox = new ListBoxMVP();
		listBox.setEnabled(appController.isEditable());
		listBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent arg0) {
				int selectedIndex = listBox.getSelectedIndex();
				if (getItemText(selectedIndex).startsWith("-"))
					listBox.setSelectedIndex(selectedIndex - 1);
				attributes = new ArrayList<Attribute>();
				setText();				
				link.getElement().setAttribute("role", "alert");
				if(getCanHaveAttributes())
					link.setVisible(true);
				else
					link.setVisible(false);
			}
		});

		link = new AnchorWithSingleClickHandler();
	}

	public Phrase(AppController appController, String text) {
		this(appController);
		setText(text);
	}
	
	public  boolean getCanHaveAttributes() {
		boolean canHaveAttributes = true;
		if (text == null || text.equals("") || text.equals(SimpleStatement.NONE))
			canHaveAttributes = false;	
		else if (appController.isMeasurePhrase(text))
			canHaveAttributes = false;
		
		if(text.startsWith(ConstantMessages.MEASUREMENT_PERIOD))
			canHaveAttributes=false;
		if(text.startsWith(ConstantMessages.MEASUREMENT_START_DATE))
			canHaveAttributes=false;
		if(text.startsWith(ConstantMessages.MEASUREMENT_END_DATE))
			canHaveAttributes=false;
		return canHaveAttributes;
	}
	
	public void setAppController(AppController appController) {
		this.appController = appController;
	}

	public AppController getAppController() {
		return appController;
	}

	public Anchor getLink() {
		return link;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attrList) {
		this.attributes.clear();
		this.attributes = null;
		this.attributes = cloneAttributes(attrList);
		setAttributesLinkCaption();
		
	}

	public boolean hasAttributes() {
		return attributes.size() > 0;
	}
	
	public String attributesToString() {
		if (attributes.size() == 0)
			return "";
		
		StringBuilder attributeString = new StringBuilder();
		attributeString.append(".");
		if (attributes.size() == 1)
			attributeString.append(attributes.get(0).getAttribute());
		else {
			String separator = "";
			attributeString.append("[");
			for (Attribute attribute : attributes) {
				attributeString.append(separator);
				attributeString.append(attribute.getAttribute());
				separator = ", ";
			}
			attributeString.append("]");
		}
		
		return attributeString.toString();
	}
	
	public ListBoxMVP getListBox() {
		return listBox;
	}

	public void setListBox(ListBoxMVP listBox) {
		this.listBox = listBox;
	}

	public String getItemText() {
		if (listBox == null)
			return "";
		int selectedIndex = listBox.getSelectedIndex();
		if (selectedIndex < 0)	
			if (listBox.getItemCount() > 0) {
				listBox.setSelectedIndex(0);
				selectedIndex = 0;
			}
			else
				return "";
		return listBox.getValue(selectedIndex);
	}

	public String getItemText(int index) {
		if (listBox == null)
			return "";	
		int selectedIndex = listBox.getSelectedIndex();
		if (selectedIndex < 0)	
			if (listBox.getItemCount() > 0) {
				selectedIndex = 0;
				listBox.setSelectedIndex(0);
			}
			else
				return "";
		return listBox.getValue(selectedIndex);
	}

	public void setText() {
		if (listBox == null){
			text = "";
			return;
		}
		int selectedIndex = listBox.getSelectedIndex();
	
		if (selectedIndex < 0)	
			if (listBox.getItemCount() > 0) {
				listBox.setSelectedIndex(0);
				selectedIndex = 0;
			}
			else
				text = "";
		if (selectedIndex >= 0) {
			text = listBox.getValue(selectedIndex);
			listBox.setTitle(text);
		}
	}

	public void setText(String text) {
		this.text = text;
		setSelection(text);
		listBox.setTitle(text);
	}

	public void setSelection() {
		setSelection(text);
	}

	public void setSelection(String text) {
		listBox.setSelectedIndex(-1);
		for (int i = 0; i < listBox.getItemCount(); ++i){
			if (listBox.getItemText(i).equals(text) || listBox.getValue(i).equals(text)) {
				listBox.setSelectedIndex(i);
				break;
			}
		}
	}

	public String getText() {
		return text;
	}

	/*
	 * assuming text is of form *:<<category>>-<<oid>>
	 * where * could contain a -
	 * return text without -<<oid>>
	 */
	public String getTextSansOid() {
		String ret = MatContext.get().getTextSansOid(text);
		return ret;
	}
	
	public boolean isMeasurePhrase() {
		return isMeasurePhrase;
	}

	public void setMeasurePhrase(boolean isMeasurePhrase) {
		this.isMeasurePhrase = isMeasurePhrase;
	}

	public String[] getQDSElements() {	
		return appController.getQDSElements();
	}

	public void setAttributesLinkCaption() {
		StringBuilder sb = new StringBuilder();
		sb.append(attributes.size()).append(" attribute").append((attributes.size() == 1) ? "" : "s").append("; add or edit");
		link.setText(sb.toString());
		
		sb = new StringBuilder();
		String strAttributes = attributesToString();
		sb.append(attributes.size()).append(" attribute").append((attributes.size() == 1) ? "" : "s")
			.append(": \n");
		if (strAttributes.length() > 0)
			sb.append(strAttributes).append("\n");
		sb.append("add or edit");
		link.setTitle(sb.toString());
	}
	
	public Phrase clone() {
		Phrase dest = new Phrase(appController);
		dest.link = link; 
		dest.attributes = new ArrayList<Attribute>();
		for (Attribute attribute : attributes)
			dest.attributes.add(attribute.clone());
		dest.text = text;
		dest.isMeasurePhrase = isMeasurePhrase;
		return dest;
	}

	public List<Attribute> cloneAttributes() {
		return cloneAttributes(attributes);
	}
	
	public List<Attribute> cloneAttributes(List<Attribute> list) {
		List<Attribute> clonedAttributes = new ArrayList<Attribute>();
		for (Attribute attribute : list)
			clonedAttributes.add(attribute.clone());
		return clonedAttributes;
	}
	
	
}
