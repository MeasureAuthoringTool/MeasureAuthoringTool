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
                <xsl:call-template name="data_criteria"/>
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
        <xsl:variable name="displayName"><xsl:value-of select="@displayName"/></xsl:variable>
                
        <entry typeCode="DRIV">
            <observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
                <xsl:attribute name="actionNegationInd">false</xsl:attribute>                  
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
                <xsl:apply-templates select="*"/>
            </observation>
        </entry>
    </xsl:template>
    
    <!-- template which will handle clauses other than "initialPatientPopulation" clause-->
    <xsl:template match="group" mode="processOtherPopulations">
        <xsl:variable name="initPopUUID"><xsl:value-of select="clause[@type='initialPatientPopulation']/@uuid"/></xsl:variable>
        <xsl:variable name="initPopDisplayName"><xsl:value-of select="clause[@type='initialPatientPopulation']/@displayName"/></xsl:variable>
        <xsl:for-each select="clause">
            <xsl:choose>
                <xsl:when test=".[@type='initialPatientPopulation']">
                    <!-- do nothing -->
                </xsl:when>
                <xsl:when test=".[@type='denominator']">
                    <!-- Process 'denominator' clause with attachment to initialPatientPopulation -->
                    <xsl:call-template name="handleClause">
                        <xsl:with-param name="attachedUUID"><xsl:value-of select="$initPopUUID"/></xsl:with-param>
                        <xsl:with-param name="attachedTitle"><xsl:value-of select="$initPopDisplayName"/></xsl:with-param>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:call-template name="handleClause">
                        <xsl:with-param name="attachedUUID"><xsl:value-of select="following-sibling::clause[@type=('denominator','initialPatientPopulation')][1]/@uuid"/></xsl:with-param>
                        <xsl:with-param name="attachedTitle"><xsl:value-of select="following-sibling::clause[@type=('denominator','initialPatientPopulation')][1]/@displayName"/></xsl:with-param>
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
                <xsl:otherwise>MISSING_POPULATION_CODE</xsl:otherwise>
            </xsl:choose>         
        </xsl:variable>
        <xsl:variable name="displayName">
            <xsl:value-of select="@displayName"/>         
        </xsl:variable>
        <xsl:variable name="entry_uuid">
            <xsl:choose>
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
                <xsl:for-each select="*">
                    <xsl:call-template name="topCond">
                        <xsl:with-param name="attachedUUID"><xsl:value-of select="$attachedUUID"/></xsl:with-param>
                        <xsl:with-param name="attachedTitle"><xsl:value-of select="$attachedTitle"/></xsl:with-param>
                    </xsl:call-template>
                </xsl:for-each>    
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
                <xsl:apply-templates select="./*" mode="nested"/>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="logicalOp">
        <xsl:variable name="isNot"><xsl:apply-templates select="." mode="isChildOfNot"/></xsl:variable>
        <xsl:variable name="conj">
            <xsl:value-of select="upper-case(@type)"/>
        </xsl:variable>
       <xsl:text>
               
       </xsl:text>
       <xsl:comment>  top and/or </xsl:comment>
       <xsl:apply-templates select="*" mode="nested"/>
    </xsl:template>
    
    <xsl:template match="elementRef" mode="nested">
        <sourceOf typeCode="PRCN">
            <conjunctionCode>
                <xsl:attribute name="code">
                    <xsl:apply-templates select=".." mode="getConj"/>
                </xsl:attribute>   
            </conjunctionCode>
            <xsl:apply-templates select="."/>
        </sourceOf>
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
                <xsl:for-each select="elementLookUp/qdm">
                    <xsl:variable name="id">
                        <xsl:value-of select="@id"/>
                    </xsl:variable>
                    <xsl:variable name="addToDC">
                        <xsl:value-of select="@suppDataElement"/>
                    </xsl:variable>
                    
                    <xsl:if test="$addToDC != 'true'">
                        <xsl:apply-templates select="." mode="datacriteria"/>
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
    
    <xsl:template match="logicalOp" mode="getConj">
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
    
</xsl:stylesheet>