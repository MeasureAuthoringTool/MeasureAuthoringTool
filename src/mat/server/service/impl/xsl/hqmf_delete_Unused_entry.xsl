<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xmlns:func="http://exslt.org/functions"
 	extension-element-prefixes="func"
 	xmlns:qdm="urn:hhs-qdm:hqmf-r2-extensions:v1" xmlns:hl7="urn:hl7-org:v3" version="1.0">

<xsl:output method="xml" encoding="UTF-8" indent="no"/>

<xsl:template match="hl7:dataCriteriaSection//hl7:entry">
	<xsl:variable name="qdm_root">
		<xsl:value-of select="./*/hl7:id/@root"></xsl:value-of>
	</xsl:variable>
	<xsl:variable name="qdm_extension">
		<xsl:value-of select="./*/hl7:id/@extension"></xsl:value-of>
	</xsl:variable>

	<xsl:choose>
		<xsl:when test="not(count(//hl7:criteriaReference/hl7:id[@root = $qdm_root and @extension = $qdm_extension]) > 0)">
			<xsl:text disable-output-escaping="yes">&lt;!--</xsl:text>
			<xsl:copy>
				<xsl:apply-templates select="node()|@*"></xsl:apply-templates>
			</xsl:copy>
			<xsl:text disable-output-escaping="yes">--&gt;</xsl:text>
		</xsl:when>
		<xsl:otherwise>
			<xsl:copy>
				<xsl:apply-templates select="node()|@*"></xsl:apply-templates>
			</xsl:copy>
		</xsl:otherwise>
	</xsl:choose>

</xsl:template> 

<xsl:template match="@*|*|text()|comment()">
	<xsl:copy>
		<xsl:apply-templates select="@*|*|text()|comment()"/>
	</xsl:copy>
</xsl:template>

</xsl:stylesheet>
