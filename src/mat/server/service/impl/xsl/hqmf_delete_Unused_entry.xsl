<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xmlns:func="http://exslt.org/functions"
 	extension-element-prefixes="func"
 	xmlns:qdm="urn:hhs-qdm:hqmf-r2-extensions:v1" xmlns:hl7="urn:hl7-org:v3" version="1.0">

<xsl:output method="xml" encoding="UTF-8" indent="no"/>

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
    <xsl:variable name="rav_count">
       <xsl:value-of select="count(../entry[@riskAdjVar='true']/*/id[@root=$qdm_root and @extension=$qdm_extension])"></xsl:value-of>
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

	<xsl:choose>
		<xsl:when test="($criteriaRef = 0) and ($valueValue = 0) and ($expressionValue = 0) and ($rav_count = 0)">
			<xsl:text disable-output-escaping="yes">&lt;!--</xsl:text>
			<xsl:copy>
				<xsl:apply-templates select="node()|@*">
					<xsl:with-param name="includeInternelComments">No</xsl:with-param>
				</xsl:apply-templates>
			</xsl:copy>
			<xsl:text disable-output-escaping="yes">--&gt;</xsl:text>
		</xsl:when>
		<xsl:otherwise>
			<xsl:copy>
				<xsl:apply-templates select="node()|@*">
					<xsl:with-param name="includeInternelComments">Yes</xsl:with-param>
				</xsl:apply-templates>
			</xsl:copy>
		</xsl:otherwise>
	</xsl:choose>

</xsl:template> 

<xsl:template match="@*|*|text()|comment()">
	<xsl:param name="includeInternelComments"/>
	<xsl:choose>
		<xsl:when test="$includeInternelComments = 'No'">
		<xsl:copy>
				<xsl:apply-templates select="@*|*|text()">
					<xsl:with-param name="includeInternelComments">No</xsl:with-param>
				</xsl:apply-templates>
			</xsl:copy>
			
		</xsl:when>
		<xsl:otherwise>
			<xsl:copy>
				<xsl:apply-templates select="@*|*|text()|comment()">
					<xsl:with-param name="includeInternelComments">Yes</xsl:with-param>
				</xsl:apply-templates>
			</xsl:copy>		
		</xsl:otherwise>
	</xsl:choose>
	
</xsl:template>
<xsl:template match="@riskAdjVar"/>

</xsl:stylesheet>
