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
        <xsl:include href="qds_datatype_patterns.xsl"/>
        <xsl:include href="measureDetails.xsl"/>
        
    <xsl:variable name="smallcase" select="'abcdefghijklmnopqrstuvwxyz'" />
    <xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'" />
        
        <xsl:template match="/">
            <xsl:apply-templates select="measure"/>
        </xsl:template>
    
        <xsl:template match="measure">
            <QualityMeasureDocument xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="urn:hl7-org:v3 ../xsd/schemas/EMeasure.xsd" moodCode="EVN" classCode="DOC">
                <xsl:apply-templates select="measureDetails"/>
                <xsl:apply-templates select="measureGrouping"/>
                <xsl:call-template name="measure_observations"/>
                <xsl:call-template name="data_criteria"/>
                 <xsl:call-template name="stratification"/>
                <xsl:call-template name="supplemental_data_elements"/>
            </QualityMeasureDocument>    
        </xsl:template>
        
        <xsl:template match="measureGrouping">
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
                    <xsl:apply-templates select="group"/>
                </section>
            </component>
        </xsl:template>

        <xsl:template match="group">
            <xsl:for-each select="clause">
                <xsl:choose>
                    <xsl:when test=".[@type='initialPatientPopulation']">
                        <!-- Process 'initialPatientPopulation' clause seperately  -->
                        <xsl:apply-templates select="." mode="initialPatientPopulation"/>
                    </xsl:when>
                </xsl:choose>
            </xsl:for-each> 
            <!-- Process all other population types ..pass 'group' node-->
            <xsl:apply-templates select="." mode="processOtherPopulations"/>
        </xsl:template>
    
    <!-- template which will handle "initialPatientPopulation" clause-->
    <xsl:template match="clause" mode="initialPatientPopulation">
        <xsl:text>
         
        </xsl:text>
        <xsl:comment>
            **************************************************************   
            Population Criteria Section: <xsl:value-of select="@type"/> 
            **************************************************************
      </xsl:comment>
        <xsl:variable name="code">IPP</xsl:variable>
        <xsl:variable name="entry_uuid">
            <xsl:choose>
                <xsl:when test="@uuid"><xsl:value-of select="@uuid"/></xsl:when>            
                <xsl:otherwise>MISSING_UUID</xsl:otherwise>
            </xsl:choose>         
        </xsl:variable>
        <xsl:variable name="displayName">Initial Patient Population</xsl:variable>
                
        <entry typeCode="DRIV">
            <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
                <xsl:attribute name="actionNegationInd">false</xsl:attribute>                  
                <id root="{$entry_uuid}"/>
                <code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
                <value xsi:type="CD" code="{$code}" codeSystem="2.16.840.1.113883.5.1063"
                    codeSystemName="HL7 Observation Value" displayName="{$displayName}"/>
                <xsl:apply-templates select="*" mode="topmost"/>
            </observation>
        </entry>
    </xsl:template>
    
    <xsl:template match="logicalOp" mode="topmost">
        <xsl:variable name="conj">
            <xsl:value-of select="upper-case(@type)"/>
        </xsl:variable>
        <xsl:variable name="isNot"><xsl:apply-templates select="." mode="isChildOfNot"/></xsl:variable>
        <xsl:if test="(count(child::*) > 0) and ($conj='AND' or $conj='OR')">
            <xsl:text>
           
            </xsl:text>
            
            <!-- Iterate over elements -->
            <xsl:for-each select="*">
                <xsl:variable name="elemName"><xsl:value-of select="name()"/></xsl:variable>
                
                <xsl:choose>
                    <xsl:when test="$elemName = 'elementRef'">
                        <xsl:apply-templates select="." mode="handleElementRef">
                            <xsl:with-param name="conj"><xsl:value-of select="$conj"/></xsl:with-param>
                        </xsl:apply-templates> 
                    </xsl:when>
                    <xsl:when test="$elemName = 'logicalOp' and (count(*)>0)">
                        <sourceOf typeCode="PRCN">
                            <conjunctionCode code="{$conj}"/>
                            <xsl:apply-templates select="." mode="handleFunctionalOps"/>
                            <act classCode="ACT" moodCode="EVN" isCriterionInd="true">
                                <xsl:if test="$isNot = 'true' "><xsl:attribute name="actionNegationInd">true</xsl:attribute></xsl:if>
                                   <xsl:apply-templates select="." mode="topmost"/>
                            </act>
                        </sourceOf>
                    </xsl:when>
                    <xsl:when test="$elemName = ('relationalOp','functionalOp')">
                        <xsl:apply-templates select=".">
                            <xsl:with-param name="conj"><xsl:value-of select="$conj"/></xsl:with-param>
                        </xsl:apply-templates>
                    </xsl:when>
                </xsl:choose>
            </xsl:for-each>    
            <!-- Done Iterating -->
                <!-- Handle QDM elements -->
            <!--<xsl:if test="count(child::*[name()='elementRef']) &gt; 0">
                <xsl:for-each select="elementRef">
                   <xsl:apply-templates select="." mode="handleElementRef">
                       <xsl:with-param name="conj"><xsl:value-of select="$conj"/></xsl:with-param>
                   </xsl:apply-templates>   
                </xsl:for-each>
            </xsl:if>-->
            
            <!-- Handle nested AND/OR -->
           <!-- <test><xsl:value-of select="name()"/></test>
            <parent><xsl:value-of select="name(current()/parent::node())"/></parent>-->
<!--             <xsl:variable name="countLogicalOp"><xsl:value-of select="count(child::*[name()='logicalOp'])"/></xsl:variable> -->
            <!--<countLogicalOp><xsl:value-of select="$countLogicalOp"/></countLogicalOp>-->
            <!--<xsl:if test="$countLogicalOp &gt; 0">
                <sourceOf typeCode="PRCN">
                    <conjunctionCode code="{$conj}"/>
                    <xsl:apply-templates select="." mode="handleFunctionalOps"/>
                    <act classCode="ACT" moodCode="EVN" isCriterionInd="true">
                        <xsl:if test="$isNot = 'true' "><xsl:attribute name="actionNegationInd">true</xsl:attribute></xsl:if>
                        <xsl:for-each select="logicalOp">
                           <xsl:apply-templates select="." mode="topmost"/>
                        </xsl:for-each>
                    </act>
                </sourceOf>
            </xsl:if>-->
            
            <!-- Iterate over all <functionalOp> children.-->
            <!--<xsl:for-each select="functionalOp">
                <xsl:apply-templates select=".">
                    <xsl:with-param name="conj"><xsl:value-of select="$conj"/></xsl:with-param>
                </xsl:apply-templates>
            </xsl:for-each>-->
            
            <!-- Iterate over all <relationalOp> children.-->
            <!--<xsl:for-each select="relationalOp">
                <xsl:apply-templates select=".">
                    <xsl:with-param name="conj"><xsl:value-of select="$conj"/></xsl:with-param>
                </xsl:apply-templates>
            </xsl:for-each>-->
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="*" mode="handleFunctionalOps">
        <xsl:if test="parent::functionalOp[@type != 'NOT']">
            <xsl:variable name="functionName">
                <xsl:value-of select="upper-case(parent::functionalOp/@type)"></xsl:value-of>
            </xsl:variable>
            
            <subsetCode>
                <xsl:attribute name="code">
                    <xsl:choose>
                        <xsl:when test="$functionName='MOST RECENT'">RECENT</xsl:when>
                        <xsl:otherwise><xsl:value-of select="$functionName"/></xsl:otherwise>
                    </xsl:choose>
                </xsl:attribute>
            </subsetCode>                
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="relationalOp">
        <xsl:param name="conj"/>
        <xsl:variable name="isNot"><xsl:apply-templates select="." mode="isChildOfNot"/></xsl:variable>
        <xsl:variable name="countelementRef"><xsl:value-of select="count(child::*[name()='elementRef'])"/></xsl:variable>
        
        <xsl:choose>
            <xsl:when test="$countelementRef = 2">
                <xsl:if test="string-length($conj)>0">
                    <sourceOf typeCode="PRCN">
                        <xsl:apply-templates select="." mode="handleFunctionalOps"/>
                        <conjunctionCode code="{$conj}"/>
                        <xsl:apply-templates select="."/>
                    </sourceOf>
                </xsl:if>
                <xsl:if test="string-length($conj)=0">
                    <xsl:apply-templates select="child::elementRef[1]"/>
                </xsl:if>
            </xsl:when>
            
            <xsl:when test="string-length($conj)=0">
                <xsl:apply-templates select="." mode="handleFunctionalOps"/>
                <!-- Process first child i.e. LHS -->
                <xsl:variable name="child1Name"><xsl:value-of select="name(child::*[1])"/></xsl:variable>
                <xsl:choose>
                    <xsl:when test="$child1Name='logicalOp' and (count(child::*[1]/*) > 0)">
                        <act classCode="ACT" moodCode="EVN" isCriterionInd="true">
                            <xsl:apply-templates select="child::*[1]" mode="topmost"/>
                            <!-- Process second child i.e. RHS -->
                            <xsl:apply-templates select="child::*[2]" mode="processRelational_Func_RHS">
                                <xsl:with-param name="showAtt">true</xsl:with-param>
                                <xsl:with-param name="conj"><xsl:value-of select="$conj"/></xsl:with-param>
                            </xsl:apply-templates>
                        </act>
                    </xsl:when>
                    <xsl:when test="$child1Name='elementRef'">
                        <xsl:apply-templates select="child::*[1]"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:apply-templates select="child::*[1]"/>
                        <!-- Process second child i.e. RHS -->
                        <xsl:apply-templates select="child::*[2]" mode="processRelational_Func_RHS">
                            <xsl:with-param name="showAtt">true</xsl:with-param>
                            <xsl:with-param name="conj"><xsl:value-of select="$conj"/></xsl:with-param>
                        </xsl:apply-templates>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <sourceOf typeCode="PRCN">
                    <conjunctionCode code="{$conj}"/>
                    <xsl:apply-templates select="." mode="handleFunctionalOps"/>
                    <!-- Process first child i.e. LHS -->
                    <xsl:variable name="child1Name"><xsl:value-of select="name(child::*[1])"/></xsl:variable>
                    <xsl:choose>
                        <xsl:when test="$child1Name='logicalOp' and (count(child::*[1]/*) > 0)">
                           <act classCode="ACT" moodCode="EVN" isCriterionInd="true">
                                <xsl:apply-templates select="child::*[1]" mode="topmost"/>
                                <!-- Process second child i.e. RHS -->
                                <xsl:apply-templates select="child::*[2]" mode="processRelational_Func_RHS">
                                    <xsl:with-param name="showAtt">true</xsl:with-param>
                                    <xsl:with-param name="conj"><xsl:value-of select="$conj"/></xsl:with-param>
                                </xsl:apply-templates>
                            </act>
                        </xsl:when>
                        <xsl:when test="$child1Name='elementRef'">
                            <xsl:apply-templates select="child::*[1]"/>
                        </xsl:when>
                        <xsl:otherwise>
                           <xsl:apply-templates select="child::*[1]"/>
                           <!-- Process second child i.e. RHS -->
                           <xsl:apply-templates select="child::*[2]" mode="processRelational_Func_RHS">
                               <xsl:with-param name="showAtt">true</xsl:with-param>
                               <xsl:with-param name="conj"><xsl:value-of select="$conj"/></xsl:with-param>
                           </xsl:apply-templates>
                        </xsl:otherwise>
                    </xsl:choose>
               </sourceOf>  
           </xsl:otherwise>
           
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="*" mode="processRelational_Func_RHS">
        <xsl:param name="showAtt"/>
        <xsl:param name="conj"/>
        
        <xsl:variable name="isNot"><xsl:apply-templates select="." mode="isChildOfNot"/></xsl:variable>
        <xsl:variable name="rel">
            <xsl:if test="parent::relationalOp">
                <xsl:choose>
                    <xsl:when test="parent::relationalOp/@type='SBOD'">EAS</xsl:when>
                    <xsl:when test="parent::relationalOp/@type='EBOD'">EAE</xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="parent::relationalOp/@type"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:if>
        </xsl:variable>
        
        <xsl:variable name="inversion">
            <xsl:if test="parent::relationalOp">
                <xsl:choose>
                    <xsl:when test="parent::relationalOp/@type='SBOD'">true</xsl:when>
                    <xsl:when test="parent::relationalOp/@type='EBOD'">true</xsl:when>
                    <xsl:otherwise>false</xsl:otherwise>
                </xsl:choose>
            </xsl:if>
        </xsl:variable>
        
        <!-- sourceOf here -->
        <sourceOf typeCode="{$rel}">
            <xsl:if test="$inversion = 'true'">
                <xsl:attribute name="inversionInd">true</xsl:attribute>                  
            </xsl:if>
            <xsl:attribute name="displayInd">true</xsl:attribute>
            <!--<xsl:apply-templates select=".." mode="handleFunctionalOps"/>-->
            
            <xsl:apply-templates select="parent::relationalOp" mode="pauseQuantity"/>
            <xsl:choose>
                <xsl:when test="name() = 'logicalOp' and (count(*) > 0)">
                     <act classCode="ACT" moodCode="EVN" isCriterionInd="true">
                         <xsl:if test="$isNot = 'true' and $showAtt"><xsl:attribute name="actionNegationInd">true</xsl:attribute></xsl:if>    
                             <xsl:apply-templates select="." mode="topmost"/>
                     </act>
                 </xsl:when>
                 <xsl:otherwise>
                     <xsl:apply-templates select=".">
                         <!--<xsl:with-param name="conj"><xsl:value-of select="$conj"/></xsl:with-param>-->
                     </xsl:apply-templates>
                 </xsl:otherwise>
             </xsl:choose>
        </sourceOf>
    </xsl:template>
    
    <xsl:template match="functionalOp">
        <xsl:param name="conj"/>
        <xsl:variable name="isNot"><xsl:apply-templates select="." mode="isChildOfNot"/></xsl:variable>
       <!-- <xsl:choose>
            <xsl:when test="@type='NOT'">-->
                <xsl:for-each select="*">
                    <xsl:choose>
                        <xsl:when test="name(.)='logicalOp' and (count(*) > 0)">
                            <xsl:if test="string-length($conj) > 0">
                                <sourceOf typeCode="PRCN">
                                    <conjunctionCode code="{$conj}"/>
                                    <xsl:apply-templates select="." mode="handleFunctionalOps"/>
                                    <act classCode="ACT" moodCode="EVN" isCriterionInd="true">
                                        <xsl:if test="$isNot = 'true' "><xsl:attribute name="actionNegationInd">true</xsl:attribute></xsl:if>
                                        <xsl:apply-templates select="." mode="topmost"/>
                                    </act>
                                </sourceOf>
                            </xsl:if>
                            <xsl:if test="string-length($conj) = 0">
                                <xsl:apply-templates select="." mode="topmost"/>
                            </xsl:if>
                        </xsl:when>
                        <xsl:when test="name(.)='elementRef'">
                            <xsl:apply-templates select="." mode="handleElementRef">
                                <xsl:with-param name="conj"><xsl:value-of select="$conj"/></xsl:with-param>
                            </xsl:apply-templates>
                        </xsl:when>
                        <xsl:when test="name(.)=('functionalOp','relationalOp')">
                            <xsl:apply-templates select=".">
                                <xsl:with-param name="conj"><xsl:value-of select="$conj"/></xsl:with-param>
                            </xsl:apply-templates>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:apply-templates select="."/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:for-each>
           <!-- </xsl:when>
        </xsl:choose>-->
    </xsl:template>
        
    <xsl:template match="elementRef" mode="handleElementRef">
        <xsl:param name="conj"/>
        <sourceOf typeCode="PRCN">
            <xsl:apply-templates select="." mode="handleFunctionalOps"/>
            <conjunctionCode code="{$conj}"/>
            <xsl:apply-templates select="."/>
        </sourceOf>
    </xsl:template>
    
    <!-- template which will handle clauses other than "initialPatientPopulation" clause-->
    <xsl:template match="group" mode="processOtherPopulations">
        <xsl:variable name="initPopUUID"><xsl:value-of select="clause[@type='initialPatientPopulation']/@uuid"/></xsl:variable>
        <xsl:variable name="initPopDisplayName">Initial Patient Population</xsl:variable>
        
        <xsl:variable name="denominatorPopUUID"><xsl:value-of select="clause[@type='denominator']/@uuid"/></xsl:variable>
        <xsl:variable name="denominatorPopDisplayName">Denominator</xsl:variable>
        
        <xsl:for-each select="clause">
            <xsl:choose>
                <xsl:when test=".[@type=('initialPatientPopulation','measureObservation')]">
                    <!-- do nothing -->
                </xsl:when>
                <xsl:when test=".[@type=('denominator','denominatorExclusions')]">
                    <!-- Process 'denominator' clause with attachment to initialPatientPopulation -->
                    <xsl:call-template name="handleClause">
                        <xsl:with-param name="attachedUUID"><xsl:value-of select="$initPopUUID"/></xsl:with-param>
                        <xsl:with-param name="attachedTitle"><xsl:value-of select="$initPopDisplayName"/></xsl:with-param>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                	<xsl:variable name="attach_uuid">
	                	<xsl:choose>
		                	<xsl:when test="string-length($denominatorPopUUID)=0"><xsl:value-of select="$initPopUUID"/></xsl:when>
		                	<xsl:otherwise><xsl:value-of select="$denominatorPopUUID"/></xsl:otherwise>
	                	</xsl:choose>
	                </xsl:variable>
	                <xsl:variable name="attach_title">
	                	<xsl:choose>
	                	    <xsl:when test="string-length($denominatorPopUUID)=0"><xsl:value-of select="$initPopDisplayName"/></xsl:when>
		                	<xsl:otherwise><xsl:value-of select="$denominatorPopDisplayName"/></xsl:otherwise>
	                	</xsl:choose>
	                </xsl:variable>	
                    <xsl:call-template name="handleClause">
                        <xsl:with-param name="attachedUUID"><xsl:value-of select="$attach_uuid"/></xsl:with-param>
                        <xsl:with-param name="attachedTitle"><xsl:value-of select="$attach_title"/></xsl:with-param>
                    </xsl:call-template> 
                </xsl:otherwise>
            </xsl:choose>    
        </xsl:for-each>
    </xsl:template>
    
    <xsl:template name="handleClause">
        <xsl:param name="attachedUUID"/>
        <xsl:param name="attachedTitle"/>
        <xsl:text>
         
      </xsl:text>
        <xsl:comment>
            **************************************************************   
            Population Criteria Section: <xsl:value-of select="@type"/> 
            **************************************************************
      </xsl:comment>
        <xsl:variable name="code">
            <xsl:choose>
                <xsl:when test="@type = 'initialPatientPopulation'">IPP</xsl:when>            
                <xsl:when test="@type = 'numerator'">NUMER</xsl:when>            
                <xsl:when test="@type = 'denominator'">DENOM</xsl:when>            
                <xsl:when test="@type = 'denominatorExclusions'">DENOM</xsl:when>            
                <xsl:when test="@type = 'denominatorExceptions'">DENEXCEP</xsl:when>
                <xsl:when test="@type = 'measurePopulation'">MSRPOPL</xsl:when>
                <xsl:when test="@type = 'numeratorExclusions'">NUMER</xsl:when>
                <xsl:when test="@type = 'measureObservation'">MSROBS</xsl:when>
                <xsl:otherwise>MISSING_POPULATION_CODE</xsl:otherwise>
            </xsl:choose>         
        </xsl:variable>
        <xsl:variable name="displayName">
            <xsl:choose>
                <xsl:when test="@type = 'initialPatientPopulation'">Initial Patient Population</xsl:when>            
                <xsl:when test="@type = 'numerator'">Numerator</xsl:when>            
                <xsl:when test="@type = 'denominator'">Denominator</xsl:when>            
                <xsl:when test="@type = 'denominatorExclusions'">Denominator</xsl:when>            
                <xsl:when test="@type = 'denominatorExceptions'">Denominator Exception</xsl:when>
                <xsl:when test="@type = 'measurePopulation'">Measure Population</xsl:when>
                <xsl:when test="@type = 'measureObservation'">Measure Observation</xsl:when>
                <xsl:when test="@type = 'numeratorExclusions'">Numerator Exclusions</xsl:when>
                <xsl:otherwise>MISSING_POPULATION_DISPLAYNAME</xsl:otherwise>
            </xsl:choose>         
        </xsl:variable>
        <xsl:variable name="entry_uuid">
            <xsl:choose>
                <xsl:when test="@type='numerator' or @type='denominatorExclusions' or @type='denominatorExceptions'">
                    <xsl:value-of select="uuid:randomUUID()"/>
                </xsl:when>
                <xsl:when test="@uuid"><xsl:value-of select="@uuid"/></xsl:when>            
                <xsl:otherwise>MISSING_UUID</xsl:otherwise>
            </xsl:choose>    
        </xsl:variable>
        <xsl:variable name="actionNegationInd">
            <xsl:choose>
                <xsl:when test="@type = 'denominatorExclusions'">true</xsl:when>
                <xsl:when test="@type = 'numeratorExclusions'">true</xsl:when>
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
                 <xsl:choose>
                    <xsl:when test="$code='MSROBS'">
                        <sourceOf typeCode="COMP">
                            <xsl:for-each select="*">
                                <xsl:call-template name="topCond">
                                    <xsl:with-param name="attachedUUID"><xsl:value-of select="$attachedUUID"/></xsl:with-param>
                                    <xsl:with-param name="attachedTitle"><xsl:value-of select="$attachedTitle"/></xsl:with-param>
                                </xsl:call-template>
                            </xsl:for-each>
                        </sourceOf>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:for-each select="*">
                            <xsl:call-template name="topCond">
                                <xsl:with-param name="attachedUUID"><xsl:value-of select="$attachedUUID"/></xsl:with-param>
                                <xsl:with-param name="attachedTitle"><xsl:value-of select="$attachedTitle"/></xsl:with-param>
                            </xsl:call-template>
                        </xsl:for-each>        
                    </xsl:otherwise>
                </xsl:choose>    
            </observation>
        </entry>        
    </xsl:template>
    
    <xsl:template name="topCond">
        <xsl:param name="attachedUUID"/>
        <xsl:param name="attachedTitle"/>
        
        <xsl:variable name="conj">
            <xsl:value-of select="upper-case(@type)"/>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="$conj='AND' or $conj='OR'">
                <xsl:text>
               
                </xsl:text>
                <xsl:comment>  top and/or </xsl:comment>
                <sourceOf typeCode="PRCN">
                    <conjunctionCode code="{$conj}"/>
                    <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
                        <id root="{$attachedUUID}" />
                        <title><xsl:value-of select="$attachedTitle"/></title>
                    </observation>
                </sourceOf>
                <xsl:if test="count(child::*) > 0">
                    <xsl:apply-templates select="." mode="topmost"/>
                </xsl:if>
           </xsl:when>
        </xsl:choose>
    </xsl:template>
         
    <xsl:template name="measure_observations">
        <xsl:if test="count(measureObservations) >= 1">
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
                    <xsl:for-each select="measureObservations/clause/logicalOp/*">
                        <entry typeCode="DRIV" derivationExprInd="true" showArgsInd="true">
                             <xsl:apply-templates select="."/>
                        </entry>
                    </xsl:for-each>
                </section>
            </component>
        </xsl:if>
    </xsl:template>
    
    <xsl:template name="stratification">
      <xsl:if test="count(strata) >= 1">
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
               <xsl:for-each select="strata/clause[logicalOp/*]">
                  <entry typeCode="DRIV" derivationExprInd="true" showArgsInd="true">
                     <observation classCode="OBS" moodCode="EVN" isCriterionInd="true" actionNegationInd="true">
                        <id root="{@uuid}"/>
                        <code nullFlavor="OTH">
                           <originalText>Stratum</originalText>
                        </code>
                        <!--<xsl:call-template name="criteria"/>-->
                        <xsl:apply-templates select="logicalOp" mode="topmost"/>
                     </observation>
                  </entry>
               </xsl:for-each>
            </section>
         </component>
      </xsl:if>
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

                <xsl:for-each select="elementLookUp/qdm[@datatype != 'attribute'][@datatype != 'Timing Element'][@suppDataElement != 'true']">
                    <!--<xsl:if test="@datatype != 'attribute' and @datatype != 'Timing Element'">-->
                        <xsl:variable name="this_oid"><xsl:value-of select="@oid"/></xsl:variable>
                        <xsl:variable name="this_datatype"><xsl:value-of select="@datatype"/></xsl:variable>
                        
                        <xsl:variable name="count_preceeding">
                            <xsl:value-of select="count(preceding-sibling::qdm[@suppDataElement != 'true']
                                                                              [@oid = $this_oid]
                                                                              [@datatype != ('attribute','Timing Element')]
                                                                              [@datatype=$this_datatype])"/>
                        </xsl:variable>
                                               
                        <xsl:if test="$count_preceeding = 0">
                            <xsl:apply-templates select="." mode="datacriteria"/>
                        </xsl:if>    
                </xsl:for-each>
                <!-- For each attribute with a mode of 'Value Set' we need to show the QDM used in the value set with the datatype as the name of the QDM
                attribute. Also If the same QDM element is used as Value Set in more than 1 QDM Attributes, then we need to show the <propel> element twice.-->
                <xsl:for-each select="elementLookUp/qdm[@datatype='attribute']">
                    <xsl:variable name="qdmID"><xsl:value-of select="@id"/></xsl:variable>
                    
                    <xsl:if test="count('ancestor::measure/measureGrouping//attribute[@qdmUUID = $qdmID]') > 0">
                        
                        <xsl:variable name="qdmName"><xsl:value-of select="@name"/></xsl:variable>
                        <xsl:variable name="qdmOid"><xsl:value-of select="@oid"/></xsl:variable>
                        <xsl:variable name="qdmUuid"><xsl:value-of select="@uuid"/></xsl:variable>
                        <xsl:variable name="qdmTaxonomy"><xsl:value-of select="@taxonomy"/></xsl:variable>
                        <xsl:variable name="qdmVersion"><xsl:value-of select="@version"/></xsl:variable>
                        <xsl:variable name="qdmCodeSystem"><xsl:value-of select="@codeSystem"/></xsl:variable>
                        <xsl:variable name="qdmCodeSystemName"><xsl:value-of select="@codeSystemName"/></xsl:variable>
                        
                        <xsl:for-each select="distinct-values(ancestor::measure/measureGrouping//attribute[@qdmUUID = $qdmID]/@name)">
                            <propel id="{$qdmID}"  name="{$qdmName}" displayName="{$qdmName}" datatype="{.}" oid="{$qdmOid}" uuid="{$qdmUuid}" taxonomy="{$qdmTaxonomy}" taxonomyVersion="{$qdmVersion}" codeSystem="{$qdmCodeSystem}" codeSystemName="{$qdmCodeSystemName}"/>
                        </xsl:for-each>    
                    </xsl:if>
                </xsl:for-each>
            </section>
        </component> 
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
                <xsl:for-each select="supplementalDataElements/elementRef">
                    <xsl:apply-templates select="." mode="datacriteria"/>
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
    
    <!--<xsl:template match="logicalOp" mode="getConj">
        <xsl:choose>
            <xsl:when test="@type = 'or' ">OR</xsl:when>         
            <xsl:when test="@type = 'and' ">AND</xsl:when>         
            <xsl:when test="@type='reference'">
                <xsl:apply-templates select=".." mode="getConj"/>
            </xsl:when>
            <xsl:when test="@type = 'or' ">OR</xsl:when>         
            <xsl:when test="@type = 'and' ">AND</xsl:when>
            <xsl:when test="@type = 'or' ">OR</xsl:when>         
            <xsl:when test="@type = 'and' ">AND</xsl:when>
            <xsl:otherwise>MISSING_CONJUNCTION</xsl:otherwise>
        </xsl:choose>
    </xsl:template>-->
    
    <xsl:template match="*" mode="pauseQuantity">
        <xsl:variable name="unitval">
            <xsl:choose>
                <xsl:when test="@unit">
                    <xsl:value-of select="@unit"/>
                </xsl:when>
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
                <xsl:apply-templates select="current()" mode="pq_comparison_new"/>
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
            <xsl:when test="$uval='copies/mL'">[copies]/mL</xsl:when>
            
            <xsl:otherwise><xsl:value-of select="$uval"/></xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
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
    
    <xsl:template match="*" mode="isChildOfNot">
        <xsl:choose>
            <xsl:when test="parent::functionalOp[@type='NOT']">true</xsl:when>
            <xsl:when test="parent::relationalOp[parent::functionalOp[@type='NOT']]">true</xsl:when>
            <xsl:otherwise>false</xsl:otherwise>
        </xsl:choose>
    </xsl:template>    
    
</xsl:stylesheet>