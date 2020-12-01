<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
      xmlns:xs="http://www.w3.org/2001/XMLSchema"
      exclude-result-prefixes="xs"
      version="2.0">
	<xsl:output method="xml" indent="yes" encoding="ISO-8859-1"/>
    
	<xsl:template match="/">
        <xsl:apply-templates select="*"/>
    </xsl:template>
    
	<xsl:template match="*[name()='and' or name()='or'][count(*)=0][ancestor::and]"></xsl:template>
	
	<xsl:template match="*[name()=name(./*[1]) and count(*)=1 and not(@rel) and not(./*[1][@rel])]">
		<xsl:apply-templates select="./*"/>
	</xsl:template>

	<!-- ignore <qdsel/> and <qdsel>...</qdsel> -->
	<xsl:template match="qdsel[not(@*)]">
		<xsl:apply-templates select="./*"/>
	</xsl:template>
	
    <xsl:template match="node()|@*">
		<xsl:copy>
			<xsl:apply-templates select="node()|@*"/>
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
