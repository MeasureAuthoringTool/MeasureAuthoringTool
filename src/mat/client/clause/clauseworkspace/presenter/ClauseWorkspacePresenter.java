package mat.client.clause.clauseworkspace.presenter;

import java.util.ArrayList;
import java.util.List;

import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.MeasureComposerPresenter;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.clause.clauseworkspace.model.TreeModel;
import mat.client.clause.clauseworkspace.view.XmlTreeView;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.MatContext;
import mat.client.shared.SuccessMessageDisplay;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class ClauseWorkspacePresenter implements MatPresenter {

	public static interface XmlTreeDisplay {
		public CellTree getXmlTree();

		public Button getSaveButton();

		public Widget asWidget();
		
		public SuccessMessageDisplay getSuccessMessageDisplay();
		
		public ErrorMessageDisplay getErrorMessageDisplay();
		
		public void clearMessages();
		
		public void setEnabled(boolean enable);
		
	}

	private SimplePanel emptyWidget = new SimplePanel();
	private SimplePanel panel = new SimplePanel();
	private XmlTreeDisplay xmlTreeDisplay;
	MeasureServiceAsync service = MatContext.get().getMeasureService();
	MeasureXmlModel measureXmlModel;
	private static final String NAMESPACE_XML = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\r\n";

	public ClauseWorkspacePresenter() {
		emptyWidget.add(new Label("No Measure Selected"));
		panel.setStyleName("contentPanel");

	}

	@Override
	public void beforeDisplay() {
		if (MatContext.get().getCurrentMeasureId() != null
				&& !MatContext.get().getCurrentMeasureId().equals("")) {
			panel.clear();
			service.getMeasureXmlForMeasure(MatContext.get()
					.getCurrentMeasureId(),
					new AsyncCallback<MeasureXmlModel>() {// Loading the measure's SimpleXML from the Measure_XML table 

						@Override
						public void onSuccess(MeasureXmlModel result) {
							String xml = null;
							if(result != null && result.getXml() != null){
								xml = result.getXml();
							}
							XmlTreeView xmlTreeView = new XmlTreeView(createTreeModel(xml));
							CellTree cellTree = new CellTree(xmlTreeView, null);
							xmlTreeView.createPageView(cellTree);
							panel.add(xmlTreeView.asWidget());
							xmlTreeDisplay = (XmlTreeDisplay) xmlTreeView;
							xmlTreeDisplay.setEnabled(MatContext.get().getMeasureLockService().checkForEditPermission());
							measureXmlModel = createMeasureExportModel();
							processXmlTreeHandlers();
							
						}

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
						}
					});

		} else {
			Mat.hideLoadingMessage();
			displayEmpty();
		}
		
		MeasureComposerPresenter.setSubSkipEmbeddedLink("ClauseWorkspaceTree");
		Mat.focusSkipLists("MeasureComposer");
	}

	private MeasureXmlModel createMeasureExportModel() {
		MeasureXmlModel exportModal = new MeasureXmlModel();
		exportModal.setMeasureId(MatContext.get().getCurrentMeasureId());
		return exportModal;
	}

	private void processXmlTreeHandlers() {
		xmlTreeDisplay.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				xmlTreeDisplay.clearMessages();
				measureXmlModel.setXml(createXmlFromTree());	
				System.out.println(measureXmlModel.getXml());		
				
				
				service.saveMeasureXml(measureXmlModel,
					new AsyncCallback<Void>() {
		
						@Override
						public void onFailure(Throwable caught) {
						}
		
						@Override
						public void onSuccess(Void result) {
							xmlTreeDisplay.getSuccessMessageDisplay().setMessage("Changes are successfully saved.");
						}
					});
			}
		});
	}


	/**
	 * Creates TreeModel object which has list of children objects and a parent object from the XML
	 * @param xml
	 * @return TreeModel
	 */ 
	private TreeModel createTreeModel(String xml){
		TreeModel parentParent = new TreeModel();
		if(xml != null){
			Document document = XMLParser.parse(xml);
			Node node = document.getFirstChild();
			if(node.getNodeName().equals("xml")){// IE returns XML as the firstchild so this check is done 
				node = node.getNextSibling();
			}
			parentParent.setName(node.getNodeName());
			List<TreeModel> childs = new ArrayList<TreeModel>();
			createChilds(parentParent, node, childs);
		}else{
			parentParent.setName("measure");
			TreeModel child = new TreeModel();
			List<TreeModel> childs = new ArrayList<TreeModel>();
			createChildObject(parentParent, child, "measure", childs);
			parentParent.setChilds(childs);
		}
		
		return parentParent;
	}
	
	
	/**
	 * Creating all TreeModel Child Objects
	 * @param treeModel Parent Object
	 * @param root Xml Node
	 * @param childs List of Childs for @TreeModel
	 */
	private void createChilds(TreeModel parent, Node root, List<TreeModel> childs){
		
		TreeModel child = new TreeModel();//child Object
		String name = null;
		if(root.getNodeName().equalsIgnoreCase("#text")){//if node is an Value node
			name = root.getNodeValue();
		}else{//Element node
			name = root.getNodeName();
		}
		createChildObject(parent, child, name, childs);// Create complete child Object with parent and sub Childs
		
		if(root.getNodeType() == root.ELEMENT_NODE && root.hasAttributes()){// if Attribute node
			ArrayList<TreeModel> attrChilds = new ArrayList<TreeModel>();// List will contain only one Object with name as "attribute"
			TreeModel attr = new TreeModel();
			createChildObject(child, attr, "attributes", attrChilds);// create attribute child for top child
			NamedNodeMap namedNodeMap = root.getAttributes();
			ArrayList<TreeModel> attrChildChilds = null;
			for (int j = 0; j < namedNodeMap.getLength(); j++) {// iterate through All Attribute values
				if(j == 0){
					attrChildChilds = new ArrayList<TreeModel>();
				}
				TreeModel attrModel = new TreeModel();
				String attrValue = namedNodeMap.item(j).getNodeName() +" = " +namedNodeMap.item(j).getNodeValue();				
				createChildObject(attr, attrModel, attrValue, attrChildChilds); // create childs for AttributeChild
			}
			attr.setChilds(attrChildChilds);// set the attribute value childs to attribute child
			if(child.getChilds() != null ){
				child.getChilds().addAll(attrChilds); // if parent's child is not null, add the atribute childs to it, 
			}else{
				child.setChilds(attrChilds);//if parent's child is  null, set the atribute childs to it
			}
		}
		
		parent.setChilds(childs);// set parent's childs
		
		NodeList nodes = root.getChildNodes();// get Child nodes for the Processed node and repeat the process
		for(int i = 0; i < nodes.getLength(); i++){
			if(i == 0){
				if(child.getChilds() == null){ 
					childs = new ArrayList<TreeModel>();
				}else{
					childs  = child.getChilds();
				}
			}
			Node node = nodes.item(i);
			String nodeName = node.getNodeName().replaceAll("\n\r", "").trim();
			if(!(nodeName.equalsIgnoreCase("#text") && nodeName.isEmpty())){	
				createChilds(child, node, childs);
			}
		}
	}
	
	private void createChildObject(TreeModel parent, TreeModel child, String name, List<TreeModel> childs){
		child.setName(name);//set the name to Child
		childs.add(child);// add child to child list
		child.setParent(parent);// set parent in child 
	}
	
	
	/**
	 * Creating XML from GWT tree using GWT Document object
	 * @return XML String
	 */
	private String createXmlFromTree(){
		Document doc = XMLParser.createDocument();
		TreeModel model = (TreeModel) xmlTreeDisplay.getXmlTree().getRootTreeNode().getChildValue(0);
		if(model != null){
			return NAMESPACE_XML + createXmlFromTree(model, doc, null, null);
		}
		
		return null;
	}



	/**
	 * Iterating through the Tree's Children to create the Document Element, Nodes and Attributes.
	 * @param treeItem
	 * @param doc
	 * @param node
	 * @param element
	 * @return
	 */
	private String createXmlFromTree(TreeModel treeModel, Document doc, Node node,
			Element element) {
		String nodetext = treeModel.getName();

		if (null == element) {//First Node creation. Executed for the First time
			element = doc.createElement(nodetext);
			node = doc.appendChild(element);
		} else {// if nodeText is not null
			if (nodetext.equalsIgnoreCase("attributes")) {// if Attributes
				if(treeModel.getChilds() != null){
					for (TreeModel attrChild : treeModel.getChilds()) {
						String[] attrPair = attrChild.getName()
								.split("=");// concat with "="
						element.setAttribute(attrPair[0].trim(), attrPair[1].trim());// set it to the element as Attribute.
					}	
				}
				
			} else if (treeModel.getParent() == null	|| !treeModel.getParent().getName()
							.equalsIgnoreCase("attributes")) {		// if not atrributes 		 
				if (treeModel.getChilds() == null || treeModel.getChilds().size() == 0) {// if child count is 0 then consider this as XML value text, else it will be node.
					node.appendChild(doc.createTextNode(nodetext));
				} else {
					element = doc.createElement(nodetext);
					node = node.appendChild(element);
				}
			}
		}

		if(treeModel.getChilds() != null && treeModel.getChilds().size() > 0){
			for (TreeModel model : treeModel.getChilds()) {
				createXmlFromTree(model, doc, node, element);
			}
		}
		return doc.toString();
	}
	
	@Override
	public void beforeClosingDisplay() {

	}

	@Override
	public Widget getWidget() {
		return panel;
	}

	private void displayEmpty() {
		panel.clear();
		panel.add(emptyWidget);
	}

	/**
	 * @return the measureExportModal
	 */
	public MeasureXmlModel getMeasureExportModal() {
		return measureXmlModel;
	}

	/**
	 * @param measureXmlModel
	 *            the measureExportModal to set
	 */
	public void setMeasureExportModal(MeasureXmlModel measureXmlModel) {
		this.measureXmlModel = measureXmlModel;
	}

}
