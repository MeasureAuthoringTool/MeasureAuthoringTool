<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xmlns:func="http://exslt.org/functions"
 	extension-element-prefixes="func"
 	xmlns:qdm="urn:hhs-qdm:hqmf-r2-extensions:v1" xmlns:hl7="urn:hl7-org:v3" version="1.0">

<xsl:output method="xml" encoding="UTF-8" indent="no"/>
<xsl:strip-space  elements="*"/>
<xsl:template match="dataCriteriaSection//entry">
	
	<xsl:variable name="qdm_root">
		<xsl:value-of select="./*/id/@root"/>
	</xsl:variable>
	
	<xsl:variable name="qdm_extension">
		<xsl:value-of select="./*/id/@extension"/>
	</xsl:variable>
	
	<xsl:variable name="qdm_localVarName_value">
       <xsl:value-of select="./localVariableName/@value"/>
    </xsl:variable>
    
    <xsl:variable name="criteriaRef">
        <xsl:value-of select="count(//criteriaReference/id[@root = $qdm_root and @extension = $qdm_extension])"/>
    </xsl:variable>
    
    <xsl:variable name="msrObs_value">
        <xsl:value-of select="count(//measureObservationDefinition//value[contains(@value, $qdm_localVarName_value)])"/>
    </xsl:variable>
    
    <xsl:variable name="msrObs_expressionValue">
        <xsl:value-of select="count(//measureObservationDefinition//value/expression[contains(@value, $qdm_localVarName_value)])"/>
    </xsl:variable>
    
    <xsl:variable name="header_ItemCount">
    	<xsl:value-of select="count(//subjectOf/measureAttribute[code/@code='ITMCNT']/value[@root = $qdm_root and @extension = $qdm_extension])"/>
    </xsl:variable>
    
    <xsl:variable name="population_ItemCount">
   
    	<xsl:value-of select="count(//component/populationCriteriaSection/component/*/component/measureAttribute[code/@code='ITMCNT']
    								/value[@root = $qdm_root and @extension = $qdm_extension])"/>
    		
    </xsl:variable>
    
    <xsl:if test="($criteriaRef != 0) or ($msrObs_value != 0) or ($msrObs_expressionValue != 0) or 
    							($header_ItemCount != 0) or ($population_ItemCount != 0) or (@riskAdjVar)">
		<xsl:copy>
			<xsl:apply-templates select="node()|@*"/>	
		</xsl:copy>
		
	</xsl:if>

</xsl:template> 

<xsl:template match="@*|*|text()|comment()">
	<xsl:copy>
		<xsl:apply-templates select="@*|*|text()|comment()"/>
	</xsl:copy>			
</xsl:template>

<xsl:template match="@riskAdjVar"/>

</xsl:stylesheet>
