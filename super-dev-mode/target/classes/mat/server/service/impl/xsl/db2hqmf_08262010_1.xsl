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
   <xsl:key name="NON_DC_IDS" match="*[@id and (not(ancestor::elementlookup) or ancestor::measurecalc) and not(ancestor::abbreviations)  and not(ancestor::supplementalDataElements)]" use="@id" />
   <xsl:key name="NON_DC_TOS" match="*[@to and not(ancestor::elementlookup) and not(ancestor::abbreviations) and not(ancestor::supplementalDataElements)]" use="@to" />
   <xsl:key name="MCALC_IDS" match="measurecalc[@refid]" use="@refid" />
   <xsl:key name="IQDSEL_REFIDS" match="elementlookup/iqdsel" use="@refid" />
   <xsl:key name="QDSELS" match="elementlookup/qdsel" use="@id" />
   <xsl:preserve-space elements="content"/>
   <xsl:include href="qds_datatype_patterns.xsl"/>

   <xsl:template match="/">
      <xsl:apply-templates select="measure"/>
   </xsl:template>

   <xsl:template match="measure">
      <QualityMeasureDocument xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="urn:hl7-org:v3 ../xsd/schemas/EMeasure.xsd" moodCode="EVN" classCode="DOC">
         <xsl:apply-templates select="headers"/>
         <xsl:call-template name="population_criteria"/>
         <xsl:call-template name="measure_observations"/>
         <xsl:call-template name="data_criteria"/>
         <xsl:call-template name="stratification"/>
         <xsl:call-template name="supplemental_data_elements"/>
      </QualityMeasureDocument>
   </xsl:template>

   <xsl:template name="population_criteria">
      <xsl:text>
         
      </xsl:text>
      <xsl:comment>
         **************************************************************   
         Population Criteria Section
         **************************************************************
      </xsl:comment>
      <component typeCode="COMP">
         <section>
            <code code="57026-7" codeSystem="2.16.840.1.113883.6.1"
               displayName="Population criteria"/>
            <title>Population criteria</title>
            <text/>
            <xsl:choose>
               <!-- if score type = Continuous Variable then process only pop and measure pop-->
               <xsl:when test="headers/scores/score[@id='CONTVAR']">
                  <xsl:for-each select="population|measurePopulation">
                     <xsl:call-template name="criteria"/>
                  </xsl:for-each>
               </xsl:when>
               <!-- else process only pop, num, den, excl, exce, num excl -->
               <xsl:otherwise>
                  <xsl:for-each select="population|numerator|denominator|exclusions|exceptions|numeratorExclusions">
                     <xsl:call-template name="criteria"/>
                  </xsl:for-each>
               </xsl:otherwise>
            </xsl:choose>
            
         </section>
      </component>
   </xsl:template>

   <xsl:template name="measure_observations">
      <xsl:if test="headers/scores/score[@id='CONTVAR'] and ./measureObservation">
         <xsl:text>
            
         </xsl:text>
         <xsl:comment>
            **************************************************************   
            Measure Observations Section
            **************************************************************
         </xsl:comment>
         <component typeCode="COMP">
            <section>
               <code code="57027-5" codeSystem="2.16.840.1.113883.6.1"
                  displayName="Measure observations"/>
               <title>Measure observations</title>
               <text/>
               <xsl:for-each select="measureObservation/and/*">
                  <entry typeCode="DRIV" derivationExprInd="true" showArgsInd="true">
                     <xsl:call-template name="criteria"/>
                  </entry>
               </xsl:for-each>
            </section>
         </component>
      </xsl:if>
   </xsl:template>
   
   <xsl:template name="stratification">
      <xsl:if test="count(stratification) >= 1">
         <xsl:text>
            
         </xsl:text>
         <xsl:comment>
            **************************************************************   
            Stratification Section
            **************************************************************
         </xsl:comment>
         <component typeCode="COMP">
            <section>
               <code code="69669-0" codeSystem="2.16.840.1.113883.6.1" displayName="Population stratification description"/>
               <title>Reporting Stratification</title>               
               <text/>
               <xsl:for-each select="stratification[and/*]">
                  <entry typeCode="DRIV" derivationExprInd="true" showArgsInd="true">
                     <observation classCode="OBS" moodCode="EVN" isCriterionInd="true" actionNegationInd="true">
                        <id root="{@uuid}"/>
                        <code nullFlavor="OTH">
                           <originalText>Stratum</originalText>
                        </code>
                        <!--<xsl:call-template name="criteria"/>-->
                        <xsl:apply-templates select="and"/>
                     </observation>
                  </entry>
               </xsl:for-each>
            </section>
         </component>
      </xsl:if>
   </xsl:template>

   <xsl:template name="criteria">
      <xsl:choose>
         <xsl:when test="not(attachments)">
            <xsl:apply-templates select="."/>
         </xsl:when>
         <xsl:otherwise>
            <xsl:for-each select="attachments/attachment">
               <xsl:call-template name="pcrit">
                  <xsl:with-param name="attachment" select="."/>
                  <xsl:with-param name="crit" select="../.."/>
               </xsl:call-template>
            </xsl:for-each>
         </xsl:otherwise>
      </xsl:choose>
   </xsl:template>

   <xsl:template name="data_criteria">
      <xsl:text>
         
      </xsl:text>
      <xsl:comment>
         ******************************************   
         Data Criteria Section
         ******************************************
      </xsl:comment>
      <component typeCode="COMP">
         <section>
            <code code="57025-9" codeSystem="2.16.840.1.113883.6.1"
               displayName="Data criteria (QDM Data Elements)"/>
            <title>Data criteria (QDM Data Elements)</title>
            <text/>
            <xsl:for-each select="elementlookup/qdsel">
               <xsl:variable name="id">
                  <xsl:value-of select="@id"/>
               </xsl:variable>
               <xsl:variable name="addToDC">
                  <xsl:choose>
                     <xsl:when test="(key('NON_DC_IDS',$id) or key('NON_DC_TOS',$id) or key('MCALC_IDS',$id)) and .[@oid]">
                        <xsl:text>true</xsl:text>
                     </xsl:when>
                     <xsl:when test="key('IQDSEL_REFIDS',$id) and .[@oid]">
                        <xsl:for-each select="key('IQDSEL_REFIDS',$id)">
                           <xsl:if test="(key('NON_DC_IDS',@id) or key('NON_DC_TOS',@id) or key('MCALC_IDS',@id))">
                                 <xsl:text>true</xsl:text>
                              </xsl:if>
                        </xsl:for-each>
                     </xsl:when>
                  </xsl:choose>
               </xsl:variable>
               <xsl:if test="contains($addToDC,'true')">
                  <xsl:apply-templates select="." mode="datacriteria"/>
               </xsl:if>
             </xsl:for-each> 
            <xsl:for-each select="elementlookup/propel">
               <propel id="{@id}"  name="{@name}" displayName="{@displayName}" datatype="{@datatype}" oid="{@oid}" uuid="{@uuid}" taxonomy="{@taxonomy}" taxonomyVersion="{@taxonomyVersion}" codeSystem="{@codeSystem}" codeSystemName="{@codeSystemName}"/>
            </xsl:for-each>
            <xsl:for-each select="elementlookup/iqdsel">
               <xsl:if test="key('NON_DC_IDS',@id) or key('NON_DC_TOS',@id) or key('MCALC_IDS',@id)">
                  <xsl:apply-templates select="current()" mode="dc_iqdsel"/>
               </xsl:if>
            </xsl:for-each>
         </section>
      </component> 
   </xsl:template>
   <xsl:template match="*" mode="datacriteria">
		<xsl:variable name="elId"><xsl:value-of select="@id"/></xsl:variable>
		<entry typeCode="DRIV">
			<xsl:apply-templates select="."/>
		</entry>
   </xsl:template>
   
   <xsl:template name="supplemental_data_elements">
      <xsl:text>
         
      </xsl:text>
      <xsl:comment>
         ******************************************   
         Supplemental Data Elements Section
         ******************************************
      </xsl:comment>
      <component typeCode="COMP">
         <section>
            <code code="69670-8" codeSystem="2.16.840.1.113883.6.1"
               displayName="Supplemental Data Elements"/>
            <title>Supplemental Data Elements</title>
            <text/>
            <xsl:for-each select="supplementalDataElements/qdsel">
               <xsl:apply-templates select="." mode="datacriteria"/>
             </xsl:for-each> 
         </section>
      </component> 
   </xsl:template>

   <xsl:template match="population|denominator|exclusions|numerator|exceptions|measurePopulation|measureObservation|numeratorExclusions">
      <xsl:text>
         
      </xsl:text>
      <xsl:comment>
            **************************************************************   
            Population Criteria Section: <xsl:value-of select="name(.)"/> 
            **************************************************************
      </xsl:comment>
      <xsl:variable name="code">
         <xsl:choose>
            <xsl:when test="name(.) = 'population'">IPP</xsl:when>            
            <xsl:when test="name(.) = 'numerator'">NUMER</xsl:when>            
            <xsl:when test="name(.) = 'denominator'">DENOM</xsl:when>            
            <xsl:when test="name(.) = 'exclusions'">DENOM</xsl:when>            
            <xsl:when test="name(.) = 'exceptions'">DENEXCEP</xsl:when>
            <xsl:when test="name(.) = 'measurePopulation'">MSRPOPL</xsl:when>
            <xsl:when test="name(.) = 'measureObservation'">MSROBS</xsl:when>
            <xsl:when test="name(.) = 'numeratorExclusions'">NUMER</xsl:when>
            <xsl:otherwise>MISSING_POPULATION_CODE</xsl:otherwise>
         </xsl:choose>         
      </xsl:variable>
	  <xsl:variable name="entry_uuid">
         <xsl:choose>
            <xsl:when test="@uuid"><xsl:value-of select="@uuid"/></xsl:when>            
            <xsl:otherwise>MISSING_UUID</xsl:otherwise>
         </xsl:choose>         
      </xsl:variable>
      <xsl:variable name="displayName">
         <xsl:choose>
            <xsl:when test="name(.) = 'population'">Initial Patient Population</xsl:when>            
            <xsl:when test="name(.) = 'numerator'">Numerator</xsl:when>            
            <xsl:when test="name(.) = 'denominator'">Denominator</xsl:when>            
            <xsl:when test="name(.) = 'exclusions'">Denominator</xsl:when>            
            <xsl:when test="name(.) = 'exceptions'">Denominator Exception</xsl:when>
            <xsl:when test="name(.) = 'measurePopulation'">Measure Population</xsl:when>
            <xsl:when test="name(.) = 'measureObservation'">Measure Observation</xsl:when>
            <xsl:when test="name(.) = 'numeratorExclusions'">Numerator Exclusions</xsl:when>
            <xsl:otherwise>MISSING_POPULATION_DISPLAYNAME</xsl:otherwise>
         </xsl:choose>         
      </xsl:variable>
      <xsl:variable name="actionNegationInd">
         <xsl:choose>
            <xsl:when test="name(.) = 'exclusions'">true</xsl:when>            
            <xsl:when test="name(.) = 'numeratorExclusions'">true</xsl:when>
            <xsl:otherwise></xsl:otherwise>
         </xsl:choose>         
      </xsl:variable>

      <entry typeCode="DRIV">
         <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
            <xsl:if test="$actionNegationInd = 'true'">
               <xsl:attribute name="actionNegationInd">true</xsl:attribute>                  
            </xsl:if>
            <id root="{$entry_uuid}"/>
            <code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
            <value xsi:type="CD" code="{$code}" codeSystem="2.16.840.1.113883.5.1063"
            codeSystemName="HL7 Observation Value" displayName="{$displayName}"/>
            <!-- TODO verify handling of measure observation section -->
            <!-- 
               PRCN: (precondition) defines the criteria that must hold 
               in order to satisfy inclusion. 
               The target of the precondition can be 
               a reference to a criterion defined elsewhere, 
               whether in the same eMeasure document or 
               contained in an external library of criteria, if necessary. 
               
               COMP: (component), for the general case where 
               the only assertion is that the 
               related entries are contained within 
               the source section and 
               no other semantics are implied
            -->
            <xsl:choose>
               <xsl:when test="$code='MSROBS'">
                  <sourceOf typeCode="COMP">
                     <xsl:apply-templates select="*"/>
                  </sourceOf>
               </xsl:when>
               <xsl:otherwise>
                  <xsl:apply-templates select="*"/>
               </xsl:otherwise>
            </xsl:choose>
            
         </observation>
      </entry>
   </xsl:template>

   <xsl:template name="pcrit">
 	  <xsl:param name="crit"/>
 	  <xsl:param name="attachment"/>
      <xsl:text>
         
      </xsl:text>
      <xsl:comment>
            **************************************************************   
            Population Criteria Section: <xsl:value-of select="name($crit)"/> 
            **************************************************************
      </xsl:comment>
      <xsl:variable name="code">
         <xsl:choose>
            <xsl:when test="name($crit) = 'population'">IPP</xsl:when>            
            <xsl:when test="name($crit) = 'numerator'">NUMER</xsl:when>            
            <xsl:when test="name($crit) = 'denominator'">DENOM</xsl:when>            
            <xsl:when test="name($crit) = 'exclusions'">DENOM</xsl:when>            
            <xsl:when test="name($crit) = 'exceptions'">DENEXCEP</xsl:when>
            <xsl:when test="name($crit) = 'measurePopulation'">MSRPOPL</xsl:when>
            <xsl:when test="name($crit) = 'numeratorExclusions'">NUMER</xsl:when>
            <xsl:otherwise>MISSING_POPULATION_CODE</xsl:otherwise>
         </xsl:choose>         
      </xsl:variable>
      <xsl:variable name="displayName">
         <xsl:choose>
            <xsl:when test="name($crit) = 'population'">Initial Patient Population</xsl:when>            
            <xsl:when test="name($crit) = 'numerator'">Numerator</xsl:when>            
            <xsl:when test="name($crit) = 'denominator'">Denominator</xsl:when>            
            <xsl:when test="name($crit) = 'exclusions'">Denominator</xsl:when>            
            <xsl:when test="name($crit) = 'exceptions'">Denominator Exception</xsl:when>
            <xsl:when test="name($crit) = 'measurePopulation'">Measure Population</xsl:when>
            <xsl:when test="name($crit) = 'numeratorExclusions'">Numerator</xsl:when>
            <xsl:otherwise>MISSING_POPULATION_DISPLAYNAME</xsl:otherwise>
         </xsl:choose>         
      </xsl:variable>
      <xsl:variable name="entry_uuid">
         <xsl:choose>
         	<xsl:when test="name($crit)='numerator' or name($crit)='exclusions' or name($crit)='exceptions'">
         		<xsl:value-of select="uuid:randomUUID()"/>
         	</xsl:when>
            <xsl:when test="$crit/@uuid"><xsl:value-of select="$crit/@uuid"/></xsl:when>            
            <xsl:otherwise>MISSING_UUID</xsl:otherwise>
         </xsl:choose>         
      </xsl:variable>
      <xsl:variable name="actionNegationInd">
         <xsl:choose>
            <xsl:when test="name($crit) = 'exclusions'">true</xsl:when>
            <xsl:when test="name($crit) = 'numeratorExclusions'">true</xsl:when>
            <xsl:otherwise></xsl:otherwise>
         </xsl:choose>         
      </xsl:variable>

      <entry typeCode="DRIV">
         <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
            <xsl:if test="$actionNegationInd = 'true'">
               <xsl:attribute name="actionNegationInd">true</xsl:attribute>                  
            </xsl:if>
            <id root="{$entry_uuid}"/>
            <code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
            <value xsi:type="CD" code="{$code}" codeSystem="2.16.840.1.113883.5.1063"
               codeSystemName="HL7 Observation Value" displayName="{$displayName}"/>
            <xsl:for-each select="$crit/*">
            	<xsl:choose>
            		<xsl:when test="name(../..) != 'measure'">
            			<xsl:apply-templates select="$crit/*"/>
            		</xsl:when>
            		<xsl:otherwise>
		            	<xsl:call-template name="topCond">
							<xsl:with-param name="attachment" select="$attachment"/>
							<xsl:with-param name="cond" select="."/>
				     	</xsl:call-template>
            		</xsl:otherwise>
            	</xsl:choose>
            </xsl:for-each>
         </observation>
      </entry>
   </xsl:template>
   
	<xsl:template name="topCond">
		<xsl:param name="cond"/>
 	  	<xsl:param name="attachment"/>
      <xsl:variable name="isNot"><xsl:apply-templates select="$cond" mode="isChildOfNot"/></xsl:variable>
      <xsl:variable name="conj">
	      <xsl:value-of select="upper-case(name($cond))"/>
      </xsl:variable>
      <xsl:choose>
         <xsl:when test="name($cond)='and' or name($cond)='or'">
            <xsl:text>
               
            </xsl:text>
			<xsl:comment>  top and/or </xsl:comment>
            <xsl:variable name="scoring-type">
               <xsl:value-of select="/measure/headers/scores/score/@id"/>
            </xsl:variable>
            <xsl:variable name="parent-type">
               <xsl:value-of select="name(..)"/>
            </xsl:variable>
       		<xsl:variable name="root">
       		   <xsl:choose>
       		      <!-- when both a numerator and scoring type is RATIO, drive off of population not denominator -->
       		      <xsl:when test="(lower-case($parent-type)='numerator' or lower-case($parent-type)='numeratorexclusions') and lower-case($scoring-type)='ratio'">
       		         <xsl:value-of select="/measure/denominator[@uuid=$attachment/@uuid]/attachments/attachment/@uuid"/>
       		      </xsl:when>
       		      <xsl:otherwise>
       			     <xsl:value-of select="$attachment/@uuid"/>
       		      </xsl:otherwise>
       		   </xsl:choose>
       		</xsl:variable>
       		<xsl:variable name="title">
       			<xsl:choose>
       			   <!-- when RATIO, drive off of population not denominator -->
       			   <xsl:when test="(lower-case($parent-type)='numerator' or lower-case($parent-type)='numeratorexclusions') and lower-case($scoring-type)='ratio'">
       			      <xsl:value-of select="/measure/denominator[@uuid=$attachment/@uuid]/attachments/attachment/@title"/>
       			   </xsl:when>
       				<xsl:when test="$attachment/@title">
       					<xsl:value-of select="$attachment/@title"/>
       				</xsl:when>
       				<xsl:otherwise>Initial Patient Population</xsl:otherwise>
       			</xsl:choose>
       		</xsl:variable>
       		<sourceOf typeCode="PRCN">
         		<conjunctionCode code="{$conj}"/>
				<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
					<id root="{$root}"/>
					<title><xsl:value-of select="$title"/></title>
				</observation>
			</sourceOf>
            <xsl:apply-templates select="$cond/*" mode="nested"/>
         </xsl:when>
      </xsl:choose>
   </xsl:template>
   
   
   <xsl:template match="*" mode="isChildOfNot">
		<xsl:choose>
			<xsl:when test="parent::not">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
   
      <!--
            <not>
               <or>
                  <qdsel id="1386"/>
      -->
      <!-- 
      <xsl:variable name="currentId" select="generate-id(.)"/>
   
      <xsl:choose>
         <xsl:when test="name(..) != 'or' and name(..) != 'and' and name(../..) != 'or' and name(../..) != 'and'">
            <xsl:choose>
               <xsl:when test="
                  generate-id(ancestor::not[name(..)='and' or name(..)='or' or name(..)='to']//qdsel[position()=1]) = $currentId
                  ">true</xsl:when>
               <xsl:when test="
                  generate-id(ancestor::not[name(..)='and' or name(..)='or' or name(..)='to']//measureel[position()=1]) = $currentId
                  ">true</xsl:when>
               <xsl:when test="
                  generate-id(ancestor::not[name(..)='and' or name(..)='or' or name(..)='to']//and[position()=1]) = $currentId
                  ">true</xsl:when>
               <xsl:when test="
                  generate-id(ancestor::not[name(..)='and' or name(..)='or' or name(..)='to']//or[position()=1]) = $currentId
                  ">true</xsl:when>
               <xsl:otherwise>false</xsl:otherwise>
            </xsl:choose>            
         </xsl:when>
         <xsl:otherwise>false</xsl:otherwise>
      </xsl:choose>
       -->
   </xsl:template>

   <xsl:template match="and|or">
      <xsl:variable name="isNot"><xsl:apply-templates select="." mode="isChildOfNot"/></xsl:variable>
      <xsl:variable name="conj">
	      <xsl:value-of select="upper-case(name(.))"/>
      </xsl:variable>
      <xsl:choose>
         <xsl:when test="name(../..) != 'measure' ">
            <act classCode="ACT" moodCode="EVN" isCriterionInd="true">
               <xsl:if test="$isNot = 'true' "><xsl:attribute name="actionNegationInd">true</xsl:attribute></xsl:if>
               <!-- do not output for 'to' tag -->
               <xsl:apply-templates select=".." mode="count"/>
               <xsl:apply-templates select="*[name(.)!='to']" mode="nested"/>
               <xsl:apply-templates select="." mode="relative"/>
            </act>
         </xsl:when>
         <xsl:otherwise>
            <xsl:text>
               
            </xsl:text>
               <xsl:comment>  top and/or </xsl:comment>
	               <xsl:choose>
		               <xsl:when test="../attachments">
		               		<xsl:for-each select="../attachments/attachment">
			               		<xsl:variable name="root">
			               			<xsl:value-of select="@uuid"/>
			               		</xsl:variable>
			               		<xsl:variable name="title">
			               			<xsl:choose>
			               				<xsl:when test="@title">
			               					<xsl:value-of select="@title"/>
			               				</xsl:when>
			               				<xsl:otherwise>Initial Patient Population</xsl:otherwise>
			               			</xsl:choose>
			               		</xsl:variable>
			               		<sourceOf typeCode="PRCN">
				                	<conjunctionCode code="{$conj}"/>
									<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
										<id root="{$root}"/>
										<title><xsl:value-of select="$title"/></title>
									</observation>
								</sourceOf>
							</xsl:for-each>
		               </xsl:when>
					</xsl:choose>
            <xsl:apply-templates select="*" mode="nested"/>
         </xsl:otherwise>
      </xsl:choose>
   </xsl:template>
  
   <xsl:template match="qdsel|measureel|and|or|calc" mode="nested">
		<xsl:choose> 
			<xsl:when test="reference/qdsel[@id]">
				<xsl:for-each select="reference/qdsel">
					<xsl:apply-templates select="." mode="nested"/>
				</xsl:for-each>
			</xsl:when>
			<xsl:otherwise>    
				<sourceOf typeCode="PRCN">
				   <!-- TODO generalize this if check -->
				   <xsl:if test="name(..)='first' and name(../..)='not'">
				      <xsl:attribute name="negationInd">true</xsl:attribute>
				   </xsl:if>
					<xsl:apply-templates select="@order" mode="sequence"/>
					<conjunctionCode>
						<xsl:attribute name="code">
							<xsl:choose>
								<xsl:when test="name(..)='not'">
									<xsl:apply-templates select="../.." mode="getConj"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:apply-templates select=".." mode="getConj"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:attribute>   
					</conjunctionCode>
					<xsl:choose>
						<xsl:when test="name(..)='not'">
							<xsl:apply-templates select="../.." mode="subset"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:apply-templates select=".." mode="subset"/>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:apply-templates select="."/>
				</sourceOf>
	      	</xsl:otherwise>
		</xsl:choose>
   </xsl:template>
   
   <xsl:template match="qdsel" mode="source">
      <xsl:param name="conj"/>
         <xsl:apply-templates select="@order" mode="sequence"/>
         <xsl:if test="string-length($conj)>0">
            <conjunctionCode code="{$conj}"/>
         </xsl:if>
         <xsl:apply-templates select=".." mode="subset"/>
         <xsl:apply-templates select="."/>
   </xsl:template>
   
   <xsl:template match="@*" mode="sequence">
      <sequenceNumber value="{.}"/>
   </xsl:template>

   <xsl:template match="*" mode="getConj">
      <xsl:choose>
         <xsl:when test="name(.) = 'or' ">OR</xsl:when>         
         <xsl:when test="name(.) = 'and' ">AND</xsl:when>         
        <xsl:when test="name(.)='reference'">
            <xsl:apply-templates select=".." mode="getConj"/>
         </xsl:when>
         <xsl:when test="name(..) = 'or' ">OR</xsl:when>         
         <xsl:when test="name(..) = 'and' ">AND</xsl:when>
         <xsl:when test="name(../..) = 'or' ">OR</xsl:when>         
         <xsl:when test="name(../..) = 'and' ">AND</xsl:when>
         <xsl:otherwise>MISSING_CONJUNCTION</xsl:otherwise>
      </xsl:choose>
   </xsl:template>

   <!-- FUNCTIONS:
      COUNT
      MAX
      MIN
      LAST -> RECENT
      FIRST  
      SECOND
   -->

   <xsl:template match="count|min|max|last|first|second|third|fourth|fifth" mode="nested">
         <xsl:apply-templates select="*" mode="nested"/>
   </xsl:template>

   <xsl:template match="min|max|last|first|second|third|fourth|fifth" mode="count">
      <!-- 1. loop back up through parents looking for a "count" element -->
      <xsl:apply-templates select=".." mode="count"/>
   </xsl:template>
   <xsl:template match="and|or|to|not|qdsel|measureel" mode="count">
      <!-- 2. if one of these elements is encountered, stop -->
   </xsl:template>
   <xsl:template match="count" mode="count">
      <!-- 3. if "count" is found: -->
      <xsl:if test="./@lownum|./@highnum">
            <repeatNumber>
               <xsl:if test="@lownum">
                  <low value="{@lownum}" inclusive="{@lowinclusive}"/>
               </xsl:if>
               <xsl:if test="@highnum">
                  <high value="{@highnum}" inclusive="{@highinclusive}"/>
               </xsl:if>
            </repeatNumber>
         </xsl:if>
   </xsl:template>

   <xsl:template match="count" mode="subset">
      <!-- 1. loop back up through parents looking for a "count" element -->
      <xsl:apply-templates select=".." mode="subset"/>
   </xsl:template>
   <xsl:template match="and|or|to|not|qdsel|measureel" mode="subset">
      <!-- 2. if one of these elements is encountered, stop -->
   </xsl:template>
   <xsl:template match="min|max|last|first|second|third|fourth|fifth" mode="subset">
      <!-- 3. if "count" is found: -->
      <subsetCode>
         <xsl:attribute name="code">
            <xsl:choose>
               <xsl:when test="name(.)='last'">RECENT</xsl:when>
               <xsl:otherwise><xsl:value-of select="translate(name(.), $smallcase, $uppercase)"/></xsl:otherwise>
            </xsl:choose>
         </xsl:attribute>
      </subsetCode>
   </xsl:template>

   <xsl:template match="measureel">
      <xsl:variable name="id">
         <xsl:value-of select="current()/@id"/>
      </xsl:variable>
      <xsl:variable name="measureeldatatype">
         <xsl:value-of select="/measure/elementlookup/measureel[@id=$id]/@datatype"/>
      </xsl:variable>
      <xsl:variable name="measureeldef">
         <xsl:copy-of select="/measure/elementlookup/measureel[@id=$id]"/>
      </xsl:variable>
      <xsl:variable name="measureelname">
         <xsl:value-of select="/measure/elementlookup/measureel[@id=$id]/@name"/>
      </xsl:variable>
      <xsl:variable name="measureelid">
         <xsl:choose>
            <xsl:when test=" lower-case($measureelname) = 'measurement period' ">
               <xsl:value-of select="/measure/headers/period/duration/@uuid"/>
            </xsl:when>
            <xsl:when test=" lower-case($measureelname) = 'measurement enddate' ">
               <xsl:value-of select="/measure/headers/period/stopdate/@uuid"/>
            </xsl:when>
            <xsl:when test=" lower-case($measureelname) = 'measurement startdate' ">
               <xsl:value-of select="/measure/headers/period/startdate/@uuid"/>
            </xsl:when>
            <xsl:otherwise>
               <xsl:value-of select="/measure/elementlookup/measureel[@id=$id]/@oid"/>
            </xsl:otherwise>
         </xsl:choose>
      </xsl:variable>
      <xsl:variable name="measureeluuid">
         <xsl:value-of select="/measure/elementlookup/measureel[@id=$id]/@uuid"/>
      </xsl:variable>

      <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
         <xsl:if test="not($measureeldef)">
            <ERROR>
               <TYPE>missing measureel</TYPE>
               <SOURCE><xsl:copy-of select="."/></SOURCE>               
               <DESCRIPTION>No measureelement in the elementlookup section of the simple XML
                  matching ID of "<xsl:value-of select="$measureelid"/>"
               </DESCRIPTION>
            </ERROR>
         </xsl:if>
         <id root="{$measureeluuid}"/>
         <title>
            <xsl:call-template name="capitalize">
               <xsl:with-param name="textString" select="$measureelname"/>
            </xsl:call-template>
         </title>
      </observation>
   </xsl:template>

   <xsl:template match="calc">
      <xsl:variable name="isNot"><xsl:apply-templates select="." mode="isChildOfNot"/></xsl:variable>
      
      <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
         <xsl:if test="$isNot = 'true' "><xsl:attribute name="actionNegationInd">true</xsl:attribute></xsl:if>
         <templateId root="2.16.840.1.113883.3.560.1.79"/>
         <id root="{@uuid}"/>
         <!--
         <code code="13457-7" codeSystem="2.16.840.1.113883.6.1" displayName="Cholesterol in LDL"/>
         -->
         <derivationExpr><xsl:value-of select="equation"/></derivationExpr>
         <title><xsl:call-template name="elTitle"/></title>
         <!--xsl:apply-templates select="." mode="resultvalue"/-->
		  <xsl:apply-templates select="properties/property" mode="src_of"/>
		  <xsl:apply-templates select="properties/property" mode="obs_val"/>
		  <xsl:for-each select="variables/*">
            <sourceOf typeCode="DRIV">
               <localVariableName><xsl:value-of select="@name"/></localVariableName>
               <xsl:apply-templates select="*" mode="source"/>                          
            </sourceOf>
         </xsl:for-each>
         <xsl:apply-templates select=".." mode="count"/>
         <xsl:apply-templates select="." mode="relative"/>
      </observation>
   </xsl:template>

   <xsl:template match="*" mode="getId">
      <id root="dTBD"/>
   </xsl:template>

	<xsl:template match="*" mode="pauseQuantity">
		<xsl:variable name="unitval">
	   	  	<xsl:choose>
	   	  		<xsl:when test="value and value/low">
	   	  			<xsl:value-of select="current()/value/low/@unit"/>
	   	  		</xsl:when>
	   	  		<xsl:when test="value and value/high">
	   	  			<xsl:value-of select="current()/value/high/@unit"/>
	   	  		</xsl:when>
	   	  	   <xsl:when test="value and value/equal">
	   	  	      <xsl:value-of select="current()/value/equal/@unit"/>
	   	  	   </xsl:when>
	   	  	   <xsl:when test="@lowunit">
	   	  	      <xsl:value-of select="current()/@lowunit"/>
	   	  	   </xsl:when>
	   	  	   <xsl:when test="@highunit">
	   	  	      <xsl:value-of select="current()/@highunit"/>
	   	  	   </xsl:when>
	   	  	   <xsl:when test="@equalunit">
	   	  	      <xsl:value-of select="current()/@equalunit"/>
	   	  	   </xsl:when>
	   	  		<xsl:otherwise>NA</xsl:otherwise>
	   	  	</xsl:choose>
   	  </xsl:variable>
   	  <xsl:variable name="unit">
		<xsl:call-template name="unitvalue">
			<xsl:with-param name="uval" select="$unitval"/>
     	</xsl:call-template>
   	  </xsl:variable>
   	  <xsl:variable name="vtype">
         <xsl:choose>
            <xsl:when test="value/@type"><xsl:value-of select="value/@type"/></xsl:when>
            <xsl:otherwise>IVL_PQ</xsl:otherwise>
         </xsl:choose>
      </xsl:variable>
      <xsl:choose>
         <xsl:when test="$unit='a' or $unit= 'mo' or $unit= 'd' or $unit= 'h' or $unit= 'wk' or $unit= 'min' or $unit= '[qtr]' or $unit= 's'">
            <xsl:apply-templates select="current()" mode="pq_comparison"/>
      </xsl:when>
      <xsl:when test="@widthnum and @widthunit">
      	<xsl:variable name="wunit">
			<xsl:call-template name="unitvalue">
				<xsl:with-param name="uval" select="@widthunit"/>
	     	</xsl:call-template>
	    </xsl:variable>
      	<pauseQuantity xsi:type="URG_PQ">
      		<width value="{@widthnum}" unit="{$wunit}"/>
      	</pauseQuantity>
      </xsl:when>
      </xsl:choose>
      
	</xsl:template>
	<xsl:template name="unitvalue">
		<xsl:param name="uval"/>
		<xsl:choose>
   	  		<xsl:when test="$uval='years'">a</xsl:when>
   	  		<xsl:when test="$uval='year'">a</xsl:when>
   	  		<xsl:when test="$uval='month'">mo</xsl:when>
			<xsl:when test="$uval='months'">mo</xsl:when>
   	  		<xsl:when test="$uval='day'">d</xsl:when>
   	  		<xsl:when test="$uval='days'">d</xsl:when>
   	  		<xsl:when test="$uval='hour'">h</xsl:when>
   	  		<xsl:when test="$uval='hours'">h</xsl:when>
   	  		<xsl:when test="$uval='week'">wk</xsl:when>
   	  		<xsl:when test="$uval='weeks'">wk</xsl:when>
   	  		<xsl:when test="$uval='minute'">min</xsl:when>
   	  		<xsl:when test="$uval='minutes'">min</xsl:when>
   	  		<xsl:when test="$uval='quarter'">[qtr]</xsl:when>
   	  		<xsl:when test="$uval='quarters'">[qtr]</xsl:when>
		    <xsl:when test="$uval='second'">s</xsl:when>
   	  		<xsl:when test="$uval='seconds'">s</xsl:when>
		   
		    <xsl:when test="$uval='RAD'">RAD</xsl:when>
		    <xsl:when test="$uval='bpm'">{H.B}/min</xsl:when>
		    <xsl:when test="$uval='mmHg'">mm[Hg]</xsl:when>
		    <xsl:when test="$uval='mEq'">meq</xsl:when>
		   <xsl:when test="$uval='celsius'">cel</xsl:when>
		   
		   <xsl:when test="$uval='WBC/mm3'">{WBC}/mm3</xsl:when>
		   <xsl:when test="$uval='WBC/hpf'">{WBC}/[HPF]</xsl:when>
		   <xsl:when test="$uval='CFU/mL'">{CFU}/mL</xsl:when>
		   <xsl:when test="$uval='per mm3'">/mm3</xsl:when>
		      
   	  		<xsl:otherwise><xsl:value-of select="$uval"/></xsl:otherwise>
   	  	</xsl:choose>
	</xsl:template>
   <xsl:template match="*" mode="resultvalue">
   	  <xsl:variable name="unitval">
   	  	<xsl:choose>
   	  		<xsl:when test="value and value/low">
   	  			<xsl:value-of select="current()/value/low/@unit"/>
   	  		</xsl:when>
   	  		<xsl:when test="value and value/high">
   	  			<xsl:value-of select="current()/value/high/@unit"/>
   	  		</xsl:when>
   	  	   <xsl:when test="@lowunit">
   	  	      <xsl:value-of select="current()/@lowunit"/>
   	  	   </xsl:when>
   	  	   <xsl:when test="@highunit">
   	  	      <xsl:value-of select="current()/@highunit"/>
   	  	   </xsl:when>
   	  		<xsl:otherwise>NA</xsl:otherwise>
   	  	</xsl:choose>
   	  </xsl:variable>
   	  <xsl:variable name="unit">
		<xsl:call-template name="unitvalue">
			<xsl:with-param name="uval" select="$unitval"/>
     	</xsl:call-template>
   	  </xsl:variable>
      <xsl:variable name="vtype">
         <xsl:choose>
            <xsl:when test="value/@type"><xsl:value-of select="value/@type"/></xsl:when>
            <xsl:otherwise>IVL_PQ</xsl:otherwise>
         </xsl:choose>
      </xsl:variable>
      <xsl:if test="$unit!='a' and $unit!= 'mo' and $unit!= 'd' and $unit!= 'h' and $unit!= 'wk' and $unit!= 'min' and $unit!= '[qtr]' and $unit!= 's'">
	      <xsl:choose>
	         <xsl:when test="value or @lowunit or @highunit">
	            <value xsi:type="{$vtype}">
	               <xsl:if test="value/low">
	                  <low value="{value/low/@num}" unit="{$unit}" inclusive="{value/low/@inclusive}"/>
	               </xsl:if>
	               <xsl:if test="value/high">
	                  <high value="{value/high/@num}" unit="{$unit}" inclusive="{value/high/@inclusive}"/>
	               </xsl:if>
	               <xsl:if test="@lowunit">
	                  <low value="{@lownum}" unit="{$unit}" inclusive="{@lowinclusive}"/>
	               </xsl:if>
	               <xsl:if test="@highunit">
	                  <high value="{@highnum}" unit="{$unit}" inclusive="{@highinclusive}"/>
	               </xsl:if>
	            </value>
	         </xsl:when>
	      </xsl:choose>
      </xsl:if>
   </xsl:template>
	<!-- 
	<xsl:template match="*" mode="medicationresultvalue">
	  <xsl:variable name="unit">
	  	<xsl:if test="value/low">
			<xsl:call-template name="unitvalue">
				<xsl:with-param name="uval" select="value/low/@unit"/>
	     	</xsl:call-template>
	  	</xsl:if>
	  	<xsl:if test="value/high">
	  		<xsl:call-template name="unitvalue">
				<xsl:with-param name="uval" select="value/high/@unit"/>
	     	</xsl:call-template>
	  	</xsl:if>
	  </xsl:variable>
      <xsl:choose>
         <xsl:when test="value">
            <doseQuantity xsi:type="{value/@type}">
               <xsl:if test="value/low">
                  <low value="{value/low/@num}" unit="{value/low/@unit}" inclusive="{value/low/@inclusive}"/>
               </xsl:if>
               <xsl:if test="value/high">
                  <high value="{value/high/@num}" unit="{value/high/@unit}" inclusive="{value/high/@inclusive}"/>
               </xsl:if>
            </doseQuantity>
         </xsl:when>
      </xsl:choose>
   </xsl:template>
    -->

   <xsl:template match="*" mode="relative">
      <xsl:choose>
         <xsl:when test="./@rel">
            <xsl:variable name="currentto">
               <xsl:value-of select="./@to"/>
            </xsl:variable>
            <xsl:variable name="childto">
               <xsl:value-of select="to/@id"/>
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
            <xsl:variable name="inversion">
               <xsl:choose>
                  <xsl:when test="@rel='SBOD'">true</xsl:when>
                  <xsl:when test="@rel='EBOD'">true</xsl:when>
                  <xsl:otherwise>false</xsl:otherwise>
               </xsl:choose>
            </xsl:variable>
            <!-- sourceOf HERE -->
            <sourceOf typeCode="{$rel}">
               <xsl:if test="$inversion = 'true'">
                  <xsl:attribute name="inversionInd">true</xsl:attribute>                  
               </xsl:if>
               <xsl:if test="@relnegation = 'true' ">
                  <xsl:attribute name="negationInd">true</xsl:attribute>
               </xsl:if>
			   <xsl:attribute name="displayInd">true</xsl:attribute>                  
			  <!--<xsl:apply-templates select="@order" mode="sequence"/>-->
               <xsl:apply-templates select="to/*[@order]/@order" mode="sequence"/>
			   <xsl:choose>
	               <xsl:when test="./@lownum|./@highnum">
	                  <pauseQuantity xsi:type="URG_PQ">
	                     <xsl:if test="@lownum">
	                     	<xsl:variable name="unitval">
								<xsl:call-template name="unitvalue">
									<xsl:with-param name="uval" select="@lowunit"/>
								</xsl:call-template>
							</xsl:variable>
	                        <low value="{@lownum}" unit="{$unitval}" inclusive="{@lowinclusive}"/>
	                     </xsl:if>
	                     <xsl:if test="@highnum">
	                     	<xsl:variable name="unitval">
								<xsl:call-template name="unitvalue">
									<xsl:with-param name="uval" select="@highunit"/>
								</xsl:call-template>
							</xsl:variable>
	                        <high value="{@highnum}" unit="{$unitval}" inclusive="{@highinclusive}"/>
	                     </xsl:if>
	                  </pauseQuantity>
	               </xsl:when>
	               <xsl:when test="@widthnum and @widthunit">
					   <xsl:variable name="wunit">
							<xsl:call-template name="unitvalue">
								<xsl:with-param name="uval" select="@widthunit"/>
					     	</xsl:call-template>
					    </xsl:variable>
						<pauseQuantity xsi:type="URG_PQ">
							<width value="{@widthnum}" unit="{$wunit}"/>
						</pauseQuantity>
			      </xsl:when>
			   </xsl:choose>
               <xsl:apply-templates select=".." mode="resultvalue"/>
               <xsl:choose>
                  <xsl:when test="./@to">
                     <!-- <qdsel .. rel="..." to="xxxx"/> -->
                     <!--<xsl:apply-templates select="@toorder" mode="sequence"/>-->
                     <xsl:apply-templates select="/measure/elementlookup/*[@id=$currentto]"/>
                  </xsl:when>
                  <xsl:otherwise>
                     <xsl:choose>
                        <xsl:when test="to">
                           <xsl:choose>
                               <xsl:when test="count(to/*) > 1">
                                     <ERROR>
                                        <TYPE>incorrect syntax</TYPE>
                                        <SOURCE><xsl:copy-of select="to"/></SOURCE>               
                                        <DESCRIPTION>
                                           When using "to", it may only have 1 child: 
                                           qdsel, measureel, or and/or/not.
    
                                           If you have more than one qdsel or measureel, consider wrapping
                                           in "and" or "or," depending on the relativity requirements.
                                           </DESCRIPTION>
                                     </ERROR>
                               </xsl:when>
                               <xsl:when test="to/min or to/max or to/last or to/first or to/second or to/third or to/fourth or to/fifth">
									<xsl:apply-templates select="to/*" mode="subset"/>
									<xsl:apply-templates select="to/node()/*"/>
								</xsl:when>
                               <xsl:otherwise>
                                  <xsl:apply-templates select="to/*"/>
                               </xsl:otherwise>                                    
                           </xsl:choose>
                        </xsl:when>  
                     </xsl:choose>                     
                  </xsl:otherwise>
               </xsl:choose>
            </sourceOf>
         </xsl:when>
      </xsl:choose>
   </xsl:template>

   <xsl:template match="headers">
      <xsl:text>
         
      </xsl:text>
      <xsl:comment>
         **************************************************************   
         Measure Header Section
         **************************************************************
      </xsl:comment>
      <typeId root="2.16.840.1.113883.1.3" extension="POQM_HD000001"/>
      <id root="{normalize-space(uuid)}"/>
      <code code="57024-2" codeSystem="2.16.840.1.113883.6.1" displayName="Health Quality Measure Document"/>
      <title>
		<xsl:call-template name="trim">
			<xsl:with-param name="textString" select="title"/>
 		</xsl:call-template>
      </title>
      <text>
		<xsl:call-template name="trim">
			<xsl:with-param name="textString" select="description"/>
 		</xsl:call-template>
      </text>
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
      		<xsl:when test="string-length(normalize-space(setid))>0">
      			<xsl:value-of select="normalize-space(setid)"/>
      		</xsl:when>
      		<xsl:otherwise>
      			<xsl:text>pending</xsl:text>
      		</xsl:otherwise>
      	</xsl:choose>
      </xsl:variable>
      <setId root="{$set-id}"/>
      <versionNumber value="{normalize-space(version)}"/>

      <xsl:for-each select="authors/author">
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
      <custodian>
         <assignedPerson classCode="ASSIGNED">
            <representedOrganization classCode="ORG" determinerCode="INSTANCE">
               <id root="{custodian/@id}"/>
               <name>
					<xsl:call-template name="trim">
              			 <xsl:with-param name="textString" select="custodian"/>
            		</xsl:call-template>
               </name>
               <contactParty classCode="CON" nullFlavor="UNK"/>
            </representedOrganization>
         </assignedPerson>
      </custodian>

      <xsl:for-each select="verifiers/*">
         <verifier typeCode="VRF">
            <assignedPerson classCode="ASSIGNED">
               <representedOrganization classCode="ORG" determinerCode="INSTANCE">
               	  <xsl:if test="id/@root">
                  	<id root="{@id}"/>
                  </xsl:if>
                  <name>
                  		<xsl:choose>
							<xsl:when test="string-length(.)>0">
								<xsl:call-template name="trim">
			              			 <xsl:with-param name="textString" select="."/>
			            		</xsl:call-template>
		            		</xsl:when>
		            		<xsl:otherwise>None</xsl:otherwise>
	            		</xsl:choose>
                  </name>
                  <contactParty classCode="CON" nullFlavor="UNK"/>
               </representedOrganization>
            </assignedPerson>
         </verifier>
      </xsl:for-each>
      <xsl:if test="not(verifiers/*)">
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
      <xsl:if test="/measure/headers/finalizedDate/@value">
         <subjectOf>
            <measureAttribute>
               <code nullFlavor="OTH">
                  <originalText>Finalized Date/Time</originalText>            
               </code>
               <value xsi:type="TS" value="{normalize-space(/measure/headers/finalizedDate/@value)}"/>
            </measureAttribute>
         </subjectOf>
      </xsl:if>
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

      <xsl:for-each select="scores/score">
         <subjectOf>
            <measureAttribute>
               <code code="MSRSCORE" codeSystem="2.16.840.1.113883.5.4" displayName="Measure Scoring"/>
               <value xsi:type="CD" code="{@id}" codeSystem="2.16.840.1.113883.1.11.20367" displayName="{normalize-space(.)}"/>
            </measureAttribute>
         </subjectOf>
      </xsl:for-each>
      <xsl:for-each select="types/type">
         <subjectOf>
            <measureAttribute>
               <code code="MSRTYPE" codeSystem="2.16.840.1.113883.5.4" displayName="Measure Type"/>
               <value xsi:type="CD" code="{@id}" codeSystem="2.16.840.1.113883.1.11.20368" displayName="{normalize-space(.)}"/>
            </measureAttribute>
         </subjectOf>
      </xsl:for-each>
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
      
      <xsl:for-each select="rationales/rationale">
         <subjectOf>
            <measureAttribute>
               <code code="RAT" codeSystem="2.16.840.1.113883.5.4" displayName="Rationale"/>
               <value xsi:type="ED" mediaType="text/plain">
               		<xsl:call-template name="trim">
              			 <xsl:with-param name="textString" select="."/>
            		</xsl:call-template>	
               </value>
            </measureAttribute>
         </subjectOf>
      </xsl:for-each>
      <xsl:for-each select="recommendations/recommendation">
         <subjectOf>
            <measureAttribute>
               <code code="CRS" codeSystem="2.16.840.1.113883.3.560" displayName="Clinical Recommendation Statement"/>
               <value xsi:type="ED" mediaType="text/plain">
               		<xsl:call-template name="trim">
              			 <xsl:with-param name="textString" select="."/>
            		</xsl:call-template>
			   </value>
            </measureAttribute>
         </subjectOf>
      </xsl:for-each>
      <subjectOf>
         <measureAttribute>
            <code code="IDUR" codeSystem="2.16.840.1.113883.3.560" displayName="Improvement Notation"/>
            <value xsi:type="ED" mediaType="text/plain">
            	<xsl:choose>
	            	<xsl:when test="improvementNotation">
	            		<xsl:call-template name="trim">
              			 	<xsl:with-param name="textString" select="improvementNotation"/>
            			</xsl:call-template>
	            	</xsl:when>
            		<xsl:otherwise>
            			<xsl:attribute name="nullFlavor">NI</xsl:attribute>
            		</xsl:otherwise>
            	</xsl:choose>
            </value>
         </measureAttribute>
      </subjectOf>

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
      
      <xsl:for-each select="descriptions/description">
         <subjectOf>
            <measureAttribute>
               <code>
                  <xsl:if test="string-length(@code)>0">
                     <xsl:attribute name="code">
                        <xsl:value-of select="@code"/>
                     </xsl:attribute>
                  </xsl:if>
                  <xsl:if test="string-length(@codeSystem)>0">
                     <xsl:attribute name="codeSystem">
                        <xsl:value-of select="@codeSystem"/>
                     </xsl:attribute>
                  </xsl:if>
                  <xsl:if test="not(string-length(@code)>0)">
                     <xsl:attribute name="nullFlavor">
                        <xsl:text>OTH</xsl:text>
                     </xsl:attribute>
                  </xsl:if>
                  <originalText>
                     <xsl:value-of select="@name"/>
                  </originalText>
               </code>
               <value xsi:type="ED" mediaType="text/plain"><xsl:call-template name="trim">
                  <xsl:with-param name="textString" select="."/>
               </xsl:call-template></value>
            </measureAttribute>
         </subjectOf>
      </xsl:for-each>
      
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
			<xsl:when test="definitions/definition">
				<xsl:for-each select="definitions/definition">
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
      
      <!-- transmission format -->
      <xsl:call-template name="subjOfOrigText">
         <xsl:with-param name="origText"><xsl:text>Transmission Format</xsl:text></xsl:with-param>
         <xsl:with-param name="text">
            <xsl:value-of select="transmissionFormat/text()"/>
         </xsl:with-param>
      </xsl:call-template>   
      
      <!-- Initial Patient Population Description -->
      <xsl:call-template name="subjOfOrigText">
         <xsl:with-param name="origText">Initial Patient Population</xsl:with-param>
         <xsl:with-param name="text">
            <xsl:value-of select="initialPatientPopDescription/text()"/>
         </xsl:with-param>
         <xsl:with-param name="code">IPP</xsl:with-param>
         <xsl:with-param name="codeSystem">2.16.840.1.113883.5.1063</xsl:with-param>
      </xsl:call-template>  
      
      <!-- Denominator Description -->
      <xsl:call-template name="subjOfOrigText">
         <xsl:with-param name="origText"><xsl:text>Denominator</xsl:text></xsl:with-param>
         <xsl:with-param name="text">
            <xsl:value-of select="denominatorDescription/text()"/>
         </xsl:with-param>
         <xsl:with-param name="code">DENOM</xsl:with-param>
         <xsl:with-param name="codeSystem">2.16.840.1.113883.5.1063</xsl:with-param>
      </xsl:call-template>
      
      <!-- Denominator Exclusions Description -->
      <xsl:call-template name="subjOfOrigText">
         <xsl:with-param name="origText"><xsl:text>Denominator Exclusions</xsl:text></xsl:with-param>
         <xsl:with-param name="text">
            <xsl:value-of select="denominatorExclusionsDescription/text()"/>
         </xsl:with-param>
      </xsl:call-template>
      
      <!-- Numerator Description -->
      <xsl:call-template name="subjOfOrigText">
         <xsl:with-param name="origText"><xsl:text>Numerator</xsl:text></xsl:with-param>
         <xsl:with-param name="text">
            <xsl:value-of select="numeratorDescription/text()"/>
         </xsl:with-param>
         <xsl:with-param name="code">NUMER</xsl:with-param>
         <xsl:with-param name="codeSystem">2.16.840.1.113883.5.1063</xsl:with-param>
      </xsl:call-template>
 
      <!-- Numerator Exclusions Description -->
      <xsl:call-template name="subjOfOrigText">
         <xsl:with-param name="origText"><xsl:text>Numerator Exclusions</xsl:text></xsl:with-param>
         <xsl:with-param name="text">
            <xsl:value-of select="numeratorExclusionsDescription/text()"/>
         </xsl:with-param>
      </xsl:call-template>
      
      <!-- Denominator Exceptions Description -->
      <xsl:call-template name="subjOfOrigText">
         <xsl:with-param name="origText"><xsl:text>Denominator Exceptions</xsl:text></xsl:with-param>
         <xsl:with-param name="text">
            <xsl:value-of select="denominatorExceptionsDescription/text()"/>
         </xsl:with-param>
         <xsl:with-param name="code">DENEXCEP</xsl:with-param>
         <xsl:with-param name="codeSystem">2.16.840.1.113883.5.1063</xsl:with-param>
      </xsl:call-template>
      
      <!-- Measure Population Description -->
      <xsl:call-template name="subjOfOrigText">
         <xsl:with-param name="origText"><xsl:text>Measure Population</xsl:text></xsl:with-param>
         <xsl:with-param name="text">
            <xsl:value-of select="measurePopulationDescription/text()"/>
         </xsl:with-param>
         <xsl:with-param name="code">MSRPOPL</xsl:with-param>
         <xsl:with-param name="codeSystem">2.16.840.1.113883.5.1063</xsl:with-param>
      </xsl:call-template>
      
      <!-- Measure Observations Description -->
      <xsl:call-template name="subjOfOrigText">
         <xsl:with-param name="origText"><xsl:text>Measure Observations</xsl:text></xsl:with-param>
         <xsl:with-param name="text">
            <xsl:value-of select="measureObservationsDescription/text()"/>
         </xsl:with-param>
      </xsl:call-template>
 
      <!-- supplemental data elements -->
      <xsl:call-template name="subjOfOrigText">
         <xsl:with-param name="origText"><xsl:text>Supplemental Data Elements</xsl:text></xsl:with-param>
         <xsl:with-param name="text">
            <xsl:value-of select="supplementalData/text()"/>
         </xsl:with-param>
      </xsl:call-template>
      
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
	
   <xsl:template name="measure_specific_data_elements">
      
      <xsl:variable name="duration_value">
         <xsl:choose>
            <xsl:when test="string-length(/measure/headers/period/duration/text())">
               <xsl:call-template name="trim">
                  <xsl:with-param name="textString" select="/measure/headers/period/duration/text()"/>
               </xsl:call-template>
            </xsl:when>
         </xsl:choose>
      </xsl:variable>
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
      
		<subjectOf>
		    <measureAttribute>
		        <templateId root="2.16.840.1.113883.3.560.1.74"/>
		        <!-- measureAttribute.id = "variable" -->
		        <id root="{period/@uuid}"/>
		        <code code="MSRTP" displayName="Measurement period" codeSystem="2.16.840.1.113883.3.560"/>
		        <value xsi:type="IVL_TS">
		           <low value="{normalize-space(period/startdate/text())}"/>
		           <high value="{normalize-space(period/stopdate/text())}"/>
				</value>
		    </measureAttribute>
		 </subjectOf>
		<subjectOf>
			<localVariableName>measureStartDate</localVariableName>
		    <measureAttribute>
		        <templateId root="2.16.840.1.113883.3.560.1.75"/>
		        <!-- measureAttribute.id = "variable" -->
		        <id root="{period/startdate/@uuid}"/>
		        <code code="MSRSD" displayName="Measurement start date" codeSystem="2.16.840.1.113883.3.560"/>
		        <value xsi:type="TS" value="{normalize-space(period/startdate/text())}"/>
		     </measureAttribute>
		 </subjectOf>
		<subjectOf>
			<localVariableName>measureEndDate</localVariableName>
		    <measureAttribute>
		       <templateId root="2.16.840.1.113883.3.560.1.75"/>
		       <!-- measureAttribute.id = "variable" -->
		       <id root="{period/stopdate/@uuid}"/>
		       <code code="MSRED" displayName="Measurement end date" codeSystem="2.16.840.1.113883.3.560"/>
		       <value xsi:type="TS" value="{normalize-space(period/stopdate/text())}"/>
		     </measureAttribute>
		</subjectOf>
   </xsl:template>
   
   <xsl:template name="elTitle">
      <xsl:param name="datatype"/>
      <xsl:param name="name"/>
      <xsl:choose>
         <xsl:when test="@description"><xsl:value-of select="@description"/></xsl:when>
         <xsl:when test="description"><xsl:value-of select="description"/></xsl:when>
         <xsl:otherwise>
            <xsl:call-template name="capitalize">
               <xsl:with-param name="textString" select="$datatype"/>
            </xsl:call-template>: <xsl:value-of select="$name"/>
         </xsl:otherwise>
      </xsl:choose>
   </xsl:template>
   

   <xsl:variable name="smallcase" select="'abcdefghijklmnopqrstuvwxyz'" />
   <xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'" />

   <xsl:template name="capitalize">
      <xsl:param name="textString"/>

      <xsl:variable name="firstLetter">
         <xsl:value-of select="translate(substring($textString, 1, 1), $smallcase, $uppercase)"/>
      </xsl:variable>
      <xsl:variable name="restLetters">
         <xsl:value-of select="substring($textString, 2, (string-length($textString)-1) )"/>
      </xsl:variable>

      <xsl:value-of select="concat($firstLetter, $restLetters)"/>
   </xsl:template>
	<xsl:template name="trim" >
		<xsl:param name="textString"/>
		<xsl:value-of select="replace(replace($textString,'\s+$',''),'^\s+','')"/>
	</xsl:template>
</xsl:stylesheet>
