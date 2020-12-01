<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:exsl="http://exslt.org/common"
   xmlns:uuid="java:java.util.UUID" xmlns:math="http://exslt.org/math" xmlns="urn:hl7-org:v3"
   xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:msxsl="urn:schemas-microsoft-com:xslt"
   extension-element-prefixes="exsl uuid math xs" 
   exclude-result-prefixes="exsl uuid math xs msxsl">
	<xsl:output method="xml" indent="yes" encoding="ISO-8859-1"/>
	
	<xsl:template name="derivation_expression">
		<xsl:param name="qdsuuid"/>
		
		<xsl:variable name="refid">
			<xsl:value-of select="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]/@refid"/>
		</xsl:variable>
		<xsl:variable name="refdatatype">
			<xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$refid]/@datatype"/>
		</xsl:variable>
		<xsl:variable name="refname">
			<xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$refid]/@name"/>
		</xsl:variable>
		<xsl:variable name="calcname">
			<xsl:value-of select="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]/@name"/>
		</xsl:variable>
		<!-- evalue = measurecalc/@value -->
		<xsl:variable name="pvalue">
			<xsl:value-of select="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]/@value"/>
		</xsl:variable>
		<xsl:variable name="comparison">
			<xsl:choose>
				<xsl:when test="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]/high">
					<xsl:apply-templates select="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]/high" mode="high_text"/>
				</xsl:when>
				<xsl:when test="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]/low">
					<xsl:apply-templates select="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]/low" mode="low_text"/>
				</xsl:when>
				<xsl:when test="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]/equal">
					<xsl:apply-templates select="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]/equal" mode="equal_text"/>
				</xsl:when>
				<xsl:when test="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid][@highnum]">
					<xsl:apply-templates select="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]" mode="inline_high_text"/>
				</xsl:when>
				<xsl:when test="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid][@lownum]">
					<xsl:apply-templates select="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]" mode="inline_low_text"/>
				</xsl:when>
				<xsl:when test="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid][@equalnum]">
					<xsl:apply-templates select="ancestor::measure//elementLookUp/*[@uuid=$qdsuuid]" mode="inline_equal_text"/>
				</xsl:when>
				<xsl:when test="ancestor::measure//elementLookUp/*[@id=$pvalue]">
					<xsl:text> </xsl:text>
					<xsl:choose>
						<xsl:when test="ancestor::measure//elementLookUp/*[@id=$pvalue][lower-case(@name)='is present' or lower-case(@name)='is not present']">
							<xsl:value-of select="ancestor::measure//elementLookUp/*[@id=$pvalue]/@name"/>
						</xsl:when>
						<xsl:when test="ancestor::measure//elementLookUp/*[@id=$pvalue][string-length(@oid)>0]">
							<xsl:text>= </xsl:text>
							<xsl:call-template name="elTitle">
					      		<xsl:with-param name="datatype" select="/measure/elementLookUp/*[@id=$pvalue]/@datatype"/>
					            <xsl:with-param name="name" select="/measure/elementLookUp/*[@id=$pvalue]/@name"/>
					         </xsl:call-template>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
				<!-- TODO remove 'specialized' handling: if no comparison and calcname is 'duration', then default to 'is present' -->
				<xsl:when test="lower-case($calcname)='duration'">
					<xsl:text> is present</xsl:text>
				</xsl:when>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="title">
			<xsl:choose>
				<xsl:when test="string-length($refid)>0">
					<xsl:call-template name="elTitle">
						<xsl:with-param name="datatype" select="$refdatatype"/>
						<xsl:with-param name="name" select="$refname"/>
					</xsl:call-template>
					<xsl:text> (</xsl:text>
					<xsl:value-of select="$calcname"/>
					<xsl:value-of select="$comparison"/>
					<xsl:text>)</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$calcname"/>
					<xsl:value-of select="$comparison"/>
					<xsl:text> of:</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
	    </xsl:variable>
	    
	    <!-- derivation expression handled here -->
		<xsl:variable name="id">
			<xsl:value-of select="current()/@id"/>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="name(current()) = 'qdsel' and /*/elementLookUp/measurecalc[@id=$id]">
				<xsl:apply-templates select="/*/elementLookUp/measurecalc[@id=$id]" mode="dc_measurecalc"/>
			</xsl:when>
			<xsl:otherwise>
				<observation classCode="OBS" moodCode="EVN" isCriterionInd="true">
					
					<id root="{$qdsuuid}"/>
					<title><xsl:value-of select="$title"/></title>
					<xsl:apply-templates select="attribute" mode="obs_val_new"/>
					<xsl:for-each select="attribute"> <xsl:apply-templates select="." mode="src_of_new"/> </xsl:for-each>
					
					<xsl:apply-templates select="." mode="to"/>
				</observation>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
</xsl:stylesheet>