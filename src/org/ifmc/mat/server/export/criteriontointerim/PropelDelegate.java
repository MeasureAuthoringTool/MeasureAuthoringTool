package org.ifmc.mat.server.export.criteriontointerim;

import java.util.List;

import org.ifmc.mat.dao.clause.AttributeDetailsDAO;
import org.ifmc.mat.model.QualityDataSet;
import org.ifmc.mat.model.clause.AttributeDetails;
import org.ifmc.mat.simplexml.model.Propel;
import org.ifmc.mat.simplexml.model.Property;
import org.springframework.context.ApplicationContext;

/**
 * Property and Propel related helper methods 
 * @author aschmidt
 *
 */
public class PropelDelegate {
	/**
	 * 1. find the rule instance using property.
	 * 2. Load property details.
	 * 3. generate fields.
	 * 4. if a property uses code list, load code list details.
	 * 5. create propel.
	 * @param property
	 * @param codelist
	 * @param context
	 * @return
	 */
	public static Propel generatePropel(Property property, QualityDataSet codelist, ApplicationContext context) {

		AttributeDetailsDAO attrDetailsDAO = (AttributeDetailsDAO) context.getBean("attributeDetailsDAO");
		AttributeDetails details = attrDetailsDAO.findByName(property.getName());
		Propel prop = new Propel();

		prop.setId(property.getValue());

		if (codelist != null) {
			prop.setName(codelist.getListObject().getName());
			prop.setOid(codelist.getListObject().getOid());
			prop.setTaxonomy(codelist.getListObject().getCodeSystem().getDescription());
		} else {
			prop.setName("is present");
			prop.setOid("");
			prop.setTaxonomy("");
		}

		prop.setDatatype(property.getName());
		prop.setCode(details.getCode());
		prop.setCodeSystem(details.getCodeSystem());
		prop.setCodeSystemName(details.getCodeSystemName());
		prop.setMode(details.getMode());
		prop.setTypeCode(details.getTypeCode());
		return prop;
	}
	
	/**
	 * perform check to see if this propel has already been generated
	 * @param codelist
	 * @param p
	 * @return null if not found else existing propel id
	 */
	public static String getExistingAttribute(Property property, QualityDataSet codelist, List<Propel> propels){
		String propertyValue = null;
		if(codelist != null){
			String name = codelist.getListObject().getName();
			String oid = codelist.getListObject().getOid();
			String taxonomy = codelist.getListObject().getCodeSystem().getDescription();
			String datatype = property.getName();
			for(Propel propel : propels){
				 if(propel.getName().equalsIgnoreCase(name) &&
					propel.getOid().equalsIgnoreCase(oid) && 
					propel.getTaxonomy().equalsIgnoreCase(taxonomy) &&
					propel.getDatatype().equalsIgnoreCase(datatype)
					){
					 propertyValue = propel.getId();
					 break;
				 }
			}
		}
		return propertyValue;
	}
}
