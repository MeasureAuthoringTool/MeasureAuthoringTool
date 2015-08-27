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
		<xsl:value-of select="./*/id/@root"></xsl:value-of>
	</xsl:variable>
	<xsl:variable name="qdm_extension">
		<xsl:value-of select="./*/id/@extension"></xsl:value-of>
	</xsl:variable>
	<xsl:variable name="qdm_value">
       <xsl:value-of select="./localVariableName/@value"></xsl:value-of>
    </xsl:variable>
    <xsl:variable name="criteriaRef">
        <xsl:value-of select="count(//criteriaReference/id[@root = $qdm_root and @extension = $qdm_extension])"></xsl:value-of>
    </xsl:variable>
    <xsl:variable name="valueValue">
        <xsl:value-of select="count(//measureObservationDefinition//value[contains(@value, $qdm_value)])"></xsl:value-of>
    </xsl:variable>
    <xsl:variable name="expressionValue">
        <xsl:value-of select="count(//measureObservationDefinition//value/expression[contains(@value, $qdm_value)])"></xsl:value-of>
    </xsl:variable>
		<xsl:if test="($criteriaRef != 0) or ($valueValue != 0) or ($expressionValue != 0) or (@riskAdjVar)">
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
