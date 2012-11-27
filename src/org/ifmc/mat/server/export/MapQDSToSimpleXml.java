package org.ifmc.mat.server.export;

import java.util.Collection;

import org.ifmc.mat.model.QualityDataSet;
import org.ifmc.mat.simplexml.model.Elementlookup;
import org.ifmc.mat.simplexml.model.Qdsel;

public class MapQDSToSimpleXml {

	public Elementlookup mapQualityDataSetsToElementlookup(Collection<QualityDataSet> qdses){
		Elementlookup el = new Elementlookup();
		for(QualityDataSet qds : qdses){
			if (qds!=null) {
				el.getQdsels().add(mapQualityDataSetToQDSEL(qds));
			}
		}
		return el;
	}
	
	public Elementlookup mapQualityDataSetsToElementlookup(Collection<QualityDataSet> qdses, Elementlookup el){
		for(QualityDataSet qds : qdses){
			el.getQdsels().add(mapQualityDataSetToQDSEL(qds));
		}
		return el;
	}
	
	private Qdsel mapQualityDataSetToQDSEL(QualityDataSet qds){
		Qdsel qdsel = null;
		try {
//			qds.getQualityDataElement().getStandardDataElement();
//			QualityDataElement qde = qds.getQualityDataElement();
//			StandardDataElement sde = qde != null ? qde.getStandardDataElement() : null;
//			QualityDataType qdt = qde != null ? qde.getQualityDataType() : null;
			
//			qdsel = new Qdsel();
//			
//			qdsel.setIdAttr(qds.getSequenceNumber());
//			qdsel.setName(sde != null ? sde.getDataElementName(): null);
//			qdsel.setDatatype(qdt != null ? qdt.getDataType() : null);
//			qdsel.setOid(sde != null ? sde.getOid() : null);
//			qdsel.setUuid(qds.getUuid());
//			qdsel.setTaxonomy(sde != null ? sde.getCodeSet() : null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return qdsel;
	}
}
