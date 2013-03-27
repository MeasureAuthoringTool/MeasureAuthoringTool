package mat.client.clause.clauseworkspace.presenter;

import java.util.ArrayList;
import java.util.List;

import mat.client.clause.clauseworkspace.model.TreeModel;
import mat.client.shared.MatContext;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class XmlConversionlHelper {

	private static final String NAMESPACE_XML = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\r\n";
	
	private static String measureTitle = null;
	
	
	/**
	 * Creates TreeModel object which has list of children objects and a parent object from the XML
	 * @param xml
	 * @return TreeModel
	 */ 
	public static TreeModel createTreeModel(String xml, String tagName){
		TreeModel parentParent = new TreeModel();
		Node node = null;
		String shortTitle = MatContext.get().getCurrentShortName();
		measureTitle = shortTitle;
		if(xml != null && xml.trim().length() > 0){
			Document document = XMLParser.parse(xml);
			NodeList nodeList = document.getElementsByTagName(tagName);
			if(nodeList.getLength() > 0){
				for (int i = 0; i < nodeList.getLength(); i++) {
					if("measure".equals(nodeList.item(i).getParentNode().getNodeName())){
						node = nodeList.item(i);
					}
				}
			}else{
				node = document.getElementsByTagName(tagName).item(0);	
			}
			
			if(node != null){
				parentParent.setName(node.getNodeName());
				List<TreeModel> childs = new ArrayList<TreeModel>();
				createChilds(parentParent, node, childs);
			}
			
		}
		if(node == null){
			parentParent.setName(tagName);
			parentParent.setEditable(false);
			parentParent.setRemovable(false);
			TreeModel child = new TreeModel();
			child.setEditable(false);
			List<TreeModel> childs = new ArrayList<TreeModel>();
			setChildObject(parentParent, child, tagName, childs);
			createStaticChilds(tagName, child);
			parentParent.setChilds(childs);
		}
		return parentParent;
	}
	
	
	private static void createStaticChilds(String tagName, TreeModel parent){
		List<TreeModel> childs = new ArrayList<TreeModel>();
		if("populations".equals(tagName)){
			for (int i = 0; i < ClauseConstants.getPopulationsChildren().length; i++) {
				TreeModel child = createChild(ClauseConstants.getPopulationsChildren()[i], parent, false, false);
				childs.add(child);
				String key = ClauseConstants.getPopulationsChildren()[i] + "" + 1;				
				List<TreeModel> subChilds = new ArrayList<TreeModel>();
				subChilds.add(createChild(key, child, false, false));
				child.setChilds(subChilds);
			}
			parent.setChilds(childs);
		}else if("measureObservations".equals(tagName)){
			String key = "measureObservations1";		
			childs.add(createChild(key, parent, false, false));
			parent.setChilds(childs);
		}else if("stratification".equals(tagName)){
			String key = "stratification1";		
			childs.add(createChild(key, parent, false, false));
			parent.setChilds(childs);
		}
		
	}
	
	private static TreeModel createChild(String name, TreeModel parent, boolean isEditable, boolean isRemovable){
		TreeModel child = new TreeModel();
		child.setName(name);
		child.setLabel(ClauseConstants.get(name));
		child.setParent(parent);
		child.setEditable(isEditable);
		child.setRemovable(isRemovable);
		return child;
	}
	
	/**
	 * Creating all TreeModel Child Objects
	 * @param treeModel Parent Object
	 * @param root Xml Node
	 * @param childs List of Childs for @TreeModel
	 */
	private static void createChilds(TreeModel parent, Node root, List<TreeModel> childs){
		
		TreeModel child = new TreeModel();//child Object
		String name = null;
		if(root.getNodeName().equalsIgnoreCase("#text")){//if node is an Value node
			name = root.getNodeValue().replaceAll("\n\r", "").trim();
		}else{//Element node
			name = root.getNodeName();
		}
		if(name.length() > 0){
			
			setChildObject(parent, child, name, childs);// Create complete child Object with parent and sub Childs
		}
		
		if(root.getNodeType() == root.ELEMENT_NODE && root.hasAttributes()){// if Attribute node
			ArrayList<TreeModel> attrChilds = new ArrayList<TreeModel>();// List will contain only one Object with name as "attribute"
			TreeModel attr = new TreeModel();
			attr.setEditable(false);
			setChildObject(child, attr, "attributes", attrChilds);// create attribute child for top child
			NamedNodeMap namedNodeMap = root.getAttributes();
			ArrayList<TreeModel> attrChildChilds = null;
			for (int j = 0; j < namedNodeMap.getLength(); j++) {// iterate through All Attribute values
				if(j == 0){
					attrChildChilds = new ArrayList<TreeModel>();
				}
				TreeModel attrModel = new TreeModel();
				String attrValue = namedNodeMap.item(j).getNodeName() +" = " +namedNodeMap.item(j).getNodeValue();				
				setChildObject(attr, attrModel, attrValue, attrChildChilds); // create childs for AttributeChild
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
	
	private static void setChildObject(TreeModel parent, TreeModel child, String name, List<TreeModel> childs){
		child.setName(name);//set the name to Child
		child.setLabel(ClauseConstants.get(name));
		childs.add(child);// add child to child list
		child.setParent(parent);// set parent in child 
		if(child.getLabel() != null){
			child.setRemovable(false);
			child.setEditable(false);
		}
	}
	
	
	/**
	 * Creating XML from GWT tree using GWT Document object
	 * @return XML String
	 */
	public static String createXmlFromTree(TreeModel model){
		Document doc = XMLParser.createDocument();
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
	private static String createXmlFromTree(TreeModel treeModel, Document doc, Node node,
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
}
