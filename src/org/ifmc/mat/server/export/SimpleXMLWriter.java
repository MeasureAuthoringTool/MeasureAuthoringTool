package org.ifmc.mat.server.export;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ifmc.mat.model.Author;
import org.ifmc.mat.model.MeasureType;
import org.ifmc.mat.server.service.impl.XMLUtility;
import org.ifmc.mat.simplexml.model.Attachment;
import org.ifmc.mat.simplexml.model.Count;
import org.ifmc.mat.simplexml.model.CountUniqueByDate;
import org.ifmc.mat.simplexml.model.Criterion;
import org.ifmc.mat.simplexml.model.Denominator;
import org.ifmc.mat.simplexml.model.Exceptions;
import org.ifmc.mat.simplexml.model.Exclusions;
import org.ifmc.mat.simplexml.model.First;
import org.ifmc.mat.simplexml.model.Function;
import org.ifmc.mat.simplexml.model.FunctionHolder;
import org.ifmc.mat.simplexml.model.Iqdsel;
import org.ifmc.mat.simplexml.model.Last;
import org.ifmc.mat.simplexml.model.LogicOp;
import org.ifmc.mat.simplexml.model.Max;
import org.ifmc.mat.simplexml.model.Measure;
import org.ifmc.mat.simplexml.model.MeasureObservation;
import org.ifmc.mat.simplexml.model.MeasurePopulation;
import org.ifmc.mat.simplexml.model.Min;
import org.ifmc.mat.simplexml.model.MinMax;
import org.ifmc.mat.simplexml.model.Not;
import org.ifmc.mat.simplexml.model.NqfId;
import org.ifmc.mat.simplexml.model.Numerator;
import org.ifmc.mat.simplexml.model.NumeratorExclusions;
import org.ifmc.mat.simplexml.model.Period;
import org.ifmc.mat.simplexml.model.Population;
import org.ifmc.mat.simplexml.model.Propel;
import org.ifmc.mat.simplexml.model.Property;
import org.ifmc.mat.simplexml.model.Second;
import org.ifmc.mat.simplexml.model.Stratification;
import org.ifmc.mat.simplexml.model.Third;
import org.ifmc.mat.simplexml.model.Verifier;

import com.thoughtworks.xstream.XStream;

public class SimpleXMLWriter {
	
	private static final String CONVERSION_FILE_FILTER="xsl/filter.xsl";
	
	public String toXML(Measure measure) {
		XStream xstream = new XStream();
		setAlias(measure.getClass(), xstream);

		List<Class<?>> classList = new ArrayList<Class<?>>();
		getClasses(Measure.class, classList);
		classList.add(Propel.class);
		classList.add(Iqdsel.class);//This line makes the iqdsel tag to appear as part of ElementLookUp
		
		for (Class<?> c : classList) {
			removeCollectionTags(c, xstream);
			setAlias(c, xstream);
			setAttributes(c, xstream);
		}
		
		setManually(xstream);
		String xmlStr = xstream.toXML(measure);
		xmlStr = cleanManually(xmlStr);
		xmlStr = filterXSL(xmlStr);
		return xmlStr;
	}
	
	private void setManually(XStream xstream) {
		setAttributes(LogicOp.class, xstream);
		removeCollectionTags(LogicOp.class, xstream);
		removeCollectionTags(FunctionHolder.class, xstream);
		setAlias(Count.class, xstream);
		setAttributes(Count.class, xstream);

		setAlias(Not.class, xstream);
		setAlias(Last.class, xstream);
		setAlias(First.class, xstream);
		setAlias(Second.class, xstream);
		setAlias(Third.class,xstream);

		setAttributes(MinMax.class, xstream);
		xstream.omitField(MinMax.class, "funcName");
		xstream.omitField(First.class, "funcName");
		
		setAlias(Min.class, xstream);
		
		xstream.useAttributeFor(Criterion.class, "uuid");
		
		setAttributes(CountUniqueByDate.class, xstream);
		setAlias(CountUniqueByDate.class, xstream);
		xstream.omitField(CountUniqueByDate.class, "funcName");
		xstream.addImplicitCollection(FunctionHolder.class, "listOfCountUniqueByDate");
		xstream.omitField(Count.class, "funcName");
		setAlias(Author.class, xstream);
		setAlias(MeasureType.class, xstream);
//		setAlias(Function.class,xstream);
		setAlias(Attachment.class, xstream);
		setAlias(Property.class, xstream);
		setAlias(Numerator.class, xstream);
		setAlias(NumeratorExclusions.class, xstream);
		setAlias(Denominator.class, xstream);
		setAlias(Population.class, xstream);
		setAlias(Exceptions.class, xstream);
		setAlias(Exclusions.class, xstream);
		setAlias(MeasurePopulation.class, xstream);
		setAlias(MeasureObservation.class, xstream);
		setAlias(Stratification.class, xstream);
		setAlias(Verifier.class, xstream);
		setAlias(Max.class,xstream);
		setAlias(Min.class,xstream);
		setAlias(NqfId.class,xstream);
		xstream.alias("measurecalc", Function.class);
		xstream.useAttributeFor(Property.class, "name");
		xstream.useAttributeFor(Property.class, "value");
		xstream.useAttributeFor(Function.class, "lowinclusive");
		xstream.useAttributeFor(Function.class, "lownum");
		xstream.useAttributeFor(Function.class, "lowunit");
		xstream.useAttributeFor(Function.class, "highinclusive");
		xstream.useAttributeFor(Function.class, "highnum");
		xstream.useAttributeFor(Function.class, "highunit");
		xstream.useAttributeFor(Function.class, "equalnum");
		xstream.useAttributeFor(Function.class, "equalunit");
		xstream.useAttributeFor(Function.class, "equalnegationind");
		xstream.useAttributeFor(Function.class, "name");
		xstream.useAttributeFor(Function.class, "datatype");
		xstream.useAttributeFor(Function.class, "uuid");
		xstream.useAttributeFor(Function.class, "origText");
		xstream.useAttributeFor(Function.class, "idAttr");
		xstream.useAttributeFor(Function.class, "value");
		xstream.useAttributeFor(Function.class, "refid");
		
		xstream.useAttributeFor(NqfId.class, "root");
		xstream.useAttributeFor(NqfId.class, "extension");

		xstream.useAttributeFor(Period.class, "uuid");
		
		xstream.useAttributeFor(Attachment.class, "uuid");
		xstream.useAttributeFor(Attachment.class, "title");
		xstream.useAttributeFor(Attachment.class, "clause");
		
	}
	private String filterXSL(String xmlStr) {
		XMLUtility xmlUtility = new XMLUtility();
		String tempXML = xmlUtility.applyXSL(xmlStr, xmlUtility.getXMLResource(CONVERSION_FILE_FILTER));
		return tempXML;
	}
	
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
	
	private void setAlias(Class cls, XStream xstream) {
		xstream.alias(convertFirstCharactertoLowercase(cls.getSimpleName()), cls);
	}
	/*
	 * This method just converts the first letter in the class name to lowercase, since in transform
	 * the measurepopulation has been named as measurePopulation and measureobservation has been named
	 * as measureObservation.
	 */
	private String convertFirstCharactertoLowercase(String name){
		String firstCharacter = name.substring(0,1);
		String remainingCharacters = name.substring(1, name.length());
		return firstCharacter.toLowerCase() + remainingCharacters;
	}
	
	private void removeCollectionTags(Class cls,  XStream xstream) {
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			if (field.getType().getName().contains("List")) {
				xstream.useAttributeFor( cls, field.getName());
				xstream.addImplicitCollection(cls, field.getName());
			}
		}
	}
	
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
	private boolean isInList(List<Class<?>> list, String key) {
		for (Class<?> c : list) {
			if (c.getName().equalsIgnoreCase(key)) {
				return true;
			}
		}
		return false;
	}
}
