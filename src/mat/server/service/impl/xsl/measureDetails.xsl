<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:exsl="http://exslt.org/common"
   xmlns:uuid="java:java.util.UUID" xmlns:math="http://exslt.org/math"
   xmlns:xs="http://www.w3.org/2001/XMLSchema" 
   xmlns:msxsl = "urn:schemas-microsoft-com:xslt"   
   xmlns="urn:hl7-org:v3" 
   extension-element-prefixes="exsl uuid math xs"
   exclude-result-prefixes="exsl uuid math xs msxsl">
   <xsl:output method="xml" indent="yes" encoding="ISO-8859-1"/>
   <xsl:preserve-space elements="content"/>
     
   <xsl:template match="measureDetails">
      <xsl:text>
         
      </xsl:text>
      <xsl:comment>
         **************************************************************   
         Measure Details Section
         **************************************************************
      </xsl:comment>
      <xsl:text>
         
      </xsl:text>
      <typeId root="2.16.840.1.113883.1.3" extension="POQM_HD000001"/>
      <id root="{normalize-space(uuid)}"/>
      <code code="57024-2" codeSystem="2.16.840.1.113883.6.1" displayName="Health Quality Measure Document"/>
      <title>
    		<xsl:call-template name="trim">
    			<xsl:with-param name="textString" select="title"/>
     		</xsl:call-template>
      </title>
      <xsl:if test="description">
         <text>
   		<xsl:call-template name="trim">
   			<xsl:with-param name="textString" select="description"/>
    		</xsl:call-template>
      </text>
      </xsl:if>
      <xsl:variable name="status">
	      <xsl:choose>
			<xsl:when test="lower-case(normalize-space(status))='in progress'">
				<xsl:text>InProgress</xsl:text>
			</xsl:when>
			<xsl:otherwise><xsl:value-of select="normalize-space(status)"/></xsl:otherwise>
	      </xsl:choose>
      </xsl:variable>
      <statusCode code="{$status}"/>
      <xsl:variable name="set-id">
      	<xsl:choose>
      	    <xsl:when test="string-length(normalize-space(guid))>0">
      	        <xsl:value-of select="normalize-space(guid)"/>
      		</xsl:when>
      		<xsl:otherwise>
      			<xsl:text>pending</xsl:text>
      		</xsl:otherwise>
      	</xsl:choose>
      </xsl:variable>
      <setId root="{$set-id}"/>
      <versionNumber value="{normalize-space(version)}"/>
      <xsl:if test="developers">
          <xsl:for-each select="developers/developer">
             <author typeCode="AUT">
               <assignedPerson classCode="ASSIGNED">
                  <representedOrganization classCode="ORG" determinerCode="INSTANCE">
                     <id root="{@id}"/>
                     <name>
   						<xsl:call-template name="trim">
   	              			 <xsl:with-param name="textString" select="."/>
   	            		</xsl:call-template>
                     </name>
                     <contactParty classCode="CON" nullFlavor="UNK"/>
                  </representedOrganization>
               </assignedPerson>
            </author>
         </xsl:for-each>
      </xsl:if>
      <xsl:if test="steward">
         <custodian>
          <assignedPerson classCode="ASSIGNED">
             <representedOrganization classCode="ORG" determinerCode="INSTANCE">
                 <id root="{steward/@id}"/>
                <name>
 					<xsl:call-template name="trim">
 					    <xsl:with-param name="textString" select="steward"/>
             		</xsl:call-template>
                </name>
                <contactParty classCode="CON" nullFlavor="UNK"/>
             </representedOrganization>
          </assignedPerson>
       </custodian>
      </xsl:if>
      
      <xsl:if test="endorsement">
         <verifier typeCode="VRF">
            <assignedPerson classCode="ASSIGNED">
               <representedOrganization classCode="ORG" determinerCode="INSTANCE">
                  <xsl:if test="endorsement/@id">
                     <id root="{endorsement/@id}"/>
                  </xsl:if>
                  <name>
                     <xsl:choose>
                        <xsl:when test="string-length(.)>0">
                           <xsl:call-template name="trim">
                              <xsl:with-param name="textString" select="endorsement"/>
                           </xsl:call-template>
                        </xsl:when>
                        <xsl:otherwise>None</xsl:otherwise>
                     </xsl:choose>
                  </name>
                  <contactParty classCode="CON" nullFlavor="UNK"/>
               </representedOrganization>
            </assignedPerson>
         </verifier>
      </xsl:if>
      <xsl:if test="not(endorsement)">
         <verifier typeCode="VRF">
            <assignedPerson classCode="ASSIGNED">
               <representedOrganization classCode="ORG" determinerCode="INSTANCE">
                  <name>
                     <xsl:text>None</xsl:text>
                  </name>
                  <contactParty classCode="CON" nullFlavor="UNK"/>
               </representedOrganization>
            </assignedPerson>
         </verifier>
      </xsl:if>
        <xsl:if test="finalizedDate">
         <xsl:variable name="finalizedDateVal" select="finalizedDate"></xsl:variable>
         <subjectOf>
            <measureAttribute>
               <code nullFlavor="OTH">
                  <originalText>Finalized Date/Time</originalText>            
               </code>
               <value xsi:type="TS" value="{normalize-space($finalizedDateVal)}"/>
            </measureAttribute>
         </subjectOf>
      </xsl:if>
      <xsl:if test="copyright">
       <subjectOf>
          <measureAttribute>
             <code code="COPY" codeSystem="2.16.840.1.113883.5.4" displayName="Copyright"/>
             <value xsi:type="ED" mediaType="text/plain">
 				<xsl:call-template name="trim">
              			 <xsl:with-param name="textString" select="copyright"/>
            		</xsl:call-template>
             </value>
          </measureAttribute>
       </subjectOf>      
      </xsl:if>
      <xsl:if test="scoring">
       <subjectOf>
          <xsl:variable name="scoring_id">
             <xsl:value-of select="scoring/@id"></xsl:value-of>
          </xsl:variable>
          <xsl:variable name="scoring_value">
             <xsl:value-of select="scoring"></xsl:value-of>
          </xsl:variable>
          <measureAttribute>
             <code code="MSRSCORE" codeSystem="2.16.840.1.113883.5.4" displayName="Measure Scoring"/>
             <value xsi:type="CD" code="{$scoring_id}" codeSystem="2.16.840.1.113883.1.11.20367" displayName="{normalize-space($scoring_value)}"/>
           </measureAttribute>
       </subjectOf>
      </xsl:if>
      <xsl:if test="types">
       <xsl:for-each select="types/type">
          <subjectOf>
             <measureAttribute>
                <code code="MSRTYPE" codeSystem="2.16.840.1.113883.5.4" displayName="Measure Type"/>
                <value xsi:type="CD" code="{@id}" codeSystem="2.16.840.1.113883.1.11.20368" displayName="{normalize-space(.)}"/>
             </measureAttribute>
          </subjectOf>
       </xsl:for-each>
      </xsl:if>
      <xsl:if test="stratification">
         <subjectOf>
            <measureAttribute>
               <code code="STRAT" codeSystem="2.16.840.1.113883.5.4" displayName="Stratification"/>
               <value xsi:type="ED" mediaType="text/plain">
   				<xsl:call-template name="trim">
                			 <xsl:with-param name="textString" select="stratification"/>
              		</xsl:call-template>
               </value>
            </measureAttribute>
         </subjectOf>
      </xsl:if>
      
		<subjectOf>
			<measureAttribute>
		      <code code="MSRADJ" codeSystem="2.16.840.1.113883.5.4" displayName="Risk Adjustment"/>
		      <value xsi:type="ED" mediaType="text/plain">
		      	<xsl:choose>
		      		<xsl:when test="string-length(riskAdjustment)>0">
						<xsl:call-template name="trim">
	              			 <xsl:with-param name="textString" select="riskAdjustment"/>
	            		</xsl:call-template>
		      		</xsl:when>
		      		<xsl:otherwise><xsl:text>None</xsl:text></xsl:otherwise>
		      	</xsl:choose>
		      </value>
		   </measureAttribute>
		</subjectOf>
      <xsl:if test="aggregation">
       <subjectOf>
          <measureAttribute>
             <code code="MSRAGG" codeSystem="2.16.840.1.113883.5.4" displayName="Rate Aggregation"/>
             <value xsi:type="ED" mediaType="text/plain">
 				<xsl:call-template name="trim">
              			 <xsl:with-param name="textString" select="aggregation"/>
            		</xsl:call-template>
             </value>
          </measureAttribute>
       </subjectOf>
      </xsl:if>
      <xsl:if test="rationale">
      <subjectOf>
         <measureAttribute>
            <code code="RAT" codeSystem="2.16.840.1.113883.5.4" displayName="Rationale"/>
            <value xsi:type="ED" mediaType="text/plain">
               	<xsl:call-template name="trim">
               	   <xsl:with-param name="textString" select="rationale"/>
            	</xsl:call-template>	
            </value>
         </measureAttribute>
      </subjectOf>
      </xsl:if>
      <xsl:if test="recommendations">
       <subjectOf>
          <measureAttribute>
             <code code="CRS" codeSystem="2.16.840.1.113883.3.560" displayName="Clinical Recommendation Statement"/>
             <value xsi:type="ED" mediaType="text/plain">
                	<xsl:call-template name="trim">
                	   <xsl:with-param name="textString" select="recommendations"/>
             	</xsl:call-template>
 		   </value>
          </measureAttribute>
       </subjectOf>
      </xsl:if>
      <xsl:if test="improvementNotations">
      <subjectOf>
         <measureAttribute>
            <code code="IDUR" codeSystem="2.16.840.1.113883.3.560" displayName="Improvement Notation"/>
            <value xsi:type="ED" mediaType="text/plain">
            	<xsl:choose>
	            	<xsl:when test="improvementNotations">
	            		<xsl:call-template name="trim">
              			 	<xsl:with-param name="textString" select="improvementNotations"/>
            			</xsl:call-template>
	            	</xsl:when>
            		<xsl:otherwise>
            			<xsl:attribute name="nullFlavor">NI</xsl:attribute>
            		</xsl:otherwise>
            	</xsl:choose>
            </value>
         </measureAttribute>
      </subjectOf>
      </xsl:if>
      <xsl:if test="cmsid">
         <subjectOf>
            <measureAttribute>
               <code nullFlavor="OTH">
                  <originalText>CMS ID Number</originalText>
               </code>
               <value xsi:type="II" root="{cmsid/@root}"/>
            </measureAttribute>
         </subjectOf>
      </xsl:if>
      <xsl:if test="nqfid">
         <subjectOf>
            <measureAttribute>
               <code nullFlavor="OTH">
                  <originalText>NQF ID Number</originalText>
               </code>
               <value xsi:type="II" root="{nqfid/@root}">
                  <xsl:if test="string-length(nqfid/@extension)>0">
                     <xsl:attribute name="extension">
                        <xsl:value-of select="nqfid/@extension"/>
                     </xsl:attribute>
                  </xsl:if>
               </value>
            </measureAttribute>
         </subjectOf>
      </xsl:if>
      <xsl:if test="disclaimer">
         <subjectOf>
            <measureAttribute>
               <code code="DISC" codeSystem="2.16.840.1.113883.5.4" displayName="Disclaimer">
                  <originalText>Disclaimer</originalText>
               </code>
               <value xsi:type="ED" mediaType="text/plain">
                  <xsl:call-template name="trim">
                     <xsl:with-param name="textString" select="disclaimer"/>
                  </xsl:call-template>
               </value>
            </measureAttribute>
         </subjectOf>         
      </xsl:if>
      <xsl:if test="emeasureid">
        <subjectOf>
           <measureAttribute>
              <code nullFlavor="OTH">
                 <originalText>eMeasure Identifier</originalText>
              </code>
              <value xsi:type="ED" mediaType="text/plain">
                 <xsl:if test="emeasureid">
                    <xsl:call-template name="trim">
                       <xsl:with-param name="textString" select="emeasureid"/>
                    </xsl:call-template>
                 </xsl:if>
              </value>
           </measureAttribute>
        </subjectOf>
      </xsl:if>
      <xsl:call-template name="measure_specific_data_elements"/>
      <xsl:for-each select="references/reference">
         <subjectOf>
            <measureAttribute>
               <code code="REF" codeSystem="2.16.840.1.113883.5.4" displayName="Reference"/>
               <value xsi:type="ED" mediaType="text/plain">
               		<xsl:call-template name="trim">
              			 <xsl:with-param name="textString" select="."/>
            		</xsl:call-template>
               </value>
            </measureAttribute>
         </subjectOf>
      </xsl:for-each>
      
      <xsl:choose>
			<xsl:when test="definitions">
				<xsl:for-each select="definitions">
					<subjectOf>
						<measureAttribute>
							<code code="DEF" codeSystem="2.16.840.1.113883.3.560" displayName="Definition"/>
							<xsl:choose>
								<xsl:when test="string-length(.)>0">
									<value xsi:type="ED" mediaType="text/plain">
										<xsl:call-template name="trim">
					              			 <xsl:with-param name="textString" select="."/>
					            		</xsl:call-template>
									</value>
								</xsl:when>
								<xsl:otherwise>
									<value xsi:type="ED" mediaType="text/plain" nullFlavor="NI"/>
								</xsl:otherwise>
							</xsl:choose>						
						</measureAttribute>
					</subjectOf>
				</xsl:for-each>
			</xsl:when>
			<xsl:otherwise>
				<subjectOf>
					<measureAttribute>
						<code code="DEF" codeSystem="2.16.840.1.113883.3.560" displayName="Definition"/>
						<value xsi:type="ED" mediaType="text/plain" nullFlavor="NI"/>
					</measureAttribute>
				</subjectOf>
			</xsl:otherwise>
		</xsl:choose>
      <xsl:if test="guidance">
      <subjectOf>
         <measureAttribute>
            <code code="GUIDE" codeSystem="2.16.840.1.113883.5.4" displayName="Guidance"/>
            <value xsi:type="ED" mediaType="text/plain">
				<xsl:call-template name="trim">
             			 <xsl:with-param name="textString" select="guidance"/>
           		</xsl:call-template>
            </value>
         </measureAttribute>
      </subjectOf> 
      </xsl:if>

      <!-- transmission format -->
      <xsl:if test="transmissionFormat">
      <xsl:call-template name="subjOfOrigText">
         <xsl:with-param name="origText"><xsl:text>Transmission Format</xsl:text></xsl:with-param>
         <xsl:with-param name="text">
            <xsl:value-of select="transmissionFormat/text()"/>
         </xsl:with-param>
      </xsl:call-template>   
      </xsl:if>
      
      <!-- Initial Patient Population Description -->
      <xsl:if test="initialPatientPopDescription">
      <xsl:call-template name="subjOfOrigText">
         <xsl:with-param name="origText">Initial Patient Population</xsl:with-param>
         <xsl:with-param name="text">
            <xsl:value-of select="initialPatientPopDescription/text()"/>
         </xsl:with-param>
         <xsl:with-param name="code">IPP</xsl:with-param>
         <xsl:with-param name="codeSystem">2.16.840.1.113883.5.1063</xsl:with-param>
      </xsl:call-template>  
      </xsl:if>
      
      <!-- Denominator Description -->
      <xsl:if test="denominatorDescription">
      <xsl:call-template name="subjOfOrigText">
         <xsl:with-param name="origText"><xsl:text>Denominator</xsl:text></xsl:with-param>
         <xsl:with-param name="text">
            <xsl:value-of select="denominatorDescription/text()"/>
         </xsl:with-param>
         <xsl:with-param name="code">DENOM</xsl:with-param>
         <xsl:with-param name="codeSystem">2.16.840.1.113883.5.1063</xsl:with-param>
      </xsl:call-template>
      </xsl:if>
      
      <!-- Denominator Exclusions Description -->
      <xsl:if test="denominatorExclusionsDescription">
      <xsl:call-template name="subjOfOrigText">
         <xsl:with-param name="origText"><xsl:text>Denominator Exclusions</xsl:text></xsl:with-param>
         <xsl:with-param name="text">
            <xsl:value-of select="denominatorExclusionsDescription/text()"/>
         </xsl:with-param>
      </xsl:call-template>
      </xsl:if>
      
      <!-- Numerator Description -->
      <xsl:if test="numeratorDescription">
      <xsl:call-template name="subjOfOrigText">
         <xsl:with-param name="origText"><xsl:text>Numerator</xsl:text></xsl:with-param>
         <xsl:with-param name="text">
            <xsl:value-of select="numeratorDescription/text()"/>
         </xsl:with-param>
         <xsl:with-param name="code">NUMER</xsl:with-param>
         <xsl:with-param name="codeSystem">2.16.840.1.113883.5.1063</xsl:with-param>
      </xsl:call-template>
      </xsl:if>
      <!-- Numerator Exclusions Description -->
      <xsl:if test="numeratorExclusionsDescription">
      <xsl:call-template name="subjOfOrigText">
         <xsl:with-param name="origText"><xsl:text>Numerator Exclusions</xsl:text></xsl:with-param>
         <xsl:with-param name="text">
            <xsl:value-of select="numeratorExclusionsDescription/text()"/>
         </xsl:with-param>
      </xsl:call-template>
      </xsl:if>
      <!-- Denominator Exceptions Description -->
      <xsl:if test="denominatorExceptionsDescription">
      <xsl:call-template name="subjOfOrigText">
         <xsl:with-param name="origText"><xsl:text>Denominator Exceptions</xsl:text></xsl:with-param>
         <xsl:with-param name="text">
            <xsl:value-of select="denominatorExceptionsDescription/text()"/>
         </xsl:with-param>
         <xsl:with-param name="code">DENEXCEP</xsl:with-param>
         <xsl:with-param name="codeSystem">2.16.840.1.113883.5.1063</xsl:with-param>
      </xsl:call-template>
      </xsl:if>
      <!-- Measure Population Description -->
      <xsl:if test="measurePopulationDescription">
      <xsl:call-template name="subjOfOrigText">
         <xsl:with-param name="origText"><xsl:text>Measure Population</xsl:text></xsl:with-param>
         <xsl:with-param name="text">
            <xsl:value-of select="measurePopulationDescription/text()"/>
         </xsl:with-param>
         <xsl:with-param name="code">MSRPOPL</xsl:with-param>
         <xsl:with-param name="codeSystem">2.16.840.1.113883.5.1063</xsl:with-param>
      </xsl:call-template>
      </xsl:if>
      <!-- Measure Observations Description -->
      <xsl:if test="measureObservationsDescription">
      <xsl:call-template name="subjOfOrigText">
         <xsl:with-param name="origText"><xsl:text>Measure Observations</xsl:text></xsl:with-param>
         <xsl:with-param name="text">
            <xsl:value-of select="measureObservationsDescription/text()"/>
         </xsl:with-param>
      </xsl:call-template>
      </xsl:if>
      <xsl:if test="supplementalData">
      <!-- supplemental data elements -->
      <xsl:call-template name="subjOfOrigText">
         <xsl:with-param name="origText"><xsl:text>Supplemental Data Elements</xsl:text></xsl:with-param>
         <xsl:with-param name="text">
            <xsl:value-of select="supplementalData/text()"/>
         </xsl:with-param>
      </xsl:call-template>
      </xsl:if>      
      <xsl:if test="qualityMeasureSet">
         <xsl:variable name="qms_uuid"><xsl:value-of select="qualityMeasureSet/@uuid"/></xsl:variable>
         <componentOf>
            <qualityMeasureSet classCode="ACT">
               <id root="{$qms_uuid}"/>
               <title>
                  <xsl:call-template name="trim">
                     <xsl:with-param name="textString" select="qualityMeasureSet"/>
                  </xsl:call-template>
               </title>
            </qualityMeasureSet>
         </componentOf>
      </xsl:if>
      		
   </xsl:template> 
  
	   <xsl:template name="measure_specific_data_elements">      
     
      <xsl:variable name="duration_ucum_unit">
         <xsl:choose>
            <xsl:when test="/measure/headers/period/duration[lower-case(@unit)='a']">a</xsl:when>
            <xsl:when test="/measure/headers/period/duration[lower-case(@unit)='m']">mo</xsl:when>
            <xsl:when test="/measure/headers/period/duration[lower-case(@unit)='mo']">mo</xsl:when>
            <xsl:when test="/measure/headers/period/duration[lower-case(@unit)='d']">d</xsl:when>
            <xsl:when test="/measure/headers/period/duration[lower-case(@unit)='min']">min</xsl:when>
            <xsl:when test="/measure/headers/period/duration[lower-case(@unit)='h']">h</xsl:when>
            <xsl:when test="/measure/headers/period/duration[lower-case(@unit)='qtr']">[qtr]</xsl:when>
            <xsl:when test="/measure/headers/period/duration[lower-case(@unit)='[qtr]']">[qtr]</xsl:when>
            <xsl:when test="/measure/headers/period/duration[lower-case(@unit)='w']">wk</xsl:when>
            <xsl:when test="/measure/headers/period/duration[lower-case(@unit)='wk']">wk</xsl:when>
            <xsl:when test="/measure/headers/period/duration[lower-case(@unit)='h']">h</xsl:when>
            <xsl:when test="/measure/headers/period/duration[lower-case(@unit)='s']">s</xsl:when>
         </xsl:choose>
      </xsl:variable>
      
      <xsl:variable name="duration_unit">
         <xsl:choose>
            <xsl:when test="lower-case($duration_ucum_unit)='a'">year(s)</xsl:when>
            <xsl:when test="lower-case($duration_ucum_unit)='m'">month(s)</xsl:when>
            <xsl:when test="lower-case($duration_ucum_unit)='mo'">month(s)</xsl:when>
            <xsl:when test="lower-case($duration_ucum_unit)='d'">day(s)</xsl:when>
            <xsl:when test="lower-case($duration_ucum_unit)='min'">minute(s)</xsl:when>
            <xsl:when test="lower-case($duration_ucum_unit)='h'">hour(s)</xsl:when>
            <xsl:when test="lower-case($duration_ucum_unit)='qtr'">quarter(s)</xsl:when>
            <xsl:when test="lower-case($duration_ucum_unit)='[qtr]'">quarter(s)</xsl:when>
            <xsl:when test="lower-case($duration_ucum_unit)='w'">week(s)</xsl:when>
            <xsl:when test="lower-case($duration_ucum_unit)='wk'">week(s)</xsl:when>
            <xsl:when test="lower-case($duration_ucum_unit)='h'">hour(s)</xsl:when>
            <xsl:when test="lower-case($duration_ucum_unit)='s'">second(s)</xsl:when>
         </xsl:choose>
      </xsl:variable>
      <xsl:if test="period">
		<subjectOf>
		    <measureAttribute>
		        <templateId root="2.16.840.1.113883.3.560.1.74"/>
		        <!-- measureAttribute.id = "variable" -->
		        <id root="{period/@uuid}"/>
		        <code code="MSRTP" displayName="Measurement period" codeSystem="2.16.840.1.113883.3.560"/>
		        <value xsi:type="IVL_TS">
		           <low value="{normalize-space(period/startDate/text())}"/>
		           <high value="{normalize-space(period/stopDate/text())}"/>
				</value>
		    </measureAttribute>
		 </subjectOf>      
	      
		<subjectOf>
			<localVariableName>measureStartDate</localVariableName>
		    <measureAttribute>
		        <templateId root="2.16.840.1.113883.3.560.1.75"/>
		        <!-- measureAttribute.id = "variable" -->
		        <id root="{period/startDate/@uuid}"/>
		        <code code="MSRSD" displayName="Measurement start date" codeSystem="2.16.840.1.113883.3.560"/>
		        <value xsi:type="TS" value="{normalize-space(period/startDate/text())}"/>
		     </measureAttribute>
		 </subjectOf>
		<subjectOf>
			<localVariableName>measureEndDate</localVariableName>
		    <measureAttribute>
		       <templateId root="2.16.840.1.113883.3.560.1.75"/>
		       <!-- measureAttribute.id = "variable" -->
		       <id root="{period/stopDate/@uuid}"/>
		       <code code="MSRED" displayName="Measurement end date" codeSystem="2.16.840.1.113883.3.560"/>
		       <value xsi:type="TS" value="{normalize-space(period/stopDate/text())}"/>
		     </measureAttribute>
		</subjectOf>
      </xsl:if>
   </xsl:template>
	
   <xsl:template name="subjOfOrigText">
      <xsl:param name="origText"/>
      <xsl:param name="text"/>
      <xsl:param name="xtype"/>
      <xsl:param name="code"/>
      <xsl:param name="codeSystem"/>
      
      <xsl:variable name="xsitype">
         <xsl:choose>
            <xsl:when test="string-length($xtype)>0">
               <xsl:value-of select="$xtype"/>
            </xsl:when>
            <xsl:otherwise>ED</xsl:otherwise>
         </xsl:choose>
      </xsl:variable>
      <subjectOf>
         <measureAttribute>
            <code>
               <xsl:choose>
                  <xsl:when test="string-length($code)=0 or string-length($codeSystem)=0">
                     <xsl:attribute name="nullFlavor">OTH</xsl:attribute>
                  </xsl:when>
                  <xsl:otherwise>
                     <xsl:attribute name="code">
                        <xsl:value-of select="$code"/>
                     </xsl:attribute>
                     <xsl:attribute name="codeSystem">
                        <xsl:value-of select="$codeSystem"/>
                     </xsl:attribute>
                  </xsl:otherwise>
               </xsl:choose>
               <originalText>
                  <xsl:value-of select="$origText"/>
               </originalText>            
            </code>
            <value xsi:type="{$xsitype}" mediaType="text/plain">
               <xsl:choose>
                  <xsl:when test="string-length($text)>0">
                     <xsl:call-template name="trim">
                        <xsl:with-param name="textString" select="$text"/>
                     </xsl:call-template>
                  </xsl:when>
                  <xsl:otherwise>None</xsl:otherwise>
               </xsl:choose>
            </value>
         </measureAttribute>
      </subjectOf>
   </xsl:template>
	
</xsl:stylesheet>