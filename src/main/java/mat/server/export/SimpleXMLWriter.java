package mat.server.export;

import com.thoughtworks.xstream.XStream;
import mat.server.service.impl.XMLUtility;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Class SimpleXMLWriter.
 */
public class SimpleXMLWriter {
	
	/** The Constant CONVERSION_FILE_FILTER. */
	private static final String CONVERSION_FILE_FILTER="xsl/filter.xsl";
	
	
	/**
	 * Filter xsl.
	 * 
	 * @param xmlStr
	 *            the xml str
	 * @return the string
	 */
	private String filterXSL(String xmlStr) {
		String tempXML = XMLUtility.getInstance().applyXSL(xmlStr, XMLUtility.getInstance().getXMLResource(CONVERSION_FILE_FILTER));
		return tempXML;
	}
	
	/**
	 * Clean manually.
	 * 
	 * @param xmlStr
	 *            the xml str
	 * @return the string
	 */
	private String cleanManually(String xmlStr) {
		//using an array of string so it can be referenced by "reference" 
		List<String> strContainer = new ArrayList<String>();
		
		searchAndReplace("<ttext>", "", xmlStr, strContainer);
		searchAndReplace("</ttext>", "", strContainer.get(0), strContainer);
		searchAndReplace("<toAttr>", "<to>", strContainer.get(0), strContainer);
		searchAndReplace("</toAttr>", "</to>", strContainer.get(0), strContainer);
		
		final String[] funcs = {"measurecalcs", "listOfProperties","listOfFunctions","listOfFirst", "listOfSecond", "listOfThird", "listOfLast", 
				"listOfCount", "listOfCountUniqueByDate", "listOfMax", "listOfMin", "listOfNot"};
		
		for (String func : funcs) {
			strContainer.set(0 , strContainer.get(0).replaceAll("<" + func + ">", ""));
			strContainer.set(0 , strContainer.get(0).replaceAll("</" + func + ">", ""));
		}

		//		searchAndReplace("<listOfCount>", "<listOfCount>", strContainer.get(0), strContainer);
//		searchAndReplace("</listOfCount>", "</listOfCount>", strContainer.get(0), strContainer);
		
		searchAndReplace("<listOfCountUniqueByDate>", "<listOfCountUniqueByDate>", strContainer.get(0), strContainer);
		searchAndReplace("</listOfCountUniqueByDate>", "</listOfCountUniqueByDate>", strContainer.get(0), strContainer);
		
		searchAndReplace("<listOfFirst>[\\s\\t\\n]*<listOfFirst>", "<listOfFirst>", strContainer.get(0), strContainer);
		searchAndReplace("<listOfFirst>[\\s\\t\\n]*<listOfFirst", "<listOfFirst", strContainer.get(0), strContainer);
		searchAndReplace("</listOfFirst>[\\s\\t\\n]*</listOfFirst>", "</listOfFirst>", strContainer.get(0), strContainer);
		
		searchAndReplace("<listOfSecond>[\\s\\t\\n]*<listOfSecond>", "<listOfSecond>", strContainer.get(0), strContainer);
		searchAndReplace("<listOfSecond>[\\s\\t\\n]*<listOfSecond", "<listOfSecond", strContainer.get(0), strContainer);
		searchAndReplace("</listOfSecond>[\\s\\t\\n]*</listOfSecond>", "</listOfSecond>", strContainer.get(0), strContainer);
		
		searchAndReplace("<listOfThird>[\\s\\t\\n]*<listOfThird>", "<listOfThird>", strContainer.get(0), strContainer);
		searchAndReplace("<listOfThird>[\\s\\t\\n]*<listOfThird", "<listOfThird", strContainer.get(0), strContainer);
		searchAndReplace("</listOfThird>[\\s\\t\\n]*</listOfThird>", "</listOfThird>", strContainer.get(0), strContainer);
		
		searchAndReplace("<listOfLast>[\\s\\t\\n]*<listOfLast>", "<listOfLast>", strContainer.get(0), strContainer);
		searchAndReplace("<listOfLast>[\\s\\t\\n]*<listOfLast", "<listOfLast", strContainer.get(0), strContainer);
		searchAndReplace("</listOfLast>[\\s\\t\\n]*</listOfLast>", "</listOfLast>", strContainer.get(0), strContainer);

		searchAndReplace("<listOfMin>[\\s\\t\\n]*<listOfMin>", "<listOfMin>", strContainer.get(0), strContainer);
		searchAndReplace("<listOfMin>[\\s\\t\\n]*<listOfMin", "<listOfMin", strContainer.get(0), strContainer);
		searchAndReplace("</listOfMin>[\\s\\t\\n]*</listOfMin>", "</listOfMin>", strContainer.get(0), strContainer);

		searchAndReplace("<listOfMax>[\\s\\t\\n]*<listOfMax>", "<listOfMax>", strContainer.get(0), strContainer);
		searchAndReplace("<listOfMax>[\\s\\t\\n]*<listOfMax", "<listOfMax", strContainer.get(0), strContainer);
		searchAndReplace("</listOfMax>[\\s\\t\\n]*</listOfMax>", "</listOfMax>", strContainer.get(0), strContainer);

		searchAndReplace("<listOfNot>[\\s\\t\\n]*<listOfNot>", "<listOfNot>", strContainer.get(0), strContainer);
		searchAndReplace("<listOfNot>[\\s\\t\\n]*<listOfNot", "<listOfNot", strContainer.get(0), strContainer);
		searchAndReplace("</listOfNot>[\\s\\t\\n]*</listOfNot>", "</listOfNot>", strContainer.get(0), strContainer);
		searchAndReplace("<and>[\\s\\t\\n]*<and/>[\\s\\t\\n]*</and>", "<and></and>", strContainer.get(0), strContainer);
		
		/*
		 * commenting extraneous <and></and> processing because case of:
		 * <and><and rel="SBS" ...></and><and>
		 * ...
		 * <and><and>...</and><and>
		 * results in the trimming of a valid tag
		 */
//		boolean replaced = false;
//		replaced = searchAndReplace("</and>[\\s\\t\\n]*</and>", "</and>", strContainer.get(0), strContainer);
//		if (replaced) {
//			searchAndReplace("<and>[\\s\\t\\n]*<and>", "<and>", strContainer.get(0), strContainer);
//		}
		
//		replaced = false;
//		replaced = searchAndReplace("</or>[\\s\\t\\n]*</or>", "</or>", strContainer.get(0), strContainer);
//		if (replaced) {
//			searchAndReplace("<or>[\\s\\t\\n]*<or", "<or", strContainer.get(0), strContainer);
//		}
		
////does not work 100%
//use manual search and replace for <qdsel>		
		
//		replaced = searchAndReplace("<qdsel>[\\s\\t\\n]*<id>", "", strContainer.get(0), strContainer);
//		if (replaced) {
//			searchAndReplace("</id>[\\s\\t\\n]*</qdsel>", "", strContainer.get(0), strContainer);
//		}
//		
//		searchAndReplace("</or>[\\s\\t\\n]*</qdsel>", "", strContainer.get(0), strContainer);
//		searchAndReplace("<qdsel>[\\s\\t\\n]*<or>", "", strContainer.get(0), strContainer);
//		
//		searchAndReplace("<reference>[\\s\\t\\n]*<qdsel>", "<reference>", strContainer.get(0), strContainer);
//		searchAndReplace("</qdsel>[\\s\\t\\n]*</reference>", "</reference>", strContainer.get(0), strContainer);

		return strContainer.get(0);
	}
	
	/**
	 * Search and replace.
	 * 
	 * @param search
	 *            the search
	 * @param replace
	 *            the replace
	 * @param inString
	 *            the in string
	 * @param strContainer
	 *            the str container
	 * @return true, if successful
	 */
	private boolean searchAndReplace(String search, String replace, String inString, List<String> strContainer) {
		
		Pattern rangePattern;
		rangePattern = Pattern.compile(search);
		Matcher m = rangePattern.matcher(inString);
		boolean x = m.find();
		String temp = m.replaceAll(replace);
		strContainer.clear();
		strContainer.add(temp);
				 
		return x;
	}
	
	/**
	 * Sets the attributes.
	 * 
	 * @param cls
	 *            the cls
	 * @param xstream
	 *            the xstream
	 */
	private void setAttributes(Class cls, XStream xstream) {
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			if (field.getType().getName().contains("String")) {
				if (field.getName().contains("Attr")) {
					xstream.useAttributeFor( cls, field.getName());
					xstream.aliasAttribute(field.getName().substring(0, field.getName().length()-4), field.getName());
				} else if (field.getName().contains("ttext")) {
					//do nothing
				} else {
					xstream.useAttributeFor( cls, field.getName());
				}
			}
		}
	}
	
	/**
	 * Sets the alias.
	 * 
	 * @param cls
	 *            the cls
	 * @param xstream
	 *            the xstream
	 */
	private void setAlias(Class cls, XStream xstream) {
		xstream.alias(convertFirstCharactertoLowercase(cls.getSimpleName()), cls);
	}
	/*
	 * This method just converts the first letter in the class name to lowercase, since in transform
	 * the measurepopulation has been named as measurePopulation and measureobservation has been named
	 * as measureObservation.
	 */
	/**
	 * Convert first characterto lowercase.
	 * 
	 * @param name
	 *            the name
	 * @return the string
	 */
	private String convertFirstCharactertoLowercase(String name){
		String firstCharacter = name.substring(0,1);
		String remainingCharacters = name.substring(1, name.length());
		return firstCharacter.toLowerCase() + remainingCharacters;
	}
	
	/**
	 * Removes the collection tags.
	 * 
	 * @param cls
	 *            the cls
	 * @param xstream
	 *            the xstream
	 */
	private void removeCollectionTags(Class cls,  XStream xstream) {
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			if (field.getType().getName().contains("List")) {
				xstream.useAttributeFor( cls, field.getName());
				xstream.addImplicitCollection(cls, field.getName());
			}
		}
	}
	
	/**
	 * Gets the classes.
	 * 
	 * @param cls
	 *            the cls
	 * @param classList
	 *            the class list
	 * @return the classes
	 */
	private void getClasses(Class cls, List<Class<?>> classList) {
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			if (!field.getType().getName().contains("String")) {
				Class<?> c = null;
				try {
					if (field.getType().getName().equalsIgnoreCase("java.util.List")) {
						//this assumes that the list contains a list of the child of then same name
						String temp = cls.getName();
						int len = temp.length();
						try {
						c = Class.forName(temp.substring(0, len-1));
						}
						catch (ClassNotFoundException cnfe){
							c = Class.forName(temp);
							
						}
					} else {
						c = Class.forName(field.getType().getName());
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				if (c!=null) {
					if (!isInList(classList, c.getName())) {
						classList.add(c);
						getClasses(c, classList);
						
					}
				}
			}
		}
		
		if ( (cls.getSuperclass() != null && 
				!cls.getSuperclass().getPackage().getName().startsWith("java"))) {
				getClasses(cls.getSuperclass(), classList);
		}
	}
	
	/**
	 * Checks if is in list.
	 * 
	 * @param list
	 *            the list
	 * @param key
	 *            the key
	 * @return true, if is in list
	 */
	private boolean isInList(List<Class<?>> list, String key) {
		for (Class<?> c : list) {
			if (c.getName().equalsIgnoreCase(key)) {
				return true;
			}
		}
		return false;
	}
}
