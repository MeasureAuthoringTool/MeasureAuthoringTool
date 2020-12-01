<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:exsl="http://exslt.org/common"
   xmlns:uuid="java:java.util.UUID" xmlns:math="http://exslt.org/math" xmlns="urn:hl7-org:v3"
   xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:msxsl="urn:schemas-microsoft-com:xslt"
   extension-element-prefixes="exsl uuid math xs" 
   exclude-result-prefixes="exsl uuid math xs msxsl">
   <xsl:output method="xml" indent="yes" encoding="ISO-8859-1"/>
   	<!-- read pattern xml into a lookup table -->
	<xsl:variable name="the_tidrootMapping" select="document('tidrootMapping.xml')"/>
	<xsl:variable name="the_healthRecordMapping" select="document('healthRecordMapping.xml')"/>
	<xsl:variable name="the_qdmAttributeMapping" select="document('attribute_details.xml')"/>
   	<xsl:preserve-space elements="content"/>
   	<xsl:include href="qds_datatype_patterns_comparison_templates.xsl"/>
   	<xsl:include href="qds_datatype_patterns_src_of_template.xsl"/>
   	<xsl:include href="qds_datatype_patterns_obs_val_template.xsl"/>
   	<xsl:include href="qds_datatype_patterns_dc_measurecalc_template.xsl"/>
   	<xsl:include href="qds_datatype_patterns_to_template.xsl"/>
	<xsl:include href="qds_datatype_patterns_derivation_expression.xsl"/>
	<xsl:include href="qds_datatype_patterns_title_template.xsl"/>

	<xsl:template match="elementRef|qdm|qdsel|iqdsel">
      <xsl:param name="conj"/>
      <xsl:param name="process_Neg_RatId"/>

	<xsl:variable name="refid">
      	<xsl:choose>
	      	<!-- <xsl:when test="name(..)='reference' and ../../@id"><xsl:value-of select="current()/@id"/></xsl:when> -->
	      	<xsl:when test="string-length($process_Neg_RatId) > 0">
	      		<xsl:value-of select="$process_Neg_RatId"/>
	      	</xsl:when>
	      	<xsl:otherwise>NA</xsl:otherwise>
      	</xsl:choose>
      </xsl:variable>

	<xsl:variable name="iid">
		<xsl:value-of select="@id"/>
	</xsl:variable>		

      <xsl:variable name="qdsid">
      	<xsl:choose>
	      	<xsl:when test="name(..)='reference' and ../../@id"><xsl:value-of select="../../@id"/></xsl:when>
      		<xsl:when test="name(/measure/elementLookUp/*[@id=$iid])='iqdsel'">
      			<xsl:value-of select="/measure/elementLookUp/*[@id=$iid]/@refid"/>
      		</xsl:when>
	      	<xsl:otherwise><xsl:value-of select="current()/@id"/></xsl:otherwise>
	    </xsl:choose>
      </xsl:variable>
      
      <xsl:variable name="qdsuuid">
      	<xsl:choose>
	      	<xsl:when test="name(/measure/elementLookUp/*[@id=$iid])='iqdsel'">
	      		<xsl:value-of select="/measure/elementLookUp/*[@id=$iid]/@uuid"/>
	      	</xsl:when>
      		<!--<xsl:when test="name(..)='supplementalDataElements'">
      			<xsl:value-of select="/measure/supplementalDataElements/*[@id=$qdsid]/@uuid"/>
      		</xsl:when>-->
      		<xsl:when test="$refid != 'NA'"><xsl:value-of select="$refid"/></xsl:when>
      		<xsl:otherwise>
         		<xsl:value-of select="/measure/elementLookUp/*[@id=$qdsid]/@uuid"/>
      		</xsl:otherwise>
      	</xsl:choose>
      </xsl:variable>
      <xsl:variable name="qdsdatatype">
		<!--<xsl:choose>-->
			<!--<xsl:when test="name(..)='supplementalDataElements'">
				<xsl:value-of select="/measure/supplementalDataElements/*[@id=$qdsid]/@datatype"/>
			</xsl:when>-->
			<!--<xsl:otherwise>-->
		         <xsl:value-of select="/measure/elementLookUp/*[@id=$qdsid]/@datatype"/>
			<!--</xsl:otherwise>
		</xsl:choose>-->
      </xsl:variable>
      <xsl:variable name="qdsdef">
		<xsl:copy-of select="/measure/elementLookUp/*[@id=$qdsid]"/>  
      </xsl:variable>
      <xsl:variable name="qdsname">
      	<!--<xsl:choose>-->
      		<!--<xsl:when test="name(..)='supplementalDataElements'">
      			<xsl:value-of select="/measure/supplementalDataElements/*[@id=$qdsid]/@name"/>
      		</xsl:when>-->
      		<!--<xsl:otherwise>-->
      			<xsl:value-of select="/measure/elementLookUp/*[@id=$qdsid]/@name"/>
      		<!--</xsl:otherwise>
      	</xsl:choose>-->
      </xsl:variable>
      <xsl:variable name="qdsoid">
      	<!--<xsl:choose>-->
      		<!--<xsl:when test="name(..)='supplementalDataElements'">
      			<xsl:value-of select="/measure/supplementalDataElements/*[@id=$qdsid]/@oid"/>
      		</xsl:when>-->
      		<!--<xsl:otherwise>-->
      			<xsl:value-of select="/measure/elementLookUp/*[@id=$qdsid]/@oid"/>
      		<!--</xsl:otherwise>
      	</xsl:choose>-->
      </xsl:variable>
      <xsl:variable name="qdsdisplayname">
		<xsl:choose>
			<!--<xsl:when test="name(..)='supplementalDataElements'">
				<xsl:value-of select="/measure/supplementalDataElements/*[@id=$qdsid]/@name"/><xsl:text> </xsl:text><xsl:value-of select="/measure/supplementalDataElements/*[@id=$qdsid]/@taxonomy"/><xsl:text> Value Set</xsl:text>
			</xsl:when>-->
			<xsl:when test="/measure/elementLookUp/*[@id=$qdsid]/@taxonomy='GROUPING'">
				<xsl:value-of select="/measure/elementLookUp/*[@id=$qdsid]/@name"/><xsl:text> Value Set </xsl:text><xsl:value-of select="/measure/elementLookUp/*[@id=$qdsid]/@taxonomy"/>
			</xsl:when>
			<xsl:when test="/measure/elementLookUp/*[@id=$qdsid]/@taxonomy">
        		<xsl:value-of select="/measure/elementLookUp/*[@id=$qdsid]/@name"/><xsl:text> </xsl:text><xsl:value-of select="/measure/elementLookUp/*[@id=$qdsid]/@taxonomy"/><xsl:text> Value Set</xsl:text>
        	</xsl:when>
        </xsl:choose>
      </xsl:variable>
      <xsl:variable name="qdsdatatypemode">
         <xsl:value-of select="translate($qdsdatatype, ' ', '_')"/>
      </xsl:variable>

      <xsl:variable name="refdatatype">
         <xsl:value-of select="/measure/elementLookUp/*[@id=$refid]/@datatype"/>
      </xsl:variable>
      <xsl:variable name="refdef">
         <xsl:copy-of select="/measure/elementLookUp/*[@id=$refid]"/>
      </xsl:variable>
      <xsl:variable name="refname">
         <xsl:value-of select="/measure/elementLookUp/*[@id=$refid]/@name"/>
      </xsl:variable>
      <xsl:variable name="refoid">
         <xsl:value-of select="/measure/elementLookUp/*[@id=$refid]/@oid"/>
      </xsl:variable>
      <xsl:variable name="refdisplayname">
      	<xsl:choose>
	      	<xsl:when test="/measure/elementLookUp/*[@id=$refid]/@taxonomy='GROUPING'">
	      		<xsl:value-of select="/measure/elementLookUp/*[@id=$refid]/@name"/>
	      		<xsl:text> Value Set </xsl:text>
	      		<xsl:value-of select="/measure/elementLookUp/*[@id=$refid]/@taxonomy"/>
	      	</xsl:when>
	      	<xsl:when test="/measure/elementLookUp/*[@id=$refid]/@taxonomy">
	      		<xsl:value-of select="/measure/elementLookUp/*[@id=$refid]/@name"/>
	      		<xsl:text> </xsl:text>
	      		<xsl:value-of select="/measure/elementLookUp/*[@id=$refid]/@taxonomy"/><xsl:text> Value Set</xsl:text>
	      	</xsl:when>
      	</xsl:choose>      	
      </xsl:variable>
	
	<xsl:variable name="isRHS_Of_RelationalOp">
    	<xsl:if test="(parent::relationalOp) and (count(preceding-sibling::*) = 1)">true</xsl:if>
    </xsl:variable>
	
    <xsl:variable name="isNot">
       <xsl:if test="$isRHS_Of_RelationalOp != 'true'">
       		<xsl:apply-templates select="." mode="isChildOfNot"/>
       </xsl:if>
    </xsl:variable>
    
    <xsl:variable name="arit_text">
      	<xsl:apply-templates select="." mode="inline_arit_text"/>
	</xsl:variable>
	
      <xsl:variable name="title">
      	<xsl:call-template name="title">
      		<xsl:with-param name="ref-id"><xsl:value-of select="$refid"/></xsl:with-param>
      	</xsl:call-template>
     </xsl:variable>
	
   	<xsl:variable name="rel">
   		<xsl:choose>
   			<xsl:when test="@rel='SBOD'">EAS</xsl:when>
   			<xsl:when test="@rel='EBOD'">EAE</xsl:when>
   			<xsl:otherwise>
   				<xsl:value-of select="@rel"/>
   			</xsl:otherwise>
   		</xsl:choose>
   	</xsl:variable>
   	<xsl:variable name="rel_to_uuid">
		<xsl:if test="current()/@to">
			<xsl:value-of select="/measure/elementLookUp/*[@id=current()/@to]/@uuid"/>
		</xsl:if>
	</xsl:variable>
	
	<xsl:variable name="tid-root">
		<xsl:choose>
			<xsl:when test="$refid != 'NA'">
				<xsl:variable name="negDataTyp"><xsl:value-of select="concat($qdsdatatype,' not done')"/></xsl:variable>
				<xsl:value-of select="$the_tidrootMapping/PatternMapping/pattern[@dataType=lower-case($negDataTyp)]/@root"/>
	    	</xsl:when>
			<xsl:when test="$the_tidrootMapping/PatternMapping/pattern[@dataType=lower-case($qdsdatatype)]">
				<xsl:value-of select="$the_tidrootMapping/PatternMapping/pattern[@dataType=lower-case($qdsdatatype)]/@root"/>
			</xsl:when>
		</xsl:choose>
	</xsl:variable>
	
	<xsl:variable name="has_act_code">
		<xsl:apply-templates select="attribute" mode="has_act_code"/>
	</xsl:variable>
	
	<xsl:variable name="actcode">
		<xsl:choose>
			<xsl:when test="contains($has_act_code,'true')">
				<xsl:apply-templates select="attribute" mode="act_code"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>NA</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:choose>
		<xsl:when test="lower-case($qdsdatatype)='derivation expression'">
			<xsl:call-template name="derivation_expression">
				<xsl:with-param name="qdsuuid" select="$qdsuuid"/>
			</xsl:call-template>
		</xsl:when>
		<xsl:otherwise>
   		<act classCode="ACT" moodCode="EVN" isCriterionInd="true">
   		
	    <!-- when not done or not -->
	    <xsl:if test="($isNot='true' or (child::attribute[@name = 'negation rationale']))">
	    	<xsl:attribute name="actionNegationInd">true</xsl:attribute>
	    	<!-- only when not done -->
	    	<xsl:if test="not($isNot='true')">
	    		<xsl:attribute name="notDoneInd">true</xsl:attribute>
	    	</xsl:if>
	    </xsl:if>
		<xsl:comment><xsl:text> </xsl:text><xsl:value-of select="$qdsdatatype"/> pattern </xsl:comment>

         <xsl:choose>
            <xsl:when test="/measure/elementLookUp/*[@id=$qdsid]">
            </xsl:when>
         	<xsl:when test="/measure/supplementalDataElements/*[@id=$qdsid]">
         	</xsl:when>
            <xsl:otherwise>
               <ERROR>
                  <TYPE>missing qdsel</TYPE>                  
                  <SOURCE><xsl:copy-of select="."/></SOURCE>               
                  <DESCRIPTION>No qds element in the elementLookUp section of the simple XML
                     matching ID of "<xsl:value-of select="$qdsid"/>"
                  </DESCRIPTION>
               </ERROR>
            </xsl:otherwise>
         </xsl:choose>
		 <xsl:choose>
			
		 	<!--Patient Characteristic-->
		 	<xsl:when test="lower-case($qdsdatatype)='patient characteristic'">
		 		<templateId root="{$tid-root}"/>  
		 		<id root="{$qdsuuid}"/> 
		 		<sourceOf typeCode="COMP">
		 			<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
		 				<code code="{$qdsoid}"  codeSystem="2.16.840.1.113883.3.560.101.1" displayName="{$qdsdisplayname}"/>
		 				<title><xsl:value-of select="$title"/></title>
		 				<xsl:call-template name="handleAttributesAndRHS"/>
		 			</observation>
		 		</sourceOf>
		 	</xsl:when>
			
		 	<!--Patient Characteristic Birthdate-->
		 	<xsl:when test="lower-case($qdsdatatype)='patient characteristic birthdate'">
		 		<templateId root="{$tid-root}"/>  
		 		<id root="{$qdsuuid}"/> 
		 		<code code="45970-1" displayName="Demographics" codeSystem="2.16.840.1.113883.6.1"/>
		 		<sourceOf typeCode="COMP">
		 			<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
		 				<code code="21112-8" codeSystem="2.16.840.1.113883.6.1" displayName="Birth date"/>
		 				<title><xsl:value-of select="$title"/></title>
		 				<value xsi:type="CD" code="{$qdsoid}" codeSystem="2.16.840.1.113883.3.560.101.1" displayName="{$qdsdisplayname}" />
		 				<xsl:call-template name="handleAttributesAndRHS"/>
		 			</observation>
		 		</sourceOf>
		 	</xsl:when>
		 	
		 	<!--Patient Characteristic Clinical Trial Participant-->
		 	<xsl:when test="lower-case($qdsdatatype)='patient characteristic clinical trial participant'">
		 		<templateId root="{$tid-root}"/>  
		 		<id root="{$qdsuuid}"/> 
		 		<sourceOf typeCode="COMP">
		 			<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
		 				<code code="428024001" codeSystem="2.16.840.1.113883.6.96" displayName="Clinical Trial Participant"/>
		 				<title><xsl:value-of select="$title"/></title>
		 				<value xsi:type="CD" code="{$qdsoid}" codeSystem="2.16.840.1.113883.3.560.101.1" displayName="{$qdsdisplayname}" />
		 				<xsl:call-template name="handleAttributesAndRHS"/>
		 			</observation>
		 		</sourceOf>
		 	</xsl:when>
		 	
		 	<!--Patient Characteristic Gender-->
		 	<xsl:when test="lower-case($qdsdatatype)='patient characteristic gender'">
		 		<templateId root="{$tid-root}"/>  
		 		<id root="{$qdsuuid}"/> 
		 		<code code="45970-1" displayName="Demographics" codeSystem="2.16.840.1.113883.6.1"/>
		 		<sourceOf typeCode="COMP">
		 			<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
		 				<code code="263495000" codeSystem="2.16.840.1.113883.6.96" displayName="Gender"/>
		 				<title><xsl:value-of select="$title"/></title>
		 				<value xsi:type="CD" code="{$qdsoid}" codeSystem="2.16.840.1.113883.3.560.101.1" displayName="{$qdsdisplayname}" />
		 				<xsl:call-template name="handleAttributesAndRHS"/>
		 			</observation>
		 		</sourceOf>
		 	</xsl:when>
		 	
		 	<!--Patient Characteristic Gender-->
		 	<xsl:when test="lower-case($qdsdatatype)='patient characteristic sex'">
		 		<templateId root="{$tid-root}"/>  
		 		<id root="{$qdsuuid}"/> 
		 		<code code="45970-1" displayName="Demographics" codeSystem="2.16.840.1.113883.6.1"/>
		 		<sourceOf typeCode="COMP">
		 			<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
		 				<code code="263495000" codeSystem="2.16.840.1.113883.6.96" displayName="Sex"/>
		 				<title><xsl:value-of select="$title"/></title>
		 				<value xsi:type="CD" code="{$qdsoid}" codeSystem="2.16.840.1.113883.3.560.101.1" displayName="{$qdsdisplayname}" />
		 				<xsl:call-template name="handleAttributesAndRHS"/>
		 			</observation>
		 		</sourceOf>
		 	</xsl:when>
		 	
		 	<!--Patient Characteristic Ethnicity-->
		 	<xsl:when test="lower-case($qdsdatatype)='patient characteristic ethnicity'">
		 		<templateId root="{$tid-root}"/>  
		 		<id root="{$qdsuuid}"/> 
		 		<code code="45970-1" displayName="Demographics" codeSystem="2.16.840.1.113883.6.1"/>
		 		<sourceOf typeCode="COMP">
		 			<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
		 				<code code="54133-4" codeSystem="2.16.840.1.113883.6.1" displayName="Ethnicity"/>
		 				<title><xsl:value-of select="$title"/></title>
		 				<value xsi:type="CD" code="{$qdsoid}" codeSystem="2.16.840.1.113883.3.560.101.1" displayName="{$qdsdisplayname}" />
		 				<xsl:call-template name="handleAttributesAndRHS"/>
		 			</observation>
		 		</sourceOf>
		 	</xsl:when>
		 	
		 	<!--Patient Characteristic Expired-->
		 	<xsl:when test="lower-case($qdsdatatype)='patient characteristic expired'">
		 		<templateId root="{$tid-root}"/>  
		 		<id root="{$qdsuuid}"/> 
		 		<code code="45970-1" displayName="Demographics" codeSystem="2.16.840.1.113883.6.1"/>
		 		<sourceOf typeCode="COMP">
		 			<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
		 				<code code="419099009" codeSystem="2.16.840.1.113883.6.96" displayName="Expired"/>
		 				<title><xsl:value-of select="$title"/></title>
		 				<value xsi:type="CD" code="{$qdsoid}" codeSystem="2.16.840.1.113883.3.560.101.1" displayName="{$qdsdisplayname}" />
		 				<xsl:call-template name="handleAttributesAndRHS"/>
		 			</observation>
		 		</sourceOf>
		 	</xsl:when>
		 	
		 	<!--Patient Characteristic Payer-->
		 	<xsl:when test="lower-case($qdsdatatype)='patient characteristic payer'">
		 		<templateId root="{$tid-root}"/>  
		 		<id root="{$qdsuuid}"/> 
		 		<code code="45970-1" displayName="Demographics" codeSystem="2.16.840.1.113883.6.1"/>
				<sourceOf typeCode="COMP">
				  <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				    <code code="48768-6" codeSystem="2.16.840.1.113883.6.1" displayName="Payer"/>
				    <title><xsl:value-of select="$title"/></title>
				    <value xsi:type="CD" code="{$qdsoid}" codeSystem="2.16.840.1.113883.3.560.101.1" displayName="{$qdsdisplayname}" />
				  	<xsl:call-template name="handleAttributesAndRHS"/>
				  </observation>
				</sourceOf>
		 	</xsl:when>
		 	
		 	<!--Patient Characteristic Race-->
		 	<xsl:when test="lower-case($qdsdatatype)='patient characteristic race'">
		 		<templateId root="{$tid-root}"/>  
		 		<id root="{$qdsuuid}"/> 
		 		<code code="45970-1" displayName="Demographics" codeSystem="2.16.840.1.113883.6.1"/>
				<sourceOf typeCode="COMP">
				  <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				    <code code="32624-9" codeSystem="2.16.840.1.113883.6.1" displayName="Race"/>
				    <title><xsl:value-of select="$title"/></title>
				    <value xsi:type="CD" code="{$qdsoid}" codeSystem="2.16.840.1.113883.3.560.101.1" displayName="{$qdsdisplayname}" />
				  	<xsl:call-template name="handleAttributesAndRHS"/>
				  </observation>
				</sourceOf>
		 	</xsl:when>
		 	
			<!--OLD Patient Characteristic-->
<!--
			<xsl:when test="lower-case($qdsdatatype)='patient characteristic'">
				<xsl:choose>

                  <xsl:when test="$qdsname='birthdate' or $qdsname='birth date'">
					   <templateId root="2.16.840.1.113883.3.560.1.25"/>   
					   <id root="{$qdsuuid}"/>
					   <xsl:choose>
					    <xsl:when test="$actcode='NA'">
					    	<code code="45970-1" displayName="Demographics" codeSystem="2.16.840.1.113883.6.1"/>
					    </xsl:when>
					    <xsl:otherwise>
					    	<xsl:copy-of select="$actcode"/>
					    </xsl:otherwise>
					   </xsl:choose>
					    
					   <sourceOf typeCode="COMP">
					     <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
					     	<code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
							<title><xsl:value-of select="$title"/></title>
							<xsl:apply-templates select="attribute" mode="obs_val"/>
							<xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>   
							<xsl:apply-templates select="." mode="to"/>
					     </observation>
					  </sourceOf>
                  </xsl:when>

               	<xsl:when test="$qdsname='Administrative Gender Female' or $qdsname='Administrative Gender Male' or $qdsname='Administrative Gender Undifferentiated' or $refname='Administrative Gender Female' or $refname='Administrative Gender Male' or $refname='Administrative Gender Undifferentiated'">
               		<templateId root="2.16.840.1.113883.3.560.1.26"/>  
               		<id root="{$qdsuuid}"/>
               		<xsl:choose>
					    <xsl:when test="$actcode='NA'">
					    	<code code="45970-1" displayName="Demographics" codeSystem="2.16.840.1.113883.6.1"/>
					    </xsl:when>
					    <xsl:otherwise>
					    	<xsl:copy-of select="$actcode"/>
					    </xsl:otherwise>
				    </xsl:choose>
               		<sourceOf typeCode="COMP">
               			<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
               				<code code="263495000" codeSystem="2.16.840.1.113883.6.96" displayName="Gender"/>
               				<title><xsl:value-of select="$title"/></title>
               				<value xsi:type="CD" code="{$qdsoid}" displayName="{$qdsdisplayname}" />
               				<xsl:apply-templates select="attribute" mode="obs_val"/>
							<xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
							<xsl:apply-templates select="." mode="to"/>
               			</observation>
               		</sourceOf>
               	</xsl:when>

<xsl:when test="$qdsname='Race'">
   <templateId root="2.16.840.1.113883.3.560.1.300"/>  
   <id root="{$qdsuuid}"/>
   <xsl:choose>
    <xsl:when test="$actcode='NA'">
    	<code code="45970-1" displayName="Demographics" codeSystem="2.16.840.1.113883.6.1"/>
    </xsl:when>
    <xsl:otherwise>
    	<xsl:copy-of select="$actcode"/>
    </xsl:otherwise>
   </xsl:choose>
    
   <sourceOf typeCode="COMP">
     <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
     	<code code="32624-9" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="Race"/>
		<title><xsl:value-of select="$title"/></title>
		<value xsi:type="CD" code="{$qdsoid}" displayName="{$qdsdisplayname}" />
		<xsl:apply-templates select="attribute" mode="obs_val"/>
		<xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>   
		<xsl:apply-templates select="." mode="to"/>
     </observation>
  </sourceOf>
</xsl:when>

<xsl:when test="$qdsname='Ethnicity'">
   <templateId root="2.16.840.1.113883.3.560.1.301"/>  
   <id root="{$qdsuuid}"/>
   <xsl:choose>
    <xsl:when test="$actcode='NA'">
    	<code code="45970-1" displayName="Demographics" codeSystem="2.16.840.1.113883.6.1"/>
    </xsl:when>
    <xsl:otherwise>
    	<xsl:copy-of select="$actcode"/>
    </xsl:otherwise>
   </xsl:choose>
    
   <sourceOf typeCode="COMP">
     <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
     	<code code="54133-4" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="Ethnicity"/>
		<title><xsl:value-of select="$title"/></title>
		<value xsi:type="CD" code="{$qdsoid}" displayName="{$qdsdisplayname}" />
		<xsl:apply-templates select="attribute" mode="obs_val"/>
		<xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>   
		<xsl:apply-templates select="." mode="to"/>
     </observation>
  </sourceOf>
</xsl:when>


<xsl:when test="$qdsname='Gender'">
   <templateId root="2.16.840.1.113883.3.560.1.303"/>  
   <id root="{$qdsuuid}"/>
   <xsl:choose>
    <xsl:when test="$actcode='NA'">
    	<code code="45970-1" displayName="Demographics" codeSystem="2.16.840.1.113883.6.1"/>
    </xsl:when>
    <xsl:otherwise>
    	<xsl:copy-of select="$actcode"/>
    </xsl:otherwise>
   </xsl:choose>
    
   <sourceOf typeCode="COMP">
     <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
     	<code code="263495000" codeSystem="2.16.840.1.113883.6.96" displayName="Gender"/>
		<title><xsl:value-of select="$title"/></title>
		<value xsi:type="CD" code="{$qdsoid}" displayName="{$qdsdisplayname}" />
		<xsl:apply-templates select="attribute" mode="obs_val"/>
		<xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>   
		<xsl:apply-templates select="." mode="to"/>
     </observation>
  </sourceOf>
</xsl:when>

<xsl:when test="$qdsname='Payer'">
   <templateId root="2.16.840.1.113883.3.560.1.303"/>  
   <id root="{$qdsuuid}"/>
   <xsl:choose>
    <xsl:when test="$actcode='NA'">
    	<code code="45970-1" displayName="Demographics" codeSystem="2.16.840.1.113883.6.1"/>
    </xsl:when>
    <xsl:otherwise>
    	<xsl:copy-of select="$actcode"/>
    </xsl:otherwise>
   </xsl:choose>
    
   <sourceOf typeCode="COMP">
     <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
		<code code="48768-6" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="Payer"/>
		<title><xsl:value-of select="$title"/></title>
		<value xsi:type="CD" code="{$qdsoid}" displayName="{$qdsdisplayname}" />
		<xsl:apply-templates select="attribute" mode="obs_val"/>
		<xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>   
		<xsl:apply-templates select="." mode="to"/>
     </observation>
  </sourceOf>
</xsl:when>

				<xsl:otherwise>
					<templateId root="{$tid-root}" /> 
					<id root="{$qdsuuid}" />
					<xsl:choose>
					    <xsl:when test="$actcode='NA'">
					    	<code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
					    </xsl:when>
					    <xsl:otherwise>
					    	<xsl:copy-of select="$actcode"/>
					    </xsl:otherwise>
				    </xsl:choose>
					<sourceOf typeCode="COMP">
						<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
							<code code="{$qdsoid}"  displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
							<title><xsl:value-of select="$title"/></title>
							<xsl:apply-templates select="attribute" mode="obs_val"/>
							<xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
							<xsl:apply-templates select="." mode="to"/>
						</observation>
					</sourceOf>
				</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
-->
			<!--DEVICE RECOMMENDED-->
			<xsl:when test="lower-case($qdsdatatype)='device recommended' or lower-case($qdsdatatype)='device recommended not done' or lower-case($qdsdatatype)='device, recommended' or lower-case($qdsdatatype)='device, recommended not done'">
					 <templateId root="{$tid-root}"/> <!-- device, recommended -->
					 <id root="{$qdsuuid}"/>
					 <xsl:choose>
					    <xsl:when test="$actcode='NA'">
					    	<code code="18776-5" displayName="Plan of care" codeSystem="2.16.840.1.113883.6.1"/>
					    </xsl:when>
					    <xsl:otherwise>
					    	<xsl:copy-of select="$actcode"/>
					    </xsl:otherwise>
				     </xsl:choose>
					 <sourceOf typeCode="COMP">
						<supply classCode="SPLY" moodCode="INT" isCriterionInd="true" >
						<title><xsl:value-of select="$title"/></title>
						
				     	<xsl:apply-templates select="attribute" mode="obs_val_new"/>
						<participant typeCode="DEV">
						  <roleParticipant classCode="MANU">
							  <playingDevice classCode="DEV" determinerCode="KIND">
								  <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
							  </playingDevice>
						  </roleParticipant>
					   </participant>
					   <xsl:for-each select="attribute">
					   		<xsl:apply-templates select="." mode="src_of_new"/>
					   </xsl:for-each>
					   <xsl:if test="$refid!='NA' and $refoid!=''"> 
							<sourceOf typeCode="RSON">
								<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
									<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
									<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
								</observation>
							</sourceOf>
							<!-- <xsl:apply-templates select="../.." mode="to"/> -->
						</xsl:if>
						    
						<xsl:apply-templates select="." mode="to"/>
					  </supply>
				  </sourceOf>
			</xsl:when>

			<!--functional status performed-->
			<xsl:when test="lower-case($qdsdatatype)='functional status performed' or lower-case($qdsdatatype)='functional status performed not done' or lower-case($qdsdatatype)='functional status, performed' or lower-case($qdsdatatype)='functional status, performed not done'">
					<templateId root="{$tid-root}"/> <!-- functional status performed -->
					<id root="{$qdsuuid}"/>
					<xsl:choose>
					    <xsl:when test="$actcode='NA'">
					    	<code code="47420-5" displayName="Functional status assessment" codeSystem="2.16.840.1.113883.6.1"/>
					    </xsl:when>
					    <xsl:otherwise>
					    	<xsl:copy-of select="$actcode"/>
					    </xsl:otherwise>
				     </xsl:choose>
					<sourceOf typeCode="COMP">
						<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
							<code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
							<title><xsl:value-of select="$title"/></title>
							<statusCode code="completed"/>
							
							<xsl:apply-templates select="attribute" mode="obs_val_new"/>
							<xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
							<xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
							    
							<xsl:apply-templates select="." mode="to"/>
						</observation>
					</sourceOf>
			</xsl:when>

			<!--functional status order-->
			<xsl:when test="lower-case($qdsdatatype)='functional status order' or lower-case($qdsdatatype)='functional status order not done' or lower-case($qdsdatatype)='functional status, order' or lower-case($qdsdatatype)='functional status, order not done'">
					<templateId root="{$tid-root}"/> <!-- functional status order -->
					<id root="{$qdsuuid}"/>
					<xsl:choose>
						<xsl:when test="$actcode='NA'">
							<code code="18776-5" codeSystem="2.16.840.1.113883.6.1" displayName="Plan of Care"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:copy-of select="$actcode"/>
						</xsl:otherwise>
					</xsl:choose>
					<sourceOf typeCode="COMP">
						<observation classCode="OBS" moodCode="RQO" isCriterionInd="true">
							<code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
							<title><xsl:value-of select="$title"/></title>
							<statusCode code="completed"/>
							
							<xsl:apply-templates select="attribute" mode="obs_val_new"/>
							<xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
							<xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
							    
							<xsl:apply-templates select="." mode="to"/>
						</observation>
					</sourceOf>
			</xsl:when>

			<!--functional status recommended-->
			<xsl:when test="lower-case($qdsdatatype)='functional status recommended' or lower-case($qdsdatatype)='functional status recommended not done' or lower-case($qdsdatatype)='functional status, recommended' or lower-case($qdsdatatype)='functional status, recommended not done'">
					<templateId root="{$tid-root}"/> <!-- functional status recommended -->
					<id root="{$qdsuuid}"/>
					<xsl:choose>
						<xsl:when test="$actcode='NA'">
							<code code="18776-5" displayName="Plan of care" codeSystem="2.16.840.1.113883.6.1"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:copy-of select="$actcode"/>
						</xsl:otherwise>
					</xsl:choose>
					<sourceOf typeCode="COMP">
						<observation classCode="OBS" moodCode="INT" isCriterionInd="true">
							<code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
							<title><xsl:value-of select="$title"/></title>
							
							<xsl:apply-templates select="attribute" mode="obs_val_new"/>
							<xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
							<xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
							    
							<xsl:apply-templates select="." mode="to"/>
						</observation>
					</sourceOf>
			</xsl:when>

			<!--Functional Status Result-->
			<xsl:when test="lower-case($qdsdatatype)='functional status result' or lower-case($qdsdatatype)='functional status result not done' or lower-case($qdsdatatype)='functional status, result' or lower-case($qdsdatatype)='functional status, result not done'">
				<templateId root="{$tid-root}"/><!-- functional status result pattern-->
				<id root="{$qdsuuid}"/>
				<xsl:choose>
					<xsl:when test="$actcode='NA'">
						<code code="47420-5" displayName="Functional status assessment" codeSystem="2.16.840.1.113883.6.1"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:copy-of select="$actcode"/>
					</xsl:otherwise>
				</xsl:choose>
				<sourceOf typeCode="COMP">
					<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
						<title><xsl:value-of select="$title"/></title>
						<statusCode code="completed"/>
						
						<xsl:apply-templates select="attribute" mode="obs_val_new"/>
						<xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
						<xsl:if test="$refid != 'NA'">
							<sourceOf typeCode="RSON">
								<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
									<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
									<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
								</observation>
							</sourceOf>
						</xsl:if>
						 
						<xsl:apply-templates select="." mode="to"/>
					</observation>
				</sourceOf>
			</xsl:when>

			<!--Intervention Recommended-->
			<xsl:when test="lower-case($qdsdatatype)='intervention recommended' or lower-case($qdsdatatype)='intervention recommended not done' or lower-case($qdsdatatype)='intervention, recommended' or lower-case($qdsdatatype)='intervention, recommended not done'">
					<templateId root="{$tid-root}" />  <!-- intervention recommended Pattern-->
					<id root="{$qdsuuid}"/>
					<xsl:choose>
						<xsl:when test="$actcode='NA'">
							<code code="18776-5" displayName="Plan of care" codeSystem="2.16.840.1.113883.6.1"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:copy-of select="$actcode"/>
						</xsl:otherwise>
					</xsl:choose>
					<sourceOf typeCode="COMP">
						<procedure classCode="PROC" moodCode="INT" isCriterionInd="true">
							 <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
							 <title><xsl:value-of select="$title"/></title>
							 
							 <xsl:apply-templates select="attribute" mode="obs_val_new"/>
							 <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
							 <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
							    
							<xsl:apply-templates select="." mode="to"/>
						</procedure>
					</sourceOf>
			</xsl:when>

			<!-- laboratory test recommended -->
			<xsl:when test="lower-case($qdsdatatype)='laboratory test recommended' or lower-case($qdsdatatype)='laboratory test recommended not done' or lower-case($qdsdatatype)='laboratory test, recommended' or lower-case($qdsdatatype)='laboratory test, recommended not done'">
					<templateId root="{$tid-root}"/> <!-- laboratory test recommended pattern -->
					<id root="{$qdsuuid}"/>
					<xsl:choose>
						<xsl:when test="$actcode='NA'">
							<code code="18776-5" displayName="Plan of care" codeSystem="2.16.840.1.113883.6.1"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:copy-of select="$actcode"/>
						</xsl:otherwise>
					</xsl:choose>
					<sourceOf typeCode="COMP">
						<procedure classCode="PROC" moodCode="INT" isCriterionInd="true">
							<code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
							<title><xsl:value-of select="$title"/></title>
							
							<xsl:apply-templates select="attribute" mode="obs_val_new"/>
							<xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
							<xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
							    
							<xsl:apply-templates select="." mode="to"/>
						</procedure>
					</sourceOf>
			</xsl:when>

			<!--physical exam recommended -->
			<xsl:when test="lower-case($qdsdatatype)='physical exam recommended' or lower-case($qdsdatatype)='physical exam recommended not done' or lower-case($qdsdatatype)='physical exam, recommended' or lower-case($qdsdatatype)='physical exam, recommended not done'">
					<templateId root="{$tid-root}" /> <!-- Physical Exam recommended -->
					<id root="{$qdsuuid}"/>
					<xsl:choose>
						<xsl:when test="$actcode='NA'">
							<code code="18776-5" displayName="Plan of care" codeSystem="2.16.840.1.113883.6.1"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:copy-of select="$actcode"/>
						</xsl:otherwise>
					</xsl:choose>
					<sourceOf typeCode="COMP">
						<observation classCode="OBS" moodCode="INT" isCriterionInd="true" >
							<code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
							<title><xsl:value-of select="$title"/></title>
							
							<xsl:apply-templates select="attribute" mode="obs_val_new"/>
							<xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
							<xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
							    
							<xsl:apply-templates select="." mode="to"/>
						</observation>
				   </sourceOf>
			</xsl:when>

			<!--procedure recommended -->
			<xsl:when test="lower-case($qdsdatatype)='procedure recommended' or lower-case($qdsdatatype)='procedure recommended not done' or lower-case($qdsdatatype)='procedure, recommended' or lower-case($qdsdatatype)='procedure, recommended not done'">
					<templateId root="{$tid-root}" />  <!-- procedure recommended-->
					<id root="{$qdsuuid}"/>
					<xsl:choose>
						<xsl:when test="$actcode='NA'">
							<code code="18776-5" displayName="Plan of care" codeSystem="2.16.840.1.113883.6.1"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:copy-of select="$actcode"/>
						</xsl:otherwise>
					</xsl:choose>
					<sourceOf typeCode="COMP">
						<procedure classCode="PROC" moodCode="INT" isCriterionInd="true">
							 <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
							 <title><xsl:value-of select="$title"/></title>
							 
							 <xsl:apply-templates select="attribute" mode="obs_val_new"/>
							 <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
							  <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
							    
							<xsl:apply-templates select="." mode="to"/>
						</procedure>
					</sourceOf>
			</xsl:when>

			<!--substance recommended-->
			<xsl:when test="lower-case($qdsdatatype)='substance recommended' or lower-case($qdsdatatype)='substance recommended not done' or lower-case($qdsdatatype)='substance, recommended' or lower-case($qdsdatatype)='substance, recommended not done'">
					<templateId root="{$tid-root}"/> <!-- substance recommended pattern -->
					<id root="{$qdsuuid}"/>
					<xsl:choose>
						<xsl:when test="$actcode='NA'">
							<code code="18776-5" displayName="Plan of care" codeSystem="2.16.840.1.113883.6.1"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:copy-of select="$actcode"/>
						</xsl:otherwise>
					</xsl:choose>
					<sourceOf typeCode="COMP">
						<substanceAdministration classCode="SBADM" moodCode="INT" isCriterionInd="true">
							<title><xsl:value-of select="$title"/></title>
							
							<xsl:apply-templates select="attribute" mode="obs_val_new"/>
							<participant typeCode="CSM">
								<roleParticipant classCode="MANU">
									<playingMaterial classCode="MMAT" determinerCode="KIND">
									   <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
									</playingMaterial>
								</roleParticipant>
							</participant>
						   <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
						   <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
							    
							<xsl:apply-templates select="." mode="to"/>
					   </substanceAdministration>
				   </sourceOf>
			</xsl:when>

			<!--System Chraacteristic-->
			<xsl:when test="lower-case($qdsdatatype)='system characteristic'">
					<templateId root="{$tid-root}"/>
					<id root="{$qdsuuid}"/>
					<xsl:if test="$actcode!='NA'">
						<xsl:copy-of select="$actcode"/>
					</xsl:if>
					<sourceOf typeCode="COMP">
						<observation classCode="OBS" moodCode="DEF" isCriterionInd="true">
							<code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
							<title><xsl:value-of select="$title"/></title>
							
							<xsl:apply-templates select="attribute" mode="obs_val_new"/>
							<xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
							<xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
							    
							<xsl:apply-templates select="." mode="to"/>
						</observation>
					</sourceOf>
			</xsl:when>

			<!--Provider Chraacteristic-->
			<xsl:when test="lower-case($qdsdatatype)='provider characteristic' or lower-case($qdsdatatype)='provider characteristic not done'">
					<templateId root="{$tid-root}"/>
					<id root="{$qdsuuid}"/>
					<xsl:if test="$actcode!='NA'">
						<xsl:copy-of select="$actcode"/>
					</xsl:if>
					<sourceOf typeCode="COMP">
						<observation classCode="OBS" moodCode="DEF" isCriterionInd="true">
							<title><xsl:value-of select="$title"/></title>
													
							<xsl:apply-templates select="attribute" mode="obs_val_new"/>
							<participant typeCode="PRF"> 
							  <roleParticipant classCode="ASSIGNED">  
								 <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
							  </roleParticipant>
							</participant>
							<xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
							<xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>							    
							<xsl:apply-templates select="." mode="to"/>
						</observation>
					</sourceOf>
			</xsl:when>
			
			<!--Encounter Active-->
			<xsl:when test="lower-case($qdsdatatype)='encounter active' or lower-case($qdsdatatype)='encounter active not done' or lower-case($qdsdatatype)='encounter, active' or lower-case($qdsdatatype)='encounter, active not done'">
				   <templateId root="{$tid-root}"/> <!-- encounter active -->
				   <id root="{$qdsuuid}"/>
					<xsl:choose>
						<xsl:when test="$actcode='NA'">
							<code code="46240-8" displayName="Encounter List" codeSystem="2.16.840.1.113883.6.1"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:copy-of select="$actcode"/>
						</xsl:otherwise>
					</xsl:choose>
				   <sourceOf typeCode="COMP">
						 <encounter classCode="ENC" moodCode="EVN" isCriterionInd="true">
							<code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/> 
							<title><xsl:value-of select="$title"/></title>							
							
							<xsl:apply-templates select="attribute" mode="obs_val_new"/>
						 	<sourceOf typeCode="REFR">
						 		<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						 			<code code="33999-4" codeSystem="2.16.840.1.113883.6.1" displayName="Status"/>
						 			<value xsi:type="CD" code="55561003" displayName="active" codeSystem="2.16.840.1.113883.6.96"/>
						 		</observation>
						 	</sourceOf>
							<xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
							<xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
							<xsl:apply-templates select="." mode="to"/>
							</encounter>
					</sourceOf>
			</xsl:when>

			<!--Encounter Performed-->
			<xsl:when test="lower-case($qdsdatatype)='encounter performed' or lower-case($qdsdatatype)='encounter performed not done' or lower-case($qdsdatatype)='encounter, performed' or lower-case($qdsdatatype)='encounter, performed not done'">
				   <templateId root="{$tid-root}"/> <!-- encounter performed -->
				   <id root="{$qdsuuid}"/>
					<xsl:choose>
						<xsl:when test="$actcode='NA'">
							<code code="46240-8" codeSystem="2.16.840.1.113883.6.1" displayName="Encounter list"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:copy-of select="$actcode"/>
						</xsl:otherwise>
					</xsl:choose>
				   <sourceOf typeCode="COMP">
						<encounter classCode="ENC" moodCode="EVN" isCriterionInd="true">
							<code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/> 
							<title><xsl:value-of select="$title"/></title>
							<statusCode code="completed"/>
							
							<xsl:apply-templates select="attribute" mode="obs_val_new"/>
							<xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
							<xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
							<xsl:apply-templates select="." mode="to"/>
						</encounter>
					</sourceOf>
			</xsl:when>

			<!--Encounter Order-->
			<xsl:when test="lower-case($qdsdatatype)='encounter order' or lower-case($qdsdatatype)='encounter order not done' or lower-case($qdsdatatype)='encounter, order' or lower-case($qdsdatatype)='encounter, order not done'">
				   <templateId root="{$tid-root}"/> <!-- encounter order -->
				   <id root="{$qdsuuid}"/>
					<xsl:choose>
						<xsl:when test="$actcode='NA'">
							<code code="18776-5" codeSystem="2.16.840.1.113883.6.1" displayName="Plan of Care"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:copy-of select="$actcode"/>
						</xsl:otherwise>
					</xsl:choose>
				   <sourceOf typeCode="COMP">
						 <encounter classCode="ENC" moodCode="RQO" isCriterionInd="true">
							<code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/> 
							<title><xsl:value-of select="$title"/></title>
							<statusCode code="completed"/>
							
							<xsl:apply-templates select="attribute" mode="obs_val_new"/>
							<xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
							<xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
							<xsl:apply-templates select="." mode="to"/>
						</encounter>
					</sourceOf>
			</xsl:when>

			<!--Encounter Recommended-->
			<xsl:when test="lower-case($qdsdatatype)='encounter recommended' or lower-case($qdsdatatype)='encounter recommended not done' or lower-case($qdsdatatype)='encounter, recommended' or lower-case($qdsdatatype)='encounter, recommended not done'">
				   <templateId root="{$tid-root}"/> <!-- encounter recommended -->
				   <id root="{$qdsuuid}"/>
				<xsl:choose>
					<xsl:when test="$actcode='NA'">
						<code code="18776-5" displayName="Plan of care" codeSystem="2.16.840.1.113883.6.1"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:copy-of select="$actcode"/>
					</xsl:otherwise>
				</xsl:choose>
				   <sourceOf typeCode="COMP">
						<encounter classCode="ENC" moodCode="INT" isCriterionInd="true">
							<code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/> 
							<title><xsl:value-of select="$title"/></title>
							
							<xsl:apply-templates select="attribute" mode="obs_val_new"/>
							<xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
							<xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
							<xsl:apply-templates select="." mode="to"/>
						</encounter>
					</sourceOf>
			</xsl:when>
			
            <xsl:when test="lower-case($qdsdatatype)='encounter' or lower-case($qdsdatatype)='encounter not done'">
				   <templateId root="{$tid-root}"/> <!-- encounter pattern -->
				   <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="46240-8" displayName="Encounter list" codeSystem="2.16.840.1.113883.6.1"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				   <sourceOf typeCode="COMP">
				         <encounter classCode="ENC" moodCode="EVN" isCriterionInd="true">
				              <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/> 
				              <title><xsl:value-of select="$title"/></title>
								
								<xsl:apply-templates select="attribute" mode="obs_val_new"/>
								<xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
								
								<xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
								<xsl:apply-templates select="." mode="to"/>
					        </encounter>
				    </sourceOf>
            </xsl:when>

            <xsl:when test="lower-case($qdsdatatype)='diagnostic study performed' or lower-case($qdsdatatype)='diagnostic study performed not done' or lower-case($qdsdatatype)='diagnostic study, performed' or lower-case($qdsdatatype)='diagnostic study, performed not done'">
				    <templateId root="{$tid-root}"/> <!-- diagnostic study performed pattern-->
				    <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="30954-2" displayName="Results" codeSystem="2.16.840.1.113883.6.1"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				    <sourceOf typeCode="COMP">
				           <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
								<code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				                <title><xsl:value-of select="$title"/></title>
				                <statusCode code="completed"/>
				                
			            		<xsl:apply-templates select="attribute" mode="obs_val_new"/>
								<xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>    
								<xsl:if test="$refid!='NA' and $refoid!=''"> 
									<sourceOf typeCode="RSON">
										<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
											<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
											<value xsi:type="CD" code="{$refoid}"  displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
										</observation>
									</sourceOf>
									<!-- <xsl:apply-templates select="../.." mode="to"/> -->
	            				</xsl:if>
	            				<xsl:apply-templates select="." mode="to"/>
				          </observation>
				    </sourceOf>
            </xsl:when>

            <xsl:when test="lower-case($qdsdatatype)='procedure performed' or lower-case($qdsdatatype)='procedure performed not done' or lower-case($qdsdatatype)='procedure, performed' or lower-case($qdsdatatype)='procedure, performed not done'">
				<templateId root="{$tid-root}"/> <!-- procedure performed pattern -->
				<id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="47519-4" displayName="Procedures" codeSystem="2.16.840.1.113883.6.1"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				<sourceOf typeCode="COMP">
				     <procedure classCode="PROC" moodCode="EVN" isCriterionInd="true">
				         <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				         <title><xsl:value-of select="$title"/></title>
				         
				         <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				         <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				         <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
						<xsl:apply-templates select="." mode="to"/>
				     </procedure>
				</sourceOf>
            </xsl:when>

            <xsl:when test="lower-case($qdsdatatype)='diagnosis active' or lower-case($qdsdatatype)='diagnosis active not done' or lower-case($qdsdatatype)='diagnosis, active' or lower-case($qdsdatatype)='diagnosis, active not done'">		
				    <templateId root="{$tid-root}"/>   <!-- diagnosis active pattern -->
				    <id root="{$qdsuuid}"/>
				    <xsl:choose>
					    <xsl:when test="$actcode='NA'">
					    	<code code="11450-4" displayName="Problem" codeSystem="2.16.840.1.113883.6.1"/>
					    </xsl:when>
					    <xsl:otherwise>
					    	<xsl:copy-of select="$actcode"/>
					    </xsl:otherwise>
				    </xsl:choose>
				    <sourceOf typeCode="COMP">
				        <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				            <code code="55607006" codeSystem="2.16.840.1.113883.6.96" displayName="Problem"/>
				            <title><xsl:value-of select="$title"/></title>
				            
				        	<value xsi:type="CD" code="{$qdsoid}" displayName="{$qdsdisplayname}">
								<xsl:if test="properties/property[@name='location_anatomical id']">
				             		<xsl:apply-templates select="attribute" mode="val_qual"/>
				             	</xsl:if>
				        	</value>
				        	<xsl:apply-templates select="attribute" mode="obs_val_new"/>
				            <sourceOf typeCode="REFR">
				                <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				                    <code code="33999-4" codeSystem="2.16.840.1.113883.6.1" displayName="Status"/>
				                    <value xsi:type="CD" code="55561003" displayName="active" codeSystem="2.16.840.1.113883.6.96"/>
				                </observation>
				            </sourceOf>
				            <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each><!-- 0014 UNSENSITIZED 0047 severity=persistent -->
							<xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
							<xsl:apply-templates select="." mode="to"/>
				      </observation>
				   </sourceOf>
            </xsl:when>

            <xsl:when test="lower-case($qdsdatatype)='medication dispensed' or lower-case($qdsdatatype)='medication dispensed not done' or lower-case($qdsdatatype)='medication, dispensed' or lower-case($qdsdatatype)='medication, dispensed not done'">
			    <templateId root="{$tid-root}"/> <!-- medication dispensed pattern -->
			    <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="10160-0" displayName="Medications" codeSystem="2.16.840.1.113883.6.1"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
			    <sourceOf typeCode="COMP">
			        <supply classCode="SPLY" moodCode="EVN" isCriterionInd="true">
				        <title><xsl:value-of select="$title"/></title>
				        
				        <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				        <participant typeCode="CSM">
				            <roleParticipant classCode="MANU">
				                <playingMaterial classCode="MMAT" determinerCode="KIND">
				                    <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				                </playingMaterial>
				            </roleParticipant>
				        </participant>
				        <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
						<xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
							<xsl:apply-templates select="." mode="to"/>
			      </supply>
			   	</sourceOf> 	
            </xsl:when>
         	
            <xsl:when test="lower-case($qdsdatatype)='medication ordered' or lower-case($qdsdatatype)='medication order' or lower-case($qdsdatatype)='medication ordered not done' or lower-case($qdsdatatype)='medication order not done' or lower-case($qdsdatatype)='medication, order' or lower-case($qdsdatatype)='medication, order not done'">
			    	<templateId root="{$tid-root}"/> <!-- medication, order pattern -->
				    <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="10160-0" displayName="Medications" codeSystem="2.16.840.1.113883.6.1"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <substanceAdministration classCode="SBADM" moodCode="RQO" isCriterionInd="true">
				            <title><xsl:value-of select="$title"/></title>
				            
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				            <participant typeCode="CSM">
				                <roleParticipant classCode="MANU">
				                   <playingMaterial classCode="MMAT" determinerCode="KIND">
				                       <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				                   </playingMaterial>
				                </roleParticipant>
				           </participant>
				           <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				        	<xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
				        	    
							<xsl:apply-templates select="." mode="to"/>
				       </substanceAdministration>
				   </sourceOf>
            </xsl:when>
         	
         	<!-- JH:12/25/2010 added medication for discharge 12/26/2010 Took this section back out-->    
         
            
            <xsl:when test="lower-case($qdsdatatype)='medication active' or lower-case($qdsdatatype)='medication active not done' or lower-case($qdsdatatype)='medication, active' or lower-case($qdsdatatype)='medication, active not done'">
					<templateId root="{$tid-root}"/>  <!-- medication active pattern -->
				    <id root="{$qdsuuid}"/>
				    <xsl:choose>
				    	<xsl:when test="$actcode!='NA'">
				    		<xsl:copy-of select="$actcode"/>
				    	</xsl:when>
					    <xsl:otherwise>
					    	<code code="10160-0" displayName="Medications" codeSystem="2.16.840.1.113883.6.1"/>
					    </xsl:otherwise>
				    </xsl:choose>
				    <sourceOf typeCode="COMP">
				        <substanceAdministration classCode="SBADM" moodCode="EVN" isCriterionInd="true">
				            <title><xsl:value-of select="$title"/></title>
				            
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				            <participant typeCode="CSM">
				                <roleParticipant classCode="MANU">
				                    <playingMaterial classCode="MMAT" determinerCode="KIND">
				                        <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				                    </playingMaterial>
				                </roleParticipant>
				            </participant>
				            <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				            <sourceOf typeCode="REFR">
				                <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				                    <code code="33999-4" displayName="Status" codeSystem="2.16.840.1.113883.6.1"/>
				                    <value xsi:type="CD" code="55561003" displayName="Active" codeSystem="2.16.840.1.113883.6.96" />
				               </observation>
				            </sourceOf>
				        	<xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
							    
							<xsl:apply-templates select="." mode="to"/>
				      </substanceAdministration>
				   </sourceOf>
            </xsl:when>
            
            <!--Medication, Discharge Template-->
			
			
			<xsl:when test="lower-case($qdsdatatype)='medication discharge' or lower-case($qdsdatatype)='medication discharge not done' or lower-case($qdsdatatype)='medication, discharge' or lower-case($qdsdatatype)='medication, discharge not done'">
					<templateId root="{$tid-root}"/>  <!-- discharge medication pattern -->
					<id root="{$qdsuuid}"/>
				<xsl:choose>
					<xsl:when test="$actcode='NA'">
						<code code="10183-2" displayName="Discharge Medications" codeSystem="2.16.840.1.113883.6.1"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:copy-of select="$actcode"/>
					</xsl:otherwise>
				</xsl:choose>
					<sourceOf typeCode="COMP">
						<substanceAdministration classCode="SBADM" moodCode="RQO" isCriterionInd="true">
							<title><xsl:value-of select="$title"/></title>
							
							<xsl:apply-templates select="attribute" mode="obs_val_new"/>
							<participant typeCode="CSM">
								<roleParticipant classCode="MANU">
								   <playingMaterial classCode="MMAT" determinerCode="KIND">
									   <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
								   </playingMaterial>
								</roleParticipant>
							</participant>
							<xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
								
							
							<xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
							<xsl:apply-templates select="." mode="to"/>
						</substanceAdministration>
				   </sourceOf>
				   <!-- <xsl:apply-templates select="/access preference node"/> -->
			</xsl:when>

			

            <xsl:when test="lower-case($qdsdatatype)='physical exam finding' or lower-case($qdsdatatype)='physical exam finding not done' or lower-case($qdsdatatype)='physical exam, finding' or lower-case($qdsdatatype)='physical exam, finding not done'">
			    <templateId root="{$tid-root}"/> <!--physical exam, finding -->
			    <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="29545-1" codeSystem="2.16.840.1.113883.6.1" displayName="Physical Examination"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
			    <sourceOf typeCode="COMP">
			        <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
			            <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1">
			            	<xsl:if test="properties/property[@name='anatomical_structure id']">
			             		<xsl:apply-templates select="attribute" mode="val_qual"/>
			             	</xsl:if>
			            </code>
			            <title><xsl:value-of select="$title"/></title>
			            
			            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
						<xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
			            <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
						    
						<xsl:apply-templates select="." mode="to"/>
			        </observation>
			    </sourceOf>
            </xsl:when>

            <xsl:when test="lower-case($qdsdatatype)='laboratory test result' or lower-case($qdsdatatype)='laboratory test result not done' or lower-case($qdsdatatype)='laboratory test, result' or lower-case($qdsdatatype)='laboratory test, result not done'">
			    <templateId root="{$tid-root}"/><!-- lab test result pattern-->
			    <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="30954-2" displayName="Results" codeSystem="2.16.840.1.113883.6.1">
            				<xsl:apply-templates select="properties/property[@name='value' or @name='result']"/>
            			</code>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
			    <sourceOf typeCode="COMP">
			        <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
			            <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
			            <title><xsl:value-of select="$title"/></title>
			            <statusCode code="completed"/>
			            
			            <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
			            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
						<xsl:if test="$refid != 'NA'">
							<sourceOf typeCode="RSON">
								<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
									<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
									<value xsi:type="CD" code="{$refoid}"  displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
								</observation>
							</sourceOf>
						</xsl:if>
				         
						<xsl:apply-templates select="." mode="to"/>
			        </observation>
			    </sourceOf>
            </xsl:when>

            <xsl:when test="lower-case($qdsdatatype)='care goal' or lower-case($qdsdatatype)='care goal not done' or lower-case($qdsdatatype)='care plan' or lower-case($qdsdatatype)='care plan not done'">
				    <templateId root="{$tid-root}" />  <!-- Care goal in a Plan of Care Pattern-->
				    <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="18776-5" displayName="Plan of care" codeSystem="2.16.840.1.113883.6.1"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				    <sourceOf typeCode="COMP">
				         <observation classCode="OBS" moodCode="GOL" isCriterionInd="true">
				             <code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
				             <title><xsl:value-of select="$title"/></title>
				             
				             <value xsi:type="CD" code="{$qdsoid}" displayName="{$qdsdisplayname}">
				             	<xsl:if test="properties/property[@name='related to id']">
				             		<xsl:apply-templates select="attribute" mode="val_qual"/>
				             	</xsl:if>
				             </value>
				             <xsl:apply-templates select="attribute" mode="obs_val_new"/>
							 <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				             <xsl:if test="$refid != 'NA'">
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}"  displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
							</xsl:if>
							    
							<xsl:apply-templates select="." mode="to"/>
				          </observation>
				     </sourceOf>
            </xsl:when>

            <xsl:when test="lower-case($qdsdatatype)='communication from patient to provider' or lower-case($qdsdatatype)='communication from patient' or lower-case($qdsdatatype)='communication from patient to provider not done' or lower-case($qdsdatatype)='communication from patient not done' or lower-case($qdsdatatype)='communication: from patient to provider' or lower-case($qdsdatatype)='communication: from patient to provider not done'">
				<xsl:variable name="ircp_cd">
              	 	<xsl:choose>
	              	 	<xsl:when test="properties/property/@name='information recipient'">
	                 		<xsl:value-of select="properties/property[@name='information recipient']/@value"/>
	                 	</xsl:when>
	                 	<xsl:otherwise><xsl:text>158965000</xsl:text></xsl:otherwise>
                 	</xsl:choose>
              	</xsl:variable>
				<xsl:variable name="ircp_cs">
              	 	<xsl:choose>
	              	 	<xsl:when test="properties/property/@name='information recipient code system'">
	                 		<xsl:value-of select="properties/property[@name='information recipient code system']/@value"/>
	                 	</xsl:when>
	                 	<xsl:otherwise><xsl:text>2.16.840.1.113883.6.96</xsl:text></xsl:otherwise>
                 	</xsl:choose>
              	</xsl:variable>
			    <templateId root="{$tid-root}"/>   <!-- communication: from patient -->
			    <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="COMMGR_X" displayName="Communication Manager" codeSystem="2.16.840.1.113883.6.1"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
			    <sourceOf typeCode="COMP">
			        <act classCode="ACT" moodCode="EVN" isCriterionInd="true">
			            <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
			            <title><xsl:value-of select="$title"/></title>
			            
			            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
			            <participant typeCode="AUT">
			                <patientParticipant classCode="PAT"/>
			            </participant>
			            <participant typeCode="IRCP">  <!-- information recipient -->
			                <signatureCode code="S" />
			                <roleParticipant classCode="ASSIGNED">
							 	<code code="{$ircp_cd}" codeSystem="{$ircp_cs}" displayName="Medical Practitioner"/>
			                </roleParticipant>
			            </participant>
			            <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
			            <xsl:if test="$refid != 'NA'">
							<sourceOf typeCode="RSON">
								<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
									<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
									<value xsi:type="CD" code="{$refoid}"  displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
								</observation>
							</sourceOf>
						</xsl:if>
						    
						<xsl:apply-templates select="." mode="to"/>
			        </act>
			     </sourceOf>
            </xsl:when>

            <xsl:when test="lower-case($qdsdatatype)='communication from provider to patient' or lower-case($qdsdatatype)='communication from provider to patient not done' or lower-case($qdsdatatype)='communication to patient' or lower-case($qdsdatatype)='communication to patient not done' or lower-case($qdsdatatype)='communication: from provider to patient' or lower-case($qdsdatatype)='communication: from provider to patient not done'">
				<xsl:variable name="iaut_cd">
					<xsl:choose>
						<xsl:when test="properties/property/@name='information author'">
							<xsl:value-of select="properties/property[@name='information author']/@value"/>
						</xsl:when>
						<xsl:otherwise><xsl:text>158965000</xsl:text></xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:variable name="iaut_cs">
              	 	<xsl:choose>
	              	 	<xsl:when test="properties/property/@name='information author code system'">
	                 		<xsl:value-of select="properties/property[@name='information author code system']/@value"/>
	                 	</xsl:when>
	                 	<xsl:otherwise><xsl:text>2.16.840.1.113883.6.96</xsl:text></xsl:otherwise>
                 	</xsl:choose>
              	</xsl:variable>
				<templateId root="{$tid-root}"/>   <!-- communication: provider to patient -->
				<id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="COMMGR_X" displayName="Communication Manager" codeSystem="2.16.840.1.113883.6.1"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				  <sourceOf typeCode="COMP">
				     <act classCode="ACT" moodCode="EVN" isCriterionInd="true">
				         <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				         <title><xsl:value-of select="$title"/></title>
				         
				         <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				          <participant typeCode="AUT">
				              <roleParticipant classCode="ASSIGNED">
								 <code code="{$iaut_cd}" codeSystem="{$iaut_cs}" displayName="Medical Practitioner"/>
				              </roleParticipant>
				          </participant>
				          <participant typeCode="IRCP">  <!-- information receipient -->
				               <signatureCode code="S" />
				               <patientParticipant classCode="PAT"/>
				           </participant>
				           <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				           <xsl:if test="$refid != 'NA'">
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}"  displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				         </act>
				     </sourceOf>
            </xsl:when>
            
            <xsl:when test="lower-case($qdsdatatype)='communication provider to provider' or lower-case($qdsdatatype)='communication provider to provider not done' or lower-case($qdsdatatype)='communication from provider to provider' or lower-case($qdsdatatype)='communication from provider to provider not done' or lower-case($qdsdatatype)='communication: provider to provider' or lower-case($qdsdatatype)='communication: provider to provider not done' or lower-case($qdsdatatype)='communication: from provider to provider' or lower-case($qdsdatatype)='communication: from provider to provider not done'">
				<xsl:variable name="ircp_cd">
              	 	<xsl:choose>
	              	 	<xsl:when test="properties/property/@name='information recipient'">
	                 		<xsl:value-of select="properties/property[@name='information recipient']/@value"/>
	                 	</xsl:when>
	                 	<xsl:otherwise><xsl:text>158965000</xsl:text></xsl:otherwise>
                 	</xsl:choose>
              	</xsl:variable>
				<xsl:variable name="ircp_cs">
              	 	<xsl:choose>
	              	 	<xsl:when test="properties/property/@name='information recipient code system'">
	                 		<xsl:value-of select="properties/property[@name='information recipient code system']/@value"/>
	                 	</xsl:when>
	                 	<xsl:otherwise><xsl:text>2.16.840.1.113883.6.96</xsl:text></xsl:otherwise>
                 	</xsl:choose>
              	</xsl:variable>
				<xsl:variable name="iaut_cd">
					<xsl:choose>
						<xsl:when test="properties/property/@name='information author'">
							<xsl:value-of select="properties/property[@name='information author']/@value"/>
						</xsl:when>
						<xsl:otherwise><xsl:text>158965000</xsl:text></xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:variable name="iaut_cs">
              	 	<xsl:choose>
	              	 	<xsl:when test="properties/property/@name='information author code system'">
	                 		<xsl:value-of select="properties/property[@name='information author code system']/@value"/>
	                 	</xsl:when>
	                 	<xsl:otherwise><xsl:text>2.16.840.1.113883.6.96</xsl:text></xsl:otherwise>
                 	</xsl:choose>
              	</xsl:variable>
				    <templateId root="{$tid-root}"/>   <!-- communication: provider to provider -->
				    <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="COMMGR_X" displayName="Communication Manager" codeSystem="2.16.840.1.113883.6.1"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <act classCode="ACT" moodCode="EVN" isCriterionInd="true">
				            <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				            <title><xsl:value-of select="$title"/></title>
				            
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				            <participant typeCode="AUT">
				                <roleParticipant classCode="ASSIGNED">
								 <code code="{$iaut_cd}" codeSystem="{$iaut_cs}"  displayName="Medical Practitioner"/>
				                </roleParticipant>
				            </participant>
				            <participant typeCode="IRCP">  <!-- information recipient -->
				                 <signatureCode code="S" />
				                 <roleParticipant classCode="ASSIGNED">
								 <code code="{$ircp_cd}" codeSystem="{$ircp_cs}" displayName="Medical Practitioner"/>
				                 </roleParticipant>
				            </participant>
							<xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
							<xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
							<xsl:apply-templates select="." mode="to"/>
				        </act>
				     </sourceOf>
            </xsl:when>

            <xsl:when test="lower-case($qdsdatatype)='device applied' or lower-case($qdsdatatype)='device applied not done' or lower-case($qdsdatatype)='device, applied' or lower-case($qdsdatatype)='device, applied not done'">
				    <templateId root="{$tid-root}"/> <!-- device applied pattern -->
				    <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="47519-4" displayName="Procedures" codeSystem="2.16.840.1.113883.6.1"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <procedure classCode="PROC" moodCode="EVN" isCriterionInd="true">
				            <code code="360030002" displayName="application of device" codeSystem="2.16.840.1.113883.6.96" />
				            <title><xsl:value-of select="$title"/></title>
				            
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				            <participant typeCode="DEV">
				                <roleParticipant classCode="MANU">
				                    <playingDevice classCode="DEV" determinerCode="KIND">
				                         <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				                    </playingDevice>
				                </roleParticipant>
				            </participant>
				            <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				            <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>			            
				         </procedure>
				     </sourceOf>
            </xsl:when>

            <xsl:when test="lower-case($qdsdatatype)='diagnosis resolved' or lower-case($qdsdatatype)='diagnosis resolved not done' or lower-case($qdsdatatype)='diagnosis, resolved' or lower-case($qdsdatatype)='diagnosis, resolved not done'">
				   <templateId root="{$tid-root}"/>   <!-- diagnosis resolved pattern -->
				   <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="11450-4" displayName="Problem" codeSystem="2.16.840.1.113883.6.1"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				   <sourceOf typeCode="COMP">
				      <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				         <code code="55607006" codeSystem="2.16.840.1.113883.6.96" displayName="Problem"/>
				         <title><xsl:value-of select="$title"/></title>
				         
				         <value xsi:type="CD" code="{$qdsoid}" displayName="{$qdsdisplayname}"/>
				         <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				         <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				         <sourceOf typeCode="REFR">
				            <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				               <code code="33999-4" codeSystem="2.16.840.1.113883.6.1" displayName="Status"/>
				               <value xsi:type="CD" code="413322009" displayName="resolved" codeSystem="2.16.840.1.113883.6.96"/>
				            </observation>
				         </sourceOf>
				             
						 <xsl:apply-templates select="." mode="to"/>
				      </observation>
				   </sourceOf>
            </xsl:when>

            <xsl:when test="lower-case($qdsdatatype)='diagnosis family history' or lower-case($qdsdatatype)='diagnosis family history not done' or lower-case($qdsdatatype)='diagnosis, family history' or lower-case($qdsdatatype)='diagnosis, family history not done'">
				    <templateId root="{$tid-root}"/>   <!-- diagnosis active pattern -->
				    <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="10157-6" displayName="History of family member diseases" codeSystem="2.16.840.1.113883.6.1"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				            <code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
				            <title><xsl:value-of select="$title"/></title>
				            
				            <value xsi:type="CD" code="{$qdsoid}" displayName="{$qdsdisplayname}"/>
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				            <participant typeCode="SBJ" >
				                <roleParticipant classCode="PRS">
				                    <code code="125677006" codeSystem="2.16.840.1.113883.6.96" displayName="Relative"/>
				                </roleParticipant>
				           </participant>
				           <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				            <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				       </observation>
				   </sourceOf>
            </xsl:when>

            <xsl:when test="lower-case($qdsdatatype)='diagnosis inactive' or lower-case($qdsdatatype)='diagnosis inactive not done' or lower-case($qdsdatatype)='diagnosis, inactive' or lower-case($qdsdatatype)='diagnosis, inactive not done'">
				    <templateId root="{$tid-root}"/>   <!-- diagnosis inactive pattern -->
				    <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="11450-4" displayName="Problem" codeSystem="2.16.840.1.113883.6.1"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				            <code code="55607006" codeSystem="2.16.840.1.113883.6.96" displayName="Problem"/>
				            <title><xsl:value-of select="$title"/></title>
				            
				            <value xsi:type="CD" code="{$qdsoid}"  displayName="{$qdsdisplayname}"/>
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				            <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				            <sourceOf typeCode="REFR">
				                <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				                    <code code="33999-4" codeSystem="2.16.840.1.113883.6.1" displayName="Status"/>
				                    <value xsi:type="CD" code="73425007" displayName="inactive" codeSystem="2.16.840.1.113883.6.96"/>
				                </observation>
				           </sourceOf>
				           <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				      </observation>
				   </sourceOf>
            </xsl:when>

            <xsl:when test="lower-case($qdsdatatype)='diagnostic study result' or lower-case($qdsdatatype)='diagnostic study result not done' or lower-case($qdsdatatype)='diagnostic study, result' or lower-case($qdsdatatype)='diagnostic study, result not done'">
				    <templateId root="{$tid-root}"/> <!-- diagnostic study result pattern-->
				    <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="30954-2" displayName="Results" codeSystem="2.16.840.1.113883.6.1">
            				<xsl:apply-templates select="properties/property[@name='result' or @name='value']"/>
            			</code>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				            <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				            <title><xsl:value-of select="$title"/></title>
				            <statusCode code="completed"/>
				            
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				            <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				            <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
							
							<xsl:apply-templates select="." mode="to"/>
				        </observation>
				    </sourceOf>
            </xsl:when>

            <xsl:when test="lower-case($qdsdatatype)='laboratory test performed' or lower-case($qdsdatatype)='laboratory test performed not done' or lower-case($qdsdatatype)='laboratory test, performed' or lower-case($qdsdatatype)='laboratory test, performed not done'">
				    <templateId root="{$tid-root}"/> <!-- lab test performed pattern-->
				    <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="30954-2" displayName="Results" codeSystem="2.16.840.1.113883.6.1"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				             <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				             <title><xsl:value-of select="$title"/></title>
				             <statusCode code="completed"/>
				             
					        <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				            <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
							<xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
							<xsl:apply-templates select="." mode="to"/>
				        </observation>
				    </sourceOf>
            </xsl:when>


         	<xsl:when test="lower-case($qdsdatatype)='medication administered' or lower-case($qdsdatatype)='medication administered not done' or lower-case($qdsdatatype)='medication, administered' or lower-case($qdsdatatype)='medication, administered not done'">
				    <templateId root="{$tid-root}"/>  <!-- medication administered pattern -->
				    <id root="{$qdsuuid}"/>
         		<xsl:choose>
         			<xsl:when test="$actcode='NA'">
         				<code code="18610-6" displayName="Medication administered" codeSystem="2.16.840.1.113883.6.1"/>
         			</xsl:when>
         			<xsl:otherwise>
         				<xsl:copy-of select="$actcode"/>
         			</xsl:otherwise>
         		</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <substanceAdministration classCode="SBADM" moodCode="EVN" isCriterionInd="true">
				            <title><xsl:value-of select="$title"/></title>
				            
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				            <participant typeCode="CSM">
				                <roleParticipant classCode="MANU">
				                   <playingMaterial classCode="MMAT" determinerCode="KIND">
				                       <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				                   </playingMaterial>
				                </roleParticipant>
				            </participant>
				            <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
					            
							
				        	<xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> --> 
							</xsl:if>
							<xsl:apply-templates select="." mode="to"/>
				        </substanceAdministration>
				   </sourceOf>
				   <!-- <xsl:apply-templates select="/access preference node"/> -->
            </xsl:when>

            <xsl:when test="lower-case($qdsdatatype)='physical exam ordered' or lower-case($qdsdatatype)='physical exam order' or lower-case($qdsdatatype)='physical exam ordered not done' or lower-case($qdsdatatype)='physical exam order not done' or lower-case($qdsdatatype)='physical exam, order' or lower-case($qdsdatatype)='physical exam, order not done'">
				    <templateId root="{$tid-root}" /> <!-- Physical Exam, ordered -->
				    <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="18776-5" displayName="Plan of care" codeSystem="2.16.840.1.113883.6.1"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <observation classCode="OBS" moodCode="RQO" isCriterionInd="true" >
				            <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				            <title><xsl:value-of select="$title"/></title>
				            
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				            <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				            <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				        </observation>
				   </sourceOf>
            </xsl:when>

            <xsl:when test="lower-case($qdsdatatype)='physical exam performed' or lower-case($qdsdatatype)='physical exam performed not done' or lower-case($qdsdatatype)='physical exam, performed' or lower-case($qdsdatatype)='physical exam, performed not done'">
					<templateId root="{$tid-root}"/> <!-- Physical Exam not done -->
				    <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="29545-1" codeSystem="2.16.840.1.113883.6.1" displayName="Physical Examination"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				            <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1">
				            	<xsl:if test="properties/property[@name='anatomical_structure id']">
			             			<xsl:apply-templates select="attribute" mode="val_qual"/>
			             		</xsl:if>
				            </code>
				            <title><xsl:value-of select="$title"/></title>
				            <statusCode code="completed"/>
				            
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				            <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				            <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				        </observation>
				    </sourceOf>
            </xsl:when>

            <xsl:when test="lower-case($qdsdatatype)='procedure order' or lower-case($qdsdatatype)='procedure order not done' or lower-case($qdsdatatype)='procedure, order' or lower-case($qdsdatatype)='procedure, order not done'">
				    <templateId root="{$tid-root}" />  <!-- procedure order in a Plan of Care Pattern-->
				    <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="18776-5" displayName="Plan of care" codeSystem="2.16.840.1.113883.6.1"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <procedure classCode="PROC" moodCode="RQO" isCriterionInd="true">
				             <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				             <title><xsl:value-of select="$title"/></title>
				             
				             <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				             <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				              <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				        </procedure>
				    </sourceOf>
            </xsl:when>

            <xsl:when test="lower-case($qdsdatatype)='procedure result' or lower-case($qdsdatatype)='procedure result not done' or lower-case($qdsdatatype)='procedure, result' or lower-case($qdsdatatype)='procedure, result not done'">
				    <templateId root="{$tid-root}" /> <!-- procedure result pattern -->
				    <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="47519-4" displayName="Procedures" codeSystem="2.16.840.1.113883.6.1" />
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <procedure classCode="PROC" moodCode="EVN" isCriterionInd="true">
				            <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				            <title><xsl:value-of select="$title"/></title>
				            
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
							<xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
							<xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
							    
							<xsl:apply-templates select="." mode="to"/>
				       </procedure> 
				   </sourceOf>
            </xsl:when>

            <xsl:when test="lower-case($qdsdatatype)='risk category / assessment' or lower-case($qdsdatatype)='risk category / assessment not done' or lower-case($qdsdatatype)='risk category assessment' or lower-case($qdsdatatype)='risk category assessment not done'">
				     <templateId root="{$tid-root}" /> <!-- risk category / assessment pattern -->
				     <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="51848-0" displayName="Assessment" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				     <sourceOf typeCode="COMP">
				         <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				             <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				             <title><xsl:value-of select="$title"/></title>
				             <statusCode code="completed"/>
				             
				             <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				             <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				             <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
							    
							<xsl:apply-templates select="." mode="to"/>
				        </observation>
				    </sourceOf>
            </xsl:when>

            <xsl:when test="lower-case($qdsdatatype)='substance administered' or lower-case($qdsdatatype)='substance administered not done' or lower-case($qdsdatatype)='substance, administered' or lower-case($qdsdatatype)='substance, administered not done'">
				   <templateId root="{$tid-root}"/>  <!-- substance administered pattern -->
				   <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="11320-9" displayName="Feeding and dietary status" codeSystem="2.16.840.1.113883.6.1"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				   <sourceOf typeCode="COMP">
				      <substanceAdministration classCode="SBADM" moodCode="EVN" isCriterionInd="true">
				        <title><xsl:value-of select="$title"/></title>
				        
				        <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				         <participant typeCode="CSM">
				            <roleParticipant classCode="MANU">
				               <playingMaterial classCode="MMAT" determinerCode="KIND">
				               	<code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				               </playingMaterial>
				            </roleParticipant>
				         </participant>
				         <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				         <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
				            
						<xsl:apply-templates select="." mode="to"/>
				      </substanceAdministration>
				   </sourceOf>
            </xsl:when>

            <xsl:when test="lower-case($qdsdatatype)='substance ordered' or lower-case($qdsdatatype)='substance order' or lower-case($qdsdatatype)='substance ordered not done' or lower-case($qdsdatatype)='substance order not done' or lower-case($qdsdatatype)='substance, order' or lower-case($qdsdatatype)='substance, order not done'">
				    <templateId root="{$tid-root}"/> <!-- substance, ordered pattern -->
				    <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="11320-9" displayName="Feeding and dietary status" codeSystem="2.16.840.1.113883.6.1"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <substanceAdministration classCode="SBADM" moodCode="RQO" isCriterionInd="true">
				            <title><xsl:value-of select="$title"/></title>
				            
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				            <participant typeCode="CSM">
				                <roleParticipant classCode="MANU">
				                    <playingMaterial classCode="MMAT" determinerCode="KIND">
				                       <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				                    </playingMaterial>
				                </roleParticipant>
				            </participant>
				           <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				           <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				       </substanceAdministration>
				   </sourceOf>
            </xsl:when>

            <xsl:when test="lower-case($qdsdatatype)='symptom active' or lower-case($qdsdatatype)='symptom active not done' or lower-case($qdsdatatype)='symptom, active' or lower-case($qdsdatatype)='symptom, active not done'">
				    <templateId root="{$tid-root}"/> <!--symptom, active -->
				    <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="46691-2" codeSystem="2.16.840.1.113883.6.1" displayName="Symptoms"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				            <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				            <title><xsl:value-of select="$title"/></title>
				            
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				            <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				            <sourceOf typeCode="REFR">
				                <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				                    <code code="33999-4" codeSystem="2.16.840.1.113883.6.1" displayName="Status"/>
				                    <value xsi:type="CD" code="55561003" displayName="active" codeSystem="2.16.840.1.113883.6.96"/>
				               </observation>
				            </sourceOf>
				            <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
							    
							<xsl:apply-templates select="." mode="to"/>
				      </observation>
				   </sourceOf>
            </xsl:when>
			
            <xsl:when test="lower-case($qdsdatatype)='symptom inactive' or lower-case($qdsdatatype)='symptom inactive not done' or lower-case($qdsdatatype)='symptom, inactive' or lower-case($qdsdatatype)='symptom, inactive not done'">
				    <templateId root="{$tid-root}"/>   <!-- symptom inactive pattern -->
				    <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="46691-2" codeSystem="2.16.840.1.113883.6.1" displayName="Symptoms"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				            <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				            <title><xsl:value-of select="$title"/></title>
				            
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				            <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				            <sourceOf typeCode="REFR">
				                <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				                    <code code="33999-4" codeSystem="2.16.840.1.113883.6.1" displayName="Status"/>
				                    <value xsi:type="CD" code="73425007" displayName="inactive" codeSystem="2.16.840.1.113883.6.96"/>
				                </observation>
				           </sourceOf>
				           <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				      </observation>
				   </sourceOf>
            </xsl:when>			
			
            <xsl:when test="lower-case($qdsdatatype)='symptom resolved' or lower-case($qdsdatatype)='symptom resolved not done' or lower-case($qdsdatatype)='symptom, resolved' or lower-case($qdsdatatype)='symptom, resolved not done'">
				    <templateId root="{$tid-root}"/>   <!-- symptom inactive pattern -->
				    <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="46691-2" codeSystem="2.16.840.1.113883.6.1" displayName="Symptoms"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				            <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				            <title><xsl:value-of select="$title"/></title>
				            
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				            <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
							<sourceOf typeCode="REFR">
								<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
								   <code code="33999-4" codeSystem="2.16.840.1.113883.6.1" displayName="Status"/>
								   <value xsi:type="CD" code="413322009" displayName="resolved" codeSystem="2.16.840.1.113883.6.96"/>
								</observation>
							</sourceOf>
				           <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				      </observation>
				   </sourceOf>
            </xsl:when>

            <xsl:when test="lower-case($qdsdatatype)='symptom assessed' or lower-case($qdsdatatype)='symptom assessed not done' or lower-case($qdsdatatype)='symptom, assessed' or lower-case($qdsdatatype)='symptom, assessed not done'">
				    <templateId root="{$tid-root}"/> <!--symptom, active -->
				    <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="46691-2" codeSystem="2.16.840.1.113883.6.1" displayName="Symptoms"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				            <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				            <title><xsl:value-of select="$title"/></title>
				            
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				            <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				            <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				        </observation>
				     </sourceOf>
            </xsl:when>

            <xsl:when test="lower-case($qdsdatatype)='transfer from'">
			    <templateId root="{$tid-root}"/>
            	<id root="{$qdsuuid}"/>
            	<xsl:if test="$actcode!='NA'">
            		<xsl:copy-of select="$actcode"/>
            	</xsl:if>
            	<sourceOf typeCode="COMP">
            		<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				    <title><xsl:value-of select="$title"/></title>
				    
				    <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				    <participant typeCode="ORG">
				         <roleParticipant classCode="LOCE">
				             <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				         </roleParticipant>
				    </participant>
				    <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
	            	<xsl:apply-templates select="." mode="to"/>
	            	</observation>
            	</sourceOf>
            </xsl:when>

            <xsl:when test="lower-case($qdsdatatype)='transfer to'">
			    <templateId root="{$tid-root}"/>
            	<id root="{$qdsuuid}"/>
            	<xsl:if test="$actcode!='NA'">
            		<xsl:copy-of select="$actcode"/>
            	</xsl:if>
            	<sourceOf typeCode="COMP">
            		<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
					     <title><xsl:value-of select="$title"/></title>
					     
					     <xsl:apply-templates select="attribute" mode="obs_val_new"/>
					     <participant typeCode="DST">
					         <roleParticipant classCode="LOCE">
					              <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
					         </roleParticipant>
					     </participant>
					     <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
	            		<xsl:apply-templates select="." mode="to"/>
            		</observation>
            	</sourceOf>
            </xsl:when>

			<xsl:when test="lower-case($qdsdatatype)='substance allergy' or lower-case($qdsdatatype)='substance allergy not done' or lower-case($qdsdatatype)='substance, allergy' or lower-case($qdsdatatype)='substance, allergy not done'">
				    <templateId root="{$tid-root}"/> <!-- substance allergy pattern -->
				    <id root="{$qdsuuid}"/>
				<xsl:choose>
					<xsl:when test="$actcode='NA'">
						<code code="48765-2" displayName="Allergies, adverse reactions, alerts" codeSystem="2.16.840.1.113883.6.1"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:copy-of select="$actcode"/>
					</xsl:otherwise>
				</xsl:choose>
				   <sourceOf typeCode="COMP">
				       <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				            <code code="419199007" codeSystem="2.16.840.1.113883.6.96" displayName="allergy to substance"/>
				            <title><xsl:value-of select="$title"/></title>
				            
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				            <participant typeCode="CSM">
				                <roleParticipant classCode="MANU">
				                    <playingMaterial classCode="MMAT" determinerCode="KIND">
				                        <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				                    </playingMaterial>
				                </roleParticipant>
				            </participant>
				            <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>					  
				          <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
				            
						<xsl:apply-templates select="." mode="to"/>
				      </observation>
				   </sourceOf>
			</xsl:when>
			
			<xsl:when test="lower-case($qdsdatatype)='procedure adverse event' or lower-case($qdsdatatype)='procedure adverse event not done' or lower-case($qdsdatatype)='procedure, adverse event' or lower-case($qdsdatatype)='procedure, adverse event not done'">
				    <templateId root="{$tid-root}" /> <!-- procedure adverse event pattern -->
				    <id root="{$qdsuuid}"/>
				<xsl:choose>
					<xsl:when test="$actcode='NA'">
						<code code="48765-2" displayName="Allergies, adverse reactions, alerts" codeSystem="2.16.840.1.113883.6.1" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:copy-of select="$actcode"/>
					</xsl:otherwise>
				</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				            <code code="281647001" codeSystem="2.16.840.1.113883.6.96" displayName="Adverse reaction" />
				           <title><xsl:value-of select="$title"/></title>
				           
				           <xsl:apply-templates select="attribute" mode="obs_val_new"/>
						   <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				           <sourceOf typeCode="CAUS" inversionInd="true">
				               <procedure classCode="PROC" moodCode="EVN" isCriterionInd="true">
				               	<code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				              </procedure>
				           </sourceOf>    	
				           <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				        </observation>
				    </sourceOf>
			</xsl:when>
			
			<xsl:when test="lower-case($qdsdatatype)='procedure intolerance' or lower-case($qdsdatatype)='procedure intolerance not done' or lower-case($qdsdatatype)='procedure, intolerance' or lower-case($qdsdatatype)='procedure, intolerance not done'">
				    <templateId root="{$tid-root}" /> <!-- procedure intolerance pattern -->
				    <id root="{$qdsuuid}"/>
				<xsl:choose>
					<xsl:when test="$actcode='NA'">
						<code code="48765-2" displayName="Allergies, adverse reactions, alerts"	codeSystem="2.16.840.1.113883.6.1" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:copy-of select="$actcode"/>
					</xsl:otherwise>
				</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				            <code code="INTOL-X" codeSystem="2.16.840.1.113883.3.560" displayName="Intolerance"/>
				            <title><xsl:value-of select="$title"/></title>
				            
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
						    <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				            <sourceOf typeCode="CAUS" inversionInd="true">
				                <procedure classCode="PROC" moodCode="EVN" isCriterionInd="true">
				                	<code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				                </procedure>
				            </sourceOf>    
				          <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				       </observation>
				    </sourceOf>
			</xsl:when>
			
			<xsl:when test="lower-case($qdsdatatype)='medication adverse effects' or lower-case($qdsdatatype)='medication adverse effects not done' or lower-case($qdsdatatype)='medication, adverse effects' or lower-case($qdsdatatype)='medication, adverse effects not done'">
				    <templateId root="{$tid-root}"/> <!-- medication adverse effect pattern -->
				    <!--<id root="variable"/>-->
				    <id root="{$qdsuuid}"/>
				<xsl:choose>
					<xsl:when test="$actcode='NA'">
						<code code="48765-2" displayName="Allergies, adverse reactions, alerts" codeSystem="2.16.840.1.113883.6.1"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:copy-of select="$actcode"/>
					</xsl:otherwise>
				</xsl:choose>
				   <sourceOf typeCode="COMP">
				       <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				           <code code="419511003" codeSystem="2.16.840.1.113883.6.96" displayName="Drug adverse effect"/>
				           <!--<title>Medication adverse events: <xsl:value-of select="$title"/></title>-->
				           <title><xsl:value-of select="$title"/></title>
				           
				           <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				           <participant typeCode="CSM">
				               <roleParticipant classCode="MANU">
				                   <playingMaterial classCode="MMAT" determinerCode="KIND">
				                       <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				                   </playingMaterial>
				               </roleParticipant>
				           </participant>	
				           <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				           	<xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				      </observation>
				   </sourceOf>
			</xsl:when>

			<xsl:when test="lower-case($qdsdatatype)='medication allergy' or lower-case($qdsdatatype)='medication allergy not done' or lower-case($qdsdatatype)='medication, allergy' or lower-case($qdsdatatype)='medication, allergy not done'">
				    <templateId root="{$tid-root}"/> <!-- medication allergy pattern -->
				    <id root="{$qdsuuid}"/>
				<xsl:choose>
					<xsl:when test="$actcode='NA'">
						<code code="48765-2" displayName="Allergies, adverse reactions, alerts" codeSystem="2.16.840.1.113883.6.1"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:copy-of select="$actcode"/>
					</xsl:otherwise>
				</xsl:choose>
				    <sourceOf typeCode="COMP">
				    <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				        <code code="416098002" codeSystem="2.16.840.1.113883.6.96" displayName="Drug allergy"/>
				        <title><xsl:value-of select="$title"/></title>
				        
				        <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				        <participant typeCode="CSM">
				            <roleParticipant classCode="MANU">
				                <playingMaterial classCode="MMAT" determinerCode="KIND">
				                    <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				                </playingMaterial>
				            </roleParticipant>
				         </participant>
				         <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				          	<xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				      </observation>
				   </sourceOf>
			</xsl:when>
			
			<xsl:when test="lower-case($qdsdatatype)='medication intolerance' or lower-case($qdsdatatype)='medication intolerance not done' or lower-case($qdsdatatype)='medication, intolerance' or lower-case($qdsdatatype)='medication, intolerance not done'">
				    <templateId root="{$tid-root}"/> <!-- medication intolerance pattern -->
				    <id root="{$qdsuuid}"/>
				<xsl:choose>
					<xsl:when test="$actcode='NA'">
						<code code="48765-2" displayName="Allergies, adverse reactions, alerts" codeSystem="2.16.840.1.113883.6.1"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:copy-of select="$actcode"/>
					</xsl:otherwise>
				</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				            <code code="59037007" codeSystem="2.16.840.1.113883.6.96" displayName="Drug intolerance"/>
				            <title><xsl:value-of select="$title"/></title>
				            
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				            <participant typeCode="CSM">
				                <roleParticipant classCode="MANU">
				                   <playingMaterial classCode="MMAT" determinerCode="KIND">
				                       <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				                   </playingMaterial>
				                </roleParticipant>
				            </participant>
				            <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>   
				          <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				      </observation>
				   </sourceOf>
			</xsl:when>

			<xsl:when test="lower-case($qdsdatatype)='device adverse event' or lower-case($qdsdatatype)='device adverse event not done' or lower-case($qdsdatatype)='device, adverse event' or lower-case($qdsdatatype)='device, adverse event not done'">
				    <templateId root="{$tid-root}" /> <!-- device adverse event pattern -->
				    <id root="{$qdsuuid}"/>
				<xsl:choose>
					<xsl:when test="$actcode='NA'">
						<code code="48765-2" displayName="Allergies, adverse reactions, alerts"  codeSystem="2.16.840.1.113883.6.1" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:copy-of select="$actcode"/>
					</xsl:otherwise>
				</xsl:choose>
				    <sourceOf typeCode="COMP">
				    <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				        <code code="281647001" codeSystem="2.16.840.1.113883.6.96" displayName="Adverse reaction" /> 
				        <title><xsl:value-of select="$title"/></title>
				        
				        <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				        <participant typeCode="PRD">
				            <roleParticipant classCode="MANU">
				                <playingMaterial classCode="DEV" determinerCode="KIND">
				                    <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				                </playingMaterial>
				            </roleParticipant>
				        </participant>
				        <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
							<xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				      </observation>
				   </sourceOf>
            </xsl:when>

			<xsl:when test="lower-case($qdsdatatype)='device allergy' or lower-case($qdsdatatype)='device allergy not done' or lower-case($qdsdatatype)='device, allergy' or lower-case($qdsdatatype)='device, allergy not done'">
				    <templateId root="{$tid-root}"/> <!-- device allergy pattern -->
				    <id root="{$qdsuuid}"/>
				<xsl:choose>
					<xsl:when test="$actcode='NA'">
						<code code="48765-2" displayName="Allergies, adverse reactions, alerts"  codeSystem="2.16.840.1.113883.6.1" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:copy-of select="$actcode"/>
					</xsl:otherwise>
				</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				            <code code="106190000" codeSystem="2.16.840.1.113883.6.96"  displayName="allergy"/>
				            <title><xsl:value-of select="$title"/></title>
				            
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				            <participant typeCode="PRD">
				                <roleParticipant classCode="MANU">
				                    <playingMaterial classCode="DEV" determinerCode="KIND">
				                        <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				                   </playingMaterial>
				               </roleParticipant>
				           </participant>
				           <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				          <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				      </observation>
				   </sourceOf>
            </xsl:when>

			<xsl:when test="lower-case($qdsdatatype)='device intolerance' or lower-case($qdsdatatype)='device intolerance not done' or lower-case($qdsdatatype)='device, intolerance' or lower-case($qdsdatatype)='device, intolerance not done'">
				    <templateId root="{$tid-root}"/> <!-- device intolerance pattern -->
				    <id root="{$qdsuuid}"/>
				<xsl:choose>
					<xsl:when test="$actcode='NA'">
						<code code="48765-2" displayName="Allergies, adverse reactions, alerts"  codeSystem="2.16.840.1.113883.6.1" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:copy-of select="$actcode"/>
					</xsl:otherwise>
				</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				            <code code="INTOL-X" codeSystem="2.16.840.1.113883.3.560" displayName="Intolerance"/>
				            <title><xsl:value-of select="$title"/></title>
				            
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				            <participant typeCode="PRD">
				                 <roleParticipant classCode="MANU">
				                     <playingMaterial classCode="DEV" determinerCode="KIND">
				                         <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				                     </playingMaterial>
				                 </roleParticipant>
				            </participant>
				            <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>        
							<xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				         </observation>
				   </sourceOf>
            </xsl:when>
            
		 	<xsl:when test="lower-case($qdsdatatype)='device order' or lower-case($qdsdatatype)='device order not done' or lower-case($qdsdatatype)='device, order' or lower-case($qdsdatatype)='device, order not done'">
				     <templateId root="{$tid-root}"/> <!-- device, order -->
				     <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="18776-5" displayName="Plan of care" codeSystem="2.16.840.1.113883.6.1"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				     <sourceOf typeCode="COMP">
				        <supply classCode="SPLY" moodCode="RQO" isCriterionInd="true" >
				        <title><xsl:value-of select="$title"/></title>
				        
				        <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				        <participant typeCode="DEV">
				          <roleParticipant classCode="MANU">
				              <playingDevice classCode="DEV" determinerCode="KIND">
				                  <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				              </playingDevice>
				          </roleParticipant>
				       </participant>
				       <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				       <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
				            
						<xsl:apply-templates select="." mode="to"/>
				      </supply>
				  </sourceOf>
            </xsl:when>
			
			<xsl:when test="lower-case($qdsdatatype)='diagnostic study adverse event' or lower-case($qdsdatatype)='diagnostic study adverse event not done' or lower-case($qdsdatatype)='diagnostic study, adverse event' or lower-case($qdsdatatype)='diagnostic study, adverse event not done'">
				    <templateId root="{$tid-root}" /> <!-- diagnostic study adverse event pattern -->
				    <id root="{$qdsuuid}"/>
				<xsl:choose>
					<xsl:when test="$actcode='NA'">
						<code code="48765-2" displayName="Allergies, adverse reactions, alerts"  codeSystem="2.16.840.1.113883.6.1" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:copy-of select="$actcode"/>
					</xsl:otherwise>
				</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				            <code code="281647001" codeSystem="2.16.840.1.113883.6.96" displayName="Adverse reaction" />
				            <title><xsl:value-of select="$title"/></title>
				            
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
						    <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				            <sourceOf typeCode="CAUS" inversionInd="true">
				                <procedure classCode="PROC" moodCode="EVN" isCriterionInd="true">
				                    <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				                </procedure>
				           </sourceOf>    
				          <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				       </observation>
				   </sourceOf>
            </xsl:when>
           
             
            <xsl:when test="lower-case($qdsdatatype)='diagnostic study intolerance' or lower-case($qdsdatatype)='diagnostic study intolerance not done' or lower-case($qdsdatatype)='diagnostic study, intolerance' or lower-case($qdsdatatype)='diagnostic study, intolerance not done'">
				    <templateId root="{$tid-root}" /> <!-- diagnostic study intolerance pattern -->
				    <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="48765-2" displayName="Allergies, adverse reactions, alerts"  codeSystem="2.16.840.1.113883.6.1" />
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				            <code code="INTOL-X" codeSystem="2.16.840.1.113883.3.560" displayName="Intolerance"/>
				           <title><xsl:value-of select="$title"/></title>
				           
				           <xsl:apply-templates select="attribute" mode="obs_val_new"/>
						   <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				           <sourceOf typeCode="CAUS" inversionInd="true">
				               <procedure classCode="PROC" moodCode="EVN" isCriterionInd="true">
				                   <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				               </procedure>
				           </sourceOf>    		
				          <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				       </observation>
				    </sourceOf>
            </xsl:when>

			<xsl:when test="lower-case($qdsdatatype)='diagnostic study order' or lower-case($qdsdatatype)='diagnostic study order not done' or lower-case($qdsdatatype)='diagnostic study, order' or lower-case($qdsdatatype)='diagnostic study, order not done'">
				    <templateId root="{$tid-root}"/> <!-- diagnostic study order pattern -->
				    <id root="{$qdsuuid}"/>
				<xsl:choose>
					<xsl:when test="$actcode='NA'">
						<code code="18776-5" displayName="Plan of care" codeSystem="2.16.840.1.113883.6.1"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:copy-of select="$actcode"/>
					</xsl:otherwise>
				</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <procedure classCode="PROC" moodCode="RQO" isCriterionInd="true">
				            <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				            <title><xsl:value-of select="$title"/></title>
				            
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
						    <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
							<xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
							<xsl:apply-templates select="." mode="to"/>
				        </procedure>
				   </sourceOf>
            </xsl:when>
            
            <xsl:when test="lower-case($qdsdatatype)='diagnostic study recommended' or lower-case($qdsdatatype)='diagnostic study recommended not done' or lower-case($qdsdatatype)='diagnostic study, recommended' or lower-case($qdsdatatype)='diagnostic study, recommended not done'">
				    <templateId root="{$tid-root}"/> <!-- diagnostic study recommended pattern -->
				    <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="18776-5" displayName="Plan of care" codeSystem="2.16.840.1.113883.6.1"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <procedure classCode="PROC" moodCode="INT" isCriterionInd="true">
				            <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				            <title><xsl:value-of select="$title"/></title>
				            
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
						    <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
							
							<xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
							<xsl:apply-templates select="." mode="to"/>
				        </procedure>
				   </sourceOf>
            </xsl:when>
			
			<xsl:when test="lower-case($qdsdatatype)='functional status' or lower-case($qdsdatatype)='functional status not done'">
				    <templateId root="{$tid-root}"/> <!-- functional status pattern -->
				    <id root="{$qdsuuid}"/>
				<xsl:choose>
					<xsl:when test="$actcode='NA'">
						<code code="47420-5" displayName="Functional status assessment" codeSystem="2.16.840.1.113883.6.1"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:copy-of select="$actcode"/>
					</xsl:otherwise>
				</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				            <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				            <title><xsl:value-of select="$title"/></title>
				            
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
						    <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				            <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				        </observation>
				    </sourceOf>
            </xsl:when>
            
            <xsl:when test="lower-case($qdsdatatype)='intervention adverse event' or lower-case($qdsdatatype)='intervention adverse event not done' or lower-case($qdsdatatype)='intervention, adverse event' or lower-case($qdsdatatype)='intervention, adverse event not done'">
				    <templateId root="{$tid-root}" /> <!-- intervention adverse event pattern -->
				    <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="48765-2" displayName="Allergies, adverse reactions, alerts"  codeSystem="2.16.840.1.113883.6.1" />
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				             <code code="281647001" codeSystem="2.16.840.1.113883.6.96" displayName="Adverse reaction" />
				             <title><xsl:value-of select="$title"/></title>
				             
				             <xsl:apply-templates select="attribute" mode="obs_val_new"/>
						     <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				             <sourceOf typeCode="CAUS" inversionInd="true">
				                 <procedure classCode="PROC" moodCode="EVN" isCriterionInd="true">
				                     <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				                </procedure>
				             </sourceOf>    
				             <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				         </observation>
				    </sourceOf>
            </xsl:when>
            
            <xsl:when test="lower-case($qdsdatatype)='intervention intolerance' or lower-case($qdsdatatype)='intervention intolerance not done' or lower-case($qdsdatatype)='intervention, intolerance' or lower-case($qdsdatatype)='intervention, intolerance not done'">
				    <templateId root="{$tid-root}" /> <!-- intervention intolerance pattern -->
				    <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="48765-2" displayName="Allergies, adverse reactions, alerts"  codeSystem="2.16.840.1.113883.6.1" />
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				             <code code="INTOL-X" codeSystem="2.16.840.1.113883.3.560" displayName="Intolerance"/>
				            <title><xsl:value-of select="$title"/></title>
				            
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
						    <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				            <sourceOf typeCode="CAUS" inversionInd="true">
				                <procedure classCode="PROC" moodCode="EVN" isCriterionInd="true">
				                     <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				                 </procedure>
				            </sourceOf>    
				          <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				       </observation>
				    </sourceOf>
            </xsl:when>
           
           <xsl:when test="lower-case($qdsdatatype)='intervention order' or lower-case($qdsdatatype)='intervention order not done' or lower-case($qdsdatatype)='intervention ordered' or lower-case($qdsdatatype)='intervention ordered not done' or lower-case($qdsdatatype)='intervention, order' or lower-case($qdsdatatype)='intervention, order not done'">
				    <templateId root="{$tid-root}" />  <!-- intervention order Pattern-->
				    <id root="{$qdsuuid}"/>
           	<xsl:choose>
           		<xsl:when test="$actcode='NA'">
           			<code code="18776-5" displayName="Plan of care" codeSystem="2.16.840.1.113883.6.1"/>
           		</xsl:when>
           		<xsl:otherwise>
           			<xsl:copy-of select="$actcode"/>
           		</xsl:otherwise>
           	</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <procedure classCode="PROC" moodCode="RQO" isCriterionInd="true">
				             <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				             <title><xsl:value-of select="$title"/></title>
				             
				             <xsl:apply-templates select="attribute" mode="obs_val_new"/>
						     <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				             <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				        </procedure>
				    </sourceOf>
            </xsl:when>
            
            <xsl:when test="lower-case($qdsdatatype)='intervention performed' or lower-case($qdsdatatype)='intervention performed not done' or lower-case($qdsdatatype)='intervention, performed' or lower-case($qdsdatatype)='intervention, performed not done'">
				    <templateId root="{$tid-root}"/> <!-- intervention performed pattern -->
				    <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="47519-4" displayName="Procedures" codeSystem="2.16.840.1.113883.6.1"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				    <sourceOf typeCode="COMP">
				         <procedure classCode="PROC" moodCode="EVN" isCriterionInd="true">
				            <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				            <title><xsl:value-of select="$title"/></title>
				            <statusCode code="completed"/>
				            
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
						    <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				             <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				        </procedure>
				    </sourceOf>
            </xsl:when>
            
             <xsl:when test="lower-case($qdsdatatype)='intervention result' or lower-case($qdsdatatype)='intervention result not done' or lower-case($qdsdatatype)='intervention, result' or lower-case($qdsdatatype)='intervention, result not done'">
				    <templateId root="{$tid-root}" /> <!-- intervention result pattern -->
				    <id root="{$qdsuuid}"/>
             	<xsl:choose>
             		<xsl:when test="$actcode='NA'">
             			<code code="47519-4" displayName="Procedures" codeSystem="2.16.840.1.113883.6.1" />
             		</xsl:when>
             		<xsl:otherwise>
             			<xsl:copy-of select="$actcode"/>
             		</xsl:otherwise>
             	</xsl:choose>
				    <sourceOf typeCode="COMP">
				         <procedure classCode="PROC" moodCode="EVN" isCriterionInd="true">
				             <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				             <title><xsl:value-of select="$title"/></title>
				             <statusCode code="completed"/>
				             
				             <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				           	 <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				            <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				        </procedure> 
				    </sourceOf>
            </xsl:when>
            
            <xsl:when test="lower-case($qdsdatatype)='laboratory test adverse event' or lower-case($qdsdatatype)='laboratory test adverse event not done' or lower-case($qdsdatatype)='laboratory test, adverse event' or lower-case($qdsdatatype)='laboratory test, adverse event not done'">
				    <templateId root="{$tid-root}" /> <!-- laboratory test adverse event pattern -->
				    <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="48765-2" displayName="Allergies, adverse reactions, alerts" codeSystem="2.16.840.1.113883.6.1" />
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				            <code code="281647001" codeSystem="2.16.840.1.113883.6.96" displayName="Adverse reaction" />
				            <title><xsl:value-of select="$title"/></title>
				            
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
						    <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				            <sourceOf typeCode="CAUS" inversionInd="true">
				                <procedure classCode="PROC" moodCode="EVN" isCriterionInd="true">
				                    <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				                </procedure>
				            </sourceOf>    
				           <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				        </observation>
				    </sourceOf>
            </xsl:when>
            
            <xsl:when test="lower-case($qdsdatatype)='laboratory test intolerance' or lower-case($qdsdatatype)='laboratory test intolerance not done' or lower-case($qdsdatatype)='laboratory test, intolerance' or lower-case($qdsdatatype)='laboratory test, intolerance not done'">
				    <templateId root="{$tid-root}" /> <!-- laboratory test intolerance pattern -->
				    <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="48765-2" displayName="Allergies, adverse reactions, alerts" codeSystem="2.16.840.1.113883.6.1" />
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				             <code code="INTOL-X" codeSystem="2.16.840.1.113883.3.560" displayName="Intolerance"/>
				             <title><xsl:value-of select="$title"/></title>
				             
				             <xsl:apply-templates select="attribute" mode="obs_val_new"/>
						     <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				             <sourceOf typeCode="CAUS" inversionInd="true">
				                 <procedure classCode="PROC" moodCode="EVN" isCriterionInd="true">
				                     <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				                 </procedure>
				             </sourceOf>    
				             <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				         </observation>
				    </sourceOf>
            </xsl:when>
            
            <xsl:when test="lower-case($qdsdatatype)='laboratory test order' or lower-case($qdsdatatype)='laboratory test order not done' or lower-case($qdsdatatype)='laboratory test, order' or lower-case($qdsdatatype)='laboratory test, order not done'">
				    <templateId root="{$tid-root}"/> <!-- laboratory test order pattern -->
				    <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="18776-5" displayName="Plan of care" codeSystem="2.16.840.1.113883.6.1"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <procedure classCode="PROC" moodCode="RQO" isCriterionInd="true">
				            <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				            <title><xsl:value-of select="$title"/></title>
				            
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
						    <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				            <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				        </procedure>
				    </sourceOf>
            </xsl:when>
            
            <xsl:when test="lower-case($qdsdatatype)='medication history' or lower-case($qdsdatatype)='medication history not done'">
				    <templateId root="{$tid-root}"/> <!-- medication, history -->
				    <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="10160-0" displayName="Medications" codeSystem="2.16.840.1.113883.6.1"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <substanceAdministration classCode="SBADM" moodCode="EVN" isCriterionInd="true">
				            <title><xsl:value-of select="$title"/></title>
				            <statusCode code="completed"/>
				            
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				            <participant typeCode="CSM">
				                <roleParticipant classCode="MANU">
				                    <playingMaterial classCode="MMAT" determinerCode="KIND">
				                        <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				                    </playingMaterial>
				                </roleParticipant>
				           </participant>
				           <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				           <sourceOf typeCode="REFR">
				               <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				                   <code code="33999-4" displayName="Status" codeSystem="2.16.840.1.113883.6.1"/>
				                   <value xsi:type="CD" code="392521001" displayName="Prior History" codeSystem="2.16.840.1.113883.6.96" />
				               </observation>
				           </sourceOf>
				           <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				      </substanceAdministration>
				   </sourceOf>
            </xsl:when>
            
            <xsl:when test="lower-case($qdsdatatype)='substance adverse event' or lower-case($qdsdatatype)='substance adverse event not done' or lower-case($qdsdatatype)='substance, adverse event' or lower-case($qdsdatatype)='substance, adverse event not done'">
				    <templateId root="{$tid-root}"/> <!-- substance, adverse event pattern -->
				    <id root="{$qdsuuid}"/>
            	<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="48765-2" displayName="Allergies, adverse reactions, alerts" codeSystem="2.16.840.1.113883.6.1"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				            <code code="281647001" codeSystem="2.16.840.1.113883.6.96" displayName="Adverse reaction" /> 
				            <title><xsl:value-of select="$title"/></title>
				            
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				            <participant typeCode="CSM">
				                <roleParticipant classCode="MANU">
				                    <playingMaterial classCode="MMAT" determinerCode="KIND">
				                        <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				                    </playingMaterial>
				                </roleParticipant>
				           </participant>
				           <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				          <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				      </observation>
				   </sourceOf>
            </xsl:when>
            
            <xsl:when test="lower-case($qdsdatatype)='substance intolerance' or lower-case($qdsdatatype)='substance intolerance not done' or lower-case($qdsdatatype)='substance, intolerance' or lower-case($qdsdatatype)='substance, intolerance not done'">
				    <templateId root="{$tid-root}"/> <!-- substance intolerance pattern -->
				    <id root="{$qdsuuid}"/>
				<xsl:choose>
            		<xsl:when test="$actcode='NA'">
            			<code code="48765-2" displayName="Allergies, adverse reactions, alerts" codeSystem="2.16.840.1.113883.6.1"/>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:copy-of select="$actcode"/>
            		</xsl:otherwise>
            	</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				            <code code="INTOL-X" codeSystem="2.16.840.1.113883.3.560" displayName="Intolerance"/>
				            <title><xsl:value-of select="$title"/></title>
				            
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
				            <participant typeCode="CSM">
				                <roleParticipant classCode="MANU">
				                    <playingMaterial classCode="MMAT" determinerCode="KIND">
				                        <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				                    </playingMaterial>
				                </roleParticipant>
				             </participant>
				             <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				            <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				      </observation>
				   </sourceOf>
            </xsl:when>
			
			<xsl:when test="lower-case($qdsdatatype)='patient care experience' or lower-case($qdsdatatype)='patient care experience not done'">
				    <templateId root="{$tid-root}" />
				    <id root="{$qdsuuid}"/>
				<xsl:if test="$actcode!='NA'">
					<xsl:copy-of select="$actcode"/>
				</xsl:if>
				    <sourceOf typeCode="COMP">
				        <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				            <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				            <title><xsl:value-of select="$title"/></title>
				            
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
						    <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				            <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				        </observation>
				    </sourceOf>
            </xsl:when>
           
           <xsl:when test="lower-case($qdsdatatype)='provider care experience' or lower-case($qdsdatatype)='provider care experience not done'">
				    <templateId root="{$tid-root}" />
				    <id root="{$qdsuuid}"/>
           	<xsl:choose>
           		<xsl:when test="$actcode='NA'">
           			<code code="405193005" codeSystem="2.16.840.1.113883.6.96" displayName="Caregiver satisfaction"/>
           		</xsl:when>
           		<xsl:otherwise>
           			<xsl:copy-of select="$actcode"/>
           		</xsl:otherwise>
           	</xsl:choose>
				    <sourceOf typeCode="COMP">
				        <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				            <code code="{$qdsoid}" displayName="{$qdsdisplayname}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
				            <title><xsl:value-of select="$title"/></title>
				            <xsl:apply-templates select="attribute" mode="obs_val_new"/>
						    <xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
				            <xsl:if test="$refid!='NA' and $refoid!=''"> 
								<sourceOf typeCode="RSON">
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
										<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}" notDoneDisplayName="{$qdsdisplayname}"/>
									</observation>
								</sourceOf>
								<!-- <xsl:apply-templates select="../.." mode="to"/> -->
							</xsl:if>
					            
							<xsl:apply-templates select="." mode="to"/>
				        </observation>
				    </sourceOf>
            </xsl:when>
            
            <xsl:when test="lower-case($qdsname)='measurement enddate' or lower-case($qdsname)='measurement end date' or lower-case($qdsname)='measurement period' or lower-case($qdsname)='measurement startdate' or lower-case($qdsname)='measurement start date'">
            	<xsl:variable name="arit_text">
            		<xsl:apply-templates select="." mode="inline_arit_text"/>
            	</xsl:variable>
            	<sourceOf typeCode="COMP">
					<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
					<!-- [1128] measurement period -->
					<id root="{$qdsuuid}"/>
					<title>
						<xsl:value-of select="$qdsname"/>
						<xsl:if test="string-length(normalize-space($arit_text))>0">
							<xsl:text> (</xsl:text>
							<xsl:value-of select="normalize-space($arit_text)"/>
							<xsl:text>)</xsl:text>
						</xsl:if>
					</title>
					</observation>
				</sourceOf>
            </xsl:when>
            
            <xsl:otherwise>
               <ERROR>
                  <TYPE>missing template</TYPE>                  
                  <SOURCE><xsl:copy-of select="."/></SOURCE>               
                  <DESCRIPTION>No template found matching the data type of
                     "<xsl:value-of select="$qdsdatatype"/>" for the value set
                     "<xsl:value-of select="$qdsname"/>"
                  </DESCRIPTION>
               </ERROR>
            </xsl:otherwise>
            </xsl:choose>
		</act>
	</xsl:otherwise>
	</xsl:choose>
   </xsl:template>
	
	<xsl:template name="rson">
		<xsl:param name="refoid"/>
		<xsl:param name="refdisplayname"/>
		<sourceOf typeCode="RSON">
			<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				<code code="ASSERTION"  codeSystem="2.16.840.1.113883.5.4"/>
				<value xsi:type="CD" code="{$refoid}" displayName="{$refdisplayname}"/>
			</observation>
		</sourceOf>
	</xsl:template>
	
	<xsl:template match="attribute" mode="has_act_code">
		<xsl:variable name="pvalue">
			<xsl:value-of select="@value"/>
		</xsl:variable>
		<!-- <xsl:variable name="poid">
			<xsl:value-of select="$the_qdmAttributeMapping/DATA/ROW[ATTR_NAME='$pdisplayName']/OID"/>
			<xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@oid"/>
		</xsl:variable> -->
		<xsl:variable name="pmode">
			<xsl:value-of select="$the_qdmAttributeMapping/DATA/ROW[ATTR_NAME=@name]/MODE"/>
			<!-- <xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@mode"/> -->
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$pmode='AC'">
				<xsl:text>true</xsl:text>
			</xsl:when>
			<xsl:when test="contains(@name, 'health record')"><!-- 0241 diagnosis active -->
				<xsl:text>true</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>false</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="attribute" mode="act_code">
		<xsl:variable name="pvalue">
			<xsl:value-of select="@value"/>
		</xsl:variable>
		<xsl:variable name="pdisplayName">
			<xsl:value-of select="@name"/>
			<!-- <xsl:choose>
				<xsl:when test="string-length(ancestor::measure//elementLookUp/*[@id=$pvalue]/@displayName)>0">
					<xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@displayName"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@name"/>
				</xsl:otherwise>
			</xsl:choose> -->
		</xsl:variable>
		<xsl:variable name="poid">
			<xsl:value-of select="$the_qdmAttributeMapping/DATA/ROW[ATTR_NAME='$pdisplayName']/OID"/>
			<!-- <xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@oid"/> -->
		</xsl:variable>
		<xsl:variable name="pmode">
			<xsl:value-of select="$the_qdmAttributeMapping/DATA/ROW[ATTR_NAME=$pdisplayName]/MODE"/>
			<!-- <xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@mode"/> -->
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$pmode='AC'">
				<code code="{$poid}" displayName="{$pdisplayName}" codeSystem="2.16.840.1.113883.3.560.101.1"/>
			</xsl:when>
			<xsl:when test="contains(@name, 'health record')"><!-- 0241 diagnosis active -->
				<xsl:variable name="hrname">
					<xsl:if test="$the_healthRecordMapping/HealthRecordMapping/health-record[@code=$pvalue]">
	        			<xsl:value-of select="$the_healthRecordMapping/HealthRecordMapping/health-record[@code=$pvalue]/@healthRecordName"/>
	        		</xsl:if>
				</xsl:variable>
				<code code="{$pvalue}" displayName="{$hrname}" codeSystem="2.16.840.1.113883.6.1"/>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="qualifier">
		<xsl:param name="qname"/>
		<xsl:choose>
			<xsl:when test="$qname='NOTEMPTY'">
				<qualifier>
					<name code="405662005" codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED-CT" displayName="NOTEMPTY"/>
				</qualifier>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="attribute" mode="val_qual">
		<!-- <xsl:variable name="pvalue">
			<xsl:value-of select="@value"/>
		</xsl:variable> -->
		<xsl:variable name="pdisplayName">
			<xsl:value-of select="@name"/>
		</xsl:variable>
		<xsl:variable name="pcode">
			<xsl:value-of select="$the_qdmAttributeMapping/DATA/ROW[ATTR_NAME=$pdisplayName]/CODE" />
			<!-- <xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@code"/> -->
		</xsl:variable>
		
		<xsl:variable name="pcodeSystem">
			<xsl:value-of select="$the_qdmAttributeMapping/DATA/ROW[ATTR_NAME=$pdisplayName]/CODE_SYSTEM"/>
			<!-- <xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@codeSystem"/> -->
		</xsl:variable>		
		<xsl:choose>
			<xsl:when test="@name='anatomical_structure id'">
				<qualifier>
					<name code="91723000"  codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED-CT" displayName="Anatomical Structure"/>
					<value code="{$pcode}"   displayName="{$pdisplayName}"/>
           		</qualifier>
			</xsl:when>
			<xsl:when test="@name='location_anatomical id'">
				<qualifier>
					<name code="182353008'" codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED-CT" displayName="Side"/>
					<value code="{$pcode}"   displayName="{$pdisplayName}"/>
           		</qualifier>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="attribute">
		<xsl:choose>
			<xsl:when test="contains(@name, 'value')">
			    <qualifier>
					<xsl:choose>
						<xsl:when test="@value='negative'"><!-- 0014 -->
							<name code="NEG" codeSystem="2.16.840.1.113883.1.11.78" displayName="ObservationInterpretationDetection"/>
						</xsl:when>
						<xsl:when test="@value='positive'"><!-- 0399 -->
							<name code="POS" codeSystem="2.16.840.1.113883.1.11.78" displayName="ObservationInterpretationDetection"/>
						</xsl:when>
					</xsl:choose>
				</qualifier>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="severity">
		<xsl:param name="prop"/>
		<xsl:variable name="pvalue">
			<xsl:value-of select="@value"/>
		</xsl:variable>
		<xsl:variable name="code">
			<xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@oid"/>
		</xsl:variable>
		<xsl:variable name="displayName">
			<xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@name"/>
		</xsl:variable>
		<xsl:variable name="codeSystem">
			<xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@codeSystem"/>
		</xsl:variable>
       	<sourceOf typeCode="SUBJ">
           	<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
               	<code code="SEV" codeSystem="2.16.840.1.113883.5.4" displayName="Severity observation"/>
               	<value xsi:type="CD" code="{$code}"   displayName="{$displayName}"/>
           	</observation>
       	</sourceOf>
	</xsl:template>
	
	<xsl:template name="ordinal">
		<xsl:param name="prop"/>
		<xsl:variable name="pvalue">
			<xsl:value-of select="@value"/>
		</xsl:variable>
		<xsl:variable name="code">
			<xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@oid"/>
		</xsl:variable>
		<xsl:variable name="displayName">
			<xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@name"/>
		</xsl:variable>
		<xsl:variable name="codeSystem">
			<xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@codeSystem"/>
		</xsl:variable>
		<sourceOf typeCode="SUBJ">
			<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
				<code code="117363000" codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED-CT" displayName="Ordinal" />
				<value xsi:type="CD" code="{$code}"   displayName="{$displayName}"/>
			</observation>
		</sourceOf>
	</xsl:template>
	
	<xsl:template match="iqdsel" mode="dc_iqdsel">
		<xsl:variable name="refid">
			<xsl:value-of select="@refid"/>
		</xsl:variable>
		<xsl:variable name="refdatatype">
			<xsl:value-of select="/measure/elementLookUp/*[@id=$refid]/@datatype"/>
		</xsl:variable>
		<xsl:variable name="refuuid">
			<xsl:value-of select="/measure/elementLookUp/*[@id=$refid]/@uuid"/>
		</xsl:variable>
		<xsl:variable name="tid-root">
			<xsl:value-of select="$the_tidrootMapping/PatternMapping/pattern[@dataType=lower-case($refdatatype)]/@root"/>
		</xsl:variable>
	
	   	<entry typeCode="COMP" instanceInd="true">
			<observation classCode="OBS" moodCode="DEF">
				<templateId root="{$tid-root}"/>
				<id root="{@uuid}"/>
				<sourceOf typeCode="DRIV">
				  	<localVariableName><xsl:value-of select="@name"/></localVariableName>
					<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
						<id root="{$refuuid}"/>
					</observation>
				</sourceOf>
			</observation>
		</entry>
	</xsl:template>
	
	<xsl:template name="handleAttributesAndRHS">
		<xsl:call-template name="handleAttributes"/>
		<xsl:apply-templates select="." mode="to"/>
	</xsl:template>
	
	<xsl:template name="handleAttributes">
		<!-- deprecate start QDM 2.1.1.1 -->
		<xsl:apply-templates select="attribute" mode="obs_val_new"/>
		<!-- deprecate end -->
		<xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
	</xsl:template>
	
</xsl:stylesheet>
