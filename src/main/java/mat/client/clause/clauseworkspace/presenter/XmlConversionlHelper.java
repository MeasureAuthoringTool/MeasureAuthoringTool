package mat.client.clause.clauseworkspace.presenter;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.Text;
import com.google.gwt.xml.client.XMLParser;
import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.model.CellTreeNodeImpl;
import mat.client.shared.MatContext;
import mat.shared.UUIDUtilClient;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The Class XmlConversionlHelper.
 */
public class XmlConversionlHelper {
	
	/** The Constant NAMESPACE_XML. */
	private static final String NAMESPACE_XML = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\r\n";
	
	/**
	 * Creates CellTreeNode object which has list of children objects and a
	 * parent object from the XML.
	 * @param xml
	 *            the xml
	 * @param tagName
	 *            the tag name
	 * @return CellTreeNode
	 */
	public static CellTreeNode createCellTreeNode(String xml, String tagName) {
		
		CellTreeNode mainNode = new CellTreeNodeImpl();
		if ((xml != null) && (xml.trim().length() > 0)) {
			Document document = XMLParser.parse(xml);
			mainNode = createCellTreeNode(document, tagName);
		}
		return mainNode;
	}
	
	/**
	 * Creates CellTreeNode object which has list of children objects and a
	 * parent object from the XML.
	 *
	 * @param doc the doc
	 * @param tagName the tag name
	 * @return CellTreeNode
	 */
	public static CellTreeNode createCellTreeNode(Document doc, String tagName) {
		Node node = null;
		CellTreeNode mainNode = new CellTreeNodeImpl();
		List<CellTreeNode> childs = new ArrayList<CellTreeNode>();
		
		NodeList nodeList = doc.getElementsByTagName(tagName);
		if (nodeList.getLength() > 0) {
			node = nodeList.item(0);
		}
		if (node != null) {
			mainNode.setName(tagName);
			createCellTreeNodeChilds(mainNode, node, childs);
		}
		
		if (node == null) {
			mainNode.setName(tagName);
			childs.add(createRootNode(tagName));
			mainNode.setChilds(childs);
		}
		return mainNode;
	}
	
	/**
	 * Creates CellTreeNode object which has list of children objects and a
	 * parent object from the XML.
	 *
	 * @param node the node
	 * @param tagName the tag name
	 * @return CellTreeNode
	 */
	public static CellTreeNode createCellTreeNode(Node node, String tagName) {
		
		CellTreeNode mainNode = new CellTreeNodeImpl();
		List<CellTreeNode> childs = new ArrayList<CellTreeNode>();
		
		if (node != null) {
			mainNode.setName(tagName);
			createCellTreeNodeChilds(mainNode, node, childs);
		}
		
		if (node == null) {
			mainNode.setName(tagName);
			childs.add(createRootNode(tagName));
			mainNode.setChilds(childs);
		}
		return mainNode;
	}
	
	/**
	 * Method to create Root Clause Node for Sub Tree Node in Clause WorkSpace.
	 * @return CellTreeNode
	 */
	public static CellTreeNode createRootClauseNode() {
		CellTreeNode parent = new CellTreeNodeImpl();
		CellTreeNode mainNode = new CellTreeNodeImpl();
		List<CellTreeNode> childs = new ArrayList<CellTreeNode>();
		List<CellTreeNode> parentchilds = new ArrayList<CellTreeNode>();
		parent.setName(PopulationWorkSpaceConstants.CLAUSE);
		parent.setLabel(PopulationWorkSpaceConstants.CLAUSE);
		parent.setNodeType(CellTreeNode.SUBTREE_ROOT_NODE);
		parent.setChilds(parentchilds);
		parent.setOpen(true);
		childs.add(parent);
		mainNode.setChilds(childs);
		return mainNode;
	}
	
	public static CellTreeNode createOccurenceClauseNode(CellTreeNode clauseNode) {
		//CellTreeNode parent = new CellTreeNodeImpl();
		CellTreeNode occurrenceNode = new CellTreeNodeImpl();
		List<CellTreeNode> parentchilds = new ArrayList<CellTreeNode>();
		occurrenceNode.setName(clauseNode.getName());
		occurrenceNode.setLabel(clauseNode.getLabel());
		occurrenceNode.setUUID(UUIDUtilClient.uuid());
		occurrenceNode.setNodeType(clauseNode.getNodeType());
		occurrenceNode.setChilds(parentchilds);
		occurrenceNode.setOpen(true);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("qdmVariable", "true");
		occurrenceNode.setExtraInformation(PopulationWorkSpaceConstants.EXTRA_ATTRIBUTES, map);
		//childs.add(parent);
		//occurrenceNode.setChilds(childs);
		
		return occurrenceNode;
	}
	
	/**
	 * Creates the root node.
	 * @param tagName
	 *            the tag name
	 * @return the cell tree node
	 */
	private static CellTreeNode createRootNode(String tagName) {
		CellTreeNode parent = new CellTreeNodeImpl();
		List<CellTreeNode> childs = new ArrayList<CellTreeNode>();
		if (tagName.equalsIgnoreCase("populations")) {
			parent.setName(PopulationWorkSpaceConstants.get(tagName));
			parent.setLabel(PopulationWorkSpaceConstants.get(tagName));
			parent.setNodeType(CellTreeNode.MASTER_ROOT_NODE);
			for (int i = 0; i < PopulationWorkSpaceConstants.getPopulationsChildren().length; i++) {
				String nodeValue = PopulationWorkSpaceConstants.getPopulationsChildren()[i];
				//Adding Root nodes under Population.
				CellTreeNode child = createChild(nodeValue, nodeValue, CellTreeNode.ROOT_NODE, parent);
				childs.add(child);
				//Clause Nodes should not have 's' in end. For example 'Numerators' child should be 'Numerator'.
				String name = nodeValue.substring(0, nodeValue.lastIndexOf('s')) + " "  + 1;
				//Adding Clause Nodes
				List<CellTreeNode> subChilds = new ArrayList<CellTreeNode>();
				subChilds.add(createChild(name, name, CellTreeNode.CLAUSE_NODE, child));
				// Adding First 'AND' under clause node.
				for (int j = 0; j < subChilds.size(); j++) {
					List<CellTreeNode> logicalOp = new ArrayList<CellTreeNode>();
					logicalOp.add(createChild(PopulationWorkSpaceConstants.AND, PopulationWorkSpaceConstants.AND,
							CellTreeNode.LOGICAL_OP_NODE, subChilds.get(j)));
					subChilds.get(j).setChilds(logicalOp);
				}
				child.setChilds(subChilds);
			}
			parent.setChilds(childs);
		} else if ("measureObservations".equals(tagName)) {
			parent.setName(PopulationWorkSpaceConstants.get(tagName));
			parent.setLabel(PopulationWorkSpaceConstants.get(tagName));
			parent.setNodeType(CellTreeNode.ROOT_NODE);
			CellTreeNode clauseNode = createChild("Measure Observation 1",
					"Measure Observation 1", CellTreeNode.CLAUSE_NODE, parent);
			childs.add(clauseNode);
			parent.setChilds(childs);
			List<CellTreeNode> logicalOp = new ArrayList<CellTreeNode>();
			logicalOp.add(createChild(PopulationWorkSpaceConstants.AND, PopulationWorkSpaceConstants.AND,
					CellTreeNode.LOGICAL_OP_NODE, clauseNode));
			clauseNode.setChilds(logicalOp);
		} else if ("strata".equalsIgnoreCase(tagName)) {
			parent.setName(PopulationWorkSpaceConstants.get(tagName));
			parent.setLabel(PopulationWorkSpaceConstants.get(tagName));
			parent.setNodeType(CellTreeNode.MASTER_ROOT_NODE);
			
			CellTreeNode child = createChild("Stratification 1", "Stratification 1", CellTreeNode.ROOT_NODE, parent);
			child.setUUID(UUIDUtilClient.uuid());
			childs.add(child);
			parent.setChilds(childs);
			
			List<CellTreeNode> clauseNode = new ArrayList<CellTreeNode>();
			clauseNode.add(createChild("Stratum 1", "Stratum 1", CellTreeNode.CLAUSE_NODE, child));
			clauseNode.get(0).setUUID(UUIDUtilClient.uuid());
			child.setChilds(clauseNode);
			
			for (int j = 0; j < clauseNode.size(); j++) {
				List<CellTreeNode> logicalOp = new ArrayList<CellTreeNode>();
				logicalOp.add(createChild(PopulationWorkSpaceConstants.AND, PopulationWorkSpaceConstants.AND,
						CellTreeNode.LOGICAL_OP_NODE, clauseNode.get(j)));
				clauseNode.get(j).setChilds(logicalOp);
			}
			
			
		}
		return parent;
	}
	
	/**
	 * Creates the child.
	 * 
	 * @param name
	 *            the name
	 * @param label
	 *            the label
	 * @param nodeType
	 *            the node type
	 * @param parent
	 *            the parent
	 * @return the cell tree node
	 */
	private static CellTreeNode createChild(String name, String label, short nodeType, CellTreeNode parent) {
		CellTreeNode child = new CellTreeNodeImpl();
		child.setName(name);
		child.setLabel(label);
		child.setParent(parent);
		child.setNodeType(nodeType);
		return child;
	}
	
	/**
	 * Creates the cell tree node childs.
	 * 
	 * @param parent
	 *            the parent
	 * @param root
	 *            the root
	 * @param childs
	 *            the childs
	 */
	@SuppressWarnings("unchecked")
	private static void createCellTreeNodeChilds(CellTreeNode parent, Node root, List<CellTreeNode> childs) {
		String nodeName = root.getNodeName();
		String nodeValue = root.hasAttributes()
				? root.getAttributes().getNamedItem(PopulationWorkSpaceConstants.DISPLAY_NAME).getNodeValue() : nodeName;
				
				CellTreeNode child = new CellTreeNodeImpl(); //child Object
				if (nodeValue.length() > 0) {
					setCellTreeNodeValues(root, parent, child, childs); // Create complete child Object with parent and sub Childs
				}
				
				parent.setChilds(childs); // set parent's childs
				NodeList nodes = root.getChildNodes(); // get Child nodes for the Processed node and repeat the process
				for (int i = 0; i < nodes.getLength(); i++) {
					if (i == 0) {
						if (child.getChilds() == null) {
							childs = new ArrayList<CellTreeNode>();
						} else {
							childs  = child.getChilds();
						}
					}
					Node node = nodes.item(i);
					String name = node.getNodeName().replaceAll("\n\r", "").trim();
					//if(!(name.equalsIgnoreCase("#text") && name.isEmpty())){
					if ((name.length() > 0) && !name.equalsIgnoreCase("#text") && !name.equalsIgnoreCase("attribute")
							&& !name.equalsIgnoreCase("comment")) {
						createCellTreeNodeChilds(child, node, childs);
					}
					/**
					 * This part for QDM node attributes. The attribute XML node is not to be displayed on the CellTree.
					 */
					else if (name.equalsIgnoreCase("attribute")) {
						Object attributes = child.getExtraInformation("attributes");
						if (attributes == null) {
							attributes = new ArrayList<CellTreeNode>();
							child.setExtraInformation("attributes", attributes);
						}
						List<CellTreeNode> attributeList = (List<CellTreeNode>) attributes;
						CellTreeNode cellNode = new CellTreeNodeImpl();
						NamedNodeMap attNodeMap = node.getAttributes();
						for (int j = 0; j < attNodeMap.getLength(); j++) {
							Node attrib = attNodeMap.item(j);
							cellNode.setExtraInformation(attrib.getNodeName(), attrib.getNodeValue());
						}
						attributeList.add(cellNode);
					} else if (name.equalsIgnoreCase(PopulationWorkSpaceConstants.COMMENTS)) {
						Object comments = child.getExtraInformation(PopulationWorkSpaceConstants.COMMENTS);
						if (comments == null) {
							comments = new ArrayList<CellTreeNode>();
							child.setExtraInformation(PopulationWorkSpaceConstants.COMMENTS, comments);
						}
						List<CellTreeNode> commentList = (List<CellTreeNode>) comments;
						CellTreeNode cellNode = new CellTreeNodeImpl();
						if (node.hasChildNodes()) {
							cellNode.setNodeText(node.getChildNodes().item(0).getNodeValue());
						} else {
							cellNode.setNodeText(StringUtils.EMPTY);
						}
						cellNode.setNodeType(CellTreeNode.COMMENT_NODE);
						cellNode.setName(PopulationWorkSpaceConstants.COMMENT_NODE_NAME);
						commentList.add(cellNode);
					}
				}
	}
	
	/**
	 * Creating XML from GWT tree using GWT Document object.
	 * 
	 * @param model
	 *            the model
	 * @return XML String
	 */
	public static String createXmlFromTree(CellTreeNode model) {
		Document doc = XMLParser.createDocument();
		if (model != null) {
			String returnXml = NAMESPACE_XML + createXmlFromTree(model, doc, null);
			return returnXml;
		}
		
		return null;
	}
	
	/**
	 * Iterating through the Tree's Children to create the Document Element,
	 * Nodes and Attributes.
	 * 
	 * @param cellTreeNode
	 *            the cell tree node
	 * @param doc
	 *            the doc
	 * @param node
	 *            the node
	 * @return the string
	 */
	private static String createXmlFromTree(CellTreeNode cellTreeNode, Document doc, Node node) {
		Element element = getNodeName(cellTreeNode, doc);
		
		if (node != null) {
			node = node.appendChild(element);
		} else {
			node = doc.appendChild(element);
		}
		
		if ((cellTreeNode.getChilds() != null) && (cellTreeNode.getChilds().size() > 0)) {
			for (CellTreeNode model : cellTreeNode.getChilds()) {
				createXmlFromTree(model, doc, node);
			}
		}
		return doc.toString();
	}
	
	/**
	 * Sets the cell tree node values.
	 * 
	 * @param node
	 *            the node
	 * @param parent
	 *            the parent
	 * @param child
	 *            the child
	 * @param childs
	 *            the childs
	 */
	private static void setCellTreeNodeValues(Node node, CellTreeNode parent, CellTreeNode child, List<CellTreeNode> childs) {
		String nodeName = node.getNodeName();
		String nodeValue = node.hasAttributes()
				? node.getAttributes().getNamedItem(PopulationWorkSpaceConstants.DISPLAY_NAME).getNodeValue() : nodeName;
				short cellTreeNodeType = 0;
				String uuid = "";
				if (nodeName.equalsIgnoreCase(PopulationWorkSpaceConstants.MASTER_ROOT_NODE_POPULATION)||
						nodeName.equalsIgnoreCase(PopulationWorkSpaceConstants.MASTER_ROOT_NODE_STRATA) ) {
					cellTreeNodeType =  CellTreeNode.MASTER_ROOT_NODE;
				} else if (PopulationWorkSpaceConstants.ROOT_NODES.contains(nodeName)) {
					cellTreeNodeType =  CellTreeNode.ROOT_NODE;
					
					if((node.getAttributes().getNamedItem(PopulationWorkSpaceConstants.UUID) != null) &&
							(node.getAttributes().getNamedItem(PopulationWorkSpaceConstants.UUID).getNodeValue() != null) )
					{
						uuid = node.getAttributes().getNamedItem(PopulationWorkSpaceConstants.UUID).getNodeValue();
					}
					
				} else if (nodeName.equalsIgnoreCase(PopulationWorkSpaceConstants.CLAUSE_TYPE)) {
					cellTreeNodeType =  CellTreeNode.CLAUSE_NODE;
					uuid = node.getAttributes().getNamedItem(PopulationWorkSpaceConstants.UUID).getNodeValue();
				} else if(nodeName.equalsIgnoreCase(PopulationWorkSpaceConstants.CQL_DEFINITION_TYPE)) {
					cellTreeNodeType =  CellTreeNode.CQL_DEFINITION_NODE;
					uuid = node.getAttributes().getNamedItem(PopulationWorkSpaceConstants.UUID).getNodeValue();
				}else if(nodeName.equalsIgnoreCase(PopulationWorkSpaceConstants.CQL_FUNCTION_TYPE)) {
					cellTreeNodeType =  CellTreeNode.CQL_FUNCTION_NODE;
					uuid = node.getAttributes().getNamedItem(PopulationWorkSpaceConstants.UUID).getNodeValue();
				} else if(nodeName.equalsIgnoreCase(PopulationWorkSpaceConstants.CQL_AGG_FUNCTION_TYPE)){
					cellTreeNodeType =  CellTreeNode.CQL_AGG_FUNCTION_NODE;
				}
				else if (nodeName.equalsIgnoreCase(PopulationWorkSpaceConstants.LOG_OP)) {
					cellTreeNodeType = CellTreeNode.LOGICAL_OP_NODE;
				}  else if (nodeName.equalsIgnoreCase(PopulationWorkSpaceConstants.SET_OP)) {
					cellTreeNodeType = CellTreeNode.SET_OP_NODE;
				} else if(nodeName.equalsIgnoreCase(PopulationWorkSpaceConstants.SUBTREE_NAME)) {
					cellTreeNodeType = CellTreeNode.SUBTREE_NODE;
					uuid = node.getAttributes().getNamedItem(PopulationWorkSpaceConstants.UUID).getNodeValue();
					HashMap<String, String> map = new HashMap<String, String>();
					if (node.hasAttributes()) {
						NamedNodeMap nodeMap = node.getAttributes();
						for (int i = 0; i < nodeMap.getLength(); i++) {
							String key = nodeMap.item(i).getNodeName();
							if(key.equalsIgnoreCase("qdmVariable")) {
								String value = nodeMap.item(i).getNodeValue();
								map.put(key, value);
							}
						}
					}
					child.setExtraInformation(PopulationWorkSpaceConstants.EXTRA_ATTRIBUTES, map);
				}
				else if (nodeName.equalsIgnoreCase(PopulationWorkSpaceConstants.RELATIONAL_OP)) {
					String type = node.getAttributes().getNamedItem(PopulationWorkSpaceConstants.TYPE).getNodeValue();
					String longName = MatContext.get().operatorMapKeyShort.get(type);
					if (MatContext.get().timings.contains(longName)) {
						cellTreeNodeType = CellTreeNode.TIMING_NODE;
						NamedNodeMap nodeMap = node.getAttributes();
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < nodeMap.getLength(); i++) {
							String key = nodeMap.item(i).getNodeName();
							String value = nodeMap.item(i).getNodeValue();
							map.put(key, value);
						}
						child.setExtraInformation(PopulationWorkSpaceConstants.EXTRA_ATTRIBUTES, map);
					} else {
						cellTreeNodeType = CellTreeNode.RELATIONSHIP_NODE;
					}
				} else if (nodeName.equalsIgnoreCase(PopulationWorkSpaceConstants.ELEMENT_REF)) {
					cellTreeNodeType = CellTreeNode.ELEMENT_REF_NODE;
					uuid =  node.getAttributes().getNamedItem(PopulationWorkSpaceConstants.ID).getNodeValue();
				} else if (nodeName.equalsIgnoreCase(PopulationWorkSpaceConstants.SUBTREE_REF)) {
					cellTreeNodeType = CellTreeNode.SUBTREE_REF_NODE;
					uuid =  node.getAttributes().getNamedItem(PopulationWorkSpaceConstants.ID).getNodeValue();
					//in case if SubTree with uuid has been changed.
					nodeValue = PopulationWorkSpaceConstants.subTreeLookUpName.get(uuid);
				} else if (nodeName.equalsIgnoreCase(PopulationWorkSpaceConstants.FUNC_NAME)) {
					cellTreeNodeType = CellTreeNode.FUNCTIONS_NODE;
					HashMap<String, String> map = new HashMap<String, String>();
					if (node.hasAttributes()) {
						NamedNodeMap nodeMap = node.getAttributes();
						for (int i = 0; i < nodeMap.getLength(); i++) {
							String key = nodeMap.item(i).getNodeName();
							String value = nodeMap.item(i).getNodeValue();
							map.put(key, value);
						}
					}
					child.setExtraInformation(PopulationWorkSpaceConstants.EXTRA_ATTRIBUTES, map);
				}
				child.setName(nodeValue); //set the name to Child
				child.setLabel(nodeValue);
				child.setNodeType(cellTreeNodeType);
				child.setParent(parent); // set parent in child
				child.setUUID(uuid);
				childs.add(child); // add child to child list
	}
	
	/**
	 * Gets the node name. Recently added code for SubTree Node.
	 * 
	 * @param cellTreeNode
	 *            the cell tree node
	 * @param document
	 *            the document
	 * @return the node name
	 */
	@SuppressWarnings("unchecked")
	private static Element getNodeName(CellTreeNode cellTreeNode, Document document) {
		Element element = null;
		System.out.println("Node name:"+cellTreeNode.getName());
		System.out.println("Node type:"+cellTreeNode.getNodeType());
		System.out.println();
		switch (cellTreeNode.getNodeType()) {
			case CellTreeNode.MASTER_ROOT_NODE:
				element = document.createElement(PopulationWorkSpaceConstants.get(cellTreeNode.getName()));
				element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME, cellTreeNode.getName());
				break;
			case CellTreeNode.ROOT_NODE:
				
				if(cellTreeNode.getName().contains("Stratification"))
				{
					
					element = document.createElement(cellTreeNode.getName().substring(0, cellTreeNode.getName().lastIndexOf(" ")).replaceAll("S", "s"));
					element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME, cellTreeNode.getName());
					element.setAttribute(PopulationWorkSpaceConstants.UUID, cellTreeNode.getUUID());
					
				}
				else
				{
					element = document.createElement(PopulationWorkSpaceConstants.get(cellTreeNode.getName()));
					element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME, cellTreeNode.getName());
				}
				
				break;
			case CellTreeNode.CQL_DEFINITION_NODE:
				element = document.createElement(PopulationWorkSpaceConstants.CQL_DEFINITION_TYPE);
				element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME, cellTreeNode.getName());				
				element.setAttribute(PopulationWorkSpaceConstants.UUID, cellTreeNode.getUUID());
				break;
			case CellTreeNode.CQL_FUNCTION_NODE:
				element = document.createElement(PopulationWorkSpaceConstants.CQL_FUNCTION_TYPE);
				element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME, cellTreeNode.getName());				
				element.setAttribute(PopulationWorkSpaceConstants.UUID, cellTreeNode.getUUID());
				break;
			case CellTreeNode.CQL_AGG_FUNCTION_NODE:
				element = document.createElement(PopulationWorkSpaceConstants.CQL_AGG_FUNCTION_TYPE);
				element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME, cellTreeNode.getName());				
				break;
			case CellTreeNode.CLAUSE_NODE:
				element = document.createElement(PopulationWorkSpaceConstants.CLAUSE_TYPE);
				element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME, cellTreeNode.getName());
				element.setAttribute(PopulationWorkSpaceConstants.TYPE,
						toCamelCase(cellTreeNode.getName().substring(0, cellTreeNode.getName().lastIndexOf(" "))));
				element.setAttribute(PopulationWorkSpaceConstants.UUID, cellTreeNode.getUUID());
				break;
			case CellTreeNode.LOGICAL_OP_NODE:
				element = document.createElement(PopulationWorkSpaceConstants.LOG_OP);
				element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME, cellTreeNode.getName());
				element.setAttribute(PopulationWorkSpaceConstants.TYPE, toCamelCase(cellTreeNode.getName()));
				List<CellTreeNode> commentList = (List<CellTreeNode>) cellTreeNode.getExtraInformation(PopulationWorkSpaceConstants.COMMENTS);
				if (commentList != null) {
					for (CellTreeNode commentNode:commentList) {
						Element commentElement = document.createElement(
								PopulationWorkSpaceConstants.COMMENT_NODE_NAME);
						Text commentNodeText = document.createTextNode(commentNode.getNodeText());
						commentElement.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME
								, commentNode.getName());
						commentElement.setAttribute(PopulationWorkSpaceConstants.TYPE, commentNode.getName());
						commentElement.appendChild(commentNodeText);
						element.appendChild(commentElement);
						
					}
				}
				break;
			case CellTreeNode.SET_OP_NODE:
				element = document.createElement(PopulationWorkSpaceConstants.SET_OP);
				element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME, capWords(cellTreeNode.getName(), null));
				element.setAttribute(PopulationWorkSpaceConstants.TYPE, toCamelCase(cellTreeNode.getName()));
				break;
			case CellTreeNode.TIMING_NODE:
				element = document.createElement(PopulationWorkSpaceConstants.RELATIONAL_OP);
				HashMap<String, String> map = (HashMap<String, String>) cellTreeNode.getExtraInformation(
						PopulationWorkSpaceConstants.EXTRA_ATTRIBUTES);
				if (map != null) {
					element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME,
							map.get(PopulationWorkSpaceConstants.DISPLAY_NAME));
					//String typeValue = ClauseConstants.getTimingOperators().containsKey(map.get(ClauseConstants.TYPE))
					//	? ClauseConstants.getTimingOperators().get(map.get(ClauseConstants.TYPE)):
					//  map.get(ClauseConstants.DISPLAY_NAME);
					String typeValue = map.get(PopulationWorkSpaceConstants.TYPE);
					element.setAttribute(PopulationWorkSpaceConstants.TYPE, typeValue);
					if (map.containsKey(PopulationWorkSpaceConstants.OPERATOR_TYPE)) {
						element.setAttribute(PopulationWorkSpaceConstants.OPERATOR_TYPE,
								map.get(PopulationWorkSpaceConstants.OPERATOR_TYPE));
					}
					if (map.containsKey(PopulationWorkSpaceConstants.QUANTITY)) {
						element.setAttribute(PopulationWorkSpaceConstants.QUANTITY,
								map.get(PopulationWorkSpaceConstants.QUANTITY));
					}
					if (map.containsKey(PopulationWorkSpaceConstants.UNIT)) {
						element.setAttribute(PopulationWorkSpaceConstants.UNIT,
								map.get(PopulationWorkSpaceConstants.UNIT));
					}
				} else {
					element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME, cellTreeNode.getName());
					element.setAttribute(PopulationWorkSpaceConstants.TYPE,
							MatContext.get().operatorMapKeyLong.get(cellTreeNode.getName()));
				}
				break;
			case CellTreeNode.RELATIONSHIP_NODE:
				element = document.createElement(PopulationWorkSpaceConstants.RELATIONAL_OP);
				element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME, cellTreeNode.getName());
				if(MatContext.get().relationships.contains(cellTreeNode.getName())){
					element.setAttribute(PopulationWorkSpaceConstants.TYPE,
							MatContext.get().operatorMapKeyLong.get(cellTreeNode.getName()));
				} else {
					element.setAttribute(PopulationWorkSpaceConstants.TYPE,
							MatContext.get().removedRelationshipTypes.get(cellTreeNode.getName()));
				}
				break;
			case CellTreeNode.ELEMENT_REF_NODE:
				element = document.createElement(PopulationWorkSpaceConstants.ELEMENT_REF);
				//			Node idNode = ClauseConstants.getElementLookUps()
				//.get(cellTreeNode.getName()).getAttributes().getNamedItem("uuid");
				element.setAttribute(PopulationWorkSpaceConstants.ID, cellTreeNode.getUUID()); // TBD if we need this
				element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME, cellTreeNode.getName());
				element.setAttribute(PopulationWorkSpaceConstants.TYPE, "qdm"); //this can change
				
				List<CellTreeNode> attributeList = (List<CellTreeNode>) cellTreeNode.getExtraInformation("attributes");
				if (attributeList != null) {
					for (CellTreeNode attribNode:attributeList) {
						Element attribElement = document.createElement(PopulationWorkSpaceConstants.ATTRIBUTE);
						for (String name:((CellTreeNodeImpl) attribNode).getExtraInformationMap().keySet()) {
							attribElement.setAttribute(name, (String) attribNode.getExtraInformation(name));
						}
						element.appendChild(attribElement);
					}
				}
				break;
			case CellTreeNode.FUNCTIONS_NODE:
				element = document.createElement(PopulationWorkSpaceConstants.FUNC_NAME);
				
				HashMap<String, String> functionMap = (HashMap<String, String>) cellTreeNode.getExtraInformation(
						PopulationWorkSpaceConstants.EXTRA_ATTRIBUTES);
				if (functionMap != null) {
					String operator = null;
					if (functionMap.containsKey(PopulationWorkSpaceConstants.OPERATOR_TYPE)) {
						operator = MatContext.get().operatorMapKeyLong.get(
								functionMap.get(PopulationWorkSpaceConstants.OPERATOR_TYPE));
					}
					element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME,
							capWords(functionMap.get(PopulationWorkSpaceConstants.DISPLAY_NAME), operator));
					element.setAttribute(PopulationWorkSpaceConstants.TYPE,
							functionMap.get(PopulationWorkSpaceConstants.TYPE));
					if (functionMap.containsKey(PopulationWorkSpaceConstants.OPERATOR_TYPE)) {
						element.setAttribute(PopulationWorkSpaceConstants.OPERATOR_TYPE,
								functionMap.get(PopulationWorkSpaceConstants.OPERATOR_TYPE));
					}
					if (functionMap.containsKey(PopulationWorkSpaceConstants.QUANTITY)) {
						element.setAttribute(PopulationWorkSpaceConstants.QUANTITY,
								functionMap.get(PopulationWorkSpaceConstants.QUANTITY));
					}
					if (functionMap.containsKey(PopulationWorkSpaceConstants.UNIT)) {
						element.setAttribute(PopulationWorkSpaceConstants.UNIT,
								functionMap.get(PopulationWorkSpaceConstants.UNIT));
					}
				} else {
					element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME
							, capWords(cellTreeNode.getName(), null));
					element.setAttribute(PopulationWorkSpaceConstants.TYPE,
							MatContext.get().operatorMapKeyLong.get(cellTreeNode.getName()));
				}
				break;
			case CellTreeNode.SUBTREE_ROOT_NODE:
				element = document.createElement(PopulationWorkSpaceConstants.SUBTREE_ROOT_NAME);
				break;
			case CellTreeNode.SUBTREE_NODE:
				element = document.createElement(PopulationWorkSpaceConstants.SUBTREE_NAME);
				element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME, cellTreeNode.getName());
				element.setAttribute(PopulationWorkSpaceConstants.UUID, cellTreeNode.getUUID());
				if (cellTreeNode.getExtraInformation(PopulationWorkSpaceConstants.EXTRA_ATTRIBUTES) != null) {
					HashMap <String, String> qdmVariableMap = (HashMap<String, String>)
							cellTreeNode.getExtraInformation(PopulationWorkSpaceConstants.EXTRA_ATTRIBUTES);
					element.setAttribute(PopulationWorkSpaceConstants.CLAUSE_QDM_VARIABLE,
							qdmVariableMap.get("qdmVariable"));
				} else {
					element.setAttribute(PopulationWorkSpaceConstants.CLAUSE_QDM_VARIABLE, "false");
				}
				break;
			case CellTreeNode.SUBTREE_REF_NODE:
				element = document.createElement(PopulationWorkSpaceConstants.SUBTREE_REF);
				element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME, cellTreeNode.getName());
				element.setAttribute(PopulationWorkSpaceConstants.ID, cellTreeNode.getUUID());
				element.setAttribute(PopulationWorkSpaceConstants.TYPE, "subTree");
				List<CellTreeNode> commentInSubTreeList = (List<CellTreeNode>)
						cellTreeNode.getExtraInformation(PopulationWorkSpaceConstants.COMMENTS);
				if (commentInSubTreeList != null) {
					for (CellTreeNode commentNode:commentInSubTreeList) {
						Element commentElement = document.createElement(
								PopulationWorkSpaceConstants.COMMENT_NODE_NAME);
						Text commentNodeText = document.createTextNode(commentNode.getNodeText());
						commentElement.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME
								, commentNode.getName());
						commentElement.setAttribute(PopulationWorkSpaceConstants.TYPE, commentNode.getName());
						commentElement.appendChild(commentNodeText);
						element.appendChild(commentElement);
					}
				}
				break;
			default:
				element = document.createElement(cellTreeNode.getName());
				break;
		}
		return element;
	}
	
	/**
	 * Method to convert case of string into camel case.
	 * 
	 * @param name
	 *            the name
	 * @return the string
	 */
	private static String toCamelCase(String name) {
		name = name.toLowerCase();
		String[] parts = name.split(" ");
		String camelCaseString = parts[0].substring(0, 1).toLowerCase() + parts[0].substring(1);
		for (int i = 1; i < parts.length; i++) {
			camelCaseString = camelCaseString + toProperCase(parts[i]);
		}
		return camelCaseString;
	}
	
	/**
	 * To proper case.
	 * @param s
	 *            the s
	 * @return the string
	 */
	private static String toProperCase(String s) {
		return s.substring(0, 1).toUpperCase()
				+ s.substring(1).toLowerCase();
	}
	/**
	 * Method to convert String into Sentence/Title Case.
	 * Operator If Any.
	 * @param strToConvert - String
	 * @param operator - String
	 * @return Sentence Case String
	 */
	private static String capWords(String strToConvert , String operator) {
		if ((strToConvert == null)) {
			return strToConvert;
		} else {
			StringBuilder sb = new StringBuilder();
			if (operator != null) {
				if (strToConvert.contains(operator)) {
					String[] newString = strToConvert.split(operator);
					// For functional Ops, Function name is always contains in index 0.
					//This will not work for RelOp/Timing.
					String token = newString[0];
					for (String tokenSpace : token.split(" ")) {
						if (tokenSpace.isEmpty()) {
							if (sb.length() > 0) {
								sb.append(" ");
							}
						} else {
							if (sb.length() > 0) {
								sb.append(" ");
							}
							sb.append(Character.toUpperCase(tokenSpace.charAt(0)));
							if (tokenSpace.length() > 1) {
								sb.append(tokenSpace.substring(1).toLowerCase());
							}
						}
					}
				}
				int indexOfOperator = strToConvert.indexOf(operator);
				String subString = strToConvert.substring(indexOfOperator);
				sb.append(subString);
			} else {
				for (String token : strToConvert.split(" ")) {
					if (token.isEmpty()) {
						if (sb.length() > 0) {
							sb.append(" ");
						}
					} else {
						if (sb.length() > 0) {
							sb.append(" ");
						}
						sb.append(Character.toUpperCase(token.charAt(0)));
						if (token.length() > 1) {
							sb.append(token.substring(1).toLowerCase());
						}
					}
				}
			}
			return sb.toString();
		}
	}
}
